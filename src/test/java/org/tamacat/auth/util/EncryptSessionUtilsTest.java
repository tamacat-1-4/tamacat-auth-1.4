package org.tamacat.auth.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tamacat.auth.util.EncryptSessionUtils;

public class EncryptSessionUtilsTest {

	String encrypted = "bl6lLwc6hBPUMRLh7YmtRzJFgp7z2jYwPBE2b5IY8HLzmgcQEW6l0Y2-GPBsXCrt";
	String original = "test1234567890test1234567890test1234567890";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncryptSession() {
		//System.out.println(EncryptSessionUtils.encryptSession(original));
		assertEquals(encrypted, EncryptSessionUtils.encryptSession(original));
	}

	@Test
	public void testDecryptSession() {
		assertEquals(original, EncryptSessionUtils.decryptSession(encrypted));
	}
}
