package com.htong.ui.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.type.IndexTypeEnum;
import com.htong.ui.GetIndexType;
import com.htong.ui.MainUI;
import com.htong.ui.PinyinComparator;

public class Sorter extends ViewerSorter {
	public int compare(Viewer viewer, Object e1, Object e2) {
		if (e1 instanceof IndexModel && e2 instanceof IndexModel) {	//变量索引排第一
			IndexModel im1 = (IndexModel) e1;
			IndexModel im2 = (IndexModel) e2;
			if (im1.getType() != null && "main".equals(im1.getType())) {
				return -1;
			} else
				return 1;
		}
		if (e1 instanceof IndexNodeModel
				&& e2 instanceof IndexNodeModel
				&& GetIndexType.getIndexType((IndexNodeModel) e1).equals(
						IndexTypeEnum.MAIN)
				&& GetIndexType.getIndexType((IndexNodeModel) e2).equals(
						IndexTypeEnum.MAIN)) {	//变量索引下按编号排序
			IndexNodeModel inm1 = (IndexNodeModel) e1;
			IndexNodeModel inm2 = (IndexNodeModel) e2;
			int i1 = inm1.getNumber() == null ? 1000000 : Integer.valueOf(inm1
					.getNumber());
			int i2 = inm2.getNumber() == null ? 1000000 : Integer.valueOf(inm2
					.getNumber());
			return i1 - i2;

		}

		if (e1 instanceof DeviceModel && e2 instanceof DeviceModel) {	//设备按地址排序
			DeviceModel d1 = (DeviceModel) e1;
			DeviceModel d2 = (DeviceModel) e2;
			int i1 = d1.getSlaveId() == null ? 1000000 : Integer.valueOf(d1
					.getSlaveId());
			int i2 = d2.getSlaveId() == null ? 1000000 : Integer.valueOf(d2
					.getSlaveId());
			return i1 - i2;
		}
		
		if(e1 instanceof ChannelModel && e2 instanceof ChannelModel) {
			ChannelModel c1 = (ChannelModel)e1;
			ChannelModel c2 = (ChannelModel)e2;
			int c11=0;
			try {
				c11 = Integer.valueOf(c1.getId());
			} catch (NumberFormatException e) {
				c11 = 100000000;
				e.printStackTrace();
			}
			int c22=0;
			try {
				c22 = Integer.valueOf(c2.getId());
			} catch (NumberFormatException e) {
				c22=100000000;
				e.printStackTrace();
			}
			
			int i1 = c1.getId()==null?100000000:c11;
			int i2 = c2.getId()==null?100000000:c22;
			
			if(MainUI.channelNameSorter==0) {//按序号排序
				if(MainUI.channelNumSorter==1 || MainUI.channelNumSorter==0) {
					return i1-i2;
				} else if(MainUI.channelNumSorter==-1) {
					return i2-i1;
				}
			} else if(MainUI.channelNumSorter==0) {//按通道名称
				if(MainUI.channelNameSorter==1 || MainUI.channelNameSorter==0) {
					return PinyinComparator.INSTANCE.compare(c1.getName(), c2.getName());
				} else if(MainUI.channelNameSorter==-1) {
					return PinyinComparator.INSTANCE.compare(c2.getName(), c1.getName());
				}
			}
		}

		return 0;
	}
}
