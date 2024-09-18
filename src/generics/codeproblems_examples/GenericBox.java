package generics.codeproblems_examples;

public class GenericBox<T extends Comparable> {

    private T value;

    public GenericBox(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public int compareTo(GenericBox<T> otherBox){
        return  this.value.compareTo(otherBox.getValue());
    }

    public static void main(String[] args) {
        GenericBox<Integer> box1 = new GenericBox<>(10);
        GenericBox<Integer> box2 = new GenericBox<>(20);
        int comparison = box1.compareTo(box2);
        System.out.println(comparison);
        if(comparison<0){
            System.out.println("Box1 is less than Box2");
        } else if (comparison>0) {
            System.out.println("Box1 is greater than Box2");
        } else {
            System.out.println("Box1 is equal to Box2");
        }

        GenericBox<String> boxA = new GenericBox<>("Hi");
        GenericBox<String> boxB = new GenericBox<>("Hello");
        System.out.println(boxB.compareTo(boxA)); //-4; +4
    }
}
