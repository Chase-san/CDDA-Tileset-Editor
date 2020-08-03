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
package org.csdgn.maru.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ArraySet<E> extends ArrayList<E> implements Set<E>, List<E> {
	private static final long serialVersionUID = 2055331278263513884L;

	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException("This set does not support null elements.");
		}
		if (!this.contains(e)) {
			return super.add(e);
		}

		return false;
	}

	@Override
	public void add(int index, E e) {
		if (e == null) {
			throw new NullPointerException("This set does not support null elements.");
		}
		if (this.contains(e)) {
			throw new RuntimeException("Elements in the set must be unique.");
		}
		super.add(index, e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c == null) {
			throw new NullPointerException("Argument is null.");
		}
		boolean changed = false;
		for (E e : c) {
			if (add(e)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (c == null) {
			throw new NullPointerException("Argument is null.");
		}
		boolean changed = false;
		for (E e : c) {
			if (e == null) {
				throw new NullPointerException("This set does not support null elements.");
			}
			if (!this.contains(e)) {
				add(index++, e);
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Do this later
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Do this later
		return null;
	}

	@Override
	public E set(int index, E e) {
		if (e == null) {
			throw new NullPointerException("This set does not support null elements.");
		}
		if (this.contains(e)) {
			throw new RuntimeException("Elements in the set must be unique.");
		}
		return super.set(index, e);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Do this later
		return null;
	}

}
