package com.htong.tags.model;

import org.dom4j.Element;

/**
 * 建筑属性
 * @author 赵磊
 *
 */
public class BuildingProp {
	public static final String NODE_NAME = "prop";
	
	public static final String NAME_ATTR = "name";
	
	private String name;
	private String text;
	
	private Element element;
	
	public BuildingProp(Element e) {
		this.element = e;
		
		this.name = e.attributeValue(NAME_ATTR);
		this.text = e.getTextTrim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}
