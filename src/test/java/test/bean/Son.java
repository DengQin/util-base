package test.bean;

public class Son extends Father {

	private int priss;

	public int pubss;

	int ss;

	public int getPriss() {
		return priss;
	}

	public void setPriss(int priss) {
		this.priss = priss;
	}

	public int getPubss() {
		return pubss;
	}

	public void setPubss(int pubss) {
		this.pubss = pubss;
	}

	public int getSs() {
		return ss;
	}

	public void setSs(int ss) {
		this.ss = ss;
	}

	@Override
	public String toString() {
		return "Son [priss=" + priss + ", pubss=" + pubss + ", ss=" + ss + ", pubff=" + pubff + ", ff=" + ff + "]";
	}

}
