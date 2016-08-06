package bseJava;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public interface printLogs {

	DateTime timeNow = DateTime.now();
    DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy hh:ss:SSS z");
    String str = fmt.print(timeNow);
    
    static void printLog(String text) {
    	System.out.println("[ " + str + " ] " + text);
    }
}
