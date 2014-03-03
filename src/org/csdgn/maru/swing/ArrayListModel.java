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
		fireContentsChanged(this,0,getSize());
	}
}
