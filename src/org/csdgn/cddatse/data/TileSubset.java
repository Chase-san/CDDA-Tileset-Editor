/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2019-2020 Robert Maupin
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TileSubset {
	public String imageFile;
	public int width;
	public int height;
	public int offsetX;
	public int offsetY;
	public ArrayList<BufferedImage> sprites;
	public List<FallbackTile> fallback;
	public List<ImageTile> tiles;
	private BufferedImage image;
	public Tileset tileset;

	public TileSubset(Tileset set) {
		sprites = new ArrayList<BufferedImage>();
		fallback = new ArrayList<FallbackTile>();
		tiles = new ArrayList<ImageTile>();

		tileset = set;
		width = set.tileWidth;
		height = set.tileHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void close() {
		image.flush();
		image = null;
		
		sprites.clear();
		fallback.clear();
		tiles.clear();
		
		tileset = null;
	}

	public void read(JsonObject obj) {
		imageFile = obj.get("file").getAsString();
		readSpriteData(obj);

		loadImage();

		obj.get("tiles").getAsJsonArray().forEach((a) -> {
			ImageTile tile = new ImageTile();
			tile.read(a.getAsJsonObject());
			tiles.add(tile);
		});

		if (obj.has("ascii")) {
			parseFallback(obj.get("ascii").getAsJsonArray());
		}

	}

	private void loadImage() {
		try {
			image = ImageIO.read(new File(tileset.file.getParentFile(), imageFile));

			// split image based on tileWidth/tileHeight
			int cols = image.getWidth() / width;
			int rows = image.getHeight() / height;

			System.out.println(String.format("Loading %s @ %dx%d. Found %d tiles (%dx%d).", imageFile, width, height,
					cols * rows, cols, rows));

			for (int row = 0; row < rows; ++row) {
				int y = row * height;
				for (int col = 0; col < cols; ++col) {
					int x = col * width;
					sprites.add(image.getSubimage(x, y, width, height));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void write(JsonObject obj) {
		writeSpriteData(obj);

		// actual tiles
		JsonArray tiles = new JsonArray();
		JsonArray fallbacks = new JsonArray();

		for (ImageTile tile : this.tiles) {
			ImageTile it = (ImageTile) tile;
			JsonObject tobj = new JsonObject();
			it.write(tobj);
			tiles.add(tobj);
		}

		for (FallbackTile tile : this.fallback) {
			FallbackTile ft = (FallbackTile) tile;
			JsonObject tobj = new JsonObject();
			ft.write(tobj);
			fallbacks.add(tobj);
		}

		obj.add("tiles", tiles);

		if (fallbacks.size() > 0) {
			obj.add("ascii", fallbacks);
		}
	}

	private void parseFallback(JsonArray arr) {
		arr.forEach((a) -> {
			FallbackTile tile = new FallbackTile();
			tile.read(a.getAsJsonObject());
			fallback.add(tile);
		});
	}

	private void readSpriteData(JsonObject obj) {
		if (obj.has("sprite_width")) {
			width = obj.get("sprite_width").getAsInt();
		}
		if (obj.has("sprite_height")) {
			height = obj.get("sprite_height").getAsInt();
		}
		if (obj.has("sprite_offset_x")) {
			offsetX = obj.get("sprite_offset_x").getAsInt();
		}
		if (obj.has("sprite_offset_y")) {
			offsetY = obj.get("sprite_offset_y").getAsInt();
		}
	}

	private void writeSpriteData(JsonObject obj) {
		if (width != tileset.tileWidth) {
			obj.addProperty("sprite_width", width);
		}
		if (width != tileset.tileHeight) {
			obj.addProperty("sprite_height", height);
		}
		if (offsetX != 0) {
			obj.addProperty("sprite_offset_x", offsetX);
		}
		if (offsetX != 0) {
			obj.addProperty("sprite_offset_y", offsetY);
		}
	}

	protected BufferedImage getImageFromIndex(int index) {
		if (index >= sprites.size()) {
			// TODO some fallback
			System.out.println("Cannot find image at index " + index + ".");
			return null;
		}
		return sprites.get(index);
	}

	public void sortImageTiles() {
		HashMap<String, ImageTile> ref = new HashMap<String, ImageTile>();
		List<String> list = new ArrayList<String>();
		for(ImageTile tile : tiles) {
			tile.sortIds();
			String lid = tile.id.iterator().next();
			ref.put(lid, tile);
			list.add(lid);
		}
		Collections.sort(list);
		tiles.clear();
		for(String id : list) {
			tiles.add(ref.get(id));
		}
	}
}
