package org.tamacat.auth.model;

import java.io.Serializable;

import org.tamacat.util.StringUtils;

public class SingleSignOnSession implements Serializable {
	
	private static final long serialVersionUID = 8409715891935518505L;
	
	String profile;
	String username;
	String sessionId;
	String created;
	
	public SingleSignOnSession() {}
	
	public static SingleSignOnSession parseSession(String value) {
		if (value != null && value.indexOf("\t") >= 0) {
			String[] sep = value.split("\t");
			if (sep.length >= 3) {
				String username = sep[0];
				String sessionId = sep[1];
				String time = sep[2];
				if (StringUtils.isNotEmpty(sessionId) && StringUtils.isNotEmpty(username)
						&& StringUtils.isNotEmpty(time)) {
					SingleSignOnSession session = new SingleSignOnSession();
					session.setUsername(username);
					session.setSessionId(sessionId);
					session.setCreated(time);
					if (sep.length >= 4) {
						String profile = sep[3];
						session.setProfile(profile);
					}
					return session;
				}
			}
		}
		return null;
	}
	
	public String getProfile() {
		return profile;
	}
	
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}
