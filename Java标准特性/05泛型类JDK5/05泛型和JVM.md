泛型机制和JVM之间存在以下三种现象:  
1. 类型擦除(编译器负责)  
1. 强制类型转换(存在泛型表达式中)
1. 桥方法(解决和多态的冲突),有时也叫做协变返回类型.  

---

类型擦除:  
- 在虚拟机的世界里是没有泛型类的.  
- 无论何时,编译器都会给泛型类型提供一个原始类型(`raw-type`)(手动或之默认为`Object`),然后将类型变量`T`替换为原始类型.这一过程就是类型擦除.  

类型擦除的准则:  
1. 将类型变量擦除为原始类型;  
1. 若无具体的限定类型,则擦除为`Object`类型;  
1. 若有限定类型,则擦除为限定列表中的第一个原始类型.  
1. 若擦除后的原始类型在运行的时候不满足要求,编译器会在必要的时候根据限定列表中的候选类型进行强制类型转换,但这会影响效率.  
1. 若限定列表中存在标签接口,应当置于限定列表的末尾.  

验证未限定类型变量时的类型擦除.  
__泛型类源码__:  
```java
class Generic<T> {  
    private T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }
    public T getT() { return this.t; }
    // @formatter:on  
}  
```  
__擦除后的代码__:  
```java
class Generic {  
    private Object t;  
    // @formatter:off  
    public void setT(Object t) { this.t=t; }  
    public Object getT() { return this.t; }  
    // @formatter:on  
}  
```  
__实际测试代码__:  
```java
package ocn.axy.run;  
class Generic<T> {  
    private T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }  
    public T getT() { return this.t; }  
    // @formatter:on  
}  
public class App {  
    public static void main(String[] args) {
        Generic<String> g = new Generic<>();
        g.setT("erased-type");  
        @SuppressWarnings("unused")  
        String t = g.getT();  
    }  
}  
```  
__反汇编验证__:  
```java
Compiled from "App.java"  
public class ocn.axy.run.App {  
  public ocn.axy.run.App();  
    Code:  
       0: aload_0  
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V  
       4: return  

  public static void main(java.lang.String[]);  
    Code:  
       0: new           #16                 // class ocn/axy/run/Generic  
       3: dup  
       4: invokespecial #18                 // Method ocn/axy/run/Generic."<init>":()V  
       7: astore_1  
       8: aload_1  
       9: ldc           #19                 // String erased-type  
      11: invokevirtual #21                 // Method ocn/axy/run/Generic.setT:(Ljava/lang/Object;)V  
      14: aload_1  
      15: invokevirtual #25                 // Method ocn/axy/run/Generic.getT:()Ljava/lang/Object;  
      18: checkcast     #29                 // class java/lang/String  
      21: astore_2  
      22: return  
}  
```  

验证存在类型变量限定时的类型擦除.  
__泛型类源码__:  
```java
class Sub {}  
class Generic<T extends Sub> {  
    private T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }
    public T getT() { return this.t; }  
    // @formatter:on  
}  
```  
__擦除后的代码__:  
```java
class Sub {}  
class Generic {  
    private Sub t;  
    // @formatter:off  
    public void setT(Sub t) { this.t=t; }  
    public Sub getT() { return this.t; }  
    // @formatter:on  
}  
```  
__实际测试代码__:  
```java
package ocn.axy.run;  
class Sub {}  
class Generic<T extends Sub> {  
    private T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }  
    public T getT() { return this.t; }  
    // @formatter:on  
}  
public class App {  
    public static void main(String[] args) {  
        Generic<Sub> g = new Generic<>();  
        g.getT();  
        g.setT(new Sub());  
    }  
}  
```  
__反汇编验证__:  
```java
Compiled from "App.java"  
public class ocn.axy.run.App {  
  public ocn.axy.run.App();  
    Code:  
       0: aload_0  
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V  
       4: return  

  public static void main(java.lang.String[]);  
    Code:  
       0: new           #16                 // class ocn/axy/run/Generic  
       3: dup  
       4: invokespecial #18                 // Method ocn/axy/run/Generic."<init>":()V  
       7: astore_1  
       8: aload_1  
       9: invokevirtual #19                 // Method ocn/axy/run/Generic.getT:()Locn/axy/run/Sub;  
      12: pop  
      13: aload_1  
      14: new           #23                 // class ocn/axy/run/Sub  
      17: dup  
      18: invokespecial #25                 // Method ocn/axy/run/Sub."<init>":()V  
      21: invokevirtual #26                 // Method ocn/axy/run/Generic.setT:(Locn/axy/run/Sub;)V  
      24: return  
}  
```  
关于必要的强制类型转换,暂时未找到对应的测试场景,仅限制于理论,待定...  

---

调用泛型的表达式中,存在必要的强制类型转换:  
- 当调用泛型方法时,编译器会在必要的时候插入强制类型转换的指令,这是在编译期发生的.  
- 当前执行的泛型表达式,返回的是泛型方法中的返回类型,而且该类型被擦除为原始类型.  
- 这种情况出现的地方,比较典型的是调用泛型类的`getter`方法,或之直接访问泛型类的全局属性(若开放权限的话).  

__实际代码__:  
```java
package ocn.axy.run;  
class Sub {}  
class Generic<T> {  
    public T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }  
    public T getT() { return this.t; }  
    // @formatter:on  
}  
public class App {  
    @SuppressWarnings("unused")  
    public static void main(String[] args) {  
        Generic<Sub> g = new Generic<>();  
        Sub t = g.getT();  
        Sub t2 = g.t;  
    }  
}  
```  
__反汇编验证__:  
```java
Compiled from "App.java"  
public class ocn.axy.run.App {  
  public ocn.axy.run.App();  
    Code:  
       0: aload_0  
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V  
       4: return  

  public static void main(java.lang.String[]);  
    Code:  
       0: new           #16                 // class ocn/axy/run/Generic  
       3: dup  
       4: invokespecial #18                 // Method ocn/axy/run/Generic."<init>":()V  
       7: astore_1  
       8: aload_1  
       9: invokevirtual #19                 // Method ocn/axy/run/Generic.getT:()Ljava/lang/Object;  
      12: checkcast     #23                 // class ocn/axy/run/Sub  
      15: astore_2  
      16: aload_1  
      17: getfield      #25                 // Field ocn/axy/run/Generic.t:Ljava/lang/Object;  
      20: checkcast     #23                 // class ocn/axy/run/Sub  
      23: astore_3  
      24: return  
}  
```  

---

桥方法:  
- 类型擦除的现象,在泛型类继承重写父类方法的时候,会造成两个问题,一是和多态的冲突,一是不合法的重复方法.  
- 前者用桥方法`bridge-method`解决,后者用虚拟机区分方法的机制解决(JVM和编译器区分方法的原则不同).  

在涉及到泛型类的继承关系中,类型擦除和多态会发生冲突.  
- 即重写方法造成的冲突!  
- 编译器用方法签名(方法名称+参数列表)来区分不同的方法.  
- 重写泛型父类的方法会导致两个问题.仅限概念层面:  
  - 一是重写导致两个合法的`override`方法,即泛型父类的方法`fun(raw-type)`,和泛型子类的方法`fun(limit-type)`.  
  - 二是重写导致两个不合法的`overload`方法,即泛型父类的方法`fun()`,和泛型子类的方法`fun()`.(以及不同返回类型情况)  
  - 前者违背Java的多态规范,后者违背类方法的重载规范.  

编译器为了解决类型擦除和多态的冲突,使用了桥方法(bridge-method)的策略.  
- 子类若重写父类的方法,则子类的内部持有两个同名但不同参数列表的方法.前者是泛型父类的方法`fun_0`,后者是子类的方法`fun_1`.  
- 注意`fun_1`重写了`fun_0`,在多态机制中,这应该是同一个方法.但是因类型擦除的机制,这是两个不同的方法,因为参数列表不同!  
- 编译器为了解决这个问题,在`fun_0`的内部生成了一个桥方法`bridge-method`,即内部调用`fun_1`.  
- 这样既支持了泛型机制的类型擦除,又支持了Java的多态特性.  
- 另外桥方法`bridge-method`在类方法的重载机制中也有使用.(参见类方法重载机制部分)  

虚拟机区别方法的标准:  
在虚拟机的世界里,用参数类型和返回类型来区分方法,这就解决了第二个问题.  

__泛型类源码__:  
```java
class Generic<T> {  
    public T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }  
    public T getT() { return this.t; }  
    // @formatter:on  
}  
class GenericImpl extends Generic<Sub> {  
    @Override  
    public void setT(Sub t) {  
        super.t = t;  
        System.out.println("generic-impl");  
    }  
    @Override  
    public Sub getT() {  
        System.out.println("generic-impl");  
        return super.getT();  
    }  
}  
```  
__类型擦除概览(关注setter和getter方法)__:  
```java
Compiled from "App.java"  
class ocn.axy.run.GenericImpl extends ocn.axy.run.Generic<ocn.axy.run.Sub> {  
  ocn.axy.run.GenericImpl();  
    Code:  
       0: aload_0  
       1: invokespecial #8                  // Method ocn/axy/run/Generic."<init>":()V  
       4: return  

  public void setT(ocn.axy.run.Sub);  
    Code:  
       0: aload_0  
       1: aload_1  
       2: putfield      #16                 // Field ocn/axy/run/Generic.t:Ljava/lang/Object;  
       5: getstatic     #20                 // Field java/lang/System.out:Ljava/io/PrintStream;  
       8: ldc           #26                 // String generic-impl  
      10: invokevirtual #28                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V  
      13: return  

  public ocn.axy.run.Sub getT();  在虚拟机眼里是同一方法,且合法
    Code:  
       0: getstatic     #20                 // Field java/lang/System.out:Ljava/io/PrintStream;  
       3: ldc           #26                 // String generic-impl  
       5: invokevirtual #28                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V  
       8: aload_0  
       9: invokespecial #37                 // Method ocn/axy/run/Generic.getT:()Ljava/lang/Object;  
       12: checkcast     #40                 // class ocn/axy/run/Sub  
       15: areturn  

   public java.lang.Object getT();  在虚拟机眼里是同一方法,且合法
     Code:  
        0: aload_0  
        1: invokevirtual #42                 // Method getT:()Locn/axy/run/Sub;  
        4: areturn  

   public void setT(java.lang.Object);  
     Code:  
        0: aload_0  
        1: aload_1  
        2: checkcast     #40                 // class ocn/axy/run/Sub  必要的强制类型转换
        5: invokevirtual #45                 // Method setT:(Locn/axy/run/Sub;)V  桥方法,内部调用重写方法,解决多态冲突
        8: return  
 }  
```  
__实际代码__:  
```java
package ocn.axy.run;  
class Sup {}  
class Sub extends Sup {}  
class Generic<T> {  
    public T t;  
    // @formatter:off  
    public void setT(T t) { this.t=t; }  
    public T getT() { return this.t; }  
    // @formatter:on  
}  
class GenericImpl extends Generic<Sub> {  
    @Override  
    public void setT(Sub t) {  
        super.t = t;  
        System.out.println("generic-impl");  
    }  
    @Override  
    public Sub getT() {  
        System.out.println("generic-impl");  
        return super.getT();  
    }  
}  
public class App {  
    @SuppressWarnings("unused")  
    public static void main(String[] args) {  
        GenericImpl genericImpl = new GenericImpl();  
        Generic<Sub> generic = genericImpl;  
        generic.setT(new Sub());// 桥方法，解决类型擦出和多态的冲突  
        Sub t = generic.getT();// 虚拟机本身解决冲突不合法的重复方法  
         // console: generic-impl X2
    }  
}  
```  
__反汇编验证__:  
```java
Compiled from "App.java"  
public class ocn.axy.run.App {  
  public ocn.axy.run.App();  
    Code:  
       0: aload_0  
       1: invokespecial #8                  // Method java/lang/Object."<init>":()V  
       4: return  

  public static void main(java.lang.String[]);  
    Code:  
       0: new           #16                 // class ocn/axy/run/GenericImpl  
       3: dup  
       4: invokespecial #18                 // Method ocn/axy/run/GenericImpl."<init>":()V  
       7: astore_1  
       8: aload_1  
       9: astore_2  
      10: aload_2  
      11: new           #19                 // class ocn/axy/run/Sub  
      14: dup  
      15: invokespecial #21                 // Method ocn/axy/run/Sub."<init>":()V  
      18: invokevirtual #22                 // Method ocn/axy/run/Generic.setT:(Ljava/lang/Object;)V  满足类型擦除和Java多态性
      21: aload_2  
      22: invokevirtual #28                 // Method ocn/axy/run/Generic.getT:()Ljava/lang/Object;  满足类型擦除和Java多态性
      25: checkcast     #19                 // class ocn/axy/run/Sub  
      28: astore_3  
      29: return  
}  
```  

---

关于泛型和遗留代码之间的处理.  
泛型机制是JDK5推出的,若调用到遗留代码,可使用注解来压制安全警告.  
即注解`@SuppressWarnings("unchecked")`.  
以下截取的是遗留类`JSlider`中和泛型冲突的代码:  
```java
public void setLabelTable( Dictionary labels ) {  
    Dictionary oldTable = labelTable;  
    labelTable = labels;  
    updateLabelUIs();  
    firePropertyChange("labelTable", oldTable, labelTable );  
    if (labels != oldTable) {  
        revalidate();  
        repaint();  
    }  
}  
```  
