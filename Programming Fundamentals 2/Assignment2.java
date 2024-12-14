/*
Project 2
Michael Wilson
9-16-17
*/

import java.util.Scanner;
import java.lang.Math;

public class Assignment2
{
	public static void main(String[] args)
	{
      Scanner input = new Scanner(System.in); // initialize scanner variable
		
      System.out.print("Enter an odd positive integer: "); // prompt user to input data
      
      int data = input.nextInt(); // get input data
      
      if (data % 2 != 0 && data > 0) // check if input data is odd and greater than zero
      {
         double sum = 0.0, sum2 = 0.0, pi = 0.0; // desclare summation variables
         
         for (int i = 1; i <= data - 2; i += 2) sum += ((double)i/(i + 2.0)); // calculate sum
		
      	for (int i = 1; i < data; i++) pi += 4*(Math.pow(-1.0, i+1.0)/((2.0*i) - 1.0)); // to approximate pi 
         
         // print calculated values
         System.out.printf("\nSum = %12.12f", sum);
		   System.out.printf("\nPi  = %2.12f", pi);
      }
      else System.out.print("\nInvalid input"); // print error message if input data is invalid
      
	}
}
