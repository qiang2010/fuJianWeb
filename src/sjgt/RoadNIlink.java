package sjgt;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import road.GPS2Dist;
import road.Geometry;
import road.Point2;
import road.RoadModel;
import road.SegModel;
import util.FileUtil;



public class RoadNIlink implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<NIlink> forlinks = null;
	public List<NIlink> backlinks = null;
	// 路段映射到的带权重的nilink
	public Map<Integer, List<SegWeight>> segForlink = null;
	public Map<Integer, List<SegWeight>> segBacklink = null;
	private static int nullNIlink = -1;

	public RoadNIlink() {
		super();
	}
	public RoadNIlink(String niFile){
		this.readNilinks(niFile);
	}

	public RoadNIlink(String niFile, List<SegModel> segs) {
		this.readNilinks(niFile);
		segForlink = this.seg2NIlink(segs, forlinks, 1);
		segBacklink = this.seg2NIlink(segs, backlinks, -1);
	}
	public List<NIlink> getForlinks() {
		return forlinks;
	}
	public void setForlinks(List<NIlink> forlinks) {
		this.forlinks = forlinks;
	}

	public Map<Integer, List<SegWeight>> getSegForlink() {
		return segForlink;
	}

	public void clearSpeeds() {
		for (NIlink link : forlinks) {
			link.speeds.clear();
			link.setSpeed(-1);
		}
		for (NIlink link : backlinks) {
			link.speeds.clear();
			link.setSpeed(-1);
		}
	}

	/**
	 * 把路段序号映射为多个带权重的NIlink序号，权重为路段占NIlink长度的比重
	 * @param segs
	 * @param links
	 * @param step
	 *            指示方向，1正向，-1负向
	 * @return
	 */
	private Map<Integer, List<SegWeight>> seg2NIlink(List<SegModel> segs,
			List<NIlink> links, int step) {
		Projection lastproj = null;
		Map<Integer, List<SegWeight>> segLink = new HashMap<Integer, List<SegWeight>>();
		int size = segs.size(), i = 0;
		if (step < 0) {
			size = -1;
			i = segs.size() - 1;
		}

		for (; i != size; i += step) {
			SegModel seg = segs.get(i);
			Point2 segPoint = seg.getSegPoint();

			Projection proj = this.projectionONlinks(segPoint, links,step);
			
			if (proj == null){
				System.out.println("路段投影不到NIlink上step="+step+"\t"+seg);
				continue;
			}
			if (lastproj == null) {
				lastproj = proj;
				continue;
			}

			List<SegWeight> segnis = this.segONlinks(lastproj, proj, links);
//			System.out.println(segnis);
			if (segnis != null) {
				if (step > 0)
					segLink.put(i, segnis);
				else
					segLink.put(i + 1, segnis);
			}
			lastproj = proj;
		}
		return segLink;
	}

	/**
	 * 获取a，b两个投影点之间的带权重的NIlink
	 * @param a
	 * @param b
	 * @param links
	 * @return
	 */
	private List<SegWeight> segONlinks(Projection a, Projection b,List<NIlink> links) {
		List<SegWeight> segnis = new ArrayList<SegWeight>();
		if (a.index > b.index)
			return null;
		NIlink link = links.get(a.index);
		if (a.index == b.index) {
			Point2 st = link.getSt();
			double d1 = GPS2Dist.distance(a.p, st);
			double d2 = GPS2Dist.distance(b.p, st);
			if (d1 >= d2)
				return null;
			SegWeight sw = new SegWeight(a.index, GPS2Dist.distance(a.p, b.p)
					/ GPS2Dist.distance(link.getSt(), link.getEnd()));
			segnis.add(sw);
			return segnis;
		}

		SegWeight sw = new SegWeight(a.index, GPS2Dist.distance(a.p,link.getEnd())/ 
				GPS2Dist.distance(link.getSt(), link.getEnd()));
		segnis.add(sw);
		for (int i = a.index + 1; i < b.index; i++) {
			segnis.add(new SegWeight(i, 1));
		}
		link = links.get(b.index);
		sw = new SegWeight(b.index, GPS2Dist.distance(b.p, link.getSt())
				/ GPS2Dist.distance(link.getSt(), link.getEnd()));
		segnis.add(sw);
		return segnis;
	}

	/**
	 * 点p在nilinks上的投影
	 * @param p
	 * @param links
	 * @return 投影：nilnk链表中的序号，投影点
	 */
	private Projection projectionONlinks(Point2 p, List<NIlink> links,int step) {
		Projection proj = null;
		for (int i = 0; i < links.size(); i++) {
			Point2 st = links.get(i).getSt();
			Point2 end = links.get(i).getEnd();

			// 道路线段参数
			double a1 = -(end.getY() - st.getY()), b1 = end.getX() - st.getX();
			double c1 = -a1 * end.getX() - b1 * end.getY();
			double a2 = b1, b2 = -a1;
			double c2 = -a2 * p.getX() - b2 * p.getY();
			Point2 cp = Geometry.crossPoint2(a1, b1, c1, a2, b2, c2);

			if (cp == null || !Geometry.isPointOnSegment(cp, st, end))
				continue;

			double dist = GPS2Dist.distance(cp, p);
			if (step>0&&dist<0.1||step<0&&dist < 1.5) {
				proj = new Projection(i, cp);
				break;
			}
		}
		return proj;
	}
	/**
	 * 从文件中读取有序的NIlink
	 * @param fname
	 */
	private void readNilinks(String fname) {
		forlinks = new ArrayList<NIlink>();
		backlinks = new ArrayList<NIlink>();
		FileUtil fin = new FileUtil(fname);
		String str = null;

		while ((str = fin.readLine()) != null) {
			String s[] = str.split("\t");
			NIlink ni = this.parseNIlink(s);
			if (ni.getDirection() == 0) {
				this.replenish2links(forlinks, ni);
				forlinks.add(ni);
			} else if (ni.getDirection() == 1){
				this.replenish2links(backlinks, ni);
				backlinks.add(ni);
			}
		}
	}

	/**
	 * 判断link加入到links之前是否需要补充一个虚拟的link 如果前后两个NIlink是不相连的，
	 * 需要补充一个,其id以负号开头
	 * @param links
	 * @param link
	 */
	public void replenish2links(List<NIlink> links, NIlink link) {
		if (links.size() == 0)
			return;
		NIlink last = links.get(links.size() - 1);
		if (last.getEnd().equals(link.getSt()))
			return;

		double dist = GPS2Dist.distance(last.getEnd(), link.getSt());
		NIlink replen = new NIlink(String.valueOf(nullNIlink--), -1, dist,
				last.getEnd(), link.getSt());
		links.add(replen);
	}
	public NIlink parseNIlink(String s[]) {
		//System.out.println(s[0]);
		String id = s[0];
		Integer dir = Integer.valueOf(s[1]);
		Double dist = Double.valueOf(s[2]);
		Point2 st = new Point2(Double.valueOf(s[3]), Double.valueOf(s[4]));
		Point2 end = new Point2(Double.valueOf(s[5]), Double.valueOf(s[6]));
		NIlink ni = new NIlink(id, dir, dist, st, end);
		return ni;
	}

	private static void seg2NIpre() {
		Map<String,RoadModel> roads = (HashMap<String, RoadModel>) new FileUtil(
				"fujian/road/roadObj").readObj();
		Map<String, RoadNIlink> roadnis = new HashMap<String, RoadNIlink>();
		for (RoadModel road:roads.values()) {
			List<SegModel> segs = road.getSegs();
			RoadNIlink roadni = new RoadNIlink("fujian/nilinks/"
					+ road.getName() + ".txt", segs);
			roadnis.put(road.getName(), roadni);
		}
//		System.out.println(roadnis.get("G15").segForlink);
	
		// 把G72的反向nilink对应表输出
		segLinktoString(roadnis.get("G72").segBacklink, roadnis.get("G72").backlinks);
		new FileUtil("fujian/nilinks/roadnilinkObj").writeObj(roadnis);
	}
	private static void segLinktoString(Map<Integer, List<SegWeight>> segLink,List<NIlink> links){
		for(Integer index:segLink.keySet()){
			System.out.println(index);
			for(SegWeight sw:segLink.get(index))
				System.out.println(links.get(sw.niIndex).id+"\t"+sw.w);
		}
	}

	public static void main(String[] args) {
		RoadNIlink.seg2NIpre();
	}
}
