package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.TShirt.Gender;
import models.TShirt.Size;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="Orders")
public class Order extends Model {

	@ManyToOne
	public TShirt tShirt;
	public Gender gender;
	public Size size;
	
	@Required
	public String name;
	
	@Required
	@Email
	public String email;
	
	@Required
	public String phone;
	
	@Required
	public String address;
	
	@Required
	public String zipCode;
	
	@Required
	public String city;
	
	@Required
	public String province;
	
}
