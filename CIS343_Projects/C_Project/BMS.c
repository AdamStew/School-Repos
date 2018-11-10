#include <stdio.h>
#include <stdlib.h>

/**
 * Author: Adam Stewart
 * Assignment: Programming Project Two in C
 * Due Date: Monday, October 10, 2016
 */

int main(int argc, char *argv[]){
	//References code from https://www.codingunit.com/c-tutorial-file-io-using-text-files
	FILE *input_file = fopen(argv[1] , "r");
	FILE *output_file = fopen("BMSOut.txt", "w");
	int ln=0;
	char str[80];

	/* opening file for reading */
	if(input_file == NULL) {
		perror("Error opening file");
		return(-1);
	}

	
	while(fgets(str, 80, input_file)!=NULL){
		ln++;
		puts(str);
		fprintf(output_file, "%d %s", ln, str);
    }

	fprintf(output_file, "\n");

	fclose(input_file);
	fopen(argv[1], "r");

	InvalidFirstChar(input_file, output_file);

	fclose(input_file);
	fopen(argv[1], "r");

	LabelTooLong(input_file, output_file);

	fclose(input_file);
	fopen(argv[1], "r");

	NoBlank(input_file, output_file);

	fclose(input_file);
	fopen(argv[1], "r");

	IllOpCode(input_file, output_file);

	fclose(input_file);
	fopen(argv[1], "r");

	OpcodeWrongCol(input_file, output_file);

	fclose(input_file);
	fopen(argv[1], "r");

	CheckEndOperand(input_file, output_file);

	fclose(input_file);
	fclose(output_file);
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Checks the first character of each and every line in a file to see it's eligible for a BMS program.
 * INPUT: File *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once end of the document is reached, that way it doesn't reach out of bounds.
 */
void InvalidFirstChar(FILE *ifile, FILE *ofile){
	int c, ln=1;
	char er[60] = "ERROR: Invalid first character. Line: "; 
	//checks first line
	c=fgetc(ifile);
	if((c!='*') && (c!=' ') && !(isupper(c))){ //Checks if it's a valid starting character.
				fprintf(ofile, "%s %d \n", er, ln);
			}
	//checks all the rest of the lines
	while((c=fgetc(ifile))!=EOF){
		if(c == '\n'){ //Looks for a new line to start on.
			ln++;
			c=fgetc(ifile);
			if((c!='*') && (c!=' ') && !(isupper(c)) && (c!=EOF)){ //Checks if it's a valid starting character or end of file.
				fprintf(ofile, "%s %d \n", er, ln);
			}
		}
	}
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Checks to see if any labels are longer than 7 characters.
 * INPUT: FILE *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once the end of the document is reached, so it doesn't go out of bounds.
 */
void LabelTooLong(FILE *ifile, FILE *ofile){
	int c, i, ln=2;
	char er[60] = "ERROR: Label is too long. Line: ";
	c=fgetc(ifile);
	
	while((c=fgetc(ifile))!=EOF){ //Checks for end of the file.
		if(c == '\n'){ //Finds new line.
			ln++;
			c=fgetc(ifile);
			if(isupper(c)){ //Makes sure that this is a label.
				for(i=0; i<7; i++){
					c=fgetc(ifile);
				}
				if(c!=' '){ //Checks if the label is still going on after 7 chars.
					fprintf(ofile, "%s %d \n", er, ln);
				}
			}
		}
	}
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Checks the 8th and 9th columns of each line in the BMS program to see if they're empty spaces.
 * INPUT: FILE *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once the end of the document is reached, so it doesn't go out of bounds.
 */
void NoBlank(FILE *ifile, FILE *ofile){
	int c, d, ln, i=0;
	char er[60] = "ERROR: Columns 8 and 9 are not blanks. Line: ";

	//Check first line.
	if(c!='*'){ //Checks if it's a comment.
		ln=1;
		while(i<7){
		c=fgetc(ifile); //skips cols 1-7
		i++;
		}
		c=fgetc(ifile); //col 8
		d=fgetc(ifile); //col 9
		
		if((c!=' ') || (d!=' ')){
			fprintf(ofile, "%s %d \n", er, ln);
		}
	}

	//Checks all the rest of the lines.
	while((c=fgetc(ifile))!=EOF){
		if(c == '\n'){ //Finds the start of a new line.
			ln++;
			c=fgetc(ifile);
			if(c!='*' && c!=EOF){ //Checks to see if it's a comment, or the end of the file.
				while(i<6){
					c=fgetc(ifile); //skips cols 1-7
					i++;
				}
				c=fgetc(ifile); //col 8
				d=fgetc(ifile); //col 9
				i=0; //Resets for the next line.

				if((c!=' ') || (d!=' ')){
					fprintf(ofile, "%s %d \n", er, ln);
				}
			}
		}
	}
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Checks to see if the Op-Code is legal.
 * INPUT: FILE *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once the end of the document is reached, so it doesn't go out of bounds.
 */
void IllOpCode(FILE *ifile, FILE *ofile){
	int c, i, ln=1;
	char opcode[6];
	char opend[3];
	char er[60] = "ERROR: Found illegal Op-Code. Line: ";

	//All legal op-codes.
	char op1[6] = {'D', 'F', 'H', 'M', 'D', 'I'};
	char op2[6] = {'D', 'F', 'H', 'M', 'D', 'F'};
	char op3[6] = {'D', 'F', 'H', 'M', 'S', 'D'};
	char op4[6] = {' ', ' ', ' ', ' ', ' ', ' '};


	//Checks first line.
	c=fgetc(ifile);
	if(c==' ' || isupper(c)){
		for(i=0; i<9; i++){ //Finds col 10.
			c=fgetc(ifile);
		}

		for(i=0; i<sizeof(opcode); i++){
			opcode[i] = c; //Putting the characters of the op-code into a string.
			c=fgetc(ifile);
		}

		
		if((strcmp(opcode, op1) != 0) && (strcmp(opcode, op2) != 0) && (strcmp(opcode, op3) != 0) 
				&& (strcmp(opcode, op4) != 0)){ //Compares to legal codes.
			fprintf(ofile, "%s %d \n", er, ln);
		}
	}

	//Checks all of rest of the lines.
	while((c=fgetc(ifile))!=EOF){
		if(c == '\n'){ //Finds the start of a new line.
			ln++;
			c=fgetc(ifile);
			if((c==' ' || isupper(c)) && c!=EOF){ //Checks to see if an op-code is allowed in this line.
				for(i=0; i<9; i++){ //Finds col 10.
					c=fgetc(ifile);
				}

				for(i=0; i<sizeof(opcode); i++){
					opcode[i] = c; //Putting the characters of the op-code into a string.
					c=fgetc(ifile);
				}

				opend[0] = opcode[0]; //Checking if it's 'END', doesn't count as op-code.
				opend[1] = opcode[1];
				opend[2] = opcode[2];

				if((strcmp(opcode, op1) != 0) && (strcmp(opcode, op2) != 0) && (strcmp(opcode, op3) != 0) //Compares to legal codes.
						&& (strcmp(opcode, op4) != 0) && (opend[0]!='E') && (opend[1]!='N') && (opend[2]!='D')){ 
					fprintf(ofile, "%s %d \n", er, ln);
				}
			}
		}
	}
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Scans the entire program checking to see if any legal Op-Codes are found, however are in the wrong columns.
 * INPUT: FILE *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once the end of the document is reached, so it doesn't go out of bounds.
 */
void OpcodeWrongCol(FILE *ifile, FILE *ofile){
	int c, i, ln=1, col=1;
	char opcode[6];
	char er[80] = "ERROR: Found an Op-Code on this line, however in the wrong column. Line: ";

	//All legal op-codes.
	char op1[6] = {'D', 'F', 'H', 'M', 'D', 'I'};
	char op2[6] = {'D', 'F', 'H', 'M', 'D', 'F'};
	char op3[6] = {'D', 'F', 'H', 'M', 'S', 'D'};
	
	//Filling up a string with the first 6 chars.
	for(i=0; i<6; i++){
		c=fgetc(ifile);
		col++;
		opcode[i]=c; 
	}

	//Checking first string.        Checks if a op-code exists on the line, and isn't in correct spot. -V
	if(((strcmp(opcode, op1)==0) || (strcmp(opcode, op2)==0) || (strcmp(opcode, op3)==0)) && col!=15){ 
		fprintf(ofile, "%s %d \n", er, ln);
	}

	//This checks the entire rest of the text file.
	while((c=fgetc(ifile))!=EOF){
		if(c=='\n'){
			c=fgetc(ifile);
			col=1; //This will keep track to what column we're in on the line.
			i=1; //Starts the loop early if it's the start of a new line.
			if((c==' ' || isupper(c)) && c!=EOF){ //Checks to make sure it's not a comment.
				opcode[0]=c;
			}
		}
		if(col==1){ //Comes to here after break statement.
			ln++;
			if((c==' ' || isupper(c)) && c!=EOF){ //Checks if it's a comment or the end of the file.				
				opcode[0]=opcode[1];             //This is needed so the first character isn't skipped.
				opcode[1]=opcode[2];
				opcode[2]=opcode[3];
				opcode[3]=opcode[4];
				opcode[4]=opcode[5];
				opcode[5]=c;
			}	
		}
		col++;
		for(i; i<6; i++){
			if(c=='\n'){ //Breaks out of for loop if encountering the end of a line.
				col=1;
				break;
			}
			if(c!=EOF){ //Shifts all of the chars over in the string.		
				opcode[0]=opcode[1];
				opcode[1]=opcode[2];
				opcode[2]=opcode[3];
				opcode[3]=opcode[4];
				opcode[4]=opcode[5];
				opcode[5]=c;           //Checks if a op-code exists on the line, and isn't in incorrect column. -V
				if(((strcmp(opcode, op1)==0) || (strcmp(opcode, op2)==0) || (strcmp(opcode, op3)==0)) && col!=16){ 
					fprintf(ofile, "%s %d \n", er, ln);                                                          
				}
			}
			if(i==5){ //This is so the char isn't incremented twice in the for & while loop.
				break;
			}
			col++; //Increments what column we're in by 1.
			c=fgetc(ifile); //Moves over in the column.
		}
		i=0; //Resets loop.
	}
}

/**
 * AUTHOR: Adam Stewart
 * PURPOSE: Checks if the operand lies in the correct columns, assuming there's no operand.
 * INPUT: File *file - Takes a text file to read.
 * OUTPUT: Void.
 * ERROR HANDLING: Program stops once end of the document is reached, so it doesn't go out of bounds.
 */
void CheckEndOperand(FILE *ifile, FILE *ofile){
	int i, col, ln=0, c=fgetc(ifile);
	char opcode[6];
	char opend[3];
	char er1[60] = "ERROR: Operand is in the wrong column. Line: ";
	char er2[60] = "ERROR: Text found after 'END' statement. Line: ";

	//All legal op-codes.
	char op1[6] = {'D', 'F', 'H', 'M', 'D', 'I'};
	char op2[6] = {'D', 'F', 'H', 'M', 'D', 'F'};
	char op3[6] = {'D', 'F', 'H', 'M', 'S', 'D'};

	char endcheck[4] = {'E', 'N', 'D', '\0'};
	char blank[6] = {' ', ' ', ' ', ' ', ' ', ' '};

	while(c!=EOF){
		ln++;
		col=1; //Resets what column you're in when finding a newline.
		for(i=0; i<9; i++){ //Going to where the opcode starts on a line.
			if(c==EOF){
				break;
			}
			if(c=='*' && col==1){ //Checking if it's a comment.
				while(c!='\n'){
					c=fgetc(ifile);
				}
				break;
			}
			c=fgetc(ifile); //Skipping to opcode.
			col++;
		}

		for(i=0; i<3; i++){ //Collecting the characters where opcodes go.
			if(c==EOF || c=='\n'){ //Only does half of the string, to see if it's 'END'.
				break;
			}
			opcode[i]=c; //Data for op-code.
			opend[i]=c;  //Data for 'END'.
			c=fgetc(ifile);
			col++;
		}

		for(i; i<6; i++){ //Does other half here, to fill in the rest of the op-code.
			if(c==EOF || c=='\n'){
				break;
			}
			opcode[i]=c; //Data for second half of op-code.
			c=fgetc(ifile);
			col++;
		}

		if(c!='\n'){ //Resets all the code if at the end of the line.	
			if(strcmp(opcode, blank) == 0){ //Checking if there's text where the op-code goes.
				if(c==' ' && c!=EOF){ //This is where the operand should begin. 
					while(c!='\n'){
						c=fgetc(ifile);
						col++;
						if(c!=' ' && c!='\n'){ //Checking if characters exist on this line (in wrong col).
							fprintf(ofile, "%s %d \n", er1, ln);
							while(c!='\n'){ //Once the error is found, it skip the rest of the line.
								c=fgetc(ifile);
							}
						}
					}
				}else{ //Comes here if operand is proper.
					while(c!='\n'){
						c=fgetc(ifile);
					}
				}
			}else{ //This code checks if any Op-Codes are present.
				if((strcmp(opcode, op1) == 0) || (strcmp(opcode, op2) == 0) || (strcmp(opcode, op3) == 0) 
						|| (strcmp(opend, endcheck) == 0)){
					if(strcmp(opend, endcheck) == 0){ //Confirming it's 'END'.
						while(c!=EOF){ //Checking if there's text after the 'END' statement.
							if((c!=' ')&&(c!= '\n')){
								fprintf(ofile, "%s %d \n", er2, ln);
								while(c!=EOF){ //When the error is found, just end the file.
									c=fgetc(ifile);
								}
							}
						c=fgetc(ifile);
						}	
					} //Comes down here if it's not 'END', but is an Op-Code.
					while(c!='\n'){ //If an op-code exists, it just skips to the next line, and starts over.
						if(c==EOF){
							break;
						}
						c=fgetc(ifile);
					}
				}else{ //Goes here if text exists, but isn't Op-Code, implying it's an operand, in wrong col.
					fprintf(ofile, "%s %d \n", er1, ln);
					while(c!='\n'){
						if(c==EOF){
							break;
						}
						c=fgetc(ifile);					
					}
				}
			}
		}
		c=fgetc(ifile); //Brings it to the first column of a newline.
	}
}




