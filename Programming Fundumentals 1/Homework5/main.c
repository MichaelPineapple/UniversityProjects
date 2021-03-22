/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Homework 5 - Prime number counter
Due Date:  3/5/2017

Tells the user how many prime numbers exist between 0 and a given value
*/

#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// define constants
#define MIN_VALUE 1
#define MAX_VALUE 1000

/* Returns if the given value is within a given range
min - Minimum of the range
max - Maximum of the range
value - The value being tested
*/
bool withinRange(int value, int min, int max)
{
	return (value <= max && value >= min);
}

/* Returns if the given value is prime
testValue - The value being tested
*/
bool isPrime(int testValue)
{
	bool status = true;

	if (testValue < 2) status = false; // Test if the value is less than 2

	if (testValue != 2 && testValue % 2 == 0) status = false; // Test if the value is even (except 2)
	
	// Test if the value is evenly divisable by any odd number between 3 and the square root of the value
	for (int i = 3; i <= sqrt(testValue); i++)
	{
		if (i % 2 != 0) if (testValue % i == 0) status = false;
	}

	return status;
}

/* Returns the amount of prime numbers between 0 and the given value
input - the given value
*/
int countPrimes(int input)
{
	int count = 0;

	// Loop through every number between 0 and input to determine which are prime
	for (int i = 0; i <= input; i++)
	{
		if (isPrime(i)) count++;
	}

	return count;
}

int main()
{
	int number = 0; // Initialize program varable

	printf("Michael Wilson\nHomework 5\n"); // Display prgrammer name and project title

	printf("\nEnter a number between %d and %d: ", MIN_VALUE, MAX_VALUE); // Prompt the user to enter a number between 1 and 1000


	if (scanf("%d", &number) == 0) printf("\nInvalid input"); // Test if the entered value is valid
	else
	{
		// Test if the entered value is within range
		if (withinRange(number, MIN_VALUE, MAX_VALUE))
		{
			printf("\nExactly %d prime numbers exist between 0 and %d", countPrimes(number), number); // If so, display the amount of prime numbers which exist between 0 and the entered value
		}
		else
		{
			printf("\nThe value %d is out of range [%d to %d]", number, MIN_VALUE, MAX_VALUE); // If not, display error message
		}
	}
	
		
	
	printf("\n\n");
	system("pause");
	return 0;
}