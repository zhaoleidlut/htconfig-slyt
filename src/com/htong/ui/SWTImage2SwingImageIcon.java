package com.htong.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.ui.PlatformUI;



/**
 * 
 * Change javax.swing.ImageIcon object to org.eclipse.swt.graphics.Image object.
 * 
 * @author Freshwind
 */
public class SWTImage2SwingImageIcon {

	public Image getImage(Object element)

	{

	    //得到文件图标

	    ImageIcon systemIcon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon((File) element);

	    java.awt.Image image = systemIcon.getImage();


	 

	    int width = image.getWidth(null);

	    int height = image.getHeight(null);

	    //创建用于绘制Icon的缓冲区

	    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	 

	    //绘制Icon到缓冲区

	    Graphics2D g2d = bufferedImage.createGraphics();

	    g2d.drawImage(image, 0, 0, null);

	    g2d.dispose();

	    //读取缓冲区图片数据到一个数组

	    int[] data = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();

	    //将没有颜色的点设置为白色

	    for (int i = 0; i < data.length; i++)

	    {

	    if (data[i] == 0)

	        data[i] = 0xFFFFFF;

	    }

	    //根据数组数据生成ImageData对象

	    ImageData imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));

	    imageData.setPixels(0, 0, data.length, data, 0);

	    //生成Image对象

//	    Image swtImage = new Image(PlatformUI.getWorkbench().getDisplay(), imageData);

	    return null;
	}

}
