package school_system;

import java.sql.*;

public class DBConnection {

	/* variables */
	PreparedStatement pstmt;
	Connection conn;
	ResultSet rs;
	
	/* DB Connection Info */
	String IP = "localhost";
	String PORT = "8629";
	String TB_SID = "tibero";
	String userid = "sys";
	String pwd = "tibero";
	
	
	/* constructor */
	public DBConnection() {}
	
	
	/* connect to db */
	public void connect() throws SQLException, ClassNotFoundException {
		
		Class.forName("com.tmax.tibero.jdbc.TbDriver");
		conn = DriverManager.getConnection("jdbc:tibero:thin:@"+IP+":"+PORT+":"+TB_SID, userid, pwd);
		
		if (conn == null) {
			System.out.println("Tibero Connection Failure");
			System.exit(-1);
		}
		System.out.println("Tibero DBConnection Success!");
	}
	
	
	/* close connection */
	public void close() throws Exception {
		try {
			if (pstmt != null) pstmt.close();
			if (rs != null) rs.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* queries */
	
	
	/*
	 * login
	 * 
	 * return 0 on failure
	 * 1 on student success
	 * 2 on instructor success
	 * 
	 */
	public int login(String id, String name) throws SQLException {
		
		String qStudent = "select * from student where id=? and name=?";
		String qInstructor = "select * from instructor where ID=? and name=?";
		
		/* check student table */
		pstmt = conn.prepareStatement(qStudent);
		pstmt.setString(1, id);
		pstmt.setString(2, name);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return 1;
		
		/* check instructor table */
		pstmt = conn.prepareStatement(qInstructor);
		pstmt.setString(1, id);
		pstmt.setString(2, name);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return 2;
		
		return 0;
	}
	
	/*
	 * getStudentCredit
	 * 
	 * return total credit of a student
	 * 
	 * if error return -1
	 */
	public int getStudentCredit(String id) throws SQLException {
		
		String query = "select tot_cred from student where id=?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return rs.getInt(1);
		
		return -1;
	}
	
	/*
	 * getStudentDept
	 * 
	 * return dept name of a student
	 * 
	 * if error return null
	 */
	public String getStudentDept(String id) throws SQLException {
		
		String query = "select dept_name from student where id=?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return rs.getString(1);
		
		return null;
	}
	
	/*
	 * printReport
	 *
	 */
	public void printReport(String id) throws SQLException {
		
		String query = "select year, semester from takes where id=? group by (year, semester) order by year desc, decode(semester, 'Spring', 4, 'Summer', 3, 'Fall', 2, 'Winter', 1)";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		while (rs.next()) { // iterate semester
			
			int year = rs.getInt(1);
			String sem = rs.getString(2);
			
			System.out.print(year + "		" + sem + "   GPA : ");
			
			if (!checkNullGrade(id, year, sem)) System.out.println(calGPA(id, year, sem));
			else System.out.println("");
			
			System.out.println("course_id	title	dept_name	credits	grade");
			
			printCourse(id, year, sem);
			System.out.println("");
		}
	}
	
	/*
	 * checkNullGrade
	 * 
	 * return true on null grade exists
	 * else false
	 */
	public boolean checkNullGrade(String id, int year, String semester) throws SQLException {
		
		String query = "select count(*) from takes where id=? and year=? and semester=? and grade is null";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.setInt(2, year);
		pstmt.setString(3, semester);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next())
			if (rs.getInt(1) > 0) return true;
					
		return false;
	}
	
	/*
	 * calGPA
	 * 
	 */
	public float calGPA(String id, int year, String semester) throws SQLException {
		
		String grToFl = "decode(grade, 'A+', 4.3, 'A', 4.0, 'A-', 3.7, 'B+', 3.3, 'B', 3.0, 'B-', 2.7, 'C+', 2.3, 'C', 2.0, 'C-', 1.7, 'D+', 1.3, 'D', 1.0, 'D-', 0.7, 'F', 0.0)";
		String query = "select avg(" + grToFl + ") from takes where id=? and year=? and semester=?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.setInt(2, year);
		pstmt.setString(3, semester);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.next()) return rs.getFloat(1);
		
		return -1;
	}
	
	/*
	 * printCourse
	 * 
	 */
	public void printCourse(String id, int year, String semester) throws SQLException {
		
		String query = "select course_id, title, dept_name, credits, grade from takes natural join course where id=? and year=? and semester=? order by course_id";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.setInt(2, year);
		pstmt.setString(3, semester);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			
			System.out.print(rs.getString(1) + "	" + rs.getString(2) + "	" + rs.getString(3) + "	" + rs.getInt(4) + " 	");
			
			if (rs.getString(5) != null) System.out.println(rs.getString(5));
			else System.out.println("");
		}
	}
}
