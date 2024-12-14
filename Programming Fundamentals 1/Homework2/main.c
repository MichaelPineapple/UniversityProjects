/*
    Author:     Michael Wilson
    Course:     COP2220
    Homework:   2
    Title:      Data Sorter
    Due Date:   1/22/17

    Inputs data from a file, sorts the data in both ascending and descending order,
    displays the data to the user, and finally writes the data to an output file.
*/


#include <stdio.h>
#include <stdlib.h>

// Define constant symbols
#define INPUT_FILE "Input.txt"
#define OUPUT_FILE "Output.txt"
#define SIZE_OF_ARRAY 50

// Initialize functions
void displayArray(int array[], bool sorted, bool ascending);
void readFile(int array[], char* filename);
void sortArray(int array[], bool ascending);
void swap(int array[], int pos1, int pos2);
void writeFile(int array[], char* filename);



int main() 
{
    // Initialize the array
    int array[SIZE_OF_ARRAY];

    // Display the programmer’s name and project title    
    printf("Ima C Programmer\nHomework 2\n\n"); 
    
    // Read the input file, sort the data, display the data, and then write the sorted data to the output file
    readFile(array, INPUT_FILE);
    displayArray(array, false, false); // Display the numbers stored in the array (original unsorted order)
    sortArray(array, true); // Sort the array in ascending order
    displayArray(array, true, true); // Display the numbers stored in the array (sorted order - ascending)
    sortArray(array, false); // Sort the array in descending order
    displayArray(array, true, false); // Display the array (sorted order - descending)
    writeFile(array, OUPUT_FILE);
}




/* Displays an array to the user.
   array[] - The array being displayed.
   sorted - Indicates if the array is sorted.
   ascending - Indicates how the array is sorted.
*/
void displayArray(int array[], bool sorted, bool ascending) 
{
    // Initialize function varaible x
    int x;

    // Display the type of sorting
    if (!sorted) 
    {
        printf("Array Contents (unsorted)");
    } 
    else if (ascending) 
    {
        printf("Array Contents (sorted ascending)");
    } 
    else 
    {
        printf("Array Contents (sorted descending)");
    }

    // Display the array
    for (x = 0; x < SIZE_OF_ARRAY; x++) 
    {
        printf("%d\n", array[x]);
    }
}




/* Reads data from a file.
   array[] - The data storage.
   filename - Name of the input file.
*/
void readFile(int array[], char* filename) 
{
    // This function reads data from the file into the array
}




/* Sorts an array in either ascending or descending order.
   array[] - The array being sorted.
   ascending - Indicates ascending or descending order.
*/
void sortArray(int array[], bool ascending) 
{
    // Initialize function variables
    int x, y;
    bool swapped = true;

    // Sort the array
    for (y = 0; y < SIZE_OF_ARRAY && swapped; y++) 
    {
        swapped = false;
        for (x = 0; x < SIZE_OF_ARRAY - (y + 1); x++) 
        {
            if (array[x] > array[x + 1]) 
            {
                swap(x, x + 1);
                swapped = true;
            }
        }
    }

}




/* Swaps two values in an array.
   array[] - The array being modified.
   pos1 - The first array index being swapped.
   pos2 - The second array index being swapped.
*/
void swap(int array[], int pos1, int pos2) 
{
    // Store the first array value being swapped
    int temp = array[pos1];

    // Swap the values
    array[pos1] = array[pos2];
    array[pos2] = temp;
}




/* Writes array data to a file.
   array[] - The array data being written.
   filename - Name of the output file.
*/
void writeFile(int array[], char* filename) 
{
    // This function writes data from the array out to the file
}