package org.tamacat.auth.util;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.ExceptionUtils;
import org.tamacat.util.PropertyUtils;
import org.tamacat.util.ResourceNotFoundException;
import org.tamacat.util.StringUtils;

public class EncryptSessionUtils {

	static final Log LOG = LogFactory.getLog(EncryptSessionUtils.class);
	
	static String secretKey = "secret1234567890";
	static String algorithm = "AES/CBC/PKCS5Padding";
	static String iv;
	
	static Cipher encrypt;
	static Cipher decrypt;
	static IvParameterSpec ips;
	
	static {
		load("encrypt-session.properties");
	}
	
	public static void load(String propFile) {
		try {
			Properties props = PropertyUtils.getProperties(propFile);
			String getSecretKey = props.getProperty("secret_key");
			String getAlgorithm = props.getProperty("algorithm");
			if (StringUtils.isNotEmpty(secretKey)) {
				secretKey = getSecretKey;
			}
			if (StringUtils.isNotEmpty(getAlgorithm)) {
				algorithm = getAlgorithm;
			}
			iv = props.getProperty("iv"); //for AES/CBC MODE
		} catch (ResourceNotFoundException e) {
			LOG.warn(e.getMessage());
		}
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			SecretKey key = new SecretKeySpec(md5.digest(secretKey.getBytes()), "AES");
			encrypt = Cipher.getInstance(algorithm);
			decrypt = Cipher.getInstance(algorithm);
			if (algorithm.indexOf("CBC")>=0) {
				if (iv != null) {
					if (iv.length() >= 16) {
						iv = iv.substring(0, 16);
					}
					ips = new IvParameterSpec(iv.getBytes("UTF-8"));
				} else if (secretKey.length() >= 16) {
					ips = new IvParameterSpec(secretKey.substring(0, 16).getBytes("UTF-8"));
				}
			}
			encrypt.init(Cipher.ENCRYPT_MODE, key, ips);
			decrypt.init(Cipher.DECRYPT_MODE, key, ips);
		} catch (Exception e) {
			LOG.warn(e.getMessage());
		}
	}
	
	public static String getSecretKey() {
		return secretKey;
	}
	
	public static String getAlgorithm() {
		return algorithm;
	}
	
	public static synchronized String encryptSession(String session) {
		try {
			byte[] encryptedData = encrypt.doFinal(session.getBytes("UTF-8"));
			return new String(Base64.getUrlEncoder().withoutPadding().encode(encryptedData),"UTF-8");
		} catch (Exception e) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("ERROR session="+session);
				LOG.trace(ExceptionUtils.getStackTrace(e));
			}
			LOG.debug("ERROR encryptSession, message="+e.getMessage());
		}
		return null;
	}
	
	public static synchronized String decryptSession(String session) {
		try {
			byte[] decryptedData = decrypt.doFinal(Base64.getUrlDecoder().decode(session));
			return new String(decryptedData, "UTF-8");
		} catch (Exception e) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("ERROR session="+session);
				LOG.trace(ExceptionUtils.getStackTrace(e));
			}
			LOG.debug("ERROR decryptSession, message="+e.getMessage());
		}
		return null;
	}
}
