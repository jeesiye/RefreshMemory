#### 规范:  
- 无序列表  
- 分类  
- 无方法签名

#### 解析(部分):  
- 解析构造函数的执行过程:`File(String)`  
  - 此操作是依赖具体平台实现的jar包的(当前环境是unix);  
  - 大体分为三个步骤处理(不论是参数是单独的路径,还是父子路径,还是URI路径,基本思路一致):  
    - 对构造函数的参数合法性的校验;  
    - 对参数路径进行标准化处理(不排除传入合法的但野路子的路径),并把结果赋予给内置属性`File.path:String`;  
    - 判断接收的路径是绝对路径(1)还是相对路径(0),把结果赋予给内置属性`File.prefixLength:int`;  
  - 真正底层实现真实操作的是接口`FileSystem`,每个平台有不同的实现;  
  ```java
  // java.io.File.File(String)
  // 其他重载的构造函数,思路一致,不予整理  
  public File(String pathname) {
      // (1)检查路径参数是否为null
      if (pathname == null) {
          throw new NullPointerException();
      }
      // (2)对路径标准化处理
      this.path = fs.normalize(pathname);
      // (3)用1和0标记是否是绝对路径
      this.prefixLength = fs.prefixLength(this.path);
  }
  /*
  第二步:对路径标准化处理
  */
  // java.io.FileSystem.normalize(String)
  public abstract String normalize(String path);

  // 根据方法的动态绑定规则,定位以下实现方法:  
  // java.io.UnixFileSystem.normalize(String)
  // 方法释义:(不规范判断标准:连续的符号`/`)
  // 处理不规范的路径(若规范不处理直接返回)
  // 以下是典型不规范路径的代表:(`//`表示两个或两个以上,此处简化标记)
  //     (1) //  (不规范的根路径)
  //     (2) abc/abc/abc/ (结尾不规范)
  //     (3) //abc//abc//abc
  //     (4) //abc//abc//abc/
  //     (5) //abc//abc//abc//
  //     (6) abc//abc//abc
  //     (7) abc//abc//abc/
  //     (8) abc//abc//abc//
  public String normalize(String pathname) {
      int n = pathname.length();
      char prevChar = 0;
      for (int i = 0; i < n; i++) {
          char c = pathname.charAt(i);
          // 路径中,但凡出现连续的符号`/`,都进入if条件处理
          // 并把结果直接返回.
          if ((prevChar == '/') && (c == '/'))
              return normalize(pathname, n, i - 1);
          prevChar = c;
      }
      // 只有结尾处是不规范的,比如abc/abc/abc/
      // 只有这一种情况,返回规范路径:abc/abc/abc
      if (prevChar == '/') return normalize(pathname, n, n - 1);
      // 若是规范的路径,直接返回,不做任何处理.
      return pathname;
  }

  // 上面的方法,联动调用以下方法
  // java.io.UnixFileSystem.normalize(String, int, int)
  private String normalize(String pathname, int len, int off) {
      // ???????
      // 此处校验接收的参数len是否为0
      // 个人认为没有必要.原因是:调用此方法的方法内部若出现参数为0的情况,根本就不会调用该方法.
      // 可能为复用考虑...
      // ???????
      if (len == 0) return pathname;
      int n = len;
      // 此处while和if联合处理一种状况的路径,即不规范的根路径
      // 比如`/////`  或者`//`
      // 规范化,在unix中直接返回`/`
      while ((n > 0) && (pathname.charAt(n - 1) == '/')) n--;
      if (n == 0) return "/";
      StringBuffer sb = new StringBuffer(pathname.length());
      // 此处if处理开头没有连续符号`/`的路径情况
      // (1)abc///abc//abc
      // (2)abc///abc//abc//
      // 大体以上2种典型的代表路径
      if (off > 0) sb.append(pathname.substring(0, off));
      char prevChar = 0;//局部变量:上个字符占位比对
      // 以上一个字符判断比对,符号条件追加当前的字符
      for (int i = off; i < n; i++) {
          char c = pathname.charAt(i);
          // 此处if语句处理:
          // a 和 / 追加 a;  ->sb
          // / 和 a 追加 /;  -> sb
          // / 和 / 不追加跳过
          // 有四种情况的路径:
          // (1)abc///abc//abc ==> abc/abc/abc
          // (2)abc///abc//abc// ==> abc/abc/abc
          // (3)///abc//abc//abc// ==> abc/abc/abc
          // (4)///abc//abc//abc  ==> abc/abc/abc
          if ((prevChar == '/') && (c == '/')) continue;
          sb.append(c);
          prevChar = c;
      }
      return sb.toString();
  }
  /*
  第三步:用1和0标记是否是绝对路径:  
  */
  // java.io.FileSystem.prefixLength(String)
  public abstract int prefixLength(String path);

  // 根据Java方法的动态绑定规则,定位该实现方法
  // java.io.UnixFileSystem.prefixLength(String)
  public int prefixLength(String pathname) {
      // 过于简单,不予赘述.  
      if (pathname.length() == 0) return 0;
      return (pathname.charAt(0) == '/') ? 1 : 0;
  }
  ```  
- 获取绝对路径的函数:`File.getAbsolutePath()`  
  - 若当前对象的内置属性`path`是绝对路径,则直接返回;  
  - 若是相对路径,则前缀路径是从系统属性`$user.dir`开始的;  
  ```java
  // java.io.File.getAbsolutePath()
  public String getAbsolutePath() {
      // 把当前File的实例给传递进去
      return fs.resolve(this);
  }

  // java.io.FileSystem.resolve(File)
  public abstract String resolve(File f);

  // 方法动态绑定机制,定位该方法:  
  // java.io.UnixFileSystem.resolve(File)
  public String resolve(File f) {
      // 若是绝对路径直接返回
      if (isAbsolute(f)) return f.getPath();
      // 若不是,路径从$user.dir开始,拼接后返回  
      return resolve(System.getProperty("user.dir"), f.getPath());
  }
  ```  

#### 结构概览:  
```java
public class java.io.File implements java.io.Serializable, java.lang.Comparable<java.io.File> {
  public static final char separatorChar;
  public static final java.lang.String separator;
  public static final char pathSeparatorChar;
  public static final java.lang.String pathSeparator;
  public java.io.File(java.lang.String);
  public java.io.File(java.lang.String, java.lang.String);
  public java.io.File(java.io.File, java.lang.String);
  public java.io.File(java.net.URI);
  public java.lang.String getName();
  public java.lang.String getParent();
  public java.io.File getParentFile();
  public java.lang.String getPath();
  public boolean isAbsolute();
  public java.lang.String getAbsolutePath();
  public java.io.File getAbsoluteFile();
  public java.lang.String getCanonicalPath() throws java.io.IOException;
  public java.io.File getCanonicalFile() throws java.io.IOException;
  public java.net.URL toURL() throws java.net.MalformedURLException;
  public java.net.URI toURI();
  public boolean canRead();
  public boolean canWrite();
  public boolean exists();
  public boolean isDirectory();
  public boolean isFile();
  public boolean isHidden();
  public long lastModified();
  public long length();
  public boolean createNewFile() throws java.io.IOException;
  public boolean delete();
  public void deleteOnExit();
  public java.lang.String[] list();
  public java.lang.String[] list(java.io.FilenameFilter);
  public java.io.File[] listFiles();
  public java.io.File[] listFiles(java.io.FilenameFilter);
  public java.io.File[] listFiles(java.io.FileFilter);
  public boolean mkdir();
  public boolean mkdirs();
  public boolean renameTo(java.io.File);
  public boolean setLastModified(long);
  public boolean setReadOnly();
  public boolean setWritable(boolean, boolean);
  public boolean setWritable(boolean);
  public boolean setReadable(boolean, boolean);
  public boolean setReadable(boolean);
  public boolean setExecutable(boolean, boolean);
  public boolean setExecutable(boolean);
  public boolean canExecute();
  public static java.io.File[] listRoots();
  public long getTotalSpace();
  public long getFreeSpace();
  public long getUsableSpace();
  public static java.io.File createTempFile(java.lang.String, java.lang.String, java.io.File) throws java.io.IOException;
  public static java.io.File createTempFile(java.lang.String, java.lang.String) throws java.io.IOException;
  public int compareTo(java.io.File);
  public boolean equals(java.lang.Object);
  public int hashCode();
  public java.lang.String toString();
  public java.nio.file.Path toPath();
  public int compareTo(java.lang.Object);
}
```  
