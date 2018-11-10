
/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 6: Controlled Process Synchronization	 	 *
* Due Date: 10/12/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program creates a shared memory block    *
* and a semaphore to be shared a parent and their forked 	 *
* child.  Each process will constantly swap the values, but	 *
* in order for our data to not be crushed by lack of mutual	 *
* exclusion, we use our semaphore to wait and signal when to *
* take their turn.  										 *
**************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/sem.h>
#include <signal.h>

#define SIZE 16

void sigExit(int sigNum);
int shmId, semId;
long int *shmPtr;

int main(int argc, const char* argv[] ) {
	int status;
	long int i, loop, temp;
	pid_t pid;
	struct sembuf addBuf, subBuf;

	signal(SIGINT, sigExit);

	//Get value of loop variable (from command-line argument).
	loop = atol(argv[1]);

	if ((shmId = shmget(IPC_PRIVATE, SIZE, IPC_CREAT|S_IRUSR|S_IWUSR)) < 0) {
		perror ("Can't allocate shared memory, for some reason.\n");
		exit (1);
	}

	if ((shmPtr = shmat(shmId, 0, 0)) == (void*) -1) {
		perror ("Can't attach, for some reason.\n");
		exit (1);
	}

	shmPtr[0] = 0;
	shmPtr[1] = 1;

	//Setup sembufs.
	addBuf.sem_num = 0;
	addBuf.sem_op = 1; //Add 1 to semaphore.
	addBuf.sem_flg = SEM_UNDO;

	subBuf.sem_num = 0;
	subBuf.sem_op = -1; //Subtract 1 from semaphore.
	subBuf.sem_flg = SEM_UNDO;

	//create semaphore
	semId = semget(IPC_PRIVATE, 1, 00600); //Key, number of semaphores, permissions.
	semctl(semId, 0, SETVAL, 1); //Semaphore ID, number of semaphores, command, union semaphores (this sets value to 1).

	if (!(pid = fork())) {
		for (i=0; i<loop; i++) {
			//Wait.
			semop(semId, &subBuf, 1);

			//Swap the contents of shmPtr[0] and shmPtr[1].
			temp = shmPtr[0];
			shmPtr[0] = shmPtr[1];
			shmPtr[1] = temp;

			//Signal.
			semop(semId, &addBuf, 1);
		}

		//Detach from shared memory.
		if(shmdt(shmPtr) < 0) {
			perror("Can't detach, for some reason.\n");
			exit(1);
		}
		exit(0);
	} else {
		for (i=0; i<loop; i++) {
			//Wait.
			semop(semId, &subBuf, 1);

			//Swap the contents of shmPtr[1] and shmPtr[0].
			temp = shmPtr[0];
			shmPtr[0] = shmPtr[1];
			shmPtr[1] = temp;

			//Signal.
			semop(semId, &addBuf, 1);
		}
	}

	wait(&status);
	semctl(semId, 0, IPC_RMID); //Remove semaphore.
	printf("Values: %li\t%li\n", shmPtr[0], shmPtr[1]);

	//Detach from shared memory.
	if (shmdt(shmPtr) < 0) {
		perror("Can't detach, for some reason.\n");
		exit(1);
	}

	//Deallocate shared memory.
	if (shmctl(shmId, IPC_RMID, 0) < 0) {
		perror("Can't deallocate shared memory, for some reason.\n");
		exit(1);
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
    	perror("Can't deallocate shared memory, for some reason.\n");
		exit(1);
	}
	semctl(semId, 0, IPC_RMID); //Remove semaphore.

    printf(" Shutting down, graciously.");
    exit(0);
}

