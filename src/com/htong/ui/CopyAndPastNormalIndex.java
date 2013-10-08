package com.htong.ui;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.window.Window;

import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.XMLDocumentFactory;
import com.htong.ui.dialog.PastIndexNodeDialog;
import com.htong.ui.dialog.PastNormalIndexNodeDialog;

/**
 * 复制粘贴常规分类索引
 * @author 赵磊
 *
 */
public enum CopyAndPastNormalIndex {
	instance;
	private Element indexNodeElement;
	public void copyNode(Element element) {
		indexNodeElement =  (Element) element.clone();
	}
	
	public IndexNodeModel pastNode(Element element) {
		if(hasCopy()) {
			IndexNodeModel inm = new IndexNodeModel((Element)indexNodeElement.clone());
			inm.setName(inm.getElement().attributeValue("name")+"的复制");
			PastNormalIndexNodeDialog pid = new PastNormalIndexNodeDialog(null, inm);
			if(pid.open()==Window.OK) {
				//indexNodeElement.setParent(null);
				Element myElement = inm.getElement();
				
				element.add(myElement);

	
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
