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
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class AppToolkit {
	public static void setFileChooserReadOnly(boolean readOnly) {
		UIManager.put("FileChooser.readOnly", readOnly);
	}

	private static File directory = null;
	private static ArrayList<Image> icons = null;

	/**
	 * Gets the directory for the program.
	 */
	public static File getLocalDirectory() {
		if (directory != null) {
			return directory;
		}

		// Determine our base directory
		String codePath = AppToolkit.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File codeFile = new File(codePath);
		if (codeFile.exists() && codePath != null) {
			// So we have a predictable format
			codePath = codeFile.toString();
			int len = codePath.length();
			if (len > 3) {
				String last = codePath.substring(len - 3).toLowerCase();
				if (last.equals("jar") || last.equals("bin")) {
					// Executing from a jar or development path
					directory = codeFile.getParentFile();
					if ("lib".equalsIgnoreCase(directory.getName())) {
						directory = directory.getParentFile();
					}
					return directory;
				}
			}
			if (directory == null) {
				directory = codeFile;
			}
			while (!directory.isDirectory()) {
				directory = directory.getParentFile();
			}
			if ("lib".equalsIgnoreCase(directory.getName())) {
				directory = directory.getParentFile();
			}
			// System.out.println(directory);

			return directory;
		}

		// I guess we can't do anything else...
		return directory = new File(".");
	}

	/**
	 * Get the program Icons
	 * 
	 * @return
	 */
	public static ArrayList<Image> getAppIconImages() {
		if (icons != null)
			return icons;

		icons = new ArrayList<Image>();

		for (int val : new int[] { 16, 24, 32, 48, 64, 256, 512 }) {
			String iconString = String.format("icon%d.png", val);

			Image img = getImageResource(iconString);
			if (img != null) {
				icons.add(img);
			}
		}

		return icons;
	}

	private static Map<String, BufferedImage> imageResources;

	public static BufferedImage getImageResource(String resource) {
		if (imageResources == null) {
			imageResources = new HashMap<String, BufferedImage>();
		}
		if (imageResources.containsKey(resource)) {
			return imageResources.get(resource);
		}
		InputStream stream = getLocalResource(resource);
		if (stream != null) {
			try {
				BufferedImage image = ImageIO.read(stream);
				if (image != null) {
					imageResources.put(resource, image);
					return image;
				}
			} catch (IOException e) {
				// do nothing if we fail (ergo we will return null)
			}
		}
		return null;
	}

	public static InputStream getLocalResource(String resource) {
		// check within current directory first
		try {
			File file = new File(getLocalDirectory(), resource);
			if (file.exists()) {
				return new FileInputStream(file);
			}
		} catch (Exception e) {
			// TODO
		}

		// fall back to in jar
		InputStream stream = ClassLoader.getSystemResourceAsStream(resource);
		if (stream != null)
			return stream;

		// if that fails, try development location
		try {
			File file = new File(getLocalDirectory(), "resource/" + resource);
			if (file.exists()) {
				return new FileInputStream(file);
			}
		} catch (Exception e) {
			// TODO
		}

		return null;
	}

	public static void showError(Component owner, String message) {
		JOptionPane.showMessageDialog(owner, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void showWarning(Component owner, String message) {
		JOptionPane.showMessageDialog(owner, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static int confirmWarning(Component owner, String message) {
		return JOptionPane.showConfirmDialog(owner, message, "Warning", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	public static int showYesNoOption(Component owner, String message, String title) {
		return JOptionPane.showConfirmDialog(owner, message, title, JOptionPane.YES_NO_OPTION);
	}

	public static Font getBestMonospaceFont(float size) {
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fonts = g.getAvailableFontFamilyNames();
		String[] bestFonts = new String[] { "Consolas", "Inconsolata", "DejaVu Sans Mono", "FreeMono" };
		for (String bestName : bestFonts) {
			for (String fontName : fonts) {
				if (fontName.equalsIgnoreCase(bestName)) {
					return Font.decode(fontName + "-PLAIN-" + size);
				}
			}
		}
		// Fallback to whatever system monospace java can find.
		return Font.decode(Font.MONOSPACED + "-PLAIN-" + size);
	}

	public static boolean isEqual(BufferedImage a, BufferedImage b) {
		int width = a.getWidth();
		int height = a.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (a.getRGB(x, y) != b.getRGB(x, y)) {
					return false;
				}
			}
		}

		return true;
	}
}
