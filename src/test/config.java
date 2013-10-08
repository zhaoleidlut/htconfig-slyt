package test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class config {

	protected Shell shell;
	private Text text_8;
	private Text txtHtdb;
	private Text text_9;
	private Text txtPdm;
	private Text text_13;
	private Text text;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			config window = new config();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(ResourceManager.getPluginImage("htconfig-slyt", "icons/index_main_node.gif"));
		shell.setSize(418, 392);
		shell.setText("系统初始配置");
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("数据库服务");
		group_1.setBounds(27, 10, 333, 161);
		
		Label label_9 = new Label(group_1, SWT.NONE);
		label_9.setBounds(21, 20, 78, 17);
		label_9.setText("数据库类型：");
		
		Combo combo_1 = new Combo(group_1, SWT.NONE);
		combo_1.setBounds(105, 20, 87, 20);
		combo_1.setText("非关系型");
		
		Label label_10 = new Label(group_1, SWT.NONE);
		label_10.setBounds(21, 73, 78, 17);
		label_10.setText("服务器地址：");
		
		text_8 = new Text(group_1, SWT.BORDER);
		text_8.setText("10.67.106.205");
		text_8.setBounds(105, 75, 120, 18);
		
		Label label_11 = new Label(group_1, SWT.NONE);
		label_11.setText("数据库名称：");
		label_11.setBounds(21, 96, 78, 21);
		
		txtHtdb = new Text(group_1, SWT.BORDER);
		txtHtdb.setText("htdb");
		txtHtdb.setBounds(104, 103, 55, 18);
		
		Label label_12 = new Label(group_1, SWT.NONE);
		label_12.setBounds(31, 123, 54, 23);
		label_12.setText("库表驱动：");
		
		text_9 = new Text(group_1, SWT.BORDER);
		text_9.setBounds(105, 128, 131, 18);
		
		Button button = new Button(group_1, SWT.NONE);
		button.setBounds(258, 124, 36, 22);
		button.setText("选择");
		
		Label label_13 = new Label(group_1, SWT.NONE);
		label_13.setText("数据库：");
		label_13.setBounds(44, 47, 55, 17);
		
		Combo combo_2 = new Combo(group_1, SWT.NONE);
		combo_2.setBounds(105, 47, 87, 20);
		combo_2.setText("MongoDB");
		
		Group group_2 = new Group(shell, SWT.NONE);
		group_2.setText("页面初始配置");
		group_2.setBounds(27, 178, 333, 154);
		
		Label label_15 = new Label(group_2, SWT.NONE);
		label_15.setText("系统标题配置：");
		label_15.setBounds(21, 28, 99, 17);
		
		txtPdm = new Text(group_2, SWT.BORDER);
		txtPdm.setText("PDM2000 数字化油田监控系统");
		txtPdm.setBounds(126, 25, 186, 18);
		
		Label label_17 = new Label(group_2, SWT.NONE);
		label_17.setText("页面皮肤选择：");
		label_17.setBounds(21, 61, 99, 18);
		
		text_13 = new Text(group_2, SWT.BORDER);
		text_13.setText("灰色方案#1");
		text_13.setBounds(126, 61, 70, 18);
		
		Button button_4 = new Button(group_2, SWT.NONE);
		button_4.setText("进入配置");
		button_4.setBounds(202, 58, 56, 22);
		
		Label label = new Label(group_2, SWT.NONE);
		label.setText("功能模块选择：");
		label.setBounds(21, 98, 99, 17);
		
		text = new Text(group_2, SWT.BORDER);
		text.setText("功能方案#1");
		text.setBounds(126, 98, 70, 18);
		
		Button button_1 = new Button(group_2, SWT.NONE);
		button_1.setText("进入配置");
		button_1.setBounds(202, 95, 56, 22);

	}
}
