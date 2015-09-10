package sjgt;

import road.Point2;



public class Projection {
	int index;
	Point2 p = null;
	public Projection(int index, Point2 p) {
		super();
		this.index = index;
		this.p = p;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public Point2 getP() {
		return p;
	}
	public void setP(Point2 p) {
		this.p = p;
	}
	@Override
	public String toString() {
		return "Projection [index=" + index + ", p=" + p + "]";
	}
}
