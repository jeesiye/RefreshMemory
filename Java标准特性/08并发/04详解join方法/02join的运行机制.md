join方法如何运行的?  
- 可以说成是:当前线程执行指定线程的实例方法`Thread.join`(继承而来的).  
- 比如线程T1在线程内部执行线程T2实例的`join`方法.  
- T1就会尝试获取T2的监视器对象,而这个监视器对象即使T2本身(因为`join`是sync同步方法)  
- 若请求不到监视器的所有权,T1就会进入阻塞状态.直到获取到监视器的所有权.  
- 而获取到监视器所有权后,有两种情况:  
  1. 在阻塞期间,线程已经死亡,则当前线程不进入等待状态,继续执行后续任务;  
  1. 若`join`方法带有计时参数,指定线程未死亡,则比较计时参数是否已经过时,过时的话当前线程继续执行,否则当前线程进入计时差损等待状态.  

_测试代码_:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger log = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    public static void main(String[] args) {  
        try {  
            // main尝试获取T1的监视器，但T1运行持有监视器5秒。所以需要等待5秒，才会真正的执行join方法。  
            // 当mian获取T1的监视器后，发现预期的等待时间已经过期了，所以会立刻执行后续代码。  
            // 测试环境输出的结果是5秒多点！  
            long base1 = System.currentTimeMillis();  
            Thread1 thread1 = new Thread1();  
            thread1.start();  
            Thread.sleep(500);  
            thread1.join(1_000);  
            long now1 = System.currentTimeMillis();  
            log.info("consume millis is " + (now1 - base1));  
            long base2 = System.currentTimeMillis();  
            // main尝试获取T1的监视器，而请求的监视器就是T1。  
            // 因为T1在执行的过程中持有的监视器是mutex，非T1本身；  
            // 所以main会立刻获得T1的监视器T1，然后在join的内部循环执行wait方法。  
            // 测试环境输出的结果是1秒多点！  
            Thread2 thread2 = new Thread2();  
            thread2.start();  
            Thread.sleep(500);  
            thread2.join(1_000);  
            long now2 = System.currentTimeMillis();  
            log.info("comsume millis is " + (now2 - base2));  
        } catch (InterruptedException ex) {  
            log.log(Level.WARNING, ex.toString(), ex);  
        }  
    }  
    private static class Thread1 extends Thread {  
        @Override  
        // 当前线程执行体中，持有所有对象实例的监视器。  
        public synchronized void run() {  
            try {  
                Thread.sleep(5_000);  
            } catch (InterruptedException ex) {  
                log.log(Level.WARNING, ex.toString(), ex);  
              }  
          }  
      }  
      private static class Thread2 extends Thread {  
          final Object mutex = new Object();  
          @Override  
          // 当前线程执行体中，持有的监视器是内置属性对象mutex。  
          public void run() {  
              try {  
                  synchronized (mutex) {  
                      Thread.sleep(5_000);  
                  }  
              } catch (InterruptedException ex) {  
                  log.log(Level.WARNING, ex.toString(), ex);  
              }  
          }  
      }  
  }  
```  
_输出验证结果:_  
```java
星期四 十一月 21 15:04:50 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: consume millis is 5004  
星期四 十一月 21 15:04:51 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: comsume millis is 1505  
```  
