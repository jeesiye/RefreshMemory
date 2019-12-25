_思路概念和对应的管道字节流基本一致,不予赘述._  

`java.io.PipedReader`:概览;  
```java
public class java.io.PipedReader extends java.io.Reader {
  public java.io.PipedReader(java.io.PipedWriter) throws java.io.IOException;
  public java.io.PipedReader(java.io.PipedWriter, int) throws java.io.IOException;
  public java.io.PipedReader();
  public java.io.PipedReader(int);
  public void connect(java.io.PipedWriter) throws java.io.IOException;
  public synchronized int read() throws java.io.IOException;
  public synchronized int read(char[], int, int) throws java.io.IOException;
  public synchronized boolean ready() throws java.io.IOException;
  public void close() throws java.io.IOException;
}
```  
`java.io.PipedWriter`:概览;  
```java
public class java.io.PipedWriter extends java.io.Writer {
  public java.io.PipedWriter(java.io.PipedReader) throws java.io.IOException;
  public java.io.PipedWriter();
  public synchronized void connect(java.io.PipedReader) throws java.io.IOException;
  public void write(int) throws java.io.IOException;
  public void write(char[], int, int) throws java.io.IOException;
  public synchronized void flush() throws java.io.IOException;
  public void close() throws java.io.IOException;
}
```  
单线程简单使用(正确方式应当在多线程下测试):  
```java
PipedReader in = new PipedReader();
PipedWriter out = new PipedWriter();
try {
	in.connect(out);
	out.write(97);
	int read = in.read();
	char ch = (char) read;
	System.out.println(ch);
	out.close();
	System.out.println(in.read());
} finally {
	in.close();
	out.close();
}
```  
