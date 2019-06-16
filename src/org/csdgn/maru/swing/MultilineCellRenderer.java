/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2019 Robert Maupin
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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

public class MultilineCellRenderer<E> implements ListCellRenderer<E> {

	private JPanel panel;
	private JTextArea textArea;

	public MultilineCellRenderer() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		textArea = new JTextArea();
		textArea.setOpaque(true);
		panel.add(textArea, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean selected,
			boolean focus) {
		textArea.setText((String) value.toString());
		int width = list.getWidth();
		if (width > 0) {
			textArea.setSize(width, Short.MAX_VALUE);
		}
		if (focus) {
			textArea.setForeground(list.getSelectionForeground());
			textArea.setBackground(list.getSelectionBackground());
		} else {
			textArea.setForeground(list.getForeground());
			textArea.setBackground(list.getBackground());
		}
		return panel;

	}
}