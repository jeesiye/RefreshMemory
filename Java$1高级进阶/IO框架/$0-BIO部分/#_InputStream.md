#### 规范:  
`java.io.InputStream`:基于字节的读取流的基类.  
- 基本的`读取字节`和`关闭流`的操作,为`read`和`close`方法;  
- 流操作的`标记`和`回退`的操作,为`mark`和`reset`方法;  
- 基本流属性的查询操作,`可读字节数量`和`是否支持标记`查询,为`available`和`markSupport`方法;  
- 基于字节的`跳过`读取的支持,为`skip`方法;  
- 该抽象类中的行为只是一种规范,即使内置具体方法,但大多数的实现类不会采用;  

---  
#### 解析(部分):  
阅读无意义,依赖具体子类来实现.  

---  
内置`int`全局属性的规范:  
`private static final int MAX_SKIP_BUFFER_SIZE = 2048;`  
- 规范在跳过字节功能时,最大的跳过字节数量(注意是缓冲的大小).  
- 8bits=1bytes;1024bits=1k;(其中指的是2048个字节);  
- 仅作参考;  

---  
将字节读取存储到指定字节数组的具体方法:  
`read(byte[], int, int)`方法:  
```java
// 概述:
// (1)对接收的三个参数进行合法性的校验
// (2)循环调用本地重载的read方法,以是否返回-1作为结束的阀值;  
// (3)返回的值,不一定是len,但会小于等于len.
// 参数的概述:
// b[] 接收读取字节的容器
// off 存储到b[]中的开始下标
// len 计划读取多少个字节,若没有可读取的字节,读取的字节长度就是返回值
// 返回值概述:
// 返回的值是内部定义的本地变量i.表示成功读取多少个字节.也可能返回-1.
public int read(byte b[], int off, int len) throws IOException {
    // step1:合法性校验
    // 1.1 判断byte数组参数b是否为null,若是抛出空指针异常.
    if (b == null) {
        throw new NullPointerException();
    // 1.2 判断off,len参数不能为负数,以及要读取的数组不能大于可读取的数组长度,
    // 否则抛出下标越界异常.  
    } else if (off < 0 || len < 0 || len > b.length - off) {
        throw new IndexOutOfBoundsException();
    // 1.3 检查len的参数是否为0,若是直接返回0.
    // 此处的0表示的是读取的字节数量,即表示没有读取字节.  
    } else if (len == 0) {
        return 0;
    }

    // step2:校验通过后,开始按照要求读取字节数组.
    // 2.1 先读取一个字节,赋值给本地int变量c.
    //    判断c是否为-1,若是直接返回-1,表示当前流中没有可读取的字节.
    int c = read();
    if (c == -1) {
        return -1;
    }
    // 2.2 若读取字节c不是负数,则将其赋值给b[off]  
    //    接收的参数b[]表示接收读取字节的容器;
    //    接收的off,表示接收的开始下标,len表示要读取的长度
    b[off] = (byte)c;
    // 2.3 定义本地变量,初始为1,表示读取的字节数量.
    int i = 1;
    try {
        // 开始循环读取,阀值是参数len,读取成功一次,i+1
        for (; i < len ; i++) {
            c = read();
            // 若读取失败,结束循环
            if (c == -1) {
                break;
            }
            // 读取成功,存储读取的字节
            b[off + i] = (byte)c;
        }
    } catch (IOException ee) {
    }
    // 2.4 返回读取本地变量i
    return i;
}
```  
```java
// 内部调用了重载的方法read(byte b[],int off,int len)
// 不过传递的参数off是0值.
public int read(byte b[]) throws IOException {
    return read(b, 0, b.length);
}
```  

---  
反复读取的`skip`方法(参doc):  
```java
// 具体功能的实现细节,应参具体的实现类,而非该基类,无实际意义.  
// 该方法把流中的可读字节都会读取完毕,若接收参数足够大,会直至返回-1;  
public long skip(long n) throws IOException {
    // remaining:剩下的
    long remaining = n;
    int nr;
    // 如果参数是非整数和0,直接返回0.
    if (n <= 0) {
        return 0;
    }
    // 当前层面一次读取的最大字节数量.
    // 参考规范中的内置属性 MAX_SKIP_BUFFER_SIZE  
    int size = (int)Math.min(MAX_SKIP_BUFFER_SIZE, remaining);
    // 读取字节转储到字节数组操作
    byte[] skipBuffer = new byte[size];
    // 当接收的参数小于2048的时候,只会执行一次
    // 当接收的参数大于2048的时候,会执行多次,直至nr<0(初次读取的大小是2048,往后读取的大小是n-remaining)  
    while (remaining > 0) {
        nr = read(skipBuffer, 0, (int)Math.min(size, remaining));
        if (nr < 0) {
            break;
        }
        remaining -= nr;
    }
    // 返回的是真实读取的字节的数量  
    return n - remaining;
}
```  

---  
#### 结构:  
```java
public abstract class java.io.InputStream implements java.io.Closeable {
  public java.io.InputStream();
  public abstract int read() throws java.io.IOException;
  public int read(byte[]) throws java.io.IOException;
  public int read(byte[], int, int) throws java.io.IOException;
  public long skip(long) throws java.io.IOException;
  public int available() throws java.io.IOException;
  public void close() throws java.io.IOException;
  public synchronized void mark(int);
  public synchronized void reset() throws java.io.IOException;
  public boolean markSupported();
}
```  
