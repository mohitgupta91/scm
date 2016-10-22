package com.snapdeal.scm.preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author prateek
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PreferenceHashMap<T extends PreferenceKey, V> {

	private HashMap<T, V> preferenceHashMap = new HashMap<>();
	private List<Preference> preferenceList = new ArrayList<>();

	public PreferenceHashMap(){

	}

	public HashMap<T, V> getPreferenceHashMap() {
		return preferenceHashMap;
	}

	public void setPreferenceHashMap(HashMap<T, V> preferenceHashMap) {
		this.preferenceHashMap = preferenceHashMap;
	}

	public void put(T preferenceKey, V preferenceValue) {
		this.preferenceHashMap.put(preferenceKey, preferenceValue);
	}

	public V get(T preferenceKey){
		try {
			PreferenceKey originalPreferenceKey = preferenceKey.clone();
			for(Preference preference : preferenceList){
				preference.setPreferenceKey(preferenceKey);
				V preferenceValue = preferenceHashMap.get(preferenceKey);
				if(preferenceValue != null){
					return preferenceValue;
				}
				preference.resetPreferenceKey(originalPreferenceKey, preferenceKey);
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Preference> getPreferenceList() {
		return preferenceList;
	}

	public void setPreferenceList(List<Preference> preferenceList) {
		this.preferenceList = preferenceList;
	}

	public void addPreference(Preference preference){
		this.preferenceList.add(preference);
	}
}
