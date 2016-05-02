package school_system;

import java.sql.*;
import java.util.ArrayList;

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
	
	public String getStudentname(String id) throws SQLException {
		
		String query = "select name from student where id =?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return rs.getString(1);
		
		return null;
	}
	/*
	 * getAdvisee
	 * 
	 * return advisee id list of instructor
	 * 
	 * if error return null
	 */
	public ArrayList<String> getAdviseeId(String id) throws SQLException {
		
		String query = "select s_id from advisor where i_id=?";
		ArrayList<String> result = new ArrayList<String>();
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		while(rs.next()){
			result.add(rs.getString(1));
		}
		
		return result;
	}
}
