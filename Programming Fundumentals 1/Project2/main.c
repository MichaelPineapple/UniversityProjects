/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Project 2 - Wind Chill Calculator
Due Date:  2/19/2017

Calculates wind chill in Fahrenheit and Celcius.
*/

#define _CRT_SECURE_NO_WARNINGS

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// Define constants
#define INVALID_INPUT_ERR "\nThe entered value is invalid"
#define OUT_OF_RANGE_ERR "\nThe entered value is out of range [%d - %d]"
#define OUTPUT_FORMAT "\n%20s%12s%17s%17s"
#define TEMP_MIN -58
#define TEMP_MAX 41
#define WIND_MIN 2
#define WIND_MAX 50

/* Calculates Wind Chill based on the temperature and wind speed given.
temperature - Outside Temperature used for calulating wind chill.
windspeed - Wind Speed used for calculating wind chill.
*/
double calculateWindChill(int temperature, int windSpeed)
{
	return (35.74 + (0.6215*temperature) - 35.75*pow(windSpeed, 0.16) + 0.4275*temperature*pow(windSpeed, 0.16));
}

/* Converts a given temperature in Fahrenheit to Celcius
windChill_F - the wind chill temperature to be converted
*/
double convertTemperature(double windChill_F)
{
	return (windChill_F - 32) * 5 / 9;
}

/* Displays data to the user.
temperature - The temperature value to be displayed.
windSpeed - The windSpeed value to be displayed.
windChill_F - The wind chill value in Fahrenheit to be displayed.
windChill_C - The wind chill value in Celcius to be displayed.
*/
void displayResults(int temperature, int windSpeed, double windChill_F, double windChill_C)
{
	printf(OUTPUT_FORMAT, "Outside Temp (F)", "Wind Speed", "Wind-Chill (F)", "Wind-Chill (C)");
	printf(OUTPUT_FORMAT, "----------------", "----------", "--------------", "--------------");
	printf("\n%20d%12d%17.3f%17.3f", temperature, windSpeed, windChill_F, windChill_C);
}

/* Determines if a given value is within the range provided.
input - The value being tested.
minRange - The minimum value of the range.
maxRange - The maximum value of the range.
*/
bool withinRange(int input, int minRange, int maxRange)
{
	return ((input <= maxRange) && (input >= minRange));
}

/* Prompts the user to input data and checks if the data is valid and within range
pTemperature - The pointer value of the temperature to be assigned user input.
pWindSpeed - The pointer value of the wind speed to be assigned user input.
*/
bool getInput(int *pTemperature, int *pWindSpeed)
{
	// Initialize function variables
	int temp = 0, wind = 0;
	bool valid = false;

	// Prompt the user to input Outside Temperature
	printf("\nEnter Outside Temperature (Fahrenheit) [%d - %d]: ", TEMP_MIN, TEMP_MAX);

	if (scanf("%d", &temp) == 0) printf(INVALID_INPUT_ERR);
	else
	{
		// Check if OutsideTemperature is within the correct range
		if (withinRange(temp, TEMP_MIN, TEMP_MAX))
		{
			// Prompt the user to input Wind Speed
			printf("Enter Wind Speed [%d - %d]: ", WIND_MIN, WIND_MAX);

			if (scanf("%d", &wind) == 0) printf(INVALID_INPUT_ERR);
			else
			{
				// Check if WindSpeed is within the correct range
				if (withinRange(wind, WIND_MIN, WIND_MAX)) valid = true;
				else printf(OUT_OF_RANGE_ERR, WIND_MIN, WIND_MAX);
			}
		}
		else printf(OUT_OF_RANGE_ERR, TEMP_MIN, TEMP_MAX);
	}

	// Assign the input variables to the pointers
	*pTemperature = temp;
	*pWindSpeed = wind;

	return valid;
}

/* Calculates the data given into a wind chill in Fahrenheit and celcius.
temperature - The temperature value used for calculations.
windSpeed - The windSpeed value used for calculations 
pWindChill_F - The pointer value of wind chill in Fahrenheit to be calculated.
pWindChill_C - The pointer value of wind chill in Celcius to be calculated.
*/
void performCalculations(int temperature, int windSpeed, double *pWindChill_F, double *pWindChill_C)
{
	double windChillFtemp = calculateWindChill(temperature, windSpeed);
	*pWindChill_F = windChillFtemp;
	*pWindChill_C = convertTemperature(windChillFtemp);
}



int main()
{
	// Initialize program variables
	int outsideTemp = 0, windSpeed = 0;
	double windChillF = 0, windChillC = 0;

	// Display programmer name and program title.
	printf("Michael Wilson\n");
	printf("Project 2 - Wind Chill Calculator\n");
	
	if (getInput(&outsideTemp, &windSpeed)) // Get user input and check if valid
	{
		performCalculations(outsideTemp, windSpeed, &windChillF, &windChillC); // Calculate data based on user input
		displayResults(outsideTemp, windSpeed, windChillF, windChillC); // Display data
	}

	printf("\n\n");
	system("pause");
	return 0;
}