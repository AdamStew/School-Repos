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

//http://www.microhowto.info/howto/calculate_an_internet_protocol_checksum_in_c.html

uint16_t ip_checksum(void* vdata,size_t length) {
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

int main(int argc, char* argv[]){
  int packet_socket;
  //get list of interfaces (actually addresses)
  struct ifaddrs *ifaddr, *tmp;
  struct ether_arp ea;
  struct sockaddr_ll *eth1_mac;
  struct sockaddr_in *eth1_ipstruct;
  
  /*char line[1000];
  char* interfaces[5];
  char* ips[5];

  FILE *r1; // , *r2
  r1 = fopen(argv[1], "r");

  while(fgets(line, sizeof(line), r1)){
	int i = 0;
	int part = 0;
	char temp[] = "";

	char ip[] = "";
	strncat(ip, &line[0], 8);
	printf("IP: %s \n", ip);

	char range[] = "";
	strncat(range, &line[9], 3);
	printf("Range: %s \n", range);

	char startIP[] = "";
	if(strcmp(range, "24") > 0){
		strncat(startIP, &line[0], 6);
	}else{
		strncat(startIP, &line[0], 4);
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
	}

	printf("Next Hop: %s \n", nextHopIP);
	printf("Interface: %s \n", interface);

	interfaces[i] = interface;
	ips[i] = startIP;
	i++;
  }
	 */

  if(getifaddrs(&ifaddr)==-1){
    perror("getifaddrs");
    return 1;
  }
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
      if(!strncmp(&(tmp->ifa_name[3]),"eth1",4)){
		printf("Creating Socket on interface %s\n",tmp->ifa_name);

		eth1_mac = (struct sockaddr_ll*) tmp->ifa_addr; //WORKS!@!!!

		//create a packet socket
		//AF_PACKET makes it a packet socket
		//SOCK_RAW makes it so we get the entire packet
		//could also use SOCK_DGRAM to cut off link layer header
		//ETH_P_ALL indicates we want all (upper layer) protocols
		//we could specify just a specific one
		packet_socket = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ALL));
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
		

		if(!strncmp(&(tmp->ifa_name[3]),"eth1",4)){
			printf("Found our IP? \n");
			eth1_ipstruct = (struct sockaddr_in*) tmp->ifa_addr;
		
			//printf("RETURN: %s: %s \n", tmp->ifa_name, inet_ntoa(eth1_ipstruct->sin_addr));
		}
	}
  }
  //free the interface list when we don't need it anymore
  freeifaddrs(ifaddr);

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
	
    int recvaddrlen=sizeof(struct sockaddr_ll);

    //we can use recv, since the addresses are in the packet, but we
    //use recvfrom because it gives us an easy way to determine if
    //this packet is incoming or outgoing (when using ETH_P_ALL, we
    //see packets in both directions. Only outgoing can be seen when
    //using a packet socket with some specific protocol)

    int n = recvfrom(packet_socket, buf, 1500,0,(struct sockaddr*)&recvaddr, &recvaddrlen);

	memcpy(&eh, &buf[0], 14);

	unsigned int ourIP;
	memcpy(&ourIP, &buf[38], 4);

	if(ntohs(eh.ether_type) == 2054 && ourIP == eth1_ipstruct->sin_addr.s_addr){ // 806 hex : 2054 decimal arp
		printf("Retrevied arp header\n");

		memcpy(&ea, &buf[14], sizeof(ea));

		int i;
		for(i=0; i<6; i++){
			ehOut.ether_shost[i] = eth1_mac->sll_addr[i]; //Our MAC is src.
			ehOut.ether_dhost[i] = eh.ether_shost[i]; //Old src is dest.
		}
		ehOut.ether_type = htons(0x806); //ARP type.
		memcpy(arpBuf, &ehOut, sizeof(ehOut));

		for(i=0; i<4; i++){
			eaOut.arp_spa[i] = ea.arp_tpa[i]; //Our IP.
			eaOut.arp_tpa[i] = ea.arp_spa[i]; //Dst IP.
		}

		for(i=0; i<6; i++){
			eaOut.arp_sha[i] = eth1_mac->sll_addr[i]; //Our MAC addr.
			eaOut.arp_tha[i] = ea.arp_sha[i]; //Dst MAC addr.
		}

		ahOut.ar_hrd = htons(1); //Hardware type.
		ahOut.ar_pro = htons(0x800); //Protocol is ethernet?
		ahOut.ar_hln = 6; //Length of MAC addr in bytes.
		ahOut.ar_pln = 4; //Length of IP addr in bytes.
		ahOut.ar_op = htons(2); //Reply.
		eaOut.ea_hdr = ahOut; //Attaching ARP hdr to our ARP packet.
		
		memcpy(arpBuf+sizeof(ehOut), &eaOut, sizeof(eaOut));

		if(recvaddr.sll_pkttype==PACKET_OUTGOING)
      		continue;

		printf("Got a %d byte packet\n", n);

		if(send(packet_socket, arpBuf, sizeof(arpBuf), 0) < 0){
			printf("Error sending ARP packet\n");
		}else{
			printf("Sent ARP packet correctly.\n");
		}
	}else if(ntohs(eh.ether_type) == 2048){ // 800 hex : 2048 decimal ip
		printf("Retrevied an IP header \n");

		int i;

		for(i=0; i<6; i++){
			ehOut.ether_shost[i] = eth1_mac->sll_addr[i]; //Our MAC is src.
			ehOut.ether_dhost[i] = eh.ether_shost[i]; //Old src is the dst.
		}

		ehOut.ether_type = htons(0x800); //ARP type.
		memcpy(imcpBuf, &ehOut, sizeof(ehOut)); //Copies ethernet header to packet.

		memcpy(&iph, &buf[14], sizeof(iph)); //Getting old IP header.

		int n;
		n = iph.saddr;
		iph.saddr = iph.daddr; //Source addr is now dest. addr.
		iph.daddr = n; //Dest. addr is now source addr.
		
		memcpy(imcpBuf+sizeof(ehOut), &iph, sizeof(iph)); //Copies IP header to packet.
		memcpy(&icmph, &buf[14+sizeof(iph)], sizeof(icmph)); //Getting old IMCP header.

		icmph.type = 0; //Change from request to reply.
		icmph.checksum = 0; //Make sure the checksum is 0, so it doesn't hurt new calc.
		icmph.checksum = htons(ip_checksum(&icmph, 16)); //Set new checksum (doesn't work).

		int dataOffset = sizeof(ehOut)+sizeof(iph)+sizeof(icmph);

		memcpy(imcpBuf+dataOffset, buf+dataOffset, 98 - dataOffset);
		memcpy(imcpBuf+sizeof(ehOut)+sizeof(iph), &icmph, sizeof(icmph)); //Copies IMCP header to packet.

	   if(recvaddr.sll_pkttype==PACKET_OUTGOING)
		  continue;

		printf("Got a %d byte packet\n", n);
		
		//what else to do is up to you, you can send packets with send,
		//just like we used for TCP sockets (or you can use sendto, but it
		//is not necessary, since the headers, including all addresses,
		//need to be in the buffer you are sending)

		if(send(packet_socket, imcpBuf, sizeof(imcpBuf), 0) < 0){
			printf("Error sending IMCP packet.\n");
		}else{
			printf("IMCP packet sent correctly.\n");
		}
	}
	
    //ignore outgoing packets (we can't disable some from being sent
    //by the OS automatically, for example ICMP port unreachable
    //messages, so we will just ignore them here)

  }
  //exit
  return 0;
}
