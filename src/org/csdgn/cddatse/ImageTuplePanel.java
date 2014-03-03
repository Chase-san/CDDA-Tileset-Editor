package org.csdgn.cddatse;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JLabel;

import org.csdgn.maru.util.Tuple;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class ImageTuplePanel extends JPanel {
	
	private static final long serialVersionUID = 2997385408755316623L;
	private static BufferedImage BLANK = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	
	private Tuple<BufferedImage> tuple;
	private ImageIcon fore;
	private ImageIcon back;
	protected Window window;

	/**
	 * Create the panel.
	 */
	public ImageTuplePanel(Window window) {
		this.window = window;
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		setLayout(gridBagLayout);
		
		JLabel lblForeground = new JLabel("Foreground");
		GridBagConstraints gbc_lblForeground = new GridBagConstraints();
		gbc_lblForeground.insets = new Insets(0, 0, 5, 5);
		gbc_lblForeground.gridx = 0;
		gbc_lblForeground.gridy = 0;
		add(lblForeground, gbc_lblForeground);
		
		JLabel lblBackground = new JLabel("Background");
		GridBagConstraints gbc_lblBackground = new GridBagConstraints();
		gbc_lblBackground.insets = new Insets(0, 0, 5, 0);
		gbc_lblBackground.gridx = 1;
		gbc_lblBackground.gridy = 0;
		add(lblBackground, gbc_lblBackground);
		
		final JButton btnFore = new JButton(fore = new ImageIcon());
		btnFore.setPreferredSize(new Dimension(64,64));
		btnFore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTileChooser(fore,true);
			}
		});
		btnFore.setContentAreaFilled(false);
		btnFore.setFocusPainted(false);
		btnFore.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnFore.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnA = new GridBagConstraints();
		gbc_btnA.fill = GridBagConstraints.BOTH;
		gbc_btnA.insets = new Insets(0, 0, 5, 5);
		gbc_btnA.gridx = 0;
		gbc_btnA.gridy = 1;
		add(btnFore, gbc_btnA);
		
		final JButton btnBack = new JButton(back = new ImageIcon());
		btnBack.setPreferredSize(new Dimension(64,64));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTileChooser(back,false);
			}
		});
		
		btnBack.setContentAreaFilled(false);
		btnBack.setFocusPainted(false);
		btnBack.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnB = new GridBagConstraints();
		gbc_btnB.insets = new Insets(0, 0, 5, 0);
		gbc_btnB.fill = GridBagConstraints.BOTH;
		gbc_btnB.gridx = 1;
		gbc_btnB.gridy = 1;
		add(btnBack, gbc_btnB);
	}
	
	protected void setImageTuple(final Tuple<BufferedImage> tuple) {
		this.tuple = tuple;
		
		if(tuple.first != null)
			fore.setImage(tuple.first);
		else
			fore.setImage(BLANK);
		
		if(tuple.second != null)
			back.setImage(tuple.second);
		else
			back.setImage(BLANK);
	}
	
	private void showTileChooser(ImageIcon icon, boolean fg) {
		TileChooser tc = new TileChooser(window);
		tc.setExternalSelectedImage((BufferedImage)icon.getImage());
		int ret = tc.showDialog();
		if(ret == TileChooser.OPTION_OK) {
			icon.setImage(tc.getSelectedImage());
			if(fg) {
				tuple.first = tc.getSelectedImage();
			} else {
				tuple.second = tc.getSelectedImage();
			}			
		} else if(ret == TileChooser.OPTION_REMOVE) {
			//remove image from icon
			icon.setImage(BLANK);
			if(fg) {
				tuple.first = null;
			} else {
				tuple.second = null;
			}
		}
		
	}
}
