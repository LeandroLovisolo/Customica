package models;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import models.design.DesignRenderingService;
import models.design.ImageUtils;
import play.Play;

public class ThumbnailService {
	
	private static final String BASE_TSHIRT_PATH = Play.applicationPath + "/assets/base-tshirt.png";
	private static final double ON_SCREEN_DESIGN_WIDTH = 212;
	private static final int DESIGN_POSITION_X = 124;
	private static final int DESIGN_POSITION_Y = 90;
	private static final double THUMBNAIL_SCALE = 0.5;

	private static ThumbnailService instance;
	
	public static ThumbnailService get() {
		if(instance == null) instance = new ThumbnailService();
		return instance;
	}
	
	private ThumbnailService() {
	}
	
	public void generateDesignAndThumbnail(TShirt tShirt) {
		BufferedImage designOnTShirt = composeDesignOnTShirtImage(tShirt);
		savePicture(tShirt, designOnTShirt);
		saveThumbnail(tShirt, designOnTShirt);
	}
	
	private BufferedImage composeDesignOnTShirtImage(TShirt tShirt) {
		BufferedImage scaledDesign = getScaledDesign(tShirt);
		BufferedImage baseTShirt = getBaseTShirt();
		BufferedImage composite = new BufferedImage(baseTShirt.getWidth(), baseTShirt.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = composite.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.drawImage(baseTShirt, 0, 0, baseTShirt.getWidth(), baseTShirt.getHeight(), null);
		graphics.drawImage(scaledDesign, DESIGN_POSITION_X, DESIGN_POSITION_Y, scaledDesign.getWidth(), scaledDesign.getHeight(), null);
		return composite;
	}
	
	private BufferedImage getScaledDesign(TShirt tShirt) {
		BufferedImage design = DesignRenderingService.get().render(tShirt);
		return ImageUtils.scale(
				design,
				ON_SCREEN_DESIGN_WIDTH / Double.valueOf(design.getWidth()));
	}
	
	private BufferedImage getBaseTShirt() {
		File file = new File(BASE_TSHIRT_PATH);
		byte[] imageData = new byte[(int) file.length()];
		try {
			FileInputStream stream = new FileInputStream(file);
			stream.read(imageData);
			stream.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
		return ImageUtils.getImage(imageData);
	}
	
	private void saveThumbnail(TShirt tShirt, BufferedImage designOnTShirt) {
		BufferedImage thumbnail = ImageUtils.scale(designOnTShirt, THUMBNAIL_SCALE);
		writeImageToDisk(thumbnail, tShirt.getThumbnailPath());
	}
	
	private void savePicture(TShirt tShirt, BufferedImage designOnTShirt) {
		writeImageToDisk(designOnTShirt, tShirt.getDesignPath());
	}
	
	private void writeImageToDisk(BufferedImage image, String path) {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
			stream.write(ImageUtils.getBytes(image));
			stream.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
