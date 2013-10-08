package com.htong.ui;

import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.type.IndexTypeEnum;

public class GetIndexType {
	/**
	 * 获得主索引类型
	 * @param indexNodeModel
	 * @return
	 */
	public static IndexTypeEnum getIndexType(IndexNodeModel indexNodeModel) {
		Object parentObject = indexNodeModel.getParentObject();
		while (!(parentObject instanceof IndexModel)) {
			parentObject = ((IndexNodeModel) parentObject)
					.getParentObject();
		}
		IndexModel im = (IndexModel) parentObject;
		IndexTypeEnum indexType = IndexTypeEnum
				.getByLabel(im.getType());
		return indexType;
		
	}

}
