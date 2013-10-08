package com.htong.ui;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;

public class ImportOtherFile {
	private static final Logger log = Logger.getLogger(ImportOtherFile.class);

	/**
	 * 为主索引导入标签
	 * @param indexNodeModel
	 * @param treeViewer
	 */
	public static void importLabelForMainIndexNode(IndexNodeModel indexNodeModel,
			TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
			//MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) indexNodeModel
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					IndexNodeModel inm = new IndexNodeModel(myElement);
					inm.setParentObject(indexNodeModel);

					myElement.setParent(null);
					indexNodeModel.getElement().add(myElement);
					myElement.setParent(indexNodeModel.getElement());

					List<Element> tagsElementList = myElement
							.selectNodes(".//tags");
					if (tagsElementList != null) {
						log.debug(tagsElementList.size());
						for (Element tagsElement : tagsElementList) {
							List<Element> ielementList = tagsElement
									.elements(TagModel.NODE_NAME);
							// 更新变量名
							log.debug(ielementList.size());
							for (Element ele : ielementList) {
								String varType = ele.attributeValue("type");
								String varName = ele
										.attributeValue(TagModel.VAR_NAME_ATTR);
								String oldName = varName.substring(0,
										varName.length() - 6);
								String newNum = GetVarNum.instanse
										.getVarNum(varType);
								// String newNum = "0000";
								String newName = oldName + newNum;
								ele.addAttribute(TagModel.VAR_NAME_ATTR,
										newName);
							}
						}
					}
					treeViewer.add(indexNodeModel, inm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}

	/**
	 * 为主索引子标签导入标签
	 * @param indexModel
	 * @param treeViewer
	 */
	public static void importLabelForMainIndex(IndexModel indexModel,
			TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
			//MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getIndexXPathByName(indexModel);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) indexModel
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					IndexNodeModel inm = new IndexNodeModel(myElement);
					inm.setParentObject(indexModel);

					myElement.setParent(null);
					indexModel.getElement().add(myElement);
					myElement.setParent(indexModel.getElement());

					List<Element> tagsElementList = myElement
							.selectNodes(".//tags");
					if (tagsElementList != null) {
						log.debug(tagsElementList.size());
						for (Element tagsElement : tagsElementList) {
							List<Element> ielementList = tagsElement
									.elements(TagModel.NODE_NAME);
							// 更新变量名
							log.debug(ielementList.size());
							for (Element ele : ielementList) {
								String varType = ele.attributeValue("type");
								String varName = ele
										.attributeValue(TagModel.VAR_NAME_ATTR);
								String oldName = varName.substring(0,
										varName.length() - 6);
								String newNum = GetVarNum.instanse
										.getVarNum(varType);
								// String newNum = "0000";
								String newName = oldName + newNum;
								ele.addAttribute(TagModel.VAR_NAME_ATTR,
										newName);
							}
						}
					}
					treeViewer.add(indexModel, inm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}

	/**
	 * 为其他索引导入标签
	 * @param indexModel
	 * @param treeViewer
	 */
	public static void importLabelForOtherIndex(IndexModel indexModel,
			TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
			//MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getIndexXPathByName(indexModel);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) indexModel
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					IndexNodeModel inm = new IndexNodeModel(myElement);
					inm.setParentObject(indexModel);

					myElement.setParent(null);
					indexModel.getElement().add(myElement);
					myElement.setParent(indexModel.getElement());

					treeViewer.add(indexModel, inm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}

	/**
	 * 为其他索引子索引导入标签
	 * @param indexNodeModel
	 * @param treeViewer
	 */
	public static void importLabelForOtherIndexNode(IndexNodeModel indexNodeModel, TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
			//MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) indexNodeModel
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					IndexNodeModel inm = new IndexNodeModel(myElement);
					inm.setParentObject(indexNodeModel);

					myElement.setParent(null);
					indexNodeModel.getElement().add(myElement);
					myElement.setParent(indexNodeModel.getElement());

					treeViewer.add(indexNodeModel, inm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}
	
	/**
	 * 导入变量类型
	 * @param tagStr
	 * @param treeViewer
	 */
	public static void importLabelForType(String tagStr, TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
//			MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
					+ XMLDocumentFactory.TAGTYPE_NODE;	// /root/tagindex
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				//MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				Element tagTypeElement = (Element) XMLDocumentFactory.instance.getMainDocument().selectSingleNode("/root/tagtype");
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>)tagTypeElement.elements()
							) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					TagTypeModel ttm = new TagTypeModel(myElement);
					ttm.setParentObject(tagStr);

					myElement.setParent(null);
					tagTypeElement.add(myElement);
					myElement.setParent(tagTypeElement);

					treeViewer.add(tagStr, ttm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}
	
	public static void importLabelForType(TagTypeModel type, TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
			//MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getTagTypeXPathByName(type);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) type
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					TagTypeModel tagTypeModel = new TagTypeModel(myElement);
					tagTypeModel.setParentObject(type);

					myElement.setParent(null);
					type.getElement().add(myElement);
					myElement.setParent(type.getElement());

					treeViewer.add(type, tagTypeModel); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}
	
	public static void importLabelForChannel(ChannelModel channel, TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
//			MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = GetXpathUtil.getChannelXPathById(channel);
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				//MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>) channel
							.getElement().elements()) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					DeviceModel deviceModel = new DeviceModel(myElement);
					deviceModel.setParentObject(channel);

					myElement.setParent(null);
					channel.getElement().add(myElement);
					myElement.setParent(channel.getElement());

					treeViewer.add(channel, deviceModel); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}
	
	public static void importLabelForChannel(String tagStr, TreeViewer treeViewer) {
		Document doc = getDoc();
		if (doc == null) {
//			MessageDialog.openError(null, "错误", "读取文件出错！");
			return;
		} else {
			String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
					+ XMLDocumentFactory.CHANNEL_NODE;	// /root/channels
			log.debug(path);
			Element element = (Element) doc.selectSingleNode(path);
			if (element == null) {
				MessageDialog.openError(null, "错误", "该文件格式错误！");
				return;
			} else {
				List<Element> elementList = element.elements();
				Element tagTypeElement = (Element) XMLDocumentFactory.instance.getMainDocument().selectSingleNode("/root/channels");
				for (Element myElement : elementList) {
					boolean hasName = false;
					for (Element paElement : (List<Element>)tagTypeElement.elements()
							) {
						if (paElement.attributeValue("name").equals(
								myElement.attributeValue("name"))) {
							hasName = true;
							break;
						}
					}
					if (hasName) { // 已存在该索引
						continue;
					}
					ChannelModel cm = new ChannelModel(myElement);
					cm.setParentObject(tagStr);

					myElement.setParent(null);
					tagTypeElement.add(myElement);
					myElement.setParent(tagTypeElement);

					treeViewer.add(tagStr, cm); // 更新树
				}
				XMLDocumentFactory.instance.saveMainDocument();
			}
		}
	}
	
	private static Document getDoc() {
		File filePath = new File("标签库/");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		FileDialog fileDialog = new FileDialog(Display.getDefault()
				.getActiveShell().getShell(), SWT.OPEN);
		fileDialog.setFilterPath("标签库/");
		fileDialog.setFilterExtensions(new String[] { "*.xml" });
		fileDialog.setFilterNames(new String[] { "XML标签库文件（*.xml）" });

		String fileName = fileDialog.open();

		if (fileName != null && !"".equals(fileName)) {
			if (!fileName.contains(".")) {
				fileName += ".xml";
			}

			File file = new File(fileName);
			if (!file.exists()) {
				MessageDialog.openError(null, "错误", "找不到指定文件！");
				return null;
			}
			log.debug(fileName);

			SAXReader reader = new SAXReader();
			Document doc = null;
			try {
				doc = reader.read(new File(fileName));
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
			return doc;
		} else {
			return null;
		}
	}
}
