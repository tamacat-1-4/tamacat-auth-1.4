/*
 * Copyright (c) 2017 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.tamacat.util.StringUtils;
import org.tamacat.util.UniqueCodeGenerator;

/**
 * @see org.tamacat.auth.crypt.PasswordEncoder
 */
@Deprecated
public class PasswordBuilder {
	
	String algorithm = "PBKDF2WithHmacSHA256";
	int stretch = 10000;
	int keyLength = 256;
	
	public PasswordBuilder() {}
	
	public static PasswordBuilder getDefault() {
		return new PasswordBuilder()
			.stretch(10000)
			.keyLength(256);
	}
	
	public PasswordBuilder algorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}
	
	public PasswordBuilder stretch(int stretch) {
		this.stretch = stretch;
		return this;
	}
	
	public PasswordBuilder keyLength(int keyLength) {
		this.keyLength = keyLength;
		return this;
	}
	
	public String getEncryptedPassword(String password, String salt) {
        char[] passCharAry = password.toCharArray();
        byte[] hashedSalt = getSHA256Salt(salt);
        PBEKeySpec keySpec = new PBEKeySpec(passCharAry, hashedSalt, stretch, keyLength);
        StringBuilder hashedPassword = new StringBuilder(64);
        try {
        	SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);
        	SecretKey secretKey = kf.generateSecret(keySpec);
            byte[] passwordBytes = secretKey.getEncoded();
            //convert hexadecimal characters
            for (byte b : passwordBytes) {
            	hashedPassword.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return hashedPassword.toString();
    }
	
	public boolean check(String hashedPassword, String password, String salt) {
		if (StringUtils.isEmpty(hashedPassword)
		  || StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)) {
			return false;
		}
		String target = getEncryptedPassword(password, salt);
		return target.equals(hashedPassword);
	}
	
	public static String generateSalt() {
		return UniqueCodeGenerator.generate(true);
	}
	
    static byte[] getSHA256Salt(String salt) {
        try {
        	MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes());
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
