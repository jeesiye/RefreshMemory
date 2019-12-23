#### 规范:  
`java.io.FileOutputStream`:在`java.io`包库中算是输出流的中流砥柱;  
- 功能提供与否,于基类无关,具体看本身的职责定位;  
- 提供了`输出`和`关闭流`的基本功能,为`write`和`close`方法;  
  `write`方法中有三个重载的方法,他们都是本地方法的实现,和基类继承的某些具体方法无关,都采取了覆盖;  
  `close`方法的实现保证了幂等性的操作,以及包装流中关闭流链条的功能;  
  参openjdk8中,最终执行的具体函数为`writeSingle`和`writeBytes`;  
- 其他的就是访问器的方法了,提供了获取`文件状态描述`和`文件管道`实例的功能,为方法`getFD`和`getChannel`;  
  需要注意的是`FileDecriptor`是被初始化的,`FileChannel`若不调用是不会被初始化的.  
- 总结:基于字节的输出流,提供了`输出`和`关闭流`的功能;  

#### 解析(部分):  
- 基于字节的文件输出流的构造过程:`FileOutputStream(File, boolean)`  
  - 处理思路和对应的输入流基本类似;  
  - 执行步骤大致分为三个步骤:  
    - 对接收的参数进行相关的合法性校验,包括安全管理器(若存在);  
    - 初始化`fd`成员变量,并把当前对象添加到关闭流链条上,调用`fd`的`attch`方法;  
    - 初始化`append`和`path`成员变量;  
    - 打开物理位置上的文件(本地方法实现);
  ```java
  public FileOutputStream(File file, boolean append)
      throws FileNotFoundException
  {
      // 检查接收的file对象是否为null;
      // 若通过,把file对象标准化处理后的path赋值给局部变量name(留做后用赋予给成员变量path)  
      String name = (file != null ? file.getPath() : null);
      // 若存在安全管理器,进行安全检查  
      SecurityManager security = System.getSecurityManager();
      if (security != null) {
          security.checkWrite(name);
      }
      // 检查name的值是否为null,即接收的文件路径是不是空值;  
      if (name == null) {
          throw new NullPointerException();
      }
      // 检查file对象是否是不合法的,实际检查的是路径path的第一个字符是否不在编码表中
      // 对于该方法的源码,参$_FileInputStream文件;  
      if (file.isInvalid()) {
          throw new FileNotFoundException("Invalid file path");
      }
      // 调用默认构造器,其状态为-1不合法的,依赖后期处理;  
      this.fd = new FileDescriptor();
      // 讲当前对象添加到关闭流的链条上
      // 该方法的源码,参$_FileInputStream文件;  
      fd.attach(this);
      // 是否采用追加的模式,创建文件输出流
      // 若不是追加的模式,就是覆盖的默认.
      // 在其他的构造器中,若不指定该参数,默认是覆盖的默认打开文件的,这点需要注意!  
      this.append = append;
      // 赋值成员变量path,也就是文件的路径
      this.path = name;
      // 使用本地方法open0打开文件
      // 参openjdk8中,最终调用的是`fileOpen`函数  

      open(name, append);
  }
  ```  
- 至于其他构造器,思路类似,不予赘述;  
- 三个重载的`write`方法:  
  - 底层都是本地方法的实现,和基类中的规范定义无关;  
  - 具体细节不予赘述,参`$_FileInputStream`文件和`openjdk8`;  
- 关于`close`方法,思路和`FileInputStream`一致,参其相关文件;  
- 关于方法`getFD`和`getChannel`,不予赘述;  

#### 结构:  
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
