package com.htong.tags.daoImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.htong.tags.dao.RecorderModelDao;
import com.htong.tags.dao.TagModelDao;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.tags.model.tag.RecorderModel;
import com.htong.ui.GetXpathUtil;

public class RecorderModelDaoImpl implements RecorderModelDao {

	@Override
	public List<RecorderModel> getRecorderModels(TagModel tagModel,
			String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		List<RecorderModel> recorderList = new ArrayList<RecorderModel>();
		Element e = (Element) document.selectSingleNode(parentXpath);
		for (Iterator<Element> i = e.elementIterator(RecorderModel.NODE_NAME); i
				.hasNext();) {
			Element element = i.next();
			RecorderModel rm = new RecorderModel(element);
			rm.setTagModel(tagModel); // 设置上级标签
			recorderList.add(rm);
		}
		return recorderList;
	}

	@Override
	public void addRecorder(RecorderModel recorder, String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element tagElement = (Element) document.selectSingleNode(parentXpath);
		Element myElement = tagElement.addElement(RecorderModel.NODE_NAME);

		myElement.addAttribute(RecorderModel.TYPE_ATTR, recorder.getType());
		myElement.addAttribute(RecorderModel.NAME_ATTR, recorder.getName());

		myElement.addAttribute(RecorderModel.STATISTIC_MODE_ATTR,
				recorder.getStatisticMode());
		myElement.addAttribute(RecorderModel.COMPUTE_MODE_ATTR,
				recorder.getComputeMode());
		myElement.addAttribute(RecorderModel.START_ATTR, recorder.getStart());
		myElement.addAttribute(RecorderModel.END_ATTR, recorder.getEnd());
		myElement.addAttribute(RecorderModel.DAY_INCLUDE_ATTR,
				recorder.getDayInclude());
		myElement.addAttribute(RecorderModel.DAY_EXCLUDE_ATTR,
				recorder.getDayExclude());
		myElement.addAttribute(RecorderModel.WEEK_INCLUDE_ATTR,
				recorder.getWeekInclude());
		myElement.addAttribute(RecorderModel.WEEK_EXCLUDE_ATTR,
				recorder.getWeekExclude());

		myElement.addAttribute(RecorderModel.UPPER_ATTR, recorder.getUpper());
		myElement.addAttribute(RecorderModel.UPPER_INFO_ATTR,
				recorder.getUpperInfo());
		myElement.addAttribute(RecorderModel.LOWER_ATTR, recorder.getLower());
		myElement.addAttribute(RecorderModel.LOWER_INFO_ATTR,
				recorder.getLowerInfo());

		myElement.addAttribute(RecorderModel.WHEN_ATTR, recorder.getWhen());

		if (recorder.getOffInfo() != null) {
			myElement.addAttribute(RecorderModel.OFF_INFO_ATTR,
					recorder.getOffInfo());
		} else if (recorder.getRs_offInfo() != null) {
			myElement.addAttribute(RecorderModel.RS_OFF_INFO_ATTR,
					recorder.getRs_offInfo());
		} else if (recorder.getOffInfo() == null
				&& recorder.getRs_offInfo() == null) {
			myElement.addAttribute(RecorderModel.RS_OFF_INFO_ATTR, null);
		}

		if (recorder.getOnInfo() != null) {
			myElement.addAttribute(RecorderModel.ON_INFO_ATTR,
					recorder.getOnInfo());
		} else if (recorder.getRs_onInfo() != null) {
			myElement.addAttribute(RecorderModel.RS_ON_INFO_ATTR,
					recorder.getRs_onInfo());
		} else if (recorder.getOnInfo() == null
				&& recorder.getRs_onInfo() == null) {
			myElement.addAttribute(RecorderModel.RS_ON_INFO_ATTR, null);
		}

		// myElement.addAttribute(RecorderModel.ON_INFO_ATTR,
		// recorder.getOnInfo());
		// myElement.addAttribute(RecorderModel.OFF_INFO_ATTR,
		// recorder.getOffInfo());
		//
		// myElement.addAttribute(RecorderModel.RS_ON_INFO_ATTR,
		// recorder.getRs_onInfo());
		// myElement.addAttribute(RecorderModel.RS_OFF_INFO_ATTR,
		// recorder.getRs_offInfo());

		if (recorder.getShow_alarm() != null
				&& recorder.getShow_alarm().equals("true")
				|| recorder.getShow_fault() != null
				&& recorder.getShow_fault().equals("true")
				|| recorder.getShow_rs() != null
				&& recorder.getShow_rs().equals("true")) {
			myElement.addAttribute(RecorderModel.SHOW_ATTR, "true");
		} else {
			myElement.addAttribute(RecorderModel.SHOW_ATTR, null);
		}

		myElement.addAttribute(RecorderModel.INFO_ATTR, recorder.getInfo());
		if (recorder.getSoeJS() != null) {
			myElement.addCDATA(recorder.getSoeJS()); // cdata
		} else {
			myElement.clearContent();
		}

		myElement.addAttribute(RecorderModel.RANGE_ATTR, recorder.getRange());
		myElement.addAttribute(RecorderModel.INTERVAL_ATTR,
				recorder.getInterval());

		myElement.clearContent();
		if (recorder.getYmtagExpression() != null) {
			myElement.addCDATA(recorder.getYmtagExpression());
		}
		
		myElement.addAttribute(RecorderModel.MIN_VALUE, recorder.getMinValue());
		myElement.addAttribute(RecorderModel.MAX_VALUE, recorder.getMaxValue());
		myElement.addAttribute(RecorderModel.UNIT_VALUE, recorder.getUnitValue());

		recorder.setElement(myElement);

		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void updateRecorder(RecorderModel recorder, String oldName,
			String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		parentXpath += "/" + RecorderModel.NODE_NAME + "[@"
				+ RecorderModel.NAME_ATTR + "='" + oldName + "']";
		Element myElement = (Element) document.selectSingleNode(parentXpath);

		myElement.addAttribute(RecorderModel.TYPE_ATTR, recorder.getType());
		myElement.addAttribute(RecorderModel.NAME_ATTR, recorder.getName());

		myElement.addAttribute(RecorderModel.STATISTIC_MODE_ATTR,
				recorder.getStatisticMode());
		myElement.addAttribute(RecorderModel.COMPUTE_MODE_ATTR,
				recorder.getComputeMode());
		myElement.addAttribute(RecorderModel.START_ATTR, recorder.getStart());
		myElement.addAttribute(RecorderModel.END_ATTR, recorder.getEnd());
		myElement.addAttribute(RecorderModel.DAY_INCLUDE_ATTR,
				recorder.getDayInclude());
		myElement.addAttribute(RecorderModel.DAY_EXCLUDE_ATTR,
				recorder.getDayExclude());
		myElement.addAttribute(RecorderModel.WEEK_INCLUDE_ATTR,
				recorder.getWeekInclude());
		myElement.addAttribute(RecorderModel.WEEK_EXCLUDE_ATTR,
				recorder.getWeekExclude());

		myElement.addAttribute(RecorderModel.UPPER_ATTR, recorder.getUpper());
		myElement.addAttribute(RecorderModel.UPPER_INFO_ATTR,
				recorder.getUpperInfo());
		myElement.addAttribute(RecorderModel.LOWER_ATTR, recorder.getLower());
		myElement.addAttribute(RecorderModel.LOWER_INFO_ATTR,
				recorder.getLowerInfo());

		myElement.addAttribute(RecorderModel.WHEN_ATTR, recorder.getWhen());

		if (recorder.getOffInfo() != null) {
			myElement.addAttribute(RecorderModel.OFF_INFO_ATTR,
					recorder.getOffInfo());
		} else if (recorder.getRs_offInfo() != null) {
			myElement.addAttribute(RecorderModel.RS_OFF_INFO_ATTR,
					recorder.getRs_offInfo());
		} else if (recorder.getOffInfo() == null
				&& recorder.getRs_offInfo() == null) {
			myElement.addAttribute(RecorderModel.RS_OFF_INFO_ATTR, null);
		}

		if (recorder.getOnInfo() != null) {
			myElement.addAttribute(RecorderModel.ON_INFO_ATTR,
					recorder.getOnInfo());
		} else if (recorder.getRs_onInfo() != null) {
			myElement.addAttribute(RecorderModel.RS_ON_INFO_ATTR,
					recorder.getRs_onInfo());
		} else if (recorder.getOnInfo() == null
				&& recorder.getRs_onInfo() == null) {
			myElement.addAttribute(RecorderModel.RS_ON_INFO_ATTR, null);
		}

		if (recorder.getShow_alarm() != null
				&& recorder.getShow_alarm().equals("true")
				|| recorder.getShow_fault() != null
				&& recorder.getShow_fault().equals("true")
				|| recorder.getShow_rs() != null
				&& recorder.getShow_rs().equals("true")) {
			myElement.addAttribute(RecorderModel.SHOW_ATTR, "true");
		} else {
			myElement.addAttribute(RecorderModel.SHOW_ATTR, null);
		}

		myElement.addAttribute(RecorderModel.INFO_ATTR, recorder.getInfo());
		if (recorder.getSoeJS() != null) {
			myElement.clearContent();
			myElement.addCDATA(recorder.getSoeJS());
		}

		myElement.addAttribute(RecorderModel.RANGE_ATTR, recorder.getRange());
		myElement.addAttribute(RecorderModel.INTERVAL_ATTR,
				recorder.getInterval());

		myElement.clearContent();
		if (recorder.getYmtagExpression() != null) {
			myElement.addCDATA(recorder.getYmtagExpression());
		}
		
		myElement.addAttribute(RecorderModel.MIN_VALUE, recorder.getMinValue());
		myElement.addAttribute(RecorderModel.MAX_VALUE, recorder.getMaxValue());
		myElement.addAttribute(RecorderModel.UNIT_VALUE, recorder.getUnitValue());

		recorder.setElement(myElement);

		XMLDocumentFactory.instance.saveMainDocument(document);

	}

	@Override
	public void removeRecorder(RecorderModel recorder, String parentXpath) {
		Document document = XMLDocumentFactory.instance.getMainDocument();
		Element parentElement = (Element) document
				.selectSingleNode(parentXpath);
		parentXpath += "/" + RecorderModel.NODE_NAME + "[@"
				+ RecorderModel.NAME_ATTR + "='" + recorder.getName() + "']";
		Element e = (Element) document.selectSingleNode(parentXpath);
		parentElement.remove(e);

		XMLDocumentFactory.instance.saveMainDocument(document);
	}

}
