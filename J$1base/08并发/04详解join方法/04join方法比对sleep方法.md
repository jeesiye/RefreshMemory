`Thread.join`方法是不等价于`Thread.sleep`方法的!  
`join`方法和`sleep`方法是有很大区别的.  
&#8195;&#8195;`join`方法执行的时候,当前线程会尝试获取指定线程的内部监视器的所有权,然后在内部循环执行`wait`方法,在此期间又会释放所持有的监视器所有权.  
&#8195;&#8195;而`sleep`方法在执行的时候,不会尝试获取监视器的所有权,它的功效只是让当前线程休眠指定的毫秒数.但是若持有监视器的所有权的话,休眠的期间是不会释放的.  
简言之:  
1. `sleep`期间不会释放监视器所有权.  
1. `join`期间会释放监视器的所有权,但执行的前提是获取指定线程的监视器所有权,中间会有计时过时的差损计算.  

_验证代码:_  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // 此场景主要是为了验证join(long)和sleep(long)是否等价。  
    // 当前的测试代码中，可以验证。  
    // join方法尝试获取指定线程的监视器所有权。  
    // sleep方法会一直持有不释放监视器的所有权。  
    public static void main(String[] args) {  
        try {  
            long l1 = System.currentTimeMillis();  
            Thread1 thread1 = new Thread1();  
            thread1.start();  
            // 若执行下面一行代码，当前线程会尝试获取thread1的监视器所有权。  
            // thread1.join(1_000);// 将此行替换下面的休眠语句。运行的结果不是相同的。  
            Thread.sleep(1_000);  
            log.info("main end");  
            long l2 = System.currentTimeMillis();  
            log.info("comsume millis is " + (l2 - l1));  
        } catch (InterruptedException ex) {  
            log.log(Level.WARNING, ex.toString(), ex);  
          }  
      }  
      private static class Thread1 extends Thread {  
          @Override  
          public void run() {  
              m1();  
          }  
          public synchronized void m1() {  
              try {  
                  // 此段时间，执行m1方法的线程会一直持有该监视器对象的所有权。  
                  Thread.sleep(5_000);  
              } catch (InterruptedException ex) {  
                  log.log(Level.WARNING, ex.toString(), ex);  
              }  
          }  
      }  
  }  
```  
```java
星期四 十一月 21 14:58:32 CST 2019 ocn.axy.concurrent.ThreadApp main
信息: main end  
星期四 十一月 21 14:58:32 CST 2019 ocn.axy.concurrent.ThreadApp main
信息: comsume millis is 1140  
```  
