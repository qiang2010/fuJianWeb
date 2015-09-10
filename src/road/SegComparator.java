package road;


import java.util.Comparator;

public class SegComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		SegModel seg1 = (SegModel)o1;
		SegModel seg2 = (SegModel)o2;
		if(seg1.getRoadNum()!=seg2.getRoadNum()){
			return seg1.roadNum-seg2.roadNum;
		}
		double d1 = distance(seg1.getRoadPoint(),seg1.getSegPoint());
		double d2 = distance(seg2.getRoadPoint(),seg2.getSegPoint());
		if(d1<d2)
			return -1;
		else if(d1>d2)
			return 1;
		return 0;
	}
	private double distance(Point2 a,Point2 b){
		return ((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
	}

}
