package com.htong.tags.composites;

import org.dom4j.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.OperatingComposite;

/**
 * 采集通道编辑面板
 * 
 * @author 赵磊
 * 
 */
public class ChannelsEditComposite extends Composite {
	private String[] hourStr = new String[24];

	private Combo hour;
	private Combo minute;
	private final String TIMER_ATTR = "timer";

	public ChannelsEditComposite(final Composite parent, int style) {
		super(parent, style);

		final Element e = (Element) XMLDocumentFactory.instance
				.getMainDocument().selectSingleNode("/root/channels");
		String value = e.attributeValue(TIMER_ATTR);
		String myHour = null;
		String myMinute = null;
		if (value != null) {
			myHour = value.split(":")[0];
			myMinute = value.split(":")[1];
		}

		// 列出系统可用串口

		for (int i = 0; i < 24; i++) {
			hourStr[i] = String.valueOf(i);
		}
		setLayout(new GridLayout(1, false));

		Composite composite_2 = new Composite(this, SWT.NONE);
		RowLayout rl_composite_2 = new RowLayout(SWT.VERTICAL);
		rl_composite_2.wrap = false;
		composite_2.setLayout(rl_composite_2);

		Group group = new Group(composite_2, SWT.NONE);
		group.setLayout(null);
		group.setLayoutData(new RowData(300, 130));
		group.setText("采集时间");

		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(8, 26, 30, 17);
		label_1.setText("时：");

		hour = new Combo(group, SWT.READ_ONLY);
		hour.setBounds(44, 23, 64, 25);
		hour.setItems(hourStr);
		if (myHour == null) {
			hour.select(7);
		} else {
			hour.select(Integer.valueOf(myHour));
		}

		Label label = new Label(group, SWT.NONE);
		label.setText("分：");
		label.setBounds(126, 26, 24, 17);

		minute = new Combo(group, SWT.READ_ONLY);
		minute.setItems(new String[] { "00", "10", "20", "30", "40", "50" });
		minute.setBounds(154, 23, 64, 25);
		if (myMinute == null) {
			minute.select(5);
		} else {
			minute.setText(myMinute);
		}

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(null);

		Button btns = new Button(composite_1, SWT.NONE);
		btns.setBounds(74, 5, 55, 27);
		btns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e1) {
				String timer = hour.getText() + ":" + minute.getText();
				e.addAttribute(TIMER_ATTR, timer);
				XMLDocumentFactory.instance.saveMainDocument();
				((OperatingComposite)parent).setTop("已更新！");
			}
		});
		btns.setText("保 存(&S)");

		Button btnc = new Button(composite_1, SWT.NONE);
		btnc.setBounds(180, 5, 56, 27);
		btnc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((OperatingComposite) parent).setTop("已取消操作！");
			}
		});
		btnc.setText("取 消(&C)");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
