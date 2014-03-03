package org.csdgn.maru.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Matches all files that end with the given ending.
 */
public class EqualsFileFilter extends FileFilter {
	private String name;
	private String desc;
	
	public EqualsFileFilter(String filename, String description) {
		name = filename;
		desc = description;
	}
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory() || f.getName().equals(name))
			return true;
		return false;
	}

	@Override
	public String getDescription() {
		return desc;
	}

}
