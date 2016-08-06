#BSE Data Fetcher
#####Get Equity OHLC data from Bombay Stock Exchange of India 
======================================================

This utility presents the user with daily OHLC (Open, High, Low, Close prices) data w.r.t different equities present in different market segments (like Automobiles, Cotton, Petroleum, Steel, etc) and saves it to the specific equitiy's corresponding CSV file. The respective CSV file(s), upon daily execution of the project, accumulate daily OHLC data over time. 

#####What to do with the CSV file(s)?
The CSV files contain data when accumulated with data over a period of time (minimum 14 days) can be used in Equity Price Trend Analysis (read more about it here [Technical Analysis Wikpedia](https://en.wikipedia.org/wiki/Technical_analysis))



### Usage
Requires Java 1.8.0 or above.

For running the associated jar, simply navigate to the directory where the jar is placed and run:
```
java -jar VFL.jar
```
Post running the jar, the user is presented with HTML Report named **RedundancyReport.html** which presents the user with the redundant file names, their specific locations (paths) and sizes (in bytes, kb, mb, etc).

### Sample VFL console screenshot
![VFL Console Screenshot] (https://github.com/sigmakappa/VFL/blob/master/VFL_console.jpg)

### Updates on future releases:
* Currenly supports only Windows and Video files (hence the V in VFL as currently only video files are supported)
* Implement smarter algorithm to check redundency better and bring out more suggestions on redundancy. 

### Download JAR file

Download the JAR file from [here](https://github.com/sigmakappa/VFL/blob/master/VFL.jar).
