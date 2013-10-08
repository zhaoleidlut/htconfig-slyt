package com.htong.tags.daoImpl;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.htong.tags.dao.IndexNodeModelDao;
import com.htong.tags.model.BuildingProp;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;
import com.htong.ui.MainUI;
import com.htong.ui.tree.TreeContentProvider;

public class IndexNodeModelDaoImpl implements IndexNodeModelDao {

	private final Logger log = Logger.getLogger(IndexNodeModelDaoImpl.class);
	@Override
	public void addElement(IndexNodeModel indexNodeModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String path = null;
		if(indexNodeModel.getParentObject() instanceof IndexModel) {
			IndexModel indexModel = (IndexModel)indexNodeModel.getParentObject();
			path = GetXpathUtil.getIndexXPathByName(indexModel);
		} else if(indexNodeModel.getParentObject() instanceof IndexNodeModel) {
			IndexNodeModel inm = (IndexNodeModel)indexNodeModel.getParentObject();
			path = GetXpathUtil.getIndexNodeXPathByName(inm);
		}
		log.debug(path);
		
		Element oldElement1 = (Element) document.selectSingleNode(path);
		
		Element newElement = oldElement1.addElement(IndexNodeModel.NODE_NAME);
		newElement.addAttribute(IndexNodeModel.NAME_ATTR, indexNodeModel.getName());
		newElement.addAttribute(IndexNodeModel.TYPE_ATTR, indexNodeModel.getType());
		newElement.addAttribute(IndexNodeModel.AREA_ATTR, indexNodeModel.getArea());
		newElement.addAttribute(IndexNodeModel.CODE_ATTR, indexNodeModel.getCode());
		newElement.addAttribute(IndexNodeModel.END_NAME_ATTR, indexNodeModel.getEndName());
		
		newElement.addAttribute(IndexNodeModel.NORMAL_INDEX_ATTR, indexNodeModel.getNormalIndex());
		newElement.addAttribute(IndexNodeModel.ENERGY_INDEX_ATTR, indexNodeModel.getEnergyIndex());
		
		newElement.addAttribute(IndexNodeModel.CHANNEL_ID_ATTR, indexNodeModel.getChannelId());
		newElement.addAttribute(IndexNodeModel.DEVICE_ID_ATTR, indexNodeModel.getDeviceId());
		
		newElement.addAttribute(IndexNodeModel.AIR_AREA_ATTR, indexNodeModel.getAir_condition_area());
		newElement.addAttribute(IndexNodeModel.HEATING_AREA_ATTR, indexNodeModel.getHeating_area());
		newElement.addAttribute(IndexNodeModel.PEOPLE_NUM_ATTR, indexNodeModel.getPeople_num());
		
		newElement.addAttribute(IndexNodeModel.NUMBER_ATTR, indexNodeModel.getNumber());
		if(indexNodeModel.getType() == null) {
			newElement.addAttribute(IndexNodeModel.TRANSFORMER_TYPE_ATTR, indexNodeModel.getTransformer_type());
		}
		
		newElement.addAttribute(IndexNodeModel.TRANSFORMER_NAME_ATTR, indexNodeModel.getTransformer_name());
		//其他属性
		indexNodeModel.setElement(newElement);	//set元素
		
		XMLDocumentFactory.instance.saveMainDocument(document);
	}
	
	@Override
	public void updateIndexNodeModel(IndexNodeModel indexNodeModel, String oldName) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String path = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel, oldName);
		Element element = (Element) document.selectSingleNode(path);
		log.debug(path);
		
		element.addAttribute(IndexNodeModel.NAME_ATTR, indexNodeModel.getName());
		element.addAttribute(IndexNodeModel.TYPE_ATTR, indexNodeModel.getType());
		element.addAttribute(IndexNodeModel.AREA_ATTR, indexNodeModel.getArea());
		element.addAttribute(IndexNodeModel.CODE_ATTR, indexNodeModel.getCode());
		element.addAttribute(IndexNodeModel.END_NAME_ATTR, indexNodeModel.getEndName());
		
		element.addAttribute(IndexNodeModel.NORMAL_INDEX_ATTR, indexNodeModel.getNormalIndex());
		element.addAttribute(IndexNodeModel.ENERGY_INDEX_ATTR, indexNodeModel.getEnergyIndex());
		
		element.addAttribute(IndexNodeModel.CHANNEL_ID_ATTR, indexNodeModel.getChannelId());
		element.addAttribute(IndexNodeModel.DEVICE_ID_ATTR, indexNodeModel.getDeviceId());
		
		element.addAttribute(IndexNodeModel.AIR_AREA_ATTR, indexNodeModel.getAir_condition_area());
		element.addAttribute(IndexNodeModel.HEATING_AREA_ATTR, indexNodeModel.getHeating_area());
		element.addAttribute(IndexNodeModel.PEOPLE_NUM_ATTR, indexNodeModel.getPeople_num());
		
		element.addAttribute(IndexNodeModel.NUMBER_ATTR, indexNodeModel.getNumber());
		
		
		if(indexNodeModel.getType() == null) {
			element.addAttribute(IndexNodeModel.TRANSFORMER_TYPE_ATTR, indexNodeModel.getTransformer_type());
		}
		
		element.addAttribute(IndexNodeModel.TRANSFORMER_NAME_ATTR, indexNodeModel.getTransformer_name());
		
		indexNodeModel.setElement(element);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
		
		
//		changeElement(indexNodeModel, GetXpathUtil.getIndexNodeXPathByName(indexNodeModel));
		
	}
	/**
	 * 更新子节点的对象的Element信息，以更新xpath
	 * @param indexNodeModel
	 * @param parentXPath
	 */
//	private void changeElement(IndexNodeModel indexNodeModel, String parentXPath) {
//		Object o[] = ((TreeContentProvider)MainUI.treeViewer.getContentProvider()).getChildren(indexNodeModel);
//		if(o != null) {
//			for(Object object : o) {
//				IndexNodeModel inm = (IndexNodeModel)object;
//				String xPath = parentXPath + "/" + IndexNodeModel.NODE_NAME +"[@"+IndexNodeModel.NAME_ATTR + "='" +inm.getName() + "']";
//				log.debug("更新的子Element xpath:"+xPath);
//				inm.setElement((Element) XMLDocumentFactory.instance.getMainDocument().selectSingleNode(xPath));
//				changeElement(inm, GetXpathUtil.getIndexNodeXPathByName(inm));	//递归调用
//			}
//		}
//	}

	@Override
	public void removeIndexNodeModel(IndexNodeModel indexNodeModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String path = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel);
		log.debug(path);
		Element element = (Element) document.selectSingleNode(path);
		String parentXpath = null;
		if(indexNodeModel.getParentObject() instanceof IndexNodeModel) {
			parentXpath =GetXpathUtil.getIndexNodeXPathByName((IndexNodeModel)indexNodeModel.getParentObject());
		} else {
			parentXpath =GetXpathUtil.getIndexXPathByName((IndexModel)indexNodeModel.getParentObject());
		}
		Element parentElement = (Element) document.selectSingleNode(parentXpath);
		parentElement.remove(element);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
		
	}
	
	/**
	 * 通过建筑物名字获得建筑物
	 * @param buildingName
	 * @return
	 */
	public IndexNodeModel getBuildingByName(String buildingName) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']";
		Element element = (Element) document.selectSingleNode(xpath);
		if(element!=null) {
			return new IndexNodeModel(element);
		} else {
			return null;
		}
		
		
	}
	
	/**
	 * 通过名字数组获得能耗编码
	 * @param str ["电","空调用电"]
	 * @return
	 */
	public IndexNodeModel getEnergyCodeByStringArray(String[] str) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		
		String xpath = "/root/tagindex/index[@type='energy']";
		
		for(String s : str) {
			xpath = xpath.concat("/indexnode[@name='" + s + "']");
		}
		
		Element element = (Element) document.selectSingleNode(xpath);
		if(element!=null) {
			return new IndexNodeModel(element);
		} else {
			return null;
		}
		
	}
	
	
	/**
	 * 获得建筑属性
	 * @param buildingName
	 * @param propName
	 * @return
	 */
	public String getBuildingPropTest(String buildingName, String propName) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']/prop[@name='" + propName + "']";
		Element element = (Element) document.selectSingleNode(xpath);
		if(element!=null) {
			return element.getTextTrim();
		} else {
			return "";
		}
	}
	
	/**
	 * 获得建筑物介绍
	 * @param buildingName 建筑物名字
	 * @return
	 */
	public String getBuildingIntro(String buildingName) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']/intro";
		Element element = (Element) document.selectSingleNode(xpath);
		if(element!=null) {
			return element.getTextTrim();
		} else {
			return "";
		}
	}
	
	/**
	 * 添加或删除建筑物介绍 text==null 则删除属性
	 * @param buildingName
	 * @param text
	 */
	public void addOrDeleteBuildingIntro(String buildingName, String text) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']/intro";
		Element element = (Element) document.selectSingleNode(xpath);
		if(text == null) {//删除
			if(element != null) {
				Element parentElement = element.getParent();
				parentElement.remove(element);
				XMLDocumentFactory.instance.saveMainDocument(document);
			}
		} else {
			if(element != null) {
				element.setText(text);
				XMLDocumentFactory.instance.saveMainDocument(document);
			} else {
				String pxpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']";
				Element parentElement = (Element) document.selectSingleNode(pxpath);
				Element newElement = parentElement.addElement("intro");
				
				newElement.setText(text);
				XMLDocumentFactory.instance.saveMainDocument(document);
			}
		}
	}
	
	/**
	 * 添加或删除建筑物属性 text==null 则删除属性
	 * @param buildingName
	 * @param propName
	 * @param text
	 */
	public void addOrDeleteBuildingProp(String buildingName, String propName, String text) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']/prop[@name='" + propName + "']";
		Element element = (Element) document.selectSingleNode(xpath);
		if(text == null) {//删除
			if(element != null) {
				Element parentElement = element.getParent();
				parentElement.remove(element);
				XMLDocumentFactory.instance.saveMainDocument(document);
			}
		} else {
			if(element != null) {
				element.setText(text);
				XMLDocumentFactory.instance.saveMainDocument(document);
			} else {
				String pxpath = "/root/tagindex/index[@type='normal']/indexnode/indexnode[@name='"+buildingName+"']";
				Element parentElement = (Element) document.selectSingleNode(pxpath);
				Element newElement = parentElement.addElement(BuildingProp.NODE_NAME);
				newElement.addAttribute("name", propName);
				newElement.setText(text);
				XMLDocumentFactory.instance.saveMainDocument(document);
			}
		}
	}
	
	/**
	 * 获得indexNode
	 * @param xpath /root/tagindex/index[@name="电气索引"]/indexnode[@name="A楼"]
	 * @return
	 */
	public IndexNodeModel getIndexNodeByXpath(String xpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element e = (Element) document.selectSingleNode(xpath);
		if(e != null) {
			return new IndexNodeModel(e);
		} else {
			return null;
		}
	}
	
	/**
	 * 获得建筑物上级
	 * @return 
	 */
	public IndexNodeModel getBuildingNode() {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='normal']/indexnode";
		Element element = (Element) document.selectSingleNode(xpath);
		if(element != null) {
			return new IndexNodeModel(element);
		} else 
			return null;
	}
	
	/**
	 * 判断是否为末端节点
	 * @param indexNode
	 * @return
	 */
	public boolean isEndNode(IndexNodeModel indexNode) {
		Element e = indexNode.getElement();
		if(e == null) {
			return false;
		} else {
			if(e.element(IndexNodeModel.NODE_NAME)==null){
				return true;
			} else {
				return false;
			}
		}
		
	}


	public Element getLeafElement(String str) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		
		String s[] = str.split("/");
		String xpath = "/root/tagindex/index[@type='normal']";
		for(String myS : s) {
			xpath = xpath + "/indexnode[@name='"+myS+"']";
		}

		
		log.debug(xpath);
		Element e = (Element) document.selectSingleNode(xpath);
		if(e == null) {
			log.debug("为空");
			return null;
		} else  {
			return e;
		}
	}
}
