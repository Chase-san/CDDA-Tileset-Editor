package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.csdgn.cddatse.data.GFX;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateDialog extends JDialog {
	private static final long serialVersionUID = 5246549747424622718L;
	
	public static final int OPTION_CANCEL = 0;
	public static final int OPTION_CREATE = 1;
	
	private int returnValue = OPTION_CANCEL;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtAuthor;
	private JTextField txtJson;
	private JTextField txtTileset;
	private JSpinner spnWidth;
	private JSpinner spnHeight;

	/**
	 * Create the dialog.
	 */
	public CreateDialog(Window window) {
		super(window);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setTitle("New Tileset");
		setBounds(100, 100, 353, 174);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblAuthor = new JLabel("Author");
			GridBagConstraints gbc_lblAuthor = new GridBagConstraints();
			gbc_lblAuthor.anchor = GridBagConstraints.EAST;
			gbc_lblAuthor.insets = new Insets(0, 0, 5, 5);
			gbc_lblAuthor.gridx = 0;
			gbc_lblAuthor.gridy = 0;
			contentPanel.add(lblAuthor, gbc_lblAuthor);
		}
		{
			txtAuthor = new JTextField();
			GridBagConstraints gbc_txtAuthor = new GridBagConstraints();
			gbc_txtAuthor.gridwidth = 3;
			gbc_txtAuthor.insets = new Insets(0, 0, 5, 0);
			gbc_txtAuthor.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtAuthor.gridx = 1;
			gbc_txtAuthor.gridy = 0;
			contentPanel.add(txtAuthor, gbc_txtAuthor);
			txtAuthor.setColumns(10);
		}
		{
			JLabel lblWidth = new JLabel("Width");
			GridBagConstraints gbc_lblWidth = new GridBagConstraints();
			gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
			gbc_lblWidth.gridx = 0;
			gbc_lblWidth.gridy = 1;
			contentPanel.add(lblWidth, gbc_lblWidth);
		}
		{
			spnWidth = new JSpinner();
			spnWidth.setModel(new SpinnerNumberModel(16, 2, 128, 2));
			GridBagConstraints gbc_spnWidth = new GridBagConstraints();
			gbc_spnWidth.fill = GridBagConstraints.HORIZONTAL;
			gbc_spnWidth.insets = new Insets(0, 0, 5, 5);
			gbc_spnWidth.gridx = 1;
			gbc_spnWidth.gridy = 1;
			contentPanel.add(spnWidth, gbc_spnWidth);
		}
		{
			JLabel lblHeight = new JLabel("Height");
			GridBagConstraints gbc_lblHeight = new GridBagConstraints();
			gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblHeight.gridx = 2;
			gbc_lblHeight.gridy = 1;
			contentPanel.add(lblHeight, gbc_lblHeight);
		}
		{
			spnHeight = new JSpinner();
			spnHeight.setModel(new SpinnerNumberModel(16, 2, 128, 2));
			GridBagConstraints gbc_spnHeight = new GridBagConstraints();
			gbc_spnHeight.insets = new Insets(0, 0, 5, 0);
			gbc_spnHeight.fill = GridBagConstraints.HORIZONTAL;
			gbc_spnHeight.gridx = 3;
			gbc_spnHeight.gridy = 1;
			contentPanel.add(spnHeight, gbc_spnHeight);
		}
		{
			JLabel lblJsonName = new JLabel("JSON");
			GridBagConstraints gbc_lblJsonName = new GridBagConstraints();
			gbc_lblJsonName.anchor = GridBagConstraints.EAST;
			gbc_lblJsonName.insets = new Insets(0, 0, 5, 5);
			gbc_lblJsonName.gridx = 0;
			gbc_lblJsonName.gridy = 2;
			contentPanel.add(lblJsonName, gbc_lblJsonName);
		}
		{
			txtJson = new JTextField();
			txtJson.setText("tile_config");
			GridBagConstraints gbc_txtJson = new GridBagConstraints();
			gbc_txtJson.insets = new Insets(0, 0, 5, 5);
			gbc_txtJson.gridwidth = 2;
			gbc_txtJson.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtJson.gridx = 1;
			gbc_txtJson.gridy = 2;
			contentPanel.add(txtJson, gbc_txtJson);
			txtJson.setColumns(10);
		}
		{
			JLabel lbljson = new JLabel(".json");
			GridBagConstraints gbc_lbljson = new GridBagConstraints();
			gbc_lbljson.anchor = GridBagConstraints.WEST;
			gbc_lbljson.insets = new Insets(0, 0, 5, 0);
			gbc_lbljson.gridx = 3;
			gbc_lbljson.gridy = 2;
			contentPanel.add(lbljson, gbc_lbljson);
		}
		{
			JLabel lblTileset = new JLabel("Tileset");
			GridBagConstraints gbc_lblTileset = new GridBagConstraints();
			gbc_lblTileset.anchor = GridBagConstraints.EAST;
			gbc_lblTileset.insets = new Insets(0, 0, 0, 5);
			gbc_lblTileset.gridx = 0;
			gbc_lblTileset.gridy = 3;
			contentPanel.add(lblTileset, gbc_lblTileset);
		}
		{
			txtTileset = new JTextField();
			txtTileset.setText("tileset");
			GridBagConstraints gbc_txtTileset = new GridBagConstraints();
			gbc_txtTileset.insets = new Insets(0, 0, 0, 5);
			gbc_txtTileset.gridwidth = 2;
			gbc_txtTileset.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtTileset.gridx = 1;
			gbc_txtTileset.gridy = 3;
			contentPanel.add(txtTileset, gbc_txtTileset);
			txtTileset.setColumns(10);
		}
		{
			JLabel lblpng = new JLabel(".png");
			GridBagConstraints gbc_lblpng = new GridBagConstraints();
			gbc_lblpng.anchor = GridBagConstraints.WEST;
			gbc_lblpng.gridx = 3;
			gbc_lblpng.gridy = 3;
			contentPanel.add(lblpng, gbc_lblpng);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Create");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnValue = OPTION_CREATE;
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						returnValue = OPTION_CANCEL;
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public GFX createGFX() {
		return new GFX(
				txtAuthor.getText(),
				(Integer)spnWidth.getValue(),
				(Integer)spnHeight.getValue(),
				txtJson.getText() + ".json",
				txtTileset.getText() + ".png"
			);
	}
	
	public int showDialog() {
		setVisible(true);
		return returnValue;
	}
}
