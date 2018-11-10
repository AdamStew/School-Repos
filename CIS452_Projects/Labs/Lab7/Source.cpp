/*********************************************************************************************
* Assignment: Lab 07: Mini Programming Assignment
* Authors: Jarod Hanko and Adam Stewart
* Date: 10/26/2017
* Description: Uses QueryPerformanceFrequency and QueryPerformanceCounter to measure the 
* time it takes for various processes to run
*********************************************************************************************/

#include <Windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>

using namespace std;

int main() {
	//returns the frequency of the high-resolution counter
	LARGE_INTEGER bigFreq;
	QueryPerformanceFrequency(&bigFreq);
	cout << bigFreq.QuadPart  << " ticks per second\n" << flush;

	//returns the period of the high-resolution counter (in microseconds)
	cout << (1.0/ bigFreq.QuadPart) * 1000000.0 << " microseconds between ticks\n" << flush;

	//measures and reports the duration (in microseconds) of a 1,000,000-iteration empty loop
	LARGE_INTEGER startCount, endCount, elapsedTime;
	QueryPerformanceCounter(&startCount);

	for (int i = 0; i < 1000000; i++) {
		//do nothing
	}

	QueryPerformanceCounter(&endCount);
	elapsedTime.QuadPart = endCount.QuadPart - startCount.QuadPart; //get difference in counts

	elapsedTime.QuadPart *= 1000000L; //time elapse in microseconds (really still in counts)
	elapsedTime.QuadPart /= bigFreq.QuadPart; //divide by freq to get microseconds (counts * counts/sec = seconds)
	cout << elapsedTime.QuadPart << " microseconds elapsed (duration)\n" << flush;

	//reports the overhead (in microseconds) of a call to the high-resolution counter itself
	LARGE_INTEGER start, end, elapsed2;
	double total = 0;
	//we do 1000 iterations so that we can take the average (sometimes the overhead is measured as 0)
	for (int i = 0; i < 1000; i++) {
		QueryPerformanceCounter(&start);
		QueryPerformanceCounter(&end);
		elapsed2.QuadPart = end.QuadPart - start.QuadPart; //get difference in counts
		total += elapsed2.QuadPart; //add to total so we can average it later
	}
	total /= 1000.0; //(we need to divide by 1000 iterations to get average)
	total *= 1000000L; //time elapse in microseconds
	cout << (total*1.0) / (bigFreq.QuadPart*1.0) << " microseconds elapsed (duration)\n" << flush; //divide by freq to get microseconds

	return 0;
}