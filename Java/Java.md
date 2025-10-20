# Java

### Java的简介

Java

- 简单：不使用指针，直接对象引用
- 面向对象：支持类之间的单继承，接口之间的多继承
- 分布式：Java.net提供了网络编程的类库
- 健壮：强类型机制，异常处理，垃圾自动回收
- 安全：一次编译，随处豫西南
- 多线程
- 动态：重载

Java代码编写

- 类名：首字母大写，后面单词首字母大写
- 方法名：所有方法名小写字母开头，后面单词首字母大写

---

#### Java包

包：分门别类的管理各种不同的技术，便于管理技术，扩展技术，阅读技术

定义包的格式：`package 包名`，必须放在类名的最上面

导包格式：`import 包名.类名`

相同包下的类可以直接访问；不同包下的类必须导包才可以使用

***

### Java的符号

#### Java的标识符

Java 所有的组成部分都需要的名字。类名、变量名以及方法名都被称为标识符

java 的标识符需注意以下内容：

- 所有标识符应该以字母（ A-Z 或者 a-z），美元符号（$）

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/c6f2d9aa-c643-4180-9a54-6fad60082646.png)

***

#### Java的修饰符

**权限修饰符**

权限修饰符：有四种**（private -> 缺省 -> protected - > public ）**
可以修饰成员变量，修饰方法，修饰构造器，内部类，不同修饰符修饰的成员能够被访问的权限将受到限制

| 四种修饰符访问权限 | private | 缺省 | protected | public |
| ------------------ | :-----: | :--: | :-------: | :----: |
| 本类中             |    √    |  √   |     √     |   √    |
| 本包下的子类中     |    X    |  √   |     √     |   √    |
| 本包下其他类中     |    X    |  √   |     √     |   √    |
| 其他包下的子类中   |    X    |  X   |     √     |   √    |
| 其他包下的其他类中 |    X    |  X   |     X     |   √    |

protected 用于修饰成员，表示在继承体系中成员对于子类可见

* 基类的 protected 成员是包内可见的，并且对子类可见
* 若子类与基类不在同一包中，那么子类实例可以访问其从基类继承而来的 protected 方法（重写），而不能访问基类实例的 protected 方法

**1、public** 

- 访问范围：任何类都可以访问
- 适用对象：类、接口、方法、变量
- 特点
  - 最高访问级别
  - 被public 修饰的类必须与文件名相同
  - 常用于定义 API 和对外公开的方法

**2、protected**

- 访问范围：
  - 同一个包内的所有类
  - 不同包的子类
- 适用对象：方法、变量（不能用于顶级类）
- 特点：
  - 主要用于继承类
  - 子类可以通过继承访问 protected 成员

**3、default** 

- 访问范围 ：同一个包内的类
- 适用对象：类、接口、方法、变量
- 特点：
  - 不使用任何修饰符即为default
  - 也称为 "包访问权限"
  - 常用于包内共享的实现细节

**4、private**

- 访问范围：仅限本类内部
- 适用对象：方法、变量（不能用于顶级类）
- 特点：
  - 最低访问级别
  - 实现封装的关键
  - 常用于隐藏细节

***

**非权限修饰符**

**1、static**

**基本介绍**

Java 是通过成员变量是否有 static 修饰来区分是类的还是属于对象的

按照有无 static 修饰，成员变量和方法可以分为：

* 成员变量：
  * 静态成员变量（类变量）：static 修饰的成员变量，属于类本身，**与类一起加载一次，只有一个**，直接用类名访问即可
  * 实例成员变量：无 static 修饰的成员变量，属于类的每个对象的，**与类的对象一起加载**，对象有多少个，实例成员变量就加载多少个，必须用类的对象来访问

* 成员方法：
  * 静态方法：有 static 修饰的成员方法称为静态方法也叫类方法，属于类本身的，直接用类名访问即可
  * 实例方法：无 static 修饰的成员方法称为实例方法，属于类的每个对象的，必须用类的对象来访问

**static 用法**

成员变量的访问语法：

* 静态成员变量：只有一份可以被类和类的对象**共享访问**
  * 类名.静态成员变量（同一个类中访问静态成员变量可以省略类名不写）
  * 对象.静态成员变量（不推荐）

* 实例成员变量：
  * 对象.实例成员变量（先创建对象）

成员方法的访问语法：

* 静态方法：有 static 修饰，属于类

  * 类名.静态方法（同一个类中访问静态成员可以省略类名不写）
  * 对象.静态方法（不推荐，参考 JVM → 运行机制 → 方法调用）

* 实例方法：无 static 修饰，属于对象

  * 对象.实例方法

  ```java
  public class Student {
      // 1.静态方法：有static修饰，属于类，直接用类名访问即可！
      public static void inAddr(){ }
      // 2.实例方法：无static修饰，属于对象，必须用对象访问！
      public void eat(){}
      
      public static void main(String[] args) {
          // a.类名.静态方法
          Student.inAddr();
          inAddr();
          // b.对象.实例方法
          // Student.eat(); // 报错了！
          Student sea = new Student();
          sea.eat();
      }
  }
  ```

**两个问题**

内存问题：

* 栈内存存放 main 方法和地址

* 堆内存存放对象和变量

* 方法区存放 class 和静态变量（jdk8 以后移入堆）

访问问题：

* 实例方法是否可以直接访问实例成员变量？可以，因为它们都属于对象
* 实例方法是否可以直接访问静态成员变量？可以，静态成员变量可以被共享访问
* 实例方法是否可以直接访问实例方法? 可以，实例方法和实例方法都属于对象
* 实例方法是否可以直接访问静态方法？可以，静态方法可以被共享访问
* 静态方法是否可以直接访问实例变量？ 不可以，实例变量**必须用对象访问**！！
* 静态方法是否可以直接访问静态变量？ 可以，静态成员变量可以被共享访问。
* 静态方法是否可以直接访问实例方法? 不可以，实例方法必须用对象访问！！
* 静态方法是否可以直接访问静态方法？可以，静态方法可以被共享访问！

- 静态方法不能访问非静态成员

---

**2、final**

**基本介绍**

final 用于修饰：类，方法，变量

* final 修饰类，类不能被继承了，类中的方法和变量可以使用
* final 可以修饰方法，方法就不能被重写
* final 修饰变量总规则：变量有且仅能被赋值一次

final 和 abstract 的关系是**互斥关系**，不能同时修饰类或者同时修饰方法

**修饰变量**

**静态变量**

final 修饰静态成员变量，变量变成了常量

常量：有 public static final 修饰，名称字母全部大写，多个单词用下划线连接

final 修饰静态成员变量可以在哪些地方赋值：

1. 定义的时候赋值一次

2. 可以在静态代码块中赋值一次

```java
public class FinalDemo {
	//常量：public static final修饰，名称字母全部大写，下划线连接。
    public static final String SCHOOL_NAME = "张三" ;
    public static final String SCHOOL_NAME1;

    static{
        //SCHOOL_NAME = "java";//报错
        SCHOOL_NAME1 = "张三1";
    }
}
```

**实例变量**

final 修饰变量的总规则：有且仅能被赋值一次

final 修饰实例成员变量可以在哪些地方赋值 1 次：

1. 定义的时候赋值一次
2. 可以在实例代码块中赋值一次
3. 可以在每个构造器中赋值一次

```java
public class FinalDemo {
    private final String name = "张三" ;
    private final String name1;
    private final String name2;
    {
        // 可以在实例代码块中赋值一次。
        name1 = "张三1";
    }
	//构造器赋值一次
    public FinalDemo(){
        name2 = "张三2";
    }
    public FinalDemo(String a){
        name2 = "张三2";
    }

    public static void main(String[] args) {
        FinalDemo f1 = new FinalDemo();
        //f1.name = "张三1"; // 第二次赋值 报错！
    }
}
```

---

**3、abstract**

- 作用：标识不完整的实现
- 适用对象：类、方法
- 特点：
  - 抽象类不能实例化
  - 抽象方法没有实现体
  - 包含抽象方法的类必须是抽象类

**4、synchronize**

- 作用：实现线程同步
- 适用对象：方法、代码块
- 特点：
  - 同一时间只能有一个线程访问
  - 保证线程安全
  - 可能影响性能

**5、transient**

- 作用：标记不被序列化的字段
- 适用对象：变量

- 特点：
  - 用于对象序列化时，隐藏敏感数据字段

**6、volatile**

- 作用：保证多线程可见性
- 适用对象：变量
- 特点：
  - 直接从主内存读写，不缓存
  - 保证变量的修改对所有线程立即可见
  - 不保证原子性

***

#### Java运算符

- 基本运算符： + ，-， *，/，% , + 号也可以作为字符串连接符

- 自增自减： --  ，++
- 赋值运算符： = ，+=
- 关系运算符：>， >=， <， <=， !=， ==
- 三元运算符：？值1 ：值2

***

#### Java转义字符

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/a5b75e73-c227-459a-ab77-9f3e510dae02.png)

#### Java关键字

**instanceof：**判断左边的对象是否是右边的类的实例，或者是其直接或间接子类，或者是其接口的实现类

***

### Java的数据

#### 变量类型

|          |    成员变量    |       局部变量       |          静态变量           |
| :------: | :------------: | :------------------: | :-------------------------: |
| 定义位置 | 在类中，方法外 | 方法中或者方法的形参 |       在类中，方法外        |
| 初始化值 | 有默认初始化值 |  无，赋值后才能使用  |       有默认初始化值        |
| 调用方法 |    对象调用    |                      |     对象调用，类名调用      |
| 存储位置 |      堆中      |         栈中         | 方法区（JDK8 以后移到堆中） |
| 生命周期 |  与对象共存亡  |     与方法共存亡     |         与类共存亡          |
|   别名   |    实例变量    |                      |    类变量，静态成员变量     |

***

#### 基本类型

Java 语言提供了八种基本类型。六种数字类型（四个整数型，两个浮点型），一种字符类型，还有一种布尔型

**byte：**

- byte 数据类型是 8 位、有符号的，以二进制补码表示的整数，**8 位一个字节**，首位是符号位
- 最小值是 -128（-2^7）、最大值是 127（2^7-1）
- 默认值是 `0`
- byte 类型用在大型数组中节约空间，主要代替整数，byte 变量占用的空间只有 int 类型的四分之一
- 例子：`byte a = 100，byte b = -50`

**short：**

- short 数据类型是 16 位、有符号的以二进制补码表示的整数
- 最小值是 -32768（-2^15）、最大值是 32767（2^15 - 1）
- short 数据类型也可以像 byte 那样节省空间，一个 short 变量是 int 型变量所占空间的二分之一
- 默认值是 `0`
- 例子：`short s = 1000，short r = -20000`

**int：**

- int 数据类型是 32 位 4 字节、有符号的以二进制补码表示的整数
- 最小值是 -2,147,483,648（-2^31）、最大值是 2,147,483,647（2^31 - 1）
- 一般地整型变量默认为 int 类型
- 默认值是 `0`
- 例子：`int a = 100000, int b = -200000`

**long：**

- long 数据类型是 64 位 8 字节、有符号的以二进制补码表示的整数
- 最小值是 -9,223,372,036,854,775,808（-2^63）、最大值是 9,223,372,036,854,775,807（2^63 -1）
- 这种类型主要使用在需要比较大整数的系统上
- 默认值是 ` 0L`
- 例子： `long a = 100000L，Long b = -200000L`，L 理论上不分大小写，但是若写成 I 容易与数字 1 混淆，不容易分辩

**float：**

- float 数据类型是单精度、32 位、符合 IEEE 754 标准的浮点数
- float 在储存大型浮点数组的时候可节省内存空间
- 默认值是 `0.0f`
- 浮点数不能用来表示精确的值，如货币
- 例子：`float f1 = 234.5F`

**double：**

- double 数据类型是双精度、64 位、符合 IEEE 754 标准的浮点数
- 浮点数的默认类型为 double 类型
- double 类型同样不能表示精确的值，如货币
- 默认值是 `0.0d`
- 例子：`double d1 = 123.4`

**boolean：**

- boolean 数据类型表示一位的信息
- 只有两个取值：true 和 false
- JVM 规范指出 boolean 当做 int 处理，boolean 数组当做 byte 数组处理，这样可以得出 boolean 类型单独使用占了 4 个字节，在数组中是 1 个字节
- 默认值是 `false`
- 例子：`boolean one = true`

**char：**

- char 类型是一个单一的 16 位**两个字节**的 Unicode 字符
- 最小值是 `\u0000`（即为 0）
- 最大值是 `\uffff`（即为 65535）
- char 数据类型可以**存储任何字符**
- 例子：`char c = 'A'`，`char c = '张'`

***

**类型转换**

* 自动类型转换：

  ![截屏2024-08-30 11.40.39](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/3e01013e-f0e8-4f46-b678-3657e9d2b75f.png)

  表达式的最终结果类型由表达式中的最高类型决定

* 强制类型转换：

  Java 不能隐式执行**向下转型**，因为这会使得精度降低，但是可以向上转，小数强制类型转换为整数，丢掉小数部分，保留整数部分

***

#### 引用类型

引用数据类型：类，接口，数组都是引用数据类型，又叫包装类

包装类的作用：

* 包装类作为类首先拥有了 Object 类的方法
* 包装类作为引用类型的变量可以**存储 null 值**


```java
基本数据类型                包装类（引用数据类型）
byte                      Byte
short                     Short
int                       Integer
long                      Long

float                     Float
double                    Double
char                      Character
boolean                   Boolean
```

Java 为包装类做了一些特殊功能，具体来看特殊功能主要有：

* 可以把基本数据类型的值转换成字符串类型的值

  1. 调用 toString() 方法
  2. 调用 Integer.toString(基本数据类型的值) 得到字符串
  3. 直接把基本数据类型 + 空字符串就得到了字符串（推荐使用）

* 把字符串类型的数值转换成对应的基本数据类型的值（**重要**）

  1. Xxx.parseXxx("字符串类型的数值") → `Integer.parseInt(numStr)`
  2. Xxx.valueOf("字符串类型的数值")   → `Integer.valueOf(numStr)` （推荐使用）

  ```java
  public class PackageClass02 {
      public static void main(String[] args) {
          // 1.把基本数据类型的值转成字符串
          Integer it = 100 ;
          // a.调用toString()方法。
          String itStr = it.toString();
          System.out.println(itStr+1);//1001
          // b.调用Integer.toString(基本数据类型的值)得到字符串。
          String itStr1 = Integer.toString(it);
          System.out.println(itStr1+1);//1001
          // c.直接把基本数据类型+空字符串就得到了字符串。
          String itStr2 = it + "";
          System.out.println(itStr2+1);// 1001
  
          // 2.把字符串类型的数值转换成对应的基本数据类型的值
          String numStr = "23";
          int numInt = Integer.valueOf(numStr);
          System.out.println(numInt+1);//24
  
          String doubleStr = "99.9";
          double doubleDb = Double.valueOf(doubleStr);
          System.out.println(doubleDb+0.1);//100.0
      }
  }
  ```

***

**装箱拆箱**

**自动装箱**：可以直接把基本数据类型的值或者变量赋值给包装类

**自动拆箱**：可以把包装类的变量直接赋值给基本数据类型

```java
public class PackegeClass {
    public static void main(String[] args) {
        int a = 12 ;
        Integer a1 = 12 ;  // 自动装箱
        Integer a2 = a ;   // 自动装箱
        Integer a3 = null; // 引用数据类型的默认值可以为null

        Integer c = 100 ;
        int c1 = c ;      // 自动拆箱

        Integer it = Integer.valueOf(12);  	// 手工装箱！
        // Integer it1 = new Integer(12); 	// 手工装箱！
        Integer it2 = 12;

        Integer it3 = 111 ;
        int it33 = it3.intValue(); // 手工拆箱
    }
}
```

**自动装箱**反编译后底层调用 `Integer.valueOf()` 实现，源码：

```java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        // 【缓存池】，本质上是一个数组
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

自动拆箱调用 `java.lang.Integer#intValue`，源码：

```java
public int intValue() {
    return value;
}
```

***

#### 常量

常量是使用public static final修饰的成员变量， 有且只能赋值一次

- 作用：通常用于作为系统的配置信息，用作信息标识和分类

- 规范：名称全部大写

***

#### 数组类型

**初始化**

数组就是存储数据长度固定的容器，存储多个数据的数据类型要一致，**数组也是一个对象**

创建数组：

* 数据类型[] 数组名：`int[] arr`  （常用）
* 数据类型 数组名[]：`int arr[]`

静态初始化：

* 数据类型[] 数组名 = new 数据类型[]{元素1,元素2,...}：`int[] arr = new int[]{11,22,33}`
* 数据类型[] 数组名 = {元素1,元素2,...}：`int[] arr = {44,55,66}`

动态初始化

* 数据类型[] 数组名 = new 数据类型[数组长度]：`int[] arr = new int[3]`

***

**元素访问**

* **索引**：每一个存储到数组的元素，都会自动的拥有一个编号，从 **0** 开始。这个自动编号称为数组索引（index），可以通过数组的索引访问到数组中的元素

* **访问格式**：数组名[索引]，`arr[0]`
* **赋值：**`arr[0] = 10`

***

**内存分配**

内存是计算机中的重要器件，临时存储区域，作用是运行程序。编写的程序是存放在硬盘中，在硬盘中的程序是不会运行的，必须放进内存中才能运行，运行完毕后会清空内存，Java 虚拟机要运行程序，必须要对内存进行空间的分配和管理

| 区域名称   | 作用                                                       |
| ---------- | ---------------------------------------------------------- |
| 寄存器     | 给 CPU 使用                                                |
| 本地方法栈 | JVM 在使用操作系统功能的时候使用                           |
| 方法区     | 存储可以运行的 class 文件                                  |
| 堆内存     | 存储对象或者数组，new 来创建的，都存储在堆内存             |
| 方法栈     | 方法运行时使用的内存，比如 main 方法运行，进入方法栈中执行 |

内存分配图：**Java 数组分配在堆内存**

* 一个数组内存图

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/数组内存分配-一个数组内存图.png)

* 两个数组内存图

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/数组内存分配-两个数组内存图.png)

* 多个数组指向相同内存图

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/数组内存分配-多个数组指向一个数组内存图.png)

***

**数组异常**

* 索引越界异常：ArrayIndexOutOfBoundsException 

* 空指针异常：NullPointerException 

  ```java
  public class ArrayDemo {
      public static void main(String[] args) {
          int[] arr = new int[3];
          //把null赋值给数组
          arr = null;
          System.out.println(arr[0]);
      }
  }
  ```

  arr = null，表示变量 arr 将不再保存数组的内存地址，也就不允许再操作数组，因此运行的时候会抛出空指针异常。在开发中，空指针异常是不能出现的，一旦出现了，就必须要修改编写的代码

  解决方案：给数组一个真正的堆内存空间引用即可

***

**二维数组**

二维数组也是一种容器，不同于一维数组，该容器存储的都是一维数组容器

初始化：

* 动态初始化：数据类型[][] 变量名 = new 数据类型[m] [n]，`int[][] arr = new int[3][3]`

  * m 表示这个二维数组，可以存放多少个一维数组，行
  * n 表示每一个一维数组，可以存放多少个元素，列
* 静态初始化
  * 数据类型[][] 变量名 = new 数据类型 [][]{元素1, 元素2...} , {元素1, 元素2...} 
  * 数据类型[][] 变量名 = {元素1, 元素2...}, {元素1, 元素2...}...
  * `int[][] arr = {11,22,33}, {44,55,66}`

遍历：

```java
public class Test1 {
    /*
        步骤:
            1. 遍历二维数组，取出里面每一个一维数组
            2. 在遍历的过程中，对每一个一维数组继续完成遍历，获取内部存储的每一个元素
     */
    public static void main(String[] args) {
        int[][] arr = {11, 22, 33}, {33, 44, 55};
        // 1. 遍历二维数组，取出里面每一个一维数组
        for (int i = 0; i < arr.length; i++) {
            //System.out.println(arr[i]);
            // 2. 在遍历的过程中，对每一个一维数组继续完成遍历，获取内部存储的每一个元素
            //int[] temp = arr[i];
            for (int j = 0; j < arr[i].length; j++) {
                System.out.println(arr[i][j]);
            }
        }
    }
}
```

***

### Java接口

#### 基本特性

- 接口中的方法默认是 public  abstract的（Java 8 之前）
- 接口中变量默认是public static final
- 接口不能有构造方法，不能被实例化
- 一个类可以实现多个接口（解决 Java 单继承的限制）
- 接口可以继承多个接口

---

#### 函数式接口

**简介**

在Java中，函数式接口（Function Interface）是指仅包含一个抽象方法的接口（可以包含多个默认方法、静态方法，或从`Object`类继承的方法）。它的核心作用是作为 Lambda表达式和方法引用的 "载体"，是 Java8 引入函数式编程的基础

**核心特点**

**1、单一抽象方法：**接口中只有一个抽象方法（未被deafault 或 static修饰的方法）

**2、可被`@FunctionalInterface` 注解标识**：该注解用于显式声明接口为函数式接口，编译器会校验其是否符合"单一抽象"规则（非强制，但推荐使用，用于增强代码可读性）

**3、支持Lambda 表达式：**只有函数式接口可以接收 Lambda 表达式，因为 Lambda 本质是 "匿名函数"，需要通过接口的抽象方法来确定参数和返回值类型

**Java 标准库中的常用函数式接口（`java.util.function` 包）**

| 类型       | 接口                | 抽象方法            | 用途描述                   | 示例                                   |
| ---------- | ------------------- | ------------------- | -------------------------- | -------------------------------------- |
| 供给型     | `Supplier<T>`       | `T get()`           | 提供一个 `T` 类型的数据    | 生成随机数：`() -> Math.random()`      |
| 消费型     | `Consumer<T>`       | `void accept(T t)`  | 消费一个 `T` 类型的数据    | 打印数据：`s -> System.out.println(s)` |
| 函数型     | `Function<T, R>`    | `R apply(T t)`      | 将 `T` 类型转换为 `R` 类型 | 字符串转长度：`s -> s.length()`        |
| 断言型     | `Predicate<T>`      | `boolean test(T t)` | 判断 `T` 类型是否满足条件  | 检查正数：`n -> n > 0`                 |
| 二元函数型 | `BiFunction<T,U,R>` | `R apply(T t, U u)` | 接收两个参数，返回一个结果 | 两数相加：`(a,b) -> a + b`             |

**其他常见函数式接口（非 `java.util.function`）**

- `Runnable`：抽象方法 `void run()`（无参数无返回值），用于线程任务
- `Callable<V>`：抽象方法 `V call() throws Exception` （无参有返回值，可抛异常），用于有返回值的线程任务
- `Comparator<T>`：抽象方法 `int compare(T o1, T o2)` （比较两个对象），用于排序

---

#### Lambda

组成Lambda表达式的三要素：形式参数，箭头，代码块

**Lambda表达式格式：**

- 格式：（形式参数）--> {代码块}
- 形式参数：如果有多个参数，用逗号隔开；如果没有参数，留空即可
- ->：由英文中画线和大于符号组成
- 代码块：具体要做的事情

**Lambda表达式的省略模式**：

- 参数类型可以省略。如果有多个参数的情况下，不能只省略一个
- 如果参数有且只有一个，那么小括号可以省略
- 如果代码块的语句只有一条，可以忽略大括号和分号，甚至return 语句

**Lambda表达式的注意事项**

注意事项：

- 使用Lambda必须要有接口，并且要求接口有且仅有一个抽象方法
- 必须有上下文环境，才能推导出Lambda对应的接口
  - 根据局部变量的赋值得知Lambda对应的接口：Runnable r = （）-> System.out.println(”Lambda表达式“);
  - 根据调用方法的参数得知Lambda对应的接口：new Thread(()->System.out.println(“Lambda表达式”)).start();

**Lambda表达式和匿名内部类的区别**

所需类型不同：

- 匿名内部类：可以是接口，也可以是抽象类，还可以是具体类
- Lambda表达式：只能是接口

使用限制不同：

- 如果接口中有且仅有一个抽象方法，可以使用Lambda表达式，也可以使用匿名内部类
- 如果接口中多于一个抽象方法，只能使用匿名内部类，而不能使用Lambda表达式

实现原理不同：

- 匿名内部类：编译之后，产生一个单独的.class字节码文件
- Lambda表达式：编译之后，没有一个单独的.class字节码文件，对应的字节码会在运行的时候动态生成

### Java方法

#### 方法概述

方法（method）是将具有独立功能的代码块组织成为一个整体，使其具有特殊功能的代码集

注意：方法必须先创建才可以使用，该过程成为方法定义，方法创建后并不是直接可以运行的，需要手动使用后才执行，该过程成为方法调用

在方法内部定义的叫局部变量，局部变量不能加 static，包括 protected、private、public 这些也不能加

原因：局部变量是保存在栈中的，而静态变量保存于方法区（JDK8 在堆中），局部变量出了方法就被栈回收了，而静态变量不会，所以**在局部变量前不能加 static 关键字**，静态变量是定义在类中，又叫类变量

***

#### 定义调用

定义格式：

```java
public static 返回值类型 方法名(参数) {
	//方法体;
	return 数据 ;
}
```

调用格式：

```java
数据类型 变量名 = 方法名 (参数) ;
```

* 方法名：调用方法时候使用的标识
* 参数：由数据类型和变量名组成，多个参数之间用逗号隔开
* 方法体：完成功能的代码块
* return：如果方法操作完毕，有数据返回，用于把数据返回给调用者

如果方法操作完毕

* void 类型的方法，直接调用即可，而且方法体中一般不写 return
* 非 void 类型的方法，推荐用变量接收调用

原理：每个方法在被调用执行的时候，都会进入栈内存，并且拥有自己独立的内存空间，方法内部代码调用完毕之后，会从栈内存中弹栈消失

***

#### 注意事项

* 方法不能嵌套定义

  ```java
  public class MethodDemo {
  	public static void main(String[] args) {
  	}
  	public static void methodOne() {
  		public static void methodTwo() {
  			// 这里会引发编译错误!!!
  		}
  	}
  }
  ```

* void 表示无返回值，可以省略 return，也可以单独的书写 return，后面不加数据

  ```java
  public static void methodTwo() {
  	//return 100; 编译错误，因为没有具体返回值类型
  	return;
  	//System.out.println(100); return语句后面不能跟数据或代码
  }
  ```


***

#### 方法重载

**重载介绍**

方法重载指同一个类中定义的多个方法之间的关系，满足下列条件的多个方法相互构成重载：

1. 多个方法在**同一个类**中
2. 多个方法具有**相同的方法名**
3. 多个方法的**参数不相同**，类型不同或者数量不同

重载仅对应方法的定义，与方法的调用无关，调用方式参照标准格式

重载仅针对同一个类中方法的名称与参数进行识别，与返回值无关，**不能通过返回值来判定两个方法是否构成重载**

原理：JVM → 运行机制 → 方法调用 → 多态原理

```java
public class MethodDemo {
	public static void fn(int a) {
		//方法体
	}
    
	public static int fn(int a) { /*错误原因：重载与返回值无关*/
		//方法体
	}
    
    public static void fn(int a, int b) {/*正确格式*/
		//方法体
	}
}
```

***

**方法选取**

重载的方法在编译过程中即可完成识别，方法调用时 Java 编译器会根据所传入参数的声明类型（注意与实际类型区分）来选取重载方法。选取的过程共分为三个阶段：

* 一阶段：在不考虑对基本类型自动装拆箱 (auto-boxing，auto-unboxing)，以及可变长参数的情况下选取重载方法
* 二阶段：如果第一阶段中没有找到适配的方法，那么在允许自动装拆箱，但不允许可变长参数的情况下选取重载方法
* 三阶段：如果第二阶段中没有找到适配的方法，那么在允许自动装拆箱以及可变长参数的情况下选取重载方法

如果 Java 编译器在同一个阶段中找到了多个适配的方法，那么会选择一个最为贴切的，而决定贴切程度的一个关键就是形式参数类型的继承关系，**一般会选择形参为参数类型的子类的方法，因为子类时更具体的实现**：

```java
public class MethodDemo {
    void invoke(Object obj, Object... args) { ... }
    void invoke(String s, Object obj, Object... args) { ... }

    invoke(null, 1); 	// 调用第二个invoke方法，选取的第二阶段
    invoke(null, 1, 2); // 调用第二个invoke方法，匹配第一个和第二个，但String是Object的子类
    
    invoke(null, new Object[]{1}); // 只有手动绕开可变长参数的语法糖，才能调用第一个invoke方法
    							   // 可变参数底层是数组，JVM->运行机制->代码优化
}
```

因此不提倡可变长参数方法的重载

***

**继承重载**

除了同一个类中的方法，重载也可以作用于这个类所继承而来的方法。如果子类定义了与父类中**非私有方法**同名的方法，而且这两个方法的参数类型不同，那么在子类中，这两个方法同样构成了重载

* 如果这两个方法都是静态的，那么子类中的方法隐藏了父类中的方法
* 如果这两个方法都不是静态的，且都不是私有的，那么子类的方法重写了父类中的方法，也就是**多态**

***

#### 参数传递

Java 的参数是以**值传递**的形式传入方法中

值传递和引用传递的区别在于传递后会不会影响实参的值：**值传递会创建副本**，引用传递不会创建副本

* 基本数据类型：形式参数的改变，不影响实际参数

  每个方法在栈内存中，都会有独立的栈空间，方法运行结束后就会弹栈消失

  ```java
  public class ArgsDemo01 {
  	public static void main(String[] args) {
  		int number = 100;
  		System.out.println("调用change方法前：" + number);//100
  		change(number);
  		System.out.println("调用change方法后：" + number);//100
  	}
  	public static void change(int number) {
  		number = 200;
  	}
  }
  ```

* 引用类型：形式参数的改变，影响实际参数的值

  **引用数据类型的传参，本质上是将对象的地址以值的方式传递到形参中**，内存中会造成两个引用指向同一个内存的效果，所以即使方法弹栈，堆内存中的数据也已经是改变后的结果

  ```java
  public class PassByValueExample {
      public static void main(String[] args) {
          Dog dog = new Dog("A");
          func(dog);
          System.out.println(dog.getName());	// B
      }
      private static void func(Dog dog) {
          dog.setName("B");
      }
  }
  class Dog {
      String name;//.....
  }
  ```



#### 方法引用

- ：：该符号为引用运算符，而它所在表达式被称为方法引用
- Lambda表达式：usePrintable(s->System.out.print(s));
- 方法引用：usePrinable(System.out::println)

**Lambda表达式支持的方法引用**

常见的引用方式：

- 引用类方法
- 引用对象的实例方法
- 引用类的实例方法
- 引用构造器

**引用类静态方法**
引用类方法，其实就是引用类的静态方法

- 格式：类名：：静态方法

- 范例：Integer::parseInt

  - Integer类的方法：public static int parsenInt(String s),将此String转换为int类型数据

  Lambda表达式被类方法替代的时候，它的形式参数全部传递给静态方法作为参数

**引用对象的实例方法**

引用对象的实例方法，其实是引用类中的成员方法

- 格式：对象：：成员方法
- 范式：”HelloWorld“::toUpperCase

**引用类的实例方法**

引用类的实例方法，其实就是引用类中的成员方法

- 格式：类名：：成员方法

- String::substring

**引用构造器**

- 格式：类名：：new
- 范式：Student：：new



#### 方法访问

子类继承了父类就得到了父类的方法，**可以直接调用**，受权限修饰符的限制，也可以重写方法

方法重写：子类重写一个与父类申明一样的方法来**覆盖**父类的该方法

方法重写的校验注解：@Override

* 方法加了这个注解，那就必须是成功重写父类的方法，否则报错
* @Override 优势：可读性好，安全，优雅

**子类可以扩展父类的功能，但不能改变父类原有的功能**，重写有以下三个限制：

- 子类方法的访问权限必须大于等于父类方法
- 子类方法的返回类型必须是父类方法返回类型或为其子类型
- 子类方法抛出的异常类型必须是父类抛出异常类型或为其子类型

继承中的隐藏问题：

- 子类和父类方法都是静态的，那么子类中的方法会隐藏父类中的方法
- 在子类中可以定义和父类成员变量同名的成员变量，此时子类的成员变量隐藏了父类的成员变量，在创建对象为对象分配内存的过程中，**隐藏变量依然会被分配内存**

---

#### 异常处理

Java 中的异常处理是保障程序健壮性的核心机制，用于应对程序运行时的非预期情况（如文件不存在、网络中断、数组越界等）。它通过规范化的方式捕获、处理异常，避免程序直接崩溃，并提供错误信息以便调试

**一、异常的本质与分类**

异常本质上是程序运行时抛出的异常对象，所有异常都继承自 `Throwable` 类，主要分类两类：

1. `Error`（错误）

- 由 JVM 生成，代表系统级错误（如内存溢出`OutOfMemoryError`、栈溢出`StackOverflowError`）,程序无法处理，也不应该捕获
- 通常是硬件或顶层虚拟机的问题，开发者需通过优化代码（如避免无限递归）或调整环境（如增加内存）解决

2. `Exception`（异常）

- 程序运行时的可预期错误，开发者可以捕获并处理。分为两类：

  - **受检异常**（Checked Exception）

    继承自 `Exceptipon` 但不继承 `RuntimeException`，编译时必须显式处理（捕获或声明抛出），否则编译报错

    典型例子：`IOException`（文件操作错误）、`SQLException`（数据库操作错误）

  - **非受检错误**（Unchecked Exception）

    继承自 `RuntimeException`，编译时无需强制处理，通常是代码逻辑错误导致

    典型例子：`NullPointerException`（空指针）、`IndexOutBoundsException`（数组越界）

**二、异常处理的核心机制**

Java 通过 `try-catch-finally`、`throw`、`throws` 关键字实现异常处理，核心流程是：捕获异常 --> 处理异常 --> 释放资源

**1、`try-catch`：捕获并处理异常**

- `try`块：包裹可能抛出异常的代码（如果执行中出现异常，会立即跳转到对应的`catch`块）。
- `catch`块：声明要捕获的异常类型，处理异常（如打印错误信息、重试操作等）。

**2、`finally`：释放资源（可选）**

`finally`块无论是否发生异常，都会执行（除非 JVM 退出，如`System.exit(0)`），通常用于释放资源（如关闭文件、网络连接）

**3、`try-with-resouces`：自动释放资源（Java 7+）**

对于实现`AutoCloseable`接口的资源（如`FileReader`、`Socket`），可通过`try-with-resources`自动关闭，无需手动在`finally`中释放，代码更简洁。

**4、`throw`：主动抛出异常**

在方法内部，通过 `throw` 关键字主动抛出异常对象（通常用于检测非法逻辑）

**5、`throws`：声明方法可能抛出的异常**

**三、自定义异常**

当 Java 内置异常无法满足业务需求时，可自定义异常（通常继承`Exception` 或 `RuntimeException`）

- 继承 `Exception`：成为受检异常（编译时需处理）
- 继承`RuntimeException`：称为非受检异常（编译时无需处理）

**四、异常处理的最佳实践**

1. **避免捕获所有异常**：不要用`catch (Exception e)`捕获所有异常，可能掩盖真正的错误（如`Error`）。
2. **不忽略异常**：空的`catch`块（`catch (Exception e) {}`）会导致错误无法被发现，至少打印异常信息。
3. **早抛出，晚捕获**：异常发生时尽早抛出（如参数校验失败时），在合适的高层统一处理（如 UI 层展示错误信息）。
4. **优先使用非受检异常**：业务逻辑错误建议用`RuntimeException`子类（减少代码中冗余的`try-catch`）。
5. **用`try-with-resources`管理资源**：避免手动关闭资源时的遗漏或错误。

***

### Java类

#### 概述

Java 是一种面向对象的高级编程语言

面向对象三大特征：**封装，继承，多态**

两个概念：类和对象

* 类：相同事物共同特征的描述，类只是学术上的一个概念并非真实存在的，只能描述一类事物
* 对象：是真实存在的实例， 实例 == 对象，**对象是类的实例化**
* 结论：有了类和对象就可以描述万千世界所有的事物，必须先有类才能有对象

***

#### 类

#### 定义

定义格式

```java
修饰符 class 类名{
}
```

1. 类名的首字母建议大写，满足驼峰模式，比如 StudentNameCode
2. 一个 Java 代码中可以定义多个类，按照规范一个 Java 文件一个类
3. 一个 Java 代码文件中，只能有一个类是 public 修饰，**public 修饰的类名必须成为当前 Java 代码的文件名称**

```java
类中的成分:有且仅有五大成分
修饰符 class 类名{
		1.成员变量(Field):  	描述类或者对象的属性信息的。
        2.成员方法(Method):		描述类或者对象的行为信息的。
		3.构造器(Constructor):	 初始化一个对象返回。
		4.代码块
		5.内部类
	  }
类中有且仅有这五种成分，否则代码报错！
public class ClassDemo {
    System.out.println(1);//报错
}
```

***

#### 构造器

构造器格式：

```java
修饰符 类名(形参列表){

}
```

作用：初始化类的一个对象返回

分类：无参数构造器，有参数构造器

注意：**一个类默认自带一个无参数构造器**，写了有参数构造器默认的无参数构造器就消失，还需要用无参数构造器就要重新写

构造器初始化对象的格式：类名 对象名称 = new 构造器

* 无参数构造器的作用：初始化一个类的对象（使用对象的默认值初始化）返回
* 有参数构造器的作用：初始化一个类的对象（可以在初始化对象的时候为对象赋值）返回

***

#### 封装

封装的哲学思维：合理隐藏，合理暴露

封装最初的目的：提高代码的安全性和复用性，组件化

封装的步骤：

1. **成员变量应该私有，用 private 修饰，只能在本类中直接访问**
2. **提供成套的 getter 和 setter 方法暴露成员变量的取值和赋值**

使用 private 修饰成员变量的原因：实现数据封装，不想让别人使用修改你的数据，比较安全

***

#### this

this 关键字的作用：

* this 关键字代表了当前对象的引用
* this 出现在方法中：**哪个对象调用这个方法 this 就代表谁**
* this 可以出现在构造器中：代表构造器正在初始化的那个对象
* this 可以区分变量是访问的成员变量还是局部变量

***

#### 继承

**基本介绍**

继承是 Java 中一般到特殊的关系，是一种子类到父类的关系

* 被继承的类称为：父类/超类
* 继承父类的类称为：子类

继承的作用：

* **提高代码的复用**，相同代码可以定义在父类中
* 子类继承父类，可以直接使用父类这些代码（相同代码重复利用）
* 子类得到父类的属性（成员变量）和行为（方法），还可以定义自己的功能，子类更强大

继承的特点：

1. 子类的全部构造器默认先访问父类的无参数构造器，再执行自己的构造器
2. **单继承**：一个类只能继承一个直接父类
3. 多层继承：一个类可以间接继承多个父类（家谱）
4. 一个类可以有多个子类
5. 一个类要么默认继承了 Object 类，要么间接继承了 Object 类，**Object 类是 Java 中的祖宗类**

继承的格式：

```java
子类 extends 父类{

}
```

子类不能继承父类的东西：

* 子类不能继承父类的构造器，子类有自己的构造器
* 子类是不能可以继承父类的私有成员的，可以反射暴力去访问继承自父类的私有成员
* 子类是不能继承父类的静态成员，父类静态成员只有一份可以被子类共享访问，**共享并非继承**

```java
public class ExtendsDemo {
    public static void main(String[] args) {
        Cat c = new Cat();
        // c.run();
        Cat.test();
        System.out.println(Cat.schoolName);
    }
}

class Cat extends Animal{
}

class Animal{
    public static String schoolName ="seazean";
    public static void test(){}
    private void run(){}
}
```

*****

**变量访问**

继承后成员变量的访问特点：**就近原则**，子类有找子类，子类没有找父类，父类没有就报错

如果要申明访问父类的成员变量可以使用：super.父类成员变量，super指父类引用

```java
public class ExtendsDemo {
    public static void wmain(String[] args) {
        Wolf w = new Wolf();w
        w.showName();
    }
}

class Wolf extends Animal{
    private String name = "子类狼";
    public void showName(){
        String name = "局部名称";
        System.out.println(name); // 局部name
        System.out.println(this.name); // 子类对象的name
        System.out.println(super.name); // 父类的
        System.out.println(name1); // 父类的
        //System.out.println(name2); // 报错。子类父类都没有
    }
}

class Animal{
    public String name = "父类动物名称";
    public String name1 = "父类";
}
```



***



```java
public class ExtendsDemo {
    public static void main(String[] args) {
        Wolf w = new Wolf();
        w.run();
    }
}
class Wolf extends Animal{
    @Override
    public void run(){}//
}
class Animal{
    public void run(){}
}
```

***

**常见问题**

* 为什么子类构造器会先调用父类构造器？

  1. 子类的构造器的第一行默认 super() 调用父类的无参数构造器，写不写都存在
  2. 子类继承父类，子类就得到了父类的属性和行为。调用子类构造器初始化子类对象数据时，必须先调用父类构造器初始化继承自父类的属性和行为
  3. 参考 JVM → 类加载 → 对象创建

  ```java
  class Animal {
      public Animal() {
          System.out.println("==父类Animal的无参数构造器==");
      }
  }
  
  class Tiger extends Animal {
      public Tiger() {
          super(); // 默认存在的，根据参数去匹配调用父类的构造器。
          System.out.println("==子类Tiger的无参数构造器==");
      }
      public Tiger(String name) {
          //super();  默认存在的，根据参数去匹配调用父类的构造器。
          System.out.println("==子类Tiger的有参数构造器==");
      }
  }
  ```

* **为什么 Java 是单继承的？**

  答：反证法，假如 Java 可以多继承，请看如下代码：

  ```java
  class A{
  	public void test(){
  		System.out.println("A");
  	}
  }
  class B{
  	public void test(){
  		System.out.println("B");
  	}
  }
  class C extends A , B {
  	public static void main(String[] args){
  		C c = new C();
          c.test(); 
          // 出现了类的二义性！所以Java不能多继承！！
  	}
  }
  ```

  

***

#### super

继承后 super 调用父类构造器，父类构造器初始化继承自父类的数据。


总结与拓展：

* this 代表了当前对象的引用（继承中指代子类对象）：this.子类成员变量、this.子类成员方法、**this(...)** 可以根据参数匹配访问本类其他构造器
* super 代表了父类对象的引用（继承中指代了父类对象空间）：super.父类成员变量、super.父类的成员方法、super(...) 可以根据参数匹配访问父类的构造器

注意：

* this(...) 借用本类其他构造器，super(...) 调用父类的构造器
* this(...) 或 super(...) 必须放在构造器的第一行，否则报错
* this(...) 和 super(...) **不能同时出现**在构造器中，因为构造函数必须出现在第一行上，只能选择一个

```java
public class ThisDemo {
    public static void main(String[] args) {
        // 需求：希望如果不写学校默认就是”张三“！
        Student s1 = new Student("天蓬元帅", 1000 );
        Student s2 = new Student("齐天大圣", 2000, "清华大学" );
    }
}
class Study extends Student {
   public Study(String name, int age, String schoolName) {
        super(name , age , schoolName) ; 
       // 根据参数匹配调用父类构造器
   }
}

class Student{
    private String name ;
    private int age ;
    private String schoolName ;

    public Student() {
    }
    public Student(String name , int age){
        // 借用兄弟构造器的功能！
        this(name , age , "张三");
    }
	public Student(String name, int age, String schoolName) {
        this.name = name;
        this.age = age;
        this.schoolName = schoolName;
    }
	// .......get + set
}
```

#### 抽象类

基本介绍

> 父类知道子类要完成某个功能，但是每个子类实现情况不一样

抽象方法：没有方法体，只有方法签名，必须用 abstract 修饰的方法就是抽象方法

抽象类：拥有抽象方法的类必须定义成抽象类，必须用 abstract 修饰，**抽象类是为了被继承**

一个类继承抽象类，**必须重写抽象类的全部抽象方法**，否则这个类必须定义成抽象类

```java
public class AbstractDemo {
    public static void main(String[] args) {
        Dog d = new Dog();
        d.run();
    }
}

class Dog extends Animal{
    @Override
    public void run() { 
        System.out.println("🐕跑"); 
    }
}

abstract class Animal{
    public abstract void run();
}
```

常见问题

一、抽象类是否有构造器，是否可以创建对象?

* 抽象类有构造器，但是抽象类不能创建对象，类的其他成分它都具备，构造器提供给子类继承后调用父类构造器使用
* 抽象类中存在抽象方法，但不能执行，**抽象类中也可没有抽象方法**

> 抽象在学术上本身意味着不能实例化

```java
public class AbstractDemo {
    public static void main(String[] args) {
        //Animal a = new Animal(); 抽象类不能创建对象！
        //a.run(); // 抽象方法不能执行
    }
}
abstract class Animal{
    private String name;
    public static String schoolName = "张三";
    public Animal(){ }

    public abstract void run();
    //普通方法
    public void go(){ }
}
```

二、static 与 abstract 能同时使用吗？

答：不能，被 static 修饰的方法属于类，是类自己的东西，不是给子类来继承的，而抽象方法本身没有实现，就是用来给子类继承

**存在意义**

**被继承**，抽象类就是为了被子类继承，否则抽象类将毫无意义（核心）

抽象类体现的是"模板思想"：**部分实现，部分抽象**，可以使用抽象类设计一个模板模式

```java
//作文模板
public class ExtendsDemo {
    public static void main(String[] args) {
        Student xiaoMa = new Student();
        xiaoMa.write();
    }
}
class Student extends Template{
    @Override
    public String writeText() {return "\t内容"}
}
// 1.写一个模板类：代表了作文模板。
abstract class Template{
    private String title = "\t\t\t\t\t标题";
    private String start = "\t开头";
    private String last = "\t结尾";
    public void write(){
        System.out.println(title+"\n"+start);
        System.out.println(writeText());
        System.out.println(last);
    }
    // 正文部分定义成抽象方法，交给子类重写！！
    public abstract String writeText();
}
```

***



***



#### 对比抽象类

| **参数**           | **抽象类**                                                   | **接口**                                                     |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 默认的方法实现     | 可以有默认的方法实现                                         | 接口完全是抽象的，jdk8 以后有默认的实现                      |
| 实现               | 子类使用 **extends** 关键字来继承抽象类。如果子类不是抽象类的话，它需要提供抽象类中所有声明的方法的实现。 | 子类使用关键字 **implements** 来实现接口。它需要提供接口中所有声明的方法的实现 |
| 构造器             | 抽象类可以有构造器                                           | 接口不能有构造器                                             |
| 与正常Java类的区别 | 除了不能实例化抽象类之外，和普通 Java 类没有任何区别         | 接口是完全不同的类型                                         |
| 访问修饰符         | 抽象方法有 **public**、**protected** 和 **default** 这些修饰符 | 接口默认修饰符是 **public**，别的修饰符需要有方法体          |
| main方法           | 抽象方法可以有 main 方法并且我们可以运行它                   | jdk8 以前接口没有 main 方法，不能运行；jdk8 以后接口可以有 default 和 static 方法，可以运行 main 方法 |
| 多继承             | 抽象方法可以继承一个类和实现多个接口                         | 接口可以继承一个或多个其它接口，接口不可继承类               |
| 速度               | 比接口速度要快                                               | 接口是稍微有点慢的，因为它需要时间去寻找在类中实现的方法     |
| 添加新方法         | 如果往抽象类中添加新的方法，可以给它提供默认的实现，因此不需要改变现在的代码 | 如果往接口中添加方法，那么必须改变实现该接口的类             |

***

#### 多态

**基本介绍**

多态的概念：同一个实体同时具有多种形式同一个类型的对象，执行同一个行为，在不同的状态下会表现出不同的行为特征

多态的格式：

* 父类类型范围 > 子类类型范围

```java
父类类型 对象名称 = new 子类构造器;
接口	  对象名称 = new 实现类构造器;
```

多态的执行：

* 对于方法的调用：**编译看左边，运行看右边**（分派机制）
* 对于变量的调用：**编译看左边，运行看左边**

多态的使用规则：

* 必须存在继承或者实现关系
* 必须存在父类类型的变量引用子类类型的对象
* 存在方法重写

多态的优势：

* 在多态形式下，右边对象可以实现组件化切换，便于扩展和维护，也可以实现类与类之间的**解耦**
* 父类类型作为方法形式参数，传递子类对象给方法，可以传入一切子类对象进行方法的调用，更能体现出多态的**扩展性**与便利性

多态的劣势： 

* 多态形式下，不能直接调用子类特有的功能，因为编译看左边，父类中没有子类独有的功能，所以代码在编译阶段就直接报错了

```java
public class PolymorphicDemo {
    public static void main(String[] args) {
        Animal c = new Cat();
        c.run();
        //c.eat();//报错  编译看左边 需要强转
        go(c);
        go(new Dog);   
    }
    //用 Dog或者Cat 都没办法让所有动物参与进来，只能用Anima
    public static void go(Animal d){}
    
}
class Dog extends Animal{}

class Cat extends Animal{
    public void eat();
    @Override
    public void run(){}
}

class Animal{
    public void run(){}
}
```

***

**上下转型**

>基本数据类型的转换：
>
>1. 小范围类型的变量或者值可以直接赋值给大范围类型的变量
>2. 大范围类型的变量或者值必须强制类型转换给小范围类型的变量

引用数据类型的**自动**类型转换语法：子类类型的对象或者变量可以自动类型转换赋值给父类类型的变量

**父类引用指向子类对象**

- **向上转型 (upcasting)**：通过子类对象（小范围）实例化父类对象（大范围），这种属于自动转换
- **向下转型 (downcasting)**：通过父类对象（大范围）实例化子类对象（小范围），这种属于强制转换

```java
public class PolymorphicDemo {
    public static void main(String[] args){
        Animal a = new Cat();	// 向上转型
        Cat c = (Cat)a;			// 向下转型
    }
}
class Animal{}
class Cat extends Animal{}
```

***

#### 内部类

**概述**

内部类是类的五大成分之一：成员变量，方法，构造器，代码块，内部类

概念：定义在一个类里面的类就是类

作用：提供更好的封装性，体现出组件思想，**间接解决类无法多继承引起的一系列问题**

分类：静态内部类、实例内部类（成员内部类）、局部内部类、**匿名内部类**（重点）

**静态内部类**

定义：有 static 修饰，属于外部类本身，会加载一次

静态内部类中的成分研究：

* 类有的成分它都有，静态内部类属于外部类本身，只会加载一次
* 特点与外部类是完全一样的，只是位置在别人里面
* 可以定义静态成员

静态内部类的访问格式：外部类名称.内部类名称

静态内部类创建对象的格式：外部类名称.内部类名称 对象名称 = new 外部类名称.内部类构造器

静态内部类的访问拓展：

* 静态内部类中是否可以直接访问外部类的静态成员?	可以，外部类的静态成员只有一份，可以被共享
* 静态内部类中是否可以直接访问外部类的实例成员?	不可以，外部类的成员必须用外部类对象访问

```java
public class Demo{
    public static void main(String[] args){
        Outter.Inner in = new Outter.Inner();
    }
}

static class Outter{
    public static int age;
    private double salary;
    public static class Inner{
         //拥有类的所有功能 构造器 方法 成员变量
         System.out.println(age);
         //System.out.println(salary);报错
	}
}
```

**匿名内部类**

匿名内部类：没有名字的局部内部类

匿名内部类的格式：

```java
new 类名|抽象类|接口(形参){
	//方法重写。
}
```

 匿名内部类的特点：

* 匿名内部类不能定义静态成员
* 匿名内部类一旦写出来，就会立即创建一个匿名内部类的对象返回
* **匿名内部类的对象的类型相当于是当前 new 的那个的类型的子类类型**
* 匿名内部类引用局部变量必须是**常量**，底层创建为内部类的成员变量（原因：JVM → 运行机制 → 代码优化）

```java
public class Anonymity {
    public static void main(String[] args) {
        Animal a = new Animal(){
            @Override
            public void run() {
                System.out.println("猫跑的贼溜~~");
                //System.out.println(n);
            }
        };
        a.run();
        a.go();
    }
}
abstract class Animal{
    public abstract void run();

    public void go(){
        System.out.println("开始go~~~");
    }
}
```

#### 代码块

**静态代码块**

静态代码块的格式：

 ```java
static {
}
 ```

* 静态代码块特点： 
  * 必须有 static 修饰，只能访问静态资源
  * 会与类一起优先加载，且自动触发执行一次
* 静态代码块作用：
  * 可以在执行类的方法等操作之前先在静态代码块中进行静态资源的初始化 
  * **先执行静态代码块，在执行 main 函数里的操作**

```java
public class CodeDemo {
    public static String schoolName ;
    public static ArrayList<String> lists = new ArrayList<>();

    // 静态代码块,属于类，与类一起加载一次!
    static {
        System.out.println("静态代码块被触发执行~~~~~~~");
        // 在静态代码块中进行静态资源的初始化操作
        schoolName = "张三";
        lists.add("3");
        lists.add("4");
        lists.add("5");
    }
    public static void main(String[] args) {
        System.out.println("main方法被执行");
        System.out.println(schoolName);
        System.out.println(lists);
    }
}
/*静态代码块被触发执行~~~~~~~
main方法被执行
张三
[3, 4, 5] */
```

**实例代码块**

实例代码块的格式：

```java
{

}
```

* 实例代码块的特点：
  * 无 static 修饰，属于对象
  * 会与类的对象一起加载，每次创建类的对象的时候，实例代码块都会被加载且自动触发执行一次
  * 实例代码块的代码在底层实际上是提取到每个构造器中去执行的

* 实例代码块的作用：实例代码块可以在创建对象之前进行实例资源的初始化操作

```java
public class CodeDemo {
    private String name;
    private ArrayList<String> lists = new ArrayList<>();
    {
        name = "代码块";
        lists.add("java");
        System.out.println("实例代码块被触发执行一次~~~~~~~~");
    }
    public CodeDemo02(){ }//构造方法
    public CodeDemo02(String name){}

    public static void main(String[] args) {
        CodeDemo c = new CodeDemo();//实例代码块被触发执行一次
        System.out.println(c.name);
        System.out.println(c.lists);
        new CodeDemo02();//实例代码块被触发执行一次
    }
}
```

### JavaAPI

#### Object

**基本介绍**

Object 类是 Java 中的祖宗类，一个类或者默认继承 Object 类，或者间接继承 Object 类，Object 类的方法是一切子类都可以直接使用

Object 类常用方法：

* `public String toString()`：默认是返回当前对象在堆内存中的地址信息：类的全限名@内存地址，例：Student@735b478；
  * 直接输出对象名称，默认会调用 toString() 方法，所以省略 toString() 不写；
  * 如果输出对象的内容，需要重写 toString() 方法，toString 方法存在的意义是为了被子类重写
* `public boolean equals(Object o)`：默认是比较两个对象的引用是否相同
* `protected Object clone()`：创建并返回此对象的副本 

只要两个对象的内容一样，就认为是相等的：

```java
public boolean equals(Object o) {
	// 1.判断是否自己和自己比较，如果是同一个对象比较直接返回true
	if (this == o) return true;
	// 2.判断被比较者是否为null ,以及是否是学生类型。
	if (o == null || this.getClass() != o.getClass()) return false;
	// 3.o一定是学生类型，强制转换成学生，开始比较内容！
	Student student = (Student) o;
	return age == student.age &&
           sex == student.sex &&
           Objects.equals(name, student.name);
}
```

**面试题**：== 和 equals 的区别

* == 比较的是变量（栈）内存中存放的对象的（堆）内存地址，用来判断两个对象的**地址**是否相同，即是否是指相同一个对象，比较的是真正意义上的指针操作
* Object 类中的方法，**默认比较两个对象的引用**，重写 equals 方法比较的是两个对象的**内容**是否相等，所有的类都是继承自 java.lang.Object 类，所以适用于所有对象

hashCode 的作用：

* hashCode 的存在主要是用于查找的快捷性，如 Hashtable，HashMap 等，可以在散列存储结构中确定对象的存储地址
* 如果两个对象相同，就是适用于 equals(java.lang.Object) 方法，那么这两个对象的 hashCode 一定要相同
* 哈希值相同的数据不一定内容相同，内容相同的数据哈希值一定相同

***

**深浅克隆**

Object 的 clone() 是 protected 方法，一个类不显式去重写 clone()，就不能直接去调用该类实例的 clone() 方法

深浅拷贝（克隆）的概念：

* 浅拷贝 (shallowCopy)：**对基本数据类型进行值传递，对引用数据类型只是复制了引用**，被复制对象属性的所有的引用仍然指向原来的对象，简而言之就是增加了一个指针指向原来对象的内存地址

  **Java 中的复制方法基本都是浅拷贝**：Object.clone()、System.arraycopy()、Arrays.copyOf()

* 深拷贝 (deepCopy)：对基本数据类型进行值传递，对引用数据类型是一个整个独立的对象拷贝，会拷贝所有的属性并指向的动态分配的内存，简而言之就是把所有属性复制到一个新的内存，增加一个指针指向新内存。所以使用深拷贝的情况下，释放内存的时候不会出现使用浅拷贝时释放同一块内存的错误

Cloneable 接口是一个标识性接口，即该接口不包含任何方法（包括 clone），但是如果一个类想合法的进行克隆，那么就必须实现这个接口，在使用 clone() 方法时，若该类未实现 Cloneable 接口，则抛出异常

* Clone & Copy：`Student s = new Student`

  `Student s1 = s`：只是 copy 了一下 reference，s 和 s1 指向内存中同一个 Object，对对象的修改会影响对方

  `Student s2 = s.clone()`：会生成一个新的 Student 对象，并且和 s 具有相同的属性值和方法

* Shallow Clone & Deep Clone：

  浅克隆：Object 中的 clone() 方法在对某个对象克隆时对其仅仅是简单地执行域对域的 copy

  * 对基本数据类型和包装类的克隆是没有问题的。String、Integer 等包装类型在内存中是**不可以被改变的对象**，所以在使用克隆时可以视为基本类型，只需浅克隆引用即可
  * 如果对一个引用类型进行克隆时只是克隆了它的引用，和原始对象共享对象成员变量

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/Object浅克隆.jpg)

  深克隆：在对整个对象浅克隆后，对其引用变量进行克隆，并将其更新到浅克隆对象中去

  ```java
  public class Student  implements Cloneable{
      private String name;
      private Integer age;
      private Date date;
  
      @Override
      protected Object clone() throws CloneNotSupportedException {
          Student s = (Student) super.clone();
          s.date = (Date) date.clone();
          return s;
      }
      //.....
  }
  ```

SDP → 创建型 → 原型模式

***

**Objects**

Objects 类与 Object 是继承关系

Objects 的方法：

* `public static boolean equals(Object a, Object b)`：比较两个对象是否相同

  ```java
  public static boolean equals(Object a, Object b) {
      // 进行非空判断，从而可以避免空指针异常
      return a == b || a != null && a.equals(b);
  }
  ```

* `public static boolean isNull(Object obj)`：判断变量是否为 null ，为 null 返回 true

* `public static String toString(对象)`：返回参数中对象的字符串表示形式

* `public static String toString(对象, 默认字符串)`：返回对象的字符串表示形式

```java
public class ObjectsDemo {
    public static void main(String[] args) {
        Student s1 = null;
        Student s2 = new Student();
        System.out.println(Objects.equals(s1 , s2));//推荐使用
        // System.out.println(s1.equals(s2)); // 空指针异常
 
        System.out.println(Objects.isNull(s1));
        System.out.println(s1 == null);//直接判断比较好
    }
}

public class Student {
}
```

***

#### String

- 基本介绍

String 被声明为 final，因此不可被继承 **（Integer 等包装类也不能被继承）**

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
 	/** The value is used for character storage. */
    private final char value[];
    /** Cache the hash code for the string */
    private int hash; // Default to 0
}
```

在 Java 9 之后，String 类的实现改用 byte 数组存储字符串，同时使用 `coder` 来标识使用了哪种编码

value 数组被声明为 final，这意味着 value 数组初始化之后就不能再引用其它数组，并且 String 内部没有改变 value 数组的方法，因此可以**保证 String 不可变，也保证线程安全**

注意：不能改变的意思是**每次更改字符串都会产生新的对象**，并不是对原始对象进行改变

```java
String s = "abc";
s = s + "cd"; //s = abccd 新对象
```

***

**常用方法**

常用 API：

* `public boolean equals(String s)`：比较两个字符串内容是否相同、区分大小写

* `public boolean equalsIgnoreCase(String anotherString)`：比较字符串的内容，忽略大小写
* `public int length()`：返回此字符串的长度
* `public String trim()`：返回一个字符串，其值为此字符串，并删除任何前导和尾随空格
* `public String[] split(String regex)`：将字符串按给定的正则表达式分割成字符串数组
* `public char charAt(int index)`：取索引处的值
* `public char[] toCharArray()`：将字符串拆分为字符数组后返回
* `public boolean startsWith(String prefix)`：测试此字符串是否以指定的前缀开头
* `public int indexOf(String str)`：返回指定子字符串第一次出现的字符串内的索引，没有返回 -1
* `public int lastIndexOf(String str)`：返回字符串最后一次出现 str 的索引，没有返回 -1
* `public String substring(int beginIndex)`：返回子字符串，以原字符串指定索引处到结尾
* `public String substring(int i, int j)`：指定索引处扩展到 j - 1 的位置，字符串长度为 j - i
* `public String toLowerCase()`：将此 String 所有字符转换为小写，使用默认语言环境的规则
* `public String toUpperCase()`：使用默认语言环境的规则将此 String 所有字符转换为大写
* `public String replace(CharSequence target, CharSequence replacement)`：使用新值，将字符串中的旧值替换，得到新的字符串

```java
String s = 123-78;
s.replace("-","");//12378
```

***

**构造方式**

构造方法：

* `public String()`：创建一个空白字符串对象，不含有任何内容
* `public String(char[] chs)`：根据字符数组的内容，来创建字符串对象
* `public String(String original)`：根据传入的字符串内容，来创建字符串对象

直接赋值：`String s = "abc"` 直接赋值的方式创建字符串对象，内容就是 abc

- 通过构造方法创建：通过 new 创建的字符串对象，每一次 new 都会申请一个内存空间，虽然内容相同，但是地址值不同，**返回堆内存中对象的引用**
- 直接赋值方式创建：以 `" "` 方式给出的字符串，只要字符序列相同（顺序和大小写），无论在程序代码中出现几次，JVM 都只会**在 String Pool 中创建一个字符串对象**，并在字符串池中维护

`String str = new String("abc")` 创建字符串对象：

* 创建一个对象：字符串池中已经存在 abc 对象，那么直接在创建一个对象放入堆中，返回堆内引用
* 创建两个对象：字符串池中未找到 abc 对象，那么分别在堆中和字符串池中创建一个对象，字符串池中的比较都是采用 equals() 
  <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/String构造方法字节码.png" style="zoom: 67%;" />

`new String("a") + new String("b")` 创建字符串对象：

* 对象 1：new StringBuilder()

* 对象 2：new String("a")、对象 3：常量池中的 a

* 对象 4：new String("b")、对象 5：常量池中的 b
  <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/String拼接方法字节码.png" style="zoom:67%;" />

* StringBuilder 的 toString()：

  ```java
  @Override
  public String toString() {
      return new String(value, 0, count);
  }
  ```

  * 对象 6：new String("ab")
  * StringBuilder 的 toString() 调用，**在字符串常量池中没有生成 ab**，new String("ab") 会创建两个对象因为传参数的时候使用字面量创建了对象 ab，当使用数组构造 String 对象时，没有加入常量池的操作

***

**String Pool**

基本介绍

字符串常量池（String Pool / StringTable / 串池）保存着所有字符串字面量（literal strings），这些字面量在编译时期就确定，常量池类似于 Java 系统级别提供的**缓存**，存放对象和引用

* StringTable，类似 HashTable 结构，通过 `-XX:StringTableSize` 设置大小，JDK 1.8 中默认 60013
* 常量池中的字符串仅是符号，第一次使用时才变为对象，可以避免重复创建字符串对象
* 字符串**变量**的拼接的原理是 StringBuilder#append，append 方法比字符串拼接效率高（JDK 1.8）
* 字符串**常量**拼接的原理是编译期优化，拼接结果放入常量池
* 可以使用 String 的 intern() 方法在运行过程将字符串添加到 String Pool 中

***

**intern**()

JDK 1.8：当一个字符串调用 intern() 方法时，如果 String Pool 中：

* 存在一个字符串和该字符串值相等，就会返回 String Pool 中字符串的引用（需要变量接收）
* 不存在，会把对象的**引用地址**复制一份放入串池，并返回串池中的引用地址，前提是堆内存有该对象，因为 Pool 在堆中，为了节省内存不再创建新对象

JDK 1.6：将这个字符串对象尝试放入串池，如果有就不放入，返回已有的串池中的对象的引用；如果没有会把此对象复制一份，放入串池，把串池中的对象返回

```java
public class Demo {
    // 常量池中的信息都加载到运行时常量池，这时a b ab是常量池中的符号，还不是java字符串对象，是懒惰的
    // ldc #2 会把 a 符号变为 "a" 字符串对象     ldc:反编译后的指令
    // ldc #3 会把 b 符号变为 "b" 字符串对象
    // ldc #4 会把 ab 符号变为 "ab" 字符串对象
    public static void main(String[] args) {
        String s1 = "a"; 	// 懒惰的
        String s2 = "b";
        String s3 = "ab";	// 串池
        
        String s4 = s1 + s2;	// 返回的是堆内地址
        // 原理：new StringBuilder().append("a").append("b").toString()  new String("ab")
        
        String s5 = "a" + "b";  // javac 在编译期间的优化，结果已经在编译期确定为ab

        System.out.println(s3 == s4); // false
        System.out.println(s3 == s5); // true

        String x2 = new String("c") + new String("d"); // new String("cd")
        // 虽然 new，但是在字符串常量池没有 cd 对象，因为 toString() 方法
        x2.intern();
        String x1 = "cd";

        System.out.println(x1 == x2); //true
    }
}
```

- == 比较基本数据类型：比较的是具体的值
- == 比较引用数据类型：比较的是对象地址值

结论：

```java
String s1 = "ab";								// 仅放入串池
String s2 = new String("a") + new String("b");	// 仅放入堆
// 上面两条指令的结果和下面的 效果 相同
String s = new String("ab");
```

***

**常见问题**

问题一：

```java
public static void main(String[] args) {
    String s = new String("a") + new String("b");//new String("ab")
    //在上一行代码执行完以后，字符串常量池中并没有"ab"

    String s2 = s.intern();
    //jdk6：串池中创建一个字符串"ab"
    //jdk8：串池中没有创建字符串"ab",而是创建一个引用指向 new String("ab")，将此引用返回

    System.out.println(s2 == "ab");//jdk6:true  jdk8:true
    System.out.println(s == "ab");//jdk6:false  jdk8:true
}
```

问题二：

```java
public static void main(String[] args) {
    String str1 = new StringBuilder("58").append("tongcheng").toString();
    System.out.println(str1 == str1.intern());//true，字符串池中不存在，把堆中的引用复制一份放入串池

    String str2 = new StringBuilder("ja").append("va").toString();
    System.out.println(str2 == str2.intern());//false，字符串池中存在，直接返回已经存在的引用
}
```

原因：

* System 类当调用 Version 的静态方法，导致 Version 初始化：

  ```java
  private static void initializeSystemClass() {
      sun.misc.Version.init();
  }
  ```

* Version 类初始化时需要对静态常量字段初始化，被 launcher_name 静态常量字段所引用的 `"java"` 字符串字面量就被放入的字符串常量池：

  ```java
  package sun.misc;
  
  public class Version {
      private static final String launcher_name = "java";
      private static final String java_version = "1.8.0_221";
      private static final String java_runtime_name = "Java(TM) SE Runtime Environment";
      private static final String java_profile_name = "";
      private static final String java_runtime_version = "1.8.0_221-b11";
      //...
  }
  ```

***

**内存位置**

Java 7 之前，String Pool 被放在运行时常量池中，属于永久代；Java 7 以后，String Pool 被移到堆中，这是因为永久代的空间有限，在大量使用字符串的场景下会导致 OutOfMemoryError 错误

演示 StringTable 位置：

* `-Xmx10m` 设置堆内存 10m

* 在 JDK8 下设置： `-Xmx10m -XX:-UseGCOverheadLimit`（运行参数在 Run Configurations VM options）

* 在 JDK6 下设置： `-XX:MaxPermSize=10m`

  ```java
  public static void main(String[] args) throws InterruptedException {
      List<String> list = new ArrayList<String>();
      int i = 0;
      try {
          for (int j = 0; j < 260000; j++) {
              list.add(String.valueOf(j).intern());
              i++;
          }
      } catch (Throwable e) {
          e.printStackTrace();
      } finally {
          System.out.println(i);
      }
  }
  ```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/JVM-内存图对比.png)

#### StringBuilder

String StringBuffer 和 StringBuilder 区别：

* String : **不可变**的字符序列，线程安全
* StringBuffer : **可变**的字符序列，线程安全，底层方法加 synchronized，效率低
* StringBuilder : **可变**的字符序列，JDK5.0 新增；线程不安全，效率高

相同点：底层使用 char[] 存储

构造方法：

* `public StringBuilder()`：创建一个空白可变字符串对象，不含有任何内容
* `public StringBuilder(String str)`：根据字符串的内容，来创建可变字符串对象

常用API : 

* `public StringBuilder append(任意类型)`：添加数据，并返回对象本身
* `public StringBuilder reverse()`：返回相反的字符序列
* `public String toString()`：通过 toString() 就可以实现把 StringBuilder 转换为 String

存储原理：

```java
String str = "abc";
char data[] = {'a', 'b', 'c'};
StringBuffer sb1 = new StringBuffer();//new byte[16] 
sb1.append('a'); //value[0] = 'a';
```

append 源码：扩容为二倍

```java
public AbstractStringBuilder append(String str) {
    if (str == null) return appendNull();
    int len = str.length();
    ensureCapacityInternal(count + len);
    str.getChars(0, len, value, count);
    count += len;
    return this;
}
private void ensureCapacityInternal(int minimumCapacity) {
    // 创建超过数组长度就新的char数组，把数据拷贝过去
    if (minimumCapacity - value.length > 0) {
        //int newCapacity = (value.length << 1) + 2;每次扩容2倍+2
        value = Arrays.copyOf(value, newCapacity(minimumCapacity));
    }
}
public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
    // 将字符串中的字符复制到目标字符数组中
	// 字符串调用该方法，此时value是字符串的值，dst是目标字符数组
    System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
}
```

****

#### StringJoiner

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/d75bc68a-8378-438b-8217-7a87104c55e9.png)

****

#### Arrays

Array 的工具类 Arrays

常用API：

* `public static String toString(int[] a)`：返回指定数组的内容的字符串表示形式
* `public static void sort(int[] a)`：按照数字顺序排列指定的数组
* `public static int binarySearch(int[] a, int key)`：利用二分查找返回指定元素的索引
* `public static <T> List<T> asList(T... a)`：返回由指定数组支持的列表

```java
public class MyArraysDemo {
      public static void main(String[] args) {
		//按照数字顺序排列指定的数组
        int [] arr = {3,2,4,6,7};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
		
        int [] arr = {1,2,3,4,5,6,7,8,9,10};
        int index = Arrays.binarySearch(arr, 0);
        System.out.println(index);
        //1,数组必须有序
        //2.如果要查找的元素存在,那么返回的是这个元素实际的索引
        //3.如果要查找的元素不存在,那么返回的是 (-插入点-1)
            //插入点:如果这个元素在数组中,他应该在哪个索引上.
      }
  }
```

***

#### Random

用于生成伪随机数。

使用步骤：

1. 导入包：`import java.util.Random`
2. 创建对象：`Random r = new Random()`
3. 随机整数：`int num = r.nextInt(10)`
   * 解释：10 代表的是一个范围，如果括号写 10，产生的随机数就是 0 - 9，括号写 20 的随机数则是 0 - 19
   * 获取 0 - 10：`int num = r.nextInt(10 + 1)`

4. 随机小数：`public double nextDouble()` 从范围 `0.0d` 至 `1.0d` （左闭右开），伪随机地生成并返回

***

#### System

System 代表当前系统

静态方法：

* `public static void exit(int status)`：终止 JVM 虚拟机，**非 0 是异常终止**

* `public static long currentTimeMillis()`：获取当前系统此刻时间毫秒值

* `static void arraycopy(Object var0, int var1, Object var2, int var3, int var4)`：数组拷贝
  * 参数一：原数组
  * 参数二：从原数组的哪个位置开始赋值
  * 参数三：目标数组
  * 参数四：从目标数组的哪个位置开始赋值
  * 参数五：赋值几个

```java
public class SystemDemo {
    public static void main(String[] args) {
        //System.exit(0); // 0代表正常终止!!
        long startTime = System.currentTimeMillis();//定义sdf 按照格式输出
        for(int i = 0; i < 10000; i++){输出i}
		long endTime = new Date().getTime();
		System.out.println( (endTime - startTime)/1000.0 +"s");//程序用时

        int[] arr1 = new int[]{10 ,20 ,30 ,40 ,50 ,60 ,70};
        int[] arr2 = new int[6]; // [ 0 , 0 , 0 , 0 , 0 , 0]
        // 变成arrs2 = [0 , 30 , 40 , 50 , 0 , 0 ]
        System.arraycopy(arr1, 2, arr2, 1, 3);
    }
}
```

***

#### Date

构造器：

* `public Date()`：创建当前系统的此刻日期时间对象。
* `public Date(long time)`：把时间毫秒值转换成日期对象

方法：

* `public long getTime()`：返回自 1970 年 1 月 1 日 00:00:00 GMT 以来总的毫秒数。

时间记录的两种方式：

1. Date 日期对象
2. 时间毫秒值：从 `1970-01-01 00:00:00` 开始走到此刻的总的毫秒值，1s = 1000ms

```java
public class DateDemo {
    public static void main(String[] args) {
        Date d = new Date();
        System.out.println(d);//Fri Oct 16 21:58:44 CST 2020
        long time = d.getTime() + 121*1000;//过121s是什么时间
        System.out.println(time);//1602856875485
        
        Date d1 = new Date(time);
        System.out.println(d1);//Fri Oct 16 22:01:15 CST 2020
    }
}
```

```java
public static void main(String[] args){
    Date d = new Date();
    long startTime = d.getTime();
    for(int i = 0; i < 10000; i++){输出i}
    long endTime = new Date().getTime();
    System.out.println( (endTime - startTime) / 1000.0 +"s");
    //运行一万次输出需要多长时间
}
```

***

#### DateFormat

DateFormat 作用：

1. 可以把“日期对象”或者“时间毫秒值”格式化成我们喜欢的时间形式（格式化时间）
2. 可以把字符串的时间形式解析成日期对象（解析字符串时间）

DateFormat 是一个抽象类，不能直接使用，使用它的子类：SimpleDateFormat

SimpleDateFormat  简单日期格式化类：

* `public SimpleDateFormat(String pattern)`：指定时间的格式创建简单日期对象
* `public String format(Date date) `：把日期对象格式化成我们喜欢的时间形式，返回字符串
* `public String format(Object time)`：把时间毫秒值格式化成设定的时间形式，返回字符串!
* `public Date parse(String date)`：把字符串的时间解析成日期对象

>yyyy年MM月dd日 HH:mm:ss EEE a" 周几 上午下午

```java
public static void main(String[] args){
	Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss);
    String time = sdf.format(date);
    System.out.println(time);//2020-10-18 19:58:34
    //过121s后是什么时间
    long time = date.getTime();
    time+=121;
    System.out.println(sdf.formate(time));
    String d = "2020-10-18 20:20:20";//格式一致
    Date newDate = sdf.parse(d);
    System.out.println(sdf.format(newDate)); //按照前面的方法输出
}
```

***

#### Calendar

Calendar 代表了系统此刻日期对应的日历对象，是一个抽象类，不能直接创建对象

Calendar 日历类创建日历对象：`Calendar rightNow = Calendar.getInstance()`（**饿汉单例模式**）

Calendar 的方法：

* `public static Calendar getInstance()`：返回一个日历类的对象
* `public int get(int field)`：取日期中的某个字段信息
* `public void set(int field,int value)`：修改日历的某个字段信息
* `public void add(int field,int amount)`：为某个字段增加/减少指定的值
* `public final Date getTime()`：拿到此刻日期对象
* `public long getTimeInMillis()`：拿到此刻时间毫秒值

```java
public static void main(String[] args){
	Calendar rightNow = Calendar.getInsance(); 
	int year = rightNow.get(Calendar.YEAR);//获取年
    int month = rightNow.get(Calendar.MONTH) + 1;//月要+1
    int days = rightNow.get(Calendar.DAY_OF_YEAR);
    rightNow.set(Calendar.YEAR , 2099);//修改某个字段
    rightNow.add(Calendar.HOUR , 15);//加15小时  -15就是减去15小时
    Date date = rightNow.getTime();//日历对象
    long time = rightNow.getTimeInMillis();//时间毫秒值
    //700天后是什么日子
    rightNow.add(Calendar.DAY_OF_YEAR , 701);
    Date date d = rightNow.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(sdf.format(d));//输出700天后的日期
}
```

***

#### LocalDateTime

JDK1.8 新增，线程安全

+ LocalDate       表示日期（年月日）  
+ LocalTime       表示时间（时分秒）
+ LocalDateTime    表示时间+ 日期 （年月日时分秒）

构造方法：

* public static LocalDateTime now()：获取当前系统时间 
* public static LocalDateTime of(年, 月 , 日, 时, 分, 秒)：使用指定年月日和时分秒初始化一个对象

常用API：

| 方法名                                                    | 说明                                                         |
| --------------------------------------------------------- | ------------------------------------------------------------ |
| public int getYear()                                      | 获取年                                                       |
| public int getMonthValue()                                | 获取月份（1-12）                                             |
| public int getDayOfMonth()                                | 获取月份中的第几天（1-31）                                   |
| public int getDayOfYear()                                 | 获取一年中的第几天（1-366）                                  |
| public DayOfWeek getDayOfWeek()                           | 获取星期                                                     |
| public int getMinute()                                    | 获取分钟                                                     |
| public int getHour()                                      | 获取小时                                                     |
| public LocalDate  toLocalDate()                           | 转换成为一个 LocalDate 对象（年月日）                        |
| public LocalTime toLocalTime()                            | 转换成为一个 LocalTime 对象（时分秒）                        |
| public String format(指定格式)                            | 把一个 LocalDateTime 格式化成为一个字符串                    |
| public LocalDateTime parse(准备解析的字符串, 解析格式)    | 把一个日期字符串解析成为一个 LocalDateTime 对象              |
| public static DateTimeFormatter ofPattern(String pattern) | 使用指定的日期模板获取一个日期格式化器 DateTimeFormatter 对象 |

```java
public class JDK8DateDemo2 {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        LocalDateTime localDateTime = LocalDateTime.of(2020, 11, 11, 11, 11, 11);
        System.out.println(localDateTime);
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String s = localDateTime.format(pattern);
		LocalDateTime parse = LocalDateTime.parse(s, pattern);
    }
}
```

| 方法名                                      | 说明           |
| ------------------------------------------- | -------------- |
| public LocalDateTime plusYears (long years) | 添加或者减去年 |
| public LocalDateTime withYear(int year)     | 直接修改年     |



**时间间隔** Duration 类API：

| 方法名                                           | 说明                 |
| ------------------------------------------------ | -------------------- |
| public static Period between(开始时间,结束时间)  | 计算两个“时间"的间隔 |
| public int getYears()                            | 获得这段时间的年数   |
| public int getMonths()                           | 获得此期间的总月数   |
| public int getDays()                             | 获得此期间的天数     |
| public long toTotalMonths()                      | 获取此期间的总月数   |
| public static Durationbetween(开始时间,结束时间) | 计算两个“时间"的间隔 |
| public long toSeconds()                          | 获得此时间间隔的秒   |
| public long toMillis()                           | 获得此时间间隔的毫秒 |
| public long toNanos()                            | 获得此时间间隔的纳秒 |

```java
public class JDK8DateDemo9 {
    public static void main(String[] args) {
        LocalDate localDate1 = LocalDate.of(2020, 1, 1);
        LocalDate localDate2 = LocalDate.of(2048, 12, 12);
        Period period = Period.between(localDate1, localDate2);
        System.out.println(period);//P28Y11M11D
		Duration duration = Duration.between(localDateTime1, localDateTime2);
        System.out.println(duration);//PT21H57M58S
    }
}
```

***

#### Number

- 包装类

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/7e4f50eb-2b53-477d-82dc-877b460f56a4.png)

- 方法

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/8bd008d3-79ee-494f-af8d-4eabc7cf3bbf.png)

#### Math

Math 用于做数学运算

Math 类中的方法全部是静态方法，直接用类名调用即可：

| 方法                                         | 说明                              |
| -------------------------------------------- | --------------------------------- |
| public static int abs(int a)                 | 获取参数a的绝对值                 |
| public static double ceil(double a)          | 向上取整                          |
| public static double floor(double a)         | 向下取整                          |
| public static double pow(double a, double b) | 获取 a 的 b 次幂                  |
| public static long round(double a)           | 四舍五入取整                      |
| public static int max(int a,int b)           | 返回较大值                        |
| public static int min(int a,int b)           | 返回较小值                        |
| public static double random()                | 返回值为 double 的正值，[0.0,1.0) |

```java
public class MathDemo {
    public static void main(String[] args) {
        // 1.取绝对值:返回正数。
        System.out.println(Math.abs(10));
        System.out.println(Math.abs(-10.3));
        // 2.向上取整: 5
        System.out.println(Math.ceil(4.00000001)); // 5.0
        System.out.println(Math.ceil(-4.00000001));//4.0
        // 3.向下取整：4
        System.out.println(Math.floor(4.99999999)); // 4.0
        System.out.println(Math.floor(-4.99999999)); // 5.0
        // 4.求指数次方
        System.out.println(Math.pow(2 , 3)); // 2^3 = 8.0
        // 5.四舍五入 10
        System.out.println(Math.round(4.49999)); // 4
        System.out.println(Math.round(4.500001)); // 5
        System.out.println(Math.round(5.5));//6
    }
}
```

***

#### DecimalFormat

使任何形式的数字解析和格式化

```java
public static void main(String[]args){
    double pi = 3.1415927;　//圆周率
    //取一位整数
    System.out.println(new DecimalFormat("0").format(pi));　　　//3
    //取一位整数和两位小数
    System.out.println(new DecimalFormat("0.00").format(pi));　//3.14
    //取两位整数和三位小数，整数不足部分以0填补。
    System.out.println(new DecimalFormat("00.000").format(pi));// 03.142
    //取所有整数部分
    System.out.println(new DecimalFormat("#").format(pi));　　　//3
    //以百分比方式计数，并取两位小数
    System.out.println(new DecimalFormat("#.##%").format(pi));　//314.16%

    long c =299792458;　　//光速
    //显示为科学计数法，并取五位小数
    System.out.println(new DecimalFormat("#.#####E0").format(c));//2.99792E8
    //显示为两位整数的科学计数法，并取四位小数
    System.out.println(new DecimalFormat("00.####E0").format(c));//29.9792E7
    //每三位以逗号进行分隔。
    System.out.println(new DecimalFormat(",###").format(c));//299,792,458
    //将格式嵌入文本
    System.out.println(new DecimalFormat("光速大小为每秒,###米。").format(c));

}
```

***

#### BigDecimal

Java 在 java.math 包中提供的 API 类，用来对超过16位有效位的数进行精确的运算

构造方法：

* `public static BigDecimal valueOf(double val)`：包装浮点数成为大数据对象。
* `public BigDecimal(double val)`
* `public BigDecimal(String val)`

常用API：

* `public BigDecimal add(BigDecimal value)`：加法运算
* `public BigDecimal subtract(BigDecimal value)`：减法运算 
* `public BigDecimal multiply(BigDecimal value)`：乘法运算 
* `public BigDecimal divide(BigDecimal value)`：除法运算
* `public double doubleValue()`：把 BigDecimal 转换成 double 类型
* `public int intValue()`：转为 int 其他类型相同
* `public BigDecimal divide (BigDecimal value，精确几位，舍入模式)`：除法

```java
public class BigDecimalDemo {
    public static void main(String[] args) {
        // 浮点型运算的时候直接+ - * / 可能会出现数据失真（精度问题）。
        System.out.println(0.1 + 0.2);
        System.out.println(1.301 / 100);
        
        double a = 0.1 ;
        double b = 0.2 ;
        double c = a + b ;
        System.out.println(c);//0.30000000000000004
        
        // 1.把浮点数转换成大数据对象运算
        BigDecimal a1 = BigDecimal.valueOf(a);
        BigDecimal b1 = BigDecimal.valueOf(b);
        BigDecimal c1 = a1.add(b1);//a1.divide(b1);也可以
		System.out.println(c1);

        // BigDecimal只是解决精度问题的手段，double数据才是我们的目的！！
        double d = c1.doubleValue();
    }
}
```

总结：

1. BigDecimal 是用来进行精确计算的
2. 创建 BigDecimal 的对象，构造方法使用参数类型为字符串的
3. 四则运算中的除法，如果除不尽请使用 divide 的三个参数的方法

```java
BigDecimal divide = bd1.divide(参与运算的对象,小数点后精确到多少位,舍入模式);
//参数1：表示参与运算的BigDecimal 对象。
//参数2：表示小数点后面精确到多少位
//参数3：舍入模式  
// BigDecimal.ROUND_UP  进一法
// BigDecimal.ROUND_FLOOR 去尾法
// BigDecimal.ROUND_HALF_UP 四舍五入
```

#### Collection

**概述**

Java 中集合的代表是 Collection，Collection 集合是 Java 中集合的祖宗类

Collection 集合底层为数组：`[value1, value2, ....]`

```java
Collection集合的体系:
                      Collection<E>(接口)
                 /                         \
          Set<E>(接口)                    List<E>(接口)
      /               \                  /             \
 HashSet<E>(实现类) TreeSet<>(实现类)  ArrayList<E>(实现类)  LinekdList<>(实现类)
 /
LinkedHashSet<>(实现类)
```

**集合的特点：**

* Set 系列集合：添加的元素是无序，不重复，无索引的
  * HashSet：添加的元素是无序，不重复，无索引的
  * LinkedHashSet：添加的元素是有序，不重复，无索引的
  * TreeSet：不重复，无索引，按照大小默认升序排序
* List 系列集合：添加的元素是有序，可重复，有索引
  * ArrayList：添加的元素是有序，可重复，有索引
  * LinekdList：添加的元素是有序，可重复，有索引

**API**

Collection 是集合的祖宗类，它的功能是全部集合都可以继承使用的，所以要学习它。

Collection 子类的构造器都有可以包装其他子类的构造方法，如：

* `public ArrayList(Collection<? extends E> c)`：构造新集合，元素按照由集合的迭代器返回的顺序

* `public HashSet(Collection<? extends E> c)`：构造一个包含指定集合中的元素的新集合

Collection API 如下：

* `public boolean add(E e)`：把给定的对象添加到当前集合中 。
* `public void clear()`：清空集合中所有的元素。
* `public boolean remove(E e)`：把给定的对象在当前集合中删除。
* `public boolean contains(Object obj)`：判断当前集合中是否包含给定的对象。
* `public boolean isEmpty()`：判断当前集合是否为空。
* `public int size()`：返回集合中元素的个数。
* `public Object[] toArray()`：把集合中的元素，存储到数组中
* `public boolean addAll(Collection<? extends E> c)`：将指定集合中的所有元素添加到此集合

```java
public class CollectionDemo {
    public static void main(String[] args) {
        Collection<String> sets = new HashSet<>();
        sets.add("MyBatis");
        System.out.println(sets.add("Java"));//true
        System.out.println(sets.add("Java"));//false
        sets.add("Spring");
        sets.add("MySQL");
        System.out.println(sets)//[]无序的;
        System.out.println(sets.contains("java"));//true 存在
        Object[] arrs = sets.toArray();
        System.out.println("数组："+ Arrays.toString(arrs));
        
        Collection<String> c1 = new ArrayList<>();
        c1.add("java");
        Collection<String> c2 = new ArrayList<>();
        c2.add("ee");
        c1.addAll(c2);// c1:[java,ee]  c2:[ee];
    }
}
```

***

**遍历**

Collection 集合的遍历方式有三种:

集合可以直接输出内容，因为底层重写了 toString() 方法

1. 迭代器

   * `public Iterator iterator()`：获取集合对应的迭代器，用来遍历集合中的元素的
   * `E next()`：获取下一个元素值
   * `boolean hasNext()`：判断是否有下一个元素，有返回 true ，反之返回 false
   * `default void remove()`：从底层集合中删除此迭代器返回的最后一个元素，这种方法只能在每次调用 next() 时调用一次

2. 增强 for 循环：可以遍历集合或者数组，遍历集合实际上是迭代器遍历的简化写法

   ```java
   for(被遍历集合或者数组中元素的类型 变量名称 : 被遍历集合或者数组){
   
   }
   ```

   缺点：遍历无法知道遍历到了哪个元素了，因为没有索引

3. JDK 1.8 开始之后的新技术 Lambda 表达式

   ```java
   public class CollectionDemo {
       public static void main(String[] args) {
           Collection<String> lists = new ArrayList<>();
           lists.add("aa");
           lists.add("bb");
           lists.add("cc");
           System.out.println(lists); // lists = [aa, bb, cc]
   		//迭代器流程
           // 1.得到集合的迭代器对象。
           Iterator<String> it = lists.iterator();
           // 2.使用while循环遍历。
           while(it.hasNext()){
               String ele = it.next();
               System.out.println(ele);
           }
           
   		//增强for
           for (String ele : lists) {
               System.out.println(ele);
           }
           //lambda表达式
           lists.forEach(s -> {
               System.out.println(s);
           });
       }
   }
   ```

   

***

**List**

**概述**

List 集合继承了 Collection 集合全部的功能。

List 系列集合有索引，所以多了很多按照索引操作元素的功能：for 循环遍历（4 种遍历）

List 系列集合：

* ArrayList：添加的元素是有序，可重复，有索引

* LinekdList：添加的元素是有序，可重复，有索引


***

**ArrayList**

**介绍**

ArrayList 添加的元素，是有序，可重复，有索引的

* `public boolean add(E e)`：将指定的元素追加到此集合的末尾
* `public void add(int index, E element)`：将指定的元素，添加到该集合中的指定位置上
* `public E get(int index)`：返回集合中指定位置的元素
* `public E remove(int index)`：移除列表中指定位置的元素，返回的是被移除的元素
* `public E set(int index, E element)`：用指定元素替换集合中指定位置的元素，返回更新前的元素值
* `int indexOf(Object o)`：返回列表中指定元素第一次出现的索引，如果不包含此元素，则返回 -1

```java
public static void main(String[] args){
    List<String> lists = new ArrayList<>();//多态
    lists.add("java1");
    lists.add("java1");//可以重复
    lists.add("java2");
    for(int i = 0 ; i < lists.size() ; i++ ) {
            String ele = lists.get(i);
            System.out.println(ele);
   }
}
```

***

**源码**

ArrayList 实现类集合底层**基于数组存储数据**的，查询快，增删慢，支持快速随机访问

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{}
```

- `RandomAccess` 是一个标志接口，表明实现这个这个接口的 List 集合是支持**快速随机访问**的。在 `ArrayList` 中，我们即可以通过元素的序号快速获取元素对象，这就是快速随机访问。
- `ArrayList` 实现了 `Cloneable` 接口 ，即覆盖了函数 `clone()`，能被克隆
- `ArrayList` 实现了 `Serializable ` 接口，这意味着 `ArrayList` 支持序列化，能通过序列化去传输

核心方法：

* 构造函数：以无参数构造方法创建 ArrayList 时，实际上初始化赋值的是一个空数组。当真正对数组进行添加元素操作时，才真正分配容量（惰性初始化），即向数组中添加第一个元素时，**数组容量扩为 10**

* 添加元素：

  ```java
  // e 插入的元素  elementData底层数组   size 插入的位置
  public boolean add(E e) {
      ensureCapacityInternal(size + 1);	// Increments modCount!!
      elementData[size++] = e;			// 插入size位置，然后加一
      return true;
  }
  ```

  当 add 第 1 个元素到 ArrayList，size 是 0，进入 ensureCapacityInternal 方法，

  ```java
  private void ensureCapacityInternal(int minCapacity) {
      ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
  }
  ```

  ```java
  private static int calculateCapacity(Object[] elementData, int minCapacity) {
      // 判断elementData是不是空数组
      if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
          // 返回默认值和最小需求容量最大的一个
          return Math.max(DEFAULT_CAPACITY, minCapacity);
      }
      return minCapacity;
  }
  ```

  如果需要的容量大于数组长度，进行扩容：

  ```java
  // 判断是否需要扩容
  private void ensureExplicitCapacity(int minCapacity) {
      modCount++;
      // 索引越界
      if (minCapacity - elementData.length > 0)
          // 调用grow方法进行扩容，调用此方法代表已经开始扩容了
          grow(minCapacity);
  }
  ```

  指定索引插入，**在旧数组上操作**：

  ```java
  public void add(int index, E element) {
      rangeCheckForAdd(index);
      ensureCapacityInternal(size + 1);  // Increments modCount!!
      // 将指定索引后的数据后移
      System.arraycopy(elementData, index, elementData, index + 1, size - index);
      elementData[index] = element;
      size++;
  }
  ```

* 扩容：新容量的大小为 `oldCapacity + (oldCapacity >> 1)`，`oldCapacity >> 1` 需要取整，所以新容量大约是旧容量的 1.5 倍左右，即 oldCapacity+oldCapacity/2

  扩容操作需要调用 `Arrays.copyOf()`（底层 `System.arraycopy()`）把原数组整个复制到**新数组**中，这个操作代价很高，因此最好在创建 ArrayList 对象时就指定大概的容量大小，减少扩容操作的次数

  ```java
  private void grow(int minCapacity) {
      int oldCapacity = elementData.length;
      int newCapacity = oldCapacity + (oldCapacity >> 1);
      //检查新容量是否大于最小需要容量，若小于最小需要容量，就把最小需要容量当作数组的新容量
      if (newCapacity - minCapacity < 0)
  		newCapacity = minCapacity;//不需要扩容计算
      //检查新容量是否大于最大数组容量
      if (newCapacity - MAX_ARRAY_SIZE > 0)
          //如果minCapacity大于最大容量，则新容量则为`Integer.MAX_VALUE`
          //否则，新容量大小则为 MAX_ARRAY_SIZE 即为 `Integer.MAX_VALUE - 8`
          newCapacity = hugeCapacity(minCapacity);
      elementData = Arrays.copyOf(elementData, newCapacity);
  }
  ```

  MAX_ARRAY_SIZE：要分配的数组的最大大小，分配更大的**可能**会导致

  * OutOfMemoryError:Requested array size exceeds VM limit（请求的数组大小超出 VM 限制）
  * OutOfMemoryError: Java heap space（堆区内存不足，可以通过设置 JVM 参数 -Xmx 来调节）

* 删除元素：需要调用 System.arraycopy() 将 index+1 后面的元素都复制到 index 位置上，在旧数组上操作，该操作的时间复杂度为 O(N)，可以看到 ArrayList 删除元素的代价是非常高的

  ```java
  public E remove(int index) {
      rangeCheck(index);
      modCount++;
      E oldValue = elementData(index);
  
      int numMoved = size - index - 1;
      if (numMoved > 0)
          System.arraycopy(elementData, index+1, elementData, index, numMoved);
      elementData[--size] = null; // clear to let GC do its work
  
      return oldValue;
  }
  ```

* 序列化：ArrayList 基于数组并且具有动态扩容特性，因此保存元素的数组不一定都会被使用，就没必要全部进行序列化。保存元素的数组 elementData 使用 transient 修饰，该关键字声明数组默认不会被序列化

  ```java
   transient Object[] elementData;
  ```

* ensureCapacity：增加此实例的容量，以确保它至少可以容纳最小容量参数指定的元素数，减少增量重新分配的次数

  ```java
   public void ensureCapacity(int minCapacity) {
       if (minCapacity > elementData.length
           && !(elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
                && minCapacity <= DEFAULT_CAPACITY)) {
           modCount++;
           grow(minCapacity);
       }
   }
  ```

* **Fail-Fast**：快速失败，modCount 用来记录 ArrayList **结构发生变化**的次数，结构发生变化是指添加或者删除至少一个元素的操作，或者是调整内部数组的大小，仅仅只是设置元素的值不算结构发生变化

  在进行序列化或者迭代等操作时，需要比较操作前后 modCount 是否改变，改变了抛出 ConcurrentModificationException 异常

  ```java
  public Iterator<E> iterator() {
      return new Itr();
  }
  ```

  ```java
  private class Itr implements Iterator<E> {
      int cursor;       // index of next element to return
      int lastRet = -1; // index of last element returned; -1 if no such
      int expectedModCount = modCount;
  
      Itr() {}
  
      public boolean hasNext() {
          return cursor != size;
      }
  
     	// 获取下一个元素时首先判断结构是否发生变化
      public E next() {
          checkForComodification();
         	// .....
      }
      // modCount 被其他线程改变抛出并发修改异常
      final void checkForComodification() {
          if (modCount != expectedModCount)
              throw new ConcurrentModificationException();
      }
  	// 【允许删除操作】
      public void remove() {
          // ...
          checkForComodification();
          // ...
          // 删除后重置 expectedModCount
          expectedModCount = modCount;
      }
  }
  ```

  

***

**Vector**

同步：Vector 的实现与 ArrayList 类似，但是方法上使用了 synchronized 进行同步

构造：默认长度为 10 的数组

扩容：Vector 的构造函数可以传入 capacityIncrement 参数，作用是在扩容时使容量 capacity 增长 capacityIncrement，如果这个参数的值小于等于 0（默认0），扩容时每次都令 capacity 为原来的两倍

对比 ArrayList

1. Vector 是同步的，开销比 ArrayList 要大，访问速度更慢。最好使用 ArrayList 而不是 Vector，因为同步操作完全可以由程序来控制

2. Vector 每次扩容请求其大小的 2 倍（也可以通过构造函数设置增长的容量），而 ArrayList 是 1.5 倍

3. 底层都是 `Object[]` 数组存储

***

**LinkedList**

**介绍**

LinkedList 也是 List 的实现类：基于**双向链表**实现，使用 Node 存储链表节点信息，增删比较快，查询慢

LinkedList 除了拥有 List 集合的全部功能还多了很多操作首尾元素的特殊功能：

* `public boolean add(E e)`：将指定元素添加到此列表的结尾
* `public E poll()`：检索并删除此列表的头（第一个元素）
* `public void addFirst(E e)`：将指定元素插入此列表的开头
* `public void addLast(E e)`：将指定元素添加到此列表的结尾
* `public E pop()`：从此列表所表示的堆栈处弹出一个元素
* `public void push(E e)`：将元素推入此列表所表示的堆栈
* `public int indexOf(Object o)`：返回此列表中指定元素的第一次出现的索引，如果不包含返回 -1
* `public int lastIndexOf(Object o)`：从尾遍历找
* ` public boolean remove(Object o)`：一次只删除一个匹配的对象，如果删除了匹配对象返回 true
* `public E remove(int index)`：删除指定位置的元素

```java
public class ListDemo {
    public static void main(String[] args) {
        // 1.用LinkedList做一个队列:先进先出，后进后出。
        LinkedList<String> queue = new LinkedList<>();
        // 入队
        queue.addLast("1号");
        queue.addLast("2号");
        queue.addLast("3号");
        System.out.println(queue); // [1号, 2号, 3号]
        // 出队
        System.out.println(queue.removeFirst());//1号
        System.out.println(queue.removeFirst());//2号
        System.out.println(queue);//[3号]

        // 做一个栈 先进后出
        LinkedList<String> stack = new LinkedList<>();
        // 压栈
        stack.push("第1颗子弹");//addFirst(e);
        stack.push("第2颗子弹");
        stack.push("第3颗子弹");
        System.out.println(stack); // [ 第3颗子弹, 第2颗子弹, 第1颗子弹]
        // 弹栈
        System.out.println(stack.pop());//removeFirst(); 第3颗子弹
        System.out.println(stack.pop());
        System.out.println(stack);// [第1颗子弹]
    }
}
```

***

**源码**

LinkedList 是一个实现了 List 接口的**双端链表**，支持高效的插入和删除操作，另外也实现了 Deque 接口，使得 LinkedList 类也具有队列的特性

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/LinkedList底层结构.png)

核心方法：

* 使 LinkedList 变成线程安全的，可以调用静态类 Collections 类中的 synchronizedList 方法：

  ```java
  List list = Collections.synchronizedList(new LinkedList(...));
  ```

* 私有内部类 Node：这个类代表双端链表的节点 Node

  ```java
  private static class Node<E> {
      E item;
      Node<E> next;
      Node<E> prev;
  
      Node(Node<E> prev, E element, Node<E> next) {
          this.item = element;
          this.next = next;
          this.prev = prev;
      }
  }
  ```

* 构造方法：只有无参构造和用已有的集合创建链表的构造方法

* 添加元素：默认加到尾部

  ```java
  public boolean add(E e) {
      linkLast(e);
      return true;
  }
  ```

* 获取元素：`get(int index)` 根据指定索引返回数据

  * 获取头节点 (index=0)：`getFirst()、element()、peek()、peekFirst()` 这四个获取头结点方法的区别在于对链表为空时的处理方式，是抛出异常还是返回NULL，其中 `getFirst() element()` 方法将会在链表为空时，抛出异常
  * 获取尾节点 (index=-1)：getLast() 方法在链表为空时，抛出 NoSuchElementException，而 peekLast() 不会，只会返回 null

* 删除元素：

  * remove()、removeFirst()、pop()：删除头节点
  * removeLast()、pollLast()：删除尾节点，removeLast()在链表为空时抛出NoSuchElementException，而pollLast()方法返回null

对比 ArrayList

1. 是否保证线程安全：ArrayList 和 LinkedList 都是不同步的，也就是不保证线程安全
2. 底层数据结构： 
   * Arraylist 底层使用的是 `Object` 数组
   * LinkedList 底层使用的是双向链表数据结构（JDK1.6 之前为循环链表，JDK1.7 取消了循环）
3. 插入和删除是否受元素位置的影响：
   * ArrayList 采用数组存储，所以插入和删除元素受元素位置的影响
   * LinkedList采 用链表存储，所以对于`add(E e)`方法的插入，删除元素不受元素位置的影响
4. 是否支持快速随机访问：
   * LinkedList 不支持高效的随机元素访问，ArrayList 支持
   * 快速随机访问就是通过元素的序号快速获取元素对象(对应于 `get(int index)` 方法)
5. 内存空间占用：
   * ArrayList 的空间浪费主要体现在在 list 列表的结尾会预留一定的容量空间
   * LinkedList 的空间花费则体现在它的每一个元素都需要消耗比 ArrayList更多的空间（因为要存放直接后继和直接前驱以及数据）

***

Set

**概述**

Set 系列集合：

* HashSet：添加的元素是无序，不重复，无索引的
* LinkedHashSet：添加的元素是有序，不重复，无索引的
* TreeSet：不重复，无索引，按照大小默认升序排序

**注意**：没有索引，不能使用普通 for 循环遍历

***

**HashSet**

哈希值：

- 哈希值：JDK 根据对象的地址或者字符串或者数字计算出来的数值

- 获取哈希值：Object 类中的 public int hashCode()

- 哈希值的特点

  - 同一个对象多次调用 hashCode() 方法返回的哈希值是相同的
  - 默认情况下，不同对象的哈希值是不同的，而重写 hashCode() 方法，可以实现让不同对象的哈希值相同

**HashSet 底层就是基于 HashMap 实现，值是  PRESENT = new Object()**

Set 集合添加的元素是无序，不重复的。

* 是如何去重复的？

  ```java
  1.对于有值特性的，Set集合可以直接判断进行去重复。
  2.对于引用数据类型的类对象，Set集合是按照如下流程进行是否重复的判断。
      Set集合会让两两对象，先调用自己的hashCode()方法得到彼此的哈希值（所谓的内存地址）
      然后比较两个对象的哈希值是否相同，如果不相同则直接认为两个对象不重复。
      如果哈希值相同，会继续让两个对象进行equals比较内容是否相同，如果相同认为真的重复了
      如果不相同认为不重复。
  
              Set集合会先让对象调用hashCode()方法获取两个对象的哈希值比较
                 /                     \
              false                    true
              /                          \
          不重复                        继续让两个对象进行equals比较
                                         /          \
                                       false        true
                                        /             \
                                      不重复          重复了
  ```

* Set 系列集合元素无序的根本原因

  Set 系列集合添加元素无序的根本原因是因为**底层采用了哈希表存储元素**。

  * JDK 1.8 之前：哈希表 = 数组（初始容量16) + 链表  + （哈希算法）
  * JDK 1.8 之后：哈希表 = 数组（初始容量16) + 链表 + 红黑树  + （哈希算法）
    * 当链表长度超过阈值 8 且当前数组的长度 > 64时，将链表转换为红黑树，减少了查找时间
    * 当链表长度超过阈值 8 且当前数组的长度 < 64时，扩容

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/HashSet底层结构哈希表.png)

  每个元素的 hashcode() 的值进行响应的算法运算，计算出的值相同的存入一个数组块中，以链表的形式存储，如果链表长度超过8就采取红黑树存储，所以输出的元素是无序的。

* 如何设置只要对象内容一样，就希望集合认为重复：**重写 hashCode 和 equals 方法**

***

**Linked**

LinkedHashSet 为什么是有序的？

LinkedHashSet 底层依然是使用哈希表存储元素的，但是每个元素都额外带一个链来维护添加顺序，不光增删查快，还有顺序，缺点是多了一个存储顺序的链会**占内存空间**，而且不允许重复，无索引

***

**TreeSet**

TreeSet 集合自排序的方式：

1. 有值特性的元素直接可以升序排序（浮点型，整型）
2. 字符串类型的元素会按照首字符的编号排序
3. 对于自定义的引用数据类型，TreeSet 默认无法排序，执行的时候报错，因为不知道排序规则

自定义的引用数据类型，TreeSet 默认无法排序，需要定制排序的规则，方案有 2 种：

   * 直接为**对象的类**实现比较器规则接口 Comparable，重写比较方法：

     方法：`public int compareTo(Employee o): this 是比较者, o 是被比较者`

        * 比较者大于被比较者，返回正数
        * 比较者小于被比较者，返回负数
        * 比较者等于被比较者，返回 0

   * 直接为**集合**设置比较器 Comparator 对象，重写比较方法：

     方法：`public int compare(Employee o1, Employee o2): o1 比较者, o2 被比较者`

     * 比较者大于被比较者，返回正数
     * 比较者小于被比较者，返回负数
     * 比较者等于被比较者，返回 0

注意：如果类和集合都带有比较规则，优先使用集合自带的比较规则

```java
public class TreeSetDemo{
    public static void main(String[] args){
        Set<Student> students = new TreeSet<>();
		Collections.add(students,s1,s2,s3);
        System.out.println(students);//按照年龄比较 升序
        
        Set<Student> s = new TreeSet<>(new Comparator<Student>(){
            @Override
            public int compare(Student o1, Student o2) {
                // o1比较者   o2被比较者
                return o2.getAge() - o1.getAge();//降序
            }
        });
    }
}

public class Student implements Comparable<Student>{
    private String name;
    private int age;
    // 重写了比较方法。
    // e1.compareTo(o)
    // 比较者：this
    // 被比较者：o
    // 需求：按照年龄比较 升序，年龄相同按照姓名
    @Override
    public int compareTo(Student o) {
        int result = this.age - o.age;
        return result == 0 ? this.getName().compareTo(o.getName):result;
    }
}
```

比较器原理：底层是以第一个元素为基准，加一个新元素，就会和第一个元素比，如果大于，就继续和大于的元素进行比较，直到遇到比新元素大的元素为止，放在该位置的左边（红黑树）




***



#### Queue

Queue：队列，先进先出的特性

PriorityQueue 是优先级队列，底层存储结构为 Object[]，默认实现为小顶堆，每次出队最小的元素

构造方法：

* `public PriorityQueue()`：构造默认长度为 11 的队列（数组）

* `public PriorityQueue(Comparator<? super E> comparator)`：利用比较器自定义堆排序的规则

  ```java
  Queue<Integer> pq = new PriorityQueue<>((v1, v2) -> v2 - v1);//实现大顶堆
  ```

常用 API：

* `public boolean offer(E e)`：将指定的元素插入到此优先级队列的**尾部**
* `public E poll() `：检索并删除此队列的**头元素**，如果此队列为空，则返回 null 
* `public E peek()`：检索但不删除此队列的头，如果此队列为空，则返回 null
* `public boolean remove(Object o)`：从该队列中删除指定元素（如果存在），删除元素 e 使用 o.equals(e) 比较，如果队列包含多个这样的元素，删除第一个



****



#### Collections

java.utils.Collections：集合**工具类**，Collections 并不属于集合，是用来操作集合的工具类

Collections 有几个常用的API：

* `public static <T> boolean addAll(Collection<? super T> c, T... e)`：给集合对象批量添加元素
* `public static void shuffle(List<?> list)`：打乱集合顺序
* `public static <T> void sort(List<T> list)`：将集合中元素按照默认规则排序
* `public static <T> void sort(List<T> list,Comparator<? super T> )`：集合中元素按照指定规则排序
* `public static <T> List<T> synchronizedList(List<T> list)`：返回由指定 list 支持的线程安全 list
* `public static <T> Set<T> singleton(T o)`：返回一个只包含指定对象的不可变组

```java
public class CollectionsDemo {
    public static void main(String[] args) {
        Collection<String> names = new ArrayList<>();
        Collections.addAll(names,"张","王","李","赵");
        
        List<Double> scores = new ArrayList<>();
        Collections.addAll(scores, 98.5, 66.5 , 59.5 , 66.5 , 99.5 );
        Collections.shuffle(scores);
        Collections.sort(scores); // 默认升序排序！
        System.out.println(scores);
        
        List<Student> students = new ArrayList<>();
        Collections.addAll(students,s1,s2,s3,s4);
        Collections.sort(students,new Comparator<Student>(){
            
        })
    }
}

public class Student{
    private String name;
    private int age;
}
```

***

#### Map

**概述**

Collection 是单值集合体系，Map集合是一种双列集合，每个元素包含两个值。

Map集合的每个元素的格式：key=value（键值对元素），Map集合也被称为键值对集合

Map集合的完整格式：`{key1=value1, key2=value2, key3=value3, ...}`

```
Map集合的体系：
        Map<K , V>(接口,Map集合的祖宗类)
       /                      \
      TreeMap<K , V>           HashMap<K , V>(实现类,经典的，用的最多)
                                 \
                                  LinkedHashMap<K, V>(实现类)
```

Map 集合的特点：

1. Map 集合的特点都是由键决定的
2. Map 集合的键是无序，不重复的，无索引的（Set）
3. Map 集合的值无要求（List）
4. Map 集合的键值对都可以为 null
5. Map 集合后面重复的键对应元素会覆盖前面的元素

HashMap：元素按照键是无序，不重复，无索引，值不做要求

LinkedHashMap：元素按照键是有序，不重复，无索引，值不做要求

***

**常用**API

Map 集合的常用 API

* `public V put(K key, V value)`：把指定的键与值添加到 Map 集合中，**重复的键会覆盖前面的值元素**
* `public V remove(Object key)`：把指定的键对应的键值对元素在集合中删除，返回被删除元素的值
* `public V get(Object key)`：根据指定的键，在 Map 集合中获取对应的值
* `public Set<K> keySet()`：获取 Map 集合中所有的键，存储到 **Set 集合**中
* `public Collection<V> values()`：获取全部值的集合，存储到 **Collection 集合**
* `public Set<Map.Entry<K,V>> entrySet()`：获取Map集合中所有的键值对对象的集合
* `public boolean containsKey(Object key)`：判断该集合中是否有此键

```java
public class MapDemo {
    public static void main(String[] args) {
        Map<String , Integer> maps = new HashMap<>();
        maps.put(.....);
        System.out.println(maps.isEmpty());//false
        Integer value = maps.get("....");//返回键值对象
        Set<String> keys = maps.keySet();//获取Map集合中所有的键，
        //Map集合的键是无序不重复的，所以返回的是一个Set集合
        Collection<Integer> values = maps.values();
        //Map集合的值是不做要求的，可能重复，所以值要用Collection集合接收!
    }
}
```

***

**遍历方式**

Map集合的遍历方式有：3种。

1. “键找值”的方式遍历：先获取 Map 集合全部的键，再根据遍历键找值。
2. “键值对”的方式遍历：难度较大，采用增强 for 或者迭代器
3. JDK 1.8 开始之后的新技术：foreach，采用 Lambda 表达式

集合可以直接输出内容，因为底层重写了 toString() 方法

```java
public static void main(String[] args){
    Map<String , Integer> maps = new HashMap<>();
	//(1)键找值
    Set<String> keys = maps.keySet();
    for(String key : keys) {
        System.out.println(key + "=" + maps.get(key));
    }
    //Iterator<String> iterator = hm.keySet().iterator();
    
    //(2)键值对
    //(2.1)普通方式
    Set<Map.Entry<String,Integer>> entries = maps.entrySet();
    for (Map.Entry<String, Integer> entry : entries) {
             System.out.println(entry.getKey() + "=>" + entry.getValue());
    }
    //(2.2)迭代器方式
    Iterator<Map.Entry<String, Integer>> iterator = maps.entrySet().iterator();
    while (iterator.hasNext()) {
        Map.Entry<String, Integer> entry = iterator.next();
        System.out.println(entry.getKey() + "=" + entry.getValue());

    }
    //(3) Lamda
    maps.forEach((k,v) -> {
        System.out.println(k + "==>" + v);
    })
}
```

**HashMap**

**基本介绍**

HashMap 基于哈希表的 Map 接口实现，是以 key-value 存储形式存在，主要用来存放键值对

特点：

* HashMap 的实现不是同步的，这意味着它不是线程安全的
* key 是唯一不重复的，底层的哈希表结构，依赖 hashCode 方法和 equals 方法保证键的唯一
* key、value 都可以为null，但是 key 位置只能是一个null
* HashMap 中的映射不是有序的，即存取是无序的
* **key 要存储的是自定义对象，需要重写 hashCode 和 equals 方法，防止出现地址不同内容相同的 key**

JDK7 对比 JDK8：

* 7 = 数组 + 链表，8 = 数组 + 链表 + 红黑树
* 7 中是头插法，多线程容易造成环，8 中是尾插法
* 7 的扩容是全部数据重新定位，8 中是位置不变或者当前位置 + 旧 size 大小来实现
* 7 是先判断是否要扩容再插入，8 中是先插入再看是否要扩容

底层数据结构：

* 哈希表（Hash table，也叫散列表），根据关键码值而直接访问的数据结构。通过把关键码值映射到表中一个位置来访问记录，以加快查找的速度，这个映射函数叫做散列函数，存放记录的数组叫做散列表

* JDK1.8 之前 HashMap 由数组+链表组成

  * 数组是 HashMap 的主体
  * 链表则是为了解决哈希冲突而存在的（**拉链法解决冲突**），拉链法就是头插法，两个对象调用的 hashCode 方法计算的哈希码值（键的哈希）一致导致计算的数组索引值相同

* JDK1.8 以后 HashMap 由**数组+链表 +红黑树**数据结构组成

  * 解决哈希冲突时有了较大的变化
  * 当链表长度**超过（大于）阈值**（或者红黑树的边界值，默认为 8）并且当前数组的**长度大于等于 64 时**，此索引位置上的所有数据改为红黑树存储
  * 即使哈希函数取得再好，也很难达到元素百分百均匀分布。当 HashMap 中有大量的元素都存放到同一个桶中时，就相当于一个长的单链表，假如单链表有 n 个元素，遍历的**时间复杂度是 O(n)**，所以 JDK1.8 中引入了 红黑树（查找**时间复杂度为 O(logn)**）来优化这个问题，使得查找效率更高

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/HashMap底层结构.png)



参考视频：https://www.bilibili.com/video/BV1nJ411J7AA

***

继承关系

HashMap 继承关系如下图所示：

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/HashMap继承关系.bmp)

说明：

* Cloneable 空接口，表示可以克隆， 创建并返回 HashMap 对象的一个副本。
* Serializable 序列化接口，属于标记性接口，HashMap 对象可以被序列化和反序列化。
* AbstractMap 父类提供了 Map 实现接口，以最大限度地减少实现此接口所需的工作

***

**成员属性**

1. 序列化版本号

   ```java
   private static final long serialVersionUID = 362498820763181265L;
   ```

2. 集合的初始化容量（**必须是二的 n 次幂** ）

   ```java
   // 默认的初始容量是16 -- 1<<4相当于1*2的4次方---1*16
   static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
   ```

   HashMap 构造方法指定集合的初始化容量大小：

   ```java
   HashMap(int initialCapacity)// 构造一个带指定初始容量和默认加载因子 (0.75) 的空 HashMap
   ```

   * 为什么必须是 2 的 n 次幂？用位运算替代取余计算，减少 rehash 的代价（移动的节点少）

     HashMap 中添加元素时，需要根据 key 的 hash 值确定在数组中的具体位置。为了减少碰撞，把数据分配均匀，每个链表长度大致相同，实现该方法就是取模 `hash%length`，计算机中直接求余效率不如位移运算， **`hash % length == hash & (length-1)` 的前提是 length 是 2 的 n 次幂**

     散列平均分布：2 的 n 次方是 1 后面 n 个 0，2 的 n 次方 -1 是 n 个 1，可以**保证散列的均匀性**，减少碰撞

     ```java
     例如长度为8时候，3&(8-1)=3  2&(8-1)=2 ，不同位置上，不碰撞；
     例如长度为9时候，3&(9-1)=0  2&(9-1)=0 ，都在0上，碰撞了；
     ```

   * 如果输入值不是 2 的幂会怎么样？

     创建 HashMap 对象时，HashMap 通过位移运算和或运算得到的肯定是 2 的幂次数，并且是大于那个数的最近的数字，底层采用 tableSizeFor() 方法

3. 默认的负载因子，默认值是 0.75 

   ```java
   static final float DEFAULT_LOAD_FACTOR = 0.75f;
   ```

4. 集合最大容量 

   ```java
   // 集合最大容量的上限是：2的30次幂
   static final int MAXIMUM_CAPACITY = 1 << 30;// 0100 0000 0000 0000 0000 0000 0000 0000 = 2 ^ 30
   ```

5. 当链表的值超过 8 则会转红黑树（JDK1.8 新增）

   ```java
   // 当桶(bucket)上的结点数大于这个值时会转成红黑树
   static final int TREEIFY_THRESHOLD = 8;
   ```

   为什么 Map 桶中节点个数大于 8 才转为红黑树？

   * 在 HashMap 中有一段注释说明：**空间和时间的权衡**

     ```java
     TreeNodes占用空间大约是普通节点的两倍，所以我们只在箱子包含足够的节点时才使用树节点。当节点变少(由于删除或调整大小)时，就会被转换回普通的桶。在使用分布良好的用户hashcode时，很少使用树箱。理想情况下，在随机哈希码下，箱子中节点的频率服从"泊松分布"，默认调整阈值为0.75，平均参数约为0.5，尽管由于调整粒度的差异很大。忽略方差，列表大小k的预期出现次数是(exp(-0.5)*pow(0.5, k)/factorial(k))
     0:    0.60653066
     1:    0.30326533
     2:    0.07581633
     3:    0.01263606
     4:    0.00157952
     5:    0.00015795
     6:    0.00001316
     7:    0.00000094
     8:    0.00000006
     more: less than 1 in ten million
     一个bin中链表长度达到8个元素的概率为0.00000006，几乎是不可能事件，所以我们选择8这个数字
     ```

   * 其他说法
     红黑树的平均查找长度是 log(n)，如果长度为 8，平均查找长度为 log(8)=3，链表的平均查找长度为 n/2，当长度为 8 时，平均查找长度为 8/2=4，这才有转换成树的必要；链表长度如果是小于等于 6，6/2=3，而 log(6)=2.6，虽然速度也很快的，但转化为树结构和生成树的时间并不短

6. 当链表的值小于 6 则会从红黑树转回链表

   ```java
   // 当桶(bucket)上的结点数小于这个值时树转链表
   static final int UNTREEIFY_THRESHOLD = 6;
   ```

7. 当 Map 里面的数量**大于等于**这个阈值时，表中的桶才能进行树形化 ，否则桶内元素超过 8 时会扩容，而不是树形化。为了避免进行扩容、树形化选择的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD (8)

   ```java
   // 桶中结构转化为红黑树对应的数组长度最小的值 
   static final int MIN_TREEIFY_CAPACITY = 64;
   ```

   原因：数组比较小的情况下变为红黑树结构，反而会降低效率，红黑树需要进行左旋，右旋，变色这些操作来保持平衡

8. table 用来初始化（必须是二的 n 次幂）

   ```java
   // 存储元素的数组 
   transient Node<K,V>[] table;
   ```

 9. HashMap 中**存放元素的个数**

    ```java
    // 存放元素的个数，HashMap中K-V的实时数量，不是table数组的长度
    transient int size;
    ```

10. 记录 HashMap 的修改次数 

    ```java
    // 每次扩容和更改map结构的计数器
     transient int modCount;  
    ```

11. 调整大小下一个容量的值计算方式为：容量 * 负载因子，容量是数组的长度

     ```java
    // 临界值，当实际大小(容量*负载因子)超过临界值时，会进行扩容
    int threshold;
     ```

12. **哈希表的加载因子**

    ```java
     final float loadFactor;
    ```

    * 加载因子的概述

      loadFactor 加载因子，是用来衡量 HashMap 满的程度，表示 HashMap 的疏密程度，影响 hash 操作到同一个数组位置的概率，计算 HashMap 的实时加载因子的方法为 **size/capacity**，而不是占用桶的数量去除以 capacity，capacity 是桶的数量，也就是 table 的长度 length

      当 HashMap 容纳的元素已经达到数组长度的 75% 时，表示 HashMap 拥挤需要扩容，而扩容这个过程涉及到 rehash、复制数据等操作，非常消耗性能，所以开发中尽量减少扩容的次数，通过创建 HashMap 集合对象时指定初始容量来避免

      ```java
      HashMap(int initialCapacity, float loadFactor)//构造指定初始容量和加载因子的空HashMap
      ```

    * 为什么加载因子设置为 0.75，初始化临界值是 12？

      loadFactor 太大导致查找元素效率低，存放的数据拥挤，太小导致数组的利用率低，存放的数据会很分散。loadFactor 的默认值为 **0.75f 是官方给出的一个比较好的临界值**

    * threshold 计算公式：capacity（数组长度默认16） * loadFactor（默认 0.75）。当 size >= threshold 的时候，那么就要考虑对数组的 resize（扩容），这就是衡量数组是否需要扩增的一个标准， 扩容后的 HashMap 容量是之前容量的**两倍**

***

**构造方法**

* 构造一个空的 HashMap ，**默认初始容量（16）和默认负载因子（0.75）**

  ```java
  public HashMap() {
  	this.loadFactor = DEFAULT_LOAD_FACTOR; 
  	// 将默认的加载因子0.75赋值给loadFactor，并没有创建数组
  }
  ```

* 构造一个具有指定的初始容量和默认负载因子（0.75）HashMap

  ```java
  // 指定“容量大小”的构造函数
  public HashMap(int initialCapacity) {
      this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }
  ```

* 构造一个具有指定的初始容量和负载因子的 HashMap

  ```java
  public HashMap(int initialCapacity, float loadFactor) {
      // 进行判断
      // 将指定的加载因子赋值给HashMap成员变量的负载因子loadFactor
      this.loadFactor = loadFactor;
    	// 最后调用了tableSizeFor
      this.threshold = tableSizeFor(initialCapacity);
  }
  ```

  * 对于 `this.threshold = tableSizeFor(initialCapacity)` 

    JDK8 以后的构造方法中，并没有对 table 这个成员变量进行初始化，table 的初始化被推迟到了 put 方法中，在 put 方法中会对 threshold 重新计算

* 包含另一个 `Map` 的构造函数 

  ```java
  // 构造一个映射关系与指定 Map 相同的新 HashMap
  public HashMap(Map<? extends K, ? extends V> m) {
      // 负载因子loadFactor变为默认的负载因子0.75
      this.loadFactor = DEFAULT_LOAD_FACTOR;
      putMapEntries(m, false);
  }
  ```

  putMapEntries 源码分析：

  ```java
  final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
      //获取参数集合的长度
      int s = m.size();
      if (s > 0) {
          //判断参数集合的长度是否大于0
          if (table == null) {  // 判断table是否已经初始化
              // pre-size
              // 未初始化，s为m的实际元素个数
              float ft = ((float)s / loadFactor) + 1.0F;
              int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                       (int)ft : MAXIMUM_CAPACITY);
              // 计算得到的t大于阈值，则初始化阈值
              if (t > threshold)
                  threshold = tableSizeFor(t);
          }
          // 已初始化，并且m元素个数大于阈值，进行扩容处理
          else if (s > threshold)
              resize();
          // 将m中的所有元素添加至HashMap中
          for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
              K key = e.getKey();
              V value = e.getValue();
              putVal(hash(key), key, value, false, evict);
          }
      }
  }
  ```

  `float ft = ((float)s / loadFactor) + 1.0F` 这一行代码中为什么要加 1.0F ？

  s / loadFactor 的结果是小数，加 1.0F 相当于是对小数做一个向上取整以尽可能的保证更大容量，更大的容量能够减少 resize 的调用次数，这样可以减少数组的扩容

***

**成员方法**

* hash()：HashMap 是支持 Key 为空的；HashTable 是直接用 Key 来获取 HashCode，key 为空会抛异常

  * &（按位与运算）：相同的二进制数位上，都是 1 的时候，结果为 1，否则为零

  * ^（按位异或运算）：相同的二进制数位上，数字相同，结果为 0，不同为 1，**不进位加法**

    0 1 相互做 & | ^ 运算，结果出现 0 和 1 的数量分别是 3:1、1:3、1:1，所以异或是最平均的

  ```java
  static final int hash(Object key) {
      int h;
      // 1）如果key等于null：可以看到当key等于null的时候也是有哈希值的，返回的是0
      // 2）如果key不等于null：首先计算出key的hashCode赋值给h,然后与h无符号右移16位后的二进制进行按位异或
      return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }
  ```

  计算 hash 的方法：将 hashCode 无符号右移 16 位，高 16bit 和低 16bit 做异或，扰动运算

  原因：当数组长度很小，假设是 16，那么 n-1 即为 1111 ，这样的值和 hashCode() 直接做按位与操作，实际上只使用了哈希值的后 4 位。如果当哈希值的高位变化很大，低位变化很小，就很容易造成哈希冲突了，所以这里**把高低位都利用起来，让高16 位也参与运算**，从而解决了这个问题

  哈希冲突的处理方式：

  * 开放定址法：线性探查法（ThreadLocalMap 使用），平方探查法（i + 1^2、i - 1^2、i + 2^2……）、双重散列（多个哈希函数）
  * 链地址法：拉链法

* put()：jdk1.8 前是头插法 (链地址法)，多线程下扩容出现循环链表，jdk1.8 以后引入红黑树，插入方法变成尾插法

  第一次调用 put 方法时创建数组 Node[] table，因为散列表耗费内存，为了防止内存浪费，所以**延迟初始化**

  存储数据步骤（存储过程）：

  1. 先通过 hash 值计算出 key 映射到哪个桶，哈希寻址
  2. 如果桶上没有碰撞冲突，则直接插入
  3. 如果出现碰撞冲突：如果该桶使用红黑树处理冲突，则调用红黑树的方法插入数据；否则采用传统的链式方法插入，如果链的长度达到临界值，则把链转变为红黑树
  4. 如果数组位置相同，通过 equals 比较内容是否相同：相同则新的 value 覆盖旧 value，不相同则将新的键值对添加到哈希表中
  5. 最后判断 size 是否大于阈值 threshold，则进行扩容

  ```java
  public V put(K key, V value) {
      return putVal(hash(key), key, value, false, true);
  }
  ```

  putVal() 方法中 key 在这里执行了一下 hash()，在 putVal 函数中使用到了上述 hash 函数计算的哈希值：

  ```java
  final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    	//。。。。。。。。。。。。。。
    	if ((p = tab[i = (n - 1) & hash]) == null){//这里的n表示数组长度16
    		//.....
        } else {
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                //onlyIfAbsent默认为false，所以可以覆盖已经存在的数据，如果为true说明不能覆盖
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                // 如果这里允许覆盖，就直接返回了
                return oldValue;
            }
        }
      // 如果是添加操作，modCount ++，如果不是替换，不会走这里的逻辑，modCount用来记录逻辑的变化
      ++modCount;
      // 数量大于扩容阈值
      if (++size > threshold)
          resize();
      afterNodeInsertion(evict);
      return null;
  }
  ```

    * `(n - 1) & hash`：计算下标位置

    <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/HashMap-putVal哈希运算.png" style="zoom: 67%;" />

    * 余数本质是不断做除法，把剩余的数减去，运算效率要比位运算低

  

* treeifyBin()

  节点添加完成之后判断此时节点个数是否大于 TREEIFY_THRESHOLD 临界值 8，如果大于则将链表转换为红黑树，转换红黑树的方法 treeifyBin，整体代码如下：

  ```java
  if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
     //转换为红黑树 tab表示数组名  hash表示哈希值
     treeifyBin(tab, hash);
  ```

  1. 如果当前数组为空或者数组的长度小于进行树形化的阈 MIN_TREEIFY_CAPACITY = 64 就去扩容，而不是将节点变为红黑树
  2. 如果是树形化遍历桶中的元素，创建相同个数的树形节点，复制内容，建立起联系，类似单向链表转换为双向链表
  3. 让桶中的第一个元素即数组中的元素指向新建的红黑树的节点，以后这个桶里的元素就是红黑树而不是链表数据结构了

* tableSizeFor()：创建 HashMap 指定容量时，HashMap 通过位移运算和或运算得到比指定初始化容量大的最小的 2 的 n 次幂

  ```java
  static final int tableSizeFor(int cap) {//int cap = 10
      int n = cap - 1;
      n |= n >>> 1;
      n |= n >>> 2;
      n |= n >>> 4;
      n |= n >>> 8;
      n |= n >>> 16;
      return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }
  ```

  分析算法：

  1. `int n = cap - 1`：防止 cap 已经是 2 的幂。如果 cap 已经是 2 的幂， 不执行减 1 操作，则执行完后面的无符号右移操作之后，返回的 capacity 将是这个 cap 的 2 倍
  2. n=0 （cap-1 之后），则经过后面的几次无符号右移依然是 0，返回的 capacity 是 1，最后有 n+1
  3. |（按位或运算）：相同的二进制数位上，都是 0 的时候，结果为 0，否则为 1
  4. 核心思想：**把最高位是 1 的位以及右边的位全部置 1**，结果加 1 后就是大于指定容量的最小的 2 的 n 次幂

  例如初始化的值为 10：

  * 第一次右移

    ```java
    int n = cap - 1;//cap=10  n=9
    n |= n >>> 1;
    00000000 00000000 00000000 00001001 //9
    00000000 00000000 00000000 00000100 //9右移之后变为4
    --------------------------------------------------
    00000000 00000000 00000000 00001101 //按位或之后是13
    //使得n的二进制表示中与最高位的1紧邻的右边一位为1
    ```

  * 第二次右移

    ```java
    n |= n >>> 2;//n通过第一次右移变为了：n=13
    00000000 00000000 00000000 00001101  // 13
    00000000 00000000 00000000 00000011  // 13右移之后变为3
    -------------------------------------------------
    00000000 00000000 00000000 00001111	 //按位或之后是15
    //无符号右移两位，会将最高位两个连续的1右移两位，然后再与原来的n做或操作，这样n的二进制表示的高位中会有4个连续的1
    ```

    注意：容量最大是 32bit 的正数，因此最后 `n |= n >>> 16`，最多是 32 个 1（但是这已经是负数了）。在执行 tableSizeFor 之前，对 initialCapacity 做了判断，如果大于 MAXIMUM_CAPACITY(2 ^ 30)，则取 MAXIMUM_CAPACITY；如果小于 MAXIMUM_CAPACITY(2 ^ 30)，会执行移位操作，所以移位操作之后，最大 30 个 1，加 1 之后得 2 ^ 30

  * 得到的 capacity 被赋值给了 threshold

    ```java
    this.threshold = tableSizeFor(initialCapacity);//initialCapacity=10
    ```

  * JDK 11

    ```java
    static final int tableSizeFor(int cap) {
        //无符号右移，高位补0
    	//-1补码: 11111111 11111111 11111111 11111111
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    //返回最高位之前的0的位数
    public static int numberOfLeadingZeros(int i) {
        if (i <= 0)
            return i == 0 ? 32 : 0;
        // 如果i>0，那么就表明在二进制表示中其至少有一位为1
        int n = 31;
        // i的最高位1在高16位，把i右移16位，让最高位1进入低16位继续递进判断
        if (i >= 1 << 16) { n -= 16; i >>>= 16; }
        if (i >= 1 <<  8) { n -=  8; i >>>=  8; }
        if (i >= 1 <<  4) { n -=  4; i >>>=  4; }
        if (i >= 1 <<  2) { n -=  2; i >>>=  2; }
        return n - (i >>> 1);
    }
    ```

    

* resize()：

  当 HashMap 中的**元素个数**超过 `(数组长度)*loadFactor(负载因子)` 或者链表过长时（链表长度 > 8，数组长度 < 64），就会进行数组扩容，创建新的数组，伴随一次重新 hash 分配，并且遍历 hash 表中所有的元素非常耗时，所以要尽量避免 resize

  扩容机制为扩容为原来容量的 2 倍：

  ```java
  if (oldCap > 0) {
      if (oldCap >= MAXIMUM_CAPACITY) {
          // 以前的容量已经是最大容量了，这时调大 扩容阈值 threshold
          threshold = Integer.MAX_VALUE;
          return oldTab;
      }
      else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
               oldCap >= DEFAULT_INITIAL_CAPACITY)
          newThr = oldThr << 1; // double threshold
  }
  else if (oldThr > 0) // 初始化的threshold赋值给newCap
      newCap = oldThr;
  else { 
      newCap = DEFAULT_INITIAL_CAPACITY;
      newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
  }
  ```

  HashMap 在进行扩容后，节点**要么就在原来的位置，要么就被分配到"原位置+旧容量"的位置**

  判断：e.hash 与 oldCap 对应的有效高位上的值是 1，即当前数组长度 n 二进制为 1 的位为 x 位，如果 key 的哈希值 x 位也为 1，则扩容后的索引为 now + n

  注意：这里要求**数组长度 2 的幂**

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Java/HashMap-resize扩容.png)

  普通节点：把所有节点分成高低位两个链表，转移到数组

  ```java
  // 遍历所有的节点
  do {
      next = e.next;
      // oldCap 旧数组大小，2 的 n 次幂
      if ((e.hash & oldCap) == 0) {
          if (loTail == null)
              loHead = e;	//指向低位链表头节点
          else
              loTail.next = e;
          loTail = e;		//指向低位链表尾节点
      }
      else {
          if (hiTail == null)
              hiHead = e;
          else
              hiTail.next = e;
          hiTail = e;
      }
  } while ((e = next) != null);
  
  if (loTail != null) {
      loTail.next = null;	// 低位链表的最后一个节点可能在原哈希表中指向其他节点，需要断开
      newTab[j] = loHead;
  }
  ```

  红黑树节点：扩容时 split 方法会将树**拆成高位和低位两个链表**，判断长度是否小于等于 6

  ```java
  //如果低位链表首节点不为null，说明有这个链表存在
  if (loHead != null) {
      //如果链表下的元素小于等于6
      if (lc <= UNTREEIFY_THRESHOLD)
          //那就从红黑树转链表了，低位链表，迁移到新数组中下标不变，还是等于原数组到下标
          tab[index] = loHead.untreeify(map);
      else {
          //低位链表，迁移到新数组中下标不变，把低位链表整个赋值到这个下标下
          tab[index] = loHead;
          //如果高位首节点不为空，说明原来的红黑树已经被拆分成两个链表了
          if (hiHead != null)
              //需要构建新的红黑树了
              loHead.treeify(tab);
      }
  }
  ```

​	

* remove()：删除是首先先找到元素的位置，如果是链表就遍历链表找到元素之后删除。如果是用红黑树就遍历树然后找到之后做删除，树小于 6 的时候退化为链表

  ```java
   final Node<K,V> removeNode(int hash, Object key, Object value,
                              boolean matchValue, boolean movable) {
       Node<K,V>[] tab; Node<K,V> p; int n, index;
       // 节点数组tab不为空、数组长度n大于0、根据hash定位到的节点对象p，
       // 该节点为树的根节点或链表的首节点）不为空，从该节点p向下遍历，找到那个和key匹配的节点对象
       if ((tab = table) != null && (n = tab.length) > 0 &&
           (p = tab[index = (n - 1) & hash]) != null) {
           Node<K,V> node = null, e; K k; V v;//临时变量，储存要返回的节点信息
           //key和value都相等，直接返回该节点
           if (p.hash == hash &&
               ((k = p.key) == key || (key != null && key.equals(k))))
               node = p;
           
           else if ((e = p.next) != null) {
               //如果是树节点，调用getTreeNode方法从树结构中查找满足条件的节点
               if (p instanceof TreeNode)
                   node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
               //遍历链表
               else {
                   do {
                       //e节点的键是否和key相等，e节点就是要删除的节点，赋值给node变量
                       if (e.hash == hash &&
                           ((k = e.key) == key ||
                            (key != null && key.equals(k)))) {
                           node = e;
                           //跳出循环
                           break;
                       }
                       p = e;//把当前节点p指向e 继续遍历
                   } while ((e = e.next) != null);
               }
           }
           //如果node不为空，说明根据key匹配到了要删除的节点
           //如果不需要对比value值或者对比value值但是value值也相等，可以直接删除
           if (node != null && (!matchValue || (v = node.value) == value ||
                                (value != null && value.equals(v)))) {
               if (node instanceof TreeNode)
                   ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
               else if (node == p)//node是首节点
                   tab[index] = node.next;
               else	//node不是首节点
                   p.next = node.next;
               ++modCount;
               --size;
               //LinkedHashMap
               afterNodeRemoval(node);
               return node;
           }
       }
       return null;
   }
  ```

  

* get()

  1. 通过 hash 值获取该 key 映射到的桶

  2. 桶上的 key 就是要查找的 key，则直接找到并返回

  3. 桶上的 key 不是要找的 key，则查看后续的节点：

     * 如果后续节点是红黑树节点，通过调用红黑树的方法根据 key 获取 value

     * 如果后续节点是链表节点，则通过循环遍历链表根据 key 获取 value 

  4. 红黑树节点调用的是 getTreeNode 方法通过树形节点的 find 方法进行查

     * 查找红黑树，之前添加时已经保证这个树是有序的，因此查找时就是折半查找，效率更高。
     * 这里和插入时一样，如果对比节点的哈希值相等并且通过 equals 判断值也相等，就会判断 key 相等，直接返回，不相等就从子树中递归查找

  5. 时间复杂度 O(1)

     * 若为树，则在树中通过 key.equals(k) 查找，**O(logn)** 
     * 若为链表，则在链表中通过 key.equals(k) 查找，**O(n)**

***

**并发异常**

HashMap 和 ArrayList 一样，内部采用 modCount 用来记录集合结构发生变化的次数，结构发生变化是指添加或者删除至少一个元素的所有操作，或者是调整内部数组的大小，仅仅只是设置元素的值不算结构发生变化

在进行序列化或者迭代等操作时，需要比较操作前后 modCount 是否改变，如果**其他线程此时修改了集合内部的结构**，就会直接抛出 ConcurrentModificationException 异常

```java
HashMap map = new HashMap();
Iterator iterator = map.keySet().iterator();
```

```java
final class KeySet extends AbstractSet<K> {
    // 底层获取的是 KeyIterator
	public final Iterator<K> iterator()     { 
        return new KeyIterator(); 
    }
}
final class KeyIterator extends HashIterator implements Iterator<K> {
    // 回调 HashMap.HashIterator#nextNode
    public final K next() { 
        return nextNode().key; 
    }
}
```

```java
abstract class HashIterator {
    Node<K,V> next;        // next entry to return
    Node<K,V> current;     // current entry
    int expectedModCount;  // for 【fast-fail】，快速失败
    int index;             // current slot

    HashIterator() {
        // 把当前 map 的数量赋值给 expectedModCount，迭代时判断
        expectedModCount = modCount;
        Node<K,V>[] t = table;
        current = next = null;
        index = 0;
        if (t != null && size > 0) { // advance to first entry
            do {} while (index < t.length && (next = t[index++]) == null);
        }
    }

    public final boolean hasNext() {
        return next != null;
    }
	// iterator.next() 会调用这个函数
    final Node<K,V> nextNode() {
        Node<K,V>[] t;
        Node<K,V> e = next;
        // 这里会判断 集合的结构是否发生了变化，变化后 modCount 会改变，直接抛出并发异常
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        if (e == null)
            throw new NoSuchElementException();
        if ((next = (current = e).next) == null && (t = table) != null) {
            do {} while (index < t.length && (next = t[index++]) == null);
        }
        return e;
    }
	// 迭代器允许删除集合的元素，【删除后会重置 expectedModCount = modCount】
    public final void remove() {
        Node<K,V> p = current;
        if (p == null)
            throw new IllegalStateException();
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        current = null;
        K key = p.key;
        removeNode(hash(key), key, null, false, false);
        // 同步expectedModCount
        expectedModCount = modCount;
    }
}
```

***

**LinkedMap**

**原理分析**

LinkedHashMap 是 HashMap 的子类

* 优点：添加的元素按照键有序不重复的，有序的原因是底层维护了一个双向链表

* 缺点：会占用一些内存空间

对比 Set：

* HashSet 集合相当于是 HashMap 集合的键，不带值
* LinkedHashSet 集合相当于是 LinkedHashMap 集合的键，不带值
* 底层原理完全一样，都是基于哈希表按照键存储数据的，只是 Map 多了一个键的值

源码解析：

* **内部维护了一个双向链表**，用来维护插入顺序或者 LRU 顺序

  ```java
  transient LinkedHashMap.Entry<K,V> head;
  transient LinkedHashMap.Entry<K,V> tail;
  ```

* accessOrder 决定了顺序，默认为 false 维护的是插入顺序（先进先出），true 为访问顺序（**LRU 顺序**）

  ```java
  final boolean accessOrder;
  ```

* 维护顺序的函数

  ```java
  void afterNodeAccess(Node<K,V> p) {}
  void afterNodeInsertion(boolean evict) {}
  ```

* put()

  ```java
  // 调用父类HashMap的put方法
  public V put(K key, V value) {
      return putVal(hash(key), key, value, false, true);
  }
  final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict)
  → afterNodeInsertion(evict);// evict为true
  ```

  afterNodeInsertion方法，当 removeEldestEntry() 方法返回 true 时会移除最近最久未使用的节点，也就是链表首部节点 first

  ```java
  void afterNodeInsertion(boolean evict) {
      LinkedHashMap.Entry<K,V> first;
      // evict 只有在构建 Map 的时候才为 false，这里为 true
      if (evict && (first = head) != null && removeEldestEntry(first)) {
          K key = first.key;
          removeNode(hash(key), key, null, false, true);//移除头节点
      }
  }
  ```

  removeEldestEntry() 默认为 false，如果需要让它为 true，需要继承 LinkedHashMap 并且覆盖这个方法的实现，在实现 LRU 的缓存中特别有用，通过移除最近最久未使用的节点，从而保证缓存空间足够，并且缓存的数据都是热点数据

  ```java
  protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
      return false;
  }
  ```

* get()

  当一个节点被访问时，如果 accessOrder 为 true，则会将该节点移到链表尾部。也就是说指定为 LRU 顺序之后，在每次访问一个节点时会将这个节点移到链表尾部，那么链表首部就是最近最久未使用的节点

  ```java
  public V get(Object key) {
      Node<K,V> e;
      if ((e = getNode(hash(key), key)) == null)
          return null;
      if (accessOrder)
          afterNodeAccess(e);
      return e.value;
  }
  ```

  ```java
  void afterNodeAccess(Node<K,V> e) {
      LinkedHashMap.Entry<K,V> last;
      if (accessOrder && (last = tail) != e) {
          // 向下转型
          LinkedHashMap.Entry<K,V> p =
              (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
          p.after = null;
          // 判断 p 是否是首节点
          if (b == null)
              //是头节点 让p后继节点成为头节点
              head = a;
          else
              //不是头节点 让p的前驱节点的next指向p的后继节点，维护链表的连接
              b.after = a;
          // 判断p是否是尾节点
          if (a != null)
              // 不是尾节点 让p后继节点指向p的前驱节点
              a.before = b;
          else
              // 是尾节点 让last指向p的前驱节点
              last = b;
          // 判断last是否是空
          if (last == null)
              // last为空说明p是尾节点或者只有p一个节点
              head = p;
          else {
              // last和p相互连接
              p.before = last;
              last.after = p;
          }
          tail = p;
          ++modCount;
      }
  }
  ```

* remove()

  ```java
  //调用HashMap的remove方法
  final Node<K,V> removeNode(int hash, Object key, Object value,boolean matchValue, boolean movable)
  → afterNodeRemoval(node);
  ```

  当 HashMap 删除一个键值对时调用，会把在 HashMap 中删除的那个键值对一并从链表中删除

  ```java
  void afterNodeRemoval(Node<K,V> e) {
      LinkedHashMap.Entry<K,V> p =
          (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
      // 让p节点与前驱节点和后继节点断开链接
      p.before = p.after = null;
      // 判断p是否是头节点
      if (b == null)
          // p是头节点 让head指向p的后继节点
          head = a;
      else
          // p不是头节点 让p的前驱节点的next指向p的后继节点，维护链表的连接
          b.after = a;
      // 判断p是否是尾节点，是就让tail指向p的前驱节点，不是就让p.after指向前驱节点，双向
      if (a == null)
          tail = b;
      else
          a.before = b;
  }
  ```

***

**LRU**

使用 LinkedHashMap 实现的一个 LRU 缓存：

- 设定最大缓存空间 MAX_ENTRIES 为 3
- 使用 LinkedHashMap 的构造函数将 accessOrder 设置为 true，开启 LRU 顺序
- 覆盖 removeEldestEntry() 方法实现，在节点多于 MAX_ENTRIES 就会将最近最久未使用的数据移除

```java
public static void main(String[] args) {
    LRUCache<Integer, String> cache = new LRUCache<>();
    cache.put(1, "a");
    cache.put(2, "b");
    cache.put(3, "c");
    cache.get(1);//把1放入尾部
    cache.put(4, "d");
    System.out.println(cache.keySet());//[3, 1, 4]只能存3个，移除2
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final int MAX_ENTRIES = 3;

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }

    LRUCache() {
        super(MAX_ENTRIES, 0.75f, true);
    }
}
```

***

**TreeMap**

TreeMap 实现了 SotredMap 接口，是有序不可重复的键值对集合，基于红黑树（Red-Black tree）实现，每个 key-value 都作为一个红黑树的节点，如果构造 TreeMap 没有指定比较器，则根据 key 执行自然排序（默认升序），如果指定了比较器则按照比较器来进行排序

TreeMap 集合指定大小规则有 2 种方式：

* 直接为对象的类实现比较器规则接口 Comparable，重写比较方法
* 直接为集合设置比较器 Comparator 对象，重写比较方法

说明：TreeSet 集合的底层是基于 TreeMap，只是键的附属值为空对象而已

成员属性：

* Entry 节点

  ```java
   static final class Entry<K,V> implements Map.Entry<K,V> {
       K key;
       V value;
       Entry<K,V> left;		//左孩子节点
       Entry<K,V> right;		//右孩子节点
       Entry<K,V> parent;		//父节点
       boolean color = BLACK;	//节点的颜色，在红黑树中只有两种颜色，红色和黑色
   }
  ```

* compare()

  ```java
  //如果comparator为null，采用comparable.compartTo进行比较，否则采用指定比较器比较大小
  final int compare(Object k1, Object k2) {
      return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
          : comparator.compare((K)k1, (K)k2);
  }
  ```



参考文章：https://blog.csdn.net/weixin_33991727/article/details/91518677

***

**WeakMap**

WeakHashMap 是基于弱引用的，内部的 Entry 继承 WeakReference，被弱引用关联的对象在**下一次垃圾回收时会被回收**，并且构造方法传入引用队列，用来在清理对象完成以后清理引用

```java
private static class Entry<K,V> extends WeakReference<Object> implements Map.Entry<K,V> {
    Entry(Object key, V value, ReferenceQueue<Object> queue, int hash, Entry<K,V> next) {
        super(key, queue);
        this.value = value;
        this.hash  = hash;
        this.next  = next;
    }
}
```

WeakHashMap 主要用来实现缓存，使用 WeakHashMap 来引用缓存对象，由 JVM 对这部分缓存进行回收

Tomcat 中的 ConcurrentCache 使用了 WeakHashMap 来实现缓存功能，ConcurrentCache 采取分代缓存：

* 经常使用的对象放入 eden 中，eden 使用 ConcurrentHashMap 实现，不用担心会被回收（伊甸园）

* 不常用的对象放入 longterm，longterm 使用 WeakHashMap 实现，这些老对象会被垃圾收集器回收

* 当调用 get() 方法时，会先从 eden 区获取，如果没有找到的话再到 longterm 获取，当从 longterm 获取到就把对象放入 eden 中，从而保证经常被访问的节点不容易被回收

* 当调用 put() 方法时，如果 eden 的大小超过了 size，那么就将 eden 中的所有对象都放入 longterm 中，利用虚拟机回收掉一部分不经常使用的对象

  ```java
  public final class ConcurrentCache<K, V> {
      private final int size;
      private final Map<K, V> eden;
      private final Map<K, V> longterm;
  
      public ConcurrentCache(int size) {
          this.size = size;
          this.eden = new ConcurrentHashMap<>(size);
          this.longterm = new WeakHashMap<>(size);
      }
  
      public V get(K k) {
          V v = this.eden.get(k);
          if (v == null) {
              v = this.longterm.get(k);
              if (v != null)
                  this.eden.put(k, v);
          }
          return v;
      }
  
      public void put(K k, V v) {
          if (this.eden.size() >= size) {
              this.longterm.putAll(this.eden);
              this.eden.clear();
          }
          this.eden.put(k, v);
      }
  }
  ```

***

#### Regex

**概述**

正则表达式的作用：是一些特殊字符组成的校验规则，可以校验信息的正确性，校验邮箱、电话号码、金额等。

比如检验 qq 号：

```java
public static boolean checkQQRegex(String qq){
    return qq!=null && qq.matches("\\d{4,}");//即是数字 必须大于4位数
}// 用\\d  是因为\用来告诉它是一个校验类，不是普通的字符 比如 \t \n
```

java.util.regex 包主要包括以下三个类：

- Pattern 类：

  Pattern 对象是一个正则表达式的编译表示。Pattern 类没有公共构造方法，要创建一个 Pattern 对象，必须首先调用其公共静态编译方法，返回一个 Pattern 对象。该方法接受一个正则表达式作为它的第一个参数

- Matcher 类：

  Matcher 对象是对输入字符串进行解释和匹配操作的引擎。与Pattern 类一样，Matcher 也没有公共构造方法，需要调用 Pattern 对象的 matcher 方法来获得一个 Matcher 对象

- PatternSyntaxException：

  PatternSyntaxException 是一个非强制异常类，它表示一个正则表达式模式中的语法错误。

***

**字符匹配**

**普通字符**

字母、数字、汉字、下划线、以及没有特殊定义的标点符号，都是“普通字符”。表达式中的普通字符，在匹配一个字符串的时候，匹配与之相同的一个字符。其他统称**元字符**

***

**特殊字符**

\r\n 是 Windows 中的文本行结束标签，在 Unix/Linux 则是 \n

| 元字符 | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| \      | 将下一个字符标记为一个特殊字符或原义字符，告诉它是一个校验类，不是普通字符 |
| \f     | 换页符                                                       |
| \n     | 换行符                                                       |
| \r     | 回车符                                                       |
| \t     | 制表符                                                       |
| \\     | 代表 \ 本身                                                  |
| ()     | 使用 () 定义一个子表达式。子表达式的内容可以当成一个独立元素 |

***

**标准字符**

能够与多种字符匹配的表达式，注意区分大小写，大写是相反的意思，只能校验**单**个字符。

| 元字符 | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| .      | 匹配任意一个字符（除了换行符），如果要匹配包括 \n 在内的所有字符，一般用 [\s\S] |
| \d     | 数字字符，0~9 中的任意一个，等价于 [0-9]                     |
| \D     | 非数字字符，等价于  [ ^0-9]                                  |
| \w     | 大小写字母或数字或下划线，等价于[a-zA-Z_0-9_]                |
| \W     | 对\w取非，等价于[ ^\w]                                       |
| \s     | 空格、制表符、换行符等空白字符的其中任意一个，等价于[\f\n\r\t\v] |
| \S     | 对 \s 取非                                                   |

\x 匹配十六进制字符，\0 匹配八进制，例如 \xA 对应值为 10 的 ASCII 字符 ，即 \n

***

**自定义符**

自定义符号集合，[ ] 方括号匹配方式，能够匹配方括号中**任意一个**字符

| 元字符       | 说明                                      |
| ------------ | ----------------------------------------- |
| [ab5@]       | 匹配 "a" 或 "b" 或 "5" 或 "@"             |
| [^abc]       | 匹配 "a","b","c" 之外的任意一个字符       |
| [f-k]        | 匹配 "f"~"k" 之间的任意一个字母           |
| [^A-F0-3]    | 匹配 "A","F","0"~"3" 之外的任意一个字符   |
| [a-d[m-p]]   | 匹配 a 到 d 或者 m 到 p：[a-dm-p]（并集） |
| [a-z&&[m-p]] | 匹配 a 到 z 并且 m 到 p：[a-dm-p]（交集） |
| [^]          | 取反                                      |

* 正则表达式的特殊符号，被包含到中括号中，则失去特殊意义，除了 ^,- 之外，需要在前面加 \

* 标准字符集合，除小数点外，如果被包含于中括号，自定义字符集合将包含该集合。
  比如：[\d. \ -+] 将匹配：数字、小数点、+、-

***

**量词字符**

修饰匹配次数的特殊符号。

* 匹配次数中的贪婪模式(匹配字符越多越好，默认 ！)，\* 和 + 都是贪婪型元字符。
* 匹配次数中的非贪婪模式（匹配字符越少越好，修饰匹配次数的特殊符号后再加上一个 ? 号）

| 元字符 | 说明                              |
| ------ | --------------------------------- |
| X?     | X 一次或一次也没，有相当于 {0,1}  |
| X*     | X 不出现或出现任意次，相当于 {0,} |
| X+     | X 至少一次，相当于 {1,}           |
| X{n}   | X 恰好 n 次                       |
| {n,}   | X 至少 n 次                       |
| {n,m}  | X 至少 n 次，但是不超过 m 次      |

***

**位置匹配**

 **字符边界**

本组标记匹配的不是字符而是位置，符合某种条件的位置

| 元字符 | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| ^      | 与字符串开始的地方匹配（在字符集合中用来求非，在字符集合外用作匹配字符串的开头） |
| $      | 与字符串结束的地方匹配                                       |
| \b     | 匹配一个单词边界                                             |

***

**捕获组**

捕获组是把多个字符当一个单独单元进行处理的方法，它通过对括号内的字符分组来创建。

在表达式 `((A)(B(C)))`，有四个这样的组：((A)(B(C)))、(A)、(B(C))、(C)（按照括号从左到右依次为 group(1)...）

* 调用 matcher 对象的 groupCount 方法返回一个 int 值，表示 matcher 对象当前有多个捕获组。
* 特殊的组 group(0)、group()，代表整个表达式，该组不包括在 groupCount 的返回值中。 

| 表达式                    | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| \|  (分支结构)            | 左右两边表达式之间 "或" 关系，匹配左边或者右边               |
| ()  (捕获组)              | (1) 在被修饰匹配次数的时候，括号中的表达式可以作为整体被修饰<br/>(2) 取匹配结果的时候，括号中的表达式匹配到的内容可以被单独得到<br/>(3) 每一对括号分配一个编号,()的捕获根据左括号的顺序从 1 开始自动编号。捕获元素编号为零的第一个捕获是由整个正则表达式模式匹配的文本 |
| (?:Expression)   非捕获组 | 一些表达式中，不得不使用( )，但又不需要保存 () 中子表达式匹配的内容，这时可以用非捕获组来抵消使用( )带来的副作用。 |

***

**反向引用**

反向引用（\number），又叫回溯引用：

* 每一对()会分配一个编号，使用 () 的捕获根据左括号的顺序从1开始自动编号

* 通过反向引用，可以对分组已捕获的字符串进行引用，继续匹配

* **把匹配到的字符重复一遍在进行匹配**

* 应用 1：

  ```java
  String regex = "((\d)3)\1[0-9](\w)\2{2}";
  ```

  * 首先匹配 ((\d)3)，其次 \1 匹配 ((\d)3) 已经匹配到的内容，\2 匹配 (\d)， {2} 指的是 \2 的值出现两次
  * 实例：23238n22（匹配到 2 未来就继续匹配 2）
  * 实例：43438n44

* 应用 2：爬虫

  ```java
  String regex = "<(h[1-6])>\w*?<\/\1>";
  ```

  匹配结果

  ```java
  <h1>x</h1>//匹配
  <h2>x</h2>//匹配
  <h3>x</h1>//不匹配
  ```

  

***

**零宽断言**

预搜索（零宽断言）（环视）

* 只进行子表达式的匹配，匹配内容不计入最终的匹配结果，是零宽度

* 判断当前位置的前后字符，是否符合指定的条件，但不匹配前后的字符，**是对位置的匹配**

* 正则表达式匹配过程中，如果子表达式匹配到的是字符内容，而非位置，并被保存到最终的匹配结果中，那么就认为这个子表达式是占有字符的；如果子表达式匹配的仅仅是位置，或者匹配的内容并不保存到最终的匹配结果中，那么就认为这个子表达式是**零宽度**的。占有字符还是零宽度，是针对匹配的内容是否保存到最终的匹配结果中而言的

  | 表达式   | 说明                                    |
  | -------- | --------------------------------------- |
  | (?=exp)  | 断言自身出现的位置的后面能匹配表达式exp |
  | (?<=exp) | 断言自身出现的位置的前面能匹配表达式exp |
  | (?!exp)  | 断言此位置的后面不能匹配表达式exp       |
  | (?<!exp) | 断言此位置的前面不能匹配表达式exp       |

***

**匹配模式**

正则表达式的匹配模式：

* IGNORECASE 忽略大小写模式
  * 匹配时忽略大小写。
  * 默认情况下，正则表达式是要区分大小写的。
* SINGLELINE 单行模式
  * 整个文本看作一个字符串，只有一个开头，一个结尾。
  * 使小数点 "." 可以匹配包含换行符（\n）在内的任意字符。
* MULTILINE 多行模式
  * 每行都是一个字符串，都有开头和结尾。
  * 在指定了 MULTILINE 之后，如果需要仅匹配字符串开始和结束位置，可以使用 \A 和 \Z

***

**分组匹配**

Pattern 类：

* `static Pattern compile(String regex)`：将给定的正则表达式编译为模式
* `Matcher matcher(CharSequence input)`：创建一个匹配器，匹配给定的输入与此模式
* `static boolean matches(String regex, CharSequence input)`：编译正则表达式，并匹配输入

Matcher 类：

* `boolean find()`：扫描输入的序列，查找与该模式匹配的下一个子序列
* `String group()`：返回与上一个匹配的输入子序列，同 group(0)，匹配整个表达式的子字符串
* `String group(int group)`：返回在上一次匹配操作期间由给定组捕获的输入子序列 
* `int groupCount()`：返回此匹配器模式中捕获组的数量

```java
public class Demo01{
	public static void main(String[] args) {
		//表达式对象
		Pattern p = Pattern.compile("\\w+");
		//创建Matcher对象
		Matcher m = p.matcher("asfsdf2&&3323");
		//boolean b = m.matches();//尝试将整个字符序列与该模式匹配
		//System.out.println(b);//false
		//boolean b2 = m.find();//该方法扫描输入的序列，查找与该模式匹配的下一个子序列
		//System.out.println(b2);//true
		
		//System.out.println(m.find());
		//System.out.println(m.group());//asfsdf2
		//System.out.println(m.find());
		//System.out.println(m.group());//3323
		
		while(m.find()){
			System.out.println(m.group());	//group(),group(0)匹配整个表达式的子字符串
			System.out.println(m.group(0));
		}
		
	}
}
```

```java
public class Demo02 {
	public static void main(String[] args) {
		//在这个字符串：asfsdf23323，是否符合指定的正则表达式：\w+
		//表达式对象
        Pattern p = Pattern.compile("(([a-z]+)([0-9]+))");//不需要加多余的括号
		//创建Matcher对象
		Matcher m = p.matcher("aa232**ssd445");
	
		while(m.find()){
			System.out.println(m.group());//aa232  ssd445
			System.out.println(m.group(1));//aa232  ssd445
			System.out.println(m.group(2));//aa     ssd
            System.out.println(m.group(3));//232    445 
		}
	}
}
```

* 正则表达式改为 `"(([a-z]+)(?:[0-9]+))"`   没有 group(3) 因为是非捕获组
* 正则表达式改为 `"([a-z]+)([0-9]+)"`  没有 group(3)    aa232  - aa  --232

***

#### Stream

**概述**

Stream 流其实就是一根传送带，元素在上面可以被 Stream 流操作

* 可以解决已有集合类库或者数组 API 的弊端
* Stream 流简化集合和数组的操作
* 链式编程

***

**获取流**

集合获取 Stream 流用：`default Stream<E> stream()`

数组：Arrays.stream(数组)   /  Stream.of(数组);

```java
// Collection集合获取Stream流。
Collection<String> c = new ArrayList<>();
Stream<String> listStream = c.stream();

// Map集合获取流
// 先获取键的Stream流。
Stream<String> keysStream = map.keySet().stream();
// 在获取值的Stream流
Stream<Integer> valuesStream = map.values().stream();
// 获取键值对的Stream流（key=value： Map.Entry<String,Integer>）
Stream<Map.Entry<String,Integer>> keyAndValues = map.entrySet().stream();

//数组获取流
String[] arr = new String[]{"Java", "JavaEE" ,"Spring Boot"};
Stream<String> arrStream1 = Arrays.stream(arr);
Stream<String> arrStream2 = Stream.of(arr);
```

***

**中间操作**

1. **Filter（过滤）**

filter()方法接受一个谓词（一个返回boolean的函数），并返回一个流，其中仅包含通过该谓词的元素

```java
List<String> names = Arrays.asList("Alex", "Brian", "Charles", "David");
List<String> collect = names.stream().filter(item -> item.length() > 4).collect(Collectors.toList());
```

2. **Map（转换）**

map()方法可将一个流的元素转换为另一个流。它接受一个函数，该函数映射流中的每个元素到另一个元素

```java
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> collect = numbers.stream().map(n ->
        {
            return n.intValue();
        }).collect(Collectors.toList());
```

3. **Sorted(排序)**

sorted()方法可以对流进行排序。它可以接受一个Comparator参数

```java
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> collect = numbers.stream().sorted((a, b) ->
        {
            return a - b; //升序
        }).collect(Collectors.toList());
```

4. **Distinct(去重)**

distinct()方法从流中返回所有不同的元素。在内部，它使用equals()方法来比较元素是否相同。因此，我们需要确保equals()方法已正确实现。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 2, 1);
        List<Integer> collect = numbers.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);
```

5. **limit(限制)**

limit()方法可以将流限制为指定的元素数。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> collect = numbers.stream().limit(3).collect(Collectors.toList());
```

6. **Skip(跳过)**

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> collect = numbers.stream().skip(2).collect(Collectors.toList());
```



***

**终结方法**

终结方法：Stream 调用了终结方法，流的操作就全部终结，不能继续使用，如 foreach，count 方法等

非终结方法：每次调用完成以后返回一个新的流对象，可以继续使用，支持**链式编程**

```java
// foreach终结方法
list.stream().filter(s -> s.startsWith("张"))
    .filter(s -> s.length() == 3).forEach(System.out::println);
```

***

**收集流**

1. forEach(循环)

 forEach()方法可将给定的方法应用于流中的每个元素。该方法是一种消费流的方式，不会返回值。

```java
List<String> names = Arrays.asList("Alex", "Brian", "Charles", "David");
        names.stream().forEach(System.out::println);
```

2. collect(收集)

collect()方法可以将流中的元素收集到一个集合中。一般与其他方法配合使用。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> evenNumbers = numbers.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
```

3. count(计数)

count()方法可以返回流中的元素数。

```
List<String> names = Arrays.asList("Alex", "Brian", "Charles", "David");
long count = names.stream().count();
```

4. reduce(聚合)

 reduce()方法可以将流元素聚合为单个结果。它接受一个BinaryOperator参数作为累加器。

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
Optional<Integer> sum = numbers.stream().reduce((a, b) -> a + b);
```

#### Function

**1. 无参数，有返回值（供给型）**

- `Supplier<T>`

  抽象方法：`T get()`

  用途：提供一个类型为 T 的数据（"供给"数据）

   示例：生成随机数

  ```java
  Supplier<Double> randomSupplier = () -> Math.random();
  System.out.println(randomSupplier.get()); // 输出随机数
  ```

**2. 单参数，无返回值（消费型）**

- **`Consumer<T>`**
  抽象方法：`void accept(T t)`
  用途：消费一个类型为 `T` 的数据（“处理” 数据，无返回）。
  示例：打印输入数据

  ```java
  Consumer<String> printer = (s) -> System.out.println(s);
  printer.accept("Hello"); // 输出 "Hello"
  ```

- **`BiConsumer<T, U>`**（双参数，无返回值）
  抽象方法：`void accept(T t, U u)`
  用途：消费两个不同类型的数据。
  示例：打印键值对

  ```java
  BiConsumer<String, Integer> pairPrinter = (k, v) -> System.out.println(k + ":" + v);
  pairPrinter.accept("age", 20); // 输出 "age:20"
  ```

**3. 单参数，有返回值（函数型）**

- **`Function<T, R>`**
  抽象方法：`R apply(T t)`
  用途：将类型 `T` 的数据转换为类型 `R` 的数据（“转换” 数据）。
  示例：字符串转长度

  ```java
  Function<String, Integer> strLength = (s) -> s.length();
  System.out.println(strLength.apply("Hello")); // 输出 5
  ```

- **`UnaryOperator<T>`**（特殊的 `Function`，输入输出类型相同）
  抽象方法：`T apply(T t)`
  用途：对类型 `T` 的数据执行一元操作（如自增、转换格式等）。
  示例：整数加 1

  ```java
  UnaryOperator<Integer> addOne = (n) -> n + 1;
  System.out.println(addOne.apply(5)); // 输出 6
  ```

- **`BiFunction<T, U, R>`**（双参数，有返回值）
  抽象方法：`R apply(T t, U u)`
  用途：将两个不同类型的参数转换为另一种类型的结果。
  示例：两数相加

  ```java
  BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
  System.out.println(sum.apply(3, 5)); // 输出 8
  ```

- **`BinaryOperator<T>`**（特殊的 `BiFunction`，输入输出类型相同）
  抽象方法：`T apply(T t, T u)`
  用途：对两个同类型参数执行二元操作（如求和、取最大值）。
  示例：两数相乘

  ```java
  BinaryOperator<Integer> multiply = (a, b) -> a * b;
  System.out.println(multiply.apply(3, 5)); // 输出 15
  ```

**4. 单参数，返回布尔值（断言型）**

- **`Predicate<T>`**
  抽象方法：`boolean test(T t)`
  用途：判断类型 `T` 的数据是否满足条件（“断言”）。
  示例：判断数字是否为正数

  ```java
  Predicate<Integer> isPositive = (n) -> n > 0;
  System.out.println(isPositive.test(5)); // 输出 true
  ```

- **`BiPredicate<T, U>`**（双参数，返回布尔值）
  抽象方法：`boolean test(T t, U u)`
  用途：判断两个参数是否满足条件。
  示例：判断第一个数是否大于第二个数

  ```java
  BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
  System.out.println(isGreater.test(5, 3)); // 输出 true
  ```

**二、其他常用函数式接口（非 `java.util.function` 包）**

- **`Runnable`**（无参数，无返回值）
  抽象方法：`void run()`
  用途：表示一个可执行的任务（如线程任务）。

  ```java
  Runnable task = () -> System.out.println("Task running");
  new Thread(task).start(); // 启动线程执行任务
  ```

- **`Callable<V>`**（无参数，有返回值，可抛异常）
  抽象方法：`V call() throws Exception`
  用途：表示一个有返回值的任务（常用于线程池）。

  ```java
  Callable<Integer> task = () -> 1 + 2;
  Executors.newSingleThreadExecutor().submit(task).get(); // 结果为 3
  ```

- **`Comparator<T>`**（双参数，返回 int）
  抽象方法：`int compare(T o1, T o2)`
  用途：比较两个对象的大小（常用于排序）。

  ```java
  Comparator<String> lengthComparator = (s1, s2) -> s1.length() - s2.length();
  List<String> list = Arrays.asList("apple", "banana");
  list.sort(lengthComparator); // 按长度排序
  ```



# Java的八股文

## ArrayList源码

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/abf7585f-1d38-46be-a16c-55b832ebe15c.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/e23d276d-7a04-4324-9000-b0e97564807c.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/5161d669-619c-4aa9-a9d6-54d5574daccc.png)

---

## ArrayList的底层的实现原理是什么？



![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/6c52c837-f080-4a8a-ab75-126b95ce4c03.png)

---

## HashMap的实现原理？

HashMap的数据结构：底层使用hash表数据结构，即数组和链表或红黑树

1. 当我们往HashMap中put元素时，利用key的hashCode重新hash计算出当前对象的元素在数据中的下标

2. 存储时，如果出现hash值相同的key，此时有两种情况

   a. 如果key相同，则覆盖原始值

   b. 如果key不同（出现冲突），则将当前的key-value放入链表或红黑树

3. 获取时，直接找到hash值对应的下表，再进一步判断key是否相同

4. 当链表长度大于8且数组长度大于64转换为红黑树

---

## HashMap的扩容？

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/e87b2897-7d2b-4dab-a1e7-f65b71ec2208.png)

---

## Hashmap 与 ConcurrentHashMap的区别

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/556d9661-4ecf-4c5b-b6c7-2c27f92f22d0.png)

---

## RBAC



---

## 什么是前缀树？

**前缀树**（Trie），又叫字典树，是一种树形数据结构，常用于存储一组字符串。它的每个节点表示一个字符，根节点到某个叶子节点所经过的路径对应一个字符串。前缀树最大的特点是它可以有效地存储和查找字符串，尤其在涉及到字符串前缀查询时效率较高。

**前缀树的结构**

- **根节点**：没有字符，代表一个空字符串。
- **节点**：每个节点包含一个字符以及指向子节点的指针。
- **路径**：从根节点到某个节点的路径代表一个字符串的前缀。
- **叶子节点**：一般指向一个完整的字符串，或者表示某个字符串的结束。

**前缀树的特点**

1. **共享公共前缀**：相同前缀的字符串会共享相同的路径，因此节省了存储空间。
2. **字符串查找效率高**：前缀树的查找、插入、删除操作的时间复杂度为 **O(k)**，其中 **k** 是字符串的长度，而与字符串的数量无关。
3. **适合用于前缀相关的操作**：如查找某个前缀的所有字符串等。

**前缀树的基本操作**

1. 插入
   - 从根节点开始，依次插入每个字符，若路径中不存在字符，则创建新节点。
   - 若到达某个字符时该路径已存在，则直接向下走。
2. 查找
   - 从根节点开始，逐个字符查找，若路径中不存在字符，则返回失败。
3. 删除
   - 删除某个字符串时，从该字符串的最后一个字符开始，逐个回溯，删除不再被其他字符串共享的节点。

### **前缀树的应用**

1. **自动补全（Auto-Completion）**：
   - 前缀树广泛应用于搜索引擎、IDE 编辑器等场景中，用于根据用户输入的前缀，快速提供可能的补全选项。例如，搜索框自动补全建议列表。
   - **应用实例**：Google 搜索的即时搜索建议、输入法中的词语补全等。
2. **词典（Dictionary）**：
   - 前缀树可以用于实现高效的词典查找。在词典中，可以快速查找一个单词是否存在，或者查找以某个前缀开头的所有单词。
   - **应用实例**：拼写检查，单词自动纠错等。
3. **字符串查找**：
   - 前缀树可以用于多字符串匹配，特别是当我们需要频繁查询某个前缀或模式时。例如，查找所有以某个前缀开头的单词。
   - **应用实例**：文件路径的快速查找，电话号码的快速匹配等。
4. **前缀匹配**：
   - 在某些情况下，我们需要查找以给定前缀开头的所有单词。使用前缀树可以非常高效地进行前缀匹配。
   - **应用实例**：社交媒体平台中的标签查找，词汇前缀查询等。
5. **IP 路由表**：
   - 前缀树在网络路由中也有应用，特别是在查找某个 IP 地址所在的路由时。IP 地址前缀可以通过前缀树快速匹配到对应的路由表。
   - **应用实例**：路由器中的路由表查询。
6. **前缀压缩（Prefix Compression）**：
   - 前缀树可用于网络协议中的地址压缩。通过去除公共前缀，减少存储空间。
   - **应用实例**：压缩 IP 地址、URL 路径等。

### **前缀树的时间复杂度**

- **查询**：O(k)，k 是查询字符串的长度。
- **插入**：O(k)，k 是插入字符串的长度。
- **删除**：O(k)，k 是删除字符串的长度。

前缀树的性能非常依赖于字符串的长度 **k**，在字符串数量很大的情况下，它的查询和插入速度仍然保持较好的表现。

---

## 接口内的默认方法和静态方法有什么区别？

在 Java 8 及以上版本中，接口可以包含默认方法（default method）和静态方法（static method），两者的主要区别体现在**定义方式、调用方式、继承特性**和**使用场景**上，具体如下：

1. **定义方式不同**

- **默认方法**：用 `default` 关键字修饰，必须有方法体。
  示例：

  ```java
  public interface MyInterface {
      // 默认方法
      default void defaultMethod() {
          System.out.println("这是默认方法");
      }
  }
  ```

- **静态方法**：用 `static` 关键字修饰，必须有方法体。
  示例：

  ```java
  public interface MyInterface {
      // 静态方法
      static void staticMethod() {
          System.out.println("这是静态方法");
      }
  }
  ```

**2. 调用方式不同**

- **默认方法**：必须通过接口的**实现类实例**调用（因为默认方法属于实例级别的行为）。
  示例：

  ```java
  public class MyImpl implements MyInterface {
      // 可以不重写默认方法，直接继承
  }
  
  // 调用默认方法
  MyInterface obj = new MyImpl();
  obj.defaultMethod(); // 正确（通过实例调用）
  ```

- **静态方法**：直接通过**接口名**调用（属于接口本身的方法，与实例无关）。
  示例：

  ```java
  // 调用静态方法
  MyInterface.staticMethod(); // 正确（通过接口名调用）
  ```

**3. 继承与重写特性不同**

- **默认方法**：
  - 实现类可以**继承**接口的默认方法（无需重写）。
  - 实现类也可以**重写**默认方法（去掉 `default` 关键字，保留方法体）。
  - 如果一个类实现了多个接口，且接口中存在**同名默认方法**，则实现类必须显式重写该方法以解决冲突。
- **静态方法**：
  - 实现类**不能继承**接口的静态方法（静态方法属于接口本身，与实现类无关）。
  - 实现类**不能重写**接口的静态方法（即使定义同名静态方法，也只是 “隐藏” 了接口的静态方法，而非重写）。

**4. 使用场景不同**

- **默认方法**：主要用于**接口的扩展**。当需要给接口新增方法时，默认方法可以提供默认实现，避免影响已有的实现类（无需所有实现类都修改）。
  例如：Java 集合框架中 `Collection` 接口的 `stream()` 方法就是默认方法，用于在不破坏原有实现的前提下新增流式处理功能。
- **静态方法**：主要用于提供与接口相关的**工具方法或辅助方法**，避免创建工具类（如 `Collections` 工具类的功能可以直接嵌入接口）。
  例如：`Comparator` 接口的 `comparing()` 静态方法，用于快速创建比较器。

**总结表格**

| 特性     | 默认方法（default）      | 静态方法（static）           |
| -------- | ------------------------ | ---------------------------- |
| 修饰符   | 用 `default` 修饰        | 用 `static` 修饰             |
| 调用方式 | 通过实现类实例调用       | 通过接口名直接调用           |
| 继承性   | 可被实现类继承，可重写   | 不可被实现类继承，不可重写   |
| 主要用途 | 接口扩展（提供默认实现） | 提供工具方法（与接口强相关） |

---

## Overload 和 Override的区别

Overload是重载的意思，Override是覆盖的意思，也就是重写。

重载Overload表示同一个类中可以有多个名称相同的方法，但这些方法的参数列表各不相同（即参数个数或类型不同）。

重写Override表示子类中的方法可以与父类中的某个方法的名称和参数完全相同，通过子类创建的实例对象调用这个方法时，将调用子类中的定义方法，这相当于把父类中定义的那个完全相同的方法给覆盖了，这也是面向对象编程的多态性的一种表现。子类覆盖父类的方法时，只能比父类抛出更少的异常，或者是抛出父类抛出的异常的子异常，因为子类可以解决父类的一些问题，不能比父类有更多的问题。子类方法的访问权限只能比父类的更大，不能更小。如果父类的方法是private类型，那么，子类则不存在覆盖的限制，相当于子类中增加了一个全新的方法。

---

## sleep() 和 wait() 有什么区别？

sleep是线程类（Thread）的方法，导致此线程暂停执行指定时间，给执行机会给其他线程，但是监控状态依然保持，到时会自动恢复。调用sleep不会释放对象锁。wait 是 Object 类的方法，此对象调用

## BigDecimal 的原理

在 Java 中，`BigDecimal` 是处理**高精度十进制大数计算**的核心类，专门解决 `float` 和 `double` 因二进制浮点数特性导致的精度丢失问题（如 `0.1 + 0.2 != 0.3`）。其核心原理基于**整数存储 + 标度控制**，通过精确表示十进制数的每一位，实现无误差的算术运算。

### 一、为什么需要 `BigDecimal`？

`float` 和 `double` 是**二进制浮点数**，底层用 IEEE 754 标准存储，无法精确表示所有十进制小数（如 `0.1` 在二进制中是无限循环小数），导致计算误差：

```java
System.out.println(0.1 + 0.2); // 输出 0.30000000000000004（误差）
```

而 `BigDecimal` 基于**十进制**设计，可精确表示任意精度的小数，适合金融、货币等对精度敏感的场景。

### 二、`BigDecimal` 的核心原理

#### 1. 内部存储结构

`BigDecimal` 内部通过两个核心字段实现高精度存储：

- **`BigInteger intVal`**：存储数值的**整数形式**（包含符号，如 `-12345` 表示 `-123.45` 的整数部分）。
- **`int scale`**：表示**标度**（小数点后的位数），即数值 = `intVal / (10^scale)`。

例如：

- `3.14` 存储为：`intVal = 314`，`scale = 2`（314 / 10² = 3.14）。
- `100` 存储为：`intVal = 100`，`scale = 0`（100 / 10⁰ = 100）。
- `0.005` 存储为：`intVal = 5`，`scale = 3`（5 / 10³ = 0.005）。

#### 2. 运算原理（以核心运算为例）

`BigDecimal` 的运算（加、减、乘、除）本质是对 `intVal` 和 `scale` 的逻辑处理，确保十进制精度不丢失。

##### （1）加法（`add`）

步骤：

- **对齐标度**：将两个数的 `scale` 统一为较大值（如 `3.14`（scale=2） + `2.5`（scale=1）→ 统一为 scale=2，`2.5` 转为 `2.50`）。
- **整数部分相加**：将 `intVal` 按统一后的 scale 转换为整数（`314 + 250 = 564`）。
- **结果处理**：新 `scale` 为统一后的 scale（2），即 `564 / 10² = 5.64`。

代码示例：

```java
BigDecimal a = new BigDecimal("3.14");
BigDecimal b = new BigDecimal("2.5");
BigDecimal c = a.add(b); // 结果：5.64
```

##### （2）减法（`subtract`）

逻辑与加法类似，先对齐标度，再对整数部分做减法：

```java
BigDecimal a = new BigDecimal("5.0");
BigDecimal b = new BigDecimal("0.001");
BigDecimal c = a.subtract(b); // 结果：4.999
```

##### （3）乘法（`multiply`）

步骤：

- 整数部分直接相乘（`intVal1 * intVal2`）。
- 新标度为两个数的标度之和（`scale1 + scale2`）。

例如：`3.14`（intVal=314，scale=2） × `2.5`（intVal=25，scale=1）：

- 整数相乘：`314 × 25 = 7850`。
- 新标度：`2 + 1 = 3`。
- 结果：`7850 / 10³ = 7.85`。

代码示例：

```java
BigDecimal a = new BigDecimal("3.14");
BigDecimal b = new BigDecimal("2.5");
BigDecimal c = a.multiply(b); // 结果：7.85
```

##### （4）除法（`divide`）

除法是最复杂的运算，因可能产生无限循环小数（如 `1 ÷ 3`），需指定**舍入模式（RoundingMode）** 控制精度：

步骤：

- 整数部分相除（`intVal1 ÷ intVal2`），根据指定的精度和舍入模式处理余数。
- 新标度由用户指定（或默认取较大值）。

例如：`1 ÷ 3` 保留 2 位小数，舍入模式为 `HALF_UP`（四舍五入）：

```java
BigDecimal a = new BigDecimal("1");
BigDecimal b = new BigDecimal("3");
// 保留2位小数，四舍五入
BigDecimal c = a.divide(b, 2, RoundingMode.HALF_UP); // 结果：0.33
```

#### 3. 舍入模式（`RoundingMode`）

除法、取整等操作中，`BigDecimal` 提供 8 种舍入模式控制精度，核心包括：

- `HALF_UP`：四舍五入（如 `2.35` 保留 1 位 → `2.4`）。
- `DOWN`：截断（如 `2.35` 保留 1 位 → `2.3`）。
- `UP`：向上进 1（如 `2.31` 保留 1 位 → `2.4`）。
- `HALF_EVEN`：银行家舍入法（四舍六入五取偶，如 `2.35` 保留 1 位 → `2.4`，`2.45` 保留 1 位 → `2.4`）。

### 三、`BigDecimal` 与其他大数类的关系

- **`BigInteger`**：`BigDecimal` 的整数部分依赖 `BigInteger` 实现，`BigInteger` 可存储任意大小的整数（突破 `long` 的范围限制），内部用 `int[]` 数组存储十进制数的每一位（如 `12345` 存储为 `[1,2,3,4,5]`）。
- 两者的共性：均为**不可变类**（每次运算生成新对象），确保线程安全；通过数组存储突破基本类型的长度限制。

### 四、关键注意事项

1. **避免用 `double` 构造 `BigDecimal`**：`new BigDecimal(0.1)` 会因 `0.1` 的二进制精度问题导致初始化误差，应使用字符串构造：

   ```java
   // 错误：存在精度误差
   BigDecimal bad = new BigDecimal(0.1); 
   // 正确：精确初始化
   BigDecimal good = new BigDecimal("0.1"); 
   ```

   

2. **主动指定舍入模式**：除法等可能产生无限小数的运算必须指定精度和舍入模式，否则抛出 `ArithmeticException`。

3. **性能权衡**：高精度计算依赖数组操作和逻辑处理，性能低于 `float`/`double`，适合精度优先的场景（如金融），不适合高性能数值计算。

### 总结

`BigDecimal` 的核心原理是通过**整数形式存储数值 + 标度控制小数点位置**，将十进制小数运算转化为整数运算，避免二进制浮点数的精度丢失。其设计兼顾了高精度需求和灵活性（通过舍入模式控制精度），是金融、货币等领域的必备工具。理解其内部存储和运算逻辑，能帮助开发者正确使用以避免精度问题。

---

## 接口和抽象类

接口可以继承接口。抽象类可以实现(implements)接口，抽象类可以继承具体类。抽象类中可以有静态的main方法。

备注：只要明白了接口和抽象类的本质和作用，这些问题都很好回答，想想看，如果自己作为是java语言的设计者，是否会提供这样的支持，如果不提供的话，有什么理由吗？如果没有道理不提供，那答案就是肯定的了。

只有记住抽象类与普通类的唯一区别就是不能创建实例对象和允许有abstract方法。

---

## Java 中 native 是什么意思？

在 Java 中，`native` 是一个关键字，用于声明**本地方法（Native Method）**—— 这类方法的**声明在 Java 代码中**，但**实现逻辑由非 Java 语言（如 C、C++、汇编）编写**，通常用于与底层操作系统、硬件交互，或复用已有的高性能本地库。

### **核心特点**

1. **无 Java 实现体**：`native` 方法仅在 Java 中声明，没有方法体（以分号结束），其实现由外部本地代码提供。

   ```java
   public class NativeExample {
       // 声明native方法，无实现体
       public native void callNativeCode();
       
       public static void main(String[] args) {
           // 加载包含本地实现的动态链接库（.dll/.so/.dylib）
           System.loadLibrary("NativeImpl"); 
           new NativeExample().callNativeCode();
       }
   }
   ```

2. **依赖 JNI 接口**：`native` 方法通过 **JNI（Java Native Interface，Java 本地接口）** 与本地代码交互。JNI 是一套规范，定义了 Java 与本地代码的通信协议（如参数传递、内存管理等）。

3. **与平台相关**：本地代码需针对特定操作系统编译（如 Windows 生成 `.dll`，Linux 生成 `.so`，macOS 生成 `.dylib`），因此包含 `native` 方法的 Java 程序可能失去跨平台性。

### **主要用途**

1. **访问底层系统资源**：Java 语言本身是跨平台的，无法直接操作特定系统的硬件或内核 API（如磁盘 IO、网络协议栈、显卡驱动），通过 `native` 方法可调用 C/C++ 编写的系统接口。
   - 例：`java.lang.System.currentTimeMillis()` 是 `native` 方法，底层通过系统调用获取当前时间戳。
2. **复用高性能本地库**：对于计算密集型任务（如音视频编码、机器学习推理），C/C++ 性能优于 Java，可通过 `native` 方法调用成熟的本地库（如 FFmpeg、OpenCV）。
3. **解决 Java 语言限制**：Java 对内存的直接操作（如指针）有严格限制，`native` 方法可突破这些限制（需谨慎使用，避免内存安全问题）。

### **工作流程（以调用 C 实现为例）**

1. **声明 `native` 方法**：在 Java 类中用 `native` 关键字声明方法。
2. **生成头文件（.h）**：通过 `javac -h` 命令根据 Java 类生成 C 语言头文件，定义 JNI 函数签名（如 `Java_NativeExample_callNativeCode`）。
3. **编写本地实现**：用 C/C++ 实现头文件中声明的函数，通过 JNI 接口与 Java 交互（如获取参数、返回结果）。
4. **编译动态链接库**：将本地代码编译为平台相关的动态库（.dll/.so）。
5. **Java 加载并调用**：通过 `System.loadLibrary()` 加载动态库，直接调用 `native` 方法。

### **注意事项**

1. **性能与安全权衡**：
   - 优势：本地代码性能高，适合底层操作；
   - 风险：可能引入内存泄漏、缓冲区溢出等安全问题（Java 的内存管理机制无法管控本地代码）。
2. **跨平台性丧失**：本地库依赖操作系统，需为不同平台编译不同版本，破坏 Java“一次编写，到处运行” 的特性。
3. **调试复杂**：本地代码与 Java 代码的调试工具不同（如 C 用 GDB，Java 用 JDB），混合调试难度大。
4. **避免过度使用**：现代 Java 已通过 JDK 内置 API 封装了大部分底层操作（如 NIO 替代传统 IO 操作），非必要不建议自定义 `native` 方法。

### **常见 `native` 方法示例**

JDK 中大量使用 `native` 方法处理底层逻辑，例如：

- `java.lang.Object.getClass()`：获取对象的运行时类信息，依赖底层虚拟机实现。
- `java.io.FileInputStream.read()`：读取文件字节，底层调用操作系统的文件读取接口。
- `sun.misc.Unsafe` 类中的方法：直接操作内存（如 `allocateMemory`），用于高性能框架（如 Netty、Hadoop）。

### **总结**

`native` 关键字是 Java 与底层系统交互的 “桥梁”，允许 Java 调用非 Java 语言实现的方法，弥补了 Java 在底层操作和性能方面的不足。但因其平台依赖性和安全风险，日常开发中较少自定义 `native` 方法，更多是使用 JDK 已封装好的 `native` 接口。理解 `native` 的原理，有助于深入认识 Java 与操作系统的交互机制。

## 一个Java 文件的非 public 顶级类的访问权限是怎么样的？

在 Java 中，一个 `.java` 文件可以包含多个**顶级类**（非内部类），但确实只能有一个类被声明为 `public`（且该类名必须与文件名一致）。其他非 `public` 类的访问权限和访问方式遵循以下规则：

### 一、非 public 类的权限修饰符

Java 中，**顶级类**（非内部类）的访问权限只有两种：

1. **`public`**：全工程可见（整个项目中任何类都可访问）；
2. **默认权限（无修饰符，package-private）**：仅同一包内的类可见。

> 注意：`private` 和 `protected` 不能修饰顶级类，仅能修饰类内部的成员（字段、方法、内部类）。

### 二、非 public 类的访问方式

非 `public` 类因权限限制，其访问范围取决于所在包：

#### 1. 同一包内的类：直接访问

如果其他类与非 `public` 类在**同一个包**中，可直接创建实例、调用静态成员，无需额外条件。

示例：文件 `Test.java`（包含一个 `public` 类和一个默认权限类）：

```java
// 文件名必须为 Test.java（与 public 类名一致）
public class Test {
    public static void main(String[] args) {
        // 同一包内，直接访问默认权限类 Helper
        Helper helper = new Helper();
        helper.print(); // 输出："我是默认权限类"
    }
}

// 非 public 类，默认权限（仅同包可见）
class Helper {
    void print() {
        System.out.println("我是默认权限类");
    }
}
```

同一包下的另一个类 `Other.java`：

```java
package com.example; // 与 Test.java 同包

public class Other {
    public void useHelper() {
        // 同一包，可直接访问 Helper
        Helper helper = new Helper();
        helper.print(); 
    }
}
```

#### 2. 不同包的类：无法直接访问

如果其他类与非 `public` 类在**不同包**中，默认权限会限制访问，此时无法直接创建实例或调用其成员。

示例：不同包下的类 `Another.java`：

```java
package com.other; // 与 Test.java 不同包

import com.example.Helper; // 编译错误：Helper 是默认权限，无法导入

public class Another {
    public void tryAccess() {
        Helper helper = new Helper(); // 编译错误：找不到类 Helper（权限不足）
    }
}
```

### 三、非 public 类的典型用途

非 `public` 类通常作为**辅助类**，仅在当前包内配合 `public` 类工作，避免对外暴露无关实现细节：

1. 封装工具方法（仅同包类使用）；
2. 定义数据模型（仅同包的 `public` 类需要依赖）；
3. 拆分 `public` 类的复杂逻辑（避免单个类过于庞大）。

### 四、特殊情况：内部类的权限

如果是**内部类**（嵌套在其他类中的类），权限修饰符可以是 `public`、`protected`、`private` 或默认，其访问范围由修饰符决定：

- `private` 内部类：仅外部类可见；
- `protected` 内部类：外部类、同包类、子类可见；
- 默认内部类：外部类、同包类可见；
- `public` 内部类：全工程可见。

```java
public class Outer {
    // private 内部类：仅 Outer 类内可见
    private class PrivateInner {
        void doSomething() {}
    }
    
    // public 内部类：全工程可见
    public class PublicInner {
        void doSomething() {}
    }
    
    public void useInner() {
        PrivateInner pi = new PrivateInner(); // 合法
        PublicInner pu = new PublicInner();   // 合法
    }
}

// 其他类访问内部类
class Other {
    public void accessInner() {
        Outer outer = new Outer();
        // 访问 public 内部类（需通过外部类实例）
        Outer.PublicInner pu = outer.new PublicInner();
        pu.doSomething(); // 合法
        
        // 无法访问 private 内部类
        Outer.PrivateInner pi = outer.new PrivateInner(); // 编译错误
    }
}
```

### 总结

- 一个 `.java` 文件中，非 `public` 顶级类的权限只能是**默认权限**（package-private），仅同一包内的类可直接访问；
- 不同包的类无法访问这些非 `public` 类，保证了实现细节的封装；
- 内部类的权限更灵活（支持 `private`/`protected`），访问范围由修饰符和外部类关系共同决定。

这种设计既允许在一个文件中组织关联类，又通过权限控制避免了不必要的对外暴露，平衡了代码组织性和封装性。

---

## String s = "hello"; s = s + " world!";  这两行代码执行后，原始的String 对象中内容到底改变没有？

没有。

因为String被设计成不可变(immutable)类，所以它的所有对象都是不可变对象。在这段代码中，s原先指向一个String对象，内容是 "Hello"，然后我们对s进行了+操作，那么s所指向的那个对象是否发生了改变呢？答案是没有。这时，s不指向原来那个对象了，而指向了另一个 String对象，内容为"Hello world!"，原来那个对象还存在于内存之中，只是s这个引用变量不再指向它了。

通过上面的说明，我们很容易导出另一个结论，如果经常对字符串进行各种各样的修改，或者说，不可预见的修改，那么使用String来代表字符串的话会引起很大的内存开销。因为 String对象建立之后不能再改变，所以对于每一个不同的字符串，都需要一个String对象来表示。这时，应该考虑使用StringBuffer类，它允许修改，而不是每个不同的字符串都要生成一个新的对象。并且，这两种类的对象转换十分容易。

---

## String 和 StringBuffer 的区别

JAVA平台提供了两个类：String和StringBuffer，它们可以储存和操作字符串，即包含多个字符的字符数据。这个String类提供了数值不可改变的字符串。而这个StringBuffer类提供的字符串进行修改。当你知道字符数据要改变的时候你就可以使用StringBuffer。典型地，你可以使用StringBuffers来动态构造字符数据。另外，String实现了equals方法，new String(“abc”).equals(new String(“abc”)的结果为true,而StringBuffer没有实现equals方法，所以，new StringBuffer(“abc”).equals(new StringBuffer(“abc”)的结果为false。

---

##









