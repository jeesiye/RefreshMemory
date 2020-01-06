`java.io.FilterReader`:装饰字符输入流;  
- 类库中只扩展了带有可回推功能的字符输入流`PushbackReader`;  
- 可参考`FilterInputStream`;  
- 概览:  
  ```java
  public abstract class java.io.FilterReader extends java.io.Reader {
    public int read() throws java.io.IOException;
    public int read(char[], int, int) throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
    public boolean ready() throws java.io.IOException;
    public boolean markSupported();
    public void mark(int) throws java.io.IOException;
    public void reset() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.PushbackReader`:可回推的字符输入流;  
- 实现基本思路和`PushbackInputStream`一致,可参考;  
- 注意事项和对应的字节流一样,构造的时候应当指定可回推的大小,因为默认的大小是1;  
- 概览:  
  ```java
  public class java.io.PushbackReader extends java.io.FilterReader {
    public java.io.PushbackReader(java.io.Reader, int);
    public java.io.PushbackReader(java.io.Reader);
    public int read() throws java.io.IOException;
    public int read(char[], int, int) throws java.io.IOException;
    public void unread(int) throws java.io.IOException;
    public void unread(char[], int, int) throws java.io.IOException;
    public void unread(char[]) throws java.io.IOException;
    public boolean ready() throws java.io.IOException;
    public void mark(int) throws java.io.IOException;
    public void reset() throws java.io.IOException;
    public boolean markSupported();
    public void close() throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
  }
  ```  

---  
`java.io.FilterWriter`:装饰字符输出流;  
- 类库中对此没有进行进步扩展的,若使用需要创建实现类的;  
- 可参考`FilterOutputStream`;  
- 概览:  
  ```java
  public abstract class java.io.FilterWriter extends java.io.Writer {
    public void write(int) throws java.io.IOException;
    public void write(char[], int, int) throws java.io.IOException;
    public void write(java.lang.String, int, int) throws java.io.IOException;
    public void flush() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  
