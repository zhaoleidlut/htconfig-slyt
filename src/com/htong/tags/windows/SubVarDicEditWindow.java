package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.htong.tags.daoImpl.GeneratorModelDaoImpl;
import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.daoImpl.RecorderModelDaoImpl;
import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.daoImpl.TriggerModelDaoImpl;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.GeneratorModel;
import com.htong.tags.model.tag.RecorderModel;
import com.htong.ui.GetXpathUtil;
import com.htong.util.LayoutUtil;

/**
 * 编辑变量词典窗口
 * 
 * @author 赵磊
 * 
 */
public class SubVarDicEditWindow extends ApplicationWindow {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			return ((RecorderModel) element).getName() == null ? ""
					: ((RecorderModel) element).getName();
		}
	}

	private static final Logger log = Logger
			.getLogger(SubVarDicEditWindow.class);

	private TagModel tagModel; // 当前所选的标签
	private TagModel subTagModel; // 子标签
	private Text text_name;
	private Text text_tagtype;
	private Text text_value;

	private TagModelDaoImpl tagModelDao = new TagModelDaoImpl();
	private IndexModelDaoImpl indexModeDao = new IndexModelDaoImpl();
	private RecorderModelDaoImpl recorderModelDao = new RecorderModelDaoImpl();
	private GeneratorModelDaoImpl generatorModelDao = new GeneratorModelDaoImpl();

	DefaultListModel dlm;
	private EditVarDicWindow evdw;

	/**
	 * @wbp.parser.constructor
	 */
	public SubVarDicEditWindow(Shell parentShell, TagModel tagModel,
			TagModel subTagModel, EditVarDicWindow evdw) {
		super(parentShell);
		this.tagModel = tagModel;
		this.subTagModel = subTagModel;
		this.evdw = evdw;
		init();
	}

	/**
	 * 初始化配置
	 * 
	 * @param xpath
	 */
	private void init() {

	}

	@Override
	protected void configureShell(Shell shell) {
		
		shell.setSize(500, 300);
		shell.setText("编辑子变量");
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), shell);
		super.configureShell(shell);

	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(567, 300);
	}

	protected Control createContents(Composite parent1) {
		Composite parent = new Composite(parent1, SWT.NONE);
		parent.setLayout(new GridLayout(1, false));

		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_composite_1.heightHint = 100;
		composite_1.setLayoutData(gd_composite_1);

		Composite composite_basic = new Composite(composite_1, SWT.NONE);
		composite_basic.setLayout(new GridLayout(1, false));
		GridData gd_composite_basic = new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1);
		gd_composite_basic.heightHint = 87;
		gd_composite_basic.widthHint = 468;
		composite_basic.setLayoutData(gd_composite_basic);

		Group group_base = new Group(composite_basic, SWT.NONE);
		group_base.setText("基本信息");
		group_base.setLayout(new GridLayout(5, false));
		group_base.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		Label lblNewLabel = new Label(group_base, SWT.NONE);
		lblNewLabel.setText("名称：");

		text_name = new Text(group_base, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_name.widthHint = 70;
		text_name.setLayoutData(gd_text_name);
				
						Label label_3 = new Label(group_base, SWT.NONE);
						label_3.setText("值：");
		
				text_value = new Text(group_base, SWT.BORDER);
				GridData gd_text_value = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_text_value.widthHint = 70;
				text_value.setLayoutData(gd_text_value);
		new Label(group_base, SWT.NONE);

		Label label_1 = new Label(group_base, SWT.NONE);
		label_1.setText("标签类型：");

		text_tagtype = new Text(group_base, SWT.BORDER);
		text_tagtype.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		text_tagtype.setEditable(false);

		Button button_2 = new Button(group_base, SWT.NONE);
		GridData gd_button_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button_2.widthHint = 50;
		button_2.setLayoutData(gd_button_2);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new EditTagTypeWindow(tagModel, text_tagtype).open();
			}
		});
		button_2.setText("编 辑");
		
				Composite composite_6 = new Composite(composite_1, SWT.NONE);
				composite_6.setLayout(new GridLayout(2, false));
				
						Button button_1 = new Button(composite_6, SWT.NONE);
						GridData gd_button_1 = new GridData(SWT.CENTER, SWT.CENTER, true,
								false, 1, 1);
						gd_button_1.widthHint = 70;
						button_1.setLayoutData(gd_button_1);
						button_1.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								save();

							}
						});
						button_1.setText(" 保  存 ");
						
								Button btnNewButton_3 = new Button(composite_6, SWT.NONE);
								btnNewButton_3
										.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
											@Override
											public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
												close();
											}
										});
								GridData gd_btnNewButton_3 = new GridData(SWT.CENTER, SWT.CENTER, true,
										false, 1, 1);
								gd_btnNewButton_3.widthHint = 70;
								btnNewButton_3.setLayoutData(gd_btnNewButton_3);
								btnNewButton_3.setText("  退  出  ");

		initBasicControlValues(subTagModel);

		return parent;

	}

	/**
	 * 初始化基本信息控件值
	 */
	private void initBasicControlValues(TagModel tagModel) {
		if (tagModel == null) {
			return;
		} else {
			text_name.setText(tagModel.getName());
			text_value.setText(tagModel.getValue() == null ? "" : tagModel.getValue());
			text_tagtype.setText(tagModel.getType());
		}

//		initRecorder();
//		initGenerator();

	}

	/**
	 * 初始化存储器
	 * 
	 * @param tagModel
	 */
//	public void initRecorder() {
//		log.debug("初始化存储器.....");
//		String xpath = GetXpathUtil.getTagModelXpath(tagModel) + "/"
//				+ TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='"
//				+ subTagModel.getName() + "']";
//		List<RecorderModel> recorderList = recorderModelDao.getRecorderModels(
//				subTagModel, xpath);
//		if (recorderList == null || recorderList.isEmpty()) {
//			subTagModel.setRecorderList(null);
//			listViewer_recorder.setInput(null);
//			listViewer_recorder.refresh();
//			return;
//		} else {
//
//			subTagModel.setRecorderList(recorderList);
//			listViewer_recorder.setInput(recorderList);
//			listViewer_recorder.refresh();
//		}
//		log.debug("存储器个数：" + recorderList.size());
//	}

	/**
	 * 初始化生成器
	 */
//	private void initGenerator() {
//		log.debug("初始化生成器.....");
//
//		String xpath = GetXpathUtil.getTagModelXpath(tagModel) + "/"
//				+ TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='"
//				+ subTagModel.getName() + "']";
//		GeneratorModel generator = generatorModelDao.getGeneratorModel(xpath);
//		if (generator == null) {
//			subTagModel.setGenerator(null);
//			generator_text.setText("");
//			return;
//		} else {
//			subTagModel.setGenerator(generator);
//			generator_text.setText(generator.getExpression());
//		}
//
//	}

	/**
	 * 保存
	 */
	private void save() {
		if (subTagModel == null) {// 新建的变量进行保存
			TagModel newTagModel = new TagModel();
			newTagModel.setName(text_name.getText().trim());
			newTagModel.setValue(text_value.getText().trim());
			newTagModel.setType(text_tagtype.getText().trim());
			String xpath = GetXpathUtil.getTagModelXpath(tagModel);
			tagModelDao.addSubTagModel(xpath, newTagModel);

		} else {
			if (MessageDialog.openConfirm(getShell(), "修改", "确定要修改当前变量？")) {
				String xpath = GetXpathUtil.getTagModelXpath(tagModel);

				tagModelDao.removeSubTagModel(xpath, subTagModel);

				TagModel newTagModel = new TagModel();
				newTagModel.setName(text_name.getText().trim());
				newTagModel.setValue(text_value.getText().trim());
				newTagModel.setType(text_tagtype.getText().trim());

				tagModelDao.addSubTagModel(xpath, newTagModel);
			} else
				return;
		}

		//evdw.initSubVar();

		close();

	}

	/**
	 * 删除
	 */
	private void remove() {
		tagModelDao.removeTagModel(tagModel);
		XMLDocumentFactory.instance.saveMainDocument();
		tagModel = null;
	}
}
