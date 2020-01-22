`java.util.logging.Formatter`:对于日志输出的文本进行格式化.  
- 该类是抽象类,内置了两个比较常用的实现类`SimpleFormatter`和`XmlFormatter`;  
- 涉及到正则表达式的部分,以及参考`java.util.Formatter`工具类;  

对于`java.util.logging.SimpleFormatter`:
- 默认的格式化配置是:`sun.util.logging.LoggingSupport.DEFAULT_FORMAT`字段,有必要查看相关的调用代码;  
- 关于字符所代表的含义,和`java.util.Formatter`中的DOC规范相同;  
- 简单的样例查看方法的DOC说明:`java.util.logging.SimpleFormatter.format(LogRecord)`  


对于`java.util.loging.XmlFormatter`:  
- xml文档的格式,在该类的对应方法中,都有设置;  
- 若有扩展xml的格式化日志文件,扩展该类,或者扩展`Formatter`的同时参考该类即可;  
