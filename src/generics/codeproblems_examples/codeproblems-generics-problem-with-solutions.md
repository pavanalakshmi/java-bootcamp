## GENERIC CLASS EXAMPLES

### **Problem: Generic Container**

**Objective**:  
Create a generic class `Container` that can hold a single object of any type. The class should provide methods to set, get, and clear the value stored in the container.

---

### **Detailed Requirements**

1. **Class Declaration**:
   
   - **Generic Type Parameter**: The class should be generic with a type parameter `T`.
   
   ```java
   public class Container<T> {
       // Class body
   }
   ```
   
2. **Fields**:
   - **Value**: A private field to store the object of type `T`.

   ```java
   private T value;
   ```

3. **Methods**:
   - **setValue(T value)**: Sets the value in the container.
   - **getValue()**: Returns the value stored in the container.
   - **clear()**: Clears the value stored in the container (sets it to `null`).

   ```java
   public void setValue(T value) {
       this.value = value;
   }
   
   public T getValue() {
       return value;
   }
   
   public void clear() {
       value = null;
   }
   ```

4. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Container<String> stringContainer = new Container<>();
           stringContainer.setValue("Hello");
           System.out.println("Stored Value: " + stringContainer.getValue()); // Output: Hello
           
           stringContainer.clear();
           System.out.println("Stored Value after clear: " + stringContainer.getValue()); // Output: null
       }
   }
   ```

---

### **Problem: Generic Triple**

**Objective**:  
Create a generic class `Triple` that can hold three objects of any type. The class should provide methods to get each of the three values.

---

### **Detailed Requirements**

1. **Class Declaration**:
   - **Generic Type Parameters**: The class should be generic with three type parameters `T1`, `T2`, and `T3`.

   ```java
   public class Triple<T1, T2, T3> {
       // Class body
   }
   ```

2. **Fields**:
   - **Three Values**: Private fields to store the three objects of types `T1`, `T2`, and `T3`.

   ```java
   private T1 first;
   private T2 second;
   private T3 third;
   ```

3. **Constructor**:
   - **Parameters**: Accept three objects as parameters and initialize the fields.

   ```java
   public Triple(T1 first, T2 second, T3 third) {
       this.first = first;
       this.second = second;
       this.third = third;
   }
   ```

4. **Methods**:
   - **getFirst()**: Returns the first value.
   - **getSecond()**: Returns the second value.
   - **getThird()**: Returns the third value.

   ```java
   public T1 getFirst() {
       return first;
   }
   
   public T2 getSecond() {
       return second;
   }
   
   public T3 getThird() {
       return third;
   }
   ```

5. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Triple<Integer, String, Boolean> triple = new Triple<>(1, "Hello", true);
           System.out.println("First: " + triple.getFirst()); // Output: 1
           System.out.println("Second: " + triple.getSecond()); // Output: Hello
           System.out.println("Third: " + triple.getThird()); // Output: true
       }
   }
   ```

---

### **Problem: Generic Pair**

**Objective**:  
Create a generic class `Pair` that can hold a pair of objects of any type. The class should provide methods to get and set each of the two values.

---

### **Detailed Requirements**

1. **Class Declaration**:
   - **Generic Type Parameters**: The class should be generic with two type parameters `T1` and `T2`.

   ```java
   public class Pair<T1, T2> {
       // Class body
   }
   ```

2. **Fields**:
   - **Two Values**: Private fields to store the two objects of types `T1` and `T2`.

   ```java
   private T1 first;
   private T2 second;
   ```

3. **Constructor**:
   - **Parameters**: Accept two objects as parameters and initialize the fields.

   ```java
   public Pair(T1 first, T2 second) {
       this.first = first;
       this.second = second;
   }
   ```

4. **Methods**:
   - **getFirst()**: Returns the first value.
   - **getSecond()**: Returns the second value.
   - **setFirst(T1 value)**: Sets the first value.
   - **setSecond(T2 value)**: Sets the second value.

   ```java
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
   ```

5. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Pair<String, Integer> pair = new Pair<>("Age", 25);
           System.out.println("First: " + pair.getFirst()); // Output: Age
           System.out.println("Second: " + pair.getSecond()); // Output: 25
           
           pair.setFirst("New Age");
           pair.setSecond(26);
           System.out.println("Updated First: " + pair.getFirst()); // Output: New Age
           System.out.println("Updated Second: " + pair.getSecond()); // Output: 26
       }
   }
   ```

---

### **Problem: Generic Box**

**Objective**:  
Create a generic class `Box` that can hold an object of any type and provide a method to compare the current value in the box with another value of the same type using the `compareTo` method (assuming the type `T` implements `Comparable`).

---

### **Detailed Requirements**

1. **Class Declaration**:
   - **Generic Type Parameter**: The class should be generic with a type parameter `T` that extends `Comparable<T>`.

   ```java
   public class Box<T extends Comparable<T>> {
       // Class body
   }
   ```

2. **Fields**:
   - **Value**: A private field to store the object of type `T`.

   ```java
   private T value;
   ```

3. **Constructor**:
   - **Parameters**: Accept an object of type `T` as a parameter and initialize the field.

   ```java
   public Box(T value) {
       this.value = value;
   }
   ```

4. **Methods**:
   - **getValue()**: Returns the value stored in the box.
   - **compareTo(Box<T> otherBox)**: Compares the value in this box with the value in another `Box` of the same type.

   ```java
   public T getValue() {
       return value;
   }
   
   public int compareTo(Box<T> otherBox) {
       return this.value.compareTo(otherBox.getValue());
   }
   ```

5. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Box<Integer> box1 = new Box<>(10);
           Box<Integer> box2 = new Box<>(20);
           
           int comparison = box1.compareTo(box2);
           if (comparison < 0) {
               System.out.println("Box1 is less than Box2");
           } else if (comparison > 0) {
               System.out.println("Box1 is greater than Box2");
           } else {
               System.out.println("Box1 is equal to Box2");
           }
       }
   }
   ```

---

These problems are simpler and focus on fundamental generic concepts such as encapsulation, type safety, and basic operations on generic types. 







## GENERIC METHODS EXAMPLES 





Here are a few examples of problems involving generic methods that can help students practice and understand how to create and use generic methods in Java:

---

### **Problem 1: Generic Method to Find Maximum**

**Objective**:  
Write a generic method `findMaximum` that accepts three parameters of the same type and returns the maximum of the three. The method should work with any type that implements the `Comparable` interface.

---

### **Detailed Requirements**

1. **Method Declaration**:
   - **Generic Type Parameter**: The method should be generic with a type parameter `T` that extends `Comparable<T>`.

   ```java
   public static <T extends Comparable<T>> T findMaximum(T a, T b, T c) {
       T max = a;
       if (b.compareTo(max) > 0) {
           max = b;
       }
       if (c.compareTo(max) > 0) {
           max = c;
       }
       return max;
   }
   ```

2. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           System.out.println(findMaximum(3, 7, 5)); // Output: 7
           System.out.println(findMaximum("apple", "banana", "pear")); // Output: pear
       }
   }
   ```

---

### **Problem 2: Generic Method to Swap Elements in an Array**

**Objective**:  
Write a generic method `swapElements` that swaps the positions of two elements in an array. The method should accept the array and the two indices to swap.

---

### **Detailed Requirements**

1. **Method Declaration**:
   - **Generic Type Parameter**: The method should be generic with a type parameter `T`.

   ```java
   public static <T> void swapElements(T[] array, int index1, int index2) {
       T temp = array[index1];
       array[index1] = array[index2];
       array[index2] = temp;
   }
   ```

2. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Integer[] numbers = {1, 2, 3, 4};
           swapElements(numbers, 1, 3);
           System.out.println(Arrays.toString(numbers)); // Output: [1, 4, 3, 2]
           
           String[] words = {"apple", "banana", "cherry"};
           swapElements(words, 0, 2);
           System.out.println(Arrays.toString(words)); // Output: [cherry, banana, apple]
       }
   }
   ```

---

### **Problem 3: Generic Method to Print Elements of an Array**

**Objective**:  
Write a generic method `printArray` that prints all the elements of an array. The method should work with an array of any type.

---

### **Detailed Requirements**

1. **Method Declaration**:
   - **Generic Type Parameter**: The method should be generic with a type parameter `T`.

   ```java
   public static <T> void printArray(T[] array) {
       for (T element : array) {
           System.out.print(element + " ");
       }
       System.out.println();
   }
   ```

2. **Usage Example**:

   ```java
   public class org.pavani.Main {
       public static void main(String[] args) {
           Integer[] numbers = {1, 2, 3, 4};
           printArray(numbers); // Output: 1 2 3 4 
           
           String[] words = {"apple", "banana", "cherry"};
           printArray(words); // Output: apple banana cherry 
       }
   }
   ```

---

These examples are simpler and focus on fundamental operations using generic methods. They allow students to get comfortable with the syntax and concepts of generics in Java, such as type safety, reusability, and working with different types without writing multiple methods for each type.