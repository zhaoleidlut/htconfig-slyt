package com.htong.tags.dao;

import com.htong.tags.model.TagModel;
import com.htong.tags.model.tag.TriggerModel;

/**
 * 触发器
 * @author 赵磊
 *
 */
public interface TriggerModelDao {
	/**
	 * 添加触发器
	 * @param triggerModel
	 */
	public void addTriggerModel(TagModel tagModel);
	/**
	 * 删除触发器
	 * @param triggerModel
	 */
	public void removeTriggerModel(TagModel tagModel);
	/**
	 * 更新触发器
	 * @param triggerModel
	 */
	public void updateTriggerModel(TagModel tagModel);
	
	/**
	 * 查找触发器
	 * @param tagModel
	 */
	public TriggerModel getTriggerModel(TagModel tagModel);

}
