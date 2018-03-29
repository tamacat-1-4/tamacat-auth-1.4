package org.tamacat.auth.util;

import org.junit.Test;
import org.tamacat.auth.otp.Totp;

public class Totp_Test {
	String secret = "2ZDZXLT7CJ3O5M3L"; //Base32 encoded

	@Test
	public void test() {
		String seed = Base32.encode("secret".getBytes());
		
		String code0 = new Totp(seed, 30).now(); //, time, "6");
		System.out.println(code0);

		boolean result = new Totp(seed, 30).verify(code0);
		System.out.println(result);
	}
}
