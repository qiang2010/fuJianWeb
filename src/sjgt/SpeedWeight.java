package sjgt;


public class SpeedWeight {
	double speed,weight;
	public SpeedWeight(double speed, double weight) {
		super();
		this.speed = speed;
		this.weight = weight;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "speed=" + speed + ", weight=" + weight;
	}
}
