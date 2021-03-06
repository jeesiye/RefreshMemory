`java.io.RandomAccessFile`:随机读写流;  
- 这个常规类,可以说是`java.io`包库的亲儿子;  
- 它算是集成了四个常规类的所有功能:  
  - `FileInputStream`和`FileOutputStream`;  
  - `DataInputStream`和`DataOutputStream`;  
- 但是底层实现上除了`seek(int)`方法外,都是一致的,没有什么区别;  
  - 参`openjdk8`中,调用的最终函数;  
  - 打开文件,也是调用函数`fileOpen`;  
  - 读写文件,也是调用函数`readSingle`,`readBytes`,`writeSingle`,`writeBytes`;  
- 该常规类最突出的是寻找功能,即`seek(int)`;  
- 关于文件的打开模式,参源码中的构造器;  
- 其余方法不予赘述,参考集成的4个类;  
- _注意,该类中的下标是从0开始的,但是第一个字节的下标是1,需要特别注意!!!_  
- 概览:  
  ```java
  public class java.io.RandomAccessFile implements java.io.DataOutput,java.io.DataInput,java.io.Closeable {
    public java.io.RandomAccessFile(java.lang.String, java.lang.String) throws java.io.FileNotFoundException;
    public java.io.RandomAccessFile(java.io.File, java.lang.String) throws java.io.FileNotFoundException;
    public final java.io.FileDescriptor getFD() throws java.io.IOException;
    public final java.nio.channels.FileChannel getChannel();
    public int read() throws java.io.IOException;
    public int read(byte[], int, int) throws java.io.IOException;
    public int read(byte[]) throws java.io.IOException;
    public final void readFully(byte[]) throws java.io.IOException;
    public final void readFully(byte[], int, int) throws java.io.IOException;
    public int skipBytes(int) throws java.io.IOException;
    public void write(int) throws java.io.IOException;
    public void write(byte[]) throws java.io.IOException;
    public void write(byte[], int, int) throws java.io.IOException;
    public native long getFilePointer() throws java.io.IOException;
    public void seek(long) throws java.io.IOException;
    public native long length() throws java.io.IOException;
    public native void setLength(long) throws java.io.IOException;
    public void close() throws java.io.IOException;
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
  }
  ```  
