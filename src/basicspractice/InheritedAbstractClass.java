package basicspractice;

class InheritedAbstractClass extends AbstractClass {
    @Override
    public void abstractmethod() {
        System.out.println("Abstract method implementation in inherited class");
    }

    public static void main(String[] args) {
        InheritedAbstractClass inheritedAbstractClass = new InheritedAbstractClass();
        inheritedAbstractClass.abstractmethod();
        inheritedAbstractClass.regularmethod();
    }
}