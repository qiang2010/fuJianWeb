package road;




public class Geometry {
	public final static double eps1 = 1e-4;
	/**
	 * 判断两个向量是否平行。如果向量的单位特别小，容易造成不平行的判断成平行的，首相要进行归一化
	 * @param a1 向量1
	 * @param b1
	 * @param a2 向量2
	 * @param b2
	 * @return
	 */
	private static boolean isParallel2(double a1,double b1,double a2,double b2){
		double guiyi = 1;
		if(a1!=0&&Math.abs(1/a1)>guiyi) guiyi = Math.abs(1/a1);
		if(a2!=0&&Math.abs(1/a2)>guiyi) guiyi = Math.abs(1/a2);
		if(b1!=0&&Math.abs(1/b1)>guiyi) guiyi = Math.abs(1/b1);
		if(b2!=0&&Math.abs(1/b2)>guiyi) guiyi = Math.abs(1/b2);
		a1*=guiyi;a2*=guiyi;b1*=guiyi;b2*=guiyi;
		
		if(Math.abs(a1*b2-a2*b1)<eps1/10) 
			return true;
		return false;
	}
	/**
	 * 由标准的直线方程ax+by+c=0确定的两条直线，得到交点
	 * @return 两条直线平行的时候返回null
	 */
	public static Point2 crossPoint2(double a1,double b1,double c1,double a2,double b2,double c2){
		if(isParallel2(a1,b1,a2,b2))
			return null;
		Point2 p = new Point2();
		double fenMu = a1*b2-a2*b1;
		p.setX(-(c1*b2-c2*b1)/fenMu);
		p.setY(-(a1*c2-a2*c1)/fenMu);
		return p;
	}
	/**
	 * 计算两个点之间的距离
	 * @return
	 */
	public static double distance(Point2 a,Point2 b){
		return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
	}
	/**
	 * 判断点是否在线段上
	 * @param p 点
	 * @param st 线段开始节点
	 * @param end
	 * @return
	 */
	public static boolean isPointOnSegment(Point2 p,Point2 st,Point2 end){
		double dist = distance(st,end);
		double dist1 = distance(st,p);
		double dist2 = distance(end,p);
		//System.out.println(dist+"\t"+dist1+"\t"+dist2);
		if(Math.abs(dist2+dist1-dist)<eps1*2)
			return true;
		return false;
	}
}
