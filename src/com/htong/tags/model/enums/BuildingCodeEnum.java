package com.htong.tags.model.enums;

/**
 * 建筑类别编码
 * 
 * @author 赵磊
 * 
 */
public enum BuildingCodeEnum {
	A("办公建筑"), B("商场建筑"), C("宾馆饭店建筑"), D("文化教育建筑"), E("医疗卫生建筑"), F("体育建筑"), G(
			"综合建筑"), H("其它建筑");
	private String buildingCode;

	private BuildingCodeEnum() {
	}
	private BuildingCodeEnum(String buildingCode) {
		this.buildingCode = buildingCode;
	}

	public String getValue() {
		return buildingCode;
	}

	public static BuildingCodeEnum getByLabel(String buildingName) {
		if (buildingName.equals("办公建筑")) {
			return A;
		} else if (buildingName.equals("商场建筑")) {
			return B;
		} else if (buildingName.equals("宾馆饭店建筑")) {
			return C;
		} else if (buildingName.equals("文化教育建筑")) {
			return D;
		} else if (buildingName.equals("医疗卫生建筑")) {
			return E;
		} else if (buildingName.equals("体育建筑")) {
			return F;
		} else if (buildingName.equals("综合建筑")) {
			return G;
		} else if (buildingName.equals("其它建筑")) {
			return H;
		} else {
			return null;
		}
	}


}
