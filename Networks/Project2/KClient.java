/**** Kangaroo Client ****/

import java.util.*;
import java.net.*;
import java.io.*;

public class KClient
{
    public static void println(String txt) { System.out.println(txt); }
    static String host;
    static int cmd, port, goodBoyes;
    static long totalReplyTime, startTime;

    static class ClientBoi implements Runnable
    {
        public void run()
        {
            try
            {
                // *** Connect To Server And Send Request ***
                long timeA = System.nanoTime();
                Socket echoSocket = new Socket(host, port);
                PrintWriter write = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader read = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                String inputLine = "", inputTot = "";
                long timeB = System.nanoTime() - timeA;
                write.println(cmd);
                while ((inputLine = read.readLine()) != null) inputTot += "\n" + inputLine;
                totalReplyTime += ((System.nanoTime() - startTime) - timeB);
                println("\n" + inputTot + "\n");
                echoSocket.close();
                goodBoyes++;
            }
            catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        }
    }

    public static void main(String[] args)
    {
        println("Welcome to Kangaroo Client!");
        if (args.length >= 3)
        {
            try
            {
                // *** Get Info From Command Line Arguments ***
                host = args[0];
                port = Integer.parseInt(args[1]);
                int clientNum = Integer.parseInt(args[2]);
                println("Host: " + host + "\nPort: " + port + "\nClient Count:" + clientNum);

                try
                {
                    // *** Initialize Client Thread List ***
                    ArrayList<Thread> clientTheads = new ArrayList<>();

                    // *** User Menu ***
                    Scanner scan = new Scanner(System.in);
                    boolean loop = true;
                    while (loop)
                    {
                        println("\n1. Host current Date and Time" +
                                "\n2. Host uptime" +
                                "\n3. Host memory use" +
                                "\n4. Host Netstat" +
                                "\n5. Host current users" +
                                "\n6. Host running processes" +
                                "\n7. Quit");

                        println("\nEnter your selection: ");
                        int userInput = -1;
                        try { userInput = Integer.parseInt(scan.nextLine()); } catch (Exception ex) { }
                        if (userInput < 1 || userInput > 7) println("Invalid input! Please try again.");
                        else if (userInput == 7) loop = false;
                        else
                        {
                            // *** Send Requests ***
                            totalReplyTime = 0;
                            goodBoyes = 0;
                            cmd = userInput;
                            clientTheads.clear();
                            for (int i = 0; i < clientNum; i++) { clientTheads.add(new Thread(new ClientBoi())); }
                            startTime = System.nanoTime();
                            for (Thread c : clientTheads) c.run();
                            while (goodBoyes < clientNum) { }
                            long avgReplyTime = (totalReplyTime) / (long) clientNum;
                            println("\nAvg Response Time: " + avgReplyTime / (1E6) + " msec");
                        }
                    }
                }
                catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
            }
            catch (Exception ex) { println("ERROR: Invalid arguments!"); }
        }
        else println("ERROR: No command line argument!");

        println("Goodbye...\n");
    }
}
