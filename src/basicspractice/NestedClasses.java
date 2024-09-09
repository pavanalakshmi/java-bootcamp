package basicspractice;

class OuterClasses {
    int x=10;
    class InnerClasses {
        int y=20;
    }

    private class privateInnerClass{
        int z = 15;
    }

    static class staticInnerClass{
        int i = 5;
    }

}
public class NestedClasses {
    public static void main(String[] args) {
//        OuterClasses outerClasses = new OuterClasses();
//        OuterClasses.InnerClasses innerClasses = outerClasses.new InnerClasses();
//        System.out.println(outerClasses.x+" "+innerClasses.y);
        //        OuterClasses.privateInnerClass - error
        OuterClasses.staticInnerClass staticInnerClass = new OuterClasses.staticInnerClass();
        System.out.println(staticInnerClass.i);



    }
}
