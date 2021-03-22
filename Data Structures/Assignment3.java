// Michael Wilson
// 2-10-18
// Assignment 3

import java.util.Scanner;

public class Assignment3
{
   public static void main(String[] args)
   {
      Scanner input = new Scanner(System.in);
      int list = 0, start = 0, skip = 0;
      boolean keepGoing = true;

      while(keepGoing)
      {
         System.out.print("Enter three positive integers or enter 'stop' to terminate the program: \n");
         
         if (input.hasNextInt())
         {
            list = input.nextInt();
            start = input.nextInt();
            skip = input.nextInt();
   
            try 
            {
               System.out.print(doTheThing(list, start, skip)+"\n"); 
            }
            catch (Exception ex)  
            {
               System.out.print("\nERROR\n");
            }
         }
         else if (input.next().equals("stop")) 
         {
            keepGoing = false;
            System.out.print("goodbye...");
         }
         else System.out.print("invalid input!\n");
      }
           
   }
   
   static int doTheThing(int list ,int start, int skip)
   {
      LinkedList linkedList = new LinkedList();

      for (int i = list; i > 0; i--) linkedList.insertFirst(i);
      Link curLink = linkedList.find(start);
      
      while (linkedList.getNum() > 1)
      {
         for (int i = 0; i < skip; i++) 
         {
            if (curLink.next == null) curLink = linkedList.getFirst();
            curLink = curLink.next;
         }
         linkedList.delete(curLink.iData);
      }
      
      return curLink.iData;
   }
   
}


class LinkedList
{
   private Link first;
   
   public LinkedList()
   {
      first = null;
   }
   
   public Link getFirst()
   {
    return first;
   }
   
   public void insertFirst(int data)
   {
      Link newLink = new Link(data);
      newLink.next = first;
      first = newLink;
   }
   
   public Link find(int key) // find link with given key
   { // (assumes non-empty list)
      Link current = first; // start at ‘first’
      while(current.iData != key) // while no match,
      {
         if(current.next == null) // if end of list,
         return null; // didn’t find it
         else // not end of list,
         current = current.next; // go to next link
      }
      return current; // found it
   }
   
   public Link delete(int key) // delete link with given key
   { // (assumes non-empty list)
      Link current = first; // search for link
      Link previous = first;
      while(current.iData != key)
      {
         if(current.next == null)
         return null; // didn’t find it
         else
         {
            previous = current; // go to next link
            current = current.next;
         }
      } // found it
      if(current == first) // if first link,
      first = first.next; // change first
      else // otherwise,
      previous.next = current.next; // bypass it
      return current;
    }
    
   public void displayList() // display the list
   {
      System.out.print("List (first-->last): ");
      Link current = first; // start at beginning of list
      while(current != null) // until end of list,
      {
         current.displayLink(); // print data
         current = current.next; // move to next link
      }
      System.out.println("");
   }
   
   public int getNum()
   {
       int count = 0;
       Link current = first; // start at beginning of list
       while(current != null) // until end of list,
       {
         count++;
         current = current.next; // move to next link
       }

      return count;
   }
   
}


class Link
{
   public int iData; // data
   public Link next; // reference to next link
   
   public Link(int data)
   {
      this.iData = data;
   }
   
   public void displayLink() // display ourself
   {
      System.out.print("{" + iData + "}");
   }   
}





