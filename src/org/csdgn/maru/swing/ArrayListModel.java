/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2019 Robert Maupin
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
import javax.swing.AbstractListModel;

public class ArrayListModel<E> extends AbstractListModel<E> {
	private static final long serialVersionUID = 5698969358046633170L;

	private final ArrayList<E> list;

	public ArrayListModel() {
		list = new ArrayList<E>();
	}

	public ArrayListModel(ArrayList<E> wrap) {
		list = wrap;
	}

	public ArrayList<E> getList() {
		return list;
	}

	public void add(E element) {
		int index = list.size();
		list.add(element);
		fireIntervalAdded(this, index, index);
	}

	public void remove(int index) {
		list.remove(index);
		fireIntervalRemoved(this, index, index);
	}

	public void remove(E e) {
		remove(list.indexOf(e));
	}

	public E get(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public E getElementAt(int index) {
		return list.get(index);
	}

	public void fireUpdate() {
		fireContentsChanged(this, 0, getSize());
	}
}
