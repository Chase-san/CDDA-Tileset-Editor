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
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class TilesetStub {
	public String name;
	public String view;
	public File file;
	public File json;

	public boolean load(File file) {
		try (Reader r = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {

			LineNumberReader lnr = new LineNumberReader(r);

			// NAME : ???
			// VIEW : ???
			// JSON : ???
			String line = null;
			while ((line = lnr.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("NAME")) {
					name = line.substring(line.indexOf(":") + 1).trim();
				} else if (line.startsWith("VIEW")) {
					view = line.substring(line.indexOf(":") + 1).trim();
				} else if (line.startsWith("JSON")) {
					line = line.substring(line.indexOf(":") + 1).trim();
					json = new File(file.getParentFile(), line).getAbsoluteFile();
				}
			}

			this.file = file;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		name = null;
		view = null;
		file = null;
		json = null;
		return false;
	}
}
