package com.htong.tags.model;

import org.dom4j.Element;

/**
 * 采集通道模型
 * 
 * @author 赵磊
 * 
 */
public class ChannelModel {
	public static final String NODE_NAME = "channel"; // 节点名字

	public static final String ID_ATTR = "id";
	public static final String NAME_ATTR = "name";
	public static final String PROTOCAL_ATTR = "protocal"; // 协议属性
	public static final String PHYSICSPORT_ATTR = "physics-port";	//串口、tcp口
	
	public static final String COMPORT_ATTR = "com-port";	//串口名字属性
	public static final String BAUD_ATTR = "baud";	//波特率属性
	public static final String CHECK_ATTR = "parity";	//校验位属性
	public static final String DATABIT_ATTR = "data-bit";	//数据位属性
	public static final String STOPBIT_ATTR = "stop-bit";	//停止位属性
	
	public static final String TCPPORT_ATTR = "tcp-port";	//tcp口属性
	public static final String IP_ATTR = "ip";		//ip属性

	public static final String INTERVAL_ATTR = "interval";	//采样间隔属性
	public static final String OFFLINE_ATTR = "offline";	//通讯离线
	public static final String LOOP_INTERVAL_ATTR = "loop-interval";	//循环间隔
	
	public static final String DTU_ID_ATTR = "dtu-id";
	public static final String HEART_BEAT_ATTR = "heart-beat";
	public static final String DTU_PORT_ATTR = "dtu-port";
	
	public static final String INSTALL_POSITION_ATTR = "fix-position";	//安装位置
	
	public ChannelModel() {
		
	}
	
	private Object parentObject;	//上级对象，用于树的相关操作   （新建时用）

	public ChannelModel(Element e) {
		this.element = e;
		
		this.id = element.attributeValue(ID_ATTR);
		this.name = element.attributeValue(NAME_ATTR);
		this.protocal = element.attributeValue(PROTOCAL_ATTR);
		
		this.comPort = element.attributeValue(COMPORT_ATTR);
		this.baud = element.attributeValue(BAUD_ATTR);
		this.check = element.attributeValue(CHECK_ATTR);
		this.dataBit = element.attributeValue(DATABIT_ATTR);
		this.stopBit = element.attributeValue(STOPBIT_ATTR);
		
		this.tcpPort = element.attributeValue(TCPPORT_ATTR);
		this.ip = element.attributeValue(IP_ATTR);
		
		this.interval = element.attributeValue(INTERVAL_ATTR);
		this.offline = element.attributeValue(OFFLINE_ATTR);
		this.loopInterval = element.attributeValue(LOOP_INTERVAL_ATTR);
		
		this.dtuId = element.attributeValue(DTU_ID_ATTR);
		this.heartBeat = element.attributeValue(HEART_BEAT_ATTR);
		this.dtuPort = element.attributeValue(DTU_PORT_ATTR);
		
		this.fixPositon = element.attributeValue(INSTALL_POSITION_ATTR);
	}
	private Element element;
	
	private String id;	//id
	
	private String name;	//采集通道名字

	private String protocal; // 通讯协议
	
	private String comPort;	//串口名
	private String baud;	//波特率
	private String check;	//校验
	private String dataBit;	//数据位
	private String stopBit;	//停止位
	
	
	private String tcpPort;	//tcp端口
	private String ip;		//ip

	private String interval;	//采样间隔
	private String offline;		//通讯离线
	private String loopInterval;	//循环间隔
	
	private String dtuId;
	private String heartBeat;
	private String dtuPort;
	
	private String fixPositon;	//安装位置
	
	

	public String getDtuId() {
		return dtuId;
	}
	public void setDtuId(String dtuId) {
		this.dtuId = dtuId;
	}
	public String getHeartBeat() {
		return heartBeat;
	}
	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
	}
	public String getDtuPort() {
		return dtuPort;
	}
	public void setDtuPort(String dtuPort) {
		this.dtuPort = dtuPort;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtocal() {
		return protocal;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public String getComPort() {
		return comPort;
	}
	public void setComPort(String comPort) {
		this.comPort = comPort;
	}
	public String getBaud() {
		return baud;
	}
	public void setBaud(String baud) {
		this.baud = baud;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getDataBit() {
		return dataBit;
	}
	public void setDataBit(String dataBit) {
		this.dataBit = dataBit;
	}
	public String getStopBit() {
		return stopBit;
	}
	public void setStopBit(String stopBit) {
		this.stopBit = stopBit;
	}
	public String getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getOffline() {
		return offline;
	}
	public void setOffline(String offline) {
		this.offline = offline;
	}
	
	public Object getParentObject() {
		return parentObject;
	}
	public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public String getLoopInterval() {
		return loopInterval;
	}
	public void setLoopInterval(String loopInterval) {
		this.loopInterval = loopInterval;
	}
	public String getFixPositon() {
		return fixPositon;
	}
	public void setFixPositon(String fixPositon) {
		this.fixPositon = fixPositon;
	}
	
}
