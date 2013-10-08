package com.htong.tags.daoImpl;

import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.YKDetailModel;
import com.htong.ui.GetXpathUtil;

public class SubTagModelDaoImpl {

	public YKDetailModel getSubYKDetailModel(String parentTagXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element parentElement = (Element) document
				.selectSingleNode(parentTagXpath);
		if (parentElement != null) {
			Element ykSubTagElement = parentElement
					.element(YKDetailModel.NODE_NAME);
			if (ykSubTagElement != null) {
				return new YKDetailModel(ykSubTagElement);
			}
		}
		return null;
	}

	public void saveOrUpdate(TagModel tagModel, YKDetailModel yk) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String parentTagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element parentElement = (Element) document
				.selectSingleNode(parentTagXpath);
		if (parentElement != null) {
			Element ykSubTagElement = parentElement
					.element(YKDetailModel.NODE_NAME);
			if (ykSubTagElement == null) {// 添加
				ykSubTagElement = parentElement
						.addElement(YKDetailModel.NODE_NAME);
			}

			ykSubTagElement.addAttribute(YKDetailModel.ON_INFO_ATTR,
					yk.getOnInfo());
			ykSubTagElement.addAttribute(YKDetailModel.OFF_INFO_ATTR,
					yk.getOffInfo());
			ykSubTagElement.addAttribute(YKDetailModel.REVERSE_TAG_ATTR,
					yk.getReverseTag());
			ykSubTagElement.addAttribute(YKDetailModel.REVERSE_TIMEOUT_ATTR,
					yk.getReverseTimeout());
			ykSubTagElement.addAttribute(YKDetailModel.DISABLE_TAG_ATTR,
					yk.getDisableTag());
			ykSubTagElement.addAttribute(YKDetailModel.DISABLE_TYPE_ATTR,
					yk.getDisableType());
			ykSubTagElement.addAttribute(YKDetailModel.OUTPUT_TYPE_ATTR,
					yk.getOutputType());

			if (yk.getOnInfo() == null && yk.getOffInfo() == null
					&& yk.getReverseTag() == null
					&& yk.getReverseTimeout() == null
					&& yk.getDisableTag() == null
					&& yk.getDisableType() == null
					&& yk.getOutputType() == null) {
				parentElement.remove(ykSubTagElement);
			}

			XMLDocumentFactory.instance.saveMainDocument();
		}

	}
}
