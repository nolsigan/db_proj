package school_system;


public class Control {
	
	
	/* main control function */
	public static void main(String[] args) throws Exception {
		
		DBConnection dbc;
		int mode;
		
		/* connect to DB */
		dbc = new DBConnection();
		dbc.connect();
		
		
		/* Login */
		Login ln = new Login(dbc);
		
		mode = ln.login();
		
		
		if (mode == 1) {
			// student menu
			
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
