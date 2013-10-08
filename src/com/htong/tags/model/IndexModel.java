package com.htong.tags.model;

import org.dom4j.Element;
/**
 * 索引模型
 * @author 赵磊
 *
 */
public class IndexModel {
	public static final String NODE_NAME = "index";		//节点名字
	
	public static final String NAME_ATTR = "name"; 	//名字属性
	public static final String TYPE_ATTR = "type";	//索引类型
	
	private String name;
	private String type;
	private Element element;
	
	public IndexModel(Element element) {
		this.element = element;
		
		this.name = element.attributeValue(IndexModel.NAME_ATTR);
		this.type = element.attributeValue(IndexModel.TYPE_ATTR);
	}
	
	public IndexModel() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
