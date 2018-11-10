#include <stdio.h>
#include <stdlib.h>

/*
Adam Stewart
CIS 343 01
9/26/2016
*/

int main()
{
	int maxValue = 5;
	int array[maxValue];
	int n = FillTable(array, maxValue);
	PrintReverseTable(array, n);
}

int FillTable (int a[], int max)
{
	printf("DIRECTIONS: Enter numbers to fill table by following the prompt. \n");
	int count = 0;
	int i, num; 
	char ans[10];
	for(i=0; i<max; i++){
		printf("Enter a number. \n");
		scanf("%d", &num);
		a[i] = num;
		count++;
		printf("Continue? (Type 'yes' to continue, anything else will exit.) \n");
		scanf("%s", ans);
		if(strcmp(ans, "yes") != 0){
			i=max+1;
		}
	}
	if(count == max){
		printf("Max limit has been reached.  Program ending. \n");
	}
	return count;
}

void PrintReverseTable (int a[], int max)
{
	int count = max-1;
	int i;
	for(i=0; i<max; i++){
		printf("%d-->", a[count]);
		count-=1;
	}
}

/*
What does the code LINK create_list(int a[], int n) do?
Creates a linked list that must have a size of at least 1.  When the size is 
=1, the tail=head.  When new values are entered, the previous value points 
to the next value, with the last value being the tail.

What does the code int count_items(LINK head) do?
Counts total number of elements in the linked list.

What does the code void print_list(LINK head) do?
Prints all values in the linked list so that it's "#-->#-->...".

What is the result of running the code?
The linked list has 6 elements.
The list is
5-->10-->15-->20-->25-->30-->

*/
