package com.snapdeal.scm.preference;

/**
 * 
 * @author prateek
 *
 */
public class TestPreferenceHashMap {

	public static void main(String[] args) {
		PreferenceHashMap<Key, Value> preferenceHashMap = new PreferenceHashMap<Key, Value>();
		preferenceHashMap.put(new Key("panipat", "Haryana", "GoJava", null),new Value(1)); // first
		preferenceHashMap.put(new Key("panipat", "Haryana", "BD", null),new Value(2)); // first
		preferenceHashMap.put(new Key("panipat", null, "GoJava", null),new Value(3)); // third
		preferenceHashMap.put(new Key("Ghaziabad", null, "BD", null),new Value(4)); // third
		preferenceHashMap.put(new Key("panipat", "Haryana", null, "Backward"),new Value(5)); // second
		preferenceHashMap.put(new Key("panipat", "Haryana", null, "Forward"),new Value(6)); // second
		preferenceHashMap.put(new Key("panipat", null, null, "Forward"),new Value(7)); // forth
		preferenceHashMap.put(new Key("panipat", null, null, "Backward"),new Value(8)); // forth
		preferenceHashMap.put(new Key("panipat", "Haryana", null, null),new Value(9)); // fifth
		preferenceHashMap.put(new Key("Ghaziabad", "UP", null, null),new Value(10)); // fifth
		preferenceHashMap.put(new Key("panipat", null, null, null),new Value(11)); // sixth
		preferenceHashMap.put(new Key("Ghaziabad", null, null, null),new Value(12)); // sixth
		
		preferenceHashMap.addPreference(new PreferenceOne());
		preferenceHashMap.addPreference(new PreferenceTwo());
		preferenceHashMap.addPreference(new PreferenceThree());
		preferenceHashMap.addPreference(new PreferenceFour());
		preferenceHashMap.addPreference(new PreferenceFive());
		preferenceHashMap.addPreference(new PreferenceSix());
		
		Key key = new Key("panipat", "Haryana", "BD", "Forward");
		Value value = (Value) preferenceHashMap.get(key);
		System.out.println(value.getValue());
	}

}

class Value {
	Integer value;

	public Value(Integer value) {
		super();
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Value other = (Value) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Value [value=" + value + "]";
	}
}

class Key extends PreferenceKey {

	private String first;
	private String second;
	private String third;
	private String forth;

	public Key() {
		super();
	}

	public Key(String first, String second, String third, String forth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.forth = forth;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getThird() {
		return third;
	}

	public void setThird(String third) {
		this.third = third;
	}

	public String getForth() {
		return forth;
	}

	public void setForth(String forth) {
		this.forth = forth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((forth == null) ? 0 : forth.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (forth == null) {
			if (other.forth != null)
				return false;
		} else if (!forth.equals(other.forth))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Key [first=" + first + ", second=" + second + ", third="
				+ third + ", forth=" + forth + "]";
	}
}

class PreferenceOne extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setForth(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setForth(originalPreferenceKey.getForth());
	}
}

class PreferenceTwo extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setThird(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setThird(originalPreferenceKey.getThird());
	}
}

class PreferenceThree extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setSecond(null);
		preferenceKey.setForth(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setSecond(originalPreferenceKey.getSecond());
		preferenceKey.setForth(originalPreferenceKey.getForth());
	}
}

class PreferenceFour extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setSecond(null);
		preferenceKey.setThird(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setSecond(originalPreferenceKey.getSecond());
		preferenceKey.setThird(originalPreferenceKey.getThird());
	}
}


class PreferenceFive extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setThird(null);
		preferenceKey.setForth(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setThird(originalPreferenceKey.getThird());
		preferenceKey.setForth(originalPreferenceKey.getForth());
	}
}

class PreferenceSix extends Preference<Key>{

	@Override
	public void setPreferenceKey(Key preferenceKey) {
		preferenceKey.setSecond(null);
		preferenceKey.setThird(null);
		preferenceKey.setForth(null);
	}

	@Override
	public void resetPreferenceKey(Key originalPreferenceKey, Key preferenceKey) {
		preferenceKey.setSecond(originalPreferenceKey.getSecond());
		preferenceKey.setThird(originalPreferenceKey.getThird());
		preferenceKey.setForth(originalPreferenceKey.getForth());
	}
}