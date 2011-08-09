package models.design;

public class TextStamp extends AbstractStamp {

	private String text;
	private String font;
	private String color;
	
	public TextStamp() {
	}
	
	public TextStamp(double x, double y, double rotation, double scale, String text, String font, String color) {
		super(x, y, rotation, scale);
		this.text = text;
		this.font = font;
		this.color = color;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getFont() {
		return font;
	}
	
	public void setFont(String font) {
		this.font = font;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}	
	
}
