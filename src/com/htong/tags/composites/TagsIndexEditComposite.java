package com.htong.tags.composites;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.model.IndexModel;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;
import com.htong.ui.tree.RootTreeModel;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
/**
 * 标签索引编辑面板
 * @author 赵磊
 */
public class TagsIndexEditComposite extends Composite {
	private Text text;
	private IndexModel indexModel;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TagsIndexEditComposite(final Composite parent, int style, final IndexModel indexModel) {
		super(parent, style);
		this.indexModel = indexModel;
		GridLayout gridLayout = new GridLayout(2, false);
		setLayout(gridLayout);
		
		Label name = new Label(this, SWT.NONE);
		name.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		name.setText("标签索引名字：");
		
		text = new Text(this, SWT.BORDER);
		text.setText(indexModel.getName()==null?"":indexModel.getName());
		
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 200;
		text.setLayoutData(gd_text);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if("".equals(text.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
					return;
				}
				if(indexModel.getName() == null) {//新建
					indexModel.setName(text.getText());
					IndexModelDaoImpl indexModeDao = new IndexModelDaoImpl();
					indexModeDao.addIndexModel(indexModel);
					//更新树
					MainUI.treeViewer.add(new String(RootTreeModel.instanse.labelIndex), indexModel);
					MainUI.treeViewer.setExpandedState(new String(RootTreeModel.instanse.labelIndex), true);
				} else {//编辑
					String oldName = indexModel.getName();
					indexModel.setName(text.getText());
					IndexModelDaoImpl indexModeDao = new IndexModelDaoImpl();
					indexModeDao.updateIndexModel(indexModel,oldName);
					
					//更新树
					MainUI.treeViewer.update(indexModel, null);
				}
				
				((OperatingComposite)parent).setTop("标签索引已保存！");
				
//				MainUI.treeViewer.refresh();//刷新树
				
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

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
