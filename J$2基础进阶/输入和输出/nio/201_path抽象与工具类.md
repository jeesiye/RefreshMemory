`java.nio.file.Paths`:路径工具类;  
- 有两种途径获取`Path`对象,一个是路径部件,一个是URI;  
- 当前平台获取的实际类型是`sun.nio.fs.UnixPath`;  
- 概览:  
  ```java
  public final class java.nio.file.Paths {
    public static java.nio.file.Path get(java.lang.String, java.lang.String...);
    public static java.nio.file.Path get(java.net.URI);
  }
  ```  

---  
`java.nio.file.Path`:_路径的抽象(通常无需关注实现者)_  
- 路径抽象的接口,使用依赖平台的实现;  
- 通过`Paths`工具类来获取该对象;  
- 路径中有一个概念,即部件;  
  路径中一层目录或者是文件名,就是所谓的部件,路径通常由多个部件组成;  
- 但不局限一种途径获取该实例:  
  通过`File.toPath()`函数,可获取实例;  
- 功能函数分类:  
  - _获取相关的函数_:`getFileName()`,`getFileSystem()`,`getName(int)`,`getNameCount()`,`getParent()`,`getRoot()`;  
  - _查询相关的函数_:`isAbsolute()`,`startsWith(Path)`,`startsWith(String)`,`endsWith(Path)`,`endsWith(String)`,`equals(Object)`,`compareTo(Path)`,`hashCode()`;  
  - _路径处理相关的函数_:`subpath(int, int)`,`normalize()`,`resolve(Path)`,`resolve(String)`,`resolveSibling(Path)`,`resolveSibling(String)`,`relativize(Path)`;  
  - _事件监听相关的函数_:`register(WatchService, Kind<?>[], Modifier...)`,`register(WatchService, Kind<?>...)`;  
  - _转换相关的函数_:`toString()`,`toFile()`,`toUri()`,`toAbsolutePath()`,`toRealPath(LinkOption...)`,`iterator()`;  


```java
public interface java.nio.file.Path extends java.lang.Comparable<java.nio.file.Path>, java.lang.Iterable<java.nio.file.Path>, java.nio.file.Watchable {


  /* 获取相关的函数 */

  // (1)获取路径中最后一个部件的名称;
  // (2)比如路径 a/b/c/d ,获取的是就是 d
  public abstract java.nio.file.Path getFileName();
  // (1)获取创建它的fs对象;
  // (2)返回的是 FileSystem 的引用;
  // (3)实际类型是 LinuxFileSystem 的实例;
  public abstract java.nio.file.FileSystem getFileSystem();
  // (1)获取指定 index 的部件;  
  // (2)路径部件的 index 是从0开始的;
  // (3)若接收的 index 是负数或者越界,则抛`非法参数异常`;  
  public abstract java.nio.file.Path getName(int);
  // (1)获取路径中部件的总数;
  // (2)比如路径 a/b/c/d/e/f ,返回的部件个数是6个;
  public abstract int getNameCount();
  // (1)获取路径中的父路径;
  // (2)比如a/b/c/d,返回的路径对象是a/b/c
  public abstract java.nio.file.Path getParent();
  // (1)获取路径中的根路径;
  // (2)返回的结果是依赖于宿主机的文件系统类别的;
  // (3)若是相对路径,返回null;
  public abstract java.nio.file.Path getRoot();


  /* 查询相关的函数 */

  // (1)查看是否是绝对路径;
  // (2)当前平台 UnixPath 中是判断以下两个要素同时存在的;
  //    a. 路径不能为空
  //    b. 路径中的第一个字节必须是47,即符号 `/`
  public abstract boolean isAbsolute();
  // (1)查看路径是否是指定路径或者字符串开始的;
  // (2)对于字符串类型的参数,在内部会转换为Path实例,然后调用重载的方法;
  public abstract boolean startsWith(java.nio.file.Path);
  public abstract boolean startsWith(java.lang.String);
  // (1)查看路径是否以指定的路径或者字符串开始的;
  // (2)对于字符串类型的参数,在内部会转换为Path实例,然后调用重载的方法;
  public abstract boolean endsWith(java.nio.file.Path);
  public abstract boolean endsWith(java.lang.String);
  // (1)查看当前路径是否和指定的对象相等;
  // (2)当前平台 UnixPath 的实现中,主要进行了以下两步的处理:
  //        a. 判断参数是否为null,且是否是UnixPath的实例;
  //              不通过直接返回false
  //        b. 然后循环比较转换后的字节数组是否相等;若相等返回true;
  public abstract boolean equals(java.lang.Object);
  // (1)查看当前路径和指定路径的比较结果;
  // (2)遵循Java比较器的结果规范;
  // (3)当前平台 UnixPath 中的实现,实际是循环比较转换后的字节数组的;
  public abstract int compareTo(java.nio.file.Path);
  public int compareTo(java.lang.Object); // 类型擦除产生的方法
  // (1)查询当前路径的哈希码;
  // (2)依赖平台的实现;
  // (3)UnixPath 的处理方案: var1 = 31 * var1 + (this.path[var2] & 255);
  public abstract int hashCode();


  /* 处理路径相关的函数 */

  // (1) 截取当前路径中指定范围的部件;
  // (2) 注意,这里所说的是部件,而不是路径字符串中的index;
  // (3) 关于参数,开始的部件index是包含的,结束的部件index是不包含的;
  // (4) 比如路径aaa/bbb/c/d,截取(0,2),则返回aaa/bbb;
  public abstract java.nio.file.Path subpath(int, int);
  // (1) 标准化处理当前的路径;
  // (2) 这里的标准化处理是区别于File类的路径标准化处理的;
  // (3) 这里标准化处理的是符号 `.`和`..`,而File中的标准化处理的是符号 `/`;
  // (5) 需要注意的是,针对的是部件,若是 a/..b/c,是不会处理的;
  //      以及超过两个以上dot的部件也是不会处理的;
  // (6) 比如路径 `a/../.../.b/c/..` ,会处理为 a/.../.b/c
  //      不对的,实际结果是 `.../.b`;
  //     该方法具体的规则是高度依赖平台的实现的,一般情况该方法不建议使用;
  public abstract java.nio.file.Path normalize();
  // (1)拼接路径,重载了函数,支持参数类型有Path和String;
  // (2)若接收的路径是absolute路径,则返回接收的绝对路径;
  // (3)若接收的路径是relative路径,则返回 当前路径 + 相对路径 ,拼接后的路径;
  public abstract java.nio.file.Path resolve(java.nio.file.Path);
  public abstract java.nio.file.Path resolve(java.lang.String);
  // (1) 返回同级的指定路径,重载了函数,支持参数类型有Path和String;
  // (2) 若接收的是absolute路径,则返回接收的绝对路径;
  // (3) 若接收的是relative路径,则返回的是 当前路径的父路径 + 相对路径 ,拼接后的路径;
  public abstract java.nio.file.Path resolveSibling(java.nio.file.Path);
  public abstract java.nio.file.Path resolveSibling(java.lang.String);
  // (1) 获取当前路径相对于指定路径的,相对路径;
  // (2) 内部实现过程和具体的宿主平台实现可能有所出入,需要查看具体的实现过程;
  // (3) 和normalize函数一样,如非特别需要,一般情况下不建议使用;
  public abstract java.nio.file.Path relativize(java.nio.file.Path);


  /* 注册文件的监听事件*/
  // 放到他处单独备忘;
  public abstract java.nio.file.WatchKey register(java.nio.file.WatchService, java.nio.file.WatchEvent$Kind<?>[], java.nio.file.WatchEvent$Modifier...) throws java.io.IOException;
  public abstract java.nio.file.WatchKey register(java.nio.file.WatchService, java.nio.file.WatchEvent$Kind<?>...) throws java.io.IOException;


  /* 路径的转换相关的函数 */

  // (1)把当前路径转换为字符串
  public abstract java.lang.String toString();
  // (1)把当前路径转换为 File 实例;
  // (2)File和Path是支持互相转换的;
  public abstract java.io.File toFile();
  // (1)把当前路径转换为 URI 实例;
  // (2)当前平台 UnixPath 使用的是 UnixUriUtils 工具类;(是FILES方案的)
  public abstract java.net.URI toUri();
  // (1)把当前路径转换为绝对路径(若是相对路径的化);
  // (2)若本身是绝对路径,则什么也不做,直接返回;
  // (3)默认机制和 File 类是一样的,都是从 $user.dir 开始的;
  public abstract java.nio.file.Path toAbsolutePath();
  // (1)把当前路径转换为真实存在的路径;
  // (2)若当前的路径,在文件系统中是不存在的,会抛出异常;
  // (3)路径若是相对路径,默认是从$user.dir开始的;
  // (4)关于参数中的枚举类型LinkOption,目前是只有一个实例的LinkOption.NOFOLLOW_LINKS;
  public abstract java.nio.file.Path toRealPath(java.nio.file.LinkOption...) throws java.io.IOException;
  // (1) 把当前路径中所有的部件,都解析到迭代器中;
  // (2) 比如路径 a/b/c ,转换后的迭代器中就存储了三个元素,即三个部件;
  public abstract java.util.Iterator<java.nio.file.Path> iterator();

}
```  
