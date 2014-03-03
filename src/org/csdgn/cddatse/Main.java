package org.csdgn.cddatse;

import java.awt.EventQueue;
import javax.swing.UIManager;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Options.load();
		
		//show the pane
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditorFrame frame = new EditorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
