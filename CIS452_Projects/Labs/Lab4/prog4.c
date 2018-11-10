/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 4: Blocking Multi-threaded Server   	 	 *
* Due Date: 09/28/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program simulates what a multi-threaded  *
* file server would be like.  Upon user request, a thread	 *
* will be created where the child process goes to look		 *
* said file.  If it's in cache it'll simulate 1 second time, *
* but if it's not, then it'll take 7-10 seconds.  Since		 * 
* this is non-blocking, the user can continue to request	 *
* more files while the previous files are being attained.    *
**************************************************************/


#include <pthread.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
#include <signal.h>

using namespace std;

void* worker(void* arg); //"Retrieves" file in a thread.
void sigExit (int); //Custom exit.
int totRec = 0; //Keeps track of total requests received (global for sigExit).

int main() {
	signal (SIGINT, sigExit); //Custom termination signal.
	int TRUE = 1;
	srand(time(NULL)); //Seed randomness.

	while(TRUE){
    	int status;
    	pthread_t thread;
		char *input = new char[1024]; //Basically malloc, but C++.

		cout << "Please enter a filename\n";
		fgets(input, 1024, stdin);
		totRec++;
		if ((status = pthread_create (&thread, NULL,  worker, (void *) input)) != 0) {
			fprintf (stderr, "thread create error %d: %s\n", status, strerror(status));
			exit (1);
		}
	}
    
    return 0;
}

void* worker (void* arg) {
	// note the cast of the void pointer to the desired data type
    char *filename = (char *) arg;

	int prob = rand() % 5;
 
	if(prob){ //80%
		sleep(1);
	}else{ //20%
		int prob2 = (rand() % 4) + 7; //Wait 7-10 seconds.
		sleep(prob2);
	}

	cout << "Retrieved file: \t" << filename; //Print after "retrieving".
	delete[] filename; //Deallocating memory.

    return NULL;
} 

void sigExit(int sigNum) {
	cout << " Total number of requests received: " << totRec;
	fflush(stdout);
	exit(0);
}
