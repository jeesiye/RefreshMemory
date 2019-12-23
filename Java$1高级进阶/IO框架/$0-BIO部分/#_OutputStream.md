#### 规范:  
`java.io.OutputStream`:基于字节的输出流;  
- 基于字节的基本功能,`输出`和`关闭流`的操作,为`write`和`close`方法;  
- 若带有缓冲功能,`刷新缓冲区域`的功能,为`flush`方法;  
- 注意区分缓冲和缓存的概念;  

---  
#### 解析(部分):  
阅读无实际意义,依赖具体子类的实现.  

---  
输出指定字节数组中的批量字节方法:  
```java
// 调用本地重载的write(byte b[],int off,int len)方法
public void write(byte b[]) throws IOException {
    write(b, 0, b.length);
}
public void write(byte b[], int off, int len) throws IOException {
    // step1:对参数的合法性校验
    if (b == null) {
        // b不能为null,否则抛出空指针异常
        throw new NullPointerException();
    } else if ((off < 0) || (off > b.length) || (len < 0) ||
               ((off + len) > b.length) || ((off + len) < 0)) {
        // off和len的合法逻辑限制,违反抛出下标越界异常
        throw new IndexOutOfBoundsException();
    } else if (len == 0) {
        return;
    }
    // 单个字节写入,调用本地重载的方法write(int b)
    // 循环写入
    for (int i = 0 ; i < len ; i++) {
        write(b[off + i]);
    }
}
```  
---  
#### 结构:  
```java
public abstract class java.io.OutputStream implements java.io.Closeable,java.io.Flushable {
  public java.io.OutputStream();
  public abstract void write(int) throws java.io.IOException;
  public void write(byte[]) throws java.io.IOException;
  public void write(byte[], int, int) throws java.io.IOException;
  public void flush() throws java.io.IOException;
  public void close() throws java.io.IOException;
}
```  
