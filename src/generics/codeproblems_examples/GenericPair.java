package generics.codeproblems_examples;

public class GenericPair <T1, T2> {

    private T1 first;
    private T2 second;

    public GenericPair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public void setFirst(T1 first) {
        this.first = first;
    }

    public T2 getSecond() {
        return second;
    }

    public void setSecond(T2 second) {
        this.second = second;
    }

    public static void main(String[] args) {
        GenericPair<String, Integer> pair = new GenericPair<>("Age", 26);
        System.out.println(pair.getFirst());
        System.out.println(pair.getSecond());
        pair.setFirst("New Age");
        pair.setSecond(25);
        System.out.println("Updated string: "+pair.getFirst());
        System.out.println("Updated age: "+pair.getSecond());
    }
}
