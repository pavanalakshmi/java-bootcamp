package basicspractice;

public class InterfaceImplementation implements InterfaceExample {
    @Override
    public void interfaceMethod() {
        System.out.println("From interface");
    }

    @Override
    public void interfaceMethod2() {
        System.out.println("From interface 2");
    }

    public static void main(String[] args) {
        InterfaceImplementation interfaceImplementation = new InterfaceImplementation();
        interfaceImplementation.interfaceMethod();
        interfaceImplementation.interfaceMethod2();
    }
}
