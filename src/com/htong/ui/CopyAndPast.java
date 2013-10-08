package com.htong.ui;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.window.Window;

import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.dialog.PastIndexNodeDialog;

/**
 * 复制粘贴回路
 * @author 赵磊
 *
 */
public enum CopyAndPast {
	instance;
	private Element indexNodeElement;
	public void copyNode(Element element) {
		indexNodeElement =  (Element) element.clone();
	}
	
	public IndexNodeModel pastNode(Element element) {
		if(hasCopy()) {
			IndexNodeModel inm = new IndexNodeModel((Element)indexNodeElement.clone());
			inm.setName(inm.getElement().attributeValue("name")+"的复制");
			PastIndexNodeDialog pid = new PastIndexNodeDialog(null, inm);
			if(pid.open()==Window.OK) {
				//indexNodeElement.setParent(null);
				Element myElement = inm.getElement();
				
				element.add(myElement);
				
				//更新变量名字
//				Element tagsElement = myElement.element("tags");
				List<Element> tagsElementList = myElement.selectNodes(".//tags");
				
				if(tagsElementList != null) {
					//先将变量名序号设置成最大，避免出现混乱情况
//					for(Element tagsElement : tagsElementList) {
//						List<Element> elementList = tagsElement.elements(TagModel.NODE_NAME);
//						for(Element ele : elementList) {	
//							String varName = ele.attributeValue(TagModel.VAR_NAME_ATTR);
//							String oldName = varName.substring(0, varName.length()-6);							
//							String newName = oldName + "999998";
//							ele.addAttribute(TagModel.VAR_NAME_ATTR, newName);
//						}
//					}
					for(Element tagsElement : tagsElementList) {
						@SuppressWarnings("unchecked")
						List<Element> elementList = tagsElement.elements(TagModel.NODE_NAME);
						//更新变量名
						for(Element ele : elementList) {
							String varType = ele.attributeValue("type");
							String varName = ele.attributeValue(TagModel.VAR_NAME_ATTR);
							String oldName = varName.substring(0, varName.length()-6);
							String newNum = GetVarNum.instanse.getVarNum(varType);
							//String newNum = "0000";
							String newName = oldName + newNum;
							ele.addAttribute(TagModel.VAR_NAME_ATTR, newName);
						}
					}
					
				}
	
				XMLDocumentFactory.instance.saveMainDocument();
				return inm;
			} else {
				return null;
			}
			
		} else {
			return null;
		}
	}
	
	public boolean hasCopy() {
		if(indexNodeElement != null) {
			return true;
		} else {
			return false;
		}
			
	}

}
