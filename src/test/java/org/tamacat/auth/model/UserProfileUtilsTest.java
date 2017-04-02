package org.tamacat.auth.model;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tamacat.auth.util.EncryptSessionUtils;

public class UserProfileUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProfile() {
		String json = "{\"tid\":\"tamacat.org\",\"id\":\"123\",\"user_id\":\"test@tamacat.org\",\"role\":\"user\",\"last_login\":\"2015-01-01 00:00:00.0\",\"multi_login\":\"1\",\"login_status\":\"logout\",\"username\":\"Test User\",\"email\":\"test@tamacat.org\"}";
		String profile = EncryptSessionUtils.encryptSession(json);
		
		LoginUser loginUser = new DefaultUser("users","tid","id","user_id","password","salt","role","last_login","multi_login","login_status");
		Set<String> columns = new LinkedHashSet<>();
		columns.add("user_id");
		columns.add("username");
		columns.add("email");
		
		loginUser = UserProfileUtils.getProfile(profile, (DefaultUser) loginUser, columns);
		assertEquals("test@tamacat.org", loginUser.get("user_id"));
		assertEquals("test@tamacat.org", loginUser.get("email"));
		assertEquals("Test User", loginUser.get("username"));
	}

}
