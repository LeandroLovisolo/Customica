package controllers.admin;

import play.Play;

public class Security extends controllers.Secure.Security {
	
    static boolean authenticate(String username, String password) {
        return Play.configuration.getProperty("admin.user").equals(username) &&
               Play.configuration.getProperty("admin.pass").equals(password);
    }
    
}
