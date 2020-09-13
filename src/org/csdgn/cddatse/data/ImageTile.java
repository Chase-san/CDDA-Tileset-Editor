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
package org.csdgn.cddatse.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ImageTile {
	public Set<String> id;
	public Set<SpriteSet> fg;
	public Set<SpriteSet> bg;
	public boolean rotates;
	public Set<ImageTile> extra;

	public ImageTile() {
		id = new LinkedHashSet<String>();
		fg = new LinkedHashSet<SpriteSet>();
		bg = new LinkedHashSet<SpriteSet>();
		extra = new LinkedHashSet<ImageTile>();
		rotates = false;
	}

	private List<SpriteSet> setSprites(JsonElement ele) {
		List<SpriteSet> out = new ArrayList<SpriteSet>();
		if (ele.isJsonArray()) {
			ele.getAsJsonArray().forEach((value) -> {
				out.add(new SpriteSet(value));
			});
		} else {
			out.add(new SpriteSet(ele));
		}
		return out;
	}

	private JsonElement getSprites(Set<SpriteSet> set) {
		if (set.size() > 1) {
			JsonArray array = new JsonArray();
			for (SpriteSet sprite : set) {
				array.add(sprite.getJson());
			}
			return array;
		}
		return set.iterator().next().getJson();
	}

	public void read(JsonObject obj) {
		if (obj.has("id")) {
			id.addAll(JsonToolkit.strings(obj.get("id")));
		}
		if (obj.has("fg")) {
			fg.addAll(setSprites(obj.get("fg")));
		}
		if (obj.has("bg")) {
			bg.addAll(setSprites(obj.get("bg")));
		}
		if (obj.has("rotates")) {
			rotates = obj.get("rotates").getAsBoolean();
		}

		if (obj.has("additional_tiles")) {
			JsonArray array = obj.get("additional_tiles").getAsJsonArray();
			array.forEach((ele) -> {
				ImageTile tile = new ImageTile();
				tile.read(ele.getAsJsonObject());
				extra.add(tile);
			});
		}
	}

	public void write(JsonObject obj) {
		obj.add("id", JsonToolkit.strings(id));
		if (fg.size() > 0) {
			obj.add("fg", getSprites(fg));
		}
		if (bg.size() > 0) {
			obj.add("bg", getSprites(bg));
		}
		obj.addProperty("rotates", rotates);

		if (extra.size() > 0) {
			obj.addProperty("multitile", true);
			JsonArray array = new JsonArray();
			for (ImageTile tile : extra) {
				JsonObject tObj = new JsonObject();
				tile.write(tObj);
				array.add(tObj);
			}
			obj.add("additional_tiles", array);
		}
	}

	protected void sortIds() {
		List<String> list = new ArrayList<String>();
		list.addAll(id);
		Collections.sort(list);
		id.clear();
		id.addAll(list);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (String nid : id) {
			if (!first) {
				buf.append("\n    ");
			}
			first = false;

			buf.append(nid);
		}
		return buf.toString();
	}

}
