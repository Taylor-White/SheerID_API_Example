import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/* 
 * Class for getting and storing a list of universities stored at SheerID.
 * Requires a url to an API call which returns a JSON including a university "name" key.
 * 
 * Stores results in a sorted ArrayList.  
 * 
 * Provides compareName functionality to compare an input name with elements in the stored list.
 */
public class MasterData {
	
	private ArrayList<String> universities;
	
	/*
	 * Creates ArrayList of Strings from the callAPI method. Then sorts the list
	 * 
	 * @param url Must be a url to an API which returns a JSON including a "name" key.
	 */
	public void buildList(String url){
		//Create list of universities from API call
		universities = this.callAPI(url);
		
		//Sort List
		universities = this.sortList(universities);
		
		return;
	}
	
	/*
	 * Sorts and returns an ArrayList
	 * 
	 * @param arr
	 */
	public ArrayList<String> sortList(ArrayList<String> arr){
		arr.sort(String::compareToIgnoreCase);
		return arr;
	}
	
	/*
	 * Requests a JSON of universities from the SheerID API. Calls method putIntoList to put the response
	 * into an ArrayList of university names
	 * 
	 *  @param url Must be a url to an API which returns a JSON including a "name" key.
	 */
	private ArrayList<String> callAPI(String url_string){
		ArrayList<String> list = new ArrayList<String>();
		
		//Connect to url
		URL url;
		try {
			url = new URL(url_string);
		
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");
	
	        //Check response code
	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	        }
	        
	        //put response into list
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	        list = putIntoList(br);
	        conn.disconnect();
		} catch (MalformedURLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
		return list;
	}
	
	/*
	 * Uses BufferedReader to get JSON and put universities in an ArrayList
	 * 
	 * @param br BufferedReader with an InputStreamReader with a HttpURLConnection
	 */
	private ArrayList<String> putIntoList(BufferedReader br){
		String output = "";
		int array_size;
		
		//Put JSON into String
		System.out.println("Output from Server .... \n");
		try {
			output = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Parse JSON
		JSONParser parser = new JSONParser();
		JSONArray jsonArray;
		try {
			jsonArray = (JSONArray) parser.parse(output);
		} catch (ParseException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return null;//TODO: Caller needs to handle this null case.
		}
       
        //Put JSON university "name" values into ArrayList
		array_size = jsonArray.size();
	    ArrayList<String> list = new ArrayList<String>(array_size);
	    for(Object jsonObject : jsonArray){
	    	JSONObject university = (JSONObject) jsonObject;
	    	String name = (String) university.get("name");
	    	list.add(normalizeUniversityName(name));
	    }
	    return list;
	}
	
	/*
	 * Prints all elements from the universities ArrayList
	 */
	public void walkData(){
		for(int i=0; i<universities.size(); i++){
			System.out.println(universities.get(i));
		}
		return;
	}	
	
	/*
	 * Prints first i elements from the universities ArrayList
	 */
	public void walkData(int num){
		for(int i = 0;i<num;i++){
			System.out.println(universities.get(i));
		}
		return;
	}	
	
	/*
	 * Searches for a specific element in the data
	 * 
	 * TODO: Can be more involved if universities are stored using a different data structure.
	 * 
	 * For example:  If the location of the first letters in the ArrayList are stored, searchData can start
	 * its search at the location of the inputs first letter to save time.
	 * 
	 * @param name
	 */
	public boolean searchData(String name){
		//If string is null, return false
		if (name == null){return false;}
		
		//Search for university in SheerID Master list.  If it is found, result will be greater or equal to 0.  Otherwise result will be negative.
		int result = Collections.binarySearch(universities, name);

		//Returns true if element is in the list, false otherwise
		return (result >= 0) ? true : false;
	}	
	
	/*
	 * Normalizes a string. Removes special characters and extra whitespace.
	 * 
	 * @param srs
	 */
	private String normalizeUniversityName(String str){
		//Normalizes.  Removes special characters and extra whitespace.
		String new_str = str.replaceAll("[^a-zA-Z0-9 +]", "").replaceAll(" +", " ").toLowerCase().trim();
		return new_str;
	}
	
	/*
	 * Method comparing a university name. 
	 * 
	 * TODO: Could call searchData multiple times to include abbreviations or acronyms. Could
	 * also call a modified searchData that allows for regular expressions, typos e.g. swapped letters, etc. to allow search 
	 * to find more accurate matches.
	 * 
	 * @param str
	 */
	public boolean compareName(String str){
		boolean exists = searchData(normalizeUniversityName(str));
		
		// TODO Add functionality to flag similar names, such as ones which differ by acronym or abbreviation etc. 
		
		return exists;
		
	}
}
