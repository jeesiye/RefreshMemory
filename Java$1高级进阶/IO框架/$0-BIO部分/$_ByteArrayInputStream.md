#### 规范:  
`java.io.ByteArrayInputStream`:这是一个模拟流;  
- 从单独使用的角度来讲,这不是一个流,只是对外提供操作字节数组功能的类;  
- 涉及IO操作中的中转处理操作时,这是一个非常有用的工具流;  
- 提供`读取`功能,但没有`关闭流`的功能(没有意义),为方法`read`和`close`;  
- 提供`跳转`,`标记`,`重置`的功能,为方法`skip`,`mark`和`reset`;  
- 提供流属性查询`可读字节总量`,`是否支持标记`的功能,为方法`available`和`markSupport`;  
- __注意,该类的大部分方法是同步的!__  
- 不作总结;  

#### 解析(部分):  
- 字节数组输入流的构造过程  
  - `ByteArrayInputStream(byte[])`  
  ```java
  public ByteArrayInputStream(byte buf[]) {
      // 指向接收字节数组的引用,用成员变量buf
      // 并进行相关的初始化配置
      this.buf = buf;
      // pos表示当前读取字节的下标  
      this.pos = 0;
      // count表示可读取的字节总量  
      this.count = buf.length;
  }
  ```  
  - `ByteArrayInputStream(byte[], int, int)`  
  ```java
  public ByteArrayInputStream(byte buf[], int offset, int length) {
      this.buf = buf;
      this.pos = offset;
      // 有两种情况,假设字节长度为1024
      // (1)constructor(bytes,24,1024)  或者 constructor(bytes,24,2048)
      //   以上计算可读取的长度为1024
      // (2)constructor(bytes,24,64)
      //   以上的计算可读取的长度为88
      // 所以使用最小比对函数,
      // 至于采用offset+length,是为了便于维护成员变量pos
      this.count = Math.min(offset + length, buf.length);
      this.mark = offset;
  }
  ```  
- 关于读取的`read`方法:  
  - 有三个重载的方法,只标记其中一个,其他的不予赘述;  
  ```java
  public synchronized int read() {
      // 比对当前位置和可读字节长度,可读取返回指定的数组,不可读返回-1  
      // 至于其他的重载方法,只是多涉及了pos的维护,以及数组的复制操作;  
      return (pos < count) ? (buf[pos++] & 0xff) : -1;
  }
  ```  
- 关于`close`方法是空实现;  
- 其他方法不予赘述;  

#### 结构:  
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
