package com.htong.tags.daoImpl;

import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.dao.GeneratorModelDao;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.GeneratorModel;

public class GeneratorModelDaoImpl implements GeneratorModelDao {

	@Override
	public void updateGenerator(GeneratorModel generatorModel,
			String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element element = (Element) document.selectSingleNode(parentXpath);
		
		Element generatorElement = element.element(GeneratorModel.NODE_NAME);
		
		generatorElement.clearContent();
		if(generatorModel.getExpression()!=null) {
			
			generatorElement.addCDATA(generatorModel.getExpression());									
		}
		generatorElement.addAttribute(GeneratorModel.REFER_NAME, generatorModel.getRefer());
		
		generatorModel.setElement(generatorElement);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
	}

	@Override
	public void removeGeverator(GeneratorModel generatorModel,
			String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element element = (Element) document.selectSingleNode(parentXpath);
		
		Element generatorElement = element.element(GeneratorModel.NODE_NAME);
		element.remove(generatorElement);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
	}

	@Override
	public void addGenerator(GeneratorModel generatorModel, String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element element = (Element) document.selectSingleNode(parentXpath);
		
		Element generatorElement = element.addElement(GeneratorModel.NODE_NAME);
		
		generatorElement.addAttribute(GeneratorModel.REFER_NAME, generatorModel.getRefer());
		
		generatorElement.addCDATA(generatorModel.getExpression());
		
		generatorModel.setElement(generatorElement);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
	}

	@Override
	public GeneratorModel getGeneratorModel(String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element element = (Element) document.selectSingleNode(parentXpath);
		
		Element generatorElement = element.element(GeneratorModel.NODE_NAME);
		if(generatorElement == null) {
			return null;
		} else {
			GeneratorModel gm = new GeneratorModel(generatorElement);
			return gm;
		}
		
	}



}
