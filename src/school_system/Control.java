package school_system;


public class Control {
	
	
	/* main control function */
	public static void main(String[] args) throws Exception {
		
		DBConnection dbc;
		int mode;
		
		String name;
		String id;
		
		/* connect to DB */
		dbc = new DBConnection();
		dbc.connect();
		
		
		/* Login */
		Login ln = new Login(dbc);
		
		mode = ln.login();
		name = ln.getName();
		id = ln.getId();
		
		
		if (mode == 1) {
			// student menu
			
			System.out.println("Please select student menu");
			System.out.println("1) Student report");
			System.out.println("2) View time table");
			System.out.println("3) Exit");
			
		} else if (mode == 2) {
			// instructor menu
			
		} else {	
			System.out.println("login failure!");
			System.exit(-1);
		}
		
		
		dbc.close();
	}
	
	
	/* student manage function */
	public void student() {
		
	}
	
	
	/* instructor manage function */
	public void instructor() {
		
	}


}
