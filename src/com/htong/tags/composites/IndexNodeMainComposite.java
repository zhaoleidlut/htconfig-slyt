package com.htong.tags.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.type.IndexTypeEnum;
import com.htong.tags.windows.EditTagEnergyIndexWindow;
import com.htong.tags.windows.EditTagNormalIndexWindow;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * 标签索引编辑面板
 * 
 * @author 赵磊
 * 
 */
public class IndexNodeMainComposite extends Composite {
	private Text text_name;
	private IndexNodeModel indexNodeModel;
	private Text text_number;
	private Text text_transformer_name;
	private Combo combo_transformer_type;
	private Text end_name;
	private Text text_normal;
	private Text text_energy;
	private Text text_channel;
	private Text text_device;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IndexNodeMainComposite(final Composite parent, int style,
			final IndexNodeModel indexNodeModel) {
		super(parent, style);

		this.indexNodeModel = indexNodeModel;
		GridLayout gridLayout = new GridLayout(3, false);
		setLayout(gridLayout);

		Label name = new Label(this, SWT.NONE);
		name.setText("索引名字：");

		text_name = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		text_name.setText(indexNodeModel.getName() == null ? ""
				: indexNodeModel.getName());

		new Label(this, SWT.NONE);

		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("线路代码（编号）：");

		text_number = new Text(this, SWT.BORDER);
		GridData gd_text_number = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_text_number.widthHint = 100;
		text_number.setLayoutData(gd_text_number);
		text_number.setText(indexNodeModel.getNumber() == null ? ""
				: indexNodeModel.getNumber());
		new Label(this, SWT.NONE);
		
		Label label_3 = new Label(this, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("末端名字：");
		
		end_name = new Text(this, SWT.BORDER);
		GridData gd_end_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_end_name.widthHint = 100;
		end_name.setLayoutData(gd_end_name);
		end_name.setText(indexNodeModel.getEndName() == null?"":indexNodeModel.getEndName());
		new Label(this, SWT.NONE);
		
		Group group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		group.setText("类型");
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("变压器类型：");
		
		combo_transformer_type = new Combo(group, SWT.READ_ONLY);
		combo_transformer_type.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(combo_transformer_type.getSelectionIndex()<=0){
					text_transformer_name.setEnabled(false);
					text_transformer_name.setText("");
				} else {
					text_transformer_name.setEnabled(true);
				}
			}
		});
		
		String comboItems[] = new String[]{"","高压侧","低压侧"}; 
		combo_transformer_type.setItems(comboItems);
		
		if(indexNodeModel.getTransformer_type() == null) {
			combo_transformer_type.select(0);
		} else if(indexNodeModel.getTransformer_type().equals("power-high")) {
			combo_transformer_type.select(1);
		} else if(indexNodeModel.getTransformer_type().equals("power-low")) {
			combo_transformer_type.select(2);
		} else {
			combo_transformer_type.select(0);
		}
		
		
		Label label_2 = new Label(group, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("变压器名字：");
		
		text_transformer_name = new Text(group, SWT.BORDER);
		GridData gd_text_transformer_name = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_transformer_name.widthHint = 90;
		text_transformer_name.setLayoutData(gd_text_transformer_name);
		if(combo_transformer_type.getSelectionIndex()<=0) {
			text_transformer_name.setEnabled(false);
		} else {
			text_transformer_name.setEnabled(true);
			text_transformer_name.setText(indexNodeModel.getTransformer_name() == null ?"" : indexNodeModel.getTransformer_name());
		}
		
		new Label(this, SWT.NONE);
		
		Group group_1 = new Group(this, SWT.NONE);
		group_1.setText("批量关联索引");
		GridData gd_group_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_group_1.heightHint = 110;
		group_1.setLayoutData(gd_group_1);
		
		Label label_4 = new Label(group_1, SWT.NONE);
		label_4.setText("常规分类索引：");
		label_4.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_4.setBounds(10, 20, 84, 15);
		
		text_normal = new Text(group_1, SWT.BORDER);
		text_normal.setEditable(false);
		text_normal.setBounds(10, 43, 510, 20);
		text_normal.setText(indexNodeModel.getNormalIndex()==null?"":indexNodeModel.getNormalIndex());
		
		Button button = new Button(group_1, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new EditTagNormalIndexWindow(indexNodeModel, text_normal).open();
			}
		});
		button.setText("编辑");
		button.setBounds(100, 18, 54, 22);
		
		Button button_1 = new Button(group_1, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new EditTagEnergyIndexWindow(indexNodeModel, text_energy).open();
			}
		});
		button_1.setText("编辑");
		button_1.setBounds(100, 74, 54, 22);
		
		text_energy = new Text(group_1, SWT.BORDER);
		text_energy.setEditable(false);
		text_energy.setBounds(10, 100, 510, 20);
		text_energy.setText(indexNodeModel.getEnergyIndex()==null?"":indexNodeModel.getEnergyIndex());
		
		Label label_5 = new Label(group_1, SWT.NONE);
		label_5.setText("能耗分类分项：");
		label_5.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_5.setBounds(10, 77, 84, 17);
		new Label(this, SWT.NONE);
		
		Group group_2 = new Group(this, SWT.NONE);
		GridData gd_group_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_group_2.widthHint = 70;
		group_2.setLayoutData(gd_group_2);
		group_2.setText("批量关联通道和设备");
		
		Label label_6 = new Label(group_2, SWT.NONE);
		label_6.setBounds(10, 29, 92, 17);
		label_6.setText("采集通道序号：");
		
		text_channel = new Text(group_2, SWT.BORDER);
		text_channel.setBounds(108, 26, 92, 23);
		text_channel.setText(indexNodeModel.getChannelId()==null?"":indexNodeModel.getChannelId());
		
		Label label_7 = new Label(group_2, SWT.NONE);
		label_7.setBounds(225, 29, 61, 17);
		label_7.setText("设备地址：");
		
		text_device = new Text(group_2, SWT.BORDER);
		text_device.setBounds(292, 26, 73, 23);
		text_device.setText(indexNodeModel.getDeviceId()==null?"":indexNodeModel.getDeviceId());
		new Label(this, SWT.NONE);
		


		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3,
				1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if ("".equals(text_name.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
					return;
				}

				if(!"".equals(text_number.getText().trim())) {
//					try {
//						int a = Integer.parseInt(text_number.getText().trim());
//					} catch (NumberFormatException e1) {
//						MessageDialog.openError(getShell(), "错误", "输入正确的索引编号！");
//						e1.printStackTrace();
//						return;
//					}
				}
				
				

				if (indexNodeModel.getName() == null) {// 新建
					indexNodeModel.setName(text_name.getText().trim());
					indexNodeModel.setNumber("".equals(text_number.getText()
							.trim()) ? null : text_number.getText().trim());
					indexNodeModel.setEndName("".equals(end_name.getText())?null:end_name.getText());
					indexNodeModel.setEnergyIndex("".equals(text_energy.getText())?null:text_energy.getText());
					indexNodeModel.setNormalIndex("".equals(text_normal.getText())?null:text_normal.getText());
					indexNodeModel.setChannelId("".equals(text_channel.getText())?null:text_channel.getText());
					indexNodeModel.setDeviceId("".equals(text_device.getText())?null:text_device.getText());
					
					if(combo_transformer_type.getSelectionIndex()<=0) {
						indexNodeModel.setTransformer_type(null);
						indexNodeModel.setTransformer_name(null);
					} else if(combo_transformer_type.getSelectionIndex() == 1) {
						indexNodeModel.setTransformer_type("power-high");
						indexNodeModel.setTransformer_name("".equals(text_transformer_name.getText().trim())?null:text_transformer_name.getText().trim());
					} else if(combo_transformer_type.getSelectionIndex() == 2) {
						indexNodeModel.setTransformer_type("power-low");
						indexNodeModel.setTransformer_name("".equals(text_transformer_name.getText().trim())?null:text_transformer_name.getText().trim());
					}

					IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

					indexNodeModelDao.addElement(indexNodeModel);

					MainUI.treeViewer.add(indexNodeModel.getParentObject(),
							indexNodeModel); // 更新树
					MainUI.treeViewer.setExpandedState(
							indexNodeModel.getParentObject(), true); // 展开新节点

				} else {// 编辑
					String oldName = indexNodeModel.getName();
					indexNodeModel.setName(text_name.getText());
					indexNodeModel.setNumber("".equals(text_number.getText()
							.trim()) ? null : text_number.getText().trim());
					indexNodeModel.setEndName("".equals(end_name.getText())?null:end_name.getText());
					indexNodeModel.setEnergyIndex("".equals(text_energy.getText())?null:text_energy.getText());
					indexNodeModel.setNormalIndex("".equals(text_normal.getText())?null:text_normal.getText());
					indexNodeModel.setChannelId("".equals(text_channel.getText())?null:text_channel.getText());
					indexNodeModel.setDeviceId("".equals(text_device.getText())?null:text_device.getText());
					
					if(combo_transformer_type.getSelectionIndex()<=0) {
						indexNodeModel.setTransformer_type(null);
						indexNodeModel.setTransformer_name(null);
					} else if(combo_transformer_type.getSelectionIndex() == 1) {
						indexNodeModel.setTransformer_type("power-high");
						indexNodeModel.setTransformer_name("".equals(text_transformer_name.getText().trim())?null:text_transformer_name.getText().trim());
					} else if(combo_transformer_type.getSelectionIndex() == 2) {
						indexNodeModel.setTransformer_type("power-low");
						indexNodeModel.setTransformer_name("".equals(text_transformer_name.getText().trim())?null:text_transformer_name.getText().trim());
					}

					IndexNodeModelDaoImpl indexNodeModeDao = new IndexNodeModelDaoImpl();
					indexNodeModeDao.updateIndexNodeModel(indexNodeModel,
							oldName);

					// 更新树
					MainUI.treeViewer.update(indexNodeModel, null);

				}

				((OperatingComposite) parent).setTop("标签已保存！");
			}
		});
		btnNewButton.setBounds(45, 39, 72, 22);
		btnNewButton.setText("保  存");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				((OperatingComposite) parent).setTop("操作已取消！");
			}
		});
		btnNewButton_1.setBounds(173, 39, 72, 22);
		btnNewButton_1.setText("取  消");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
