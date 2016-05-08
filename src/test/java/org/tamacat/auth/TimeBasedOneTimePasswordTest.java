package org.tamacat.auth;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tamacat.util.DateUtils;

public class TimeBasedOneTimePasswordTest {

	String secret = "secret-1234567890";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerate() {
		assertEquals(64, new TimeBasedOneTimePassword().generate(secret).length());
	}

	@Test
	public void testGenerateDate() {
		Date d0 = DateUtils.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String pw0 = new TimeBasedOneTimePassword().generate(secret, d0);
		assertEquals("39C70C8C09087E6A199D511B5D4EAECB2B858A725600231932505AA60DE8B0DC", pw0);
		
		Date d1 = DateUtils.parse("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String pw1 = new TimeBasedOneTimePassword().generate(secret, d1);
		assertEquals("41E5CB3805001F27E772FCBF2F0D0F04796ACAEF037B7768DEE439E80D28A5CA", pw1);
	}
	
	@Test
	public void testCheck() throws Exception {
		TimeBasedOneTimePassword otp = new TimeBasedOneTimePassword();
		otp.setOneTimePasswordPeriod(10);
		String key = otp.generate(secret);
		//Thread.sleep(60000);
		//System.out.println(key);
		assertEquals(true, otp.check(secret, key));
	}
		
	@Test
	public void testSetAlgorithm() {
		TimeBasedOneTimePassword otp = new TimeBasedOneTimePassword();
		Date d1 = DateUtils.parse("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		otp.setAlgorithm("HmacSHA256");
		String pw1 = otp.generate(secret, d1);
		
		otp.setAlgorithm("HmacSHA1");
		String pw2 = otp.generate(secret, d1);
			assertNotEquals(pw1, pw2);
	}
	
	@Test
	public void testTimeFormat() {
		TimeBasedOneTimePassword otp = new TimeBasedOneTimePassword();
		Date d1 = DateUtils.parse("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		otp.setTimeFormat("yyyyMMddHHmm");
		String pw1 = otp.generate(secret, d1);
		
		otp.setTimeFormat("yyyy-MM-dd HH:mm");
		String pw2 = otp.generate(secret, d1);

		assertNotEquals(pw1, pw2);
	}
}
