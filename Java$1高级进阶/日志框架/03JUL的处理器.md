`java.util.logging.Handler`,日志处理器,主要作用是对于接手到的日志消息,采取如何处理的策略.  
- 内部依赖(`has-a`)了两个类:  
  - `java.util.logging.Filter`:对日志记录进行过滤甄别;  
  - `java.util.logging.Formatter`:对日志的输出格式进行排版;  
- 内置了两个比较常用的实现类:  
  - 一个是将日志消息转储到文件中的处理器`java.util.logging.FileHandler`;  
  - 一个是将日志消息转储到控制台的处理器`java.util.logging.ConsoleHandler`;  
- 对于处理器的实现类,重要的一点,是通过读取配置文件来配置选项的.  

---

`ConsoleHandler`在配置文件中支持的选项(JDK8环境,在JDK13上删除了)  
```java
// java.util.logging.ConsoleHandler.configure()
private void configure() {                                                                              
    LogManager manager = LogManager.getLogManager();                                                    
    // 以当前类的完全限定名作为属性配置的前缀,即:java.util.logging.ConsoleHandler
    String cname = getClass().getName();                                                                

    setLevel(manager.getLevelProperty(cname +".level", Level.INFO));                                    
    setFilter(manager.getFilterProperty(cname +".filter", null));                                       
    setFormatter(manager.getFormatterProperty(cname +".formatter", new SimpleFormatter()));             
    try {                                                                                               
        setEncoding(manager.getStringProperty(cname +".encoding", null));                               
    } catch (Exception ex) {                                                                            
        try {                                                                                           
            setEncoding(null);                                                                          
        } catch (Exception ex2) {                                                                       
            // doing a setEncoding with null should always work.                                        
            // assert false;                                                                            
        }                                                                                               
    }                                                                                                   
}                                                                                                       
```  
由上可得出以下总结(在配置文件中的配置`key`值的含义):  
- `java.util.logging.ConsoleHandler.level`:设定日志的级别,默认值是`INFO`;  
- `java.util.logging.ConsoleHandler.filter`:设定记录日志的过滤器类,默认不设置;  
- `java.util.logging.ConsoleHandler.formatter`:设定记录日志的格式化类,默认是`SimpleFormatter`;  
- `java.util.logging.ConsoleHandler.encoding`:设定记录日志的字符编码,默认是`null`;  

---

`FileHandler`在配置文件中支持的选项:  
```java
// java.util.logging.FileHandler.configure()
private void configure() {                                                                             
    LogManager manager = LogManager.getLogManager();                                                   
    // 以当前类的完全限定名作为属性配置的前缀
    String cname = getClass().getName();                                                               

    pattern = manager.getStringProperty(cname + ".pattern", "%h/java%u.log");                          
    limit = manager.getIntProperty(cname + ".limit", 0);                                               
    if (limit < 0) {                                                                                   
        limit = 0;                                                                                     
    }                                                                                                  
    count = manager.getIntProperty(cname + ".count", 1);                                               
    if (count <= 0) {                                                                                  
        count = 1;                                                                                     
    }                                                                                                  
    append = manager.getBooleanProperty(cname + ".append", false);                                     
    setLevel(manager.getLevelProperty(cname + ".level", Level.ALL));                                   
    setFilter(manager.getFilterProperty(cname + ".filter", null));                                     
    setFormatter(manager.getFormatterProperty(cname + ".formatter", new XMLFormatter()));              
    try {                                                                                              
        setEncoding(manager.getStringProperty(cname +".encoding", null));                              
    } catch (Exception ex) {                                                                           
        try {                                                                                          
            setEncoding(null);                                                                         
        } catch (Exception ex2) {                                                                      
            // doing a setEncoding with null should always work.                                       
            // assert false;                                                                           
        }                                                                                              
    }                                                                                                  
}                                                                                                      
```  
由上可得出以下总结(在配置文件中的配置`key`值的含义):  
- `java.util.logging.FileHandler.pattern`:日志文件命名模式,默认是`%h/java%u.log`;  
- `java.util.logging.FileHandler.limit`:日志文件存储的最大字节数,默认是0表示不限制;  
- `java.util.logging.FileHandler.count`:循环记录中的阀值表示日志的数目,默认是1表示不循环;  
- `java.util.logging.FileHandler.append`:是追加记录,还是每次启动新建日志文件,默认是`false`表示新建;  
- `java.util.logging.FileHandler.level`:日志记录的级别开关,默认是记录所有的日志;  
- `java.util.logging.FileHandler.filter`:日志记录的过滤器类,默认为`null`;  
- `java.util.logging.FileHandler.formatter`:日志记录的格式化类,默认是`SimpleFormatter`;  
- `java.util.logging.FileHandler.encoding`:日志记录的字符编码,默认是`null`;  
