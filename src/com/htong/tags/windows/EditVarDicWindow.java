package com.htong.tags.windows;



import htconfig_slyt.Activator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Panel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

import com.htong.tags.daoImpl.GeneratorModelDaoImpl;
import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.daoImpl.RecorderModelDaoImpl;
import com.htong.tags.daoImpl.SubTagModelDaoImpl;
import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.daoImpl.TriggerModelDaoImpl;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.GeneratorModel;
import com.htong.tags.model.tag.RecorderModel;
import com.htong.tags.model.tag.TriggerModel;
import com.htong.tags.model.tag.YKDetailModel;
import com.htong.ui.GetVarNum;
import com.htong.ui.GetXpathUtil;
import com.htong.ui.ImportAndExport;
import com.htong.ui.PinyinComparator;
import com.htong.util.LayoutUtil;
import org.eclipse.swt.widgets.Combo;

/**
 * 编辑变量词典窗口
 * 
 * @author 赵磊
 * 
 */
public class EditVarDicWindow extends ApplicationWindow {
	private static class ViewerLabelProvider_1 extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			if (element instanceof TagModel) {
				TagModel tagModel = (TagModel) element;
				return tagModel.getName();
			}
			return super.getText(element);
		}
	}

	/**
	 * 存储器标签提供器
	 * 
	 * @author 赵磊
	 * 
	 */
	private static class RecorderViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		public String getText(Object element) {
			return ((RecorderModel) element).getName() == null ? "无名存储器"
					: ((RecorderModel) element).getName();
		}
	}

	private static final Logger log = Logger.getLogger(EditVarDicWindow.class);

	private TagModel tagModel; // 当前所选的标签

	private RecorderModel recorderModel; // 当前所选存储器

	private IndexNodeModel indexNodeModel;

	private List<TagModel> tagModelList; // 所有变量
	private List<TagModel> subTagModelList;
	DefaultListModel dlm; // 变量

	private String currentPosition; // 当前位置 如：部门/工程
	private String defaultMainIndex = ""; // 新建变量时默认的主索引位置

	private TagModelDaoImpl tagModelDao = new TagModelDaoImpl();
	private IndexModelDaoImpl indexModeDao = new IndexModelDaoImpl();
	private RecorderModelDaoImpl recorderModelDao = new RecorderModelDaoImpl();
	private TriggerModelDaoImpl triggerModelDao = new TriggerModelDaoImpl();
	private GeneratorModelDaoImpl generatorModelDao = new GeneratorModelDaoImpl();
	private IndexNodeModelDaoImpl indexNodeDao = new IndexNodeModelDaoImpl();
	private RecorderModelDaoImpl recorderDao = new RecorderModelDaoImpl();

	private Text text_normal;
	private Text generator_text;
	private Text trigger_text;
	private ListViewer listViewer_recorder;
	private ListViewer subVar_listViewer;
	private Text text_energy;
	private Text text_ratedpower;
	private Text text_value;
	private Text text_varname;
	private JList var_list;
	private Text text_name;
	private Text text_tagtype;
	private Text text_main;
	private Text text_id;

	private String buildingClass;// 建筑类别
	private String buildingCode;// 建筑识别码
	private String energyClass;// 能耗分类
	private String energyItem;// 分项
	private String energyItem1;// 一级子项
	private String energyItem2;// 二级子项

	private String varType;// 变量类型
	private String varNum;// 变量序号
	private TabItem tabItem;
	private Text generatorrefer_text;
	private org.eclipse.swt.widgets.List recorder_combo;
	private org.eclipse.swt.widgets.List subvar_list;
	
	private boolean isEndNode;	//判断是否是叶子节点
	
	ImageIcon ycIcon;
	ImageIcon yxIcon;
	ImageIcon ymIcon;
	ImageIcon ytIcon;
	ImageIcon ykIcon;
	ImageIcon ncIcon; //内存常量
	ImageIcon nbIcon; //内存变量
	
	ImageIcon otherIcon;
	private TabItem ykDetailItem;
	private TabFolder tabFolder;
	private Text yk_disable_tag_text;
	private Text yk_reverse_tag;
	private Text yk_oninfo_text;
	private Text yk_offinfo_text;
	private Combo yk_disable_type_combo;
	private Combo yk_output_combo;
	private Button yk_reverse_tag_button;
	private Button yk_disable_tag_button;
	private Button yk_detail_save_button;
	private Composite yk_detail_composite;
	private Text yk_reverse_timeout_text;
	
	

	public EditVarDicWindow(Shell parentShell, IndexNodeModel indexNodeModel) {
		super(parentShell);
		
		ycIcon = getImageIcon("icons/yc_var.png");
		yxIcon = getImageIcon("icons/yx_var.png");
		ymIcon = getImageIcon("icons/ym_var.gif");
		ytIcon = getImageIcon("icons/yt_var.png");
		ykIcon = getImageIcon("icons/yk_var.png");
		
		ncIcon = getImageIcon("icons/nc_var.png");
		nbIcon = getImageIcon("icons/nb_var.png");
		
		otherIcon = getImageIcon("icons/other_var.gif");
		
		this.indexNodeModel = indexNodeModel;
		
		
		init();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public EditVarDicWindow(Shell parentShell) {
		super(parentShell);
		init();
	}

	/**
	 * 初始化配置
	 * 
	 * @param xpath
	 */
	private void init() {
		tagModelList = new ArrayList<TagModel>();
		dlm = new DefaultListModel();
		

		// 初始化节点标签当前位置
		StringBuilder curentPostionStr = new StringBuilder();
		curentPostionStr.append(indexNodeModel.getName());

		Element tempElement = indexNodeModel.getElement().getParent();
		while (!tempElement.getPath().equals(
				"/" + XMLDocumentFactory.ROOT_NODE + "/"
						+ XMLDocumentFactory.TAGINDEX_NODE)) {
			curentPostionStr
					.insert(0, tempElement.attributeValue("name") + "/");
			tempElement = tempElement.getParent();
		}
		currentPosition = curentPostionStr.toString();

		log.debug("当前位置：" + currentPosition);

		if (tagModelDao.isMainIndex(indexNodeModel)) {// 主索引
			// 初始化变量
			tagModelList = tagModelDao.getTagsByMainIndex(currentPosition);
			// 初始化默认主索引位置
			defaultMainIndex = currentPosition;
			if(tagModelList != null && !tagModelList.isEmpty()) {
				for(TagModel tagModel : tagModelList) {
					tagModel.setMainIndex(currentPosition);
				}
			}
			

		} else {// 分项索引
			log.debug("按分项索引查找变量：" + currentPosition);
			//String subSecendIndex = currentPosition.substring(7);
			Element tempElement1 = indexNodeModel.getElement().getParent();
			while (!tempElement1.getPath().equals(
					"/" + XMLDocumentFactory.ROOT_NODE + "/"
							+ XMLDocumentFactory.TAGINDEX_NODE + "/" + IndexModel.NODE_NAME)) {
				curentPostionStr
						.insert(0, tempElement1.attributeValue("name") + "/");
				tempElement1 = tempElement1.getParent();
			}
			int i = tempElement1.attributeValue("name").length();
			
			String subSecendIndex = currentPosition.substring(i+1);
			tagModelList = tagModelDao.getTagsBySecondIndex(subSecendIndex);//已设置主索引
		}
		if (tagModelList != null) {
			//按名字排序
			Comparator<TagModel> comparator = new Comparator<TagModel>(){
				   public int compare(TagModel t1, TagModel t2) {
				     return PinyinComparator.INSTANCE.compare(t1.getType(), t2.getType());
				   }
			};
				   
			Collections.sort(tagModelList,comparator);
			
			for (TagModel tm : tagModelList) {
//				tm.setMainIndex(currentPosition);//设置主索引
				tm.setIndexNodeModel(indexNodeModel);// 设置上级索引节点
				dlm.addElement(tm);
			}
		}
		
		isEndNode = indexNodeDao.isEndNode(indexNodeModel);
		
		


	}

	@Override
	protected void configureShell(Shell shell) {
		shell.setSize(650, 825);
		shell.setText("编辑变量词典");
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), shell);
		super.configureShell(shell);

	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(650, 825);
	}

	protected Control createContents(Composite parent1) {
		Composite parent = new Composite(parent1, SWT.NONE);
		parent.setLayout(new GridLayout(1, false));

		Group group = new Group(parent, SWT.NONE);
		group.setText("变量词典");
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_group = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_group.heightHint = 180;
		group.setLayoutData(gd_group);

		Composite composite = new Composite(group, SWT.EMBEDDED);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Frame frame = SWT_AWT.new_Frame(composite);

		Panel panel = new Panel();
		frame.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JRootPane rootPane = new JRootPane();
		panel.add(rootPane);

		var_list = new JList();
		var_list.setVisibleRowCount(10);
		var_list.setLayoutOrientation(JList.VERTICAL_WRAP);
		var_list.setBorder(new LineBorder(new Color(0, 0, 0)));
		var_list.setCellRenderer(new MyListCellRenderer());
		var_list.setFixedCellWidth(130);
		
		var_list.setAutoscrolls(true);
		
		JScrollPane scrollPane = new JScrollPane(var_list); 
		
		
		var_list.setModel(dlm);
		var_list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (var_list.isSelectionEmpty()) {
					return;
				}
				if (e.getValueIsAdjusting() == false) {
					tagModel = (TagModel) dlm.get(var_list.getSelectedIndex());
					log.debug("所选变量名字：" + tagModel.getName() + "--index:"
							+ var_list.getSelectedIndex());

					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							initBasicControlValues(tagModel);
							initRecorder();
							initTrigger();
							initGenerator();
							//initSubVar();
							initDetail();
							
							generateVarName();
							
						}
					});
				}
			}
		});

		rootPane.getContentPane().add(scrollPane, BorderLayout.CENTER);

		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_composite_1.heightHint = 344;
		composite_1.setLayoutData(gd_composite_1);

		Composite composite_basic = new Composite(composite_1, SWT.NONE);
		composite_basic.setLayout(new GridLayout(1, false));
		GridData gd_composite_basic = new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1);
		gd_composite_basic.heightHint = 160;
		gd_composite_basic.widthHint = 468;
		composite_basic.setLayoutData(gd_composite_basic);

		Group group_base = new Group(composite_basic, SWT.NONE);
		group_base.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		group_base.setText("基本信息");
		GridData gd_group_base = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_group_base.heightHint = 167;
		gd_group_base.widthHint = 547;
		group_base.setLayoutData(gd_group_base);
		group_base.setLayout(null);

		Label lblNewLabel = new Label(group_base, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		lblNewLabel.setBounds(10, 23, 36, 18);
		lblNewLabel.setText("名称：");

		text_name = new Text(group_base, SWT.BORDER);
		text_name.setBounds(52, 23, 73, 20);

		Label label_1 = new Label(group_base, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_1.setBounds(270, 48, 60, 18);
		label_1.setText("标签类型：");

		text_tagtype = new Text(group_base, SWT.BORDER);
		text_tagtype.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if(tagModel != null && tagModel.getName() == null) {
					//改变序号
					if(text_tagtype.getText().trim().equals("")) {
						varNum = "XXXXXX";
					} else {
						varNum = GetVarNum.instanse.getVarNum(text_tagtype.getText().trim());
					}
					log.debug("新建的变量序号为："+varNum);
					
					//设定初始值
					if(text_tagtype.getText().trim().startsWith("遥测")) {
						text_value.setText("0");
					} else if(text_tagtype.getText().trim().startsWith("遥信")) {
						text_value.setText("0");
					}
					
				}
				
				
				generateVarName();
			}
		});
		text_tagtype.setEditable(false);
		text_tagtype.setBounds(336, 49, 126, 20);

		Button button_2 = new Button(group_base, SWT.NONE);
		button_2.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tagModel == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择变量或新建变量！");
					return;
				}
				new EditTagTypeWindow(tagModel, text_tagtype).open();
			}
		});
		button_2.setBounds(468, 48, 54, 22);
		button_2.setText("编辑");

		Label label_2 = new Label(group_base, SWT.NONE);
		label_2.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_2.setBounds(10, 78, 84, 15);
		label_2.setText("变量标签索引：");

		text_main = new Text(group_base, SWT.BORDER);
//		text_main.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				generateVarName();
//			}
//		});
		text_main.setEditable(false);
		text_main.setBounds(100, 78, 362, 20);

		Label label_3 = new Label(group_base, SWT.NONE);
		label_3.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_3.setBounds(10, 48, 36, 18);
		label_3.setText("序号：");
		label_3.setVisible(false);

		text_id = new Text(group_base, SWT.BORDER);
		text_id.setVisible(false);
//		text_id.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				generateVarName();
//			}
//		});
//		text_id.addVerifyListener(new VerifyListener() {
//			public void verifyText(VerifyEvent e) {
//				log.debug(text_id.getText());
//			}
//		});
		text_id.setBounds(52, 48, 60, 20);

		Label label_4 = new Label(group_base, SWT.NONE);
		label_4.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_4.setText("常规分类索引：");
		label_4.setBounds(10, 102, 84, 15);

		text_normal = new Text(group_base, SWT.BORDER);
		text_normal.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				generateVarName();
			}
		});
		text_normal.setEditable(false);
		text_normal.setBounds(100, 102, 362, 20);

		Button button_energy = new Button(group_base, SWT.NONE);
		button_energy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tagModel == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择变量或新建变量！");
					return;
				}
				new EditTagEnergyIndexWindow(tagModel, text_energy).open();
			}
		});
		button_energy.setText("编辑");
		button_energy.setBounds(468, 124, 54, 22);

		Button button_main = new Button(group_base, SWT.NONE);

		button_main.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择变量或新建变量！");
					return;
				}
				new EditTagMainIndexWindow(tagModel, text_main).open();
			}
		});
		button_main.setBounds(468, 78, 54, 22);
		button_main.setText("编辑");
		
		button_main.setVisible(true);
		

		Label lblNewLabel_1 = new Label(group_base, SWT.NONE);
		lblNewLabel_1
				.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 125, 84, 17);
		lblNewLabel_1.setText("能耗分类分项：");

		text_energy = new Text(group_base, SWT.BORDER);
		text_energy.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				generateVarName();
			}
		});
		text_energy.setEditable(false);
		text_energy.setBounds(100, 125, 362, 20);

		Button button_normal = new Button(group_base, SWT.NONE);
		button_normal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择变量或新建变量！");
					return;
				}
				new EditTagNormalIndexWindow(tagModel, text_normal).open();
			}
		});

		button_normal.setText("编辑");
		button_normal.setBounds(468, 100, 54, 22);

		Label label_7 = new Label(group_base, SWT.NONE);
		label_7.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_7.setBounds(128, 48, 60, 24);
		label_7.setText("额定功率：");

		text_ratedpower = new Text(group_base, SWT.BORDER);
		text_ratedpower.setBounds(192, 48, 60, 20);
		if(!isEndNode) {
			text_ratedpower.setEnabled(false);
		}

		Label label_8 = new Label(group_base, SWT.NONE);
		label_8.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_8.setBounds(142, 23, 46, 18);
		label_8.setText("初始值：");

		text_value = new Text(group_base, SWT.BORDER);
		text_value.setBounds(192, 23, 60, 20);

		Label label_9 = new Label(group_base, SWT.NONE);
		label_9.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		label_9.setText("变量名：");
		label_9.setBounds(282, 23, 48, 18);

		text_varname = new Text(group_base, SWT.BORDER);
		text_varname.setEditable(false);
		text_varname.setBounds(336, 20, 186, 20);

		Composite composite_5 = new Composite(composite_1, SWT.NONE);
		composite_5.setLayout(new GridLayout(1, false));
		GridData gd_composite_5 = new GridData(SWT.RIGHT, SWT.FILL, false,
				true, 1, 2);
		gd_composite_5.widthHint = 67;
		composite_5.setLayoutData(gd_composite_5);

		Label label = new Label(composite_5, SWT.NONE);
		label.setText(" ");

		Button btnNewButton = new Button(composite_5, SWT.NONE);
		btnNewButton
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					@Override
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						newVariable();
						
						varNum = "XXXXXX";
					}
				});
		btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnNewButton.setText(" 新  建 ");

		Button button = new Button(composite_5, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tagModel == null) {
					MessageDialog.openWarning(getShell(), "警告", "请选择要删除的变量！");
				} else {
					if (MessageDialog.openConfirm(getShell(), "确定", "确定要删除选择的变量吗？")) {
						remove();
					}

				}
			}
		});
		button.setText(" 删  除 ");

		Button button_1 = new Button(composite_5, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if("".equals(text_name.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "请输入正确的变量名字");
					return;
				} else if("".equals(text_tagtype.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "请选择变量类型");
					return;
				}
				
				String var_name = text_name.getText().trim();
				
				if (tagModel != null && tagModel.getVarName() == null) {// 新建的变量进行保存
					
					String xpath_ = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel);
					xpath_ = xpath_ + "/tags/tag[@name='" + var_name + "']";
					TagModel myTagModel = tagModelDao.getTagByXpath(xpath_);
					if(myTagModel != null) {
						MessageDialog.openWarning(getShell(), "错误", "已经存在该变量!");
						return;
					}
				}
				
				
				save();
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
//				varNum = "XXXX";
//				generateVarName();
//				log.debug(dlm.size());
//				log.debug(tagModelList.size());
				for(int i=0;i<dlm.size();i++) {
					TagModel iTag = (TagModel)dlm.get(i);
//					log.debug(iTag.getName());
					if(iTag.getName().equals(var_name)) {
						var_list.setSelectedIndex(i);
//						var_list.doLayout();
//						log.debug("应该选择：" + var_name);
						tagModel = (TagModel)dlm.get(i);
						break;
					}
				}
				
				

			}
		});
		button_1.setText(" 保  存 ");

		Composite composite_ext = new Composite(composite_1, SWT.NONE);
		composite_ext.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_ext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));

		Group group_1 = new Group(composite_ext, SWT.NONE);
		group_1.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		group_1.setText("扩展信息");
		group_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new TabFolder(group_1, SWT.NONE);
		
		

		tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("存储器");
		

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_2);

		listViewer_recorder = new ListViewer(composite_2, SWT.BORDER
				| SWT.V_SCROLL);
		listViewer_recorder.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				StructuredSelection ss = (StructuredSelection) listViewer_recorder
						.getSelection();
				RecorderModel recorderModel = (RecorderModel) ss
						.getFirstElement();
				new RecorderEditorWindow(tagModel, recorderModel,
						EditVarDicWindow.this, false).open();
			}
		});
		listViewer_recorder
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection iss = (IStructuredSelection) event
								.getSelection();
						recorderModel = (RecorderModel) iss.getFirstElement();
					}
				});
		recorder_combo = listViewer_recorder
				.getList();

		recorder_combo.setBounds(23, 10, 132, 160);
		listViewer_recorder.setLabelProvider(new RecorderViewerLabelProvider());
		listViewer_recorder.setContentProvider(ArrayContentProvider
				.getInstance());

		Button button_3 = new Button(composite_2, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tagModel == null || tagModel.getName() == null) {
					MessageDialog.openError(getShell(), "错误", "选择变量！");
				} else {
					new RecorderEditorWindow(tagModel, null,
							EditVarDicWindow.this, false).open();
				}
			}
		});
		button_3.setBounds(167, 22, 50, 22);
		button_3.setText("新建");

		Button button_5 = new Button(composite_2, SWT.NONE);
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null || tagModel.getName() == null) {
					MessageDialog.openError(getShell(), "错误", "未选择变量！");
					return;
				} 
				if ((RecorderModel)(((StructuredSelection)listViewer_recorder.getSelection()).getFirstElement()) == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择要删除的存储器！");
				} else {
					if(MessageDialog.openConfirm(getShell(), "确认删除", "确定要删除该存储器吗？")) {
						StructuredSelection ss = (StructuredSelection) listViewer_recorder
								.getSelection();
						RecorderModel recorderModel = (RecorderModel) ss
								.getFirstElement();
						recorderModelDao.removeRecorder(recorderModel, GetXpathUtil
								.getTagModelXpath(recorderModel.getTagModel()));

						initRecorder();
					}
					
				}
			}
		});
		button_5.setBounds(167, 50, 50, 22);
		button_5.setText("删除");

		Button button_6 = new Button(composite_2, SWT.NONE);
		button_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null || tagModel.getName() == null) {
					MessageDialog.openError(getShell(), "错误", "未选择变量！");
					return;
				} 
				
				if ((RecorderModel)(((StructuredSelection)listViewer_recorder.getSelection()).getFirstElement()) == null) {
					MessageDialog.openWarning(getShell(), "错误", "请选择要修改的存储器！");
				} else {
					StructuredSelection ss = (StructuredSelection) listViewer_recorder
							.getSelection();
					RecorderModel recorderModel = (RecorderModel) ss
							.getFirstElement();
					new RecorderEditorWindow(tagModel, recorderModel,
							EditVarDicWindow.this, false).open();
				}
			}
		});
		button_6.setBounds(167, 78, 50, 22);
		button_6.setText("修改");

		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("生成器");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_3);
		composite_3.setLayout(new GridLayout(3, false));
		
		Label label_10 = new Label(composite_3, SWT.NONE);
		label_10.setText("变量：");
		label_10.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		
		generatorrefer_text = new Text(composite_3, SWT.BORDER);
		generatorrefer_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button button_4 = new Button(composite_3, SWT.NONE);
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageDialog.openInformation(getShell(), "信息", "先手工输入，不同变量用逗号隔开，如 A001_YX0001，B00201A11_YM0002，智能选择敬请期待！");
			}
		});
		button_4.setText(" 选  择 ");
		
				Label label_5 = new Label(composite_3, SWT.NONE);
				label_5.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
				label_5.setText("脚本：");
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
				generator_text = new Text(composite_3, SWT.BORDER | SWT.MULTI);
				generator_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		Button button_9 = new Button(composite_3, SWT.NONE);
		GridData gd_button_9 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_button_9.widthHint = 60;
		button_9.setLayoutData(gd_button_9);
		button_9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					return;
				} else if (tagModel.getGenerator() == null) {
//					if ("".equals(generator_text.getText().trim())) {
//						return;
//					} else {// 添加
						GeneratorModel generator = new GeneratorModel();
						generator
								.setExpression(generator_text.getText().trim().equals("")?null:generator_text.getText().trim());
						generator.setRefer(generatorrefer_text.getText().trim().equals("")?null:generatorrefer_text.getText().trim());
						tagModel.setGenerator(generator);
						generatorModelDao.addGenerator(generator,
								GetXpathUtil.getTagModelXpath(tagModel));
//					}
				} else {
//					if ("".equals(generator_text.getText().trim())) {// 删除
//						generatorModelDao.removeGeverator(
//								tagModel.getGenerator(),
//								GetXpathUtil.getTagModelXpath(tagModel));
//					} else {// 更新
						tagModel.getGenerator().setExpression(
								generator_text.getText().trim().equals("")?null:generator_text.getText().trim());
						tagModel.getGenerator().setRefer(generatorrefer_text.getText().trim().equals("")?null:generatorrefer_text.getText().trim());
						generatorModelDao.updateGenerator(
								tagModel.getGenerator(),
								GetXpathUtil.getTagModelXpath(tagModel));
//					}
				}
				initGenerator();
				MessageDialog.openInformation(getShell(), "提示", "保存成功！");
			}
		});
		button_9.setText("保存");
		new Label(composite_3, SWT.NONE);

		TabItem tabItem_3 = new TabItem(tabFolder, SWT.NONE);
		tabItem_3.setText("触发器");

		Composite composite_7 = new Composite(tabFolder, SWT.NONE);
		tabItem_3.setControl(composite_7);
		composite_7.setLayout(new GridLayout(1, false));

		Label label_6 = new Label(composite_7, SWT.NONE);
		label_6.setText("触发脚本：");

		trigger_text = new Text(composite_7, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		trigger_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Button button_10 = new Button(composite_7, SWT.NONE);
		GridData gd_button_10 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button_10.widthHint = 60;
		button_10.setLayoutData(gd_button_10);
		button_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openError(getShell(), "错误", "请选择变量！");
					return;
				} else if (tagModel.getTrigger() == null) {
					if (!"".equals(trigger_text.getText().trim())) {// 添加
						TriggerModel trigger = new TriggerModel();
						trigger.setExpression(trigger_text.getText().trim());
						tagModel.setTrigger(trigger);
						triggerModelDao.addTriggerModel(tagModel);
					} else
						return;
				} else {
					if ("".equals(trigger_text.getText().trim())) {// 删除
						triggerModelDao.removeTriggerModel(tagModel);
					} else {// 更新
						tagModel.getTrigger().setExpression(
								trigger_text.getText().trim());
						triggerModelDao.updateTriggerModel(tagModel);
					}
				}

				initTrigger();
				MessageDialog.openInformation(getShell(), "提示", "保存成功！");
			}
		});
		button_10.setText("保存");

	//	TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
	//	tabItem_2.setText("子变量");

		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
	//	tabItem_2.setControl(composite_4);

		subVar_listViewer = new ListViewer(composite_4, SWT.BORDER
				| SWT.V_SCROLL);
		subVar_listViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection) subVar_listViewer
						.getSelection();
				TagModel subTagModel = (TagModel) iss.getFirstElement();
				if (subTagModel == null) {
					MessageDialog.openError(getShell(), "错误", "未选择子变量！");
				} else {
					new SubVarDicEditWindow(getShell(), tagModel,
							subTagModel, EditVarDicWindow.this).open();
				}
			}
		});
		subvar_list = subVar_listViewer.getList();
		subVar_listViewer
				.setContentProvider(ArrayContentProvider.getInstance());
		subvar_list.setBounds(10, 10, 157, 178);
		subVar_listViewer.setLabelProvider(new ViewerLabelProvider_1());
		subVar_listViewer.setInput(subVar_listViewer);

		Button btnNewButton_5 = new Button(composite_4, SWT.NONE);
		btnNewButton_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openError(getShell(), "错误", "未选择变量！");
				} else {
					new SubVarDicEditWindow(getShell(), tagModel, null,
							EditVarDicWindow.this).open();
				}

			}
		});
		btnNewButton_5.setBounds(195, 25, 72, 22);
		btnNewButton_5.setText("新建");

		Button button_8 = new Button(composite_4, SWT.NONE);
		button_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openError(getShell(), "错误", "未选择变量！");
				} else if (subVar_listViewer.getSelection() == null) {
					MessageDialog.openError(getShell(), "错误", "未选择子变量！");

				} else {
					IStructuredSelection iss = (IStructuredSelection) subVar_listViewer
							.getSelection();
					TagModel subTagModel = (TagModel) iss.getFirstElement();
					String parentTagXpath = GetXpathUtil
							.getTagModelXpath(tagModel);
					tagModelDao.removeSubTagModel(parentTagXpath, subTagModel);

					//initSubVar();
				}
			}
		});
		button_8.setBounds(195, 53, 72, 22);
		button_8.setText("删除");

		Button btnNewButton_6 = new Button(composite_4, SWT.NONE);
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (tagModel == null) {
					MessageDialog.openError(getShell(), "错误", "未选择变量！");
				} else {
					IStructuredSelection iss = (IStructuredSelection) subVar_listViewer
							.getSelection();
					TagModel subTagModel = (TagModel) iss.getFirstElement();
					if (subTagModel == null) {
						MessageDialog.openError(getShell(), "错误", "未选择子变量！");
					} else {
						new SubVarDicEditWindow(getShell(), tagModel,
								subTagModel, EditVarDicWindow.this).open();
					}
				}
			}
		});
		btnNewButton_6.setBounds(195, 81, 72, 22);
		btnNewButton_6.setText("修改");
		

		Composite composite_6 = new Composite(parent, SWT.NONE);
		composite_6.setLayout(new GridLayout(4, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false,
				false, 1, 1));

		Button btnNewButton_1 = new Button(composite_6, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				File filePath = new File("变量词典/");
				if (!filePath.exists()) {
					filePath.mkdirs();
				}

				FileDialog fileDialog = new FileDialog(Display.getDefault()
						.getActiveShell().getShell(), SWT.OPEN);
				fileDialog.setFilterPath("变量词典/");
				fileDialog.setFilterExtensions(new String[] { "*.xml" });
				fileDialog.setFilterNames(new String[] { "变量词典文件（*.xml）" });

				String fileName = fileDialog.open();
				if (fileName != null) {
					if (!fileName.contains(".")) {
						fileName += ".xml";
					}

					File file = new File(fileName);
					if (!file.exists()) {
						MessageDialog.openError(null, "错误", "找不到指定文件！");
						return;
					}

					log.debug(fileName);

					ImportAndExport importVarDic = new ImportAndExport();
					List<Element> importVarElementList = importVarDic.importVariableDic(fileName);
					
					String allPath = GetXpathUtil.getTagModelMainIndexXPath(currentPosition);
					log.debug(allPath);
					
					if(importVarElementList != null) {
						int totalNum = importVarElementList.size();
						int remaining = totalNum;	//剩下的变量个数
						
						boolean allYes = false;
						boolean allNo = false;
						
						int result = -1;
						for(Element ele : importVarElementList) {
							remaining--;
							ele.setParent(null);
							String name = ele.attributeValue("name");
							String xpath = allPath + "/tags/tag[@name='"+name+"']";
							log.debug(xpath);
							TagModel myTagModel = tagModelDao.getTagByXpath(xpath);
							if(myTagModel != null) {
								
								if(!allYes && !allNo) {
									MessageDialog dialog = new MessageDialog(getShell(), "变量已存在", null, "变量[" + name + "]已存在，是否覆盖？",
											MessageDialog.QUESTION, new String[] {"是", "否", "剩余全部是","剩余全部否","取消"}, 0);
									result = dialog.open();
								}
								if(result == 2) {
									allYes = true;
								} else if(result == 3) {
									allNo = true;
								} 
								
								
								if(allYes || result == 0) {
									tagModelDao.removeTagModel(allPath, name);
								} else if(allNo || result == 1) {
									totalNum--;
									continue;
								} else if(result == 4) {
									totalNum = totalNum - remaining + 1;
									break;
								}

							}
							//更新变量名字
							String varType = ele.attributeValue("type");
							String varName = ele.attributeValue(TagModel.VAR_NAME_ATTR);
							String oldName = varName.substring(0, varName.length()-4);
							String newNum = GetVarNum.instanse.getVarNum(varType);
							String newName = oldName + newNum;
							ele.addAttribute(TagModel.VAR_NAME_ATTR, newName);
							
							
							Document document = XMLDocumentFactory.instance.getMainDocument();
							
							Element indexNodeElement = (Element) document.selectSingleNode(allPath);
							
							Element tagsElement = indexNodeElement.element("tags");
							if(tagsElement == null) {
								tagsElement = indexNodeElement.addElement("tags");
							}
							tagsElement.add(ele);
							
						}
						XMLDocumentFactory.instance.saveMainDocument();
						
						refreshList();
						clearText();	
						
						MessageDialog.openInformation(getShell(), "提示", "成功导入变量"+totalNum+"个。");
					}
	
								
				}
			}
		});
		btnNewButton_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		btnNewButton_1.setText(" 导入变量词典 ");

		Button btnNewButton_2 = new Button(composite_6, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File filePath = new File("变量词典/");
				if (!filePath.exists()) {
					filePath.mkdirs();
				}

				FileDialog fileDialog = new FileDialog(Display.getDefault()
						.getActiveShell().getShell(), SWT.SAVE);
				fileDialog.setFilterPath("变量词典/");
				fileDialog.setFilterExtensions(new String[] { "*.xml" });
				fileDialog.setFilterNames(new String[] { "变量词典文件（*.xml）" });
				fileDialog.setOverwrite(true);

				fileDialog.setFileName(indexNodeModel.getName() + ".xml");

				String fileName = fileDialog.open();
				if (fileName == null) {
					return;
				}

				int index = fileName.lastIndexOf(".");
				// log.debug(index);
				// log.debug(fileName.substring(index));
				if (index == -1) {
					fileName += ".xml";
				}

				log.debug(fileName);
				
				String allPath = GetXpathUtil.getTagModelMainIndexXPath(currentPosition);
				log.debug(allPath);
				Document document = XMLDocumentFactory.instance.getMainDocument();
				
				Element varElement = (Element) document.selectSingleNode(allPath);
				
				
				ImportAndExport exportVarDic = new ImportAndExport();
				exportVarDic.exportVariableDic(varElement, fileName);

			}
		});
		btnNewButton_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		btnNewButton_2.setText("  导出变量词典  ");

		Button btnNewButton_4 = new Button(composite_6, SWT.NONE);
		btnNewButton_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		btnNewButton_4.setText("  帮  助  ");

		Button btnNewButton_3 = new Button(composite_6, SWT.NONE);
		btnNewButton_3
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					@Override
					public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
						close();
					}
				});
		btnNewButton_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		btnNewButton_3.setText("  退  出  ");

		return parent;

	}

	/**
	 * 初始化子变量
	 */
	private void initSubVar() {
		log.debug("初始化子变量.....");
		subTagModelList = new ArrayList<TagModel>();
		String parentTagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		subTagModelList = tagModelDao.getSubTagModel(parentTagXpath);
		if (subTagModelList == null || subTagModelList.isEmpty()) {
			subVar_listViewer.setInput(null);
			subVar_listViewer.refresh();
			return;
		} else {
			subVar_listViewer.setInput(subTagModelList);
			subVar_listViewer.refresh();
		}
	}

	/**
	 * 初始化基本信息控件值
	 */
	private void initBasicControlValues(TagModel tagModel) {
		text_name.setText(tagModel.getName());
		text_varname.setText(tagModel.getVarName() == null ? "" : tagModel
				.getVarName());
		text_id.setText(text_varname.getText().trim().substring(text_varname.getText().trim().length()-4));
		
		text_value.setText(tagModel.getValue() == null ? "" : tagModel
				.getValue());
		text_tagtype.setText(tagModel.getType() == null ? "" : tagModel
				.getType());
		text_ratedpower.setText(tagModel.getRatedPower() == null ? ""
				: tagModel.getRatedPower());
		//非遥脉量不可编辑
		if(!isEndNode || tagModel.getType() == null || !tagModel.getType().startsWith("遥脉") ) {
			text_ratedpower.setEnabled(false);
		} else {
			text_ratedpower.setEnabled(true);
		}
		
		text_main.setText(tagModel.getMainIndex().substring(7));
		text_normal.setText(tagModel.getNormalIndex() == null ? "" : tagModel
				.getNormalIndex());
		text_energy.setText(tagModel.getEnergyIndex() == null ? "" : tagModel
				.getEnergyIndex());
		
		varNum = tagModel.getVarName().substring(tagModel.getVarName().length()-6);
		
		
	}

	/**
	 * 初始化存储器
	 * 
	 * @param tagModel
	 */
	public void initRecorder() {
		log.debug("初始化存储器.....");
		String parent = GetXpathUtil.getTagModelXpath(tagModel);
		//log.debug(parent);
		List<RecorderModel> recorderList = recorderModelDao.getRecorderModels(
				tagModel, parent);
		if (recorderList == null || recorderList.isEmpty()) {
			tagModel.setRecorderList(null);
			listViewer_recorder.setInput(null);
			listViewer_recorder.refresh();
			return;
		} else {

			tagModel.setRecorderList(recorderList);
			listViewer_recorder.setInput(recorderList);
			listViewer_recorder.refresh();
		}
		log.debug("存储器个数：" + recorderList.size());
	}

	/**
	 * 初始化生成器
	 */
	private void initGenerator() {
		log.debug("初始化生成器.....");

		GeneratorModel generator = generatorModelDao
				.getGeneratorModel(GetXpathUtil.getTagModelXpath(tagModel));
		if (generator == null) {
			tagModel.setGenerator(null);
			generator_text.setText("");
			generatorrefer_text.setText("");
			return;
		} else {
			tagModel.setGenerator(generator);
			generator_text.setText(generator.getExpression()==null?"":generator.getExpression());
			generatorrefer_text.setText(generator.getRefer() == null?"":generator.getRefer());
		}
	}

	/**
	 * 初始化触发器
	 */
	private void initTrigger() {
		log.debug("初始化触发器.....");
		TriggerModel triggerModel = triggerModelDao.getTriggerModel(tagModel);
		if (triggerModel == null) {
			tagModel.setTrigger(null);
			trigger_text.setText("");
			return;
		} else {
			tagModel.setTrigger(triggerModel);
			log.debug("触发器内容：" + triggerModel.getExpression());
			trigger_text.setText(triggerModel.getExpression());
		}
	}
	
	/**
	 * 初始化扩展信息
	 */
	private void initDetail() {
		if(tagModel != null && tagModel.getType().startsWith("遥控")) {
			if(ykDetailItem == null) {
				ykDetailItem = new TabItem(tabFolder, SWT.NONE);
				ykDetailItem.setText("遥控扩展");
				
				yk_detail_composite = new Composite(tabFolder, SWT.NONE);
				ykDetailItem.setControl(yk_detail_composite);
				yk_detail_composite.setLayout(new GridLayout(4, false));
				
				Label label_15 = new Label(yk_detail_composite, SWT.NONE);
				label_15.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_15.setText("合消息：");
				
				yk_oninfo_text = new Text(yk_detail_composite, SWT.BORDER);
				GridData gd_yk_oninfo_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_oninfo_text.widthHint = 100;
				yk_oninfo_text.setLayoutData(gd_yk_oninfo_text);
				new Label(yk_detail_composite, SWT.NONE);
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_16 = new Label(yk_detail_composite, SWT.NONE);
				label_16.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_16.setText("分消息：");
				
				yk_offinfo_text = new Text(yk_detail_composite, SWT.BORDER);
				GridData gd_yk_offinfo_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_offinfo_text.widthHint = 100;
				yk_offinfo_text.setLayoutData(gd_yk_offinfo_text);
				new Label(yk_detail_composite, SWT.NONE);
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_11 = new Label(yk_detail_composite, SWT.NONE);
				label_11.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_11.setText("闭锁遥信点：");
				
				yk_disable_tag_text = new Text(yk_detail_composite, SWT.BORDER);
				GridData gd_yk_disable_tag_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_disable_tag_text.widthHint = 120;
				yk_disable_tag_text.setLayoutData(gd_yk_disable_tag_text);
				
				yk_disable_tag_button = new Button(yk_detail_composite, SWT.NONE);
				GridData gd_yk_disable_tag_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_disable_tag_button.widthHint = 60;
				yk_disable_tag_button.setLayoutData(gd_yk_disable_tag_button);
				yk_disable_tag_button.setText("选择");
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_12 = new Label(yk_detail_composite, SWT.NONE);
				label_12.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_12.setText("闭锁方式：");
				
				yk_disable_type_combo = new Combo(yk_detail_composite, SWT.READ_ONLY);
				yk_disable_type_combo.setItems(new String[] {"", "合闭锁", "分闭锁"});
				GridData gd_yk_disable_type_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_disable_type_combo.widthHint = 80;
				yk_disable_type_combo.setLayoutData(gd_yk_disable_type_combo);
				new Label(yk_detail_composite, SWT.NONE);
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_13 = new Label(yk_detail_composite, SWT.NONE);
				label_13.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_13.setText("返校遥信点：");
				
				yk_reverse_tag = new Text(yk_detail_composite, SWT.BORDER);
				GridData gd_yk_reverse_tag = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_reverse_tag.widthHint = 120;
				yk_reverse_tag.setLayoutData(gd_yk_reverse_tag);
				
				yk_reverse_tag_button = new Button(yk_detail_composite, SWT.NONE);
				GridData gd_yk_reverse_tag_button = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_reverse_tag_button.widthHint = 60;
				yk_reverse_tag_button.setLayoutData(gd_yk_reverse_tag_button);
				yk_reverse_tag_button.setText("选择");
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_17 = new Label(yk_detail_composite, SWT.NONE);
				label_17.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_17.setText("返校超时时间：");
				
				yk_reverse_timeout_text = new Text(yk_detail_composite, SWT.BORDER);
				GridData gd_yk_reverse_timeout_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_reverse_timeout_text.widthHint = 110;
				yk_reverse_timeout_text.setLayoutData(gd_yk_reverse_timeout_text);
				
				Label label_18 = new Label(yk_detail_composite, SWT.NONE);
				label_18.setText("秒");
				new Label(yk_detail_composite, SWT.NONE);
				
				Label label_14 = new Label(yk_detail_composite, SWT.NONE);
				label_14.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				label_14.setText("允许操作类型：");
				
				yk_output_combo = new Combo(yk_detail_composite, SWT.READ_ONLY);
				yk_output_combo.setItems(new String[] {"","合操作", "分操作", "合/分操作"});
				GridData gd_yk_output_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_yk_output_combo.widthHint = 80;
				yk_output_combo.setLayoutData(gd_yk_output_combo);
				new Label(yk_detail_composite, SWT.NONE);
//				
//				new Label(yk_detail_composite, SWT.NONE);
				yk_detail_save_button = new Button(yk_detail_composite, 0);
				yk_detail_save_button.setText("  保 存  ");
				yk_detail_save_button.addSelectionListener(new SelectionListener() {
					
					@Override
					public void widgetSelected(SelectionEvent e) {
						YKDetailModel yk = new YKDetailModel();
						yk.setOnInfo("".equals(yk_oninfo_text.getText().trim())?null:yk_oninfo_text.getText().trim());
						yk.setOffInfo("".equals(yk_offinfo_text.getText().trim())?null:yk_offinfo_text.getText().trim());
						yk.setDisableTag("".equals(yk_disable_tag_text.getText().trim())?null:yk_disable_tag_text.getText().trim());
						yk.setReverseTimeout("".equals(yk_reverse_timeout_text.getText().trim())?null:yk_reverse_timeout_text.getText().trim());
						yk.setReverseTag("".equals(yk_reverse_tag.getText().trim())?null:yk_reverse_tag.getText().trim());
						
						if("合闭锁".equals(yk_disable_type_combo.getText())) {
							yk.setDisableType("1");
						} else if("分闭锁".equals(yk_disable_type_combo.getText())) {
							yk.setDisableType("0");
						} else {
							yk.setDisableType(null);
						}
						
						if("合操作".equals(yk_output_combo.getText())) {
							yk.setOutputType("1");
						} else if("分操作".equals(yk_output_combo.getText())) {
							yk.setOutputType("0");
						} else  if("合/分操作".equals(yk_output_combo.getText())) {
							yk.setOutputType("-1");
						} else {
							yk.setOutputType(null);
						}
						
						new SubTagModelDaoImpl().saveOrUpdate(tagModel, yk);
						MessageDialog.openInformation(getShell(), "保存成功", "遥控扩展信息保存成功！");
						
					}
					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
					}
				});
			}
			initYKDetailValue();
			
			
		} else {
			if(ykDetailItem != null) {
				ykDetailItem.dispose();
				ykDetailItem = null;
			}
		}
	}
	/**
	 * 初始化遥控值
	 */
	private void initYKDetailValue() {
		
		log.debug("初始化遥控扩展信息.....");
		String parentTagXpath = GetXpathUtil.getTagModelXpath(tagModel);
		YKDetailModel yk = new SubTagModelDaoImpl().getSubYKDetailModel(parentTagXpath);
		if(yk == null) {
			yk_oninfo_text.setText("合闸");
			yk_offinfo_text.setText("分闸");
			yk_disable_tag_text.setText("");
			yk_disable_type_combo.setText("");
			yk_reverse_tag.setText("");
			yk_reverse_timeout_text.setText("10");
			yk_output_combo.setText("");
		} else {
			yk_oninfo_text.setText(yk.getOnInfo()==null?"":yk.getOnInfo());
			yk_offinfo_text.setText(yk.getOffInfo()==null?"":yk.getOffInfo());
			yk_disable_tag_text.setText(yk.getDisableTag()==null?"":yk.getDisableTag());
			yk_disable_type_combo.setText(yk.getDisableType()==null?"":(yk.getDisableType().equals("1")?"合闭锁":"分闭锁"));
			yk_reverse_tag.setText(yk.getReverseTag()==null?"":yk.getReverseTag());
			yk_reverse_timeout_text.setText(yk.getReverseTimeout()==null?"":yk.getReverseTimeout());
			yk_output_combo.setText(yk.getOutputType()==null?"":(yk.getOutputType().equals("1")?"合操作":(yk.getOutputType().equals("0")?"分操作":"合/分操作")));
		}
		
	}

	class MyListCellRenderer extends JLabel implements ListCellRenderer {
		public MyListCellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (value != null) {
				TagModel tag = (TagModel) value;
				setText(tag.getName());
				if(tag.getType().startsWith("遥测")) {
					setIcon(ycIcon);
				} else if(tag.getType().startsWith("遥信")) {
					setIcon(yxIcon);
				} else if(tag.getType().startsWith("遥脉")) {
					setIcon(ymIcon);
				} else if(tag.getType().startsWith("遥调")) {
					setIcon(ytIcon);
				} else if(tag.getType().startsWith("遥控")) {
					setIcon(ykIcon);
				} else if(tag.getType().startsWith("内存变量")) {
					setIcon(nbIcon);
				} else if(tag.getType().startsWith("内存常量")) {
					setIcon(ncIcon);
				} else  {
					setIcon(otherIcon);
				}
			}
			// setIcon(longIcon);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				// 设置选取与取消选取的前景与背景颜色.
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			return this;
		}
	}

	/**
	 * 新建
	 */
	private void newVariable() {
		tagModel = new TagModel();
		
		this.var_list.clearSelection();
		this.text_name.setText("");
		this.text_varname.setText("");
		this.text_id.setText("");
		this.text_tagtype.setText("");
		this.text_value.setText("");
		this.text_main
				.setText(defaultMainIndex.length() >= 7 ? defaultMainIndex
						.substring(7) : "");
		this.text_normal.setText("");
		this.text_energy.setText("");
		this.text_ratedpower.setText("");
		
		clearSubControl();
		log.debug("新建变量主索引：" + currentPosition);
		tagModel.setMainIndex(currentPosition);

	}

	/**
	 * 保存
	 */
	private void save() {
		if (tagModel == null) {
			MessageDialog.openWarning(getShell(), "警告", "请选择要编辑的变量或新建变量");
			return;
		}
		if (tagModel.getVarName() == null) {// 新建的变量进行保存
			
			String var_name = text_name.getText().trim();
			
			String xpath_ = GetXpathUtil.getIndexNodeXPathByName(indexNodeModel);
			xpath_ = xpath_ + "/tags/tag[@name='" + var_name + "']";
			TagModel myTagModel = tagModelDao.getTagByXpath(xpath_);
			if(myTagModel != null) {
				MessageDialog.openWarning(getShell(), "错误", "已经存在该变量!");
				return;
			}

			tagModel.setName(text_name.getText().trim());
			tagModel.setVarName(text_varname.getText().trim());
			tagModel.setType(text_tagtype.getText().trim());
			tagModel.setValue(text_value.getText().trim().equals("") ? null
					: text_value.getText().trim());
			tagModel.setMainIndex("变量标签索引/" + text_main.getText().trim());
			tagModel.setNormalIndex(text_normal.getText().trim().equals("") ? null
					: text_normal.getText().trim());
			tagModel.setEnergyIndex("".equals(text_energy.getText().trim()) ? null
					: text_energy.getText().trim());
			tagModel.setRatedPower("".equals(text_ratedpower.getText().trim()) ? null
					: text_ratedpower.getText().trim());

			tagModelDao.addVariable(tagModel);
			
			//添加默认存储器与自变量
			if(tagModel.getType().startsWith("遥脉")) {
				RecorderModel rm = new RecorderModel();
				rm.setTagModel(tagModel);

				rm.setInterval("30");
				rm.setMinValue("0");
				rm.setMaxValue("599999999");
				rm.setUnitValue("1");
//				StringBuilder strBuilder = new StringBuilder();
//				strBuilder.append("if (typeof(__value__) != \"undefined\") {"+"\r\n");
//				strBuilder.append("\t" + "var change = 0;"+"\r\n");
//				strBuilder.append("\t" + "if (typeof(上次转换表读数) == \"undefined\") {"+"\r\n");
//				strBuilder.append("\t\t" + "change = 0;"+"\r\n");
//				strBuilder.append("\t" + "} else if( (__value__ - 上次转换表读数) < 0) {"+"\r\n");
//				strBuilder.append("\t\t" + "change = (__value__ - 上次转换表读数 + 最大值 - 最小值) * 单位脉冲电度量;"+"\r\n");
//				strBuilder.append("\t" + "} else {"+"\r\n");
//				strBuilder.append("\t\t" + "change = (__value__ - 上次转换表读数) * 单位脉冲电度量;"+"\r\n");
//				strBuilder.append("\t" + "}"+"\r\n");
//				strBuilder.append("\t" + "上次转换表读数 = __value__;"+"\r\n");
//				strBuilder.append("\t" + "change;"+"\r\n");
//				strBuilder.append("}"+"\r\n");
//				if (typeof(__value__) != "undefined") {
//					var change = 0;
//					if (typeof(上次转换表读数) == "undefined") {
//						change = 0;
//					} else if( (__value__ - 上次转换表读数) < 0) {
//						change = (__value__ - 上次转换表读数 + 最大值 - 最小值) * 单位脉冲电度量;
//					} else {
//						change = (__value__ - 上次转换表读数) * 单位脉冲电度量;
//					}
//					上次转换表读数 = __value__;
//					change;
//				}
				
				//rm.setYmtagExpression(strBuilder.toString());
				rm.setYmtagExpression(null);
				
				rm.setName("遥脉数据存储器");
				rm.setType("YMTag");
				recorderModelDao.addRecorder(rm, GetXpathUtil.getTagModelXpath(tagModel));
				
//				<tag name="表底值" value="0" type="内存常量/数值型" />
//				<tag name="最小值" value="1" type="内存常量/数值型" />
//				<tag name="最大值" value="20000" type="内存常量/数值型" />
//				<tag name="单位脉冲电度量" value="2" type="内存常量/数值型" />
//				<tag name="脉冲灵敏度" value="1" type="内存常量/数值型" />
				//添加子变量
//				String xpath = GetXpathUtil.getTagModelXpath(tagModel);
//				
//				TagModel newTagModel = new TagModel();
//				newTagModel.setName("表底值");
//				newTagModel.setValue("0");
//				newTagModel.setType("内存常量/数值型");
//				
//				TagModel newTagModel1 = new TagModel();
//				newTagModel1.setName("最小值");
//				newTagModel1.setValue("0");
//				newTagModel1.setType("内存常量/数值型");
//				
//				TagModel newTagModel2 = new TagModel();
//				newTagModel2.setName("最大值");
//				newTagModel2.setValue("599999999");
//				newTagModel2.setType("内存常量/数值型");
//				
//				TagModel newTagModel3 = new TagModel();
//				newTagModel3.setName("单位脉冲电度量");
//				newTagModel3.setValue("1");
//				newTagModel3.setType("内存常量/数值型");
//				
//				TagModel newTagModel4 = new TagModel();
//				newTagModel4.setName("脉冲灵敏度");
//				newTagModel4.setValue("1");
//				newTagModel4.setType("内存常量/数值型");
//				
//				tagModelDao.addSubTagModel(xpath, newTagModel);
//				tagModelDao.addSubTagModel(xpath, newTagModel1);
//				tagModelDao.addSubTagModel(xpath, newTagModel2);
//				tagModelDao.addSubTagModel(xpath, newTagModel3);
//				tagModelDao.addSubTagModel(xpath, newTagModel4);
				
			} else if(tagModel.getType().startsWith("遥测")) {
				RecorderModel rm = new RecorderModel();
				rm.setTagModel(tagModel);
				
				rm.setName("遥测数据存储器");
				rm.setInterval("10");
				rm.setType("YCTag");
				recorderModelDao.addRecorder(rm, GetXpathUtil.getTagModelXpath(tagModel));
			} else if(tagModel.getType().startsWith("遥信")) {
				RecorderModel rm = new RecorderModel();
				rm.setTagModel(tagModel);
				
				rm.setName("变位报警");
				rm.setRs_onInfo("合闸");
				rm.setRs_offInfo("分闸");
				rm.setType("RSChange");
				recorderModelDao.addRecorder(rm, GetXpathUtil.getTagModelXpath(tagModel));
			}
			refreshList();
			clearText();

		} else {
			if (MessageDialog.openConfirm(getShell(), "修改", "确定要修改当前变量？")) {
				String oldname = tagModel.getName();
				
				String mainIndex = tagModel.getMainIndex();
				String tagType = tagModel.getType();

				TagModel newTagModel = new TagModel();

				newTagModel.setName(text_name.getText().trim());
				newTagModel.setVarName(text_varname.getText().trim());
				newTagModel.setType(tagType);
				newTagModel
						.setValue(text_value.getText().trim().equals("") ? null
								: text_value.getText().trim());
				newTagModel.setMainIndex(mainIndex);
				newTagModel.setNormalIndex(text_normal.getText().trim()
						.equals("") ? null : text_normal.getText().trim());
				newTagModel.setEnergyIndex("".equals(text_energy.getText()
						.trim()) ? null : text_energy.getText().trim());
				newTagModel.setRatedPower("".equals(text_ratedpower.getText()
						.trim()) ? null : text_ratedpower.getText().trim());

				tagModelDao.updateTagModel(newTagModel, oldname);
				
				refreshList();
				clearText();

			} else
				return;
		}
		tagModel = null;

	}

	/**
	 * 刷新列表
	 */
	private void refreshList() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				init();
				tagModel = null;
				var_list.setModel(dlm);
			}
		});

	}

	/**
	 * 清空文本
	 */
	private void clearText() {
		text_name.setText("");
		text_varname.setText("");
		text_id.setText("");
		text_tagtype.setText("");
		text_main.setText("");
		text_normal.setText("");
		text_energy.setText("");
		text_value.setText("");
		text_ratedpower.setText("");
	}

	/**
	 * 删除
	 */
	private void remove() {
		int[] selectedIndexs = var_list.getSelectedIndices();
		for(int i:selectedIndexs) {
			tagModelDao.removeTagModel(tagModelList.get(i));
//			tagModelList.remove(i);
		}
		XMLDocumentFactory.instance.saveMainDocument();
		
		refreshList();
		clearText();
		tagModel = null;
	}

	/**
	 * 生成变量名字
	 */
	private void generateVarName() {
		if ("".equals(text_normal.getText().trim())) {// 无建筑
			buildingClass = "X";
			buildingCode = "XXX";
		} else {
			String[] str = text_normal.getText().trim().split(",");
			for (String s : str) {
				log.debug(s);
				String buildingsName = indexNodeDao.getBuildingNode().getName();//建筑分区
				if (s.trim().startsWith(buildingsName)) {
					String name="";
					if(s.trim().length()>buildingsName.length()) {
						name = s.trim().substring(5).split("/")[0];
					}
					
					IndexNodeModel indexNodeModel = indexNodeDao
							.getBuildingByName(name);
					if (indexNodeModel == null) {
						buildingClass = "X";
						buildingCode = "XXX";
					} else {
						if(indexNodeModel.getType() == null) {
							buildingClass = "X";
						} else {
							buildingClass = indexNodeModel.getType();
						}
						
						if(indexNodeModel.getCode() == null) {
							buildingCode = "XXX";
						} else {
							buildingCode = indexNodeModel.getCode();
						}						
					}
					break;
				}
			}
		}

		if (text_tagtype.getText().trim().startsWith("遥脉")) {
			varType = "YM";

			if ("".equals(text_energy.getText().trim())) {
				energyClass = "XX";
				energyItem = "X";
				energyItem1 = "X";
				energyItem2 = "X";
			} else {
				String[] energyStr = text_energy.getText().trim().split("/");
				energyClass = "XX";
				energyItem = "X";
				energyItem1 = "X";
				energyItem2 = "X";

				if (energyStr.length >= 1) {
					String s[] = new String[1];
					s[0] = energyStr[0];
					IndexNodeModel inm = indexNodeDao
							.getEnergyCodeByStringArray(s);
					if (inm != null) {
						if(inm.getType() == null) {
							energyClass = "XX";
						} else {
							energyClass = inm.getType();
						}
					} else {
						energyClass = "XX";
					}
				}
				if (energyStr.length >= 2) {
					String s[] = new String[2];
					s[0] = energyStr[0];
					s[1] = energyStr[1];
					IndexNodeModel inm = indexNodeDao
							.getEnergyCodeByStringArray(s);
					if (inm != null) {
						if(inm.getType() == null) {
							energyItem = "X";
						} else {
							energyItem = inm.getType();
						}
					} else {
						energyItem = "X";
					}
				}
				if (energyStr.length >= 3) {
					String s[] = new String[3];
					s[0] = energyStr[0];
					s[1] = energyStr[1];
					s[2] = energyStr[2];
					IndexNodeModel inm = indexNodeDao
							.getEnergyCodeByStringArray(s);
					if (inm != null) {
						if(inm.getType() == null) {
							energyItem1 = "X";
						} else {
							energyItem1 = inm.getType();
						}
						
					} else {
						energyItem1 = "X";
					}
				}
				if (energyStr.length >= 4) {
					String s[] = new String[4];
					s[0] = energyStr[0];
					s[1] = energyStr[1];
					s[2] = energyStr[2];
					s[3] = energyStr[3];
					IndexNodeModel inm = indexNodeDao
							.getEnergyCodeByStringArray(s);
					if (inm != null) {
						if(inm.getType() == null) {
							energyItem2 = "X";
						} else {
							energyItem2 = inm.getType();
						}
						
					} else {
						energyItem2 = "X";
					}
				}

			}

			text_varname.setText(buildingClass+buildingCode+energyClass+energyItem+energyItem1+energyItem2+"_" + varType + varNum);
		} else {
			if(text_tagtype.getText().trim().startsWith("遥测")) {
				varType = "YC";
			} else if(text_tagtype.getText().trim().startsWith("遥信")) {
				varType = "YX";
			} else if(text_tagtype.getText().trim().startsWith("遥调")) {
				varType = "YT";
			} else if(text_tagtype.getText().trim().startsWith("遥控")) {
				varType = "YK";
			} else {
				varType = "XX";
			}
			
			text_varname.setText(buildingClass+buildingCode+"_" + varType + varNum);

		}

	}
	
	/**
	 * 清空存储器等
	 */
	private void clearSubControl() {
		recorder_combo.removeAll();
		trigger_text.setText("");
		generator_text.setText("");
		generatorrefer_text.setText("");
		subvar_list.removeAll();
		
	}
	
	/**
	 * 获得awt icon
	 * @param imagePath
	 * @return
	 */
	private ImageIcon getImageIcon(String imagePath) {
		
		Bundle bundle = Activator.getDefault().getBundle();
		URL url = bundle.getEntry(imagePath);//"icons/yc_var.png"
		String absolutePath = null;
		try {
			absolutePath = FileLocator.toFileURL(url).getPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ImageIcon(absolutePath);
	}
}
