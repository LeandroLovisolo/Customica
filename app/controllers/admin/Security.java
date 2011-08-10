package controllers.admin;

public class Security extends controllers.Secure.Security {
	
	public static final String USERNAME = "leandro";
	public static final String PASSWORD = "tomahawk2007boreal";
	
    static boolean authenticate(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
    
}
