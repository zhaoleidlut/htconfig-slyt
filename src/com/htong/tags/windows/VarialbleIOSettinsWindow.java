package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.internal.CellSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.GridViewerColumnSorter;

import com.htong.help.SettingVarIOHelp;
import com.htong.tags.daoImpl.ChannelModelDaoImpl;
import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.daoImpl.IOInfoMedelDaoImpl;
import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.IOInfoModel;
import com.htong.ui.PinyinComparator;
import com.htong.ui.dialog.IntegerModifySettingsDialog;
import com.htong.ui.dialog.IntegerModifyTypeModel;
import com.htong.ui.dialog.StringModifySettingsDialog;
import com.htong.ui.dialog.StringModifyTypeModel;
import com.htong.util.LayoutUtil;

public class VarialbleIOSettinsWindow extends ApplicationWindow {
	private static class ViewerLabelProvider_1 extends LabelProvider {
		public Image getImage(Object element) {
			return null;
		}
		public String getText(Object element) {
			TagTypeModel ttm = (TagTypeModel)element;
			return ttm.getName();
		}
	}


	private static final Logger log = Logger
			.getLogger(VarialbleIOSettinsWindow.class);

	private class ViewerLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			TagModel v = (TagModel) element;
			switch (columnIndex) {
			case 0:// 变量名
				String[] mainIndex = v.getMainIndex().split("/");
				return  mainIndex[mainIndex.length-1] + ":" + v.getName();
			case 1:// 变量类型
				return v.getType();
			case 2://采集通道：采集通道号
				if(v.getIoInfo() == null) {
					return "";
				}
				if(v.getIoInfo().getChannelId() != null) {
					ChannelModel channelModel = channelDao.getChannelModelById(v.getIoInfo().getChannelId(), null);
					log.debug(v.getName() + "查询通道");
					if(channelModel!=null) {//存在采集通道
						return channelModel.getName()+":"+v.getIoInfo().getChannelId();
					} else {
						ioInfoDao.deleteAllIO(v);
						v.setIoInfo(null);
						
						
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								gridTableViewer.refresh();
							}
						});
						return "";
					}
					
				} else
					return "";
			case 3://设备：设备地址
				if(v.getIoInfo() == null) {
					return "";
				}
				if(v.getIoInfo().getDeviceId() != null) {
					if(v.getIoInfo().getChannelId() == null) {
						return v.getIoInfo().getDeviceId();
					} else {
						
						DeviceModel device = deviceDao.getDeviceByChannelIdAndDeviceSlaveId(v.getIoInfo().getChannelId(), v.getIoInfo().getDeviceId());
						if(device != null) {
							return device.getName()+":"+device.getSlaveId();
						} else {
							v.getIoInfo().setDeviceId(null);
							return "";
						}
					}
				}			
				return "";
			case 4://转发通道
				return "";
			default:
				break;
			}
			return "";
		}
	}

	private List<TagModel> allVariableList; // 所有变量的列表
	
	private List<TagTypeModel> tagTypeList;	//变量类型列表

	private IndexNodeModel indexNodeModel;
	private ChannelModelDaoImpl channelDao;
	private DeviceModelDaoImpl deviceDao;
	private IOInfoMedelDaoImpl ioInfoDao;
	private TagTypeModelDaoImpl tagTypeDao;

	private Button save; // 保存
	private Button over; // 结束
	private Button help; // 帮助按钮
	private GridTableViewer gridTableViewer;
	private Grid grid;

	private ViewerLabelProvider gridViewerLabelProvider;
	private GridColumn gridColumn;
	private GridViewerColumn gridViewerColumn;
	private GridColumn gridColumn_1;
	private GridViewerColumn gridViewerColumn_1;
	private GridColumn gridColumn_2;
	private GridViewerColumn gridViewerColumn_2;
	private GridColumn gridColumn_3;
	private GridViewerColumn gridViewerColumn_3;
	private Combo combo;
	private ComboViewer comboViewer;
	
	private boolean hasEdit = false;	//编辑标志

	public VarialbleIOSettinsWindow(Shell parentShell,
			IndexNodeModel indexNodeModel, boolean isAllVar) {
		super(parentShell);

		this.indexNodeModel = indexNodeModel;
		channelDao = new ChannelModelDaoImpl();
		deviceDao = new DeviceModelDaoImpl();
		ioInfoDao = new IOInfoMedelDaoImpl();
		tagTypeDao = new TagTypeModelDaoImpl();

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
		String currentPosition = curentPostionStr.toString();

		TagModelDaoImpl tagModelDao = new TagModelDaoImpl();
		
		if(!isAllVar) {
			allVariableList = tagModelDao.getTagsByMainIndex(currentPosition);
		} else {
			allVariableList = tagModelDao.getTagsIncludeSubByMainIndex(currentPosition);
		}
		

		if(allVariableList != null) {
			log.debug("设定变量IO信息的变量个数：" + allVariableList.size());

			// 初始化tagio
//			for (TagModel tagModel : allVariableList) {
//				if(tagModel.getIoInfo() == null) {
//					IOInfoModel io = new IOInfoModel();
//					tagModel.setIoInfo(io);
//				}
//			}
		}
		
		gridViewerLabelProvider = new ViewerLabelProvider();
		
		tagTypeList = new ArrayList<TagTypeModel>();
		List<TagTypeModel> tList = tagTypeDao.getAllTagTypeModel();
		for(TagTypeModel tag:tList) {
			if(tag.getName().startsWith("内存")){
				continue;
			}
			tagTypeList.add(tag);
		}
		TagTypeModel allType = new TagTypeModel();
		allType.setName("所有类型");
		tagTypeList.add(0,allType);
		log.debug("变量类型个数："+tagTypeList.size());
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("设定变量IO信息");
		shell.setSize(new Point(800, 600));
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), shell);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("变量类型");
		
		comboViewer = new ComboViewer(composite, SWT.NONE);
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
					
				gridTableViewer.refresh();
			}
		});
		combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 100;
		combo.setLayoutData(gd_combo);
		comboViewer.setLabelProvider(new ViewerLabelProvider_1());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(tagTypeList);
		combo.select(0);

		Button btns = new Button(composite, SWT.NONE);
		GridData gd_btns = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_btns.widthHint = 70;
		btns.setLayoutData(gd_btns);
		btns.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openWarning(getShell(), "提示", "敬请期待");
			}
		});
		btns.setText("导 出(&O)");

		gridTableViewer = new GridTableViewer(composite, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		gridTableViewer.setAutoPreferredHeight(true);
		grid = gridTableViewer.getGrid();
		grid.setColumnScrolling(true);
		grid.setTreeLinesVisible(false);
		grid.setCellSelectionEnabled(true);
		grid.setHeaderVisible(true);
		grid.setRowHeaderVisible(true);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4));

		GridViewerColumn gridViewerColumn_9 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_9 = gridViewerColumn_9.getColumn();
		gridColumn_9.setWidth(130);
		gridColumn_9.setCellSelectionEnabled(false);
		gridColumn_9.setText("索引编号：变量名");
		new GridViewerColumnSorter(gridViewerColumn_9) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				return PinyinComparator.INSTANCE.compare(v1.getName(),
						v2.getName());
			}
		};

		gridViewerColumn = new GridViewerColumn(gridTableViewer, SWT.NONE);
		gridColumn = gridViewerColumn.getColumn();
		gridColumn.setWidth(100);
		gridColumn.setText("变量类型");
		gridColumn.setCellSelectionEnabled(false);
		new GridViewerColumnSorter(gridViewerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				return PinyinComparator.INSTANCE.compare(v1.getType(),
						v2.getType());
			}
		};

		gridViewerColumn_1 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		gridColumn_1 = gridViewerColumn_1.getColumn();
		gridColumn_1.setWidth(110);
		gridColumn_1.setText("采集通道：序号");
		gridColumn_1.setCellSelectionEnabled(true);
		gridViewerColumn_1.setEditingSupport(new EditingSupport(gridTableViewer) {
			private int gridTopIndex = 0;
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				hasEdit = true;
				return new TextCellEditor(grid);
			}

			protected Object getValue(Object element) {
				gridTopIndex = grid.getTopIndex();
				
				TagModel var = (TagModel) element;
				if(var.getIoInfo() == null) {
					return "";
				}
				if (var.getIoInfo().getChannelId() != null) {
					return var.getIoInfo().getChannelId();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagModel> tags = cellSel.toList();

//				int channelIndex = 0;
//				try {
//					channelIndex = Integer.parseInt((String) value);
//				} catch (NumberFormatException e) {
//					if (!((String) value).isEmpty()) {
//						MessageDialog.openError(null, "错误",
//								"请输入正确的采集通道序号！");
//						return;
//					}
//				}
				String channelIndex = (String)value;
				
				StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
				if (tags.size() > 1) {
					im.setBase(channelIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							getShell(), im, allVariableList.size());
					
					if (dlg.open() == Window.OK) {
						channelIndex = im.getBase();
						
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								IOInfoModel ioInfoModel=((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
								if(ioInfoModel == null) {
									ioInfoModel = new IOInfoModel();
								}
								
								ioInfoModel.setChannelId("".equals(String.valueOf(channelIndex))?null:String.valueOf(channelIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(channelIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									channelIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									channelIndex = String.valueOf(ci - im.getInterval());
								}
							}
							
							gridTableViewer.update(allVariableList.toArray(), null);
							grid.setTopIndex(gridTopIndex);
							return;
						}
						
						for(TagModel tag : tags) {
							IOInfoModel ioInfoModel=tag.getIoInfo();
							if(ioInfoModel == null) {
								ioInfoModel = new IOInfoModel();
								tag.setIoInfo(ioInfoModel);
							}
							ioInfoModel.setChannelId("".equals(String.valueOf(channelIndex))?null:String.valueOf(channelIndex));
							
							int ci;
							try {
								ci = Integer.parseInt(channelIndex);
							} catch (NumberFormatException e) {
								MessageDialog.openError(getShell(), "错误", "非数字字符串不能计算");
								e.printStackTrace();
								return;
							}
							
							if (im.getType() > 0) {
//								channelIndex += im.getInterval();
								channelIndex = String.valueOf(ci + im.getInterval());
							} else if (im.getType() < 0) {
								channelIndex = String.valueOf(ci - im.getInterval());
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
						
					} else {
						return;
					}
				} else {// 选中一个单元格
					TagModel tag = (TagModel)element;
					String v = (String)value;
					if(tag.getIoInfo() != null) {
						tag.getIoInfo().setChannelId("".equals(v)?null:v);
						gridTableViewer.update(tag, null);
						grid.setTopIndex(gridTopIndex);
						return;
					} else {
						IOInfoModel io = new IOInfoModel();
						tag.setIoInfo(io);
						io.setChannelId("".equals(v)?null:v);
						gridTableViewer.update(tag, null);
						grid.setTopIndex(gridTopIndex);
						return;
					}
				}
				
				
			}
		});
		new GridViewerColumnSorter(gridViewerColumn_1) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				int i1 = v1.getIoInfo()==null?0:Integer.valueOf(v1.getIoInfo().getChannelId()==null?"0":v1.getIoInfo().getChannelId());
				int i2 = v2.getIoInfo()==null?0:Integer.valueOf(v2.getIoInfo().getChannelId()==null?"0":v2.getIoInfo().getChannelId());
				
				return i1-i2;
			}
		};

		gridViewerColumn_2 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setWidth(110);
		gridColumn_2.setText("设备名称：地址");
		gridColumn_2.setCellSelectionEnabled(true);
		gridViewerColumn_2.setEditingSupport(new EditingSupport(gridTableViewer) {
			private int gridTopIndex = 0;
			protected boolean canEdit(Object element) {
				return true;
			}

			protected CellEditor getCellEditor(Object element) {
				hasEdit = true;
				return new TextCellEditor(grid);
			}

			protected Object getValue(Object element) {
				gridTopIndex = grid.getTopIndex();
				
				TagModel var = (TagModel) element;
				if(var.getIoInfo() == null) {
					return "";
				}
				if (var.getIoInfo().getDeviceId() != null) {
					return var.getIoInfo().getDeviceId();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagModel> tags = cellSel.toList();

//				int deviceIndex = 0;
//				try {
//					deviceIndex = Integer.parseInt((String) value);
//				} catch (NumberFormatException e) {
//					if (!((String) value).isEmpty()) {
//						MessageDialog.openError(null, "错误",
//								"请输入正确的设备号！");
//						return;
//					}
//				}
				
				String deviceIndex = (String)value;
				
				StringModifyTypeModel im = new StringModifyTypeModel();// 值修改方式模型
				if (tags.size() > 1) {
					im.setBase(deviceIndex);
					StringModifySettingsDialog dlg = new StringModifySettingsDialog(
							getShell(), im, allVariableList.size());
					if (dlg.open() == Window.OK) {
						deviceIndex = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								IOInfoModel ioInfoModel=((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
								if(ioInfoModel == null) {
									ioInfoModel = new IOInfoModel();
								}
								
								ioInfoModel.setDeviceId("".equals(String.valueOf(deviceIndex))?null:String.valueOf(deviceIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(deviceIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									deviceIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									deviceIndex = String.valueOf(ci - im.getInterval());
								}
							}
							
							gridTableViewer.update(allVariableList.toArray(), null);
							grid.setTopIndex(gridTopIndex);
							
						} else {
							for(TagModel tag : tags) {
								IOInfoModel ioInfoModel=tag.getIoInfo();
								if(ioInfoModel == null) {
									ioInfoModel = new IOInfoModel();
								}
								
								ioInfoModel.setDeviceId("".equals(String.valueOf(deviceIndex))?null:String.valueOf(deviceIndex));
								
								int ci;
								try {
									ci = Integer.parseInt(deviceIndex);
								} catch (NumberFormatException e) {
									MessageDialog.openError(getShell(), "错误", "非数字字符串不能计算");
									e.printStackTrace();
									return;
								}
								
								if (im.getType() > 0) {
									deviceIndex = String.valueOf(ci + im.getInterval());
								} else if (im.getType() < 0) {
									deviceIndex = String.valueOf(ci - im.getInterval());
								}
							}

							gridTableViewer.update(tags.toArray(), null);
							grid.setTopIndex(gridTopIndex);
						}
					} else {
						return;
					}
				} else {// 选中一个单元格
					TagModel tag = (TagModel)element;
					String v = (String)value;
					if(tag.getIoInfo() != null) {
						tag.getIoInfo().setDeviceId("".equals(v)?null:v);
						gridTableViewer.update(tag, null);
						grid.setTopIndex(gridTopIndex);
						return;
					} else {
						gridTableViewer.update(tag, null);
						return;
					}
				}
				
				
				
			}
		});
		new GridViewerColumnSorter(gridViewerColumn_2) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				int i1 = v1.getIoInfo()==null?0:Integer.valueOf(v1.getIoInfo().getDeviceId()==null?"0":v1.getIoInfo().getDeviceId());
				int i2 = v2.getIoInfo()==null?0:Integer.valueOf(v2.getIoInfo().getDeviceId()==null?"0":v2.getIoInfo().getDeviceId());
				
				return i1-i2;
			}
		};

		gridViewerColumn_3 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		gridColumn_3 = gridViewerColumn_3.getColumn();
		gridColumn_3.setWidth(110);
		gridColumn_3.setText("转发通道：序号");
		gridColumn_3.setCellSelectionEnabled(false);
		// new GridViewerColumnSorter(gridViewerColumn_9) {
		// @Override
		// protected int doCompare(Viewer viewer, Object e1, Object e2) {
		// Variable v1 = (Variable) e1;
		// Variable v2 = (Variable) e2;
		// return PinyinComparator.INSTANCE.compare(
		// v1.getCircuit().getCabinet().getFloor().getArea().getAreaName()
		// , v2.getCircuit().getCabinet().getFloor().getArea().getAreaName());
		// }
		// };

		gridTableViewer.setLabelProvider(gridViewerLabelProvider);
		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setInput(this.allVariableList);
		
		ViewerFilter filter = new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				TagModel tag = (TagModel) element;
				
				if (combo.getText().equals("所有类型")) {// all
					return true;
				} else {
					return tag.getType().contains(combo.getText());
				}
			}
		};
		gridTableViewer.setFilters(new ViewerFilter[] { filter });

		this.save = new Button(composite, SWT.PUSH);
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(hasEdit) {
					save();
					hasEdit = false;
				} else {
					MessageDialog.openInformation(getShell(), "提示", "变量IO信息未做任何修改！");
					return;
				}
				 
			}
		});
		
		GridData gd_save = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_save.widthHint = 70;
		save.setLayoutData(gd_save);
		this.save.setText("保 存(&S)");

		this.help = new Button(composite, SWT.PUSH);
		help.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new SettingVarIOHelp(getShell()).open();
			}
		});
		GridData gd_help = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_help.widthHint = 70;
		help.setLayoutData(gd_help);
		this.help.setText("帮 助(&H)");

		this.over = new Button(composite, SWT.PUSH);
		GridData gd_over = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_over.widthHint = 70;
		over.setLayoutData(gd_over);
		over.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		this.over.setText("结 束(&C)");
		new Label(composite, SWT.NONE);

		return super.createContents(parent);
	}
	
	private void save() {
		if (allVariableList != null && !allVariableList.isEmpty()) {
			 for(TagModel tagModel : allVariableList) {
				 //加入默认值
				 IOInfoModel io = tagModel.getIoInfo();
				 if(io != null) {
					 if(tagModel.getType().startsWith("遥测")) {
						 if(io.getFunCode() == null) {
							 io.setFunCode("3");
						 }
						 if(io.getByteLen() == null) {
							 io.setByteLen("2");
						 }
						 if(io.getBaseValue()==null) {
							 io.setBaseValue("0");
						 }
						 if(io.getCoef() == null) {
							 io.setCoef("1");
						 }
						 if(io.getValueType() == null) {
							 io.setValueType("uint16");
						 }
						 
					 } else if(tagModel.getType().startsWith("遥信")) {
						 if(io.getValueType() == null) {
							 io.setValueType("bool");
						 }
						 
					 } else if(tagModel.getType().startsWith("遥脉")) {
						 if(io.getFunCode() == null) {
							 io.setFunCode("3");
						 }
						 if(io.getByteLen() == null) {
							 io.setByteLen("4");
						 }
						 if(io.getValueType() == null) {
							 io.setValueType("uint32");
						 }
						 if(io.getBaseValue()==null) {
							 io.setBaseValue("0");
						 }
						 if(io.getCoef() == null) {
							 io.setCoef("1");
						 }
						 
					 } else if(tagModel.getType().startsWith("遥控")) {
						 if(io.getFunCode() == null) {
							 io.setFunCode("5");
						 }
						 if(io.getValueType() == null) {
							 io.setValueType("bool");
						 }
						 
					 } else if(tagModel.getType().startsWith("遥调")) {
						 
						 
					 }
					 
//					 if(io.getElement() != null) {
//						 ioInfoDao.update(tagModel);
//					 } else {
						 if(tagModel.getIoInfo().getChannelId() == null && tagModel.getIoInfo().getDeviceId() == null) {
							 continue;
						 } else {
							 ioInfoDao.saveOrUpdate(tagModel);
						 }
//					 }
				 } else {
					 ioInfoDao.deleteIO(tagModel);	//删除IO 
				 }
			 }
		 }
		 XMLDocumentFactory.instance.saveMainDocument();
		 MessageDialog.openInformation(getShell(), "信息", "保存成功！");
	}

	@Override
	public boolean close() {
		if(hasEdit) {
			MessageDialog dialog = new MessageDialog(getShell(), "提示", null, "变量IO信息已更改，是否保存？",
					MessageDialog.QUESTION, new String[] {"是", "否", "取消"}, 0);
			int result = dialog.open();
			
			if(result==0) {
				save();
			} else  if(result == 1){
				return super.close();
			} else {
				return true;
			}
		}
		return super.close();
	}
}
