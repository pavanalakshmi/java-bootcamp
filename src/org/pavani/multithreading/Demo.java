package org.pavani.multithreading;

//public class Demo extends Thread{
//    public void run(){
//        System.out.println("This block extends thread class");
//    }
//
//    public static void main(String[] args) {
//        Demo demo = new Demo();
//        demo.start(); // thread
//        System.out.println("Outside thread");
//    }
//
//}


public class Demo implements Runnable{
    public void run(){
        System.out.println("This block implements runnable interface");
    }
    public static void main(String[] args) {
        Demo demo = new Demo();
        Thread thread = new Thread();
        thread.start(); // thread
        System.out.println("Outside thread");
        demo.run();
    }

}
