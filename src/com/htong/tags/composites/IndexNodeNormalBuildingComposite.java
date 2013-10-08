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
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 标签索引编辑面板
 * 
 * @author 赵磊
 * 
 */
public class IndexNodeNormalBuildingComposite extends Composite {


	private Text text_name;
	private IndexNodeModel indexNodeModel;
	private Text area_text;

	private Text text_type;
	private Text text_code;
	private Text text_air_area;
	private Text text_heat_area;
	private Text text_addr;
	private Text text_year;
	private Text text_floors;
	private Text text_function;
	
	private IndexNodeModelDaoImpl indexNodeDao = new IndexNodeModelDaoImpl();
	private Text text_people_num;
	private Text text_intro;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IndexNodeNormalBuildingComposite(final Composite parent, int style,
			final IndexNodeModel indexNodeModel) {
		super(parent, style);

		this.indexNodeModel = indexNodeModel;
		GridLayout gridLayout = new GridLayout(4, false);
		setLayout(gridLayout);

		Label name = new Label(this, SWT.NONE);
		name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		name.setText("名称：");

		text_name = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_text_name.widthHint = 200;
		text_name.setLayoutData(gd_text_name);
		text_name.setText(indexNodeModel.getName() == null ? ""
				: indexNodeModel.getName());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
				Label label_1 = new Label(this, SWT.NONE);
				label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_1.setText("单位代码（建筑类别）：");
		
				
				text_type = new Text(this, SWT.BORDER);
				GridData gd_text_type = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd_text_type.widthHint = 100;
				text_type.setLayoutData(gd_text_type);
				text_type.setText(indexNodeModel.getType() == null ? ""
						: indexNodeModel.getType());
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setText("（A：办公建筑，B：商场建筑，……）");
		new Label(this, SWT.NONE);
		
		Label label_2 = new Label(this, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("建筑识别编码：");
		
		text_code = new Text(this, SWT.BORDER);
		GridData gd_text_code = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_code.widthHint = 100;
		text_code.setLayoutData(gd_text_code);
		text_code.setText(indexNodeModel.getCode() == null ? ""
				: indexNodeModel.getCode());
		
		Label label_3 = new Label(this, SWT.NONE);
		label_3.setText("（001，002,003，……）");
						new Label(this, SWT.NONE);
				
						Label label = new Label(this, SWT.NONE);
						label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
								1, 1));
						label.setText("建筑面积：");
				
						area_text = new Text(this, SWT.BORDER);
						GridData gd_area_text = new GridData(SWT.FILL, SWT.CENTER, true,
								false, 1, 1);
						gd_area_text.widthHint = 100;
						area_text.setLayoutData(gd_area_text);
						
						area_text.setText(indexNodeModel.getArea() != null?indexNodeModel.getArea():"");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(this, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("空调面积：");
		
		text_air_area = new Text(this, SWT.BORDER);
		text_air_area.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_air_area.setText(indexNodeModel.getAir_condition_area() == null ? ""
				: indexNodeModel.getAir_condition_area());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label label_4 = new Label(this, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("采暖面积：");
		
		text_heat_area = new Text(this, SWT.BORDER);
		text_heat_area.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_heat_area.setText(indexNodeModel.getHeating_area() == null ? ""
				: indexNodeModel.getHeating_area());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label label_11 = new Label(this, SWT.NONE);
		label_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_11.setText("容纳人数：");
		
		text_people_num = new Text(this, SWT.BORDER);
		text_people_num.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_people_num.setText(indexNodeModel.getPeople_num() == null ? "" : indexNodeModel.getPeople_num());
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label lblNewLabel_2 = new Label(this, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("建筑地址：");
		
		text_addr = new Text(this, SWT.BORDER);
		text_addr.setText("<dynamic>");
		text_addr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_addr.setText(indexNodeDao.getBuildingPropTest(indexNodeModel.getName(), "建筑地址"));
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label label_5 = new Label(this, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText("建筑年代：");
		
		text_year = new Text(this, SWT.BORDER);
		text_year.setText("<dynamic>");
		text_year.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_year.setText(indexNodeDao.getBuildingPropTest(indexNodeModel.getName(), "建筑年代"));
		
		Label label_8 = new Label(this, SWT.NONE);
		label_8.setText("（如“2005”）");
		new Label(this, SWT.NONE);
		
		Label label_6 = new Label(this, SWT.NONE);
		label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_6.setText("建筑层数：");
		
		text_floors = new Text(this, SWT.BORDER);
		text_floors.setText("<dynamic>");
		text_floors.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_floors.setText(indexNodeDao.getBuildingPropTest(indexNodeModel.getName(), "建筑层数"));
		
		Label label_9 = new Label(this, SWT.NONE);
		label_9.setText("（如“8”）");
		new Label(this, SWT.NONE);
		
		Label label_7 = new Label(this, SWT.NONE);
		label_7.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_7.setText("建筑功能：");
		
		text_function = new Text(this, SWT.BORDER);
		text_function.setText("<dynamic>");
		text_function.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text_function.setText(indexNodeDao.getBuildingPropTest(indexNodeModel.getName(), "建筑功能"));
		
		Label label_10 = new Label(this, SWT.NONE);
		label_10.setText("（如“办公”）");
		new Label(this, SWT.NONE);
		
		Label label_12 = new Label(this, SWT.NONE);
		label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_12.setText("建筑简介：");
		
		text_intro = new Text(this, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		text_intro.setText(indexNodeDao.getBuildingIntro(indexNodeModel.getName()));
		GridData gd_text_intro = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_text_intro.heightHint = 35;
		text_intro.setLayoutData(gd_text_intro);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4,
				1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if ("".equals(text_name.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
					return;
				}
				if (indexNodeModel.getName() == null) {// 新建
					indexNodeModel.setName(text_name.getText().trim());
					indexNodeModel.setArea("".equals(area_text.getText().trim())?null:area_text.getText().trim());
					indexNodeModel.setType("".equals(text_type.getText().trim())?null:text_type.getText().trim());
					indexNodeModel.setCode("".equals(text_code.getText().trim())?null:text_code.getText().trim());
					
					indexNodeModel.setAir_condition_area("".equals(text_air_area.getText().trim())?null:text_air_area.getText().trim());
					indexNodeModel.setHeating_area("".equals(text_heat_area.getText().trim())?null:text_heat_area.getText().trim());
					indexNodeModel.setPeople_num("".equals(text_people_num.getText().trim())?null:text_people_num.getText().trim());


					indexNodeDao.addElement(indexNodeModel);
					
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑地址", "".equals(text_addr.getText().trim())?null:text_addr.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑年代", "".equals(text_year.getText().trim())?null:text_year.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑层数", "".equals(text_floors.getText().trim())?null:text_floors.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑功能", "".equals(text_function.getText().trim())?null:text_function.getText().trim());

					indexNodeDao.addOrDeleteBuildingIntro(indexNodeModel.getName(), "".equals(text_intro.getText().trim())?null:text_intro.getText().trim());
					
					MainUI.treeViewer.add(indexNodeModel.getParentObject(),
							indexNodeModel); // 更新树
					MainUI.treeViewer.setExpandedState(
							indexNodeModel.getParentObject(), true); // 展开新节点

				} else {// 编辑
					String oldName = indexNodeModel.getName();
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑地址", "".equals(text_addr.getText().trim())?null:text_addr.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑年代", "".equals(text_year.getText().trim())?null:text_year.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑层数", "".equals(text_floors.getText().trim())?null:text_floors.getText().trim());
					indexNodeDao.addOrDeleteBuildingProp(indexNodeModel.getName(), "建筑功能", "".equals(text_function.getText().trim())?null:text_function.getText().trim());
					
					indexNodeModel.setName(text_name.getText());
					indexNodeModel.setArea("".equals(area_text.getText().trim())?null:area_text.getText().trim());
					indexNodeModel.setType("".equals(text_type.getText().trim())?null:text_type.getText().trim());
					indexNodeModel.setCode("".equals(text_code.getText().trim())?null:text_code.getText().trim());

					indexNodeModel.setAir_condition_area("".equals(text_air_area.getText().trim())?null:text_air_area.getText().trim());
					indexNodeModel.setHeating_area("".equals(text_heat_area.getText().trim())?null:text_heat_area.getText().trim());
					indexNodeModel.setPeople_num("".equals(text_people_num.getText().trim())?null:text_people_num.getText().trim());
					
					indexNodeDao.addOrDeleteBuildingIntro(indexNodeModel.getName(), "".equals(text_intro.getText().trim())?null:text_intro.getText().trim());
					
					indexNodeDao.updateIndexNodeModel(indexNodeModel,
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
