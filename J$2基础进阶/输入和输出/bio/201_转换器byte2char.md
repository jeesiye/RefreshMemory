_&#8195;&#8195;字符流是不能够直接创建的,必须要借助指定的输入流实例来创建.仅仅在`java.io`包库的范围内,能够创建流实例的只有`FileInputStream`,`FileOutputStream`和`RandomAccessFile`这两种类型的类(最终调用了本地库的函数`fileOpen`).但是往往使用的是BIO库中的苦力,文件字节流来创建流实例的._  
_&#8195;&#8195;但是从字符流接口的设计和内置的实现类来看,创建他们的过程,通常需要依赖一个字符流实例的.这就需要一个中转站,把字节流实例转换为字符流实例.(BIO库中没有内置直接创建字符流实例的类对象的!)_  
_&#8195;&#8195;BIO库中提供了两个转换流,负责把字节输入流转换为字符输入流的`InputStreamReader`;和负责把字节输出流转换为字符输出流的`OutputStreamWriter`.实际上其内部调用的是流的解码和编码的类对象,如下:_  
- `sun.nio.cs.StreamDecoder`  
- `sun.nio.cs.StreamEncoder`  

_&#8195;&#8195;它们都是公有类,提供了公开的静态工厂方法,来接收处理字节流;实际上字符流的操作,最终是它们负责的.字符流不过是进行了外部的包装优化而已._  

---  
`java.io.InputStreamReader`:字节输入流转字符输入流;  
- 构造的过程对外部是多选的,可以由用户指定处理数据的编码;  
- 若用户不指定编码,则使用当前系统默认的编码;  
- 对外提供单一的`读取`方法,实际的工作是由`StreamDecoder`来负责处理的;  
- 有一个便捷的实现类,但是通常很少使用,因为效率,大多数再使用缓冲流包装后处理;  
- 概览:  
  ```java
  public class java.io.InputStreamReader extends java.io.Reader {
    public java.io.InputStreamReader(java.io.InputStream);
    public java.io.InputStreamReader(java.io.InputStream, java.lang.String) throws java.io.UnsupportedEncodingException;
    public java.io.InputStreamReader(java.io.InputStream, java.nio.charset.Charset);
    public java.io.InputStreamReader(java.io.InputStream, java.nio.charset.CharsetDecoder);
    public java.lang.String getEncoding();
    public int read() throws java.io.IOException;
    public int read(char[], int, int) throws java.io.IOException;
    public boolean ready() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.FileReader`:便捷处理文件的字节转字符输入流;  
- 使用这个类的好处是方便,缺点是不能指定编码;  
- 其构造过程,大概是这样的:构造`File`实例,构造`FileInputStream`实例,调用`InputStreamReader`的构造器;  
  以下是3个构造器的源码:  
  - `super(new FileInputStream(fileName));`  
  - `super(new FileInputStream(file));`  
  - `super(new FileInputStream(fd));`  
- 概览:  
  ```java
  public class java.io.FileReader extends java.io.InputStreamReader {
    public java.io.FileReader(java.lang.String) throws java.io.FileNotFoundException;
    public java.io.FileReader(java.io.File) throws java.io.FileNotFoundException;
    public java.io.FileReader(java.io.FileDescriptor);
  }
  ```  

---  
`java.io.OutputStreamWriter`:字节输出流转换字符输出流;  
- 构造的过程对用户来说,是多选的,可以选择是否指定字符的编码;  
- 若不指定编码,则使用默认的平台编码;  
- 对外提供的`写入`和`刷新`功能,实际是有`StreamEncoder`来负责处理的;  
- 同样也扩展了一个便捷的类`FileWriter`,不可指定编码;  
- 概览:  
  ```java
  public class java.io.OutputStreamWriter extends java.io.Writer {
    public java.io.OutputStreamWriter(java.io.OutputStream, java.lang.String) throws java.io.UnsupportedEncodingException;
    public java.io.OutputStreamWriter(java.io.OutputStream);
    public java.io.OutputStreamWriter(java.io.OutputStream, java.nio.charset.Charset);
    public java.io.OutputStreamWriter(java.io.OutputStream, java.nio.charset.CharsetEncoder);
    public java.lang.String getEncoding();
    public void write(int) throws java.io.IOException;
    public void write(char[], int, int) throws java.io.IOException;
    public void write(java.lang.String, int, int) throws java.io.IOException;
    public void flush() throws java.io.IOException;
    public void close() throws java.io.IOException;
  }
  ```  

---  
`java.io.FileWriter`:便捷处理文件的字节转字符的输出流;  
- 不予赘述;  
- 概览:  
  ```java
  public class java.io.FileWriter extends java.io.OutputStreamWriter {
    public java.io.FileWriter(java.lang.String) throws java.io.IOException;
    public java.io.FileWriter(java.lang.String, boolean) throws java.io.IOException;
    public java.io.FileWriter(java.io.File) throws java.io.IOException;
    public java.io.FileWriter(java.io.File, boolean) throws java.io.IOException;
    public java.io.FileWriter(java.io.FileDescriptor);
  }
  ```  
