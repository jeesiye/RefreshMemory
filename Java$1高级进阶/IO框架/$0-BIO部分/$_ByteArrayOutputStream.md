#### 规范:  
`java.io.ByteArrayOutputStream`:可以说是个模拟输出流;  
- 提供的功能比较简单,直接查看源码即可;  
- 有几个比较实用的方法,比如转换为字符串,转换为字节数组,把字节数组输出到指定的输出流中;  
- 其中大部分的方法是同步的,这和对应的输入流是一样的.  

#### 解析(部分):  
- 不予赘述;  
- 可参`$_ByteArrayInputStream`文件;  

#### 结构:  
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
