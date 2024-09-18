package generics.codeproblems_examples;

public class PrintElements {
    public static<T> void printArray(T[] array){
        for(T element:array){
            System.out.println(element+ " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] numbers = {1,2,3,4};
        printArray(numbers);

        String[] words = {"Apple", "Banana", "Pears", "Cherries"};
        printArray(words);
    }
}
