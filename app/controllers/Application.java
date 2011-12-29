package controllers;

import java.util.HashMap;
import java.util.Map;

import models.Category;
import models.Order;
import models.TShirt;
import models.TShirt.Gender;
import models.TShirt.Size;
import models.dineromail.DineroMailHttpPost;
import models.dineromail.DineroMailHttpPostFactory;
import play.Play;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Router;
import play.templates.JavaExtensions;

public class Application extends Controller {

	private static final String LOGO_URL = "http://customica.com/images/logo.png";

	@Before
	static void addReadOnlyFlag() {
		renderArgs.put("readonly", isReadOnly());
	}

	public static void index() {
		renderArgs.put("newestTShirts", TShirt.findLatest6());
		renderArgs.put("categories", Category.findAll());
		render();
	}

	public static void category(Long id, String slug) {
		Category category = (Category) (id == null ? null : Category.findById(id));
		renderArgs.put("categories", Category.findAll());
		renderArgs.put("category", category);
		renderArgs.put("tShirts", category == null ? TShirt.findLatest() : category.getTShirts());
		render();
	}

	public static void tshirt(Long id, String slug) {
		TShirt tShirt = TShirt.findById(id);
		if(tShirt == null) Application.index();

		// Open Graph tags
		renderArgs.put("ogTitle", tShirt.title);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", tShirt.id);
		params.put("slug", JavaExtensions.slugify(tShirt.title));
		renderArgs.put("ogUrl", Router.getFullUrl("Application.tshirt", params));
		renderArgs.put("ogImage", request.getBase() + tShirt.getDesignUrl());

		renderArgs.put("tShirt", tShirt);
		renderArgs.put("moreTShirtsInCategory", tShirt.category.getTop4TShirts());
		render();
	}

	public static void buyButton(Long id, Gender gender, Size size) {
		failIfReadOnly();
		order(id, genderToUrlParam(gender), sizeToUrlParam(size));
	}

	public static void order(Long id, String gender, String size) {
		failIfReadOnly();
		TShirt tShirt = TShirt.findById(id);
		Gender genderValue = genderFromUrlParam(gender);
		Size sizeValue = sizeFromUrlParam(size);
		if(tShirt == null || genderValue == null || sizeValue == null) Application.index();
		renderArgs.put("tShirt", tShirt);
		renderArgs.put("gender", genderValue);
		renderArgs.put("size", sizeValue);
		render();
	}

	public static void submitOrder(Long id, Gender gender, Size size, @Valid Order order) {
		failIfReadOnly();
		TShirt tShirt = TShirt.findById(id);
		if(tShirt == null) Application.index();

		if(validation.hasErrors()) {
			params.flash();
			validation.keep();
			order(id, genderToUrlParam(gender), sizeToUrlParam(size));
		} else {
			order.tShirt = tShirt;
			order.gender = gender;
			order.size = size;
			order.save();
			flash.put("orderId", order.id);
			redirectToDineroMail();
		}
	}

	public static void redirectToDineroMail() {
		Long orderId = null;
		try {
			orderId = Long.parseLong(flash.get("orderId"));
		} catch(NumberFormatException e) {
			Application.index();
		}

		Order order = Order.findById(orderId);
		if(order == null) Application.index();

		DineroMailHttpPost post = DineroMailHttpPostFactory.create(order, LOGO_URL,
				Router.getFullUrl("Application.thanks"), Router.getFullUrl("Application.index"));

		render(post);
	}

	public static void thanks() {
		renderText("Gracias!");
	}

	public static void designer(Long id) {
		if(id != null) {
			TShirt tShirt = TShirt.findById(id);
			if(tShirt == null) error("TShirt doesn't exist");
			if(!tShirt.isAuthorLoggedIn()) error("User is not author.");
			renderArgs.put("tShirt", tShirt);
			renderArgs.put("xml", new String(tShirt.xml).replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n"));
		}
		renderArgs.put("categories", Category.findAll());
		render();
	}

	public static void submitDesign(Long id, String xml, String title, Long categoryId, boolean shareOnFacebook) {
		failIfReadOnly();

		if(xml == null || title == null || categoryId == null) error("Invalid input.");

		TShirt tShirt = null;
		if(id != null) tShirt = TShirt.findById(id);

		if(tShirt == null) {
			tShirt = TShirt.create(title, categoryId, xml.getBytes(), shareOnFacebook);
		} else {
			tShirt.update(title, categoryId, xml.getBytes());
		}

		tshirt(tShirt.id, JavaExtensions.slugify(tShirt.title));
	}

	public static void deleteTShirt(Long id) {
		failIfReadOnly();

		TShirt tShirt = TShirt.findById(id);
		if(tShirt != null && tShirt.isAuthorLoggedIn()) tShirt.delete();
		Application.index();
	}

	private static String genderToUrlParam(Gender gender) {
		return gender == Gender.MALE ? "varon" : "mujer";
	}

	private static Gender genderFromUrlParam(String gender) {
		if(gender == null) return null;
		return gender.equals("varon") ? Gender.MALE : Gender.FEMALE;
	}

	private static String sizeToUrlParam(Size size) {
		return size.toString().toLowerCase();
	}

	private static Size sizeFromUrlParam(String size) {
		if(size == null) return null;
		return Size.fromString(size.toUpperCase());
	}

	private static boolean isReadOnly() {
		return "true".equals(Play.configuration.getProperty("readonly").trim().toLowerCase());
	}

	private static void failIfReadOnly() {
		if(isReadOnly()) throw new RuntimeException("Action not available in read-only mode.");
	}

}