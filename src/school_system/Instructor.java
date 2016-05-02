package school_system;

public class Instructor {

	DBConnection dbc;
	String id;
	String name;
	
	/* constructor */
	public Instructor(DBConnection dbIn) { dbc = dbIn; }
	public Instructor(DBConnection dbIn, String idIn, String nameIn){
		dbc = dbIn;
		id = idIn;
		name = nameIn;
	}
}
