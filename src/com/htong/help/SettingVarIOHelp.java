package com.htong.help;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class SettingVarIOHelp extends Dialog {
	private Text text;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SettingVarIOHelp(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		text = new Text(container, SWT.READ_ONLY);
		text.setText("\r\n\t配置转发通道功能，暂未实现。" +
				"\r\n" +
				"\r\n\t不存在的通道，IO信息将不被配置。");
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

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
		button.setText("关闭");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(333, 184);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("设定变量IO信息帮助");
		super.configureShell(newShell);
	}
	
	

}
