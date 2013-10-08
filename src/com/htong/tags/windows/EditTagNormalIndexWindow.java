package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.htong.tags.daoImpl.IndexModelDaoImpl;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.ui.tree.TagNormalIndexTreeContentProvider;
import com.htong.ui.tree.TagNormalIndexTreeLabelProvider;
import com.htong.util.LayoutUtil;

/**
 * 编辑常规分类索引
 * 
 * @author 赵磊
 * 
 */
public class EditTagNormalIndexWindow extends ApplicationWindow {

	private final Logger log = Logger.getLogger(EditTagNormalIndexWindow.class);
	private List<IndexNodeModel> indexNodeList = new ArrayList<IndexNodeModel>();

	private TagModel tagModel; // 变量
	private Text text; // 更改的文本
	private IndexNodeModel indexNodeModel;

	private String[] secondIndexStr;
	

	/**
	 * Create the application window.
	 */
	public EditTagNormalIndexWindow(TagModel tagModel, Text text) {
		super(null);
		this.tagModel = tagModel;
		this.text = text;
	}
	
	public EditTagNormalIndexWindow(IndexNodeModel indexNodeModel, Text text) {
		super(null);
		this.indexNodeModel = indexNodeModel;
		this.text = text;
	}

	/**
	 * 初始化
	 */
	private void init() {
		IndexModelDaoImpl dao = new IndexModelDaoImpl();
		IndexModel indexModel = dao.getNormalIndexModel();
		Element element = indexModel.getElement();
		if (element.element(IndexNodeModel.NODE_NAME) != null) {
			for (Iterator i = element.elementIterator(IndexNodeModel.NODE_NAME); i
					.hasNext();) {
				Element e = (Element) i.next();
				IndexNodeModel im = new IndexNodeModel(e);
				im.setParentObject(indexModel); // 设置上级节点
				indexNodeList.add(im);
			}
		}

		if(tagModel != null) {//变量的
			if (tagModel.getName() != null && tagModel.getNormalIndex() != null) {// 非新建
				secondIndexStr = tagModel.getNormalIndex().split(",");
			}
		} else {//索引的
			if (indexNodeModel.getName() != null && indexNodeModel.getNormalIndex() != null) {// 非新建
				secondIndexStr = indexNodeModel.getNormalIndex().split(",");
			}
		}
		
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		init();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		final CheckboxTreeViewer treeViewer = new CheckboxTreeViewer(container,
				SWT.BORDER | SWT.CHECK);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		final Tree tree = treeViewer.getTree();
		treeViewer.setLabelProvider(new TagNormalIndexTreeLabelProvider());
		treeViewer.setContentProvider(new TagNormalIndexTreeContentProvider());
		treeViewer.setInput(indexNodeList);

		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				IndexNodeModel entry = (IndexNodeModel) event.getElement();

				// 取消相同索引的选择
				IndexNodeModel parentO = entry;

				while (!(parentO.getParentObject() instanceof IndexModel)) {
					parentO = (IndexNodeModel) parentO.getParentObject();
				}
				for (IndexNodeModel im : indexNodeList) {
					if (im.equals(parentO)) {
						treeViewer.setSubtreeChecked(parentO, false);
						break;
					}
				}
				treeViewer.setChecked(entry, event.getChecked());

				// 设置上级为选择
				if (entry instanceof IndexNodeModel) {
					IndexNodeModel indexNodeModel = ((IndexNodeModel) entry);
					Object o = indexNodeModel.getParentObject();
					while (o != null) {
						treeViewer.setChecked(o, true);
						if (o instanceof IndexNodeModel) {
							o = ((IndexNodeModel) o).getParentObject();
						} else if (o instanceof IndexModel) {
							break;
						}
					}
				}
				// 同时取消子节点的选择
				if (!treeViewer.getChecked(entry)) {
					treeViewer.setSubtreeChecked(entry, false);
				}

				// 同步更新编辑变量词典的文本框
				Object checkedObject[] = treeViewer.getCheckedElements();
				StringBuilder sb = new StringBuilder();
				for (Object o : checkedObject) {
					boolean hasChild = false;
					for (Object oo : checkedObject) {
						if (oo == o) {
							continue;
						} else {
							if (oo instanceof IndexNodeModel) {
								if (((IndexNodeModel) oo).getParentObject()
										.equals(o)) {
									hasChild = true;
									break;
								}
							} else {
								continue;
							}
						}
					}
					if (!hasChild) {// 叶子节点
						
						while (!(o instanceof IndexModel)) {
							sb.insert(0, "/" + ((IndexNodeModel) o).getName());
							o = ((IndexNodeModel) o).getParentObject();
						}
						if(sb.toString().length()>1) {
							sb.deleteCharAt(0);	//删除最前面的“/”
						}
						sb.insert(0, ", ");

					}
				}
				if(sb.toString().length()>2) {
					sb.deleteCharAt(0);
					sb.deleteCharAt(0);// 删除最前面的‘, ’
				}
				log.debug(sb.toString());
				if(tagModel!=null) {
					tagModel.setNormalIndex(sb.toString());
				} else {
					indexNodeModel.setNormalIndex(sb.toString());
				}
				
				text.setText(sb.toString());
			}
		});

		if(tagModel != null) {
			if (tagModel.getNormalIndex() != null) {
				// 初始化选择
				if(secondIndexStr != null) {
					for (String indexStr : secondIndexStr) {
						String str[] = indexStr.trim().split("/");
						TreeItem treeItems[] = tree.getItems();
						for (String s : str) {
							for (TreeItem ti : treeItems) {
								if (ti.getText().equals(s.trim())) {
									ti.setChecked(true);
									treeItems = ti.getItems();
									break;
								}
							}
						}
					}
				}
				
			}
		} else if(indexNodeModel != null) {
			if (indexNodeModel.getNormalIndex() != null) {
				// 初始化选择
				if(secondIndexStr != null) {
					for (String indexStr : secondIndexStr) {
						String str[] = indexStr.trim().split("/");
						TreeItem treeItems[] = tree.getItems();
						for (String s : str) {
							for (TreeItem ti : treeItems) {
								if (ti.getText().equals(s.trim())) {
									ti.setChecked(true);
									treeItems = ti.getItems();
									break;
								}
							}
						}
					}
				}
				
			}
		}
		

		return container;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			EditTagNormalIndexWindow window = new EditTagNormalIndexWindow(
					new TagModel(), null);
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), newShell);
		super.configureShell(newShell);
		newShell.setText("选择常规分类索引");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(343, 489);
	}
}
