# Go

## 概念

### 原因

（1）目前主流的编程语言不能合理利用多核多CPU

（2）软件系统越来越复杂，维护成本越来越高，缺乏一个简洁高效的编程语言

（3）c和c++存在内存泄露，编译慢

### 特点

- 既达到了静态编译语言，又达到了动态语言开发维护的高效率
- 垃圾回收机制
- 一个文件都要归属于一个包
- 支持并发
- 函数可以返回多个值
- 增加了管道通信机制

### 执行流程

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/b27128db-47db-4dab-abe7-691b51fe3ce4.png)

1） 如果先编译生成了可执行文件，那么我们可以将可执行文件拷贝到没有go开发环境的机器上，仍可以运行

2）如果我们是直接go run，则在另一台机器上运行，也需要go开发环境

3）在编译时，编译器会将程序运行依赖的库文件包含在可执行文件中

### 注意事项

- 源文件以go为扩展名
- 执行入口为main函数
- 严格区分大小写
- 不需要加分号
- 定义的变量和import的包没有使用到编译无法通过

### 接口文档

```
https://studygolang.com/pkgdoc
```

## 代码

### 变量

- 声明变量

```go
//1.指定变量类型
var i int
i = 10
//2.根据值自动判断变量类型
var num = 10.11
//3. 忽略var
num := 10.11
//多个变量
var n1, n2, n3 int
var n1, name, n3 = 100, "tom", 888
n1, name, n3 := 100, "tom", 888
//多个变量
var (
	n3 =300
  n4 = 900
  name2 = "tom"
)
```

- 注意事项

1. 该区域的数据值可以在同意类型范围内不断变化
2. 变量在同一个作用域内不能重名
3. 变量如果没有设值，go会使用默认值
4. +号两边都是字符时，是字符拼接，当两边为字符时，则是加法运算

***

### 基本数据类型

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/0617e9a8-ce5c-4b7a-84a4-7843d4c09dc9.png)

- 数据类型转换

T（v）：T是数据类型，v是需要转换的变量

```go
	var i int8
	var n1 float32 
	n1 = 1.6
	i = int8(n1)	
	fmt.Println("i=", i)
```

- 基本数据类型转换为String

1、 fmt.Springf("%参数", 表达式)

```go
	var name string
	var i int =10
	name = fmt.Sprintf("%v", i)
	fmt.Printf(name)
```

2、使用strconv包的函数

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/fee887bd-a006-4ca1-ad1c-48c6402eeb82.png)

```go
	var name string
	var num3 int64 =10
	name = strconv.FormatI  nt(num3, 10) //10进制
	fmt.Printf("%q:", name)
```

- String类型转换为基本数据类型

1、使用strconv包的函数

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/f9b0b8be-d50c-4d12-9715-537476ae8199.png)

**注意**：当string无法转换成基本数据类型时，将会自动转换为默认值

***

### 复杂数据类型

#### 指针

1. 基本数据类型，变量中存的就是值，也叫值类型
2. 获取变量的地址，用&，比如&num
3. 指针类型，指针变量存的是一个地址，这个地址指向的空间存的才是值
4. 获取指针类型所指向的值，使用 *, 比如 var ptr *int = &num

***

### 值类型和引用类型

值类型：基本数据类型，string，数组和结构体struct，内存通常在栈中分配

引用类型：指针，切片，map，管道chan，interface等都是引用类型，内存通常在堆中分配，当没有任何变量引用这个地址时，该地址对应的数据空间就成为一个垃圾，由GC来回收

***

### 标识符

- 命名规范

1）包名：保持package的名字和目录保持一致，尽量采取有意义的包名，简短有意义，不要和标准库冲突

2）变量，函数名，常量名采用驼峰法

3）如果变量名，函数名，常量名首字母大写，则可以被其他包访问，如果首字母小写，则只能在本包中使用

***

### 关键字

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/d240151f-98f0-45b0-8e0b-0da4b5e8d476.png)

***

### 预定义标识符

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/e05871e1-aced-4ea2-b5d6-301c84b60839.png)

***

### 算术运算符

- Golang的自增自减只能当作一个独立语言使用，不能 b ：=a++
- ++, -- 只能写在变量的后面，不能写在变量的前面

### 位运算符

- 按位与 & 

- 按位或 |
- 按位异或 ^

- 右移运算符 >>   低位移溢出，符号位不变，并用符号位补溢出的高位
- 左移运算符 <<   符号位不变，低位补0

### 键盘输入语句

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/f0acb520-f5d9-41e6-8aa4-2a0a638bf1a0.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/656b799e-b75b-45cd-8a62-d206473a1504.png)

### 流程控制

- 顺序控制

- 分支控制

1. 单分支

```go
	var age int
	fmt.Println("请输入年龄：")
	fmt.Scanln(&age)
	if age > 18 {
		fmt.Println("年龄大于18")
	}
```

2. 双分支

```go
	var age int
	fmt.Println("请输入年龄：")
	fmt.Scanln(&age)
	if age > 18 {
		fmt.Println("年龄大于18")
	}else{
		fmt.Println("年龄小于18")
	}
```

3. 多分支

```go
	var age int
	fmt.Println("请输入年龄：")
	fmt.Scanln(&age)
	if age > 18 {
		fmt.Println("年龄大于18")
	}else if age < 14 {
		fmt.Println("年龄小于14")
	}else{
		fmt.Println("年龄在14-18")
	}
```

- 循环控制

```go
// 第一种写法	
for i := 1; i <= 10; i++ {
		fmt.Println("你好，尚硅谷")
	}
// 第二种写法
j := 1
for j<=10 {
  fmt.Println("你好，尚硅谷")
  j++
}
// 第三种写法，通常需要配合break语句使用
j := 1
for {
  fmt.Println("你好，尚硅谷")
  j++;
  if j>10 {
    break
  }
}
// 第四种方式 for-range，可以方便遍历字符串和数组

```

