package test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

public class jb {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private Text text_8;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			jb window = new jb();
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
		shell.setSize(454, 540);
		shell.setText("SWT Application");
		
		Button button = new Button(shell, SWT.NONE);
		button.setText("保 存(&S)");
		button.setBounds(75, 469, 55, 27);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setText("取 消(&C)");
		button_1.setBounds(164, 469, 56, 27);
		
		Group group = new Group(shell, SWT.NONE);
		group.setText("监控对象基本信息");
		group.setBounds(24, 10, 279, 229);
		
		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(79, 23, 44, 12);
		lblNewLabel.setText("名称：");
		
		text = new Text(group, SWT.BORDER);
		text.setText("草13-平3");
		text.setBounds(127, 20, 97, 18);
		
		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setBounds(79, 45, 44, 12);
		lblNewLabel_1.setText("类型：");
		
		Combo combo = new Combo(group, SWT.NONE);
		combo.setBounds(127, 41, 97, 20);
		combo.setText("单井");
		
		Label label = new Label(group, SWT.NONE);
		label.setText("抽油机类型：");
		label.setBounds(43, 72, 71, 12);
		
		Combo combo_1 = new Combo(group, SWT.NONE);
		combo_1.setBounds(127, 67, 97, 20);
		combo_1.setText("游梁式");
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("抽油机型号：");
		label_1.setBounds(43, 100, 71, 12);
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setBounds(127, 98, 97, 18);
		
		Label label_2 = new Label(group, SWT.NONE);
		label_2.setBounds(31, 126, 84, 12);
		label_2.setText("泵径（毫米）：");
		
		text_2 = new Text(group, SWT.BORDER);
		text_2.setText("56");
		text_2.setBounds(127, 122, 97, 18);
		
		Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("泵深（米）：");
		label_3.setBounds(43, 152, 78, 12);
		
		text_3 = new Text(group, SWT.BORDER);
		text_3.setText("998.7");
		text_3.setBounds(127, 148, 97, 18);
		
		Label label_4 = new Label(group, SWT.NONE);
		label_4.setText("抽油杆组成：");
		label_4.setBounds(43, 177, 78, 12);
		
		Button btnNewButton = new Button(group, SWT.NONE);
		btnNewButton.setBounds(127, 173, 60, 22);
		btnNewButton.setText("进入配置");
		
		Label label_5 = new Label(group, SWT.NONE);
		label_5.setBounds(43, 201, 71, 12);
		label_5.setText("配重块位置：");
		
		text_4 = new Text(group, SWT.BORDER);
		text_4.setText("25");
		text_4.setBounds(127, 200, 97, 18);
		
		Group grpGis = new Group(shell, SWT.NONE);
		grpGis.setText("GIS配置");
		grpGis.setBounds(24, 241, 279, 82);
		
		Label lblx = new Label(grpGis, SWT.NONE);
		lblx.setBounds(25, 22, 36, 12);
		lblx.setText("坐标X：");
		
		Label lbly = new Label(grpGis, SWT.NONE);
		lbly.setText("坐标Y：");
		lbly.setBounds(131, 22, 36, 12);
		
		text_5 = new Text(grpGis, SWT.BORDER);
		text_5.setText("156");
		text_5.setBounds(68, 18, 45, 18);
		
		text_6 = new Text(grpGis, SWT.BORDER);
		text_6.setText("368");
		text_6.setBounds(173, 18, 45, 18);
		
		Label label_6 = new Label(grpGis, SWT.NONE);
		label_6.setBounds(25, 43, 54, 12);
		label_6.setText("图标：");
		
		Button button_2 = new Button(grpGis, SWT.CHECK);
		button_2.setBounds(25, 63, 93, 16);
		button_2.setText("是否加入链接");
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("视频服务器配置");
		group_1.setBounds(24, 326, 279, 86);
		
		Label lblNewLabel_2 = new Label(group_1, SWT.NONE);
		lblNewLabel_2.setBounds(20, 22, 82, 12);
		lblNewLabel_2.setText("服务器地址：");
		
		Label lblNewLabel_3 = new Label(group_1, SWT.NONE);
		lblNewLabel_3.setBounds(55, 43, 36, 12);
		lblNewLabel_3.setText("端口：");
		
		text_7 = new Text(group_1, SWT.BORDER);
		text_7.setText("10.67.106.205");
		text_7.setBounds(103, 19, 111, 18);
		
		text_8 = new Text(group_1, SWT.BORDER);
		text_8.setText("3500");
		text_8.setBounds(103, 40, 70, 18);
		
		Label lblNewLabel_4 = new Label(group_1, SWT.NONE);
		lblNewLabel_4.setBounds(30, 67, 68, 12);
		lblNewLabel_4.setText("联动控制：");
		
		Button button_3 = new Button(group_1, SWT.NONE);
		button_3.setBounds(103, 61, 60, 22);
		button_3.setText("进入配置");

	}
}
