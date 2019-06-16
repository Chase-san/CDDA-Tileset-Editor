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

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Options {
	private static final String DefaultSettingsFile = "options";
	private static final String KeyGamePath = "GAME_PATH";
	private static final String KeyLastPath = "LAST_PATH";

	public File gamePath;
	public File lastPath;
	public File myPath;

	public Options() {
		myPath = AppToolkit.getLocalDirectory();
		lastPath = FileSystemView.getFileSystemView().getHomeDirectory();
		gamePath = null;
	}

	public String getGamePathString() {
		if (gamePath != null) {
			return gamePath.getAbsolutePath();
		}
		return "";
	}

	public boolean load() {
		return load(new File(myPath, DefaultSettingsFile));
	}

	public boolean load(File file) {
		if (!file.exists()) {
			return false;
		}
		Properties props = new Properties();
		try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
			props.load(r);
			String path = props.getProperty(KeyGamePath);
			if (path != null) {
				gamePath = new File(path);
			}
			path = props.getProperty(KeyLastPath);
			if (path != null) {
				lastPath = new File(path);
			}

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public boolean save() {
		return save(new File(myPath, DefaultSettingsFile));
	}

	public boolean save(File file) {
		Properties props = new Properties();
		if (gamePath != null) {
			props.setProperty(KeyGamePath, gamePath.getAbsolutePath());
		}
		if (lastPath != null) {
			props.setProperty(KeyLastPath, lastPath.getAbsolutePath());
		}
		if (props.size() <= 0) {
			return false;
		}
		try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
			props.store(w, "CDDATSE Settings File");
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	private JFileChooser fileChooser = null;
	private JFileChooser folderChooser = null;

	public File browseForDirectory(Component parent, String title) {
		if (folderChooser == null) {
			AppToolkit.setFileChooserReadOnly(true);
			folderChooser = new JFileChooser();
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		folderChooser.setDialogTitle(title);
		folderChooser.setCurrentDirectory(lastPath);

		if (folderChooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		File file = folderChooser.getSelectedFile();
		if (file == null) {
			return null;
		}
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		if (!file.exists()) {
			return null;
		}

		lastPath = file.getAbsoluteFile();
		return file;
	}

	public File browseForFile(Component parent, String title) {
		if (fileChooser == null) {
			AppToolkit.setFileChooserReadOnly(true);
			fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}
		fileChooser.setDialogTitle(title);
		fileChooser.setCurrentDirectory(lastPath);

		if (fileChooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		lastPath = fileChooser.getCurrentDirectory().getAbsoluteFile();

		File file = fileChooser.getSelectedFile();
		if (file == null || !file.exists()) {
			return null;
		}

		return file;
	}

}
