#include <stdio.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <errno.h>
#include <string.h>

/*************************************************************
* Authors: Jarod Hanko & Adam Stewart				  		 *
* Assignment: Lab 12: File System Interface: Information 	 *
* Due Date: 11/30/2017 10:00AM EDT                    		 *
*                                                     		 *
* Description: This program takes a directory path and 		 *
* retrieves basic stats about them, such as file size and	 *
* INODE#.													 *
**************************************************************/
int main(int argc, char *argv[])
{
   DIR *dirPtr;
   struct dirent *entryPtr;
   dirPtr = opendir(argv[1]);


   struct stat statBuf;
   while ((entryPtr = readdir (dirPtr))) {
      printf ("%-20s", entryPtr->d_name); //Print directory name.
	  char path[4096]; //Holds entire path.
      strcpy(path, argv[1]);
      strcat(path, "/");
      strcat(path, entryPtr->d_name);
      if(stat(path, &statBuf) < 0) { //Gets the stats from our path.
         perror("huh? there is ");
         break;
      }
	  printf(" %zd bytes", statBuf.st_size); //Print size of file in bytes.
      printf("\t\t %zd INODE#\n", statBuf.st_ino); //Print INODE #.
   }
   closedir (dirPtr);
   return 0;
} 
