package com.htong.tags.dao;

import java.util.List;

import com.htong.tags.model.TagModel;
import com.htong.tags.model.tag.RecorderModel;

/**
 * 
 * @author 赵磊
 *
 */
public interface RecorderModelDao {
	/**
	 * 添加存储器
	 * @param tagModel
	 */
	public void addRecorder(RecorderModel recorder, String parentXpath);
	/**
	 * 更新存储器
	 * @param recorder
	 */
	public void updateRecorder(RecorderModel recorder, String oldName, String parentXpath);
	/**
	 * 删除存储器
	 * @param recorder
	 */
	public void removeRecorder(RecorderModel recorder, String parentXpath);
	/**
	 * 获得标签存储器
	 * @param tagModel
	 * @return
	 */
	public List<RecorderModel> getRecorderModels(TagModel tagModel, String parentXpath);

}
