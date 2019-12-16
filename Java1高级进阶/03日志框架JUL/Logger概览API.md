#### 记录日志的方法:  
- `log`方法:手动指定级别的简单消息记录;  
- `logp`方法:带有类名和方法名;  
- `logrb`方法:带有类型,方法名,和本地化绑定名;  
- `entering`方法:记录方法进入的消息;  
- `exiting`方法:记录方法退出的消息;  
- `throwing`方法:记录方法抛出的异常对象;  
- 指定7个级别的简单消息记录;  

___`log`方法,不带有类名`className`和方法名`methodName`:___  
```java
public void log(java.util.logging.LogRecord);
public void log(java.util.logging.Level, java.lang.String);
public void log(java.util.logging.Level, java.util.function.Supplier<java.lang.String>);
public void log(java.util.logging.Level, java.lang.String, java.lang.Object);
public void log(java.util.logging.Level, java.lang.String, java.lang.Object[]);
public void log(java.util.logging.Level, java.lang.String, java.lang.Throwable);
public void log(java.util.logging.Level, java.lang.Throwable, java.util.function.Supplier<java.lang.String>);
```  
___`logp`方法,带有类名`clasName`+方法名`methodName`:___  
```java
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String);
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.util.function.Supplier<java.lang.String>);
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Object);
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Object[]);
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.Throwable);
public void logp(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.Throwable, java.util.function.Supplier<java.lang.String>);
```  
___`logrb`方法,带有类名`className`+方法名`methodName`+本地化绑定名`bundleName`:___  
```java
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String);
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object);
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Object[]);
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.util.ResourceBundle, java.lang.String, java.lang.Object...);
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Throwable);
public void logrb(java.util.logging.Level, java.lang.String, java.lang.String, java.util.ResourceBundle, java.lang.String, java.lang.Throwable);
```  
___`entering`方法,记录方法进入`entry`的日志.(内部调用`logp`方法,并强制消息`msg`参数值为`ENTRY`):___  
```java
public void entering(java.lang.String, java.lang.String);
public void entering(java.lang.String, java.lang.String, java.lang.Object);
public void entering(java.lang.String, java.lang.String, java.lang.Object[]);
```  
___`exiting`方法,记录方法退出`return`的日志.(内部调用`logp`方法,并强制消息`msg`参数值为`RETURN`):___  
```java
public void exiting(java.lang.String, java.lang.String);
public void exiting(java.lang.String, java.lang.String, java.lang.Object);
```  
___`throwing`方法,记录指定异常对象的日志.(内部调用私有方法`doLog`):___  
```java
public void throwing(java.lang.String, java.lang.String, java.lang.Throwable);
```  
___指定日志级别的简单消息记录,具体查看`java.util.logging.Level`类:___  
```java
public void severe(java.lang.String);
public void warning(java.lang.String);
public void info(java.lang.String);
public void config(java.lang.String);
public void fine(java.lang.String);
public void finer(java.lang.String);
public void finest(java.lang.String);
public void severe(java.util.function.Supplier<java.lang.String>);
public void warning(java.util.function.Supplier<java.lang.String>);
public void info(java.util.function.Supplier<java.lang.String>);
public void config(java.util.function.Supplier<java.lang.String>);
public void fine(java.util.function.Supplier<java.lang.String>);
public void finer(java.util.function.Supplier<java.lang.String>);
public void finest(java.util.function.Supplier<java.lang.String>);
```  

---

#### 访问器相关的方法

```java
public static final java.util.logging.Logger getGlobal();
public static java.util.logging.Logger getLogger(java.lang.String);
public static java.util.logging.Logger getLogger(java.lang.String, java.lang.String);
public static java.util.logging.Logger getAnonymousLogger();
public static java.util.logging.Logger getAnonymousLogger(java.lang.String);
public java.util.ResourceBundle getResourceBundle();
public java.lang.String getResourceBundleName();
public java.util.logging.Filter getFilter();
public java.util.logging.Level getLevel();
public java.lang.String getName();
public java.util.logging.Handler[] getHandlers();
public boolean getUseParentHandlers();
public java.util.logging.Logger getParent();
```  

---

#### 修改器相关的方法

```java
public void setFilter(java.util.logging.Filter) throws java.lang.SecurityException;
public void setLevel(java.util.logging.Level) throws java.lang.SecurityException;
public void setUseParentHandlers(boolean);
public void setResourceBundle(java.util.ResourceBundle);
public void setParent(java.util.logging.Logger);
```  

---

#### 其他方法

```java
public boolean isLoggable(java.util.logging.Level);
public void addHandler(java.util.logging.Handler) throws java.lang.SecurityException;
public void removeHandler(java.util.logging.Handler) throws java.lang.SecurityException;
```  

---

#### 包含的内部类

```java
java.util.logging.Logger$LoggerBundle
java.util.logging.Logger$SystemLoggerHelper
```  
