package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import util.MysqlConnection;


/**
 *  这个类用于自动建表，保证当数据传递过来，有新的表可以直接写入数据。
 *  可以在服务器端，启动一个线程，执行下面的代码。
 *  
 * @author jq
 *
 */

public class TableAtuoCreat {
	static MysqlConnection mysql = new MysqlConnection();
	static Connection conn = mysql.getConnection();
	static int dayStamp = 24*60*60*1000;
	static int period = 7;
	static String creatTableSql = "  ("+
										  "`roadName` varchar(64)  NOT NULL, "+
										  "`segId` varchar(64)  NOT NULL, "+
										  "`time` varchar(32)  NOT NULL default '0000-00-00', "+
										  "`direction` int(2)  NOT NULL, "+
										  "`speed` double default NULL, "+
										  "PRIMARY KEY  (`roadName`,`segId`,`time`,`direction`) "+
										") ENGINE=MyISAM DEFAULT CHARSET=utf8;";
	public static void autoCreatTable(){
		Date todayDate = new Date();
		long curStamp = todayDate.getTime(); // 当前时间戳
		List<String> tableNamesList = tableNameList(curStamp);
		// 判断是否存在表
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			Statement ps = conn.createStatement();
			
			int size = tableNamesList.size();
			ResultSet rt;
			String oneTableNameString;
			for(int j =0; j < size; j++){
				oneTableNameString = tableNamesList.get(j);
				rt = metaData.getTables(null, null, oneTableNameString , null);
				if(rt.next()){
					continue;
				}
				ps.executeUpdate("CREATE TABLE "+ oneTableNameString+creatTableSql   );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static List<String> tableNameList(long cur){
		List<String> ans = new LinkedList<String>();
		DateFormat format=new SimpleDateFormat("yyyyMMdd");
		for(int i =0 ; i < period ; i++){
			ans.add("report"+format.format(new Date(cur+i*dayStamp)));
		}
		//System.out.println(ans);
		return ans;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		autoCreatTable();
	}

}
