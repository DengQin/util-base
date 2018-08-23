package com.duowan.lobby.util.base;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import test.bean.Son;

public class BeanUtilTest {

	@Test
	public void testGetField() throws Exception {
		Field f = BeanUtil.getField(Son.class, "priff");
		assertEquals(true, f == null);
		f = BeanUtil.getField(Son.class, "pubff");
		assertEquals(true, f != null);
		assertEquals(true, "pubff".equals(f.getName()));
		f = BeanUtil.getField(Son.class, "ff");
		assertEquals(true, f == null);

		f = BeanUtil.getField(Son.class, "priss");
		assertEquals(true, f == null);
		f = BeanUtil.getField(Son.class, "pubss");
		assertEquals(true, f != null);
		assertEquals(true, "pubss".equals(f.getName()));
		f = BeanUtil.getField(Son.class, "ss");
		assertEquals(true, f == null);
	}

	@Test
	public void testGetDeclaredField() throws Exception {
		Field f = BeanUtil.getDeclaredField(Son.class, "priff");
		assertEquals(true, f == null);
		f = BeanUtil.getDeclaredField(Son.class, "pubff");
		assertEquals(true, f == null);
		f = BeanUtil.getDeclaredField(Son.class, "ff");
		assertEquals(true, f == null);

		f = BeanUtil.getDeclaredField(Son.class, "priss");
		assertEquals(true, f != null);
		f = BeanUtil.getDeclaredField(Son.class, "pubss");
		assertEquals(true, f != null);
		assertEquals(true, "pubss".equals(f.getName()));
		f = BeanUtil.getDeclaredField(Son.class, "ss");
		assertEquals(true, f != null);
	}

	@Test
	public void testGetFieldWithExtend() throws Exception {
		Field f = BeanUtil.getFieldWithExtend(Son.class, "priff");
		assertEquals(true, f == null);
		f = BeanUtil.getFieldWithExtend(Son.class, "pubff");
		assertEquals(true, f != null);
		f = BeanUtil.getFieldWithExtend(Son.class, "ff");
		assertEquals(true, f != null);
		f = BeanUtil.getFieldWithExtend(Son.class, "finff");
		assertEquals(true, f == null);

		f = BeanUtil.getFieldWithExtend(Son.class, "priss");
		assertEquals(true, f != null);
		f = BeanUtil.getFieldWithExtend(Son.class, "pubss");
		assertEquals(true, f != null);
		assertEquals(true, "pubss".equals(f.getName()));
		f = BeanUtil.getFieldWithExtend(Son.class, "ss");
		assertEquals(true, f != null);
	}

	@Test
	public void testDescribe() throws Exception {
		Son s = new Son();
		s.setPriff(2);
		s.setPriss(3);
		s.setPubff(4);
		s.setPubss(5);
		s.setSs(6);
		s.setAa(1);
	}

	@Test
	public void testGetFields() throws Exception {
		assertEquals(5, BeanUtil.getFields(Son.class).size());
	}

	// @Test
	// public void testGetField2() throws Exception {
	// Field f = BeanUtil.getField(Son.class, "aa");
	// assertEquals(true, f == null);
	// f = BeanUtil.getField(Son.class, "bb");
	// assertEquals(true, f != null);
	// assertEquals(true, "bb".equals(f.getName()));
	// }

}
