package com.htong.tags.daoImpl;

import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.dao.TriggerModelDao;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.TriggerModel;
import com.htong.ui.GetXpathUtil;

public class TriggerModelDaoImpl implements TriggerModelDao {

	@Override
	public void addTriggerModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element element = (Element) document.selectSingleNode(xpath);
		Element addedElement = element.addElement(TriggerModel.NODE_NAME);
		
		if(tagModel.getTrigger().getExpression()!=null) {
			addedElement.addCDATA(tagModel.getTrigger().getExpression());									
		}
		
		tagModel.getTrigger().setElement(addedElement);
		
		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void removeTriggerModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element element = (Element) document.selectSingleNode(xpath);
		element.remove(element.element(TriggerModel.NODE_NAME));
		
		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void updateTriggerModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element element = (Element) document.selectSingleNode(xpath);
		if(element.element(TriggerModel.NODE_NAME) == null) {
			return;
		} else {
			Element triggerElement = element.element(TriggerModel.NODE_NAME);
			if(tagModel.getTrigger().getExpression()!=null) {
				triggerElement.clearContent();
				triggerElement.addCDATA(tagModel.getTrigger().getExpression());									
			}
			
			tagModel.getTrigger().setElement(triggerElement);//更新element
			XMLDocumentFactory.instance.saveMainDocument(document);
		}

	}

	@Override
	public TriggerModel getTriggerModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element element = (Element) document.selectSingleNode(xpath);
		if(element.element(TriggerModel.NODE_NAME) == null) {
			return null;
		} else {
			Element triggerElement = element.element(TriggerModel.NODE_NAME);
			TriggerModel trigger = new TriggerModel(triggerElement);
			return trigger;
		}
		
	}

}
