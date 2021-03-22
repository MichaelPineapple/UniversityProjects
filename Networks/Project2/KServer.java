/**** Kangaroo Server ****/

import java.util.*;
import java.net.*;
import java.io.*;

public class KServer implements Runnable
{
    Socket client;

    public KServer(Socket _sock) throws Exception
    {
        client = _sock;
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintStream out = new PrintStream(client.getOutputStream());
            int input = Integer.parseInt(in.readLine());
            println("IN: " + input);
            String output = "THANK YOU";
            switch (input)
            {
                case 1:
                    output = linuxCmd("date");
                    break;
                case 2:
                    output = linuxCmd("uptime");
                    break;
                case 3:
                    output = linuxCmd("free");
                    break;
                case 4:
                    output = linuxCmd("netstat");
                    break;
                case 5:
                    output = linuxCmd("who");
                    break;
                case 6:
                    output = linuxCmd("ps -e");
                    break;
            }
            out.print(output);
            out.close();
            println("OUT: " + output);
            client.close();
        }
        catch (Exception ex) { println("ERROR:" + ex.getMessage()); }
    }

    public String linuxCmd(String command)
    {
        String $return = "<null>";
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new ProcessBuilder(command.split(" ")).start().getInputStream()));
            StringBuilder strBuild = new StringBuilder();
            String s;
            while ((s = in.readLine()) != null) strBuild.append(s).append("\n");
            $return = strBuild.toString();
        }
        catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        return $return;
    }


    ////// STATIC //////

    public static void println(String txt) { System.out.println(txt); }

    public static void main(String[] args)
    {
        println("Kangaroo started.");
        if (args.length >= 1)
        {
            try
            {
                int port = Integer.parseInt(args[0]);
                ServerSocket server = new ServerSocket(port);
                println("Kangaroo is now running...");
                while (true)  new Thread(new KServer(server.accept())).start();
            }
            catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        }
        else println("ERROR: No command line args!");
    }
}
