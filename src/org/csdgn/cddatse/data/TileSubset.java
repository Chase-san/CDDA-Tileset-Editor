package org.csdgn.cddatse.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	/** TODO
	 * "transparency": { "R": 0, "G": 0, "B": 0 },
	 */

	public List<BufferedImage> sprites;
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
	
	public void finalize() throws Throwable {
		image.flush();
		super.finalize();
	}

	public void read(JsonObject obj) {
		imageFile = obj.get("file").getAsString();
		readSpriteData(obj);

		loadImage();
		
		obj.get("tiles").getAsJsonArray().forEach((a) -> {
			ImageTile tile = new ImageTile(this);
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
			
			//split image based on tileWidth/tileHeight
			int cols = image.getWidth() / width - 1;
			int rows = image.getHeight() / height - 1;
			
			for(int col = 0; col < cols; ++col) {
				int x = cols * width;
				for(int row = 0; row < rows; ++row) {
					int y = row * height;
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
		if(index >= sprites.size()) {
			//TODO some fallback
			return null;
		}
		return sprites.get(index);
	}
}
