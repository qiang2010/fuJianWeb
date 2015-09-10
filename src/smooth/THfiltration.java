package smooth;


import java.util.List;

import road.SegModel;

public class THfiltration {
	//前historySize-1时间范围内的影响，空间影响最大的范围maxDistkm
	static int historySize = 13,nowIndex ,maxDist = 30;
	static double Cf = 80,Cc=-15,deltaV = 20,Vc = 60,sigma = 0.6,tao = 1.1/60;
	static int base = 10;
 
	public static  void setHistorySize(int size){
		historySize = size;
	}
	/**
	 * @param segs
	 * @param i
	 * @param j
	 * @param dir  0正向，1负向
	 * @return seg[i]-seg[j]的距离，根据方向有正负性
	 */
	private static double segMidDist(List<SegModel> segs,int i,int j,int dir){
		double midi = (segs.get(i).getDist()+segs.get(i-1).getDist())/2;
		double midj = (segs.get(j).getDist()+segs.get(j-1).getDist())/2;
		if(dir==0) return midi-midj;
		return midj-midi;
	}
	private static double exponential(double t,double x){
		return Math.exp(-Math.abs(x)/sigma-Math.abs(t)/tao);
	}
	
    public static double filtering(double [][] speeds,List<SegModel> segs, int index, int dir){
		double Nc = 0,Nf = 0,Zc = 0,Zf = 0;
		for(int t=0,i=nowIndex;t<historySize*5;t+=5,i=(i-1+historySize)%historySize){
			for(int j=1;j<segs.size();j++)
				if(speeds[j][i]>1){
					double dist = segMidDist(segs,j,index,dir);
					if(Math.abs(dist)>maxDist) continue;
					double PHIc = exponential((-t+base)/60.0-dist/Cc, dist);
					double PHIf = exponential((-t+base)/60.0-dist/Cf, dist);
					Nc+=PHIc;
					Nf+=PHIf;
					Zc+=speeds[j][i]*PHIc;
					Zf+=speeds[j][i]*PHIf;
				}
		}
		if(Nc<=0||Nf<=0) return -1;
		Zc/=Nc;
		Zf/=Nf;
		double weight = 0.5*(1+Math.tanh((Vc-Math.min(Zc, Zf))/deltaV));
		return Zf+weight*(Zc-Zf);
	}
	public static int getNowIndex() {
		return nowIndex;
	}
	public static void setNowIndex(int nowIndex) {
		THfiltration.nowIndex = nowIndex;
	}
	public static int getBase() {
		return base;
	}
	public static void setBase(int base) {
		THfiltration.base = base;
	}
	 
	
}
