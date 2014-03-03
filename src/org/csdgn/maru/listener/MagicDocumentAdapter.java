package org.csdgn.maru.listener;

import java.lang.reflect.Method;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * I suppose this means I was feeling rather lazy one day.
 * 
 * @author Chase
 */
public class MagicDocumentAdapter implements DocumentListener {
	private Method fun;
	private Object obj;
	
	/** Method should take a single string element. */
	public MagicDocumentAdapter(Object obj, String methodName) {
		try {
			this.obj = obj;
			this.fun = obj.getClass().getMethod(methodName, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Method should take a single string element. */
	public MagicDocumentAdapter(Class<?> clz, String methodName) {
		try {
			this.fun = clz.getMethod(methodName, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Method should take a single string element. */
	public MagicDocumentAdapter(Object obj, Method m) {
		this.obj = obj;
		this.fun = m;
	}
	
	public void setObject(Object obj) {
		this.obj = obj;
	}
	
	private void invoke(DocumentEvent e) {
		try {
			Document doc = e.getDocument();
			String text = doc.getText(0, doc.getLength());
			fun.invoke(obj, text);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		invoke(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		invoke(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) { }
}
