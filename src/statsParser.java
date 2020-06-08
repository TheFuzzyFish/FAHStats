import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class statsParser {
    private ArrayList<String> statsList; // Stores stats of the team in "username score wu team" format
    public String date; // The date taken from the statistics file (assumed to be first line)
    public BigInteger teamScore; // The total score of the team
    public BigInteger teamWU; // The total number of work units the team has completed

    /**
     * Opens up a tab-delimited file from Folding@Home and compiles team-wide percentages of contribution
     * @param filepath the path to the decompressed statistics file (download from https://apps.foldingathome.org/daily_user_summary.txt.bz2)
     * @param teamNumber the team number to compile statistics for
     */
    public statsParser(String filepath, long teamNumber) {
        statsList = new ArrayList<>();
        teamScore = BigInteger.ZERO;
        teamWU = BigInteger.ZERO;

        try {
            File file = new File(filepath);
            Scanner scan = new Scanner(file);
            BigInteger wu; // Used to temporarily store work units for users
            BigInteger score; // Used to temporarily store the score of users

            date = scan.nextLine();
            scan.nextLine();

            String line;
            while (scan.hasNextLine()) {
                line = scan.nextLine();

                if (line.contains(Long.toString(teamNumber))) { // This greatly speeds up the searching algorithm by mostly avoiding the creation of unnecessary objects
                    if (!line.isBlank() && new StringTokenizer(line, "\t").countTokens() == 4) { // Weeds out corrupted and blank lines
                        Scanner lineScan = new Scanner(line).useDelimiter("\t");

                        lineScan.next(); // Skips username
                        score = lineScan.nextBigInteger(); // Skips user score
                        wu = lineScan.nextBigInteger(); // Skips user WU

                        if (lineScan.nextInt() == teamNumber) { // If the user belongs to the team specified, add it to the list
                            statsList.add(line);
                            teamWU = teamWU.add(wu);
                            teamScore = teamScore.add(score);
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

        BigInteger tmpScore;
        BigInteger tmpWU;

        /* Calculates percentages of contribution and adds them to the ArrayLists */
        Scanner scan;
        String username;
        BigDecimal percScore;
        BigDecimal percWU;
        for (int i = 0; i < this.statsList.size(); i++) {
            scan = new Scanner(statsList.get(i));
            username = scan.next();
            tmpScore = scan.nextBigInteger();
            tmpWU = scan.nextBigInteger();

            percScore = new BigDecimal(tmpScore).divide(new BigDecimal(this.teamScore), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            percWU = new BigDecimal(tmpWU).divide(new BigDecimal(this.teamWU), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

            percScore = percScore.setScale(2, RoundingMode.HALF_UP);
            percWU = percWU.setScale(2, RoundingMode.HALF_UP);

            userStats.add("<td>" + username + "</td><td>" + percScore.toString() + "%</td><td>" + percWU.toString() + "%</td>");
            userPercScores.add(percScore.doubleValue());
        }

        /* Runs bubble sort to sort the team statistics based on their point contributions */
        boolean unsorted = true;
        String tmp;
        double tmp2;
        while (unsorted) {
            unsorted = false;
            for (int i = 1; i < userStats.size(); i++) {
                if (userPercScores.get(i - 1) < userPercScores.get(i)) {
                    unsorted = true;

                    tmp = userStats.get(i);
                    userStats.set(i, userStats.get(i - 1));
                    userStats.set(i - 1, tmp);

                    tmp2 = userPercScores.get(i);
                    userPercScores.set(i, userPercScores.get(i - 1));
                    userPercScores.set(i - 1, tmp2);
                }
            }
        }

        return userStats;
    }
}
