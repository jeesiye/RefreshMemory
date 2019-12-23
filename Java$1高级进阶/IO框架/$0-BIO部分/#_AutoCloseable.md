### 行为规范:  
`java.lang.AutoCloseable`:(JDK7)自动关闭  
- 约定该接口的实现者有自动关闭资源流的功能,即`try-with-resources`语句;  
- 基于BIO的操作,推荐使用;  
- 基于NIO的操作,没有必要使用;  
- 在jdk8中,大多数的BIO操作都是支持`try-with-resources`语句;  

### 源码解析(部分):  
无!  
对该接口唯一方法实现的doc说明:  
- 关于内部的实现细节:  
  - 在实现关闭操作的过程中,即使已经释放底层资源,也推荐在内部把关闭标记设置为`false`;  
  - 预防多次调用该方法,以及处理包装流时,出现未知的错误;  
- 关于实现方法抛出的异常类型:  
  - 该接口方法声明的抛出异常类型是`Exception`,但实现者应当抛出具体的异常类型;  
  - 另外不要抛出`InterruptedException`异常,因为该异常是和线程终端状态交互的,抛出该异常可能会压制正确的异常类型;  
- 关于该方法幂等性的要求:  
  - 该方法和`java.io.Closeable.close()`的要求不同;  
  - 前者的实现不要求幂等性,后者的实现要求幂等性;  
  - 虽然没有要求,但还是推荐以具有幂等性的方式实现;  

### 结构概览  
```java
public interface java.lang.AutoCloseable {
  public abstract void close() throws java.lang.Exception;
}
```  
