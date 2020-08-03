/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014-2020 Robert Maupin
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
import javax.swing.text.AbstractDocument;

import org.csdgn.maru.swing.DocumentChangeListener;
import org.csdgn.maru.swing.IntegerDocumentFilter;
import org.csdgn.maru.swing.TableLayout;

public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = -3772481664983484013L;
	private boolean pressedOk;


	public OptionsDialog(Window window) {
		super(window);
		setResizable(false);
		setModal(true);
		setTitle("Options");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 200);
		setLocationRelativeTo(window);

		setContentPane(createPanel());
		pressedOk = false;

		pack();
	}

	//TODO make this more modular
	public JPanel createPanel() {
		JPanel panel = new JPanel(new TableLayout(4, 4, true));
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		// Game Path
		JLabel label = new JLabel("Game Path");
		JTextField text = new JTextField(Options.INSTANCE.getGamePathString());
		text.addCaretListener(e -> {
			File file = new File(text.getText());
			if (file.exists()) {
				Options.INSTANCE.gamePath = file;
				Options.INSTANCE.markChanged();
			}
		});
		JButton browse = new JButton("Browse");
		browse.addActionListener(e -> {
			File dir = Options.INSTANCE.browseForDirectory(this, "Select CDDA Directory");
			if (dir != null) {
				text.setText(dir.getAbsolutePath());
				Options.INSTANCE.gamePath = dir;
				Options.INSTANCE.markChanged();
			}
		});

		panel.add(label, "x=0;y=0");
		panel.add(text, "x=1;y=0;w=300");
		panel.add(browse, "x=2;y=0");


		// Image Scale
		label = new JLabel("Image Scale");
		JTextField scale = new JTextField(Integer.toString(Options.INSTANCE.imageScale));
		AbstractDocument document = (AbstractDocument) scale.getDocument();
		document.setDocumentFilter(new IntegerDocumentFilter(true));
		document.addDocumentListener(new DocumentChangeListener(e -> {
				try {
					int value = Integer.parseInt(scale.getText().trim());
					if(value >= Options.MIN_SCALE && value <= Options.MAX_SCALE) {
						Options.INSTANCE.imageScale = value;
						Options.INSTANCE.markChanged();
					}
				} catch(NumberFormatException ex) {
					//fail silently
				}
			}));

		panel.add(label, "x=0;y=1");
		panel.add(scale, "colspan=2;x=1;y=1");

		// OK Button
		JButton ok = new JButton("OK");
		ok.addActionListener(e -> {
			//only save when OK is pressed.
			pressedOk = true;
			Options.INSTANCE.save();
			dispose();
		});
		panel.add(ok, "colspan=3;x=0;y=2");

		return panel;
	}

	public void dispose() {
		if(!pressedOk) {
			Options.INSTANCE.load();
		}
		super.dispose();
	}
}
