以简单的案例输出结束日志为测试目的.  

---

不使用`join`方法输出结束日志.  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    public static void main(String[] args) {  
        try {  
            Runnable task = () -> {  
                for (int i = 0; i < 5; i++) {  
                    try {  
                        Thread.sleep(1_000);  
                        log.info(String.valueOf(i));  
                    } catch (InterruptedException ex) {  
                        log.log(Level.WARNING, ex.toString(), ex);  
                    }  
                }  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            Thread.sleep(500);  
            log.info("main thread ending");  
        } catch (InterruptedException ex) {  
            log.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
}  
```  
```java
星期四 十一月 21 14:42:32 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: main thread ending  
星期四 十一月 21 14:42:33 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 0  
星期四 十一月 21 14:42:34 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 1  
星期四 十一月 21 14:42:35 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 2  
星期四 十一月 21 14:42:36 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 3  
星期四 十一月 21 14:42:37 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 4  
```  

---

使用`join`方法输出结束日志.  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    public static void main(String[] args) {  
        try {  
            Runnable task = () -> {  
                try {  
                    for (int i = 0; i < 5; i++) {  
                        Thread.sleep(1_000);  
                        log.info(String.valueOf(i));  
                    }  
                } catch (InterruptedException ex) {  
                    log.log(Level.WARNING, ex.toString(), ex);  
                }  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            Thread.sleep(500);  
            thread.join(0);// 直至等到指定线程死亡。  
            log.info("main thread ending");  
        } catch (InterruptedException ex) {  
            log.log(Level.WARNING, ex.toString(), ex);  
        }  
      }  
  }  
```  
```java
星期四 十一月 21 14:47:40 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 0  
星期四 十一月 21 14:47:41 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 1  
星期四 十一月 21 14:47:42 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 2  
星期四 十一月 21 14:47:43 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 3  
星期四 十一月 21 14:47:44 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
信息: 4  
星期四 十一月 21 14:47:44 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: main thread ending  
```  
