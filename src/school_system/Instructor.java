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
		
		String semester;
		
	}
	
	/* Advisee report */
	public void advisee() throws SQLException {
		
		ArrayList<String> list = dbc.getAdviseeId(id);
		System.out.println("ID	 name	 dept_name	 tot_cred");
		
		for(int i = 0; i<list.size() ; i++){
			String adv_name = dbc.getStudentname(list.get(i));
			String adv_dept = dbc.getStudentDept(list.get(i));
			int adv_cred = dbc.getStudentCredit(list.get(i));
			System.out.println(list.get(i) + "	" + adv_name + "	" + adv_dept + "	" + adv_cred);
		}
		
	}
}
