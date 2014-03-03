package org.csdgn.cddatse.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseTile extends Tile {
	public boolean rotates = false;
	public Boolean multitile = false;
	public List<Tile> additional_tiles = new ArrayList<Tile>();
	
	public ArrayList<Integer> imageList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.addAll(super.imageList());
		if(additional_tiles != null) {
			for(Tile t : additional_tiles) {
				list.addAll(t.imageList());
			}
		}
		return list;
	}
	
	public void replaceIndexes(Map<Integer,Integer> replace) {
		super.replaceIndexes(replace);
		if(additional_tiles != null) {
			for(Tile tile : additional_tiles) {
				tile.replaceIndexes(replace);
			}
		}
	}
	
	public BaseTile copy() {
		BaseTile copy = new BaseTile();
		copy.rotates = rotates;
		copy.multitile = multitile;
		copy.id = id;
		copy.fg = fg;
		copy.bg = bg;
		if(additional_tiles != null) {
			for(Tile t : additional_tiles) {
				copy.additional_tiles.add(t.copy());
			}
		} else {
			copy.additional_tiles = null;
		}
		return copy;
	}
}
