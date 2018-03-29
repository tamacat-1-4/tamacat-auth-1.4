package org.tamacat.auth.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.tamacat.auth.otp.Totp;

public class TOTPTest {
	String secret = "2ZDZXLT7CJ3O5M3L"; //Base32 encoded

	@Test
	public void test() {
		String seed = Base32.encode("secret".getBytes());
		
		String code0 = new Totp(seed, 30).now(); //, time, "6");
		System.out.println(code0);

		boolean result = new Totp(seed, 30).verify(code0);
		System.out.println(result);
	}
	
	@Test
	public void testGenerateTOTP() {
        // Seed for HMAC-SHA1 - 20 bytes
        String seed20 = "3132333435363738393031323334353637383930";
        // Seed for HMAC-SHA256 - 32 bytes
        String seed32 = "3132333435363738393031323334353637383930" +
        	"313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930" +
	        "3132333435363738393031323334353637383930" +
	        "3132333435363738393031323334353637383930" +
	        "31323334";
        long T0 = 0;
        long X = 30;
        long testTime[] = {59L, 1111111109L, 1111111111L, 1234567890L, System.currentTimeMillis()/1000, 2000000000L, 20000000000L};

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            System.out.println("+---------------+-----------------------+------------------+--------+--------+");
            System.out.println("|  Time(sec)    |   Time (UTC format)   | Value of T(Hex)  |  TOTP  | Mode   |");
            System.out.println("+---------------+-----------------------+------------------+--------+--------+");
            for (int i=0; i<testTime.length; i++) {
                long time = (testTime[i] - T0)/X;
                String fmtTime = String.format("%1$-11s", testTime[i]);
                String utcTime = df.format(new Date(testTime[i]*1000));
                System.out.print("|  "+fmtTime+"  |  "+utcTime+"  | "+time+" |");
                System.out.print(TOTP.generateTOTP(seed20, time, "8", "HmacSHA1")+"| SHA1   |\n");
                System.out.print("|  "+fmtTime+"  |  "+utcTime+"  | "+time+" |");
                System.out.print(TOTP.generateTOTP(seed32, time, "8", "HmacSHA256")+"| SHA256 |\n");
                System.out.print("|  "+fmtTime+"  |  "+utcTime+"  | "+time+" |");
                System.out.println(TOTP.generateTOTP(seed64, time, "8", "HmacSHA512")+"| SHA512 |");
                System.out.print("+---------------+-----------------------+------------------+--------+--------+\n");
            }
        } catch (final Exception e) {
            System.out.println("Error : " + e);
        }
	}
}
