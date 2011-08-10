package controllers.admin;

import java.util.List;

import models.TShirt;
import models.ThumbnailService;
import play.jobs.Job;

public class RegenerateDesignsJob extends Job<String> {

	private List<TShirt> tShirts;
	private int currentTShirtIndex = 0;
	
	public RegenerateDesignsJob() {
		tShirts = TShirt.findAll();
	}
	
	public boolean hasTShirtsLeft() {
		return currentTShirtIndex < tShirts.size();
	}
	
	@Override
	public String doJobWithResult() throws Exception {
		if(!hasTShirtsLeft()) return "Done";
		TShirt tShirt = tShirts.get(currentTShirtIndex);
		ThumbnailService.get().generateDesignAndThumbnail(tShirt);
		String message = "(" + (currentTShirtIndex + 1) + "/" + tShirts.size() + ") ID " + tShirt.id + ": " + tShirt.title;
		currentTShirtIndex++;
		return message;
	}
	
}
