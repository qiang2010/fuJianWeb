package road;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SegModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�ָ��ľ�̬��Ϣ
	Point2 segPoint = null;
	double dist,secant;    // secant��ʲô����    ���ߣ�
	Point2 sitePoint = null;   // ��վ
	double ramp;
	//�ָ�·�εĶ�̬�ٶ���Ϣ
	double speed[];
	boolean isVD[];
	List<SingleSpeed> forwards,backwards = null;
	List<String> forUsers = null,backUsers =null;
	//������������������ʱҪ��
	int roadNum;
	Point2 roadPoint;

	public SegModel(Point2 segPoint,double secant, Point2 sitePoint,
			int roadNum, Point2 roadPoint) {
		super();
		this.segPoint = segPoint;
		this.secant = secant;
		this.sitePoint = sitePoint;
		this.roadNum = roadNum;
		this.roadPoint = roadPoint;
		
		this.speed = new double[2];
		this.isVD = new boolean[2];
		this.forwards = new ArrayList<SingleSpeed>();
		this.backwards = new ArrayList<SingleSpeed>();
		this.forUsers =new ArrayList<String>();
		this.backUsers = new ArrayList<String>();
		
		this.ramp = 0;
	}
	public Point2 getSegPoint() {
		return segPoint;
	}
	public void setSegPoint(Point2 segPoint) {
		this.segPoint = segPoint;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public Point2 getSitePoint() {
		return sitePoint;
	}
	public void setSitePoint(Point2 sitePoint) {
		this.sitePoint = sitePoint;
	}
	public double[] getSpeed() {
		return speed;
	}
	public void setSpeed(double[] speed) {
		this.speed = speed;
	}
	public boolean[] getIsVD() {
		return isVD;
	}
	public void setIsVD(boolean[] isVD) {
		this.isVD = isVD;
	}
	public List<SingleSpeed> getForwards() {
		return forwards;
	}
	public void setForwards(List<SingleSpeed> forwards) {
		this.forwards = forwards;
	}
	public List<SingleSpeed> getBackwards() {
		return backwards;
	}
	public void setBackwards(List<SingleSpeed> backwards) {
		this.backwards = backwards;
	}
	public int getRoadNum() {
		return roadNum;
	}
	public void setRoadNum(int roadNum) {
		this.roadNum = roadNum;
	}
	public Point2 getRoadPoint() {
		return roadPoint;
	}
	public void setRoadPoint(Point2 roadPoint) {
		this.roadPoint = roadPoint;
	}
//	public int getLac() {
//		return lac;
//	}
//	public void setLac(int lac) {
//		this.lac = lac;
//	}
	public List<String> getForUsers() {
		return forUsers;
	}
	public void setForUsers(List<String> forUsers) {
		this.forUsers = forUsers;
	}
	public List<String> getBackUsers() {
		return backUsers;
	}
	public void setBackUsers(List<String> backUsers) {
		this.backUsers = backUsers;
	}
	public double getSecant() {
		return secant;
	}
	public void setSecant(double secant) {
		this.secant = secant;
	}
	public double getRamp() {
		return ramp;
	}
	public void setRamp(double ramp) {
		this.ramp = ramp;
	}
	@Override
	public String toString() {
		return segPoint +"\t" +sitePoint+"\t"+dist +"\t"+ramp+"\n";
	}
}