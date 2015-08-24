package app.entity;

 

 
public class RoadInfo {
	//private long id ;
	private String roadName ;
	private int segId;
	private double cellLong;
	private double cellLat;
	private double startLong;
	private double startLat;
	private double endLong;
	private double endLat;
	private double dis;
	private int ramp;
	
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
	public int getSegId() {
		return segId;
	}
	public void setSegId(int segId) {
		this.segId = segId;
	}
	public double getCellLong() {
		return cellLong;
	}
	public void setCellLong(double cellLong) {
		this.cellLong = cellLong;
	}
	public double getCellLat() {
		return cellLat;
	}
	public void setCellLat(double cellLat) {
		this.cellLat = cellLat;
	}
	public double getStartLong() {
		return startLong;
	}
	public void setStartLong(double startLong) {
		this.startLong = startLong;
	}
	public double getStartLat() {
		return startLat;
	}
	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}
	public double getEndLong() {
		return endLong;
	}
	public void setEndLong(double endLong) {
		this.endLong = endLong;
	}
	public double getEndLat() {
		return endLat;
	}
	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}
	public double getDis() {
		return dis;
	}
	public void setDis(double dis) {
		this.dis = dis;
	}
	public int getRamp() {
		return ramp;
	}
	public void setRamp(int ramp) {
		this.ramp = ramp;
	}
}
