package road;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.FileUtil;

public class RoadModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static double Radius = 1.5,MinDist = 0.1;
	public static int ExpireTime = 60*60;
	public static double FurthestDist = 30;
	
	int serial = -1;
	String name = null; // ·������
	List<Point2> road = null; //���ȣ�γ��
	List<LacCell> roadCells = null;
	/**
	 * ·���и���Ϣ
	 */
	//Map<Integer,Integer> siteSeg = null; //��վcellId���ָ��id��ӳ��
	Map<Point2,Integer> siteSeg = null; //��վλ�õ��ָ���ӳ��
	List<SegModel> segs = null;
	public RoadModel(){
		
	}
	public RoadModel(int serial,String name,String roadFile){
		this.serial = serial;
		this.name = name;
		this.readRoad(roadFile);
		this.siteSeg = new HashMap<Point2,Integer>();
		this.segs = new ArrayList<SegModel>();
	}
	public RoadModel(int id,String roadFile,String siteFile,Map<LacCell,SiteModel> info){
		this.serial = id;
		this.readRoad(roadFile);
		this.readSiteAlongTheRoad(siteFile,info);
		this.siteSeg = new HashMap<Point2,Integer>();
		this.segs = new ArrayList<SegModel>();
		//this.roadSeg2(info);
		//this.roadSeg(info);
		this.roadSeg2Disorder(info);
	}
//	public int getId() {
//		return serial;
//	}
//	public void setId(int id) {
//		this.serial = id;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Point2> getRoad() {
		return road;
	}
	public void setRoad(List<Point2> road) {
		this.road = road;
	}
	public List<LacCell> getRoadCells() {
		return roadCells;
	}
	public void setRoadCells(List<LacCell> roadCells) {
		this.roadCells = roadCells;
	}
	public Map<Point2, Integer> getSiteSeg() {
		return siteSeg;
	}
	public void setSiteSeg(Map<Point2, Integer> siteSeg) {
		this.siteSeg = siteSeg;
	}
	public List<SegModel> getSegs() {
		return segs;
	}
	public void setSegs(List<SegModel> segs) {
		this.segs = segs;
	}
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		int i=0;
		for(SegModel seg:segs){
			buff.append(this.name+"\t"+(i++)+"\t");
			buff.append(seg.toString());
		}
		return buff.toString();
	}
	/**
	 * ��γ��,���� �����ɾ���,γ��
	 * @param fileName
	 */
	private void readRoad(String fileName){
		road = new ArrayList<Point2>();
		FileUtil reader = new FileUtil(fileName);
		String str = null;
		while((str = reader.readLine())!=null){
			String s[] = null;
			Point2 p = null;
			//�ӵ�ͼ�Ͽ۵�
			if(str.contains(",")){
				s = str.split(",");
				p = new Point2(Double.valueOf(s[1]),Double.valueOf(s[0]));
			}
			//��mMapInfo��ȡ��
			else if(str.contains("\t")){
				s = str.split("\t");
				p = new Point2(Double.valueOf(s[0]),Double.valueOf(s[1]));
			}
			road.add(p);
		}
	}
	private void readSiteAlongTheRoad(String fileName,Map<LacCell,SiteModel> info){
		roadCells = new ArrayList<LacCell>();
		FileUtil reader = new FileUtil(fileName);
		String str = null;
		while((str = reader.readLine())!=null){
			String s[] = str.split("\t");
			Long cell = Long.valueOf(s[0]);
			int lac = Integer.valueOf(s[1]);
			LacCell lc = new LacCell(lac,cell);
			if(!roadCells.contains(lc)){
				roadCells.add(lc);
			}
		}
	}
	public void clearSpeed(){
		for(SegModel seg:this.segs){
			seg.getBackwards().clear();
			seg.getForwards().clear();
			seg.getForUsers().clear();
			seg.getBackUsers().clear();
			seg.speed[0] = seg.speed[1] = -1;
			seg.isVD[0] = seg.isVD[1] = false;
		}
	}
	/**
	 * ���·�����ߣ����ڸ��Ļ�վ���в������ߵģ�ֻ������ֱ�㣬�ٸ��ݴ�ֱ���λ������
	 * @param info
	 */
	public void roadSeg2Disorder(Map<LacCell,SiteModel> info){
		for(int indS = 0;indS<roadCells.size();indS++){
			LacCell nowlc = roadCells.get(indS);
			//int nowSiteId = info.get(nowCellId).getSiteID();
			Point2 nowS = info.get(nowlc).getPos();
			SegModel nowSeg = null;
			double secant;
			for(int indR=1;indR<road.size();indR++){
				Point2 lastR = road.get(indR-1);
				Point2 nowR = road.get(indR);
				//��·�߶β���
				double a1 = -(nowR.getY()-lastR.getY()), b1 = nowR.getX()-lastR.getX();
				double c1 = -a1*nowR.getX()-b1*nowR.getY();
				double a2 = b1,b2 = -a1;
				double c2 = -a2*nowS.getX()-b2*nowS.getY();
				Point2 cp = Geometry.crossPoint2(a1, b1, c1, a2, b2, c2);
	
				if(cp == null || !Geometry.isPointOnSegment(cp, nowR, lastR))
					continue;
				
				//System.out.println("�ҵ����ߵ�"+nowCellId);
				secant = GPS2Dist.distance(cp,nowS);
				if(secant>=this.Radius){
					continue;
				}
				
//				System.out.println(name+"\t"+nowlc.cell+"\t"+nowlc.lac+"\t"+info.get(nowlc).type
//						+"\t"+nowS);
//				secant = 0.5*Math.sqrt(this.Radius*this.Radius-secant*secant);
				if(nowSeg==null||secant<nowSeg.secant)
					nowSeg = new SegModel(cp,secant,nowS,indR-1,lastR);
			}
			//�ҵ���ֱ�������С��
			if(nowSeg==null) continue;
			secant =nowSeg.secant;
			secant = 0.5*Math.sqrt(this.Radius*this.Radius-secant*secant);
			nowSeg.setSecant(secant);
			segs.add(nowSeg);
		}
		Collections.sort(segs, new SegComparator());
		
		for(int i=0;i<segs.size();i++){
			SegModel seg = segs.get(i);
			//�Ѿ��������siteId�ķָ���Ϣ�����·��������·��
			if(siteSeg.containsKey(seg.getSitePoint())){
				segs.remove(i);
				i--;
				continue;
			}
			if(i==0)
				siteSeg.put(seg.getSitePoint(), i);
			else if(i>0){
				double dist = GPS2Dist.distance(seg.getSegPoint(), segs.get(i-1).getSegPoint());
				//������վͶӰ����ܽ�����ͬӳ�䵽ǰ�ָ�㣬ȥ����һ���ָ��
				if(dist<MinDist){
					siteSeg.put(seg.getSitePoint(), i-1);
					segs.remove(i);
					i--;
					continue;
				}
				else
				{
					seg.setDist(dist+segs.get(i-1).getDist());
					siteSeg.put(seg.getSitePoint(), i);
				}
					
			}
		}
	}
	public static void main(String[] args) {
		RoadModel test = new RoadModel();
		Point2 nowS = new Point2(118.683388,27.264886);
		Point2 lastR = new Point2(118.69633,27.27657);
		Point2 nowR = new Point2(118.68743,27.26277);
		double a1 = -(nowR.getY()-lastR.getY()), b1 = nowR.getX()-lastR.getX();
		double c1 = -a1*nowR.getX()-b1*nowR.getY();
		double a2 = b1,b2 = -a1;
		double c2 = -a2*nowS.getX()-b2*nowS.getY();
		Point2 cp = Geometry.crossPoint2(a1, b1, c1, a2, b2, c2);
		System.out.println(cp);
		System.out.println(Geometry.isPointOnSegment(cp, nowR, lastR));
	}
}
