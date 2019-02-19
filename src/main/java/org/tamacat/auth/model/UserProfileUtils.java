package org.tamacat.auth.model;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue.ValueType;

import org.tamacat.auth.util.EncryptSessionUtils;
import org.tamacat.dao.meta.Column;
import org.tamacat.dao.orm.MapBasedORMappingBean;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.StringUtils;

public class UserProfileUtils {

	static final Log LOG = LogFactory.getLog(UserProfileUtils.class);
	
	public static <T extends LoginUser> T getProfile(String profile, T loginUser, Collection<String> columns) {
		String json = EncryptSessionUtils.decryptSession(profile);
		if (json == null) return null;
		try {
			JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes("UTF-8")));
			JsonObject obj = reader.readObject();
			for (String col : columns) {
				if (obj.get(col) == null) continue;
				ValueType type = obj.get(col).getValueType();
				if (type == ValueType.STRING) {
					String value = obj.getString(col, "");
					if (StringUtils.isNotEmpty(value)) {
						loginUser.put(col, value);
					}
				} else if (type == ValueType.NUMBER) {
					JsonNumber value = obj.getJsonNumber(col);
					if (value != null) {
						loginUser.put(col, value.longValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.trace("json="+json);
			LOG.warn("ERROR:"+e.getMessage());
		}
		return loginUser;
	}
	
	public static <T extends MapBasedORMappingBean<T>> T getProfile(String profile, T loginUser, Collection<Column> columns) {
		String json = EncryptSessionUtils.decryptSession(profile);
		if (json == null) return null;
		try {
			JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes("UTF-8")));
			JsonObject obj = reader.readObject();
			for (Column col : columns) {
				if (obj.get(col.getName()) == null) continue;
				ValueType type = obj.get(col.getName()).getValueType();
				if (type == ValueType.STRING) {
					String value = obj.getString(col.getName(), "");
					if (StringUtils.isNotEmpty(value)) {
						loginUser.val(col, value);
					}
				} else if (type == ValueType.NUMBER) {
					JsonNumber value = obj.getJsonNumber(col.getName());
					if (value != null) {
						loginUser.val(col, value.longValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.trace("json="+json);
			LOG.warn("ERROR:"+e.getMessage());
		}
		return loginUser;
	}
}