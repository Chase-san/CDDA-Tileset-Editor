package org.csdgn.cddatse.data;

import java.util.ArrayList;
import java.util.Map;

public class Tile {
	public String id;
	public Integer fg = null;
	public Integer bg = null;
	
	public ArrayList<Integer> imageList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if(fg != null)
			list.add(fg);
		if(bg != null)
			list.add(bg);
		return list;
	}
	
	public void replaceIndexes(Map<Integer,Integer> replace) {
		if(fg != null) {
			Integer n = replace.get(fg);
			if(n != null) {
				fg = n;
			}
		}
		if(bg != null) {
			Integer n = replace.get(bg);
			if(n != null) {
				bg = n;
			}
		}
	}
	
	public Tile copy() {
		Tile copy = new Tile();
		copy.id = id;
		copy.fg = fg;
		copy.bg = bg;
		return copy;
	}
}
