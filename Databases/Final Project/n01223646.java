import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class n01223646
{
    static final boolean DEBUG = false; // ToDo: Set False
    static final String PASSWORD = "root"; //ToDo: Change Password
    static final String DATABASE_NAME = "mclDB";
    static Statement SQL;
    static Connection CON;
    static HashMap<Integer, String> FILM_TITLE_MAP = new HashMap();
    static HashMap<String, Integer> FILM_ID_MAP = new HashMap();
    static void print(String txt)
    {
        System.out.println(txt);
    }
    static String input(String msg)
    {
        System.out.print(msg);
        return new Scanner(System.in).nextLine();
    }
    static void fun1(ArrayList<String> _list, ArrayList<Movie> mov_list, int score)
    {
        for (String s : _list)
        {
            String title = FILM_TITLE_MAP.get(Integer.parseInt(s));
            if (title != null)
            {
                int index = -1;
                for (int i = 0; i < mov_list.size(); i++) if (mov_list.get(i).getTitle().equals(title)) index = i;
                if (index == -1) mov_list.add(new Movie(title, score));
                else mov_list.get(index).incScore(score);
            }
        }
    }
    static void loadTable(String table, String format, int format2) throws Exception
    {
        print("> Inserting '" + table + "'...");
        SQL.execute("CREATE TABLE " + table + format);
        //SQL.execute("LOAD DATA LOCAL INFILE '" + table + ".csv' INTO TABLE "+table+" FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'");
        BufferedReader file = new BufferedReader(new FileReader(new File(table + ".csv")));
        String line;
        PreparedStatement insertStmt = CON.prepareStatement("INSERT INTO " + table + " VALUES (?,?,?);");
        while ((line = file.readLine()) != null)
        {
            String values[] = line.replace('\'', '`').split(",");
            try
            {
                if (format2 == 0)
                {
                    try
                    {
                        insertStmt.setInt(1, Integer.parseInt(values[0].trim()));
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(1, 0);
                    }
                    try
                    {
                        insertStmt.setInt(2, Integer.parseInt(values[1].trim()));
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(2, 0);
                    }
                    try
                    {
                        insertStmt.setString(3, values[2]);
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(3, 0);
                    }
                }
                else if (format2 == 1)
                {
                    try
                    {
                        insertStmt.setInt(1, Integer.parseInt(values[0].trim()));
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(1, 0);
                    }
                    try
                    {
                        insertStmt.setFloat(2, Float.parseFloat(values[1].trim()));
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(2, 0);
                    }
                    try
                    {
                        insertStmt.setString(3, values[2]);
                    }
                    catch (Exception ex)
                    {
                        insertStmt.setNull(3, 0);
                    }
                }
                insertStmt.executeUpdate();
            }
            //catch (Exception ex) {}
            catch (Exception ex)
            {
                print("\nERROR:" + ex.getMessage());
            }
        }
        file.close();
    }
    static void search(String _in) throws Exception
    {
        String user = _in.toLowerCase();
        int id = -1;
        try { id = FILM_ID_MAP.get(user); } catch (Exception ex) {};
        if (id != -1)
        {
            print("Searching movies similar to '" + _in + "'...");
            String[] ganres = new Table("SELECT g_id FROM ganres WHERE m_id = " + id).getColData(0);
            String[] keywords = new Table("SELECT kw_id FROM keywords WHERE m_id = " + id).getColData(0);
            ArrayList<String> id_genre = new ArrayList<>(), id_kw = new ArrayList<>();
            ArrayList<Movie> mov_list = new ArrayList<>();
            for (int i = 0; i < ganres.length; i++)
                id_genre.addAll(Arrays.asList(new Table("SELECT m_id FROM ganres WHERE NOT m_id = "+id+" AND g_id = " + ganres[i]).getColData(0)));
            for (int i = 0; i < keywords.length; i++)
                id_kw.addAll(Arrays.asList(new Table("SELECT m_id FROM keywords WHERE NOT m_id = "+id+" AND kw_id = " + keywords[i]).getColData(0)));
            fun1(id_genre, mov_list, 3);
            fun1(id_kw, mov_list, 1);
            mov_list.sort((Movie obj1, Movie obj2) ->
            {
                if (obj1.getScore() > obj2.getScore()) return -1;
                else if (obj1.getScore() < obj2.getScore()) return 1;
                else return 0;
            });

            print("\nRECOMMENDED MOVIES:");
            int len = 5;
            if (mov_list.size() < 5) len = mov_list.size();
            for (int i = 0; i < len; i++) print(mov_list.get(i).getTitle());
        }
        else
        {
            String[] sugg = new Table("SELECT title FROM movies WHERE title LIKE '%" + user + "%'").getColData(0);
            print("Could not find movie '" + _in + "'");
            if (sugg.length > 0)
            {
                print("\nDid you mean?");
                for (int i = 0; i < sugg.length; i++) print(i + "} " + sugg[i]);
                print("B} [BACK]");
                String suggSel = input("\nSelection: ").trim();
                int suggSelNum = -1;
                if (!suggSel.equals("b"))
                {
                    try { suggSelNum = Integer.parseInt(suggSel); }
                    catch (Exception ex) { }
                    if (suggSelNum >= 0 && suggSelNum < sugg.length) search(sugg[suggSelNum]);
                    else print("Invalid selection!");
                }
            }
        }


    }
    private static class Movie
    {
        private String title = "[null]";
        private int score = 0;
        public Movie(String _title, int _startScore) { this.title = _title; this.score = _startScore;}
        public String getTitle() {return this.title;}
        public int getScore() {return this.score;}
        public void incScore(int val) {this.score += val;}
    }
    private static class Table
    {
        private int rowCount, columnCount;
        private ResultSet result;
        private String tableStr;
        private String[][] data;

        public int getRowCount() { return this.rowCount; }
        public int getColumnCount() { return this.columnCount; }
        public ResultSet getResult() { return this.result; }
        public String[][] getData() { return this.data; }
        public String[] getColData(int col)
        {
            String[] $return = new String[data.length];
            for (int i = 0; i < data.length; i++)  $return[i] = data[i][col];
            return $return;
        }
        public void print() { System.out.print(tableStr); }

        private void processRsults(ResultSet result) throws Exception
        {
            result.last();
            rowCount = result.getRow();
            result.beforeFirst();
            ResultSetMetaData meta = result.getMetaData();
            columnCount = meta.getColumnCount();
            int columnSizes[] = new int[columnCount];
            Object columnNames[] = new String[columnCount];
            String format = "", bar = "+";
            for (int i = 0; i < columnCount; i++)
            {
                String name = meta.getColumnLabel(i+1);
                int size = meta.getColumnDisplaySize(i+1);
                columnNames[i] = name;
                if (size < name.length()) size = name.length();
                size += 2;
                columnSizes[i] = size;
                bar += "-".repeat(size)+"+";
                format += "|%-"+size+"s";
            }
            format += "|\n";
            data = new String[rowCount][columnCount];
            tableStr = "";
            if (rowCount < 1) tableStr += "No results.\n";
            else
            {
                tableStr += bar + "\n";
                tableStr += String.format(format, columnNames);
                tableStr += bar + "\n";
                for (int i = 0; i < rowCount; i++)
                {
                    result.next();
                    Object dataArray[] = new String[columnCount];
                    for (int q = 0; q < columnCount; q++)
                    {
                        String temp = result.getString(q+1);
                        dataArray[q] = temp;
                        data[i][q] = temp;
                    }
                    tableStr += String.format(format, dataArray);
                }
                tableStr += bar + "\n";
            }
        }
        public Table(ResultSet result) throws Exception
        {
            processRsults(result);
        }
        public Table(String query) throws Exception { processRsults(SQL.executeQuery(query)); }
    }

    public static void main(String[] args) throws Exception
    {
        print("==================================================================================\n");
        print("Welcome!");
        print("> Establishing connection...");
        CON = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", PASSWORD);
        SQL = CON.createStatement();
        if (!DEBUG)
        {
            SQL.execute("DROP DATABASE IF EXISTS " + DATABASE_NAME);
            SQL.execute("CREATE DATABASE " + DATABASE_NAME);
        }
        SQL.execute("USE "+DATABASE_NAME);
        if (!DEBUG)
        {
            loadTable("ganres", "(m_id INT, g_id INT, ganreName VARCHAR(100))", 0);
            loadTable("keywords", "(m_id INT, kw_id INT, kwName VARCHAR(100))", 0);
            loadTable("movies", "(m_id INT, pop FLOAT, title VARCHAR(200))", 1);
        }

        Table movTbl = new Table("SELECT m_id, title FROM movies");
        String[] movTitleArray = movTbl.getColData(1);
        String[] movIdArray = movTbl.getColData(0);
        for (int i = 0; i < movTitleArray.length; i++)
        {
            try
            {
                int id = Integer.parseInt(movIdArray[i]);
                String title = movTitleArray[i];
                FILM_ID_MAP.put(title.toLowerCase(), id);
                FILM_TITLE_MAP.put(id, title);
            }
            catch (Exception ex){}
        }

        boolean loop = true;
        while (loop)
        {
            String str = input("\nEnter movie title: ").trim();
            if (str.toLowerCase().equals("/quit")) loop = false;
            else
            {
                try
                {
                    search(str);
                }
                catch (Exception ex) { print("An error ocurred. Please try again."); }
            }
        }
        print("Goodbye...");
        if (!DEBUG) SQL.execute("DROP DATABASE IF EXISTS " + DATABASE_NAME);
        print("\n==================================================================================");
    }
}



/*
    static void searchMovie(String _userInput) throws Exception
    {

        String userInput = _userInput.toLowerCase();
        Table tbl = new Table("SELECT * FROM movies WHERE title = '" + userInput + "'");
        if (tbl.getRowCount() > 0)
        {
            print("Searching movies similar to '" + _userInput + "'...");
            String m_id = tbl.getData()[0][0];
            String[] ganres = new Table("SELECT g_id FROM ganres WHERE m_id = " + m_id).getColData(0);
            String[] keywords = new Table("SELECT kw_id FROM keywords WHERE m_id = " + m_id).getColData(0);
            ArrayList<String> id_genre = new ArrayList<>(), id_kw = new ArrayList<>();
            ArrayList<Movie> mov_list = new ArrayList<>();
            for (int i = 0; i < ganres.length; i++)
                id_genre.addAll(Arrays.asList(new Table("SELECT m_id FROM ganres WHERE g_id = " + ganres[i]).getColData(0)));
            for (int i = 0; i < keywords.length; i++)
                id_kw.addAll(Arrays.asList(new Table("SELECT m_id FROM keywords WHERE kw_id = " + keywords[i]).getColData(0)));

            PreparedStatement stmtA = CON.prepareStatement("SELECT title, pop FROM movies WHERE m_id = ?");

            for (String s : id_genre)
            {
                if (!s.equals(m_id))
                {
                    try
                    {
                        stmtA.setInt(1, Integer.parseInt(s));
                        Table tmpTbl = new Table(stmtA.executeQuery());
                        String tmp = tmpTbl.getData()[0][0], tmpA = tmpTbl.getData()[0][1];
                        int tmp2 = -1;
                        for (int i = 0; i < mov_list.size(); i++) if (mov_list.get(i).getTitle().equals(tmp)) tmp2 = i;
                        if (tmp2 == -1) mov_list.add(new Movie(tmp, Float.parseFloat(tmpA), 3));
                        else mov_list.get(tmp2).incScore(3);
                    }
                    catch (Exception ex) { }
                    //catch (Exception ex) {print("ERROR: "+ex.getMessage());}
                }
            }

            for (String s : id_kw)
            {
                if (!s.equals(m_id))
                {
                    try
                    {
                        stmtA.setInt(1, Integer.parseInt(s));
                        Table tmpTbl = new Table(stmtA.executeQuery());
                        String tmp = tmpTbl.getData()[0][0], tmpA = tmpTbl.getData()[0][1];
                        int tmp2 = -1;
                        for (int i = 0; i < mov_list.size(); i++) if (mov_list.get(i).getTitle().equals(tmp)) tmp2 = i;
                        if (tmp2 == -1) mov_list.add(new Movie(tmp, Float.parseFloat(tmpA), 1));
                        else mov_list.get(tmp2).incScore(1);
                    }
                    catch (Exception ex) { }
                    //catch (Exception ex) {print("ERROR: "+ex.getMessage());}
                }
            }

            //for (Movie s : mov_list) s.mulScore(s.pop);

            mov_list.sort((Movie obj1, Movie obj2) ->
            {
                if (obj1.getScore() > obj2.getScore()) return -1;
                else if (obj1.getScore() < obj2.getScore()) return 1;
                else return 0;
            });

            print("\nRECOMMENDED MOVIES:");
            int len = 5;
            if (mov_list.size() < 5) len = mov_list.size();
            for (int i = 0; i < len; i++) print(mov_list.get(i).getTitle());
        }
        else
        {

        }
    }

 */