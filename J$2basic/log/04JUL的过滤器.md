`java.util.logging.Filter`:在JUL框架中,定义的是一个接口.  
而且没有内置实现类,若要使用日志的过滤功能,就需要自己手动的实现.  
从接口中的方法,可以看出,主要过滤的对象是`LogRecord`.  
```java
@FunctionalInterface
public interface Filter {

    /**
     * Check if a given log record should be published.
     * @param record  a LogRecord
     * @return true if the log record should be published.
     */
    public boolean isLoggable(LogRecord record);
}
```  
该接口是一个函数式接口,若是硬编码的方式配置,可以使用`lambda`表达式;若是在配置文件中配置过滤器,应当规矩的创建该接口的实现类.  

---

以更改配置文件演示日志过滤器的功效:  
(1)创建`Filter`的实现类:  
```java
package ocn.axy.run;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
public class FilterImpl implements Filter {
	@Override
	public boolean isLoggable(LogRecord record) {
		String message = record.getMessage();
		String key = "java";
		return message.contains(key) ? true : false;
	}
}
```  
(2)更改配置文件`$java.home/lib/logging.properties`:  
```java
# Limit the message that are printed on the console to INFO and above.
java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.filter = ocn.axy.run.FilterImpl
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
```  
(3)模拟测试:  
```java
package ocn.axy.run;
import java.util.logging.Logger;
public class App {
	private final static Logger logger = Logger.getLogger(App.class.getPackage().getName());
	public static void main(String[] args) {
		logger.info("abc");
		logger.info("java test logging");
		logger.info("jav  no word");
	}
}
```  
(4)验证:  
```java
十二月 18, 2019 1:51:07 下午 ocn.axy.run.App main
信息: java test logging
```  
