/*
 * Copyright (c) 2017 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.util;

import static org.junit.Assert.*;

import java.util.Base64;

import org.junit.Test;
import org.tamacat.util.EncryptionUtils;

public class PasswordBuilderTest {

	@Test
	public void testGetHashedPassword() {
		//stretch=10000, keyLength=256
		String encrypted = new PasswordBuilder().getEncryptedPassword("password", "salt");
		//System.out.println(encrypted);
		assertEquals("94cd23156f03105e12dfe432cfee2cc86d1dc0661a3fdae646ee26c125bcebdd", encrypted);
		
		//stretch=12345, keyLength=256
		String encrypted2 = new PasswordBuilder().stretch(12345).getEncryptedPassword("password", "salt");
		//System.out.println(encrypted2);
		assertEquals("625bbe7690006ae284757600f3f1babf48ccfc19a41aea0322f64cfde57a6141", encrypted2);
		
		//stretch=12345, keyLength=512
		String encrypted3 = new PasswordBuilder().stretch(12345).keyLength(512).getEncryptedPassword("password", "salt");
		//System.out.println(encrypted3);
		assertEquals("625bbe7690006ae284757600f3f1babf48ccfc19a41aea0322f64cfde57a6141ab69f17df159165feed9a2078fb90c477b876ab648514ee8744accf6c9fcea58", encrypted3);
		
		//stretch=12345, keyLength=128
		String encrypted4 = new PasswordBuilder().stretch(12345).keyLength(128).getEncryptedPassword("password", "salt");
		//System.out.println(encrypted4);
		assertEquals("625bbe7690006ae284757600f3f1babf", encrypted4);
	}

	@Test
	public void testGetSHA256Salt() {
		byte[] salt = PasswordBuilder.getSHA256Salt("abcdefg1234567890");
		//System.out.println(Base64.getUrlEncoder().encodeToString(salt));
		assertEquals("FaPVaf0EzTjyrHUNezrQjmIYRa0v71L-S8br2rN1-DE=", Base64.getUrlEncoder().encodeToString(salt));
	}
	
	protected String getMessageDigest(String value) {
		return EncryptionUtils.getMessageDigest(value, "SHA-256").toLowerCase();
	}
}
