package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.duowan.lobby.util.base.StringUtil;

public class StringUtilTest {

	@Test
	public void getFileType() {
		assertEquals(null, StringUtil.getFileType(null));
		assertEquals(null, StringUtil.getFileType(""));
		assertEquals(null, StringUtil.getFileType("asfwr"));
		assertEquals(".jpg", StringUtil.getFileType("asdf.jpg"));
		assertEquals(".txt", StringUtil.getFileType("dsad.txt"));
		assertEquals(".txt", StringUtil.getFileType("ds.ad.txt"));

	}

	@Test
	public void isEmpty() {
		assertEquals(true, StringUtil.isBlank(null));
		assertEquals(true, StringUtil.isBlank(""));
		assertEquals(true, StringUtil.isBlank("  "));
		assertEquals(false, StringUtil.isBlank("  1"));
		assertEquals(false, StringUtil.isBlank("  a  "));
		assertEquals(true, StringUtil.isNotBlank("  a  "));
		assertEquals(true, StringUtil.isNotBlank("  1"));
		assertEquals(false, StringUtil.isNotBlank(null));
		assertEquals(false, StringUtil.isNotBlank(""));
		assertEquals(false, StringUtil.isNotBlank("  "));
	}

	@Test
	public void getBetweenString() {
		String s = "sdfgwe<a>eew</a>wer";
		assertEquals("eew", StringUtil.getBetweenString(s, "<a>", "</a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));

		s = "sdfgwe<a>eew<a>";
		assertEquals("eew", StringUtil.getBetweenString(s, "<a>", "<a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</b>"));

		s = "<a>eew<a>";
		assertEquals("eew", StringUtil.getBetweenString(s, "<a>", "<a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</b>"));

		s = "<a>eew</a>";
		assertEquals("eew", StringUtil.getBetweenString(s, "<a>", "</a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "<a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</b>"));

		s = "<a></a>";
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "<a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</b>"));

		s = "<a</a>";
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "<a>"));
		assertEquals("", StringUtil.getBetweenString(s, "<b>", "</b>"));
		assertEquals("", StringUtil.getBetweenString(s, "<a>", "</b>"));
	}
}
