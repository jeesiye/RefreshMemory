`Thread.join`释义(参考自API文档):  
- 先决条件:执行该方法的前提是,当前线程获取指定线程的监视器,否则进入阻塞状态.  
  具体的细节可参考`join`方法运行机制篇幅.  
- 功能概述:当前线程等待指定线程执行结束,或之等待指定的毫秒数后,再继续执行.  
  - 若参数为0,则进入无限期的等待状态,实际内部不断调用`Object.wait`方法,直到指定线程执行结束.  
  - 至于内部循环等待的时间,不是简单的叠加计算得出的.  
- 异常概述:  
  - 当传递的参数是负数的时候,就抛出`IllegalArgumentException`异常.  
  - 当前线程在等待指定线程执行的期间,若其他线程中断了当前线程,就抛出`InterruptedException`异常.  

关于`Thread.join`内部方法的疑问?  
- 其内部不断的调用`Object.wait`方法,那么是在什么时候被唤醒的.  
- 当线程死亡,执行清理作业的时候,会调用`Object.notifyAll`方法.  
  - `Thread.join`的API上有特意的指出.  
  - 另外`Thread.exit`方法内部却有执行`Object.notifyAll`方法.  

关于`Thread.join`方法的归纳:  
1. 指定该方法,当前线程必须获取指定线程内部的监视器.(即内部请求获取监视器).  
1. 其本质是内部通过有条件的死循环,不断调用`Object.wait`方法实现的.  
1. 日常使用推荐`Thread.join(long)`的重载方法.  

以下是`Thead.join()`的源码,可验证某些结论:  
```java
public final synchronized void join(long millis)
throws InterruptedException {
    long base = System.currentTimeMillis();
    long now = 0;

    if (millis < 0) {
        throw new IllegalArgumentException("timeout value is negative");
    }

    if (millis == 0) {
        while (isAlive()) {
            wait(0);
        }
    } else {
        while (isAlive()) {
            long delay = millis - now;
            if (delay <= 0) {
                break;
            }
            wait(delay);
            now = System.currentTimeMillis() - base;
        }
    }
}
```  
