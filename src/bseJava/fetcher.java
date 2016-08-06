package bseJava;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class fetcher {

	public static void main(String[] args) throws UnirestException {
		HttpResponse<JsonNode> jsonResponse = Unirest.post("http://www.omdbapi.com/?i=tt1285016&plot=short&r=json")
//				  .header("accept", "application/json")
//				  .queryString("apiKey", "123")
//				  .field("parameter", "value")
//				  .field("foo", "bar")
				  .asJson();
		
		System.out.println(jsonResponse);
	}

}
