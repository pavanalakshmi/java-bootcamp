

--------

### **Problem Statement: Bracket Matching without Using a Stack**

#### **Objective:**

Design and implement a Java program that checks whether an expression containing various types of brackets (parentheses `()`, square brackets `[]`, and curly braces `{}`) is properly balanced. The challenge is to accomplish this without using a stack data structure.

#### **Requirements:**

1. **Bracket Types:**
   - The expression will contain three types of brackets: parentheses `()`, square brackets `[]`, and curly braces `{}`.

2. **Balanced Expression Criteria:**
   - A balanced expression is one in which every opening bracket has a corresponding closing bracket of the same type, and the brackets are correctly nested. For example, `{[()]}` is balanced, while `{[(])}` is not.

3. **Input:**
   - The program should accept a string consisting of only these three types of brackets.

4. **Output:**
   - The program should return `true` if the input string is balanced according to the standard rules, and `false` otherwise.

5. **Constraints:**
   - **No Stack**: The solution must be implemented without using a stack. Instead, consider alternative approaches, such as using counters.

6. **Core Functionalities:**
   - **Bracket Matching Logic**: Implement logic to match opening and closing brackets without relying on a stack. You may use counters for each type of bracket or simulate the stack behavior using other methods.
   - **Correct Nesting**: Ensure that the solution can correctly handle nested brackets, not just counting them but ensuring they are properly ordered.
   - **Edge Cases**: Handle edge cases such as an empty string, single bracket, and mismatched brackets correctly.

7. **Examples:**
   - Input: `{[()]}`, Output: `true` (balanced)
   - Input: `{[(])}`, Output: `false` (not balanced)
   - Input: `[]`, Output: `true` (balanced)
   - Input: `{[}`, Output: `false` (not balanced)

8. **Scenario Example:**

Imagine you are tasked with developing a simple syntax checker for a custom configuration language where brackets are used to group settings. Due to certain constraints, you are not allowed to use a stack to validate the grouping. You need to ensure that all groupings are correctly nested and balanced using an alternative method.

9. **Outcome:**

Students should be able to:

- Implement a solution to the bracket matching problem without relying on a stack.
- Explore alternative data structures or algorithms to achieve the same goal.
- Ensure the solution correctly identifies balanced and unbalanced bracket sequences and handles various edge cases effectively.

-------------

### **Problem Statement: Custom Balanced Brackets Validator with Stack**

#### **Objective:**

Design and implement a Java program that uses a stack to validate expressions containing various types of brackets. Unlike the standard balanced bracket problem, this problem allows certain "twisted" bracket sequences that would traditionally be considered unbalanced. Specifically, the sequences `{[(])}`, `{[()]}`, and other similar patterns should be considered valid.

#### **Requirements:**

1. **Bracket Types:**
   - The expression will contain three types of brackets: parentheses `()`, square brackets `[]`, and curly braces `{}`.

2. **Valid Sequences:**
   - The standard rules for balanced brackets apply, where every opening bracket must have a corresponding closing bracket, and they must be properly nested. 
   - However, in this twist, certain "twisted" sequences like `{[(])}` and `{[()]}` are also considered valid.
   - Any expression that doesn't follow these patterns or is unbalanced (e.g., `{[}`) should be considered invalid.

3. **Input:**
   - The program should accept a string consisting of only these three types of brackets.

4. **Output:**
   - The program should return `true` if the input string is considered valid according to the custom rules, and `false` otherwise.

5. **Core Functionalities:**
   - **Stack Usage**: Use a stack to process the brackets. Push opening brackets onto the stack. For closing brackets, check for a valid match or an allowed "twist" sequence.
   - **Custom Validation Rules**: Implement logic to recognize and validate both standard and twisted sequences.
   - **Edge Cases**: Handle edge cases such as an empty string, single bracket, and mismatched brackets correctly.

6. **Examples:**
   - Input: `{[(])}`, Output: `true` (valid due to the custom rule)
   - Input: `{[()]}`, Output: `true` (valid due to standard balancing)
   - Input: `{[}`, Output: `false` (invalid, not balanced)
   - Input: `{[(])}()`, Output: `true` (valid, combination of twisted and standard sequences)
   - 

7. **Scenario Example:**

Imagine you are developing a tool that parses and validates complex mathematical or logical expressions. The expressions may contain different types of brackets used for grouping. However, due to certain legacy systems or specific mathematical notations, some traditionally unbalanced sequences like `{[(])}` are considered acceptable and must be recognized as valid by your validation tool.

8. **Outcome:**

Students should be able to:

- Implement a stack-based solution to validate both standard and twisted bracket sequences.
- Develop a custom validation logic that recognizes and allows specific sequences that would otherwise be considered unbalanced.
- Handle edge cases and ensure that the solution is both efficient and correct for a variety of input scenarios.









### Problem Statement: Implement a Custom `ArrayList` in Java Using Arrays

You are required to implement a custom version of the `ArrayList` in Java, called `MyArrayList`. This custom list should provide basic CRUD (Create, Read, Update, Delete) operations, dynamic resizing of the underlying array, and other commonly used list features. Your implementation must closely follow the behavior of Java's `ArrayList`, but it will use a plain Java array to store the elements internally.

### Requirements:

#### 1. Class Design:
You need to create a class named `MyArrayList` that should work with generic types (i.e., it can hold any type of object). This class must have the following functionalities:

#### 2. Constructor:
- `MyArrayList()`: Initializes the list with a default capacity (e.g., 10).
- `MyArrayList(int capacity)`: Initializes the list with a user-defined capacity.

#### 3. Methods:
You must implement the following methods in `MyArrayList`:

1. **`add(T element)`**: 
   - Adds an element to the end of the list.
   - If the internal array is full, the list should automatically double its size to accommodate more elements.

2. **`get(int index)`**:
   - Returns the element at the specified index.
   - If the index is out of bounds (less than 0 or greater than or equal to the size of the list), throw an `IndexOutOfBoundsException`.

3. **`remove(int index)`**:
   - Removes the element at the specified index.
   - After removing the element, shift all the subsequent elements to fill the gap.
   - If the index is out of bounds, throw an `IndexOutOfBoundsException`.
   - Return the element that was removed.

4. **`size()`**:
   - Returns the current number of elements in the list.

5. **`clear()`**:
   - Clears the list by removing all elements and resetting the internal array to the default capacity.

6. **`isEmpty()`**:
   - Returns `true` if the list contains no elements; otherwise, returns `false`.

7. **`toString()`**:
   - Override the `toString()` method to return a string representation of the list in the format: `[element1, element2, ...]`.

8. **`resize()`** (Helper Method):
   - A private method to dynamically resize the internal array when it's full. The array should grow by doubling its size when capacity is reached.

#### 4. Constraints:
- The `MyArrayList` should only use Java arrays (e.g., `Object[]`) internally to store the elements.
- Do not use any Java collection classes such as `ArrayList` or `LinkedList` to implement your solution.
- Use generics (`<T>`) so that `MyArrayList` can hold any type of object.
- Ensure that the `remove(int index)` and `get(int index)` methods perform boundary checks to prevent accessing or removing elements at invalid indices.
- Ensure efficient use of memory by resizing the array only when necessary.

### Example:

```java
public class Main {
    public static void main(String[] args) {
        MyArrayList<String> list = new MyArrayList<>();

        // Adding elements
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        // Print the list
        System.out.println("List: " + list); // Output: [Apple, Banana, Cherry]

        // Get an element
        System.out.println("Element at index 1: " + list.get(1)); // Output: Banana

        // Remove an element
        list.remove(1);
        System.out.println("After removal: " + list); // Output: [Apple, Cherry]

        // Check the size
        System.out.println("Size: " + list.size()); // Output: 2

        // Clear the list
        list.clear();
        System.out.println("After clear: " + list.isEmpty()); // Output: true
    }
}
```

### Additional Requirements:
- **Resizing**: When the array reaches its capacity, the array should automatically double in size.
- **Error Handling**: Properly handle cases where invalid indices are used for the `get` or `remove` methods by throwing `IndexOutOfBoundsException`.
- **Efficiency**: Focus on making the addition of elements efficient in terms of memory and performance by resizing the array only when needed.

### Submission:

Submit the following:
- The source code for your `MyArrayList` class with all methods properly implemented.
- A test class that demonstrates all the CRUD operations on `MyArrayList` and validates its correctness by adding, retrieving, updating, and removing elements from the list.

---

This problem requires you to understand how to manage a dynamic array manually, which is a fundamental concept in data structures. You'll learn how to handle capacity changes, manage array elements, and implement the basic operations that make Java's `ArrayList` so powerful.

### Updated Problem Statement: Implement a LinkedList in Java</u>

You are required to implement a custom **LinkedList** class in Java that mimics the behavior of a singly linked list. A singly linked list is a data structure where each element (node) points to the next element in the sequence, with the last node pointing to `null`.

Your implementation should:
1. **Support dynamic resizing**: The list should be able to grow dynamically as elements are added.

2. **Implement basic linked list operations**: These operations should be available:
   - **Add an element to the end of the list**.
   - **Add an element to the start of the list**.
   - **Remove an element from the start of the list**.
   - **Remove an element from the end of the list**.
   - **Print all elements in the list**.
   
3. **Handle edge cases**:
   - The list should be able to handle cases where operations are attempted on an empty list (e.g., removing from an empty list).
   
4. **Track the size of the list**: The size of the list should be maintained and returned when requested.

5. **Check if the list is empty**: Provide a method to check whether the list is empty.

   

### Detailed Requirements:
1. **Node Class**:
   - Each element in the list should be represented by a **Node** object that stores:
     - The data (integer value).
     - A reference (pointer) to the next node in the list.
2. **LinkedList Class**:
   - Should manage the head of the list (the first node) and maintain the size of the list.
   - Implement methods to manipulate the list, as specified.
3. **Error Handling**:
   - If an operation such as `removeFirst` or `removeLast` is attempted on an empty list, an appropriate error (exception) should be thrown.

### Example Scenario:

You are tasked with creating a custom linked list to store a sequence of integers. You will need to:
- Add numbers 10, 20, and 30 to the end of the list.
- Add a number 5 to the start of the list.
- Remove the first element, check the size of the list, and confirm that the correct value was removed.
- Print all the values in the list after these operations, ensuring the order of elements is maintained as expected.

### Constraints:
- The solution should use **singly linked list** structure.
- The list should not use any built-in collection classes like `ArrayList` or `LinkedList`.





### <u>Problem Statement: Implement a Custom `HashMap` in Java</u>

You are tasked with implementing a custom version of the `HashMap` class in Java, called `MyHashMap`. This class will store key-value pairs and provide basic operations such as adding, retrieving, removing, and checking for keys. Additionally, you are required to implement collision resolution using **chaining with linked lists**. The internal structure will use an array of buckets where each bucket is a linked list that stores key-value pairs in the event of a hash collision.

### Detailed Problem Breakdown

1. **Basic Structure**: The `MyHashMap` class will have the following components:

   - **Array of Buckets**: Internally, the `HashMap` will use an array where each element (bucket) is a linked list of key-value pairs. Each key-value pair is stored as a node in the linked list.
   - **Node Class**: A helper class (called `Node`) will represent a key-value pair and contain a reference to the next node, allowing you to chain key-value pairs in case of collisions.

2. **Core Operations**:

   - **Put (Add/Update)**: Insert a key-value pair into the map. If the key already exists, update its value.
   - **Get (Retrieve)**: Retrieve the value associated with a given key.
   - **Remove**: Remove the key-value pair from the map based on the key.
   - **ContainsKey**: Check if the map contains a specific key.
   - **Size**: Return the number of key-value pairs in the map.

3. **Collision Resolution with Chaining**:

   - **Hash Collision**: When two keys hash to the same index in the bucket array, store the key-value pairs in a linked list at that bucket index.
   - **Chaining**: Use a linked list to handle collisions. Multiple key-value pairs that hash to the same index will be linked together.

4. **Dynamic Resizing (Optional)**: You can optionally resize the bucket array when the load factor (number of elements relative to bucket array size) exceeds a certain threshold. For simplicity, this resizing step is optional for the base implementation.

5. **Hash Function**:

   - **`hashCode()`**: Use the `hashCode()` method provided by Java's `Object` class to generate a hash value for the key.
   - **Modulo Operation**: Use the modulo operator (`%`) to map the hash code to a valid index in the bucket array (to ensure the index falls within the array's bounds).

6. **Null Key Handling** (Optional): Optionally, allow `null` as a key, which will always be stored at a specific bucket index (e.g., index `0`).

   ### Explanation of Key Components:

   1. **`Node<K, V>` Class**:
      - Represents an individual key-value pair (node) in a linked list.
      - Each node contains a `key`, `value`, and `next` pointer to the next node (to handle collisions using chaining).
   2. **Bucket Array**:
      - The `buckets` array is an array of `Node<K, V>` references, where each element is the head of a linked list.
      - Each bucket represents a slot in the `HashMap` that can hold one or more key-value pairs (in case of collisions).
   3. **Collision Handling Using Chaining**:
      - When a collision occurs (i.e., two keys hash to the same bucket index), the key-value pairs are stored in a linked list within the same bucket.
      - If a key already exists in the linked list (within the same bucket), its value is updated.
   4. **`hashCode()` and Bucket Indexing**:
      - The `hashCode()` method of the key is used to generate a hash value.
      - The modulo operation (`%`) is used to map the hash value to a bucket index in the array.
   5. **Operations**:
      - **Put**: Adds a key-value pair to the `HashMap`. If a collision occurs, the new node is added to the linked list.
      - **Get**: Retrieves the value associated with the key by traversing the linked list in the appropriate bucket.
      - **Remove**: Removes a key-value pair from the map by unlinking the node in the linked list (if it exists).
      - **ContainsKey**: Checks whether a key is present in the map.
      - **Size**: Returns the current number of key-value pairs in the map.

   ### Handling Collisions with Chaining:

   - When a key is inserted, the hash code is computed, and the corresponding bucket index is determined.

   - If a key-value pair already exists in the bucket, a new node is added to the linked list (chaining).

   - When retrieving or removing a key, the linked list is traversed to find the correct node.

   - ### Key Notes:

     1. **Chaining**: When multiple keys hash to the same index, chaining is used to resolve collisions.
     2. **HashCode Calculation**: The `hashCode()` function is crucial for determining the correct bucket index.

     This implementation provides a basic `HashMap` functionality with key features like handling collisions, chaining, and dynamic insertion/removal of elements.

