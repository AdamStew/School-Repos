
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define SIZE 16

extern char etext, edata, end;

void stackVar();

static int i = 0;
static int k, l;
static int j = 1;

int main()
{
	char *data1, *data2;
	
	data1 = malloc(SIZE);
	data2 = malloc(SIZE);

	//Stack
	stackVar();

	//Library

	//Heap
	printf("HEAP DATA1 ADDR: %10p\n", data1);
	printf("HEAP DATA2 ADDR: %10p\n", data2);

	//Unitialized data
	printf("Uninitialized data segment (end) %10p\n", &end);
	printf("UNINIT DATA1 ADDR: %10p\n", &k);
	printf("UNINIT DATA2 ADDR: %10p\n", &l);

	//Initialized data
	printf("Initialized data segment(edata)  %10p\n", &edata);
	printf("INIT DATA1 ADDR: %10p\n", &i);
	printf("INIT DATA2 ADDR: %10p\n", &j);
	
	//Text
	printf("TEXT SEGMENT: %10p\n", &etext);

	while(1) {
		//Loop to pause.
	}

    return 0;
}

void stackVar() {
	int a=0, b=1;
	printf("STACK DATA1 ADDR: %10p\n", &a);
	printf("STACK DATA2 ADDR: %10p\n", &b);
}
