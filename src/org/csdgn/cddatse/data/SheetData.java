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

import com.google.gson.JsonObject;

public class SheetData {
	public int width;
	public int height;
	public int offsetX;
	public int offsetY;

	public SheetData() {
		width = 1;
		height = 1;
		offsetX = 0;
		offsetY = 0;
	}

	public boolean read(JsonObject obj) {
		boolean hasData = false;
		if (obj.has("sprite_width")) {
			width = obj.get("sprite_width").getAsInt();
			hasData = true;
		}
		if (obj.has("sprite_height")) {
			height = obj.get("sprite_height").getAsInt();
			hasData = true;
		}
		if (obj.has("sprite_offset_x")) {
			offsetX = obj.get("sprite_offset_x").getAsInt();
			hasData = true;
		}
		if (obj.has("sprite_offset_y")) {
			offsetY = obj.get("sprite_offset_y").getAsInt();
			hasData = true;
		}
		return hasData;
	}
	
	public void write(JsonObject obj) {
		obj.addProperty("sprite_width", width);
		obj.addProperty("sprite_height", height);
		obj.addProperty("sprite_offset_x", offsetX);
		obj.addProperty("sprite_offset_y", offsetY);
	}
}
