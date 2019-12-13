`sysout`这个非常熟悉的语句,可查看该方法的源码,它是`sync`语句:  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    // 该测试场景，程序会在3秒后终止，即使没有使用volatile标记共享变量。  
    // 原因，查看入门输出语句的内部实现，使用了sync结构。  
    // 结论：sync也可完成volatile的功能，但在低版本中开销太大。  
    // 此场景也得以验证，故不单独新建场景测试。  
    private static class Plan {  
        // 注意，该变量没有使用volatile标记。  
        private boolean bool = true;  
        public void settings(boolean bool) {  
            this.bool = bool;  
        }  
        public void m1() {  
            for (;;) {  
                if (!bool)  
                    break;  
                System.out.println("starting");  
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
```java
...  
starting  
starting  
starting  
starting  
星期四 十一月 21 16:13:08 CST 2019 ocn.axy.concurrent.ThreadApp$Plan m1  
信息: ending  
```  
