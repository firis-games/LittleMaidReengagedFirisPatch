package net.blacklab.lib.obj;

import java.util.AbstractMap;




public class SinglePair<K, V> implements Pair<K, V> {
	
	private K key;
	private V value;
	
	public SinglePair() {
		setKey(null);
		setValue(null);
	}
	
	public SinglePair(K key, V value) {
		setKey(key);
		setValue(value);
	}

	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public Pair<K, V> setKey(K s) {
		this.key = s;
		return this;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public Pair<K, V> setValue(V v) {
		this.value = v;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SinglePair) {
			return key.equals(((SinglePair) obj).getKey()) && value.equals(((SinglePair) obj).getValue());
		}
		return false;
	}

}
