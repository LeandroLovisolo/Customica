package models.design;

public class ImageStamp extends AbstractStamp {

	private byte[] image;
	
	public ImageStamp() {
	}
	
	public ImageStamp(double x, double y, double rotation, double scale, byte[] image) {
		super(x, y, rotation, scale);
		this.image = image;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
