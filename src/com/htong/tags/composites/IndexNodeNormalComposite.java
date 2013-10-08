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
public class IndexNodeNormalComposite extends Composite {


	private Text text_name;
	private IndexNodeModel indexNodeModel;
	private Text area_text;
	private Text text_type;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IndexNodeNormalComposite(final Composite parent, int style,
			final IndexNodeModel indexNodeModel) {
		super(parent, style);

		this.indexNodeModel = indexNodeModel;
		GridLayout gridLayout = new GridLayout(4, false);
		setLayout(gridLayout);

		Label name = new Label(this, SWT.NONE);
		name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		name.setText("名称：");

		text_name = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		text_name.setText(indexNodeModel.getName() == null ? ""
				: indexNodeModel.getName());
		new Label(this, SWT.NONE);

		new Label(this, SWT.NONE);

		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("面积：");

		area_text = new Text(this, SWT.BORDER);
		GridData gd_area_text = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_area_text.widthHint = 100;
		area_text.setLayoutData(gd_area_text);
		
		area_text.setText(indexNodeModel.getArea() != null?indexNodeModel.getArea():"");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		Label label_1 = new Label(this, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("代码（类型）：");
		
		text_type = new Text(this, SWT.BORDER);
		GridData gd_text_type = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_type.widthHint = 100;
		text_type.setLayoutData(gd_text_type);
		text_type.setText(indexNodeModel.getType() != null?indexNodeModel.getType():"");
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("指定分区、楼层、房间号等");
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

					IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

					indexNodeModelDao.addElement(indexNodeModel);

					MainUI.treeViewer.add(indexNodeModel.getParentObject(),
							indexNodeModel); // 更新树
					MainUI.treeViewer.setExpandedState(
							indexNodeModel.getParentObject(), true); // 展开新节点

				} else {// 编辑
					String oldName = indexNodeModel.getName();
					indexNodeModel.setName(text_name.getText());
					indexNodeModel.setArea("".equals(area_text.getText().trim())?null:area_text.getText().trim());
					indexNodeModel.setType("".equals(text_type.getText().trim())?null:text_type.getText().trim());

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
