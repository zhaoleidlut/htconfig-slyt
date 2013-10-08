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

public class SettingDeviceIOHelp extends Dialog {
	private Text text;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SettingDeviceIOHelp(Shell parentShell) {
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
		text.setText("\r\n   标签索引：" 
		+ "\r\n\t\t若配置“索引编号”则显示“索引编号”。" 
				+ "\r\n"
				+ "\r\n   功能码：" 
				+ "\r\n\t\t“遥信”、“遥测”、“遥脉”默认3，“遥控”默认5，均可更改。" 
				+ "\r\n"
				+ "\r\n   偏移量：" 
				+ "\r\n\t\t“遥信”偏移量范围：0-15，其他变量类型的偏移量不可配置。" 
				+ "\r\n"
				+ "\r\n   字节长度：" 
				+ "\r\n\t\t“遥控”无字节长度。" 
				+ "\r\n"
				+ "\r\n   值类型：" 
				+ "\r\n\t\t 0--字节数组" + "\r\n\t\t 1--布尔型"
				+ "\r\n\t\t 2--有符号整型16位" + "\r\n\t\t 3--无符号整型16位"
				+ "\r\n\t\t 4--有符号整型32位" + "\r\n\t\t 5--无符号整型32位"
				+ "\r\n\t\t 6--浮点型32位" + "\r\n\t\t 7--双浮点型64位"
				+ "\r\n\t\t 8--高字节8位" + "\r\n\t\t 9--低字节8位"
				+ "\r\n\t\t 10--MOD10000" + "\r\n\t\t 11--MOD10000_1"
				+ "\r\n\t\t 12--BCD" + "\r\n\t\t 13--ASCII"
				+ "\r\n\t\t 14--ASCII转BCD" + "\r\n\t\t 15--datetime"
				+ "\r\n"
		+ "\r\n   基数：" 
		+ "\r\n\t\t“遥测”、“遥信”、“遥脉”默认“基数”为0，“遥控”的“基数”表示遥控的值。" 
		+ "\r\n"
		+ "\r\n   系数：" 
		+ "\r\n\t\t“遥测”、“遥信”、“遥脉”默认“系数”为1。" 
		+ "\r\n"
		+ "\r\n   优先级：" 
		+ "\r\n\t\t“优先级”范围0-10,0表示不采集" 
				);
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
		return new Point(590, 459);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("设定设备IO信息帮助");
		super.configureShell(newShell);
	}

}
