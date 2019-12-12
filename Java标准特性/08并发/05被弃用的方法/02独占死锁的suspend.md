`Thread.suspend`方法会挂起一个线程,而且是持有监视器所有权的挂起.  
这就会导致独占和死锁线程发生的可能.  
对于死锁的定义:被挂起的线程等待着恢复,而将其挂起的线程等待获得锁.  

---

测试`suspend`和`resume`方法的使用:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    @SuppressWarnings("deprecation")  
    public static void main(String[] args) {  
        try {  
            Runnable task = () -> {  
                for (;;)  
                    lg.info("starting");  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            Thread.sleep(3_000);  
            thread.suspend();  
            Thread.sleep(1_000);  
            thread.resume();  
            Thread.sleep(2_000);  
            thread.suspend();  
        } catch (InterruptedException ex) {  
            lg.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
}
```  

---

模拟独占的现象:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    // 模拟suspend造成的独占场景。  
    // 这里有个不易发现的采坑点。  
    // 就是入门的控制台输出语句，是同步的。  
    // T1循环输出3秒后，就被挂起了，一直持有着监视器。  
    // 所以最后的结束输出一直在阻塞等待的状态。  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    @SuppressWarnings("deprecation")  
    public static void main(String[] args) {  
        try {  
            Runnable task = () -> {  
                for (;;)  
                    System.out.println("staring");  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            Thread.sleep(3_000);  
            thread.suspend();  
            System.out.println("main-thread ending");  
        } catch (InterruptedException ex) {  
            lg.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
}  
```  
```java
...  
staring  
staring  
staring  
staring  
staring  
```  
