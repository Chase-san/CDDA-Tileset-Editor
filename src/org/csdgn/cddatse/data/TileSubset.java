package org.csdgn.cddatse.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TileSubset {
	public String imageFile;
	public int width;
	public int height;
	public int offsetX;
	public int offsetY;

	public List<FallbackTile> fallback;
	public List<ImageTile> tiles;
	public Tileset tileset;

	public TileSubset(Tileset set) {
		fallback = new ArrayList<FallbackTile>();
		tiles = new ArrayList<ImageTile>();

		tileset = set;
		width = set.tileWidth;
		height = set.tileHeight;
		offsetX = 0;
		offsetY = 0;
	}

	public void read(JsonObject obj) {
		imageFile = obj.get("file").getAsString();
		readSpriteData(obj);

		obj.get("tiles").getAsJsonArray().forEach((a) -> {
			ImageTile tile = new ImageTile();
			tile.read(a.getAsJsonObject());
			tiles.add(tile);
		});

		if (obj.has("ascii")) {
			parseFallback(obj.get("ascii").getAsJsonArray());
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
}
