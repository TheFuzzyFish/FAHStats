import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Too many or too few arguments.\n" +
                    "You must run the program like 'java -jar FAHStats-1.0.jar [path] [team_number]' where" +
                    " [path] is the path to the decompressed donor statistics downloaded from https://apps.foldingathome.org/daily_user_summary.txt.bz2" +
                    " and [team_number] is the team number to compile statistics for.\n\nExample:\n\t'java -jar FAHStats-1.0.jar daily_user_summary.txt 255847'");
            return;
        }

	    statsParser stats = new statsParser(args[0], Integer.parseInt(args[1]));
        ArrayList<String> array = stats.getUserStats();

        System.out.println("<table style=\"width:100%\">\n" +
                "\t<tr>\n" +
                "\t\t<th>Username</th>\n" +
                "\t\t<th>% of Score Contribution</th>\n" +
                "\t\t<th>% of WU Contribution</th>\n" +
                "\t</tr>\n"); // Prints header info
        /* Prints out the ArrayList */
        for (int i = 0; i < array.size(); i++) {
            System.out.println("\t<tr>" + array.get(i) + "</tr>");
        }
        System.out.println("</table>\n" +
                "<p>Last Updated: " + stats.date + "</p>\n" +
                "<p><a href=\"https://github.com/TheFuzzyFish/FAHStats\">FAHStats</a></p>");
    }
}
