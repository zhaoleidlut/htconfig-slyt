package com.htong.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.htong.tags.composites.ChannelEditComposite;
import com.htong.tags.composites.ChannelsEditComposite;
import com.htong.tags.composites.DeviceEditComposite;
import com.htong.tags.composites.IndexNodeEnergyEditComposite;
import com.htong.tags.composites.IndexNodeNormalBuildingComposite;
import com.htong.tags.composites.IndexNodeNormalComposite;
import com.htong.tags.composites.IndexNodeNormalFenQuComposite;
import com.htong.tags.composites.TagsIndexEditComposite;
import com.htong.tags.composites.IndexNodeMainComposite;
import com.htong.tags.composites.TagsTypeEditComposite;
import com.htong.tags.composites.TextMessageComposite;
import com.htong.tags.model.ChannelModel;
import com.htong.tags.model.DeviceModel;
import com.htong.tags.model.IndexModel;
import com.htong.tags.model.IndexNodeModel;
import com.htong.tags.model.TagTypeModel;

/**
 * 操作面板
 * 
 * @author 赵磊
 * 
 */
public class OperatingComposite extends Composite {
	private Composite composite;
	private TextMessageComposite textMessageComposite; // 信息提示面板
	private TagsIndexEditComposite tagsIndexEditComposite; // 标签索引编辑面板
	private IndexNodeMainComposite IndexNodeMainComposite; // 标签索引节点编辑面板
	private TagsTypeEditComposite tagsTypeEditComposite; // 标签类型编辑面板
	private ChannelEditComposite channelEditComposite; // 采集通道编辑面板
	private DeviceEditComposite deviceEditComposite; // 设备编辑面板
	
	private IndexNodeEnergyEditComposite indexNodeEnergyEditComposite;	//编辑能耗分类分项
	private IndexNodeNormalBuildingComposite indexNodeNormalBuildingComposite;	//建筑分区
	private IndexNodeNormalComposite indexNodeNormalComposite;//常规分类索引
	private IndexNodeNormalFenQuComposite indexNodeNormalFenQuComposite;//常规分区索引
	
	private ChannelsEditComposite channelsEditComposite;	

	private StackLayout stackLayout = new StackLayout();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public OperatingComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		composite = new Composite(this, SWT.NONE);

		setLayout(stackLayout);

	}

	@Override
	protected void checkSubclass() {
	}
	
	/**
	 * 编辑能耗分类分项
	 * @param indexNode
	 */
	public void editEnergy(IndexNodeModel indexNode) {
		indexNodeEnergyEditComposite = new IndexNodeEnergyEditComposite(this, SWT.NONE, indexNode);
		stackLayout.topControl = indexNodeEnergyEditComposite;
		this.layout();
	}
	
	/**
	 * 编辑建筑分区
	 * @param indexNode
	 */
	public void editNormalBuilding(IndexNodeModel indexNode) {
		indexNodeNormalBuildingComposite = new IndexNodeNormalBuildingComposite(this, SWT.NONE, indexNode);
		stackLayout.topControl = indexNodeNormalBuildingComposite;
		this.layout();
	}
	/**
	 * 编辑常规索引
	 * @param indexNode
	 */
	public void editNormal(IndexNodeModel indexNode) {
		indexNodeNormalComposite = new IndexNodeNormalComposite(this, SWT.NONE, indexNode);
		stackLayout.topControl = indexNodeNormalComposite;
		this.layout();
	}
	
	/**
	 * 编辑常规分区索引
	 * @param indexNode
	 */
	public void editNormalFenQu(IndexNodeModel indexNode) {
		indexNodeNormalFenQuComposite = new IndexNodeNormalFenQuComposite(this, SWT.NONE, indexNode);
		stackLayout.topControl = indexNodeNormalFenQuComposite;
		this.layout();
	}
	
	/**
	 * 编辑变量标签索引
	 * @param indexNode
	 */
	public void editMain(IndexNodeModel indexNode) {
		IndexNodeMainComposite = new IndexNodeMainComposite(this,
				SWT.NONE, indexNode);
		stackLayout.topControl = IndexNodeMainComposite;
		this.layout();
	}
	
	/**
	 * 编辑变量类型
	 * @param type
	 * @param editable
	 */
	public void editType(TagTypeModel type, boolean nameEditable, boolean typeEditable) {
		tagsTypeEditComposite = new TagsTypeEditComposite(this, SWT.NONE,
				type, nameEditable, typeEditable);
		stackLayout.topControl = tagsTypeEditComposite;
		this.layout();
	}
	
	public void editChannels() {
		channelsEditComposite = new ChannelsEditComposite(this, SWT.NONE);
		stackLayout.topControl = channelsEditComposite;
		this.layout();
	}

	public void setTop(Object o) {
		if (o instanceof ChannelModel) {
			channelEditComposite = new ChannelEditComposite(this, SWT.NONE,
					(ChannelModel) o);
			stackLayout.topControl = channelEditComposite;
			this.layout();
		} else if(o instanceof IndexModel) {
			tagsIndexEditComposite = new TagsIndexEditComposite(this, SWT.NONE, (IndexModel)o);
			stackLayout.topControl = tagsIndexEditComposite;
			this.layout();
		} else if (o instanceof String) {
			textMessageComposite = new TextMessageComposite(this, SWT.NONE,
					(String) o);
			stackLayout.topControl = textMessageComposite;
			this.layout();
		} else if (o instanceof DeviceModel) {
			deviceEditComposite = new DeviceEditComposite(this, SWT.NONE, (DeviceModel)o);
			stackLayout.topControl = deviceEditComposite;
			this.layout();
		} else {
			stackLayout.topControl = composite;
			this.layout();
		}

	}

}
