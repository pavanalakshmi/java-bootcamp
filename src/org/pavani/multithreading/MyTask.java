package org.pavani.multithreading;

public class MyTask implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println(Thread.currentThread().getName()+"- Task "+i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                System.out.println("Thread interrupted");
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new MyTask());
        Thread t2 = new Thread(new MyTask());
        System.out.println(t1.getState());
        t1.start();
        t2.start();
        System.out.println(t1.getState());
    }
}
