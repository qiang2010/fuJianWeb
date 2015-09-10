package road;


import java.io.Serializable;

public class Point2 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double eps = 1e-6;
	// x是经度，y是维度
	double x,y;
	public Point2(){
	}
	public Point2(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public Point2 midPoint(Point2 p){
		Point2 midP = new Point2();
		midP.setX((this.x+p.x)/2);
		midP.setY((this.y+p.y)/2);
		return midP;
	}
	
	// 这里为什么这样写？？？？？？？？？？？
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//long temp;
		//temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (x*100);
		//temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (y*100);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point2 other = (Point2) obj;
		if(Math.abs(x-other.getX())>eps||Math.abs(y-other.getY())>eps)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return  x + "\t" + y ;
	}
}
