package com.htong.tags.dao;

import java.util.List;

import org.dom4j.Element;

import com.htong.tags.model.TagTypeModel;

public interface TagTypeModelDao {
	/**
	 * 获得所有标签类型，包括子类型
	 * @return
	 */
	public List<TagTypeModel> getAllTagTypeModel();
	/**
	 * 增加元素
	 * @param oriElement 上级元素
	 * @param tagTypeModel 新增加的标签类型
	 */
	public void addElement(TagTypeModel tagTypeModel);
	
	/**
	 * 修改节点
	 * @param tagTypeModel 标签类型
	 */
	public void updateTagTypeModel(TagTypeModel tagTypeModel, String name);
	
	/**
	 * 移除节点
	 * @param tagTypeModel 标签类型
	 */
	public void removeTagTypeModel(TagTypeModel tagTypeModel);

}
