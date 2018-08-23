package com.duowan.lobby.util.base;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.exception.CommonException;

public class AESEncrypt {
	private final static String AES_ALGORITHM = "AES";
	private static final int AES_KEY_SIZIE = 128;
	private static Logger log = LoggerFactory.getLogger(AESEncrypt.class);

	/**
	 * AES加密方法
	 * 
	 * @param encryptKey
	 *            加密的Key
	 * @param data
	 *            被加密数据
	 * @return
	 * @throws EncryptException
	 */
	public static String encryptByAES(String encryptKey, String data) {
		try {
			SecretKey secretKey = getKey(encryptKey);
			byte[] encodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(encodeFormat, AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM); // 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); // 初始化
			byte[] result = cipher.doFinal(data.getBytes());
			return new String(Base64.encodeBase64(result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException("加密[" + data + "],出错" + e.getMessage());
		}
	}

	/**
	 * AES解密方法
	 * 
	 * @param decryptKey
	 * @param data
	 * @return
	 * @throws DecryptException
	 * @throws EncryptException
	 */
	public static String decryptByAES(String decryptKey, String data) {
		try {
			SecretKey secretKey = getKey(decryptKey);
			byte[] encodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(encodeFormat, AES_ALGORITHM);
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
			byte[] encodeByte = Base64.decodeBase64(data.getBytes());
			byte[] decryptedByte = cipher.doFinal(encodeByte);
			return new String(decryptedByte);
		} catch (Exception e) {
			throw new CommonException("解密[" + data + "],出错" + e.getMessage());
		}
	}

	private static SecretKey getKey(String key) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(key.getBytes());
		keyGenerator.init(AES_KEY_SIZIE, secureRandom);
		return keyGenerator.generateKey();
	}

}
