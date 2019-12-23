### 行为规范:  
`java.lang.Readable`:JDK5  
- 标记读取操作;  
- 规范接收的处理对象是缓冲的字符;  

### 源码解析(部分):  
无!

### 结构概览  
```java
public interface java.lang.Readable {
  public abstract int read(java.nio.CharBuffer) throws java.io.IOException;
}
```  
