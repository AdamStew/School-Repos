#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv){
	int sockfd = socket(AF_INET,SOCK_DGRAM,0);

	struct timeval timeout;
	timeout.tv_sec=5; //seconds
	timeout.tv_usec=0; //microseconds

	struct sockaddr_in serveraddr,clientaddr;
	serveraddr.sin_family=AF_INET;
	serveraddr.sin_port=htons(2482);
 	serveraddr.sin_addr.s_addr=INADDR_ANY;

	bind(sockfd,(struct sockaddr*)&serveraddr,sizeof(serveraddr));

	setsockopt(sockfd,SOL_SOCKET,SO_RCVTIMEO,&timeout,sizeof(timeout));

	while(1){
		int len=sizeof(clientaddr);
		char line[5000];
		int n = recvfrom(sockfd,line,5000,0,(struct sockaddr*)&clientaddr,&len);
		if(n==-1){
			printf("Nobody sent anything in time. ");
			fflush(stdout); //Helps print more consistently.
		}else{
			printf("Got from client: %s\n",line);
		}
	}
}
