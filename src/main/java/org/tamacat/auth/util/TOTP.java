package org.tamacat.auth.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;

/**
 * https://tools.ietf.org/html/rfc6238
 */
public class TOTP {
	
	static final Log LOG = LogFactory.getLog(TOTP.class);
	
    public static boolean verifyCode(String secret, String code, long time, String returnDigits, int variance) {
		for (int i=-variance; i<=variance; i++) {
			String c = generateTOTP(secret, time, returnDigits);
			if (c.equals(code)) {
				return true;
			}
		}
		return false;
    }
    
    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *              {@link truncationDigits} digits
     */
    public static String generateTOTP(String key, long time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }
    
    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *              {@link truncationDigits} digits
     */
    public static String generateTOTP256(String key, long time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }
    
    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *              {@link truncationDigits} digits
     */
    public static String generateTOTP512(String key, long time, String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }
    
	public static String generateTOTP(String key, long time, String returnDigits, String crypto) {
        String steps = Long.toHexString(time).toUpperCase();
        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (steps.length() < 16) steps = "0" + steps;
        
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(steps);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
            ((hash[offset] & 0x7f) << 24) |
            ((hash[offset + 1] & 0xff) << 16) |
            ((hash[offset + 2] & 0xff) << 8) |
            (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
	}
	
    /**
     * This method converts a HEX string to Byte[]
     * @param hex: the HEX string
     * @return: a byte array
     */
    static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = bArray[i+1];
        }
        return ret;
    }
    
    static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }
    
    static final int[] DIGITS_POWER
    	// 0 1  2   3    4     5      6       7        8
    	= {1,10,100,1000,10000,100000,1000000,10000000,100000000 };
}
