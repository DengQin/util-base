package com.duowan.lobby.util.base;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.junit.Test;

public class EncryptUtilTest {

	@Test
	public void testThreeDesDecrypt() throws Exception {
		String s = "MDIhUXMfJhvQjtNd8vWupnvYFrxwp0GO";

		final byte[] keyBytes = EncryptUtil.Base64Decode(s);
		String szSrc = "This is a 3DES ++%%test. 测试";
		String sde = URLEncoder.encode(szSrc, "utf-8");
		System.out.println("加密前的字符串:" + sde);
		byte[] encoded = EncryptUtil.ThreeDesEncrypt(sde.getBytes(), keyBytes);
		String a = EncryptUtil.Base64Encode(encoded);
		System.out.println("加密后的字符串:" + a);

		byte[] r1 = EncryptUtil.Base64Decode(a);
		byte[] srcBytes = EncryptUtil.ThreeDesDecrypt(r1, keyBytes);
		String r2 = new String(srcBytes);
		System.out.println("解密后的字符串r2:" + r2);
		System.out.println("解密后的字符串r3:" + URLDecoder.decode(r2, "utf-8"));
	}

	@Test
	public void testThreeDesDecrypt2() throws Exception {
		String s = "MDIhUXMfJhvQjtNd8vWupnvYFrxwp0GO";

		final byte[] keyBytes = EncryptUtil.Base64Decode(s);
		String szSrc = "This is a 3DES ++%%test. 测试";
		byte[] encoded = EncryptUtil.ThreeDesEncrypt(szSrc.getBytes(), keyBytes, "12345678".getBytes());
		String a = EncryptUtil.Base64Encode(encoded);
		System.out.println("加密后的字符串:" + a);

		byte[] r1 = EncryptUtil.Base64Decode(a);
		byte[] srcBytes = EncryptUtil.ThreeDesDecrypt(r1, keyBytes, "12345678".getBytes());
		String r2 = new String(srcBytes);
		System.out.println("解密后的字符串r2:" + r2);
	}

}
