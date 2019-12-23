#### 规范:  
`java.io.Flushable`:JDK5  
- 缓冲功能,带有缓冲功能的流,应当实现此规范;  
- 实现者应当在触发缓冲区域的边界时,即达到阀值,自动刷新缓冲区的数据;  
- 刷新的操作,指的是把缓冲区的数据,输出到真实的物理存储中(并且清空缓冲区);  
- 在关闭流操作之前,应当保证缓冲区数据被刷新清空;  

---  
#### 解析(部分):  
无!

---  
#### 结构:  
```java
public interface java.io.Flushable {
  public abstract void flush() throws java.io.IOException;
}
```  
