package school_system;

import java.sql.SQLException;
import java.util.ArrayList;

public class Instructor {

	DBConnection dbc;
	String id;
	String name;
	
	/* constructor */
	public Instructor(DBConnection dbIn, String idIn, String nameIn){
		dbc = dbIn;
		id = idIn;
		name = nameIn;
	}
	
	/* Course report */
	public void course() throws SQLException {
		
		ArrayList<String> list = dbc.getRecentSemester(id);
		
		if(list!=null){
			String semester = list.get(0);
			String year = list.get(1);
			
			System.out.println("Course report - " + year + " " + semester);
			
			dbc.printTeachingCourse(id, semester, year);
		}
		/* never taught anything */
		else{
			
		}
		System.out.println("");
	}
	
	/* Advisee report */
	public void advisee() throws SQLException {
		
		ArrayList<String> list = dbc.getAdviseeId(id);
		System.out.println("ID	 name	 dept_name	 tot_cred");
		
		for(int i = 0; i<list.size() ; i++){
			String adv_name = dbc.getStudentName(list.get(i));
			String adv_dept = dbc.getStudentDept(list.get(i));
			int adv_cred = dbc.getStudentCredit(list.get(i));
			System.out.println(list.get(i) + "	" + adv_name + "	" + adv_dept + "	" + adv_cred);
		}
		
		System.out.println("");
	}
}
