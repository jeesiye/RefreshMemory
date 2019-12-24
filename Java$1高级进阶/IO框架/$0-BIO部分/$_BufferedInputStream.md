#### 规范:  

#### 解析(部分):  
- 缓冲输入流的构造过程:`BufferedInputStream(InputStream, int)`  
  - 构造过程比较简单,该类的核心部分是在首次读取字节,以及关闭流的时候;  
  - 步骤是非常明显的三步:  
    - 调用上层的装饰类,将接收的输入流实力传入;  
    - 对接收的int参数,即缓冲大小进行合法行检查;  
    - 初始化存放缓冲字节数组的字节数组(大小即为指定的,默认是8192);  
  ```java
  // 创建缓冲输入流的实力,并配置缓冲字节的大小
  // 默认的缓冲大小是8192个字节,参类变量`DEFAULT_BUFFER_SIZE`  
  public BufferedInputStream(InputStream in, int size) {
      // 调用父类的构造器(根据Java构造器的机制,这个是必须手动编写的)
      // 参$_00部分(FilterInputStream是个装饰类,使用了装饰模式)
      super(in);
      // 对缓冲大小参数的合法性检查
      if (size <= 0) {
          throw new IllegalArgumentException("Buffer size <= 0");
      }
      // 初始化成员变量buf(字节数组),长度是接收的参数size
      buf = new byte[size];
  }

  // $_00
  // java.io.FilterInputStream.FilterInputStream(InputStream)
  // 把接收到的输入流对象,指向到聚合的InputStream类型的成员变量in
  protected FilterInputStream(InputStream in) {
      this.in = in;
  }
  ```  
- 私有方法,获取输入流实例:`getInIfOpen()`  
  ```java
  private InputStream getInIfOpen() throws IOException {
      InputStream input = in;
      if (input == null)
          throw new IOException("Stream closed");
      return input;
  }
  ```  
- 私有方法,获取内部字节缓冲数组实例:`getBufIfOpen()`  
  ```java
  private byte[] getBufIfOpen() throws IOException {
      byte[] buffer = buf;
      if (buffer == null)
          throw new IOException("Stream closed");
      return buffer;
  }
  ```  
- 读取一个字节的方法:`read()`  
  ```java
  public synchronized int read() throws IOException {
      // 判断读取标记下标pos是否大于等于缓冲字节数组的长度;  
      // 以下几种情况会出发这个if分支:  
      //   (1)第一次读取的时候
      //        当该类首次初始化的时候,参构造过程,pos和count取值都是默认值0;  
      //   (2)当顺序读取到缓冲字节数组的最后一个字节的时候
      //        数组的下标从0开始标记,每次读取一个自己,都会pos都会+1
      //        当读取到count-1个字节的时候,pos的值实际就是pos+1
      //   (3)当执行skip方法的时候,可能会触发
      //        在跳过的字节数量skipped+pos的值,大于count的数值的时候;  
      if (pos >= count) {
          // 该方法是该类的核心方法,单独解释
          fill();
          // 再次检查,确认是否已经把文件流给读取完了
          if (pos >= count)
              return -1;
      }
      // 从字节数组取出当前下标pos的值,并且pos+1运算
      // 和0xff与运算,确保得到的是合法的编码表字符(负数情况,16进制转换)
      return getBufIfOpen()[pos++] & 0xff;
  }
   ```  

#### 结构:  
```java
public class java.io.BufferedInputStream extends java.io.FilterInputStream {
  public java.io.BufferedInputStream(java.io.InputStream);
  public java.io.BufferedInputStream(java.io.InputStream, int);
  public synchronized int read() throws java.io.IOException;
  public synchronized int read(byte[], int, int) throws java.io.IOException;
  public synchronized long skip(long) throws java.io.IOException;
  public synchronized int available() throws java.io.IOException;
  public synchronized void mark(int);
  public synchronized void reset() throws java.io.IOException;
  public boolean markSupported();
  public void close() throws java.io.IOException;
}
```  
