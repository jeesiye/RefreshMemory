#### 规范:  
`java.io.FileInputStream`:在io包库中算是中流砥柱;  
- 功能与否与基类无关,具体看本身职责定位;  
- 基于字节的`读取`和`关闭流`的操作,为方法`read`和`close`;  
  对于`read`方法,都是基于本地实现的,参openjdk8是为本地方法`readSingle`和`readBytes`;  
  和`InputStream`中重载的具体`read`方法无关;  
- `跳过`读取功能的支持,为`skip`方法;  
  本地方法的实现,参openjdk8为匹配方法,直接操作指针,使用`seek`概念;  
- 查询输入流属性信息`可读取字节`的支持,为`available`方法;  
  本地方法的实现,参openjdk8为匹配方法;  
- 其他访问器的方法,获取`FileDescriptor`和`FileChannel`的实例,为`getFD`和`getChannel`方法;  
  对于聚合的`fd`对象,在该对象构造的时期,就联动初始化的(直接调用`FileDescriptor`的无参构造器);  
  对于聚合的`channel`对象,构造期间和使用期间几乎不使用,但若是获取,会触发使用其实现类构造实例,并返回;  
- 总结,该流是输入流中的中流砥柱,提供`读取`,`跳过`,`查询`,`关闭`,这四种最基本的功能;  

---  
#### 解析(部分):  
- 文件输入流的构建过程:`FileInputStream(File)`  
  - 对参数相关的合法性校验,包括安全管理器方面(若存在);  
  - 初始化成员变量`fd`,并使用`fd`把当前对象添加到关闭链条上;    
  - 初始化成员变量`path`;  
  - 打开物理位置上的文件;  
  ```java
  // java.io.FileInputStream.FileInputStream(File)
  // 其他重载的构造器不予赘述,思路基本类似;  
  public FileInputStream(File file) throws FileNotFoundException {
      // 检查file对象实例是否为null;
      // 若为null,直接赋予局部变量name为null,后续会作分支,直接抛出空指针异常
      // File.getPath()获取经过File对象标准化处理后的文件路径;  
      String name = (file != null ? file.getPath() : null);
      // 检查当前环境是否配置了安全管理器
      SecurityManager security = System.getSecurityManager();
      if (security != null) {
          // 若存在安全管理器,安全检查
          security.checkRead(name);
      }
      // 若接收的参数实例为null,直接抛出空指针异常;  
      if (name == null) {
          throw new NullPointerException();
      }
      // 检查文件对象是否是非法的,说白了就是文件路径是否是非法的.
      // 其内部只是检查路径中的第一个字符是否不在编码中
      // 参以下标记$_00
      if (file.isInvalid()) {
          throw new FileNotFoundException("Invalid file path");
      }
      // FileDescriptor:这是一个文件输入的类,描述文件:输入,输出,错误三种状态  
      // 调用的无参构造器,其内部指示的状态是-1,是非法的,待后续调整
      fd = new FileDescriptor();
      // 将当前对象添加到关闭流链条上  
      // 主要涉及处理包装流的时候,为简化代码,提供的内部的处理方式.
      // 参以下标记$_01  
      fd.attach(this);
      // 初始化成员变量,这实际就是File标准化处理后的文件路径
      path = name;
      // 调用本地方法open0
      // 参openjdk8中,实际调用的是`fileOpen`方法  
      open(name);
  }

  // $_00
  // File类中检查文件是否存在的方法
  // java.io.File.isInvalid()
  // 注意方法的声明,该方法只能在`java.io`包中被调用.  
  final boolean isInvalid() {
      // status的引用是File类的内部枚举类File$PathStatus
      if (status == null) {
          // 这种检查的方式是:检查路径中的第一个字符是否不在编码表中;  
          // 其中的path是经过标准化处理的
          status = (this.path.indexOf('\u0000') < 0) ? PathStatus.CHECKED
                                                     : PathStatus.INVALID;
      }
      return status == PathStatus.INVALID;
  }

  // $_01
  // java.io.FileDescriptor.attach(Closeable)
  // 解析该方法,需要查看该类的两个成员变量:
  // private Closeable parent; // 包装流中的最内部的流,可以这么认为
  // private List<Closeable> otherParents; // 包装流中的,依次包装的存储,可以这么认为
  // 该方法的这种处理,主要是为了在使用包装流的时候;  
  //      做到以下这种操作:关闭最外部的流,其内部包装的流都会依次关闭.  
  //   验证以上的结论,参见常规流的close和FileDescriptor的closeAll方法即可.  
  synchronized void attach(Closeable c) {
      // 成员parent是否为null,若是把接收的流赋予
      // 首次调用的时候,一般都为null  
      // 在该对象初始化的时候,不会对处理parent成员变量  
      if (parent == null) {
          // first caller gets to do this
          parent = c;
      } else if (otherParents == null) {
          // 首次调用otherParents是为null的
          // 用数组列表存储
          // 依次把内部流parent添加,然后再添加当前的流对象
          otherParents = new ArrayList<>();
          otherParents.add(parent);
          otherParents.add(c);
      } else {
          // 如果是超过三层以及以上的包装,不需要初始化parent和otherParents
          // 直接讲当前接收的流添加到数组列表中  

          otherParents.add(c);
      }
  }
  ```  
- 至于其他重载构造器,思路一直,不予赘述;  
- 字节输入流读取方法,三个重载的`read`方法:  
  - `read(byte[], int, int)`调用的是本地方法,参openjdk8最终调用的是`readBytes`函数;  
  - `read(byte[])`:内部调用重载的方法`read(byte[], int, int)`;  
  - `read()`:内部调用本地方法实现,参openjdk8最终调用的是`readSingle`函数;  
  - 仅作简单使用标记,源码分析超出语言范围;  
  ```java
  byte[] arr = new byte[24];
  FileInputStream in = new FileInputStream("files");
  int read = in.read();
  System.out.println(read);// out:97
  int size = in.read(arr);
  System.out.println(size);// out:7
  int limit = in.read(arr, 10, 10);
  System.out.println(limit);// out:-1
  System.out.println(Arrays.toString(arr));
  // [98, 99, 10, 49, 50, 51, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
  // 其中的10在编码表中表示的是换行符号
  ```  
- 文件输入流的关闭过程:`close()`  
  - 总体分为三步操作;  
  - 检查内部的布尔关闭标志,若为真什么也不做(直接返回),保证幂等性;  
  - 检查成员变量channel是否为null,若存在实例调用,调用该对象的close方法;  
  - 调用fd的closeAll方法,关闭包装流中的所有流;  
  ```java
  // java.io.FileInputStream.close()
  // 关闭流的操作,遵循了接口中的实现规范:  
  //      (1)幂等性操作;  
  //      (2)链条式的关闭(内部使用数组链表实现);  
  public void close() throws IOException {
      // 加锁同步操作:检查当前对象是否已经处理关闭的状态;  
      // 主要检查内部的布尔关闭标志
      synchronized (closeLock) {
          if (closed) {
              // 若已经关闭了,直接返回,不进行后续的操作
              return;
          }
          // 先设置布尔关闭标志,这也是为什么使用同步块,防止并发场景的未知错误  
          closed = true;
      }
      // 检查聚合的FileChannel是否存在实例
      // 不继续往下深究,涉及nio中的会解释该点;  
      if (channel != null) {
         channel.close();
      }
      // 实际关闭的操作使用本地方法实现,传递Closeable的实现类(使用匿名内部类的方式)
      fd.closeAll(new Closeable() {
          public void close() throws IOException {
             close0();
         }
      });
  }

  // java.io.FileDescriptor.closeAll(Closeable)
  @SuppressWarnings("try")
  synchronized void closeAll(Closeable releaser) throws IOException
      // 检查FileDescriptor中的布尔关闭标志
      // 该方法是个同步方法,防止并发同时关闭操作
      // 如果标记已经关闭,什么也不做,直接返回;  
      if (!closed) {
          closed = true; // 首先设置关闭标志为true
          IOException ioe = null;
          try (Closeable c = releaser) {
              if (otherParents != null) {
                  // foreach循环遍历包装流中的流
                  // 依次关闭流,调用流实例的close方法
                  for (Closeable referent : otherParents) {
                      try {
                          referent.close();
                      } catch(IOException x) {
                          // 存在异常,处理压制到异常链条信息中

                          if (ioe == null) {
                              ioe = x;
                          } else {
                              ioe.addSuppressed(x);
                          }
                      }
                  }
              }
          } catch(IOException ex) {
              // 如果存在异常,添加到异常链条中
              // 其中的处理方法,会进行鉴别,不继续查看  
              if (ioe != null)
                  ex.addSuppressed(ioe);
              ioe = ex;
          } finally {
              // 如果存在异常,抛出压制处理后的异常链条信息
              if (ioe != null)
                  throw ioe;
          }
      }
  }
  ```  
- 关于`available`和`skip`方法:  
  - 都是使用本地方法实现的,不作解析;  
  - 简单的使用:  
  ```java
  FileInputStream in = new FileInputStream("files");
  System.out.println(string.available());// out:8
  in.skip(6);
  System.out.println(in.read());// out:51
  System.out.println(in.read());// out:10
  System.out.println(in.read());// out:-1
  ```  
- 关于`getFD`方法,不予赘述;  
- 关于`getChannel`方法,涉及nio,在该模块中解释,此处不解释;  

---  
#### 结构:  
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
