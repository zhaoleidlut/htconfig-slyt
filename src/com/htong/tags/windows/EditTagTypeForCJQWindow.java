package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
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

import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.tree.TagTypeTreeContentProvider;
import com.htong.ui.tree.TagTypeTreeLabelProvider;
import com.htong.util.LayoutUtil;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

/**
 * 编辑标签类型窗口
 * 
 * @author 赵磊
 * 
 */
public class EditTagTypeForCJQWindow extends Dialog {

	
	public EditTagTypeForCJQWindow(Shell parentShell, TagModel tagModel) {
		super(parentShell);
		setBlockOnOpen(true);
		this.tagModel = tagModel;

	}

	private final Logger log = Logger.getLogger(EditTagTypeForCJQWindow.class);
	private List<TagTypeModel> tagTypeList = new ArrayList<TagTypeModel>();

	private TagModel tagModel;
	private CheckboxTreeViewer treeViewer;


	/**
	 * Create the application window.
	 */


	/**
	 * 初始化
	 */
	private void init() {
		TagTypeModelDaoImpl dao = new TagTypeModelDaoImpl();
		tagTypeList = dao.getAllTagTypeModel();
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
		container.setLayout(null);

		treeViewer = new CheckboxTreeViewer(container, SWT.BORDER | SWT.CHECK);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS); // 展开所有节点
		Tree tree = treeViewer.getTree();
		tree.setBounds(0, 0, 266, 422);
		treeViewer.setLabelProvider(new TagTypeTreeLabelProvider());
		treeViewer.setContentProvider(new TagTypeTreeContentProvider());
		treeViewer.setInput(tagTypeList);

		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object entry = event.getElement();
				// 实现单选
				for (Object o : treeViewer.getCheckedElements()) {
					treeViewer.setChecked(o, false);
				}
				treeViewer.setChecked(entry, event.getChecked());

				// 取消相同索引的选择
				Object parentO = ((TagTypeModel) entry).getParentObject();
				Object rootO = parentO;// 根节点

				if (parentO != null) {
					while (parentO != null) {
						rootO = parentO;
						parentO = ((TagTypeModel) parentO).getParentObject();
					}
					for (TagTypeModel im : tagTypeList) {
						if (im.equals(rootO)) {
							treeViewer.setSubtreeChecked(im, false);
							break;
						}
					}
					treeViewer.setChecked(entry, true);
				}

				// 设置上级为选择
				if (entry instanceof TagTypeModel) {
					TagTypeModel tagTypeModel = ((TagTypeModel) entry);
					Object o = tagTypeModel.getParentObject();
					while (o != null) {
						treeViewer.setChecked(o, true);
						o = ((TagTypeModel) o).getParentObject();
					}
				}
				// 同时取消子节点的选择
				if (!treeViewer.getChecked(entry)) {
					treeViewer.setSubtreeChecked(entry, false);
				}

				// 改变编辑变量词典窗口中 tagtype文字
				StringBuilder sb = new StringBuilder();
				for(Object o : treeViewer.getCheckedElements()) {
					boolean isRootCheck = true;	//判断是否为所选的根节点
					for(Object object : treeViewer.getCheckedElements()) {
						if(object == o) {
							continue;
						} else {
							
							TagTypeModel ttm = (TagTypeModel)object;
							if(ttm.getParentObject() == o) {
								isRootCheck = false;
								break;
							}
						}
					}
					if(isRootCheck) {//为所找节点
						String typePath = "/" + XMLDocumentFactory.ROOT_NODE
								+ "/" + XMLDocumentFactory.TAGTYPE_NODE;
						Element e = ((TagTypeModel) o).getElement();
						sb.append(e.attributeValue(TagTypeModel.NAME_ATTR));
						Element parentElement = e.getParent();
						while (!parentElement.getPath().equals(typePath)) {
							sb.insert(
									0,
									parentElement
											.attributeValue(TagTypeModel.NAME_ATTR)
											+ "/");
							parentElement = parentElement.getParent();
						}
						break;
					}
				}
//				for (Object o : treeViewer.getCheckedElements()) {
//					Element e = ((TagTypeModel) o).getElement();
//					if (!e.elementIterator().hasNext()) {
//						sb.append(e.attributeValue(TagTypeModel.NAME_ATTR));
//						Element parentElement = e.getParent();
//						String typePath = "/" + XMLDocumentFactory.ROOT_NODE
//								+ "/" + XMLDocumentFactory.TAGTYPE_NODE;
//						while (!parentElement.getPath().equals(typePath)) {
//							sb.insert(
//									0,
//									parentElement
//											.attributeValue(TagTypeModel.NAME_ATTR)
//											+ "-");
//							parentElement = parentElement.getParent();
//						}
//						break; // 第一个
//					}
//				}
				tagModel.setType(sb.toString());

			}
		});

		// 初始化选择
		
			String str[] = tagModel.getType().split("/");
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
		
		

		return container;
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
		newShell.setText("选择标签类型");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(273, 454);
	}
	
	
}
