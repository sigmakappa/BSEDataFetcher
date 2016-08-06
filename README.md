#BSE Data Fetcher
#####Get Equity OHLC data from Bombay Stock Exchange of India 
======================================================

This utility presents the user with daily OHLC (Open, High, Low, Close prices) data w.r.t different equities present in different market segments (like Automobiles, Cotton, Petroleum, Steel, etc) and saves it to the specific equity's corresponding CSV file. The respective CSV file(s), upon daily execution of the project, accumulate daily OHLC data over time. 

#####What to do with the CSV file(s)?
The CSV files contain data when accumulated with data over a period of time (minimum 14 days) can be used in Equity Price Trend Analysis (read more about it here [Technical Analysis Wikpedia](https://en.wikipedia.org/wiki/Technical_analysis)). These trend analysis reports can help in making better decisions in stock trading.


### Usage
Requires Java 1.8.0 or above.

For running the associated jar, simply unzip the zip file associated below and navigate to the directory where the jar is placed and run:
```
java -jar BSEDataFetcher.jar
```
Post running the jar, the user is presented with CSV files in associated directory **EquityData** which contains the OHLC data for today for the specific equity, appended to the bottom of the CSV file.

### Updates on future releases:
There can be unlimited uses to an equity's long time OHLC data. For the time, focus is to apply different models of predictive analysis and follow the best combined indication.  

### Download the ZIP (containing the JAR) file

Download the ZIP file from [here](https://github.com/sigmakappa/BSEDataFetcher/blob/master/BSEDataFetcher.zip).
