#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

/**
* Authors: Jarod Hanko, Adam Steward, and Stack Overflow
* Date: Pearl Harbor Day + 1, 2017
* Description: A short program that uses /dev/random to seed the random number
* generator
**/
int main()
{
    int byte_count = 4;
    char data[4];
    FILE *fp;
    fp = fopen("/dev/random", "r");
    fread(&data, 1, byte_count, fp);
    fclose(fp);

    unsigned int seed = *(unsigned int *)data;
    printf("Seed %u \n", seed);
    srandom(seed);
    int i = 0;
    for(i = 0; i < 10; i++){
        printf("random  number: %ld \n", random());
    }

    return 0;
}

