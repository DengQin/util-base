package test;

import com.duowan.lobby.util.base.JsonUtil;

public class JsonUtilsTest {
	public static void main(String[] args) {
		String s = "{\"a\":1,\"s\":\"tjh\"}";
		System.out.println(JsonUtil.toObject(s, AA.class));
	}
}

class AA {
	private int a;

	private String s;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return "AA [a=" + a + ", s=" + s + "]";
	}

}
