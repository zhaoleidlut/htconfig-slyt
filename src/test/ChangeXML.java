package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ChangeXML {

	private static Document document;
	
	public Document getMainDocument(){
		if(document == null) {
			File file = new File("c:/vtags.xml");
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"utf-8");
					BufferedWriter bw = new BufferedWriter(write);

					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SAXReader reader = new SAXReader();
//			Document doc = null;
			try {
				document = reader.read(new File("c:/vtags.xml"));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			return document;
		} else {
			return document;
		}
		
	}
	
	public void saveMainDocument(){
		OutputFormat former =OutputFormat.createPrettyPrint();//设置格式化输出器  
        former.setEncoding("UTF-8");  
          
        try {
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream("c:/vtags.xml"),"UTF-8"),former);  
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
	
	
	public static void main(String[] args) {
		ChangeXML c = new ChangeXML();
		c.getMainDocument();
		
		String xpath = "//@var-name";
		List<Attribute> attrList =  document.selectNodes(xpath);
		for(Attribute a : attrList) {
			//System.out.println(a.getText());
//			if(a.getText().contains("YMYM")) {
//				a.setText(a.getText().replaceAll("YMYM", "YM"));
//				
//				System.out.println(a.getText());
//			}
			String name = a.getText();
			int len = name.length();
			String last4 = name.substring(len-4);
			String last6 = "00" + last4;
			String start4 = name.substring(0,len-4);
			String newVarName = start4+last6;
			System.out.println(newVarName);
			a.setText(newVarName);
			
			
		}
		c.saveMainDocument();

	}

}
