#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>

/***************************************************
* Authors: Jarod Hanko & Adam Stewart              *
* Assignment: Lab 3: Communicating Process         *
* Due Date: 09/21/2017                             *
*      											   *
* Descripton: A program that receives a random     *
* user-defined signal (sig1 or sig2) from their    *
* forked child, who waits a random amount of time. *
* Custom signal upon termination.    			   *
****************************************************/

void sigHandler1 (int);
void sigHandler2 (int);
void sigExit (int);
void sigExit2 (int);

int main() {
	pid_t pid;

	
	if ((pid = fork()) < 0){
		perror("fork failure");
		exit(1);
	}else if(pid == 0){
		signal (SIGINT, sigExit2);
		while(1){
			int wait = (rand() % 5) + 1;  //1-5 seconds.
			sleep(wait);	
			int oneortwo = rand() % 2; //Randomly pick sig1 or sig2.
			if(oneortwo){
				kill(getppid(), SIGUSR1);
			}else{
				kill(getppid(), SIGUSR2);
			}
		}
		exit(0);
	}else{
		printf("spawned child PID# %ld\n", (long)pid);
		while(1){
			signal (SIGUSR1, sigHandler1);
			signal (SIGUSR2, sigHandler2);
			signal (SIGINT, sigExit);
			printf ("waiting...\t"); //Wait for any signal.
			fflush(stdout);
			pause();
		}
	}
    return 0;
}


void sigHandler1 (int sigNum) {
    printf ("received a SIGUSR1 signal\n");
	fflush(stdout);
}

void sigHandler2 (int sigNum) {
    printf ("received a SIGUSR2 signal\n");
	fflush(stdout);
} 

void sigExit(int sigNum){
	printf(" received.  That's it, I'm shutting you down...\n");
	fflush(stdout);
	exit(0); //Exit for the parent.
}

void sigExit2(int sigNum){
	exit(0); //Exit for the child.
}
