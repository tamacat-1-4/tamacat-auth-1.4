package org.tamacat.auth.crypt;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;
import org.tamacat.util.PropertyUtils;
import org.tamacat.auth.crypt.PasswordEncoder;

public class PasswordEncoderTest {
	
	Properties props = PropertyUtils.getProperties("password-encoder.properties");
	String password = "password";

	@Test
	public void testGetPasswordEncoderDefault() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "default");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("default="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
	
	@Test
	public void testGetPasswordEncoderPBKDF2WithHmacSHA256() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "PBKDF2WithHmacSHA256");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("PBKDF2WithHmacSHA256="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
	
	@Test
	public void testGetPasswordEncoderPBKDF2WithHmacSHA512() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "PBKDF2WithHmacSHA512");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("PBKDF2WithHmacSHA512="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
	
	@Test
	public void testGetPasswordEncoderBCrypt() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "BCrypt");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("BCrypt="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
	
	@Test
	public void testGetPasswordEncoderPlain() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "Plain");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("Plain="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
	
	@Test
	public void testGetPasswordEncoderSHA256() {
		PasswordEncoder encoder = PasswordEncoder.getInstance(props, "SHA-256");
		
		String encodedPassword = encoder.encode(password);
		System.out.println("SHA-256="+encodedPassword);
		assertTrue(encoder.matches(password, encodedPassword));
	}
}
