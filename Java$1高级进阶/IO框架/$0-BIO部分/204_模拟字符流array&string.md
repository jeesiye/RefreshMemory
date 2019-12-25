_对于模拟字符流,思路基本和对应的模拟字节流是一致的,提供的内置类型也是一样,都是3个,范围都是数组和字符串,不过在模拟字符流中,是字符数组.不予赘述了._  

`java.io.CharArrayReader`:直接概览;  
```java
public class java.io.CharArrayReader extends java.io.Reader {
  public java.io.CharArrayReader(char[]);
  public java.io.CharArrayReader(char[], int, int);
  public int read() throws java.io.IOException;
  public int read(char[], int, int) throws java.io.IOException;
  public long skip(long) throws java.io.IOException;
  public boolean ready() throws java.io.IOException;
  public boolean markSupported();
  public void mark(int) throws java.io.IOException;
  public void reset() throws java.io.IOException;
  public void close();
}
```  
`java.io.CharArrayWriter`:概览;  
```java
public class java.io.CharArrayWriter extends java.io.Writer {
  public java.io.CharArrayWriter();
  public java.io.CharArrayWriter(int);
  public void write(int);
  public void write(char[], int, int);
  public void write(java.lang.String, int, int);
  public void writeTo(java.io.Writer) throws java.io.IOException;
  public java.io.CharArrayWriter append(java.lang.CharSequence);
  public java.io.CharArrayWriter append(java.lang.CharSequence, int, int);
  public java.io.CharArrayWriter append(char);
  public void reset();
  public char[] toCharArray();
  public int size();
  public java.lang.String toString();
  public void flush();
  public void close();
  public java.io.Writer append(char) throws java.io.IOException;
  public java.io.Writer append(java.lang.CharSequence, int, int) throws java.io.IOException;
  public java.io.Writer append(java.lang.CharSequence) throws java.io.IOException;
  public java.lang.Appendable append(char) throws java.io.IOException;
  public java.lang.Appendable append(java.lang.CharSequence, int, int) throws java.io.IOException;
  public java.lang.Appendable append(java.lang.CharSequence) throws java.io.IOException;
}
```  
`java.io.StringReader`:概览;  
```java
public class java.io.StringReader extends java.io.Reader {
  public java.io.StringReader(java.lang.String);
  public int read() throws java.io.IOException;
  public int read(char[], int, int) throws java.io.IOException;
  public long skip(long) throws java.io.IOException;
  public boolean ready() throws java.io.IOException;
  public boolean markSupported();
  public void mark(int) throws java.io.IOException;
  public void reset() throws java.io.IOException;
  public void close();
}
```  
