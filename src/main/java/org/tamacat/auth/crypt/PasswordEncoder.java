/*
 * Copyright 2011-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tamacat.auth.crypt;

import java.util.Properties;

import org.tamacat.auth.util.EncryptSessionUtils;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.StringUtils;

/**
 * Service interface for encoding passwords.
 *
 * The preferred implementation is {@code BCryptPasswordEncoder}.
 *
 * @author Keith Donald
 * @author tamacat.org
 * 
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * https://github.com/spring-projects/spring-security/blob/master/crypto/src/main/java/org/springframework/security/crypto/password/PasswordEncoder.java
 */
public interface PasswordEncoder {

	/**
	 * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1
	 * or greater hash combined with an 8-byte or greater randomly generated salt.
	 * 
	 * @param rawPassword
	 * @return encoded string.
	 */
	String encode(CharSequence rawPassword);
	
	/**
	 * Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
	 * Returns true if the passwords match, false if they do not. The stored password itself is never decoded.
	 * 
	 * @param rawPassword the raw password to encode and match
	 * @param encodedPassword the encoded password from storage to compare with
	 * @return true if the raw password, after encoding, matches the encoded password from storage
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);
	
	static final Log LOG = LogFactory.getLog(PasswordEncoder.class);
	
	/**
	 * Get a instance of PasswordEncoder. (Factory method)
	 * @param props
	 * @param version key in properties. ex) ${version}.password-encoder.algorithm=PBKDF2WithHmacSHA256
	 * @return PasswordEncoder
	 */
	public static PasswordEncoder getInstance(Properties props, String version) {
		if (StringUtils.isEmpty(version)) {
			version = "default";
		}
		try {
			String algorithm = props.getProperty(version+".password-encoder.algorithm", "PBKDF2WithHmacSHA256");
			if (algorithm.startsWith("PBKDF2")){
				int iterations = StringUtils.parse(props.getProperty(version+".password-encoder.iterations", "185000"), 185000);
				int hashWidth = StringUtils.parse(props.getProperty(version+".password-encoder.hash_width", "256"), 256);
				String secret = props.getProperty(version+".password-encoder.secret", EncryptSessionUtils.getSecretKey());
				return new Pbkdf2PasswordEncoder(secret, iterations, hashWidth).algorithm(algorithm);
			} else if (algorithm.startsWith("BCrypt")) {
				int strength = StringUtils.parse(props.getProperty(version+".password-encoder.strength", "10"), 10);
				return new BCryptPasswordEncoder(strength);
			} else if (algorithm.startsWith("Plain")) {
				return new PlainPasswordEncoder();
			} else if (algorithm.startsWith("SHA-")) {
				return new HashedPasswordEncoder(algorithm);
			} else {
				LOG.warn("Can not read a configurations. Loading default Pbkdf2PasswordEncoder.");
				return new Pbkdf2PasswordEncoder();
			}
		} catch (Exception e) {
			throw new UnsupportedEncoderException(e);
		}
	}
}
