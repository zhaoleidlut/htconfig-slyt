package com.htong.tags.model.tag;

import org.dom4j.Element;

public class IOInfoModel {
	public static final String NODE_NAME = "tagioinfo";
	
	public static final String CHANNEL_ID_ATTR = "channel-id";
	public static final String TRANS_CHANNEL_ID_ATTR = "trans-channel-id";
	public static final String DEVICE_ID_ATTR = "device-id";
	public static final String FUNCTION_CODE_ATTR = "fun-code";
	public static final String REG_ADDRESS_ATTR = "reg-addr";	//
	public static final String BYTE_LENGTH_ATTR = "bytelen";
	public static final String OFFSET_ATTR = "offset";
	public static final String VALUE_TYPE_ATTR = "value-type";
	public static final String BASE_VALUE_ATTR = "base-value";
	public static final String COEF_ATTR = "coef";
	public static final String PRIORITY_ATTR = "priority";
	public static final String PUSH_DATA_ATTR = "push-data";
	
	private Element element;
	
	private String channelId;
	private String transChannelId;
	private String deviceId;
	private String funCode;
	private String regAddress;
	private String byteLen;
	private String offset;
	private String valueType;
	private String baseValue;//基数
	private String coef;	//系数
	private String priority;	//优先级
	private String pushData;	//主动上传
	
	public IOInfoModel(Element element) {
		this.element = element;
		this.channelId = element.attributeValue(CHANNEL_ID_ATTR);
		this.transChannelId = element.attributeValue(TRANS_CHANNEL_ID_ATTR);
		this.deviceId = element.attributeValue(DEVICE_ID_ATTR);
		this.funCode = element.attributeValue(FUNCTION_CODE_ATTR);
		this.regAddress = element.attributeValue(REG_ADDRESS_ATTR);
		this.byteLen = element.attributeValue(BYTE_LENGTH_ATTR);
		this.offset = element.attributeValue(OFFSET_ATTR);
		this.valueType = element.attributeValue(VALUE_TYPE_ATTR);
		this.baseValue = element.attributeValue(BASE_VALUE_ATTR);
		this.coef = element.attributeValue(COEF_ATTR);
		this.priority = element.attributeValue(PRIORITY_ATTR);
		this.pushData = element.attributeValue(PUSH_DATA_ATTR);
		// TODO: 获取属性
	}
	public IOInfoModel() {
		
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTransChannelId() {
		return transChannelId;
	}

	public void setTransChannelId(String transChannelId) {
		this.transChannelId = transChannelId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getFunCode() {
		return funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}

	public String getByteLen() {
		return byteLen;
	}

	public void setByteLen(String byteLen) {
		this.byteLen = byteLen;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(String baseValue) {
		this.baseValue = baseValue;
	}

	public String getCoef() {
		return coef;
	}

	public void setCoef(String coef) {
		this.coef = coef;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getPushData() {
		return pushData;
	}
	public void setPushData(String pushData) {
		this.pushData = pushData;
	}


}
