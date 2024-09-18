package generics.codeproblems_examples;

public class GenericContainer<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void clear(){
        value = null;
    }

    public static void main(String[] args) {
        GenericContainer<String> stringGenericContainer = new GenericContainer<>();
        stringGenericContainer.setValue("Hello");
        System.out.println("Store value: "+stringGenericContainer.getValue());
        stringGenericContainer.clear();
        System.out.println("Container after clearing: "+stringGenericContainer.getValue());
    }

}
