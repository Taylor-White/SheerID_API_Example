import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 * Application that compares a master SheerID set of universities
 * to a complete set of universities to find missing universities.
 * 
 * Requires ability to connect to two urls, one of which is the SheerID API which returns a JSON with "name" as a key, and
 * another which is a CSV file.  The first is stored, sorted and normalized.  The second will check if each name can be
 * found in the first.
 * 
 * @author Taylor White
 * 
 */
public class CompareUniversityNamesWithMaster {
	
	//URLs with data for universities to compare
	static final String UNIVERSITY_NAMES_MASTER_URL = "https://services-sandbox.sheerid.com/rest/0.5/organization?type=UNIVERSITY";
	static final String UNIVERSITY_NAMES_COMPLETE_URL = "http://www.studentclearinghouse.org/colleges/enrollmentverify/Participation-List.csv";

	public static void main(String[] args) throws IOException, ParseException{
		
		//Initialize SheerID Master Data Class
		MasterData masterData = new MasterData();
		masterData.buildList(UNIVERSITY_NAMES_MASTER_URL);
		
		//Initialize Complete University List Class
		UniversityNames universityNames = new UniversityNames(UNIVERSITY_NAMES_COMPLETE_URL);

		//Print the universities that are not found in SheerID Master list
		System.out.println("Universities not found in SheerID Master list: \n");		
		String next_name = universityNames.getNextName();
		while(next_name != null){
			if(!masterData.compareName(next_name)){
				System.out.println(next_name);
			}
			next_name = universityNames.getNextName();
		}
		//Close Scanner
		universityNames.closeScanner();
	}
}
