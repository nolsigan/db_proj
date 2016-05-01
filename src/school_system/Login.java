package school_system;

import java.util.Scanner;

public class Login {
	
	private int id;
	private String name;
	private DBConnection dbc;
	
	private final int STUDENT = 1;
	private final int INSTRUCTOR = 2;
	
	
	/* Constructor */
	public Login(DBConnection dbIn) { dbc = dbIn; }
	
	
	/* basic get functions */
	public String getName() { return name; }
	public int getId() { return id; }
	
	
	/* login function
	 * 
	 * Ensures login success when return
	 * return type of menu to control
	 */
	public int login() {
		
		int inId;
		String inName;		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Welcome\n");
		System.out.println("Please sign in");
		System.out.print("ID	: ");		
		inId = scan.nextInt();
		
		System.out.print("Name	: ");
		inName = scan.next();
		
		System.out.println(inId + ", " + inName);
		
		scan.close();
		
		return 123456789;
	}

}
