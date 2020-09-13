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
import java.awt.Color;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RepaintManager;

import org.csdgn.maru.swing.ArrayListModel;
import org.csdgn.maru.swing.ImageListCellRenderer;

public class SpriteDialog extends JDialog {
	private static final long serialVersionUID = 1295779393623512757L;
	private MainPanel main;
	private String sheet;
	private int spriteId;
	private JList<BufferedImage> spriteList;

	public SpriteDialog(Window win, MainPanel main, int spriteId) {
		super(win);
		this.main = main;
		this.spriteId = spriteId;
		
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setContentPane(createContentPane());
		setSize(480, 320);
		setLocationRelativeTo(win);
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(Box.createHorizontalGlue());

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			spriteId = -1;
			cancel.setVisible(false);
			dispose();
		});
		panel.add(cancel);
		panel.add(Box.createHorizontalStrut(4));

		JButton ok = new JButton("OK");
		ok.addActionListener(e -> {
			cancel.setVisible(false);
			dispose();
		});
		panel.add(ok);

		return panel;
	}

	private JPanel createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		panel.add(createTopPanel(), BorderLayout.NORTH);

		int oldIndex = spriteId;
		
		spriteList = new JList<BufferedImage>();
		spriteList.setVisibleRowCount(0);
		spriteList.setCellRenderer(new ImageListCellRenderer());
		spriteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		spriteList.setSelectionBackground(Color.MAGENTA);
		spriteList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		spriteList.addListSelectionListener(e -> {
			int id = spriteList.getSelectedIndex();
			if (id >= 0) {
				spriteId = main.tileset.getGlobalIdForLocalId(sheet, id);
			}
		});
		
		JScrollPane spriteScrollPane = new JScrollPane(spriteList);
		panel.add(spriteScrollPane, BorderLayout.CENTER);
		updateList();
		spriteId = oldIndex;
		selectSpriteByGlobalIndex(spriteId);

		panel.add(createBottomPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(new JLabel("Current Sheet"));
		panel.add(Box.createHorizontalStrut(4));

		JComboBox<String> box = new JComboBox<String>();

		for (String name : main.tileset.getSheetNames()) {
			if (!name.contains("fallback")) {
				box.addItem(name);
			}

		}
		box.addActionListener(e -> {
			sheet = (String) box.getSelectedItem();
			updateList();
		});
		sheet = (String) box.getSelectedItem();
		panel.add(box);

		return panel;
	}

	private void selectSpriteByGlobalIndex(int spriteIndex) {
		int index = main.tileset.getLocalIdForGlobalId(spriteIndex);
		spriteList.setSelectedIndex(index);
	}

	public int getSpriteId() {
		return spriteId;
	}

	private void updateList() {
		ArrayListModel<BufferedImage> listModel = new ArrayListModel<BufferedImage>(
				main.tileset.subsets.get(sheet).sprites);

		spriteList.setModel(listModel);
		spriteList.setSelectedIndex(0);
		spriteList.clearSelection();
	}

	public static int selectSprite(Window win, MainPanel main, int spriteId) {
		SpriteDialog diag = new SpriteDialog(win, main, spriteId);
		diag.setVisible(true);
		return diag.getSpriteId();
	}
}
