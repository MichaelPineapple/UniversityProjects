import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;

public class Assembler
{
    // VARIABLES =======================================================================================================
    static Instruction NULL_INSTRUCTION = new Instruction("[NULL]", "NN", -1);
    static ArrayList<Instruction> INSTRUCTION_LIST = new ArrayList<>();
    static String kek = "[kek]";
    static String ERROR_STRING = "";

    // METHODS =========================================================================================================
    static void printError(String str)
    {
        ERROR_STRING += ("ERROR: " + str + " ! \n");
    }


    static String convertToHex(int dec)
    {
        return Integer.toHexString(dec).toUpperCase();
    }

    static int convertToDec(String hex)
    {
        return Integer.parseInt(hex, 16);
    }

    static int convertToDecFromBin(String bin)
    {
        return Integer.parseInt(bin, 2);
    }

    static String padStr(String padding, String str)
    {
        return (padding + str).substring(str.length());
    }

    static String convertToAddress(int addr)
    {
        return padStr("00000", convertToHex(addr));
    }
    static String convertToAddress2(int addr)
    {
        return padStr("000000", convertToHex(addr));
    }

    static Instruction searchInstruction(String _key)
    {
        Instruction exp = NULL_INSTRUCTION;
        for (int i = 0; i < INSTRUCTION_LIST.size(); i++) if (INSTRUCTION_LIST.get(i).mnemonic.equals(_key)) exp = INSTRUCTION_LIST.get(i);
        return exp;
    }

    static int checkForDuplicate(String key, Entry[] hashTable)
    {
        int export = -1, insertionIndex = hash(key.toLowerCase(), hashTable.length);
        boolean loop = true;
        while (loop)
        {
            if (insertionIndex > hashTable.length - 1) insertionIndex = 0;
            if (hashTable[insertionIndex] == null) loop = false;
            else
            {
                if (hashTable[insertionIndex].str.equals(key)) export = insertionIndex;
                insertionIndex++;
            }
        }
        return export;
    }

    static void probe(Entry data, Entry[] hashTable)
    {
        int insertionIndex = hash(data.str, hashTable.length);
        boolean loop = true;
        while (loop)
        {
            if (insertionIndex > hashTable.length - 1) insertionIndex = 0;
            if (hashTable[insertionIndex] == null)
            {
                hashTable[insertionIndex] = data;
                loop = false;
            }
            else insertionIndex++;
        }
    }

    static int hash(String key, int tableSize)
    {
        key = key.toLowerCase();
        int hashVal = 0;
        for (int i = 0; i < key.length(); i++)
        {
            int letter = key.charAt(i) - 96;
            hashVal = (hashVal * 27 + letter) % tableSize;
        }
        return hashVal;
    }

    // CLASSES =========================================================================================================
    static class Nicholas
    {
        int address = -1;
        int bytes = 0;
        boolean extended;
        boolean skip;
        char prefix;
        String label, operand, original;
        Instruction instruct;

        public Nicholas(String _label, boolean _extended, Instruction _instruct, char _prefix, String _operand, String _original)
        {
            extended = _extended;
            prefix = _prefix;
            label = _label;
            operand = _operand;
            instruct = _instruct;
            original = _original;
            skip = false;
        }
        public Nicholas(String _original)
        {
            extended = false;
            prefix = 'x';
            label = "[null]";
            operand = "[null]";
            instruct = NULL_INSTRUCTION;
            original = _original;
            skip = true;
        }

        String getHexAddress()
        {
            return convertToAddress(address);
        }

    }

    static class Instruction
    {
        int format;
        String mnemonic, opCode;

        public Instruction(String _mnemonic, String _opCode, int _format)
        {
            mnemonic = _mnemonic;
            opCode = _opCode;
            format = _format;
        }
    }

    static class Entry
    {
        String str;
        int val;

        public Entry(String _str, int _val)
        {
            str = _str;
            val = _val;
        }

        String getHexVal()
        {
            return convertToAddress(val);
        }


    }




















    // MAIN ============================================================================================================
    public static void main(String[] args)
    {
        // LOAD --------------------------------------------------------------------------------------------------------
        String filename = "[NULL]";
        try
        {
            filename = args[0];
        }
        catch (Exception ex)
        {
            printError("ERROR: No command line input!");
        }

        LOAD_INSTRUCTIONS();

        ArrayList<Nicholas> nickList = new ArrayList<>();
        try
        {
            FileReader reader = new FileReader(new File(filename));
            BufferedReader fb = new BufferedReader(reader);
            String line;
            while ((line = fb.readLine()) != null)
            {
                String save = line;
                line = line.toUpperCase();
                String label = "", mnemonic = "", operand = "";
                char prefix = ' ';
                boolean extended = false;

                for (int i = 0; i < line.length(); i++)
                {
                    char cursor = line.charAt(i);
                    if (i > 30) break;
                    else if (cursor == '.') break;
                    else if (i >= 0 && i <= 7) label += cursor;
                    else if (i == 9 && cursor == '+') extended = true;
                    else if (i >= 10 && i <= 16) mnemonic += cursor;
                    else if (i == 18) prefix = cursor;
                    else if (i >= 19 && i <= 28) operand += cursor;
                }

                label = label.trim();
                mnemonic = mnemonic.trim();
                operand = operand.trim();

                boolean asai = false;
                Instruction tempInst = NULL_INSTRUCTION;

                if (mnemonic.length() > 0)
                {
                    tempInst = searchInstruction(mnemonic);
                    if (tempInst == NULL_INSTRUCTION) printError("Invalid mnemonic '" + mnemonic + "'");
                    else
                    {
                        if (!(label.length() == 0 && operand.length() == 0)) asai = true;
                    }
                }

                if (asai) nickList.add(new Nicholas(label, extended, tempInst, prefix, operand, save));
                else nickList.add(new Nicholas(save));

            }
        }
        catch (Exception ex)
        {
            printError("File not found");
        }

        // PASS 1 ------------------------------------------------------------------------------------------------------
        int curAddress = 0;
        int startAddress = 0;
        int craig = 0, jimbo = 0;
        ArrayList<Entry> dataBuffer = new ArrayList<>();
        for (int i = 0; i < nickList.size(); i++)
        {
            try
            {
                Nicholas nick = nickList.get(i);
                if (!nick.skip)
                {
                    if (nick.instruct.mnemonic.equals("START"))
                    {
                        startAddress = convertToDec(nick.operand);
                        curAddress = startAddress;
                    }
                    nick.address = curAddress;
                    if (nick.label.length() > 0) dataBuffer.add(new Entry(nick.label, nick.address));
                    int addressDelta = nick.instruct.format;
                    if (nick.extended) addressDelta = 4;
                    if (nick.instruct.mnemonic.equals("RESW"))
                    {
                        addressDelta = 3 * (Integer.parseInt(nick.operand));
                        craig++;
                    }
                    if (nick.instruct.mnemonic.equals("RESB"))
                    {
                        addressDelta = (convertToDec(nick.operand));
                        craig++;
                    }
                    nick.bytes = addressDelta;
                    curAddress += addressDelta;
                }
            }
            catch (Exception ex)
            {
                printError("Unknown error in pass 1");
            }
        }

        Entry[] hashArray = new Entry[2 * dataBuffer.size()];
        for (int i = 0; i < dataBuffer.size(); i++)
        {
            try
            {
                if (checkForDuplicate(dataBuffer.get(i).str, hashArray) == -1) probe(dataBuffer.get(i), hashArray);
                else printError("Duplicate label '" + dataBuffer.get(i).str + "'");
            }
            catch (Exception ex)
            {
                printError("Unknown error in hash table");
            }
        }


        String lstStr = "";
        String objStr = "";

        // PASS 2 ------------------------------------------------------------------------------------------------------
        int BASE = -1;
        String strFormat = "%-4s %-5s %-10s \t %s \n";
        lstStr += String.format(strFormat, "", "Loc", "Object Code", "Source Code");
        lstStr += String.format(strFormat, "", "-----", "-----------", "-----------");
        for (int j = 0; j < nickList.size(); j++)
        {
            try
            {
                Nicholas nick = nickList.get(j);
                if (!nick.skip)
                {
                    String curCode = "??????";
                    String jucc = "000";
                    char plus = ' ';
                    int displacement = 0;
                    int n = 1, i = 1, x = 0, b = 0, p = 0, e = 0;
                    if (nick.operand.contains(",X"))
                    {
                        x = 1;
                        nick.operand = nick.operand.substring(0, nick.operand.indexOf(','));
                    }
                    if (nick.prefix == '#')
                    {
                        n = 0;
                        i = 1;
                    }
                    if (nick.prefix == '@')
                    {
                        n = 1;
                        i = 0;
                    }

                    boolean isValue = Character.isDigit(nick.operand.charAt(0));

                    int targetAddress = 0;
                    if (nick.operand.length() > 0)
                    {
                        if (isValue)
                        {
                            targetAddress = Integer.parseInt(nick.operand);
                        }
                        else
                        {
                            int boi = checkForDuplicate(nick.operand, hashArray);
                            if (boi == -1) printError("Reference to undefined label '" + nick.operand + "'");
                            else targetAddress = hashArray[boi].val;
                        }
                    }
                    else printError("No label provided at line " + i);

                    if (nick.instruct.opCode.equals(kek))
                    {
                        curCode = "";
                        if (nick.instruct.mnemonic.equals("WORD"))
                        {
                            curCode = padStr("000000", convertToHex(targetAddress));
                            objStr += curCode + "\n";
                        }
                        else if (nick.instruct.mnemonic.equals("BYTE"))
                        {
                            curCode = convertToHex(Integer.parseInt(nick.operand));
                            objStr += curCode + "\n";
                        }
                        else if (nick.instruct.mnemonic.equals("RESW") || nick.instruct.mnemonic.equals("RESB"))
                        {
                            jimbo++;
                            objStr += "!\n" + convertToAddress2(nick.address + nick.bytes) + "\n";
                            if (jimbo == craig) objStr += convertToAddress2(startAddress);
                            else objStr += "000000";
                            objStr += "\n";
                        }
                        else if (nick.instruct.mnemonic.equals("END")) objStr += "!";
                        else if (nick.instruct.mnemonic.equals("START")) objStr += convertToAddress2(startAddress) + "\n000000\n";
                    }
                    else
                    {
                        if (nick.instruct.mnemonic.equals("BASE"))
                        {
                            BASE = targetAddress;
                            curCode = "";
                        }
                        else
                        {
                            int programCounter = nick.address + nick.bytes;

                            if (nick.extended)
                            {
                                e = 1;
                                p = 0;
                                b = 0;
                                plus = '+';
                                displacement = targetAddress;
                                jucc = "00000";
                            }
                            else
                            {
                                e = 0;
                                p = 1;
                                b = 0;
                                if (isValue) displacement = targetAddress;
                                else
                                {
                                    displacement = targetAddress - programCounter;
                                    if (displacement < -2047 || displacement > 2048)
                                    {
                                        if (BASE == -1) printError("Base undeclared");
                                        else
                                        {
                                            e = 0;
                                            p = 0;
                                            b = 1;
                                            displacement = targetAddress - BASE;
                                        }
                                    }
                                }

                            }


                            curCode = (padStr("00", convertToHex(convertToDec(nick.instruct.opCode) + convertToDecFromBin((n + "") + (i + ""))))) + convertToHex(convertToDecFromBin((x + "") + (b + "") + (p + "") + (e + ""))) + padStr(jucc, convertToHex(displacement));

                            objStr += curCode + "\n";

                        }
                    }


                    //lstStr += String.format(strFormat, padStr("000", (j+1) + "")+"-", nick.getHexAddress(), curCode, nick.label, plus, nick.instruct.mnemonic, nick.prefix, nick.operand);
                    lstStr += String.format(strFormat, padStr("000", (j + 1) + "") + "-", nick.getHexAddress(), curCode, nick.original);
                }
                else lstStr += String.format(strFormat, padStr("000", (j + 1) + "") + "-", "", "", nick.original);
            }
            catch (Exception ex)
            {
                printError("Unknown error in pass 2 at line " + j);
            }

        }

        // OUTPUT ------------------------------------------------------------------------------------------------------
        String hashFormatStr = "%-20s\t%-10s\t%-10s\t%-10s\t%-10s\n";
        System.out.println("\nSYMBOL TABLE:\n");
        System.out.printf(hashFormatStr, "Table Location", "Label", "Address", "Use", "Csect");
        System.out.println("___________________________________________________________________\n");
        for (int i = 0; i < hashArray.length; i++)
            if (hashArray[i] != null) System.out.printf(hashFormatStr, padStr("00", i + ""), hashArray[i].str.toUpperCase(), hashArray[i].getHexVal(), "main", "main");

        System.out.println("\n");
        System.out.print(lstStr);
        System.out.println("\n");
        System.out.print("OBJECT FILE:\n" + objStr);
        System.out.println("\n");
        System.out.print(ERROR_STRING);
        try
        {
            FileWriter fileWriter = new FileWriter(filename + ".lst");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("*****************************************\n");
            printWriter.print("SIC/XE Assembler\nMichael Wilson (N01223646)\n11-30-18\n");
            printWriter.print("*****************************************\n\n");
            printWriter.print("ASSEMBLER REPORT\n");
            printWriter.print("----------------\n\n\n");
            printWriter.print(lstStr);
            printWriter.print("\n\n" + ERROR_STRING);
            printWriter.close();

            fileWriter = new FileWriter(filename + ".obj");
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(objStr);
            printWriter.close();
        }
        catch (Exception ex)
        {
            System.out.println("Unable to create files");
        }


    }



















































    // UGLY CODE =======================================================================================================
    static void F(String __name, String __opCode, int __format)
    {
        INSTRUCTION_LIST.add(new Instruction(__name, __opCode, __format));
    }

    static void LOAD_INSTRUCTIONS()
    {
        F("BASE",    "xx", 0);

        F("START",   kek, 0);
        F("END",     kek, 0);
        F("WORD",    kek, 3);
        F("BYTE",    kek, 1);
        F("RESW",    kek, 0);
        F("RESB",    kek, 0);

        F("MULR",    "98",    2);
        F("WD",      "DC",    3);
        F("AND",     "40",    3);
        F("LPS",     "D0",    3);
        F("TIXR",    "B8",    2);
        F("SUBF",    "5C",    3);
        F("LDX",     "04",    3);
        F("SVC",     "B0",    2);
        F("STT",     "84",    3);
        F("TIX",     "2C",    3);
        F("FLOAT",   "C0",    1);
        F("LDT",     "74",    3);
        F("STA",     "0C",    3);
        F("SHIFTR",  "A8",    2);
        F("STB",     "78",    3);
        F("SIO",     "F0",    1);
        F("LDA",     "00",    3);
        F("HIO",     "F4",    1);
        F("DIVF",    "64",    3);
        F("LDCH",    "50",    3);
        F("JEQ",     "30",    3);
        F("SSK",     "EC",    3);
        F("LDS",     "6C",    3);
        F("J",       "3C",    3);
        F("SUB",     "1C",    3);
        F("RD",      "D8",    3);
        F("LDB",     "68",    3);
        F("RSUB",    "4C",    3);
        F("MULF",    "60",    3);
        F("JSUB",    "48",    3);
        F("SUBR",    "94",    2);
        F("DIVR",    "9C",    2);
        F("LDL",     "08",    3);
        F("STSW",    "E8",    3);
        F("COMPF",   "88",    3);
        F("TIO",     "F8",    1);
        F("JLT",     "38",    3);
        F("MUL",     "20",    3);
        F("OR",      "44",    3);
        F("COMP",    "28",    3);
        F("TD",      "E0",    3);
        F("STS",     "7C",    3);
        F("LDF",     "70",    3);
        F("ADD",     "18",    3);
        F("FIX",     "C4",    1);
        F("NORM",    "C8",    1);
        F("STF",     "80",    3);
        F("CLEAR",   "B4",    2);
        F("ADDF",    "58",    3);
        F("STCH",    "54",    3);
        F("STX",     "10",    3);
        F("RMO",     "AC",    2);
        F("COMPR",   "A0",    2);
        F("SHIFTL",  "A4",    2);
        F("STL",     "14",    3);
        F("ADDR",    "90",    2);
        F("STI",     "D4",    3);
        F("JGT",     "34",    3);
        F("DIV",     "24",    3);
    }












}
