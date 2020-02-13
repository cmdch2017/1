import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    //多线程下线程安全的计数器
    private static volatile AtomicInteger count= new AtomicInteger();
    private Object lock=new Object();
    private ThreadPoolExecutor pool=new ThreadPoolExecutor(
            3,3,0, TimeUnit.MICROSECONDS,
            new LinkedBlockingQueue<>(),new ThreadPoolExecutor.CallerRunsPolicy());
    public void scan(String path) {
        //递归使用线程池
        count.incrementAndGet();
        doScan(new File(path));
    }

    private void doScan(File dir){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                File[] children=dir.listFiles();//下一级文件和文件夹
                if(children!=null){
                    for(File child:children){
                        if(child.isDirectory()) {
                            System.out.println("文件夹"+child.getPath());
                            count.incrementAndGet();
                            doScan(child);
                        }else{//如果是文件，待做的工作
                            //TODO
                            System.out.println("文件"+child.getPath());
                        }
                    }
                }
                int r=count.decrementAndGet();
                if(r==0){
                    synchronized (lock){
                        lock.notify();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        for(int i=0; i<20; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j<10000; j++){
                        count.incrementAndGet();//++i;
//                        count.getAndIncrement();//i++
                    }
                }
            }).start();
        }
        while(Thread.activeCount() > 1)
            Thread.yield();
        System.out.println(count.get());
    }
}
