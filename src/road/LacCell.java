package road;


import java.io.Serializable;

public class LacCell implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int lac;
	long cell;
	public LacCell(int lac, long cell) {
		super();
		this.lac = lac;
		this.cell = cell;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	public long getCell() {
		return cell;
	}
	public void setCell(long cell) {
		this.cell = cell;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cell ^ (cell >>> 32));
		result = prime * result + lac;
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
		LacCell other = (LacCell) obj;
		if (cell != other.cell)
			return false;
		if (lac != other.lac)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "lac=" + lac + ", cell=" + cell;
	}
}
