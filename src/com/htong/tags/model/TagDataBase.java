package com.htong.tags.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;


public class TagDataBase {
	private Document document;
	private Element root;
	
		
	private Element index;
	private Element tpl;
	private Element tagType;
	private Element tags;
	
	private Map<String, IndexModel> indexMap = new HashMap<String, IndexModel>();// 索引集合
	private Map<String, TplModel> tplMap = new HashMap<String, TplModel>();// 模板集合
	
	private List<TagModel> realTimeTags = new ArrayList<TagModel>();// 实时变量
	private List<TagModel> memoryTags = new ArrayList<TagModel>();// 内存中间变量(包括常量)
	
	@SuppressWarnings("unchecked")
	public TagDataBase(Document doc) {
		this.document = doc;
		this.root = doc.getRootElement();
		
		// 标签索引
		index = root.element("tagindex");
		if (index != null) {
			for ( Iterator i = index.elementIterator( "index" ); i.hasNext(); ) {
	            Element e = (Element) i.next();
	            IndexModel im = new IndexModel(e);
	            this.indexMap.put(im.getName(), im);
	            System.out.println("标签索引:" + im.getName());
	        }
		}
		
		// 标签模板
		tpl = root.element("tagtpl");
		if (tpl != null) {
			for ( Iterator i = tpl.elementIterator( "tpl" ); i.hasNext(); ) {
	            Element tplEl = (Element) i.next();
	            TplModel tm = new TplModel(tplEl);
	            this.tplMap.put(tm.getName(), tm);
	        }
		}
	
		// 标签类型
		this.tagType = root.element("tagtype");
		
		// 标签变量
		tags = root.element("tags");
		if (tags != null) {
			for ( Iterator i = tags.elementIterator( "tag" ); i.hasNext(); ) {
	            Element e = (Element) i.next();
	            TagModel tm = new TagModel(e);
	            if (tm.getType().startsWith("内存变量")) {
	            	memoryTags.add(tm);
	            } else {
	            	realTimeTags.add(tm);
	            }
	        }
		}
	}

	public Map<String, IndexModel> getIndexMap() {
		return indexMap;
	}
	
	

}
