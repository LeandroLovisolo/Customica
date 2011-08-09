package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.Play;
import play.db.jpa.Model;

@Entity
public class TShirt extends Model {
	
	public enum Gender { MALE, FEMALE }
	
	public enum Size {
		
		S("S"), M("M"), L("L"), XL("XL"), XXL("2XL"), XXXL("3XL");
		
		private String asString;
		
		Size(String asString) {
			this.asString = asString;
		}
		
		@Override
		public String toString() {
			return asString;
		}

		public static Size fromString(String string) {
			for(Size size : Size.values()) {
				if(size.toString().equals(string)) return size;
			}
			return null;
		}
		
	}
	
	public static final Integer PRICE = 50;
	public static final Integer SHIPPING_COST = 25;

	private static final String BASE_DESIGN_PATH = Play.applicationPath + "/public/designs";
	private static final String BASE_DESIGN_URL = "/designs";
	
	public Date created;
	public String title;
	
	@Lob
	public String xml;
	
	@ManyToOne
	public User author;
	
	@ManyToOne
	public Category category;
	
	public static TShirt create(String title, Long categoryId, String xml) {
		TShirt tShirt = new TShirt();
		tShirt.created = new Date();
		tShirt.title = title;
		tShirt.xml = xml;
		tShirt.author = User.current();
		tShirt.category = Category.findById(categoryId);
		if(tShirt.author == null) throw new RuntimeException("User not logged in.");
		if(tShirt.category == null) throw new RuntimeException("Category doesn't exist.");
		tShirt.save();
		ThumbnailService.get().generateDesignAndThumbnail(tShirt);
		return tShirt;
	}
	
	public void update(String title, Long categoryId, String xml) {
		if(!isAuthorLoggedIn()) throw new RuntimeException("User is not author.");
		this.title = title;
		this.category = Category.findById(categoryId);
		this.xml = xml;
		if(this.category == null) throw new RuntimeException("Category doesn't exist.");
		save();
		ThumbnailService.get().generateDesignAndThumbnail(this);
	}
	
	public boolean isAuthorLoggedIn() {
		return User.current() == author;
	}
	
	public String getDesignPath() {
		return BASE_DESIGN_PATH + "/" + id + ".png";
	}
	
	public String getThumbnailPath() {
		return BASE_DESIGN_PATH + "/" + id + "_thumb.png";
	}
	
	public static List<TShirt> findLatest6() {
		return TShirt.find("order by created desc").fetch(6);
	}
	
	public static List<TShirt> findLatest() {
		return TShirt.find("order by created desc").fetch();
	}
	
}
