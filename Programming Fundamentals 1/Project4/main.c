/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Project 4 - Random Number Statistics
Due Date:  4/9/2017

Reads a file, generates an amount of random values based on the count and seed data read, gets the amount of occurances of each value, 
calculates the minimum, maximum, median, and average occurances, and finally prints out a statistics report.
*/

#define _CRT_SECURE_NO_WARNINGS
#define _CRT_NONSTDC_NO_DEPRECATE

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#define ARRAY_SIZE 100
#define SEED_MIN 0
#define SEED_MAX 10000
#define COUNT_MIN 100
#define COUNT_MAX 1000
#define OUT_OF_RANGE_ERR "The supplied value [%d] is out of range [%d - %d]"

/* Copies an array of integers from one array to another
array[] - the array to be copied
copy[] - the array to store the copy
*/
void copyArray(int array[], int copy[])
{
	for (int i = 0; i < ARRAY_SIZE; i++) copy[i] = array[i];
}

/* Generates an array of the number of occurances from a set of random numbers below 100
array[] - the array being created
seed - the random number seed
count - the amount of random numbers to be generated
*/
void generateNumbers(int array[], int seed, int count)
{
	srand(seed);
	for (int i = 0; i < count; i++) array[rand() % ARRAY_SIZE]++;
}

/* Swaps two value in an array provided
array[] - the array to be modified
pos1 - the position of the first value to be swapped
pos2 - the position of the second value to be swapped
*/
void swap(int array[], int pos1, int pos2)
{
	int temp = array[pos1];
	array[pos1] = array[pos2];
	array[pos2] = temp;
}

/* Sorts an array of integers into numerical order using the bubble sort meathod
array[] - the array to be sorted
*/
void sortArray(int array[])
{
	bool didSwap = true;
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		didSwap = false;
		for (int j = 0; j < ARRAY_SIZE - (i + 1); j++)
		{
			if (array[j] > array[j + 1])
			{
				swap(array, j, j + 1);
				didSwap = true;
			}
		}
	}
}

/* Gets the average value of an array provided
array[] - the array
*/
double getAverage(int array[])
{
	double total = 0;
	for (int i = 0; i < ARRAY_SIZE; i++) total += array[i];
	return total / ARRAY_SIZE;
}

/* Gets the maximum value of an array provided
array[] - the array
*/
int getMax(int array[])
{
	int max = array[0];
	for (int i = 0; i < ARRAY_SIZE; i++) if (array[i] > max) max = array[i];
	return max;
}

/* Gets the minimum value of an array provided
array[] - the array
*/
int getMin(int array[])
{
	int min = array[0];
	for (int i = 0; i < ARRAY_SIZE; i++) if (array[i] < min) min = array[i];
	return min;
}


/* Gets the median value of an array provided
array[] - the array
*/
double getMedian(int array[])
{
	int arrayB[ARRAY_SIZE];
	copyArray(array, arrayB);
	sortArray(arrayB);
	return ((arrayB[49] + arrayB[50]) / 2.0);
}

/* Prints out the statistics provided about an array of random number occurances
array[] - the array of random number occurances
count - the amount of random numbers
min - the minimum number of occurances
max - the maximum number of occurances
median - the median number of occurances
average - the average number of occurances
*/
void printReport(int array[], int count, int min, int max, double median, double average)
{
	printf("Random number statistics report\n\nMinimum Count: %d\nMaximum Count: %d\nMedian Count: %.2f\nAverage Count: %.2f\n\nArray analysis (Position: Count [Percentage])\n", 
		min, max, median, average);

	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		if (i % 8 == 0) printf("\n");
		printf("%2d:%2d [%.1f%%] ", i, array[i], ((double)array[i]/(double)count)*100.0);
	}
}

/* Opens and reads data from a file, also check if the file exists and if the data is valid 
filename - the adress of the file to open
pSeed - a pointer to store the seed value from the file
pCount - a pointer to store the count value from the file
*/
bool readFile(char *filename, int *pSeed, int *pCount)
{
	bool status = false;

	FILE *pFile = fopen(filename, "r");

	if (pFile == NULL) printf("Cannot find the required data file."); // [EOF was not working]
	else
	{
		if (fscanf(pFile, "%d %d", pSeed, pCount) == 2) status = true;
		else printf("At least one of the supplied values is invalid.");

		fclose(pFile);
	}

	return status;
}

/* Returns whether a value provided is within the range provided
value - the value being tested
minRange - the minimum of the range
maxRange - the maximum of the range
*/
bool withinRange(int value, int minRange, int maxRange)
{
	return (value >= minRange && value <= maxRange);
}

/* Checks in the values provided are within their respective ranges
seed - the seed value to be checked
count - the count value to be checked
*/
bool validateInput(int seed, int count)
{
	bool status = false;

	if (withinRange(seed, 0, 10000))
	{
		if (withinRange(count, 100, 1000))
		{
			status = true;
		}
		else printf(OUT_OF_RANGE_ERR, count, COUNT_MIN, COUNT_MAX);
	}
	else printf(OUT_OF_RANGE_ERR, seed, SEED_MIN, SEED_MAX);

	return status;
}

/* Calculates the minimum, maximum, median, and average values of the array provided
array[] - the array provided
pMin - a pointer to store the minimum
pMax - a pointer to store the maximum
pMedian - a pointer to store the median
pAverage - a pointer to store the average
*/
void calculateStatistics(int array[], int *pMin, int *pMax, double *pMedian, double *pAverage)
{
	*pAverage = getAverage(array);
	*pMax = getMax(array);
	*pMedian = getMedian(array);
	*pMin = getMin(array);
}



int main(int argc, char* argv[])
{
	// declare program variables
	int mainSeed = 0, mainCount = 0, mainMinimum = 0, mainMaximum = 0, mainArray[ARRAY_SIZE] = { 0 };
	double mainMedian = 0, mainAverage = 0;
	char* mainFileName;

	printf("Michael Wilson\nProject 4 - Random Number Statistics\n\n"); // print programmer name and program title

	if (argc == 2) // test if the amount of command line arguments is correct (2)
	{
		mainFileName = argv[1];

		if (readFile(mainFileName, &mainSeed, &mainCount)) // open and read the file for data, also check if the file exists and if the data is valid
		{
			if (validateInput(mainSeed, mainCount)) // check if the imported data is within the correct ranges
			{
				generateNumbers(mainArray, mainSeed, mainCount);
				calculateStatistics(mainArray, &mainMinimum, &mainMaximum, &mainMedian, &mainAverage);
				printReport(mainArray, mainCount, mainMinimum, mainMaximum, mainMedian, mainAverage);
			}
		}
	}
	else printf("Invalid Command Line Arguments");

	printf("\n\n");
	system("pause");
	return 0;
}
