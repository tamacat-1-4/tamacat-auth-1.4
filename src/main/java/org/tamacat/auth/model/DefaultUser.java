/*
 * Copyright (c) 2015 tamacat.org
 * All rights reserved.
 */
package org.tamacat.auth.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.tamacat.dao.meta.Column;
import org.tamacat.dao.meta.DataType;
import org.tamacat.dao.meta.DefaultColumn;
import org.tamacat.dao.meta.DefaultTable;
import org.tamacat.dao.orm.MapBasedORMappingBean;
import org.tamacat.util.StringUtils;

public class DefaultUser extends MapBasedORMappingBean<DefaultUser> implements UserRole, CacheSupportLoginUser {
	private static final long serialVersionUID = 1L;

	protected DefaultTable table;
	
	protected Map<String, Column> columnMap = new LinkedHashMap<>();
	protected List<DefaultColumn> columnList = new ArrayList<>();
	protected long createTime = System.currentTimeMillis();

	String tableName;
	String userKey;
	String passwordKey;
	String saltKey;
	String roleKey;
	String lastLoginKey; 
	String multiLoginKey;
	String loginStatusKey;
	
	public DefaultUser() {}
	
	public DefaultUser(String tableName, String userKey, String passwordKey,
			String saltKey, String roleKey, String lastLoginKey, 
			String multiLoginKey, String loginStatusKey, String... columns) {
		this.tableName = tableName;
		this.userKey = userKey;
		this.passwordKey = passwordKey;
		this.saltKey = saltKey;
		this.roleKey = roleKey;
		this.lastLoginKey = lastLoginKey;
		this.multiLoginKey = multiLoginKey;
		this.loginStatusKey = loginStatusKey;
		
		table = new DefaultTable(tableName);
		table.registerColumn(
			newColumn(userKey).type(DataType.STRING),
			newColumn(passwordKey).type(DataType.STRING)
		);
		
		if (StringUtils.isNotEmpty(saltKey)) {
			table.registerColumn(newColumn(saltKey).type(DataType.STRING));
		}
		if (StringUtils.isNotEmpty(roleKey)) {
			table.registerColumn(newColumn(roleKey).type(DataType.STRING));
		}
		if (StringUtils.isNotEmpty(lastLoginKey)) {
			table.registerColumn(newColumn(lastLoginKey).type(DataType.TIME));
		}
		if (StringUtils.isNotEmpty(multiLoginKey)) {
			table.registerColumn(newColumn(multiLoginKey).type(DataType.NUMERIC));
		}
		if (StringUtils.isNotEmpty(loginStatusKey)) {
			table.registerColumn(newColumn(loginStatusKey).type(DataType.STRING));
		}
		for (String colname : columns) {
			DefaultColumn col = newColumn(colname).type(DataType.STRING);
			columnList.add(col);
			table.registerColumn(col);
		}
	}
	
	public DefaultColumn newColumn(String key) {
		DefaultColumn col = new DefaultColumn(key);
		columnMap.put(key, col);
		return col;
	}
	
	public void setColumn(String key, Column col) {
		columnMap.put(key, col);
	}
	
	public Column getColumn(String key) {
		return columnMap.get(key);
	}
	
	@Override
	public boolean isEncrypted() {
		return StringUtils.isNotEmpty(getSalt());
	}
	
	@Override
	public boolean isMultiLoginAllowed() {
		if (StringUtils.isEmpty(getSalt())) {
			return true;
		} else if (StringUtils.isNotEmpty(multiLoginKey)) {
			return "1".equals(val(getColumn(multiLoginKey)));
		} else {
			return true;
		}
	}

	@Override
	public String getUserId() {
		return val(getColumn(userKey));
	}

	@Override
	public String getPassword() {
		return val(getColumn(passwordKey));
	}

	@Override
	public String getSalt() {
		if (saltKey != null) {
			return val(getColumn(saltKey));
		} else {
			return "";
		}
	}
	
	@Override
	public String getLoginStatus() {
		if (loginStatusKey != null) {
			return val(getColumn(loginStatusKey));
		} else {
			return "";
		}
	}
	
	@Override
	public boolean isCacheExpired(long expire) {
		return System.currentTimeMillis() - createTime > expire;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (StringUtils.isNotEmpty(role) && StringUtils.isNotEmpty(roleKey)) {
			return role.equalsIgnoreCase(val(getColumn(roleKey)));
		} else {
			return false;
		}
	}
	
	@Override
	public String toJson() {
		return super.toJson(columnMap.values().toArray(new Column[columnMap.size()])).build().toString();
	}
}
