```java
package ocn.axy.concurrent;  
import java.util.concurrent.locks.Condition;  
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReentrantLock;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // 可以设置多个条件，不予测试。  
    // 通过此可进一步理解sync,其实sync就相当于只有一个默认的条件.  
    // 但是lock是可以手动配置多个指定的条件的.  
    private static class Plan {  
        private Lock lock = new ReentrantLock();  
        private Condition condition1 = lock.newCondition();  
        public void m1() {  
            try {  
                lock.lock();  
                lg.info("m1 start");  
                Thread.sleep(3_000);  
                lg.info("condition1 staart await");  
                condition1.await();  
                lg.info("m1 end");  
            } catch (InterruptedException ex) {  
                lg.log(Level.WARNING, ex.toString(), ex);  
              } finally {  
                  lock.unlock();  
              }  
          }  
          public void m2() {  
              try {  
                  lock.lock();  
                  lg.info("m2 start");  
                  Thread.sleep(2_000);  
                  lg.info("condition1 signal");  
                  // 条件上通知的机制原理和Object的一样，通知之后不是立即释放锁，而是完成工作之后。  
                  condition1.signal();  
                  lg.info("m2 end");  
              } catch (InterruptedException ex) {  
                  lg.log(Level.WARNING, ex.toString(), ex);  
              } finally {  
                  lock.unlock();  
              }  
          }  
      }  
      public static void main(String[] args) {  
          try {  
            final Plan plan = new Plan();  
            Thread thread1 = new Thread(() -> plan.m1());  
            Thread thread2 = new Thread(() -> plan.m2());  
            thread1.start();  
            Thread.sleep(500);  
            thread2.start();  
            Thread.sleep(500);  
        } catch (InterruptedException ex) {  
            lg.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
}  
```  
```java
星期四 十一月 21 16:48:04 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m1  
信息: m1 start  
星期四 十一月 21 16:48:07 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m1  
信息: condition1 start await  
星期四 十一月 21 16:48:07 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m2  
信息: m2 start  
星期四 十一月 21 16:48:09 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m2  
信息: condition1 signal  
星期四 十一月 21 16:48:09 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m2  
信息: m2 end  
星期四 十一月 21 16:48:09 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m1  
信息: m1 end  
```  
