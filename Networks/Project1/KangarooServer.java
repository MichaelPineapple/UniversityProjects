/**** Kangaroo Server ****/

import java.util.*;
import java.net.*;
import java.io.*;

public class KangarooServer implements Runnable
{
    public static void println(String txt) { System.out.println(txt); }
    ServerSocket server;

    public KangarooServer(int port) throws IOException
    {
        server = new ServerSocket(port);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                Socket client = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintStream out = new PrintStream(client.getOutputStream());
                int input = Integer.parseInt(in.readLine());
                println("IN: " + input);
                String output = "THANK YOU";

                switch (input)
                {
                    case 1: output = linuxCmd("date"); 	break;
                    case 2: output = linuxCmd("uptime"); 	break;
                    case 3: output = linuxCmd("free"); 	break;
                    case 4: output = linuxCmd("netstat"); 	break;
                    case 5: output = linuxCmd("who"); 		break;
                    case 6: output = linuxCmd("ps"); 		break;
                }

                out.print(output);
                out.close();
                println("OUT: " + output);
            }
            catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        }
    }

    public String linuxCmd(String command)
    {
        String export = "<null>";
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new ProcessBuilder(command).start().getInputStream()));
            StringBuilder strBuild = new StringBuilder();
            String s;
            while ((s = in.readLine()) != null) strBuild.append(s).append("\n");
            export = strBuild.toString();
        }
        catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        return export;
    }

    public static void main(String[] args)
    {
        println("Kangaroo is now running...");
        if (args.length >= 1)
        {
            try
            {
                new KangarooServer(Integer.parseInt(args[0])).run();
            }
            catch (Exception ex) { println("ERROR: " + ex.getMessage()); }
        }
        else println("ERROR: No command line args!");
    }
}
