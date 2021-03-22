// Michael Wilson
// 3-10-18
// Assignment 5

import java.util.*;
import java.io.*;
import java.lang.*;

public class Assignment5
{
   public static void main(String[] args)
   {
      String fileStr = "";
      String cleanData = "";
      Scanner fileScanner = new Scanner("[null]");
      int A = 0, B = 0, C = 0, D = 0, E = 0, F = 0, G = 0;
      
      try
      {
         File inputFile = new File(args[0]);
         fileScanner = new Scanner(inputFile);
      }
      catch (Exception ex)
      {
         print("\nFILE NOT FOUND!\n");
      }

      while (fileScanner.hasNextLine()) fileStr = fileStr.concat(fileScanner.nextLine());
      
      for (int i = 0; i < fileStr.length(); i++)
      {
         char cc = fileStr.charAt(i);
         if (cc == 'A') { A++; cleanData += "A "; }
         else if (cc == 'B') { B++; cleanData += "B "; }
         else if (cc == 'C') { C++; cleanData += "C ";}
         else if (cc == 'D') { D++; cleanData += "D ";}
         else if (cc == 'E') { E++; cleanData += "E ";}
         else if (cc == 'F') { F++; cleanData += "F ";}
         else if (cc == 'G') { G++; cleanData += "G ";}
      }
      
      // MAKE TREE
      Node nodeA = new Node(A, "A");
      Node nodeB = new Node(B, "B");
      Node nodeC = new Node(C, "C");
      Node nodeD = new Node(D, "D");
      Node nodeE = new Node(E, "E");
      Node nodeF = new Node(F, "F");
      Node nodeG = new Node(G, "G");
      
      Tree treeA = new Tree(nodeA);
      Tree treeB = new Tree(nodeB);
      Tree treeC = new Tree(nodeC);
      Tree treeD = new Tree(nodeD);
      Tree treeE = new Tree(nodeE);
      Tree treeF = new Tree(nodeF);
      Tree treeG = new Tree(nodeG);
  
      Comparator<Tree> treeComp = new TreeComparator();
      PriorityQueue<Tree> queue = new PriorityQueue(7, treeComp);
     
      queue.add(treeA);
      queue.add(treeB);
      queue.add(treeC);
      queue.add(treeD);
      queue.add(treeE);
      queue.add(treeF);
      queue.add(treeG);
      
      while(queue.size() > 1)
      {
         Node a = queue.poll().getRoot();
         Node b = queue.poll().getRoot();
         Node c = new Node(a.iData+b.iData, "");
         
         a.parent = c;
         b.parent = c;
         
         if (a.iData < b.iData)
         {
            a.zigzag = 0;
            b.zigzag = 1;
            c.leftChild = a;
            c.rightChild = b;   
         }
         else
         {
            a.zigzag = 1;
            b.zigzag = 0;
            c.leftChild = b;
            c.rightChild = a; 
         }
         
         Tree treeAB = new Tree(c);
         queue.add(treeAB);
      }
      
      Tree huff = queue.poll();
      
      // USE TREE TO MAKE CODES
      String[] ASCII = new String[7];
      ASCII[0] = huff.getPath(nodeA);
      ASCII[1] = huff.getPath(nodeB);
      ASCII[2] = huff.getPath(nodeC);
      ASCII[3] = huff.getPath(nodeD);
      ASCII[4] = huff.getPath(nodeE);
      ASCII[5] = huff.getPath(nodeF);
      ASCII[6] = huff.getPath(nodeG);
      
      String[] ASCII_B = new String[7];
      ASCII_B[0] = "A";
      ASCII_B[1] = "B";
      ASCII_B[2] = "C";
      ASCII_B[3] = "D";
      ASCII_B[4] = "E";
      ASCII_B[5] = "F";
      ASCII_B[6] = "G";
 
      // ENCODE DATA
      Scanner dataScanner = new Scanner(cleanData);
      String encodedData = "";
      while(dataScanner.hasNext())
      {
         String nextChar = dataScanner.next();
         if (nextChar.equals("A")) encodedData += ASCII[0];
         else if (nextChar.equals("B")) encodedData += ASCII[1];
         else if (nextChar.equals("C")) encodedData += ASCII[2];
         else if (nextChar.equals("D")) encodedData += ASCII[3];
         else if (nextChar.equals("E")) encodedData += ASCII[4];
         else if (nextChar.equals("F")) encodedData += ASCII[5];
         else if (nextChar.equals("G")) encodedData += ASCII[6];
      }

      // DECODE DATA
      String decodedData = "";
      Node currentNode = huff.getRoot();
      char[] dAta = encodedData.toCharArray();
      
      for (int j = 0; j < dAta.length; j++)
      {
         char q = dAta[j];
         if (q == '1') currentNode = currentNode.rightChild;
         else if (q == '0') currentNode = currentNode.leftChild;
         else  print(" ERR ");
         
         if (currentNode.cData != "")
         {
             decodedData += currentNode.cData;
            currentNode = huff.getRoot();
         }
      }

      // MENU
      print("\n~MENU~\na - display tree\nb - code table\nc - binary encoding\nd - decoded file\nx - end program\n");
      Scanner userInput = new Scanner(System.in);
      boolean run = true;
      
      while(run)
      {
         String input = userInput.next();
         
         if (input.equals("a"))
         {
            print("~HUFFMAN TREE~\n");
            huff.displayTree();
         }
         else if (input.equals("b"))
         {
            print("~CODE TABLE~\n");
            for (int i = 0; i < 7; i++) print(ASCII_B[i] + " - " + ASCII[i] + "\n");
         }
         else if (input.equals("c"))
         {
             print("~ENCODED DATA~\n");
             print(encodedData+"\n");
         }
         else if (input.equals("d"))
         {
            print("~DECODED DATA~\n");
            print(decodedData+"\n");
         }
         else if (input.equals("x"))
         {
            print("Terminating program...");
            run = false;
         }
         else
         {
            print("Invalid input!");
            print("\n~MENU~\na - display tree\nb - code table\nc - binary encoding\nd - decoded file\nx - end program\n");
         }
      }
      
          
   }
   
   static void print(String str)
   {
      System.out.printf(str);
   }
   
   
   
   
}

class TreeComparator implements Comparator<Tree>
{
    @Override
    public int compare(Tree x, Tree y)
    {
        if (x.getRoot().iData < y.getRoot().iData) return -1;
        if (x.getRoot().iData > y.getRoot().iData) return 1;
        return 0;
    }
}



// ---------------------------------------------

class Node
{
   public int iData; // data item (key)
   public double dData; // data item
   public String cData = "";
   public Node leftChild; // this node’s left child
   public Node rightChild; // this node’s right child
   public int zigzag = -1;
   public Node parent;
   public void displayNode() // display ourself
   {
      System.out.print("{");
      System.out.print(iData);
      System.out.print(", ");
      System.out.print(dData);
      System.out.print("} ");
   }
   public Node(int _id, String _c)
   {
      iData = _id;
      cData = _c;
   }
   public Node()
   {
   
   }
}

class Tree
{
   private Node root; // first node of tree
   
   public Tree() 
   {
      root = null;
   }
   
   public Tree(Node _root)
   {
      root = _root;  
   }
   
   public Node getRoot()
   {
      return root;
   }
     
   public String getPath(Node n)
   {
     String output = "";
     Node current = n;
     
     while (current.parent != null)
     {
        output = current.zigzag + output;
        current = current.parent;
     }
     return output; // found it
   }
   
   // INSERT
   public void insert(int id, String cd)
   {
      Node newNode = new Node(); // make new node
      newNode.iData = id; // insert data
      newNode.dData = 0;
      newNode.cData = cd;
      if(root==null) // no node in root
      root = newNode;
      else // root occupied
      {
      Node current = root; // start at root
      Node parent;
      while(true) // (exits internally)
      {
      parent = current;
      if(id < current.iData) // go left?
      {
      current = current.leftChild;
      if(current == null) // if end of the line,
      { // insert on left
      parent.leftChild = newNode;
      return;
      }
      } // end if go left
      else // or go right?
      {
      current = current.rightChild;
      if(current == null) // if end of the line
      { // insert on right
      parent.rightChild = newNode;
      return;
      }
      } // end else go right
      } // end while
      } // end else not root
   } // end insert()
   
   // DELETE
   public boolean delete(int key) // delete node with given key
   { // (assumes non-empty list)
      Node current = root;
      Node parent = root;
      boolean isLeftChild = true;
      while(current.iData != key) // search for node
      {
      parent = current;
      if(key < current.iData) // go left?
      {
      isLeftChild = true;
      current = current.leftChild;
      }
      else // or go right?
      {
      isLeftChild = false;
      current = current.rightChild;
      }
      if(current == null) // end of the line,
      return false; // didn’t find it
      } // end while
      // found node to delete
      // if no children, simply delete it
      if(current.leftChild==null &&
      current.rightChild==null)
      {
      if(current == root) // if root,
      root = null; // tree is empty
      else if(isLeftChild)
      parent.leftChild = null; // disconnect
      else // from parent
      parent.rightChild = null;
      }
      // if no right child, replace with left subtree
      else if(current.rightChild==null)
      if(current == root)
      root = current.leftChild;
      else if(isLeftChild)
      parent.leftChild = current.leftChild;
      else
      parent.rightChild = current.leftChild;
      // if no left child, replace with right subtree
      else if(current.leftChild==null)
      if(current == root)
      root = current.rightChild;
      else if(isLeftChild)
      parent.leftChild = current.rightChild;
      else
      parent.rightChild = current.rightChild;
      else // two children, so replace with inorder successor
      {
      // get successor of node to delete (current)
      Node successor = getSuccessor(current);
      // connect parent of current to successor instead
      if(current == root)
      root = successor;
      else if(isLeftChild)
      parent.leftChild = successor;
      else
      parent.rightChild = successor;
      // connect successor to current’s left child
      successor.leftChild = current.leftChild;
      } // end else two children
      // (successor cannot have a left child)
      return true; // success
   } // end delete()
   
   private Node getSuccessor(Node delNode)
   {
      Node successorParent = delNode;
      Node successor = delNode;
      Node current = delNode.rightChild; // go to right child
      while(current != null) // until no more
      { // left children,
      successorParent = successor;
      successor = current;
      current = current.leftChild; // go to left child
      }
      // if successor not
      if(successor != delNode.rightChild) // right child,
      { // make connections
      successorParent.leftChild = successor.rightChild;
      successor.rightChild = delNode.rightChild;
      }
      return successor;
   }
   
   public void traverse(int traverseType)
   {
      switch(traverseType)
      {
      case 1: System.out.print("\nPreorder traversal: ");
      preOrder(root);
      break;
      case 2: System.out.print("\nInorder traversal: ");
      inOrder(root);
      break;
      case 3: System.out.print("\nPostorder traversal: ");
      postOrder(root);
      break;
      }
      System.out.println();
   }
   
   private void preOrder(Node localRoot)
   {
      if(localRoot != null)
      {
      System.out.print(localRoot.iData + " ");
      preOrder(localRoot.leftChild);
      preOrder(localRoot.rightChild);
      }
   }
   
   private void inOrder(Node localRoot)
   {
      if(localRoot != null)
      {
      inOrder(localRoot.leftChild);
      System.out.print(localRoot.iData + " ");
      inOrder(localRoot.rightChild);
      }
   }

   private void postOrder(Node localRoot)
   {
      if(localRoot != null)
      {
      postOrder(localRoot.leftChild);
      postOrder(localRoot.rightChild);
      System.out.print(localRoot.iData + " ");
      }
   }
   
   public void displayTree()
   {
      Stack globalStack = new Stack();
      globalStack.push(root);
      int nBlanks = 32;
      boolean isRowEmpty = false;
      System.out.println(
      "......................................................");
      while(isRowEmpty==false)
      {
      Stack localStack = new Stack();
      isRowEmpty = true;
      for(int j=0; j<nBlanks; j++)
      System.out.print(" ");
      while(globalStack.isEmpty()==false)
      {
      Node temp = (Node)globalStack.pop();
      if(temp != null)
      {
      System.out.print("["+temp.cData+temp.iData+"]");
      localStack.push(temp.leftChild);
      localStack.push(temp.rightChild);
      if(temp.leftChild != null ||
      temp.rightChild != null)
      isRowEmpty = false;
      }
      else
      {
      System.out.print("--");
      localStack.push(null);
      localStack.push(null);
      }
      for(int j=0; j<nBlanks*2-2; j++)
      System.out.print(" ");
      } // end while globalStack not empty
      System.out.println();
      nBlanks /= 2;
      while(localStack.isEmpty()==false)
      globalStack.push( localStack.pop() );
      } // end while isRowEmpty is false
      System.out.println(
      "......................................................");
   } // end displayTree()
   
}