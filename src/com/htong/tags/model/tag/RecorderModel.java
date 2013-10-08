package com.htong.tags.model.tag;

import org.dom4j.Element;

import com.htong.tags.model.TagModel;

/**
 * 存储器
 * @author 赵磊
 *
 */
public class RecorderModel {
	
	public static final String NODE_NAME ="recorder";
	
	public static final String NAME_ATTR = "name";

	public static final String TYPE_ATTR = "type";
	
	public static final String STATISTIC_MODE_ATTR = "statistic-mode";
	public static final String COMPUTE_MODE_ATTR = "compute-mode";
	
	public static final String START_ATTR = "start";
	public static final String END_ATTR = "end";
	public static final String DAY_INCLUDE_ATTR = "day-include";
	public static final String DAY_EXCLUDE_ATTR = "day-exclude";
	public static final String WEEK_INCLUDE_ATTR = "week-include";
	public static final String WEEK_EXCLUDE_ATTR = "week-exclude";
	
	public static final String UPPER_ATTR = "upper";
	public static final String UPPER_INFO_ATTR = "upper-info";
	public static final String LOWER_ATTR = "lower";
	public static final String LOWER_INFO_ATTR = "lower-info";
	
	public static final String WHEN_ATTR = "when";
	public static final String ON_INFO_ATTR = "on-info";
	public static final String OFF_INFO_ATTR = "off-info";
	
	public static final String RS_ON_INFO_ATTR = "on-info";
	public static final String RS_OFF_INFO_ATTR = "off-info";
	
	public static final String INFO_ATTR = "info";
	
	public static final String RANGE_ATTR = "range";
	public static final String INTERVAL_ATTR = "interval";   //遥脉 遥测
	
	public static final String SHOW_ATTR = "show";	//推画面
	
	public static final String MAX_VALUE = "max-value";	//最大值
	public static final String MIN_VALUE = "min-value";	//最小值
	public static final String UNIT_VALUE = "unit-value";	//单位脉冲电度量
	
	private TagModel tagModel;

	private Element element;
	
	private String type;	//类型 Statistic Alarm Fault RSChange soe tag
	
	private String name;
	
	//statistic-mode:custom,normal	compute-mode:avg,sum
	private String statisticMode;
	private String computeMode;
	
	private String start;
	private String end;
	private String dayInclude;
	private String dayExclude;
	private String weekInclude;
	private String weekExclude;
	
	//upper="500" upper-info="越上限报警" lower="100" lower-info="越下限报警"
	private String upper;
	private String upperInfo;
	private String lower;
	private String lowerInfo;
	
	//when="1" on-info="离线" off-info="在线"
	private String when;
	private String onInfo;
	private String offInfo;
	
	//on-info="总故障报警" off-info="总故障解除" type="RSChange"
	private String rs_onInfo;
	private String rs_offInfo;
	
	//info="漏电报警" type="SOE"
	private String info;
	
	//range="20" interval="1" type="Tag"
	private String range;
	private String interval;
	
	private String soeJS;	//soe脚本
	
	private String ymtagExpression;
	
	private String show_alarm;	//推画面
	private String show_fault;
	private String show_rs;
	
	private String maxValue;
	private String minValue;
	private String unitValue;
	
	public RecorderModel() {
		
	}
	
	public RecorderModel(Element element) {
		this.element = element;
		this.type = element.attributeValue(TYPE_ATTR);
		this.name = element.attributeValue(NAME_ATTR);
		
		this.statisticMode = element.attributeValue(STATISTIC_MODE_ATTR);
		this.computeMode = element.attributeValue(COMPUTE_MODE_ATTR);
		
		this.start = element.attributeValue(START_ATTR);
		this.end = element.attributeValue(END_ATTR);
		this.dayInclude = element.attributeValue(DAY_INCLUDE_ATTR);
		this.dayExclude = element.attributeValue(DAY_EXCLUDE_ATTR);
		this.weekInclude = element.attributeValue(WEEK_INCLUDE_ATTR);
		this.weekExclude = element.attributeValue(WEEK_EXCLUDE_ATTR);
		
		//upper="500" upper-info="越上限报警" lower="100" lower-info="越下限报警"
		this.upper = element.attributeValue(UPPER_ATTR);
		this.upperInfo = element.attributeValue(UPPER_INFO_ATTR);
		this.lower = element.attributeValue(LOWER_ATTR);
		this.lowerInfo = element.attributeValue(LOWER_INFO_ATTR);
		
		//when="1" on-info="离线" off-info="在线"
		this.when = element.attributeValue(WHEN_ATTR);
		this.onInfo = element.attributeValue(ON_INFO_ATTR);
		this.offInfo = element.attributeValue(OFF_INFO_ATTR);
		
		//on-info="总故障报警" off-info="总故障解除" type="RSChange"
		this.rs_onInfo = element.attributeValue(RS_ON_INFO_ATTR);
		this.rs_offInfo = element.attributeValue(RS_OFF_INFO_ATTR);
		
		//info="漏电报警" type="SOE"
		this.info = element.attributeValue(INFO_ATTR);
		
		//range="20" interval="1" type="Tag"
		this.range = element.attributeValue(RANGE_ATTR);
		this.interval = element.attributeValue(INTERVAL_ATTR);
		
		this.soeJS = element.getTextTrim();
		
		this.ymtagExpression = element.getText();
		
		this.show_alarm = element.attributeValue(SHOW_ATTR);
		this.show_fault = element.attributeValue(SHOW_ATTR);
		this.show_rs = element.attributeValue(SHOW_ATTR);
		
		this.minValue = element.attributeValue(MIN_VALUE);
		this.maxValue = element.attributeValue(MAX_VALUE);
		this.unitValue = element.attributeValue(UNIT_VALUE);
				
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatisticMode() {
		return statisticMode;
	}

	public void setStatisticMode(String statisticMode) {
		this.statisticMode = statisticMode;
	}

	public String getComputeMode() {
		return computeMode;
	}

	public void setComputeMode(String computeMode) {
		this.computeMode = computeMode;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDayInclude() {
		return dayInclude;
	}

	public void setDayInclude(String dayInclude) {
		this.dayInclude = dayInclude;
	}

	public String getDayExclude() {
		return dayExclude;
	}

	public void setDayExclude(String dayExclude) {
		this.dayExclude = dayExclude;
	}

	public String getWeekInclude() {
		return weekInclude;
	}

	public void setWeekInclude(String weekInclude) {
		this.weekInclude = weekInclude;
	}

	public String getWeekExclude() {
		return weekExclude;
	}

	public void setWeekExclude(String weekExclude) {
		this.weekExclude = weekExclude;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getUpperInfo() {
		return upperInfo;
	}

	public void setUpperInfo(String upperInfo) {
		this.upperInfo = upperInfo;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

	public String getLowerInfo() {
		return lowerInfo;
	}

	public void setLowerInfo(String lowerInfo) {
		this.lowerInfo = lowerInfo;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
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

	public String getRs_onInfo() {
		return rs_onInfo;
	}

	public void setRs_onInfo(String rs_onInfo) {
		this.rs_onInfo = rs_onInfo;
	}

	public String getRs_offInfo() {
		return rs_offInfo;
	}

	public void setRs_offInfo(String rs_offInfo) {
		this.rs_offInfo = rs_offInfo;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getSoeJS() {
		return soeJS;
	}

	public void setSoeJS(String soeJS) {
		this.soeJS = soeJS;
	}

	public TagModel getTagModel() {
		return tagModel;
	}

	public void setTagModel(TagModel tagModel) {
		this.tagModel = tagModel;
	}

	public String getYmtagExpression() {
		return ymtagExpression;
	}

	public void setYmtagExpression(String ymtagExpression) {
		this.ymtagExpression = ymtagExpression;
	}

	public String getShow_alarm() {
		return show_alarm;
	}

	public void setShow_alarm(String show_alarm) {
		this.show_alarm = show_alarm;
	}

	public String getShow_fault() {
		return show_fault;
	}

	public void setShow_fault(String show_fault) {
		this.show_fault = show_fault;
	}

	public String getShow_rs() {
		return show_rs;
	}

	public void setShow_rs(String show_rs) {
		this.show_rs = show_rs;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getUnitValue() {
		return unitValue;
	}

	public void setUnitValue(String unitValue) {
		this.unitValue = unitValue;
	}

}
