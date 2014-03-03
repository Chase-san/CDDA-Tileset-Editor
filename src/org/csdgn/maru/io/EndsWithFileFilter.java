package org.csdgn.maru.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Matches all files that end with the given ending.
 */
public class EndsWithFileFilter extends FileFilter {
	private String[] ends;
	private String desc;
	
	public EndsWithFileFilter(String description, String ... endings) {
		ends = endings;
		desc = description;
	}
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		String name = f.getName();
		for(String end : ends)
			if(name.endsWith(end))
				return true;
		return false;
	}

	@Override
	public String getDescription() {
		return desc;
	}

}
