#include <stdio.h>
#include <stdlib.h>
#include <term.h>
#include <curses.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>

/**
 * Author: Adam Stewart
 * Assignment: Program 1
 * Due Date: 09/14/2017
 */

//Reference: https://stackoverflow.com/questions/14422775/how-to-check-a-given-string-contains-only-number-or-not-in-c
int numbers_only(const char *s) {
    while(*s) {
        if(isdigit(*s++) == 0) {
			return 0;
		}
    }

    return 1;
}

int main(){

	initscr(); //Automatically calls setupterm().
	start_color(); //Should be called right after initscr().
	init_pair(1, COLOR_GREEN, COLOR_BLACK); //Pair number, Text color, background color.
	attron(COLOR_PAIR(1));

	int rows = tigetnum("lines"); //Get rows.
	int cols = tigetnum("cols");  //Gets columns.

	printw("Rows: %d \nCols: %d", rows, cols);
	wrefresh(stdscr); //Displays printw stmt.
	sleep(5); //Wait 5 seconds.
	wclear(stdscr); //Clear page. (stdscr is standard screen & curscr is current screen)
	wrefresh(stdscr); //Refresh blank page.
	mvwprintw(stdscr, 0, 0, "%d,%d:", rows, cols); //Print prompt.
	wrefresh(stdscr); //Display printw.

	char input[1024];
	char* token_1;
	char* token_2;
	int prevR = 0;
	int prevC = 0;

	while(1) {
		getstr(input);
		
		input[strcspn(input, "\n")] = '\0'; //Gets rid of \n char.
		if(strcmp(input, "quit") == 0) { //Type 'quit' to quit.
			break;
		}

		token_1 = strtok(input, ",");
		token_2 = strtok(NULL, ",");

		//If there's no comma, and/or our tokens aren't positive numbers, print error.  Else run code.
		if(token_2 != NULL && numbers_only(token_1) && numbers_only(token_2)) {
			//Wraps the values, so the user can't carelessly go out of bounds.
			int r = atoi(token_1)%rows;
			int c = atoi(token_2)%cols;

			move(c,r);
			mvwprintw(stdscr, r, c, "%d,%d:", rows, cols);

			//Remember previous spot, incase we want to print an error message.
			prevR = r;
			prevC = c;
		} else {
			//Print error message in the same spot.
			mvwprintw(stdscr, prevR, prevC, "Please enter valid coordinates. %d,%d:", rows, cols);
		}
		
		wrefresh(stdscr); //Refresh after every round.
	}
	endwin(); //Close special ncurses window.
	
	return 0;

}

