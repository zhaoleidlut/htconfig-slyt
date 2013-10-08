package com.htong.tags.composites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.daoImpl.TimeRangeDaoImpl;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TimeRange;
import com.htong.tags.model.enums.TimeRangeType;
import com.htong.ui.MainUI;
import com.htong.ui.OperatingComposite;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * 标签索引编辑面板
 * 
 * @author 赵磊
 * 
 */
public class IndexNodeEnergyEditComposite extends Composite {
	
	private static final Logger log = Logger.getLogger(IndexNodeEnergyEditComposite.class);
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			TimeRange timeRange = (TimeRange)element;
			if(columnIndex == 0) {
				return timeRange.getName()==null?"":timeRange.getName();
			} else if(columnIndex == 1) {
				if(timeRange.getType() == null) {
					return "";
				} else {
					return TimeRangeType.valueOf(timeRange.getType()).getType();
				}
			} else if(columnIndex == 2) {
				return timeRange.getStart()==null?"":timeRange.getStart();
			} else if(columnIndex == 3) {
				return timeRange.getEnd()==null?"":timeRange.getEnd();
			} else if(columnIndex == 4) {
				return timeRange.getDayInclude()==null?"":timeRange.getDayInclude();
			} else if(columnIndex == 5) {
				return timeRange.getDayExclude()==null?"":timeRange.getDayExclude();
			} else if(columnIndex == 6) {
				return timeRange.getWeekInclude()==null?"":timeRange.getWeekInclude();
			} else if(columnIndex == 7) {
				return timeRange.getWeekExclude()==null?"":timeRange.getWeekExclude();
			}
			return "";
		}
	}
	


	private Text text_name;
	private IndexNodeModel indexNodeModel;
	private Text type_text;
	private Table table;
	private Text text;
	private List<TimeRange> timeRangeList;
	private TimeRangeDaoImpl timeRangeDao;


	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IndexNodeEnergyEditComposite(final Composite parent, int style,
			final IndexNodeModel indexNodeModel) {
		super(parent, style);


		this.indexNodeModel = indexNodeModel;
		
		timeRangeDao = new TimeRangeDaoImpl();
		timeRangeList = timeRangeDao.getAllTimeRange(indexNodeModel);
		if(timeRangeList == null || timeRangeList.isEmpty()) {
			timeRangeList = new ArrayList<TimeRange>();
		}
		log.debug("分时统计个数：" + timeRangeList.size());
		
		
		GridLayout gridLayout = new GridLayout(3, false);
		setLayout(gridLayout);

		Label name = new Label(this, SWT.NONE);
		name.setText("分类分项名字：");

		text_name = new Text(this, SWT.BORDER);
		GridData gd_text_name = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_text_name.widthHint = 100;
		text_name.setLayoutData(gd_text_name);
		text_name.setText(indexNodeModel.getName() == null ? ""
				: indexNodeModel.getName());

		new Label(this, SWT.NONE);
		



		Label label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("编码：");

		type_text = new Text(this, SWT.BORDER);
		GridData gd_type_text = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_type_text.widthHint = 100;
		type_text.setLayoutData(gd_type_text);
		
		type_text.setText(indexNodeModel.getType() != null?indexNodeModel.getType():"");
		new Label(this, SWT.NONE);
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Group group = new Group(composite_1, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		group.setText("分时统计");
		
		final TableViewer tableViewer = new TableViewer(group, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);

		table.setHeaderVisible(true);
		GridData gd_table = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_table.heightHint = 150;
		gd_table.minimumHeight = 80;
		table.setLayoutData(gd_table);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getName()==null?"":timeRange.getName();
			}
			protected void setValue(Object element, Object value) {
				String name = (String)value;
				if("".equals(name)) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
				}
				TimeRange timeRange = (TimeRange)element;
				if(timeRangeList != null && !timeRangeList.isEmpty()) {
					for(TimeRange tr : timeRangeList) {
						if(tr.equals(timeRange)) {
							continue;
						} else if(tr.getName().equals(name)) {
							MessageDialog.openError(getShell(), "错误", tr.getName() + "已存在");
							break;
						}
					}
				}
				
				timeRange.setName(name);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(80);
		tableColumn.setText("名字");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor editor = new ComboBoxCellEditor(
						table, TimeRangeType.typeValues, SWT.READ_ONLY);
				return editor;
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				
				if(timeRange.getType() == null) {
					return -1;
				} else {
					int i = 0;
					for(;i<TimeRangeType.typeValues.length;i++) {
						if(TimeRangeType.valueOf(timeRange.getType()).getType().equals(TimeRangeType.typeValues[i])) {
							break;
						}
					}
					return i;
				}
			}
			protected void setValue(Object element, Object value) {
				int type = (Integer)value;
				TimeRange timeRange = (TimeRange)element;
				if(type==-1) {
					timeRange.setType(null);
				} else {
					timeRange.setType(TimeRangeType.getByLabel(TimeRangeType.typeValues[type]).toString());
				}
				tableViewer.update(timeRange, null);
				
			}
		});
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(61);
		tableColumn_1.setText("类型");
		
		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_6.setEditingSupport(new EditingSupport(tableViewer) {//起始时间
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getStart() == null?"":timeRange.getStart();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String start = (String) value;
				timeRange.setStart("".equals(start)?null:start);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_6 = tableViewerColumn_6.getColumn();
		tableColumn_6.setWidth(74);
		tableColumn_6.setText("起始时间");
		
		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_7.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getEnd()==null?"":timeRange.getEnd();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String v = (String)value;
				timeRange.setEnd("".equals(v)?null:v);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_7 = tableViewerColumn_7.getColumn();
		tableColumn_7.setWidth(73);
		tableColumn_7.setText("结束时间");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getDayInclude()==null?"":timeRange.getDayInclude();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String v = (String)value;
				timeRange.setDayInclude("".equals(v)?null:v);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_2 = tableViewerColumn_2.getColumn();
		tableColumn_2.setWidth(105);
		tableColumn_2.setText("统计日");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_3.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getDayExclude()==null?"":timeRange.getDayExclude();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String v = (String)value;
				timeRange.setDayExclude("".equals(v)?null:v);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_3 = tableViewerColumn_3.getColumn();
		tableColumn_3.setWidth(108);
		tableColumn_3.setText("不统计日");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_4.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getWeekInclude()==null?"":timeRange.getWeekInclude();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String v = (String)value;
				timeRange.setWeekInclude("".equals(v)?null:v);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_4 = tableViewerColumn_4.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("统计星期");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_5.setEditingSupport(new EditingSupport(tableViewer) {
			protected boolean canEdit(Object element) {
				return true;
			}
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(table);
			}
			protected Object getValue(Object element) {
				TimeRange timeRange = (TimeRange)element;
				return timeRange.getWeekExclude()==null?"":timeRange.getWeekExclude();
			}
			protected void setValue(Object element, Object value) {
				TimeRange timeRange = (TimeRange)element;
				String v = (String)value;
				timeRange.setWeekExclude("".equals(v)?null:v);
				tableViewer.update(timeRange, null);
			}
		});
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(111);
		tableColumn_5.setText("不统计星期");
		
		Menu menu = new Menu(table);
		
		
		
		table.setMenu(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TimeRange timeRange = new TimeRange();
				timeRange.setName("新建分时统计");
				timeRangeList.add(timeRange);
//				tableViewer.setInput(timeRangeList);
				table.setSelection(timeRangeList.size());
				tableViewer.refresh();
				
			}
		});
		menuItem.setImage(ResourceManager.getPluginImage("htconfig-slyt", "icons/add.png"));
		menuItem.setText("添加");
		
		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iss = (IStructuredSelection) tableViewer.getSelection();
				Object first = iss.getFirstElement();
				if(first == null) {
					MessageDialog.openError(getShell(), "错误", "未选择要删除的分时统计");
					return;
				}
				TimeRange timeRange = (TimeRange)first;
				timeRangeList.remove(timeRange);
				
				tableViewer.refresh();
			}
		});
		menuItem_1.setImage(ResourceManager.getPluginImage("htconfig", "icons/remove.png"));
		menuItem_1.setText("删除");
		tableViewer.setLabelProvider(new TableLabelProvider());
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(timeRangeList);
		
		text = new Text(group, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		text.setText("说明：“统计日”与“不统计日”的格式如（05-01,10-20），“统计日”优先级高于“不统计日”，“统计日”为空则全统计；“统计星期”和“不统计星期”的格式如（6,7）。");
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.heightHint = 46;
		gd_text.widthHint = 592;
		text.setLayoutData(gd_text);
		text.setBounds(0, 0, 70, 18);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3,
				1));

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if ("".equals(text_name.getText().trim())) {
					MessageDialog.openError(getShell(), "错误", "名字不能为空！");
					return;
				}
				if (indexNodeModel.getName() == null) {// 新建
					indexNodeModel.setName(text_name.getText().trim());
					indexNodeModel.setType(type_text.getText().trim());


					IndexNodeModelDaoImpl indexNodeModelDao = new IndexNodeModelDaoImpl();

					indexNodeModelDao.addElement(indexNodeModel);

					MainUI.treeViewer.add(indexNodeModel.getParentObject(),
							indexNodeModel); // 更新树
					MainUI.treeViewer.setExpandedState(
							indexNodeModel.getParentObject(), true); // 展开新节点

				} else {// 编辑
					String oldName = indexNodeModel.getName();
					indexNodeModel.setName(text_name.getText());
					indexNodeModel.setType(type_text.getText().trim());


					IndexNodeModelDaoImpl indexNodeModeDao = new IndexNodeModelDaoImpl();
					indexNodeModeDao.updateIndexNodeModel(indexNodeModel,
							oldName);

					// 更新树
					MainUI.treeViewer.update(indexNodeModel, null);
				}
				
				timeRangeDao.saveAll(indexNodeModel, timeRangeList);

				((OperatingComposite) parent).setTop("标签已保存！");
			}
		});
		btnNewButton.setBounds(45, 39, 72, 22);
		btnNewButton.setText("保  存");

		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				((OperatingComposite) parent).setTop("操作已取消！");
			}
		});
		btnNewButton_1.setBounds(173, 39, 72, 22);
		btnNewButton_1.setText("取  消");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
