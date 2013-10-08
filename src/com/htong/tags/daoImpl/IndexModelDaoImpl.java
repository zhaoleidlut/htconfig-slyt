package com.htong.tags.daoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.dao.IndexModelDao;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.type.IndexTypeEnum;
import com.htong.ui.GetXpathUtil;
import com.htong.ui.MainUI;
import com.htong.ui.tree.TreeContentProvider;

public class IndexModelDaoImpl implements IndexModelDao {
	private final Logger log = Logger.getLogger(IndexModelDaoImpl.class);

	@Override
	public List<IndexModel> getAllIndexModel() {
		Element element = XMLDocumentFactory.instance.getMainDocument()
				.getRootElement().element(XMLDocumentFactory.TAGINDEX_NODE);
		List<IndexModel> indexModelList = new ArrayList<IndexModel>();
		if (element != null) {
			for (Iterator<?> i = element.elementIterator(IndexModel.NODE_NAME); i
					.hasNext();) {
				Element e = (Element) i.next();
				IndexModel im = new IndexModel(e);
				indexModelList.add(im);
			}
		}
		return indexModelList;
	}

	@Override
	public void addIndexModel(IndexModel indexModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element e = document.getRootElement().element(
				XMLDocumentFactory.TAGINDEX_NODE);
		Element addedElement = e.addElement(IndexModel.NODE_NAME);
		addedElement.addAttribute(IndexModel.NAME_ATTR, indexModel.getName()); // 名字属性
		addedElement.addAttribute(IndexModel.TYPE_ATTR, indexModel.getType()); // 索引类型
		

		indexModel.setElement(addedElement); // 设置对象的元素属性

		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void updateIndexModel(IndexModel indexModel, String oldName) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String path = GetXpathUtil.getIndexXPathByName(indexModel, oldName);
		Element e = (Element) document.selectSingleNode(path);
		e.addAttribute(IndexModel.NAME_ATTR, indexModel.getName());
		e.addAttribute(IndexModel.TYPE_ATTR, indexModel.getType());
		// 其他属性

		indexModel.setElement(e);

		XMLDocumentFactory.instance.saveMainDocument(document);

		Object o[] = ((TreeContentProvider) MainUI.treeViewer
				.getContentProvider()).getChildren(indexModel);
//		changeElement(o, GetXpathUtil.getIndexXPathByName(indexModel));

	}

	/**
	 * 更新子节点的对象的Element信息，以更新xpath
	 * 
	 * @param o
	 * @param parentXPath
	 */
//	private void changeElement(Object o[], String parentXPath) {
//		if (o != null) {
//			for (Object object : o) {
//				IndexNodeModel inm = (IndexNodeModel) object;
//				String xPath = parentXPath + "/" + IndexNodeModel.NODE_NAME
//						+ "[@" + IndexNodeModel.NAME_ATTR + "='"
//						+ inm.getName() + "']";
//				log.debug("更新的子Element xpath:" + xPath);
//				inm.setElement((Element) XMLDocumentFactory.instance
//						.getMainDocument().selectSingleNode(xPath));
//				Object myObject[] = ((TreeContentProvider) MainUI.treeViewer
//						.getContentProvider()).getChildren(inm);
//				changeElement(myObject,
//						GetXpathUtil.getIndexNodeXPathByName(inm)); // 递归调用
//			}
//		}
//	}

	@Override
	public void removeIndexModel(IndexModel indexModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element tagindexElement = document.getRootElement().element(
				XMLDocumentFactory.TAGINDEX_NODE);
		for (Iterator<?> i = tagindexElement.elementIterator(IndexModel.NODE_NAME); i
				.hasNext();) {
			Element e = (Element) i.next();
			if (e.attributeValue(IndexModel.NAME_ATTR).equals(
					indexModel.getName())) {
				tagindexElement.remove(e);
				break;
			}
		}

		XMLDocumentFactory.instance.saveMainDocument(document);
	}

	@Override
	public IndexModel getMainIndexModel() {
		Element element = XMLDocumentFactory.instance.getMainDocument()
				.getRootElement().element(XMLDocumentFactory.TAGINDEX_NODE);
		if (element != null) {
			for (Iterator<?> i = element.elementIterator(IndexModel.NODE_NAME); i
					.hasNext();) {
				Element e = (Element) i.next();
				if (e.attributeValue(IndexModel.TYPE_ATTR) != null
						&& IndexTypeEnum.MAIN.getType()
								.equals(e.attributeValue(IndexModel.TYPE_ATTR))) {
					return new IndexModel(e);
				}
			}
		}
		return null;
	}

	@Override
	public IndexModel getNormalIndexModel() {
		Element element = XMLDocumentFactory.instance.getMainDocument()
				.getRootElement().element(XMLDocumentFactory.TAGINDEX_NODE);
		if (element != null) {
			for (Iterator<?> i = element.elementIterator(IndexModel.NODE_NAME); i
					.hasNext();) {
				Element e = (Element) i.next();
				if (e.attributeValue(IndexModel.TYPE_ATTR) != null
						&& IndexTypeEnum.NORMAL.getType()
								.equals(e.attributeValue(IndexModel.TYPE_ATTR))) {
					return new IndexModel(e);
				}
			}
		}
		return null;
	}

	@Override
	public IndexModel getEnergyIndexModel() {
		Element element = XMLDocumentFactory.instance.getMainDocument()
				.getRootElement().element(XMLDocumentFactory.TAGINDEX_NODE);
		if (element != null) {
			for (Iterator<?> i = element.elementIterator(IndexModel.NODE_NAME); i
					.hasNext();) {
				Element e = (Element) i.next();
				if (e.attributeValue(IndexModel.TYPE_ATTR) != null
						&& IndexTypeEnum.ENERGY.getType()
								.equals(e.attributeValue(IndexModel.TYPE_ATTR))) {
					return new IndexModel(e);
				}
			}
		}
		return null;
	}

}
