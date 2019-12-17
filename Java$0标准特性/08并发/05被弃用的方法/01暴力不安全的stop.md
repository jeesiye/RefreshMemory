`Thread.stop`方法是天生的暴力不安全的.  
- 它是暴力的,调用该方法,直接抛出一个非受检查型异常`TheadDeath`.  
  以此来杀死线程,并且释放该线程锁持有的所有监视器.  
- 它是不安全的,不论线程此刻在做什么重要的事情,一律直接杀死,很有可能会破坏线程持有的共有对象.  

---

关于`ThreadDeath`非受检查型异常,以下是其API的部分说明:  
```java
/**
 * An instance of {@code ThreadDeath} is thrown in the victim thread
 * when the (deprecated) {@link Thread#stop()} method is invoked.
```  
_测试代码:_  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static void main(String[] args) {  
        @SuppressWarnings("deprecation")  
        Runnable task = () -> {  
            try {  
                Thread.currentThread().stop();  
            } catch (ThreadDeath ex) {  
                // 关于该异常，查看API，有详细的说明。  
                // 1、当调用stop方法的时候，会自动抛出该异常。  
                // 2、该异常是Error的子类。  
                lg.log(Level.WARNING, ex.toString(), ex);  
            }  
        };  
        Thread thread = new Thread(task);  
        thread.start();  
    }  
}  
```  
```java
星期四 十一月 21 13:50:51 CST 2019 ocn.axy.concurrent.ThreadApp lambda$0  
警告: java.lang.ThreadDeath  
java.lang.ThreadDeath  
    at java.lang.Thread.stop(Thread.java:853)  
    at ocn.axy.concurrent.ThreadApp.lambda$0(ThreadApp.java:11)  
    at java.lang.Thread.run(Thread.java:748)  
```  

---

模拟`stop`方法的暴力不安全性:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    // 此案例模拟了stop方法带来的两个不安全的隐患。  
    // 1、中断所有的任务，搁浅了必要的任务。  
    // T1在执行500-millisecond后，就被终止了，后续的修改任务，和清理任务都不可能继续了。  
    // 2、没有保证数据的安全性。  
    // T1只修改了user的id，但age属性还是初始化的数值。  
    // 若在应用场景要求user的操作保证原子性，显然没有做到。  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    @SuppressWarnings("deprecation")  
    public static void main(String[] args) {  
        try {  
            final User user = new User();  
            Runnable task = () -> {  
                user.settings(2, 45);  
                user.clean();  
            };  
            Thread thread = new Thread(task);  
            thread.start();  
            Thread.sleep(500);  
            thread.stop();  
            thread.join(0);  
            lg.info(user.getInfo());  
          } catch (InterruptedException ex) {  
              lg.log(Level.WARNING, ex.toString(), ex);  
          }  
      }  
      private static class User {  
          private int id;  
          private int age;  
          {  
              this.id = 1;  
              this.age = 35;  
          }  
          public void settings(int id, int age) {  
              try {  
                  this.id = id;  
                  Thread.sleep(3_000);  
                  this.age = age;  
              } catch (InterruptedException ex) {  
                  lg.log(Level.WARNING, ex.toString(), ex);  
              }  
          }  
          public void clean() {  
              lg.info("starting clean");  
          }  
          public String getInfo() {  
          return "User [id=" + id + ", age=" + age + "]";  
      }  
  }  
}  
```  
```java
星期四 十一月 21 13:53:08 CST 2019 ocn.axy.concurrent.ThreadApp main  
信息: User [id=2, age=35]  
```  
