_&#8195;&#8195;通常,在大多数的情况下,若没有随机访问文件的需求,使用任何功能的流时,都是依赖文件字节流的实例的,因此,它在整个`java.io`包中的作用是至关重要的._  

---  
`java.io.FileInputStream`:文件输入字节流;  
- 构造该对象的时候,是依赖`File`类实例的;  
- 可以说是`java.io`中包装流中最底层的流了,在整个包中起着中流砥柱的作用;  
- 对外提供了`读取`,`跳过`的功能,以及`查询可读字节总数`这3种最基本的功能;  
- 对于访问函数很少使用,一是获取`文件描述`对象,一是获取`文件管道`对象;  
  (前者是在构造器时期创建的,后者是在需要使用时候新建的.)  
- 和`RandomAccessFile`的关系在底层比较亲密,参openjdk8中,最终调用了函数`fileOpen`,`readSingle`,`readBytes`;  
- 父类中的具体方法采取的是覆盖的方式,使用本地方法实现;  
- 概览:  
  ```java
  public class java.io.FileInputStream extends java.io.InputStream {
    public java.io.FileInputStream(java.lang.String) throws java.io.FileNotFoundException;
    public java.io.FileInputStream(java.io.File) throws java.io.FileNotFoundException;
    public java.io.FileInputStream(java.io.FileDescriptor);
    public int read() throws java.io.IOException;
    public int read(byte[]) throws java.io.IOException;
    public int read(byte[], int, int) throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
    public int available() throws java.io.IOException;
    public void close() throws java.io.IOException;
    public final java.io.FileDescriptor getFD() throws java.io.IOException;
    public java.nio.channels.FileChannel getChannel();
  }
  ```  

---  
`java.io.FileOutputStream`:文件输出字节流;  
- 构造该对象的时候,是依赖`File`类实例的;  
- 可以说是`java.io`中包装流中最底层的流了,在这个包中起着中流砥柱的作用;  
- 对外只提供了`写入`这一单一的功能,基于字节;  
- 对于访问函数,和对应的输入流类似,很少使用,一个是`文件描述`,一个是`文件管道`;  
- 和`RandomAccessFile`的关系在底层中比较亲密,参openjdk8中,最终调用了函数`fileOpen`,`writeSingle`,`writeBytes`;  
- 父类中的具体方法采取的是覆盖的方式,使用本地方法实现;  
- 概览:  
  ```java
  public class java.io.FileOutputStream extends java.io.OutputStream {
    public java.io.FileOutputStream(java.lang.String) throws java.io.FileNotFoundException;
    public java.io.FileOutputStream(java.lang.String, boolean) throws java.io.FileNotFoundException;
    public java.io.FileOutputStream(java.io.File) throws java.io.FileNotFoundException;
    public java.io.FileOutputStream(java.io.File, boolean) throws java.io.FileNotFoundException;
    public java.io.FileOutputStream(java.io.FileDescriptor);
    public void write(int) throws java.io.IOException;
    public void write(byte[]) throws java.io.IOException;
    public void write(byte[], int, int) throws java.io.IOException;
    public void close() throws java.io.IOException;
    public final java.io.FileDescriptor getFD() throws java.io.IOException;
    public java.nio.channels.FileChannel getChannel();
  }
  ```  
