package com.htong.tags.model;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.osgi.baseadaptor.bundlefile.FileBundleEntry;

/**
 * XML文件入口
 * @author 赵磊
 *
 */
public enum XMLDocumentFactory {
	instance;
	
	private static final Logger log = Logger.getLogger(XMLDocumentFactory.class);
	
	private static Document document;
	
	public static final String ROOT_NODE = "root";		//根
	public static final String TAGINDEX_NODE = "tagindex";	//标签索引
	public static final String TAGTYPE_NODE = "tagtype";		//标签类型
	public static final String TAGS_NODE = "tags";	//标签
	public static final String CHANNEL_NODE = "channels";	//采集通道
	public static final String TRANSCHANNEL_NODE = "trans-channels";	//转发通道
	
	private final String fileName = System.getProperty("user.dir") + "/vtags.xml";
	
	/**
	 * 获取主要XML文件
	 * @return
	 * @throws IOException 
	 * @throws DocumentException
	 */
	public Document getMainDocument(){
		if(document == null) {
			File file = new File(fileName);
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
					BufferedWriter bw = new BufferedWriter(write);

					bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n\t");
					bw.write("<root>\r\n\t");
					bw.write("<tagindex>\r\n\t");

					bw.write("<index name=\"变量标签索引\" type=\"main\" id=\"main\"/>\r\n\t");
//					bw.write("</index>\r\n\t");
					
					bw.write("<index name=\"常规分类索引\" type=\"normal\" id=\"normal\">\r\n\t");
					bw.write("<indexnode name=\"建筑分区\" type=\"building\"/>\r\n\t");
//					bw.write("</indexnode>\r\n\t");
					bw.write("</index>\r\n\t");
					
					bw.write("<index name=\"能耗分类分项\" type=\"energy\" id=\"energy\">\r\n\t");
					
					bw.write("<indexnode name=\"电\" type=\"01\">\r\n\t");
					
					bw.write("<indexnode name=\"照明插座用电\" type=\"A\">\r\n\t");
					bw.write("<indexnode name=\"照明与插座\" type=\"1\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"走廊与应急\" type=\"2\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"室外景观照明\" type=\"3\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"空调用电\" type=\"B\">\r\n\t");
					bw.write("<indexnode name=\"冷热站\" type=\"1\">\r\n\t");
					bw.write("<indexnode name=\"冷冻泵\" type=\"A\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"冷却泵\" type=\"B\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"冷机\" type=\"C\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"冷塔\" type=\"D\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"热水循环泵\" type=\"E\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"电锅炉\" type=\"F\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"空调末端\" type=\"2\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"动力用电\" type=\"C\">\r\n\t");
					bw.write("<indexnode name=\"电梯\" type=\"1\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"水泵\" type=\"2\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"通风机\" type=\"3\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"特殊用电\" type=\"D\">\r\n\t");
					bw.write("<indexnode name=\"信息中心\" type=\"1\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"洗衣房\" type=\"2\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"厨房餐厅\" type=\"3\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"游泳池\" type=\"4\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("<indexnode name=\"健身房\" type=\"5\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					
					
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"水\" type=\"02\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"燃气\" type=\"03\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"集中供热量\" type=\"04\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"集中供冷量\" type=\"05\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"其它能源\" type=\"06\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"煤\" type=\"07\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"液化石油气\" type=\"08\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"人工煤气\" type=\"09\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"汽油\" type=\"10\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"煤油\" type=\"11\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"柴油\" type=\"12\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("<indexnode name=\"可再生能源\" type=\"13\">\r\n\t");
					bw.write("</indexnode>\r\n\t");
					
					bw.write("</index>\r\n\t");
					
					
					bw.write("</tagindex>\r\n\t");
					
					
					bw.write("<tagtype>\r\n\t");
					
					bw.write("<type name=\"遥脉\" data-type=\"number\">\r\n\t");
					bw.write("<type name=\"电能\">\r\n\t"); 
					bw.write("<type name=\"有功电度（正向）\"/>\r\n\t"); 
					bw.write("<type name=\"无功电度（正向）\"/>\r\n\t"); 
					bw.write("<type name=\"视在电度\"/>\r\n\t"); 
					bw.write("<type name=\"有功电度（反向）\"/>\r\n\t"); 
					bw.write("<type name=\"无功电度（反向）\"/>\r\n\t"); 
					
					bw.write("<type name=\"正向有功总\"/>\r\n\t"); 
					bw.write("<type name=\"正向有功尖\"/>\r\n\t"); 
					bw.write("<type name=\"正向有功峰\"/>\r\n\t"); 
					bw.write("<type name=\"正向有功平\"/>\r\n\t"); 
					bw.write("<type name=\"正向有功谷\"/>\r\n\t"); 
					bw.write("<type name=\"反向有功总\"/>\r\n\t"); 
					bw.write("<type name=\"反向有功尖\"/>\r\n\t"); 
					bw.write("<type name=\"反向有功峰\"/>\r\n\t"); 
					bw.write("<type name=\"反向有功平\"/>\r\n\t"); 
					bw.write("<type name=\"反向有功谷\"/>\r\n\t"); 
					bw.write("<type name=\"正向无功总\"/>\r\n\t"); 
					bw.write("<type name=\"正向无功尖\"/>\r\n\t"); 
					bw.write("<type name=\"正向无功峰\"/>\r\n\t"); 
					bw.write("<type name=\"正向无功平\"/>\r\n\t"); 
					bw.write("<type name=\"正向无功谷\"/>\r\n\t"); 
					bw.write("<type name=\"反向无功总\"/>\r\n\t"); 
					bw.write("<type name=\"反向无功尖\"/>\r\n\t"); 
					bw.write("<type name=\"反向无功峰\"/>\r\n\t"); 
					bw.write("<type name=\"反向无功平\"/>\r\n\t"); 
					bw.write("<type name=\"反向无功谷\"/>\r\n\t"); 
					bw.write("</type>\r\n\t");
					
					bw.write("<type name=\"其他\"/>\r\n\t"); 
					
					bw.write("</type>\r\n\t");
					
					bw.write("<type name=\"遥测\" data-type=\"number\">\r\n\t");
					bw.write("<type name=\"电压\">\r\n\t");
					bw.write("<type name=\"A相电压\"/>\r\n\t"); 
					bw.write("<type name=\"B相电压\"/>\r\n\t"); 
					bw.write("<type name=\"C相电压\"/>\r\n\t"); 
					bw.write("</type>\r\n\t"); 
					
					bw.write("<type name=\"电流\">\r\n\t"); 
					bw.write("<type name=\"A相电流\"/>\r\n\t"); 
					bw.write("<type name=\"B相电流\"/>\r\n\t"); 
					bw.write("<type name=\"C相电流\"/>\r\n\t"); 
					bw.write("</type>\r\n\t"); 
					
					bw.write("<type name=\"功率\">\r\n\t"); 
					bw.write("<type name=\"总有功功率\"/>\r\n\t"); 
					bw.write("<type name=\"A相有功功率\"/>\r\n\t"); 
					bw.write("<type name=\"B相有功功率\"/>\r\n\t"); 
					bw.write("<type name=\"C相有功功率\"/>\r\n\t");
					bw.write("<type name=\"总无功功率\"/>\r\n\t"); 
					bw.write("<type name=\"A相无功功率\"/>\r\n\t"); 
					bw.write("<type name=\"B相无功功率\"/>\r\n\t"); 
					bw.write("<type name=\"C相无功功率\"/>\r\n\t");
					bw.write("<type name=\"总功率因数\"/>\r\n\t"); 
					bw.write("<type name=\"A相功率因数\"/>\r\n\t"); 
					bw.write("<type name=\"B相功率因数\"/>\r\n\t"); 
					bw.write("<type name=\"C相功率因数\"/>\r\n\t");
					bw.write("</type>\r\n\t"); 
					
					
					bw.write("<type name=\"空调\">\r\n\t"); 
					bw.write("<type name=\"冷冻水\" data-type=\"number\">\r\n\t"); 
					bw.write("<type name=\"进水温度\" data-type=\"number\"/>\r\n\t"); 
					bw.write("<type name=\"回水温度\" data-type=\"number\"/>\r\n\t"); 
					bw.write("</type>\r\n\t"); 
					bw.write("<type name=\"冷却水\" data-type=\"number\">\r\n\t"); 
					bw.write("<type name=\"进水温度\" data-type=\"number\"/>\r\n\t"); 
					bw.write("<type name=\"回水温度\" data-type=\"number\"/>\r\n\t"); 
					bw.write("</type>\r\n\t"); 
					bw.write("</type>\r\n\t"); 
					
					
					
					bw.write("<type name=\"其他\"/>\r\n\t"); 
					bw.write("</type>\r\n\t");
					
					
					bw.write("<type name=\"遥信\" data-type=\"bool\">\r\n\t");
					bw.write("<type name=\"断路器\"/>\r\n\t"); 
					bw.write("<type name=\"隔离刀闸\"/>\r\n\t"); 
					bw.write("<type name=\"故障\"/>\r\n\t"); 
					bw.write("<type name=\"报警\"/>\r\n\t"); 
					bw.write("<type name=\"预警\"/>\r\n\t"); 
					bw.write("<type name=\"子站状态\"/>\r\n\t"); 
					bw.write("<type name=\"其他\"/>\r\n\t");  
					bw.write("</type>\r\n\t");
					
					bw.write("<type name=\"遥控\" data-type=\"bool\"/>\r\n\t");
					bw.write("<type name=\"遥调\" data-type=\"number\"/>\r\n\t");
					
					bw.write("<type name=\"内存变量\">\r\n\t");
					bw.write("<type name=\"数值型\" data-type=\"number\"/>\r\n\t"); 
					bw.write("<type name=\"字符型\" data-type=\"string\"/>\r\n\t"); 
					bw.write("<type name=\"布尔型\" data-type=\"bool\"/>\r\n\t"); 
					bw.write("</type>\r\n\t");
					
					bw.write("<type name=\"内存常量\">\r\n\t");
					bw.write("<type name=\"数值型\" data-type=\"number\"/>\r\n\t"); 
					bw.write("<type name=\"字符型\" data-type=\"string\"/>\r\n\t"); 
					bw.write("<type name=\"布尔型\" data-type=\"bool\"/>\r\n\t"); 
					bw.write("</type>\r\n\t");
					
					bw.write("</tagtype>\r\n\t");
					bw.write("<channels>\r\n\t");
					bw.write("</channels>\r\n\t");
					bw.write("<trans-channels>\r\n\t");
					bw.write("</trans-channels>\r\n");
					bw.write("</root>\r\n");
					bw.flush();
					bw.close();
					write.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SAXReader reader = new SAXReader();
//			Document doc = null;
			try {
				document = reader.read(new File(fileName));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return document;
		} else {
			return document;
		}
		
	}
	
	public void saveMainDocument(Document document){
		OutputFormat former =OutputFormat.createPrettyPrint();//设置格式化输出器  
        former.setEncoding("UTF-8");  
          
        try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"),former);  
			writer.write(document);  
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveMainDocument(){
		OutputFormat former =OutputFormat.createPrettyPrint();//设置格式化输出器  
        former.setEncoding("UTF-8");  
          
        try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"),former);  
			writer.write(document);  
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
