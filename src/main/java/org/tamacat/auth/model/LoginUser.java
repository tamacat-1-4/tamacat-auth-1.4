/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import java.io.Serializable;

//import org.tamacat.dao.orm.ORMappingSupport;

public interface LoginUser extends /* ORMappingSupport<LoginUser>,*/ Serializable {

	/**
	 * Internal Unique ID
	 * @since 1.4
	 */
	String getId();
	
	/**
	 * Tenant ID
	 * @since 1.4
	 */
	String getTid();
	
	/**
	 * Login ID
	 * @return
	 */
	String getUserId();
	
	/**
	 * Password
	 * @return
	 */
	String getPassword();
	
	String getSalt();
	
	String getLoginStatus();
	
	boolean isEncrypted();
	
	boolean isMultiLoginAllowed();
	
	String toJson();

	Object get(Object name);
	Object put(String name, Object value);
}
