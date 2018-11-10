#include <stdio.h>
#include <stdlib.h>


int main(int argc, char *argv[])
{
	FILE *input_file = fopen(argv[1] , "r");;
	FILE *output_file = fopen("output.txt", "w");
	char str[71];
	char result[10];
	int c;

	/* opening file for reading */
	if(input_file == NULL) 
	{
		perror("Error opening file");
		return(-1);
	}

	c=fgetc(input_file);

	while (c!=EOF) {
		printf("%c", c);
		c=fgetc(input_file);
	}
   
	fclose(input_file);
	fclose(output_file);
   
	return(0);

	/*
	//This will print out each character in the string.
	char string[] = {'a', 'd', 'a', 'm'};
	int i;
	
	for(i=0; i<sizeof(string); i++){
		printf("%c \n", string[i]);
	}
	*/
}
