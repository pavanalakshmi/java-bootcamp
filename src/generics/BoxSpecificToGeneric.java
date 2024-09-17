package generics;

public class BoxSpecificToGeneric<T> {
    private T object;
    public void set(T object){
        this.object = object;
    }
    public T get(){
        return object;
    }
}
