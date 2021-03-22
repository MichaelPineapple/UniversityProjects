// Michael Wilson
// 2-24-18
// Assignment 4 
// Data Structures

import java.util.*;

class Assignment4
{
   public static void main(String[] args)
   {
      Scanner input = new Scanner(System.in);
      System.out.printf("Enter values: ");
      ArrayList<Integer> inputArray = new ArrayList<Integer>();
      String inputString = input.nextLine();
      Scanner read = new Scanner(inputString);
      int first = read.nextInt();
      while (read.hasNextInt()) inputArray.add(read.nextInt());
      int[] ary = new int[inputArray.size()];
      for (int i = 0; i < inputArray.size(); i++) ary[i] = inputArray.get(i);
      knapsack(first, new int[0], 0, ary, inputArray.size());
   }
   
   public static void knapsack(int total, int[] picks, int pickCount, int[] bag, int bagCount)
   {
         if (total == 0)
         {
            System.out.printf("\nSolution:");
            for (int i = 0; i < pickCount; i++)  System.out.printf(" " + picks[i] + " ");
         }
         else if (total > 0 && bagCount > 0)
         {
            int nextPick = bag[0];
            int[] nextBagArray = new int[bagCount-1];
            int[] nextPickArray = new int[pickCount+1];
            
            for (int i = 1; i < bagCount; i++) nextBagArray[i-1] = bag[i];
            for (int i = 0; i < pickCount; i++) nextPickArray[i] = picks[i];
            nextPickArray[pickCount] = nextPick;
         
            knapsack(total - nextPick, nextPickArray, pickCount + 1, nextBagArray, bagCount -1);
            knapsack(total, picks, pickCount, nextBagArray, bagCount -1);
         }
   }
   
}