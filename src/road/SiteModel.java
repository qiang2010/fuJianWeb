package road;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SiteModel implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	int siteID;
//	int lac;
	Point2 pos = null;    ///  »ùÕ¾Î»ÖÃ
	int type;
	int belongRoadCount;
	Set<String> names = null;
//	public SiteModel(int lac, Point2 pos) {
//		super();
//		this.lac = lac;
//		this.pos = pos;
//		
//		this.belongRoadCount = 0;
//	}
//	public SiteModel(int lac, Point2 pos, int type) {
//		this.lac = lac;
//		this.type = type;
//		this.pos = pos;
//		this.belongRoadCount = 0;
//	}
	
	public SiteModel(Point2 pos, int type) {
		super();
		this.pos = pos;
		this.type = type;
		
		this.belongRoadCount = 0;
		this.names = new HashSet<String>();
	}
	
	public SiteModel(Point2 pos) {
		super();
		this.pos = pos;
		
		this.belongRoadCount = 0;
	}

//	public int getSiteID() {
//		return siteID;
//	}
//	public void setSiteID(int siteID) {
//		this.siteID = siteID;
//	}
//	public int getLac() {
//		return lac;
//	}
//	public void setLac(int lac) {
//		this.lac = lac;
//	}
	public Point2 getPos() {
		return pos;
	}

	public void setPos(Point2 pos) {
		this.pos = pos;
	}
	public int getBelongRoadCount() {
		return belongRoadCount;
	}
	public void setBelongRoadCount(int belongRoadCount) {
		this.belongRoadCount = belongRoadCount;
	}
	public Set<String> getNames() {
		return names;
	}
	public void setNames(Set<String> names) {
		this.names = names;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
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
		SiteModel other = (SiteModel) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
}
