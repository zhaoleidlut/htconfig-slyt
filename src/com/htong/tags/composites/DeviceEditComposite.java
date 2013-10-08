package com.htong.tags.composites;

import java.util.Calendar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.windows.EditTagMainIndexWindow;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * 设备编辑面板
 * 
 * @author 赵磊
 * 
 */
public class DeviceEditComposite extends Composite {

	private DeviceModel deviceModel;

	private Text deviceName_text;
	private Text type_text;
	private Text remark_text;
	private Text slaveid_text;
	private Text manufacturer_text;
	private Text timeout_text;
	private Text retry_text;
	private DateTime installTime_datetime;
	private Button useButton;

	private boolean isNew; // 判断是否为新建
	private Text text_pt;
	private Text text_ct;
	private Text fix_position;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public DeviceEditComposite(final Composite composite, int style,
			final DeviceModel deviceModel) {
		super(composite, style);
		this.deviceModel = deviceModel;
		if (this.deviceModel.getElement() == null) {
			isNew = true; // 为新建
		} else {
			isNew = false; // 为编辑
		}

		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.wrap = false;
		setLayout(rowLayout);

		Composite composite_2 = new Composite(this, SWT.NONE);
		RowLayout rl_composite_2 = new RowLayout(SWT.VERTICAL);
		rl_composite_2.wrap = false;
		composite_2.setLayout(rl_composite_2);

		Group group = new Group(composite_2, SWT.NONE);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new RowData(300, 230));
		group.setText("基本信息");

		Label label = new Label(group, SWT.NONE);
		label.setText("设备名称：");

		deviceName_text = new Text(group, SWT.BORDER);
		GridData gd_deviceName_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_deviceName_text.widthHint = 130;
		deviceName_text.setLayoutData(gd_deviceName_text);
		new Label(group, SWT.NONE);

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("生产厂家：");
				
						manufacturer_text = new Text(group, SWT.BORDER);
						GridData gd_manufacturer_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_manufacturer_text.widthHint = 130;
						manufacturer_text.setLayoutData(gd_manufacturer_text);
				new Label(group, SWT.NONE);
		
				Label label_2 = new Label(group, SWT.NONE);
				label_2.setText("型　　号：");

		type_text = new Text(group, SWT.BORDER);
		GridData gd_type_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_type_text.widthHint = 130;
		type_text.setLayoutData(gd_type_text);
								new Label(group, SWT.NONE);
						
								Label label_5 = new Label(group, SWT.NONE);
								label_5.setText("安装日期：");
				
						installTime_datetime = new DateTime(group, SWT.BORDER);
						GridData gd_installTime_datetime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
						gd_installTime_datetime.widthHint = 120;
						installTime_datetime.setLayoutData(gd_installTime_datetime);
				new Label(group, SWT.NONE);
				
				Label label_8 = new Label(group, SWT.NONE);
				label_8.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_8.setText("安装位置：");
				
				fix_position = new Text(group, SWT.BORDER);
				GridData gd_fix_position = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
				gd_fix_position.widthHint = 200;
				fix_position.setLayoutData(gd_fix_position);
				
				Button button = new Button(group, SWT.NONE);
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						new EditTagMainIndexWindow(deviceModel, fix_position).open();
					}
				});
				button.setText(" 选择 ");
		
				Label label_3 = new Label(group, SWT.NONE);
				label_3.setText("备　　注：");

		remark_text = new Text(group, SWT.BORDER);
		GridData gd_remark_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_remark_text.widthHint = 140;
		remark_text.setLayoutData(gd_remark_text);
		new Label(group, SWT.NONE);
		
		Label lblPt = new Label(group, SWT.NONE);
		lblPt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPt.setText("PT：");
		
		text_pt = new Text(group, SWT.BORDER);
		text_pt.setText("1");
		GridData gd_text_pt = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_pt.widthHint = 80;
		text_pt.setLayoutData(gd_text_pt);
		new Label(group, SWT.NONE);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("CT：");
		
		text_ct = new Text(group, SWT.BORDER);
		text_ct.setText("1");
		GridData gd_text_ct = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_ct.widthHint = 80;
		text_ct.setLayoutData(gd_text_ct);
		new Label(group, SWT.NONE);

		Group group_1 = new Group(composite_2, SWT.NONE);
		group_1.setLayout(new GridLayout(3, false));
		group_1.setLayoutData(new RowData(300, -1));
		group_1.setText("通讯信息");

		Label label_4 = new Label(group_1, SWT.NONE);
		label_4.setText("设备地址：");
		
				slaveid_text = new Text(group_1, SWT.BORDER);
				GridData gd_slaveid_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_slaveid_text.widthHint = 80;
				slaveid_text.setLayoutData(gd_slaveid_text);
		new Label(group_1, SWT.NONE);

		Label label_6 = new Label(group_1, SWT.NONE);
		label_6.setText("超　　时：");
		
				timeout_text = new Text(group_1, SWT.BORDER);
				GridData gd_timeout_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_timeout_text.widthHint = 80;
				timeout_text.setLayoutData(gd_timeout_text);

		Label lblMs = new Label(group_1, SWT.NONE);
		lblMs.setText("ms");

		Label label_7 = new Label(group_1, SWT.NONE);
		label_7.setText("重发次数：");
		
				retry_text = new Text(group_1, SWT.BORDER);
				GridData gd_retry_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_retry_text.widthHint = 80;
				retry_text.setLayoutData(gd_retry_text);
		new Label(group_1, SWT.NONE);

		useButton = new Button(group_1, SWT.CHECK);
		useButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		useButton.setText("使用");
		new Label(group_1, SWT.NONE);

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(null);

		Button btnc = new Button(composite_1, SWT.NONE);
		btnc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((OperatingComposite)composite).setTop("操作已取消！");
			}
		});
		btnc.setBounds(75, 5, 72, 22);
		btnc.setBounds(180, 10, 72, 22);
		btnc.setText("取 消(&C)");

		Button btns = new Button(composite_1, SWT.NONE);
		btns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(saveInfo()) {
					if(isNew) {
						{
							MainUI.treeViewer.add(deviceModel.getParentObject(), deviceModel);
							MainUI.treeViewer.setExpandedState(deviceModel.getParentObject(),true);
							
							((OperatingComposite)composite).setTop("设备已保存！");
						}
					} else {
						MainUI.treeViewer.update(deviceModel, null);
						
						((OperatingComposite)composite).setTop("设备已更新！");
					}
				}
				
			}
		});
		btns.setBounds(292, 5, 72, 22);
		btns.setBounds(72, 10, 72, 22);
		btns.setText("保 存(&S)");

		initControlValue(); // 初始化信息
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * 初始化控件值
	 */
	private void initControlValue() {
		// 设备名称、生产厂家
		if (isNew) {// 新建
			this.deviceName_text.setText("PDM");
			this.manufacturer_text.setText("丹东华通测控有限公司");
			this.useButton.setSelection(true);
		} else {
			this.deviceName_text
					.setText(this.deviceModel.getName() == null ? ""
							: this.deviceModel.getName());
			this.manufacturer_text
					.setText(this.deviceModel.getManufacturer() == null ? ""
							: this.deviceModel.getManufacturer());
			// 备用
			this.useButton
					.setSelection((this.deviceModel.getUsed()==null || "false".equals(this.deviceModel.getUsed())) ? false
							: true);
		}

		// 设备型号
		this.type_text.setText(this.deviceModel.getType() == null ? ""
				: this.deviceModel.getType());

		// 安装日期
		if (this.deviceModel.getInstallTime() == null) {
			if (isNew) {
				this.installTime_datetime.setDate(
						Calendar.getInstance().get(Calendar.YEAR), Calendar
								.getInstance().get(Calendar.MONTH), Calendar
								.getInstance().get(Calendar.DAY_OF_MONTH));
			}

		} else {
			int year = this.deviceModel.getInstallTime().get(Calendar.YEAR);
			int month = this.deviceModel.getInstallTime().get(Calendar.MONTH);
			int day = this.deviceModel.getInstallTime().get(
					(Calendar.DAY_OF_MONTH));
			this.installTime_datetime.setDate(year, month, day);
		}
		
		this.text_pt.setText(this.deviceModel.getPt() == null?"1":this.deviceModel.getPt());
		this.text_ct.setText(this.deviceModel.getCt() == null?"1":this.deviceModel.getCt());
		this.fix_position.setText(this.deviceModel.getFixPosition() == null ?"":this.deviceModel.getFixPosition());

		// 地址
		this.slaveid_text.setText(this.deviceModel.getSlaveId() == null ? ""
				: this.deviceModel.getSlaveId());
		// 超时时间、重发次数
		this.timeout_text.setText(this.deviceModel.getTimeout() == null ? "500"
				: this.deviceModel.getTimeout());
		this.retry_text.setText(this.deviceModel.getRetry() == null ? "2"
				: this.deviceModel.getRetry());
		
		this.remark_text.setText(this.deviceModel.getRemark()==null?"":this.deviceModel.getRemark());
		
	}

	/**
	 * 保存信息
	 */
	private boolean saveInfo() {
		String oldSlaveId = this.deviceModel.getSlaveId();
		
		if ("".equals(this.deviceName_text.getText().trim())) {
			MessageDialog.openWarning(null, "提示", "设备名称不能为空！");
			return false;
		}
		
		if("".equals(this.slaveid_text.getText().trim())) {
			MessageDialog.openWarning(null, "提示", "设备地址不能为空！");
			return false;
		}
		//设备名字
		this.deviceModel.setName("".equals(this.deviceName_text.getText()
				.trim()) ? null : this.deviceName_text.getText().trim());
		//生产厂家
		this.deviceModel.setManufacturer("".equals(this.manufacturer_text
				.getText().trim()) ? null : this.manufacturer_text.getText()
				.trim());
		//型号
		this.deviceModel
				.setType("".equals(this.type_text.getText().trim()) ? null
						: this.type_text.getText().trim());
		//安装日期
		Calendar calendar = Calendar.getInstance();
		int year = this.installTime_datetime.getYear();
		int month = this.installTime_datetime.getMonth();
		int date = this.installTime_datetime.getDay();
		calendar.set(year, month, date);
		this.deviceModel.setInstallTime(calendar);
		//备注
		this.deviceModel.setRemark("".equals(this.remark_text.getText().trim())?null:this.remark_text.getText().trim());
		
		this.deviceModel.setPt("".equals(this.text_pt.getText().trim())?null:this.text_pt.getText().trim());
		this.deviceModel.setCt("".equals(this.text_ct.getText().trim())?null:this.text_ct.getText().trim());
		this.deviceModel.setFixPosition("".equals(this.fix_position.getText().trim())?null:this.fix_position.getText().trim());

		//设备地址
//		int slave = 0;
//		try {
//			slave = Integer.parseInt(this.slaveid_text.getText().trim());
//		} catch (NumberFormatException e1) {
//			this.deviceModel.setSlaveId(null);
//			MessageDialog.openWarning(null, "提示", "设备地址请输入1-247之间的整数！");
//			return false;
//		}
//		if (slave < 1 || slave > 247) {
//			MessageDialog.openWarning(null, "提示", "设备地址请输入1-247之间的整数！");
//			return false;
//		}
		
		
		this.deviceModel.setSlaveId(this.slaveid_text.getText().trim());

		//超时时间、重发次数、备用
		this.deviceModel.setTimeout("".equals(this.timeout_text.getText().trim())?null:this.timeout_text.getText().trim());
		this.deviceModel.setRetry("".equals(this.retry_text.getText().trim())?null:this.retry_text.getText().trim());
		this.deviceModel.setUsed(this.useButton.getSelection()?"true":"false");

		DeviceModelDaoImpl deviceDao = new DeviceModelDaoImpl();

		if (isNew) {// 新建设备
			deviceDao.addDeviceModel(this.deviceModel);
		} else {
			deviceDao.updateDeviceModelBySlaveId(this.deviceModel, oldSlaveId);
		}
		
		return true;

	}

}
