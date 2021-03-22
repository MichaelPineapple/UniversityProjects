import java.util.*;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.*;

public class Main
{
    static final boolean DEBUG = true; // ToDo: Remove DEBUG
    static Statement SQL;
    static void print(String txt) { System.out.println(txt); }
    static String input(String msg) { System.out.print(msg); return new Scanner(System.in).next(); }
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
        public void print() { System.out.print(tableStr); }

        public Table(String query) throws Exception
        {
            result = SQL.executeQuery(query);
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
    }
    static void loadTable(String table, String format) throws Exception
    {
        print("> Inserting '"+table+"'...");
        SQL.execute("CREATE TABLE " + table + format);
        BufferedReader file = new BufferedReader(new FileReader(new File(table + ".csv")));
        String line, values = "";
        while ((line = file.readLine()) != null)
        {
            values += "(";
            if (line.endsWith(",")) line += "NULL";
            String csv[] = line.split(",");
            for (String s : csv)
            {
                if (s.length() < 1) values += "NULL,";
                else values += s + ",";
            }
            values = values.substring(0, values.length()-1) + "),";
        }
        file.close();
        SQL.execute("INSERT INTO " + table + " VALUES " + values.substring(0, values.length() - 1));
    }
    static ArrayList<String> Q4_func(String region) throws Exception
    {
        // 7 = playerA
        // 8 = playerB
        // 9 = scoreA
        // 10 = scoreB
        ArrayList<String> winners = new ArrayList<>();
        String temp_data[][] =
                new Table("SELECT player FROM tournaments, earnings WHERE tournament = tournament_id AND region = '"+region+"' AND major = True AND position = 1 ORDER BY player").getData();
        for (int i = 0; i < temp_data.length; i++) winners.add(temp_data[i][0]);
        return winners;
    }
    private static class Q6_object
    {
        public String tag, nation;
        float ratio;

        public Q6_object(String _tag, String _nation, float _ratio)
        {
            tag = _tag;
            nation = _nation;
            ratio = _ratio;
        }
    }

    public static void main(String[] args) throws Exception
    {
            // Startup
            print("Welcome!");
            print("> Establishing connection...");
            SQL = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "oralcumshot").createStatement(); // ToDo: change password to 'root'
            print("> Creating database...");
            if (!DEBUG)
            {
                SQL.execute("DROP DATABASE IF EXISTS PlayerDB_Assign4");
                SQL.execute("CREATE DATABASE PlayerDB_Assign4");
            }
            SQL.execute("USE PlayerDB_Assign4");
            if (!DEBUG)
            {
                // Load data
                loadTable("players", "(player_id INT, tag VARCHAR(20), real_name VARCHAR(40), nationality VARCHAR(2), birthday DATE, game_race VARCHAR(1), PRIMARY KEY(player_id))");
                loadTable("teams", "(team_id INT, name VARCHAR(30), founded DATE, disbanded DATE, PRIMARY KEY(team_id))");
                loadTable("members", "(player INT, team INT, start_date DATE, end_date DATE)");
                loadTable("tournaments", "(tournament_id INT, name VARCHAR(100), region VARCHAR(2), major BOOL, PRIMARY KEY(tournament_id))");
                loadTable("matches_v2", "(match_id INT, date DATE, tournament INT, playerA INT, playerB INT, scoreA INT, scoreB INT, offline BOOL, PRIMARY KEY(match_id))");
                loadTable("earnings", "(tournament INT, player INT, prize_money INT, position INT, PRIMARY KEY(tournament, player))");
            }

            // Main Program
            String today = "'" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()).replace("/", "-") + "'";
            boolean loop = true;
            while (loop)
            {
                System.out.print(
                        "\n~MENU~" +
                        "\n1) Q1: Search For Players By Birth." +
                        "\n2) Q2: Add Player To Team." +
                        "\n3) Q3: Search For Players By Nationality And Birth Year." +
                        "\n4) Q4: List 'Triple Crown' Recipients." +
                        "\n5) Q5: List Former Members of 'ROOT Gaming.'" +
                        "\n6) Q6: List Protoss Players With Over 65% Win Rate Against Terran Players." +
                        "\n7) Q7: List Active Teams Older Than 2011." +
                        "\n0) Drop Database And Quit." +
                        "\n\n");

                int userInput = -1;
                try { userInput = Integer.parseInt(input("Enter Selection: ")); } catch (NumberFormatException ex) { }
                if (userInput < 0 || userInput > 7) print("Invalid input! Try again...");
                else
                {
                    switch (userInput)
                    {
                        case 0: // QUIT
                        {
                            loop = false;
                        }
                            break;

                        case 1: // Q1
                        {
                            try
                            {
                                int year = Integer.parseInt(input("Enter birth year: ")), month = -1;
                                try {month = Integer.parseInt(input("Enter birth month (1-12): "));} catch (NumberFormatException ex) {}
                                if (month >= 1 && month <= 12)
                                {
                                    new Table("SELECT real_name AS Name, tag AS Tag, nationality AS Nationality " + "FROM players " + "WHERE YEAR(birthday) = " + year + " AND MONTH(birthday) = " + month).print();
                                }
                                else print("Invalid month!");
                            }
                            catch (NumberFormatException ex) { print("Invalid year!"); }
                        }
                        break;

                        case 2: // Q2
                        {
                            try
                            {
                                int player = Integer.parseInt(input("Enter player ID: "));
                                if (new Table("SELECT * FROM players WHERE player_id = " + player).getRowCount() > 0)
                                {
                                    try
                                    {
                                        int team = Integer.parseInt(input("Enter team ID: "));
                                        if (new Table("SELECT * FROM teams WHERE team_id = " + team).getRowCount() > 0)
                                        {
                                            if (new Table("SELECT * FROM members WHERE team = " + team + " AND player = " + player + " AND end_date IS NULL").getRowCount() > 0) print("Player " + player + " is already a member of team " + team + ".");
                                            else
                                            {
                                                SQL.execute("UPDATE members SET end_date = " + today + " WHERE player = " + player + " AND end_date IS NULL");
                                                SQL.execute("INSERT INTO members VALUES (" + player + ", " + team + ", " + today + ", NULL)");
                                                print("Player " + player + " has been added to team " + team + " on " + today + ".");
                                            }
                                            new Table("SELECT player AS 'Player ID', team AS 'Team', start_date AS 'Start Date', end_date AS 'End Date' FROM members WHERE player = "+player).print();
                                        }
                                        else print("Team not found.");
                                    }
                                    catch (NumberFormatException ex) { print("Invalid team ID!"); }
                                }
                                else print("Player not found.");
                            }
                            catch (NumberFormatException ex) { print("Invalid player ID!"); }
                        }
                        break;

                        case 3: // Q3
                        {
                            try
                            {
                                int year = Integer.parseInt(input("Enter birth year: "));
                                String nation = input("Enter nationality: ");
                                new Table("SELECT real_name AS Name, birthday AS Birthday " + "FROM players " + "WHERE YEAR(birthday) = " + year + " AND nationality = '" + nation + "'").print();
                            }
                            catch (NumberFormatException ex) { print("Invalid year!"); }
                        }
                        break;

                        case 4: // Q4
                        {
                            print("Calculating...");
                            ArrayList<String> KR_winners = Q4_func("KR"), AM_winners = Q4_func("AM"), EU_winners = Q4_func("EU");
                            String format = "|%-20s|%-10s|\n", bar = "+--------------------+----------+";
                            print("'Triple Crown' Recipients:");
                            print(bar);
                            System.out.printf(format, "Tag", "Game Race");
                            print(bar);
                            String players[][] = new Table("SELECT player_id, tag, game_race FROM players ORDER BY tag").getData();
                            for (int i = 0; i < players.length; i++)
                            {
                                String p = players[i][0];
                                if (KR_winners.contains(p) && AM_winners.contains(p) && EU_winners.contains(p)) System.out.printf(format, players[i][1], players[i][2]);
                            }
                            print(bar);
                        }
                            break;

                        case 5: // Q5
                        {
                            print("Calculating...");
                            String gay[][] = new Table("SELECT * FROM players, members WHERE player_id = player AND team = 39 ORDER BY end_date").getData();
                            ArrayList<String> currentMembers = new ArrayList<>(), uniqueFormerMembers = new ArrayList<>();
                            ArrayList<String[]> formerMembers = new ArrayList<>();
                            for (int i = 0; i < gay.length; i++) if (gay[i][9] == null) currentMembers.add(gay[i][0]);
                            for (int i = 0; i < gay.length; i++)  if (!currentMembers.contains(gay[i][0])) formerMembers.add(new String[]{gay[i][1], gay[i][2], gay[i][9]});
                            for (int i = 0; i < formerMembers.size(); i++)
                            {
                                String tmpID = formerMembers.get(i)[0];
                                if (!uniqueFormerMembers.contains(tmpID))  uniqueFormerMembers.add(tmpID);
                            }
                            String output[][] = new String[uniqueFormerMembers.size()][gay[1].length];
                            for (int i = 0; i < uniqueFormerMembers.size(); i++)
                            {
                                for (int q = 0; q < formerMembers.size(); q++) if (formerMembers.get(q)[0].equals(uniqueFormerMembers.get(i))) output[i] = formerMembers.get(q);
                            }
                            String format = "|%-20s|%-40s|%-21s|\n", bar = "+--------------------+----------------------------------------+---------------------+";
                            print("Former members of 'ROOT Gaming':");
                            print(bar);
                            System.out.printf(format, "Tag", "Real Name", "Most Recent Departure");
                            print(bar);
                            for (int i = 0; i < output.length; i++)  System.out.printf(format, output[i][0], output[i][1], output[i][2]);
                            print(bar);
                        }
                            break;

                        case 6: // Q6
                        {
                            print("Calculating...");
                            ArrayList<Q6_object> realNiggas = new ArrayList<>();
                            String[][] terranPlayersArray = new Table("SELECT player_id FROM players WHERE game_race = 'T'").getData();
                            ArrayList<String> terranPlayers = new ArrayList<>();
                            for (int k = 0; k < terranPlayersArray.length; k++) terranPlayers.add(terranPlayersArray[k][0]);
                            String protoPlayersData[][] = new Table("SELECT player_id, tag, nationality FROM players WHERE game_race = 'P'").getData();
                            for (int i = 0; i < protoPlayersData.length; i++)
                            {
                                int wins = 0, total = 0;
                                String id = protoPlayersData[i][0];
                                String[][] matches = new Table("SELECT playerA, playerB, scoreA, scoreB FROM matches_v2 WHERE playerA = " +id + " OR playerB = " + id).getData();
                                for (int q = 0; q < matches.length; q++) // for each match involving player i
                                {
                                    String myScore, opScore, opponent;
                                    boolean A = matches[q][0].equals(id);
                                    if (A) { opponent = matches[q][1]; opScore = matches[q][3]; myScore = matches[q][2]; }
                                    else { opponent = matches[q][0]; opScore = matches[q][2]; myScore = matches[q][3]; }
                                    if (terranPlayers.contains(opponent))
                                    {
                                        if (Integer.parseInt(myScore) > Integer.parseInt(opScore)) wins++;
                                        total++;
                                    }
                                }
                                if (total >= 10)
                                {
                                    float KD = ((float)wins/(float)total)*100; // calculate win rate
                                    if (KD > 65) realNiggas.add(new Q6_object(protoPlayersData[i][1], protoPlayersData[i][2], KD));
                                }
                            }
                            realNiggas.sort((Q6_object obj1, Q6_object obj2) -> {
                                if (obj1.ratio > obj2.ratio) return -1;
                                else if (obj1.ratio < obj2.ratio) return 1;
                                else return 0; });
                            print("Protoss Players With Over 65% Win Rate Against Terran Players\n(Must Have At Least 10 Matches Against Terran Players):");
                            String format = "|%-20s|%-20s|%-27s|\n", bar = "+--------------------+--------------------+---------------------------+";
                            print(bar);
                            System.out.printf(format, "Tag", "Nationality", "Win Rate Vs. Terran Players");
                            print(bar);
                            for (Q6_object x : realNiggas)  System.out.printf(format,x.tag,x.nation,String.format("%.1f", x.ratio)+"%");
                            print(bar);
                        }
                            break;

                        case 7: // Q7
                        {
                            String teamsArray[][] = new Table("SELECT team_id, name, founded FROM teams WHERE YEAR(founded) < 2011 AND disbanded IS NULL ORDER BY name").getData();
                            int[] protossPlayers = new int[teamsArray.length], terranPlayers = new int[teamsArray.length], zergPlayers = new int[teamsArray.length];
                            for (int i = 0; i < teamsArray.length; i++)
                            {
                                protossPlayers[i] = 0; terranPlayers[i] = 0; zergPlayers[i] = 0;
                                String[][] playersArray = new Table("SELECT player FROM members WHERE end_date IS NULL AND team = " + teamsArray[i][0]).getData();
                                for (int q = 0; q < playersArray.length; q++)
                                {
                                    String race = new Table("SELECT game_race FROM players WHERE player_id = " + playersArray[q][0]).getData()[0][0];
                                    if (race.equals("P")) protossPlayers[i] += 1;
                                    else if (race.equals("T")) terranPlayers[i] += 1;
                                    else if (race.equals("Z")) zergPlayers[i] += 1;
                                }
                            }
                            String format = "|%-30s|%-15s|%-15s|%-15s|%-15s|\n", bar = "+------------------------------+---------------+---------------+---------------+---------------+";
                            print("Active Teams Older Than 2011:");
                            print(bar);
                            System.out.printf(format, "Team Name", "Date Founded", "Protoss Players", "Terran Players", "Zerg Players");
                            print(bar);
                            for (int i = 0; i < teamsArray.length; i++)  System.out.printf(format,teamsArray[i][1],teamsArray[i][2],protossPlayers[i],terranPlayers[i],zergPlayers[i]);
                            print(bar);
                        }
                            break;
                    }
                }
            }

            // Shutdown
            print("\n> Dropping database...");
            SQL.execute("DROP DATABASE PlayerDB_Assign4");
            print("> Closing connection...");
            SQL.close();
            print("Goodbye...");
    }
}