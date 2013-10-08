package com.htong.tags.model.enums;
/**
 * 峰尖谷平
 * @author 赵磊
 *
 */
public enum TimeRangeType {
	F("峰"),J("尖"),G("谷"),P("平");
	public static String[] typeValues = {"峰","尖","谷","平"};
	private String type;
	private TimeRangeType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
	
	public static TimeRangeType getByLabel(String type) {
		if(type.equals("峰")) {
			return F;
		} else if(type.equals("尖")) {
			return J;
		} else if(type.equals("谷")) {
			return G;
		} else if(type.equals("平")) {
			return P;
		}
		return null;
	}
	

}
