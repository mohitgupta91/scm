package com.snapdeal.scm.preference;

/**
 * 
 * @author prateek
 *
 */
public abstract class PreferenceKey implements Cloneable {
	
	/**
	 * Override this method for deep cloning.
	 */
	@Override
	public PreferenceKey clone() throws CloneNotSupportedException {
		return (PreferenceKey)super.clone();
	}
	
	public abstract boolean equals(Object obj);
	
	public abstract int hashCode();
}