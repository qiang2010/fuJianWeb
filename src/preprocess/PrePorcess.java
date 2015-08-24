package preprocess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import util.FileUtil;



/**
 * 读取文件中的数据，并导入到数据库中
 * @author jq
 *
 */
public class PrePorcess {

	String roadFileName = "E:\\实验室工作\\前段显示设计\\需要导入的前期数据\\福建路段划分.txt";
	String reportFile = "E:\\实验室工作\\前段显示设计\\需要导入的前期数据\\离线结果\\";
	String mysqlUrl = "jdbc:mysql://localhost:3306/roadfujian?user=root&password=jq";
	String fileRepoPosfix = "_20150615.txt";
	String lines[] = {"G15","G70","G72","G76","G1501","G1514","S35","G3","G25","G319","G324"};
	//String lines[] = { "G25","G319","G324" };
	public void insertRoadInfoIntoDatabase(){
		
		FileUtil roadFile = new FileUtil(reportFile);
		String tempLine;
		String []detail;
		Connection con;
		Statement stmt;
	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(mysqlUrl);
			stmt = con.createStatement();
			int count=1;
			while((tempLine = roadFile.readLine())!=null){
				detail = tempLine.split("\\s{1,}");
				if(detail == null || detail.length <7) continue;
				//if(falg)break;
				String insertSql = "insert into roadinfo(roadName,segId,cellLong,cellLat,"
						+ "startLong,startLat,dis,ramp) values('"+detail[0]+"',"+detail[1]+","+detail[2]
						+","+detail[3]+","+detail[4]+","+detail[5]+","+detail[6]+","+detail[7]+ ")";
				System.out.println(tempLine);
				System.out.println(insertSql);
				stmt.addBatch(insertSql);
				System.out.println(count);
				if(count%100==0)stmt.executeBatch();
				count++;
			}
			stmt.executeBatch();
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void insertReportData(){
	
		FileUtil roadFile;
		String tempLine;
		String []detail;
		Connection con;
		Statement stmt;
	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar  = Calendar.getInstance();
		FileUtil fileSql = new FileUtil(reportFile+"insertSql.txt");
		Date date;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(mysqlUrl);
			stmt = con.createStatement();
			int count=1;
			for(String tempRoad:lines){
				roadFile = new FileUtil(reportFile+tempRoad+fileRepoPosfix);
				while((tempLine = roadFile.readLine())!=null){
					detail = tempLine.split("\t");
					if(detail == null || detail.length != 4) continue;
					//if(falg)break;
					date = simpleDateFormat.parse(detail[0]);
					//calendar.setTime(date);
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					String insertSql = "insert into report20150615(roadName,segId,time,direction,"
						+ "speed) values('"+tempRoad+"',"+detail[1]+","+((sqlDate.getTime()))+","+detail[2]
						+","+detail[3]+")";
					//System.out.println(tempLine);
					//System.out.println(insertSql);
					fileSql.writeLine(insertSql);
					stmt.addBatch(insertSql);
					//if(count%1000==0) stmt.executeBatch();
					System.out.println(count);
					count++;
					//if(count == 392010) return ;
				}
				stmt.executeBatch();
			}
			stmt.close();
			con.close();
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fileSql.close();
			//roadFile.close();
		}
	}
	
	public static void main(String []args){
		//PrePorcess pp = new PrePorcess();
		//pp.insertReportData();
		
		
	}
	
}
