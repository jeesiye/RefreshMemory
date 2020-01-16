装饰流的关闭机制:  
- `java.io.FilterInputStream`  
- `java.io.FilterOutputStream`  
- 支持链条式的关闭流操作;  
- 因为是使用聚合的方式内置顶层基类的;  
- _涉及使用的常规类_:  
  - `BufferedInputStream`和`BufferedOutputStream`  
  - `DataInputStream`和`DataOutputStream`  
  - `PushbackInputStream`  
  - `PrintStream`  

---  
#### 缓冲字节数组流
_这里的缓冲指的是:在读取的时候,会触发式的把缓冲数组都填满,在满足条件的情况下,实际读取的是临时内存中数组中的字节;在写入的时候,只要达不到阀值,或者没有手动调用刷新功能,写入字节实际是写入到内部维护的缓冲数组中的._  

`java.io.BufferedInputStream`:缓冲字节数组输入流;  
- 若不指定大小,默认的缓冲大小是8192;  
- 使用字节数组维护的,最大的缓冲大小是`Integer.MAX_VALUE-8`;  
- 缓冲的操作,是维护:记录当前下标`pos`,缓冲长度`count`;  
- 标记和重置操作,是维护:记录标记下标`markpos`,记录标记长度`marklimit`;  
- 内部维护作业的时候,涉及复杂的计算调用`fill()`方法来填充缓冲
- 概览
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
- 总结:  
  - 对象在创建的时期,指定缓冲的大小;  
  - 缓冲大小是不能超过数组本身的长度限制的,否则OOM;  
  - 除了提供基本的`读取`操作,还提供了`标记`和`重置`的操作;  

`java.io.BufferedOutputStream`:缓冲字节数组输出流;  
- 和对应的输入流类似,若不指定缓冲大小,默认是8192;  
- 和对应的输入流类似,内部使用字节数组实现,最大缓冲大小是`Integer.MAX_VALUE-8`;  
- 缓冲写入的操作`write`实际上,是把字节写入到内部的数组`buf`,当达到阀值触发调用私有方法`flushBuffer()`把数组一次性写入到目标源中;  
- 在最后一次写入操作后,或者在手动关闭流之前,是有必要执行`flush()`方法的;  
- 结构:  
  ```java
  public class java.io.BufferedOutputStream extends java.io.FilterOutputStream {
    public java.io.BufferedOutputStream(java.io.OutputStream);
    public java.io.BufferedOutputStream(java.io.OutputStream, int);
    public synchronized void write(int) throws java.io.IOException;
    public synchronized void write(byte[], int, int) throws java.io.IOException;
    public synchronized void flush() throws java.io.IOException;
  }
  ```  
- 总结:  
  - 对象是在创建的时期,指定缓冲的大小的;  
  - 缓冲的大小是不能超过数组本身长度的限制的,否则OOM;  
  - 只提供了基本的写入缓冲`write`和刷新缓存`flush`的功能操作;  

---  
#### 二进制流
_这里的二进制流,说白了,核心部分就是提供读写Java中的8种数据类型结构的数据.但是不仅仅这些,还有基本的字节的读写操作,以及读取UTF编码表的操作._  

`java.io.DataInputStream`:二进制输入流;  
`java.io.DataOutputStream`:二进制输出流;  
- 这是装饰流,依赖具体的输入流或者输出流实例的,在`java.io`包中通常使用最底层的输入流是`FileInputStream`或者`FileOutputStream`的;  
- 二进制的操作,无非就是操作`Java`中8种基本的数据类型,实现的过程就是二进制运算中的`位移运算`和`与运算`这2种运算;  
- 有时为了保证读取到的是一个字节,会和`0xff`与运算的;  
- 查看操作8种基本类型的读写功能时,可以得出Java是采用高位在前的方式存储长数据的;  
- 两者的共性:  
  - 因为构造依赖具体的流实例,以及实现了`InputStream`和`OutputStream`抽象类;  
    提供了最基本的`读写字节`的操作,以及`读写到指定数组`的操作;  
  - 突出本身职责的功能,提供了读写`Java中8种基本类型`的功能操作;  
  - 提供基于UTF编码表的读写操作;  
- 两者的异性:  
  - 输入流:  
    - `read(byte[],int,int)`和`readFully(byte[],int,int)`的区别:  
      前者对参数没有过多的限制,可以读取0个字节,但后者是有限制的,必须是大于0个字节,即第三个参数的限制;  
    - 提供了支持基于无符号位`unsinged`的读取操作;  
  - 输出流:  
    - 该类中提供了一个刷新方法`flush()`;对于该方法的调用,要根据构造时接收的输出流实例的种类;  
      若包装的输出流实例支持缓冲的功能,调用该方法是有效的;若不支持,输出流基类的`flush()`方法是空方法,没有意义;  
- 二进制输入流概览:  
  ```java
  public class java.io.DataInputStream extends java.io.FilterInputStream implements java.io.DataInput {
    public java.io.DataInputStream(java.io.InputStream);
    public final int read(byte[]) throws java.io.IOException;
    public final int read(byte[], int, int) throws java.io.IOException;
    public final void readFully(byte[]) throws java.io.IOException;
    public final void readFully(byte[], int, int) throws java.io.IOException;
    public final int skipBytes(int) throws java.io.IOException;
    public final boolean readBoolean() throws java.io.IOException;
    public final byte readByte() throws java.io.IOException;
    public final int readUnsignedByte() throws java.io.IOException;
    public final short readShort() throws java.io.IOException;
    public final int readUnsignedShort() throws java.io.IOException;
    public final char readChar() throws java.io.IOException;
    public final int readInt() throws java.io.IOException;
    public final long readLong() throws java.io.IOException;
    public final float readFloat() throws java.io.IOException;
    public final double readDouble() throws java.io.IOException;
    public final java.lang.String readLine() throws java.io.IOException;
    public final java.lang.String readUTF() throws java.io.IOException;
    public static final java.lang.String readUTF(java.io.DataInput) throws java.io.IOException;
  }
  ```  
- 二进制输出流概览:  
  ```java
  public class java.io.DataOutputStream extends java.io.FilterOutputStream implements java.io.DataOutput {
    public java.io.DataOutputStream(java.io.OutputStream);
    public synchronized void write(int) throws java.io.IOException;
    public synchronized void write(byte[], int, int) throws java.io.IOException;
    public void flush() throws java.io.IOException;
    public final void writeBoolean(boolean) throws java.io.IOException;
    public final void writeByte(int) throws java.io.IOException;
    public final void writeShort(int) throws java.io.IOException;
    public final void writeChar(int) throws java.io.IOException;
    public final void writeInt(int) throws java.io.IOException;
    public final void writeLong(long) throws java.io.IOException;
    public final void writeFloat(float) throws java.io.IOException;
    public final void writeDouble(double) throws java.io.IOException;
    public final void writeBytes(java.lang.String) throws java.io.IOException;
    public final void writeChars(java.lang.String) throws java.io.IOException;
    public final void writeUTF(java.lang.String) throws java.io.IOException;
    public final int size();
  }
  ```  

---  
#### 其他的装饰流  
_总计3个,1个弃用(LineNumberInputStream),2个可用._  

`java.io.LineNumberInputStream`:弃用的类:  
_错误的假设字节足够代表所有的字符,详细可参考UTF-16,所以是不推荐使用的._  

`java.io.PushbackInputStream`:可回退操作的输入流;  
- 内部使用数组来实现回退的操作;  
- 实现思路和缓冲流是不一样的,读取的时候是真实的读取,不会存储到`buf`中,在回退操作的时候,是把数据存储到`buf`的;  
- 构造该对象的时候,一定要指定可回退的范围,默认的回退长度是1的.  
- 该对象提供了`读取`,`回退`,`跳过`三种操作的;至于`标记`是不支持的;  
- 概览:  
  ```java
  public class java.io.PushbackInputStream extends java.io.FilterInputStream {
    public java.io.PushbackInputStream(java.io.InputStream, int);
    public java.io.PushbackInputStream(java.io.InputStream);
    public int read() throws java.io.IOException;
    public int read(byte[], int, int) throws java.io.IOException;
    public void unread(int) throws java.io.IOException;
    public void unread(byte[], int, int) throws java.io.IOException;
    public void unread(byte[]) throws java.io.IOException;
    public int available() throws java.io.IOException;
    public long skip(long) throws java.io.IOException;
    public boolean markSupported();
    public synchronized void mark(int);
    public synchronized void reset() throws java.io.IOException;
    public synchronized void close() throws java.io.IOException;
  }
  ```  

`java.io.PrintStream`:通用的字节输出流  
- 算是最常用的输出流,`System.out`获取的就是该实例;  
- 和`PrintWriter`的区别,大概是前者使用的是默认的编码,后者可以手动指定编码.(当前前者也能指定)  
- 内部聚合了`缓冲字符流`和`字节转换字符流`,加上Java方法动态绑定的机制,不能以字节流和字符流作为区分;  
- 输出的类型是非常广泛的:  
  - 支持输出Java中8种基本数据类型,而且是可选是否输出后换行的;  
  - 支出输出引用类型,内部的参数是`java.lang.Object`;  
  - 支出格式化输出,参`java.util.Formatter`格式化工具类;  
  - 支持基于字节的输出,但不常用;  
- 概览:  
  ```java
  public class java.io.PrintStream extends java.io.FilterOutputStream implements java.lang.Appendable,java.io.Closeable {
    public java.io.PrintStream(java.io.OutputStream);
    public java.io.PrintStream(java.io.OutputStream, boolean);
    public java.io.PrintStream(java.io.OutputStream, boolean, java.lang.String) throws java.io.UnsupportedEncodingException;
    public java.io.PrintStream(java.lang.String) throws java.io.FileNotFoundException;
    public java.io.PrintStream(java.lang.String, java.lang.String) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException;
    public java.io.PrintStream(java.io.File) throws java.io.FileNotFoundException;
    public java.io.PrintStream(java.io.File, java.lang.String) throws java.io.FileNotFoundException, java.io.UnsupportedEncodingException;
    public void flush();
    public void close();
    public boolean checkError();
    public void write(int);
    public void write(byte[], int, int);
    public void print(boolean);
    public void print(char);
    public void print(int);
    public void print(long);
    public void print(float);
    public void print(double);
    public void print(char[]);
    public void print(java.lang.String);
    public void print(java.lang.Object);
    public void println();
    public void println(boolean);
    public void println(char);
    public void println(int);
    public void println(long);
    public void println(float);
    public void println(double);
    public void println(char[]);
    public void println(java.lang.String);
    public void println(java.lang.Object);
    public java.io.PrintStream printf(java.lang.String, java.lang.Object...);
    public java.io.PrintStream printf(java.util.Locale, java.lang.String, java.lang.Object...);
    public java.io.PrintStream format(java.lang.String, java.lang.Object...);
    public java.io.PrintStream format(java.util.Locale, java.lang.String, java.lang.Object...);
    public java.io.PrintStream append(java.lang.CharSequence);
    public java.io.PrintStream append(java.lang.CharSequence, int, int);
    public java.io.PrintStream append(char);
    public java.lang.Appendable append(char) throws java.io.IOException;
    public java.lang.Appendable append(java.lang.CharSequence, int, int) throws java.io.IOException;
    public java.lang.Appendable append(java.lang.CharSequence) throws java.io.IOException;
  }
  ```  
