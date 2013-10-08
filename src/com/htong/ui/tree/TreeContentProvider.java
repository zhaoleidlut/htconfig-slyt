package com.htong.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.htong.tags.daoImpl.ChannelModelDaoImpl;
import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;

public class TreeContentProvider implements ITreeContentProvider {
	private final Logger log = Logger.getLogger(TreeContentProvider.class);

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof String) {
			String label = (String) parentElement;
			if (label.equals("标签索引配置")) {
				IndexModelDaoImpl indexModelDao = new IndexModelDaoImpl();
				List<IndexModel> indexModelList = indexModelDao
						.getAllIndexModel();
				return indexModelList.toArray();

			} else if (label.equals("标签类型配置")) {
				TagTypeModelDaoImpl tagTypeModelDao = new TagTypeModelDaoImpl();
				List<TagTypeModel> tagTypeModelList = tagTypeModelDao
						.getAllTagTypeModel();
				return tagTypeModelList.toArray();

			} else if (label.equals("规约配置")) {

			} else if (label.equals("通道配置")) {
				return new String[] { "采集通道", "转发通道", "其他通道" };

			} else if (label.equals("窗口配置")) {

			} else if (label.equals("模板配置")) {
				return new String[] { "标签索引模板", "标签类型模板" };
			} else if (label.equals("采集通道")) {
				ChannelModelDaoImpl channelModelDao = new ChannelModelDaoImpl();
				List<ChannelModel> channelModeList = channelModelDao
						.getAllChannelModel();
				return channelModeList.toArray();
			}
		} else if (parentElement instanceof IndexModel) {
			Document document = XMLDocumentFactory.instance.getMainDocument();
			IndexModel indexModel = (IndexModel) parentElement;
			Element element = (Element) document.selectSingleNode(GetXpathUtil.getIndexXPathByName(indexModel));
			List<IndexNodeModel> indexModelList = new ArrayList<IndexNodeModel>();
			if (element.element(IndexNodeModel.NODE_NAME) != null) {
				for (Iterator i = element
						.elementIterator(IndexNodeModel.NODE_NAME); i.hasNext();) {
					Element e = (Element) i.next();
					IndexNodeModel im = new IndexNodeModel(e);
					im.setParentObject(parentElement);
					indexModelList.add(im);
				}
			}
			return indexModelList.toArray();
		} else if (parentElement instanceof IndexNodeModel) {
			Document document = XMLDocumentFactory.instance.getMainDocument();
			IndexNodeModel indexNode = (IndexNodeModel) parentElement;

			String path = GetXpathUtil.getIndexNodeXPathByName(indexNode);
			log.debug(path);
			Element element = (Element) document.selectSingleNode(path);
			List<IndexNodeModel> indexNodeModelList = new ArrayList<IndexNodeModel>();

			if (element.element(IndexNodeModel.NODE_NAME) != null) {
				for (Iterator i = element
						.elementIterator(IndexNodeModel.NODE_NAME); i.hasNext();) {
					Element el = (Element) i.next();
					IndexNodeModel im = new IndexNodeModel(el);
					im.setParentObject(parentElement);
					indexNodeModelList.add(im);
				}
			}
			return indexNodeModelList.toArray();
		} else if (parentElement instanceof TagTypeModel) {
			TagTypeModel tagType = (TagTypeModel) parentElement;
			
			Element element = (Element) XMLDocumentFactory.instance.getMainDocument().selectSingleNode(GetXpathUtil.getTagTypeXPathByName(tagType));
			List<TagTypeModel> tagTypeList = new ArrayList<TagTypeModel>();
			if (element != null) {
				for (Iterator i = element
						.elementIterator(TagTypeModel.NODE_NAME); i.hasNext();) {
					Element e = (Element) i.next();
					TagTypeModel tt = new TagTypeModel(e);
					tt.setParentObject(parentElement);
					tagTypeList.add(tt);
				}
			}
			return tagTypeList.toArray();
		} else if (parentElement instanceof ChannelModel) {
			ChannelModel cm = (ChannelModel) parentElement;
			DeviceModelDaoImpl deviceModelDao = new DeviceModelDaoImpl();
			List<DeviceModel> deviceModelList = deviceModelDao
					.getDeviceModelsByChannelId(cm.getId());
			for(DeviceModel deviceModel : deviceModelList) {
				deviceModel.setParentObject(parentElement);
			}
			return deviceModelList.toArray();
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Enum) {
			return ((RootTreeModel) inputElement).getRoottree();
		}
		return null;
	}

	@Override
	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		Object[] children = this.getChildren(arg0);
		if (children == null) {
			return false;
		} else if (children.length > 0) {
			return true;
		} else {
			return false;
		}
	}

}
