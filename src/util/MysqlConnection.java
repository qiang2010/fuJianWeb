package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class MysqlConnection {

	String mysqlUrl = "jdbc:mysql://localhost:3306/roadfujian?user=root&password=jq";
	Connection con=null;
	Statement stat = null;
	public Connection getConnection(){
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(mysqlUrl);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
		//stmt = con.createStatement();
	}
	public Statement getStatement(Connection con){
		try {
			stat =  con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stat;
	}
	
	public void close(){
		if(stat != null){
			try {
				stat.close();
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
