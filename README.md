# FAHStats
Compiles an HTML table of team member contributions for a [Folding@Home](https://foldingathome.org/) team based on an input statistics file. It's important to note that this should not be embedded in webpages directly, since parsing the statistics can take several seconds. Instead, occasionally download the latest statistics, use this to parse it, and embed those results into the webpage.
This program was created because the Folding@Home statistics servers were down a lot during the COVID-19 pandemic and I got annoyed.

# Installation
You can either clone this repository and compile the code yourself (not recommended), or you can just download my precompiled .jar file (recommended) from [the releases page](https://github.com/TheFuzzyFish/FAHStats/releases).

# Usage
The program requires you to be part of a team, and to also download the donor list from [this link](https://apps.foldingathome.org/daily_user_summary.txt.bz2). **Please note that you must decompress the bzip2 file. This program only accepts plain text files.** It accepts both of these as arguments, like so:
<pre>
java -jar FAHStats.jar /home/zach/Downloads/daily_user_summary.txt 255847
</pre>

The program assumes that the donor list file follows the format:
<pre>
date
header
username    score    workunits    teamnumber
username    score    workunits    teamnumber
username    score    workunits    teamnumber
username    score    workunits    teamnumber
...
</pre>
If the file format ever changes, I'll do my best to update this program.
The program parses the first 2 lines, then will skip empty lines or lines that do not follow the [username\tscore\tworkunits\tteamnumber] format.
As of version 1.1, FAHStats supports bignum, and will also output numbers with commas 

FAHStat will output an HTML formatted table in the following format:
```html
<table style="width:100%">
	<tr>
		<th>Rank</th>
		<th>Username</th>
		<th>Score Contribution</th>
		<th>WU Contribution</th>
	</tr>

	<tr><td>1</td><td><b>Jim</b></td><td><b>74,350 pts</b><br><i>27.34%</i></td><td><b>9 units</b><br><i>20.45%</i></td></tr>
	<tr><td>2</td><td><b>Jessica</b></td><td><b>72,251 pts</b><br><i>26.57%</i></td><td><b>18 units</b><br><i>40.91%</i></td></tr>
	<tr><td>3</td><td><b>Josh</b></td><td><b>58,717 pts</b><br><i>21.59%</i></td><td><b>7 units</b><br><i>15.91%</i></td></tr>
	<tr><td>4</td><td><b>Jacob</b></td><td><b>38,953 pts</b><br><i>14.32%</i></td><td><b>5 units</b><br><i>11.36%</i></td></tr>
	<tr><td>5</td><td><b>Joe</b></td><td><b>26,781 pts</b><br><i>9.85%</i></td><td><b>4 units</b><br><i>9.09%</i></td></tr>
	<tr><td>6</td><td><b>Jayden</b></td><td><b>885 pts</b><br><i>0.33%</i></td><td><b>1 units</b><br><i>2.27%</i></td></tr>
</table>
<p><b>Total team score:</b> 271,937<br><b>Total work units processed:</b> 44<br>Last Updated: Mon Jun 08 20:48:31 GMT 2020</p>
```

# Notes
 - My search algorithm is awful. You *can* run it on the current statistics and finish within a few seconds, but you'd really be better off in the long run by filtering it with a faster search algorithm as implemented in grep or awk. Just be sure to keep the first 2 lines of the file, as FAHStats assumes they're there. My driver script uses awk like so: <pre> curl --output - https://apps.foldingathome.org/daily_user_summary.txt.bz2 | bzip2 -d | awk 'NR==1 || NR==2 || /255847/' > fahstats.txt </pre>
 - I currently do not provide any switches or flags to customize the outputted table. The reasoning behind this is simple: I don't care enough to. If you want to implement this on your own site, you're likely savvy enough to edit the source code yourself. You're probably looking for line 101 in statsParser.java
