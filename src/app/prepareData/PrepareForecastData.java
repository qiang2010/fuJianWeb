package app.prepareData;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import road.RoadModel;
import road.SegModel;
import smooth.HistorySmooth;
import util.FileUtil;
import util.MysqlConnection;
import app.entity.Line;
import app.entity.ReportDetail;
import app.entity.RoadInfo;

public class PrepareForecastData {

	String tableName = "report20150615";
	
//	private static PrepareData uniqueInstance = null;
	boolean first = true;
//	private PrepareData(){
//		allRoadDetail = getRoadInfo();  
//	}
//	public static PrepareData  getInstance(){
//		if(uniqueInstance == null){
//			synchronized(PrepareData.class){
//				if(uniqueInstance == null ){
//					uniqueInstance = new PrepareData();
//				}
//			}
//		}
//		return uniqueInstance;
//	}
	
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
	
	public List<String> getTimestampList(String date,int size){
		
		List<String>  stampList = new LinkedList<String>();
		 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );


		 Date timeS =null;
		 long currentStamp ;
		 try {
			timeS = format.parse(date);
			currentStamp = timeS.getTime();
			for(int i = size*5; i >0; i-=5){
				stampList.add(""+(currentStamp-i*60000));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return stampList;
	}
	
	
	// ��̬��ʼ��
	static HashMap<String ,RoadInfo> allRoadDetail= null;
	static {
		allRoadDetail = getRoadInfo();
	}
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
		// �����������ݹ���road map
		// String ��road�����֣�RoadModel������·�ε�seg List���ٶ�[speed1,spee2].
//		Map<String,RoadModel> roads = getRoadMap(changeToTimeStamp(timeDate), roadName);
//		System.out.println("get Roadinfo");
//		NIres tempN = new NIres();
//		List<Line> lines = tempN.change2NI(roads); 	
		

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
		System.out.println("ajax done!");
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
			if(allRoadDetail.containsKey(startSegKey) && allRoadDetail.containsKey(endSegkey)){
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
		// and direction="+dir +" ����������
		String selectSql = "select * from "+tableName+" where time="+time+"  and roadName != 'G324' and roadName != 'G319' ";
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
	
 
	
	

	
	/**
	 *  ����roadMapֻ��Ҫ��ȡobj�������ڻ�ȡ·�ε������Ϣ������·�������ͳ��ȵȡ�
	 * 
	 * @param timeDate
	 * @param roadName
	 * @param direction
	 * @return
	 */
	
	static Map<String,RoadModel> roadsMap =  (HashMap<String,RoadModel>)new FileUtil("D:/code_workspace64/FujianWeb/nilinks/roadObj").readObj();
	//  ���ڴ����ʷֵ��ֻ��һ����������ˣ���ͬ�Ĳ������ݻ��ȡ��ͬ�ķ���
	private  Map<String,double[][]> historySpeed = new HashMap<String,double[][]>();
//	private  Map<String,double[][]> backspeeds = new HashMap<String,double[][]>();
	//�������ڴ��Ԥ��ֵ
	//private  Map<String,double[]> forecastForSpeeds = new HashMap<String,double[]>();
	//private  Map<String,double[]> forecastBackSpeeds = new HashMap<String,double[]>();
	private  List<ReportDetail> forecast ;
//	private  List<ReportDetail> forecastOne = new LinkedList<ReportDetail>();
	
	private  int historySmoothHistorySize = 6;
	private  int THhistorySize = 13;
	public List<Line> getAllLinePoint_HistorySmooth_Forecast(String timeDate,String roadName,int direction){
		
		//roadsMap = (HashMap<String,RoadModel>)new FileUtil("D:/code_workspace64/webTest/nilinks/roadObj").readObj();	
		// ���ȸ��ݴ���Ĳ�����ȡ���ݿ��е����ݣ����ڹ���roadMap,��ʷƽ��
		List<String> timestampList = getTimestampList(timeDate,historySmoothHistorySize);
		inithistorySpeedSize(roadsMap, historySmoothHistorySize);
//		int size = timestampList.size();
		String tempTime;
		for( int i = 0 ; i < historySmoothHistorySize; i ++){
			tempTime = timestampList.get(i);
			// ��ȡʱ�䣬Ȼ�����roadNamd��direction�ֱ��ѯ��
			getReport_forecast(tempTime, direction, roadName, i);
		}
		int roadSelect = Integer.parseInt(roadName);
		boolean chooseRoad = false;
		String roadNameChooseString = null;
		if(roadSelect != 0){
			roadNameChooseString = roadNames[roadSelect];
			chooseRoad = true;
		}
		// ��ʱӦ���Ѿ���ȡ���趨����ʷ����
		forecast = new LinkedList<ReportDetail>();
		ReportDetail tempReportDetail;
		for(RoadModel road:roadsMap.values()){
			if(chooseRoad && !road.getName().equals(roadNameChooseString)) continue;
			List<SegModel> segs = road.getSegs();
			double [][] speed = historySpeed.get(road.getName());
			for(int i=0;i<segs.size();i++){ 
				tempReportDetail = new ReportDetail();
				tempReportDetail.setSpeed( HistorySmooth.historyAverage(speed[i]));
				tempReportDetail.setSegId(i);
				tempReportDetail.setRoadName(road.getName());
				forecast.add(tempReportDetail);
			}
		}
		
		System.out.println("forecast size:"+forecast.size());
		
		// ת���� line
		List<Line> ansLines = new LinkedList<Line>();
		ReportDetail tempRD ;
		String tempName;
		int tempSegId;
		String startSegKey,endSegkey;
		RoadInfo s1,s2;
		Line line;
		double speed;
		for(int i=1;i<forecast.size();i++){
			tempRD = forecast.get(i);
			speed  = tempRD.getSpeed();
			tempName = tempRD.getRoadName();
			tempSegId = tempRD.getSegId();
			startSegKey = tempName+"_"+tempSegId;
			endSegkey   = tempName+"_"+(tempSegId+1);
			System.out.println(startSegKey +"  " + endSegkey+" "+speed);
			if(allRoadDetail.containsKey(startSegKey) && allRoadDetail.containsKey(endSegkey)){
				s1 = allRoadDetail.get(startSegKey);
				s2 = allRoadDetail.get(endSegkey);
				line = new Line(s1.getStartLong(), s1.getStartLat(), s2.getStartLong(), s2.getStartLat(), speed);
				ansLines.add(line);
				//System.out.println(line);
			}else{
				//System.out.println("end");
			}
		}
		System.out.println("ans lines size:"+ansLines.size());
		return ansLines;
	}
	
	private void inithistorySpeedSize(Map<String,RoadModel> roads,int size){
		for(RoadModel road:roads.values()){
			historySpeed.put(road.getName(), new double[road.getSegs().size()][size]);
		}
	}
	
	// �ú����᷵��Ԥ�������ݵ�json��ʽ 
	public String getAllLinePointJSON_Forecast(String timeDate,String roadName,int direction){
		
		List<Line> lines = getAllLinePoint_HistorySmooth_Forecast(timeDate, roadName, direction);
		// �����������ݹ���road map
		// String ��road�����֣�RoadModel������·�ε�seg List���ٶ�[speed1,spee2].
//		Map<String,RoadModel> roads = getRoadMap(changeToTimeStamp(timeDate), roadName);
//		System.out.println("get Roadinfo");
//		NIres tempN = new NIres();
//		List<Line> lines = tempN.change2NI(roads); 	
		

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
		System.out.println("ajax done!");
		return jsonArray.toString();
	}
	
	String[] roadNames = {"","G15","G70","G72","G76","G1501","G1514","S35","G3","G25","G319","G324"};
	// ���Ȼ�ȡ ��Ӧʱ��� roadName segId direction speed 
	public void getReport_forecast(String time,int dir,String roadName,int timeIndex){
		MysqlConnection con = new MysqlConnection();
		Statement stat = con.getStatement(con.getConnection());
		// and direction="+dir +" ����������
		String selectSql = "select * from "+tableName+" where time="+time+"  and roadName != 'G324' and roadName != 'G319' ";
		int roadSelect = Integer.parseInt(roadName);  
		if(roadSelect != 0){
			selectSql +=" and roadName='"+roadNames[roadSelect]+"'"; 
		}
		System.out.println(selectSql);
		ResultSet rs ;
		double [][]tempSpeed;
		try {
			rs = stat.executeQuery(selectSql);
			while(rs.next()){
				tempSpeed = historySpeed.get(rs.getString("roadName"));
				tempSpeed[rs.getInt("segId")][timeIndex] = rs.getDouble("speed");
				System.out.println(tempSpeed[rs.getInt("segId")][timeIndex]);
				historySpeed.put(rs.getString("roadName"),tempSpeed);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// ���Ȼ�ȡ ��Ӧʱ��� roadName segId direction speed ��������ת����nilink��roads map
		// string ��·�� 
		public Map<String,RoadModel> getRoadMap(String time,String roadName,int dir){
			
			MysqlConnection con = new MysqlConnection();
			Statement stat = con.getStatement(con.getConnection());
			
			String[] roadNames = {"","G15","G70","G72","G76","G1501","G1514","S35","G3","G25","G319","G324"};
			//  ����������
			String selectSql = "select * from "+tableName+" where time="+time+" and direction="+dir +" and roadName != 'G324' and roadName != 'G319' ";
			int roadSelect = Integer.parseInt(roadName);  
			if(roadSelect != 0){
				selectSql +=" and roadName='"+roadNames[roadSelect]+"'"; 
			}
			System.out.println(selectSql);
			ResultSet rs ;
			RoadModel tempModel;
			int tempDir;
			String tempRoadName;
			List<SegModel> tempSegs;
			int count = 0;
			try {
				rs = stat.executeQuery(selectSql);
				while(rs.next()){
					tempRoadName = rs.getString("roadName");
					if(roadsMap.containsKey(tempRoadName)){
						tempModel = roadsMap.get(tempRoadName);
						tempSegs =  tempModel.getSegs();
						tempDir =  rs.getInt("direction");
						SegModel tempSeg = tempSegs.get(rs.getInt("segId"));
						double []speed = tempSeg.getSpeed();
						speed[tempDir] = rs.getDouble("speed");
						tempSeg.setSpeed(speed);
						//tempSegs.add(tempSeg);
						roadsMap.put(tempRoadName,tempModel);
						count++;
					}else{
						//tempModel = new RoadModel();
						
					}
				}
				System.out.println("rs size : "+count);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return roadsMap;
		}
	public static void main(String[] args) {
		PrepareData pp = new PrepareData();
		//pp.getReport(pp.changeToTimeStamp(null),0);
		//System.out.println(ans.size());
		PrepareData.getRoadInfo();
		pp.getAllLinePoint(pp.changeToTimeStamp(null), null, 0);
	}

}
