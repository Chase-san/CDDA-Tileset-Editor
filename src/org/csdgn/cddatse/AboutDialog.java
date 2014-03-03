/**
 * Copyright (c) 2013 Robert Maupin
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 
 *    3. This notice may not be removed or altered from any source
 *    distribution.
 */
package org.csdgn.cddatse;

import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;

import javax.swing.BoxLayout;

public class AboutDialog extends JPanel {
	private static final long serialVersionUID = -2028002813135850360L;

	public AboutDialog() {
		
		setBorder(new EmptyBorder(2,2,2,2));
		setLayout(new BorderLayout(0, 8));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout(8, 0));
		add(titlePanel, BorderLayout.NORTH);
		
		JLabel logoLabel = new JLabel(new ImageIcon(AppToolkit.getAppIconImages().get(2)));
		titlePanel.add(logoLabel, BorderLayout.WEST);
		
		JPanel subTitlePanel = new JPanel();
		subTitlePanel.setLayout(new BoxLayout(subTitlePanel, BoxLayout.Y_AXIS));
		titlePanel.add(subTitlePanel, BorderLayout.CENTER);
		
		JLabel titleLabel = new JLabel(Version.SHORT_NAME);
		titleLabel.setForeground(Color.decode("#da819a"));
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
		subTitlePanel.add(titleLabel);
		
		JLabel buildLabel = new JLabel(Version.getVersionString());
		buildLabel.setBorder(new EmptyBorder(0, 2, 0, 0));
		buildLabel.setForeground(SystemColor.textInactiveText);
		buildLabel.setFont(new Font("Tahoma", Font.BOLD, 10));
		subTitlePanel.add(buildLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setOpaque(false);
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textArea.setFocusable(false);
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(150,100));
		textArea.setText("Copyright© 2014 Robert Maupin\nAll Rights Reserved.\n\nCreated because... there wasn't one.\n");
		add(textArea);
	}
	
	public void showAboutDialog(Window owner) {
		JOptionPane pane = new JOptionPane(this,JOptionPane.PLAIN_MESSAGE,JOptionPane.DEFAULT_OPTION);
		JDialog dialog = pane.createDialog(owner,"About");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.pack();
		
		dialog.setVisible(true);
		
	}

}
