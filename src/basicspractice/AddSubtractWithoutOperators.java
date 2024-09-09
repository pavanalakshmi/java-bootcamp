package basicspractice;

public class AddSubtractWithoutOperators {
    public static void main(String[] args) {
        int n1 = 1; int n2=5;

        for(int i=0;i<n2;i++){
            n1 = n1 + 1;
        }
        System.out.println("Sum without directly adding them: "+n1);

        int t1 = 10; int t2 =5;

        for(int i = 0; i<t2; i++){
            t1 = t1 - 1;
        }
        System.out.println("Sum without directly adding them: "+t1);

    }
}
