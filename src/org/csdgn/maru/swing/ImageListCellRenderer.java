/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2020 Robert Maupin
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.csdgn.maru.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ImageListCellRenderer extends JLabel implements ListCellRenderer<Image> {
	private static final long serialVersionUID = -6530533873699384730L;
	public ImageIcon icon = new ImageIcon();

	public ImageListCellRenderer() {
		setOpaque(true);
		setText("");
		setIcon(icon);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Image> list, Image value, int index,
			boolean isSelected, boolean hasFocus) {
		icon.setImage((Image) value);

		if (hasFocus) {
			setBackground(new Color(list.getSelectionBackground().getRGB() & 0xFFFFFF));
		} else {
			setBackground(new Color(list.getBackground().getRGB() & 0xFFFFFF));
		}
		return this;
	}
}