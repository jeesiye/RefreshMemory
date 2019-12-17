使用JUL:  
1. 使用JUL输出日志;  
1. JUL的本地化操作;

---

使用JUL记录日志(步骤简单分为两步):  
- 创建`java.util.logging.Logger`的实例,该引用声明为静态的,防止被GC回收.  
- 使用`Logger`的输出日志方法,至于记录的级别参考`$java.home/lib/logging.properties`配置文件,以及`java.util.logging.Level`常规类.  

```java
package ocn.axy.run;
import java.util.logging.Logger;
public class App {
	private final static Logger log = Logger.getLogger(App.class.getPackage().getName());
	public static void main(String[] args) {
		log.info("JUL info message");
	}
}
```  
```java
星期一 十二月 16 16:27:07 CST 2019 ocn.axy.run.App main
信息: JUL info message
```  

---

JUL的可以结合本地化操作.  
步骤如下:  
(1)配置资源化文件:  
目录树结构:  
```sh
ocn/axy/locale/
├── LocaleTag.java
├── logging_en.properties
└── logging_zh.properties

0 directories, 3 files
```  
英文配置文件:  
`locale=locale`  
中文配置文件:  
`locale=\u672C\u5730\u5316 {0} {1}`  
常规类`LocaleTag`是标记类,为了使用反射获取包名.  
(2)验证:  
```java
package ocn.axy.run;
import java.util.logging.Level;
import java.util.logging.Logger;
import ocn.axy.locale.LocaleTag;
public class App {
	private final static Logger logger = Logger.getLogger(App.class.getPackage().getName(),
			LocaleTag.class.getPackage().getName() + ".logging");
	public static void main(String[] args) {
		logger.info("locale");
		logger.log(Level.FINE, "locale", new Object[] { "abc", "bca" });
	}
}
```  
```java
星期二 十二月 17 15:04:46 CST 2019 ocn.axy.run.App main
信息: 本地化 {0} {1}
星期二 十二月 17 15:04:46 CST 2019 ocn.axy.run.App main
详细: 本地化 abc bca
```  
