package com.duowan.lobby.util.base;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.exception.VisualException;

/**
 * 加密类
 * 
 * @author Fentid Peng 2007-2-7 下午07:16:28
 */
public class EncryptUtil {
	private static Logger log = LoggerFactory.getLogger(EncryptUtil.class);

	public static String getFileMd5String(File file) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] arr = new byte[10240];

			int len = 0;
			MessageDigest messagedigest = java.security.MessageDigest.getInstance("MD5");
			while ((len = in.read(arr)) > 0) {
				messagedigest.update(arr, 0, len);
			}
			return byte2hex(messagedigest.digest());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtil.closeQuietly(in);
		}
		return "";
	}

	public static String getMD5(String str) {
		return encode(str, "MD5");
	}

	public static String getSHA1(String str) {
		return encode(str, "SHA-1");
	}

	public static String getLittleMD5(String str) {
		String estr = encode(str, "MD5");
		return estr.substring(0, 20);
	}

	public static String getLittleSHA1(String str) {
		String estr = encode(str, "SHA-1");
		return estr.substring(0, 20);
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            - 待加密的数据
	 * @param key
	 *            - 加密使用的key
	 * @return 正常情况下返回加密后的字符串，纯小写；如果出错，返回null
	 */
	public static String getHMacSha1(String str, String key) {
		try {
			byte[] data = str.getBytes("UTF-8");
			byte[] keyData = key.getBytes("UTF-8");
			return getHMacSha1(data, keyData);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 生成签名数据
	 */
	private static String getHMacSha1(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
		String HMAC_SHA1 = "HmacSHA1";
		SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data);
		return byte2hex(rawHmac);
	}

	private static String encode(String str, String type) {
		try {
			MessageDigest alga = java.security.MessageDigest.getInstance(type);
			alga.update(str.getBytes("UTF-8"));
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2byte(String content) {
		return hex2byte(content.getBytes());
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 异或加密
	 */
	public static String xorEncrypt(String str, String key) {
		BigInteger strbi = new BigInteger(str.getBytes());
		BigInteger keybi = new BigInteger(key.getBytes());
		BigInteger encryptbi = strbi.xor(keybi);

		return new String(encryptbi.toByteArray());
	}

	/**
	 * 异或解密
	 */
	public static String xorDecrypt(String encryptStr, String key) {
		BigInteger encryptbi = new BigInteger(encryptStr.getBytes());
		BigInteger keybi = new BigInteger(key.getBytes());
		BigInteger decryptbi = encryptbi.xor(keybi);
		return new String(decryptbi.toByteArray());
	}

	/**
	 * BASE64编码
	 */
	@SuppressWarnings("restriction")
	public static String Base64Encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * BASE64解码
	 */
	@SuppressWarnings("restriction")
	public static byte[] Base64Decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return bt;
	}

	/**
	 * DES加密
	 */
	public static byte[] desEncrypt(byte[] datasource, String password) {
		VisualException.isTrue(password.length() > 7, "密钥长度不能小于8位");
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * DES解密
	 */
	public static byte[] desDecrypt(byte[] src, String password) throws Exception {
		VisualException.isTrue(password.length() > 7, "密钥长度不能小于8位");
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 3DES加密
	 * 
	 * @param src
	 * @param password
	 * @param iv
	 *            向量
	 * @return
	 */
	public static byte[] ThreeDesEncrypt(byte[] src, byte[] key, byte[] iv) {
		try {
			SecureRandom sr = new SecureRandom();
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, ivParameterSpec, sr);
			return cipher.doFinal(src);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 3DES解密
	 * 
	 * @param src
	 * @param password
	 * @param iv
	 *            向量
	 * @return
	 */
	public static byte[] ThreeDesDecrypt(byte[] src, byte[] key, byte[] iv) {
		try {
			SecureRandom sr = new SecureRandom();
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, securekey, ivParameterSpec, sr);
			return cipher.doFinal(src);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 3DES加密
	 * 
	 * @param src
	 *            加密的数据缓冲区
	 * @param key
	 *            加密密钥，长度为24字节
	 * @return
	 */
	public static byte[] ThreeDesEncrypt(byte[] src, byte[] key) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key, "DESede");

			// 加密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 3DES解密
	 * 
	 * @param src
	 *            加密的数据缓冲区
	 * @param key
	 *            加密密钥，长度为24字节
	 * @return
	 */
	public static byte[] ThreeDesDecrypt(byte[] src, byte[] key) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(key, "DESede");

			// 解密
			Cipher c1 = Cipher.getInstance("DESede");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

}
