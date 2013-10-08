package com.htong.tags.dao;

import com.htong.tags.model.TagModel;
import com.htong.tags.model.tag.GeneratorModel;

/**
 * 生成器
 * @author 赵磊
 *
 */
public interface GeneratorModelDao {
	/**
	 * 更新生成器
	 * @param generatorModel
	 */
	public void updateGenerator(GeneratorModel generatorModel, String parentXpath);
	/**
	 * 删除生成器
	 * @param generatorModel
	 */
	public void removeGeverator(GeneratorModel generatorModel, String parentXpath);
	/**
	 * 增加生成器
	 * @param generatorModel
	 */
	public void addGenerator(GeneratorModel generatorModel, String parentXpath);
	/**
	 * 查找生成器
	 * @param parentXpath 上级xpath
	 * @return
	 */
	public GeneratorModel getGeneratorModel(String parentXpath);

}
