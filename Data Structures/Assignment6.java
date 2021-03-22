// Assignment 6
// Michael Wilson
// 4-7-18

import java.util.*;
import java.io.*;
import java.lang.*;

public class Assignment6
{
   public static void main(String[] args)
   {
   
      // open files
      Scanner fs1 = new Scanner(""), fs2 = new Scanner(""), fs3 = new Scanner("");
   
      //for (int i = 0; i < args.length; i++) print(args[i]+" ");
   
      try
      {
         File file1 = new File(args[0]), file2 = new File(args[1]), file3 = new File(args[2]);
         fs1 = new Scanner(file1);
         fs2 = new Scanner(file2);
         fs3 = new Scanner(file3);
      }
      catch (Exception ex)
      {
         print("\nFILE NOT FOUND!\n");
      }
      
      // task 1
      ArrayList<String> file1data = new ArrayList<String>(), file2data = new ArrayList<String>(), file3data = new ArrayList<String>();
      
      while (fs1.hasNext()) file1data.add(fs1.nextLine());
      while (fs2.hasNext()) file2data.add(fs2.nextLine());
      while (fs3.hasNext()) file3data.add(fs3.nextLine());
      
      // task 2
      int p = findNextPrime(file1data.size()*2);
      
      // task 3
      String[] A = new String[p], B = new String[p];
      
      // task 4
      
      print("INSERTION:\n\n~~~A~~~\nindex string probe length for insertion\n");
      int probeSum = 0;
      for (int i = 0; i < file1data.size(); i++)
      {
         int key = getHashVal(file1data.get(i), p);
         int probe = 0;
         while(A[key+probe] != null) probe++;
         A[key+probe] = file1data.get(i);
         print(key+probe+"\t\t"+file1data.get(i)+"\t\t\t"+probe+"\n");
         probeSum += probe;
      }
       
       print("average probe length: "+((double)probeSum/(double)file1data.size())+"\n\n~~~B~~~\nindex string probe length for insertion\n");
       probeSum = 0;
       
      for (int i = 0; i < file1data.size(); i++)
      {  
         int key = getHashVal(file1data.get(i), p);
         int yiff = 0;
      
         try
         {
            while(B[key+(yiff*yiff)] != null) 
            {
               yiff++;
               
               if (key+(yiff*yiff) > B.length)
               {
                  if (yiff*yiff > B.length) yiff = 0;
                  else key = 0;
               }
            }
            B[key+(yiff*yiff)] = file1data.get(i);
            print(key+(yiff*yiff)+"\t\t"+file1data.get(i)+"\t\t\t"+yiff+"\n");
            probeSum += yiff;
         }
         catch (Exception ex)
         {
            print("\nERROR!\n");
         }
      }
      
      // task 5
      
      print("average probe length: "+((double)probeSum/(double)file1data.size())+
      "\n\nSEARCH:\n\n~~~A~~~\nString   Success Failure Probe length for success  Probe length for failure\n");
      
      int probeSumFail = 0;
      probeSum = 0;
      
      for (int i = 0; i < file2data.size(); i++)
      {
         int key = getHashVal(file2data.get(i), p);
         int probe = 0;
         boolean good = true;
         while(A[key+probe] != null && good)
         {
            if (key+probe > file2data.size()) good = false;
            else probe++;
         }
         
         if (good)  probeSum += probe;
         else probeSumFail += probe;
         
         print(file2data.get(i)+"\t\t"+good+"\t\t"+!good+"\t\t\t"+probe+"\t\t\t"+probe+"\n");
        
      }
      
      
      print("average probe length for success: "+((double)probeSum/(double)file2data.size())+"\naverage probe length for failure: "+
      ((double)probeSumFail/(double)file2data.size())+"\n\n~~~B~~~\nString   Success Failure Probe length for success  Probe length for failure\n");
      
      probeSumFail = 0;
      probeSum = 0;
      
      for (int i = 0; i < file2data.size(); i++)
      {  
         int key = getHashVal(file2data.get(i), p);
         int yiff = 0;
         boolean good = true;
         try
         {
            while(B[key+(yiff*yiff)] != null && good) 
            {
               if (key+(yiff*yiff) > B.length) good = false;
               else yiff++;
            }           
         }
         catch (Exception ex)
         {
         }
         
         print(file2data.get(i)+"\t\t"+good+"\t\t"+!good+"\t\t\t"+yiff+"\t\t\t"+yiff+"\n");
         if (good)  probeSum += yiff;
         else probeSumFail += yiff;
         
      }

      
      // task 6
      
      print("average probe length for success: "+((double)probeSum/(double)file2data.size())+"\naverage probe length for failure: "+
      ((double)probeSumFail/(double)file2data.size())+"\n\nDELETE:\n\n~~~A~~~\nString   Success Failure Probe length for success  Probe length for failure\n");
      
      probeSumFail = 0;
      probeSum = 0;
      
      for (int i = 0; i < file3data.size(); i++)
      {
         int key = getHashVal(file3data.get(i), p);
         int probe = 0;
         boolean good = true;
         while(A[key+probe] != null && good)
         {
            if (key+probe > file3data.size()) good = false;
            else probe++;
         }
         if (good) A[key+probe] = null;
         
         if (good)  probeSum += probe;
         else probeSumFail += probe;
         
         print(file3data.get(i)+"\t\t"+good+"\t\t"+!good+"\t\t\t"+probe+"\t\t\t"+probe+"\n");
      }
      
      print("average probe length for success: "+((double)probeSum/(double)file3data.size())+"\naverage probe length for failure: "+
      ((double)probeSumFail/(double)file3data.size())+"\n\n~~~B~~~\nString   Success Failure Probe length for success  Probe length for failure\n");
      
      for (int i = 0; i < file3data.size(); i++)
      {  
         int key = getHashVal(file3data.get(i), p);
         int yiff = 0;
         boolean good = true;
         try
         {
            while(B[key+(yiff*yiff)] != null && good) 
            {
               yiff++;
               if (key+(yiff*yiff) > B.length) good = false;
            }
         }
         catch (Exception ex)
         {
         }
         
         print(file3data.get(i)+"\t\t"+good+"\t\t"+!good+"\t\t\t"+yiff+"\t\t\t"+yiff+"\n");
         if (good)  probeSum += yiff;
         else probeSumFail += yiff;
      }

      print("average probe length for success: "+((double)probeSum/(double)file3data.size())+
      "\naverage probe length for failure: "+((double)probeSumFail/(double)file3data.size()));
   }
   
   static int getHashVal(String str, int p)
   {
      int hashVal = 0;
      int yeet = str.length();
      for (int i = 0; i < str.length(); i++)
      {
         int letter = str.charAt(i) - 96;
         hashVal = (hashVal * 27 + letter) % p;
      }
      return hashVal;
   }
   
   static void print(String str)
   {
      System.out.printf(str);
   }
   
   static int findNextPrime(int n)
   {
       int nextPrime = n;
       boolean found = false;
       while (!found)
       {
           nextPrime++;
           if (isPrime(nextPrime)) found = true;
       }
       return nextPrime;
   }
   
   static boolean isPrime(int n)
   {
       for (int i = 2; i <= n/2; i++)  if (n % i == 0) return false;
       return true;
   }
      
}