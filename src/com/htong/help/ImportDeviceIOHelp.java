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

public class ImportDeviceIOHelp extends Dialog {
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ImportDeviceIOHelp(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(false);
		setShellStyle(SWT.MAX | SWT.TITLE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		text = new Text(container, SWT.READ_ONLY | SWT.V_SCROLL);
		text.setText("\r\n   配置了“标签索引”的变量才会被导入，配置时直接输入“标签索引”的“索引编号”。" );
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
		button.setText("关闭");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 203);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("导入设备IO信息帮助");
		super.configureShell(newShell);
	}

}
