#### 规范:  
`java.lang.Appendable`:JDK5  
- 可追加操作的标记;  
- 说白了就是追加的形式形式写入文件;  
- 处理的数值是字符序列或者是字符,编码是UTF16;  

---  
#### 解析(部分):  
无!

---  
#### 结构:  
```java
public interface java.lang.Appendable {
  public abstract java.lang.Appendable append(java.lang.CharSequence) throws java.io.IOException;
  public abstract java.lang.Appendable append(java.lang.CharSequence, int, int) throws java.io.IOException;
  public abstract java.lang.Appendable append(char) throws java.io.IOException;
}
```  
