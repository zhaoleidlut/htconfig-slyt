package com.htong.tags.model.tag;

import org.dom4j.Element;

/**
 * @author 赵磊
 * 当表达式成立时触发数据采集
 */
public class TriggerModel {
	
	public static final String NODE_NAME = "trigger";
	
	private Element element;
	
	private String expression;

	public TriggerModel(Element element) {
		this.element = element;
		expression = element.getText();
	}
	
	public TriggerModel() {
		
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	
}
