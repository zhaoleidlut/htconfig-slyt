package com.htong.ui.tree;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.type.IndexTypeEnum;
import com.htong.ui.GetIndexType;

public class TreeLabelProvider implements ILabelProvider {
	
	private Image labelConfig = ResourceManager.getPluginImage("htconfig-slyt", "icons/label_config.png");
	private Image tagTypeConfig = ResourceManager.getPluginImage("htconfig-slyt", "icons/tagtype.gif");
	private Image tongdaoConfig = ResourceManager.getPluginImage("htconfig-slyt", "icons/channel_config.png");
	private Image channelConfig = ResourceManager.getPluginImage("htconfig-slyt", "icons/channel.png");
	private Image deviceConfig = ResourceManager.getPluginImage("htconfig-slyt", "icons/device.png");
	private Image device_beiyong = ResourceManager.getPluginImage("htconfig-slyt", "icons/device_beiyong.png");
	
	private Image index_main = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_var.gif");
	private Image index_normal = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_normal.gif");
	private Image index_energy = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_energy.gif");
	
	private Image index_main_node = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_main_node.gif");
	private Image index_energy_node = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_energy_node.gif");
	private Image building = ResourceManager.getPluginImage("htconfig-slyt", "icons/building.gif");
	private Image index_normal_node = ResourceManager.getPluginImage("htconfig-slyt", "icons/index_normal_node.gif");
	
	private Image tagType_node = ResourceManager.getPluginImage("htconfig-slyt", "icons/tagtype_node.gif");
	
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object object) {
		if(object instanceof String) {
			String label = (String)object;
			if(label.equals(RootTreeModel.instanse.labelIndex)) {//标签索引配置
				return labelConfig;
			} else if(label.equals(RootTreeModel.instanse.labelType)) {//标签类型配置
				return tagTypeConfig;
			} else if(label.equals(RootTreeModel.instanse.channelConfig)) {//通道配置
				return tongdaoConfig;
			} else if(label.equals("采集通道")) {
				return channelConfig;
			}
		} else if(object instanceof IndexModel) {
			IndexModel indexModel = (IndexModel)object;
			if(indexModel.getType().equals("main")) {
				return index_main;
			} else if(indexModel.getType().equals("energy")) {
				return index_energy;
			} else if(indexModel.getType().equals("normal")) {
				return index_normal;
			}
			
		} else if(object instanceof IndexNodeModel) {
			IndexNodeModel inm = (IndexNodeModel)object;
			
			IndexTypeEnum indexType = GetIndexType.getIndexType(inm);
			if (indexType.equals(IndexTypeEnum.MAIN)) {
				return index_main_node;
			} else if(indexType.equals(IndexTypeEnum.ENERGY)) {
				return index_energy_node;
			} else if(indexType.equals(IndexTypeEnum.NORMAL)) {
				if(inm.getParentObject() instanceof IndexModel) {
					return building;
				} else {
					return index_normal_node;
				}
			}
		} else if(object instanceof TagTypeModel) {
			return tagType_node;
		} else if(object instanceof ChannelModel) {
			return channelConfig;
		} else if(object instanceof DeviceModel) {
			DeviceModel d = (DeviceModel)object;
			if(d.getUsed().equals("true")) {
				return deviceConfig;
			} else 
				return device_beiyong;
		}
		return null;
	}

	@Override
	public String getText(Object object) {
		if(object instanceof String) {
			return (String)object;
		} else if(object instanceof IndexModel) {
			return ((IndexModel)object).getName();
		} else if(object instanceof IndexNodeModel) {
			IndexNodeModel inm = (IndexNodeModel)object;
			IndexTypeEnum indexType = GetIndexType.getIndexType(inm);
			if (indexType.equals(IndexTypeEnum.MAIN)) {
				if(inm.getNumber() == null) {
					return inm.getName();
				} else {
					return   inm.getName() + ":"+inm.getNumber();
				}
			}
			
			return inm.getName();
		} else if(object instanceof TagTypeModel) {
			return ((TagTypeModel)object).getName();
		} else if(object instanceof ChannelModel) {
			ChannelModel c = (ChannelModel)object;
			return c.getName()+":" + c.getId();
		} else if(object instanceof DeviceModel) {
			DeviceModel d = (DeviceModel)object;
			return d.getName() + ":" + d.getSlaveId();
		}
	
		return null;
	}

}
