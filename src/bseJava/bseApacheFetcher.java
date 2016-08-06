package bseJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class bseApacheFetcher implements printLogs, zipper {

	static String preText = "BSEData\\";
	static ArrayList<String> bseEquities = new ArrayList<String>();

	public static void main(String[] args) throws Exception {

		// THE EQUITY DATA SOURCE FILES LOCATION
		File equityDirectory = new File("EquityData\\");

		// get all the files from the equity directory
		File[] equityFilesList = equityDirectory.listFiles();

		// Take backup till yesterday's records
		zipper.zipFiles(equityFilesList);

		// THE CATEGORY SOURCE FILES LOCATION
		File directory = new File("BSEData\\");

		// get all the files from a directory
		File[] fList = directory.listFiles();

		// Print 'em
		for (int i =0; i <= fList.length - 1; i++) {
			//System.out.println(fList[i].toString());
			//frequencyCounter(fList[i].toString(), counterData, depth);

			// Prepare equity list
			prepareLocalEquityDataList(fList[i].toString(), bseEquities);
		}

		for (int i = 0; i < bseEquities.size(); i++) {

			//get ticker code
			String recordData[] = bseEquities.get(i).split(",");

			// Get data w.r.t a ticker code
			printLogs.printLog("Getting equity data for " + recordData[1] + "...");
			String data = getEquityData(recordData[0]);

			// Commenting for now; add file saver later here
			// Get today's prices; recordData[1] is Security Id as 'SBIN' in State Bank of India
			getPrices(recordData[1], data);
		}

	}

	/**
	 * Prepares the list of equities based on market segment 
	 * 
	 * @param files - Location where the BSE Segment files are located
	 * @param bseEquities2 - List of equities prepared from the equity segment files
	 * 
	 * @author Shagun Kaushik [shagunprakash19@gmail.com]
	 */
	private static void prepareLocalEquityDataList(String file, ArrayList<String> bseEquities2) throws Exception {

		printLogs.printLog("Preparing equity list for category " + file.replace(preText, "").replace(".csv", "") +" ...");

		// Security Code, Security Id, Security Name, Status, Group, Face Value, ISIN No, Industry, Instrument
		final String [] FILE_HEADER_MAPPING = {"Security Code", "Security Id", "Security Name", "Status", "Group", "Face Value", "ISIN No", "Industry", "Instrument"};
		FileReader fileReader = null;
		CSVParser csvFileParser = null;

		//Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

		//Create a new list of student to be filled by CSV file data 
		List<String> localEquities = new ArrayList<String>();

		//initialize FileReader object
		fileReader = new FileReader(file);

		//initialize CSVParser object
		csvFileParser = new CSVParser(fileReader, csvFileFormat);

		//Get a list of CSV file records
		List csvRecords = csvFileParser.getRecords(); 
		int size = csvRecords.size();

		for (int i = 1; i < size ; i++) {

			// Get a record from row i
			CSVRecord record = (CSVRecord) csvRecords.get(i);

			// Get data from csv file
			String data = record.get("Security Code") + "," + record.get("Security Id");

			// Add data collected above to localEquities
			localEquities.add(i -1, data);
		}

		// Add all the equity data from the market segment to overall BSE Data List
		bseEquities.addAll(localEquities);

		printLogs.printLog("Successfuly prepared equity list for category " + file.replace(preText, "").replace(".csv", "") +" ...");
	}

	/**
	 * Prepares local data for a particular equity, later to be saved in its local file
	 * 
	 * @param id - Unique ID of the Equity whose data we are dealing with here
	 * @param data - The raw data from the REST Response containing the OHLC data w.r.t to the same day
	 * 
	 * @author Shagun Kaushik [shagunprakash19@gmail.com]
	 */
	private static void getPrices(String id, String data) throws Exception {
		String[] values = data.split("#");
		String value = values[values.length-1];

		printLogs.printLog("Equity data for " + id + " >>> " + value);

		String[] prices = value.split(",");

		String previousClose = prices[0].trim();
		String todayOpen = prices[1].trim();
		String todayHigh = prices[2].trim();
		String todayLow = prices[3].trim();
		String todayClose = prices[4].trim();

		//		System.out.println("ID  >> " + id);
		//		System.out.println("previousClose  >> " + previousClose);
		//		System.out.println("todayOpen  >> " + todayOpen);
		//		System.out.println("todayHigh  >>" + todayHigh );
		//		System.out.println("todayLow  >>" + todayLow);
		//		System.out.println("todayClose  >>" + todayClose);

		//		184.95,        		187.10, 	 189.40, 	  185.45, 	  188.40
		//		Previous Close,	  Open Price,  Today High,  Today Low,  Today Close

		// Get the file to be created/updated
		String folder = "EquityData\\";
		String fileToBeCreatedOrUpdated = new String(folder + id + ".csv");

		File equityFile = new File(fileToBeCreatedOrUpdated);
		if (equityFile.exists() && !equityFile.isDirectory()) { 
			updateAndSaveTodayReport(previousClose, todayOpen, todayHigh, todayLow, todayClose, id);
		}
		else
			createAndSaveTodayReport(previousClose, todayOpen, todayHigh, todayLow, todayClose, id);

	}

	/**
	 * Updates and Saves today's data to equity's CSV File (pre-existing) 
	 * 
	 * @param previousClose - Yesterday Close Rate
	 * @param todayOpen - Today Open Rate
	 * @param todayHigh - Today's Highest Rate
	 * @param todayLow - Today's Lowest Rates
	 * @param todayClose - Today's Closing Rates
	 * @param id - Unique ID of the Equity whose data we are dealing with here
	 * 
	 * @author Shagun Kaushik [shagunprakash19@gmail.com]
	 */
	private static void updateAndSaveTodayReport(String previousClose, String todayOpen, String todayHigh, String todayLow, String todayClose, String id) throws Exception {
		// Getting today's date
		DateTime timeNow = DateTime.now();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy");
		String dateToday = fmt.print(timeNow);

		String dataToBeUpdated = "\n" + dateToday + "," + previousClose + "," + todayOpen + "," + todayHigh + "," + todayLow + "," + todayClose;

		// Get a new output file and print writer
		String folder = "EquityData\\";
		String fileoutput = new String(folder + id + ".csv");
		Files.write(Paths.get(fileoutput), dataToBeUpdated.getBytes(), StandardOpenOption.APPEND);
	}

	/**
	 * Creates the equity's CSV file and Saves today's data to it 
	 * 
	 * @param previousClose - Yesterday Close Rate
	 * @param todayOpen - Today Open Rate
	 * @param todayHigh - Today's Highest Rate
	 * @param todayLow - Today's Lowest Rates
	 * @param todayClose - Today's Closing Rates
	 * @param id - Unique ID of the Equity whose data we are dealing with here
	 * 
	 * @author Shagun Kaushik [shagunprakash19@gmail.com]
	 */
	private static void createAndSaveTodayReport(String previousClose, String todayOpen, String todayHigh, String todayLow, String todayClose, String id) throws Exception {

		// Getting today's date
		DateTime timeNow = DateTime.now();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy");
		String dateToday = fmt.print(timeNow);

		// Get a new output file and print writer
		String folder = "EquityData\\";
		FileOutputStream fileoutput = new FileOutputStream(folder + id + ".csv");
		PrintWriter pw = new PrintWriter(fileoutput);

		pw.println("Date,Previous Close,Today Open,Today High,Today Low,Today Close");

		// Writing today's data its file
		pw.println(dateToday + "," + previousClose + "," + todayOpen + "," + todayHigh + "," + todayLow + "," + todayClose);	
		pw.close();

	}


	/**
	 * Gives the raw response of the REST Request w.r.t an rquity to get its OHLC Data 
	 * 
	 * @param tickerCode - Ticker Code is the unique code of the equity
	 * @return Raw response to the REST Request to get the OHLC Data of equity 
	 * 
	 * @author Shagun Kaushik [shagunprakash19@gmail.com]
	 */
	private static String getEquityData(String tickerCode) throws Exception {
		// Source: http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e49
		// Path to main tutorial: 
		URI uri = new URIBuilder()
				.setScheme("http")
				.setHost("www.bseindia.com/stock-share-price/SiteCache/EQHeaderData.aspx")
				.setParameter("text", tickerCode)
				.build();
		HttpGet httpget = new HttpGet(uri);
		URI uriAsString = httpget.getURI();

		// Prints -->  //  http://www.omdbapi.com/?i=tt1285016&plot=short&r=json
		//System.out.println(uriAsString);

		// For printing the JSON

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uriAsString);

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// prints the acquired JSON
		// the below line would print json in a single line so using gson method after that
		httpClient.close();

		return response.toString();
	}

}
