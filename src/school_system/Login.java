package school_system;

import java.sql.SQLException;
import java.util.Scanner;
import java.io.FilterInputStream;

public class Login {
	
	private String id;
	private String name;
	private DBConnection dbc;
	
	
	/* Constructor */
	public Login(DBConnection dbIn) { dbc = dbIn; }
	
	
	/* basic get functions */
	public String getName() { return name; }
	public String getId() { return id; }
	
	
	/* login function
	 * 
	 * Ensures login success when return
	 * return type of menu to control
	 */
	public int login() {
		
		String inId;
		int loginResult = -1;
		String inName;
		Scanner scan = new Scanner(new FilterInputStream(System.in) {
			@Override
			public void close() {
				
			}
		});
		
		System.out.println("Welcome\n");
		
		while (true) {
			System.out.println("Please sign in");
			System.out.print("ID	: ");		
			inId = scan.next();
			
			System.out.print("Name	: ");
			inName = scan.next();
			
			System.out.println("");
			
			try {
				loginResult = dbc.login(inId, inName);
			} catch (SQLException e) {
				System.out.println(e);
				System.exit(-1);
			}
			
			if (loginResult == 1 || loginResult == 2) break;
			else if (loginResult == -1) {
				System.out.println("Error in login query");
				System.exit(-1);
			}
			else if (loginResult == 0) {
				System.out.println("Wrong authentication");
			}
		}	
		
		scan.close();
		
		return loginResult;
	}

}
