package models.design;

import java.util.ArrayList;
import java.util.List;

public class Design {
	
	private String tShirtColor;
	private List<AbstractStamp> stamps = new ArrayList<AbstractStamp>();
	
	public String getTShirtColor() {
		return tShirtColor;
	}
	
	public void setTShirtColor(String tShirtColor) {
		this.tShirtColor = tShirtColor;
	}
	
	public List<AbstractStamp> getStamps() {
		return stamps;
	}

	public void setStamps(List<AbstractStamp> stamps) {
		this.stamps = stamps;
	}
	
}
