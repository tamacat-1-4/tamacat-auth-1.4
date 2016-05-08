/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import org.tamacat.dao.Condition;
import org.tamacat.dao.DaoAdapter;
import org.tamacat.dao.Query;
import org.tamacat.dao.Search;
import org.tamacat.dao.meta.Column;

public class DefaultUserDao extends DaoAdapter<DefaultUser> {
	
	public DefaultUser search(DefaultUser user) {
		setORMapper(new DefaultUserORMapper(user));

		Search search = createSearch();
		if (user.isNotEmpty(user.getColumn(user.userKey))) {
			Column col = user.getColumn(user.userKey);
			search.and(col, Condition.EQUAL, user.val(col));
		} else {
			return null;
		}
		Query<DefaultUser> query = createQuery().select(user.table.columns()).and(search, null);
		return super.search(query);
	}

	public int updateUserLastLogin(DefaultUser user) {
		if (user.isEmpty(user.getColumn(user.userKey))) {
			return 0;
		}
		setORMapper(new DefaultUserORMapper(user));
		Query<DefaultUser> query = createQuery()
			.addUpdateColumn(user.getColumn(user.lastLoginKey))
			.addUpdateColumn(user.getColumn(user.loginStatusKey))
			.where(param(user.getColumn(user.userKey), Condition.EQUAL, user.val(user.getColumn(user.userKey))));
		return executeUpdate(query.getUpdateSQL(user));
	}
	
	public int updateUserSalt(DefaultUser user) {
		if (user.isEmpty(user.getColumn(user.userKey))) {
			return 0;
		}
		setORMapper(new DefaultUserORMapper(user));
		Query<DefaultUser> query = createQuery()
			.addUpdateColumn(user.getColumn(user.saltKey))
			.addUpdateColumn(user.getColumn(user.lastLoginKey))
			.addUpdateColumn(user.getColumn(user.loginStatusKey))
			.where(param(user.getColumn(user.userKey), Condition.EQUAL, user.val(user.getColumn(user.userKey))));
		return executeUpdate(query.getUpdateSQL(user));
	}
}
