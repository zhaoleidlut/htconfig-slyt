package com.htong.tags.composites;

import java.nio.channels.Channel;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.ChannelModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.windows.EditTagMainIndexWindow;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;

/**
 * 采集通道编辑面板
 * 
 * @author 赵磊
 * 
 */
public class ChannelEditComposite extends Composite {
	private static Logger log = Logger.getLogger(ChannelEditComposite.class);

	private final String parentTreeNodeName = "采集通道";
	private ChannelModel channel;
	
	private ChannelModelDaoImpl channelModelDao = new ChannelModelDaoImpl();

	public static final String[] commRuleShowArray = { "标准Modbus RTU（串口）", "Modbus-RTU（从模式）","标准Modbus TCP（主模式）","Modbus TCP（从模式）",
											"标准Modbus RTU（DTU）","Modbus（DTU从模式）","DL645电能表规约（DTU）","DL645电能表规约（串口）","DL645非标流量监测仪规约" }; // 通讯规约显示值
	public static final String[] commRuleValueArray = { "modbus_rtu_master","modbus_rtu_slaver",
			"modbus_tcp_master",  "modbus_tcp_slaver", "modbus_dtu_master",  "modbus_dtu_slaver", "dl645_dtu","dl645_rtu","dl645_dtu_pdm800t"}; // 通讯规约实际值

	private String[] comNameArray = new String[100];

	private String[] baudArray = { "", "2400", "4800", "9600", "19200",
			"38400", "57600", "115200", "230400", "460800", "921600" };

	private String[] dataBitArray = { "", "5", "6", "7", "8" };

	private String[] checkArray = { "", "无校验", "奇校验", "偶校验" };
	private String[] checkValueArray = { "", "none", "odd", "even" };

	private String[] stopBitArray = { "", "1位", "1.5位", "2位" };
	private String[] stopBitValueArray = { "", "1", "1.5", "2" };

	private Text channelName;
	private Text commOffText;
	private Text ipAddress;
	private Text port;
	private Combo channelRule;
	private Combo comName;
	private Combo baud;
	private Combo dataBit;
	private Combo check;
	private Combo stopBit;
	private Composite composite_net;
	private Composite composite_serial;

	private StackLayout sl_physicsInfoGroup;
	private Group physicsInfoGroup;
	private Text intervalText;
	private Text text_id;
	private Text dtu_id;
	private Text dtu_port;
	private Text heart_beat;
	private Composite composite_dtu;
	private Text loopIntervalText;
	private Text text_install_position;

	public ChannelEditComposite(final Composite parent, int style,
			final ChannelModel channelModel) {
		super(parent, style);

		this.channel = channelModel;

		// 列出系统可用串口
		comNameArray[0] = "";
		for (int i = 1; i < 100; i++) {
			comNameArray[i] = "COM" + i;
		}
		setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(this, SWT.NONE);
		RowLayout rl_composite_2 = new RowLayout(SWT.VERTICAL);
		rl_composite_2.wrap = false;
		composite_2.setLayout(rl_composite_2);

		Group group = new Group(composite_2, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		group.setLayoutData(new RowData(300, 130));
		group.setText("基本信息");

		Label label = new Label(group, SWT.NONE);
		label.setText("通道名称：");

		channelName = new Text(group, SWT.BORDER);
		GridData gd_channelName = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1);
		gd_channelName.widthHint = 110;
		channelName.setLayoutData(gd_channelName);

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("通信规约：");

		channelRule = new Combo(group, SWT.READ_ONLY);
		GridData gd_channelRule = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 3, 1);
		gd_channelRule.widthHint = 140;
		channelRule.setLayoutData(gd_channelRule);

		Label label_2 = new Label(group, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label_2.setText("序号：");

		text_id = new Text(group, SWT.BORDER);
		GridData gd_text_id = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_text_id.widthHint = 80;
		text_id.setLayoutData(gd_text_id);

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setVisible(false);
		lblNewLabel_1.setText("格式如:1xxx");
		new Label(group, SWT.NONE);
		
		Label label_13 = new Label(group, SWT.NONE);
		label_13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_13.setText("安装位置：");
		
		text_install_position = new Text(group, SWT.BORDER);
		GridData gd_text_install_position = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_text_install_position.widthHint = 155;
		text_install_position.setLayoutData(gd_text_install_position);
		
		Button button = new Button(group, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new EditTagMainIndexWindow(channelModel, text_install_position).open();
			}
		});
		button.setText(" 选择 ");
		new Label(group, SWT.NONE);
		channelRule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 初始化STACK
				int index = channelRule.getSelectionIndex();
				if (index == -1 || index >= commRuleValueArray.length)
					return;
				if (index == 0 || index == 1 || index == 7) {
					sl_physicsInfoGroup.topControl = composite_serial;
				} else if (index == 2 || index == 3) {
					sl_physicsInfoGroup.topControl = composite_net;
				} else if(index == 4 || index == 5 || index == 6 || index == 8) {
					sl_physicsInfoGroup.topControl = composite_dtu;
				}

				physicsInfoGroup.layout();

			}
		});

		Group group_1 = new Group(composite_2, SWT.NONE);
		group_1.setLayout(new GridLayout(3, false));
		group_1.setLayoutData(new RowData(300, 90));
		group_1.setText("通讯信息");

		Label label_4 = new Label(group_1, SWT.NONE);
		label_4.setText("通讯离线：");

		commOffText = new Text(group_1, SWT.BORDER);
		GridData gd_commOffText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_commOffText.widthHint = 80;
		commOffText.setLayoutData(gd_commOffText);
		new Label(group_1, SWT.NONE);

		Label lblNewLabel = new Label(group_1, SWT.NONE);
		lblNewLabel.setText("采样间隔：");

		intervalText = new Text(group_1, SWT.BORDER);
		GridData gd_intervalText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_intervalText.widthHint = 80;
		intervalText.setLayoutData(gd_intervalText);
		
		Label lblMs = new Label(group_1, SWT.NONE);
		lblMs.setText("ms");
		
		Label label_12 = new Label(group_1, SWT.NONE);
		label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_12.setText("循环间隔：");
		
		loopIntervalText = new Text(group_1, SWT.BORDER);
		GridData gd_loopIntervalText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_loopIntervalText.widthHint = 80;
		loopIntervalText.setLayoutData(gd_loopIntervalText);
		
		Label lblS = new Label(group_1, SWT.NONE);
		lblS.setText("s");

		physicsInfoGroup = new Group(composite_2, SWT.NONE);
		sl_physicsInfoGroup = new StackLayout();
		physicsInfoGroup.setLayout(sl_physicsInfoGroup);
		physicsInfoGroup.setLayoutData(new RowData(300, 172));
		physicsInfoGroup.setText("物理信息");

		composite_serial = new Composite(physicsInfoGroup, SWT.NONE);
		sl_physicsInfoGroup.topControl = composite_serial;
		composite_serial.setLayout(new GridLayout(2, false));

		Label label_6 = new Label(composite_serial, SWT.NONE);
		label_6.setText("端　口：");
				
						comName = new Combo(composite_serial, SWT.READ_ONLY);
						GridData gd_comName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_comName.widthHint = 60;
						comName.setLayoutData(gd_comName);
		
				Label label_7 = new Label(composite_serial, SWT.NONE);
				label_7.setText("波特率：");

		baud = new Combo(composite_serial, SWT.READ_ONLY);
		GridData gd_baud = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_baud.widthHint = 60;
		baud.setLayoutData(gd_baud);
		
				Label label_8 = new Label(composite_serial, SWT.NONE);
				label_8.setText("数据位：");

		dataBit = new Combo(composite_serial, SWT.READ_ONLY);
		GridData gd_dataBit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dataBit.widthHint = 60;
		dataBit.setLayoutData(gd_dataBit);
		
				Label label_9 = new Label(composite_serial, SWT.NONE);
				label_9.setText("校验位：");

		check = new Combo(composite_serial, SWT.READ_ONLY);
		GridData gd_check = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_check.widthHint = 60;
		check.setLayoutData(gd_check);
		
				Label label_10 = new Label(composite_serial, SWT.NONE);
				label_10.setText("停止位：");

		stopBit = new Combo(composite_serial, SWT.READ_ONLY);
		GridData gd_stopBit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_stopBit.widthHint = 60;
		stopBit.setLayoutData(gd_stopBit);
		
		composite_dtu = new Composite(physicsInfoGroup, SWT.NONE);
		composite_dtu.setLayout(new GridLayout(2, false));
		
		Label lblDtuid = new Label(composite_dtu, SWT.NONE);
		lblDtuid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDtuid.setText("DTU-ID：");
		
		dtu_id = new Text(composite_dtu, SWT.BORDER);
		GridData gd_dtu_id = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dtu_id.widthHint = 80;
		dtu_id.setLayoutData(gd_dtu_id);
		
		Label label_11 = new Label(composite_dtu, SWT.NONE);
		label_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_11.setText("端　口：");
		
		dtu_port = new Text(composite_dtu, SWT.BORDER);
		GridData gd_dtu_port = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dtu_port.widthHint = 80;
		dtu_port.setLayoutData(gd_dtu_port);
		
		Label label_3 = new Label(composite_dtu, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("心跳信号：");
		
		heart_beat = new Text(composite_dtu, SWT.BORDER);
		GridData gd_heart_beat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_heart_beat.widthHint = 80;
		heart_beat.setLayoutData(gd_heart_beat);

		composite_net = new Composite(physicsInfoGroup, SWT.NONE);
		composite_net.setLayout(null);

		ipAddress = new Text(composite_net, SWT.BORDER);
		ipAddress.setBounds(73, 14, 150, 18);

		Label lblIp = new Label(composite_net, SWT.NONE);
		lblIp.setBounds(10, 13, 54, 28);
		lblIp.setText("IP地址：");

		port = new Text(composite_net, SWT.BORDER);
		port.setBounds(73, 46, 60, 18);

		Label label_5 = new Label(composite_net, SWT.NONE);
		label_5.setBounds(10, 47, 54, 25);
		label_5.setText("端　口：");
						
								Composite composite_1 = new Composite(this, SWT.NONE);
										composite_1.setLayout(null);
								
										Button btns = new Button(composite_1, SWT.NONE);
										btns.setBounds(74, 5, 55, 27);
										btns.addSelectionListener(new SelectionAdapter() {
											@Override
											public void widgetSelected(SelectionEvent e) {
												if (channel.getElement() == null) {// 新建
													if(saveInfo(1)) {
														MainUI.treeViewer.add(channel.getParentObject(), channel);
														MainUI.treeViewer.setExpandedState(channel.getParentObject(), true);
														
														((OperatingComposite)parent).setTop("采集通道已保存！");
													}
												} else {
													if(saveInfo(0)) {
														MainUI.treeViewer.update(channel, null);
														
														((OperatingComposite)parent).setTop("采集通道已更新！");
													}
												}
												
												
											}
										});
										btns.setText("保 存(&S)");
										
												Button btnc = new Button(composite_1, SWT.NONE);
												btnc.setBounds(180, 5, 56, 27);
												btnc.addSelectionListener(new SelectionAdapter() {
													@Override
													public void widgetSelected(SelectionEvent e) {
														((OperatingComposite)parent).setTop("已取消操作！");
													}
												});
												btnc.setText("取 消(&C)");

		initControlValue(); // 初始化值

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * 初始化控件值
	 */
	private void initControlValue() {
		// 初始化通道名字、id
		this.channelName.setText(channel.getName() == null ? "" : channel
				.getName());

		this.text_id.setText(channel.getId() == null ? "" : channel.getId());
		this.text_install_position.setText(channel.getFixPositon()== null?"":channel.getFixPositon());

		// 初始化通讯信息
		this.commOffText.setText(this.channel.getOffline() == null ? "5"
				: channel.getOffline());
		this.intervalText.setText(this.channel.getInterval() == null ? "100"
				: channel.getInterval());
		
		this.loopIntervalText.setText(this.channel.getLoopInterval() == null?"":channel.getLoopInterval());

		// 初始化规约
		this.channelRule.setItems(commRuleShowArray);
		this.channelRule.select(0);

		// 初始化串口名字、波特率、数据位、校验、停止位
		this.comName.setItems(this.comNameArray);
		this.baud.setItems(this.baudArray);
		this.baud.select(3);
		this.dataBit.setItems(this.dataBitArray);
		this.dataBit.select(4);
		this.check.setItems(this.checkArray);
		this.check.select(1);
		this.stopBit.setItems(this.stopBitArray);
		this.stopBit.select(1);

		try {
			// 初始化通道规约
			if (this.channel.getProtocal() != null) {
				String commRuleStr = this.channel.getProtocal();
				int ii;
				for (ii = 0; ii < commRuleValueArray.length;) {
					if (commRuleStr.equals(commRuleValueArray[ii])) {
						break;
					}
					ii++;
				}
				this.channelRule.select(ii);

				// 初始化STACK
				if (this.channel.getProtocal().equals(commRuleValueArray[0]) || this.channel.getProtocal().equals(commRuleValueArray[1]) || this.channel.getProtocal().equals(commRuleValueArray[7]))
					sl_physicsInfoGroup.topControl = composite_serial;
				else if (this.channel.getProtocal().equals(
						commRuleValueArray[2]) || this.channel.getProtocal().equals(commRuleValueArray[3]))
					sl_physicsInfoGroup.topControl = composite_net;
				else if(this.channel.getProtocal().equals(
						commRuleValueArray[4]) || this.channel.getProtocal().equals(commRuleValueArray[5]) || this.channel.getProtocal().equals(
								commRuleValueArray[6]) || this.channel.getProtocal().equals(
										commRuleValueArray[8])) {
					sl_physicsInfoGroup.topControl = composite_dtu;
				}
				
				physicsInfoGroup.layout();
			}

			// 初始化串口名字
			if (this.channel.getComPort() != null) {
				String comNameStr = this.channel.getComPort();
				for (int i = 0; i < this.comNameArray.length; i++) {
					if (comNameStr.equals(this.comNameArray[i])) {
						this.comName.select(i);
						break;
					}
				}
			}

			// 初始化波特率
			if (this.channel.getBaud() != null) {
				String baudStr = this.channel.getBaud();
				for (int i = 0; i < this.baudArray.length; i++) {
					if (baudStr.equals(this.baudArray[i])) {
						this.baud.select(i);
						break;
					}
				}
			}

			// 初始化数据位
			if (this.channel.getDataBit() != null) {
				String dataBitStr = this.channel.getDataBit();
				for (int i = 0; i < this.dataBitArray.length; i++) {
					if (dataBitStr.equals(this.dataBitArray[i])) {
						this.dataBit.select(i);
						break;
					}
				}
			}

			// 初始化校验位
			if (this.channel.getCheck() != null) {
				String checkStr = this.channel.getCheck();
				for (int i = 0; i < this.checkValueArray.length; i++) {
					if (checkStr.equals(this.checkValueArray[i])) {
						this.check.select(i);
						break;
					}
				}
			}

			// 初始化停止位
			if (this.channel.getStopBit() != null) {
				String stopBitStr = this.channel.getStopBit();
				for (int i = 0; i < this.stopBitValueArray.length; i++) {
					if (stopBitStr.equals(this.stopBitValueArray[i])) {
						this.stopBit.select(i);
						break;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始化Ip、端口
		this.ipAddress.setText(this.channel.getIp() == null ? "" : this.channel
				.getIp());
		this.port.setText(this.channel.getTcpPort() == null ? "" : this.channel
				.getTcpPort());
		
		this.dtu_id.setText(this.channel.getDtuId()==null?"":this.channel.getDtuId());
		this.heart_beat.setText("hello");
		this.dtu_port.setText(this.channel.getDtuPort()==null?"":this.channel.getDtuPort());

	}

	/**
	 * 保存信息
	 * @param type 1为新建 0为修改
	 * @return
	 */
	private boolean saveInfo(int type) {
		String oriId = this.channel.getId();	//原来的id
		// 通道名称
		if(!isValidChannelName(type)) {
			return false;
		}
		this.channel.setName(this.channelName.getText().trim());

		// 序号
		if(!isValidChannelId(type)) {
			return false;
		}
		this.channel.setId(this.text_id.getText().trim());
		this.channel.setFixPositon(this.text_install_position.getText() == null?"":this.text_install_position.getText().trim());

		// 通道规约
		if (this.channelRule.getSelectionIndex() == -1) {
			MessageDialog.openWarning(Display.getCurrent().getActiveShell()
					.getShell(), "提示", "请选择通道规约");
			return false;
		} else
			this.channel.setProtocal(commRuleValueArray[this.channelRule
					.getSelectionIndex()]);

		// 串口名称
		if (this.comName.getSelectionIndex() == -1
				|| "".equals(this.comName.getText().trim())) {
			this.channel.setComPort(null);
		} else
			this.channel.setComPort(this.comNameArray[this.comName
					.getSelectionIndex()]);

		// 波特率
		if (this.baud.getSelectionIndex() == -1
				|| "".equals(this.baud.getText().trim())) {
			this.channel.setBaud(null);
		} else
			this.channel.setBaud(this.baudArray[this.baud.getSelectionIndex()]);

		// 数据位
		if (this.dataBit.getSelectionIndex() == -1
				|| "".equals(this.dataBit.getText().trim())) {
			this.channel.setDataBit(null);
		} else
			this.channel.setDataBit(this.dataBitArray[this.dataBit
					.getSelectionIndex()]);

		// 校验
		if (this.check.getSelectionIndex() == -1
				|| "".equals(this.check.getText().trim())) {
			this.channel.setCheck(null);
		} else
			this.channel.setCheck(this.checkValueArray[this.check
					.getSelectionIndex()]);

		// 停止位
		if (this.stopBit.getSelectionIndex() == -1
				|| "".equals(this.stopBit.getText().trim())) {
			this.channel.setStopBit("");
		} else
			this.channel.setStopBit(this.stopBitValueArray[this.stopBit
					.getSelectionIndex()]);
		// ip 端口
		this.channel.setIp("".equals(this.ipAddress.getText().trim()) ? null
				: this.ipAddress.getText().trim());
		this.channel.setTcpPort("".equals(this.port.getText().trim()) ? null
				: this.port.getText().trim());

		// 通讯离线、采样间隔、循环间隔
		this.channel
				.setOffline(("".equals(this.commOffText.getText().trim())) ? null
						: this.commOffText.getText().trim());
		this.channel
				.setInterval(("".equals(this.intervalText.getText().trim())) ? null
						: this.intervalText.getText().trim());
		
		this.channel.setLoopInterval("".equals(loopIntervalText.getText().trim())?null:loopIntervalText.getText().trim());

		this.channel.setDtuId("".equals(this.dtu_id.getText().trim()) ? null
						: this.dtu_id.getText().trim());
		this.channel.setHeartBeat("".equals(this.heart_beat.getText().trim()) ? null
				: this.heart_beat.getText().trim());
		this.channel.setDtuPort("".equals(this.dtu_port.getText().trim()) ? null
						: this.dtu_port.getText().trim());
		
		if(this.channel.getProtocal().equals(commRuleValueArray[0]) || this.channel.getProtocal().equals(commRuleValueArray[1])  || this.channel.getProtocal().equals(commRuleValueArray[7])) {//rtu
			this.channel.setIp(null);
			this.channel.setTcpPort(null);
			
			this.channel.setDtuId(null);
			this.channel.setHeartBeat(null);
			this.channel.setDtuPort(null);
		} else if(this.channel.getProtocal().equals(commRuleValueArray[2]) || this.channel.getProtocal().equals(commRuleValueArray[3])) {//tcp
			this.channel.setBaud(null);
			this.channel.setCheck(null);
			this.channel.setDataBit(null);
			this.channel.setStopBit(null);
			this.channel.setComPort(null);
			
			this.channel.setDtuId(null);
			this.channel.setHeartBeat(null);
			this.channel.setDtuPort(null);
		} else if(this.channel.getProtocal().equals(commRuleValueArray[4]) || this.channel.getProtocal().equals(commRuleValueArray[5]) || this.channel.getProtocal().equals(commRuleValueArray[6]) || this.channel.getProtocal().equals(commRuleValueArray[8])) {//dtu
			this.channel.setIp(null);
			this.channel.setTcpPort(null);
			
			this.channel.setBaud(null);
			this.channel.setCheck(null);
			this.channel.setDataBit(null);
			this.channel.setStopBit(null);
			this.channel.setComPort(null);
		}

		if (type == 1) {// 新建
			channelModelDao.addChannelModel(channel);

		} else {
			channelModelDao.updateChannelModelById(channel, oriId);

		}

		return true;
	}
	/**
	 * 验证是否是合法通道名字
	 * @param type 1为新建 0为修改
	 * @return
	 */
	private boolean isValidChannelName(int type) {
		if ("".equals(this.channelName.getText().trim())) {
			MessageDialog.openWarning(null, "提示", "采集通道名称不能为空！");
			return false;
		}
		if(type == 1) {//新建
			ChannelModel channelModel = channelModelDao.getChannelModelByName(this.channelName.getText().trim(), null);
			if(channelModel != null) {
				MessageDialog.openWarning(null, "提示", "该采集通道名称已存在！");
				return false;
			}
		} else {//修改
			ChannelModel channelModel = channelModelDao.getChannelModelByName(this.channelName.getText().trim(), this.channel.getName());
			if(channelModel != null) {
				MessageDialog.openWarning(null, "提示", "该采集通道名称已存在！");
				return false;
			}
		}
		return true;
	}
	/**
	 * 验证是否是合法通道序号
	 * @param type 1为新建 0为修改
	 * @return
	 */
	private boolean isValidChannelId(int type) {
		if ("".equals(this.text_id.getText().trim())) {
			MessageDialog.openWarning(null, "提示", "序号不能为空！");
			return false;
		}
		if(type == 1) {//新建
			ChannelModel channelModel = channelModelDao.getChannelModelById(this.text_id.getText().trim(), null);
			if(channelModel != null) {
				MessageDialog.openWarning(null, "提示", "序号重复！");
				return false;
			}
		} else {//修改
			ChannelModel channelModel = channelModelDao.getChannelModelById(this.text_id.getText().trim(), this.channel.getId());
			if(channelModel != null) {
				MessageDialog.openWarning(null, "提示", "序号重复！");
				return false;
			}
		}
		String str = this.text_id.getText().trim();
//		if(!str.startsWith("1") || str.length() != 4) {
//			MessageDialog.openWarning(null, "提示", "序号请输入 1xxx 格式！");
//			return false;
//		}
		int i;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			MessageDialog.openWarning(null, "提示", "序号请输入数字！");
			return false;
		}
		
		return true;
	}
}
