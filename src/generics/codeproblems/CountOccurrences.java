package generics.codeproblems;

//Write a generic method countOccurrences that counts the number of times a specific element appears in an array.
// The method should work with an array of any type.

public class CountOccurrences {
    public static void main(String[] args) {
        Integer[] numberArray = {1,2,2,1,3,2,1,3,3,4};
        String[] stringArray = {"Apple", "Banana", "Apple", "Pears"};
        String[] stringArray1 = {"Java", "Kafka", "null", "Spring", "Java", "null"};

        System.out.println(countOccurrences(numberArray,2));
        System.out.println(countOccurrences(stringArray,"Apple"));
        System.out.println(countOccurrences(stringArray1,"null"));
    }
    public static <T> int countOccurrences(T[] array, T element){
        int count = 0;
        for(int i=0;i<array.length;i++){
            if(element==null) {
                if (array[i] == null) {
                    count++;
                }
            } else if(array[i].toString().equalsIgnoreCase(element.toString())){
                count++;
            } else {
                if(element.equals(array[i])){
                    count++;
                }
            }
        }
        return count;
    }
}
