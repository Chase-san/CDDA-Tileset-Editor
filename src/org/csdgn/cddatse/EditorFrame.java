package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.csdgn.cddatse.data.AsciiEntry;
import org.csdgn.cddatse.data.BaseTile;
import org.csdgn.cddatse.data.GFX;
import org.csdgn.cddatse.data.InternalTile;
import org.csdgn.cddatse.data.TileInfo;
import org.csdgn.maru.io.EndsWithFileFilter;
import org.csdgn.maru.io.EqualsFileFilter;
import org.csdgn.maru.swing.ArrayListModel;
import org.csdgn.maru.util.Tuple;

public class EditorFrame extends JFrame {
	private static class InternalTileCellRenderer extends JLabel implements ListCellRenderer<InternalTile> {
		private static final long serialVersionUID = 468626665326983329L;

		public InternalTileCellRenderer() {
			setOpaque(true);
			setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends InternalTile> list, InternalTile value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			if(!list.isDisplayable()) {
				setBackground(UIManager.getColor("List.background"));
				setForeground(UIManager.getColor("List.disabledForeground"));
				return this;
			}
			if(isSelected) {
				setBackground(UIManager.getColor("List.selectionBackground"));
				setForeground(UIManager.getColor("List.selectionForeground"));
			} else {
				setBackground(UIManager.getColor("List.background"));
				if(value.isImageless()) {
					setForeground(Color.decode("#AA0000"));
				} else {
					setForeground(UIManager.getColor("List.foreground"));
				}
			}
			return this;
		}
	}

	private static final long serialVersionUID = -4047187015916073121L;
	private AboutDialog about = new AboutDialog();
	private JPanel blankPanel = new JPanel();
	private JButton btnAdd;
	private JButton btnSort;
	private JButton btnSub;
	private JPanel contentPane;
	private CreateDialog create;
	private JList<InternalTile> list;
	private ArrayListModel<InternalTile> listModel;
	private JPanel listPanel;
	private MergeConflictDialog mcDialog;
	private JMenuItem miAddMissingTiles;
	private JMenuItem miCombineIdenticalImages;

	private JMenuItem miGenerateAsciiTiles;
	private JMenuItem miGenerateAsciiTilesBG;
	private JMenuItem miImport;
	private JMenuItem miOptimize;
	private JMenuItem miSave;

	private JMenuItem miSaveAs;
	private JMenuItem mntmOptions;

	private HashMap<String, Integer> nameMap = new HashMap<String, Integer>();
	private boolean opened = false;
	private OptionsDialog options;
	private File outputFolder;
	private JFileChooser saveChooser = null;
	private boolean savedBefore = false;
	private boolean searching = false;
	private ArrayListModel<InternalTile> searchListModel;

	private String searchString = "";
	private TilePanel tilePanel;
	private JScrollPane tileScrollPane;
	private JFileChooser tilesetChooser = null;

	private JTextField txtSearch;

	/**
	 * Create the frame.
	 */
	public EditorFrame() {
		setTitle(Version.getVersionString());
		setIconImages(AppToolkit.getAppIconImages());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 629, 382);

		setJMenuBar(createMenu());

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				doPostShow();
				removeComponentListener(this);
			}
		});

		create = new CreateDialog(this);
		mcDialog = new MergeConflictDialog(this);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);
		contentPane.add(splitPane, BorderLayout.CENTER);

		listPanel = new JPanel();
		listPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		splitPane.setLeftComponent(listPanel);
		listPanel.setLayout(new BorderLayout(5, 2));

		listModel = new ArrayListModel<InternalTile>();
		searchListModel = new ArrayListModel<InternalTile>();
		list = new JList<InternalTile>(listModel);
		list.setCellRenderer(new InternalTileCellRenderer());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				onListSelected(list.getSelectedIndex());
			}
		});

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(140, 130));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		listPanel.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		btnAdd = new JButton("+");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listModel.add(new InternalTile());
				int index = listModel.getSize() - 1;
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		});
		buttonPanel.add(btnAdd);

		btnSub = new JButton("-");
		btnSub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] data = list.getSelectedIndices();

				ArrayList<InternalTile> dalist = listModel.getList();
				if(searching) {
					for(int i = data.length - 1; i >= 0; --i) {
						InternalTile tile = searchListModel.get(data[i]);
						decNameMap(tile.id);
						dalist.remove(tile);
					}
					listModel.fireUpdate();
					doSearch(searchString);
				} else {
					for(int i = data.length - 1; i >= 0; --i) {
						decNameMap(dalist.get(data[i]).id);
						dalist.remove(data[i]);
					}
					listModel.fireUpdate();
				}

			}
		});

		buttonPanel.add(btnSub);

		btnSort = new JButton("Sort");
		btnSort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sortList();
			}
		});
		buttonPanel.add(btnSort);

		JPanel searchPanel = new JPanel();
		listPanel.add(searchPanel, BorderLayout.SOUTH);
		searchPanel.setLayout(new BorderLayout(5, 2));

		JLabel lblSearch = new JLabel("Search:");
		searchPanel.add(lblSearch, BorderLayout.WEST);

		txtSearch = new JTextField();
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearch(txtSearch.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearch(txtSearch.getText());
			}
		});
		searchPanel.add(txtSearch);
		txtSearch.setColumns(10);

		tileScrollPane = new JScrollPane();
		tileScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tileScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(tileScrollPane);

		tileScrollPane.setViewportView(blankPanel);
		tilePanel = new TilePanel(this);

		disableInterface();
	}

	private void browseForSaveLocation() {
		if(saveChooser == null) {
			AppToolkit.setFileChooserReadOnly(false);
			saveChooser = new JFileChooser();
			saveChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		saveChooser.setCurrentDirectory(Options.lastBrowsedDirectory);

		if(saveChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		Options.lastBrowsedDirectory = saveChooser.getCurrentDirectory();

		File file = saveChooser.getSelectedFile();
		if(!file.exists()) {
			return;
		}

		if(!file.isDirectory()) {
			file = file.getParentFile();
		}

		outputFolder = file;
	}

	private File browseForTileset() {
		if(tilesetChooser == null) {
			AppToolkit.setFileChooserReadOnly(true);
			tilesetChooser = new JFileChooser();
			FileFilter filter = new EqualsFileFilter("tileset.txt", "Tileset Definition");
			tilesetChooser.addChoosableFileFilter(filter);
			tilesetChooser.setFileFilter(filter);
		}

		tilesetChooser.setCurrentDirectory(Options.lastBrowsedDirectory);

		if(tilesetChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		Options.lastBrowsedDirectory = tilesetChooser.getCurrentDirectory();

		return tilesetChooser.getSelectedFile();
	}

	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);

		JMenuItem miNew = new JMenuItem("New");
		miNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(create.showDialog() == CreateDialog.OPTION_CREATE) {
					reset();
					if(GFX.instance != null) {
						GFX.instance.dispose();
					}
					GFX.instance = create.createGFX();
					enableInterface();
				}
			}
		});
		miNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		miNew.setMnemonic(KeyEvent.VK_N);
		mnFile.add(miNew);

		JMenuItem miOpen = new JMenuItem("Open");
		miOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOpen();
			}
		});
		miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		miOpen.setMnemonic(KeyEvent.VK_O);
		mnFile.add(miOpen);

		mnFile.addSeparator();

		miImport = new JMenuItem("Import...");
		miImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doImport();
			}
		});
		miImport.setMnemonic(KeyEvent.VK_I);
		mnFile.add(miImport);
		mnFile.addSeparator();

		miSave = new JMenuItem("Save");
		miSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		miSave.setMnemonic(KeyEvent.VK_S);
		mnFile.add(miSave);

		miSaveAs = new JMenuItem("Save As");
		miSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				browseForSaveLocation();
				opened = false;
				doSave();
			}
		});
		miSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		miSaveAs.setMnemonic(KeyEvent.VK_A);
		mnFile.add(miSaveAs);

		mnFile.addSeparator();

		JMenuItem miExit = new JMenuItem("Exit");
		miExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		miExit.setMnemonic(KeyEvent.VK_X);
		mnFile.add(miExit);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(mnEdit);

		miOptimize = new JMenuItem("Optimize");
		miOptimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		miOptimize.setMnemonic(KeyEvent.VK_P);
		miOptimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOptimize();
			}
		});

		mnEdit.add(miOptimize);

		miCombineIdenticalImages = new JMenuItem("Merge identical tiles");
		miCombineIdenticalImages.setMnemonic(KeyEvent.VK_M);
		miCombineIdenticalImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doMergeTiles();
			}
		});
		mnEdit.add(miCombineIdenticalImages);

		miAddMissingTiles = new JMenuItem("Add missing tiles");
		miAddMissingTiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAddMissingIds();
			}
		});

		miAddMissingTiles.setMnemonic(KeyEvent.VK_A);
		mnEdit.add(miAddMissingTiles);

		mnEdit.addSeparator();

		miGenerateAsciiTiles = new JMenuItem("Generate ascii tiles");
		miGenerateAsciiTiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doGenerateAsciiTiles(false);
			}
		});
		miGenerateAsciiTiles.setToolTipText("Generates ascii tiles for entries that have no tiles at all. NOT PERFECT!");
		mnEdit.add(miGenerateAsciiTiles);

		miGenerateAsciiTilesBG = new JMenuItem("Generate ascii tiles /w bg");
		miGenerateAsciiTilesBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doGenerateAsciiTiles(true);
			}
		});
		miGenerateAsciiTilesBG
				.setToolTipText("Generates ascii tiles for entries that have no tiles at all. Generates a black background for these tiles as well. NOT PERFECT!");

		mnEdit.add(miGenerateAsciiTilesBG);

		mnEdit.addSeparator();

		mntmOptions = new JMenuItem("Options");
		mntmOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doShowOptions();
			}
		});

		mnEdit.add(mntmOptions);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);

		JMenuItem miAbout = new JMenuItem("About");
		miAbout.setMnemonic(KeyEvent.VK_A);
		miAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doShowAbout();
			}
		});
		mnHelp.add(miAbout);

		return menuBar;
	}

	private int decNameMap(String id) {
		Integer count = nameMap.get(id);
		if(count == null) {
			return 0;
		}
		--count;
		if(count <= 0) {
			nameMap.remove(id);
			return 0;
		}
		nameMap.put(id, count);
		return count;
	}

	private void disableInterface() {
		btnAdd.setEnabled(false);
		btnSub.setEnabled(false);
		btnSort.setEnabled(false);
		txtSearch.setEnabled(false);
		list.setEnabled(false);
		miOptimize.setEnabled(false);
		miImport.setEnabled(false);
		miSave.setEnabled(false);
		miSaveAs.setEnabled(false);
		miCombineIdenticalImages.setEnabled(false);
		miAddMissingTiles.setEnabled(false);
		miGenerateAsciiTiles.setEnabled(false);
		miGenerateAsciiTilesBG.setEnabled(false);
	}

	@Override
	public void dispose() {
		// TODO WARNING ARE YOU SURE YOU WANT TO CLOSE WITHOUT SAVING?
		Options.save();
		super.dispose();
	}

	private void doAddMissingIds() {
		HashMap<String, AsciiEntry> entries = getAsciiEntries();

		ArrayList<InternalTile> list = listModel.getList();

		for(InternalTile tile : list) {
			entries.remove(tile.id);
		}

		for(AsciiEntry e : entries.values()) {
			InternalTile tile = new InternalTile();
			tile.id = e.id;
			incNameMap(e.id);
			list.add(tile);
		}

		listModel.fireUpdate();
	}

	private void doGenerateAsciiTiles(boolean withBG) {
		HashMap<String, AsciiEntry> entries = getAsciiEntries();

		ArrayList<InternalTile> list = listModel.getList();

		GFX gfx = GFX.instance;

		BufferedImage tilesToUse = getAsciiTileset();
		TileInfo info = GFX.instance.getTileInfo();

		if(tilesToUse == null) {
			int dialogBtn = JOptionPane.YES_NO_OPTION;
			int tileCheckDlg = JOptionPane.showConfirmDialog(null,
					"No tilepage selected - would you like to generate font-based ASCII tiles instead?", "Continue anyway?", dialogBtn);
			if(tileCheckDlg == JOptionPane.NO_OPTION) {
				return;
			}
		} else {
			if(tilesToUse.getWidth() != info.width * 16 || tilesToUse.getHeight() != info.height * 16) {
				AppToolkit.showError(this, "Tilepage dimensions do not match loaded tileset! Must be " + String.valueOf(info.width * 16)
						+ "x" + String.valueOf(info.height * 16) + " pixels.");
				return;
			}
		}

		BufferedImage bg = new BufferedImage(info.width, info.height, BufferedImage.TYPE_INT_RGB);

		if(withBG) {
			Graphics2D gx = bg.createGraphics();
			gx.setColor(Color.BLACK);
			gx.fillRect(0, 0, info.width, info.height);
			gx.dispose();

			gfx.images.add(bg);
		}

		for(InternalTile tile : list) {
			if(tile.isImageless()) {
				AsciiEntry e = entries.get(tile.id);
				if(e != null) {
					BufferedImage img = e.createAsciiTile(info.width, info.height, tilesToUse);

					gfx.images.add(img);
					tile.image.first = img;
					if(withBG && !e.isOverlay()) {
						tile.image.second = bg;
					}
					if(e.isMultitile()) {
						// let'sa go!
						// center
						// JOptionPane.showMessageDialog(null,e.id);
						img = e.createAsciiTile(info.width, info.height, 197, tilesToUse);
						gfx.images.add(img);
						tile.center = true;
						tile.centerImage.first = img;
						if(withBG) {
							tile.centerImage.second = bg;
						}
						// corner
						img = e.createAsciiTile(info.width, info.height, 218, tilesToUse);
						gfx.images.add(img);
						tile.corner = true;
						tile.cornerImage.first = img;
						if(withBG) {
							tile.cornerImage.second = bg;
						}
						// edge
						img = e.createAsciiTile(info.width, info.height, 179, tilesToUse);
						gfx.images.add(img);
						tile.edge = true;
						tile.edgeImage.first = img;
						if(withBG) {
							tile.edgeImage.second = bg;
						}
						// tConnection
						img = e.createAsciiTile(info.width, info.height, 194, tilesToUse);
						gfx.images.add(img);
						tile.tConnection = true;
						tile.tConnectionImage.first = img;
						if(withBG) {
							tile.tConnectionImage.second = bg;
						}
						// end_piece
						img = e.createAsciiTile(info.width, info.height, 179, tilesToUse);
						gfx.images.add(img);
						tile.endPiece = true;
						tile.endPieceImage.first = img;
						if(withBG) {
							tile.endPieceImage.second = bg;
						}
						// unconnected
						img = e.createAsciiTile(info.width, info.height, 254, tilesToUse);
						gfx.images.add(img);
						tile.unconnected = true;
						tile.unconnectedImage.first = img;
						if(withBG) {
							tile.unconnectedImage.second = bg;
						}

					}
					if(e.id.startsWith("vp_")) {
						tile.rotates = true;
					}
					if(e.hasBrokenTile()) {
						img = e.createBrokenAsciiTile(info.width, info.height, tilesToUse);
						gfx.images.add(img);
						tile.broken = true;
						tile.brokenImage.first = img;
						if(withBG) {
							tile.brokenImage.second = bg;
						}
					}
				}
			} else if(entries.get(tile.id) == null) {
				System.out.println("Nothing for '" + tile.id + "'.");
			}
		}

		listModel.fireUpdate();
	}

	private void doImport() {
		File file = browseForTileset();

		if(file == null) {
			return;
		}

		if(!file.exists()) {
			AppToolkit.showError(this, "File not found!");
			return;
		}

		GFX theirs = GFX.load(file);

		if(theirs == null) {
			AppToolkit.showError(this, "Failed to load Tileset!");
			return;
		}

		GFX ours = GFX.instance;

		if(theirs.getTileInfo().width != ours.getTileInfo().width || theirs.getTileInfo().height != ours.getTileInfo().height) {
			AppToolkit.showError(this, "Targeted tileset does not have the same tilesize!");
			return;
		}

		// check for duplicate id's
		ArrayList<Tuple<BaseTile>> duplicates = new ArrayList<Tuple<BaseTile>>();

		// first is ours, second is theirs
		// for every entry, check every entry... >.<
		HashMap<String, BaseTile> map = new HashMap<String, BaseTile>();

		updateGFXTiles();

		for(BaseTile tile : ours.tileset.tiles) {
			map.put(tile.id, tile);
		}

		for(BaseTile tile : theirs.tileset.tiles) {
			if(map.containsKey(tile.id)) {
				// add it to the duplicate list
				duplicates.add(new Tuple<BaseTile>(map.get(tile.id), tile));
			} else {
				// just add the tile if we don't already have it
				ours.addTile(theirs, tile);
			}
		}

		map.clear();

		if(duplicates.size() > 0) {
			int option = mcDialog.showDialog();
			switch(option) {
			case MergeConflictDialog.OPTION_OURS:
				// we're done!
				break;
			case MergeConflictDialog.OPTION_THEIRS:
				// for every item in the duplicate map
				for(Tuple<BaseTile> dup : duplicates) {
					ours.replaceTile(dup.first, theirs, dup.second);
				}
				break;
			case MergeConflictDialog.OPTION_MANUAL:
				// TODO
				break;
			}
		}

		updateInternalTiles();

	}

	private void doMergeTiles() {
		if(AppToolkit.confirmWarning(this, "Merging tiles is a time consuming process, are you sure you want to continue?") != JOptionPane.YES_OPTION) {
			return;
		}

		disableInterface();
		updateGFXTiles();

		final ProgressDialog dialog = new ProgressDialog(this);

		dialog.execute(new Runnable() {

			@Override
			public void run() {
				// build a list of identical tiles
				HashMap<Integer, Integer> shiftMap = new HashMap<Integer, Integer>();
				ArrayList<BufferedImage> ref = GFX.instance.images;

				dialog.setLabelText("Locating duplicate images.");
				dialog.setProgressMaximum(ref.size());
				for(int i = 0; i < ref.size() - 1; ++i) {
					if(shiftMap.containsKey(i)) {
						continue;
					}
					for(int j = i + 1; j < ref.size(); ++j) {
						if(AppToolkit.isEqual(ref.get(i), ref.get(j))) {
							shiftMap.put(j, i);
						}
					}
					dialog.setProgress(i);
				}

				// update indexes
				dialog.setLabelText("Updating tile indexes (1st pass).");
				dialog.setProgress(0);
				dialog.setProgressMaximum(1);

				for(BaseTile tile : GFX.instance.tileset.tiles) {
					tile.replaceIndexes(shiftMap);
				}
				dialog.setProgress(1);

				// rebuild tileset, removing duplicates
				dialog.setLabelText("Rebuilding tileset.");
				dialog.setProgress(0);
				dialog.setProgressMaximum(1);

				HashSet<Integer> dupSet = new HashSet<Integer>(shiftMap.keySet());
				shiftMap.clear();

				ArrayList<BufferedImage> keep = new ArrayList<BufferedImage>();
				for(int i = 0; i < ref.size(); ++i) {
					int index = keep.size();

					if(!dupSet.contains(i)) {
						keep.add(ref.get(i));
					}
					shiftMap.put(i, index);
				}
				dialog.setProgress(1);

				ref.clear();
				GFX.instance.images = keep;

				// update indexes again for the rebuilt tileset
				dialog.setLabelText("Updating tile indexes (2nd pass).");
				dialog.setProgress(0);
				dialog.setProgressMaximum(1);
				for(BaseTile tile : GFX.instance.tileset.tiles) {
					tile.replaceIndexes(shiftMap);
				}
				dialog.setProgress(1);

				dialog.setVisible(false);
			}

		});

		updateInternalTiles();
		enableInterface();
	}

	private void doOpen() {
		File file = browseForTileset();

		if(file == null) {
			return;
		}

		if(!file.exists()) {
			AppToolkit.showError(this, "File not found!");
			return;
		}

		GFX tmp = GFX.load(file);

		if(tmp == null) {
			AppToolkit.showError(this, "Failed to load Tileset!");
			return;
		}

		reset();

		GFX.instance = tmp;

		opened = true;
		outputFolder = file.getParentFile();

		updateInternalTiles();

		updateIDList();

		enableInterface();
	}

	private void doOptimize() {
		updateGFXTiles();
		GFX.instance.optimize();
		updateInternalTiles();
	}

	private void doPostShow() {
		if(Options.cataclysmDirectory == null) {
			doShowOptions();
		}
	}

	private void doSave() {
		if(opened && !savedBefore) {
			if(JOptionPane.showConfirmDialog(this, "Are you sure you want to overwrite?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}
		}

		if(outputFolder == null) {
			browseForSaveLocation();
			if(outputFolder == null) {
				return;
			}
		}

		updateGFXTiles();

		try {
			GFX.instance.save(outputFolder);
		} catch(IOException e) {
			e.printStackTrace();
		}

		savedBefore = true;
	}

	private void doSearch(String search) {
		int minIndex = list.getMinSelectionIndex();
		InternalTile lastValue = null;
		if(minIndex >= 0 && minIndex < list.getModel().getSize()) {
			lastValue = list.getModel().getElementAt(minIndex);
		}

		if(search.length() == 0) {
			// no search
			if(searching) {
				searching = false;
				list.setModel(listModel);
				list.setSelectedValue(lastValue, true);
				// enable Add and Sort
				btnAdd.setEnabled(true);
				btnSort.setEnabled(true);
			}
		} else {
			if(!searching) {
				searching = true;
				list.setModel(searchListModel);
				// disable Add and Sort
				btnAdd.setEnabled(false);
				btnSort.setEnabled(false);
			}

			ArrayListModel<InternalTile> model = listModel;

			// search the old searchList instead (small optimization)
			if(searchString.length() > 0 && !searchString.equals(search) && search.contains(searchString)) {
				model = searchListModel;
			}

			// TODO create a hashmap for every character and combine a list of
			// those and then search on that list
			// since these lists can get pretty long otherwise

			ArrayList<InternalTile> tiles = new ArrayList<InternalTile>();
			for(InternalTile tile : model.getList()) {
				if(tile.id.contains(search)) {
					tiles.add(tile);
				}
			}

			searchListModel.getList().clear();
			searchListModel.getList().addAll(tiles);
			searchListModel.fireUpdate();

			// check if list contains lastValue
			if(lastValue != null && tiles.contains(lastValue)) {
				list.setSelectedValue(lastValue, true);
			}
			// TODO otherwise deselect/select first
		}

		searchString = search;
	}

	private void doShowAbout() {
		about.showAboutDialog(this);
	}

	private void doShowOptions() {
		if(options == null) {
			options = new OptionsDialog(this);
		}
		options.setLocationRelativeTo(this);
		options.setVisible(true);

		if(Options.cataclysmDirectory != null) {
			miAddMissingTiles.setEnabled(true);
		} else {
			miAddMissingTiles.setEnabled(false);
		}
	}

	private void enableInterface() {
		btnAdd.setEnabled(true);
		btnSub.setEnabled(true);
		btnSort.setEnabled(true);
		txtSearch.setEnabled(true);
		list.setEnabled(true);
		miOptimize.setEnabled(true);
		miImport.setEnabled(true);
		miSave.setEnabled(true);
		miSaveAs.setEnabled(true);
		miCombineIdenticalImages.setEnabled(true);

		if(Options.cataclysmDirectory != null) {
			miAddMissingTiles.setEnabled(true);
			miGenerateAsciiTiles.setEnabled(true);
			miGenerateAsciiTilesBG.setEnabled(true);
		}
	}

	private HashMap<String, AsciiEntry> getAsciiEntries() {
		File dataFolder = new File(Options.cataclysmDirectory, "data");
		File jsonFolder = new File(dataFolder, "json");

		HashMap<String, AsciiEntry> entries = new HashMap<String, AsciiEntry>();

		AsciiEntry.getAllAsciiTiles(jsonFolder, entries);
		return entries;
	}

	private BufferedImage getAsciiTileset() {
		JFileChooser imageChooser = null;
		AppToolkit.setFileChooserReadOnly(true);
		imageChooser = new JFileChooser();
		FileFilter filter = new EndsWithFileFilter("Image File", ".png", ".jpg", ".gif", ".bmp");
		imageChooser.addChoosableFileFilter(filter);
		imageChooser.setFileFilter(filter);
		imageChooser.setDialogTitle("Select ASCII tile page:");

		imageChooser.setCurrentDirectory(Options.lastBrowsedDirectory);

		if(imageChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		Options.lastBrowsedDirectory = imageChooser.getCurrentDirectory();

		File file = imageChooser.getSelectedFile();
		if(!file.exists()) {
			return null;
		}

		try {
			return ImageIO.read(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int incNameMap(String id) {
		Integer count = nameMap.get(id);
		if(count == null) {
			count = 0;
		}
		++count;
		nameMap.put(id, count);
		return count;
	}

	private void onListSelected(int index) {
		ArrayListModel<InternalTile> model = listModel;
		if(searching) {
			model = searchListModel;
		}
		if(index >= 0) {
			tilePanel.setTilesetEntry(model.get(index));
			tileScrollPane.setViewportView(tilePanel);
		} else {
			tileScrollPane.setViewportView(blankPanel);
		}
	}

	private void reset() {
		if(GFX.instance != null) {
			GFX.instance.dispose();
		}
		GFX.instance = null;
		opened = false;
		savedBefore = false;
		outputFolder = null;

		listModel.getList().clear();
		listModel.fireUpdate();
		if(searching && txtSearch.isEnabled()) {
			txtSearch.setText("");
			doSearch("");
		}
	}

	private void sortList() {
		Collections.sort(listModel.getList(), new Comparator<InternalTile>() {
			@Override
			public int compare(InternalTile t1, InternalTile t2) {
				return t1.id.compareToIgnoreCase(t2.id);
			}
		});
		updateIDList();
	}

	private void updateGFXTiles() {
		GFX.instance.tileset.tiles.clear();
		for(InternalTile tile : listModel.getList()) {
			GFX.instance.tileset.tiles.add(tile.toBaseTile());
		}
	}

	protected void updateIDList() {
		list.repaint();
	}

	private void updateInternalTiles() {
		// convert basetiles to internal tiles
		ArrayList<InternalTile> list = listModel.getList();
		list.clear();

		nameMap.clear();
		for(BaseTile tile : GFX.instance.tileset.tiles) {
			// add these base tiles to list
			InternalTile it = new InternalTile(tile);
			incNameMap(it.id);
			list.add(it);
		}

		// add all sortList to listModel
		listModel.fireUpdate();
	}

	protected boolean updateTileId(String oldId, String newId) {
		if(oldId != null) {
			decNameMap(oldId);
			return incNameMap(newId) == 1;
		}
		Integer count = nameMap.get(newId);
		if(count == null || count <= 1) {
			return true;
		}

		return false;
	}
}