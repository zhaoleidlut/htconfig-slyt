package com.htong.tags.dao;

import java.util.List;

import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.tag.RecorderModel;

/**
 * 变量标签模型
 * @author 赵磊
 *
 */
public interface TagModelDao {
	/**
	 * 判断是不是主索引
	 * @param indexNodeModel
	 * @return
	 */
	public boolean isMainIndex(IndexNodeModel indexNodeModel);
	
	public TagModel getTagByXpath(String xpath);
	/**
	 * 添加标签
	 * @param tagModel
	 */
	public void addVariable(TagModel tagModel);
	
	/**
	 * 通过主索引查询变量
	 * @param mainIndex
	 */
	public List<TagModel> getTagsByMainIndex(String mainIndex);
	/**
	 * 通过分项索引查询变量
	 * @param secondIndex
	 */
	public List<TagModel> getTagsBySecondIndex(String secondIndex);
	
	/**
	 * 删除标签
	 * @param tagModel
	 */
	public void removeTagModel(TagModel tagModel);
	/**
	 * 更新标签
	 * @param tagModel
	 */
	public void updateTagModel(TagModel tagModel, String oldname);
	
	/**
	 * 获得子变量
	 * @param parentTagXpath
	 */
	public List<TagModel> getSubTagModel(String parentTagXpath);
	/**
	 * 添加子变量
	 * @param parentTagXpath
	 * @param tagModel
	 */
	public void addSubTagModel(String parentTagXpath, TagModel tagModel);
	/**
	 * 更新变量
	 * @param parentTagXpath
	 * @param tagModel
	 * @param oldName
	 */
	public void updateSubTagModel(String parentTagXpath, TagModel tagModel, String oldName);
	/**
	 * 删除子变量
	 * @param parentTagXpath
	 * @param tagModel
	 */
	public void removeSubTagModel(String parentTagXpath, TagModel tagModel);



}
