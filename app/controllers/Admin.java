package controllers;

import controllers.admin.ImportDesignsJob;
import controllers.admin.RegenerateDesignsJob;

public class Admin extends Secure {

	public static void index() {
		renderTemplate("Admin/admin.html");
	}
	
	public static void importDesigns() {
		response.chunked = true;
		ImportDesignsJob job = new ImportDesignsJob();
		while(job.hasTShirtsLeft()) {
			response.writeChunk(await(job.now()));
		}
	}
	
	public static void regenerateDesigns() {
		response.chunked = true;
		RegenerateDesignsJob job = new RegenerateDesignsJob();
		while(job.hasTShirtsLeft()) {
			response.writeChunk(await(job.now()));
		}
	}
	
}
