package org.csdgn.cddatse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import javax.swing.filechooser.FileSystemView;

public class Options {
	public static File lastBrowsedDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
	
	public static File cataclysmDirectory = null;
	public static boolean prettyPrint = true;
	
	private static final String KEY_CATACLYSM_DIRECTORY = "cataclysm_dir";
	private static final String KEY_PRETTY_PRINT = "pretty_print";
	
	private static final Color[] defaultColorSpace = new Color[] { new Color(0, 0, 0), new Color(255, 0, 0), new Color(0, 110, 0),
		new Color(92, 51, 23), new Color(0, 0, 200), new Color(139, 58, 98), new Color(0, 150, 180), new Color(150, 150, 150),
		new Color(150, 150, 150), new Color(99, 99, 99), new Color(99, 99, 99), new Color(255, 150, 150), new Color(255, 150, 150),
		new Color(0, 255, 0), new Color(0, 255, 0), new Color(190, 190, 0), new Color(100, 100, 255), new Color(100, 100, 255),
		new Color(255, 0, 255), new Color(0, 240, 255), new Color(0, 240, 255), new Color(255, 255, 255) };
		
	public static Color[] colorSpace = new Color[22];
	
	public static File getOptionsFile() {
		return new File(AppToolkit.getLocalDirectory(), "options");
	}
	
	public static File getColorOptionsFile() {
		return new File(AppToolkit.getLocalDirectory(), "colorspace.bmp");
	}
	
	public static void load() {
		Properties config = new Properties();
		try {
			config.load(new FileReader(getOptionsFile()));
		} catch (IOException e) {
			//ignore failure
		}
		
		String cdir = config.getProperty(KEY_CATACLYSM_DIRECTORY);
		if(cdir != null) {
			cataclysmDirectory = new File(cdir);
			if(cataclysmDirectory.exists()) {
				lastBrowsedDirectory = new File(cataclysmDirectory,"gfx");
			} else {
				cataclysmDirectory = null;
			}
		}
		String pprint = config.getProperty(KEY_PRETTY_PRINT,"true");
		prettyPrint = Boolean.parseBoolean(pprint);
		
		//color options amount to setting the colorspace
		//try loading colors
		BufferedImage CSImage = new BufferedImage(8,2,BufferedImage.TYPE_INT_RGB);
		try {
			CSImage = ImageIO.read(getColorOptionsFile());
		} catch (IOException e) {
			colorSpace = defaultColorSpace;
		}

		//if colorspace defaulted, ignore next section
		if(colorSpace[1] != defaultColorSpace[1]) {
		//check dimensions, must be 8x2
		if(CSImage.getWidth() == 8 && CSImage.getHeight() ==2) {
			//start setting colors
			//"BLACK"
			colorSpace[0] = new Color(CSImage.getRGB(0,0));
			//"RED"
			colorSpace[1] = new Color(CSImage.getRGB(1,0));
			//"GREEN"
			colorSpace[2] = new Color(CSImage.getRGB(2,0));
			//"BROWN"
			colorSpace[3] = new Color(CSImage.getRGB(3,0));
			//"BLUE"
			colorSpace[4] = new Color(CSImage.getRGB(4,0));
			//"MAGENTA"
			colorSpace[5] = new Color(CSImage.getRGB(5,0));
			//"CYAN"
			colorSpace[6] = new Color(CSImage.getRGB(6,0));
			//"LTGRAY"/"LIGHT_GRAY"
			colorSpace[7] = new Color(CSImage.getRGB(7,0));
			colorSpace[8] = new Color(CSImage.getRGB(7,0));
			//"DKGRAY"/"DARK_GRAY"
			colorSpace[9] = new Color(CSImage.getRGB(0,1));
			colorSpace[10] = new Color(CSImage.getRGB(0,1));
			//"LTRED"/"LIGHT_RED"
			colorSpace[11] = new Color(CSImage.getRGB(1,1));
			colorSpace[12] = new Color(CSImage.getRGB(1,1));
			//"LTGREEN"/"LIGHT_GREEN"
			colorSpace[13] = new Color(CSImage.getRGB(2,1));
			colorSpace[14] = new Color(CSImage.getRGB(2,1));
			//"YELLOW"
			colorSpace[15] = new Color(CSImage.getRGB(3,1));
			//"LTBLUE"/"LIGHT_BLUE"
			colorSpace[16] = new Color(CSImage.getRGB(4,1));
			colorSpace[17] = new Color(CSImage.getRGB(4,1));
			//"PINK"
			colorSpace[18] = new Color(CSImage.getRGB(5,1));
			//"LTCYAN"/"LIGHT_CYAN"
			colorSpace[19] = new Color(CSImage.getRGB(6,1));
			colorSpace[20] = new Color(CSImage.getRGB(6,1));
			//"WHITE"
			colorSpace[21] = new Color(CSImage.getRGB(7,1));
		} else { //default colorspace
			colorSpace = defaultColorSpace;
		}
		}
		
	}
	
	public static void save() {
		Properties config = new Properties();
		if(cataclysmDirectory != null) {
			config.setProperty(KEY_CATACLYSM_DIRECTORY, cataclysmDirectory.getAbsolutePath());
		}
		config.setProperty(KEY_PRETTY_PRINT, Boolean.toString(prettyPrint));
		
		try {
			config.store(new FileWriter(getOptionsFile()), null);
		} catch (IOException e) {
			//ignore failure
		}
		//write out color file
		BufferedImage colorSet = new BufferedImage(8,2,BufferedImage.TYPE_INT_RGB);
		//start setting colors
		//"BLACK"
		colorSet.setRGB(0,0,colorSpace[0].getRGB());
		//"RED"
		colorSet.setRGB(1,0,colorSpace[1].getRGB());
		//"GREEN"
		colorSet.setRGB(2,0,colorSpace[2].getRGB());
		//"BROWN"
		colorSet.setRGB(3,0,colorSpace[3].getRGB());
		//"BLUE"
		colorSet.setRGB(4,0,colorSpace[4].getRGB());
		//"MAGENTA"
		colorSet.setRGB(5,0,colorSpace[5].getRGB());
		//"CYAN"
		colorSet.setRGB(6,0,colorSpace[6].getRGB());
		//"LTGRAY"/"LIGHT_GRAY"
		colorSet.setRGB(7,0,colorSpace[7].getRGB());
		//"DKGRAY"/"DARK_GRAY"
		colorSet.setRGB(0,1,colorSpace[9].getRGB());
		//"LTRED"/"LIGHT_RED"
		colorSet.setRGB(1,1,colorSpace[11].getRGB());
		//"LTGREEN"/"LIGHT_GREEN"
		colorSet.setRGB(2,1,colorSpace[13].getRGB());
		//"YELLOW"
		colorSet.setRGB(3,1,colorSpace[15].getRGB());
		//"LTBLUE"/"LIGHT_BLUE"
		colorSet.setRGB(4,1,colorSpace[16].getRGB());
		//"PINK"
		colorSet.setRGB(5,1,colorSpace[18].getRGB());
		//"LTCYAN"/"LIGHT_CYAN"
		colorSet.setRGB(6,1,colorSpace[19].getRGB());
		//"WHITE"
		colorSet.setRGB(7,1,colorSpace[21].getRGB());
		try {
			ImageIO.write(colorSet, "bmp", getColorOptionsFile());
		} catch (IOException e) {
			//ignore failure
		}
	}
}