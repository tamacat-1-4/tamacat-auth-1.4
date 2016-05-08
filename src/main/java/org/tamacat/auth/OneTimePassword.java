/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth;

public interface OneTimePassword {
	
	void setTimeFormat(String timeFormat);

	void setAlgorithm(String algorithm);

	String generate(String secret);

	boolean check(String secret, String id);

}