package net.blacklab.lib.obj;

public interface Pair<K, V> {
	
	public K getKey();
	public void setKey(K s);
	
	public V getValue();
	public void setValue(V v);

}
