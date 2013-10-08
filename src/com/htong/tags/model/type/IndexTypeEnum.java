package com.htong.tags.model.type;
/**
 * 标签索引类型
 * @author 赵磊
 *
 */
public enum IndexTypeEnum {
	MAIN("main"), ENERGY("energy"), NORMAL("normal");
	
	private String type;
	private IndexTypeEnum() {
		this(null);
	}
	private IndexTypeEnum(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public static IndexTypeEnum getByLabel(String label) {
		if(label.equals("main")) {
			return MAIN;
		} else if(label.equals("energy")) {
			return ENERGY;
		} else if(label.equals("normal")) {
			return NORMAL;
		} else {
			return null;
		}
	}

}
