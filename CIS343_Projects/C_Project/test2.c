#include <stdio.h>
#include <stdlib.h>


int main(int argc, char *argv[])
{
	FILE *input_file = fopen(argv[1] , "r");;
	FILE *output_file = fopen("output.txt", "w");
	char str[71];
	char result[10];
	int i=0;
	int c;

	/* opening file for reading */
	if(input_file == NULL) 
	{
		perror("Error opening file");
		return(-1);
	}
	//checks first line
	c=fgetc(input_file);
	if((c!='*') && (c!=' ') && !(isupper(c))){
				i++;
				printf("%d", i);
				printf("It seems there's an error. \n");
			}else{
				i++;
				printf("%d", i);
				printf("This line checks out. \n");
			}
	//checks all the rest of the lines
	while((c=fgetc(input_file))!=EOF){
		if(c == '\n'){
			c=fgetc(input_file);
			if((c!='*') && (c!=' ') && !(isupper(c)) && (c!=EOF)){
				i++;
				printf("%d", i);
				printf("It seems there's an error. \n");
			}else{
				i++;
				printf("%d", i);
				printf("This line checks out. \n");
			}
		}
		//if(c == '*'){
		//	printf("%c", c);
		//	fprintf(output_file,"%c", c);
		//}
	}

   
	fclose(input_file);
	fclose(output_file);
   
	return(0);
}
