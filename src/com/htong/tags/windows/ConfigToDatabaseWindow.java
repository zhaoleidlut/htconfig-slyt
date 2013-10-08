package com.htong.tags.windows;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.htong.dao.sql.TMeter2;
import com.htong.dao.sql.TMeter2Dao;
import com.htong.tags.dao.DeviceModelDao;
import com.htong.tags.daoImpl.ChannelModelDaoImpl;
import com.htong.tags.daoImpl.DeviceModelDaoImpl;
import com.htong.tags.daoImpl.IOInfoMedelDaoImpl;
import com.htong.tags.daoImpl.IndexNodeModelDaoImpl;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;
import com.htong.util.LayoutUtil;
import com.htong.util.SQLServerConstants;
import org.eclipse.swt.widgets.ProgressBar;

public class ConfigToDatabaseWindow {
	
	private static final Logger log = Logger.getLogger(ConfigToDatabaseWindow.class);

	protected Shell shell;
	private Text username;
	private Text password;
	private Text ip;
	private Text database;
	private ProgressBar progressBar;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ConfigToDatabaseWindow window = new ConfigToDatabaseWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		shell.setSize(300, 344);
		shell.setText("将配置信息写入数据库");
		
		LayoutUtil.centerShell(Display.getCurrent(), shell);
		
		Combo combo = new Combo(shell, SWT.NONE);
		combo.setBounds(105, 33, 153, 25);
		combo.setItems(new String[]{"SQL Server"});
		combo.select(0);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(21, 36, 78, 17);
		label.setText("数据库类型：");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(21, 154, 61, 17);
		label_1.setText("用户名：");
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(21, 191, 61, 17);
		label_2.setText("密码：");
		
		username = new Text(shell, SWT.BORDER);
		username.setText("ydgl");
		username.setBounds(105, 151, 153, 23);
		
		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setText("xhydgl");
		password.setBounds(105, 188, 153, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!MessageDialog.openQuestion(shell, "提示", "该操作要持续几分钟甚至更长，是否继续？")) {
					return;
				}
				SQLServerConstants.instance.setIp(ip.getText().trim());
				SQLServerConstants.instance.setDatabase(database.getText().trim());
				SQLServerConstants.instance.setUsername(username.getText().trim());
				SQLServerConstants.instance.setPassword(password.getText().trim());
				
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						TMeter2Dao tmDao;
						try {
							tmDao = new TMeter2Dao();
						} catch (Exception e1) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									MessageDialog.openInformation(shell, "错误", "数据库配置参数错误！");
								}
							});
							e1.printStackTrace();
							return;
						}
						
						ChannelModelDaoImpl channelDao = new ChannelModelDaoImpl();
						DeviceModelDaoImpl deviceDao = new DeviceModelDaoImpl();
						IOInfoMedelDaoImpl ioDao = new IOInfoMedelDaoImpl();
						IndexNodeModelDaoImpl indexNodeDao = new IndexNodeModelDaoImpl();
						
						final List<TMeter2> tmList = new ArrayList<TMeter2>();
						
						final List<ChannelModel> channelList = channelDao.getAllChannelModel();
						if(channelList != null && !channelList.isEmpty()) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									progressBar.setVisible(true);
									progressBar.setMinimum(0);
									progressBar.setMaximum(channelList.size());
								}
							});
							
							for(ChannelModel channel : channelList) {
								List<DeviceModel> deviceList = deviceDao.getDeviceModelsByChannelId(channel.getId());
								if(deviceList != null &&!deviceList.isEmpty()) {
									for(DeviceModel device : deviceList) {
										TMeter2  tm;
										if(device.getElement().attributeValue("mid")==null) {
											tm = new TMeter2();
										} else {
											tm = tmDao.getByMId(Integer.valueOf(device.getElement().attributeValue("mid")));
										}
										if(tm == null) {
											tm = new TMeter2();
										}
										tm.setName(device.getName());
										tm.setCj(device.getManufacturer());	//厂家
										tm.setDeviceId(Integer.valueOf(device.getSlaveId()));
										tm.setDtuId(Integer.valueOf(channel.getDtuId()));
										tm.setType(device.getType());
										tm.setFixdate(device.getInstallTime()==null?null:device.getInstallTime().getTime());
										tm.setLocation(device.getFixPosition());
										
										Element tagModelElement = ioDao.getIOInfoByChannelIdAndDeviceId(channel.getId(), device.getSlaveId());
										if(tagModelElement != null) {
											String mainIndex = GetXpathUtil.getMainIndexByElementPath(tagModelElement);
											String system[] = mainIndex.split("/");
											String line = mainIndex.substring(mainIndex.indexOf("/")+1);
											if(system.length>1) {
												tm.setSystem(mainIndex.split("/")[1]);
											} else {
												tm.setSystem(mainIndex);
											}
											
											tm.setLine(line);
											tm.setLineId(tagModelElement.getParent().getParent().attributeValue("number"));
											
											String secondIndex = tagModelElement.getParent().getParent().attributeValue("normal-index");
											if(secondIndex != null) {
												String subStr[] = secondIndex.split(",");
												for(String str:subStr) {
													if(str.contains("单位")) {
														tm.setDw(str.substring(str.indexOf("/")+1));
														Element myE = indexNodeDao.getLeafElement(str);
														if(myE != null) {
															tm.setDwId(myE.attributeValue("type"));
														}
													} else if(str.contains("油田")) {
														tm.setOilPath(str.substring(str.indexOf("/")+1));
														Element myE = indexNodeDao.getLeafElement(str);
														if(myE != null) {
															tm.setOilId(myE.attributeValue("type"));
														}
													} else if(str.contains("用电")) {
														tm.setYddx(str.substring(str.indexOf("/")+1));
														Element myE = indexNodeDao.getLeafElement(str);
														if(myE != null) {
															tm.setYddxId(myE.attributeValue("type"));
														}
													} else if(str.contains("注水")) {
														tm.setZsyl(str.substring(str.indexOf("/")+1));
														Element myE = indexNodeDao.getLeafElement(str);
														if(myE != null) {
															tm.setZsylId(myE.attributeValue("type"));
														}
													}
												}
											}
										}
										tm.setXsxs(1.07f);
										tmList.add(tm);
										
										TMeter2 newTm = tmDao.saveOrUpdate(tm);	//保存到SQL数据库
										device.getElement().addAttribute("mid",String.valueOf(newTm.getId()));	//更新文件mid
									}
								}
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									if(progressBar.isDisposed()) {
										return;
									}
									progressBar.setSelection(progressBar.getSelection()+1);
								}
							});
							}
							
							XMLDocumentFactory.instance.saveMainDocument();
						}
//						tmDao.deleteAll();
//						tmDao.saveOrUpdate(tmList);
						
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								MessageDialog.openInformation(shell, "信息", "操作成功！共"+tmList.size()+"个电表！");
								progressBar.setVisible(false);
								progressBar.setSelection(0);
							}
						});
					}
				};
				
				new Thread(runnable).start();
			}
		});
		button.setBounds(178, 228, 80, 27);
		button.setText("执行");
		
		Label ip1 = new Label(shell, SWT.NONE);
		ip1.setBounds(21, 72, 78, 17);
		ip1.setText("服务器地址：");
		
		ip = new Text(shell, SWT.BORDER);
		ip.setText("10.67.106.205");
		ip.setBounds(105, 69, 153, 23);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setText("数据库名：");
		label_4.setBounds(21, 112, 61, 17);
		
		database = new Text(shell, SWT.BORDER);
		database.setText("Ydglxt");
		database.setBounds(105, 109, 153, 23);
		
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(10, 268, 248, 17);
		progressBar.setVisible(false);

	}
}
