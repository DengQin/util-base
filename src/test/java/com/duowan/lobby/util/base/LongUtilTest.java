package com.duowan.lobby.util.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LongUtilTest {

	@Test
	public void testParseLong() throws Exception {
		assertEquals(123L, LongUtil.parseLong("123"));
		assertEquals(0L, LongUtil.parseLong("0"));
		assertEquals(-123L, LongUtil.parseLong("-123"));
		assertEquals(0L, LongUtil.parseLong("aa"));
	}

}
