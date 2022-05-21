//----------------------------------------------------------------------------
//                             Assignment 3
//  Written by Cleopatr-Aliak Manoukian 40211001 & William Nazarian 40213100
//----------------------------------------------------------------------------

package CSV2HTML;
import java.util.Scanner;
import java.io.*;

/**
 * This program asks the user for a file to convert to html. Then the file's
 * going to be transformed to csv(extension) and then the file will be turned
 * into an html file. The program will then ask the user to choose which file
 * read and will then display that file. If the user does not correctly give
 * the name, then the program will tell the user they only have one more try.
 */
public class CSV2HTML {

	/**
	 * This program uses the ConvertCSVtoHTML method and the readFile method execute the purpose of 
	 * the program which is to convert CSV files to HTML and read a file determined by the user
	 * @param args is a default parameter of the main method. I don't really have a description for it honestly
	 */
	public static void main(String[] args) {

		// define the whiles

		Scanner kb = new Scanner(System.in);
		Scanner br = null;
		BufferedReader sc = null;
		PrintWriter pw = null;
		PrintWriter ew = null;
		
		System.out.println("Welcome to CSV2HTML. We are Nazarian and Manoukian. Please follow the instructions to convert your csv file to html.");

		ConvertCSVtoHTML(kb, pw, br, ew);
		readFile(kb, sc);

		kb.close();

		System.out.println("\nThank you for using CSV2HTML The program is now terminated.");
	}
	
	

	/**
	* This static method converts CSV files to html. All tags are writter and proprely formated. When data is Missing, that row is not converted to html.
	 * If an attribute is missing, the converting automatically stops, and the html file becomes invalid as there are open, but not closed tags.  
	 * @param kb is a scanner who's input stream is the keyboard
	 * @param pw is a PrintWriter who's output stream is an html file that has the same name as the file that is being converted
	 * @param br is a Scanner who's input stream is a csv file determined by the user
	 * @param ew is PrintWriter who's output stream is the exception file who's extension is .log
	 */
	public static void ConvertCSVtoHTML(Scanner kb, PrintWriter pw, Scanner br, PrintWriter ew) {
		String s1 = null, str = null;
		try {

			int count = 0;
			ew = new PrintWriter(new FileOutputStream("Exceptions.log", true));
			System.out.println("Please enter the input file name without the extension:");
			s1 = kb.next();

			System.out.print(s1);
			br = new Scanner(new FileInputStream(s1 + ".csv"));
			System.out.print("input file ok ");

			pw = new PrintWriter(new FileOutputStream(s1 + ".html"));
			System.out.print("output");

			pw.println(
					"<!Doctype>\n<html>\n<style>\ntable{font-family: arial, sans-serif;border-collapse: collapse;}\ntd,th{border: 1px solid #000000;text-align:left;padding: 8px;}");
			pw.println("tr:nth-child(even) {background-color: #dddddd;}\nspan{font-size:small}\n</style>\n<body>");

			str = br.nextLine();
			String arr[] = str.split(",");

			pw.println("<table>\n<caption>");
			pw.println(arr[0] + "</caption>");
			str = br.nextLine();

			arr = str.split(",", -1);
			pw.println("\t<tr>");

			for (String item : arr) {
				if (item.isBlank() || item == null || item == " " || item == "") {
					ew.close();
					pw.close();
					throw new CSVDataMissing("\nERROR: In file " + s1
							+ ".csv. Missing Attribute. File is not converted to HTML : missing data: ICU.");
				}
			}

			pw.println("\t</tr>");
			str = br.nextLine();

			while (br.hasNextLine()) {

				arr = str.split(",", -1);
				String arrnote[] = str.split(":");
				if (arrnote[0].compareToIgnoreCase("Note") == 0)
					break;

				pw.println("\t<tr>");
				count = 2;
				for (String item : arr) {
					count++;
					try {
						pw.println("\t\t<td>" + item + "</td>");
						if (item.isBlank())
							throw new CSVDataMissing("\nWARNING: In file" + s1 + ".csv line " + count
									+ " is not converted to HTML : missing data: ICU.");
					} catch (CSVDataMissing cdm2) {
						ew.println(cdm2.getMessage());
						System.out.print(cdm2.getMessage());
					}

				}
				pw.println("\t</tr>");
				str = br.nextLine();
			}

			
			String arrnote[] = str.split(":");
			if (arrnote[0].compareToIgnoreCase("Note") == 0) {
				arr = str.split(",");
				pw.println("<span>" + arr[0] + "</span>");

			}

			pw.println("\n</body>\n</html>");

			pw.close();
			br.close();
			ew.close();
		} catch (CSVDataMissing cdm1) {
			ew.println(cdm1.getMessage());
			System.out.print(cdm1.getMessage());
			ew.close();
			pw.close();
		} catch (FileNotFoundException e) {

			// here it is initialized as null. so will it work or not is the question

			System.out.println("\nCould not open file " + s1
					+ ".csv for Reading\nProgram will now terminate after closing open files.");
			if (pw != null)
				pw.close();
			if (br != null)
				br.close();
			if (ew != null)
				ew.close();
			System.exit(0);
		}
	}
	
	
/**
 * This static method takes the user input to choose a file that is of type html and displays the contents of the html file on the screen. 
 * @param kb is a scanner who's input stream is the keyboard
 * @param sc is a BufferedReader who's input stream is an html file determined by the user
 */
	public static void readFile(Scanner kb, BufferedReader sc) {
		String s2 = null, str = null;

		try {
			System.out.println("\nPlease enter the html file to read");
			s2 = kb.next();

			File f = new File(s2 + ".html");
			if (!f.exists())
				throw new FileNotFoundException("That file does not exist. You have one more try left.");

		} catch (FileNotFoundException fnf) {
			System.out.print(fnf.getMessage());
			s2 = kb.next();
			File f = new File(s2 + ".html");
			if (!f.exists()) {
				System.out.print("You have no more attempts left. Program will now terminate.");
				kb.close();
				System.exit(0);
			}

		}

		try {
			sc = new BufferedReader(new FileReader(s2 + ".html"));
			str = sc.readLine();
			while (str != null) {
				System.out.print("\n" + str);
				str = sc.readLine();
			}
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}

	}
	
	
	

}
