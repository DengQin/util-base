package com.duowan.lobby.util.base;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void testGetStartOfDay() throws Exception {
		Date now = new Date();
		long t = DateUtil.strToTime(DateUtil.formatDate(now) + " 00:00:00").getTime();
		assertEquals(t, DateUtil.getStartOfDay(now).getTime());
	}

	@Test
	public void testGetEndOfDay() throws Exception {
		Date now = new Date();
		long t = DateUtil.strToTime(DateUtil.formatDate(now) + " 23:59:59").getTime();
		assertEquals(t, DateUtil.getEndOfDay(now).getTime());
	}

	@Test
	public void isLegalDate() {
		assertEquals(false, DateUtil.isLegalDate(2012, 2, 31));
		assertEquals(false, DateUtil.isLegalDate(2011, 2, 29));
		assertEquals(false, DateUtil.isLegalDate(1700, 2, 29));
		assertEquals(false, DateUtil.isLegalDate(2012, 9, 0));
		assertEquals(false, DateUtil.isLegalDate(2012, 0, 1));
		assertEquals(false, DateUtil.isLegalDate(2012, 9, 31));

		assertEquals(true, DateUtil.isLegalDate(2011, 2, 28));
		assertEquals(true, DateUtil.isLegalDate(2012, 2, 29));
		assertEquals(true, DateUtil.isLegalDate(2012, 12, 31));
		assertEquals(true, DateUtil.isLegalDate(2012, 9, 30));
		assertEquals(true, DateUtil.isLegalDate(2012, 9, 1));
		assertEquals(true, DateUtil.isLegalDate(2012, 1, 1));
		assertEquals(true, DateUtil.isLegalDate(2000, 2, 29));
	}

	@Test
	public void getFirstDayOfMonth() throws Exception {
		Date d = DateUtil.strToTime("2012-10-10 10:10:02");
		assertEquals(DateUtil.strToTime("2012-10-01 00:00:00"), DateUtil.getFirstDayOfMonth(d));
		assertEquals(DateUtil.strToTime("2012-10-31 00:00:00"), DateUtil.getLastDayOfMonth(d));

		d = DateUtil.strToTime("2012-02-05 10:10:02");
		assertEquals(DateUtil.strToTime("2012-02-01 00:00:00"), DateUtil.getFirstDayOfMonth(d));
		assertEquals(DateUtil.strToTime("2012-02-29 00:00:00"), DateUtil.getLastDayOfMonth(d));

		d = DateUtil.strToTime("2011-02-05 10:10:02");
		assertEquals(DateUtil.strToTime("2011-02-01 00:00:00"), DateUtil.getFirstDayOfMonth(d));
		assertEquals(DateUtil.strToTime("2011-02-28 00:00:00"), DateUtil.getLastDayOfMonth(d));

	}

	@Test
	public void testGetAbsDifferDays() throws Exception {
		Date d1 = DateUtil.strToTime("2012-10-10 10:10:02");
		Date d2 = DateUtil.strToTime("2012-10-15 10:10:00");
		assertEquals(DateUtil.getAbsDifferDays(d1, d2), 4);

		d2 = DateUtil.strToTime("2012-10-10 10:10:02");
		d1 = DateUtil.strToTime("2012-10-15 10:10:00");
		assertEquals(DateUtil.getAbsDifferDays(d1, d2), 4);

		d2 = DateUtil.strToTime("2012-10-15 10:10:02");
		d1 = DateUtil.strToTime("2012-10-15 10:10:00");
		assertEquals(DateUtil.getAbsDifferDays(d1, d2), 0);
	}

	@Test
	public void testIsBetween() throws Exception {
		Date start = DateUtil.strToTime("2012-10-15 10:10:02");
		Date end = DateUtil.strToTime("2012-10-15 10:10:04");

		Date date = DateUtil.strToTime("2012-10-15 10:10:03");
		assertEquals(DateUtil.isBetween(date, start, end), true);

		date = DateUtil.strToTime("2012-10-15 10:10:04");
		assertEquals(DateUtil.isBetween(date, start, end), true);

		date = DateUtil.strToTime("2012-10-15 10:10:02");
		assertEquals(DateUtil.isBetween(date, start, end), true);

		date = DateUtil.strToTime("2012-10-15 10:10:03");
		assertEquals(DateUtil.isBetween(date, start, end), true);

		date = DateUtil.strToTime("2012-10-15 10:10:01");
		assertEquals(DateUtil.isBetween(date, start, end), false);

		date = DateUtil.strToTime("2012-10-15 10:10:05");
		assertEquals(DateUtil.isBetween(date, start, end), false);

		date = DateUtil.strToTime("2012-10-15 10:10:05");
		assertEquals(DateUtil.isBetween(date, date, date), true);

		try {
			assertEquals(DateUtil.isBetween(date, end, start), false);
			throw new RuntimeException("message");
		} catch (Exception e) {
			assertEquals(e.getMessage().contains("参数错误"), true);
		}

	}

}
