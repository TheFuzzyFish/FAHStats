# FAHStats
Compiles an HTML table of team member contributions for a [Folding@Home](https://foldingathome.org/) team based on an input statistics file. It's important to note that this should not be embedded in webpages directly, since parsing the statistics can take several seconds. Instead, occasionally download the latest statistics, use this to parse it, and embed those results into the webpage.
This program was created because the Folding@Home statistics servers were down a lot during the COVID-19 pandemic and I got annoyed.

# Installation
You can either clone this repository and compile the code yourself (not recommended), or you can just download my precompiled .jar file (recommended) from [FAHStats-1.0.jar](https://github.com/TheFuzzyFish/FAHStats/raw/master/FAHStats-1.0.jar).

# Usage
The program requires you to be part of a team, and to also download the donor list from [this link](https://apps.foldingathome.org/daily_user_summary.txt.bz2). **Please note that you must decompress the bzip2 file. This program only accepts plain text files.** It accepts both of these as arguments, like so:
<pre>
java -jar FAHStats-1.0.jar /home/zach/Downloads/daily_user_summary.txt 255847
</pre>

The program assumes that the donor list file follows the format:
<pre>
[date]
[header]
[username score workunits teamnumber]...
</pre>
If the file format ever changes, I'll do my best to update this program.
The program parses the first 2 lines, then will skip empty lines or lines that do not follow the [username score workunits teamnumber] format

FAHStat will output an HTML formatted table in the following format:
```html
<table style="width:100%">
	<tr>
		<th>Username</th>
		<th>% of Score Contribution</th>
		<th>% of WU Contribution</th>
	</tr>

	<tr><td>user1</td><td>63.75%</td><td>11.11%</td></tr>
	<tr><td>user2</td><td>30.25%</td><td>77.78%</td></tr>
	<tr><td>user3</td><td>6.0%</td><td>11.11%</td></tr>
</table>
<p>Last Updated: Mon Mar 30 17:51:40 GMT 2020</p>
```
