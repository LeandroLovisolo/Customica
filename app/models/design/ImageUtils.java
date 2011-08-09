package models.design;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.thebuzzmedia.imgscalr.Scalr;
import com.thebuzzmedia.imgscalr.Scalr.Method;

public class ImageUtils {

	public static BufferedImage getImage(byte[] bytes) {
		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] getBytes(BufferedImage image) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", outputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return outputStream.toByteArray();
	}
	
	public static BufferedImage scale(BufferedImage image, double scale) {
		return Scalr.resize(
				image,
				Method.QUALITY,
				new Double(image.getWidth() * scale).intValue(),
				new Double(image.getHeight() * scale).intValue());
	}
	
}
