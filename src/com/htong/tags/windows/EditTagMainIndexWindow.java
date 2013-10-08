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
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.tree.TagMainIndexTreeContentProvider;
import com.htong.ui.tree.TagMainIndexTreeLabelProvider;
import com.htong.util.LayoutUtil;

/**
 * 编辑主索引窗口
 * 
 * @author 赵磊
 * 
 */
public class EditTagMainIndexWindow extends ApplicationWindow {
	private final Logger log = Logger.getLogger(EditTagMainIndexWindow.class);
	private List<IndexNodeModel> indexNodeModelList = new ArrayList<IndexNodeModel>();

	private TagModel tagModel;
	private CheckboxTreeViewer treeViewer;
	private ChannelModel channel;	
	private DeviceModel deviceModel;

	private Text text; // 编辑的文本框

	/**
	 * Create the application window.
	 */
	public EditTagMainIndexWindow(TagModel tagModel, Text text) {
		super(null);
		this.tagModel = tagModel;
		this.text = text;
	}
	
	public EditTagMainIndexWindow(ChannelModel channel, Text text) {
		super(null);
		this.channel = channel;
		this.text = text;
	}
	
	public EditTagMainIndexWindow(DeviceModel deviceModel, Text text) {
		super(null);
		this.deviceModel = deviceModel;
		this.text = text;
	}

	/**
	 * 初始化
	 */
	private void init() {
		IndexModelDaoImpl dao = new IndexModelDaoImpl();
		IndexModel indexModel = dao.getMainIndexModel();
		
		Element element = indexModel.getElement();
		if(element.element(IndexNodeModel.NODE_NAME) != null) {
			for ( Iterator i = element.elementIterator(IndexNodeModel.NODE_NAME); i.hasNext(); ) {
	            Element e = (Element) i.next();
	            IndexNodeModel im = new IndexNodeModel(e);
	            im.setParentObject(indexModel);	//设置上级节点
	            indexNodeModelList.add(im);
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

		treeViewer = new CheckboxTreeViewer(container, SWT.BORDER | SWT.CHECK);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS); // 展开所有节点
		Tree tree = treeViewer.getTree();
		treeViewer.setLabelProvider(new TagMainIndexTreeLabelProvider());
		treeViewer.setContentProvider(new TagMainIndexTreeContentProvider());
		treeViewer.setInput(indexNodeModelList);

		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object entry = event.getElement();
				// 实现单选
				for (Object o : treeViewer.getCheckedElements()) {
					treeViewer.setChecked(o, false);
				}
				treeViewer.setChecked(entry, event.getChecked());

				// 设置上级为选择
				if (entry instanceof IndexNodeModel) {
					IndexNodeModel indexNodeModel = ((IndexNodeModel) entry);
					Object o = indexNodeModel.getParentObject();
					while (!(o instanceof IndexModel)) {
						treeViewer.setChecked(o, true);
						o = ((IndexNodeModel) o).getParentObject();
					}
					treeViewer.setChecked(o, true);//设置根节点为选择
				}
				// 同时取消子节点的选择
				if (!treeViewer.getChecked(entry)) {
					treeViewer.setSubtreeChecked(entry, false);
				}

				// 改变编辑变量词典窗口中 text文字
				StringBuilder sb = new StringBuilder();
				for(Object o : treeViewer.getCheckedElements()) {
					boolean isRootCheck = true;	//判断是否为所选的根节点
					for(Object object : treeViewer.getCheckedElements()) {
						if(object == o) {
							continue;
						} else {
							if(object instanceof IndexModel) {
								continue;	//根节点则返回
							} else {
								IndexNodeModel indexNodeModel = (IndexNodeModel)object;
								if(indexNodeModel.getParentObject() == o) {
									isRootCheck = false;
									break;
								}
							}
						}
					}
					if(isRootCheck) {//为所找节点
						String typePath = "/" + XMLDocumentFactory.ROOT_NODE
								+ "/" + XMLDocumentFactory.TAGINDEX_NODE;
						Element e = null;
						if(o instanceof IndexModel) {
							e = ((IndexModel) o).getElement();
						} else {
							e = ((IndexNodeModel) o).getElement();
						}
						
						sb.append(e.attributeValue("name"));
						Element parentElement = e.getParent();
						while (!parentElement.getPath().equals(typePath)) {
							sb.insert(
									0,
									parentElement
											.attributeValue("name")
											+ "/");
							parentElement = parentElement.getParent();
						}
						break;
					}
				}
				String path = "/root/tagindex/index[@type='main']";
				Element element = (Element) XMLDocumentFactory.instance.getMainDocument().selectSingleNode(path);
				int len = element.attributeValue("name").length() + 1;
				if(tagModel != null) {
					tagModel.setMainIndex(sb.toString());
					text.setText(tagModel.getMainIndex().length()<len?"":tagModel.getMainIndex().substring(len));
				} else if(channel != null) {
					String str = sb.toString().length()<len?"":sb.toString().substring(len);
					text.setText(str);
					channel.setFixPositon(str);
				} else if(deviceModel != null) {
					String str = sb.toString().length()<len?"":sb.toString().substring(len);
					text.setText(str);
					deviceModel.setFixPosition(str);
				}
				
				
//				close();
			}
		});

		// 初始化选择
		if(tagModel != null) {
			if(tagModel.getMainIndex() != null) {//非新建
				String str[] = tagModel.getMainIndex().split("/");
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
		} else if(channel != null) {
			if(channel.getFixPositon() != null) {//非新建
				String str[] = channel.getFixPositon().split("/");
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
		} else if(deviceModel != null) {
			if(deviceModel.getFixPosition() != null) {//非新建
				String str[] = deviceModel.getFixPosition().split("/");
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
		
		

		return container;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
//		try {
//			EditTagMainIndexWindow window = new EditTagMainIndexWindow(null, null);
//			window.setBlockOnOpen(true);
//			window.open();
//			Display.getCurrent().dispose();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
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
		newShell.setText("选择变量标签索引");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(273, 454);
	}
}
