重复执行`Object.wait`方法,所带来的时间累加计算是很复杂的.  
但是要确保线程会死亡,或之确保唤醒线程,否则可能导致无休止的休眠.  

---

照搬`Thread.join`方法,进行测试.  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    public static void main(String[] args) {  
        try {  
            long base = System.currentTimeMillis();  
            Thread1 thread = new Thread1();  
            thread.start();  
            thread.m2(5_000);// 此方法在有条件的死循环运行，所以不会执行后续的代码。  
            log.info("main end");  
            long now = System.currentTimeMillis();  
            log.info("comsume millis is " + (now - base));  
        } catch (InterruptedException ex) {  
            log.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
    private static class Thread1 extends Thread {  
        @Override  
        public void run() {  
            try {  
                Thread.sleep(20_000);  
            } catch (InterruptedException ex) {  
                log.log(Level.WARNING, ex.toString(), ex);  
              }  
          }  
          // 此方法照搬Thread的join(long)方法！  
          // 注意此调用的等待时间不只是简单的叠加问题。  
          // 另外在线程死亡的时候，退出函数会调用notifyAll函数。具体查看join(long)的API。  
          public final synchronized void m2(long waitMillis) throws InterruptedException {  
              log.info("m2 start");  
              long methodStartMillis = System.currentTimeMillis();  
              long comsumeMillis = 0;  
              if (waitMillis < 0)  
                  throw new IllegalArgumentException("param millis must >= 0");  
              if (waitMillis == 0)  
                  while (isAlive()) {  
                      wait(0);  
                  }  
              else {  
                  while (isAlive()) {  
                      long delayMillis = waitMillis - comsumeMillis;  
                      if (delayMillis <= 0)  
                          break;  
                      wait(delayMillis);  
                      comsumeMillis = System.currentTimeMillis() - methodStartMillis;  
                    }  
                }  
                log.info("m2 end");  
            }  
        }  
    }  
```  
```java
星期四 十一月 21 15:10:07 CST 2019 ocn.axy.concurrent.ThreadApp$Thread1 m2  
信息: m2 start  
星期四 十一月 21 15:10:13 CST 2019 ocn.axy.concurrent.ThreadApp$Thread1 m2  
信息: m2 end  
星期四 十一月 21 15:10:13 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: main end  
星期四 十一月 21 15:10:13 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: comsume millis is 5139  
```  

---

定义一个重复调用,查看时间的计算问题,注意该测试线程无限期的休眠.  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    public static void main(String[] args) {  
        new Thread1().start();  
    }  
    private static class Thread1 extends Thread {  
        @Override  
        public void run() {  
            m1();  
        }  
        public synchronized void m1() {  
            try {  
                long base = System.currentTimeMillis();  
                log.info("m1 start");  
                // Thread.sleep(1_000);  
                for (int i = 4; i < 9; i += 4) {  
                    wait(i * 1_000);  
                }  
                log.info("m1 end");  
                long now = System.currentTimeMillis();  
                log.info("comsume millis is " + (now - base));  
            } catch (InterruptedException ex) {  
                log.log(Level.WARNING, ex.toString(), ex);  
            }  
        }  
    }  
}  
```  
```java
星期四 十一月 21 15:16:08 CST 2019 ocn.axy.concurrent.ThreadApp$Thread1 m1  
信息: m1 start  
星期四 十一月 21 15:16:21 CST 2019 ocn.axy.concurrent.ThreadApp$Thread1 m1  
信息: m1 end  
星期四 十一月 21 15:16:21 CST 2019 ocn.axy.concurrent.ThreadApp$Thread1 m1  
信息: comsume millis is 12191  
```  
