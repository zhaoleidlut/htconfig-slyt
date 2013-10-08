package com.htong.tags.comm;
/**
 * 数据类型
 * @author 赵磊
 *
 */
public enum DataTypeEnum {
	bool("布尔型"),string("字符型"),number("数值型"),bin("字符数组");
	private String value;
	private DataTypeEnum() {
		this(null);
	}
	private DataTypeEnum(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

}
