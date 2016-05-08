/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import org.tamacat.dao.orm.ORMapper;

public class DefaultUserORMapper extends ORMapper<DefaultUser> {
	
	protected DefaultUser user;
	
	public DefaultUserORMapper(DefaultUser user){
		this.user = user;
	}
	
	@Override
	public DefaultUser getMappedObject() {
		return (DefaultUser) user.clone(); //new DefaultUser(user.tableName, userKey, passwordKey, saltKey, roleKey, lastLoginKey, multiLoginKey, loginStatusKey);
	}
}
