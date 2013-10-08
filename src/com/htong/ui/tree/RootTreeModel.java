package com.htong.ui.tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import com.htong.util.FileConstants;

/**
 * 树根节点模型
 * 
 * @author 赵磊
 * 
 */
public enum RootTreeModel {
	instanse;
//	public static final String[] rootTree = { "模板配置", "标签索引配置", "标签类型配置","规约配置", "通道配置", "窗口配置" };

//	public static final String[] rootTree = {"标签索引配置", "标签类型配置", "通道配置", "其他配置"};
//	
//	public static String[] getRoottree() {
//		return rootTree;
//	}
	public String labelIndex = "标签索引配置";
	public String labelType = "标签类型配置";
	public String channelConfig = "通道配置";
	public String otherConfig = "其他配置";
	
	private RootTreeModel() {
		loadConfig();
	}
	
	public  String[] getRoottree() {
		String ss[] = new String[4];
		ss[0] = labelIndex;
		ss[1] = labelType;
		ss[2] = channelConfig;
		ss[3] = otherConfig;
		
	return ss;
}
	
	private void loadConfig() {
		Properties properties = new Properties();
		try {
			File path = new File(FileConstants.CONFIG_PATH);
			if(!path.exists()) {
				path.mkdir();
			}
			File file = new File(FileConstants.LABEL_NAME_PATH);
			if(!file.exists()) {
				file.createNewFile();
				saveConfig();
				return;
			}
			
			//properties.load(new FileInputStream(FileConstants.LABEL_NAME_PATH));
//			new InputStreamReader(new FileInputStream("s")).close();
			properties.load(new InputStreamReader(new FileInputStream(FileConstants.LABEL_NAME_PATH), "UTF-8"));
			this.labelIndex = properties.getProperty("labelIndex","标签索引配置");
			this.labelType = properties.getProperty("labelType","标签类型配置");
			this.channelConfig = properties.getProperty("channelConfig","通道配置");
			this.otherConfig = properties.getProperty("otherConfig","其他配置");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveConfig() {
		Properties properties = new Properties();
		try {
			properties.setProperty("labelIndex", this.labelIndex);
			properties.setProperty("labelType", this.labelType);
			properties.setProperty("channelConfig", this.channelConfig);
			properties.setProperty("otherConfig", this.otherConfig);

			properties.store(
					new OutputStreamWriter(new FileOutputStream(FileConstants.LABEL_NAME_PATH),"UTF-8"), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
