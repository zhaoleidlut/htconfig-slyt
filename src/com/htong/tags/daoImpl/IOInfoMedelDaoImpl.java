package com.htong.tags.daoImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.IOInfoModel;
import com.htong.ui.GetXpathUtil;

public class IOInfoMedelDaoImpl {
	
	private static final Logger log = Logger.getLogger(IOInfoMedelDaoImpl.class);

	/**
	 * 获得IO信息
	 * 
	 * @param tagModel
	 * @return
	 */
	public IOInfoModel getIoInfoModel(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
//		Document document = tagModel.getElement().getDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		Element ioElement = e.element(IOInfoModel.NODE_NAME);
		if (ioElement != null) {
			return new IOInfoModel(ioElement);
		}
		return null;

	}
	/**
	 * 添加
	 * @param tagModel
	 */
	public void add(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
//		Document document = tagModel.getElement().getDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		Element ioElement = e.addElement(IOInfoModel.NODE_NAME);
		
		IOInfoModel io = tagModel.getIoInfo();
		
		ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, io.getChannelId());
		ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
				io.getTransChannelId());
		ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, io.getDeviceId());
		ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, io.getFunCode());
		ioElement
				.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, io.getRegAddress());
		ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, io.getByteLen());
		ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, io.getOffset());
		ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, io.getValueType());
		ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, io.getBaseValue());
		ioElement.addAttribute(IOInfoModel.COEF_ATTR, io.getCoef());
		ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, io.getPriority());
		ioElement.addAttribute(IOInfoModel.PUSH_DATA_ATTR, io.getPushData());

//		XMLDocumentFactory.instance.saveMainDocument(document);
		
	}
	
	public void deleteIO(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		
		String ioXpath = tagXpath + "/" + IOInfoModel.NODE_NAME;
		
		Element ioE = (Element) document.selectSingleNode(ioXpath);
		
		if(ioE != null) {
			boolean s = e.remove(ioE);
			System.out.println(s==true);
		}
	}
	
	public void deleteAllIO(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		
		String ioXpath = tagXpath + "/" + IOInfoModel.NODE_NAME;
		
		List<Element> ioEList = document.selectNodes(ioXpath);
		
		if(ioEList != null && !ioEList.isEmpty()) {
			for(Element ioE : ioEList) {
				boolean s = e.remove(ioE);
				//System.out.println(s==true);
			}
		}
	}
	
	public void saveOrUpdate(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		Element ioElement = e.element(IOInfoModel.NODE_NAME);
		if (ioElement != null) {
			IOInfoModel io = tagModel.getIoInfo();
			
			ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, io.getChannelId());
			ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
					io.getTransChannelId());
			ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, io.getDeviceId());
			ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, io.getFunCode());
			ioElement
					.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, io.getRegAddress());
			ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, io.getByteLen());
			ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, io.getOffset());
			ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, io.getValueType());
			ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, io.getBaseValue());
			ioElement.addAttribute(IOInfoModel.COEF_ATTR, io.getCoef());
			ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, io.getPriority());
			ioElement.addAttribute(IOInfoModel.PUSH_DATA_ATTR, io.getPushData());
		} else {
			ioElement = e.addElement(IOInfoModel.NODE_NAME);
			
			IOInfoModel io = tagModel.getIoInfo();
			
			ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, io.getChannelId());
			ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
					io.getTransChannelId());
			ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, io.getDeviceId());
			ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, io.getFunCode());
			ioElement
					.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, io.getRegAddress());
			ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, io.getByteLen());
			ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, io.getOffset());
			ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, io.getValueType());
			ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, io.getBaseValue());
			ioElement.addAttribute(IOInfoModel.COEF_ATTR, io.getCoef());
			ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, io.getPriority());
			ioElement.addAttribute(IOInfoModel.PUSH_DATA_ATTR, io.getPushData());
		}
	}

	/**
	 * 修改
	 */
	public void update(TagModel tagModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();

		String tagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		Element e = (Element) document.selectSingleNode(tagXpath);
		Element ioElement = e.element(IOInfoModel.NODE_NAME);
		if (ioElement != null) {
			IOInfoModel io = tagModel.getIoInfo();
			
			ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, io.getChannelId());
			ioElement.addAttribute(IOInfoModel.TRANS_CHANNEL_ID_ATTR,
					io.getTransChannelId());
			ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, io.getDeviceId());
			ioElement.addAttribute(IOInfoModel.FUNCTION_CODE_ATTR, io.getFunCode());
			ioElement
					.addAttribute(IOInfoModel.REG_ADDRESS_ATTR, io.getRegAddress());
			ioElement.addAttribute(IOInfoModel.BYTE_LENGTH_ATTR, io.getByteLen());
			ioElement.addAttribute(IOInfoModel.OFFSET_ATTR, io.getOffset());
			ioElement.addAttribute(IOInfoModel.VALUE_TYPE_ATTR, io.getValueType());
			ioElement.addAttribute(IOInfoModel.BASE_VALUE_ATTR, io.getBaseValue());
			ioElement.addAttribute(IOInfoModel.COEF_ATTR, io.getCoef());
			ioElement.addAttribute(IOInfoModel.PRIORITY_ATTR, io.getPriority());
			ioElement.addAttribute(IOInfoModel.PUSH_DATA_ATTR, io.getPushData());

//			XMLDocumentFactory.instance.saveMainDocument(document);
		}
		

	}

	public Element getIOInfoByChannelIdAndDeviceId(String channelId, String deviceId) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
//		Document document = tagModel.getElement().getDocument();

		String xpath = "/root/tagindex/index[@type='main']//tagioinfo[@channel-id='"+channelId+"' and @device-id='"+deviceId+"']";
		log.debug(xpath);
		Element e = (Element) document.selectSingleNode(xpath);
		if(e == null) {
			log.debug("NULL");
			return null;
		} else  {
			return e.getParent();
		}
	}
}
