package task;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("ALL")
public class FileScanner {
    //1.核心线程数：始终运行的线程数量
    //2.最大线程数：有新任务，并且当前运行线程数小于最大线程数，会创建新的线程来处理任务（正式+临时）
    //3-4.超过3这个数量，4这个时间单位，2-1（最大线程数-核心线程数）这些线程（临时）就会关闭
    //5.工作的阻塞队列
    //6.如果超出工作队列的常务，任务要处理的方式(4种策略)
    private static volatile AtomicInteger count= new AtomicInteger();

    private Object lock=new Object();

    private CountDownLatch latch=new CountDownLatch(1);//await()阻塞等待直到latch=0
    private Semaphore semaphore=new Semaphore(0);//acquire()阻塞等待一定数量的许可

    /*private ThreadPoolExecutor pool=new ThreadPoolExecutor(
            3,3,0, TimeUnit.MICROSECONDS,
            new LinkedBlockingQueue<>(),new ThreadPoolExecutor.AbortPolicy());*/
   //固定线程池，当然工作中禁止，问阿里
    private ExecutorService pool=Executors.newFixedThreadPool(4);
    private ScanCallback callback;

    public FileScanner(ScanCallback callback) {
        this.callback=callback;
    }

    /**
     * 未防止速度过慢（访问量过大），以前的思路（不执行）：看一下文件夹属性中的所占容量，如果过大直接停止运行
     重新选择的话，弹出一个确认窗口，确认就停止当前搜索
     * @param path
     */
    //多线程的任务等待
//最开始，不知道有多少子文件夹，不知道应该启动多少个线程
    public void scan(String path) {
       //递归使用线程池
        count.incrementAndGet();
        doScan(new File(path));
    }
    private void doScan(File dir){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.callback(dir);//文件保存操作
                    File[] children = dir.listFiles();//下一级文件和文件夹
                    if (children != null) {
                        for (File child : children) {
                            if (child.isDirectory()) {
                             //   System.out.println("文件夹" + child.getPath());
                                count.incrementAndGet();//启动子目录文件夹
                                doScan(child);
                            }
 //                           else {//如果是文件，待做的工作
                               // System.out.println("文件" + child.getPath());
                            //                          }
                        }
                    }
                }
                finally{
                    int r = count.decrementAndGet();
                    if (r == 0) {
                        //第一种
                     /*  synchronized (lock) {
                            lock.notify();
                        }
                      */
                     //第二种实现
                     latch.countDown();
                     //semaphore.release();
                    }
                }
            }
        });
    }

    //等待扫描任务
    //多线程的任务等待:thread.start();
    //1.join():需要使用线程Thread类的引用对象
    //2.wait()线程间的等待
    public void waitFinish() throws InterruptedException {
        //第一种实现
   /*     synchronized (lock){
            lock.wait();
        }

    */
   //第二种实现
        try {
            latch.await();
            // semaphore.acquire();
        }finally {
            //阻塞等待直到任务完成，完成后需要关闭线程池
           pool.shutdownNow();
        }
    }
    //线程池关闭
    public void shutdown(){
        //原理：通过内部Thread.interrupted方法停止
        //pool.shutdown();
        //Returns {@code true} if this executor has been shut down
          pool.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
   /*     Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        t.start();
        System.out.println(Thread.currentThread().getName());

*/
   /*
   Object obj=new Object();
   Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                        System.out.println(Thread.currentThread().getName());
                        obj.wait();
                    }
                }
        });
   t.start();
            synchronized (obj){
                obj.notify();
            }
*/
 Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        t2.start();
        t2.join();
        System.out.println(Thread.currentThread().getName());
    }

}
