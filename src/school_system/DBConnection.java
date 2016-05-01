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
}
