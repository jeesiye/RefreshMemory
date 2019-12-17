JUL配置文件的定位:  
- 加载配置文件的顺序:  
  1. `java.util.logging.config.class`系统属性指向的文件;  
  1. `java.util.logging.config.file`系统属性指向的文件;  
  1. `$java.home/lib/logging.properties`属性配置文件;  
  1. 具体加载的策略参见`LoggerManager.readConfiguration()`方法.  
- 关于系统属性`java.home`的位置:  
  - 安装JDK时配置的宿主机的环境变量`JAVA_HOME`,但指向的是`$JAVA_HOME/jre`目录.  
  - 使用语句`System.getProperty("java.home")`可查看.  
- 关于证明配置文件加载顺序的源码位置,在`java.util.logging.LoggerManager.readConfiguration()`方法内部:  
  ```java
  public void readConfiguration() throws IOException, SecurityException {
      checkPermission();

      // if a configuration class is specified, load it and use it.
      String cname = System.getProperty("java.util.logging.config.class");
      // 如果系统属性java.util.logging.config.class存在值,则加载该配置,完毕直接返回.
      // 否则,往下执行.
      if (cname != null) {
          try {
              // Instantiate the named class.  It is its constructor's
              // responsibility to initialize the logging configuration, by
              // calling readConfiguration(InputStream) with a suitable stream.
              try {
                  Class<?> clz = ClassLoader.getSystemClassLoader().loadClass(cname);
                  clz.newInstance();
                  return;
              } catch (ClassNotFoundException ex) {
                  Class<?> clz = Thread.currentThread().getContextClassLoader().loadClass(cname);
                  clz.newInstance();
                  return;
              }
          } catch (Exception ex) {
              System.err.println("Logging configuration class \"" + cname + "\" failed");
              System.err.println("" + ex);
              // keep going and useful config file.
          }
      }

      String fname = System.getProperty("java.util.logging.config.file");
      // 如果系统属性java.util.logging.config.file不存在值,则加载$java.hom/lib/logging.properties文件
      // 若存在值,跳过继续执行
      if (fname == null) {
          fname = System.getProperty("java.home");
          if (fname == null) {
              throw new Error("Can't find java.home ??");
          }
          File f = new File(fname, "lib");
          f = new File(f, "logging.properties");
          fname = f.getCanonicalPath();
      }
      try (final InputStream in = new FileInputStream(fname)) {
          final BufferedInputStream bin = new BufferedInputStream(in);
          readConfiguration(bin);
      }
  }
  ```  
- 关于如何设置默认不存在的系统属性值,使用`java`指令:  
  ```java
  用法: java [-options] class [args...]
             (执行类)
     或  java [-options] -jar jarfile [args...]
             (执行 jar 文件)
  其中选项包括:
  -D<名称>=<值>
                 设置系统属性
  ```  

---
