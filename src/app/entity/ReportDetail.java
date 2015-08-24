package app.entity;

public class ReportDetail {

	private String roadName;
	private int segId;
	private String time;
	private int direction;
	private double speed;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
}
