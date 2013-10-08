package com.htong.tags.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

public class TplModel {
	private Element element;

	private List<TagModel> tags = new ArrayList<TagModel>();
	
	private String name;
	private String type;

	public TplModel(Element element) {
		this.element = element;

		name = element.attributeValue("name");
		type = element.attributeValue("type");
		
		for ( Iterator i = element.elementIterator( "tag" ); i.hasNext(); ) {
            Element tag = (Element) i.next();
            tags.add(new TagModel(tag));
        }

	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public List<TagModel> getTags() {
		return tags;
	}

	public void setTags(List<TagModel> tags) {
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
