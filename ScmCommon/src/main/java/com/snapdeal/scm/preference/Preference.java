package com.snapdeal.scm.preference;

/**
 * 
 * @author prateek
 *
 */
public abstract class Preference<T extends PreferenceKey> {
	
	public abstract void setPreferenceKey(T preferenceKey);
	
	public abstract void resetPreferenceKey(T originalPreferenceKey, T preferenceKey);

}
