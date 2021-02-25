public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingMap map = new BlockingMap();
        map.add("Liza", 19);
        map.add("Kirill", 21);
        map.add("Vlad", 20);
        map.add("Arsen", 9999);
        System.out.println( map.get("Kirill"));
        map.remove("Liza");
        map.set("Arsen",20);
        System.out.println(map.get("Arsen"));

        MyThread t1 = new MyThread( (Runnable) () -> {
            map.add("Oleg", 124);

            map.add("Nastya", 24);
        });
        MyThread t2 = new MyThread( (Runnable) () -> {
            map.add("Katya", 235235);
            map.set("Kirill", 222222);
        });

        t2.start();
        t1.start();
        t1.join();
        t2.join();
        System.out.println("da");

        map.set("Vasya", 0);

        MyThread t3 = new MyThread( (Runnable) () -> {
            int i = 0;
            while (i++<100)
                map.set("Vasya", (int)map.get("Vasya")+1);

        });
        MyThread t4 = new MyThread( (Runnable) () -> {

            int i = 0;
            while (i++<100) {
                map.set("Vasya", (int)map.get("Vasya")-1);

            }

        });


        t3.start();
        t4.start();
        t3.join();
        t4.join();
        System.out.println(map.get("Vasya"));
    }




}
class  MyThread extends Thread
{

    Runnable runnable = null;
    public MyThread(  java.lang.Runnable runnable)
    {
        this.runnable = runnable;

    }

    @Override
    public void run() {
        runnable.run();

    }
}