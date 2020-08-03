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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

public class JsonToolkit {
	public static final JsonElement ints(Collection<Integer> data) {
		if (data.size() > 1) {
			JsonArray array = new JsonArray();
			for (Integer value : data) {
				array.add(value);
			}
			return array;
		} else if (data.size() > 0) {
			return new JsonPrimitive(data.iterator().next());
		}
		return JsonNull.INSTANCE;
	}

	public static final List<Integer> ints(JsonElement ele) {
		List<Integer> out = new ArrayList<Integer>();
		if (ele.isJsonArray()) {
			ele.getAsJsonArray().forEach((value) -> {
				out.add(value.getAsInt());
			});
		} else {
			out.add(ele.getAsInt());
		}
		return out;
	}

	public static final JsonElement strings(Collection<String> data) {
		if (data.size() > 1) {
			JsonArray array = new JsonArray();
			for (String value : data) {
				array.add(value);
			}
			return array;
		} else if (data.size() > 0) {
			return new JsonPrimitive(data.iterator().next());
		}
		return JsonNull.INSTANCE;
	}

	public static final List<String> strings(JsonElement ele) {
		List<String> out = new ArrayList<String>();
		if (ele.isJsonArray()) {
			ele.getAsJsonArray().forEach((value) -> {
				out.add(value.getAsString());
			});
		} else {
			out.add(ele.getAsString());
		}
		return out;
	}

	private JsonToolkit() {
	}
}
