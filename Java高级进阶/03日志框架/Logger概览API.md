---

#### 记录日志的方法:  
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
___`entering`方法,记录方法进入`entry`是的日志.(内部调用`logp`方法,并强制消息`msg`参数值为`ENTRY`):___  
```java
public void entering(java.lang.String, java.lang.String);
public void entering(java.lang.String, java.lang.String, java.lang.Object);
public void entering(java.lang.String, java.lang.String, java.lang.Object[]);
```  
