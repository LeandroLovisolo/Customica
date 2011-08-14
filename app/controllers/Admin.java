package controllers;

import play.libs.F.Promise;
import controllers.Secure;
import controllers.admin.RegenerateDesignsJob;

public class Admin extends Secure {

	public static void index() {
		renderTemplate("Admin/admin.html");
	}
	
	public static void regenerateDesigns() {
		response.chunked = true;
		RegenerateDesignsJob job = new RegenerateDesignsJob();
		while(job.hasTShirtsLeft()) {
			response.writeChunk(await(job.now()));
		}
	}
	
}
