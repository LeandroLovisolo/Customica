package models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.Play;
import play.db.jpa.Model;
import play.mvc.Http;
import play.mvc.Router;
import play.templates.JavaExtensions;

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
	
    //@Column(columnDefinition="LONGBLOB") 
	public byte[] xml;
	
	@ManyToOne
	public User author;
	
	@ManyToOne
	public Category category;
	
	public static TShirt create(String title, Long categoryId, byte[] xml, boolean shareOnFacebook) {
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
		System.out.println("Share on facebook? " + shareOnFacebook);
		if(shareOnFacebook) shareOnFacebook(tShirt);
		return tShirt;
	}

	private static void shareOnFacebook(TShirt tShirt) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", tShirt.id);
		params.put("slug", JavaExtensions.slugify(tShirt.title));
		FacebookService.get().postToWall(
				"",
				Http.Request.current().getBase() + tShirt.getThumbnailUrl(),
				Router.getFullUrl("Application.tshirt", params),
				"¡Miren la remera que diseñé!",
				tShirt.title,
				"Entrá a Customica.com y diseñá la tuya.");
	}
	
	public void update(String title, Long categoryId, byte[] xml) {
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
		return BASE_DESIGN_PATH + "/" + getDesignFilename();
	}
	
	public String getThumbnailPath() {
		return BASE_DESIGN_PATH + "/" + getThumbnailFilename();
	}
	
	public String getDesignUrl() {
		return BASE_DESIGN_URL + "/" + getDesignFilename();
	}
	
	public String getThumbnailUrl() {
		return BASE_DESIGN_URL + "/" + getThumbnailFilename();
	}	
	
	private String getDesignFilename() {
		return id + ".png";
	}
	
	private String getThumbnailFilename() {
		return id + "_thumb.png";
	}
	
	public static List<TShirt> findLatest6() {
		return TShirt.find("order by created desc").fetch(6);
	}
	
	public static List<TShirt> findLatest() {
		return TShirt.find("order by created desc").fetch();
	}
	
}
