package com.htong.tags.model.tag;

import org.dom4j.Element;
/**
 * 生成器
 * @author 赵磊
 *
 */
public class GeneratorModel {
	public static final String NODE_NAME = "generator";
	public static final String REFER_NAME = "refer";

	private Element element;
	
	private String refer;
	
	private String expression;

	public GeneratorModel(Element element) {
		this.element = element;
		this.expression = element.getText();
		this.refer = element.attributeValue(REFER_NAME);
	}
	
	public GeneratorModel() {
		
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

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}
	
	

}
