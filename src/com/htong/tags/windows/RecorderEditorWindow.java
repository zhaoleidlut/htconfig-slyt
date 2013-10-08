package com.htong.tags.windows;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.RecorderModelDaoImpl;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.tag.RecorderModel;
import com.htong.ui.GetXpathUtil;
import com.htong.util.LayoutUtil;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class RecorderEditorWindow extends ApplicationWindow {
	
	private final Logger log = Logger.getLogger(RecorderEditorWindow.class);
	
	private class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			String str = (String) element;
			if (str.equals("Statistic")) {
				return "统计";
			} else if (str.equals("Alarm")) {
				return "越限报警存储器";
			} else if (str.equals("Fault")) {
				return "故障存储器";
			} else if (str.equals("RSChange")) {
				return "变位报警";
			} else if (str.equals("SOE")) {
				return "SOE存储器";
			} else if (str.equals("YCTag")) {
				return "遥测数据存储器";
			} else if(str.equals("YMTag")) {
				return "遥脉数据存储器";
			}

			return (String) element;
		}
	}

	private Text name_text;
	private Text start_text_1;
	private Text end_text_2;
	private Text dayinclude_text_3;
	private Text dayexclude_text_4;
	private Text weekinclude_text_5;
	private Text weekexclude_text_6;
	private Text upper_text_7;
	private Text upper_info_text_8;
	private Text lower_text_9;
	private Text lower_info_text_10;
	private Text when_text_11;
	private Text on_info_text_12;
	private Text off_info_text_13;
	private Text info_text_14;
	private Text soe_js_text_15;
	private Text range_text_16;
	private Text interval_text_17;
	private java.util.List<String> typeList = new ArrayList<String>();

	private StackLayout sl_composite_1 = new StackLayout();
	private Composite composite_Statistic;
	private Composite composite_Alarm;
	private Composite composite_Fault;
	private Composite composite_soe;
	private Composite composite_tag;
	private Text rs_on_info_text_18;
	private Text rs_off_info_text_19;
	private Composite composite_RSChange;
	private Group composite_custom;
	private TagModel tagModel;
	private RecorderModel recorderModel;
	private Combo type_combo;
	private Combo statistic_combo;
	private Combo computmode_combo;
	private ComboViewer comboViewer;
	private ApplicationWindow editVarDicWindow;
	private boolean isSub;//是否是编辑子变量
	private TagModel subTagModel;//子变量
	
	private RecorderModelDaoImpl recorderModelDao = new RecorderModelDaoImpl();
	private Group composite_1;
	private Composite composite_ymtag;
	private Label label_22;
	private Text text_yminterval;
	private Label label_23;
	private Label label_jiange;
	private Text text_ymtagexpression;
	private Button button_alarm_thm;
	private Button button_fault_thm;
	private Button button_rschange_thm;
	private Label label_21;
	private Label label_24;
	private Label label_25;
	private Text min_value;
	private Text max_value;
	private Text unit_value;
	private Text when_rschange;

	/**
	 * Create the application window.
	 * @wbp.parser.constructor
	 */
	public RecorderEditorWindow(TagModel tagModel, RecorderModel recorderModel, ApplicationWindow evdw,boolean isSub) {
		super(null);
		this.tagModel = tagModel;
		this.recorderModel = recorderModel;
		this.editVarDicWindow = evdw;
		this.isSub = isSub;
		
		if(tagModel.getType().startsWith("遥测")) {
			
			typeList.add("YCTag");
			typeList.add("Alarm");
		} else if(tagModel.getType().startsWith("遥信")) {
			typeList.add("Fault");
			
			typeList.add("RSChange");
			
		} else if(tagModel.getType().startsWith("遥脉")) {
			typeList.add("YMTag");
			
		}

//		typeList.add("Statistic");
		
//		typeList.add("SOE");
		

	}
	
	public RecorderEditorWindow(TagModel tagModel,TagModel subTagModel,  RecorderModel recorderModel, ApplicationWindow evdw,boolean isSub) {
		super(null);
		this.tagModel = tagModel;
		this.recorderModel = recorderModel;
		this.editVarDicWindow = evdw;
		this.isSub = isSub;
		this.subTagModel = subTagModel;
		
//		typeList.add("Statistic");
		typeList.add("Alarm");
		typeList.add("Fault");
		typeList.add("RSChange");
		typeList.add("SOE");
		typeList.add("YCTag");
		

	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Composite composite_3 = new Composite(container, SWT.NONE);
		composite_3.setLayout(new GridLayout(4, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setText("存储器类型：");

		comboViewer = new ComboViewer(composite_3, SWT.READ_ONLY);
		comboViewer.getSelection();
		type_combo = comboViewer.getCombo();
		GridData gd_type_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_type_combo.widthHint = 85;
		type_combo.setLayoutData(gd_type_combo);
		comboViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						
						composite_1.setVisible(true);
						IStructuredSelection iss = (IStructuredSelection) comboViewer.getSelection();
						String ss = (String) iss.getFirstElement();
						if(ss.equals("Alarm")) {
							sl_composite_1.topControl = composite_Alarm;
							composite_Alarm.getParent().layout();
						} else if(ss.equals("Fault")) {
							if(recorderModel == null) {
								when_text_11.setText("1");
							}
							
							sl_composite_1.topControl = composite_Fault;
							composite_Fault.getParent().layout();
						} else if(ss.equals("RSChange")) {
							sl_composite_1.topControl = composite_RSChange;
							composite_RSChange.getParent().layout();
						} else if(ss.equals("YCTag")) {
							sl_composite_1.topControl = composite_tag;
							composite_tag.getParent().layout();
						} else if(ss.equals("YMTag")) {
							sl_composite_1.topControl = composite_ymtag;
							composite_ymtag.getParent().layout();
						}
						
						name_text.setText(comboViewer.getCombo().getText());
						
					}
				});
		comboViewer.setLabelProvider(new ViewerLabelProvider());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(typeList);

//		type_combo.select(0);

		Label label = new Label(composite_3, SWT.NONE);
		label.setText("存储器名字：");
		label.setVisible(true);

		name_text = new Text(composite_3, SWT.BORDER);
		name_text.setVisible(true);
		GridData gd_name_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_name_text.widthHint = 100;
		name_text.setLayoutData(gd_name_text);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		composite_1 = new Group(composite, SWT.NONE);
		composite_1.setText("详细信息");
		composite_1.setLayout(sl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				2, 1));

		composite_Statistic = new Composite(composite_1, SWT.NONE);

		Label label_1 = new Label(composite_Statistic, SWT.NONE);
		label_1.setBounds(38, 37, 65, 22);
		label_1.setText("统计方式：");

		Label label_2 = new Label(composite_Statistic, SWT.NONE);
		label_2.setText("计算方式：");
		label_2.setBounds(38, 78, 65, 22);

		statistic_combo = new Combo(composite_Statistic, SWT.NONE);
		statistic_combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (statistic_combo.getSelectionIndex() == 1) {
					composite_custom.setVisible(true);
				} else {
					composite_custom.setVisible(false);
				}
			}
		});
		statistic_combo.setBounds(125, 34, 87, 20);
		statistic_combo.setItems(new String[] { "常规", "自定义" });

		computmode_combo = new Combo(composite_Statistic, SWT.NONE);
		computmode_combo.setBounds(125, 75, 87, 20);
		computmode_combo.setItems(new String[] { "平均值", "求和" });

		composite_custom = new Group(composite_Statistic, SWT.NONE);
		
		
		composite_custom.setText("自定义时间");
		composite_custom.setBounds(15, 117, 363, 143);
		composite_custom.setLayout(null);

		Label label_3 = new Label(composite_custom, SWT.NONE);
		label_3.setBounds(8, 25, 60, 17);
		label_3.setText("开始时间：");

		start_text_1 = new Text(composite_custom, SWT.BORDER);
		start_text_1.setBounds(73, 22, 73, 23);

		Label label_4 = new Label(composite_custom, SWT.NONE);
		label_4.setBounds(151, 25, 60, 17);
		label_4.setText("结束时间：");

		end_text_2 = new Text(composite_custom, SWT.BORDER);
		end_text_2.setBounds(216, 22, 73, 23);

		Label label_5 = new Label(composite_custom, SWT.NONE);
		label_5.setBounds(8, 53, 48, 17);
		label_5.setText("统计日：");

		dayinclude_text_3 = new Text(composite_custom, SWT.BORDER);
		dayinclude_text_3.setBounds(73, 50, 73, 23);

		Label label_6 = new Label(composite_custom, SWT.NONE);
		label_6.setBounds(151, 53, 60, 17);
		label_6.setText("不统计日：");

		dayexclude_text_4 = new Text(composite_custom, SWT.BORDER);
		dayexclude_text_4.setBounds(216, 50, 73, 23);

		Label label_7 = new Label(composite_custom, SWT.NONE);
		label_7.setBounds(8, 81, 48, 17);
		label_7.setText("统计周：");

		weekinclude_text_5 = new Text(composite_custom, SWT.BORDER);
		weekinclude_text_5.setBounds(73, 78, 73, 23);

		Label label_8 = new Label(composite_custom, SWT.NONE);
		label_8.setBounds(151, 81, 60, 17);
		label_8.setText("不统计周：");

		weekexclude_text_6 = new Text(composite_custom, SWT.BORDER);
		weekexclude_text_6.setBounds(216, 78, 73, 23);

		composite_Alarm = new Composite(composite_1, SWT.NONE);

		Label label_9 = new Label(composite_Alarm, SWT.NONE);
		label_9.setText("上限：");
		label_9.setBounds(29, 34, 36, 18);

		upper_text_7 = new Text(composite_Alarm, SWT.BORDER);
		upper_text_7.setBounds(69, 31, 70, 18);

		Label label_10 = new Label(composite_Alarm, SWT.NONE);
		label_10.setText("上限信息：");
		label_10.setBounds(197, 34, 54, 18);

		upper_info_text_8 = new Text(composite_Alarm, SWT.BORDER);
		upper_info_text_8.setBounds(257, 31, 70, 18);

		Label label_11 = new Label(composite_Alarm, SWT.NONE);
		label_11.setText("下限：");
		label_11.setBounds(29, 71, 36, 18);

		lower_text_9 = new Text(composite_Alarm, SWT.BORDER);
		lower_text_9.setBounds(69, 68, 70, 18);

		Label label_12 = new Label(composite_Alarm, SWT.NONE);
		label_12.setText("下限信息：");
		label_12.setBounds(197, 71, 54, 18);

		lower_info_text_10 = new Text(composite_Alarm, SWT.BORDER);
		lower_info_text_10.setBounds(257, 68, 70, 18);
		
		button_alarm_thm = new Button(composite_Alarm, SWT.CHECK);
		button_alarm_thm.setBounds(32, 109, 93, 16);
		button_alarm_thm.setText("推画面");

		composite_Fault = new Composite(composite_1, SWT.NONE);

		Label label_13 = new Label(composite_Fault, SWT.NONE);
		label_13.setBounds(32, 34, 54, 18);
		label_13.setText("报警标志：");

		when_text_11 = new Text(composite_Fault, SWT.BORDER);
		when_text_11.setBounds(105, 31, 70, 18);

		on_info_text_12 = new Text(composite_Fault, SWT.BORDER);
		on_info_text_12.setBounds(105, 55, 70, 18);

		off_info_text_13 = new Text(composite_Fault, SWT.BORDER);
		off_info_text_13.setBounds(105, 79, 70, 18);

		Label lblNewLabel_1 = new Label(composite_Fault, SWT.NONE);
		lblNewLabel_1.setBounds(32, 58, 54, 18);
		lblNewLabel_1.setText("合信息：");

		Label label_14 = new Label(composite_Fault, SWT.NONE);
		label_14.setBounds(32, 82, 54, 18);
		label_14.setText("分信息：");

		Label label_20 = new Label(composite_Fault, SWT.NONE);
		label_20.setBounds(191, 34, 54, 18);
		label_20.setText("1或者0");
		
		button_fault_thm = new Button(composite_Fault, SWT.CHECK);
		button_fault_thm.setBounds(32, 110, 93, 16);
		button_fault_thm.setText("推画面");

		composite_RSChange = new Composite(composite_1, SWT.NONE);

		Label label_18 = new Label(composite_RSChange, SWT.NONE);
		label_18.setBounds(47, 41, 54, 18);
		label_18.setText("合信息：");

		rs_on_info_text_18 = new Text(composite_RSChange, SWT.BORDER);
		rs_on_info_text_18.setBounds(120, 38, 70, 18);

		rs_off_info_text_19 = new Text(composite_RSChange, SWT.BORDER);
		rs_off_info_text_19.setBounds(120, 73, 70, 18);

		Label label_19 = new Label(composite_RSChange, SWT.NONE);
		label_19.setText("分信息：");
		label_19.setBounds(47, 76, 54, 18);
		
		button_rschange_thm = new Button(composite_RSChange, SWT.CHECK);
		button_rschange_thm.setBounds(47, 167, 93, 16);
		button_rschange_thm.setText("推画面");
		
		Label label_26 = new Label(composite_RSChange, SWT.NONE);
		label_26.setBounds(36, 120, 54, 12);
		label_26.setText("报警类型：");
		
		when_rschange = new Text(composite_RSChange, SWT.BORDER);
		when_rschange.setBounds(120, 117, 70, 18);
		
		Label lblNewLabel_4 = new Label(composite_RSChange, SWT.NONE);
		lblNewLabel_4.setBounds(120, 141, 238, 12);
		lblNewLabel_4.setText("1：合报警  0：分报警  -1：变位报警");

		composite_soe = new Composite(composite_1, SWT.NONE);

		Label label_15 = new Label(composite_soe, SWT.NONE);
		label_15.setBounds(10, 10, 54, 18);
		label_15.setText("信息：");

		info_text_14 = new Text(composite_soe, SWT.BORDER);
		info_text_14.setBounds(70, 10, 70, 18);

		Label label_16 = new Label(composite_soe, SWT.NONE);
		label_16.setBounds(10, 34, 54, 18);
		label_16.setText("脚本：");

		soe_js_text_15 = new Text(composite_soe, SWT.BORDER | SWT.MULTI);
		soe_js_text_15.setBounds(70, 34, 300, 226);

		composite_tag = new Composite(composite_1, SWT.NONE);

		Label lblNewLabel_2 = new Label(composite_tag, SWT.NONE);
		lblNewLabel_2.setBounds(56, 29, 36, 18);
		lblNewLabel_2.setText("阈值：");

		range_text_16 = new Text(composite_tag, SWT.BORDER);
		range_text_16.setSelection(0);
		range_text_16.setBounds(98, 29, 70, 18);

		Label lblNewLabel_3 = new Label(composite_tag, SWT.NONE);
		lblNewLabel_3.setBounds(56, 68, 36, 25);
		lblNewLabel_3.setText("周期：");

		interval_text_17 = new Text(composite_tag, SWT.BORDER);
		interval_text_17.setSelection(0);
		interval_text_17.setBounds(98, 65, 70, 18);

		Label label_17 = new Label(composite_tag, SWT.NONE);
		label_17.setBounds(174, 68, 54, 25);
		label_17.setText("分钟");

		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		composite_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 2, 1));

		Button btnNewButton = new Button(composite_2, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				save();
				close();

			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		btnNewButton.setText(" 保 存 ");

		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				close();
			}
		});
		btnNewButton_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		btnNewButton_1.setText(" 退 出 ");

		sl_composite_1.topControl = composite_Statistic;
		
		composite_ymtag = new Composite(composite_1, SWT.NONE);
		composite_ymtag.setLayout(new GridLayout(3, false));
		
		label_21 = new Label(composite_ymtag, SWT.NONE);
		label_21.setText("最小值：");
		
		min_value = new Text(composite_ymtag, SWT.BORDER);
		GridData gd_min_value = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_min_value.widthHint = 80;
		min_value.setLayoutData(gd_min_value);
		new Label(composite_ymtag, SWT.NONE);
		
		label_24 = new Label(composite_ymtag, SWT.NONE);
		label_24.setText("最大值：");
		
		max_value = new Text(composite_ymtag, SWT.BORDER);
		max_value.setSelection(0);
		GridData gd_max_value = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_max_value.widthHint = 80;
		max_value.setLayoutData(gd_max_value);
		new Label(composite_ymtag, SWT.NONE);
		
		label_25 = new Label(composite_ymtag, SWT.NONE);
		label_25.setText("单位脉冲电度量：");
		
		unit_value = new Text(composite_ymtag, SWT.BORDER);
		unit_value.setSelection(0);
		GridData gd_unit_value = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_unit_value.widthHint = 80;
		unit_value.setLayoutData(gd_unit_value);
		new Label(composite_ymtag, SWT.NONE);
		
		label_22 = new Label(composite_ymtag, SWT.NONE);
		label_22.setText("周期：");
		
		text_yminterval = new Text(composite_ymtag, SWT.BORDER);
		GridData gd_text_yminterval = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_yminterval.widthHint = 80;
		text_yminterval.setLayoutData(gd_text_yminterval);
		text_yminterval.setSelection(0);
		
		label_23 = new Label(composite_ymtag, SWT.NONE);
		label_23.setText("分钟");
		
		label_jiange = new Label(composite_ymtag, SWT.NONE);
		label_jiange.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		label_jiange.setText("脚本：");
		new Label(composite_ymtag, SWT.NONE);
		
		text_ymtagexpression = new Text(composite_ymtag, SWT.BORDER | SWT.MULTI);
		text_ymtagexpression.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
//		composite_Statistic.getParent().layout();

		initControlsValue();

		return container;
	}

	/**
	 * 初始化控件值
	 */
	private void initControlsValue() {
		if (recorderModel != null) {
			String ss = recorderModel.getType();
			
			if(ss.equals("Alarm")) {
				comboViewer.getCombo().setText("越限报警存储器");
				
				sl_composite_1.topControl = composite_Alarm;
				composite_Alarm.getParent().layout();
			} else if(ss.equals("Fault")) {
				comboViewer.getCombo().setText("故障存储器");
				
				sl_composite_1.topControl = composite_Fault;
				composite_Fault.getParent().layout();
				
				when_text_11.setText(recorderModel.getWhen() == null ? ""
						: recorderModel.getWhen());
			} else if(ss.equals("RSChange")) {
				comboViewer.getCombo().setText("变位报警");
				
				sl_composite_1.topControl = composite_RSChange;
				composite_RSChange.getParent().layout();
				
				when_rschange.setText(recorderModel.getWhen() == null ? "" : recorderModel.getWhen());
			} else if(ss.equals("YCTag")) {
				comboViewer.getCombo().setText("遥测数据存储器");
				
				sl_composite_1.topControl = composite_tag;
				composite_tag.getParent().layout();
			} else if(ss.equals("YMTag")) {
				comboViewer.getCombo().setText("遥脉数据存储器");
				
				sl_composite_1.topControl = composite_ymtag;
				composite_ymtag.getParent().layout();
			} else {
				composite_1.setVisible(false);
			}


			name_text.setText(recorderModel.getName() == null ? ""
					: recorderModel.getName());

			if(recorderModel.getStatisticMode() == null) {
				statistic_combo.select(-1);
			} else if("normal".equals(recorderModel.getStatisticMode())){
				statistic_combo.select(0);
			} else
				statistic_combo.select(1);

			if(recorderModel.getComputeMode() == null) {
				computmode_combo.select(-1);
			} else if("avg".equals(recorderModel.getComputeMode())) {
				computmode_combo.select(0);
			} else 
				computmode_combo.select(1);
			
			if(statistic_combo.getSelectionIndex() == 1) {
				
				composite_custom.setVisible(true);
			} else {
				composite_custom.setVisible(false);
			}

			start_text_1.setText(recorderModel.getStart() == null ? ""
					: recorderModel.getStart());
			end_text_2.setText(recorderModel.getEnd() == null ? ""
					: recorderModel.getEnd());
			dayinclude_text_3
					.setText(recorderModel.getDayInclude() == null ? ""
							: recorderModel.getDayInclude());
			dayexclude_text_4
					.setText(recorderModel.getDayExclude() == null ? ""
							: recorderModel.getDayExclude());
			weekinclude_text_5
					.setText(recorderModel.getWeekInclude() == null ? ""
							: recorderModel.getWeekInclude());
			weekexclude_text_6
					.setText(recorderModel.getWeekExclude() == null ? ""
							: recorderModel.getWeekExclude());

			upper_text_7.setText(recorderModel.getUpper() == null ? ""
					: recorderModel.getUpper());
			upper_info_text_8.setText(recorderModel.getUpperInfo() == null ? ""
					: recorderModel.getUpperInfo());
			lower_text_9.setText(recorderModel.getLower() == null ? ""
					: recorderModel.getLower());
			lower_info_text_10
					.setText(recorderModel.getLowerInfo() == null ? ""
							: recorderModel.getLowerInfo());

			
			on_info_text_12.setText(recorderModel.getOnInfo() == null ? ""
					: recorderModel.getOnInfo());
			off_info_text_13.setText(recorderModel.getOffInfo() == null ? ""
					: recorderModel.getOffInfo());

			rs_on_info_text_18
					.setText(recorderModel.getRs_onInfo() == null ? ""
							: recorderModel.getRs_onInfo());
			rs_off_info_text_19
					.setText(recorderModel.getRs_offInfo() == null ? ""
							: recorderModel.getRs_offInfo());

			info_text_14.setText(recorderModel.getInfo() == null ? ""
					: recorderModel.getInfo());
			soe_js_text_15.setText(recorderModel.getSoeJS() == null ? ""
					: recorderModel.getSoeJS());

			range_text_16.setText(recorderModel.getRange() == null ? ""
					: recorderModel.getRange());
			interval_text_17.setText(recorderModel.getInterval() == null ? ""
					: recorderModel.getInterval());
			
			text_yminterval.setText(recorderModel.getInterval()==null?"":recorderModel.getInterval());
			
			text_ymtagexpression.setText(recorderModel.getYmtagExpression() == null?"":recorderModel.getYmtagExpression());
			
			//推画面
			button_alarm_thm.setSelection(recorderModel.getShow_alarm() != null && recorderModel.getShow_alarm().equals("true") ? true:false);
			button_fault_thm.setSelection(recorderModel.getShow_fault() != null && recorderModel.getShow_fault().equals("true") ? true:false);
			button_rschange_thm.setSelection(recorderModel.getShow_rs() != null && recorderModel.getShow_rs().equals("true") ? true:false);
			
			min_value.setText(recorderModel.getMinValue()==null?"":recorderModel.getMinValue());
			max_value.setText(recorderModel.getMaxValue()==null?"":recorderModel.getMaxValue());
			unit_value.setText(recorderModel.getUnitValue()==null?"":recorderModel.getUnitValue());
			

		} else {
			//初始化新建存储器
			if(tagModel.getType().startsWith("遥测")) {
				type_combo.setText("遥测数据存储器");
				interval_text_17.setText("10");
				
				button_alarm_thm.setSelection(true);
				
				sl_composite_1.topControl = composite_tag;
				composite_tag.getParent().layout();
			} else if(tagModel.getType().startsWith("遥信")) {
				type_combo.setText("变位报警");
				rs_on_info_text_18.setText("合闸");
				rs_off_info_text_19.setText("分闸");
				
				button_fault_thm.setSelection(true);
				button_rschange_thm.setSelection(true);
				
				when_rschange.setText("-1");
				
				sl_composite_1.topControl = composite_RSChange;
				composite_RSChange.getParent().layout();
			} else if(tagModel.getType().startsWith("遥脉")) {
				type_combo.setText("遥脉数据存储器");
				text_yminterval.setText("30");
//				StringBuilder strBuilder = new StringBuilder();
//				strBuilder.append("if (typeof(__value__) != \"undefined\") {"+"\r\n");
//				strBuilder.append("\t" + "var change = 0;"+"\r\n");
//				strBuilder.append("\t" + "if (typeof(上次转换表读数) == \"undefined\") {"+"\r\n");
//				strBuilder.append("\t\t" + "change = 0;"+"\r\n");
//				strBuilder.append("\t" + "} else if( (__value__ - 上次转换表读数) < 0) {"+"\r\n");
//				strBuilder.append("\t\t" + "change = (__value__ - 上次转换表读数 + 最大值 - 最小值) * 单位脉冲电度量;"+"\r\n");
//				strBuilder.append("\t" + "} else {"+"\r\n");
//				strBuilder.append("\t\t" + "change = (__value__ - 上次转换表读数) * 单位脉冲电度量;"+"\r\n");
//				strBuilder.append("\t" + "}"+"\r\n");
//				strBuilder.append("\t" + "上次转换表读数 = __value__;"+"\r\n");
//				strBuilder.append("\t" + "change;"+"\r\n");
//				strBuilder.append("}"+"\r\n");
//				text_ymtagexpression.setText(strBuilder.toString());
				
				min_value.setText("0");
				max_value.setText("599999999");
				unit_value.setText("1");
				
				sl_composite_1.topControl = composite_ymtag;
				composite_ymtag.getParent().layout();
			} else {
				composite_1.setVisible(false);
			}
			
			name_text.setText(type_combo.getText());
		}

	}

	/**
	 * 保存
	 */
	private void save() {
		clearOtherComposite();
		if (recorderModel == null) {// 新建
			
			RecorderModel rm = new RecorderModel();
			rm.setTagModel(tagModel);
			
			StructuredSelection ss = (StructuredSelection) comboViewer.getSelection();
			String type = (String)ss.getFirstElement();
			
			rm.setType(type);
			rm.setName("".equals(name_text.getText())?null:name_text.getText().trim());
			
			if(statistic_combo.getText().equals("常规")) {
				rm.setStatisticMode("narmal");
			} else if(statistic_combo.getText().equals("自定义")) {
				rm.setStatisticMode("custom");
			} else
				rm.setStatisticMode(null);
			
			if(computmode_combo.getText().equals("平均值")) {
				rm.setComputeMode("svg");
			} else if(computmode_combo.getText().equals("求和")) {
				rm.setComputeMode("sum");
			} else
				rm.setComputeMode(null);
			rm.setStart("".equals(start_text_1.getText())?null:start_text_1.getText().trim());
			rm.setEnd("".equals(end_text_2.getText())?null:end_text_2.getText().trim());
			rm.setDayInclude("".equals(dayinclude_text_3.getText())?null:dayinclude_text_3.getText().trim());
			rm.setDayExclude("".equals(dayexclude_text_4.getText())?null:dayexclude_text_4.getText().trim());
			rm.setWeekInclude("".equals(weekinclude_text_5.getText())?null:weekinclude_text_5.getText().trim());
			rm.setWeekExclude("".equals(weekexclude_text_6.getText())?null:weekexclude_text_6.getText().trim());
			
			rm.setUpper("".equals(upper_text_7.getText())?null:upper_text_7.getText().trim());
			rm.setUpperInfo("".equals(upper_info_text_8.getText())?null:upper_info_text_8.getText().trim());
			rm.setLower("".equals(lower_text_9.getText())?null:lower_text_9.getText().trim());
			rm.setLowerInfo("".equals(lower_info_text_10.getText())?null:lower_info_text_10.getText().trim());
			
			
			if(comboViewer.getCombo().getText().startsWith("变位")){
				rm.setWhen("".equals(when_rschange.getText())?null:when_rschange.getText().trim());
			} else if(comboViewer.getCombo().getText().startsWith("故障")) {
				rm.setWhen("".equals(when_text_11.getText())?null:when_text_11.getText().trim());
			}
			
			
			
			rm.setOnInfo("".equals(on_info_text_12.getText())?null:on_info_text_12.getText().trim());
			rm.setOffInfo("".equals(off_info_text_13.getText())?null:off_info_text_13.getText().trim());
			
			rm.setRs_onInfo("".equals(rs_on_info_text_18.getText())?null:rs_on_info_text_18.getText().trim());
			rm.setRs_offInfo("".equals(rs_off_info_text_19.getText())?null:rs_off_info_text_19.getText().trim());
			
			rm.setInfo("".equals(info_text_14.getText())?null:info_text_14.getText().trim());
			rm.setSoeJS("".equals(soe_js_text_15.getText())?null:soe_js_text_15.getText().trim());
			
			rm.setRange("".equals(range_text_16.getText())?null:range_text_16.getText().trim());
			
			rm.setShow_alarm(button_alarm_thm.getSelection()?"true":"false");
			rm.setShow_fault(button_fault_thm.getSelection()?"true":"false");
			rm.setShow_rs(button_rschange_thm.getSelection()?"true":"false");
			
			rm.setMinValue("".equals(min_value.getText())?null:min_value.getText().trim());
			rm.setMaxValue("".equals(max_value.getText())?null:max_value.getText().trim());
			rm.setUnitValue("".equals(unit_value.getText())?null:unit_value.getText().trim());
			
			if(tagModel.getType().startsWith("遥脉")) {
				rm.setInterval("".equals(text_yminterval.getText())?null:text_yminterval.getText().trim());
				
			} else if(tagModel.getType().startsWith("遥测")) {
				rm.setInterval("".equals(interval_text_17.getText())?null:interval_text_17.getText().trim());
			}
			
			rm.setYmtagExpression(text_ymtagexpression.getText().trim().equals("")?null:text_ymtagexpression.getText().trim());
			
			
			
			if(isSub) {//子
				String xpath = GetXpathUtil.getTagModelXpath(tagModel);
				xpath += "/"+ TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='" + subTagModel.getName() + "']";
				recorderModelDao.addRecorder(rm, xpath);
			} else {
				recorderModelDao.addRecorder(rm, GetXpathUtil.getTagModelXpath(tagModel));
			}
			
			
			if(editVarDicWindow instanceof EditVarDicWindow) {
				((EditVarDicWindow)editVarDicWindow).initRecorder();//更新列表
			} else {
//				((SubVarDicEditWindow)editVarDicWindow).initRecorder();//更新列表
			}
			
		} else {
			StructuredSelection ss = (StructuredSelection) comboViewer.getSelection();
			String type = (String)ss.getFirstElement();
			
			String oldName = recorderModel.getName();
			log.debug("存储器类型：" + type);
			
			recorderModel.setType(type);
			recorderModel.setName("".equals(name_text.getText())?null:name_text.getText().trim());
			
			if(statistic_combo.getText().equals("常规")) {
				recorderModel.setStatisticMode("narmal");
			} else if(statistic_combo.getText().equals("自定义")) {
				recorderModel.setStatisticMode("custom");
			} else
//				recorderModel.setStatisticMode(null);
			
//			if(computmode_combo.getText().equals("平均值")) {
//				recorderModel.setComputeMode("svg");
//			} else if(computmode_combo.getText().equals("求和")) {
//				recorderModel.setComputeMode("sum");
//			} else
				recorderModel.setComputeMode(null);
			
			recorderModel.setStart("".equals(start_text_1.getText())?null:start_text_1.getText().trim());
			recorderModel.setEnd("".equals(end_text_2.getText())?null:end_text_2.getText().trim());
			recorderModel.setDayInclude("".equals(dayinclude_text_3.getText())?null:dayinclude_text_3.getText().trim());
			recorderModel.setDayExclude("".equals(dayexclude_text_4.getText())?null:dayexclude_text_4.getText().trim());
			recorderModel.setWeekInclude("".equals(weekinclude_text_5.getText())?null:weekinclude_text_5.getText().trim());
			recorderModel.setWeekExclude("".equals(weekexclude_text_6.getText())?null:weekexclude_text_6.getText().trim());
			
			recorderModel.setUpper("".equals(upper_text_7.getText())?null:upper_text_7.getText().trim());
			recorderModel.setUpperInfo("".equals(upper_info_text_8.getText())?null:upper_info_text_8.getText().trim());
			recorderModel.setLower("".equals(lower_text_9.getText())?null:lower_text_9.getText().trim());
			recorderModel.setLowerInfo("".equals(lower_info_text_10.getText())?null:lower_info_text_10.getText().trim());
			
			//recorderModel.setWhen("".equals(when_text_11.getText())?null:when_text_11.getText().trim());
			
			if(comboViewer.getCombo().getText().startsWith("变位")){
				recorderModel.setWhen("".equals(when_rschange.getText())?null:when_rschange.getText().trim());
			} else if(comboViewer.getCombo().getText().startsWith("故障")) {
				recorderModel.setWhen("".equals(when_text_11.getText())?null:when_text_11.getText().trim());
			}
			
			recorderModel.setOnInfo("".equals(on_info_text_12.getText())?null:on_info_text_12.getText().trim());
			recorderModel.setOffInfo("".equals(off_info_text_13.getText())?null:off_info_text_13.getText().trim());
			
			recorderModel.setRs_onInfo("".equals(rs_on_info_text_18.getText())?null:rs_on_info_text_18.getText().trim());
			recorderModel.setRs_offInfo("".equals(rs_off_info_text_19.getText())?null:rs_off_info_text_19.getText().trim());
			
			recorderModel.setInfo("".equals(info_text_14.getText())?null:info_text_14.getText().trim());
			recorderModel.setSoeJS("".equals(soe_js_text_15.getText())?null:soe_js_text_15.getText().trim());
			
			recorderModel.setRange("".equals(range_text_16.getText())?null:range_text_16.getText().trim());
//			recorderModel.setInterval("".equals(interval_text_17.getText())?null:interval_text_17.getText().trim());
			
			recorderModel.setShow_alarm(button_alarm_thm.getSelection()?"true":"false");
			recorderModel.setShow_fault(button_fault_thm.getSelection()?"true":"false");
			recorderModel.setShow_rs(button_rschange_thm.getSelection()?"true":"false");
			
			recorderModel.setMinValue("".equals(min_value.getText())?null:min_value.getText().trim());
			recorderModel.setMaxValue("".equals(max_value.getText())?null:max_value.getText().trim());
			recorderModel.setUnitValue("".equals(unit_value.getText())?null:unit_value.getText().trim());
			
			if(tagModel.getType().startsWith("遥脉")) {
				recorderModel.setInterval("".equals(text_yminterval.getText())?null:text_yminterval.getText().trim());
				
			} else if(tagModel.getType().startsWith("遥测")) {
				recorderModel.setInterval("".equals(interval_text_17.getText())?null:interval_text_17.getText().trim());
			}
			
			recorderModel.setYmtagExpression(text_ymtagexpression.getText().trim().equals("")?null:text_ymtagexpression.getText().trim());
			
//			recorderModelDao.updateRecorder(recorderModel, oldName,GetXpathUtil.getTagModelXpath(tagModel));
			
			if(isSub) {//子
				String xpath = GetXpathUtil.getTagModelXpath(tagModel);
				xpath += "/"+ TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='" + subTagModel.getName() + "']";
				recorderModelDao.updateRecorder(recorderModel, oldName,xpath);
			} else {
				recorderModelDao.updateRecorder(recorderModel, oldName,GetXpathUtil.getTagModelXpath(tagModel));
			}
			
			if(editVarDicWindow instanceof EditVarDicWindow) {
				((EditVarDicWindow)editVarDicWindow).initRecorder();//更新列表
			} else {
//				((SubVarDicEditWindow)editVarDicWindow).initRecorder();//更新列表
			}
		}
	}

	/**
	 * 清空其他面板数据
	 */
	private void clearOtherComposite() {
		statistic_combo.setText("");
		computmode_combo.setText("");

		start_text_1.setText("");
		end_text_2.setText("");
		dayinclude_text_3.setText("");
		dayexclude_text_4.setText("");
		weekinclude_text_5.setText("");
		weekexclude_text_6.setText("");
		
		IStructuredSelection iss = (IStructuredSelection) comboViewer.getSelection();
		String ss = (String) iss.getFirstElement();
		if(ss.equals("Alarm")) {
//			upper_text_7.setText("");//Alarm
//			upper_info_text_8.setText("");
//			lower_text_9.setText("");
//			lower_info_text_10.setText("");
//			button_alarm_thm.setSelection(false);

			when_text_11.setText("");//Fault
			on_info_text_12.setText("");
			off_info_text_13.setText("");
			button_fault_thm.setSelection(false);

			rs_on_info_text_18.setText("");//RSchange
			rs_off_info_text_19.setText("");
			button_rschange_thm.setSelection(false);
			when_rschange.setText("");

			info_text_14.setText("");//SOE
			soe_js_text_15.setText("");

			range_text_16.setText("");  //YCTag
			interval_text_17.setText("");
			
			text_yminterval.setText("");//YMTag
			text_ymtagexpression.setText("");
			
			min_value.setText("");
			max_value.setText("");
			unit_value.setText("");
		} else if(ss.equals("Fault")) {
			upper_text_7.setText("");//Alarm
			upper_info_text_8.setText("");
			lower_text_9.setText("");
			lower_info_text_10.setText("");
			button_alarm_thm.setSelection(false);

//			when_text_11.setText("");//Fault
//			on_info_text_12.setText("");
//			off_info_text_13.setText("");
//			button_fault_thm.setSelection(false);

			rs_on_info_text_18.setText("");//RSchange
			rs_off_info_text_19.setText("");
			button_rschange_thm.setSelection(false);
			when_rschange.setText("");

			info_text_14.setText("");//SOE
			soe_js_text_15.setText("");

			range_text_16.setText("");  //YCTag
			interval_text_17.setText("");
			
			text_yminterval.setText("");//YMTag
			text_ymtagexpression.setText("");
			
			min_value.setText("");
			max_value.setText("");
			unit_value.setText("");
		} else if(ss.equals("RSChange")) {
			upper_text_7.setText("");//Alarm
			upper_info_text_8.setText("");
			lower_text_9.setText("");
			lower_info_text_10.setText("");
			button_alarm_thm.setSelection(false);

			when_text_11.setText("");//Fault
			on_info_text_12.setText("");
			off_info_text_13.setText("");
			button_fault_thm.setSelection(false);

//			rs_on_info_text_18.setText("");//RSchange
//			rs_off_info_text_19.setText("");
//			button_rschange_thm.setSelection(false);
//			when_rschange.setText("");

			info_text_14.setText("");//SOE
			soe_js_text_15.setText("");

			range_text_16.setText("");  //YCTag
			interval_text_17.setText("");
			
			text_yminterval.setText("");//YMTag
			text_ymtagexpression.setText("");
			
			min_value.setText("");
			max_value.setText("");
			unit_value.setText("");
		} else if(ss.equals("YCTag")) {
			upper_text_7.setText("");//Alarm
			upper_info_text_8.setText("");
			lower_text_9.setText("");
			lower_info_text_10.setText("");
			button_alarm_thm.setSelection(false);

			when_text_11.setText("");//Fault
			on_info_text_12.setText("");
			off_info_text_13.setText("");
			button_fault_thm.setSelection(false);

			rs_on_info_text_18.setText("");//RSchange
			rs_off_info_text_19.setText("");
			button_rschange_thm.setSelection(false);
			when_rschange.setText("");

			info_text_14.setText("");//SOE
			soe_js_text_15.setText("");

//			range_text_16.setText("");  //YCTag
//			interval_text_17.setText("");
			
			text_yminterval.setText("");//YMTag
			text_ymtagexpression.setText("");
			
			min_value.setText("");
			max_value.setText("");
			unit_value.setText("");
		} else if(ss.equals("YMTag")) {
			upper_text_7.setText("");//Alarm
			upper_info_text_8.setText("");
			lower_text_9.setText("");
			lower_info_text_10.setText("");
			button_alarm_thm.setSelection(false);

			when_text_11.setText("");//Fault
			on_info_text_12.setText("");
			off_info_text_13.setText("");
			button_fault_thm.setSelection(false);

			rs_on_info_text_18.setText("");//RSchange
			rs_off_info_text_19.setText("");
			button_rschange_thm.setSelection(false);
			when_rschange.setText("");

			info_text_14.setText("");//SOE
			soe_js_text_15.setText("");

			range_text_16.setText("");  //YCTag
			interval_text_17.setText("");
			
//			text_yminterval.setText("");//YMTag
//			text_ymtagexpression.setText("");
			
//			min_value.setText("");
//			max_value.setText("");
//			unit_value.setText("");
		}
		
		
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			RecorderEditorWindow window = new RecorderEditorWindow(null, null,null,true);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), newShell);
		super.configureShell(newShell);
		newShell.setText("存储器编辑器");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(422, 434);
	}
}
