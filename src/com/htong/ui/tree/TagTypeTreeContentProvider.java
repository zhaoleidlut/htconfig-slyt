package com.htong.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.htong.tags.model.TagTypeModel;

public class TagTypeTreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((List) inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		TagTypeModel tagType = (TagTypeModel)parentElement;
		Element element = tagType.getElement();
		List<TagTypeModel> tagTypeList = new ArrayList<TagTypeModel>();
		if (element != null) {
			for ( Iterator i = element.elementIterator(TagTypeModel.NODE_NAME); i.hasNext(); ) {
	            Element e = (Element) i.next();
	            TagTypeModel tt = new TagTypeModel(e);
	            tt.setParentObject(parentElement);	//上级节点
	            tagTypeList.add(tt);
	        }
		}
		return tagTypeList.toArray();
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		Object[] children = this.getChildren(element);
		if (children == null) {
			return false;
		} else if (children.length > 0) {
			return true;
		} else {
			return false;
		}
	}

}
