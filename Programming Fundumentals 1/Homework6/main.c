/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Homework 6 - Random number generator
Due Date:  3/12/2017

Generates an array of 15 random numbers between 0 and 1000, then finds the minimum value, maximum value, and anverage of the array.
*/

#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define ARRAY_SIZE 15

int main()
{
	// Initialize program variables
	int numbers[ARRAY_SIZE] = { 0 };
	int min = 0;
	int minIndex = 0;
	int max = 0;
	int maxIndex = 0;
	int total = 0;
	double average = 0;

	// Generate array of 15 random numbers between 0 and 1000
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		numbers[i] = (rand() % 1000);
	}

	// Calculate minimum value of the array
	min = numbers[0];
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		if (numbers[i] < min)
		{
			min = numbers[i];
			minIndex = i;
		}
	}

    // Calculate maximum value of the array
	max = numbers[0];
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		if (numbers[i] > max)
		{
			max = numbers[i];
			maxIndex = i;
		}
	}

	// Calculate average value of the array
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		total += numbers[i];
	}
	average = (double)total / (double)ARRAY_SIZE;


	printf("Michael Wilson\nHomework 6\n\n"); // Display prgrammer name and project title

	// Display array contents
	printf("Contents of the array:\n");
	for (int i = 0; i < ARRAY_SIZE; i++)
	{
		printf("%d, ", numbers[i]);
	}

	// Display array min, max, and average
	printf("\n\nThe minimum value %d is located at index %d", min, minIndex);
	printf("\nThe maximum value %d is located at index %d", max, maxIndex);
	printf("\nThe average value of the array is %.2f\n\n", average);

	system("pause");
	return 0;
}