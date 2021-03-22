/*
 * Michael Wilson
 * Project 3
 * 9-23-17
 */

import java.util.Scanner;

public class Assignment3 
{
	// constant vars
	static final int ARRAY_SIZE = 3;
	static final boolean DEBUG = false;
	
	public static void main(String args[])
	{
		int[][] array1 = new int[ARRAY_SIZE][ARRAY_SIZE], array2 = new int[ARRAY_SIZE][ARRAY_SIZE]; // initialize arrays
		
		Scanner input = new Scanner(System.in); // initialize keyboard input object
		
		System.out.print("Enter "+(ARRAY_SIZE*ARRAY_SIZE)*2+" integers"); // prompt user to input number
		
		// gets user input for first array
		for (int i = 0; i < ARRAY_SIZE; i++) for (int j = 0; j < ARRAY_SIZE; j++) 
			array1[i][j] = input.nextInt();

		// gets user input for second array
		for (int i = 0; i < ARRAY_SIZE; i++) for (int j = 0; j < ARRAY_SIZE; j++) 
			array2[i][j] = input.nextInt();
		
		if (DEBUG) displayRegular(array1, array2); // when in debug mode, display both arrays unfiltered
		
		System.out.print("\n\nhowmany: "+Strict.howmany(array1, array2)); // print result of Strict.howmany
		
		System.out.print("\n\ndiagonal: "+Strict.diagonal(array1, array2)); // print result of Strict.diagonal
		
		System.out.printf("\n\naverage: %.2f", Strict.average(array1, array2)); // print result of Strict.average
		
		Strict.display(array1, array2); // displays only odd values of both arrays using Strict.display
		
		System.out.print("\n\nsilly: "+Strict.silly(array1, array2)); // displays result of Strict.silly
	}

	/*
	 Displays two arrays
    * m1 - first array
    * m2 - second array
	*/
	static void displayRegular(int[][] m1, int[][] m2)
	{
		System.out.print("\n\n");
		for (int i = 0; i < ARRAY_SIZE; i++)
		{
			System.out.print("\n");
			for (int j = 0; j < ARRAY_SIZE; j++) System.out.print(" "+m1[i][j]+" ");
			System.out.print("      ");
			for (int j = 0; j < ARRAY_SIZE; j++) System.out.print(" "+m2[i][j]+" ");
		}
		System.out.print("\n\n");
	}
}



class Strict
{
	/*
	 Returns if two arrays are equal
    * m1 - first array
    * m2 - second array
	*/
	public static boolean equals(int[][] m1, int[][] m2)
	{
		boolean result = true;
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++) for (int j = 0; j < n01223646.ARRAY_SIZE; j++) 
			if (m1[i][j] != m2[i][j]) result = false;
		return result;
	}
	
	/*
	 Returns how many cell values are identical
      in two arrays.
    * m1 - first array
    * m2 - second array
	*/
	public static int howmany(int[][] m1, int[][] m2)
	{
		int result = 0;
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++) for (int j = 0; j < n01223646.ARRAY_SIZE; j++) 
			if (m1[i][j] == m2[i][j])  result++;
		return result;
	}
	
	/*
	  Returns how many cell values are identical
      along the diagonal of two arrays 
     * m1 - first array
     * m2 - second array
	*/
	public static int diagonal(int[][] m1, int[][] m2)
	{
		int result = 0;
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++) if (m1[i][i] == m2[i][i]) result++;
		return result;
	}
	
	/*
	  Returns the average of two arrays
    * m1 - first array
    * m2 - second array
	*/
	public static double  average(int[][] m1,int[][] m2)
	{
		double result = 0.0;
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++) for (int j = 0; j < n01223646.ARRAY_SIZE; j++) 
		{
			result += (double)m1[i][j];
			result += (double)m2[i][j];
		}
		return result/(2.0*(n01223646.ARRAY_SIZE*n01223646.ARRAY_SIZE));
	}
	
	/*
	  Displays only the odd values of two arrays
    * m1 - first array
    * m2 - second array
	*/
	public static void display(int[][] m1, int[][] m2)
	{
		System.out.print("\n\n");
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++)
		{
			System.out.print("\n");
			for (int j = 0; j < n01223646.ARRAY_SIZE; j++) 
			{
				if ((m1[i][j] % 2) != 0) System.out.print(" "+m1[i][j]+" ");
				else System.out.print("   ");
			}
			System.out.print("      ");
			for (int j = 0; j < n01223646.ARRAY_SIZE; j++)
			{
				if ((m2[i][j] % 2) != 0) System.out.print(" "+m2[i][j]+" ");
				else System.out.print("   ");
			}
		}
	}

	/*
	  Determines if all values in two arrays are greater than 1 and less than or equal to 10
    * m1 - first array
    * m2 - second array
	*/
	public static boolean silly(int[][] m1, int[][] m2)
	{
		boolean result = true;
		for (int i = 0; i < n01223646.ARRAY_SIZE; i++) for (int j = 0; j < n01223646.ARRAY_SIZE; j++) 
			if ((m1[i][j] <= 1 || m1[i][j] > 10) || (m2[i][j] <= 1 || m2[i][j] > 10)) result = false;
		return result;
	}	
	
}