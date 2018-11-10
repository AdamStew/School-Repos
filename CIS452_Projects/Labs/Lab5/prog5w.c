/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 5: Readers and Writers			  	 	 *
* Due Date: 10/05/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program creates a shared memory block    *
* based on a file given (for ftok).  It then reads user		 *
* input and whenever available to attach a new message, it   *
* does so.  If needed, it'll wait to attach the message to   *
* shared memory for the readers who want to display the 	 *
* message.  												 *
**************************************************************/

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <unistd.h>

#define FOO 4096
#define SELF 0
#define OTHER 1
#define NMESSAGE 1
#define OMESSAGE 0

void sigExit(int);
int shmId; //ID of shared memory.
char *shmPtr; //Pointer to the beggining of shared memory.

/************************************************************************
* KEY:																	*
* flags[0] -> 0 means the writer doesn't want to write. 				*
*		   -> 1 means the writer does want to write.					*
* flags[1] -> 0 means readerOne doesn't want to read.					*
* 		   -> 1 means readerOne does want to read.						*
* flags[2] -> 0 means readerTwo doesn't want to read.					*
*  		   -> 1 means readerTwo does want to read.						*
* flags[3] -> 0 means there's nothing readable waiting for readerOne.	*
*		   -> 1 means there's something readable waiting for readerOne.	*
* flags[4] -> 0 means there's nothing readable waiting for readerTwo.	*
*		   -> 1 means there's something readable waiting for readerTwo.	*
************************************************************************/

int main(int argc, char* argv[]) {
	
	signal(SIGINT, sigExit);

	//Create turn and flags.
	int turn; //4 bytes.
	int flags[5]; //20 bytes.
	char input[FOO -(sizeof(turn)+sizeof(flags))]; //Our input is as big as the shared memory minus flag space.

	key_t key = ftok(argv[1], 1);
    if( 0 > key ) {
       perror("Can't create key, for some reason.\n");
		exit(1);
    }

	//Allocating memory for shared memory.
	if((shmId = shmget(key, FOO, IPC_CREAT|S_IRUSR|S_IWUSR)) < 0) {
  	    perror("Can't allocate shared memory, for some reason.\n");
   	   	exit(1);
   	}

	//Attaching to the shared memory.
	if((shmPtr = shmat(shmId, 0, 0)) == (void*)-1) {
   	   perror("Can't attach for some reason.\n");
   	   exit(1);
   	}

	//Default start.
	flags[SELF] = flags[1] = flags[2] = flags[3] = flags[4] = OMESSAGE;
	memcpy(shmPtr + sizeof(turn), &flags, sizeof(flags)); //Put flags in shared memory.
	turn = SELF;
	memcpy(shmPtr, &turn, sizeof(turn)); //Put turn in shared memory.

	while(1) {

		printf("Input a message: "); //Take an input.
		fgets(input, FOO-(sizeof(turn)+sizeof(flags)), stdin);

		flags[SELF] = NMESSAGE; //I'd like to send a new message.
		memcpy(shmPtr + sizeof(turn), &flags, sizeof(flags)); //Put flags in shared memory.

		while(turn == OTHER && (flags[1] || flags[2])) { //If it's their turn and ONE of them wants to read, then wait.
			memcpy(&turn, shmPtr, sizeof(turn)); //Check for an updated turn or flag.
			memcpy(&flags, shmPtr+sizeof(turn), sizeof(flags));
		}
		memcpy(shmPtr + sizeof(turn) + sizeof(flags), input, sizeof(input)); //Else write input to shared memory.

		flags[SELF] = OMESSAGE; //I don't want to send a message anymore.
		flags[3] = NMESSAGE; //Message waiting for user 1.
		flags[4] = NMESSAGE; //Message waiting for user 2.
		memcpy(shmPtr + sizeof(turn), &flags, sizeof(flags)); //Put flags in shared memory.

		turn = OTHER; //It's their turn to go.
		memcpy(shmPtr, &turn, sizeof(turn)); //Put turn in shared memory.

		memset(input, 0, sizeof(input)); //Reset array.

	}

	return 0;

}

void sigExit(int sigNum) {
	
	//Detach from shared memory.
	if(shmdt(shmPtr) < 0) {
      	perror("Can't detach, for some reason.\n");
      	exit (1);
   	}

	//Deallocate shared memory.
   	if(shmctl(shmId, IPC_RMID, 0) < 0) {
    	perror("Can't deallocate, for some reason.\n");
      	exit(1);
   	}

	printf(" Shutting down, graciously.");
	exit(0);
}
