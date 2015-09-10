package sjgt;


import java.io.Serializable;

public class SegWeight implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int niIndex;
	public double w;
	public SegWeight(int niIndex, double w) {
		super();
		this.niIndex = niIndex;
		this.w = w;
	}
	public int getNiIndex() {
		return niIndex;
	}
	public void setNiIndex(int niIndex) {
		this.niIndex = niIndex;
	}
	public double getW() {
		return w;
	}
	public void setW(double w) {
		this.w = w;
	}
	@Override
	public String toString() {
		return niIndex + "\t" + w + "\n";
	}
}
