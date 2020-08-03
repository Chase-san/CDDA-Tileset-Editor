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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Tileset extends TilesetStub {
	public Map<String, TileSubset> subsets;
	public int tileHeight;
	public int tileWidth;

	public Tileset() {
		subsets = new LinkedHashMap<String, TileSubset>();
	}

	public void close() {
		for (TileSubset subset : subsets.values()) {
			subset.close();
		}
	}

	public int getGlobalIdForLocalId(String sheet, int id) {
		int index = 0;
		for (String name : subsets.keySet()) {
			TileSubset sub = subsets.get(name);
			int size = sub.sprites.size();
			if (name.equalsIgnoreCase(sheet)) {
				return index + id;
			}
			index += size;
		}
		return -1;
	}

	public int getLocalIdForGlobalId(int id) {
		int index = 0;
		for (TileSubset sub : subsets.values()) {
			int size = sub.sprites.size();
			if (id < index + size) {
				return id - index;
			}
			index += size;
		}
		return -1;
	}

	public String getSheetNameForGlobalId(int id) {
		int index = 0;
		for (String name : subsets.keySet()) {
			TileSubset sub = subsets.get(name);
			int size = sub.sprites.size();
			if (id < index + size) {
				return name;
			}
			index += size;
		}
		return "";
	}

	public Set<String> getSheetNames() {
		return subsets.keySet();
	}

	public BufferedImage getSpriteForId(int id) {
		// find the correct subset
		int index = 0;
		for (TileSubset sub : subsets.values()) {
			int size = sub.sprites.size();
			if (id < index + size) {
				return sub.getImageFromIndex(id - index);
			}
			index += size;
		}
		return null;
	}

	public void load(TilesetStub stub) {
		name = stub.name;
		view = stub.view;
		json = stub.json;
		file = stub.file;

		loadJson(json);
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
				TileSubset subset = new TileSubset(this);
				subset.read(a.getAsJsonObject());

				subsets.put(subset.imageFile, subset);
			});

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void save(File dest) {
		while (!dest.isDirectory()) {
			dest = dest.getParentFile();
		}

		File mainFile = new File(dest, "tileset.txt");
		String jsonName = json.getName();

		try (Writer w = new OutputStreamWriter(new FileOutputStream(mainFile), StandardCharsets.UTF_8)) {
			w.append("NAME: ");
			w.append(name);
			w.append("\nVIEW: ");
			w.append(view);
			w.append("\nJSON: ");
			w.append(jsonName);
			w.append("\nTILESET: ");
			w.append(subsets.keySet().iterator().next());
			w.flush();
		} catch (IOException e) {
		}

		File jsonFile = new File(dest, jsonName);
		saveJson(jsonFile);
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
			for (String sheetFile : getSheetNames()) {
				JsonObject sheet = new JsonObject();
				sheet.addProperty("file", sheetFile);

				TileSubset subset = subsets.get(sheetFile);
				subset.write(sheet);

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
}
