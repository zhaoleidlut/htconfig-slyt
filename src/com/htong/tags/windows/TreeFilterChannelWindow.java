package com.htong.tags.windows;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.htong.ui.MainUI;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class TreeFilterChannelWindow extends ApplicationWindow {
	private Text text;

	/**
	 * Create the application window.
	 */
	public TreeFilterChannelWindow() {
		super(null);
		setBlockOnOpen(true);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Label label = new Label(container, SWT.NONE);
		label.setBounds(24, 31, 107, 17);
		label.setText("通道名字或序号：");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(137, 28, 123, 23);
		
		Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainUI.channelFilterStr = null;
				MainUI.treeViewer.refresh();
			}
		});
		button.setBounds(289, 74, 80, 27);
		button.setText("显示全部");
		
		Button button_1 = new Button(container, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainUI.channelFilterStr = text.getText().trim();
				MainUI.treeViewer.refresh();
			}
		});
		button_1.setBounds(289, 26, 80, 27);
		button_1.setText("查找");

		return container;
	}



	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			TreeFilterChannelWindow window = new TreeFilterChannelWindow();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("查找通道");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(422, 153);
	}

}
