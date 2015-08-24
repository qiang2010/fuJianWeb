package app.entity;

public class Line {
	private double startLong;
	private double startLat;
	private double endLong;
	private double endLat;
	private double speed;
	private String color; // 16½øÖÆÑÕÉ«
	public Line(double startLong, double startLat, double endLong,
			double endLat, double speed) {
		super();
		this.startLong = startLong;
		this.startLat = startLat;
		this.endLong = endLong;
		this.endLat = endLat;
		this.speed = speed;
		mapSpeedToColorString();
	}
	public Line(){
		
	}
	public void mapSpeedToColorString(){
		if(speed > 60){
			color = "#00FF00";
		}else{
			if(speed < 60 && speed > 40){
				color = "#FFFFFF";
			}else{
				color = "#FF0000";
			}
			
		}
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	@Override
	public String toString() {
		return "Line [startLong=" + startLong + ", startLat=" + startLat
				+ ", endLong=" + endLong + ", endLat=" + endLat + ", speed="
				+ speed + ", color="+ color+"]";
	}
	
}
