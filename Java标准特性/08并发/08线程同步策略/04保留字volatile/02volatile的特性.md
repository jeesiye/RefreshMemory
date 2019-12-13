`volatile`保证数据的可见性:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    // 执行3秒之后，会结束。  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    private static class Plan {  
        private volatile boolean bool = true;  
        public void settings(boolean bool) {  
            this.bool = bool;  
        }  
        public void m1() {  
            for (;;) {  
                if (!bool)  
                    break;  
            }  
            lg.info("ending");  
        }  
    }  
    public static void main(String[] args) {  
        try {  
            final Plan plan = new Plan();  
            Thread thread = new Thread(() -> plan.m1());  
            thread.start();  
            Thread.sleep(3_000);  
            plan.settings(false);  
        } catch (InterruptedException ex) {  
            lg.log(Level.WARNING, ex.toString(), ex);  
          }  
      }  
  }  
```  

---

`volatile`不能保证数据的原子性:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // volatile不具备原子性，当前的测试中会有重复的数据输出。  
    private static class Plan {  
        private volatile int num = 0;  
        public void m1() {  
            num++;  
            lg.info(String.valueOf(num));  
        }  
    }  
    public static void main(String[] args) {  
        final Plan plan = new Plan();  
        for (int i = 0; i < 100; i++) {  
            new Thread(() -> plan.m1()).start();  
        }  
    }  
}  
```  
