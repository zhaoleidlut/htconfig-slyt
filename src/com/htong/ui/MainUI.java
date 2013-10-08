package com.htong.ui;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.wb.swt.ResourceManager;

import com.htong.tags.daoImpl.ChannelModelDaoImpl;
import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.type.IndexTypeEnum;
import com.htong.tags.windows.ConfigToDatabaseWindow;
import com.htong.tags.windows.DeviceDL645IOSettinsWindow;
import com.htong.tags.windows.DeviceModbusIOSettinsWindow;
import com.htong.tags.windows.EditVarDicWindow;
import com.htong.tags.windows.TreeFilterChannelWindow;
import com.htong.tags.windows.TreeFilterIndexLabelWindow;
import com.htong.tags.windows.VarialbleIOSettinsWindow;
import com.htong.ui.sorter.Sorter;
import com.htong.ui.tree.RootTreeModel;
import com.htong.ui.tree.TreeContentProvider;
import com.htong.ui.tree.TreeLabelProvider;
import com.htong.util.LayoutUtil;

public class MainUI {

	// private final static Logger log = Logger.getLogger(MainUI.class);

	protected Shell shell;
	private MenuManager menuMng;
	private IndexModelDaoImpl indexModelDao;
	public static TreeViewer treeViewer;
	private OperatingComposite operatingComposite; // 操作面板
	public static int channelNameSorter = 0; // 按名字排序 0不排序 -1降序 1升序
	public static int channelNumSorter = 1; // 按序号排序 0不排序 -1降序 1升序

	public static String channelFilterStr = null; // 通道过滤器，为null为不过滤，否则按通道名或者通道序号
	public static String indexLabelFilterStr = null; // 标签过滤器，为null为不过滤，否则按名字

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainUI window = new MainUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MainUI() {
		indexModelDao = new IndexModelDaoImpl();
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
		shell.setSize(750, 680);
		// shell.setText("丹东华通能源监控管理系统组态配置软件4.4.3 Win-32-Bit");
		shell.setText("丹东华通SCADA系统组态配置软件v4.6.0 Win-32-Bit");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell.setImage(ResourceManager.getPluginImage("htconfig-slyt",
				"icons/huatong.ico"));

		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), shell);

		final Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(MessageDialog.openQuestion(null, "确定", "确定要重新初始化所有变量名吗？")) {
					new InitAllVarName().initAllVarName();
				}
			}
		});
		mntmNewItem.setText("初始化所有变量标签");

		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ConfigToDatabaseWindow().open();
			}
		});
		menuItem.setText("将配置写入数据库");

		// MenuItem mntmSvg = new MenuItem(menu, SWT.NONE);
		// mntmSvg.setText("SVG界面设计");
		//
		// MenuItem mntmGis = new MenuItem(menu, SWT.NONE);
		// mntmGis.setText("GIS集成配置");
		//
		// MenuItem mntmDcs = new MenuItem(menu, SWT.NONE);
		// mntmDcs.setText("DCS界面组态");
		//
		// MenuItem menuItem_3 = new MenuItem(menu, SWT.NONE);
		// menuItem_3.setText("视频服务配置");

		MenuItem menuItem_4 = new MenuItem(menu, SWT.NONE);
		menuItem_4.setText("帮助");
		menuItem_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(menu.getShell(), "提示",
						"丹东华通测控有限公司版权所有！");
			}
		});

		SashForm sashForm = new SashForm(shell, SWT.BORDER | SWT.SMOOTH);
		sashForm.setSashWidth(1);

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		treeViewer = new TreeViewer(composite, SWT.BORDER);
		treeViewer.setSorter(new Sorter());
		treeViewer.setAutoExpandLevel(3);
		treeViewer.setContentProvider(new TreeContentProvider());
		treeViewer.setLabelProvider(new TreeLabelProvider());
		treeViewer.setInput(RootTreeModel.instanse);

		Tree tree = treeViewer.getTree();

		// 点击打开编辑页面
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					IStructuredSelection sel = ((IStructuredSelection) treeViewer
							.getSelection());
					if (!sel.isEmpty()) {
						final Object obj = ((IStructuredSelection) treeViewer
								.getSelection()).getFirstElement();
						Display.getDefault().timerExec(
								Display.getDefault().getDoubleClickTime(),
								new Runnable() {
									public void run() {
										edit(obj);
									}
								});
					}

				}
			}

		});

		menuMng = new MenuManager();
		menuMng.setRemoveAllWhenShown(true);
		menuMng.addMenuListener(new MenuListener(treeViewer));

		tree.setMenu(menuMng.createContextMenu(tree)); // 添加菜单

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginTop = 10;
		gl_composite_1.marginLeft = 10;
		composite_1.setLayout(gl_composite_1);

		operatingComposite = new OperatingComposite(composite_1, SWT.NONE);
		operatingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		sashForm.setWeights(new int[] { 192, 325 });

		ViewerFilter filter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {

				if (element instanceof ChannelModel) {
					if (channelFilterStr != null) {
						ChannelModel channel = (ChannelModel) element;
						if (channel.getName().contains(channelFilterStr)
								|| channel.getId().contains(channelFilterStr)) {
							return true;
						} else {
							return false;
						}
					}
				} else if (element instanceof IndexNodeModel) {
					if (indexLabelFilterStr != null) {
						IndexNodeModel indexNodeModel = (IndexNodeModel) element;
						IndexTypeEnum indexType = GetIndexType
								.getIndexType(indexNodeModel);
						if (indexType.equals(IndexTypeEnum.MAIN)) {// 主索引
							return filterResult(indexNodeModel.getElement());
						}
					}

				}

				return true;
			}
		};
		treeViewer.setFilters(new ViewerFilter[] { filter });

	}

	private class MenuListener implements IMenuListener {
		private TreeViewer treeViewer;

		public MenuListener(TreeViewer treeViewer) {
			this.treeViewer = treeViewer;
		}

		@Override
		public void menuAboutToShow(IMenuManager manager) {
			IStructuredSelection selection = (IStructuredSelection) treeViewer
					.getSelection();
			if (!selection.isEmpty()) {
				createContextMenu(selection.getFirstElement());
			}
		}

		/**
		 * 右键菜单内容
		 * 
		 * @param
		 */
		private void createContextMenu(final Object selectedObject) {
			if (selectedObject instanceof String) {
				final String str = (String) selectedObject;

				if (str.equals(RootTreeModel.instanse.labelIndex)) {// 标签索引配置
					Action f5 = new Action() {
						public void run() {
							MainUI.treeViewer.refresh();
						}
					};
					f5.setText("重新加载配置文件(&F)");
					menuMng.add(f5);
				} else if (str.equals(RootTreeModel.instanse.labelType)) {// 标签类型配置
					// 新建标签类型
					Action add = new Action() {
						public void run() {
							// 新建标签类型
							TagTypeModel tagTypeModel = new TagTypeModel();
							tagTypeModel.setParentObject(str);
							operatingComposite.editType(tagTypeModel, true,
									true);
						}
					};
					add.setText("新建标签类型(&A)");
					// add.setImageDescriptor(Activator
					// .getImageDescriptor(ImageKeys.ADD));
					menuMng.add(add);

					menuMng.add(new Separator());

					// 导入标签类型模板
					Action importTpl = new Action() {
						public void run() {
							// 导入标签类型模板
							ImportOtherFile.importLabelForType(str, treeViewer);
							treeViewer.setExpandedState(str, true);
						}
					};
					importTpl.setText("导入(&T)");
					menuMng.add(importTpl);
				} else if (str.equals("采集通道")) { // 采集通道
					// 新建采集通道
					Action add = new Action() {
						public void run() {
							// 新建采集通道
							ChannelModel channelModel = new ChannelModel();
							channelModel.setParentObject(str);
							operatingComposite.setTop(channelModel);

						}
					};
					add.setText("新建采集通道(&A)");
					menuMng.add(add);

					menuMng.add(new Separator());

					// 按名字排序
					Action sortByName = new Action() {
						public void run() {

							MainUI.channelNumSorter = 0;
							if (MainUI.channelNameSorter == 1) {
								MainUI.channelNameSorter = -1;
							} else if (MainUI.channelNameSorter == -1) {
								MainUI.channelNameSorter = 1;
							} else if (MainUI.channelNameSorter == 0) {
								MainUI.channelNameSorter = 1;
							}
							MainUI.treeViewer.refresh();

						}
					};
					sortByName.setText("按名字排序(&N)");
					menuMng.add(sortByName);

					// 按序号排序
					Action sortByNum = new Action() {
						public void run() {
							MainUI.channelNameSorter = 0;
							if (MainUI.channelNumSorter == 1) {
								MainUI.channelNumSorter = -1;
							} else if (MainUI.channelNumSorter == -1) {
								MainUI.channelNumSorter = 1;
							} else if (MainUI.channelNameSorter == 0) {
								MainUI.channelNumSorter = 1;
							}
							MainUI.treeViewer.refresh();

						}
					};
					sortByNum.setText("按序号排序(&M)");
					menuMng.add(sortByNum);

					menuMng.add(new Separator());

					// 查找
					Action find = new Action() {
						public void run() {
							// 查找
							TreeFilterChannelWindow tfcw = new TreeFilterChannelWindow();
							tfcw.open();

						}
					};
					find.setText("查找(&F)");
					menuMng.add(find);

					// 取消过滤
					Action findAll = new Action() {
						public void run() {
							// 查找
							channelFilterStr = null;
							treeViewer.refresh();

						}
					};
					findAll.setText("显示全部(&R)");
					menuMng.add(findAll);

					menuMng.add(new Separator());

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForChannel("采集通道",
									treeViewer);
							treeViewer.setExpandedState("采集通道", true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);

				} else if (str.equals("转发通道")) { // 转发通道
					// 新建转发通道
					Action add = new Action() {
						public void run() {
							// 新建转发通道
							MessageDialog.openWarning(null, "提示", "暂未开发");
						}
					};
					add.setText("新建转发通道(&A)");
					menuMng.add(add);
				}

			} else if (selectedObject instanceof IndexModel) { // 标签索引
				final IndexModel indexModel = (IndexModel) selectedObject;
				// 添加下级索引
				if (indexModel.getType().equals(IndexTypeEnum.MAIN.getType())) {
					Action add = new Action() {
						public void run() {
							// 添加下级索引
							IndexNodeModel inm = new IndexNodeModel();
							inm.setParentObject(indexModel);
							operatingComposite.editMain(inm);
						}
					};
					add.setText("添加下级标签(&A)");
					menuMng.add(add);

					Action editIndex = new Action() {
						public void run() {
							// 修改标签索引
							edit(indexModel);
						}
					};
					editIndex.setText("修改标签索引(&E)");
					menuMng.add(editIndex);

					menuMng.add(new Separator()); // 分隔线

					// 粘贴
					Action past = new Action() {
						public void run() {
							Element element = indexModel.getElement();
							IndexNodeModel inm = CopyAndPast.instance
									.pastNode(element);
							if (inm == null) {
								return;
							}

							inm.setParentObject(indexModel);

							treeViewer.add(indexModel, inm); // 更新树
							treeViewer.setExpandedState(indexModel, true);
						}
					};
					past.setText("粘贴标签索引(&P)");

					past.setEnabled(CopyAndPast.instance.hasCopy());

					menuMng.add(past);

					menuMng.add(new Separator()); // 分隔线

					// 查找
					Action find = new Action() {
						public void run() {
							// 查找
							TreeFilterIndexLabelWindow tfcw = new TreeFilterIndexLabelWindow();
							tfcw.open();
						}
					};
					find.setText("查找(&F)");
					menuMng.add(find);

					// 取消过滤
					Action findAll = new Action() {
						public void run() {
							// 查找
							indexLabelFilterStr = null;
							treeViewer.refresh();

						}
					};
					findAll.setText("显示全部(&R)");
					menuMng.add(findAll);

					menuMng.add(new Separator()); // 分隔线

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForMainIndex(indexModel,
									treeViewer);
							treeViewer.setExpandedState(indexModel, true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);

				} else if (indexModel.getType().equals(
						IndexTypeEnum.ENERGY.getType())) {
					Action add = new Action() {
						public void run() {
							// 添加下级索引
							IndexNodeModel inm = new IndexNodeModel();
							inm.setParentObject(indexModel);
							operatingComposite.editEnergy(inm);
						}
					};
					add.setText("添加能耗分类(&A)");
					menuMng.add(add);

					Action editIndex = new Action() {
						public void run() {
							// 修改标签索引
							edit(indexModel);
						}
					};
					editIndex.setText("修改标签索引(&E)");
					menuMng.add(editIndex);

					menuMng.add(new Separator());
					
					// 粘贴
					Action past = new Action() {
						public void run() {
							// 粘贴标签
							Element element = indexModel.getElement();
							IndexNodeModel inm = CopyAndPastEnergyIndex.instance
									.pastNode(element);
							if (inm == null) {
								return;
							}

							inm.setParentObject(indexModel);

							treeViewer.add(indexModel, inm); // 更新树
							treeViewer.setExpandedState(indexModel, true);
						}
					};
					past.setText("粘贴标签索引(&P)");
					past.setEnabled(CopyAndPastEnergyIndex.instance.hasCopy());
					menuMng.add(past);
					
					menuMng.add(new Separator());

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForOtherIndex(
									indexModel, treeViewer);
							treeViewer.setExpandedState(indexModel, true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);
				} else if (indexModel.getType().equals(
						IndexTypeEnum.NORMAL.getType())) {
					Action add = new Action() {
						public void run() {
							// 添加下级索引
							IndexNodeModel inm = new IndexNodeModel();
							inm.setParentObject(indexModel);
							operatingComposite.editNormal(inm);
						}
					};
					add.setText("添加常规分类(&A)");
					menuMng.add(add);

					Action editIndex = new Action() {
						public void run() {
							// 修改标签索引
							edit(indexModel);
						}
					};
					editIndex.setText("修改标签索引(&E)");
					menuMng.add(editIndex);

					menuMng.add(new Separator());

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForOtherIndex(
									indexModel, treeViewer);
							treeViewer.setExpandedState(indexModel, true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);
				}

			} else if (selectedObject instanceof IndexNodeModel) { // 标签索引节点
				final IndexNodeModel indexNodeModel = (IndexNodeModel) selectedObject;

				IndexTypeEnum indexType = GetIndexType
						.getIndexType(indexNodeModel);
				if (indexType.equals(IndexTypeEnum.MAIN)) {// 主索引
					// 添加下级索引
					Action add = new Action() {
						public void run() {
							// 添加下级索引

							IndexNodeModel inm = new IndexNodeModel();
							inm.setParentObject(indexNodeModel);
							operatingComposite.editMain(inm);
						}
					};
					add.setText("添加下级索引(&A)");
					menuMng.add(add);

					// 删除标签索引
					Action del = new Action() {
						public void run() {
							// 删除标签索引
							if (MessageDialog.openConfirm(treeViewer.getTree()
									.getShell(), "删除", "确认要删除吗？")) {
								IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

								indexNodeModelDao
										.removeIndexNodeModel(indexNodeModel);

								treeViewer.remove(indexNodeModel);

							}

						}
					};
					del.setText("删除标签索引(&D)");
					menuMng.add(del);

					// 修改标签
					Action edit = new Action() {
						public void run() {
							// 修改标签
							operatingComposite.editMain(indexNodeModel);
						}
					};
					edit.setText("修改标签索引(&E)");
					menuMng.add(edit);

					menuMng.add(new Separator()); // 分隔线

					// 复制
					Action copy = new Action() {
						public void run() {
							// 复制标签
							Element element = indexNodeModel.getElement();
							CopyAndPast.instance.copyNode(element);
						}
					};
					copy.setText("复制标签索引(&C)");
					menuMng.add(copy);

					// 粘贴
					Action past = new Action() {
						public void run() {
							// 粘贴标签
							Element element = indexNodeModel.getElement();
							IndexNodeModel inm = CopyAndPast.instance
									.pastNode(element);
							if (inm == null) {
								return;
							}

							inm.setParentObject(indexNodeModel);

							treeViewer.add(indexNodeModel, inm); // 更新树
							treeViewer.setExpandedState(indexNodeModel, true);
						}
					};
					past.setText("粘贴标签索引(&P)");
					menuMng.add(past);

					past.setEnabled(CopyAndPast.instance.hasCopy());

					menuMng.add(new Separator()); // 分隔线

					// 编辑变量词典
					Action editVar = new Action() {
						public void run() {
							// 编辑变量词典
							EditVarDicWindow evdw = new EditVarDicWindow(
									operatingComposite.getShell(),
									indexNodeModel);
							evdw.open();
						}
					};
					editVar.setText("编辑变量词典(&M)");
					// if (indexNodeModel.getUsed() == null
					// || "false".equals(indexNodeModel.getUsed())) {
					// editVar.setEnabled(false);
					// } else {
					// editVar.setEnabled(true);
					// }
					menuMng.add(editVar);

					// 批量关联索引
					// Action editAllIndex = new Action() {
					// public void run() {
					// new AllIndexEditWindow().open();
					// }
					// };
					// editAllIndex.setText("批量关联索引(&Z)");
					// menuMng.add(editAllIndex);

					menuMng.add(new Separator()); // 分隔线

					// 设定变量IO信息
					Action setIO = new Action() {
						public void run() {
							// 设定变量IO信息
							VarialbleIOSettinsWindow vw = new VarialbleIOSettinsWindow(
									operatingComposite.getShell(),
									indexNodeModel, false);
							vw.open();
						}
					};
					setIO.setText("设定变量IO信息(&S)");
					menuMng.add(setIO);

					// 设定变量IO信息
					Action setAllIO = new Action() {
						public void run() {
							// 设定变量IO信息
							VarialbleIOSettinsWindow vw = new VarialbleIOSettinsWindow(
									operatingComposite.getShell(),
									indexNodeModel, true);
							vw.open();
						}
					};
					setAllIO.setText("设定所有变量IO信息(&L)");
					menuMng.add(setAllIO);

					menuMng.add(new Separator());

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForMainIndexNode(
									indexNodeModel, treeViewer);
							treeViewer.setExpandedState(indexNodeModel, true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);

				} else if (indexType.equals(IndexTypeEnum.ENERGY)) {// 能耗分类分项
					// 添加下级索引
					Action add = new Action() {
						public void run() {
							// 添加下级索引

							IndexNodeModel inm = new IndexNodeModel();
							inm.setParentObject(indexNodeModel);
							operatingComposite.editEnergy(inm);
						}
					};
					add.setText("添加下级索引(&A)");
					menuMng.add(add);

					// 删除标签索引
					Action del = new Action() {
						public void run() {
							// 删除标签索引
							if (MessageDialog.openConfirm(treeViewer.getTree()
									.getShell(), "删除", "确认要删除吗？")) {
								IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

								indexNodeModelDao
										.removeIndexNodeModel(indexNodeModel);

								treeViewer.remove(indexNodeModel);

							}

						}
					};
					del.setText("删除标签索引(&D)");
					menuMng.add(del);

					// 修改标签
					Action edit = new Action() {
						public void run() {
							// 修改标签
							operatingComposite.editEnergy(indexNodeModel);
						}
					};
					edit.setText("修改标签索引(&E)");
					menuMng.add(edit);

					menuMng.add(new Separator()); // 分隔线
					
					// 复制
					Action copy = new Action() {
						public void run() {
							// 复制标签
							Element element = indexNodeModel.getElement();
							CopyAndPastEnergyIndex.instance.copyNode(element);
						}
					};
					copy.setText("复制标签索引(&C)");
					menuMng.add(copy);

					// 粘贴
					Action past = new Action() {
						public void run() {
							// 粘贴标签
							Element element = indexNodeModel.getElement();
							IndexNodeModel inm = CopyAndPastEnergyIndex.instance
									.pastNode(element);
							if (inm == null) {
								return;
							}

							inm.setParentObject(indexNodeModel);

							treeViewer.add(indexNodeModel, inm); // 更新树
							treeViewer.setExpandedState(indexNodeModel, true);
						}
					};
					past.setText("粘贴标签索引(&P)");
					past.setEnabled(CopyAndPastEnergyIndex.instance.hasCopy());
					menuMng.add(past);
					
					menuMng.add(new Separator());

					// 编辑变量词典
					Action editVar = new Action() {
						public void run() {
							// 编辑变量词典
							EditVarDicWindow evdw = new EditVarDicWindow(
									operatingComposite.getShell(),
									indexNodeModel);
							evdw.open();
						}
					};
					editVar.setText("编辑变量词典(&M)");
					menuMng.add(editVar);

					menuMng.add(new Separator());

					// 导入
					Action importIndex = new Action() {
						public void run() {
							ImportOtherFile.importLabelForOtherIndexNode(
									indexNodeModel, treeViewer);
							treeViewer.setExpandedState(indexNodeModel, true);
						}
					};
					importIndex.setText("导入(&T)");
					menuMng.add(importIndex);
				} else if (indexType.equals(IndexTypeEnum.NORMAL)) {// 常规分类分区
					if (indexNodeModel.getType() != null
							&& indexNodeModel.getType().equals("building")) {
						Action add = new Action() {
							public void run() {
								// 添加下级索引

								IndexNodeModel inm = new IndexNodeModel();
								inm.setParentObject(indexNodeModel);
								operatingComposite.editNormalBuilding(inm);
							}
						};
						add.setText("添加建筑(&A)");
						menuMng.add(add);

						// 修改建筑
						Action edit = new Action() {
							public void run() {
								// 修改标签
								operatingComposite
										.editNormalFenQu(indexNodeModel);
							}
						};
						edit.setText("修改建筑标签索引(&E)");
						menuMng.add(edit);

						
						menuMng.add(new Separator()); // 分隔线

						// 复制
						Action copy = new Action() {
							public void run() {
								// 复制标签
								Element element = indexNodeModel.getElement();
								CopyAndPastNormalIndex.instance.copyNode(element);
							}
						};
						copy.setText("复制标签索引(&C)");
						menuMng.add(copy);

						// 粘贴
						Action past = new Action() {
							public void run() {
								// 粘贴标签
								Element element = indexNodeModel.getElement();
								IndexNodeModel inm = CopyAndPastNormalIndex.instance
										.pastNode(element);
								if (inm == null) {
									return;
								}

								inm.setParentObject(indexNodeModel);

								treeViewer.add(indexNodeModel, inm); // 更新树
								treeViewer.setExpandedState(indexNodeModel, true);
							}
						};
						past.setText("粘贴标签索引(&P)");
						past.setEnabled(CopyAndPastNormalIndex.instance.hasCopy());
						
						menuMng.add(past);
						
						menuMng.add(new Separator());

						// 导入
						Action importIndex = new Action() {
							public void run() {
								ImportOtherFile.importLabelForOtherIndexNode(
										indexNodeModel, treeViewer);
								treeViewer.setExpandedState(indexNodeModel,
										true);
							}
						};
						importIndex.setText("导入(&T)");
						menuMng.add(importIndex);
					} else {
						Object parentObj = indexNodeModel.getParentObject();
						// 添加下级索引
						Action add = new Action() {
							public void run() {
								// 添加下级索引

								IndexNodeModel inm = new IndexNodeModel();
								inm.setParentObject(indexNodeModel);
								operatingComposite.editNormal(inm);
							}
						};
						add.setText("添加下级索引(&A)");
						menuMng.add(add);

						// 删除标签索引
						Action del = new Action() {
							public void run() {
								// 删除标签索引
								if (MessageDialog.openConfirm(treeViewer
										.getTree().getShell(), "删除", "确认要删除吗？")) {
									IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

									indexNodeModelDao
											.removeIndexNodeModel(indexNodeModel);

									treeViewer.remove(indexNodeModel);

								}

							}
						};
						del.setText("删除标签索引(&D)");
						menuMng.add(del);

						if ((parentObj instanceof IndexNodeModel)
								&& ((IndexNodeModel) parentObj).getType() != null
								&& ((IndexNodeModel) parentObj).getType()
										.equals("building")) {
							// 修改建筑
							Action edit = new Action() {
								public void run() {
									// 修改标签
									operatingComposite
											.editNormalBuilding(indexNodeModel);
								}
							};
							edit.setText("修改建筑(&E)");
							menuMng.add(edit);
						} else {
							Action edit = new Action() {
								public void run() {
									// 修改标签
									operatingComposite
											.editNormal(indexNodeModel);
								}
							};
							edit.setText("修改标签索引(&E)");
							menuMng.add(edit);
						}

						// // 复制
						// Action copy = new Action() {
						// public void run() {
						// // 复制标签
						// }
						// };
						// copy.setText("复制(&C)");
						// menuMng.add(copy);

						menuMng.add(new Separator()); // 分隔线
						// 复制
						Action copy = new Action() {
							public void run() {
								// 复制标签
								Element element = indexNodeModel.getElement();
								CopyAndPastNormalIndex.instance.copyNode(element);
							}
						};
						copy.setText("复制标签索引(&C)");
						menuMng.add(copy);

						// 粘贴
						Action past = new Action() {
							public void run() {
								// 粘贴标签
								Element element = indexNodeModel.getElement();
								IndexNodeModel inm = CopyAndPastNormalIndex.instance
										.pastNode(element);
								if (inm == null) {
									return;
								}

								inm.setParentObject(indexNodeModel);

								treeViewer.add(indexNodeModel, inm); // 更新树
								treeViewer.setExpandedState(indexNodeModel, true);
							}
						};
						past.setText("粘贴标签索引(&P)");
						past.setEnabled(CopyAndPastNormalIndex.instance.hasCopy());
						menuMng.add(past);
						
						menuMng.add(new Separator());

						// 编辑变量词典
						Action editVar = new Action() {
							public void run() {
								// 编辑变量词典
								EditVarDicWindow evdw = new EditVarDicWindow(
										operatingComposite.getShell(),
										indexNodeModel);
								evdw.open();
							}
						};
						editVar.setText("编辑变量词典(&M)");
						menuMng.add(editVar);

						menuMng.add(new Separator());

						// 导入
						Action importIndex = new Action() {
							public void run() {
								ImportOtherFile.importLabelForOtherIndexNode(
										indexNodeModel, treeViewer);
								treeViewer.setExpandedState(indexNodeModel,
										true);
							}
						};
						importIndex.setText("导入(&T)");
						menuMng.add(importIndex);

					}

				}

			} else if (selectedObject instanceof TagTypeModel) { // 标签类型
				final TagTypeModel tagTypeModel = (TagTypeModel) selectedObject;
				// 添加标签类型
				Action add = new Action() {
					public void run() {
						// 添加标签类型

						TagTypeModel ttm = new TagTypeModel();
						ttm.setParentObject(tagTypeModel);
						if (tagTypeModel.getName().startsWith("遥测")) {
							// ttm.setDataType("number");
							operatingComposite.editType(ttm, true, false);
						} else if (tagTypeModel.getName().startsWith("遥脉")) {
							// ttm.setDataType("number");
							operatingComposite.editType(ttm, true, false);
						} else if (tagTypeModel.getName().startsWith("遥调")) {
							// ttm.setDataType("number");
							operatingComposite.editType(ttm, true, false);
						} else if (tagTypeModel.getName().startsWith("遥信")) {
							// ttm.setDataType("bool");
							operatingComposite.editType(ttm, true, false);
						} else if (tagTypeModel.getName().startsWith("遥控")) {
							// ttm.setDataType("bool");
							operatingComposite.editType(ttm, true, false);
						} else {
							operatingComposite.editType(ttm, true, true);
						}
					}
				};
				add.setText("添加标签类型(&A)");
				menuMng.add(add);

				if (!(tagTypeModel.getParentObject() instanceof String && (tagTypeModel
						.getName().equals("遥测")
						|| tagTypeModel.getName().equals("遥信")
						|| tagTypeModel.getName().equals("遥脉")
						|| tagTypeModel.getName().equals("遥调")
						|| tagTypeModel.getName().equals("遥控")
						|| tagTypeModel.getName().equals("内存常量") || tagTypeModel
						.getName().equals("内存变量")))) {
					// 删除标签索引类型
					Action del = new Action() {
						public void run() {
							// 添加标签类型
							if (MessageDialog.openConfirm(treeViewer.getTree()
									.getShell(), "删除", "确认要删除吗？")) {
								TagTypeModelDaoImpl tagTypeModelDao = new TagTypeModelDaoImpl();
								tagTypeModelDao
										.removeTagTypeModel(tagTypeModel);
								treeViewer.remove(tagTypeModel);
							}

						}
					};
					del.setText("删除标签类型(&D)");
					menuMng.add(del);

					// 修改标签类型
					Action edit = new Action() {
						public void run() {
							edit(tagTypeModel);
						}
					};
					edit.setText("修改标签类型(&E)");
					menuMng.add(edit);
				}

				menuMng.add(new Separator());

				// 导入
				Action importIndex = new Action() {
					public void run() {
						ImportOtherFile.importLabelForType(tagTypeModel,
								treeViewer);
						treeViewer.setExpandedState(tagTypeModel, true);
					}
				};
				importIndex.setText("导入(&T)");
				menuMng.add(importIndex);
			} else if (selectedObject instanceof ChannelModel) { // 采集通道
				final ChannelModel channelModel = (ChannelModel) selectedObject;
				// 添加设备
				Action add = new Action() {
					public void run() {
						// 添加设备
						DeviceModel deviceModel = new DeviceModel();
						deviceModel.setParentObject(channelModel);
						operatingComposite.setTop(deviceModel);

					}
				};
				add.setText("添加设备(&A)");
				menuMng.add(add);

				// 修改采集通道
				Action edit = new Action() {
					public void run() {
						// 修改采集通道
						operatingComposite.setTop(channelModel);
					}
				};
				edit.setText("修改采集通道(&E)");
				menuMng.add(edit);

				// 删除采集通道
				Action del = new Action() {
					public void run() {
						// 删除采集通道
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除该采集通道吗？")) {
							ChannelModelDaoImpl channelModeDao = new ChannelModelDaoImpl();
							channelModeDao.removeChannelModelById(channelModel);
							treeViewer.remove(channelModel);
						}

					}
				};
				del.setText("删除采集通道(&D)");
				menuMng.add(del);

				menuMng.add(new Separator());

				// 导入设备
				Action im = new Action() {
					public void run() {
						ImportAndExport importDevice = new ImportAndExport();
						importDevice.importDevice(channelModel);
					}
				};
				im.setText("导入设备(&I)");
				menuMng.add(im);

				menuMng.add(new Separator());

				// 导入采集器
				Action imCJQ = new Action() {
					public void run() {
						ImportAndExport importDevice = new ImportAndExport();
						importDevice.importCJQ(channelModel);
					}
				};
				imCJQ.setText("导入采集器(&C)");
				menuMng.add(imCJQ);

				menuMng.add(new Separator());

				// 设定通道IO信息
				Action cc = new Action() {
					public void run() {
						new DeviceDL645IOSettinsWindow(
								operatingComposite.getShell(), null,
								channelModel).open();
					}
				};
				cc.setText("设定通道IO信息(&S)");
				menuMng.add(cc);

				menuMng.add(new Separator());

				// 导入
				Action importIndex = new Action() {
					public void run() {
						ImportOtherFile.importLabelForChannel(channelModel,
								treeViewer);
						treeViewer.setExpandedState(channelModel, true);
					}
				};
				importIndex.setText("导入(&T)");
				menuMng.add(importIndex);

			} else if (selectedObject instanceof DeviceModel) { // 设备
				final DeviceModel deviceModel = (DeviceModel) selectedObject;
				// 修改设备
				Action edit = new Action() {
					public void run() {
						// 修改设备
						operatingComposite.setTop(deviceModel);
					}
				};
				edit.setText("修改设备(&E)");
				menuMng.add(edit);

				// 删除设备
				Action del = new Action() {
					public void run() {
						// 删除设备
						if (MessageDialog.openConfirm(treeViewer.getTree()
								.getShell(), "删除", "确认要删除该设备吗？")) {
							DeviceModelDaoImpl deviceModelDao = new DeviceModelDaoImpl();
							deviceModelDao.removeDeviceModelById(deviceModel);
							treeViewer.remove(deviceModel);
						}

					}
				};
				del.setText("删除设备(&D)");
				menuMng.add(del);

				menuMng.add(new Separator()); // 分割线

				// 导出设备
				Action ex = new Action() {
					public void run() {
						ImportAndExport exportDevice = new ImportAndExport();
						exportDevice.exportDevice(deviceModel);
					}
				};
				ex.setText("导出设备IO信息(&I)");
				menuMng.add(ex);

				// 导入设备IO信息
				Action exIO = new Action() {
					public void run() {
						ImportAndExport importDevice = new ImportAndExport();
						importDevice.importDeviceIOInfo(deviceModel);
					}
				};
				exIO.setText("导入设备IO信息(&R)");
				menuMng.add(exIO);

				menuMng.add(new Separator()); // 分割线

				// 导入设备IO信息
				Action im = new Action() {
					public void run() {
						ImportAndExport importDevice = new ImportAndExport();
						importDevice.importCJQIOInfo(deviceModel);
					}
				};
				im.setText("导入采集器IO信息(&J)");
				menuMng.add(im);

				menuMng.add(new Separator()); // 分割线

				// 设定设备IO信息
				Action io = new Action() {
					public void run() {
						// 设定IO信息

						ChannelModel cm = (ChannelModel) deviceModel
								.getParentObject();
						if (cm.getProtocal().startsWith("dl")) {
							new DeviceDL645IOSettinsWindow(
									operatingComposite.getShell(), deviceModel,
									null).open();
						} else {
							new DeviceModbusIOSettinsWindow(
									operatingComposite.getShell(), deviceModel,
									null).open();
						}

					}
				};
				io.setText("设定设备IO信息(&S)");
				menuMng.add(io);

			}
		}

	}

	private void edit(Object object) {
		if (object instanceof IndexModel) {
			operatingComposite.setTop(object);
		} else if (object instanceof IndexNodeModel) {
			IndexNodeModel indexNodeModel = (IndexNodeModel) object;

			Object parentObject = indexNodeModel.getParentObject();
			while (!(parentObject instanceof IndexModel)) {
				parentObject = ((IndexNodeModel) parentObject)
						.getParentObject();
			}
			IndexModel im = (IndexModel) parentObject;
			IndexTypeEnum indexType = IndexTypeEnum.getByLabel(im.getType());

			if (indexType.equals(IndexTypeEnum.MAIN)) {// 主索引
				operatingComposite.editMain(indexNodeModel);
			} else if (indexType.equals(IndexTypeEnum.NORMAL)) {// 常规分类索引

				Object parentObj = indexNodeModel.getParentObject();
				if ((parentObj instanceof IndexNodeModel)
						&& ((IndexNodeModel) parentObj).getType() != null
						&& ((IndexNodeModel) parentObj).getType().equals(
								"building")) {
					operatingComposite.editNormalBuilding(indexNodeModel);
				} else if (indexNodeModel.getType() != null
						&& indexNodeModel.getType().endsWith("building")) {
					operatingComposite.editNormalFenQu(indexNodeModel);
				} else {
					operatingComposite.editNormal(indexNodeModel);
				}

			} else if (indexType.equals(IndexTypeEnum.ENERGY)) {// 能耗分类分区
				operatingComposite.editEnergy(indexNodeModel);
			}
		} else if (object instanceof TagTypeModel) {
			TagTypeModel tagTypeModel = (TagTypeModel) object;
			Object parentO = tagTypeModel.getParentObject();
			if ((parentO instanceof String)
					&& (tagTypeModel.getName().equals("遥信")
							|| tagTypeModel.getName().equals("遥测")
							|| tagTypeModel.getName().equals("遥脉")
							|| tagTypeModel.getName().equals("遥调")
							|| tagTypeModel.getName().equals("遥控")
							|| tagTypeModel.getName().equals("内存变量") || tagTypeModel
							.getName().equals("内存常量"))) {
				operatingComposite.editType(tagTypeModel, false, false);// 常用一级不可编辑
			} else {

				if (parentO instanceof TagTypeModel) {
					TagTypeModel t = (TagTypeModel) parentO;
					if (t.getName().equals("遥信") || t.getName().equals("遥测")
							|| t.getName().equals("遥脉")
							|| t.getName().equals("遥调")
							|| t.getName().equals("遥控")) {
						operatingComposite.editType(tagTypeModel, true, false);// 常用二级
					} else {
						operatingComposite.editType(tagTypeModel, true, true);// 三级
					}
				} else {
					operatingComposite.editType(tagTypeModel, true, true);// 非常用一级
				}

			}
		} else if (object instanceof ChannelModel) {
			operatingComposite.setTop(object);
		} else if (object instanceof DeviceModel) {
			operatingComposite.setTop(object);
		} else if(object instanceof String) {
			if("采集通道".equals((String)object)) {
				operatingComposite.editChannels();
			}
		}

	}

	/**
	 * 过滤结果
	 * @param element
	 * @return
	 */
	private boolean filterResult(Element element) {
		if(element.attributeValue("name")!=null && element.attributeValue("name").contains(indexLabelFilterStr)) {
			return true;
		}
		for(Element iElement : (List<Element>)element.elements("indexnode")) {
			if(filterResult(iElement)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
}
