#include <sys/socket.h> 
#include <netpacket/packet.h> 
#include <net/ethernet.h>
#include <stdio.h>
#include <errno.h>
#include <sys/types.h>
#include <ifaddrs.h>
#include <netinet/if_ether.h>
#include <resolv.h>
#include <arpa/inet.h>
#include <linux/ip.h>

struct icmphdr {
	u_int8_t type;
	u_int8_t code;
	u_int16_t checksum;
	u_int16_t identifier;
	u_int16_t seqNum;
} __attribute__ ((__packed__));

struct icmperror {
	u_int8_t type; //empty
	u_int8_t code;
	u_int16_t checksum;
	u_int32_t unused; //empty
	u_int64_t data;
} __attribute__ ((__packed__));

//http://www.microhowto.info/howto/calculate_an_internet_protocol_checksum_in_c.html
uint16_t ip_checksum(void* vdata, size_t length) {
    // Cast the data pointer to one that can be indexed.
    char* data=(char*)vdata;

    // Initialise the accumulator.
    uint32_t acc=0xffff;

    // Handle complete 16-bit blocks.
	size_t i;
    for (i=0;i+1<length;i+=2) {
        uint16_t word;
        memcpy(&word,data+i,2);
        acc+=ntohs(word);
        if (acc>0xffff) {
            acc-=0xffff;
        }
    }

    // Handle any partial block at the end of the data.
    if (length&1) {
        uint16_t word=0;
        memcpy(&word,data+length-1,1);
        acc+=ntohs(word);
        if (acc>0xffff) {
            acc-=0xffff;
        }
    }

    // Return the checksum in network byte order.
    return htons(~acc);
}

//Page 95 of book.
u_short cksum(u_short *buf, int count) {
	printf("Made it?");
	register ulong sum = 0;
	while(count--) {
		sum += *buf++;
		if(sum & 0xFFFF0000){
			sum &= 0xFFFF;
			sum++;
		}
	}
	printf("Uh");
	return ~(sum & 0xFFFF);

}

int main(int argc, char* argv[]){
  int packet_socket;
  //get list of interfaces (actually addresses)
  struct ifaddrs *ifaddr, *tmp;
  struct ether_arp ea;
  struct sockaddr_ll *eth1_mac;
  struct sockaddr_in *eth1_ipstruct;
  struct sockaddr_in saTmp;
  struct sockaddr_in fwdTmp;
  unsigned long theForwardIP;
  int forwardToID;

  fd_set sockets;
  FD_ZERO(&sockets);
  
  char line[1000];
  char* interfaces[6];
  char* ips[6];
  char* startIps[6];
  char* nextHop[6];
  int doForwardArr[6];
  unsigned long numIps[6];
  int cutOff[6];
  int doForward = 0;

  FILE *r1;
  r1 = fopen(argv[1], "r");
  int linesRead = 0;
  int i = 0;
  while(fgets(line, sizeof(line), r1)){
	int part = 0;
	char temp[] = "";

	char ip[] = "";
	strncat(ip, &line[0], 8);
	printf("IP: %s \n", ip);

	inet_pton(AF_INET, ip, &(saTmp.sin_addr));
	numIps[i] = htonl(saTmp.sin_addr.s_addr);
	printf("IP numerical value: %lu \n", numIps[i]);

	char range[] = "";
	strncat(range, &line[9], 3);
	printf("Range: %s \n", range);

	char startIP[] = "";
	if(strcmp(range, "24") > 0){
		strncat(startIP, &line[0], 6);
		cutOff[i] = 24;
		doForward = 0;
	}else{
		strncat(startIP, &line[0], 4);
		cutOff[i] = 16;
		doForward = 1;
	}
	printf("Start of IP: %s \n", startIP);

	char nextHopIP[] = "";
	char interface[] = "";

	if(line[12] == '-'){
		printf("We won\'t forward \n");
		strncat(nextHopIP, &line[12], 1);
		strncat(interface, &line[14], 8);
	}else{
		printf("We will forward \n");
		strncat(nextHopIP, &line[12], 8);
		strncat(interface, &line[21], 8);
		inet_pton(AF_INET, nextHopIP, &(fwdTmp.sin_addr));
		theForwardIP = htonl(fwdTmp.sin_addr.s_addr);
	}
	


	printf("Ip numerical values: %lu \n", numIps[i]);

	printf("Next Hop: %s \n", nextHopIP);
	printf("Interface: %s \n", interface);
	
	doForwardArr[i] = doForward;
	interfaces[i] = interface;
	ips[i] = ip;
	startIps[i] = startIP;
	printf("STARTIP: %s \n", startIps[i]);
	nextHop[i] = nextHopIP;
	printf("IT'S ITERATTING.. %s \n", nextHop[i]);
    //printf("WHAT IS HERE: %s, %d\n", startIps[2], numIps[2]);
	i++;
	linesRead++;
  }

  struct sockaddr_ll* eth_macs[linesRead];
  struct sockaddr_in* eth_ips[linesRead];
	 

  if(getifaddrs(&ifaddr)==-1){
    perror("getifaddrs");
    return 1;
  }
  int j = 0;
  int allSockets[6];
  int u = 0;
  //have the list, loop over the list
  for(tmp = ifaddr; tmp!=NULL; tmp=tmp->ifa_next){
    //Check if this is a packet address, there will be one per
    //interface.  There are IPv4 and IPv6 as well, but we don't care
    //about those for the purpose of enumerating interfaces. We can
    //use the AF_INET addresses in this list for example to get a list
    //of our own IP addresses
    if(tmp->ifa_addr->sa_family==AF_PACKET){ // MAC ADDDRESS
      printf("Interface: %s\n",tmp->ifa_name);
      //create a packet socket on interface r?-eth1
      if(!strncmp(&(tmp->ifa_name[3]),"eth",3)){
		printf("Creating Socket on interface %s\n" ,tmp->ifa_name);
		eth_macs[u] = (struct sockaddr_ll*) tmp->ifa_addr;

		u++;

		//create a packet socket
		//AF_PACKET makes it a packet socket
		//SOCK_RAW makes it so we get the entire packet
		//could also use SOCK_DGRAM to cut off link layer header
		//ETH_P_ALL indicates we want all (upper layer) protocols
		//we could specify just a specific one
		
		packet_socket = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
		FD_SET(packet_socket,&sockets);
		allSockets[u-1] = packet_socket;
		if(packet_socket<0){
		  perror("socket");
		  return 2;
		}

		//Bind the socket to the address, so we only get packets
		//recieved on this specific interface. For packet sockets, the
		//address structure is a struct sockaddr_ll (see the man page
		//for "packet"), but of course bind takes a struct sockaddr.
		//Here, we can use the sockaddr we got from getifaddrs (which
		//we could convert to sockaddr_ll if we needed to)
		if(bind(packet_socket,tmp->ifa_addr,sizeof(struct sockaddr_ll))==-1){
		  perror("bind");
		}
		
      }
    }

	if(tmp->ifa_addr->sa_family==AF_INET){ // IP Address
		//eth1_ipstruct = (struct sockaddr_in*) tmp->sin_addr->s_addr;
		//eth1_ipstruct = (struct sockaddr_in*) tmp;
		//long val = tmp->in_addr->s_addr;
		//printf("NEW IP: %l", val);
		printf("Interface: %s\n",tmp->ifa_name);
		
		if(!strncmp(&(tmp->ifa_name[3]),"eth",3)){
		//	printf("Found our IP? \n");
		eth_ips[j] = (struct sockaddr_in*) tmp->ifa_addr;
			//eth1_ipstruct = (struct sockaddr_in*) tmp->ifa_addr;
		j++;
		
			//printf("RETURN: %s: %s \n", tmp->ifa_name, inet_ntoa(eth1_ipstruct->sin_addr));
		}
		
	}
  }
  //free the interface list when we don't need it anymore
  //freeifaddrs(ifaddr);

  //loop and recieve packets. We are only looking at one interface,
  //for the project you will probably want to look at more (to do so,
  //a good way is to have one socket per interface and use select to
  //see which ones have data)

  printf("Ready to recieve now\n");
  while(1){
    char buf[1500];
    struct sockaddr_ll recvaddr;
	struct ether_header eh;
	struct ether_arp ea;
	struct ether_arp eaOut;
	struct ether_header ehOut;
	struct arphdr ahOut;
	char arpBuf[42];
	char imcpBuf[98];
	struct iphdr iph;
	struct icmphdr icmph;
	
	

	fd_set tmp_set = sockets;
	select(FD_SETSIZE,&tmp_set,NULL,NULL,NULL); //Last param is timeout.
	int selectI;
	for(selectI=0; selectI<FD_SETSIZE; selectI++){
		if(FD_ISSET(selectI,&tmp_set)){ //Checks if data is available.
			int recvaddrlen=sizeof(struct sockaddr_ll);

			//we can use recv, since the addresses are in the packet, but we
			//use recvfrom because it gives us an easy way to determine if
			//this packet is incoming or outgoing (when using ETH_P_ALL, we
			//see packets in both directions. Only outgoing can be seen when
			//using a packet socket with some specific protocol)

			int n = recvfrom(selectI, buf, 1500,0,(struct sockaddr*)&recvaddr, &recvaddrlen);

			memcpy(&eh, &buf[0], 14);

			unsigned int ourIP;
			memcpy(&ourIP, &buf[38], 4);
			int q;
			int wasTriggered = 1;
			//printf("Start\n");
			for(q=0; q<(linesRead - 1); q++){ 
				//printf("Our IP Address: %d \n", eth_ips[q]->sin_addr.s_addr);
				//printf("Our IP Address from Buffer: %d \n", ourIP);

				//printf("Our IP Address2: %d \n", ntohl(eth_ips[q]->sin_addr.s_addr));
				//printf("Our IP Address from Buffer2: %d \n", ntohl(ourIP));

				//printf("EtherType: %d \n", ntohs(eh.ether_type));
				//printf("ourIP: %d \n", ntohs(ourIP));
				//printf("the other other IP: %d \n", eth_ips[q]->sin_addr.s_addr);
				if(ntohs(eh.ether_type) == 2054 && ntohl(ourIP) == ntohl(eth_ips[q]->sin_addr.s_addr)){ // 806 hex : 2054 decimal arp
					printf("Retrevied arp header\n");
					wasTriggered = 0;
					memcpy(&ea, &buf[14], sizeof(ea));
					
					//printf("OP: %d\n", htons(ea.ea_hdr.ar_op));
					if(htons(ea.ea_hdr.ar_op) == 1){
	  					//printf("Ready to recieve now0\n");
						//printf("dkjfkla: %u", eth_macs[0]->sll_addr);
						int i;
						for(i=0; i<6; i++){
							ehOut.ether_shost[i] = eth_macs[q]->sll_addr[i]; //Our MAC is src.
							ehOut.ether_dhost[i] = eh.ether_shost[i]; //Old src is dest.
						}
						
	  					//printf("Ready to recieve now1\n");

						ehOut.ether_type = htons(0x806); //ARP type.
						memcpy(arpBuf, &ehOut, sizeof(ehOut));

	  					//printf("Ready to recieve now2\n");

						for(i=0; i<4; i++){
							eaOut.arp_spa[i] = ea.arp_tpa[i]; //Our IP.
							eaOut.arp_tpa[i] = ea.arp_spa[i]; //Dst IP.
						}
	  					//printf("Ready to recieve now3\n");

						for(i=0; i<6; i++){
							eaOut.arp_sha[i] = eth_macs[q]->sll_addr[i]; //Our MAC addr.
							eaOut.arp_tha[i] = ea.arp_sha[i]; //Dst MAC addr.
						}
	  					//printf("Ready to recieve now4\n");
						printf("ARP REQUEST MAC: %02x:%02x:%02x:%02x:%02x:%u \n", ea.arp_sha[0], ea.arp_sha[1], ea.arp_sha[2], ea.arp_sha[3], ea.arp_sha[4], ea.arp_sha[5]);

						ahOut.ar_hrd = htons(1); //Hardware type.
						ahOut.ar_pro = htons(0x800); //Protocol is ethernet?
						ahOut.ar_hln = 6; //Length of MAC addr in bytes.
						ahOut.ar_pln = 4; //Length of IP addr in bytes.
						ahOut.ar_op = htons(2); //Reply.
						eaOut.ea_hdr = ahOut; //Attaching ARP hdr to our ARP packet.
	  					//printf("Ready to recieve now5\n");
		
						memcpy(arpBuf+sizeof(ehOut), &eaOut, sizeof(eaOut));
	  					//printf("Ready to recieve now6\n");

						if(recvaddr.sll_pkttype==PACKET_OUTGOING)
					  		continue;

						printf("Got a %d byte packet\n", n);

						if(send(selectI, arpBuf, sizeof(arpBuf), 0) < 0){
							printf("Error sending ARP reply packet\n");
						}else{
							printf("Sent ARP reply packet correctly.\n");
						}
						break;
					}
					//printf("Num: %d\n", htons(ea.ea_hdr.ar_op));
					unsigned char var = 2;
					if(htons(ea.ea_hdr.ar_op) == var){
						//printf("Retreived mac\n");
						printf("ARP REPLY MAC: %02x:%02x:%02x:%02x:%02x:%02x \n", ea.arp_sha[0], ea.arp_sha[1], ea.arp_sha[2], ea.arp_sha[3], ea.arp_sha[4], ea.arp_sha[5]);

					}
				}
				if(ntohs(eh.ether_type) == 2048){ // 800 hex : 2048 decimal ip
					printf("Retrevied an IP header %d \n", sizeof(iph));
					memcpy(&iph, &buf[14], sizeof(iph)); //Getting old IP header.
					if(iph.daddr == eth_ips[0]->sin_addr.s_addr || iph.daddr == eth_ips[1]->sin_addr.s_addr || iph.daddr == eth_ips[2]->sin_addr.s_addr) { //If the destination is us, then reply.  Might be nullpointer.
						//printf("Within 1\n");
						int i;
						for(i=0; i<6; i++){
							ehOut.ether_shost[i] = eth_macs[q]->sll_addr[i]; //Our MAC is src.
							ehOut.ether_dhost[i] = eh.ether_shost[i]; //Old src is the dst.
						}
						//printf("Within 2\n");
						ehOut.ether_type = htons(0x800); //ARP type.
						memcpy(imcpBuf, &ehOut, sizeof(ehOut)); //Copies ethernet header to packet.

						//printf("Within 3\n");
						int n;
						n = iph.saddr;
						iph.saddr = iph.daddr; //Source addr is now dest. addr.
						iph.daddr = n; //Dest. addr is now source addr.
						//printf("Within 4\n");
		
						memcpy(imcpBuf+sizeof(ehOut), &iph, sizeof(iph)); //Copies IP header to packet.
						memcpy(&icmph, &buf[14+sizeof(iph)], sizeof(icmph)); //Getting old IMCP header.

						//printf("Within 5\n");
						icmph.type = 0; //Change from request to reply.
						icmph.checksum = 0; //Make sure the checksum is 0, so it doesn't hurt new calc.
						icmph.checksum = htons(ip_checksum(&icmph, 16)); //Set new checksum.

						int dataOffset = sizeof(ehOut)+sizeof(iph)+sizeof(icmph);

						//printf("Within 6\n");
						memcpy(imcpBuf+dataOffset, buf+dataOffset, 98 - dataOffset);
						memcpy(imcpBuf+sizeof(ehOut)+sizeof(iph), &icmph, sizeof(icmph)); //Copies IMCP header to packet.

						//printf("Within 7\n");
						if(recvaddr.sll_pkttype==PACKET_OUTGOING)
							continue;

						printf("Got a %d byte packet\n", n);

						//what else to do is up to you, you can send packets with send,
						//just like we used for TCP sockets (or you can use sendto, but it
						//is not necessary, since the headers, including all addresses,
						//need to be in the buffer you are sending)

						if(send(selectI, imcpBuf, sizeof(imcpBuf), 0) < 0){
							printf("Error sending IMCP packet.\n");
						}else{
							printf("IMCP packet sent correctly.\n");
						}
						break;

					} else { //Else, look up the address to see if it's valid.
						//printf("Are we actually over here? \n");
						
						int m;
						struct ether_header freshEhOut;
						for(m=0; m<linesRead; m++){
							int compareValueOne;
							int compareValueTwo;
							unsigned long converted;
							char* str[INET_ADDRSTRLEN];
							saTmp.sin_addr.s_addr = iph.daddr;
							inet_ntop(AF_INET, &(saTmp.sin_addr), str, INET_ADDRSTRLEN);
							//printf("NEXT HOP IP: %s \n", str);
					
							inet_pton(AF_INET, str, &(saTmp.sin_addr));
							converted = htonl(saTmp.sin_addr.s_addr);
							//printf("LONG OF NEXT HOP IP: %lu \n", converted);

							//printf("Int value of sheet: %lu \nInt value of daddr: %u \n", numIps[m], converted);
							//printf("%d The IP we're looking at %s \n", m, startIps[m]);
							//printf("CUT OFF: %d \n", cutOff[m]);
							if(cutOff[m] == 16){
								compareValueOne = numIps[m] >> 16;
								compareValueTwo = converted >> 16;
							}else if(cutOff[m] == 24){
								compareValueOne = numIps[m] >> 8;
								compareValueTwo = converted >> 8;
							}
							if(compareValueOne == compareValueTwo ){ //|| compareValueThree == compareValueFour
								unsigned long theIP;
								int shouldConvert = 0;

								//printf("Bool: %d", doForwardArr[m]);
								//I tried doing it w/o 'forwardToID', however it spazzed out.
								if(doForwardArr[m] == 0){
									//will say r1-eth# or r2-eth# so just check 'r'
									theIP = iph.daddr;
									forwardToID = m;
								} else {
									//printf("Here\n\n\n\n");
									forwardToID = 0;
									theIP = htonl(theForwardIP); // saTmp.sin_addr.s_addr
									//printf("THE FORWARD IP2: %d \n", theIP);
									shouldConvert = 1;
								}
					
								saTmp.sin_addr.s_addr = theIP;	
								inet_ntop(AF_INET, &(saTmp.sin_addr), str, INET_ADDRSTRLEN);	
								printf("NEXT HOP: %s\n", str);

								struct ether_arp freshEaOut;
								struct arphdr freshAhOut;
								char freshArpBuf[42];

								//Construct ethernet header.
								int i;
								for(i=0; i<6; i++){
									freshEhOut.ether_shost[i] = eth_macs[forwardToID]->sll_addr[i]; //Our MAC is src.
									freshEhOut.ether_dhost[i] = 0xFF; //Broadcast.
								}
								freshEhOut.ether_type = htons(0x806); //ARP type.
								memcpy(freshArpBuf, &freshEhOut, sizeof(freshEhOut));

								//struct in_addr freshia = eth_ips[m].sin_addr;
								memcpy(freshEaOut.arp_spa, &(eth_ips[forwardToID]->sin_addr), sizeof(eth_ips[forwardToID])); //Our IP addr.
								memcpy(freshEaOut.arp_tpa, &theIP, sizeof(theIP)); //Dst IP addr.

								for(i=0; i<6; i++){
									freshEaOut.arp_sha[i] = eth_macs[forwardToID]->sll_addr[i]; //Our MAC addr.
									//printf("MAC: %u:", eth_macs[forwardToID]->sll_addr[i]);
									freshEaOut.arp_tha[i] = 0x00 ; //Dst MAC addr (unknown).
								}

								freshAhOut.ar_hrd = htons(1); //Hardware type.
								freshAhOut.ar_pro = htons(0x800); //Protocol is ethernet?
								freshAhOut.ar_hln = 6; //Length of MAC addr in bytes.
								freshAhOut.ar_pln = 4; //Length of IP addr in bytes.
								freshAhOut.ar_op = htons(1); //Request.
								freshEaOut.ea_hdr = freshAhOut; //Attaching ARP hdr to our ARP packet.

								memcpy(freshArpBuf+sizeof(freshEhOut), &freshEaOut, sizeof(freshEaOut));
				
								if(recvaddr.sll_pkttype==PACKET_OUTGOING)
							  		continue;

								printf("Got a %d byte packet\n", n);

								if(send(allSockets[forwardToID], freshArpBuf, sizeof(freshArpBuf), 0) < 0){
									printf("Error sending ARP request packet\n");
								}else{
									printf("Sent ARP request packet correctly.\n");
								}
							}
						}
						break;
					}
				}
			} //end of for loop
			if(wasTriggered == 1) {
				if(ntohs(eh.ether_type) == 2054){ // 806 hex : 2054 decimal arp
					printf("Retrevied arp header\n");
					wasTriggered = 0;
					memcpy(&ea, &buf[14], sizeof(ea));
				
					//printf("OP: %d\n", htons(ea.ea_hdr.ar_op));
					if(htons(ea.ea_hdr.ar_op) == 1){
	  					//printf("Ready to recieve now0\n");
						//printf("dkjfkla: %u", eth_macs[0]->sll_addr);
						int i;
						for(i=0; i<6; i++){
							ehOut.ether_shost[i] = eth_macs[0]->sll_addr[i]; //Our MAC is src.
							ehOut.ether_dhost[i] = eh.ether_shost[i]; //Old src is dest.
						}
					
	  					//printf("Ready to recieve now1\n");

						ehOut.ether_type = htons(0x806); //ARP type.
						memcpy(arpBuf, &ehOut, sizeof(ehOut));

	  					//printf("Ready to recieve now2\n");

						for(i=0; i<4; i++){
							eaOut.arp_spa[i] = ea.arp_tpa[i]; //Our IP.
							eaOut.arp_tpa[i] = ea.arp_spa[i]; //Dst IP.
						}
	  					//printf("Ready to recieve now3\n");

						for(i=0; i<6; i++){
							eaOut.arp_sha[i] = eth_macs[0]->sll_addr[i]; //Our MAC addr.
							eaOut.arp_tha[i] = ea.arp_sha[i]; //Dst MAC addr.
						}
	  					//printf("Ready to recieve now4\n");
						printf("ARP REQUEST MAC: %02x:%02x:%02x:%02x:%02x:%02x \n", ea.arp_sha[0], ea.arp_sha[1], ea.arp_sha[2], ea.arp_sha[3], ea.arp_sha[4], ea.arp_sha[5]);

						ahOut.ar_hrd = htons(1); //Hardware type.
						ahOut.ar_pro = htons(0x800); //Protocol is ethernet?
						ahOut.ar_hln = 6; //Length of MAC addr in bytes.
						ahOut.ar_pln = 4; //Length of IP addr in bytes.
						ahOut.ar_op = htons(2); //Reply.
						eaOut.ea_hdr = ahOut; //Attaching ARP hdr to our ARP packet.
	  					//printf("Ready to recieve now5\n");
	
						memcpy(arpBuf+sizeof(ehOut), &eaOut, sizeof(eaOut));
	  					//printf("Ready to recieve now6\n");

						if(recvaddr.sll_pkttype==PACKET_OUTGOING)
					  		continue;

						printf("Got a %d byte packet\n", n);

						if(send(selectI, arpBuf, sizeof(arpBuf), 0) < 0){
							printf("Error sending ARP reply packet\n");
						}else{
							printf("Sent ARP reply packet correctly.\n");
						}
						break;
					}
					//printf("Num: %d\n", htons(ea.ea_hdr.ar_op));
					unsigned char var = 2;
					if(htons(ea.ea_hdr.ar_op) == var){
						//printf("Retreived mac\n");
						printf("ARP REPLY MAC: %02x:%02x:%02x:%02x:%02x:%02x \n", ea.arp_sha[0], ea.arp_sha[1], ea.arp_sha[2], ea.arp_sha[3], ea.arp_sha[4], ea.arp_sha[5]);
					}
				}
			}

			//ignore outgoing packets (we can't disable some from being sent
			//by the OS automatically, for example ICMP port unreachable
			//messages, so we will just ignore them here)
		
			//close(i);
			///FD_CLR(selectI,&sockets);
		}
	}

  }
  //exit
  return 0;
}
