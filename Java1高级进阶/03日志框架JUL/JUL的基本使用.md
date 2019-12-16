JUL的基本使用思路是:  
1. 获取日志记录器的实例,一般设置为全局的.即`java.util.logging.Logger`的实例.  
   注意,一般设置为静态的`static`,因为不使用时,有被GC回收的可能性.  
1. 在需要记录日志的地方,调用日志相关的记录方法.  
   级别可自行选择:`SERVER`,`WARNING`,`INFO`,`CONFIG`,`FINE`,`FINER`,`FINEST`;  

以下是简单的示例:  
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
