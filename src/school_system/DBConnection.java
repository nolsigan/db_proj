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
	
	/*
	 * getStudentName
	 * 
	 * return name of a student
	 * 
	 * if error return null
	 */
	public String getStudentName(String id) throws SQLException {
		
		String query = "select name from student where id =?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if (rs.next()) return rs.getString(1);
		
		return null;
	}
	
	/*
	 * getRecentSemester
	 * 
	 * return Recent semester and year of instructor
	 * [semester ; year]
	 * if error return null
	 */
	public ArrayList<String> getRecentSemester(String id) throws SQLException {
		
		String query = "select * from teaches where id = ? order by year desc, (case semester when 'Winter' then 0 when 'Fall' then 1 when 'Summer' then 2 when 'Spring' then 3 end)";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		if(rs.next()){
			ArrayList<String> result = new ArrayList<String>();
			result.add(rs.getString(4)); //semester
			result.add(rs.getString(5)); //year
			return result;
		}
		
		return null;
	}
	
	/*
	 * getCourseName
	 * 
	 * return name of a course
	 * 
	 * if error return null
	 */
	public String getCourseName(String course_id) throws SQLException {
		
		String query = "select title from course where course_id = ?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, course_id);
		ResultSet temp_rs = pstmt.executeQuery();
		
		if (temp_rs.next()){
			String name = temp_rs.getString(1);
			temp_rs.close();
			return name;
		}
		
		temp_rs.close();
		return null;
	}
	
	public String getTime(String time_slot_id) throws SQLException {

		String query = "select day, start_hr, start_min, end_hr, end_min from time_slot where time_slot_id = ?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, time_slot_id);
		ResultSet temp_rs = pstmt.executeQuery();
		
		if (temp_rs.next()){
			String result = temp_rs.getString(2) +" : "+ temp_rs.getString(3) +" - "+ temp_rs.getString(4) +" : "+ temp_rs.getString(5) +")";
			result = temp_rs.getString(1) +" "+ result;
			
			while(temp_rs.next()){
				result = temp_rs.getString(1) +", "+ result;
			}
			
			temp_rs.close();
			return "("+ result;
		}
		
		temp_rs.close();
		return null;
	}
	
	public void printSectionInfo(String course_id, String sec_id, String semester, String year) throws SQLException{
		String query = "select building, room_number, time_slot_id from section	where course_id = ? and sec_id = ? and semester = ? and year = ?";
		String title = getCourseName(course_id);
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, course_id);
		pstmt.setString(2, sec_id);
		pstmt.setString(3, semester);
		pstmt.setString(4, year);
		ResultSet temp_rs = pstmt.executeQuery();
		
		
		if (temp_rs.next()){
			String building = temp_rs.getString(1);
			String room = temp_rs.getString(2);
			String time = getTime(temp_rs.getString(3));
			
			System.out.println("\n"+ course_id +" "+ title +" ["+ building +" "+ room +"] "+ time);
		}
		
		temp_rs.close();
		return;
	}
	
	public void printCourseStudent(String course_id, String sec_id, String semester, String year) throws SQLException {
		String query = "select id, name, dept_name, grade from student natural join takes where course_id = ? and sec_id = ? and semester = ? and year = ?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, course_id);
		pstmt.setString(2, sec_id);
		pstmt.setString(3, semester);
		pstmt.setString(4, year);
		ResultSet temp_rs = pstmt.executeQuery();
		//ID name dept_name grade
		
		System.out.println("ID	name	dept_name	grade");
		while(temp_rs.next()){
			System.out.println(temp_rs.getString(1) +"	"+ temp_rs.getString(2) +"	"+ temp_rs.getString(3) +"	"+ temp_rs.getString(4));
		}
		
		temp_rs.close();
		return;
	}
	
	public void printTeachingCourse(String id, String semester, String year) throws SQLException {
		String query = "select course_id, sec_id from teaches where id = ? and semester = ? and year = ?";
			
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.setString(2, semester);
		pstmt.setString(3, year);
		
		rs = pstmt.executeQuery();
		
		/* print course_id title [building room_no] (time_slot) */
		while(rs.next()){
			String course_id = rs.getString(1);
			String sec_id = rs.getString(2);
			printSectionInfo(course_id, sec_id, semester, year);
			printCourseStudent(course_id, sec_id, semester, year);
		}
		System.out.println("");
		return;
	}
	
	/*
	 * getAdvisee
	 * 
	 * return advisee id list of instructor
	 * 
	 * if error return empty list
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
