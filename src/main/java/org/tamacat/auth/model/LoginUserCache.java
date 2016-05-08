/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import org.tamacat.util.LimitedCacheLRU;

public class LoginUserCache extends LimitedCacheLRU<String, CacheSupportLoginUser> {

	public LoginUserCache(int maxSize, long expire) {
		super(maxSize, expire);
	}
}
