package org.csdgn.maru.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;

public class MagicCheckboxAdapter implements ActionListener {
	private Method fun;
	private Object obj;
	
	/** Method should take a single string element. */
	public MagicCheckboxAdapter(Object obj, String methodName) {
		this.obj = obj;
		try {
			this.fun = obj.getClass().getMethod(methodName, boolean.class);
		} catch (NoSuchMethodException e) {
			try {
				this.fun = obj.getClass().getMethod(methodName, Boolean.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Method should take a single string element. */
	public MagicCheckboxAdapter(Class<?> clz, String methodName) {
		try {
			this.fun = clz.getMethod(methodName, boolean.class);
		} catch (NoSuchMethodException e) {
			try {
				this.fun = clz.getMethod(methodName, Boolean.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Method should take a single string element. */
	public MagicCheckboxAdapter(Object obj, Method m) {
		this.obj = obj;
		this.fun = m;
	}
	
	public void setObject(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			JCheckBox jchbx = (JCheckBox)e.getSource();
			boolean select = jchbx.isSelected();
			fun.invoke(obj, select);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
