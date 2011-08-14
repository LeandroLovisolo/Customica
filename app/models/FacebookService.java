package models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Codec;
import play.mvc.Http;

import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.FacebookType;

public class FacebookService {
	
	public static final String API_KEY = "b382872fb2c9602a341100ef74a19e2c";
	public static final String SECRET_KEY = "68651df886984c0dfeed75f74873f24e";
	public static final String COOKIE_NAME = "fbs_" + API_KEY;
	
	public enum CookieProperties {
		
		USER_ID("uid"),
		EXPIRES("expires"),
		SECRET("secret"),
		BASE_DOMAIN("base_domain"),
		SESSION_KEY("session_key"),
		ACCESS_TOKEN("access_token"),
		SIGNATURE("sig");
		
		public String propertyName;
		
		private CookieProperties(String name) {
			this.propertyName = name;
		}
		
		@Override
		public String toString() {
			return propertyName;
		}
		
	}
	
	private static FacebookService instance;

	public static FacebookService get() {
		if(instance == null) instance = new FacebookService();
		return instance;
	}
	
	private FacebookService() {
	}
	
	public Long getLoggedInUserId() {
		if(isSignatureValid()) {
			return Long.valueOf(getCookieProperty(CookieProperties.USER_ID));
		} else {
			return null;
		}
	}
	
	private boolean isSignatureValid() {
		Map<String, String> cookieProperties = getCookieProperties();
		if(cookieProperties == null) {
			return false;
		} else {
			String payload = "";
			List<String> propertyNames = new ArrayList<String>(cookieProperties.keySet());
			Collections.sort(propertyNames);
			for(String propertyName : propertyNames) {
				if(!propertyName.equals(CookieProperties.SIGNATURE.propertyName)) {
					payload += propertyName + "=" + getCookieProperty(propertyName);
				}
			}
			payload += SECRET_KEY;
			return getCookieProperty(CookieProperties.SIGNATURE).equals(Codec.hexMD5(payload));
		}
	}
	
	private FacebookClient getFacebookClient() {
		return new DefaultFacebookClient(getCookieProperty(CookieProperties.ACCESS_TOKEN));
	}

	public String getUserEmail() throws FacebookException {
		FacebookClient facebookClient = getFacebookClient();
		com.restfb.types.User user = facebookClient.fetchObject("me", com.restfb.types.User.class);
		return user.getEmail();
	}
	
	public void postToWall(String message, String picture, String link, String name, String caption, String description) {
		FacebookClient facebookClient = getFacebookClient();		
		try {
			FacebookType response = facebookClient.publish(
					"me/feed",
					FacebookType.class,
				    Parameter.with("message", message),
				    Parameter.with("picture", picture),
				    Parameter.with("link", link),
				    Parameter.with("name", name),
				    Parameter.with("caption", caption),
				    Parameter.with("description", nonNull(description)));
			Logger.debug("Published Facebook message ID: " + response.getId());
		} catch (FacebookException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String nonNull(String string) {
		return string == null ? "" : string;
	}
	
	public boolean hasPublishStreamPermission() {
		class Permissions {
			@Facebook(value="publish_stream")
			Integer publishStream;
		}
		
		FacebookClient facebookClient = getFacebookClient();		
		List<Permissions> results = null;
		try {
			results = facebookClient.executeQuery(
					"SELECT publish_stream FROM permissions WHERE uid = me()",
					Permissions.class);
		} catch (FacebookException e) {
			throw new RuntimeException(e);
		}
		return results.get(0).publishStream == 1;
	}
	
	private String getCookieProperty(CookieProperties cookieProperty) {
		return getCookieProperty(cookieProperty.propertyName);
	}
	
	private String getCookieProperty(String property) {
		return getCookieProperties().get(property);
	}
	
	private Map<String, String> getCookieProperties() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			for(String keyValue : getCookie().split("&")) {
				String keyValueArray[] = keyValue.split("=");
				try {
					map.put(URLDecoder.decode(keyValueArray[0], "utf-8"), URLDecoder.decode(keyValueArray[1], "utf-8"));
				} catch(IndexOutOfBoundsException e) {
					return null;
				} catch(UnsupportedEncodingException e) {
					return null;
				}
			}
		} catch(NullPointerException e) {
			return null;
		}
		return map;
	}	
	
	private String getCookie() {
		if(Http.Request.current().cookies == null) {
			return null;
		} else {
			for(String name : Http.Request.current().cookies.keySet()) {
				if(name.equals(COOKIE_NAME)) return Http.Request.current().cookies.get(name).value;
			}
			return null;
		}
	}
	
}