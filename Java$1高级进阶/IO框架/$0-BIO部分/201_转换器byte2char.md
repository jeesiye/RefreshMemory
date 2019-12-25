_&#8195;&#8195;字符流是不能够直接创建的,必须要借助指定的输入流实例来创建.仅仅在`java.io`包库的范围内,能够创建流实例的只有`FileInputStream`,`FileOutputStream`和`RandomAccessFile`这两种类型的类(最终调用了本地库的函数`fileOpen`).但是往往使用的是BIO库中的苦力,文件字节流来创建流实例的._  
