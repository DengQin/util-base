package test.check;

import org.junit.Test;

import com.duowan.lobby.util.base.check.CdnStateChecker;

public class CdnStateCheckerTest {

	@Test
	public void check() {
		String s = "http://res.play.yy.com/yymobilegame/ad_pic/ddt2_bf16b7hh62hr.jpeg";
		System.out.println(CdnStateChecker.check(s, 27715));
	}

}
