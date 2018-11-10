/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 5: Readers and Writers			  	 	 *
* Due Date: 10/05/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program joins an pre-existing shared 	 *
* memory block based on a key (using ftok).  It'll ask what  *
* reader you are (1 or 2), and read user-inputed strings     *
* from the writer.  It'll get notified whenever there's a    *
* new message to read, and should never miss a message, due  *
* to flags. 												 *
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
#define SELF 1
#define OTHER 0
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
	int readerNum;
	int turn; //4 bytes.
	int flags[5]; //20 bytes.
	char input[FOO -(sizeof(turn)+sizeof(flags))]; //Our input is as big as the shared memory minus flag space.

	printf("Please enter what number reader you are (1 or 2): " );
	fgets(input, FOO-(sizeof(turn)+sizeof(flags)), stdin);
	readerNum = atoi(input);
	memset(input, 0, sizeof(input)); //Reset array.

	key_t key = ftok(argv[1], 1);
    if(0 > key) {
       perror("Can't create key, for some reason.\n");
		exit(1);
    }

	if((shmId = shmget(key, FOO, S_IRUSR|S_IWUSR)) < 0) {
        perror("Can't locate shared memory, for some reason.\n");
        exit(1);
    }

	//Attaching to the shared memory.
	if((shmPtr = shmat(shmId, 0, 0)) == (void*)-1) {
   	   perror ("Can't attach for some reason.\n");
   	   exit (1);
   	}

	//Default start.
	turn = OTHER;
	flags[OTHER] = 1;
	flags[1] = flags[2] = flags[3] = flags[4] = OMESSAGE;

	while(1) {

		while(!flags[2+readerNum]) { //Keep checking if there's a new message.
			memcpy(flags, shmPtr + sizeof(turn), sizeof(flags));
		}

		flags[readerNum] = NMESSAGE; //I'd like to read a new message.
		memcpy(shmPtr + sizeof(turn), &flags, sizeof(flags)); //Put updated flags in shared memory.

		while(turn == OTHER && flags[OTHER]) { //If it's their turn and they want to write, then wait.
			memcpy(&turn, shmPtr, sizeof(turn)); //Check for an updated turn or flag.
			memcpy(&flags, shmPtr+sizeof(turn), sizeof(flags));
		}
		memcpy(input, shmPtr + sizeof(turn) + sizeof(flags), sizeof(input)); //Else read new input.

		flags[readerNum] = OMESSAGE; //I don't want to read anymore.
		flags[2+readerNum] = OMESSAGE; //No new message waiting.

		//We want to be careful here to update only specific flags, so readers don't overwrite each other.
		memcpy(shmPtr + sizeof(turn) + (readerNum*4), &flags + (readerNum*4), 4);
		memcpy(shmPtr + sizeof(turn) + ((readerNum+2)*4), &flags + (readerNum*4), 4);

		turn = OTHER; //It's their turn to go.
		memcpy(shmPtr, &turn, sizeof(turn)); //Put turn in shared memory.
		
		printf("%s\n", input); //Print output.
		memset(input, 0, sizeof(input)); //Reset array.

	}

	return 0;

}

void sigExit(int sigNum) {
	
	//Detach from shared memory.
	if(shmdt(shmPtr) < 0) {
      	perror("Can't detach, for some reason.\n");
      	exit(1);
   	}

	printf(" Shutting down, graciously.");
	exit(0);
}

