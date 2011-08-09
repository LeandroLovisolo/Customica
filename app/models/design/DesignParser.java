package models.design;

import java.io.StringReader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import play.libs.Codec;

public class DesignParser {
	
	private Document document;
	private Design design;
	
	public static Design parse(String xml) {
		try {
			return new DesignParser(xml).design;
		} catch (DocumentException e) {
			throw new RuntimeException("Cannot parse design XML.", e);
		}
	}

	private DesignParser(String xml) throws DocumentException {
		document = new SAXReader().read(new StringReader(xml));
		parseDesign();
	}
	
	private void parseDesign() {
		design = new Design();
		setTShirtColor();
		setStamps();
	}

	@SuppressWarnings("unchecked")
	private void setStamps() {
		for(Element element : (List<Element>) document.getRootElement().elements()) {
			if(element.getName().equals("imageStamp")) {
				design.getStamps().add(parseImageStamp(element));
			} else if(element.getName().equals("textStamp")) {
				design.getStamps().add(parseTextStamp(element));
			}
		}
	}

	private void setTShirtColor() {
		design.setTShirtColor(document.getRootElement().attributeValue("tShirtColor"));
	}
	
	private ImageStamp parseImageStamp(Element element) {
		ImageStamp imageStamp = new ImageStamp();
		parseAbstractStamp(element, imageStamp);
		imageStamp.setImage(Codec.decodeBASE64(element.element("image").getText()));
		return imageStamp;
	}
	
	private TextStamp parseTextStamp(Element element) {
		TextStamp textStamp = new TextStamp();
		parseAbstractStamp(element, textStamp);
		textStamp.setText(element.element("text").getText());
		textStamp.setFont(element.element("font").getText());
		textStamp.setColor(element.element("color").getText());
		return textStamp;
	}

	private void parseAbstractStamp(Element element, AbstractStamp stamp) {
		stamp.setX(Float.valueOf(element.attributeValue("x")));
		stamp.setY(Float.valueOf(element.attributeValue("y")));
		stamp.setRotation(Float.valueOf(element.attributeValue("rotation")));
		stamp.setScale(Float.valueOf(element.attributeValue("scale")));
	}
	
}
