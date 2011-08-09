package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.db.jpa.Model;

@Entity
public class Category extends Model {

	public String name;
	public boolean isDefault;
	
	public List<TShirt> getTShirts() {
		return TShirt.find("category = ? order by created desc", this).fetch();
	}
	
	public List<TShirt> getTop4TShirts() {
		return TShirt.find("category = ?", this).fetch(4);
	}
	
}
