package app.prepareData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MysqlConnection;
import app.entity.Line;
import app.entity.ReportDetail;
import app.entity.RoadInfo;

public class PrepareData {

	String tableName = "report20150615";
	
	private static PrepareData uniqueInstance = null;
	boolean first = true;
	private PrepareData(){
		allRoadDetail = getRoadInfo();  
	}
	public static PrepareData  getInstance(){
		if(uniqueInstance == null){
			synchronized(PrepareData.class){
				if(uniqueInstance == null ){
					uniqueInstance = new PrepareData();
				}
			}
		}
		return uniqueInstance;
	}
	
	// ������� ʱ�� ת����ʱ��������ڲ�ѯ
	public String changeToTimeStamp(String date){
		// ʱ��ĸ�ʽӦ��Ϊ  2015-06-15 12:30:00
		
		
		 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );


		 Date timeS =null;
		 try {
			timeS = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return ""+timeS.getTime();
	}
	static HashMap<String ,RoadInfo> allRoadDetail= null;
	public static HashMap<String,RoadInfo> getRoadInfo(){
		allRoadDetail = new HashMap<String,RoadInfo>();
		MysqlConnection con = new MysqlConnection();
		Statement stat = con.getStatement(con.getConnection());
		String selectSql = "select * from roadinfo";
		
		ResultSet rs ;
		RoadInfo tempDetail;
		String roadName,key;
		int segId ;
		try {
			rs = stat.executeQuery(selectSql);
			while(rs.next()){
				tempDetail = new RoadInfo();
				roadName = rs.getString("roadName");
				tempDetail.setRoadName(roadName);
				segId = rs.getInt("segId");
				tempDetail.setSegId(segId);
				//tempDetail.setSpeed(rs.getDouble("speed"));
//				tempDetail.setTime(time);
				tempDetail.setStartLat(rs.getDouble("startLat"));
				tempDetail.setStartLong(rs.getDouble("startLong"));
				key = roadName+"_"+segId;
				if(allRoadDetail.containsKey(key)) System.out.println("dup");
				allRoadDetail.put(key,tempDetail);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("load road info:"+allRoadDetail.size());
		return allRoadDetail;
	}
	
	
	// �ú����᷵�����ݵ�json��ʽ 
	public String getAllLinePointJSON(String timeDate,String roadName,int direction){
		
		List<Line> lines = getAllLinePoint(timeDate, roadName, direction); 		

		JSONArray jsonArray = new JSONArray();
		Line tempLine;
		for(int i =0; i < lines.size(); i++){
			tempLine = lines.get(i);
			JSONObject  job = new JSONObject();
			job.put("startLong", tempLine.getStartLong());
			job.put("startLat",  tempLine.getStartLat());
			job.put("endLong", 	 tempLine.getEndLong());
			job.put("endLat", 	 tempLine.getEndLat());
			job.put("color", 	 tempLine.getColor());
			jsonArray.add(job);
		}
		return jsonArray.toString();
	}
	
	
	// ��ȡ���и���������Ҫ���ص�����
	public List<Line> getAllLinePoint(String timeDate,String roadName,int direction){
		
		
		// Ҫ�����timeDate��ʱ���
		//������볤��̫��˵������ʱ�����
		if(timeDate.length() >10)
			timeDate = changeToTimeStamp(timeDate);
		
		
		List<Line> ansLines = new ArrayList<Line>();
		
		
		List<ReportDetail> report = getReport(timeDate,direction,roadName);
		ReportDetail tempRD ;
		String tempName;
		int tempSegId;
		String startSegKey,endSegkey;
		RoadInfo s1,s2;
		Line line;
		double speed;
		for(int i=0;i<report.size();i++){
			tempRD = report.get(i);
			speed  = tempRD.getSpeed();
			tempName = tempRD.getRoadName();
			tempSegId = tempRD.getSegId();
			startSegKey = tempName+"_"+tempSegId;
			endSegkey   = tempName+"_"+(tempSegId+1);
			if(allRoadDetail.containsKey(startSegKey)&&allRoadDetail.containsKey(endSegkey)){
				s1 = allRoadDetail.get(startSegKey);
				s2 = allRoadDetail.get(endSegkey);
				line = new Line(s1.getStartLong(), s1.getStartLat(), s2.getStartLong(), s2.getStartLat(), speed);
				ansLines.add(line);
				//System.out.println(line);
			}else{
				System.out.println("end");
			}
		}
		return ansLines;
	}
	
	
	
	// ���Ȼ�ȡ ��Ӧʱ��� roadName segId direction speed 
	public List<ReportDetail> getReport(String time,int dir,String roadName){
		List<ReportDetail> reportAns = new ArrayList<ReportDetail>();
		MysqlConnection con = new MysqlConnection();
		Statement stat = con.getStatement(con.getConnection());
		
		
		String[] roadNames = {"","G15","G70","G72","G76","G1501","G1514","S35","G3","G25","G319","G324"};
		
		String selectSql = "select * from "+tableName+" where time="+time+" and direction="+dir +" and roadName != 'G324' and roadName != 'G319' ";
		int roadSelect = Integer.parseInt(roadName);  
		if(roadSelect != 0){
			selectSql +=" and roadName='"+roadNames[roadSelect]+"'"; 
		}
		System.out.println(selectSql);
		ResultSet rs ;
		ReportDetail tempDetail;
		try {
			rs = stat.executeQuery(selectSql);
			while(rs.next()){
				tempDetail = new ReportDetail();
				tempDetail.setRoadName(rs.getString("roadName"));
				tempDetail.setSegId(rs.getInt("segId"));
				tempDetail.setSpeed(rs.getDouble("speed"));
//				tempDetail.setTime(time);
				tempDetail.setDirection(rs.getInt("direction"));
				reportAns.add(tempDetail);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reportAns;
	}
	
 
	
	
	
	
	
	public static void main(String[] args) {
		PrepareData pp = new PrepareData();
		//pp.getReport(pp.changeToTimeStamp(null),0);
		//System.out.println(ans.size());
		PrepareData.getRoadInfo();
		pp.getAllLinePoint(pp.changeToTimeStamp(null), null, 0);
	}

}
