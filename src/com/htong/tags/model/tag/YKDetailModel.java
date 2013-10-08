package com.htong.tags.model.tag;

import org.dom4j.Element;

public class YKDetailModel {
	public static final String NODE_NAME = "tag-ext";
	
	public static final String REVERSE_TAG_ATTR = "reverse-tag";
	public static final String REVERSE_TIMEOUT_ATTR = "reverse-timeout";
	public static final String DISABLE_TAG_ATTR = "disable-tag";
	public static final String DISABLE_TYPE_ATTR = "disable-type";
	public static final String ON_INFO_ATTR = "on-info";
	public static final String OFF_INFO_ATTR = "off-info";
	public static final String OUTPUT_TYPE_ATTR = "output-type";
	
	private Element element;
	
	private String reverseTag;
	private String reverseTimeout;
	private String disableTag;
	private String disableType;
	private String onInfo;
	private String offInfo;
	private String outputType;
	
	public YKDetailModel(Element element) {
		this.element = element;
		this.reverseTag = element.attributeValue(REVERSE_TAG_ATTR);
		this.reverseTimeout = element.attributeValue(REVERSE_TIMEOUT_ATTR);
		this.disableTag = element.attributeValue(DISABLE_TAG_ATTR);
		this.disableType = element.attributeValue(DISABLE_TYPE_ATTR);
		this.onInfo = element.attributeValue(ON_INFO_ATTR);
		this.offInfo = element.attributeValue(OFF_INFO_ATTR);
		this.outputType = element.attributeValue(OUTPUT_TYPE_ATTR);
	}
	public YKDetailModel() {
		
	}
	
	
	
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public String getReverseTag() {
		return reverseTag;
	}
	public void setReverseTag(String reverseTag) {
		this.reverseTag = reverseTag;
	}
	public String getReverseTimeout() {
		return reverseTimeout;
	}
	public void setReverseTimeout(String reverseTimeout) {
		this.reverseTimeout = reverseTimeout;
	}
	public String getDisableTag() {
		return disableTag;
	}
	public void setDisableTag(String disableTag) {
		this.disableTag = disableTag;
	}
	public String getDisableType() {
		return disableType;
	}
	public void setDisableType(String disableType) {
		this.disableType = disableType;
	}
	public String getOnInfo() {
		return onInfo;
	}
	public void setOnInfo(String onInfo) {
		this.onInfo = onInfo;
	}
	public String getOffInfo() {
		return offInfo;
	}
	public void setOffInfo(String offInfo) {
		this.offInfo = offInfo;
	}
	public String getOutputType() {
		return outputType;
	}
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	
	

}
