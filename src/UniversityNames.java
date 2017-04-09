import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
 * Class for getting a list of input university names to compare to master.
 * Reads a CSV file from a url.  Iterates through rows using
 * getNextName.  
 * 
 * Must call closeScanner when finished.
 */
public class UniversityNames {
	
	private Scanner scanner;
	
	/*
	 * Constructor: calls readFromCSV
	 * 
	 * @param url address to csv file of list of all universities
	 */
	public UniversityNames(String url){
		this.readFromCSV(url);
	}
	
	/*
	 * Opens CSV file from url using Scanner.  Removes the first line.
	 * 
	 * @param url
	 */
	public void readFromCSV(String url){
		try {
			//Open file with Scanner
			this.scanner = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter(",");
			
			//Remove first line of labels in CSV
			if(this.scanner.hasNext()==true)
			{
				this.scanner.nextLine();
			}
			else
			{
			    System.out.println("Error: File is empty");
			    return;
			}
		} catch (MalformedURLException e) {
			closeScanner();
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			closeScanner();
			System.out.println(e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
		return; 
	}
	
	/*
	 * Get next university in CSV file.  If there is none return null
	 */
	public String getNextName(){
		//Get contents of scanner. Return null if at end of file
		if(scanner.hasNext()){
			return scanner.nextLine().split(",")[0];
		}else{
			//Call closeScanner here if you don't want a user to have to call it explicitly
			return null;
		}
	}
	
	/*
	 * Closes Scanner
	 */
	public void closeScanner(){
		if(scanner != null){
			scanner.close();
		}
		return;
	}
}
