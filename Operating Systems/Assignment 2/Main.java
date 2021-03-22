import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            if (args.length > 1)
            {
                // Process Input
                ArrayList<Integer> resourcesList = new ArrayList<>();
                for (int i = 1; i < args.length; i++)
                {
                    try { resourcesList.add(Integer.parseInt(args[i])); }
                    catch (Exception ex) { print("\nInvalid argument!"); }
                }
                ArrayList<ArrayList<Integer>> fileData = readFile(args[0]);
                int[] resources = convertToArray(resourcesList);

                // Initialize Bank
                BANK = new Bank(resources);
                for (int customerNum = 0; customerNum < fileData.size(); customerNum++)
                {
                    ArrayList<Integer> demands = fileData.get(customerNum);
                    BANK.addCustomer(customerNum, convertToArray(demands));
                }

                // Command Line Interface
                boolean loop = true;
                while (loop)
                {
                    Scanner uiScan = new Scanner(System.in);
                    print("\nEnter Command:\n");
                    loop = processCommand(uiScan.nextLine());
                    print("\n\n");
                }
            }
            else print("\nNo resources specified!");
        }
        else print("\nNo input file specified!");
    }


    /* THE BANK */
    static Bank BANK;
    static class Bank
    {
        private int numberOfCustomers;
        private int numberOfResources;
        private int[] resources;
        private int[] available;
        private int[][] maximum;
        private int[][] allocation;
        private int[][] need;

        Bank(int... _initialAvailability)
        {
            numberOfResources = _initialAvailability.length;
            resources = new int[numberOfResources];
            available = new int[numberOfResources];
            for (int i = 0; i < numberOfResources; i++)
            {
                int x = _initialAvailability[i];
                resources[i] = x;
                available[i] = x;
            }
        }

        void addCustomer(int _customerNum, int[] _maximumDemand)
        {
            if (_maximumDemand.length == numberOfResources)
            {
                if (lessThanOrEqual(_maximumDemand, resources))
                {
                    numberOfCustomers++;
                    maximum = addToMatrix(maximum, _maximumDemand);
                    need = addToMatrix(need, _maximumDemand);
                    allocation = addToMatrix(allocation, new int[numberOfResources]);
                }
                else print("\nError: Maximum demand of customer " + _customerNum + " exceeds resource limits!");
            }
            else print("\nError, invalid number of resources!");
        }

        public String toString()
        {
            String output = "";
            output += ("\n~BANK~");
            output += ("\nresources  = " + intArrayToStr(resources));
            output += ("\navailable  = " + intArrayToStr(available));
            output += ("\nmaximum    = " + int2dArrayToStr(maximum));
            output += ("\nallocation = " + int2dArrayToStr(allocation));
            output += ("\nneed       = " + int2dArrayToStr(need));
            return output;
        }

        boolean requestResources(int _customerNumber, int[] _request)
        {
            boolean output = false;

            if (_customerNumber < numberOfCustomers && _customerNumber >= 0)
            {
                if (lessThanOrEqual(_request, need[_customerNumber]))
                {
                    if (lessThanOrEqual(_request, available))
                    {
                        int[] rollback_available = available.clone();
                        int[][] rollback_maximum = maximum.clone();
                        int[][] rollback_allocation = allocation.clone();
                        int[][] rollback_need = need.clone();

                        available = subtractArray(available, _request);
                        allocation[_customerNumber] = addArray(allocation[_customerNumber], _request);
                        need[_customerNumber] = subtractArray(need[_customerNumber], _request);

                        if (isSafe()) output = true;
                        else
                        {
                            available = rollback_available.clone();
                            maximum = rollback_maximum.clone();
                            allocation = rollback_allocation.clone();
                            need = rollback_need.clone();
                        }
                    }
                }
            }

            return output;
        }

        void releaseResources(int _customerNumber, int[] _release)
        {
            if (_customerNumber < numberOfCustomers && _customerNumber >= 0)
            {
                int[] tmp;
                if (lessThanOrEqual(_release, allocation[_customerNumber]))  tmp = _release;
                else tmp = allocation[_customerNumber];
                available = addArray(available, tmp);
                allocation[_customerNumber] = subtractArray(allocation[_customerNumber], tmp);
                need[_customerNumber] = addArray(need[_customerNumber], tmp);
            }
            else print("\nERROR: Invalid customer number!");
        }


        public int[] getSafeSequence()
        {
            ArrayList<Integer> path = new ArrayList<>();
            int[] output = null;

            boolean loop = true;
            int[] work = available.clone();
            boolean[] finish = new boolean[numberOfCustomers];

            int q = 0;
            while (loop)
            {
                int index = -1;
                for (int i = q; i < numberOfCustomers; i++)
                {
                    if (!finish[i] && lessThanOrEqual(need[i], work))
                    {
                        index = i;
                        q = index;
                        if (q >= numberOfCustomers-1) q = 0;
                        i = numberOfCustomers;
                    }
                }

                if (index > -1)
                {
                    work = addArray(work, allocation[index]);
                    finish[index] = true;
                    path.add(index);
                }
                else
                {
                    boolean allFinished = true;
                    for (boolean b : finish)
                    {
                        if (!b) allFinished = false;
                    }
                    if (allFinished) output = convertToArray(path);
                    loop = false;
                }
            }

            return output;
        }

        private boolean isSafe()
        {
            return (getSafeSequence() != null);
        }

        void releaseAll(int _customerNumber)
        {
            releaseResources(_customerNumber, maximum[_customerNumber]);
        }

        public int[] getNeed(int _customerNum)
        {
            return need[_customerNum];
        }

        void clearNeed(int _customerNum)
        {
            need[_customerNum] = new int[numberOfResources];
        }
    }








    // ~~~~ USER INTERFACE ~~~~

    enum CMD
    {
        UNKNOWN,
        QUIT,
        HELP,
        SAFESEQ,
        DISPLAY,
        REQUEST,
        REQUESTALL,
        RELEASE,
        RELEASEALL,
        EXECUTE,
    }


    static boolean processCommand(String _userInput)
    {
        String[] uiArgs = _userInput.split(" ");
        CMD command = CMD.UNKNOWN;
        int pid = -1;
        ArrayList<Integer> resList = new ArrayList<>();

        for (int i = 0; i < uiArgs.length; i++)
        {
            String arg = uiArgs[i].toUpperCase();

            if (i == 0)
            {
                if (arg.equals("QUIT")) command = CMD.QUIT;
                else if (arg.equals("SAFESEQ")) command = CMD.SAFESEQ;
                else if (arg.equals("HELP")) command = CMD.HELP;
                else if (arg.equals("BANK")) command = CMD.DISPLAY;
                else
                {
                    try { pid = Integer.parseInt(arg); }
                    catch (Exception ex) { command = CMD.UNKNOWN; }
                }
            }
            else if (i == 1)
            {
                if (arg.equals("REQUEST")) command = CMD.REQUEST;
                if (arg.equals("REQUESTALL")) command = CMD.REQUESTALL;
                else if (arg.equals("RELEASE")) command = CMD.RELEASE;
                else if (arg.equals("RELEASEALL")) command = CMD.RELEASEALL;
                else if (arg.equals("EXECUTE")) command = CMD.EXECUTE;
            }
            else if (i > 1)
            {
                try { resList.add(Integer.parseInt(arg)); }
                catch (Exception ex) { command = CMD.UNKNOWN; }
            }
            else command = CMD.UNKNOWN;
        }

        boolean output = true;
        int[] res = convertToArray(resList);
        switch (command)
        {
            case UNKNOWN: print("Unknown command! (Try using 'HELP')"); break;
            case QUIT: print("Quitting..."); output = false; break;
            case HELP: CMD_HELP(); break;
            case SAFESEQ: CMD_SAFESEQ(); break;
            case DISPLAY: CMD_DISPLAY(); break;
            case EXECUTE: CMD_EXECUTE(pid); break;
            case RELEASE: CMD_RELEASE(pid, res); break;
            case RELEASEALL: CMD_RELEASEALL(pid); break;
            case REQUEST: CMD_REQUEST(pid, res); break;
            case REQUESTALL: CMD_REQUESTALL(pid); break;
            default: print("An unexpected error occurred!");
        }

        return output;
    }


    // ~~ COMMANDS ~~

    // 'HELP' Command
    static void CMD_HELP()
    {
        print("\n~HELP~" +
                "\nHELP                              - Display help" +
                "\nQUIT                              - Exit the program" +
                "\nSAFESEQ                           - Calculate and print safe Sequence" +
                "\nBANK                              - Show bank state" +
                "\n<P> EXECUTE                       - Execute process 'P'" +
                "\n<P> RELEASE <R0> <R1> ... <Rn>    - Release resources 'R0' ... 'Rn' from process 'P'" +
                "\n<P> RELEASEALL                    - Release all resources from process 'P'" +
                "\n<P> REQUEST <R0> <R1> ... <Rn>    - Request resources 'R0' ... 'Rn' for process 'P'" +
                "\n<P> REQUESTALL                    - Request all needed resources for process 'P'");
    }

    // 'BANK' Command
    static void CMD_DISPLAY()
    {
        print(BANK.toString());
    }

    // 'SAFESEQ' Command
    static int[] CMD_SAFESEQ()
    {
        int[] seq = BANK.getSafeSequence();
        if (seq == null || seq.length < 1) print("No Safe Sequence Found!");
        else print("Safe Sequence: "+intArrayToStr(seq));
        return seq;
    }

    // 'REQUEST' Command
    static void CMD_REQUEST(int _pid, int... _res)
    {
        if (_pid > -1)
        {
            if (_res.length > 0)
            {
                if (BANK.requestResources(_pid, _res)) print("Request Granted!");
                else print("Request Denied!");
            }
            else print(_INVALID_CMD_MSG_);
        }
        else print(_INVALID_CMD_MSG_);
    }

    // 'REQUESTALL' Command
    static void CMD_REQUESTALL(int _pid)
    {
        if (_pid > -1)
        {
            if (BANK.requestResources(_pid, BANK.getNeed(_pid))) print("Request Granted!");
            else print("Request Denied!");
        }
        else print(_INVALID_CMD_MSG_);
    }

    // 'RELEASE' Command
    static void CMD_RELEASE(int _pid, int... _res)
    {
        if (_pid > -1)
        {
            if (_res.length > 0)
            {
                BANK.releaseResources(_pid, _res);
                print("Resources Released!");
            }
            else print(_INVALID_CMD_MSG_);
        }
        else print(_INVALID_CMD_MSG_);
    }


    // 'RELEASEALL' Command
    static void CMD_RELEASEALL(int _pid)
    {
        if (_pid > -1)
        {
            BANK.releaseAll(_pid);
            print("Resources Released!");
        }
        else print(_INVALID_CMD_MSG_);
    }

    // 'EXECUTE' Command
    static void CMD_EXECUTE(int _pid)
    {
        if (_pid > -1)
        {
            if (BANK.requestResources(_pid, BANK.getNeed(_pid)))
            {
                BANK.releaseAll(_pid);
                BANK.clearNeed(_pid);
                print("P" + _pid + " Executed");
            }
            else print("Request Denied!");
        }
        else print(_INVALID_CMD_MSG_);
    }


































    // ~~~~ TOOLS ~~~~

    static final String _INVALID_CMD_MSG_ = "Invalid command! (Try using 'HELP')";

    static int[][] addToMatrix(int[][] _matrix, int[] _vector)
    {
        int[][] tmp = new int[0][0];
        if (_matrix != null) tmp = _matrix.clone();
        int[][] ouput = new int[tmp.length+1][_vector.length];
        for (int i = 0; i < tmp.length; i++) ouput[i] = tmp[i].clone();
        ouput[ouput.length - 1] = _vector.clone();
        return ouput;
    }

    static boolean lessThanOrEqual(int[] _vectorA, int[] _vectorB)
    {
        boolean output = true;
        for (int i = 0; i < _vectorA.length; i++)
        {
            if (_vectorA[i] > _vectorB[i]) output = false;
        }
        return output;
    }

    static int[] subtractArray(int[] _arrayA, int[] _arrayB)
    {
        int[] output = new int[_arrayA.length];
        for (int i = 0; i < output.length; i++)
        {
            int tmp = 0;
            if (i < _arrayB.length) tmp = _arrayB[i];
            output[i] = _arrayA[i] - tmp;
        }
        return output;
    }

    static int[] addArray(int[] _arrayA, int[] _arrayB)
    {
        int[] output = new int[_arrayA.length];
        for (int i = 0; i < output.length; i++)
        {
            int tmp = 0;
            if (i < _arrayB.length) tmp = _arrayB[i];
            output[i] = _arrayA[i] + tmp;
        }
        return output;
    }

    static void print(String _str)
    {
        System.out.print(_str);
    }

    static int[] convertToArray(ArrayList<Integer> _list)
    {
        int[] output = new int[_list.size()];
        for (int i = 0; i < _list.size(); i++) output[i] = _list.get(i);
        return output;
    }

    static String int2dArrayToStr(int[][] _array)
    {
        String output = "[";
        if (_array != null)
        {
            for (int[] a : _array) output += (intArrayToStr(a) + ", ");
            output = output.substring(0, output.length() - 2) + "]";
        }
        else output = "null";
        return output;
    }

    static String intArrayToStr(int[] _array)
    {
        String output = "[";
        if (_array != null)
        {
            for (int i : _array) output += (i + ",");
            output = output.substring(0, output.length() - 1) + "]";
        }
        else output = "null";
        return output;
    }

    static ArrayList<ArrayList<Integer>> readFile(String _filename)
    {
        ArrayList<ArrayList<Integer>> output = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(new File(_filename));
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                ArrayList<Integer> tmp2 = new ArrayList<>();
                String[] split = line.split(",");
                for (String s2 : split) tmp2.add(Integer.parseInt(s2));
                output.add(tmp2);
            }
        }
        catch (Exception ex) { print("\n Error reading file!"); }
        return output;
    }
}

/* ========= LEGACY ========= */


    /*
    static class BankCommand
    {
        private CMD command = CMD.UNKNOWN;
        private int pid = -1;
        private ArrayList<Integer> res = new ArrayList<>();

        public BankCommand(CMD _command, int _pid, ArrayList<Integer> _res)
        {
            this.command = _command;
            this.pid = _pid;
            this.res = _res;
        }

        public CMD getCmd() { return this.command; }
        public int getPid() { return this.pid; }
        public ArrayList<Integer> getRes() { return this.res; }
    }

     */


    /*
    static boolean executeCommand(BankCommand _cmd)
    {
        boolean output = true;
        int pid = _cmd.getPid();
        int[] res = convertToArray(_cmd.getRes());


        switch (_cmd.getCmd())
        {
            case UNKNOWN: print("Unknown command! (Try using 'HELP')"); break;
            case QUIT: print("Quitting..."); output = false; break;
            case HELP: CMD_HELP(); break;
            case SAFESEQ: CMD_SAFESEQ(); break;
            case DISPLAY: CMD_DISPLAY(); break;
            case DEMO: CMD_DEMO(); break;
            case EXECUTE: CMD_EXECUTE(pid); break;
            case RELEASE: CMD_RELEASE(pid, res); break;
            case RELEASEALL: CMD_RELEASEALL(pid); break;
            case REQUEST: CMD_REQUEST(pid, res); break;
            case REQUESTALL: CMD_REQUESTALL(pid); break;
            default: print("An unexpected error occurred!");
        }
        return output;
    }

     */
