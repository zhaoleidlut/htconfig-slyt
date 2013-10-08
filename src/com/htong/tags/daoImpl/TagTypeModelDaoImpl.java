package com.htong.tags.daoImpl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.Visitor;
import org.dom4j.XPath;

import com.htong.tags.dao.TagTypeModelDao;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;
import com.htong.ui.MainUI;
import com.htong.ui.tree.TreeContentProvider;

public class TagTypeModelDaoImpl implements TagTypeModelDao {
	private final Logger log = Logger.getLogger(TagTypeModelDaoImpl.class);

	private Document document;

	public TagTypeModelDaoImpl() {
		document = XMLDocumentFactory.instance.getMainDocument();

	}

	@Override
	public List<TagTypeModel> getAllTagTypeModel() {
		Element element = document.getRootElement().element(
				XMLDocumentFactory.TAGTYPE_NODE);
		List<TagTypeModel> tagTypeModel = new ArrayList<TagTypeModel>();
		if (element != null) {
			for (Iterator<?> i = element
					.elementIterator(TagTypeModel.NODE_NAME); i.hasNext();) {
				Element e = (Element) i.next();
				TagTypeModel im = new TagTypeModel(e);
				im.setParentObject("标签类型配置");
				tagTypeModel.add(im);
			}
		}
		return tagTypeModel;
	}

	@Override
	public void addElement(TagTypeModel tagTypeModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = null;
		if (tagTypeModel.getParentObject() instanceof String) {
			xpath = "/" + XMLDocumentFactory.ROOT_NODE + "/"
					+ XMLDocumentFactory.TAGTYPE_NODE;
		} else if (tagTypeModel.getParentObject() instanceof TagTypeModel) {
			TagTypeModel ttm = (TagTypeModel) tagTypeModel.getParentObject();
			xpath = GetXpathUtil.getTagTypeXPathByName(ttm, ttm.getName());
		}
		log.debug("上级节点：" + xpath);
		Element oldElement = (Element) document.selectSingleNode(xpath);

		Element newElement = oldElement.addElement(TagTypeModel.NODE_NAME);
		newElement.addAttribute(TagTypeModel.NAME_ATTR, tagTypeModel.getName());
		newElement.addAttribute(TagTypeModel.DATATYPE_ATTR,
				tagTypeModel.getDataType());
		// 其他属性
		tagTypeModel.setElement(newElement); // set元素

		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void updateTagTypeModel(TagTypeModel tagTypeModel, String name) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagTypeXPathByName(tagTypeModel, name);
		log.debug(xpath);
		Element e = (Element) document.selectSingleNode(xpath);
		e.addAttribute(TagTypeModel.NAME_ATTR, tagTypeModel.getName());

		e.addAttribute(TagTypeModel.DATATYPE_ATTR, tagTypeModel.getDataType());// 数据类型为空，则移除该属性

		tagTypeModel.setElement(e);// 更新自己element

		XMLDocumentFactory.instance.saveMainDocument(document);

		// changeElement(tagTypeModel,
		// GetXpathUtil.getTagTypeXPathByName(tagTypeModel));
	}

	/**
	 * 更新子节点的对象的Element信息，以更新xpath
	 * 
	 * @param indexNodeModel
	 * @param parentXPath
	 */
	// private void changeElement(TagTypeModel tagTypeModel, String parentXPath)
	// {
	// Object o[] =
	// ((TreeContentProvider)MainUI.treeViewer.getContentProvider()).getChildren(tagTypeModel);
	// if(o != null) {
	// for(Object object : o) {
	// TagTypeModel ttm = (TagTypeModel)object;
	// String xPath = parentXPath + "/" + TagTypeModel.NODE_NAME
	// +"[@"+TagTypeModel.NAME_ATTR + "='" +ttm.getName() + "']";
	// log.debug("更新的子Element xpath:"+xPath);
	// ttm.setElement((Element)
	// XMLDocumentFactory.instance.getMainDocument().selectSingleNode(xPath));
	// changeElement(ttm, GetXpathUtil.getTagTypeXPathByName(ttm)); //递归调用
	// }
	// }
	// }

	@Override
	public void removeTagTypeModel(TagTypeModel tagTypeModel) {
		if (tagTypeModel.getParentObject() instanceof TagTypeModel) {
			Document document = XMLDocumentFactory.instance.getMainDocument();
			Element e = (Element) document.selectSingleNode(GetXpathUtil
					.getTagTypeXPathByName(tagTypeModel));
			Element parentElement = null;
			if (tagTypeModel.getParentObject() != null) {
				parentElement = (Element) document
						.selectSingleNode(GetXpathUtil
								.getTagTypeXPathByName((TagTypeModel) tagTypeModel
										.getParentObject()));
			}
			parentElement.remove(e);

			XMLDocumentFactory.instance.saveMainDocument(document);
		} else {
			Document document = XMLDocumentFactory.instance.getMainDocument();
			String xpath = "/" + XMLDocumentFactory.ROOT_NODE + "/"
					+ XMLDocumentFactory.TAGTYPE_NODE;
			Element parentElement = (Element) document.selectSingleNode(xpath);
			xpath += "/" + TagTypeModel.NODE_NAME + "[@"
					+ TagTypeModel.NAME_ATTR + "='" + tagTypeModel.getName()
					+ "']";
			Element e = (Element) document.selectSingleNode(xpath);
			parentElement.remove(e);

			XMLDocumentFactory.instance.saveMainDocument(document);
		}

	}

}
