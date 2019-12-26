`java.io.StreamTokenizer`:带有标记功能的输入流;  
- 当前,创建方式推荐使用字符流的实例创建的;  
- 主要特色的功能,是把编码表中同类型的编码字符作为一个进行标记;
- 当调用`nextToken`函数的时候,就可以把这类的字符存储到实例变量`sval`或者`nval`中;  
- 当然,功能不是很全面,只能标记存储单词和数字;  
- 提供了自定义语法表的功能;  

_常识补充:_  
- 词汇补充:  
  - `digit`:数字;  
  - `alpha`:字母(希腊字母中的第一个字母`α`);  
  - `quote`:引用;  
  - `comment`:注释;  
  - `whitespace`:空格;  
- 标记:  
  - `EOF`:`end of file`,文件的末尾;  
  - `EOL`:`end of line`,行为末尾;  
- 回车和换行:  
  - `CR`:`carriage return`,`\r`,回车符;  
  - `LF`:`line feed`,`\n`,换行符;  
  - unix是`LF`;maxos是`CR`;windows是`CRLF`;  

---  
简单的使用流标记,仅仅了解,真实使用到可能需要扩展的;  
_文件:_  
```
abc
1.2333  23
abc
3.0
3
```  
_代码:_  
```java
try (FileReader fr = new FileReader("files");) {
	StreamTokenizer st = new StreamTokenizer(fr);
	while (true) {
		st.nextToken();
		if (st.ttype == StreamTokenizer.TT_WORD)
			System.out.println(st.sval);
		if (st.ttype == StreamTokenizer.TT_NUMBER)
			System.out.println(st.nval);
		if (st.ttype == StreamTokenizer.TT_EOF)
			return;
	}
} catch (Exception e) {
}
```  
_输出:_  
```
abc
1.2333
23.0
abc
3.0
3.0
```  
