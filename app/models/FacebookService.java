package models;

import java.util.List;

import play.Logger;
import play.Play;
import play.libs.WS;
import play.mvc.Http;

import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.types.FacebookType;

public class FacebookService {
	
	public static final String API_KEY = "109218475776229"; //"b382872fb2c9602a341100ef74a19e2c";
	public static final String SECRET_KEY = "68651df886984c0dfeed75f74873f24e";
	public static final String COOKIE_NAME = "fbsr_" + API_KEY;
	
	private static FacebookService instance;

	public static FacebookService get() {
		if(instance == null) instance = new FacebookService();
		return instance;
	}
	
	private FacebookService() {
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
	
	private FacebookCookie getValidatedFacebookCookie() {
		FacebookCookie cookie = new FacebookCookie(getCookie());
		if(!cookie.validate(SECRET_KEY)) return null;
		return cookie;
	}
	
	public Long getLoggedInUserId() {
		FacebookCookie cookie = getValidatedFacebookCookie();
		return cookie == null ? null : cookie.getUserId();
	}
	
	private String getAccessToken() {
		FacebookCookie cookie = getValidatedFacebookCookie();
		if(cookie == null) return null;
		WS.HttpResponse response = WS.url("https://graph.facebook.com/oauth/access_token?"
                + "client_id=" + API_KEY
                + "&redirect_uri="
                + "&client_secret=" + SECRET_KEY
                + "&code=" + cookie.getCode()).get();
		
		for(String parameter : response.getString().split("&")) {
			String pair[] = parameter.split("=");
			if(pair.length > 1 && "access_token".equals(pair[0])) {
				return pair[1];
			}
		}
		
		return null;
	}
	
	private FacebookClient getFacebookClient() {
		return new DefaultFacebookClient(getAccessToken());
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
	
}