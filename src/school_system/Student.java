package school_system;

import java.sql.SQLException;

public class Student {
	
	DBConnection dbc;
	String id;
	String name;
	
	/* constructor */
	public Student(DBConnection dbIn, String idIn, String nameIn) {
		dbc = dbIn;
		id = idIn;
		name = nameIn;
	}
	
	
	/* student report */
	public void report() throws SQLException {
		
		String dept;
		int credit;
		
		dept = dbc.getStudentDept(id);
		credit = dbc.getStudentCredit(id);
		
		System.out.println("\nWelcome " + name);
		System.out.println("You are a member of " + dept);
		System.out.println("You have taken total " + credit + " credits");
		System.out.println("\nSemester report\n");
		
		dbc.printReport(id);
	}	

}
