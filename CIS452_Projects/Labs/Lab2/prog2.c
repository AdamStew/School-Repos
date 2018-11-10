#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <math.h>

int main()
{ 
	char response[1024];
	char *allToks[1024];
	char *token;
	pid_t pid;
	int status;

	while(1) {

		printf("<user>:");
		fgets(response, 1024, stdin);
		response[strcspn(response, "\n")] = '\0'; //Gets rid of \n char.

		if(strcmp(response, "quit") == 0) {
			exit(0);
		}

		token = strtok(response, " ");
		allToks[0] = token;
		int i = 0;
		long int prevTotal = 0;
		long int switchCount = 0;
		struct timeval prevTime, timePassed;
		while(token != NULL) {
			allToks[i++] = token;
			token = strtok(NULL, " ");
		}

		if ((pid = fork()) < 0) { //No child created.
    	    perror("Forking failed.");
    	    exit(1);
    	}
    	else if (pid == 0) { //Child just created arrives here.
			if (execvp(allToks[0], allToks) < 0) {
    	    	perror("Execution failed.");
    	    	exit(1);
   			}
    	}
		else { //Child created.
			struct rusage usage;

			wait(&status); //Wait for child to terminate.
			getrusage(RUSAGE_CHILDREN, &usage); //Gets usage of all children that have been terminated.

			switchCount = usage.ru_nvcsw - prevTotal; //Gets the context switches (total - prev total = current).
			if(switchCount >= 0) { //If it's our first round, there's no previous total meaning 0 - Number = negative.
				timersub(&usage.ru_utime, &prevTime, &timePassed); //Time spent executing instructions.  (To get time spent in OS code, use ru_stime.)
			} else {
				switchCount = usage.ru_nvcsw;
				timePassed = usage.ru_utime;
			}
			

			printf("Time Passed: %ld seconds and %ld microseconds\n", timePassed.tv_sec, timePassed.tv_usec);
			printf("Context Switches: %ld\n", switchCount);

			prevTotal = usage.ru_nvcsw;
			prevTime = usage.ru_utime;

			//clear out old data
			memset(&allToks[0], 0, sizeof(allToks));
			token = NULL; 
			response[0] = '\0';
			pid = 0;
			status = 0;
    	}
	}
}
