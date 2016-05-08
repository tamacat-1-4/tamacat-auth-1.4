package org.tamacat.auth.model;

import java.io.ByteArrayInputStream;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.tamacat.auth.util.EncryptSessionUtils;
import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.util.StringUtils;

public class UserProfileUtils {

	static final Log LOG = LogFactory.getLog(UserProfileUtils.class);
	
	public static LoginUser getProfile(String profile, LoginUser loginUser, Set<String> columns) {
		String json = EncryptSessionUtils.decryptSession(profile);
		try {
			JsonReader reader = Json.createReader(new ByteArrayInputStream(json.getBytes("UTF-8")));
			JsonObject obj = reader.readObject();
			for (String col : columns) {
				String value = obj.getString(col, "");
				if (StringUtils.isNotEmpty(value)) {
					loginUser.put(col, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.trace("json="+json);
			LOG.warn("ERROR:"+e.getMessage());
		}
		if (loginUser instanceof LoginUser) {
			return (LoginUser)loginUser;
		}
		return null;
	}
}
