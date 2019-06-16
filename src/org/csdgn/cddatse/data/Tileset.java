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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Tileset extends TilesetStub {
	public Map<String, List<Tile>> byFile;
	public Map<String, List<Tile>> byId;
	public List<FallbackTile> fallback;
	public Map<String, SheetData> fileSheets;
	public List<ImageTile> tiles;
	public int tileHeight;
	public int tileWidth;

	public Tileset() {
		byFile = new LinkedHashMap<String, List<Tile>>();
		byId = new HashMap<String, List<Tile>>();
		fileSheets = new HashMap<String, SheetData>();

		tiles = new ArrayList<ImageTile>();
		fallback = new ArrayList<FallbackTile>();
	}

	public void load(TilesetStub stub) {
		name = stub.name;
		view = stub.view;
		json = stub.json;
		file = stub.file;

		loadJson(json);
	}

	public Set<String> getSheetNames() {
		return byFile.keySet();
	}

	public void addTile(String file, ImageTile tile) {
		getListFor(byFile, file).add(tile);
		for (String id : tile.id) {
			getListFor(byId, id).add(tile);
		}
		tiles.add(tile);
	}

	public void addFallback(String file, FallbackTile tile) {
		getListFor(byFile, file).add(tile);
		fallback.add(tile);
	}

	private List<Tile> getListFor(Map<String, List<Tile>> ref, String id) {
		List<Tile> tiles = ref.get(id);
		if (tiles == null) {
			tiles = new ArrayList<Tile>();
			ref.put(id, tiles);
		}
		return tiles;
	}

	public boolean saveJson(File file) {
		try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
			JsonObject root = new JsonObject();

			JsonArray infoArray = new JsonArray();
			JsonObject info = new JsonObject();
			info.addProperty("height", tileHeight);
			info.addProperty("width", tileWidth);
			infoArray.add(info);

			root.add("tile_info", infoArray);

			// tiles-new
			JsonArray sheets = new JsonArray();
			for (String sheetFile : byFile.keySet()) {
				JsonObject sheet = new JsonObject();
				sheet.addProperty("file", sheetFile);

				// Sprite/Sheet data
				SheetData data = fileSheets.get(sheetFile);
				if (data != null) {
					data.write(sheet);
				}

				// actual tiles
				JsonArray tiles = new JsonArray();
				JsonArray fallbacks = new JsonArray();
				for (Tile tile : byFile.get(sheetFile)) {
					if (tile instanceof ImageTile) {
						ImageTile it = (ImageTile) tile;
						JsonObject obj = new JsonObject();
						it.write(obj);
						tiles.add(obj);
					} else {
						FallbackTile ft = (FallbackTile) tile;
						JsonObject obj = new JsonObject();
						ft.write(obj);
						fallbacks.add(obj);
					}
				}

				sheet.add("tiles", tiles);
				if (fallbacks.size() > 0) {
					sheet.add("ascii", fallbacks);
				}

				sheets.add(sheet);
			}

			root.add("tiles-new", sheets);

			GsonBuilder gb = new GsonBuilder();
			gb.setPrettyPrinting();
			Gson gson = gb.create();
			w.write(gson.toJson(root));
			w.flush();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadJson(File file) {
		try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
			JsonParser parser = new JsonParser();
			JsonObject root = parser.parse(r).getAsJsonObject();

			JsonArray tileInfo = root.get("tile_info").getAsJsonArray();
			tileInfo.forEach((a) -> {
				JsonObject info = a.getAsJsonObject();
				if (info.has("height")) {
					tileHeight = info.get("height").getAsInt();
				}
				if (info.has("width")) {
					tileWidth = info.get("width").getAsInt();
				}
			});

			JsonArray tilesNew = root.get("tiles-new").getAsJsonArray();
			tilesNew.forEach((a) -> {
				JsonObject info = a.getAsJsonObject();
				String fileName = info.get("file").getAsString();
				parseSpriteData(fileName, info);
				parseTiles(fileName, info.get("tiles").getAsJsonArray());
				if (info.has("ascii")) {
					parseFallback(fileName, info.get("ascii").getAsJsonArray());
				}
			});

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private void parseSpriteData(String file, JsonObject obj) {
		SheetData data = new SheetData();
		if (data.read(obj)) {
			fileSheets.put(file, data);
		}
	}

	private void parseFallback(String file, JsonArray arr) {
		arr.forEach((a) -> {
			FallbackTile tile = new FallbackTile();
			tile.read(a.getAsJsonObject());
			addFallback(file, tile);
		});
	}

	private void parseTiles(String file, JsonArray arr) {
		arr.forEach((a) -> {
			ImageTile tile = new ImageTile();
			tile.read(a.getAsJsonObject());
			addTile(file, tile);
		});
	}

}
