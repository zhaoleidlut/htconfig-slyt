package com.htong.ui;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagModel;
import com.htong.tags.model.TagTypeModel;
import com.htong.tags.model.XMLDocumentFactory;

/**
 * 获取xpath路径
 * 
 * @author 赵磊
 * 
 */
public class GetXpathUtil {
	private static final Logger log = Logger.getLogger(GetXpathUtil.class);
	/**
	 * 通过名字获得Index的xpath
	 * 
	 * @param indexModel
	 * @return /root/tagindex/index[@name='1']
	 */
	public static String getIndexXPathByName(IndexModel indexModel) {
		String path = indexModel.getElement().getPath();
		path += "[@" + IndexModel.NAME_ATTR + "='" + indexModel.getName()
				+ "']";
		return path;
	}

	/**
	 * 通过名字获得Index的xpath
	 * 
	 * @param indexModel
	 * @param indexName
	 * @return
	 */
	public static String getIndexXPathByName(IndexModel indexModel,
			String indexName) {
		String path = indexModel.getElement().getPath();
		path += "[@" + IndexModel.NAME_ATTR + "='" + indexName + "']";
		return path;
	}

	/**
	 * 通过名字获得IndexNode的xpath
	 * 
	 * @param indexNodeModel
	 * @return /root/tagindex/index[@name='1']/indexnode[@name='1']
	 */
	public static String getIndexNodeXPathByName(IndexNodeModel indexNodeModel) {
		StringBuilder sb = new StringBuilder();

		sb.append("/" + IndexNodeModel.NODE_NAME + "[@"
				+ IndexNodeModel.NAME_ATTR + "='" + indexNodeModel.getName()
				+ "']");
		Object parentObject = indexNodeModel.getParentObject();

		while (!(parentObject instanceof IndexModel)) {
			IndexNodeModel inm = (IndexNodeModel) parentObject;
			sb.insert(0, "/" + IndexNodeModel.NODE_NAME + "[@"
					+ IndexNodeModel.NAME_ATTR + "='" + inm.getName() + "']");
			parentObject = inm.getParentObject();
		}
		IndexModel indexModel = (IndexModel) parentObject;
		sb.insert(0, "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGINDEX_NODE + "/" + IndexModel.NODE_NAME
				+ "[@" + IndexModel.NAME_ATTR + "='" + indexModel.getName()
				+ "']");

		String path = sb.toString();
		log.debug(path);
		return path;
	}
	
	/**
	 * 通过element 获得xpath
	 * @param indexNodeElement
	 * @return /root/tagindex/index[@name='1']/indexnode[@name='1']
	 */
	public static String getIndexNodeXPathByElement(Element indexNodeElement) {
		StringBuilder sb = new StringBuilder();
		sb.insert(0, "/indexnode[@name='"+indexNodeElement.attributeValue("name")+"']");
		Element parentElement = indexNodeElement.getParent();
		while(parentElement.getName().equals("indexnode")) {
			sb.insert(0, "/indexnode[@name='"+parentElement.attributeValue("name")+"']");
			parentElement = parentElement.getParent();
		}
		sb.insert(0, "/index[@name='"+parentElement.attributeValue("name")+"']");
		sb.insert(0, "/root/tagindex");
		log.debug(sb.toString());
		return sb.toString();
		
	}

	/**
	 * 通过名字获得IndexNode的xpath
	 * 
	 * @param indexNodeModel
	 * @param indexNodeName
	 *            更新时的原始名字
	 * @return
	 */
	public static String getIndexNodeXPathByName(IndexNodeModel indexNodeModel,
			String indexNodeName) {
		StringBuilder sb = new StringBuilder();
		sb.append("/" + IndexNodeModel.NODE_NAME + "[@"
				+ IndexNodeModel.NAME_ATTR + "='" + indexNodeName + "']");

		Object parentObject = indexNodeModel.getParentObject();

		while (!(parentObject instanceof IndexModel)) {
			IndexNodeModel inm = (IndexNodeModel) parentObject;
			sb.insert(0, "/" + IndexNodeModel.NODE_NAME + "[@"
					+ IndexNodeModel.NAME_ATTR + "='" + inm.getName() + "']");
			parentObject = inm.getParentObject();
		}
		IndexModel indexModel = (IndexModel) parentObject;
		sb.insert(0, "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGINDEX_NODE + "/" + IndexModel.NODE_NAME
				+ "[@" + IndexModel.NAME_ATTR + "='" + indexModel.getName()
				+ "']");

		String path = sb.toString();
		return path;
	}

	/**
	 * 通过名字获得TagType的xpath
	 * 
	 * @param tagTypeModel
	 * @return
	 */
	public static String getTagTypeXPathByName(TagTypeModel tagTypeModel) {
		StringBuilder sb = new StringBuilder();
		sb.append("/" + TagTypeModel.NODE_NAME + "[@" + TagTypeModel.NAME_ATTR
				+ "='" + tagTypeModel.getName() + "']");
		Object parentObject = tagTypeModel.getParentObject();

		while (parentObject instanceof TagTypeModel) {
			TagTypeModel ttm = (TagTypeModel) parentObject;
			sb.insert(0, "/" + TagTypeModel.NODE_NAME + "[@"
					+ TagTypeModel.NAME_ATTR + "='" + ttm.getName() + "']");
			parentObject = ttm.getParentObject();
		}
		sb.insert(0, "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGTYPE_NODE);

		String path = sb.toString();
		return path;
	}

	/**
	 * 通过名字获得TagType的xpath
	 * 
	 * @param tagTypeModel
	 * @param name
	 * @return
	 */
	public static String getTagTypeXPathByName(TagTypeModel tagTypeModel,
			String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("/" + TagTypeModel.NODE_NAME + "[@" + TagTypeModel.NAME_ATTR
				+ "='" + name + "']");
		Object parentObject = tagTypeModel.getParentObject();

		while (parentObject instanceof TagTypeModel) {
			TagTypeModel ttm = (TagTypeModel) parentObject;
			sb.insert(0, "/" + TagTypeModel.NODE_NAME + "[@"
					+ TagTypeModel.NAME_ATTR + "='" + ttm.getName() + "']");
			parentObject = ttm.getParentObject();
		}
		sb.insert(0, "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGTYPE_NODE);

		String path = sb.toString();
		return path;
	}

	/**
	 * 通过slave_id获得Device xpath
	 * 
	 * @param deviceModel
	 * @return
	 */
	public static String getDeviceXPathBySlaveId(DeviceModel deviceModel) {
		ChannelModel channelModel = (ChannelModel) deviceModel
				.getParentObject();

		String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "="
				+ channelModel.getId() + "]/" + DeviceModel.NODE_NAME + "[@"
				+ DeviceModel.SLAVEID_ATTR + "=" + deviceModel.getSlaveId()
				+ "]";
		return path;
	}

	/**
	 * 通过序号获得Channel xpath
	 * 
	 * @param channelModel
	 * @return
	 */
	public static String getChannelXPathById(ChannelModel channelModel) {
		String path = "/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.CHANNEL_NODE + "/"
				+ ChannelModel.NODE_NAME + "[@" + ChannelModel.ID_ATTR + "="
				+ channelModel.getId() + "]";
		return path;
	}

	/**
	 * 通过TagModel的主索引获得主索引完整Xpath
	 * 
	 * @param mainIndexStr
	 *            电气索引/A楼/1层/1号柜/1#回路
	 * @return   /root/tagindex/index[@name="电气索引"]/indexnode[@name="A楼"]
	 */
	public static String getTagModelMainIndexXPath(String mainIndexStr) {
		String mainIndex[] = mainIndexStr.split("/");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGINDEX_NODE + "/");
		int i = mainIndex.length;
		stringBuilder.append(IndexModel.NODE_NAME + "[@" + IndexModel.NAME_ATTR
				+ "='" + mainIndex[0] + "']");
		for (int j = 1; j < i; j++) {
			stringBuilder.append("/" + IndexNodeModel.NODE_NAME + "[@"
					+ IndexNodeModel.NAME_ATTR + "='" + mainIndex[j] + "']");
		}

		return stringBuilder.toString();
	}

	/**
	 * 获得标签完整xpath
	 * 
	 * @param tagModel
	 * @return  /root/tagindex/index[@name="电气索引"]/indexnode[@name="A楼"]/tags/tag[@name="d"]
	 */
	public static String getTagModelXpath(TagModel tagModel) {
		String mainIndexStr = tagModel.getMainIndex();
		String mainIndex[] = mainIndexStr.split("/");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("/" + XMLDocumentFactory.ROOT_NODE + "/"
				+ XMLDocumentFactory.TAGINDEX_NODE + "/");
		int i = mainIndex.length;
		stringBuilder.append(IndexModel.NODE_NAME + "[@" + IndexModel.NAME_ATTR
				+ "='" + mainIndex[0] + "']");
		for (int j = 1; j < i; j++) {
			stringBuilder.append("/" + IndexNodeModel.NODE_NAME + "[@"
					+ IndexNodeModel.NAME_ATTR + "='" + mainIndex[j] + "']");
		}
		stringBuilder.append("/" + XMLDocumentFactory.TAGS_NODE + "/"
				+ TagModel.NODE_NAME + "[@" + TagModel.NAME_ATTR + "='"
				+ tagModel.getName() + "']");

		
		return stringBuilder.toString();
	}
	
	/**
	 * 通过变量元素路径获得主索引
	 * @param 变量element
	 * @return 电气索引/A楼/1层/1号柜/1#回路
	 */
	public static String getMainIndexByElementPath(Element e) {
		StringBuilder sb = new StringBuilder();
		Element indexnodeElement = e.getParent().getParent();//索引叶子节点
		
		while(indexnodeElement.getName().equals("indexnode")) {
			sb.insert(0, "/"+indexnodeElement.attributeValue("name"));
			indexnodeElement = indexnodeElement.getParent();
		}
		sb.insert(0, indexnodeElement.attributeValue("name"));//加入主索引
		
		
		return sb.toString();
	}
	
	/**
	 * 通过索引节点元素获得主索引
	 * @param 索引节点element
	 * @return 电气索引/A楼/1层/1号柜/1#回路
	 */
	public static String getMainIndexByIndexNodeElement(Element e) {
		StringBuilder sb = new StringBuilder();
		Element indexnodeElement = e;//索引叶子节点
		
		while(indexnodeElement.getName().equals("indexnode")) {
			sb.insert(0, "/"+indexnodeElement.attributeValue("name"));
			indexnodeElement = indexnodeElement.getParent();
		}
		sb.insert(0, indexnodeElement.attributeValue("name"));//加入主索引
		
		
		return sb.toString();
	}

}
