package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JCheckBox;

public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = 1272784812120208635L;

	private final JPanel contentPanel = new JPanel();
	private JFileChooser folderChooser = null;
	private JTextField txtCDDADir;

	/**
	 * Create the dialog.
	 */
	public OptionsDialog(Window window) {
		super(window);
		setResizable(false);
		setModal(true);
		setTitle("Options");
		setBounds(100, 100, 400, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		JLabel lblCataclysmDdaDirectory = new JLabel("Cataclysm: DDA Directory");
		GridBagConstraints gbc_lblCataclysmDdaDirectory = new GridBagConstraints();
		gbc_lblCataclysmDdaDirectory.anchor = GridBagConstraints.WEST;
		gbc_lblCataclysmDdaDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblCataclysmDdaDirectory.gridx = 0;
		gbc_lblCataclysmDdaDirectory.gridy = 0;
		contentPanel.add(lblCataclysmDdaDirectory, gbc_lblCataclysmDdaDirectory);

		txtCDDADir = new JTextField();
		txtCDDADir.setEditable(false);
		GridBagConstraints gbc_txtCDDADir = new GridBagConstraints();
		gbc_txtCDDADir.insets = new Insets(0, 0, 5, 5);
		gbc_txtCDDADir.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCDDADir.gridx = 0;
		gbc_txtCDDADir.gridy = 1;
		contentPanel.add(txtCDDADir, gbc_txtCDDADir);
		txtCDDADir.setColumns(10);

		if (Options.cataclysmDirectory != null) {
			txtCDDADir.setText(Options.cataclysmDirectory.getAbsolutePath());
		}
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File folder = browseForJsonFolder();
				if (folder != null) {
					txtCDDADir.setText(folder.getAbsolutePath());
				}
			}
		});
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowse.gridx = 1;
		gbc_btnBrowse.gridy = 1;
		contentPanel.add(btnBrowse, gbc_btnBrowse);
		
		final JCheckBox chckbxPrettyPrintJson = new JCheckBox("Pretty Print JSON");
		GridBagConstraints gbc_chckbxPrettyPrintJson = new GridBagConstraints();
		gbc_chckbxPrettyPrintJson.anchor = GridBagConstraints.WEST;
		gbc_chckbxPrettyPrintJson.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxPrettyPrintJson.gridx = 0;
		gbc_chckbxPrettyPrintJson.gridy = 2;
		chckbxPrettyPrintJson.setSelected(Options.prettyPrint);
		contentPanel.add(chckbxPrettyPrintJson, gbc_chckbxPrettyPrintJson);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// update options
						File file = new File(txtCDDADir.getText());
						if (file.exists()) {
							Options.cataclysmDirectory = file;
						}
						Options.prettyPrint = chckbxPrettyPrintJson.isSelected();
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
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setLocationRelativeTo(window);
	}

	private void warnIfBadData() {
		File file = new File(txtCDDADir.getText());
		if (!file.exists()) {
			AppToolkit.showWarning(this,
					"It is highly suggested you set the cataclysm directory, some functions will not be avaiable otherwise.");
		}
	}

	private File browseForJsonFolder() {
		if (folderChooser == null) {
			AppToolkit.setFileChooserReadOnly(true);
			folderChooser = new JFileChooser();
			folderChooser.setDialogTitle("Select Cataclysm-DDA folder");
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}

		folderChooser.setCurrentDirectory(Options.lastBrowsedDirectory);

		if (folderChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		Options.lastBrowsedDirectory = folderChooser.getCurrentDirectory();

		File file = folderChooser.getSelectedFile();
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}

		return file;
	}
	
	public void setVisible(boolean visible) {
		if(!visible) {
			warnIfBadData();
		}
		super.setVisible(visible);
	}

}
