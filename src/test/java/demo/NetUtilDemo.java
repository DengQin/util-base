package demo;

import java.util.HashMap;
import java.util.Map;

import com.duowan.lobby.util.base.NetUtil;

public class NetUtilDemo {
	public static void main(String[] args) throws Exception {
		String url = "http://127.0.0.1/ws/receive/receive.do";
		Map<String, String> params = new HashMap<String, String>();
		params.put("sysCode", "aaa");
		params.put("heartbeatCode", "1234");
		params.put("content", "先发个岗位");
		
		System.out.println(NetUtil.postURL(url, "utf-8", params));
	}
}
