package com.htong.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;

public class TagNormalIndexTreeContentProvider implements ITreeContentProvider {

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
		return ((List)inputElement).toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof IndexModel) {
			IndexModel indexModel = (IndexModel)parentElement;
			Element element = indexModel.getElement();
			List<IndexNodeModel> indexModelList = new ArrayList<IndexNodeModel>();
			if(element.element(IndexNodeModel.NODE_NAME) != null) {
				for ( Iterator i = element.elementIterator(IndexNodeModel.NODE_NAME); i.hasNext(); ) {
		            Element e = (Element) i.next();
		            IndexNodeModel im = new IndexNodeModel(e);
		            im.setParentObject(parentElement);	//设置上级节点
		            indexModelList.add(im);
		        }
			}
			return indexModelList.toArray();
		} else if(parentElement instanceof IndexNodeModel) {
			IndexNodeModel indexNode = (IndexNodeModel)parentElement;
			Element element = indexNode.getElement();
			List<IndexNodeModel> indexNodeModelList = new ArrayList<IndexNodeModel>();
			
			if(element.element(IndexNodeModel.NODE_NAME) != null) {
				for ( Iterator i = element.elementIterator(IndexNodeModel.NODE_NAME); i.hasNext(); ) {
		            Element e = (Element) i.next();
		            IndexNodeModel im = new IndexNodeModel(e);
		            im.setParentObject(parentElement);	//设置上级节点
		            indexNodeModelList.add(im);
		        }
			}
			return indexNodeModelList.toArray();
		} 
		return null;
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
