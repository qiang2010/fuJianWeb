package sjgt;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import road.RoadModel;
import road.SegModel;
import util.FileUtil;
import app.entity.Line;

 

public class NIres {
	 Map<String,RoadNIlink> roadnis = (HashMap<String,RoadNIlink>)
			new FileUtil("D:/code_workspace64/webTest/nilinks/roadnilinkObj").readObj();
	FileUtil fout = null;
	//static StringBuffer sb = null;
	
	
	// 用于存放最后的nilink的结果，包括 nilink的起始gps，以及速度
	public static List<Line> lines  = new LinkedList<Line>();
	
	public   List<Line> change2NI(Map<String,RoadModel> roads){
//		fout = new FileUtil("fujian/nires/1.txt");
		lines.clear();
		
		for(String name:roads.keySet()){
			List<SegModel> segs = roads.get(name).getSegs();
			RoadNIlink rn = roadnis.get(name);
			
			rn.clearSpeeds();
			System.out.println("name: " + name );
			System.out.println("segs size: " + segs.size() );
			
			for(int i=1;i<segs.size();i++){
				SegModel seg = segs.get(i);
				double speed[] = seg.getSpeed();
				if(speed[0]>0){
					add2speeds(speed[0], rn.forlinks, rn.segForlink.get(i));
				}
				if(speed[1]>0){
					add2speeds(speed[1], rn.backlinks, rn.segBacklink.get(i));
				}
			}
			integrationSpeed(rn.forlinks);
			integrationSpeed(rn.backlinks);
		}
		//sb = new StringBuffer();
		outputSpeeds(null, roadnis);
		return lines;
//		SocketClient.connectServer(sb.toString()); 
	}
	
		/**
		 *  将传入的数据，直接转换，写到Line的list中。
		 * @param nowTime
		 * @param roadnis
		 */
	private  void outputSpeeds(String nowTime,Map<String,RoadNIlink> roadnis){
		for(String name:roadnis.keySet()){
			List<NIlink> forlinks = roadnis.get(name).forlinks;
			List<NIlink> backlinks = roadnis.get(name).backlinks;
			for(NIlink link:forlinks)
				if(!link.getId().startsWith("-")&&link.getSpeed()>0){
//					sb.append(link.getId()+","+String.format("%.2f", link.getSpeed())+"\n");
					Line tempLine = new Line(link.getSt().getX(), link.getSt().getY(), link.getEnd().getX(), link.getEnd().getY(), link.getSpeed());
					lines.add(tempLine);
					//System.out.println("add");
				}
			for(NIlink link:backlinks)
				if(!link.getId().startsWith("-")&&link.getSpeed()>0){
//					sb.append(link.getId()+","+String.format("%.2f", link.getSpeed())+"\n");
					Line tempLine = new Line(link.getSt().getX(), link.getSt().getY(), link.getEnd().getX(), link.getEnd().getY(), link.getSpeed());
					lines.add(tempLine);
					//System.out.println("add");
				}
		}
	}
	private  void integrationSpeed(List<NIlink> links){
		for(NIlink link:links){
			if(link.getSpeeds().size()==0)
				continue;
			double weight=0,speeds = 0;
			for(SpeedWeight sw:link.getSpeeds()){
				weight+=sw.getWeight();
				speeds+=sw.getSpeed()*sw.getWeight();
			}
			if(weight>=0.5)
				link.setSpeed(speeds/weight);
//			System.out.println(link.getId()+"\t"+speeds/weight);
		}
	}
	private  void add2speeds(double speed,List<NIlink> links,List<SegWeight> indexs){
		if(indexs==null)
			return;
		for(SegWeight sw:indexs){
			SpeedWeight spw = new SpeedWeight(speed,sw.w);
			links.get(sw.niIndex).getSpeeds().add(spw);
		}
	}
	public static void main(String[] args) {
		NIres nires = new NIres();
//		nires.change2NI();
	}
}
