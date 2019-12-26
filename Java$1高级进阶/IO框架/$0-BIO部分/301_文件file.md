`java.io.File`:文件操作的工具类;  
- 放置在`java.io`包中的,因为在BIO库中流的创建都是依赖该实例的!  
- 关于文件的路径,若是相对路径,默认是从`$user.dir`开始的,这点要记住;  

---  
_构造过程_  
对于`父子路径`和`URI`的路径构造,不作解释,参源码.  
```java
// 以File(String)构造函数为例,大致是以下步骤:
// (1)对接收路径的合法行校验,包含可能的安全管理器校验;
// (2)对接收路径的参数,进行标准化处理,unix平台的源码主要处理符号/的问题;
// (3)初始化必要的属性,比如path,prefixLength;
// 以下演示可能出现路径标准化的情况:
new File("/"); // ==> /
new File("////"); // ==> /
new File("/abc/files"); // ==> /abc/files
new File("/abc//files//"); // ==> /abc/files
new File("//abc//files//"); // ==> /abc/files
new File("abc/files"); // ==> abc/files
new File("abc//files"); // ==> abc/files
new File("abc//files//");// ==> abc/file
```  
---  
_路径组件的获取`Path-component accessors`_  
- `getName()`:获取文件或者目录的名称;  
- `getParent()`:获取父路径;  
- `getParentFile()`:获取父路径的`File`对象;  
- `getPath()`:获取标准化处理后的路径;  

---  
_路径的操作`Path operations`_  
- `isAbsolute()`:查看是否是绝对路径;  
- `getAbsolutePath()`:获取绝对路径;  
- `getAbsoluteFile()`:获取使用绝对路径创建的File对象;  
- `getCanonicalPath()`:获取依赖宿主系统规格处理的路径;  
- `getCanonicalFile()`:获取使用规格化的路径创建的File对象;  
- `toURL()`:弃用(不自动转移非法字符),通过URI实例获取(替代方案);  
- `toURI()`:获取url路径,实际是`FILES`方案的URI的;  

---  
_属性的获取`Attribute accessors`_  
- `canRead()`:查看是否有可读权限;  
- `canWrite()`:查看是否有写入权限;  
- `exists()`:查看文件是否真实存在;  
- `isDirectory()`:查看是否是个目录;  
- `isFile()`:查看是否是个文件;  
- `isHidden()`:查看是否是隐藏文件;  
- `lastModified()`:获取最后一次修改的时间戳;  
- `length()`:获取文件的长度(单位是字节);  

---  
_文件的操作`File operations`_  
- `createNewFile()`:根据指定的路径创建新文件;  
- `delete()`:删除指定路径的文件;  
- `deleteOnExit()`:在JVM退出的时候,请求删除指定路径的文件;  
- `list()`:若路径是目录,返回目录中的所有文件和目录;否则返回null;  
- `list(FilenameFilter)`:添加过滤条件,返回目录中的所有文件和目录;若不是目录返回null;  
- `listFiles()`:思路和`list()`函数一致,不过内部循环创建了`File`的实例而已;  
- `listFiles(FilenameFilter)`:以文件或者目录名称过滤返回;  
- `listFiles(FileFilter)`:以File对象过滤返回;  
- `mkdir()`:以路径path创建目录;  
- `mkdirs()`:递归的形式创建目录;  
- `renameTo(File)`:重命名文件或者目录;  
- `setLastModified(long)`:更改最后一次修改的时间;  
- `setReadOnly()`:设置文件为只读文件;  
- `setWritable(boolean, boolean)`:设置文件是否是可写的,是否只有所有者有权限;  
- `setReadable(boolean, boolean)`:设置文件是否是可读的,是否只有所有者有权限;  
- `setReadable(boolean)`:设置文件是否是可读的;  
- `setExecutable(boolean, boolean)`:设置文件是否是可执行的,是否只有所有者有权限;  
- `setExecutable(boolean)`:设置文件是否是可执行的;  
- `canExecute()`:查看文件是否是可执行的;  

---  
_文件系统接口`Filesystem interface`_  
- `listRoots()`:列出当前宿主即可用的文件系统的根目录;  

---  
_磁盘相关操作`Disk usage`_  
注意是当前路径所在的分区的范围的!  
- `getTotalSpace()`:获取所在分区总共的磁盘容量;  
- `getFreeSpace()`:获取所在分区空闲的磁盘空间容量(单位bytes);  
- `getUsableSpace()`:获取所在分区可用的磁盘空间容量(单位bytes);  

---  
_临时文件的创建`Temporary files`_  
使用了内部类`java.io.File.TempDirectory`来实现的,存在默认的机制的:  
1. 默认的目录是`$java.io.tmpdir`,unix下默认是`/tmp`目录;  
1. 默认的后缀是`.tmp`,若不指定的话;  
1. 强制规定,临时文件的名称前缀长度至少是3位的;  

提供创建临时文件的静态方法:  
- `createTempFile(String, String, File)`:指定`前缀`,`后缀`,`临时目录`创建临时文件;  
- `createTempFile(String, String)`:在默认目录`$java.io.tmpdir`下创建指定`前缀`,`后缀`的临时文件;  

---  
_基本功能`Basic infrastructure`_  
- `compareTo(File)`:比较两个File对象,实际比较的是path,即String的比较;  
- `equals(Object)`:判断两个File对象是否相等,实际是比较path的;  
- `hashCode()`:实际获取path的哈希码,然后异或运算`1234321`;  
- `toString()`:输出的是path;
- `toPath()`:获取的实际是path;  
