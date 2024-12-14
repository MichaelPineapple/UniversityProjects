/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Homework 8,9,10 - enum, typedef, and struct
Due Date:  4/16/2017
*/

#include <stdio.h>
#include <stdlib.h>

// define an enumerated list for the days of the week
enum days { Sun = 1, Mon, Tue, Wed, Thu, Fri, Sat };

// define typedefs
typedef bool boolean;
typedef char letter;
typedef char* string;
typedef double decimal;
typedef int number;

// define STUDENT structure
struct STUDENT
{
	letter
		studentId[10] = "N00000000",
		lastName[31] = "Sue",
		firstName[31] = "Mary",
		initial = 65;
	number enrolledCourses = 0;
	decimal gpa = 4.0;
	boolean inGoodStanding = true;
};

int main()
{
	system("pause");
	return 0;
}