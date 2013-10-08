package com.htong.tags.comm;

/**
 * 值类型
 * 
 * @author 赵磊
 * 
 */
public enum ModbusValueTypeEnum {
	bin("字节数组"),
	bool("布尔型"),
	int16("有符号整型16位"),
	uint16("无符号整型16位"),
	int32("有符号整型32位"),
	uint32("无符号整型32位"),
	FLOAT("浮点型32位"),
	DOUBLE("双浮点型64位"),
	byteh("高字节8位"),
	bytel("低字节8位"),
	MOD10000("MOD10000"),
	MOD10000_1("MOD10000_1"),
	bcd("BCD"),
	ascii("ASCII"),
	ascii2bcd("ASCII转BCD"),
	datetime("datetime");
	
	public static ModbusValueTypeEnum getByLabel(String valueType) {
		if(valueType.equalsIgnoreCase("bin")) {
			return bin;
		} else if(valueType.equalsIgnoreCase("bool")) {
			return bool;
		} else if(valueType.equalsIgnoreCase("int16")) {
			return int16;
		} else if(valueType.equalsIgnoreCase("uint16")) {
			return uint16;
		} else if(valueType.equalsIgnoreCase("int32")) {
			return int32;
		} else if(valueType.equalsIgnoreCase("uint32")) {
			return uint32;
		} else if(valueType.equalsIgnoreCase("float")) {
			return FLOAT;
		} else if(valueType.equalsIgnoreCase("double")) {
			return DOUBLE;
		} else if(valueType.equalsIgnoreCase("byteh")) {
			return byteh;
		} else if(valueType.equalsIgnoreCase("bytel")) {
			return bytel;
		} else if(valueType.equalsIgnoreCase("mod10000")) {
			return MOD10000;
		} else if(valueType.equalsIgnoreCase("mod10000_1")) {
			return MOD10000_1;
		} else if(valueType.equalsIgnoreCase("bcd")) {
			return bcd;
		} else if(valueType.equalsIgnoreCase("ascii")) {
			return ascii;
		} else if(valueType.equalsIgnoreCase("ascii2bcd")) {
			return ascii2bcd;
		} else if(valueType.equalsIgnoreCase("datetime")) {
			return datetime;
		} else
			return null;
		
	}

	private String value;

	private ModbusValueTypeEnum() {
		this(null);
	}

	private ModbusValueTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
