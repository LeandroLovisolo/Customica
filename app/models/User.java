package models;
 
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
 
@Entity
@Table(name="Users")
public class User extends Model {

	public Long facebookId;

	public static User current() {
		Long facebookId = FacebookService.get().getLoggedInUserId();
		if(facebookId == null) return null;
		return getByFacebookId(facebookId);
	}
	
	private static User getByFacebookId(Long facebookId) {
		User user = find("facebookId = ?", facebookId).first();
		if(user == null) {
			user = new User();
			user.facebookId = facebookId;
			user.save();
		}
		return user;
	}
	
}
