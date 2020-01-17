`java.io.BufferedReader`:缓冲字符输入流;  
- 处理思路和`BufferedInputStream`基本一致;  
- 唯一的区别是实现的不同,前者直接读取字节,后者需要处理负责的字符编码问题;  
- 构造过程可以选择指定缓冲字符数组的长度,默认是8192;  
- 提供了`读取`,`跳过`,`标记`,`重置`,`整行读取`的基本功能;  
- 概览:  
  ```java
  public class java.io.BufferedReader extends java.io.Reader {
    public java.io.BufferedReader(java.io.Reader, int);
    public java.io.BufferedReader(java.io.Reader);
    public int read() throws java.io.IOException;
    public int read(char[], int, int) throws java.io.IOException;
    public java.lang.String readLine() throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
    public boolean ready() throws java.io.IOException;
    public boolean markSupported();
    public void mark(int) throws java.io.IOException;
    public void reset() throws java.io.IOException;
    public void close() throws java.io.IOException;
    public java.util.stream.Stream<java.lang.String> lines();
  }
  ```  

---  
`java.io.LineNumberReader`:记录行号字符输入流;  
- 和弃用的`LineNumberInputStream`的思路基本一致,不过该类是作为替用类的;  
- 其核心的功能,都体现在父类的缓冲字符输入流中,其内部不过是多维护了一个行号的`int`类型;  
- 不要轻易使用`修改器`修改行号`lineNumber`;  
  真实读取的顺序不会改变;改变的不过是维护的`lineNumber`的值,不利于统计行号;  
- 概览:  
  ```java
  public class java.io.LineNumberReader extends java.io.BufferedReader {
    public java.io.LineNumberReader(java.io.Reader);
    public java.io.LineNumberReader(java.io.Reader, int);
    public void setLineNumber(int);
    public int getLineNumber();
    public int read() throws java.io.IOException;
    public int read(char[], int, int) throws java.io.IOException;
    public java.lang.String readLine() throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
    public void mark(int) throws java.io.IOException;
    public void reset() throws java.io.IOException;
  }
  ```  

---  
`java.io.BufferedWriter`:缓冲字符输出流;  
- 处理思路和`BufferedOutputStream`基本一致;  
- 唯一的区别是实现方式的不同,以及复杂程度的增加;  
- 构造过程可以选择指定缓冲的字符输出长度,默认是8192;  
- 提供了`写入`,`刷新`,`写入空行`的基本功能;  
- 概览:  
  ```java
  public class java.io.BufferedWriter extends java.io.Writer {
    public java.io.BufferedWriter(java.io.Writer);
    public java.io.BufferedWriter(java.io.Writer, int);
    public void write(int) throws java.io.IOException;
    public void write(char[], int, int) throws java.io.IOException;
    public void write(java.lang.String, int, int) throws java.io.IOException;
    public void newLine() throws java.io.IOException;
    public void flush() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  
