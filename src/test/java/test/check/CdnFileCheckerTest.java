package test.check;

import org.junit.Test;

import com.duowan.lobby.util.base.check.CdnFileChecker;

public class CdnFileCheckerTest {

	@Test
	public void check() {
		String url = "http://sz.duowan.com/s/yyyp/js/ticketConf.js";
		String filePathName = "E:/data/ticketConf.js";
		System.out.println(CdnFileChecker.check(url, filePathName));
	}

}
