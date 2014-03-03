package org.csdgn.cddatse.data;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.csdgn.cddatse.Options;
import org.csdgn.maru.Files;
import org.csdgn.maru.Strings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GFX {
	public static GFX instance;
	
	public String jsonFilename;
	public String tilesetFilename;
	public String name = "";
	public String view = "";
	public Tileset tileset;
	public ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	
	private GFX() {}
	
	public GFX(String author, int tileWidth, int tileHeight, String jsonFilename, String tilesetFilename) {
		tileset = new Tileset();
		name = author;
		view = author + "'s";
		this.jsonFilename = jsonFilename;
		this.tilesetFilename = tilesetFilename;
		tileset.tile_info = new TileInfo[1];
		tileset.tile_info[0] = new TileInfo();
		tileset.tile_info[0].width = tileWidth;
		tileset.tile_info[0].height = tileHeight;
		tileset.tiles = new ArrayList<BaseTile>();
	}
	
	public TileInfo getTileInfo() {
		return tileset.tile_info[0];
	}
	
	public void save(File outputFolder) throws IOException {
		//write tileset.txt
		StringBuilder sb = new StringBuilder();
		sb.append("NAME: ");
		sb.append(name);
		sb.append("\r\nVIEW: ");
		sb.append(view);
		sb.append("\r\nJSON: ");
		String path = "gfx/" + outputFolder.getName() + "/";
		sb.append(path);
		sb.append(jsonFilename);
		sb.append("\r\nTILESET: ");
		sb.append(path);
		sb.append(tilesetFilename);
		
		File file = new File(outputFolder,"tileset.txt");
		Files.setFileContents(file,sb.toString());
		
		//:3
		optimize();
		
		//write the json file
		file = new File(outputFolder,jsonFilename);
		
		GsonBuilder gb = new GsonBuilder();
		if(Options.prettyPrint) {
			gb = gb.setPrettyPrinting();
		}
		Files.setFileContents(file, gb.create().toJson(tileset));
		
		//build buffered image, 16 tiles wide :)
		if(images.size() > 0) {
			TileInfo info = getTileInfo();
			int width = 16 * info.width;
			int height = (int) Math.ceil(images.size()/16.0)*info.height;
			BufferedImage output = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			Graphics2D gx = output.createGraphics();
			int x = 0;
			int y = 0;
			for(BufferedImage image : images) {
				gx.drawImage(image, x, y, null);
				//increment
				x += info.width;
				if(x >= width) {
					x = 0;
					y += info.height;
				}
			}
			
			//write image
			file = new File(outputFolder,tilesetFilename);
			ImageIO.write(output, "png", file);
		}
	}
	
	public void optimize() {
		//remove unused images, and recalculate all indexes
		
		ArrayList<BufferedImage> keep = new ArrayList<BufferedImage>();
		HashMap<Integer,Integer> swapMap = new HashMap<Integer,Integer>();
		
		for(BaseTile tile : tileset.tiles) {
			for(int index : tile.imageList()) {
				BufferedImage img = images.get(index);
				int nindex = keep.indexOf(img);
				if(nindex == -1) {
					nindex = keep.size();
					keep.add(img);
				}
				if(index != nindex) {
					swapMap.put(index, nindex);
				}
			}
		}
		
		for(BaseTile tile : tileset.tiles) {
			tile.replaceIndexes(swapMap);
		}
		
		images.clear();
		images.addAll(keep);
	}
	
	public static GFX load(File file) {
		try {
			return _load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static GFX _load(File src) throws IOException {
		GFX gfx = new GFX();
		
		String jsonPath = "";
		String tilesetPath = "";
		
		for(String line : Files.getFileLines(src)) {
			line = line.trim();
			if(line.startsWith("#")) {
				continue;
			} else if(line.startsWith("NAME")) {
				gfx.name = line.substring(line.indexOf(':')+1).trim();
			} else if(line.startsWith("VIEW")) {
				gfx.view = line.substring(line.indexOf(':')+1).trim();
			} else if(line.startsWith("JSON")) {
				jsonPath = line.substring(line.indexOf(':')+1).trim();
			} else if(line.startsWith("TILESET")) {
				tilesetPath = line.substring(line.indexOf(':')+1).trim();
			}
		}
		
		File rootFile = src.getAbsoluteFile();
		
		for(int upd = Strings.count(jsonPath, '/'); upd >= 0; upd--) {
			rootFile = rootFile.getParentFile();
		}
		
		File jsonFile = new File(rootFile,jsonPath);
		File tilesetFile = new File(rootFile,tilesetPath);
		
		gfx.jsonFilename = jsonFile.getName();
		gfx.tilesetFilename = tilesetFile.getName();
		
		gfx.tileset = readJson(jsonFile);
		
		TileInfo info = gfx.getTileInfo();
		
		//load tileset images and cut them up
		BufferedImage tileset = ImageIO.read(tilesetFile);
		
		//go through the tileset and cut up each image into a tile
		//shove that tile into the images array
		gfx.images.addAll(cutImageIntoTiles(tileset,info.width,info.height));
		
		return gfx;
	}
	
	public static List<BufferedImage> cutImageIntoTiles(BufferedImage image, int width, int height) {
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		for(int y = 0; y < imgHeight; y += height) {
			for(int x = 0; x < imgWidth; x += width) {
				list.add(image.getSubimage(x, y, width, height));
			}
		}
		return list;
	}
	
	private static Tileset readJson(File jsonFile) throws IOException {
		Reader reader = new BufferedReader(new FileReader(jsonFile));
		Tileset tileset = new Gson().fromJson(reader, Tileset.class);
		reader.close();
		return tileset;
	}
	
	public void addTile(GFX src, BaseTile tile) {
		tile = tile.copy();
		importTileImages(src,tile);
		
		tileset.tiles.add(tile);
	}
	
	public void replaceTile(BaseTile current, GFX src, BaseTile replacement) {
		replacement = replacement.copy();
		importTileImages(src,replacement);
		
		current.id = replacement.id;
		current.fg = replacement.fg;
		current.bg = replacement.bg;
		current.rotates = replacement.rotates;
		current.multitile = replacement.multitile;
		current.additional_tiles = replacement.additional_tiles;
	}
	
	private void importTileImages(GFX src, BaseTile tile) {
		List<Integer> srcIndex = tile.imageList();
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int index : srcIndex) {
			BufferedImage img = src.images.get(index);
			
			int nindex = images.indexOf(img);
			if(nindex == -1) {
				nindex = images.size();
				images.add(img);
			}
			
			if(index != nindex) {
				map.put(index, nindex);
			}
		}
		tile.replaceIndexes(map);
	}
	
	public void dispose() {
		//clear all image resources
		for(BufferedImage img : images) {
			img.flush();
		}
		images.clear();
		images = null;
		tileset.tile_info = null;
		tileset.tiles.clear();
		tileset.tiles = null;
		tileset = null;
	}
}
