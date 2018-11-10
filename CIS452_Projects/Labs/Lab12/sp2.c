#include <stdio.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <errno.h>

int main()
{
   DIR *dirPtr;
   struct dirent *entryPtr;

   dirPtr = opendir (".");

   struct stat statBuf;
   while ((entryPtr = readdir (dirPtr))) {
      printf ("%-20s", entryPtr->d_name);
      stat(entryPtr->d_name, &statBuf);
      printf(" %zd bytes\n", statBuf.st_size);
   }
   closedir (dirPtr);
   return 0;
}

