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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.csdgn.cddatse.data.ImageTile;
import org.csdgn.cddatse.data.SpriteSet;

public class ImageTilePanel extends JPanel {
	private static final long serialVersionUID = 6328834555090823394L;

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

	private MainPanel main;

	private ImageTile tile;

	public ImageTilePanel(MainPanel main, ImageTile tile) {
		this.tile = tile;
		this.main = main;

		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout());

		// get all the name things
		add(createIdPanel(), BorderLayout.NORTH);
		add(createImagePanel(), BorderLayout.CENTER);
	}

	private JPanel createIdPanel() {
		JPanel panel = new JPanel(new BorderLayout(4, 4));

		JLabel label = new JLabel("ID(s)");
		label.setVerticalAlignment(SwingConstants.TOP);
		panel.add(label, BorderLayout.WEST);

		JTextArea text = new JTextArea();
		text.setFont(AppToolkit.getBestMonospaceFont(text.getFont().getSize2D()));

		text.setText(sanitize(tile.toString()));
		text.setRows(4);
		text.addCaretListener(e -> {
			// check if changed
			String a = sanitize(tile.toString());
			String b = sanitize(text.getText());
			if (!a.equals(b)) {
				tile.id.clear();
				for (String str : text.getText().split("[ \\t\\n\\r]+")) {
					tile.id.add(str);
				}
				main.updateTile(tile);
			}
		});
		JScrollPane scroll = new JScrollPane(text);

		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private JComponent createSpriteComponent(int id) {
		BufferedImage sprite = main.tileset.getSpriteForId(id);
		JLayeredPane pane = new JLayeredPane();
		

		JButton btnImage = new JButton(new ImageIcon(sprite));
		JButton btnDelete = new JButton();
		btnDelete.setIcon(new ImageIcon(AppToolkit.getImageResource("close.png")));
		btnDelete.setRolloverIcon(new ImageIcon(AppToolkit.getImageResource("close_hover.png")));
		btnDelete.setPressedIcon(new ImageIcon(AppToolkit.getImageResource("close_press.png")));
		btnDelete.setBorderPainted(false); 
		btnDelete.setContentAreaFilled(false); 
        btnDelete.setFocusPainted(false); 
        btnDelete.setOpaque(false);

		Dimension btnImageSize = btnImage.getPreferredSize();
		Dimension btnDeleteSize = new Dimension(12, 12);

		Dimension paneSize = new Dimension(btnImageSize.width + btnDeleteSize.width / 3,
				btnImageSize.height + btnDeleteSize.height / 3);
		pane.setPreferredSize(paneSize);

		pane.add(btnImage, 0, 0);
		btnImage.setBounds(0, paneSize.height - btnImageSize.height, btnImageSize.width, btnImageSize.height);

		pane.add(btnDelete, 0, 0);
		btnDelete.setBounds(paneSize.width - btnDeleteSize.width, 0,
				btnDeleteSize.width, btnDeleteSize.height);

		return pane;
	}

	private JPanel createImagePanel() {
		// Temporary!
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// find out if we need the weighted layout
		boolean weighted = false;
		for (SpriteSet set : tile.fg) {
			if (set.weight != null) {
				weighted = true;
				break;
			}
		}
		JCheckBox cboxWeighted = new JCheckBox("Use weighted layout?");
		cboxWeighted.setSelected(weighted);
		cboxWeighted.setEnabled(false);
		panel.add(cboxWeighted);

		if (weighted) {
			generateWeightedPanels(panel);
		} else {
			generateUnweightedPanels(panel);
		}

		return panel;
	}

	private void generateUnweightedComponents(JPanel panel, Set<SpriteSet> comp) {
		for (SpriteSet set : tile.fg) {
			for (int id : set.ids) {
				panel.add(createSpriteComponent(id));
			}
		}
	}

	private void generateUnweightedPanels(JPanel panel) {
		JPanel spritePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
		spritePanel.setBorder(BorderFactory.createTitledBorder("Foreground Sprites"));
		generateUnweightedComponents(spritePanel, tile.fg);
		panel.add(spritePanel);

		spritePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
		spritePanel.setBorder(BorderFactory.createTitledBorder("Background Sprites"));
		generateUnweightedComponents(spritePanel, tile.bg);
		panel.add(spritePanel);
	}

	private void generateWeightedComponents(JPanel panel, Set<SpriteSet> comp) {
		for (SpriteSet set : comp) {
			JPanel spritePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
			spritePanel.setBorder(BorderFactory.createTitledBorder("Sprites"));
			int weight = 0;
			if (set.weight != null) {
				weight = set.weight;
			}
			JTextField txtWeight = new JTextField(String.valueOf(weight));
			txtWeight.setEnabled(false);
			spritePanel.add(new JLabel("Weight "));
			spritePanel.add(txtWeight);

			for (int id : set.ids) {
				spritePanel.add(createSpriteComponent(id));
			}

			panel.add(spritePanel);
		}
	}

	private void generateWeightedPanels(JPanel panel) {
		JPanel innerPanel = new JPanel();
		innerPanel.setBorder(BorderFactory.createTitledBorder("Foreground"));

		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		generateWeightedComponents(innerPanel, tile.fg);
		panel.add(innerPanel);

		innerPanel = new JPanel();
		innerPanel.setBorder(BorderFactory.createTitledBorder("Background"));
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		generateWeightedComponents(innerPanel, tile.bg);
		panel.add(innerPanel);
	}
}
