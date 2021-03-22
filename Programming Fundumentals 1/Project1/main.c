/*
Author:    Michael Wilson
Course:    COP2220
Project #: 1
Title  :   Project 1 - Unit Conversion Tool
Due Date:  2/5/2017

Converts fahrenheit to celcius, feet to meters, and pounds to kilograms
*/

#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>

// define constants for unit conversions
#define FT_TO_M_RATIO 0.3048;
#define LBS_TO_KG_RATIO 0.4536;

// define constants for output format
#define ROW_ONE   "\n      Original          Value     Converted to          Value"
#define ROW_TWO   "\n      --------          -----     ------------          -----"
#define ROW_THREE "\n    Fahrenheit%15d          Celsius%15d"
#define ROW_FOUR  "\n          Feet%15d           Meters%15d"
#define ROW_FIVE  "\n        Pounds%15d        Kilograms%15d\n\n"

int main()
{
	// Initialize founction variables
	int fahrenheit, feet, pounds, celcius, meters, kilograms;

	// Display programmer name and project title
	printf("Michael Wilson\n");
	printf("Project 1 - Unit Conversion Tool\n\n");

	// Get user input
	printf("Enter a Fahrenheit temperature (integer): ");
	scanf("%d", &fahrenheit);
	printf("Enter a distance in feet (integer): ");
	scanf("%d", &feet);
	printf("Enter a weight in pounds (integer): ");
	scanf("%d", &pounds);
	
	// Perform conversions
	celcius = (fahrenheit - 32) * 5 / 9; // Fahrenheit to celcius
	meters = feet * FT_TO_M_RATIO; // Feet to meters
	kilograms =  pounds * LBS_TO_KG_RATIO; // Pounds to kilograms

	// Display data
	printf(ROW_ONE);
	printf(ROW_TWO);
	printf(ROW_THREE, fahrenheit, celcius);
	printf(ROW_FOUR, feet, meters);
	printf(ROW_FIVE, pounds, kilograms);

	system("pause");
	return 0;
}