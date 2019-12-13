`ThreadLocal`可比喻成全局存放数据的盒子，盒子里存储这每个线程的私有数据。  
`InheritableThreadLocal`子线程可以拿到父线程的值。但子线程取值同时，父线程更新，子线程拿到的是旧值。另内建库中提高了一个便利类，可选择使用`ThreadLocalRandom`。  

---

局部变量的验证:  
```java
package ocn.axy.concurrent;  
import java.util.Date;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // main线程和T1线程从ThreadLocal中取值，取值的结果都是不一样的。  
    // 但是在线程内部重复读取的时候，结果却是相同的。  
    // 结论：  
    // 内部相同，外部不同。  
    private static class Plan {  
        public final static ThreadLocal<Date> date = new ThreadLocal<Date>() {  
            protected Date initialValue() {  
                return new Date();  
            };  
        };  
    }  
    public static void main(String[] args) {  
        try {  
            Runnable task = () -> {  
                lg.info("T1 start getvalue");  
                for (int i = 0; i < 5; i++) {  
                    long time = Plan.date.get().getTime();  
                    lg.info(Thread.currentThread().toString() + "---" + String.valueOf(time));  
                }  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            thread.join(0);  
            lg.info("main-thread start getvalue");  
            for (int i = 0; i < 5; i++) {  
              long time = Plan.date.get().getTime();  
              lg.info(Thread.currentThread().toString() + "---" + String.valueOf(time));  
          }  
      } catch (InterruptedException ex) {  
          lg.log(Level.WARNING, ex.toString(), ex);  
      }  
  }  
}  
```  
```java
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: T1 start getvalue  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: Thread[Thread-1,5,main]---1574326467288  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: Thread[Thread-1,5,main]---1574326467288  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: Thread[Thread-1,5,main]---1574326467288  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: Thread[Thread-1,5,main]---1574326467288  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: Thread[Thread-1,5,main]---1574326467288  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: main-thread start getvalue  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: Thread[main,5,main]---1574326467293  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: Thread[main,5,main]---1574326467293  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: Thread[main,5,main]---1574326467293  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: Thread[main,5,main]---1574326467293  
星期四 十一月 21 16:54:27 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: Thread[main,5,main]---1574326467293  
```  
