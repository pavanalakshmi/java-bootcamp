package collections;

import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Alex", 1);
        hashMap.put("Bob", 2);
        hashMap.put("Alice", 3);

        int id = 0;

        if(hashMap.containsKey("Bob")){
            id = hashMap.get("Bob");
        }
        System.out.println(id);
    }

}
