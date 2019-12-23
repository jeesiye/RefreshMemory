### 行为规范:  
`java.io.Closeable`:(JDK5)约定持有资源时的关闭行为.  
- 约定持有资源时的关闭行为;  
- 这种关闭约束会释放持有的所有资源;  

### 源码解析(部分):  
无!  
对该接口中唯一的方法`close()`说明:  
- 调用该方法,会释放持有的所有系统资源;  
- 若资源已经关闭了,再次调用该方法会失效(幂等性);  
- 若抛出异常,确保在抛出之前释放资源,并且把内部的`Closeable`标记为`false`;  

### 结构概览  
```java
public interface java.io.Closeable extends java.lang.AutoCloseable {
  public abstract void close() throws java.io.IOException;
}
```  
