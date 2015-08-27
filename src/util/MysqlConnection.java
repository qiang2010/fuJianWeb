package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;



public class MysqlConnection {


	Connection con=null;
	Statement stat = null;
	public Connection getConnection(){
		Properties pro = new Properties();
		
		String driver = null;
		String mysqlUrl = null;
		String user = null;
	    String password = null;
		try {
			pro.load(this.getClass().getClassLoader().getResourceAsStream("DB.properties"));
			driver = pro.getProperty("driver");
			mysqlUrl = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
			Class.forName(driver);
			con = DriverManager.getConnection(mysqlUrl, user, password);
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
