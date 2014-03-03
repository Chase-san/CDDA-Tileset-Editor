package org.csdgn.maru.util;

public class Tuple<E> {
	public E first;
	public E second;

	public Tuple() {

	}

	public Tuple(E left, E right) {
		this.first = left;
		this.second = right;
		//.hashCode()
	}
	
	public boolean isNull() {
		return first == null && second == null;
	}

	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o instanceof Tuple<?>) {
			Tuple<?> t = (Tuple<?>)o;
			return doesEqual(first,t.first) && doesEqual(second,t.second);
		}
		return false;
	}
	
	private boolean doesEqual(Object a, Object b) {
		if(a == b)
			return true;
		if(a == null || b == null)
			return false;
		return a.equals(b);
	}
	
	public int hashCode() {
		//simple hashcode, xor both entries hashCodes togeather.
		return (first == null ? 0 : first.hashCode()) ^
				(second == null ? 0 : second.hashCode());
	}
}
