/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2019-2020 Robert Maupin
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
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
import javax.swing.SwingUtilities;
import javax.swing.text.PlainDocument;

import org.csdgn.cddatse.data.ImageTile;
import org.csdgn.cddatse.data.SpriteSet;
import org.csdgn.maru.swing.IntegerDocumentFilter;

public class ImageTilePanel extends JPanel {
	private class SpriteComponentData {
		JComponent comp;
		JButton delete;
		JButton image;
	}

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
	private JCheckBox cboxWeighted;
	private JPanel spritePanel;
	private JPanel bodyPanel;

	public ImageTilePanel(MainPanel main, ImageTile tile) {
		this.tile = tile;
		this.main = main;

		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout());

		// get all the name things
		add(createIdPanel(), BorderLayout.NORTH);
		add(bodyPanel = createBodyPanel(), BorderLayout.CENTER);
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

	private SpriteComponentData createSpriteComponent(int id) {
		BufferedImage sprite = main.tileset.getSpriteForId(id);
		JLayeredPane pane = new JLayeredPane();

		JButton btnImage = new JButton(Options.INSTANCE.getScaledIcon(sprite));
		btnImage.setFocusPainted(false);
		// btnImage.addActionListener(press);

		JButton btnDelete = new JButton();
		btnDelete.setIcon(new ImageIcon(AppToolkit.getImageResource("delete.png")));
		btnDelete.setRolloverIcon(new ImageIcon(AppToolkit.getImageResource("delete_hover.png")));
		btnDelete.setPressedIcon(new ImageIcon(AppToolkit.getImageResource("delete_press.png")));
		btnDelete.setBorderPainted(false);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setFocusPainted(false);
		btnDelete.setOpaque(false);

		// btnDelete.addActionListener(delete);

		Dimension btnImageSize = btnImage.getPreferredSize();
		Dimension btnDeleteSize = new Dimension(12, 12);

		Dimension paneSize = new Dimension(btnImageSize.width + btnDeleteSize.width / 3,
				btnImageSize.height + btnDeleteSize.height / 3);
		pane.setPreferredSize(paneSize);

		pane.add(btnImage, 0, 0);
		btnImage.setBounds(0, paneSize.height - btnImageSize.height, btnImageSize.width, btnImageSize.height);

		pane.add(btnDelete, 1, 0);
		btnDelete.setBounds(paneSize.width - btnDeleteSize.width, 0, btnDeleteSize.width, btnDeleteSize.height);

		SpriteComponentData data = new SpriteComponentData();
		data.comp = pane;
		data.image = btnImage;
		data.delete = btnDelete;

		return data;
	}

	private boolean isWeighted() {
		for (SpriteSet set : tile.fg) {
			if (set.weight != null) {
				return true;
			}
		}
		for (SpriteSet set : tile.bg) {
			if (set.weight != null) {
				return true;
			}
		}
		return false;
	}

	private JPanel createBodyPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		boolean weighted = isWeighted();
		cboxWeighted = new JCheckBox("Use weighted layout?");
		cboxWeighted.setSelected(weighted);
		cboxWeighted.setEnabled(false);
		panel.add(cboxWeighted, BorderLayout.NORTH);
		spritePanel = createSpritePanel(weighted);
		panel.add(spritePanel, BorderLayout.CENTER);

		return panel;
	}

	private void refreshSpritePanels() {
		JPanel nSpritePanels = createSpritePanel(cboxWeighted.isSelected());
		bodyPanel.setVisible(false);
		bodyPanel.remove(spritePanel);
		spritePanel = nSpritePanels;
		bodyPanel.add(spritePanel, BorderLayout.CENTER);
		bodyPanel.setVisible(true);
	}

	private JPanel createSpritePanel(boolean weighted) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		if (weighted) {
			generateWeightedPanels(panel);
		} else {
			generateUnweightedPanels(panel);
		}
		return panel;
	}

	private void generateUnweightedComponents(JPanel panel, Set<SpriteSet> comp) {
		for (SpriteSet set : comp) {
			for (int id : set.ids) {
				SpriteComponentData data = createSpriteComponent(id);
				panel.add(data.comp);
				// Create delete action
				data.delete.addActionListener(e -> {
					panel.remove(data.comp);
					panel.invalidate();
					panel.revalidate();

					set.ids.remove((Integer) id);

					if (set.ids.size() == 0) {
						comp.remove(set);
					}
				});
				// Create image select action
				data.image.addActionListener(e -> {
					Window win = SwingUtilities.getWindowAncestor(this);
					int nId = SpriteDialog.selectSprite(win, main, id);
					if (nId >= 0 && !set.ids.contains(nId)) {
						int index = set.ids.indexOf(id);
						set.ids.set(index, nId);
						refreshSpritePanels();
					}
				});
			}
		}
		// add "ADD button"
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> {
			SpriteSet set = new SpriteSet(main.tileset.getGlobalIdForLocalId(main.sheet, 0));
			comp.add(set);
			refreshSpritePanels();
		});
		panel.add(btnAdd);
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
			spritePanel.setBorder(BorderFactory.createTitledBorder("Weighted Sprite Set"));
			int weight = -1;
			if (set.weight != null) {
				weight = set.weight;
			}
			JTextField txtWeight = new JTextField(String.valueOf(weight));
			txtWeight.setFont(AppToolkit.getBestMonospaceFont(txtWeight.getFont().getSize2D()));
			txtWeight.setColumns(3);
			PlainDocument docWeight = (PlainDocument) txtWeight.getDocument();
			docWeight.setDocumentFilter(new IntegerDocumentFilter(true));
			txtWeight.addCaretListener(e -> {
				// remove anything non-numeric
				int newWeight = -1;
				try {
					newWeight = Integer.parseInt(txtWeight.getText());
				} catch (Exception ex) {

				}
				if (newWeight < -1) {
					newWeight = -1;
				}

				set.weight = newWeight;
			});
			spritePanel.add(new JLabel("Weight "));
			spritePanel.add(txtWeight);

			for (int id : set.ids) {
				SpriteComponentData data = createSpriteComponent(id);
				spritePanel.add(data.comp);
				// Create delete action
				data.delete.addActionListener(e -> {
					spritePanel.remove(data.comp);
					spritePanel.invalidate();
					spritePanel.revalidate();

					set.ids.remove(id);

					if (set.ids.size() == 0) {
						comp.remove(set);
						panel.remove(spritePanel);
					}
				});
				// Create image select action
				data.image.addActionListener(e -> {
					Window win = SwingUtilities.getWindowAncestor(this);
					int nId = SpriteDialog.selectSprite(win, main, id);
					if (nId >= 0 && !set.ids.contains(nId)) {
						int index = set.ids.indexOf(id);
						set.ids.set(index, nId);
						refreshSpritePanels();
					}
				});
			}

			panel.add(spritePanel);
		}
	}

	private void generateWeightedPanels(JPanel panel) {
		JPanel spritePanel = new JPanel();
		spritePanel.setBorder(BorderFactory.createTitledBorder("Foreground"));
		spritePanel.setLayout(new BoxLayout(spritePanel, BoxLayout.Y_AXIS));
		generateWeightedComponents(spritePanel, tile.fg);
		panel.add(spritePanel);

		spritePanel = new JPanel();
		spritePanel.setBorder(BorderFactory.createTitledBorder("Background"));
		spritePanel.setLayout(new BoxLayout(spritePanel, BoxLayout.Y_AXIS));
		generateWeightedComponents(spritePanel, tile.bg);
		panel.add(spritePanel);
	}
}
