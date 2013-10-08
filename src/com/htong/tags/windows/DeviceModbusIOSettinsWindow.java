package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.GridViewerColumnSorter;
import org.eclipse.wb.swt.ResourceManager;

import com.htong.help.SettingDeviceIOHelp;
import com.htong.tags.comm.ModbusValueTypeEnum;
import com.htong.tags.daoImpl.IOInfoMedelDaoImpl;
import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.daoImpl.TagModelDaoImpl;
import com.htong.tags.daoImpl.TagTypeModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.IOInfoModel;
import com.htong.ui.GetXpathUtil;
import com.htong.ui.ImportAndExport;
import com.htong.ui.PinyinComparator;
import com.htong.ui.dialog.FloatModifySettingsDialog;
import com.htong.ui.dialog.FloatModifyTypeModel;
import com.htong.ui.dialog.IntegerModifySettingsDialog;
import com.htong.ui.dialog.IntegerModifyTypeModel;
import com.htong.util.LayoutUtil;

public class DeviceModbusIOSettinsWindow extends ApplicationWindow {
	private static class ViewerLabelProvider_1 extends LabelProvider {
		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			TagTypeModel ttm = (TagTypeModel) element;
			return ttm.getName();
		}
	}

	@SuppressWarnings("unused")
	private static final Logger log = Logger
			.getLogger(DeviceModbusIOSettinsWindow.class);

	private class ViewerLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		private Map<String, Boolean> base16Map = new HashMap<String, Boolean>();

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			TagModel tagModel = (TagModel) element;
			Image image = ResourceManager.getPluginImage("htconfig", "icons/unedit.gif");
			switch(columnIndex) {
			case 6://偏移量
				if (!tagModel.getType().startsWith("遥信")) {
					return image;
				}
				break;
			
			case 7://字节长度
				if(tagModel.getType().startsWith("遥控")) {
					return image;
				}
				break;
			case 9://基值
				if(tagModel.getType().startsWith("遥测") || tagModel.getType().startsWith("遥脉") || tagModel.getType().startsWith("遥控")) {
					return null;
				} else {
					return image;
				}
			case 10://系数
				if(tagModel.getType().startsWith("遥测") || tagModel.getType().startsWith("遥脉")) {
					return null;
				} else {
					return image;
				}
				
			default: break;
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			TagModel v = (TagModel) element;

			switch (columnIndex) {
			case 0:// 索引位置

				IndexNodeModel indexNode = indexNodeDao
						.getIndexNodeByXpath(GetXpathUtil
								.getTagModelMainIndexXPath(v.getMainIndex()));
				if (indexNode != null && indexNode.getNumber() != null) {
					return indexNode.getNumber() + ":"
							+ v.getMainIndex().substring(7);
				} else {
					return v.getMainIndex().substring(7);
				}
				
			
			case 1:// 变量名
				return v.getName();
			case 2:// 变量类型
				return v.getType();
			case 3://设备
				return v.getIoInfo().getDeviceId();
			case 4:// 功能码
				if (v.getIoInfo().getFunCode() != null) {
					return v.getIoInfo().getFunCode();
				} else {
					return "";
				}
			case 5:// 寄存器地址
				return v.getIoInfo().getRegAddress() == null ? "" : v
						.getIoInfo().getRegAddress();
			case 6:// 偏移量
				return v.getIoInfo().getOffset() == null ? "" : v.getIoInfo()
						.getOffset();
			case 7:// 字节长度
				return v.getIoInfo().getByteLen() == null ? "" : v.getIoInfo()
						.getByteLen();
			case 8:// 值类型
				if (v.getIoInfo().getValueType() == null) {
					return "";
				} else {
					return ModbusValueTypeEnum.getByLabel(
							v.getIoInfo().getValueType()).getValue();
				}
			case 9:// 基数
				return v.getIoInfo().getBaseValue() == null ? "" : v
						.getIoInfo().getBaseValue();
			case 10:// 系数
				return v.getIoInfo().getCoef() == null ? "" : v.getIoInfo()
						.getCoef();
			case 11:// 优先级
				return v.getIoInfo().getPriority() == null ? "" : v.getIoInfo()
						.getPriority();

			default:
				break;
			}

			return "";
		}

	}

	private List<TagModel> allVariables; // 所有变量的列表

	private List<TagTypeModel> tagTypeList; // 变量类型列表

	private TagTypeModelDaoImpl tagTypeDao;
	private TagModelDaoImpl tagDao;
	private IOInfoMedelDaoImpl ioInfoDao;
	private IndexNodeModelDaoImpl indexNodeDao;

	private String[] modbusValueTypeStrs; // modbus值类型

	private DeviceModel device;

	private Button save; // 保存
	private Button over; // 结束
	private Button help; // 帮助按钮
	private GridTableViewer gridTableViewer;
	private Grid grid;
	
	private boolean hasEdit = false;	//已编辑标志
	
	private ChannelModel channel;

	private ViewerLabelProvider gridViewerLabelProvider;
	private Combo combo;

	public DeviceModbusIOSettinsWindow(Shell parentShell, DeviceModel device, ChannelModel channel) {
		super(parentShell);
		this.channel = channel;

		this.device = device;
		tagDao = new TagModelDaoImpl();
		ioInfoDao = new IOInfoMedelDaoImpl();
		tagTypeDao = new TagTypeModelDaoImpl();
		indexNodeDao = new IndexNodeModelDaoImpl();

		int i = ModbusValueTypeEnum.values().length;
		modbusValueTypeStrs = new String[i];
		for (int j = 0; j < i; j++) {
			modbusValueTypeStrs[j] = ModbusValueTypeEnum.values()[j].getValue();// 小写
		}
		
		List<TagModel> tagModelList=null;
		
		if(channel == null) {
			ChannelModel channelModel = (ChannelModel) device.getParentObject();

			tagModelList = tagDao.getTagByChannelIDAndDeviceSlaveId(
					channelModel.getId(), device.getSlaveId());
		} else {
			tagModelList = tagDao.getTagByChannelID(channel.getId());
		}

		

		allVariables = new ArrayList<TagModel>();
		for (TagModel tag : tagModelList) {
			if (tag.getType().startsWith("内存")) {
				continue;
			}
			allVariables.add(tag);
		}

		log.debug("设定设备IO信息的变量个数：" + allVariables.size());

		gridViewerLabelProvider = new ViewerLabelProvider();

		// 初始化tagio
		for (TagModel tagModel : allVariables) {
			IOInfoModel io = tagModel.getIoInfo();

			if (io == null) {
				io = new IOInfoModel();
			}
			tagModel.setIoInfo(io);
		}

		log.debug("设定变量IO信息的变量个数：" + allVariables.size());

		gridViewerLabelProvider = new ViewerLabelProvider();

		tagTypeList = new ArrayList<TagTypeModel>();
		List<TagTypeModel> tList = tagTypeDao.getAllTagTypeModel();
		for (TagTypeModel tag : tList) {
			if (tag.getName().startsWith("内存")) {
				continue;
			}
			tagTypeList.add(tag);
		}

		TagTypeModel allType = new TagTypeModel();
		allType.setName("所有类型");
		tagTypeList.add(0, allType);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("设定设备IO信息");
		shell.setSize(new Point(1100, 700));
		// 窗口居中
		LayoutUtil.centerShell(Display.getCurrent(), shell);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		label.setText("选择变量类型：");

		ComboViewer comboViewer = new ComboViewer(composite, SWT.NONE);
		comboViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						gridTableViewer.refresh();
					}
				});
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));
		comboViewer.setLabelProvider(new ViewerLabelProvider_1());
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(tagTypeList);
		combo.select(0);
		
		Button btne = new Button(composite, SWT.NONE);
		btne.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(device != null) {
					ImportAndExport exportDevice = new ImportAndExport();
					exportDevice.exportDevice(device);
				} else {
					MessageDialog.openInformation(getShell(), "提示", "存在多个设备，请到具体设备中进行导出IO信息！");
					return;
				}
				
			}
		});
		GridData gd_btne = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btne.widthHint = 70;
		btne.setLayoutData(gd_btne);
		btne.setText("导出IO(&E)");

		gridTableViewer = new GridTableViewer(composite, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL);

		grid = gridTableViewer.getGrid();
		grid.setRowHeaderVisible(true);
		grid.setTreeLinesVisible(false);
		grid.setCellSelectionEnabled(true);
		grid.setHeaderVisible(true);
		grid.setRowsResizeable(true);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5));
		
		

		GridViewerColumn gridViewerColumn_9 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_9 = gridViewerColumn_9.getColumn();
		gridColumn_9.setMoveable(true);
		gridColumn_9.setWidth(170);
		gridColumn_9.setText("索引编号：标签索引");
		gridColumn_9.setCellSelectionEnabled(false);
		gridViewerColumn_9
				.setEditingSupport(new EditingSupport(gridTableViewer) {
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

						TagModel v = (TagModel) element;
						if (v.getMainIndex() == null) {
							return "-1";
						}
						IndexNodeModel indexNode = indexNodeDao
								.getIndexNodeByXpath(GetXpathUtil
										.getTagModelMainIndexXPath(v
												.getMainIndex()));
						if (indexNode != null && indexNode.getNumber() != null) {
							return indexNode.getNumber();
						} else {
							return "-1";
						}

					}

					protected void setValue(Object element, Object value) {

						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int indexnodeNuber = 0;
						try {
							indexnodeNuber = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的索引号！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(indexnodeNuber);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								indexnodeNuber = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										String mainIndex = tagDao
												.getTagMainIndexByIndexNodeNumber(String
														.valueOf(indexnodeNuber));
										((TagModel)gridTableViewer.getElementAt(start-1)).setMainIndex("".equals(mainIndex) ? ""
												: mainIndex);

										if (im.getType() > 0) {
											indexnodeNuber += im.getInterval();
										} else if (im.getType() < 0) {
											indexnodeNuber -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setFunCode(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}
						}

						for (TagModel tag : tags) {
							String mainIndex = tagDao
									.getTagMainIndexByIndexNodeNumber(String
											.valueOf(indexnodeNuber));
							tag.setMainIndex("".equals(mainIndex) ? ""
									: mainIndex);

							if (im.getType() > 0) {
								indexnodeNuber += im.getInterval();
							} else if (im.getType() < 0) {
								indexnodeNuber -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});
		new GridViewerColumnSorter(gridViewerColumn_9) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;

				return PinyinComparator.INSTANCE.compare(v1.getMainIndex(),
						v2.getMainIndex());
			}
		};

		GridViewerColumn gridViewerColumn = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn = gridViewerColumn.getColumn();
		gridColumn.setMoveable(true);
		gridColumn.setCellSelectionEnabled(false);
		gridColumn.setWidth(80);
		gridColumn.setText("变量名");
		new GridViewerColumnSorter(gridViewerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;

				return PinyinComparator.INSTANCE.compare(v1.getName(),
						v2.getName());
			}
		};

		GridViewerColumn gridViewerColumn_1 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_1 = gridViewerColumn_1.getColumn();
		gridColumn_1.setMoveable(true);
		gridColumn_1.setCellSelectionEnabled(false);
		gridColumn_1.setWidth(80);
		gridColumn_1.setText("变量类型");
		new GridViewerColumnSorter(gridViewerColumn_1) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;

				return PinyinComparator.INSTANCE.compare(v1.getType(),
						v2.getType());
			}
		};
		
		GridViewerColumn gridViewerColumn_11 = new GridViewerColumn(gridTableViewer, SWT.NONE);
		GridColumn gridColumn_11 = gridViewerColumn_11.getColumn();
		gridColumn_11.setWidth(50);
		gridColumn_11.setText("设备");
		new GridViewerColumnSorter(gridViewerColumn_11) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;

				return Integer.parseInt(v1.getIoInfo().getDeviceId())-Integer.parseInt(v2.getIoInfo().getDeviceId());
			}
		};
		

		GridViewerColumn gridViewerColumn_2 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_2 = gridViewerColumn_2.getColumn();
		gridColumn_2.setMoveable(true);
		gridColumn_2.setWidth(65);
		gridColumn_2.setText("功能码");
		gridColumn_2.setCellSelectionEnabled(true);
		
		gridViewerColumn_2
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					private int gridTopIndex = 0;

					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						CellEditor ce = new TextCellEditor(grid);
						return ce;
					}

					protected Object getValue(Object element) {
						gridTopIndex = grid.getTopIndex();

						TagModel var = (TagModel) element;
						if (var.getIoInfo().getFunCode() != null) {
							return var.getIoInfo().getFunCode();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {

						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int funcode = 0;
						try {
							funcode = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的功能码！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(funcode);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								funcode = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setFunCode("".equals(String
												.valueOf(funcode)) ? null : String
												.valueOf(funcode));

										if (im.getType() > 0) {
											funcode += im.getInterval();
										} else if (im.getType() < 0) {
											funcode -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setFunCode(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}
						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setFunCode("".equals(String
									.valueOf(funcode)) ? null : String
									.valueOf(funcode));

							if (im.getType() > 0) {
								funcode += im.getInterval();
							} else if (im.getType() < 0) {
								funcode -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});

		new GridViewerColumnSorter(gridViewerColumn_2) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				if (v1.getIoInfo().getFunCode() != null
						&& v2.getIoInfo().getFunCode() != null) {
					return Integer.parseInt(v1.getIoInfo().getFunCode())
							- Integer.parseInt(v2.getIoInfo().getFunCode());
				} else {
					return 0;
				}
			}
		};

		GridViewerColumn gridViewerColumn_3 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_3
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						TagModel var = (TagModel) element;
						if (var.getIoInfo().getRegAddress() != null) {
							return var.getIoInfo().getRegAddress();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int regAddr = 0;
						try {
							regAddr = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的寄存器地址！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(regAddr);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								regAddr = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setRegAddress("".equals(String
												.valueOf(regAddr)) ? null : String
												.valueOf(regAddr));

										if (im.getType() > 0) {
											regAddr += im.getInterval();
										} else if (im.getType() < 0) {
											regAddr -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									//grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setRegAddress(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								// grid.setTopIndex(gridTopIndex);
								return;
							}
						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setRegAddress("".equals(String
									.valueOf(regAddr)) ? null : String
									.valueOf(regAddr));

							if (im.getType() > 0) {
								regAddr += im.getInterval();
							} else if (im.getType() < 0) {
								regAddr -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						// grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_3 = gridViewerColumn_3.getColumn();
		gridColumn_3.setMoveable(true);
		gridColumn_3.setWidth(85);
		gridColumn_3.setText("寄存器地址");
		new GridViewerColumnSorter(gridViewerColumn_3) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				TagModel v1 = (TagModel) e1;
				TagModel v2 = (TagModel) e2;
				if(v1.getIoInfo().getRegAddress() == null || "".equals(v1.getIoInfo().getRegAddress())) {
					return 1;
				}
				if(v2.getIoInfo().getRegAddress() == null || "".equals(v2.getIoInfo().getRegAddress())) {
					return -1;
				}
				if (v1.getIoInfo().getRegAddress() != null
						&& v2.getIoInfo().getRegAddress() != null && !"".equals(v1.getIoInfo().getRegAddress()) && !"".equals(v2.getIoInfo().getRegAddress())) {
					return Integer.parseInt(v1.getIoInfo().getRegAddress())
							- Integer.parseInt(v2.getIoInfo().getRegAddress());
				} else {
					return 0;
				}
			}

		};

		GridViewerColumn gridViewerColumn_4 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_4
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					private int gridTopIndex = 0;

					protected boolean canEdit(Object element) {
						TagModel tagModel = (TagModel) element;
						if (tagModel.getType().startsWith("遥信")) {
							return true;
						} else {
							return false;
						}
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						this.gridTopIndex = grid.getTopIndex();

						TagModel var = (TagModel) element;
						if (var.getIoInfo().getOffset() != null) {
							return var.getIoInfo().getOffset();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int offset = 0;
						try {
							offset = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的偏移量！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(offset);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								offset = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setOffset("".equals(String
												.valueOf(offset)) ? null : String
												.valueOf(offset));

										if (im.getType() > 0) {
											offset += im.getInterval();
										} else if (im.getType() < 0) {
											offset -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setOffset(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}
						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setOffset("".equals(String
									.valueOf(offset)) ? null : String
									.valueOf(offset));

							if (im.getType() > 0) {
								offset += im.getInterval();
							} else if (im.getType() < 0) {
								offset -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_4 = gridViewerColumn_4.getColumn();

		gridColumn_4.setMoveable(true);
		gridColumn_4.setWidth(65);
		gridColumn_4.setText("偏移量");

		GridViewerColumn gridViewerColumn_5 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_5
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					private int gridTopIndex = 0;

					protected boolean canEdit(Object element) {
						TagModel tagModel = (TagModel)element;
						if(tagModel.getType().startsWith("遥控")) {
							return false;
						} else {
							return true;
						}
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						gridTopIndex = grid.getTopIndex();

						TagModel var = (TagModel) element;
						if (var.getIoInfo().getByteLen() != null) {
							return var.getIoInfo().getByteLen();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int byteLen = 0;
						try {
							byteLen = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的字节长度！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(byteLen);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								byteLen = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setByteLen("".equals(String
												.valueOf(byteLen)) ? null : String
												.valueOf(byteLen));

										if (im.getType() > 0) {
											byteLen += im.getInterval();
										} else if (im.getType() < 0) {
											byteLen -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setByteLen(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}
						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setByteLen("".equals(String
									.valueOf(byteLen)) ? null : String
									.valueOf(byteLen));

							if (im.getType() > 0) {
								byteLen += im.getInterval();
							} else if (im.getType() < 0) {
								byteLen -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_5 = gridViewerColumn_5.getColumn();
		gridColumn_5.setWidth(70);
		gridColumn_5.setMoveable(true);
		gridColumn_5.setText("字节长度");

		GridViewerColumn gridViewerColumn_6 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_6
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					protected boolean canEdit(Object element) {
						return true;
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						TagModel var = (TagModel) element;
						IOInfoModel ioInfo = var.getIoInfo();
						if (ioInfo.getValueType() == null
								|| "".equals(ioInfo.getValueType())) {
							return "-1";
						}
						ModbusValueTypeEnum type = ModbusValueTypeEnum
								.getByLabel(ioInfo.getValueType());
						String s = type.getValue();
						for (int i = 0; i < modbusValueTypeStrs.length; i++) {
							if (s.equals(modbusValueTypeStrs[i])) {
								return String.valueOf(i);
							}
						}
						return "-1";
					}

					protected void setValue(Object element, Object value) {
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();
						
						int valueType = 0;
						try {
							valueType = Integer.parseInt((String) value);
							if(valueType<0 || valueType>ModbusValueTypeEnum.values().length) {	//0-14
								MessageDialog.openError(null, "错误",
										"没有该值类型！");
								return;
							}
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog.openError(null, "错误",
										"请输入正确的值类型！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(valueType);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								valueType = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setValueType(ModbusValueTypeEnum.values()[valueType]
												.toString().toLowerCase());

										if (im.getType() > 0) {
											valueType += im.getInterval();
										} else if (im.getType() < 0) {
											valueType -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									//grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setValueType(ModbusValueTypeEnum.values()[valueType]
										.toString().toLowerCase());
								gridTableViewer.update(tag, null);
								return;
							}
						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setValueType(ModbusValueTypeEnum.values()[valueType]
									.toString().toLowerCase());

							if (im.getType() > 0) {
								valueType += im.getInterval();
							} else if (im.getType() < 0) {
								valueType -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						//grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_6 = gridViewerColumn_6.getColumn();
		gridColumn_6.setCellSelectionEnabled(true);
		gridColumn_6.setWidth(120);
		gridColumn_6.setText("值类型");
		gridColumn_6.setMoveable(true);

		GridViewerColumn gridViewerColumn_7 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_7
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					private int gridTopIndex = 0;

					protected boolean canEdit(Object element) {
						TagModel tagModel = (TagModel)element;
						if(tagModel.getType().startsWith("遥测")) {
							return true;
						} else if(tagModel.getType().startsWith("遥控")) {
							return true;
						} else if(tagModel.getType().startsWith("遥脉")) {
							return true;
						} else {
							return false;
						}
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						gridTopIndex = grid.getTopIndex();

						TagModel var = (TagModel) element;
						if (var.getIoInfo().getBaseValue() != null) {
							return var.getIoInfo().getBaseValue();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {
						
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();

						int basicValue = 0;
						try {
							basicValue = Integer.parseInt((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog
										.openError(null, "错误", "请输入正确的基值！");
								return;
							}
						}

						IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							im.setBase(basicValue);
							IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
									getShell(), im, allVariables.size());
							if (dlg.open() == Window.OK) {
								basicValue = im.getBase();
								if(im.isFanwei()) {//按范围
									int start = im.getStart();
									int end = im.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setBaseValue("".equals(String
												.valueOf(basicValue)) ? null : String
												.valueOf(basicValue));

										if (im.getType() > 0) {
											basicValue += im.getInterval();
										} else if (im.getType() < 0) {
											basicValue -= im.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setBaseValue(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}

						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setBaseValue("".equals(String
									.valueOf(basicValue)) ? null : String
									.valueOf(basicValue));

							if (im.getType() > 0) {
								basicValue += im.getInterval();
							} else if (im.getType() < 0) {
								basicValue -= im.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_7 = gridViewerColumn_7.getColumn();
		gridColumn_7.setWidth(50);
		gridColumn_7.setText("基数");
		gridColumn_7.setMoveable(true);

		GridViewerColumn gridViewerColumn_8 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		gridViewerColumn_8
				.setEditingSupport(new EditingSupport(gridTableViewer) {
					private int gridTopIndex = 0;

					protected boolean canEdit(Object element) {
						TagModel tagModel = (TagModel)element;
						if(tagModel.getType().startsWith("遥测")||tagModel.getType().startsWith("遥脉")) {
							return true;
						} else {
							return false;
						}
					}

					protected CellEditor getCellEditor(Object element) {
						hasEdit = true;
						return new TextCellEditor(grid);
					}

					protected Object getValue(Object element) {
						gridTopIndex = grid.getTopIndex();

						TagModel var = (TagModel) element;
						if (var.getIoInfo().getCoef() != null) {
							return var.getIoInfo().getCoef();
						}
						return "";
					}

					protected void setValue(Object element, Object value) {
						CellSelection cellSel = (CellSelection) gridTableViewer
								.getSelection();
						@SuppressWarnings("unchecked")
						List<TagModel> tags = cellSel.toList();
						
						float coef = 0;
						try {
							coef = Float.parseFloat((String) value);
						} catch (NumberFormatException e) {
							if (!((String) value).isEmpty()) {
								MessageDialog
										.openError(null, "错误", "请输入正确的系数！");
								return;
							}
						}

						FloatModifyTypeModel fm = new FloatModifyTypeModel();// 值修改方式模型
						if (tags.size() > 1) {
							fm.setBase(coef);
							FloatModifySettingsDialog dlg = new FloatModifySettingsDialog(
									getShell(), fm, allVariables.size());
							if (dlg.open() == Window.OK) {
								coef = fm.getBase();
								
								if(fm.isFanwei()) {//按范围
									int start = fm.getStart();
									int end = fm.getEnd();
									for(;start<=end;start ++) {
										IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
										ioInfoModel.setCoef("".equals(String
												.valueOf(coef)) ? null : String
												.valueOf(coef));

										if (fm.getType() > 0) {
											coef += fm.getInterval();
										} else if (fm.getType() < 0) {
											coef -= fm.getInterval();
										}
									}
									
									gridTableViewer.update(allVariables.toArray(), null);
									grid.setTopIndex(gridTopIndex);
									return;
								}
								
							} else {
								gridTableViewer.setSelection(null);
								return;
							}
						} else {// 选中一个单元格
							TagModel tag = (TagModel) element;
							String v = (String) value;
							if (tag.getIoInfo() != null) {
								tag.getIoInfo().setCoef(
										"".equals(v) ? null : v);
								gridTableViewer.update(tag, null);
								grid.setTopIndex(gridTopIndex);
								return;
							}

						}

						for (TagModel tag : tags) {
							IOInfoModel ioInfoModel = tag.getIoInfo();
							ioInfoModel.setCoef("".equals(String
									.valueOf(coef)) ? null : String
									.valueOf(coef));

							if (fm.getType() > 0) {
								coef += fm.getInterval();
							} else if (fm.getType() < 0) {
								coef -= fm.getInterval();
							}
						}

						gridTableViewer.update(tags.toArray(), null);
						grid.setTopIndex(gridTopIndex);
					}
				});
		GridColumn gridColumn_8 = gridViewerColumn_8.getColumn();
		
		gridColumn_8.setWidth(50);
		gridColumn_8.setText("系数");
		gridColumn_8.setMoveable(true);
		gridColumn_8.setCellSelectionEnabled(true);

		GridViewerColumn gridViewerColumn_10 = new GridViewerColumn(
				gridTableViewer, SWT.NONE);
		GridColumn gridColumn_10 = gridViewerColumn_10.getColumn();
		gridColumn_10.setWidth(50);
		gridColumn_10.setText("优先级");
		gridColumn_10.setMoveable(true);
		gridViewerColumn_10.setEditingSupport(new EditingSupport(
				gridTableViewer) {
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
				if (var.getIoInfo().getPriority() != null) {
					return var.getIoInfo().getPriority();
				}
				return "";
			}

			protected void setValue(Object element, Object value) {
				CellSelection cellSel = (CellSelection) gridTableViewer
						.getSelection();
				@SuppressWarnings("unchecked")
				List<TagModel> tags = cellSel.toList();

				int priority = 0;
				try {
					priority = Integer.parseInt((String) value);
				} catch (NumberFormatException e) {
					if (!((String) value).isEmpty()) {
						MessageDialog.openError(null, "错误", "请输入正确的优先级！");
						return;
					}
				}

				IntegerModifyTypeModel im = new IntegerModifyTypeModel();// 值修改方式模型
				if (tags.size() > 1) {
					im.setBase(priority);
					IntegerModifySettingsDialog dlg = new IntegerModifySettingsDialog(
							getShell(), im, allVariables.size());
					if (dlg.open() == Window.OK) {
						priority = im.getBase();
						if(im.isFanwei()) {//按范围
							int start = im.getStart();
							int end = im.getEnd();
							for(;start<=end;start ++) {
								IOInfoModel ioInfoModel = ((TagModel)gridTableViewer.getElementAt(start-1)).getIoInfo();
								ioInfoModel.setPriority("".equals(String.valueOf(priority)) ? null
										: String.valueOf(priority));

								if (im.getType() > 0) {
									priority += im.getInterval();
								} else if (im.getType() < 0) {
									priority -= im.getInterval();
								}
							}
							
							gridTableViewer.update(allVariables.toArray(), null);
							grid.setTopIndex(gridTopIndex);
							return;
						}
					} else {
						gridTableViewer.setSelection(null);
						return;
					}
				} else {// 选中一个单元格
					TagModel tag = (TagModel) element;
					String v = (String) value;
					if (tag.getIoInfo() != null) {
						tag.getIoInfo().setPriority("".equals(v) ? null : v);
						gridTableViewer.update(tag, null);
						grid.setTopIndex(gridTopIndex);
						return;
					}
				}

				for (TagModel tag : tags) {
					IOInfoModel ioInfoModel = tag.getIoInfo();
					ioInfoModel.setPriority("".equals(String.valueOf(priority)) ? null
							: String.valueOf(priority));

					if (im.getType() > 0) {
						priority += im.getInterval();
					} else if (im.getType() < 0) {
						priority -= im.getInterval();
					}
				}

				gridTableViewer.update(tags.toArray(), null);
				grid.setTopIndex(gridTopIndex);
			}
		});
		gridColumn_10.setCellSelectionEnabled(true);

		gridTableViewer.setLabelProvider(gridViewerLabelProvider);
		gridTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		gridTableViewer.setInput(this.allVariables);

		ViewerFilter filter = new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				TagModel tag = (TagModel) element;
				
//				log.debug(tag.getType());
				
				if (combo.getText().equals("所有类型")) {// all
					return true;
				} else {
					return tag.getType().startsWith(combo.getText());
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
					MessageDialog.openInformation(getShell(), "提示", "设备IO信息未做任何修改！");
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
				new SettingDeviceIOHelp(Display.getDefault().getActiveShell().getShell()).open();
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
		new Label(composite, SWT.NONE);

		return super.createContents(parent);
	}
	
	private void save() {
		if (allVariables != null && !allVariables.isEmpty()) {
			//验证寄存器地址是否重复
			if(channel == null) {//设备
				int len = allVariables.size();
				for(int i = 0;i<len;i++) {
					IOInfoModel io = allVariables.get(i).getIoInfo();
					log.debug(allVariables.get(i).getName());
					if(io.getRegAddress() == null || io.getFunCode()==null || "".equals(io.getRegAddress()) || (allVariables.get(i).getType().startsWith("遥信")&&(io.getFunCode().equals("3")||io.getFunCode().equals("4")))) {
						continue;
					}
					for(int j = i+1;j<len;j++) {
						IOInfoModel io1 = allVariables.get(j).getIoInfo();
						if(io1.getRegAddress() == null || "".equals(io1.getRegAddress())) {
							continue;
						}
						if(io.getRegAddress().equals(io1.getRegAddress()) && allVariables.get(i).getType().substring(0, 2).equals(allVariables.get(j).getType().substring(0, 2))) {
							MessageDialog.openWarning(getShell(), "提示", "【" +allVariables.get(i).getMainIndex().substring(7) +"/" + allVariables.get(i).getName() + "】与【"+allVariables.get(j).getMainIndex().substring(7) +"/"+allVariables.get(j).getName()+"】的寄存器地址【"+io.getRegAddress()+"】重复！");
							return;
						}
					}
				}
			} else {//通道统一配置
				
			}
			
			
			
			for (TagModel tagModel : allVariables) {
				IOInfoModel io = tagModel.getIoInfo();
				
				ioInfoDao.update(tagModel);
			}
		}
		XMLDocumentFactory.instance.saveMainDocument();
		MessageDialog.openInformation(getShell(), "信息", "设备IO信息保存成功！");
	}
	
	@Override
	public boolean close() {
		if(hasEdit) {
			MessageDialog dialog = new MessageDialog(getShell(), "提示", null, "设备的IO信息已更改，是否保存？",
					MessageDialog.QUESTION, new String[] {"是", "否", "取消" }, 0);
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