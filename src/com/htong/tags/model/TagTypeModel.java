package com.htong.tags.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
/**
 * 标签类型
 * @author 赵磊
 *
 */
public class TagTypeModel {
	public static final String NODE_NAME = "type";	//节点名字
	
	public static final String NAME_ATTR = "name";		//名字属性
	public static final String DATATYPE_ATTR = "data-type";	//数据类型属性
	
	protected String name;	//名字
	private String dataType;	//数据类型
	protected Element element;
	
	private Object parentObject;
	
	public TagTypeModel(Element element) {
		this.element = element;
		this.name = element.attributeValue(TagTypeModel.NAME_ATTR);
		if(element.attributeValue(TagTypeModel.DATATYPE_ATTR) != null) {
			this.dataType = element.attributeValue(TagTypeModel.DATATYPE_ATTR);
		}
	}
	
	public TagTypeModel() {
		
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

	public Object getParentObject() {
		return parentObject;
	}

	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	
}
