package generics.codeproblems_examples;

public class GenericTriple<T1, T2, T3> {
    private T1 first;
    private T2 second;
    private T3 third;

    public GenericTriple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
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

    public T3 getThird() {
        return third;
    }

    public void setThird(T3 third) {
        this.third = third;
    }

    public static void main(String[] args) {
        GenericTriple<String, Integer, Boolean> triple = new GenericTriple<>("Hello", 1, true);
        System.out.println(triple.getFirst());
        System.out.println(triple.getSecond());
        System.out.println(triple.getThird());
    }
}
