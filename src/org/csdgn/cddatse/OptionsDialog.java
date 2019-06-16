/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2019 Robert Maupin
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

import java.awt.Window;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.csdgn.maru.swing.TableLayout;

public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = -3772481664983484013L;

	private Options options;

	public OptionsDialog(Window window, Options options) {
		super(window);
		setResizable(false);
		setModal(true);
		setTitle("Options");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		setLocationRelativeTo(window);

		this.options = options;
		setContentPane(createPanel());

		pack();

	}

	public JPanel createPanel() {
		JPanel panel = new JPanel(new TableLayout(4, 4, true));
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		JLabel label = new JLabel("Game Path");
		JTextField text = new JTextField(options.getGamePathString());
		text.addCaretListener(e -> {
			File file = new File(text.getText());
			if (file.exists()) {
				options.gamePath = file;
			}
		});
		JButton browse = new JButton("Browse");
		browse.addActionListener(e -> {
			File dir = options.browseForDirectory(this, "Select CDDA Directory");
			if (dir != null) {
				text.setText(dir.getAbsolutePath());
				options.gamePath = dir;
			}
		});

		panel.add(label, "x=0;y=0");
		panel.add(text, "x=1;y=0;w=300");
		panel.add(browse, "x=2;y=0");

		// OK Button
		JButton ok = new JButton("OK");
		ok.addActionListener(e -> {
			dispose();
		});
		panel.add(ok, "colspan=3;x=0;y=1");

		return panel;
	}

	public void dispose() {
		options.save();
		super.dispose();
	}
}
