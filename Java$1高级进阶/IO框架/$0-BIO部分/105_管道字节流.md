_&#8195;&#8195;管道流,一端写入,一端读取,在两端之间建立通信管道,就成为了所谓的管道.这种职责的流在多线程通信场景中很有用处._  
_&#8195;&#8195;实际上内部是用了并发线程的通信技术,即`Object.wait/notify`的方法._  

---  
`java.io.PipedInputStream`:管道输入字节流;  
- 构建的时候,可以指定缓冲的字节大小,可以选择是否同时建立连接;  
- 提供了基本的`读取`,`查询`,`关闭`的功能;  
- 另外提供了一种后置连接流的方法`connect(PipedOutputStream)`;  
- 管道中默认的缓冲大小是1024个字节;  
- 概览:  
  ```java
  public class java.io.PipedInputStream extends java.io.InputStream {
    public java.io.PipedInputStream(java.io.PipedOutputStream) throws java.io.IOException;
    public java.io.PipedInputStream(java.io.PipedOutputStream, int) throws java.io.IOException;
    public java.io.PipedInputStream();
    public java.io.PipedInputStream(int);
    public void connect(java.io.PipedOutputStream) throws java.io.IOException;
    public synchronized int read() throws java.io.IOException;
    public synchronized int read(byte[], int, int) throws java.io.IOException;
    public synchronized int available() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.PipedOutputStream`:管道输出字节流;  
- 构建的过程,可以选择是否建立连接;  
- 提供了基本的`写入`,`刷新`,`关闭`的功能;  
- 和对应的输入流一样,也提供了后置连接流的方法`connect(PipedInputStream)`;  
- 注意,这里的`刷新`概念,其内部执行的是并发线程中通知所有等待线程的形式;  
- 概览:  
  ```java
  public class java.io.PipedOutputStream extends java.io.OutputStream {
    public java.io.PipedOutputStream(java.io.PipedInputStream) throws java.io.IOException;
    public java.io.PipedOutputStream();
    public synchronized void connect(java.io.PipedInputStream) throws java.io.IOException;
    public void write(int) throws java.io.IOException;
    public void write(byte[], int, int) throws java.io.IOException;
    public synchronized void flush() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  

---  
以下是简单的使用,在单线程的场景下测试,实际应当在多线程场景测试;  
```java
PipedInputStream in = new PipedInputStream();
PipedOutputStream out = new PipedOutputStream();
try {
	in.connect(out);
	out.write(97);
	int read = in.read();
	System.out.println(read);
	out.close(); // 此行注释掉会产生不同的结果
	int read2 = in.read();
	System.out.println(read2);
} finally {
	in.close();
	out.close();
}
```  
