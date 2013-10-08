package com.htong.ui;

import java.util.List;

import org.apache.log4j.Logger;

import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.model.TagModel;

public enum GetVarNum {
	instanse;
	private boolean hasInit = false;
	private static int maxYC = 0;
	private int maxYX = 0;
	private int maxYT = 0;
	private int maxYK = 0;
	private int maxYM = 0;
	private int maxOther = 0;

	private static final Logger log = Logger.getLogger(GetVarNum.class);

	public String getVarNum(String varTypa) {
		if (!hasInit) {
			TagModelDaoImpl tagDao = new TagModelDaoImpl();

			List<TagModel> ycTagList = tagDao.getAllVarByType("遥测");
			List<TagModel> yxTagList = tagDao.getAllVarByType("遥信");
			List<TagModel> ytTagList = tagDao.getAllVarByType("遥调");
			List<TagModel> ykTagList = tagDao.getAllVarByType("遥控");
			List<TagModel> ymTagList = tagDao.getAllVarByType("遥脉");
			List<TagModel> otherTagList = tagDao.getAllVarByType("其他");
			maxYC = getMaxNum(ycTagList);
			maxYX = getMaxNum(yxTagList);
			maxYT = getMaxNum(ytTagList);
			maxYK = getMaxNum(ykTagList);
			maxYM = getMaxNum(ymTagList);
			maxOther = getMaxNum(otherTagList);

			hasInit = true;
		}

		int maxNum = 0;
		if (varTypa.substring(0, 2).equals("遥测")) {
			maxNum = maxYC;
			maxYC++;
		} else if (varTypa.substring(0, 2).equals("遥信")) {
			maxNum = maxYX;
			maxYX++;
		} else if (varTypa.substring(0, 2).equals("遥控")) {
			maxNum = maxYK;
			maxYK++;
		} else if (varTypa.substring(0, 2).equals("遥调")) {
			maxNum = maxYT;
			maxYT++;
		} else if (varTypa.substring(0, 2).equals("遥脉")) {
			maxNum = maxYM;
			maxYM++;
		} else {
			maxNum = maxOther;
			maxOther++;
		}

		String base = "";
		base = String.valueOf(maxNum + 1);
		int y = base.length();
		// 补足4位
		for (int oo = 0; oo < 6 - y; oo++) {
			base = "0" + base;
		}
		//log.debug(base);
		return base;
	}

	private int getMaxNum(List<TagModel> tagList) {
		if (tagList == null || tagList.isEmpty()) {
			log.debug("该类型变量没有，返回0001");
			return 0;
		} else {
			log.debug("计算num,变量个数：" + tagList.size());
			int[] nums = new int[tagList.size()];// 序号数组

			for (int i = 0; i < tagList.size(); i++) {
				try {
					nums[i] = Integer
							.valueOf(tagList
									.get(i)
									.getVarName()
									.substring(
											tagList.get(i).getVarName()
													.length() - 6));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			// 存在变量大于1
			// 从小到大排序
			for (int j = 0; j < nums.length; j++) {
				for (int k = nums.length - 2; k >= j; k--) {
					int temp;
					if (nums[k] > nums[k + 1]) {
						temp = nums[k];
						nums[k] = nums[k + 1];
						nums[k + 1] = temp;
					}
				}
			}

			return nums[nums.length - 1];
		}
	}

	public static void main(String args[]) {
		System.out.println(Integer.valueOf("0101"));
	}

}
