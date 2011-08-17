package models.design;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import play.Play;

import models.TShirt;

public class DesignRenderingService {
	
	private static final int DESIGN_WIDTH_MM = 210;
	private static final int FULL_SIZE_DESIGN_WIDTH = 2480;
	private static final int FULL_SIZE_DESIGN_HEIGHT = 3508;
	private static final String FONTS_PATH = Play.applicationPath + "/fonts";
	
	private static DesignRenderingService instance;
	
	public static DesignRenderingService get() {
		if(instance == null) instance = new DesignRenderingService();
		return instance;
	}
	
	private DesignRenderingService() {
	}
	
	public BufferedImage render(TShirt tShirt) {
		byte[] xml = null;
		xml = tShirt.xml;
		Design design = DesignParser.parse(xml);
		
		BufferedImage designImage = new BufferedImage(FULL_SIZE_DESIGN_WIDTH, FULL_SIZE_DESIGN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = designImage.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		for(AbstractStamp stamp : design.getStamps()) {
			if(stamp instanceof ImageStamp) {
				renderImageStamp((ImageStamp) stamp, graphics);
			} else if(stamp instanceof TextStamp) {
				renderTextStamp((TextStamp) stamp, graphics);
			}
		}
		
		return designImage;
	}
	
	public byte[] renderBytes(TShirt tShirt) {
		return ImageUtils.getBytes(render(tShirt));
	}

	private void renderImageStamp(ImageStamp imageStamp, Graphics2D graphics) {
		AffineTransform transform = new AffineTransform();
		transform.translate(mmToPx(imageStamp.getX()), mmToPx(imageStamp.getY()));
		transform.rotate(imageStamp.getRotation() * Math.PI / 180.0);
		transform.scale(imageStamp.getScale(), imageStamp.getScale());
		graphics.drawImage(ImageUtils.getImage(imageStamp.getImage()), transform, null);
	}
	
	private void renderTextStamp(TextStamp textStamp, Graphics2D graphics) {
		BufferedImage textImage = new BufferedImage(FULL_SIZE_DESIGN_WIDTH, FULL_SIZE_DESIGN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D textGraphics = textImage.createGraphics();
		textGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		textGraphics.setPaint(new Color(Integer.valueOf(textStamp.getColor())));
		textGraphics.setFont(createTextStampFont(textStamp.getFont()).deriveFont(Font.PLAIN, (int) (48 * textStamp.getScale())));
		textGraphics.drawString(textStamp.getText(), 0, textGraphics.getFontMetrics().getHeight());
		
		AffineTransform textTransform = new AffineTransform();
		textTransform.translate(mmToPx(textStamp.getX()), mmToPx(textStamp.getY()));
		textTransform.rotate(textStamp.getRotation() * Math.PI / 180);
		
		graphics.drawImage(textImage, textTransform, null);
	}
	
	private Font createTextStampFont(String fontName) {
		String path = getFontPaths().get(fontName);
		if(path == null) throw new RuntimeException("Font \"" + fontName + "\" not found");
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Map<String, String> getFontPaths() {
		Map<String, String> paths = new HashMap<String, String>();
		for(File file : new File(FONTS_PATH).listFiles()) {
			String path = file.getPath();
			String name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".ttf"));
			paths.put(name, path);
		}
		return paths;
	}

	private int mmToPx(double mm) {
		return (int) (mm * (double) FULL_SIZE_DESIGN_WIDTH / (double) DESIGN_WIDTH_MM);
	}
	
}
