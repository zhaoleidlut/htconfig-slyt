package com.htong.tags.composites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.comm.DataTypeEnum;
import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;

/**
 * 标签类型编辑面板
 * 
 * @author 赵磊
 * 
 */
public class TagsTypeEditComposite extends Composite {
	private static final Logger log = Logger
			.getLogger(TagsTypeEditComposite.class);

	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof String) {
				String str = (String) element;
				if (str.equals(DataTypeEnum.bool.toString())) {
					return DataTypeEnum.bool.getValue();
				} else if (str.equals(DataTypeEnum.string.toString())) {
					return DataTypeEnum.string.getValue();
				} else if (str.equals(DataTypeEnum.number.toString())) {
					return DataTypeEnum.number.getValue();
				} else if(str.equals(DataTypeEnum.bin.toString())) {
					return DataTypeEnum.bin.getValue();
				}
			}
			return "";
		}
	}

	private Text text_name;
	private TagTypeModel tagTypeModel;
	private List<String> stringList; // 数据类型集合
	

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TagsTypeEditComposite(final Composite parent, int style,
			final TagTypeModel tagTypeModel, boolean nameEditable, boolean typeEditable) {
		super(parent, style);
		this.tagTypeModel = tagTypeModel;
		// 初始化数据类型
	
		stringList = new ArrayList<String>();
		stringList.add("");
		stringList.add(DataTypeEnum.bool.toString());
		stringList.add(DataTypeEnum.string.toString());
		stringList.add(DataTypeEnum.number.toString());
		stringList.add(DataTypeEnum.bin.toString());

		GridLayout gridLayout = new GridLayout(2, false);
		setLayout(gridLayout);

		Label name = new Label(this, SWT.NONE);
		name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));
		name.setText("标签类型名字：");

		text_name = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_text_name.widthHint = 200;
		text_name.setLayoutData(gd_text_name);
		text_name.setText(tagTypeModel.getName() == null ? "" : tagTypeModel
				.getName());// 初始化名字
		text_name.setEditable(nameEditable);

		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("数据类型：");

		final ComboViewer comboViewer = new ComboViewer(this, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_combo.widthHint = 185;
		combo.setLayoutData(gd_combo);

		comboViewer.setLabelProvider(new ViewerLabelProvider());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(stringList);
		// 初始化数据类型
		if (tagTypeModel.getDataType() != null) {
			combo.select(stringList.indexOf(tagTypeModel.getDataType()));
		} else
			combo.setText("");
		
		combo.setEnabled(typeEditable);

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2,
				1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if ("".equals(text_name.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
					return;
				}
				if (tagTypeModel.getName() == null) {// 新建
					tagTypeModel.setName(text_name.getText().trim());
					IStructuredSelection is = (IStructuredSelection) comboViewer
							.getSelection();
					Object o = is.getFirstElement();
					String content = (String) o;
					if (content != null && !"".equals(content)) {
						tagTypeModel.setDataType(content);
					} else {
						tagTypeModel.setDataType(null);
					}
					
					//常用的下级不添加类型
					Object parentO = tagTypeModel.getParentObject();
					if(parentO instanceof TagTypeModel) {
						TagTypeModel parentT = (TagTypeModel)parentO;
						if(parentT.getDataType() != null && parentT.getName().equals("遥信")) {
							tagTypeModel.setDataType(null);
						} else if(parentT.getDataType() != null && parentT.getName().equals("遥测")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥控")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥脉")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥调")) {
							tagTypeModel.setDataType(null);
						}
					}
					

					TagTypeModelDaoImpl tagTypeModelDao = new TagTypeModelDaoImpl();
					
					tagTypeModelDao.addElement(tagTypeModel);

					MainUI.treeViewer.add(tagTypeModel.getParentObject(),
							tagTypeModel); // 更新树
					MainUI.treeViewer.setExpandedState(
							tagTypeModel.getParentObject(), true); // 展开新节点
				} else {// 编辑
					String oldName = tagTypeModel.getName();
					tagTypeModel.setName(text_name.getText());
					IStructuredSelection is = (IStructuredSelection) comboViewer
							.getSelection();
					Object o = is.getFirstElement();
					String content = (String) o;
					if (content != null && !"".equals(content)) {
						tagTypeModel.setDataType(content);
					} else {
						tagTypeModel.setDataType(null);
					}
					
					Object parentO = tagTypeModel.getParentObject();
					if(parentO instanceof TagTypeModel) {
						TagTypeModel parentT = (TagTypeModel)parentO;
						if(parentT.getDataType() != null && parentT.getName().equals("遥信")) {
							tagTypeModel.setDataType(null);
						} else if(parentT.getDataType() != null && parentT.getName().equals("遥测")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥控")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥脉")) {
							tagTypeModel.setDataType(null);
						} if(parentT.getDataType() != null && parentT.getName().equals("遥调")) {
							tagTypeModel.setDataType(null);
						}
					}

					TagTypeModelDaoImpl tagTypeModelDao = new TagTypeModelDaoImpl();
					tagTypeModelDao.updateTagTypeModel(tagTypeModel, oldName);

					// 更新树
					MainUI.treeViewer.update(tagTypeModel, null);
				}

				((OperatingComposite) parent).setTop("标签类型已保存！");
			}
		});
		btnNewButton.setBounds(45, 39, 72, 22);
		btnNewButton.setText("保  存");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				((OperatingComposite)parent).setTop("操作已取消！");
			}
		});
		btnNewButton_1.setBounds(173, 39, 72, 22);
		btnNewButton_1.setText("取  消");
		
		//初始化常用数据类型
			Object parentO = tagTypeModel.getParentObject();
			if(parentO instanceof TagTypeModel) {
				TagTypeModel parentT = (TagTypeModel)parentO;
				if(parentT.getDataType() != null && parentT.getName().equals("遥信")) {
					combo.setText("布尔型");
				} else if(parentT.getDataType() != null && parentT.getName().equals("遥测")) {
					combo.setText("数值型");
				} if(parentT.getDataType() != null && parentT.getName().equals("遥控")) {
					combo.setText("布尔型");
				} if(parentT.getDataType() != null && parentT.getName().equals("遥脉")) {
					combo.setText("数值型");
				} if(parentT.getDataType() != null && parentT.getName().equals("遥调")) {
					combo.setText("数值型");
				}
			}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
