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

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SpriteSet {
	public Set<Integer> ids;
	public Integer weight;

	public SpriteSet() {
		ids = new LinkedHashSet<Integer>();
		weight = null;
	}

	public SpriteSet(JsonElement ele) {
		ids = new LinkedHashSet<Integer>();
		weight = null;
		setFromJson(ele);
	}
	
	public void setFromJson(JsonElement ele) {
		if (ele.isJsonObject()) {
			JsonObject obj = ele.getAsJsonObject();
			
			ids.addAll(JsonToolkit.ints(obj.get("sprite")));
			
			weight = obj.get("weight").getAsInt();
		} else {
			ids.add(ele.getAsInt());
		}
	}

	public JsonElement getJson() {
		if (weight == null || weight < 0) {
			return JsonToolkit.ints(ids);
		}
		JsonObject obj = new JsonObject();
		obj.add("sprite", JsonToolkit.ints(ids));
		obj.addProperty("weight", weight);
		return obj;
	}
}
