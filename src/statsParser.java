import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class statsParser {
    private ArrayList<String> statsList; // Stores stats of the team in "username score wu team" format
    public String date; // The date taken from the statistics file (assumed to be first line)
    public long teamScore; // The total score of the team
    public long teamWU; // The total number of work units the team has completed

    /**
     * Opens up a tab-delimited file from Folding@Home and compiles team-wide percentages of contribution
     * @param filepath the path to the decompressed statistics file (download from https://apps.foldingathome.org/daily_user_summary.txt.bz2)
     * @param teamNumber the team number to compile statistics for
     */
    public statsParser(String filepath, int teamNumber) {
        statsList = new ArrayList<>();
        teamScore = 0;

        try {
            File file = new File(filepath);
            Scanner scan = new Scanner(file);
            long wu; // Used to temporarily store work units for users
            long score; // Used to temporarily store the score of users

            date = scan.nextLine();
            scan.nextLine();

            String line;
            while (scan.hasNextLine()) {
                line = scan.nextLine();

                if (line.contains(Integer.toString(teamNumber))) { // This greatly speeds up the searching algorithm by mostly avoiding the creation of unnecessary objects
                    if (!line.isBlank() && new StringTokenizer(line, "\t").countTokens() == 4) { // Weeds out corrupted and blank lines
                        Scanner lineScan = new Scanner(line).useDelimiter("\t");

                        lineScan.next(); // Skips username
                        score = lineScan.nextLong(); // Skips user score
                        wu = lineScan.nextLong(); // Skips user WU

                        if (lineScan.nextInt() == teamNumber) { // If the user belongs to the team specified, add it to the list
                            statsList.add(line);
                            teamWU += wu;
                            teamScore += score;
                        }
                    }
                }
            }


            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + filepath + " not found. Perhaps it doesn't exist?");
            return;
        }
    }

    /**
     * Returns the unprocessed and unsorted ArrayList of all users belonging to the team as they appear in the file
     * @return the unprocessed and unsorted ArrayList of all users belonging to the team as they appear in the file
     */
    public ArrayList<String> getRawStats() {
        return statsList;
    }

    /**
     * Returns the user percentages of contribution in "username %score %wu" format, rounded to 2 decimal places
     * @return the user percentages of contribution in "username %score %wu" format, rounded to 2 decimal places
     */
    public ArrayList<String> getUserStats() {
        ArrayList<String> userStats = new ArrayList<String>(); // Stores the users and their percentages of contribution in "username %score %wu" format
        ArrayList<Double> userPercScores = new ArrayList<Double>(); // Stores the corresponding score percentages of each user (used for sorting)

        long tmpScore = 0;
        long tmpWU = 0;

        /* Calculates percentages of contribution and adds them to the ArrayLists */
        Scanner scan;
        String username;
        double percScore;
        double percWU;
        for (int i = 0; i < this.statsList.size(); i++) {
            scan = new Scanner(statsList.get(i));
            username = scan.next();
            tmpScore = scan.nextInt();
            tmpWU = scan.nextInt();

            percScore = ((double) tmpScore / this.teamScore) * 100;
            percWU = ((double) tmpWU / this.teamWU) * 100;

            userStats.add("<td>" + username + "</td><td>" + Math.round(percScore * 100.0) / 100.0 + "%</td><td>" + Math.round(percWU * 100.0) / 100.0 + "%</td>");
            userPercScores.add(percScore);
        }

        /* Runs bubble sort to sort the team statistics based on their point contributions */
        boolean unsorted = true;
        String tmp;
        while (unsorted) {
            unsorted = false;
            for (int i = 1; i < userStats.size(); i++) {
                if (userPercScores.get(i - 1) < userPercScores.get(i)) {
                    unsorted = true;

                    tmp = userStats.get(i);
                    userStats.set(i, userStats.get(i - 1));
                    userStats.set(i - 1, tmp);
                }
            }
        }

        return userStats;
    }
}
