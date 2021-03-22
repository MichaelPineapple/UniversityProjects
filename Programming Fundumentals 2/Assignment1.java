/*
	Assignment 1
	Michael Wilson
	8-31-17
*/

import java.util.Scanner;

public class Assignment1
{
	public static void main(String[] args)
	{
		double drivingDistance = 0, milesPerGallon = 0, pricePerGallon = 0, costOfDriving = 0;
		
		// create new Scanner object
		Scanner keyboardInput = new Scanner(System.in);
		
		// prompt user to enter driving distance in miles
		System.out.print("Enter the driving distance (Miles): ");
		drivingDistance = keyboardInput.nextDouble();
		
		// prompt user to enter miles per gallon
		System.out.print("Enter miles per gallon: ");
		milesPerGallon = keyboardInput.nextDouble();
		
		// prompt user to enter price per gallon
		System.out.print("Enter price per gallon: ");
		pricePerGallon = keyboardInput.nextDouble();
		
		// If milesPerGallon is not zero, use drivingDistance, milesPerGallon, and pricePerGallon to calculate costOfDriving
		costOfDriving = (drivingDistance/milesPerGallon)*pricePerGallon;
		
		// display cost of driving
		System.out.printf("The cost of driving is: $%.2f", costOfDriving);
		
	}
}
