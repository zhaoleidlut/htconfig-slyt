package com.htong.tags.daoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TimeRange;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.GetXpathUtil;

public class TimeRangeDaoImpl {
	public List<TimeRange> getAllTimeRange(IndexNodeModel indexNode) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getIndexNodeXPathByName(indexNode);
		Element element = (Element) document.selectSingleNode(xpath);
		if(element == null) {
			return null;
		}
		if(element.elements(TimeRange.NODE_NAME) == null) {
			return null;
		} else {
			List<TimeRange> timeRangeList = new ArrayList<TimeRange>();
			for(Iterator<Element> i = element.elementIterator(TimeRange.NODE_NAME);i.hasNext();) {
				Element e = i.next();
				TimeRange timeRange = new TimeRange(e);
				timeRangeList.add(timeRange);
			}
			return timeRangeList;
		}
	}
	
	public void saveAll(IndexNodeModel indexNode, List<TimeRange> timeRangeList) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		String xpath = GetXpathUtil.getIndexNodeXPathByName(indexNode);
		Element element = (Element) document.selectSingleNode(xpath);
		//先删除所有
		if(element.elements(TimeRange.NODE_NAME) != null) {
			for(Iterator<Element> i = element.elementIterator(TimeRange.NODE_NAME);i.hasNext();) {
				Element e = i.next();
				element.remove(e);
			}
			XMLDocumentFactory.instance.saveMainDocument(document);
		}
		
		
		if(timeRangeList == null || timeRangeList.isEmpty()) {
			return;
		} else {
			document = XMLDocumentFactory.instance.getMainDocument();
			element = (Element) document.selectSingleNode(xpath);
			
			for(TimeRange timeRange : timeRangeList) {
				Element newElement = element.addElement(TimeRange.NODE_NAME);
				newElement.addAttribute(TimeRange.NAME_ATTR, timeRange.getName());
				newElement.addAttribute(TimeRange.TYPE_ATTR, timeRange.getType());
				newElement.addAttribute(TimeRange.START_ATTR, timeRange.getStart());
				newElement.addAttribute(TimeRange.END_ATTR, timeRange.getEnd());
				newElement.addAttribute(TimeRange.DAY_INCLUDE_ATTR, timeRange.getDayInclude());
				newElement.addAttribute(TimeRange.DAY_EXCLUDE_ATTR, timeRange.getDayExclude());
				newElement.addAttribute(TimeRange.WEEK_INCLUDE_ATTR, timeRange.getWeekInclude());
				newElement.addAttribute(TimeRange.WEEK_EXCLUDE_ATTR, timeRange.getWeekExclude());
			}
			
			XMLDocumentFactory.instance.saveMainDocument(document);
		}
		
		
		
	}

}
