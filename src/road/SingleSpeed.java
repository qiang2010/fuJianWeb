package road;


import java.io.Serializable;

public class SingleSpeed implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long t;
	double speed;
	public SingleSpeed(long t, double speed) {
		super();
		this.t = t;
		this.speed = speed;
	}
	public long getT() {
		return t;
	}
	public void setT(long t) {
		this.t = t;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
