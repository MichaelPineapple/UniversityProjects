/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Homework 4 - Leap Year Calculator
Due Date:  2/12/2017

Tells the user if the year they input is a leap year.
*/

#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>

// Define constants
#define MIN_YEAR 1900
#define MAX_YEAR 2030

/* Returns if a value is within the range given
min - Minimum of the range
max - Maximum of the range
value - The value being tested
*/
bool withinRange(int value, int min, int max)
{
	return (value <= max && value >= min);
}

int main()
{
	// Initialize program variables
	int year = 0;
	bool leap = false;

	// Display prgrammer name and project title
	printf("Michael Wilson\n");
	printf("Homework 4\n\n");

	// Prompt the user to enter a year
	printf("Enter a year between %d and %d: ", MIN_YEAR, MAX_YEAR);
	scanf("%d", &year);

	// Determine if the year entered is within the correct range
	if (withinRange(year, MIN_YEAR, MAX_YEAR))
	{
		// Determine if the year entered is a leap year
		if (year % 4 == 0)
		{
			leap = true;

			if (year % 100 == 0)
			{
				leap = false;

				if (year % 400 == 0)
				{
					leap = true;
				}
			}
		}

		// Display if the year is a leap year or not
		if (leap)
		{
			printf("\n%d is a leap year\n\n", year);
		}
		else
		{
			printf("\n%d is not a leap year\n\n", year);
		}
	}
	else
	{
		// Display the the year entered is outside the correct range
		printf("\nThe value %d is out of range [%d to %d]\n\n", year, MIN_YEAR, MAX_YEAR);
	}

	system("pause");
	return 0;
}