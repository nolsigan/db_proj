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
	
	
	/* student queries */
	
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
	
	/* instructor queries */
	
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
			
			if (!checkNullGrade(id, year, sem)) System.out.println( String.format("%.2f", calGPA(id, year, sem)));
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
		String query = "select sum(" + grToFl + " * credits) / sum(credits) from takes natural join course where id=? and year=? and semester=?";
		
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
	
	
	/*
	 * printSemester
	 * 
	 * print semesters of student
	 * 
	 */
	public void printSemester(String id) throws SQLException {
		
		int i = 1;
		String query = "select year, semester from takes where id=? group by (year, semester) order by year desc, decode(semester, 'Spring', 4, 'Summer', 3, 'Fall', 2, 'Winter', 1)";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			
			System.out.println(i + ") " + rs.getString(1) + " " + rs.getString(2));
			i++;
		}
		
	}
	
	
	/*
	 * printTimeTable
	 * 
	 * print time table of chosen semester
	 * 
	 */
	public void printTimeTable(String id, int menu) throws SQLException {
		
		int year;
		String semester;
		
		// get year, semester of given input
		String query = "select year, semester from takes where id=? group by (year, semester) order by year desc, decode(semester, 'Spring', 4, 'Summer', 3, 'Fall', 2, 'Winter', 1)";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		rs = pstmt.executeQuery();
		
		for (int i=0; i < menu; ++i) rs.next();
		
		year = rs.getInt(1);
		semester = rs.getString(2);
		
		
		// get time table of the year, semester
		query = "select course_id, title, day, start_hr, start_min, end_hr, end_min from ((takes natural join section) natural join course) natural join time_slot where ID=? and year=? and semester=?";
		
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.setInt(2, year);
		pstmt.setString(3, semester);
		rs = pstmt.executeQuery();
		
		System.out.println("\ncourse_id	title	day	start_time	end_time");
		
		while (rs.next()) {
			
			System.out.println(rs.getString(1) + "	" + rs.getString(2) + "	" + rs.getString(3) + "	" + rs.getInt(4) + " : " + rs.getInt(5) + "	" + rs.getInt(6) + " : " + rs.getInt(7));
		}
		System.out.println("");
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

/*
 * getTime
 * 
 * return pretty time using time_slot_id
 * 
 * if error return null
 */
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

/*
 * printSectionInfo
 * 
 * print SectionInformation
 */
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

/*
 * printCourseStudent
 * 
 * print students who have taken a course
 */
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

/*
 * printTeachingCourse
 * 
 * print Instructor's course report
 */
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
