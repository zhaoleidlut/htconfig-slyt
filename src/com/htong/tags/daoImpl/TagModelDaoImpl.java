package com.htong.tags.daoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.htong.tags.dao.TagModelDao;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.IOInfoModel;
import com.htong.ui.GetVarNum;
import com.htong.ui.GetXpathUtil;

public class TagModelDaoImpl implements TagModelDao {
	private final Logger log = Logger.getLogger(TagModelDaoImpl.class);
	
	private static int ii = 0;

	@Override
	public boolean isMainIndex(IndexNodeModel indexNodeModel) {

		StringBuilder sb = new StringBuilder();
		sb.append(indexNodeModel.getName());

		Element parentElement = indexNodeModel.getElement().getParent();
		while (!parentElement.getName().equals(IndexModel.NODE_NAME)) {
			parentElement = parentElement.getParent();
		}
		if (parentElement.attributeValue(IndexModel.TYPE_ATTR) == null
				|| !"main".equals(parentElement
						.attributeValue(IndexModel.TYPE_ATTR))) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public TagModel getTagByXpath(String xpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element e = (Element) document.selectSingleNode(xpath);
		if(e != null) {
			return new TagModel(e);
		}
		
		return null;
	}

	@Override
	public void addVariable(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		log.debug(tagModel.getMainIndex());
		String xpath = GetXpathUtil.getTagModelMainIndexXPath(tagModel
				.getMainIndex());
		log.debug(xpath);
		Element e = (Element) document.selectSingleNode(xpath);
		Element newElement = null;
		if (e.element(XMLDocumentFactory.TAGS_NODE) == null) {
			newElement = e.addElement(XMLDocumentFactory.TAGS_NODE);
		} else {
			newElement = e.element(XMLDocumentFactory.TAGS_NODE);
		}
		newElement = newElement.addElement(TagModel.NODE_NAME);

		newElement.addAttribute(TagModel.NAME_ATTR, tagModel.getName());
		newElement.addAttribute(TagModel.VAR_NAME_ATTR, tagModel.getVarName());
		newElement.addAttribute(TagModel.VALUE_ATTR, tagModel.getValue());
		newElement.addAttribute(TagModel.TYPE_ATTR, tagModel.getType());
		newElement.addAttribute(TagModel.NORMAL_INDEX_ATTR, tagModel.getNormalIndex());
		newElement.addAttribute(TagModel.ENERGY_INDEX_ATTR, tagModel.getEnergyIndex());
		
		newElement.addAttribute(TagModel.RATED_POWER, tagModel.getRatedPower());

		tagModel.setElement(newElement);

		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public List<TagModel> getTagsByMainIndex(String mainIndex) {
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		String allPath = GetXpathUtil.getTagModelMainIndexXPath(mainIndex);
		Element element = (Element) XMLDocumentFactory.instance
				.getMainDocument().selectSingleNode(allPath);
		if (element == null) {
			return null;
		} else if (element.element(XMLDocumentFactory.TAGS_NODE) == null) {
			return null;
		} else {
			Element e = element.element(XMLDocumentFactory.TAGS_NODE);
			for (Iterator<?> i = e.elementIterator(TagModel.NODE_NAME); i
					.hasNext();) {
				Element myElement = (Element) i.next();
				TagModel tagModel = new TagModel(myElement);
				tagModel.setMainIndex(mainIndex);	//设置主索引
				
				Element ioElement = myElement.element(IOInfoModel.NODE_NAME);
				if(ioElement != null) {
					IOInfoModel io = new IOInfoModel(ioElement);
					tagModel.setIoInfo(io);
				}
				
				tagModelList.add(tagModel);
			}
		}

		return tagModelList;
	}

	@Override
	public List<TagModel> getTagsBySecondIndex(String secondIndex) {
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		Document document = XMLDocumentFactory.instance.getMainDocument();
		IndexModel indexModel = new IndexModelDaoImpl().getMainIndexModel(); // 主索引
		String xpath = indexModel.getElement().getPath() + "[@"
				+ IndexModel.NAME_ATTR + "='" + indexModel.getName() + "']"; // 主索引XPath
		Element element = (Element) document.selectSingleNode(xpath);
		
		handleTagList(element, secondIndex, tagModelList);
		return tagModelList;
	}

	/**
	 * 分项索引初始化变量
	 * 
	 * @param e
	 */
	private void handleTagList(Element e, String secondIndex,
			List<TagModel> tagModelList) {

		Element tagElement = e.element(XMLDocumentFactory.TAGS_NODE);// 变量
//		log.debug(e.attributeValue("name"));

		if (tagElement != null) {
			for (Iterator<Element> i = tagElement.elementIterator(); i
					.hasNext();) {
				Element element = i.next();
				TagModel tagModel = new TagModel(element);
				boolean ok = false; // 是否是当前位置的变量
				if (tagModel.getNormalIndex() == null && tagModel.getEnergyIndex() == null) {
					continue;
				} else {
					//判断能耗分类分项是否匹配
					if(tagModel.getEnergyIndex() != null) {
						if(tagModel.getEnergyIndex().trim().equals(secondIndex)) {
							ok = true;
						}
					}
					if(!ok) {
						//判断常规分类分区是否匹配
						if(tagModel.getNormalIndex() != null) {
							for (String str : tagModel.getNormalIndex().split(",")) {
								if (str.trim().equals(secondIndex)) {
									ok = true;
									break;
								}
							}
						}
					}
				}
				if (!ok) {
					continue;
				}
				// 初始化主索引
				StringBuilder indexMainSB = new StringBuilder();

				Element tempElement = tagElement.getParent();
				while (!tempElement.getPath().equals(
						"/" + XMLDocumentFactory.ROOT_NODE + "/"
								+ XMLDocumentFactory.TAGINDEX_NODE)) {
					indexMainSB.insert(0, tempElement.attributeValue("name")
							+ "/");
					tempElement = tempElement.getParent();
				}
				indexMainSB.deleteCharAt(indexMainSB.length() - 1);// 删除最后“/”

				tagModel.setMainIndex(indexMainSB.toString()); // 设置主索引

				tagModelList.add(tagModel);
			}
		}
		// 处理子元素
		for (Iterator<Element> i = e.elementIterator(IndexNodeModel.NODE_NAME); i
				.hasNext();) {
			Element myElement = i.next();
			handleTagList(myElement, secondIndex, tagModelList);
		}
	}

	@Override
	public void removeTagModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelMainIndexXPath(tagModel
				.getMainIndex());
		xpath += "/" + XMLDocumentFactory.TAGS_NODE;
		log.debug(xpath);
		Element e = (Element) document.selectSingleNode(xpath);
		xpath += "/" + TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='" + tagModel.getName() + "']";
		Element deleteElement = (Element) document.selectSingleNode(xpath);
		e.remove(deleteElement);
		//XMLDocumentFactory.instance.saveMainDocument(document);

	}
	
	/**
	 * 
	 * @param xpath ///root/tagindex/index[@name='变量标签索引']/indexnode[@name='5AA_9']
	 * @param name
	 */
	public void removeTagModel(String xpath, String name) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String allpath = xpath + "/tags/tag[@name='"+name+"']";
		Element e = (Element) document.selectSingleNode(allpath);
		if(e!=null) {
			e.getParent().remove(e);
			//XMLDocumentFactory.instance.saveMainDocument(document);
		}
		
	}

	@Override
	public void updateTagModel(TagModel tagModel, String oldname) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getTagModelMainIndexXPath(tagModel
				.getMainIndex());
		log.debug(xpath);
		xpath = xpath + "/"+XMLDocumentFactory.TAGS_NODE+"/" + TagModel.NODE_NAME + "[@name='"+oldname+"']";
		Element e = (Element) document.selectSingleNode(xpath);
		if (e != null) {
		

		e.addAttribute(TagModel.NAME_ATTR, tagModel.getName());
		e.addAttribute(TagModel.VAR_NAME_ATTR, tagModel.getVarName());
		e.addAttribute(TagModel.VALUE_ATTR, tagModel.getValue());
		e.addAttribute(TagModel.TYPE_ATTR, tagModel.getType());
		e.addAttribute(TagModel.NORMAL_INDEX_ATTR, tagModel.getNormalIndex());
		e.addAttribute(TagModel.ENERGY_INDEX_ATTR, tagModel.getEnergyIndex());
		
		e.addAttribute(TagModel.RATED_POWER, tagModel.getRatedPower());

		tagModel.setElement(e);

		XMLDocumentFactory.instance.saveMainDocument(document);
		
		}

	}

	@Override
	public List<TagModel> getSubTagModel(String parentTagXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element parentElement = (Element) document.selectSingleNode(parentTagXpath);
		List<TagModel> subTagModel = new ArrayList<TagModel>();
		for(Iterator<Element> i = parentElement.elementIterator(TagModel.NODE_NAME);i.hasNext();) {
			Element e = i.next();
			TagModel tagModel = new TagModel(e);
			subTagModel.add(tagModel);
		}
		
		return subTagModel;
		
	}

	@Override
	public void addSubTagModel(String parentTagXpath, TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element e = (Element) document.selectSingleNode(parentTagXpath);
		Element newElement = e.addElement(TagModel.NODE_NAME);
		
		newElement.addAttribute(TagModel.NAME_ATTR, tagModel.getName());
		newElement.addAttribute(TagModel.VAR_NAME_ATTR, tagModel.getVarName());
		newElement.addAttribute(TagModel.VALUE_ATTR, tagModel.getValue());
		newElement.addAttribute(TagModel.TYPE_ATTR, tagModel.getType());
		newElement.addAttribute(TagModel.NORMAL_INDEX_ATTR, tagModel.getNormalIndex());
		newElement.addAttribute(TagModel.ENERGY_INDEX_ATTR, tagModel.getEnergyIndex());
		
		newElement.addAttribute(TagModel.RATED_POWER, tagModel.getRatedPower());

		tagModel.setElement(newElement);

		XMLDocumentFactory.instance.saveMainDocument(document);
		
	}

	@Override
	public void updateSubTagModel(String parentTagXpath, TagModel tagModel,
			String oldName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSubTagModel(String parentTagXpath, TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element e = (Element) document.selectSingleNode(parentTagXpath);
		log.debug(parentTagXpath);
		parentTagXpath += "/" + TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='" + tagModel.getName() + "']";
		log.debug(parentTagXpath);
		Element deleteElement = (Element) document.selectSingleNode(parentTagXpath);
		e.remove(deleteElement);
		XMLDocumentFactory.instance.saveMainDocument(document);
		
	}
	
	/**
	 * 通过通道号和设备地址获得变量
	 * @param channelId
	 * @param slaveId
	 * @return
	 */
	public List<TagModel> getTagByChannelIDAndDeviceSlaveId(String channelId, String slaveId) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		
		String xpath = "/root/tagindex/index[@type='main']//tagioinfo[@channel-id='"+channelId+"' and @device-id='" + slaveId + "']";
		List<?> elementList = document.selectNodes(xpath);
		Iterator<?> i = elementList.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof Element) {
				Element n = (Element) o;
				IOInfoModel ioInfo = new IOInfoModel(n);
				TagModel tagModel = new TagModel(n.getParent());
				tagModel.setIoInfo(ioInfo);	//设置io信息
				
				//设置主索引
				StringBuilder sb = new StringBuilder();
				Element ei = n.getParent().getParent().getParent();//最后一个索引节点
				while(ei.getName()!="index") {
					sb.insert(0,"/" + ei.attributeValue("name"));
					ei = ei.getParent();
				}
				sb.insert(0,ei.attributeValue("name"));
				tagModel.setMainIndex(sb.toString());
				
				tagModelList.add(tagModel);
			}
		}
		return tagModelList;
	}
	
	/**
	 * 通过通道号获得变量
	 * @param channelId
	 * @return
	 */
	public List<TagModel> getTagByChannelID(String channelId) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		
		String xpath = "/root/tagindex/index[@type='main']//tagioinfo[@channel-id='"+channelId+"']";
		List<?> elementList = document.selectNodes(xpath);
		Iterator<?> i = elementList.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof Element) {
				Element n = (Element) o;
				IOInfoModel ioInfo = new IOInfoModel(n);
				TagModel tagModel = new TagModel(n.getParent());
				tagModel.setIoInfo(ioInfo);	//设置io信息
				
				//设置主索引
				StringBuilder sb = new StringBuilder();
				Element ei = n.getParent().getParent().getParent();//最后一个索引节点
				while(ei.getName()!="index") {
					sb.insert(0,"/" + ei.attributeValue("name"));
					ei = ei.getParent();
				}
				sb.insert(0,ei.attributeValue("name"));
				tagModel.setMainIndex(sb.toString());
				
				tagModelList.add(tagModel);
			}
		}
		return tagModelList;
	}
	
	/**
	 * 获得所有变量类型
	 * @param tagType
	 * @return
	 */
	public List<TagModel> getAllVarByType(String tagType) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		
		String xpath;
		
		if(tagType.substring(0, 2).equals("遥测")||
				tagType.substring(0, 2).equals("遥信")||
				tagType.substring(0, 2).equals("遥控")||
				tagType.substring(0, 2).equals("遥脉")||
				tagType.substring(0, 2).equals("遥调")) {
			xpath = "/root/tagindex/index[@type=\"main\"]//tags/tag[starts-with(@type,'"+tagType.substring(0, 2)+"')]"; 
		} else {
			xpath = "/root/tagindex/index[@type=\"main\"]//tags/tag[not(starts-with(@type,'遥测')) and not(starts-with(@type,'遥信')) and not(starts-with(@type,'遥脉')) and not(starts-with(@type,'遥调')) and not(starts-with(@type,'遥控')) and @var-name]";
		}
		log.debug("定义序号，查找变量的xpath为：" + xpath);
		
		
		List<?> elementList = document.selectNodes(xpath);
		Iterator<?> i = elementList.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof Element) {
				Element n = (Element) o;
				TagModel tagModel = new TagModel(n);
				
				tagModelList.add(tagModel);
			}
		}
		return tagModelList;
	}
	
	/**
	 * 通过主索引获得所有下级回路的所有变量
	 * @param mainIndex
	 * @return
	 */
	public List<TagModel> getTagsIncludeSubByMainIndex(String mainIndex) {
		List<TagModel> tagModelList = new ArrayList<TagModel>();
		String allPath = GetXpathUtil.getTagModelMainIndexXPath(mainIndex);
		
		String varPath = allPath + "//tag[@var-name and not(starts-with(@type,'内存'))]";
		
		List<Element> elementList =  XMLDocumentFactory.instance
				.getMainDocument().selectNodes(varPath);
		
		for(Element e : elementList) {
			TagModel tagModel = new TagModel(e);
			String myMainIndex = GetXpathUtil.getMainIndexByElementPath(e);
			log.debug("变量主索引：" + myMainIndex + ":"+ii++);
			tagModel.setMainIndex(myMainIndex);	//设置主索引
			
			Element ioElement = e.element(IOInfoModel.NODE_NAME);
			if(ioElement != null) {
				IOInfoModel io = new IOInfoModel(ioElement);
				io.setElement(ioElement);
				tagModel.setIoInfo(io);
			}
			
			
			tagModelList.add(tagModel);
		}
		
		return tagModelList;
	}
	
	/**
	 * 通过索引编号获得主索引
	 * @param number
	 * @return  电气索引/A楼/1层/1号柜/1#回路
	 */
	public String getTagMainIndexByIndexNodeNumber(String number) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = "/root/tagindex/index[@type='main']//indexnode[@number='"+number+"']";
		Element e = (Element) document.selectSingleNode(xpath);
		if(e != null) {
			return GetXpathUtil.getMainIndexByIndexNodeElement(e);
		} else {
			return "";
		}
	}
	
	public boolean addVariable(IndexNodeModel indexNode, TagModel tagModel) {
		Element tagElement = tagModel.getElement();
		Element ioElement = tagElement.element(IOInfoModel.NODE_NAME);
		Element indexNodeElement = indexNode.getElement();
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element tagsElement = indexNodeElement.element("tags");
		if(tagsElement == null) {
			tagsElement = indexNodeElement.addElement("tags");
		}
		String xpath = GetXpathUtil.getIndexNodeXPathByElement(indexNodeElement);
		xpath = xpath+"/tags/tag[@name='"+tagElement.attributeValue("name") + "']";
		Element myTagElement = (Element) document.selectSingleNode(xpath);
		if(myTagElement != null) {//变量已存在
			MessageDialog.openWarning(Display.getDefault().getActiveShell().getShell(), "错误", "变量【"+tagElement.attributeValue("name") + "】在【" + tagModel.getMainIndex() +"】下已存在！此变量不导入！");
			return false;
		}
		tagElement.setParent(null);
		
		tagElement.addAttribute(TagModel.NAME_ATTR, tagModel.getName());
//		//更新变量名
//		String oldVarName = tagModel.getVarName();
//		String varType = tagElement.attributeValue("type");
//		String oldName = oldVarName.substring(0, oldVarName.length()-4);
//		String newNum = GetVarNum.getVarNum(varType);
//		String newName = oldName + newNum;
		
		tagElement.addAttribute(TagModel.VAR_NAME_ATTR, tagModel.getVarName());
		tagElement.addAttribute(TagModel.VALUE_ATTR, tagModel.getValue());
		tagElement.addAttribute(TagModel.TYPE_ATTR, tagModel.getType());
		tagElement.addAttribute(TagModel.NORMAL_INDEX_ATTR, tagModel.getNormalIndex());
		tagElement.addAttribute(TagModel.ENERGY_INDEX_ATTR, tagModel.getEnergyIndex());
		
		tagElement.addAttribute(TagModel.RATED_POWER, tagModel.getRatedPower());
		
		IOInfoModel ioInfo = tagModel.getIoInfo();
		
		ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, ioInfo.getChannelId());
//		ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
//				ioInfo.getTransChannelId());
		ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, ioInfo.getDeviceId());
		ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, ioInfo.getFunCode());
		ioElement
				.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, ioInfo.getRegAddress());
		ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, ioInfo.getByteLen());
		ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, ioInfo.getOffset());
		ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, ioInfo.getValueType());
		ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, ioInfo.getBaseValue());
		ioElement.addAttribute(IOInfoModel.COEF_ATTR, ioInfo.getCoef());
		ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, ioInfo.getPriority());
		
		tagsElement.add(tagElement);
		return true;

//		XMLDocumentFactory.instance.saveMainDocument(indexNodeElement.getDocument());

	}

	public boolean addCJQVariable(IndexNodeModel indexNode, TagModel tagModel) {

		Element indexNodeElement = indexNode.getElement();
		Document document = XMLDocumentFactory.instance.getMainDocument();
		
		String xpath = GetXpathUtil.getIndexNodeXPathByElement(indexNodeElement);
		xpath = xpath+"/tags/tag[@name='"+tagModel.getName() + "']";
		Element myTagElement = (Element) document.selectSingleNode(xpath);
		if(myTagElement != null) {//变量已存在
			MessageDialog.openWarning(Display.getDefault().getActiveShell().getShell(), "错误", "变量【"+tagModel.getName() + "】在标签索引下已存在！此变量不导入！");
			return false;
		}
		
		Element tagsElement = indexNodeElement.element("tags");
		if(tagsElement == null) {
			tagsElement = indexNodeElement.addElement("tags");
		}
		
		Element tagElement = tagsElement.addElement(TagModel.NODE_NAME);

		
		tagElement.addAttribute(TagModel.NAME_ATTR, tagModel.getName());
		
		tagElement.addAttribute(TagModel.VAR_NAME_ATTR, tagModel.getVarName());
		tagElement.addAttribute(TagModel.VALUE_ATTR, tagModel.getValue());
		tagElement.addAttribute(TagModel.TYPE_ATTR, tagModel.getType());
		tagElement.addAttribute(TagModel.NORMAL_INDEX_ATTR, tagModel.getNormalIndex());
		tagElement.addAttribute(TagModel.ENERGY_INDEX_ATTR, tagModel.getEnergyIndex());
		
		tagElement.addAttribute(TagModel.RATED_POWER, tagModel.getRatedPower());
		
		Element ioElement = tagElement.addElement(IOInfoModel.NODE_NAME);
		
		IOInfoModel ioInfo = tagModel.getIoInfo();
		
		ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, ioInfo.getChannelId());
//		ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
//				ioInfo.getTransChannelId());
		ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, ioInfo.getDeviceId());
		ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, ioInfo.getFunCode());
		ioElement
				.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, ioInfo.getRegAddress());
		ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, ioInfo.getByteLen());
		ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, ioInfo.getOffset());
		ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, ioInfo.getValueType());
		ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, ioInfo.getBaseValue());
		ioElement.addAttribute(IOInfoModel.COEF_ATTR, ioInfo.getCoef());
		ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, ioInfo.getPriority());

		return true;

//		XMLDocumentFactory.instance.saveMainDocument(indexNodeElement.getDocument());

	}

}
