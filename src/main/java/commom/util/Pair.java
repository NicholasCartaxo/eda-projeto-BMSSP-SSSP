package main.java.commom.util;

public class Pair<K, V> implements Comparable<Pair<K, V>> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(Pair<K, V> o) {
        return ((Comparable<V>) this.value).compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Pair<K,V> other = (Pair<K,V>) obj;
        if (this.key != other.getKey()) return false;
        return true;
    }

    @Override
    public int hashCode(){
        return this.key.hashCode();
    }
}
