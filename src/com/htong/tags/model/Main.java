package com.htong.tags.model;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Main {

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File("C:\\vtags.xml"));
		TagDataBase database = new TagDataBase(doc);
	}

}
