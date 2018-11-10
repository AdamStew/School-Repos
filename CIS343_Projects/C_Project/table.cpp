#include <stdio.h>
#include <stdlib.h>

int a[];
int max;

int main()
{
	max = 5;
	int n = FillTable(a[], max);
	PrintReverseTable(a[], n);
}

int FillTable (int a[], int max)
{
	printf("DIRECTIONS: Enter numbers to fill table, and 'exit' to finish. \n");
	int count = 0;
	int a[] = new int[max];
	for(int i=0; i<max; i++){
		printf("Enter a number. \n");
		scanf("%d", a[i]);
		count++;
		printf("Continue? (Type 'yes' to continue) \n");
		scanf(%s", ans);
		if(strcmp(ans, "yes") != 0){
			i=max+1;
		}
	}
	//if(i == max+1){
	//	for(int j=count; j<max; j++){
	//		a[j] = null;
	//	}
	//}
	if(count == max){
		printf("Max limit has been reached.  Program ending. \n");
	}
	return count;
}

void PrintReverseTable(int a[], int max)
{
	int count = max;
	for(int i=0; i<max; i++){
		printf("%d-->", a[count]);
		count-=1;
	}
}
