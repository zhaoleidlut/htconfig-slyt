package com.htong.ui;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.dialogs.MessageDialog;

import com.htong.tags.model.XMLDocumentFactory;

public class InitAllVarName {
	public void initAllVarName() {
		List<Element> eleList = XMLDocumentFactory.instance.getMainDocument().selectNodes("//tag");
		System.out.println(eleList.size());
		
		int maxYC = 1;
		int maxYX = 1;
		int maxYT = 1;
		int maxYK = 1;
		int maxYM = 1;
		int maxOther = 1;
		
		for(Element e : eleList) {
			String type = e.attributeValue("type");
			if(type.startsWith("遥测")) {
				String num = String.valueOf(maxYC);
				for(int i = 0;i<(6-String.valueOf(maxYC).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXX_YC"+num);
				maxYC++;
			} else if(type.startsWith("遥信")) {
				String num = String.valueOf(maxYX);
				for(int i = 0;i<(6-String.valueOf(maxYX).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXX_YX"+num);
				maxYX++;
			} else if(type.startsWith("遥控")) {
				String num = String.valueOf(maxYK);
				for(int i = 0;i<(6-String.valueOf(maxYK).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXX_YK"+num);
				maxYK++;
			} else if(type.startsWith("遥调")) {
				String num = String.valueOf(maxYT);
				for(int i = 0;i<(6-String.valueOf(maxYT).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXX_YT"+num);
				maxYT++;
			} else if(type.startsWith("遥脉")) {
				String num = String.valueOf(maxYM);
				for(int i = 0;i<(6-String.valueOf(maxYM).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXXXXXXX_YM"+num);
				maxYM++;
			} else  {
				String num = String.valueOf(maxOther);
				for(int i = 0;i<(6-String.valueOf(maxOther).length());i++) {
					num = "0" + num;
				}
				e.addAttribute("var-name", "XXXX_YX"+num);
				maxOther ++;
			}
		}
		XMLDocumentFactory.instance.saveMainDocument();
		
		MessageDialog.openInformation(null, "结果", "遥测"+(maxYC-1) + "，遥信"+(maxYX-1) + "，遥调"+(maxYT-1) + "，遥控"+(maxYK-1) + "，遥脉" + (maxYM-1) + "，其他" +(maxOther-1));
	}

}
