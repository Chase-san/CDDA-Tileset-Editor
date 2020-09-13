/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2009-2020 Robert Maupin
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple HTML like table layout for Java. No really!
 * 
 * @author Robert Maupin
 * @version 1.2
 */
public class TableLayout implements LayoutManager2 {
	private class Cell extends TableLayoutConstraint {
		public Component comp = null;

		public Cell(TableLayoutConstraint stlc, Component comp) {
			super(stlc);
			this.comp = comp;
		}
	}

	private class Table {
		public Cell[] cells = null;
		public int[] cols = null;
		public int[] rows = null;
	}

	private HashMap<Component, TableLayoutConstraint> defined;
	private boolean fill;
	private int hgap;
	private int vgap;

	/**
	 * Default arguments. No hgap, no vgap and to fill the cell.
	 */
	public TableLayout() {
		this(0, 0, true);
	}

	/**
	 * @param fill Fill the entire cell?
	 */
	public TableLayout(boolean fill) {
		this(0, 0, fill);
	}

	/**
	 * @param hgap Horizontal Gap between cells
	 * @param vgap Vertical Gap between cells
	 */
	public TableLayout(int hgap, int vgap) {
		this(hgap, vgap, true);
	}

	/**
	 * @param hgap Horizontal Gap between cells
	 * @param vgap Vertical Gap between cells
	 * @param fill Fill the entire cell?
	 */
	public TableLayout(int hgap, int vgap, boolean fill) {
		defined = new HashMap<Component, TableLayoutConstraint>();
		this.hgap = hgap;
		this.vgap = vgap;
		this.fill = fill;
	}

	@Override
	public void addLayoutComponent(Component comp, Object obj) {
		TableLayoutConstraint cons = null;
		if (obj instanceof TableLayoutConstraint) {
			cons = (TableLayoutConstraint) obj;
		} else if (obj instanceof String) {
			cons = TableLayoutConstraint.create((String) obj);
		} else {
			cons = new TableLayoutConstraint();
		}
		if (cons == null) {
			throw new IllegalArgumentException("Object must be a String or SimpleTableLayoutConstraint.");
		}

		defined.put(comp, cons.clone());
	}

	/**
	 * @param name Text to use to set the constraints.
	 * @param comp The component to set the contraints on.
	 * @see TableLayoutConstraint#create(String)
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		/*
		 * Easy enough!
		 */
		addLayoutComponent(comp, TableLayoutConstraint.create(name));
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container target) {
		// we do not currently cache anything

	}

	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();

		// int heightMinusInsets = parent.getHeight() - (insets.left + insets.right);
		// int widthMinusInsets = parent.getWidth() - (insets.left + insets.right);

		Table table = layoutTable(parent);

		/*
		 * Layout the components using the cell and row/col data
		 */
		for (Cell cell : table.cells) {
			/*
			 * Determine the size and offset of the cell!
			 */
			int x = insets.left + cell.x * hgap;
			int y = insets.top + cell.y * vgap;
			int width = 0;
			int height = 0;

			for (int i = 0; i < cell.x; ++i) {
				x += table.cols[i];
			}

			for (int i = 0; i < cell.y; ++i) {
				y += table.rows[i];
			}

			if (cell.colspan != 1) {
				for (int i = 0; i < cell.colspan; ++i) {
					width += table.cols[cell.x + i];
				}
				width += (cell.colspan - 1) * hgap;
			} else {
				width = table.cols[cell.x];
			}

			if (cell.rowspan != 1) {
				for (int i = 0; i < cell.rowspan; ++i) {
					height += table.rows[cell.y + i];
				}
				height += (cell.rowspan - 1) * vgap;
			} else {
				height = table.rows[cell.y];
			}

			if (fill) {
				cell.comp.setBounds(x, y, width, height);
			} else {
				/*
				 * Align the element inside the cell according to its alignment x/y and size
				 */
				Dimension dim = cell.comp.getPreferredSize();

				float xalign = cell.comp.getAlignmentX();
				float yalign = cell.comp.getAlignmentY();

				width -= dim.width;
				height -= dim.height;

				x += width * xalign;
				y += height * yalign;

				cell.comp.setBounds(x, y, dim.width, dim.height);
			}
		}
	}

	/**
	 * Do the actual calculation of the table size and all!
	 * 
	 * 
	 */
	private Table layoutTable(Container parent) {
		Table table = new Table();
		ArrayList<Cell> cells = new ArrayList<Cell>();
		Component[] cs = parent.getComponents();

		int maxX = 0;
		int maxY = 0;
		Cell last = null;
		for (Component comp : cs) {
			Cell cell = new Cell(defined.get(comp), comp);

			/*
			 * the only thing we touch here is the x/y (ergo col/row)
			 */
			if (cell.x == -1) {
				cell.x = 0;
				if (last != null) {
					cell.x = last.x + last.colspan - 1 + cell.offsetX;
				}
			}

			if (cell.y == -1) {
				cell.y = 0;
				if (last != null) {
					cell.y = last.y + last.rowspan - 1 + cell.offsetY;
				}
			}

			/*
			 * This is used to determine the number of columns and rows we will have in the
			 * end.
			 */
			if (cell.x + cell.colspan - 1 > maxX) {
				maxX = cell.x + cell.colspan - 1;
			}

			if (cell.y + cell.rowspan - 1 > maxY) {
				maxY = cell.y + cell.rowspan - 1;
			}

			cells.add(last = cell);
		}

		/*
		 * Go through our cells and determine the size of each row and column.
		 */
		int[] rows = new int[maxY + 1];
		for (int i = 0; i < rows.length; ++i) {
			rows[i] = 0;
		}

		int[] cols = new int[maxX + 1];
		for (int i = 0; i < cols.length; ++i) {
			cols[i] = 0;
		}

		/*
		 * Use the even distributed span cell to get this in one go.
		 * 
		 * If the total size of the two combined rows/columns can already accommodate
		 * it, do not increase the size of any of them!
		 * 
		 * XXX: This bit of code is a can of worms really. There is no perfect way to do
		 * it really, so I tried to imitate HTML tables as close as I care to.
		 * Unfortunately those documents say it can be done any number of ways, so WTH
		 * right?
		 */
		for (Cell cell : cells) {
			Dimension prefSize = cell.comp.getPreferredSize();
			Dimension dim = new Dimension(prefSize);
			if (cell.width >= 0) {
				dim.width = cell.width;
			}
			if (cell.height >= 0) {
				dim.height = cell.height;
			}
			if (cell.colspan != 1) {
				// Total size of the columns spanned
				int total = 0;
				for (int i = 0; i < cell.colspan; ++i) {
					total += cols[cell.x + i];
				}
				if (total < dim.width) {
					// Even distribute
					int width = dim.width / cell.colspan;
					for (int i = 0; i < cell.colspan; ++i) {
						if (cols[cell.x + i] < width) {
							cols[cell.x + i] = width;
						}
					}
				}
			} else {
				if (cols[cell.x] < dim.width) {
					cols[cell.x] = dim.width;
				}
			}

			if (cell.rowspan != 1) {
				// Total size of the rows spanned
				int total = 0;
				for (int i = 0; i < cell.rowspan; ++i) {
					total += rows[cell.y + i];
				}
				if (total < dim.height) {
					// Even distribute
					int height = dim.height / cell.rowspan;
					for (int i = 0; i < cell.rowspan; ++i) {
						if (rows[cell.y + i] < height) {
							rows[cell.y + i] = height;
						}
					}
				}
			} else {
				if (rows[cell.y] < dim.height) {
					rows[cell.y] = dim.height;
				}
			}
		}

		table.rows = rows;
		table.cols = cols;
		table.cells = cells.toArray(new Cell[cells.size()]);
		return table;
	}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return parent.getMaximumSize();
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();

		Table table = layoutTable(parent);

		int width = hgap * (table.cols.length - 1) + insets.left + insets.right;
		int height = vgap * (table.rows.length - 1) + insets.top + insets.bottom;

		/*
		 * Add up the size of the columns and rows
		 */
		for (int col : table.cols) {
			width += col;
		}

		for (int row : table.rows) {
			height += row;
		}

		return new Dimension(width, height);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		defined.remove(comp);
	}
}