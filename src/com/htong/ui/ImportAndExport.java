package com.htong.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.IOInfoModel;
import com.htong.tags.windows.ImportCJQIOSettinsWindow;
import com.htong.tags.windows.ImportDeviceIOSettinsWindow;

/**
 * 导入导出变量词典
 * 
 * @author 赵磊
 * 
 */

public class ImportAndExport {
	private static final Logger log = Logger.getLogger(ImportAndExport.class);

	/**
	 * 导出变量词典
	 * 
	 * @param circuit
	 * @throws IOException
	 * @throws CloneNotSupportedException
	 */
	public void exportVariableDic(Element element, String fileName) {

		// 清除io信息
		Element tagsElement = ((Element)element.clone()).element("tags");
		if (tagsElement != null) {
			for (Iterator<Element> i = tagsElement.elementIterator("tag"); i
					.hasNext();) {
				Element tagElement = i.next();
				if (tagElement.element(IOInfoModel.NODE_NAME) != null) {
					Element ioElement = tagElement
							.element(IOInfoModel.NODE_NAME);
					ioElement.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, null);
					ioElement.addAttribute(IOInfoModel.DEVICE_ID_ATTR, null);
				}
			}
		}

		OutputFormat former = OutputFormat.createPrettyPrint();// 设置格式化输出器
		former.setEncoding("UTF-8");

		try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"), former);
			writer.write(element);
			writer.close();

			MessageDialog.openInformation(Display.getDefault().getActiveShell()
					.getShell(), "提示", "成功导出变量词典【" + fileName + "】");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导入变量词典
	 * 
	 * @param circuit
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Element> importVariableDic(String fileName) {
		List<Element> elementList = new ArrayList<Element>();

		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(new File(fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			return null;
		} else {
			Element rootElement = doc.getRootElement();
			Element tagsElement = rootElement.element("tags");
			if (tagsElement == null) {
				return null;
			} else {
				for (Iterator<Element> i = tagsElement.elementIterator("tag"); i
						.hasNext();) {
					elementList.add(i.next());
				}
			}
		}

		return elementList;

	}

	/**
	 * 导出设备
	 * 
	 * @param deviceName
	 * @param fileName
	 */
	public void exportDevice(DeviceModel device) {
		File filePath = new File("设备库/");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		FileDialog fileDialog = new FileDialog(Display.getDefault()
				.getActiveShell().getShell(), SWT.SAVE);
		fileDialog.setFilterPath("设备库/");
		fileDialog.setFilterExtensions(new String[] { "*.xml" });
		fileDialog.setFilterNames(new String[] { "设备文件（*.xml）" });
		fileDialog.setOverwrite(true);

		fileDialog.setFileName(device.getName() + ".xml");

		String fileName = fileDialog.open();
		if (fileName == null) {
			return;
		}

		int index = fileName.lastIndexOf(".");
		if (index == -1) {
			fileName += ".xml";
		}

		// 写入根元素
		OutputStreamWriter write1;
		try {
			write1 = new OutputStreamWriter(new FileOutputStream(fileName),
					"utf-8");
			BufferedWriter bw = new BufferedWriter(write1);
			bw.write("<device-io/>\r\n\t");
			bw.close();
			write1.close();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Document document = XMLDocumentFactory.instance.getMainDocument();

		ChannelModel channel = (ChannelModel) device.getParentObject();

		String xpath = "/root/channels/channel[@name='" + channel.getName()
				+ "']/device[@name='" + device.getName() + "']";
		Element deviceElement = (Element) document.selectSingleNode(xpath);

		String xpathTag = "/root/tagindex/index//tagioinfo[@channel-id='"
				+ channel.getId() + "' and @device-id='" + device.getSlaveId()
				+ "']";
		List<Element> elementList = document.selectNodes(xpathTag);

		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(new File(fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element rooteElement = doc.getRootElement();
		deviceElement.setParent(null);
		rooteElement.add(deviceElement);
		if (elementList != null) {
			for (Element e : elementList) {
				e.getParent().setParent(null);
				e.addAttribute(IOInfoModel.CHANNEL_ID_ATTR, null);
				e.addAttribute(IOInfoModel.DEVICE_ID_ATTR, null);

				rooteElement.add(e.getParent());
			}
		}

		OutputFormat former = OutputFormat.createPrettyPrint();// 设置格式化输出器
		former.setEncoding("UTF-8");

		try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"), former);
			writer.write(doc);

			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导入设备
	 * 
	 * @param channel
	 */
	public void importDevice(final ChannelModel channel) {
		final DeviceModelDaoImpl deviceDao = new DeviceModelDaoImpl();

		IInputValidator vlidator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				try {
					int slaveId = Integer.parseInt(newText);
					if (slaveId < 1 || slaveId > 247) {
						return "请输入1-247之间的整数。";
					}
					DeviceModel device = deviceDao
							.getDeviceByChannelIdAndDeviceSlaveId(
									channel.getId(), String.valueOf(slaveId));
					if (device != null) {
						return "设备地址【" + slaveId + "】已被占用，请重新输入。";
					}
				} catch (NumberFormatException e) {
					return "请输入1-247之间的整数。";
				}
				return null;
			}
		};
		InputDialog idlg = new InputDialog(null, "提示", "请输入新的设备地址", "",
				vlidator);

		if (idlg.open() == Window.OK) {
			int slaveId = Integer.parseInt(idlg.getValue());

			File filePath = new File("设备库/");
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			FileDialog fileDialog = new FileDialog(Display.getDefault()
					.getActiveShell().getShell(), SWT.OPEN);
			fileDialog.setFilterPath("设备库/");
			fileDialog.setFilterExtensions(new String[] { "*.xml" });
			fileDialog.setFilterNames(new String[] { "设备库（*.xml）" });

			String fileName = fileDialog.open();
			if (fileName == null || fileName.equals("")) {
				return;
			}
			if (!fileName.contains(".")) {
				fileName += ".xml";
			}

			File file = new File(fileName);
			if (!file.exists()) {
				MessageDialog.openError(null, "错误", "找不到指定文件！");
				return;
			}

			SAXReader reader = new SAXReader();
			Document doc = null;
			try {
				log.debug(fileName);
				doc = reader.read(new File(fileName));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			if (doc == null) {
				return;
			} else {
				Element deviceElement = (Element) doc
						.selectSingleNode("/device-io/device");
				if (deviceElement == null) {
					return;
				} else {
					Document document = XMLDocumentFactory.instance
							.getMainDocument();
					String channelXpath = "/root/channels/channel[@id='"
							+ channel.getId() + "']";
					Element channelElement = (Element) document
							.selectSingleNode(channelXpath);
					deviceElement.addAttribute(DeviceModel.SLAVEID_ATTR,
							String.valueOf(slaveId)); // 更改设备地址
					deviceElement.addAttribute(DeviceModel.NAME_ATTR, "导入的设备"); // 更改设备名字
					deviceElement.setParent(null);
					channelElement.add(deviceElement);

					DeviceModel d = new DeviceModel(deviceElement);
					d.setParentObject(channel);

					MainUI.treeViewer.add(channel, d);

					XMLDocumentFactory.instance.saveMainDocument(document);
				}

				// 处理变量
				List<Element> tagElementList = doc
						.selectNodes("/device-io/tag");
				if (tagElementList != null) {
					ImportDeviceIOSettinsWindow id = new ImportDeviceIOSettinsWindow(
							Display.getDefault().getActiveShell().getShell(),
							tagElementList, channel.getId(), idlg.getValue());
					id.open();
				}
			}
		}
	}

	/**
	 * 导入设备IO信息
	 * 
	 * @param device
	 */
	public void importDeviceIOInfo(final DeviceModel device) {
		String slaveId = device.getSlaveId();
		ChannelModel channel = (ChannelModel) device.getParentObject();
		File filePath = new File("设备库/");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		FileDialog fileDialog = new FileDialog(Display.getDefault()
				.getActiveShell().getShell(), SWT.OPEN);
		fileDialog.setFilterPath("设备库/");
		fileDialog.setFilterExtensions(new String[] { "*.xml" });
		fileDialog.setFilterNames(new String[] { "设备库（*.xml）" });

		String fileName = fileDialog.open();
		if (fileName == null || fileName.equals("")) {
			return;
		}
		if (!fileName.contains(".")) {
			fileName += ".xml";
		}

		File file = new File(fileName);
		if (!file.exists()) {
			MessageDialog.openError(null, "错误", "找不到指定文件！");
			return;
		}

		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			log.debug(fileName);
			doc = reader.read(new File(fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			return;
		} else {

			// 处理变量
			List<Element> tagElementList = doc.selectNodes("/device-io/tag");
			if (tagElementList != null) {
				ImportDeviceIOSettinsWindow id = new ImportDeviceIOSettinsWindow(
						Display.getDefault().getActiveShell().getShell(),
						tagElementList, channel.getId(), slaveId);
				id.open();
			}
		}
	}

	/**
	 * 导入采集器
	 * 
	 * @param channel
	 */
	public void importCJQ(final ChannelModel channel) {
		final DeviceModelDaoImpl deviceDao = new DeviceModelDaoImpl();

		IInputValidator vlidator = new IInputValidator() {

			@Override
			public String isValid(String newText) {
				try {
					int slaveId = Integer.parseInt(newText);
					if (slaveId < 1 || slaveId > 247) {
						return "请输入1-247之间的整数。";
					}
					DeviceModel device = deviceDao
							.getDeviceByChannelIdAndDeviceSlaveId(
									channel.getId(), String.valueOf(slaveId));
					if (device != null) {
						return "设备地址【" + slaveId + "】已被占用，请重新输入。";
					}
				} catch (NumberFormatException e) {
					return "请输入1-247之间的整数。";
				}
				return null;
			}
		};
		InputDialog idlg = new InputDialog(null, "提示", "请输入新的设备地址", "",
				vlidator);

		if (idlg.open() == Window.OK) {
			int slaveId = Integer.parseInt(idlg.getValue());

			File filePath = new File("采集器库/");
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			FileDialog fileDialog = new FileDialog(Display.getDefault()
					.getActiveShell().getShell(), SWT.OPEN);
			fileDialog.setFilterPath("采集器库/");
			fileDialog.setFilterExtensions(new String[] { "*.xml" });
			fileDialog.setFilterNames(new String[] { "采集器库（*.xml）" });

			String fileName = fileDialog.open();
			if (fileName == null || fileName.equals("")) {
				return;
			}
			if (!fileName.contains(".")) {
				fileName += ".xml";
			}

			File file = new File(fileName);
			if (!file.exists()) {
				MessageDialog.openError(null, "错误", "找不到指定文件！");
				return;
			}

			SAXReader reader = new SAXReader();
			Document doc = null;
			try {
				log.debug(fileName);
				doc = reader.read(new File(fileName));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			if (doc == null) {
				return;
			} else {

				Document document = XMLDocumentFactory.instance
						.getMainDocument();
				String channelXpath = "/root/channels/channel[@id='"
						+ channel.getId() + "']";
				Element channelElement = (Element) document
						.selectSingleNode(channelXpath);

				Element deviceElement = channelElement
						.addElement(DeviceModel.NODE_NAME);

				deviceElement.addAttribute(DeviceModel.SLAVEID_ATTR,
						String.valueOf(slaveId)); // 设备地址
				deviceElement.addAttribute(DeviceModel.NAME_ATTR, "导入的采集器"); // 设备名字
				deviceElement.addAttribute(DeviceModel.MANUFACTURER_ATTR,
						"丹东华通测控有限公司");
				deviceElement.addAttribute(DeviceModel.RETRY_ATTR, "2");
				deviceElement.addAttribute(DeviceModel.TIMEOUT_ATTR, "500");
				deviceElement.addAttribute(DeviceModel.USED_ATTR, "true");

				DeviceModel d = new DeviceModel(deviceElement);
				d.setParentObject(channel);

				MainUI.treeViewer.add(channel, d);

				XMLDocumentFactory.instance.saveMainDocument();

				// 处理变量
				String xpath = "/root/channels/channel/channelNode[@useflag=\"启用\"]/deviceNode/viaNode";
				log.debug("采集器变量xpath:" + xpath);
				List<Element> tagElementList = doc.selectNodes(xpath);
				log.debug(tagElementList.size());

				if (tagElementList != null) {
					ImportCJQIOSettinsWindow id = new ImportCJQIOSettinsWindow(
							Display.getDefault().getActiveShell().getShell(),
							tagElementList, channel.getId(), idlg.getValue());
					id.open();
				}
			}
		}
	}

	/**
	 * 导入采集器IO信息
	 * 
	 * @param device
	 */
	public void importCJQIOInfo(final DeviceModel device) {
		String slaveId = device.getSlaveId();

		ChannelModel channel = (ChannelModel) device.getParentObject();

		File filePath = new File("采集器库/");
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		FileDialog fileDialog = new FileDialog(Display.getDefault()
				.getActiveShell().getShell(), SWT.OPEN);
		fileDialog.setFilterPath("采集器库/");
		fileDialog.setFilterExtensions(new String[] { "*.xml" });
		fileDialog.setFilterNames(new String[] { "采集器库（*.xml）" });

		String fileName = fileDialog.open();
		if (fileName == null || fileName.equals("")) {
			return;
		}
		if (!fileName.contains(".")) {
			fileName += ".xml";
		}

		File file = new File(fileName);
		if (!file.exists()) {
			MessageDialog.openError(null, "错误", "找不到指定文件！");
			return;
		}

		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			log.debug(fileName);
			doc = reader.read(new File(fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		if (doc == null) {
			return;
		} else {
			// 处理变量
			String xpath = "/root/channels/channel/channelNode[@useflag=\"启用\"]/deviceNode/viaNode";
			log.debug("采集器变量xpath:" + xpath);
			List<Element> tagElementList = doc.selectNodes(xpath);

			if (tagElementList != null) {
				ImportCJQIOSettinsWindow id = new ImportCJQIOSettinsWindow(
						Display.getDefault().getActiveShell().getShell(),
						tagElementList, channel.getId(), slaveId);
				id.open();
			}
		}

	}
}
