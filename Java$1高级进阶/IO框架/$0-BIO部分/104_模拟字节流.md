_&#8195;&#8195;对模拟流的定义,严格意义上,它不是一个真实存在的流,没有涉及打开物理资源的操作;但是对外提供了和流操作类似的功能,即模拟操作内存中的值,对外假设在真实的操作物理资源._  
_&#8195;&#8195;`java.io`包中提供了两种类型的模拟流,一种是操作字节数组的模拟流,一种是操作字符串的模拟流._  
_&#8195;&#8195;个人见解,在模拟单元调试中,这三个流蛮有用的._  

---  
`java.io.ByteArrayInputStream`:模拟字节数组输入字节流;  
- 构建的过程需要接收内存中的字节数组,即可进行后续模拟流的操作;  
- 对外提供了模拟`读取`,`跳过`,`标记`,`重置`4种流操作功能;  
- 对于`关闭`的功能,事实上是没有意义的,其内部是个空方法;  
- 当然也提供了相关的查询功能;  
- 概览:  
```java
  public class java.io.ByteArrayInputStream extends java.io.InputStream {
    public java.io.ByteArrayInputStream(byte[]);
    public java.io.ByteArrayInputStream(byte[], int, int);
    public synchronized int read();
    public synchronized int read(byte[], int, int);
    public synchronized long skip(long);
    public synchronized int available();
    public boolean markSupported();
    public void mark(int);
    public synchronized void reset();
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.ByteArrayOutputStream`:模拟字节数组输出字节流;  
- 构建的过程需要指定内部存储字节数组容量的大小,默认是`32`的大小;  
- 对外模拟提供了`写入`,`查询`2种基本的流操作功能;  
- 此外提供了`输出到真实流`,`转换字节数组`,`转换字符串`3种转换功能;  
- 注意,其内部使用自动扩容的策略,具体见源码;  
- 概览:  
  ```java
  public class java.io.ByteArrayOutputStream extends java.io.OutputStream {
    public java.io.ByteArrayOutputStream();
    public java.io.ByteArrayOutputStream(int);
    public synchronized void write(int);
    public synchronized void write(byte[], int, int);
    public synchronized void writeTo(java.io.OutputStream) throws java.io.IOException;
    public synchronized void reset();
    public synchronized byte[] toByteArray();
    public synchronized int size();
    public synchronized java.lang.String toString();
    public synchronized java.lang.String toString(java.lang.String) throws java.io.UnsupportedEncodingException;
    public synchronized java.lang.String toString(int);
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.StringBufferInputStream`:模拟字符串输入字节流;  
- 构造的过程,依赖字符串的实例;  
- 提供了基本的`读取`,`跳过`,`查询`,`重置`的功能;  
- 概览:  
  ```java
  public class java.io.StringBufferInputStream extends java.io.InputStream {
    public java.io.StringBufferInputStream(java.lang.String);
    public synchronized int read();
    public synchronized int read(byte[], int, int);
    public synchronized long skip(long);
    public synchronized int available();
    public synchronized void reset();
  }
  ```  
