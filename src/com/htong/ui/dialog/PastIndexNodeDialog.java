package com.htong.ui.dialog;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.ui.GetVarNum;

public class PastIndexNodeDialog extends Dialog {
	private Text text;
	private Text text_1;
	private IndexNodeModel indexNode;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PastIndexNodeDialog(Shell parentShell, IndexNodeModel indexNode) {
		
		super(parentShell);
		
		this.indexNode = indexNode;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("粘贴选项");
		super.configureShell(newShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		
		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("索引名字：");
		
		text = new Text(container, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 100;
		text.setLayoutData(gd_text);
		text.setText(indexNode.getName());
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("索引编号：");
		
		text_1 = new Text(container, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_1.widthHint = 70;
		text_1.setLayoutData(gd_text_1);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.setText("确定");
		Button button_1 = createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		button_1.setText("取消");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(302, 184);
	}

	@Override
	public boolean close() {
		if("".equals(text.getText().trim())){
			MessageDialog.openError(getShell(), "错误", "请输入索引节点名字！" );
			return false;
		}
		indexNode.setName(text.getText().trim());
		indexNode.setNumber("".equals(text_1.getText().trim())?null:text_1.getText().trim());
		Element myElement = indexNode.getElement();
		myElement.addAttribute(IndexNodeModel.NAME_ATTR, text.getText().trim());
		myElement.addAttribute(IndexNodeModel.NUMBER_ATTR, "".equals(text_1.getText().trim())?null:text_1.getText().trim());
		
		return super.close();
	}
	
	

}
