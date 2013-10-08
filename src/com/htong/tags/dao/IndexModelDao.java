package com.htong.tags.dao;

import java.io.IOException;
import java.util.List;

import com.htong.tags.model.IndexModel;

public interface IndexModelDao {
	/**
	 * 获得所有索引模型
	 * @return
	 */
	public List<IndexModel> getAllIndexModel();
	
	/**
	 * 添加索引
	 * @throws IOException 
	 */
	public void addIndexModel(IndexModel indexModel);
	/**
	 * 保存索引
	 * @param indexModel 修改后的属性
	 */
	public void updateIndexModel(IndexModel indexModel, String oldName);
	/**
	 * 移除该对象
	 * @param indexModel
	 */
	public void removeIndexModel(IndexModel indexModel);
	
	
	/**
	 * 获得主索引
	 * @return
	 */
	public IndexModel getMainIndexModel();
	/**
	 * 获得常规分类索引
	 * @return
	 */
	public IndexModel getNormalIndexModel();
	/**
	 * 获得能耗分类分项索引
	 * @return
	 */
	public IndexModel getEnergyIndexModel();
	


}
