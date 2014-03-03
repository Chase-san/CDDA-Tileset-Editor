package org.csdgn.cddatse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

public class Options {
	public static File lastBrowsedDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
	
	public static File cataclysmDirectory = null;
	public static boolean prettyPrint = true;
	
	private static final String KEY_CATACLYSM_DIRECTORY = "cataclysm_dir";
	private static final String KEY_PRETTY_PRINT = "pretty_print";
	
	public static File getOptionsFile() {
		return new File(AppToolkit.getLocalDirectory(), "options");
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
	}
}