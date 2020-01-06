`volatile`和`sync`的比对:  
- `volatile`  
  - 只能修饰变量,轻量级的同步实现,低版本中相对性能略高;  
  - 并发不阻塞线程;  
  - 只能保证数据的可见性,并且阻止指令重排;  
- `sync`  
  - 修饰方法,重量级的同步实现,高版本中性能问题可忽略;  
  - 并发阻塞线程;  
  - 保证原子性的同时,间接的保证了可见性;  

总结:`volatile`关键字:  
- 共享可见性+禁制指令重排;  
- 主要目的,标记变量,保证其在多个线程中的可见性.  

---

关于JVM的`-server`参数的说明:  
虚拟机启动的时候,若是使用了该参数,为了提高效率,线程会一直在私有堆栈中取值.  
- 比如线程不断的访问从公共堆栈中拷贝的数据,假设为`data`,若公共堆栈中的`data`变更的话,在`-server`的模式下,线程的私有堆栈中的拷贝值不会刷新.  
- 一般服务器上的`-server`参数是默认值.若解决这个问题,就将访问的公共堆栈的变量标记为`volatile`.  

以下是`java`指令中对`-server`的说明,平台是`linux`  
```sheel
-server	  选择 "server" VM
              默认 VM 是 server,
              因为您是在服务器类计算机上运行。
```  

_下面是一个典型的案例:_  
```java
package ocn.axy.concurrent;  
import java.util.logging.Level;  
import java.util.logging.Logger;  
public class ThreadApp {  
    // 加上-server参数，会一直死循环。  
    private final static Logger lg = Logger.getLogger(ThreadApp.class.getPackage().getName());  
    private static class Plan {  
        private boolean bool = true;  
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
