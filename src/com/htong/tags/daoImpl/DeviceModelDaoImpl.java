package com.htong.tags.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.dao.DeviceModelDao;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;

public class DeviceModelDaoImpl implements DeviceModelDao {

	private static final Logger log = Logger
			.getLogger(DeviceModelDaoImpl.class);


	@Override
	public List<DeviceModel> getDeviceModelsByChannelId(String channelId) {
		List<DeviceModel> deviceModelList = new ArrayList<DeviceModel>();

		String xpath = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "='"
				+ channelId + "']/" + DeviceModel.NODE_NAME;
		//log.debug(xpath);
		List<?> l = XMLDocumentFactory.instance.getMainDocument().selectNodes(xpath);

		Iterator<?> i = l.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof Element) {
				Element n = (Element) o;
				DeviceModel dm = new DeviceModel(n);
				deviceModelList.add(dm);
			}
		}

		return deviceModelList;
	}

	@Override
	public void addDeviceModel(DeviceModel deviceModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		ChannelModel channelModel = (ChannelModel) deviceModel
				.getParentObject();

		String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "='"
				+ channelModel.getId() + "']";
		Element e = (Element)document.selectSingleNode(path);

		if (e != null) {
			Element addedElement = e.addElement(DeviceModel.NODE_NAME);

			addedElement.addAttribute(DeviceModel.NAME_ATTR,
					deviceModel.getName());
			addedElement.addAttribute(DeviceModel.MANUFACTURER_ATTR,
					deviceModel.getManufacturer());
			addedElement.addAttribute(DeviceModel.TYPE_ATTR,
					deviceModel.getType());
			if (deviceModel.getInstallTime() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String result = sdf.format(deviceModel.getInstallTime()
						.getTime());
				addedElement.addAttribute(DeviceModel.INSTALLTIME_ATTR, result);
			} else {
				addedElement.addAttribute(DeviceModel.INSTALLTIME_ATTR, null);
			}

			addedElement.addAttribute(DeviceModel.TIMEOUT_ATTR,
					deviceModel.getTimeout());

			addedElement.addAttribute(DeviceModel.RETRY_ATTR,
					deviceModel.getRetry());
			addedElement.addAttribute(DeviceModel.SLAVEID_ATTR,
					deviceModel.getSlaveId());
			addedElement.addAttribute(DeviceModel.REMARK_ATTR,
					deviceModel.getRemark());

			addedElement.addAttribute(DeviceModel.USED_ATTR,
					deviceModel.getUsed());
			
			addedElement.addAttribute(DeviceModel.PT_ATTR, deviceModel.getPt());
			addedElement.addAttribute(DeviceModel.CT_ATTR, deviceModel.getCt());
			
			addedElement.addAttribute(DeviceModel.INSTALL_POSITION_ATTR, deviceModel.getFixPosition());

			channelModel.setElement(addedElement);

			XMLDocumentFactory.instance.saveMainDocument(document);
		}

	}

	@Override
	public void updateDeviceModelBySlaveId(DeviceModel deviceModel,
			String oldSlaveId) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		ChannelModel channelModel = new ChannelModel(deviceModel.getElement().getParent());

		String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "='"
				+ channelModel.getId() + "']/" + DeviceModel.NODE_NAME + "[@"
				+ DeviceModel.SLAVEID_ATTR + "='" + oldSlaveId + "']";
		log.debug(path);
		Element e = (Element) document.selectSingleNode(path);

		if (e != null) {
			e.addAttribute(DeviceModel.NAME_ATTR,
					deviceModel.getName());
			e.addAttribute(DeviceModel.MANUFACTURER_ATTR,
					deviceModel.getManufacturer());
			e.addAttribute(DeviceModel.TYPE_ATTR,
					deviceModel.getType());
			if (deviceModel.getInstallTime() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String result = sdf.format(deviceModel.getInstallTime()
						.getTime());
				e.addAttribute(DeviceModel.INSTALLTIME_ATTR, result);
			} else {
				e.addAttribute(DeviceModel.INSTALLTIME_ATTR, null);
			}

			e.addAttribute(DeviceModel.TIMEOUT_ATTR,
					deviceModel.getTimeout());

			e.addAttribute(DeviceModel.RETRY_ATTR,
					deviceModel.getRetry());
			e.addAttribute(DeviceModel.SLAVEID_ATTR,
					deviceModel.getSlaveId());
			e.addAttribute(DeviceModel.REMARK_ATTR,
					deviceModel.getRemark());

			e.addAttribute(DeviceModel.USED_ATTR,
					deviceModel.getUsed());
			
			e.addAttribute(DeviceModel.PT_ATTR, deviceModel.getPt());
			e.addAttribute(DeviceModel.CT_ATTR, deviceModel.getCt());
			
			e.addAttribute(DeviceModel.INSTALL_POSITION_ATTR, deviceModel.getFixPosition());
			
			deviceModel.setElement(e);

			XMLDocumentFactory.instance.saveMainDocument(document);
		}

	}

	@Override
	public void removeDeviceModelById(DeviceModel deviceModel) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		ChannelModel channelModel = (ChannelModel)deviceModel.getParentObject();
		
		String path = GetXpathUtil.getDeviceXPathBySlaveId(deviceModel);
		Element e = (Element) document.selectSingleNode(path);
		String parentPath = GetXpathUtil.getChannelXPathById(channelModel);
		Element parentElement = (Element) document.selectSingleNode(parentPath);
		parentElement.remove(e);
		
		XMLDocumentFactory.instance.saveMainDocument(document);
	}

	@Override
	public DeviceModel getDeviceByChannelIdAndDeviceSlaveId(String channelId,
			String slaveId) {
		Document document = XMLDocumentFactory.instance.getMainDocument();

		String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "='"
				+ channelId + "']/" + DeviceModel.NODE_NAME + "[@"
				+ DeviceModel.SLAVEID_ATTR + "='" + slaveId + "']";
		log.debug(path);
		Element e = (Element) document.selectSingleNode(path);
		if(e != null) {
			DeviceModel dm = new DeviceModel(e);
			return dm;
		}
		
		return null;
	}
	
}
