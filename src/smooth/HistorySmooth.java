package smooth;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import road.RoadModel;
import road.SegModel;
/**
 * @author jq
 * 空白值时用历史的前before*5min的平均值来代替
 */
public class HistorySmooth {
 
	
	public static double historyAverage(double[] sp){
		int count = 0;
		double speed = 0;
		for(int i=0;i<sp.length;i++)
			if(sp[i]>1){
				count++;
				speed+=sp[i];
			}
		speed/=count;
		if(count>0)
			return speed;
		return -1;
	}
	 
}
