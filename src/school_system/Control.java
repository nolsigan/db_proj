package school_system;

import java.io.FilterInputStream;
import java.util.Scanner;

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
			
			String menu = "";
			Scanner scan = new Scanner(new FilterInputStream(System.in) {
				@Override
				public void close() {
					
				}
			});
			
			Student st = new Student(dbc, id, name);
			
			while (true) {
				System.out.println("Please select student menu");
				System.out.println("1) Student report");
				System.out.println("2) View time table");
				System.out.println("0) Exit");
				
				System.out.print(">> ");	
				menu = scan.next();
				
				if (menu.equals("1")) {
					// student report			
					st.report();
				} else if (menu.equals("2")) {
					// time table
					st.semesterMenu();
					System.out.print(">> ");
					
					int sMenu = scan.nextInt();
					
					st.timeTable(sMenu);
					
				} else if (menu.equals("0")) break;
				
				System.out.println("");
			}
		
			scan.close();
			
		} else if (mode == 2) {
			// instructor menu
			int menu;
			Scanner scan = new Scanner(new FilterInputStream(System.in) {
				@Override
				public void close() {}
			});
			
			Instructor inst = new Instructor(dbc, id, name);
			
			while(true){
				
				System.out.println("Please select instructor menu");
				System.out.println("1) Course report");
				System.out.println("2) Advisee report");
				System.out.println("0) Exit");
				
				System.out.print(">> ");
				menu = scan.nextInt();
				
				if(menu == 1){
					inst.course();
					continue;
				}
				if(menu == 2){
					inst.advisee();
					continue;
				}
				if(menu == 0) break;
				
			}
			
			scan.close();
		} else {	
			System.out.println("login failure!");
			System.exit(-1);
		}
		
		
		dbc.close();
	}
	

}
