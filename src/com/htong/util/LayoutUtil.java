package com.htong.util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * 窗口居中
 * @author 赵磊
 */
public class LayoutUtil {
	public static void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}
}
