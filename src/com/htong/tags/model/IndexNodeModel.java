package com.htong.tags.model;

import org.dom4j.Element;

/**
 * 标签索引节点模型
 * @author 赵磊
 *
 */
public class IndexNodeModel {
	public static final String NODE_NAME = "indexnode";	//节点名字
	
	public static final String NAME_ATTR = "name";	//名字属性
//	public static final String USED_ATTR = "used";	//是否使用
	public static final String TYPE_ATTR = "type";	//编码属性
	public static final String AREA_ATTR = "area";	//面积属性
	public static final String CODE_ATTR = "code";	//识别码
	public static final String AIR_AREA_ATTR = "air-condition-area";
	public static final String HEATING_AREA_ATTR = "heating-area";
	public static final String NUMBER_ATTR = "number";	//索引编号
	public static final String PEOPLE_NUM_ATTR = "people-num";
	public static final String TRANSFORMER_TYPE_ATTR = "type";	//类型
	public static final String TRANSFORMER_NAME_ATTR = "transformer"; 	//变压器名字
	public static final String END_NAME_ATTR = "end-name";	//末端名字
	
	public static final String ENERGY_INDEX_ATTR = "energy-index";
	public static final String NORMAL_INDEX_ATTR = "normal-index";
	
	public static final String CHANNEL_ID_ATTR = "channel-id";
	public static final String DEVICE_ID_ATTR = "device-id";
	
	private Element element;
	private String name;
	private String type;
	private String area;
	private String code;
	
	private String number;	//序号
	
	private String air_condition_area;	//空调面积
	private String heating_area;	//采暖面积
	
	private String people_num;	//人数
	
	private Object parentObject;	//上级对象，用于树的相关操作   （新建时用）
	
	private String transformer_type;	//变压器类型  高压侧 低压侧  power-high  power-low
	private String transformer_name;
	
	private String endName;	//末端名字
	
	private String normalIndex;	//常规分类索引
	private String energyIndex;	//能耗分项索引
	
	private String channelId;	//批量通道ID
	private String deviceId;	//批量设备ID
	
	public IndexNodeModel(Element e) {
		this.element = e;
		this.name = e.attributeValue(IndexNodeModel.NAME_ATTR);
		this.type = e.attributeValue(IndexNodeModel.TYPE_ATTR);
		this.area = e.attributeValue(IndexNodeModel.AREA_ATTR);
		this.code = e.attributeValue(IndexNodeModel.CODE_ATTR);
		
		this.air_condition_area = e.attributeValue(AIR_AREA_ATTR);
		this.heating_area = e.attributeValue(HEATING_AREA_ATTR);
		
		this.number = e.attributeValue(NUMBER_ATTR);
		
		this.people_num = e.attributeValue(PEOPLE_NUM_ATTR);
		
		this.transformer_name = e.attributeValue(TRANSFORMER_NAME_ATTR);
		this.transformer_type = e.attributeValue(TRANSFORMER_TYPE_ATTR);
		
		this.endName = e.attributeValue(END_NAME_ATTR);
		
		this.normalIndex = e.attributeValue(NORMAL_INDEX_ATTR);
		this.energyIndex = e.attributeValue(ENERGY_INDEX_ATTR);
		
		this.channelId = e.attributeValue(CHANNEL_ID_ATTR);
		this.deviceId = e.attributeValue(DEVICE_ID_ATTR);

	}
	
	public IndexNodeModel() {
		
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getParentObject() {
		return parentObject;
	}

	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPeople_num() {
		return people_num;
	}

	public void setPeople_num(String people_num) {
		this.people_num = people_num;
	}

	public String getAir_condition_area() {
		return air_condition_area;
	}

	public void setAir_condition_area(String air_condition_area) {
		this.air_condition_area = air_condition_area;
	}

	public String getHeating_area() {
		return heating_area;
	}

	public void setHeating_area(String heating_area) {
		this.heating_area = heating_area;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTransformer_type() {
		return transformer_type;
	}

	public void setTransformer_type(String transformer_type) {
		this.transformer_type = transformer_type;
	}

	public String getTransformer_name() {
		return transformer_name;
	}

	public void setTransformer_name(String transformer_name) {
		this.transformer_name = transformer_name;
	}

	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public String getNormalIndex() {
		return normalIndex;
	}

	public void setNormalIndex(String normalIndex) {
		this.normalIndex = normalIndex;
	}

	public String getEnergyIndex() {
		return energyIndex;
	}

	public void setEnergyIndex(String energyIndex) {
		this.energyIndex = energyIndex;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	

}
