1. Lets address how to use CHATGPT in the class  -- done 
2. Interfaces, Abstract Classes, static , final keyword  --
3. Array and String 
4. tests - general discussion 
5. mob programming - pair programming -- 
6. 
7. I want to write Palindrom Checking logic in Java and I don't want you to give my the java syntax for it and I don't want you to write the full program for it 
8. what is a palindrome, can you give me some generic, non-code based examples for a palindrome. 
9. i want to generate prime numbers between 1 to 100 (between two input numbers) . i have few questions, what is prime number, without giving me code, can we discuss how the logic will flow..



12: Calculator Requirements:

		- 5/2 should 2.5    - write test for this 4/0,  0/0, 0/3, 
		- 5*2 should return 10   - write test for this 
		- 5+2 should return 7 
		- 5-2 should return 3
		- calculator should be able to handle the strings with trailing and leading space, 
		- calculator should also be able to handle the strings with spaces in the expression to be evaluated 
  - input validations: 
    - calculator should accept only one string based expression as a valid input 
    - if you pass a non number operand 1, operand2, it should give error 
    - if you pass more than two operands and one operator, it should give error "Error: Invalid input format"   ( 10/2/5 = 1)
    - if you pass a operator other than + , - , / , * it should give error ""
    - if you pass empty string or null value, it should give error   "Error: Input is empty or null"
    - if you try to divide anythign by zero, it should say "Error: division by zero is not allowed"



- you can run the calculator from the command line using psvm method 
- or you can kick off calculator methods (add, subtract etc) from your @Test methods
  - in our case we went with command line option , but we have .calculate method , that we call from the test method s