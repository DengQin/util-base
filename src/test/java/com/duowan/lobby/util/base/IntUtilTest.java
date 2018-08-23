package com.duowan.lobby.util.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntUtilTest {

	@Test
	public void testParseInt() throws Exception {
		assertEquals(123, IntUtil.parseInt("123"));
		assertEquals(0, IntUtil.parseInt("0"));
		assertEquals(-123, IntUtil.parseInt("-123"));
		assertEquals(0, IntUtil.parseInt("aa"));
	}

	@Test
	public void testParseNonnegative() throws Exception {
		assertEquals(123, IntUtil.parseNonnegative("123"));
		assertEquals(0, IntUtil.parseNonnegative("0"));
		assertEquals(0, IntUtil.parseNonnegative("-123"));
		assertEquals(0, IntUtil.parseNonnegative("aa"));
	}

	@Test
	public void testParsePositive() throws Exception {
		assertEquals(123, IntUtil.parsePositive("123"));
		assertEquals(1, IntUtil.parsePositive("0"));
		assertEquals(1, IntUtil.parsePositive("-123"));
		assertEquals(1, IntUtil.parsePositive("aa"));
	}

}
