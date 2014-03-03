package org.csdgn.cddatse;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.ButtonGroup;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MergeConflictDialog extends JDialog {
	private static final long serialVersionUID = 3605756347058136055L;
	
	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup strategyGroup = new ButtonGroup();
	
	public static final int OPTION_OURS = 0;
	public static final int OPTION_THEIRS = 1;
	public static final int OPTION_MANUAL = 2;
	
	private int returnValue = OPTION_OURS;
	
	/**
	 * Create the dialog.
	 */
	public MergeConflictDialog(Window owner) {
		super(owner);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); //for now...
		setTitle("Merge Conflict Resolver");
		setModal(true);
		setBounds(100, 100, 450, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(5, 5));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				panel_1.setBorder(new TitledBorder(null, "Tile Conflict Resolution Strategy", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_1.setLayout(new GridLayout(0, 3, 0, 0));
				{
					JRadioButton rdbtnUseOurs = new JRadioButton("Use ours");
					rdbtnUseOurs.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							returnValue = OPTION_OURS;
						}
					});
					rdbtnUseOurs.setToolTipText("For any id in which there is a conflict, this strategy will use our tiles over the imported tiles.");
					rdbtnUseOurs.setSelected(true);
					strategyGroup.add(rdbtnUseOurs);
					panel_1.add(rdbtnUseOurs);
				}
				{
					JRadioButton rdbtnUseImported = new JRadioButton("Use theirs");
					rdbtnUseImported.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							returnValue = OPTION_THEIRS;
						}
					});
					rdbtnUseImported.setToolTipText("For any id in which there is a conflict, this strategy will use the imported tiles over our tiles.");
					strategyGroup.add(rdbtnUseImported);
					panel_1.add(rdbtnUseImported);
				}
				{
					JRadioButton rdbtnNewRadioButton = new JRadioButton("Resolve Manually");
					rdbtnNewRadioButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							//TODO
						}
					});
					rdbtnNewRadioButton.setToolTipText("For every id that is conflict, you will be presented with an open to use either your our or the imported tile.");
					strategyGroup.add(rdbtnNewRadioButton);
					rdbtnNewRadioButton.setEnabled(false);
					panel_1.add(rdbtnNewRadioButton);
				}
			}
			{
				JLabel lblThereWasA = new JLabel("There was a conflict while importing the tileset.");
				panel.add(lblThereWasA, BorderLayout.NORTH);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Merge");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	public int showDialog() {
		setVisible(true);
		return returnValue;
	}
}
