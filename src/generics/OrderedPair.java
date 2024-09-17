package generics;

public class OrderedPair<K,V> implements Pair<K,V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public K getKey() {
        return null;
    }
    @Override
    public V getValue() {
        return null;
    }

    public static void main(String[] args) {
        Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
        Pair<String, Integer> p2 = new OrderedPair<>("Even", 8); //Type-inference; autoboxing
        Pair<String, String > p3 = new OrderedPair<String, String>("Hello", "World");
    }
}
