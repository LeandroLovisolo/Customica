package models.design;

public abstract class AbstractStamp {
	
	private double x;
	private double y;
	private double rotation;
	private double scale;
	
	public AbstractStamp() {
	}
	
	public AbstractStamp(double x, double y, double rotation, double scale) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}

}
