package sjgt;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import road.Point2;


public class NIlink implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id = null;
	int direction;
	double dist;
	Point2 st,end;
	
	List<SpeedWeight> speeds = null;
	double speed;
	public NIlink(String id) {
		super();
		this.id = id;
	}
	public NIlink(String id, int direction, double dist, Point2 st, Point2 end) {
		super();
		this.id = id;
		this.direction = direction;
		this.dist = dist;
		this.st = st;
		this.end = end;
		
		speeds = new ArrayList<SpeedWeight>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public Point2 getSt() {
		return st;
	}
	public void setSt(Point2 st) {
		this.st = st;
	}
	public Point2 getEnd() {
		return end;
	}
	public void setEnd(Point2 end) {
		this.end = end;
	}
	public List<SpeedWeight> getSpeeds() {
		return speeds;
	}
	public void setSpeeds(List<SpeedWeight> speeds) {
		this.speeds = speeds;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		NIlink other = (NIlink) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return id + "\t" + direction + "\t"	+ dist + "\t" + st + "\t" + end;
	}
}
