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
		if(hasFocus) {
			setBackground(new Color(list.getSelectionBackground().getRGB() & 0xFFFFFF));
		} else {
			setBackground(new Color(list.getBackground().getRGB() & 0xFFFFFF));
		}
		return this;
	}
}