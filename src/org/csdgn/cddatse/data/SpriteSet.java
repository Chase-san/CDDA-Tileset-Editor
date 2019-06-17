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
package org.csdgn.cddatse.data;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SpriteSet {
	private TileSubset subset;
	public Set<BufferedImage> sprite;
	public Integer weight;

	public SpriteSet(TileSubset subset) {
		this.subset = subset;
		sprite = new LinkedHashSet<BufferedImage>();
		weight = null;
	}

	public SpriteSet(TileSubset subset, JsonElement ele) {
		this.subset = subset;
		sprite = new LinkedHashSet<BufferedImage>();
		weight = null;
		setFromJson(ele);
	}
	
	
	
	public void setFromJson(JsonElement ele) {
		if (ele.isJsonObject()) {
			JsonObject obj = ele.getAsJsonObject();
			
			for(int index : JsonToolkit.ints(obj.get("sprite"))) {
				sprite.add(subset.getImageFromIndex(index));
			}
			
			weight = obj.get("weight").getAsInt();
		} else {
			sprite.add(subset.getImageFromIndex(ele.getAsInt()));
		}
	}

	public JsonElement getJson() {
		if (weight == null) {
			//return JsonToolkit.ints(sprite);
		}
		JsonObject obj = new JsonObject();
		//obj.add("sprite", JsonToolkit.ints(sprite));
		obj.addProperty("weight", weight);
		return obj;
	}
}
