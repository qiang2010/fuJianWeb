package road;



public class GPS2Dist {
	public static double interval = 111.31955;
	/**
	 * 输入两个GPS点（经度，纬度），计算它们之间的距离
	 * @param a
	 * @param b
	 * @return
	 */
	public static double distance(Point2 a,Point2 b){
		double y = (a.getY()-b.getY())*interval;
		double x = (a.getX()-b.getX())*Math.cos((a.getY()+b.getY())/2/180*Math.PI)*interval;
		return Math.sqrt(x*x+y*y);
	}
	public static void main(String[] args) {
		Point2 a = new Point2(119.4883185,25.97284656);
		Point2 b = new Point2(119.4780665,25.96460138);
		System.out.println(distance(a,b));
	}
}
