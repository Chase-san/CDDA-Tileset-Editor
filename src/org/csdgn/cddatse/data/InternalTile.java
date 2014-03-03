package org.csdgn.cddatse.data;

import java.awt.image.BufferedImage;

import org.csdgn.maru.util.Tuple;

public class InternalTile {
	public InternalTile() {
		
	}
	
	public InternalTile(BaseTile tile) {
		this.id = tile.id;
		this.rotates = tile.rotates;
		fillTuple(image,tile);
		if(tile.additional_tiles != null) {
			for(Tile at : tile.additional_tiles) {
				switch(at.id) {
				case "broken":
					broken = true;
					fillTuple(brokenImage,at);
					break;
				case "center":
					center = true;
					fillTuple(centerImage,at);
					break;
				case "corner":
					corner = true;
					fillTuple(cornerImage,at);
					break;
				case "edge":
					edge = true;
					fillTuple(edgeImage,at);
					break;
				case "end_piece":
					endPiece = true;
					fillTuple(endPieceImage,at);
					break;
				case "open":
					open = true;
					fillTuple(openImage,at);
					break;
				case "t_connection":
					tConnection = true;
					fillTuple(tConnectionImage,at);
					break;
				case "unconnected":
					unconnected = true;
					fillTuple(unconnectedImage,at);
					break;
				default: //unknown type :(
				}
			}
		}
	}
	
	public static void fillTuple(Tuple<BufferedImage> it, Tile tile) {
		if(tile.fg != null && tile.fg >= 0) {
			it.first = GFX.instance.images.get(tile.fg);
		}
		if(tile.bg != null && tile.bg >= 0) {
			it.second = GFX.instance.images.get(tile.bg);
		}
	}

	public int getImageId(BufferedImage img) {
		//TODO optimize this with a hashmap
		int index = GFX.instance.images.indexOf(img);
		if(index < 0) {
			System.out.println("ERROR -1 image index in \"" + id + "\"");
		}
		return index;
	}

	public boolean broken = false;
	public final Tuple<BufferedImage> brokenImage = new Tuple<BufferedImage>();
	public boolean center = false;
	public final Tuple<BufferedImage> centerImage = new Tuple<BufferedImage>();
	public boolean corner = false;
	public final Tuple<BufferedImage> cornerImage = new Tuple<BufferedImage>();
	public boolean edge = false;
	public final Tuple<BufferedImage> edgeImage = new Tuple<BufferedImage>();
	public boolean endPiece = false;
	public final Tuple<BufferedImage> endPieceImage = new Tuple<BufferedImage>();
	public String id = "";
	public final Tuple<BufferedImage> image = new Tuple<BufferedImage>();
	public boolean open = false;
	public final Tuple<BufferedImage> openImage = new Tuple<BufferedImage>();
	public boolean rotates = false;
	public boolean tConnection = false;
	public final Tuple<BufferedImage> tConnectionImage = new Tuple<BufferedImage>();
	public boolean unconnected = false;
	public final Tuple<BufferedImage> unconnectedImage = new Tuple<BufferedImage>();

	private Tile createMultitile(String id, Tuple<BufferedImage> tuple) {
		Tile tile = new Tile();
		tile.id = id;
		if (tuple.first != null) {
			tile.fg = getImageId(tuple.first);
		}
		if (tuple.second != null) {
			tile.bg = getImageId(tuple.second);
		}
		return tile;
	}

	public boolean isMultitile() {
		return corner || edge || center || tConnection || endPiece || unconnected || open || broken;
	}
	
	public boolean isImageless() {
		if(!image.isNull() || !brokenImage.isNull()
		|| !centerImage.isNull() || !cornerImage.isNull()
		|| !edgeImage.isNull() || !endPieceImage.isNull()
		|| !openImage.isNull() || !tConnectionImage.isNull()
		|| !unconnectedImage.isNull())
			return false;
		
		return true;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public void setCorner(boolean corner) {
		this.corner = corner;
	}

	public void setEdge(boolean edge) {
		this.edge = edge;
	}

	public void setEndPiece(boolean endPiece) {
		this.endPiece = endPiece;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setRotates(boolean rotates) {
		this.rotates = rotates;
	}

	public void setTConnection(boolean tConnection) {
		this.tConnection = tConnection;
	}

	public void setUnconnected(boolean unconnected) {
		this.unconnected = unconnected;
	}

	public BaseTile toBaseTile() {
		BaseTile tile = new BaseTile();
		tile.id = id;
		if (image.first != null) {
			tile.fg = getImageId(image.first);
		}
		if (image.second != null) {
			tile.bg = getImageId(image.second);
		}
		tile.rotates = rotates;
		if (isMultitile()) {
			tile.multitile = true;
			if (center) {
				tile.additional_tiles.add(createMultitile("center", centerImage));
			}
			if (corner) {
				tile.additional_tiles.add(createMultitile("corner", cornerImage));
			}
			if (edge) {
				tile.additional_tiles.add(createMultitile("edge", edgeImage));
			}
			if (tConnection) {
				tile.additional_tiles.add(createMultitile("t_connection", tConnectionImage));
			}
			if (endPiece) {
				tile.additional_tiles.add(createMultitile("end_piece", endPieceImage));
			}
			if (unconnected) {
				tile.additional_tiles.add(createMultitile("unconnected", unconnectedImage));
			}
			if (open) {
				tile.additional_tiles.add(createMultitile("open", openImage));
			}
			if (broken) {
				tile.additional_tiles.add(createMultitile("broken", brokenImage));
			}
		} else {
			tile.multitile = null;
			tile.additional_tiles = null;
		}
		return tile;
	}

	@Override
	public String toString() {
		if (id.length() == 0) {
			return " ";
		}
		return id;
	}
}
