package com.duowan.lobby.util.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JsonUtilTest {

	@Test
	public void serialize() {
		System.out.println(JsonUtil.serialize(1));
		System.out.println(JsonUtil.unserialize(JsonUtil.serialize(1)));
		System.out.println(JsonUtil.serialize(true));
		System.out.println(JsonUtil.unserialize(JsonUtil.serialize(true)));
		System.out.println(JsonUtil.serialize("string"));
		System.out.println(JsonUtil.unserialize(JsonUtil.serialize("string")));
		Date d = new Date();
		System.out.println(JsonUtil.serialize(d));
		System.out.println(JsonUtil.unserialize(JsonUtil.serialize(d)));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("int", 123);
		map.put("str", "string");
		map.put("date", new Date());
		System.out.println(JsonUtil.serializeMap(map));
		System.out.println(JsonUtil.unserializeMap(JsonUtil.serializeMap(map)));
	}

	@Test
	public void testToMapIgnoreQuote() throws Exception {
		String s = "{\"retcode\":0,\"roleinfo\":[{\"nickname\":\"''\u72ec\u7279\u8303\u3063\",\"grade\":\"48\",\"createTime\":\"2014-08-06 21:29:34\",\"sex\":\"m\",\"profession\":\"\"}]}";
		System.out.println(JsonUtil.toMapIgnoreQuote(s));
		s = "{'retcode':0,'roleinfo':[{'nickname':'\"\"\u72ec\u7279\u8303\u3063','grade':'48','createTime':'2014-08-06 21:29:34','sex':'m','profession':''}]}";
		System.out.println(JsonUtil.toMapIgnoreQuote(s));
	}
}
