/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import java.io.Serializable;

import org.tamacat.dao.orm.ORMappingSupport;

public interface LoginUser extends ORMappingSupport, Serializable {

	String getUserId();
	
	String getPassword();
	
	String getSalt();
	
	String getLoginStatus();
	
	boolean isEncrypted();
	
	boolean isMultiLoginAllowed();
	
	String toJson();

	Object get(Object name);
}
