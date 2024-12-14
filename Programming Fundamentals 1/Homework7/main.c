/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Homework 7 - Array Sorting
Due Date:  4/2/2017

Generates an array of 25 and 24 random values, sorts them in numerical order, finds their medians, and prints out the results.
*/

#define _CRT_SECURE_NO_WARNINGS
#define _CRT_NONSTDC_NO_DEPRECATE

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define ARRAY1_SIZE 25
#define ARRAY2_SIZE 24
#define RND_MAX 999
#define ROW_SIZE 10
#define HEADER_STRING "\n\nArray Size: %d\n--------------\n\nMedian Value: %.2f\n"

/* Prints out an array
array[] - the array to print
arraySize - the size of the array to print
rowSize - the number of values which should be displayed in each row
*/
void printArray(int array[], int arraySize, int rowSize)
{
	for (int i = 0; i < arraySize; i++)
	{
		if (i % rowSize == 0) printf("\n");
		printf("%d ", array[i]);
	}
}


/* Retuns the median value of an array provided
array[] - the array provided
arraySize - size of the array provided
*/
double getMedian(int array[], int arraySize)
{
	double median = 0;

	if (arraySize % 2 == 0)
	{
		int arrayHalf = arraySize / 2;
		median = (array[arrayHalf - 1] + array[arrayHalf]) / 2.0;
	}
	else
	{
		median = array[(int)((arraySize / 2) + 0.5)];
	}

	return median;
}

int main()
{
	printf("Michael Wilson\nHomework 7"); // Display programmer name and program title

	// Initialize program variables
	int array1[ARRAY1_SIZE] = { 0 }, array2[ARRAY2_SIZE] = { 0 };
	double array1Median = 0, array2Median = 0;
	
	for (int i = 0; i < ARRAY1_SIZE; i++) array1[i] = (rand() % RND_MAX); // fill array1 with a set of random values between 1 and 999

	for (int i = 0; i < ARRAY2_SIZE; i++) array2[i] = array1[i]; 	// set array2 equal to array1 except for the last value

	// use bubble sort on array2
	bool swap = true;
	for (int i = 0; i < ARRAY2_SIZE; i++)
	{
		swap = false;
		for (int j = 0; j < ARRAY2_SIZE - (i + 1); j++)
		{
			if (array2[j] > array2[j + 1])
			{
				int temp = array2[j];
				array2[j] = array2[j + 1];
				array2[j + 1] = temp;
				swap = true;
			}
		}
	}

	// use selection sort on array1
	int min = 0;
	for (int i = 0; i < ARRAY1_SIZE; i++)
	{
		min = i;
		for (int j = i + 1; j < ARRAY1_SIZE; j++)
		{
			if (array1[j] < array1[min]) min = j;
		}
		int temp = array1[i];
		array1[i] = array1[min];
		array1[min] = temp;
	}

	// calculate median values of both arrays
	array2Median = getMedian(array2, ARRAY2_SIZE);
	array1Median = getMedian(array1, ARRAY1_SIZE);

	// Display results
	printf(HEADER_STRING, ARRAY2_SIZE, array2Median);
	printArray(array2, ARRAY2_SIZE, ROW_SIZE);
	printf(HEADER_STRING, ARRAY1_SIZE, array1Median);
	printArray(array1, ARRAY1_SIZE, ROW_SIZE);

	printf("\n\n");
	system("pause");
	return 0;
}