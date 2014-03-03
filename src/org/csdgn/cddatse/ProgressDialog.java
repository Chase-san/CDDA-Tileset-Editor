package org.csdgn.cddatse;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = -3335168714276120596L;
	private ExecutorService service;
	private JLabel lblOperation;
	private JProgressBar progressBar;

	/**
	 * Create the dialog.
	 */
	public ProgressDialog(Window win) {
		setUndecorated(true);
		setModal(true);
		
		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(400,75));
		content.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(5, 20, 5, 20)));
		setContentPane(content);
		GridBagLayout gbl_content = new GridBagLayout();
		gbl_content.columnWidths = new int[]{0, 0};
		gbl_content.rowHeights = new int[]{25, 25};
		gbl_content.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_content.rowWeights = new double[]{0.0, 0.0};
		content.setLayout(gbl_content);
		
		lblOperation = new JLabel();
		GridBagConstraints gbc_lblOperation = new GridBagConstraints();
		gbc_lblOperation.fill = GridBagConstraints.BOTH;
		gbc_lblOperation.insets = new Insets(0, 0, 5, 0);
		gbc_lblOperation.gridx = 0;
		gbc_lblOperation.gridy = 0;
		content.add(lblOperation, gbc_lblOperation);
		
		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 1;
		content.add(progressBar, gbc_progressBar);
		
		pack();
		
		setLocationRelativeTo(win);

	}
	
	public void setLabelText(final String text) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				lblOperation.setText(text);
			}
		});
	}
	
	public void setProgressMinimum(final int value) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setMinimum(value);
			}
		});
	}
	
	public void setProgressMaximum(final int value) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setMaximum(value);
			}
		});
	}
	
	public void setProgress(final int value) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setValue(value);
			}
		});
	}
	
	public void execute(Runnable runnable) {
		service = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				return t;
			}
		});
		service.submit(runnable);
		setVisible(true);
		service.shutdownNow();
		dispose();
	}
}
