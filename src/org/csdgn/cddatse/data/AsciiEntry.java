package org.csdgn.cddatse.data;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.csdgn.maru.Files;
import org.csdgn.maru.Strings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AsciiEntry {
	private static final Color[] colors = new Color[] { new Color(0, 0, 0), new Color(255, 0, 0), new Color(0, 110, 0),
			new Color(92, 51, 23), new Color(0, 0, 200), new Color(139, 58, 98), new Color(0, 150, 180), new Color(150, 150, 150),
			new Color(150, 150, 150), new Color(99, 99, 99), new Color(99, 99, 99), new Color(255, 150, 150), new Color(255, 150, 150),
			new Color(0, 255, 0), new Color(0, 255, 0), new Color(190, 190, 0), new Color(100, 100, 255), new Color(100, 100, 255),
			new Color(255, 0, 255), new Color(0, 240, 255), new Color(0, 240, 255), new Color(255, 255, 255) };
	private static final String[] colorStrings = new String[] { "BLACK", "RED", "GREEN", "BROWN", "BLUE", "MAGENTA", "CYAN", "LTGRAY",
			"LIGHT_GRAY", "DKGRAY", "DARK_GRAY", "LTRED", "LIGHT_RED", "LTGREEN", "LIGHT_GREEN", "YELLOW", "LTBLUE", "LIGHT_BLUE", "PINK",
			"LTCYAN", "LIGHT_CYAN", "WHITE" };

	private static final String[] field_id = { "fd_null", "fd_blood", "fd_bile", "fd_gibs_flesh", "fd_gibs_veggy", "fd_web", "fd_slime",
			"fd_acid", "fd_sap", "fd_sludge", "fd_fire", "fd_rubble", "fd_smoke", "fd_toxic_gas", "fd_tear_gas", "fd_nuke_gas",
			"fd_gas_vent", "fd_fire_vent", "fd_flame_burst", "fd_electricity", "fd_fatigue", "fd_push_items", "fd_shock_vent",
			"fd_acid_vent", "fd_plasma", "fd_laser", "fd_blood_veggy", "fd_blood_insect", "fd_blood_invertebrate", "fd_gibs_insect",
			"fd_gibs_invertebrate" };
	private static final String[] legacy_furn_id = { "f_null", "f_hay", "f_bulletin", "f_indoor_plant", "f_bed", "f_toilet",
			"f_makeshift_bed", "f_sink", "f_oven", "f_woodstove", "f_fireplace", "f_bathtub", "f_chair", "f_armchair", "f_sofa",
			"f_cupboard", "f_trashcan", "f_desk", "f_exercise", "f_bench", "f_table", "f_pool_table", "f_counter", "f_fridge",
			"f_glass_fridge", "f_dresser", "f_locker", "f_rack", "f_bookcase", "f_washer", "f_dryer", "f_dumpster", "f_dive_block",
			"f_crate_c", "f_crate_o", "f_canvas_wall", "f_canvas_door", "f_canvas_door_o", "f_groundsheet", "f_fema_groundsheet",
			"f_skin_wall", "f_skin_door", "f_skin_door_o", "f_skin_groundsheet", "f_mutpoppy", "f_safe_c", "f_safe_l", "f_safe_o",
			"f_plant_seed", "f_plant_seedling", "f_plant_mature", "f_plant_harvest" };
	private static final String[] legacy_mon_id = { "mon_null", "mon_squirrel", "mon_rabbit", "mon_deer", "mon_moose", "mon_wolf",
			"mon_coyote", "mon_bear", "mon_cougar", "mon_crow", "mon_dog", "mon_cat", "mon_ant_larva", "mon_ant", "mon_ant_soldier",
			"mon_ant_queen", "mon_ant_fungus", "mon_fly", "mon_bee", "mon_wasp", "mon_graboid", "mon_worm", "mon_halfworm",
			"mon_sludge_crawler", "mon_zombie", "mon_zombie_cop", "mon_zombie_shrieker", "mon_zombie_spitter", "mon_zombie_electric",
			"mon_zombie_smoker", "mon_zombie_swimmer", "mon_zombie_dog", "mon_zombie_brute", "mon_zombie_hulk", "mon_zombie_fungus",
			"mon_boomer", "mon_boomer_fungus", "mon_skeleton", "mon_zombie_necro", "mon_zombie_scientist", "mon_zombie_soldier",
			"mon_zombie_grabber", "mon_zombie_master", "mon_beekeeper", "mon_zombie_child", "mon_jabberwock", "mon_triffid",
			"mon_triffid_young", "mon_triffid_queen", "mon_creeper_hub", "mon_creeper_vine", "mon_biollante", "mon_vinebeast",
			"mon_triffid_heart", "mon_fungaloid", "mon_fungaloid_dormant", "mon_fungaloid_young", "mon_spore", "mon_fungaloid_queen",
			"mon_fungal_wall", "mon_blob", "mon_blob_small", "mon_chud", "mon_one_eye", "mon_crawler", "mon_sewer_fish", "mon_sewer_snake",
			"mon_sewer_rat", "mon_rat_king", "mon_mosquito", "mon_dragonfly", "mon_centipede", "mon_frog", "mon_slug",
			"mon_dermatik_larva", "mon_dermatik", "mon_spider_wolf", "mon_spider_web", "mon_spider_jumping", "mon_spider_trapdoor",
			"mon_spider_widow", "mon_dark_wyrm", "mon_amigara_horror", "mon_dog_thing", "mon_headless_dog_thing", "mon_thing",
			"mon_human_snail", "mon_twisted_body", "mon_vortex", "mon_flying_polyp", "mon_hunting_horror", "mon_mi_go", "mon_yugg",
			"mon_gelatin", "mon_flaming_eye", "mon_kreck", "mon_gracke", "mon_blank", "mon_gozu", "mon_shadow", "mon_breather_hub",
			"mon_breather", "mon_shadow_snake", "mon_dementia", "mon_homunculus", "mon_blood_sacrifice", "mon_flesh_angel", "mon_eyebot",
			"mon_manhack", "mon_skitterbot", "mon_secubot", "mon_hazmatbot", "mon_copbot", "mon_molebot", "mon_tripod", "mon_chickenbot",
			"mon_tankbot", "mon_turret", "mon_exploder", "mon_hallu_mom", "mon_generator", "mon_turkey", "mon_raccoon", "mon_opossum",
			"mon_rattlesnake", "mon_giant_crayfish" };
	private static final String[] legacy_ter_id = { "t_null", "t_hole", "t_dirt", "t_sand", "t_dirtmound", "t_pit_shallow", "t_pit",
			"t_pit_corpsed", "t_pit_covered", "t_pit_spiked", "t_pit_spiked_covered", "t_rock_floor", "t_rubble", "t_ash", "t_metal",
			"t_wreckage", "t_grass", "t_metal_floor", "t_pavement", "t_pavement_y", "t_sidewalk", "t_concrete", "t_floor", "t_dirtfloor",
			"t_grate", "t_slime", "t_bridge", "t_skylight", "t_emergency_light_flicker", "t_emergency_light", "t_wall_log_half",
			"t_wall_log", "t_wall_log_chipped", "t_wall_log_broken", "t_palisade", "t_palisade_gate", "t_palisade_gate_o", "t_wall_half",
			"t_wall_wood", "t_wall_wood_chipped", "t_wall_wood_broken", "t_wall_v", "t_wall_h", "t_concrete_v", "t_concrete_h",
			"t_wall_metal_v", "t_wall_metal_h", "t_wall_glass_v", "t_wall_glass_h", "t_wall_glass_v_alarm", "t_wall_glass_h_alarm",
			"t_reinforced_glass_v", "t_reinforced_glass_h", "t_bars", "t_door_c", "t_door_b", "t_door_o", "t_door_locked_interior",
			"t_door_locked", "t_door_locked_alarm", "t_door_frame", "t_chaingate_l", "t_fencegate_c", "t_fencegate_o", "t_chaingate_c",
			"t_chaingate_o", "t_door_boarded", "t_door_metal_c", "t_door_metal_o", "t_door_metal_locked", "t_door_bar_c", "t_door_bar_o",
			"t_door_bar_locked", "t_door_glass_c", "t_door_glass_o", "t_portcullis", "t_recycler", "t_window", "t_window_taped",
			"t_window_domestic", "t_window_domestic_taped", "t_window_open", "t_curtains", "t_window_alarm", "t_window_alarm_taped",
			"t_window_empty", "t_window_frame", "t_window_boarded", "t_window_stained_green", "t_window_stained_red",
			"t_window_stained_blue", "t_rock", "t_fault", "t_paper", "t_tree", "t_tree_young", "t_tree_apple", "t_underbrush", "t_shrub",
			"t_shrub_blueberry", "t_shrub_strawberry", "t_trunk", "t_root_wall", "t_wax", "t_floor_wax", "t_fence_v", "t_fence_h",
			"t_chainfence_v", "t_chainfence_h", "t_chainfence_posts", "t_fence_post", "t_fence_wire", "t_fence_barbed", "t_fence_rope",
			"t_railing_v", "t_railing_h", "t_marloss", "t_fungus", "t_tree_fungal", "t_water_sh", "t_water_dp", "t_water_pool", "t_sewage",
			"t_lava", "t_sandbox", "t_slide", "t_monkey_bars", "t_backboard", "t_gas_pump", "t_gas_pump_smashed", "t_generator_broken",
			"t_missile", "t_missile_exploded", "t_radio_tower", "t_radio_controls", "t_console_broken", "t_console",
			"t_gates_mech_control", "t_gates_control_concrete", "t_barndoor", "t_palisade_pulley", "t_sewage_pipe", "t_sewage_pump",
			"t_centrifuge", "t_column", "t_vat", "t_stairs_down", "t_stairs_up", "t_manhole", "t_ladder_up", "t_ladder_down",
			"t_slope_down", "t_slope_up", "t_rope_up", "t_manhole_cover", "t_card_science", "t_card_military", "t_card_reader_broken",
			"t_slot_machine", "t_elevator_control", "t_elevator_control_off", "t_elevator", "t_pedestal_wyrm", "t_pedestal_temple",
			"t_rock_red", "t_rock_green", "t_rock_blue", "t_floor_red", "t_floor_green", "t_floor_blue", "t_switch_rg", "t_switch_gb",
			"t_switch_rb", "t_switch_even" };
	private static final String[] legacy_trap_id = { "tr_null", "tr_bubblewrap", "tr_cot", "tr_brazier", "tr_funnel",
			"tr_makeshift_funnel", "tr_rollmat", "tr_fur_rollmat", "tr_beartrap", "tr_beartrap_buried", "tr_nailboard", "tr_caltrops",
			"tr_tripwire", "tr_crossbow", "tr_shotgun_2", "tr_shotgun_1", "tr_engine", "tr_blade", "tr_light_snare", "tr_heavy_snare",
			"tr_landmine", "tr_landmine_buried", "tr_telepad", "tr_goo", "tr_dissector", "tr_sinkhole", "tr_pit", "tr_spike_pit",
			"tr_lava", "tr_portal", "tr_ledge", "tr_boobytrap", "tr_temple_flood", "tr_temple_toggle", "tr_glow", "tr_hum", "tr_shadow",
			"tr_drain", "tr_snake" };

	private static BufferedImage generateEmptyTileImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		return image;
	}
	
	private static Graphics2D generateTileGFX(BufferedImage img) {
		Graphics2D gfx = img.createGraphics();

		gfx.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		gfx.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gfx.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		gfx.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		gfx.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		
		return gfx;
	}
	
	private static BufferedImage createAsciiFromTileset(int width, int height, char[] chr, Color fg, Color bg, BufferedImage tiles) {
		BufferedImage image = generateEmptyTileImage(width,height);
		
		if(fg == null)
			return image;
		
		Graphics2D gfx = generateTileGFX(image);
		
		//That shit made my eyes bleed yo. Let's do it better.
		
		//get cols of image based on width
		int cols = tiles.getWidth() / width;
		
		//get the location of, no need for anything fancy here, integer division and modulo
		int ty = chr[0] / cols;
		int tx = chr[0] % cols;
		
		BufferedImage src = tiles.getSubimage(tx, ty, width, height);
		
		int bgRGB = -1;
		if(bg != null)
			bgRGB = bg.getRGB();
		
		//draw the tile onto the image, swapping out black for bg, and white for fg
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				int srcRGB = src.getRGB(x, y);
				int a = srcRGB >> 24 & 0xFF;
				if(a == 0) continue;
				srcRGB &= 0xFFFFFF;
				
				//assume greyscale
				int dstRGB = 0;
				if(srcRGB > 0) {
					//most CPUs eat integer math alive, so, let's use that
					int val = srcRGB & 0xFF;
					int r = fg.getRed() * val / 255;
					int g = fg.getGreen() * val / 255;
					int b = fg.getBlue() * val / 255;
					dstRGB = (r << 16) | (g << 8) | b;
					//Just a little alpha compositing
					if(bgRGB != -1)
						dstRGB = dstRGB*a/255+bgRGB*(255-a)/255;
					else
						dstRGB |= a << 24;
				}
				//This is slow
				image.setRGB(x, y, dstRGB);
			}
		}
		

		gfx.dispose();
		
		
		return image;
	}
	
	private static BufferedImage createAsciiTile(int width, int height, char[] chr, Color fg, Color bg) {
		BufferedImage image = generateEmptyTileImage(width,height);
		if(fg == null)
			return image;
		Graphics2D gfx = generateTileGFX(image);

		// Something like this...
		Font font = new Font(Font.MONOSPACED, Font.BOLD, height - 2);
		gfx.setFont(font);

		GlyphVector vec = font.createGlyphVector(gfx.getFontRenderContext(), chr);
		Rectangle2D rect = vec.getGlyphVisualBounds(0).getBounds2D();

		float yOffset = (float) -rect.getY();

		// center it
		float x = (float) ((width >> 1) - rect.getWidth() / 2.0);
		float y = (float) ((height >> 1) - rect.getHeight() / 2.0);

		gfx.translate(x, y + yOffset);

		// TODO scale it so we are sure it fits
		if(bg != null) {
			gfx.setColor(bg);
			gfx.drawGlyphVector(vec, -1, -1);
			gfx.drawGlyphVector(vec, 1, -1);
			gfx.drawGlyphVector(vec, -1, 1);
			gfx.drawGlyphVector(vec, 1, 1);
		}

		gfx.setColor(fg);
		gfx.drawGlyphVector(vec, 0, 0);
		
		gfx.dispose();

		return image;
	}

	public static void getAllAsciiTiles(File jsonFolder, HashMap<String, AsciiEntry> map) {
		map.put("unknown", new AsciiEntry("unknown", "red", "?"));
		map.put("highlight_item", new AsciiEntry("highlight_item", "blue", "_", false, true));
		map.put("player_female", new AsciiEntry("player_female", "white", "@"));
		map.put("player_male", new AsciiEntry("player_male", "white", "@"));
		map.put("corpse", new AsciiEntry("corpse", "dkgray", "o"));
		map.put("cursor", new AsciiEntry("cursor", "yellow", "O", false, true));
		map.put("animation_line", new AsciiEntry("animation_line", "yellow", "-", false, true));
		map.put("animation_hit", new AsciiEntry("animation_hit", "red", "x", false, true));
		map.put("footstep", new AsciiEntry("footstep", "yellow", "="));
		map.put("explosion", new AsciiEntry("explosion", "red_yellow", "o"));
		map.put("lighting_hidden", new AsciiEntry("lighting_hidden", "dkgray_black", "#"));
		map.put("lighting_lowlight_light", new AsciiEntry("lighting_lowlight_light", "black", " "));
		map.put("lighting_lowlight_dark", new AsciiEntry("lighting_lowlight_dark", "black", " "));
		map.put("lighting_boomered_light", new AsciiEntry("lighting_boomered_light", "magenta_pink", "#"));
		map.put("lighting_boomered_dark", new AsciiEntry("lighting_boomered_dark", "magenta_pink", "#"));

		map.put("line_target", new AsciiEntry("line_target", "yellow", "-"));
		map.put("line_trail", new AsciiEntry("line_trail", "yellow", "-"));

		map.put("weather_acid_drop", new AsciiEntry("weather_acid_drop", "green", "'"));
		map.put("weather_rain_drop", new AsciiEntry("weather_rain_drop", "blue", "'"));
		map.put("weather_snowflake", new AsciiEntry("weather_snowflake", "white", "*"));

		getLegacyAsciiTiles(map);

		getAsciiTiles(jsonFolder, map);
	}

	private static void getAsciiTiles(File jsonFolder, HashMap<String, AsciiEntry> map) {
		if(!jsonFolder.exists()) {
			return;
		}
		for(File f : jsonFolder.listFiles()) {
			if(!f.exists()) {
				continue;
			}
			if(f.isDirectory()) {
				getAsciiTiles(f, map);
			} else if(f.getAbsolutePath().endsWith(".json")) {
				JsonElement elem = new JsonParser().parse(Files.getFileStringContents(f));
				parseJson(elem, map);
			}
		}
	}

	private static void getLegacyAsciiTiles(HashMap<String, AsciiEntry> map) {
		for(String str : legacy_trap_id) {
			map.put(str, new AsciiEntry(str, "ltgray", "t"));
		}
		for(String str : field_id) {
			map.put(str, new AsciiEntry(str, "white", "."));
		}
		for(String str : legacy_furn_id) {
			map.put(str, new AsciiEntry(str, "ltgray", "f"));
		}
		for(String str : legacy_ter_id) {
			map.put(str, new AsciiEntry(str, "white", "."));
		}
		for(String str : legacy_mon_id) {
			map.put(str, new AsciiEntry(str, "green", "M"));
		}
	}

	private static void parseJson(JsonElement elem, HashMap<String, AsciiEntry> map) {
		if(elem.isJsonArray()) {
			JsonArray array = elem.getAsJsonArray();
			for(int i = 0; i < array.size(); ++i) {
				parseJson(array.get(i), map);
			}
		} else if(elem.isJsonObject()) {
			JsonObject obj = elem.getAsJsonObject();
			if(obj.has("id") && obj.has("symbol") && obj.has("color")) {
				// vp_
				String id = obj.get("id").getAsString();

				String symbol = obj.get("symbol").getAsString();
				String color = obj.get("color").getAsString();

				if("vehicle_part".equalsIgnoreCase(obj.get("type").getAsString())) {
					id = "vp_" + id;
					String flags = "";
					if(obj.has("flags")) {
						flags = obj.getAsJsonArray("flags").toString();
					}
					// symbol gets overridden based on which VP is it
					boolean frame = false;
					if(obj.has("location")) {
						if("structure".equalsIgnoreCase(obj.get("location").getAsString())) {
							frame = true;
						}
						int tileID = -1;
						// define tiles
						// _nw - top left corner
						if(id.endsWith("_nw")) {
							tileID = frame ? 201 : 218;
						}
						// _ne - top right corner
						if(id.endsWith("_ne")) {
							tileID = frame ? 187 : 191;
						}
						// _sw - bottom left corner
						if(id.endsWith("_sw")) {
							tileID = frame ? 200 : 192;
						}
						// _se - bottom right corner
						if(id.endsWith("_se")) {
							tileID = frame ? 188 : 217;
						}
						// _vertical or _vertical_2 - vertical (duh)
						if(id.endsWith("_vertical") || id.endsWith("_vertical_2")) {
							tileID = frame ? 215 : 179;
						}
						// _horizontal or _horizontal_2 - horizontal (duuuh)
						if(id.endsWith("_horizontal") || id.endsWith("_horizontal_2")) {
							tileID = frame ? 216 : 196;
						}
						// _cross - internal cross
						if(id.endsWith("_cross")) {
							tileID = frame ? 206 : 197;
						}

						// as a stopper of sorts, check for untouchable tiles
						// here and set the ID back to -1 if it's needed
						if(flags.indexOf("CARGO") >= 0 || flags.indexOf("AISLE") >= 0 || flags.indexOf("PROTRUSION") >= 0) {
							tileID = -1;
						}

						if(tileID > 0) {
							symbol = new String(new char[] { (char) tileID });
						}
					}
				}

				// JOptionPane.showMessageDialog(null, symbol);

				if(symbol.length() > 1) {
					// if((symbol=="LINE_XOXO") || (symbol=="LINE_OXOX")){
					// going multitile
					// in vanilla this is just the walls, so we can fudge it a
					// little here
					map.put(id, new AsciiEntry(id, color, "#", true, false));
				} else {
					if(obj.has("broken_symbol") && obj.has("broken_color")) {
						map.put(id, new AsciiEntry(id, color, symbol, obj.get("broken_color").getAsString(), obj.get("broken_symbol")
								.getAsString()));
					} else {
						map.put(id, new AsciiEntry(id, color, symbol));
					}
				}
			}
			for(Map.Entry<String, JsonElement> entry : obj.entrySet()) {
				parseJson(entry.getValue(), map);
			}
		}
	}

	public final Color bgcolor;
	public final Color brkn_bgcolor;
	public final Color brkn_color;
	public final char broken_symbol;
	public final Color color;
	public final String id;
	public final boolean multitile;
	/** Lays over other tiles? (No BG) */
	public final boolean overlay;

	public final char symbol;

	public AsciiEntry(String id, String color, String symbol) {
		this.id = id;
		this.symbol = symbol.charAt(0);
		multitile = false;
		overlay = false;

		Color fg = Color.WHITE;
		Color bg = null;
		String fgStr = color;
		String bgStr = null;

		if(color.contains("_")) {
			String[] str = Strings.split(color, "_");
			fgStr = str[0];
			bgStr = str[1];
		}

		for(int i = 0; i < colors.length; ++i) {
			if(colorStrings[i].equalsIgnoreCase(fgStr)) {
				fg = colors[i];
			}
			if(colorStrings[i].equalsIgnoreCase(bgStr)) {
				bg = colors[i];
			}
		}

		this.color = fg;
		bgcolor = bg;
		brkn_color = null;
		brkn_bgcolor = null;
		broken_symbol = ' ';
	}

	// custom constructor for setting multitile and overlay flags
	public AsciiEntry(String id, String color, String symbol, boolean multitile, boolean overlay) {
		this.id = id;
		this.symbol = symbol.charAt(0);
		this.multitile = multitile;
		this.overlay = overlay;

		Color fg = Color.WHITE;
		Color bg = null;
		String fgStr = color;
		String bgStr = null;

		if(color.contains("_")) {
			String[] str = Strings.split(color, "_");
			fgStr = str[0];
			bgStr = str[1];
		}

		for(int i = 0; i < colors.length; ++i) {
			if(colorStrings[i].equalsIgnoreCase(fgStr)) {
				fg = colors[i];
			}
			if(colorStrings[i].equalsIgnoreCase(bgStr)) {
				bg = colors[i];
			}
		}

		this.color = fg;
		if(overlay) {
			bgcolor = null;
		} else {
			bgcolor = bg;
		}
		brkn_color = null;
		brkn_bgcolor = null;
		broken_symbol = ' ';
	}

	public AsciiEntry(String id, String color, String symbol, String brknColor, String brknSymbol) {
		this.id = id;
		this.symbol = symbol.charAt(0);
		multitile = false;
		overlay = false;

		Color fg = Color.WHITE;
		Color bg = null;
		String fgStr = color;
		String bgStr = null;

		if(color.contains("_")) {
			String[] str = Strings.split(color, "_");
			fgStr = str[0];
			bgStr = str[1];
		}

		for(int i = 0; i < colors.length; ++i) {
			if(colorStrings[i].equalsIgnoreCase(fgStr)) {
				fg = colors[i];
			}
			if(colorStrings[i].equalsIgnoreCase(bgStr)) {
				bg = colors[i];
			}
		}

		this.color = fg;
		bgcolor = bg;

		fg = Color.WHITE;
		bg = null;
		fgStr = brknColor;
		bgStr = null;

		if(brknColor.contains("_")) {
			String[] str = Strings.split(brknColor, "_");
			fgStr = str[0];
			bgStr = str[1];
		}

		for(int i = 0; i < colors.length; ++i) {
			if(colorStrings[i].equalsIgnoreCase(fgStr)) {
				fg = colors[i];
			}
			if(colorStrings[i].equalsIgnoreCase(bgStr)) {
				bg = colors[i];
			}
		}

		brkn_color = fg;
		brkn_bgcolor = bg;
		broken_symbol = brknSymbol.charAt(0);
	}

	public BufferedImage createAsciiTile(int width, int height) {
		return createAsciiTile(width, height, new char[] { symbol }, color, bgcolor);
	}
	
	public BufferedImage createAsciiTile(int width, int height, BufferedImage tileset) {
		return createAsciiFromTileset(width, height, new char[] { symbol }, color, bgcolor, tileset);
	}

	public BufferedImage createAsciiTile(int width, int height, int tileID) {
		return createAsciiTile(width, height, new char[] { (char) tileID }, color, bgcolor);
	}
	
	public BufferedImage createAsciiTile(int width, int height, int tileID, BufferedImage tileset) {
		return createAsciiFromTileset(width, height, new char[] { (char) tileID }, color, bgcolor, tileset);
	}

	public BufferedImage createBrokenAsciiTile(int width, int height) {
		return createAsciiTile(width, height, new char[] { broken_symbol }, brkn_color, brkn_bgcolor);
	}
	
	public BufferedImage createBrokenAsciiTile(int width, int height, BufferedImage tileset) {
		return createAsciiFromTileset(width, height, new char[] { broken_symbol }, brkn_color, brkn_bgcolor, tileset);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof String) {
			return obj.equals(id);
		} else if(obj instanceof AsciiEntry) {
			return ((AsciiEntry) obj).id.equals(id);
		}
		return false;
	}

	public boolean hasBrokenTile() {
		return brkn_color != null;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public boolean isMultitile() {
		return multitile;
	}

	public boolean isOverlay() {
		return overlay;
	}
}
