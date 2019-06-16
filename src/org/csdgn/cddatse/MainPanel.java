package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.csdgn.cddatse.data.ImageTile;
import org.csdgn.cddatse.data.Tile;
import org.csdgn.cddatse.data.Tileset;
import org.csdgn.maru.swing.MultilineCellRenderer;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = -4837947074774842661L;

	private JScrollPane centerPane;
	private MainFrame frame;
	private DefaultListModel<ImageTile> model;
	private String sheet;
	private Tileset tileset;

	public MainPanel(MainFrame frame, Tileset tileset) {
		this.frame = frame;
		this.tileset = tileset;

		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout());

		add(createUpperPanel(), BorderLayout.NORTH);
		add(createSidePanel(), BorderLayout.WEST);

		centerPane = new JScrollPane();
		add(centerPane, BorderLayout.CENTER);

		setCenterPanel(new JPanel());
	}

	private JPanel createSidePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		model = new DefaultListModel<ImageTile>();
		updateList();

		JList<ImageTile> list = new JList<ImageTile>(model);
		// yeah for some reason this is required to have any kind of background
		Color bg = new Color(list.getSelectionBackground().getRGB() & 0xFFFFFF);
		list.setSelectionBackground(bg);
		list.setCellRenderer(new MultilineCellRenderer<ImageTile>());
		list.addListSelectionListener(e -> {
			ImageTile tile = list.getSelectedValue();
			if (tile != null) {
				setCenterPanel(new ImageTilePanel(this, tile));
			}
		});

		JScrollPane scroll = new JScrollPane(list);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroll, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createUpperPanel() {
		// more of a toolbar probably, but for now, lets ignore it
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.add(new JLabel("Current Sheet"));
		panel.add(Box.createHorizontalStrut(4));

		JComboBox<String> box = new JComboBox<String>();
		for (String name : tileset.getSheetNames()) {
			box.addItem(name);
		}
		box.addActionListener(e -> {
			sheet = (String) box.getSelectedItem();
			updateList();
		});
		panel.add(box);
		panel.add(Box.createHorizontalStrut(8));

		panel.add(new JLabel("Display Name"));
		panel.add(Box.createHorizontalStrut(4));

		JTextField display = new JTextField();
		display.setText(tileset.view);
		display.addCaretListener(e -> {
			tileset.view = display.getText();
			System.out.println(tileset.view);
			frame.updateTitle();
		});
		panel.add(display);
		panel.add(Box.createHorizontalStrut(8));

		panel.add(new JLabel("Internal Name"));
		panel.add(Box.createHorizontalStrut(4));

		JTextField internal = new JTextField();
		internal.setText(tileset.name);
		internal.addCaretListener(e -> {
			tileset.name = display.getText();
		});
		panel.add(internal);

		sheet = (String) box.getSelectedItem();

		return panel;
	}

	private void setCenterPanel(JComponent panel) {
		centerPane.setViewportView(panel);
		centerPane.invalidate();
		centerPane.revalidate();
	}

	private void updateList() {
		model.removeAllElements();
		for (Tile tile : tileset.byFile.get(sheet)) {
			if (tile instanceof ImageTile) {
				ImageTile it = (ImageTile) tile;
				model.addElement(it);
			}
		}
	}

	protected void updateTile(ImageTile tile) {
		// not exactly efficient but w/e, fix it if too slow
		model.set(model.indexOf(tile), tile);
	}
}
