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

public class FallbackTile implements Tile {
	public int offset;
	public boolean bold;
	public String color;
	
	public void FallbackTile() {
		bold = false;
		color = "WHITE";
	}
	
	public void read(JsonObject obj) {
		offset = obj.get("offset").getAsInt();
		
		if(obj.has("bold")) {
			bold = obj.get("bold").getAsBoolean();
		}
		if(obj.has("color")) {
			color = obj.get("color").getAsString();
		}
	}
	
	public void write(JsonObject obj) {
		obj.addProperty("offset", offset);
		obj.addProperty("bold", bold);
		obj.addProperty("color", color);
	}
}
