package com.htong.tags.dao;

import java.util.List;

import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;

/**
 * 设备模型
 * @author 赵磊
 *
 */
public interface DeviceModelDao {
	/**
	 * 获得所有设备
	 * @return
	 */
	public List<DeviceModel> getDeviceModelsByChannelId(String channelId);
	
	/**
	 * 增加设备
	 * @param deviceModel
	 */
	public void addDeviceModel(DeviceModel deviceModel);
	
	/**
	 * 更新设备
	 * @param deviceModel
	 */
	public void updateDeviceModelBySlaveId(DeviceModel deviceModel, String oldSlaveId);
	
	
	/**
	 * 移除设备
	 * @param channelModel
	 */
	public void removeDeviceModelById(DeviceModel deviceModel);
	
	/**
	 * 通过通道号和设备地址获得设备
	 * @param channelId
	 * @param slaveId
	 * @return
	 */
	public DeviceModel getDeviceByChannelIdAndDeviceSlaveId(String channelId, String slaveId);
}
