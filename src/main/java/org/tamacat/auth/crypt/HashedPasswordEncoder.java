package org.tamacat.auth.crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.tamacat.auth.util.Hex;

public class HashedPasswordEncoder implements PasswordEncoder {

	protected String algorithm = "SHA-256";
	
	public HashedPasswordEncoder() {}
	
	public HashedPasswordEncoder(String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		return getHashedString(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		return getHashedString(rawPassword.toString()).equals(encodedPassword);
	}

    protected String getHashedString(String value) {
        try {
        	MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            //return Base64.getUrlEncoder().encodeToString(messageDigest.digest());
            return new String(Hex.encode(messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
