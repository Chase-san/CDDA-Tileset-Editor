/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2009-2019 Robert Maupin
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
package org.csdgn.maru.swing;

import java.util.ArrayList;

/**
 * Some constraint class so that you can use the simple table layout without
 * having to use strings if you don't want to.
 * 
 * @author Robert Maupin
 * 
 */
public class TableLayoutConstraint implements Cloneable {
	/**
	 * Easy method to create constraints from a string!
	 * 
	 * @param data Input text that will tell use how to construct the constraints.
	 *             <br>
	 *             <br>
	 *             Examples of valid strings<br>
	 *             x=0; y=0<br>
	 *             x = 0 ; colspan = 2<br>
	 *             y=last ;x=last+ 1 ;<br>
	 *             colspan=4; x=21; y=last-8; rowspan=2
	 * @return Some useful and easy to get constraints!
	 */
	public static final TableLayoutConstraint create(String data) {
		TableLayoutConstraint stlc = new TableLayoutConstraint();
		String[] properties = sfSplit(data.toLowerCase(), ';');
		for (String param : properties) {
			String[] kv = sfSplit(param, '=');

			if (kv.length != 2) {
				continue;
			}

			kv[0] = kv[0].trim();
			kv[1] = kv[1].trim();
			if (kv[0].equals("colspan")) {
				stlc.colspan = Integer.parseInt(kv[1].trim());
				if (stlc.colspan < 1) {
					throw new IllegalArgumentException("colspan must be at least 1.");
				}
			} else if (kv[0].equals("rowspan")) {
				stlc.rowspan = Integer.parseInt(kv[1].trim());
				if (stlc.rowspan < 1) {
					throw new IllegalArgumentException("rowspan must be at least 1.");
				}
			} else if (kv[0].equals("w") || kv[0].equals("width")) {
				stlc.width = Integer.parseInt(kv[1].trim());
				if (stlc.width < 0) {
					throw new IllegalArgumentException("width must be >= 0.");
				}
			} else if (kv[0].equals("h") || kv[0].equals("height")) {
				stlc.height = Integer.parseInt(kv[1].trim());
				if (stlc.height < 0) {
					throw new IllegalArgumentException("width must be >= 0.");
				}
			} else if (kv[0].equals("x")) {
				if (kv[1].equals("last")) {
					stlc.x = -1;
					stlc.offsetX = 0;
				} else if (kv[1].startsWith("last")) {
					stlc.x = -1;
					stlc.offsetX = eval(kv[1]);
				} else {
					stlc.x = Integer.parseInt(kv[1].trim());
					if (stlc.x < 0) {
						throw new IllegalArgumentException("x must be at least 0.");
					}
				}
			} else if (kv[0].equals("y")) {
				if (kv[1].equals("last")) {
					stlc.y = -1;
					stlc.offsetY = 0;
				} else if (kv[1].startsWith("last")) {
					stlc.y = -1;
					stlc.offsetY = eval(kv[1]);
				} else {
					stlc.y = Integer.parseInt(kv[1].trim());
					if (stlc.y < 0) {
						throw new IllegalArgumentException("y must be at least 0.");
					}
				}
			}
		}

		return stlc;
	}

	/**
	 * I have no plans to jam a math expression (SYA+RPN) evaluator into this layout
	 * manager, all they get is last + x or last - x, where x is any integer
	 * constant. <br>
	 * <br>
	 * If I am really nice, I might expand that to x + last or x - last, but don't
	 * hold your breath. <br>
	 * <br>
	 * XXX: Perhaps use scripting support to do it, but that might have cross
	 * platform limitations.
	 */
	private static final int eval(String n) {
		boolean add = true;
		int index = n.indexOf('+');
		if (index == -1) {
			index = n.indexOf('-');
			add = false;
		}
		if (index != -1) {
			try {
				int val = Integer.parseInt(n.substring(index + 1).trim());

				if (add) {
					return val;
				} else {
					return -val;
				}
			} catch (Exception e) {
			}
		}
		return 0;
	}

	/**
	 * Smart Fast Split. Removes null sections (length 0). "ab..cd" would produce
	 * [ab][cd] with '.'
	 */
	private static final String[] sfSplit(String src, char delim) {
		ArrayList<String> output = new ArrayList<String>();
		String tmp = "";
		int index = 0;
		int lindex = 0;
		while ((index = src.indexOf(delim, lindex)) != -1) {
			tmp = src.substring(lindex, index);
			if (tmp.length() > 0) {
				output.add(tmp);
			}
			lindex = index + 1;
		}
		tmp = src.substring(lindex);
		if (tmp.length() > 0) {
			output.add(tmp);
		}
		return output.toArray(new String[output.size()]);
	}

	/**
	 * How many columns does this span. A column is usually the cells that span from
	 * left to right. Thus spanning more of them usually makes it take up more
	 * horizontal space.
	 */
	public int colspan = 1;

	/**
	 * How many rows does this span. A row is usually the cells that span from top
	 * to bottom. Thus spanning more of them usually makes it take up more vertical
	 * space.
	 */
	public int rowspan = 1;

	/**
	 * Sets the x location of the given element. If this is -1 we use the offset
	 * position instead.
	 */
	public int x = -1;

	/**
	 * Sets the y location of the given element. If this is -1 we use the offset
	 * position instead.
	 */
	public int y = -1;

	/**
	 * Sets the offset x position from the last entered item.
	 */
	public int offsetX = 0;

	/**
	 * Sets the offset y position from the last entered item.
	 */
	public int offsetY = 1;

	/**
	 * Sets the minimum width for the component. If >= 0 this is used instead of the
	 * component size.
	 */
	public int width = -1;

	/**
	 * Sets the minimum height for the component. If >= 0 this is used instead of
	 * the component size.
	 */
	public int height = -1;

	/**
	 * Constructs a new TableLayoutConstraint
	 */
	public TableLayoutConstraint() {
	}

	/**
	 * Initializes this TableLayoutConstraint with the values from the given
	 * TableLayoutConstraint
	 * 
	 * @param stlc Initial values to use in this TableLayoutConstraint
	 */
	public TableLayoutConstraint(TableLayoutConstraint stlc) {
		if (stlc == null) {
			return;
		}
		colspan = stlc.colspan;
		rowspan = stlc.rowspan;
		x = stlc.x;
		y = stlc.y;
		offsetX = stlc.offsetX;
		offsetY = stlc.offsetY;
		width = stlc.width;
		height = stlc.height;
	}

	/**
	 * Provided for convince. A new instance is NOT required for every cell added.
	 */
	@Override
	public TableLayoutConstraint clone() {
		return new TableLayoutConstraint(this);
	}
}