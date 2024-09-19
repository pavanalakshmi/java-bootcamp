package mosh_java_basics.collections;

import java.util.Iterator;

public class GenericList<T> implements Iterable<T>{
    //    private T[] items = new T[10]; //compilation error - as java compiler doesn't know the type of T at this stage.(char class or user class or int).
    // so, we use Object array and cast it to T array.
    public T[] items = (T[]) new Object[10];
    private int count;

    public void add(T item){
        items[count++] = item;
    }

    public T get(int index){
        return items[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator(this);
    }

    private class ListIterator implements Iterator<T>{

        private GenericList<T> list;
        private int index;

        public ListIterator(GenericList<T> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return (index<list.count);
        }

        @Override
        public T next() {
            return list.items[index++];
        }
    }
}
