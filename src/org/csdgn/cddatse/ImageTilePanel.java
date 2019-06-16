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
package org.csdgn.cddatse;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.csdgn.cddatse.data.ImageTile;

public class ImageTilePanel extends JPanel {
	private static final long serialVersionUID = 6328834555090823394L;

	private MainPanel main;
	private ImageTile tile;

	public ImageTilePanel(MainPanel main, ImageTile tile) {
		this.tile = tile;
		this.main = main;

		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout());

		// get all the name things
		JTextArea area = new JTextArea();
		area.setText(sanitize(tile.toString()));
		area.addCaretListener(e -> {
			// check if changed
			String a = sanitize(tile.toString());
			String b = sanitize(area.getText());
			if (!a.equals(b)) {
				tile.id.clear();
				for (String str : area.getText().split("[ \\t\\n\\r]+")) {
					tile.id.add(str);
				}
				main.updateTile(tile);
			}
		});

		add(area, BorderLayout.CENTER);

	}

	private static String sanitize(String list) {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (String str : list.split("[ \t\n\r]+")) {
			if (!first) {
				buf.append("\n");
			}
			first = false;
			buf.append(str);
		}
		return buf.toString();
	}
}
