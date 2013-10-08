package com.htong.tags.dao;

import com.htong.tags.model.IndexNodeModel;

public interface IndexNodeModelDao {
	/**
	 * 添加下级节点
	 * @param oriElement：原始对象(由于新建的对象无element属性，不能getParent，所以要指定上级element)
	 * @param indexNodeModel：索引节点对象
	 */
	public void addElement(IndexNodeModel indexNodeModel);
	
	/**
	 * 修改节点
	 * @param indexNodeModel
	 */
	public void updateIndexNodeModel(IndexNodeModel indexNodeModel, String oldName);
	
	/**
	 * 移除节点
	 * @param indexNodeModel
	 */
	public void removeIndexNodeModel(IndexNodeModel indexNodeModel);


}
