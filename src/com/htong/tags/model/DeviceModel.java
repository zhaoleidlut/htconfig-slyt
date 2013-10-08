package com.htong.tags.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.dom4j.Element;

/**
 * 设备
 * 
 * @author 赵磊
 */
public class DeviceModel {
	public static final String NODE_NAME = "device";	//节点名字
	
	public static final String NAME_ATTR = "name";		//名字属性
	public static final String MANUFACTURER_ATTR = "manufacturer";		//生产厂家属性
	public static final String TYPE_ATTR = "type";		//型号属性
	public static final String INSTALLTIME_ATTR = "install-time";		//安装日期属性
	public static final String TIMEOUT_ATTR = "timeout";		//超时时间属性
	public static final String RETRY_ATTR = "retry";		//重发次数属性
	public static final String SLAVEID_ATTR = "slave-id";		//设备地址属性
	public static final String REMARK_ATTR = "remark";		//备注属性
	public static final String USED_ATTR = "used";		//使用属性
	
	public static final String PT_ATTR = "pt";
	public static final String CT_ATTR = "ct";
	
	public static final String INSTALL_POSITION_ATTR = "fix-position";
	
	private String name; // 设备名称
	private String manufacturer; // 生产厂家
	private String type; // 型号
	private Calendar installTime; // 安装日期
	private String timeout; // 超时（毫秒）
	private String retry; // 重发次数
	private String slaveId; // 设备地址
	private String remark; // 备注
	private String used;	// 备用设备
	
	private String pt;
	private String ct;
	private String fixPosition;	//安装位置
	
	private Element element;
	
	private Object parentObject;	//上级对象，新建时用到
	
	public DeviceModel(Element e) {
		this.element = e;
		
		this.name = e.attributeValue(NAME_ATTR);
		this.manufacturer = e.attributeValue(MANUFACTURER_ATTR);
		this.type = e.attributeValue(TYPE_ATTR);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(e.attributeValue(INSTALLTIME_ATTR) != null) {
			this.installTime = Calendar.getInstance();
			try {
				this.installTime.setTime(sdf.parse(e.attributeValue(INSTALLTIME_ATTR)));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		this.timeout = e.attributeValue(TIMEOUT_ATTR);
		this.retry = e.attributeValue(RETRY_ATTR);
		this.slaveId = e.attributeValue(SLAVEID_ATTR);
		this.remark = e.attributeValue(REMARK_ATTR);
		this.used = e.attributeValue(USED_ATTR);
		
		this.pt = e.attributeValue(PT_ATTR);
		this.ct = e.attributeValue(CT_ATTR);
		this.fixPosition = e.attributeValue(INSTALL_POSITION_ATTR);
	}
	
	public DeviceModel() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Calendar getInstallTime() {
		return installTime;
	}
	public void setInstallTime(Calendar installTime) {
		this.installTime = installTime;
	}
	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getRetry() {
		return retry;
	}

	public void setRetry(String retry) {
		this.retry = retry;
	}

	public String getSlaveId() {
		return slaveId;
	}

	public void setSlaveId(String slaveId) {
		this.slaveId = slaveId;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element e) {
		this.element = e;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Object getParentObject() {
		return parentObject;
	}

	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getFixPosition() {
		return fixPosition;
	}

	public void setFixPosition(String fixPosition) {
		this.fixPosition = fixPosition;
	}
	
	
	
}
