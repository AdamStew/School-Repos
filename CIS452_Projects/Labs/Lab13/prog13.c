#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>


/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 13: File System Interface: Operations		 *
* Due Date: 12/07/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program takes arguments to either 		 *
* create a soft link or hard link.							 *
*															 *
* KEY: existingFile hardLinkedFile -> creates hard link.	 *
*	   -s existingFile softLinkedFile -> creates soft link.	 *
**************************************************************/
int main(int argc, char *argv[]) {	

	if(argc < 3) { //Check to see if we have the bare minimum paremeters.
		perror("Please enter the required number of parameters.");
		exit(1);
	} else if(argc == 3) { //If 3 parameters, do a hard link.
		link(argv[1], argv[2]);
		printf("%s\n", strerror(errno));
	} else if(argc == 4) { //Check if -s parameter is is inserted to have a soft link.
		if(strcmp(argv[1], "-s") == 0) { 
			symlink(argv[2], argv[3]);
			printf("%s\n", strerror(errno));
		} else {
			perror("Enter a valid argument (like -s).");
			exit(1);
		}
	} else {
		perror("Something went.. wrong.");
		exit(1);
	}
    

    return 0;
}

