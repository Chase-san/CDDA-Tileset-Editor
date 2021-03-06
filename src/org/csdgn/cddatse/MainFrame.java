/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2020 Robert Maupin
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

import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.csdgn.cddatse.data.Tileset;
import org.csdgn.cddatse.data.TilesetStub;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2077970214609358246L;

	private JMenu openMenu;
	private int openMenuItemCount;
	private List<JComponent> disabledControls;
	private Tileset tileset;

	public MainFrame() {
		setTitle(Version.getVersionString());
		setIconImages(AppToolkit.getAppIconImages());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		setLocationByPlatform(true);

		disabledControls = new ArrayList<JComponent>();

		tileset = null;
		
		if(!Options.INSTANCE.load()) {
			//popup the set options box, so the game directory can be set
			EventQueue.invokeLater(() -> {
				showOptionsDialog();
			});
		}

		setJMenuBar(createMenuBar());
	}

	private void showOptionsDialog() {
		String oldPath = Options.INSTANCE.getGamePathString();
		OptionsDialog dialog = new OptionsDialog(this);
		dialog.setVisible(true);

		//refresh open menu file list if the game path has changed
		if(Options.INSTANCE.hasChanged() && !oldPath.equals(Options.INSTANCE.getGamePathString())) {
			refreshOpenMenuFileList();
		}
	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		JMenuItem item;

		item = new JMenuItem("New");
		item.setMnemonic(KeyEvent.VK_N);
		item.setEnabled(false);
		menu.add(item);

		menu.add(createOpenMenu());

		menu.addSeparator();

		item = new JMenuItem("Save");
		item.setMnemonic(KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		item.setEnabled(false);
		item.addActionListener(e -> {
			save();
		});
		disabledControls.add(item);
		menu.add(item);

		item = new JMenuItem("Save As");
		item.setMnemonic(KeyEvent.VK_A);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		item.setEnabled(false);
		//disabledControls.add(item);
		menu.add(item);

		menu.addSeparator();

		item = new JMenuItem("Close");
		item.setMnemonic(KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(e -> {
			closeFile();
		});
		item.setEnabled(false);
		disabledControls.add(item);
		menu.add(item);

		menu.addSeparator();

		item = new JMenuItem("Exit");
		item.setMnemonic(KeyEvent.VK_X);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		item.addActionListener((e) -> {
			dispose();
		});
		menu.add(item);

		return menu;
	}

	private JMenu createEditMenu() {
		JMenu menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_E);

		JMenuItem item;
		
		item = new JMenuItem("Sort");
		item.setMnemonic(KeyEvent.VK_S);
		item.setEnabled(false);
		item.addActionListener((e) -> {
			sortSheet();
		});
		disabledControls.add(item);
		menu.add(item);

		menu.addSeparator();

		item = new JMenuItem("Options");
		item.setMnemonic(KeyEvent.VK_P);
		item.addActionListener((e) -> {
			showOptionsDialog();
		});
		menu.add(item);

		return menu;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();

		menu.add(createFileMenu());
		menu.add(createEditMenu());

		return menu;
	}

	private JMenu createOpenMenu() {
		JMenu menu = new JMenu("Open");
		menu.setMnemonic(KeyEvent.VK_O);

		JMenuItem item;

		item = new JMenuItem("Open File");
		item.setMnemonic(KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		item.setEnabled(false);
		menu.add(item);

		item = new JMenuItem("Refresh List");
		item.setMnemonic(KeyEvent.VK_R);
		item.addActionListener(e -> {
			refreshOpenMenuFileList();
		});
		menu.add(item);

		menu.addSeparator();

		openMenu = menu;
		openMenuItemCount = menu.getMenuComponentCount();

		refreshOpenMenuFileList();		

		return menu;
	}

	public void refreshOpenMenuFileList() {
		while (openMenu.getMenuComponentCount() > openMenuItemCount) {
			openMenu.remove(openMenuItemCount);
		}
		if (Options.INSTANCE.gamePath != null) {
			loadStubs(openMenu, new File(Options.INSTANCE.gamePath, "gfx"));
		}
	}

	public void dispose() {
		if(tileset != null) {
			//TODO ask to save
			closeFile();
		}
		Options.INSTANCE.save();
		super.dispose();
	}

	private void enableControls(boolean enable) {
		for (JComponent comp : disabledControls) {
			comp.setEnabled(enable);
		}
	}

	private void sortSheet() {
		MainPanel mp = (MainPanel)getContentPane();
		mp.sortCurrentSheet();
	}
	
	private void closeFile() {
		if(tileset != null) {
			tileset.close();
		}
		tileset = null;
		updateTitle();
		setContentPane(new JPanel());
		enableControls(false);
		invalidate();
		revalidate();
	}

	private void openFile(TilesetStub stub) {
		tileset = new Tileset();
		tileset.load(stub);
		updateTitle();
		setContentPane(new MainPanel(this, tileset));
		enableControls(true);
		invalidate();
		revalidate();
	}

	private void loadStubs(JMenu menu, File file) {
		if (file == null) {
			return;
		}
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				loadStubs(menu, f);
			} else if (f.getName().endsWith("tileset.txt")) {
				TilesetStub stub = new TilesetStub();
				if (stub.load(f)) {
					JMenuItem item = new JMenuItem();
					item.setText(stub.view);
					item.addActionListener(e -> {
						openFile(stub);
					});
					menu.add(item);
				}
			}
		}
	}

	private void save() {
		tileset.save(tileset.file);
	}

	protected void updateTitle() {
		if (tileset == null) {
			setTitle(Version.getVersionString());
		} else {
			setTitle(Version.getVersionString() + " - " + tileset.view);
		}
	}
}
