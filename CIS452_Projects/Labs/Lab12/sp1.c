#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <errno.h>

int main(int argc, char *argv[])
{
   struct stat statBuf;

   if (argc < 2) {
      printf ("Usage: filename required\n");
      exit(1);
   }

   if (stat (argv[1], &statBuf) < 0) {
      perror ("huh?  there is ");
      exit(1);
   }

   printf ("value is: %u\n", statBuf.st_mode);
   if((statBuf.st_mode & S_IFDIR) > 0) {
      printf("I am a directory!");
   } else if ((statBuf.st_mode & S_IFMT) > 0) {
      printf("I am a type of file!");
   }
   return 0;
} 
