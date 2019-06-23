package org.csdgn.cddatse;

import java.awt.BorderLayout;
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

import org.csdgn.maru.swing.ArrayListModel;
import org.csdgn.maru.swing.ImageListCellRenderer;

public class SpriteDialog extends JDialog {
	private static final long serialVersionUID = 1295779393623512757L;
	private MainPanel main;
	private String sheet;
	private int spriteId;
	private JList<BufferedImage> spriteList;

	public SpriteDialog(Window win, MainPanel main, int spriteId) {
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
		int index = main.tileset.getLocalIdForGlobalId(oldIndex);
		spriteList.setSelectedIndex(index);
		
		spriteId = oldIndex;

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
}
