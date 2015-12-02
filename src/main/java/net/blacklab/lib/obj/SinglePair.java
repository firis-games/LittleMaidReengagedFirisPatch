package net.blacklab.lib.obj;

import java.util.Map.Entry;

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
	public K getKey() {
		return key;
	}

	@Override
	public void setKey(K s) {
		this.key = s;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public void setValue(V v) {
		this.value = v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SinglePair) {
			return key.equals(((SinglePair) obj).getKey()) && key.equals(((SinglePair) obj).getValue());
		}
		return super.equals(obj);
	}
	
}
