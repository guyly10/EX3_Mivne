import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainClass {

	private static Map<Company, SpecialTree> companyMap;
	
	//Change this boolean for easier debugging
	private static Boolean debugVersion = true;
	public static void main(String[] args) throws FileNotFoundException {
		// Initialize
		
		if(debugVersion)
		{
			PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);	
		}
		
		
		companyMap = new HashMap<>();

		parseJSON();

	}

	/**
	 * Parsing of the received data.dat file
	 */
	public static void parseJSON() {
		try (BufferedReader br = new BufferedReader(new FileReader("src/data.dat"))) {
			SpecialTree tree = null;
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				StringTokenizer st = new StringTokenizer(line);
				String cmd = st.nextToken();
				Company company = new Company(st.nextToken());
				System.out.println("================" + cmd + "===================");
				if(!companyMap.containsKey(company))
				{
					if(!cmd.equals("company"))
					{
						System.out.println("Skipped command " + cmd + ". Company " + company + " was not found.");
						continue;	
					}
				}
				else
				{
					tree = companyMap.get(company);
				}
				
				
				switch (cmd) {
				case "company":
					System.out.println("Generating new company " + company);
					tree = new SpecialTree(company);
					companyMap.put(company, tree);
					break;

				case "department":
					String depString = st.nextToken();
					System.out.println("Generating new department " + depString);
					if (!companyMap.containsKey(company)) {
						System.out.println("Could not find company " + company);
						break;
					}
					tree.addDepartment(depString, st.nextToken());

					break;

				case "employee":
					tree.addEmployee(st.nextToken(), st.nextToken());
					break;
				case "printTree":
					System.out.println("Request to print tree received");
					tree.printTree(tree.getHead());
					break;
				case "vacation":
					tree.takeVacation(st.nextToken(), st.nextToken());
					break;
				case "merge":
					tree.mergeDepartments(st.nextToken(), st.nextToken());
					break;
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
