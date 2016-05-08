/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth;

public interface OneTimePassword {
	
	void setTimeFormat(String timeFormat);
	
	/**
	 * @since 1.4-20160508
	 * @param timeUnit
	 */
	void setTimeUnit(int timeUnit);

	void setAlgorithm(String algorithm);

	String generate(String secret);

	boolean check(String secret, String id);
	
	/**
	 * @since 1.4-20160508
	 * @param caseSensitive
	 */
	void setCaseSensitive(boolean caseSensitive);

}