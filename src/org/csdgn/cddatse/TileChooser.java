package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import org.csdgn.cddatse.data.GFX;
import org.csdgn.cddatse.data.TileInfo;
import org.csdgn.maru.io.EndsWithFileFilter;
import org.csdgn.maru.swing.ArrayListModel;

public class TileChooser extends JDialog {
	public class IconListRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -6530533873699384730L;
		public ImageIcon icon = new ImageIcon();

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText("");
			icon.setImage((BufferedImage) value);
			label.setIcon(icon);
			return label;
		}
	}

	public static final int OPTION_CANCEL = 0;
	public static final int OPTION_OK = 2;
	public static final int OPTION_REMOVE = 1;

	private JFileChooser imageChooser = null;

	private static final long serialVersionUID = -7024147904031885972L;

	private BufferedImage image = null;

	private ArrayListModel<BufferedImage> listModel;
	private boolean loadedImage = false;
	private ImageIcon preview = new ImageIcon();

	private int retVal = OPTION_CANCEL;
	private JList<BufferedImage> tileList;

	/**
	 * Create the dialog.
	 */
	public TileChooser(Window owner) {
		super(owner);
		setModal(true);
		setTitle("Tile Chooser");
		setBounds(100, 100, 450, 300);

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// just wrap the root image list
		listModel = new ArrayListModel<BufferedImage>(GFX.instance.images);

		contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);

		JPanel newTilePanel = new JPanel();
		contentPanel.add(newTilePanel, BorderLayout.NORTH);
		JPanel newTileCenterPanel = new JPanel();
		newTileCenterPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gbl_newTilePanel = new GridBagLayout();
		gbl_newTilePanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_newTilePanel.rowHeights = new int[] { 0, 0 };
		gbl_newTilePanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_newTilePanel.rowWeights = new double[] { 0.0, 0.0 };
		newTileCenterPanel.setLayout(gbl_newTilePanel);
		// add(btnFore, gbc_btnA);

		newTilePanel.setName("New Tile");
		newTilePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		newTilePanel.add(newTileCenterPanel);

		final JLabel btnPreview = new JLabel(preview = new ImageIcon());
		btnPreview.setMinimumSize(new Dimension(64, 64));
		btnPreview.setPreferredSize(new Dimension(64, 64));
		btnPreview.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnPreview.setAlignmentX(Component.CENTER_ALIGNMENT);

		GridBagConstraints gbc_btnPreview = new GridBagConstraints();
		gbc_btnPreview.gridheight = 2;
		gbc_btnPreview.fill = GridBagConstraints.BOTH;
		gbc_btnPreview.insets = new Insets(0, 0, 0, 5);
		gbc_btnPreview.gridx = 0;
		gbc_btnPreview.gridy = 0;
		newTileCenterPanel.add(btnPreview, gbc_btnPreview);

		JButton btnBrowse = new JButton("Browse for Image");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSingleTile();
			}
		});
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowse.gridx = 1;
		gbc_btnBrowse.gridy = 0;
		newTileCenterPanel.add(btnBrowse, gbc_btnBrowse);

		JLabel lblWarningTheImage = new JLabel("Warning: The image will be cropped if larger.");
		GridBagConstraints gbc_lblWarningTheImage = new GridBagConstraints();
		gbc_lblWarningTheImage.gridx = 1;
		gbc_lblWarningTheImage.gridy = 1;
		newTileCenterPanel.add(lblWarningTheImage, gbc_lblWarningTheImage);

		BorderLayout bl_existingTilePanel = new BorderLayout();
		bl_existingTilePanel.setVgap(5);
		bl_existingTilePanel.setHgap(5);
		JPanel existingTilePanel = new JPanel(bl_existingTilePanel);
		contentPanel.add(existingTilePanel, BorderLayout.CENTER);
		existingTilePanel.setName("Existing Tile");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		existingTilePanel.add(scrollPane, BorderLayout.CENTER);

		tileList = new JList<BufferedImage>(listModel);
		tileList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setSelectedImage(listModel.get(tileList.getSelectedIndex()));
			}
		});
		tileList.setVisibleRowCount(0);
		tileList.setCellRenderer(new IconListRenderer());
		tileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		scrollPane.setViewportView(tileList);

		JPanel panel = new JPanel();
		existingTilePanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JCheckBox chckbxHideUsedTiles = new JCheckBox("Hide used tiles");
		panel.add(chckbxHideUsedTiles, BorderLayout.WEST);
		chckbxHideUsedTiles.setEnabled(false);

		JButton btnImportTileset = new JButton("Import Tiles");
		btnImportTileset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openTileset();
			}
		});
		panel.add(btnImportTileset, BorderLayout.EAST);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));

		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				retVal = OPTION_REMOVE;
				setVisible(false);
			}
		});
		buttonPane.add(btnRemove);

		Component horizontalGlue = Box.createHorizontalGlue();
		buttonPane.add(horizontalGlue);
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				retVal = OPTION_OK;
				if (loadedImage) {
					// add image to gfx master image list
					GFX.instance.images.add(image);
				}
				setVisible(false);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				retVal = OPTION_CANCEL;
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		return buttonPane;
	}

	private void openTileset() {
		BufferedImage img = browseForImage();
		// cut up image
		if (img != null) {
			TileInfo info = GFX.instance.getTileInfo();
			int w = img.getWidth();
			int h = img.getHeight();
			if (w % info.width != 0) {
				AppToolkit.showError(this, "Image width must be a multiple of " + info.width + ".");
				return;
			}
			if (h % info.height != 0) {
				AppToolkit.showError(this, "Image height must be a multiple of " + info.height + ".");
				return;
			}

			// cut it up
			GFX.instance.images.addAll(GFX.cutImageIntoTiles(img, info.width, info.height));

			// reload listModel
			listModel.fireUpdate();

		} else {
			AppToolkit.showError(this, "Invalid Image!");
		}
	}
	
	private void setSelectedImage(BufferedImage img) {
		if(loadedImage) {
			image.flush();
			loadedImage = false;
		}
		image = img;
		preview.setImage(img);
		//tileList.ensureIndexIsVisible(index);
	}
	
	public void setExternalSelectedImage(BufferedImage img) {
		setSelectedImage(img);
		final int index = listModel.getList().indexOf(img);
		if(index != -1) {
			//should never be -1, but better safe...
			tileList.setSelectedIndex(index);
			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentShown(ComponentEvent e) {
					tileList.ensureIndexIsVisible(index);
					removeComponentListener(this);
				}
			});
		}
	}

	private void openSingleTile() {
		BufferedImage img = browseForImage();
		if (img != null) {
			tileList.clearSelection();
			setSelectedImage(img);
			//this must be set AFTER setSelectedImage
			loadedImage = true;
		} else {
			AppToolkit.showError(this, "Invalid Image!");
		}
	}

	private BufferedImage browseForImage() {
		if (imageChooser == null) {
			AppToolkit.setFileChooserReadOnly(true);
			imageChooser = new JFileChooser();
			FileFilter filter = new EndsWithFileFilter("Image File", ".png", ".jpg", ".gif", ".bmp");
			imageChooser.addChoosableFileFilter(filter);
			imageChooser.setFileFilter(filter);
		}
		
		imageChooser.setCurrentDirectory(Options.lastBrowsedDirectory);

		if (imageChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		
		Options.lastBrowsedDirectory = imageChooser.getCurrentDirectory();

		File file = imageChooser.getSelectedFile();
		if (!file.exists()) {
			return null;
		}

		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage getSelectedImage() {
		return image;
	}

	public int showDialog() {
		setVisible(true);
		return retVal;
	}
}