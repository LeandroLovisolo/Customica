package controllers.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.TShirt;
import play.Play;
import play.jobs.Job;


public class ImportDesignsJob extends Job<String> {

	private List<TShirt> tShirts;
	private int currentTShirtIndex = 0;
	
	public ImportDesignsJob() {
		tShirts = TShirt.findAll();
	}
	
	public boolean hasTShirtsLeft() {
		return currentTShirtIndex < tShirts.size();
	}
	
	@Override
	public String doJobWithResult() throws Exception {
		if(!hasTShirtsLeft()) return "Done";
		TShirt tShirt = tShirts.get(currentTShirtIndex);
		importDesign(tShirt);
		String message = "(" + (currentTShirtIndex + 1) + "/" + tShirts.size() + ") ID " + tShirt.id + ": " + tShirt.title;
		currentTShirtIndex++;
		return message;
	}
	
	private void importDesign(TShirt tShirt) {
		tShirt = tShirt.merge();
		File file = new File(Play.applicationPath + "/designs/design-" + tShirt.id + ".xml");
		byte[] xml = new byte[(int) file.length()];
		try {
			FileInputStream stream = new FileInputStream(file);
			stream.read(xml);
			stream.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tShirt.xml = xml;
		tShirt.save();
	}
	
}
