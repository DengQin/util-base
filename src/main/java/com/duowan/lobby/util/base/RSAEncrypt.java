package com.duowan.lobby.util.base;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.lobby.util.base.exception.CommonException;

//* 密钥产生
//* 1私钥
//* openssl genrsa -out rsa_private_key.pem 1024 
//* 2公钥
//* openssl rsa -in rsa_private_key.pem -out rsa_public_key.pem -pubout
//* 3私钥转换一下
//* openssl pkcs8 -topk8 -in rsa_private_key.pem -out pkcs8_rsa_private_key.pem -nocrypt 
//* 4利用RSAEncryptDemo产生公钥的和私钥的16进制字符串

/**
 * 
 * @author tanjh 2014-3-5
 */
public class RSAEncrypt {
	private static final Logger log = LoggerFactory.getLogger(RSAEncrypt.class);

	/**
	 * 产生签名
	 */
	public static String generateSHA1withRSASigature(String src, String privateKey) {
		try {
			Signature sigEng = Signature.getInstance("SHA1withRSA");
			byte[] pribyte = hexStrToBytes(StringUtil.trim(privateKey));
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPrivateKey key = (RSAPrivateKey) fac.generatePrivate(keySpec);
			sigEng.initSign(key);
			sigEng.update(src.getBytes());
			byte[] signature = sigEng.sign();
			return EncryptUtil.byte2hex(signature);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException("生成签名出错");
		}
	}

	/**
	 * 验证签名
	 */
	public static boolean verifySHA1withRSASigature(String sign, String src, String publicKey) {
		try {
			Signature sigEng = Signature.getInstance("SHA1withRSA");
			byte[] pubbyte = hexStrToBytes(StringUtil.trim(publicKey));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
			KeyFactory fac = KeyFactory.getInstance("RSA");
			RSAPublicKey pubKey = (RSAPublicKey) fac.generatePublic(keySpec);
			sigEng.initVerify(pubKey);
			sigEng.update(src.getBytes());
			byte[] sign1 = hexStrToBytes(sign);
			return sigEng.verify(sign1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CommonException("验证签名出错");
		}
	}

	/**
	 * 将16进制字符串还原为字节数组.
	 */
	private static final byte[] hexStrToBytes(String s) {
		return EncryptUtil.hex2byte(s.getBytes());
	}

}