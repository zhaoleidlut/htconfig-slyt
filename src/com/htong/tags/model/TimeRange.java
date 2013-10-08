package com.htong.tags.model;

import org.dom4j.Element;

/**
 * 分时统计 见峰谷平
 * @author 赵磊
 *
 */
public class TimeRange {
	public static final String NODE_NAME = "timerange";
	
	public static final String NAME_ATTR = "name";
	public static final String TYPE_ATTR = "type";
	public static final String START_ATTR = "start";
	public static final String END_ATTR = "end";
	public static final String DAY_INCLUDE_ATTR = "day-include";
	public static final String DAY_EXCLUDE_ATTR = "day-exclude";
	public static final String WEEK_INCLUDE_ATTR = "week-include";
	public static final String WEEK_EXCLUDE_ATTR = "week-exclude";
	
	private Element element;
	
	private String name;
	private String type;
	private String start;
	private String end;
	private String dayInclude;
	private String dayExclude;
	private String weekInclude;
	private String weekExclude;
	
	public TimeRange(Element element) {
		this.element = element;
		
		this.type = element.attributeValue(TYPE_ATTR);
		this.name = element.attributeValue(NAME_ATTR);
		this.start = element.attributeValue(START_ATTR);
		this.end = element.attributeValue(END_ATTR);
		this.dayInclude = element.attributeValue(DAY_INCLUDE_ATTR);
		this.dayExclude = element.attributeValue(DAY_EXCLUDE_ATTR);
		this.weekInclude = element.attributeValue(WEEK_INCLUDE_ATTR);
		this.weekExclude = element.attributeValue(WEEK_EXCLUDE_ATTR);
	
	}
	
	public TimeRange() {
		
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


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
