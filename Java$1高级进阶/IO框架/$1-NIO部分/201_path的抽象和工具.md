`java.nio.file.Paths`:路径工具类;  
- `get(String, String...)`:根据路径获取`Path`对象;  
- `get(URI)`:根据URI获取`Path`对象;  
- 实际获取的是宿主机平台实现的`Path`扩展类;  
  ```java
  Path path = Paths.get("files");
  System.out.println(path.getClass()); // class sun.nio.fs.UnixPath
  ```  

---  
`java.nio.file.Path`:_路径的抽象(通常无序关注实现者)_  
- 这是一个路径抽象的接口,使用的时候依赖具体的平台实现,常用的方式是通过`Paths`工具类来获取对象的;  
- 但不局限一种途径获取该实例:  
  - 通过`File.toPath()`函数,可获取实例;  

> _访问器相关的函数_  

- `getFileName()`:获取路径中最后一个部件的名称;  
  - 比如路径`a/b/c/d`,获取的是`d`;  
- `getFileSystem()`:获取创建它的`fs`对象;  
  - 当前平台获取的是`FileSystem`的实例;  
  - 实际类型是`LinuxFileSystem`;  
  ```java
  FileSystem fs = path.getFileSystem();
  System.out.println(fs.getClass());// class sun.nio.fs.LinuxFileSystem
  ```  
- `getNameCount()`:获取路径中部件的总数;  
  比如`a/b/c/d/e/f`,部件的个数是六个;  
