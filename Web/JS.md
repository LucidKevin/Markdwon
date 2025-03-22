# JS

## 概述

JavaScript 是一种客户端脚本语言。运行在客户端浏览器中，每一个浏览器都具备解析 JavaScript 的引擎。

脚本语言：不需要编译，就可以被浏览器直接解析执行了。

作用：增强用户和 HTML 页面的交互过程，让页面产生动态效果，增强用户的体验。

组成部分：ECMAScript、DOM、BOM

开发环境搭建：安装Node.js，是JavaScript运行环境



***



## 语法

### 引入

引入HTML文件

* 内部方式：<script>标签

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>JS快速入门</title>
  </head>
  <body>
      <!--html语句-->
  </body>
  <script>
      // JS语句
  </script>    
  </html>
  ```

* 外部方式

  * 创建js文件：my.js

    ```js
    alert("Hello");//js语句
    ```

  * 在html中引用外部js文件

    ```html
    <body>
        <!--html语句-->
    </body>
    <script src="js/my.js"></script>
    <html>
    ```




***



### 注释

* 单行注释

  ```javascript
  // 注释的内容
  ```

* 多行注释

  ```javascript
  /*
  注释的内容
  */
  ```




### 输入输出

- 输入框：prompt(“提示内容”);

- 弹出警告框：alert(“提示内容”); 

- 控制台输出：console.log(“显示内容”); 

- 页面内容输出：document.write(“显示内容”);

  注：`document.write("<br/>")`换行，通常输出数据后跟br标签



***



### 变量常量

JavaScript 属于弱类型的语言，定义变量时不区分具体的数据类型

* 定义局部变量：let 变量名 = 值; 

  ```js
  let name = "张三";
  let age = 23;
  document.write(name + "," + age +"<br>");
  ```

* 定义全局变量：变量名 = 值; 

  ```js
  {
      l2 = "bb";
  }
  document.write(l2 + "<br>");
  ```

* 定义常量：const 常量名 = 值;
  常量不能被重新赋值

  ```js
  const PI = 3.1415926;
  //PI = 3.15;  
  document.write(PI);
  ```




***



### 数据类型

| 数据类型  | 说明                         |
| --------- | ---------------------------- |
| boolean   | 布尔类型，true或false        |
| null      | 声明null值的特殊关键字       |
| undefined | 代表变量未定义               |
| number    | 整数或浮点数                 |
| string    | 字符串                       |
| bigint    | 大整数，例如：let num = 10n; |

**typeof 用于判断变量的数据类型**

```js
let age = 18; 
document.write(typeof(age)); // number
```



***



### 运算符

* 算术运算符

  | 算术运算符 | 说明     |
  | ---------- | -------- |
  | +          | 加法运算 |
  | -          | 减法运算 |
  | *          | 乘法运算 |
  | /          | 除法运算 |
  | %          | 取余数   |
  | ++         | 自增     |
  | --         | 自减     |

* 赋值运算符

  | 赋值运算符 | 说明     |
  | ---------- | -------- |
  | =          | 加法运算 |
  | +=         | 减法运算 |
  | -=         | 乘法运算 |
  | *=         | 除法运算 |
  | /=         | 取余数   |
  | %=         | 自增     |

* 比较运算符

  | 比较运算符 | 说明                     |
  | ---------- | ------------------------ |
  | ==         | 判断值是否相等           |
  | ===        | 判断数据类型和值是否相等 |
  | >          | 大于                     |
  | >=         | 大于等于                 |
  | <          | 小于                     |
  | <=         | 小于等于                 |
  | !=         | 不等于                   |

* 逻辑运算符

  | 逻辑运算符 | 说明               |
  | ---------- | ------------------ |
  | &&         | 逻辑与，并且的功能 |
  | \|\|       | 逻辑或，或者的功能 |
  | !          | 取反               |

* **三元运算符**

  * 三元运算符格式：(比较表达式) ? 表达式1 : 表达式2; 
  * 格式说明：
    如果比较表达式为true，则取表达式1
    如果比较表达式为false，则取表达式2



***



### 流程控制

* **if语句**

  ```js
  let month = 3;
  if(month >= 3 && month <= 5) {
      document.write("春季");
  }else if(month >= 6 && month <= 8) {
      document.write("夏季");
  }else if(month == 12 || month == 1 || month == 2) {
      document.write("冬季");
  }else {
      document.write("月份有误");
  }
  
  document.write("<br>");
  ```

* **switch语句**

  ```js
  switch(sex){
      case 1:
          document.write("男性");
          break;
      case 2:
          document.write("女性");
          break;
      default:
          document.write("性别有误");
          break;
  }
  ```

* **for循环**

  ```js
  for(let i = 1; i <= 5; i++) {
      document.write(i + "<br>");
  }
  ```

* **while循环**

  ```js
  let n = 6;
  while(n <= 10) {
      document.write(n + "<br>");
      n++;
  }
  ```




***



### 数组

数组的使用和 java 中的数组基本一致，在JavaScript 中的数组更加灵活，数据类型和长度都没有限制

* 定义格式

  ```js
  let 数组名 = [元素1,元素2,…];
  ```

* 索引范围：从 0 开始，最大到数组长度-1

* 数组长度：数组名.length

  ```js
  let arr = [10,20,30]; 
  document.write(arr+"<br>")// 直接输出：10,20,30
  for(let i = 0; i < arr.length; i++) {
      document.write(arr[i] + "<br>");
  }
  ```

* 数组高级运算符：...

  * 数组赋值

    ```js
    let arr2 = [...arr];
    ```

  * 合并数组

    ```js
    let arr3 = [40,50,60];
    let arr4 = [...arr2 , ...arr3];
    ```

  * 字符串转数组

    ```js
    let arr5 = [..."JavaScript"];
    ```




***



### 函数

函数类似于 java 中的方法，可以将一些代码进行抽取，达到复用的效果

* 定义格式：

  ```js
  function 方法名(参数列表) {
      方法体; 
      return 返回值; 
  }
  ```

* 调用：

  ```js
  let 变量 = 方法名();
  方法名();
  ```

* 可变参数：

  ```js
  function 方法名(... 参数名) {
      方法体; 
      return 返回值; 
  }
  ```

* 匿名函数

  ```js
  function(参数列表) {
      方法体; 
  }
  ```





***



## DOM

### DOM介绍

DOM(Document Object Model)：文档对象模型。

将 HTML 文档的各个组成部分，封装为对象。借助这些对象，可以对 HTML 文档进行增删改查的动态操作。

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/DOM介绍.png)





***



### 元素获取

Element元素的获取操作：document接口方法

| 方法                                | 说明                                  |
| ----------------------------------- | ------------------------------------- |
| getElementById(id属性值)            | 根据id属性值获取元素对象              |
| getElementsByTagName(标签名称)      | 根据标签名称获取元素对象，返回数组    |
| getElementsByClassName(class属性值) | 根据class属性值获取元素对象，返回数组 |
| getElementsByName(name属性值)       | 根据name属性值获取元素对象，返回数组  |
| 子元素对象.parentElement属性        | 获取当前元素的父元素                  |

```html
<body>
    <div id="div1">div1</div>
    <div id="div2">div2</div>
    <div class="cls">div3</div>
    <div class="cls">div4</div>
    <input type="text" name="username"/>
</body>
<script>
    let div1 = document.getElementById("div1");//根据id属性值获取元素对象
    //alert(div1);//[object HTMLDivElement]

    let body = div1.parentElement;//获取当前元素的父元素
    alert(body);
</script>
</html>
```



***



### 元素增删改

Element元素的增删改操作：

| 方法                         | 说明                       |
| ---------------------------- | -------------------------- |
| createElement(标签名)        | 创建一个新的标签元素       |
| appendChild(子元素)          | 将指定子元素添加到父元素中 |
| removeChild(子元素)          | 用父元素删除指定子元素     |
| replaceChild(新元素, 旧元素) | 用新元素替换子元素         |
| createTextNode(数据)         | 创建文本元素               |

```html
<body>
    <select id="s">
        <option>---请选择---</option>
        <option>北京</option>
    </select>
</body>
<script>
    let option = document.createElement("option");//创建新的元素
    option.innerText = "深圳";//为option添加文本内容
 
    let select = document.getElementById("s");
    select.appendChild(option);//将子元素添加到父元素中

    let option2 = document.createElement("option");
    option2.innerText = "杭州";
    select.replaceChild(option2,option);//用新元素替换老元素
</script>
```



***



### 属性操作

Attribute属性的操作：

| 方法                         | 说明                     |
| ---------------------------- | ------------------------ |
| setAttribute(属性名, 属性值) | 设置属性                 |
| getAttribute(属性名)         | 根据属性名获取属性值     |
| removeAttribute(属性名)      | 根据属性名移除指定的属性 |
| 元素名.style属性             | 为元素添加样式           |
| 元素名.className属性         | 为元素添加指定样式       |

```css
.aColor{
    color: blue;
}/*获取写在<style>标签*/
```

```html
<body>
    <a>点我呀</a>
</body>
<script>
    let a = document.getElementsByTagName("a")[0];//因为是数组
    a.setAttribute("href","https://www.baidu.com");//添加属性

    let value = a.getAttribute("href");//获取属性

    //a.style.color = "red";//添加样式
    a.className = "aColor";//添加指定CSS样式
</script>
```



***



### 文本操作

* Text文本的操作： 

  | 属性名    | 说明                       |
  | --------- | -------------------------- |
  | innerText | 元素的文本内容，不解析标签 |
  | innerHTML | 元素的文本内容，解析标签   |

  类似于赋值操作，同时支持取用该值

  ```html
  <body>
      <div id="div"></div>
  </body>
  <script>
      //1. innerText   添加文本内容，不解析标签
      let div = document.getElementById("div");
      div.innerText = "我是div";
  
      //2. innerHTML   添加文本内容，解析标签
      div.innerHTML = "<b>我是div</b>";
  </script>
  ```

* 输入框文本：input元素.value;







***



## 事件

### 事件介绍

事件指的就是当某些组件执行了某些操作后，会触发某些代码的执行

* 常用的事件：

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/JS常用的事件.png)

* 更多的事件：

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/JS更多的事件.png)



***



### 事件操作

绑定事件的方式

* 方式一：通过标签中的事件属性进行绑定

* 方式二：通过 DOM 元素属性绑定

  ```html
  <body>
      <img id="img" src="img/01.png"/>
      <br>
      <!-- <button id="up" onclick="up()">上一张</button> 
      <button id="down" onclick="down()">下一张</button> -->
      <button id="up">上一张</button> <!--图片 上一张 下一张  类似百度图库-->
      <button id="down">下一张</button>
  </body>
  <script>
      //显示第一张图片的方法
      function up(){
          let img = document.getElementById("img");
          img.setAttribute("src","img/01.png");
      }
  
      //显示第二张图片的方法
      function down(){
          let img = document.getElementById("img");
          img.setAttribute("src","img/02.png");
      }
  
      //为上一张按钮绑定单击事件
      let upBtn = document.getElementById("up");
      upBtn.onclick = up;
  
      //为下一张按钮绑定单击事件
      let downBtn = document.getElementById("down");
      downBtn.onclick = down;
  </script>
  </html>
  ```

  

***



### 综合案例

案例介绍：

在姓名、年龄、性别三个文本框中填写信息后，添加到“学生信息表”列表（表格），点击删除后，删除该行数据，并且不需刷新

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/事件案例效果.png)



* 添加功能分析

  1. 为添加按钮绑定单击事件
  2. 创建 tr 元素
  3. 创建 4 个 td 元素
  4. 将 td 添加到 tr 中
  5. 获取文本框输入的信息
  6. 创建 3 个文本元素
  7. 将文本元素添加到对应的 td 中
  8. 创建 a 元素
  9. 将 a 元素添加到对应的 td 中
  10. 将 tr 添加到 table 中

* 删除功能分析

  1. 为每个删除超链接添加单击事件属性
  2. 定义删除的方法
  3. 获取 table 元素
  4. 获取 tr 元素
  5. 通过 table 删除 tr

* HTML

  ```html
  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>动态表格</title>
      <link rel="stylesheet" href="../css/table.css"/>
  </head>
  <body>
  <div>
      <input type="text" id="name" placeholder="请输入姓名" autocomplete="off">
      <input type="text" id="age"  placeholder="请输入年龄" autocomplete="off">
      <input type="text" id="gender"  placeholder="请输入性别" autocomplete="off">
      <input type="button" value="添加" id="add">
  </div>
  
      <table id="tb">
          <caption>学生信息表</caption>
          <tr>
              <th>姓名</th>
              <th>年龄</th>
              <th>性别</th>
              <th>操作</th>
          </tr>
          <tr>
              <td>张三</td>
              <td>23</td>
              <td>男</td>
              <td><a href="JavaScript:void(0);"onclick="drop(this)">删除</a></td>
          </tr>
      </table>
  </body>
  <script>
      //一、添加功能
      //1.为添加按钮绑定单击事件
      document.getElementById("add").onclick = function(){
          //2.创建行元素
          let tr = document.createElement("tr");
          //3.创建4个单元格元素
          let nameTd = document.createElement("td");
          let ageTd = document.createElement("td");
          let genderTd = document.createElement("td");
          let deleteTd = document.createElement("td");
          //4.将td添加到tr中
          tr.appendChild(nameTd);
          tr.appendChild(ageTd);
          tr.appendChild(genderTd);
          tr.appendChild(deleteTd);
          //5.获取输入框的文本信息
          let name = document.getElementById("name").value;
          let age = document.getElementById("age").value;
          let gender = document.getElementById("gender").value;
          //6.根据获取到的信息创建3个文本元素
          let nameText = document.createTextNode(name);
          let ageText = document.createTextNode(age);
          let genderText = document.createTextNode(gender);
          //7.将3个文本元素添加到td中
          nameTd.appendChild(nameText);
          ageTd.appendChild(ageText);
          genderTd.appendChild(genderText);
          //8.创建超链接元素和显示的文本以及添加href属性
          let a = document.createElement("a");
          let aText = document.createTextNode("删除");
          a.setAttribute("href","JavaScript:void(0);");
          a.setAttribute("onclick","drop(this)");
          a.appendChild(aText);
          //9.将超链接元素添加到td中
          deleteTd.appendChild(a);
          //10.获取table元素，将tr添加到table中
          let table = document.getElementById("tb");
          table.appendChild(tr);
      }
  
      //二、删除的功能
      //1.为每个删除超链接标签添加单击事件的属性
      //2.定义删除的方法
      function drop(obj){
          //3.获取table元素
          let table = obj.parentElement.parentElement.parentElement;
          //4.获取tr元素
          let tr = obj.parentElement.parentElement;
          //5.通过table删除tr
          table.removeChild(tr);
      }
  </script>
  </html>
  ```

* CSS

  ```css
  table{
      border: 1px solid;
      margin: auto;
      width: 500px;
  }
  td,th{
      text-align: center;
      border: 1px solid;
  }
  div{
      text-align: center;
      margin: 50px;
  }
  ```






***



## 对象

### 类

* 定义格式：

  ```js
  class 类名{
      constructor(变量列表){
          变量赋值;
      }
      方法名(参数列表) {
          方法体;
          return 返回值;
      }
  }
  ```

* 使用格式

  ```js
  let 对象名 = new 类名(实际变量值);
  对象名.方法名();
  ```



* 字面量类

  ```js
  <script>
      //定义person
      let person = {
          name : "张三",
          age : 23,
          hobby : ["听课","学习"],
  
          eat : function() {
              document.write("吃饭...");
          }
      };
  
      //使用person
      document.write(person.name + "," + person.age + "," + person.hobby[0]+"<br>");
      person.eat();
  </script>
  ```




***



### 继承

- 继承：让类与类产生子父类的关系，子类可以使用父类有权限的成员。

- 继承关键字：extends

- 顶级父类：Object

```js
<script>
    //定义Person类
    class Person{
        //构造方法
        constructor(name,age){
            this.name = name;
            this.age = age;
        }
        //eat方法
        eat(){
            document.write("吃饭...");
        }
    }
    //定义Worker类继承Person
    class Worker extends Person{
        constructor(name,age,salary){
            super(name,age);
            this.salary = salary;
        }

        show(){
            document.write(this.name + "," + this.age + "," + this.salary + "<br>");
        }
    }
    //使用Worker
    let w = new Worker("张三",23,10000);
    w.show();
    w.eat();
</script>
```



****



### 内置对象

内置对象是 JavaScript 提供的带有属性和方法的特殊数据类型，常见的有普通类型、JSON和正则表达式



***



### 普通类型

#### 数字

* Number

  | 方法名            | 说明                       |
  | ----------------- | -------------------------- |
  | parseFloat(Sring) | 将传入的字符串转为浮点数   |
  | parseInt()        | 将传入的字符串整数转为整数 |

  ```js
  <script>
      //1. parseFloat()  将传入的字符串浮点数转为浮点数
      document.write(Number.parseFloat("3.14") + "<br>");
  
      //2. parseInt()    将传入的字符串整数转为整数
      document.write(Number.parseInt("100") + "<br>");
      document.write(Number.parseInt("200abc") + "<br>");//从数字开始转换，直到不是数字
  </script>
  ```

* Math

  | 方法名   | 说明                                            |
  | -------- | ----------------------------------------------- |
  | ceil(x)  | 向上取整                                        |
  | floor(x) | 向下取整                                        |
  | round(x) | 四舍五入为整数                                  |
  | random() | 随机数，返回的是0.0-1.0之间的范围（含头不含尾） |
  | pow(x,y) | 幂运算，x的y次方                                |

  ```js
  document.write(Math.pow(2,3) + "<br>"); // 8
  ```



***



#### 日期

* Date构造方法

  | 构造方法                                                     | 说明                   |
  | ------------------------------------------------------------ | ---------------------- |
  | Date()                                                       | 根据当前时间创建对象   |
  | Date(value)                                                  | 根据指定毫秒值创建对象 |
  | Date(year, month, [day, hours, minutes, seconds, milliseconds]) | 根据指定字段创建对象   |

* Date成员方法

  | 成员方法         | 说明                                              |
  | ---------------- | ------------------------------------------------- |
  | getFullYear()    | 获取年份                                          |
  | getMonth()       | 获取月份                                          |
  | getDate()        | 获取天数，相对于月份                              |
  | getHours()       | 获取小时                                          |
  | getMinutes()     | 获取分钟                                          |
  | getSeconds()     | 获取秒数                                          |
  | getTime()        | 返回据1970年1月1日至今的毫秒数                    |
  | toLocaleString() | 返回本地日期格式的字符串<br />2021/2/3下午8:20:20 |

  

***



#### 字符串

String

* 构造方法

  | 构造方法           | 说明                   |
  | ------------------ | ---------------------- |
  | gengenString(vale) | 根据指定字符串创建对象 |
  | let s = "字符串"   | 直接赋值               |

* 成员方法

  | 成员方法              | 说明                                     |
  | --------------------- | ---------------------------------------- |
  | length                | 获取字符串长度                           |
  | charAt(index)         | 获取指定索引处的字符                     |
  | indexOf(value)        | 获取指定字符串出现的索引位置，找不到为-1 |
  | substring(start, end) | 根据指定索引范围截取字符串（含头不含尾） |
  | split(value)          | 根据指定规则切割字符串，返回数组         |
  | replace(old, new)     | 使用新字符替换老字符串                   |



***



#### 数组集合

* Array

  | 方法            | 说明                     |
  | --------------- | ------------------------ |
  | push(value)     | 添加元素到数组的末尾     |
  | pop()           | 删除数组末尾的元素       |
  | shift()         | 删除数组最前面的元素     |
  | includes(value) | 判断数组是否包含给定的值 |
  | reverse()       | 反转数组中的元素         |
  | sort()          | 对数组元素进行升序排序   |
  | length          | 返回数组的长度           |

  **降序排序：先sort，再reverse**

* Set：JavaScript中的Set集合，元素唯一，存取顺序一致

  | 方法          | 说明                             |
  | ------------- | -------------------------------- |
  | Set()         | 创建Set集合对象                  |
  | add(value)    | 向集合中添加元素                 |
  | size          | 获取集合长度                     |
  | keys()        | 获取迭代器对象（遍历方法看实例） |
  | delete(value) | 删除指定元素                     |

  ```js
  let s = new Set();
  // add(元素)   添加元素
  s.add("a");s.add("b");s.add("c");s.add("c");
  
  // keys()      获取迭代器对象
  let st = s.keys();
  //遍历集合
  for(let i = 0; i < s.size; i++){
      document.write(st.next().value + "<br>");
  }
  ```

* Map：JavaScript 中的 Map 集合，key 唯一，存取顺序一致

  | 方法            | 说明              |
  | --------------- | ----------------- |
  | Map()           | 创建Map集合对象   |
  | set(key, value) | 向集合添加元素    |
  | size            | 获取集合长度      |
  | get(key)        | 根据key获取value  |
  | entries()       | 获取迭代器对象    |
  | delete(key)     | 根据key删除键值对 |




***



### JSON

#### JSON入门

JSON(JavaScript Object Notation)：是一种轻量级的数据交换格式。

- 基于 ECMAScript 规范的一个子集，采用完全独立于编程语言的文本格式来存储和表示数据
- 简洁和清晰的层次结构使得 JSON 成为理想的数据交换语言，易于人阅读和编写，同时也易于计算机解析和 生成，并有效的提升网络传输效率。

* 创建格式：
  **name是字符串类型**
  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/JSON创建格式.png)

* json常用方法

  | 方法            | 说明                           |
  | --------------- | ------------------------------ |
  | stringify(对象) | 将指定对象转换为json格式字符串 |
  | parse(字符串)   | 将指定json格式字符串解析成对象 |

* 入门案例

  ```js
   //定义天气对象
  let weather = {
      city : "北京",
      date : "2088-08-08",
      wendu : "10° ~ 23°",
  };
  
  //1.将天气对象转换为JSON格式的字符串
  let str = JSON.stringify(weather);
  document.write(str + "<br>");
  
  //2.将JSON格式字符串解析成JS对象
  let weather2 = JSON.parse(str);
  document.write("城市：" + weather2.city + "<br>");
  ```

  



***



#### 转换工具

我们除了可以在 JavaScript 中来使用 JSON 以外，在 JAVA 中同样也可以使用 JSON。

JSON 的转换工具是通过 JAVA 封装好的一些 JAR 工具包，可以将 JAVA 对象或集合转换成 JSON 格式的字符串，也可以将 JSON 格式的字符串转成 JAVA 对象。

Jackson：开源免费的 JSON 转换工具，SpringMVC 转换默认使用 Jackson。

* 常用类

  | 类名          | 说明                                                         |
  | ------------- | ------------------------------------------------------------ |
  | ObjectMapper  | 是Jackson工具包的核心类，提供方法来实现JSON字符串和对象之间的转换 |
  | TypeReference | 对集合泛型的反序列化，使用TypeReference可以明确的指定反序列化的对象类型 |

* ObjectMapper常用方法

  | 方法                                                     | 说明                       |
  | -------------------------------------------------------- | -------------------------- |
  | String writeValueAsString(Object obj)                    | 将Java对象转换成JSON字符串 |
  | <T> T readValue(String json, Class<T> valueType)         | 将JSON字符串转换成Java对象 |
  | <T> T readValue(String json, TypeReference valueTypeRef) | 将JSON字符串转换成Java对象 |



方法练习：

* 对象转 JSON，JSON 转对象

  ```java
  public void test01() throws Exception{
      //User对象转json
      User user = new User("张三",23);
      String json = mapper.writeValueAsString(user);
      System.out.println("json字符串：" + json//json字符串 = {"name":"张三","age":23}
      //json转User对象
      User user2 = mapper.readValue(json, User.class);
      System.out.println("user对象：" + user2);//user对象 = User{name='张三', age=23}
  }
  ```

* Map转 JSON，JSON 转 Map

  ```java
  public void test02() throws Exception{
      //map<String,String>转json
      HashMap<String,String> map = new HashMap<>();
      map.put("姓名","张三");
      map.put("性别","男");
      String json = mapper.writeValueAsString(map);
      System.out.println("json字符串：" + json);
  
      //json转map<String,String>
      HashMap<String,String> map2 = mapper.readValue(json, HashMap.class);
      System.out.println("map对象：" + map2);
  }
  //json字符串 = {"姓名":"张三","性别":"男"}
  //map对象 = {姓名=张三, 性别=男}
  ```

* Map转 JSON，JSON 转 Map<自定义类>

  ```java
  public void test03() throws Exception{
      //map<String,User>转json
      HashMap<String,User> map = new HashMap<>();
      map.put("sea一班",new User("张三",23));
      map.put("sea二班",new User("李四",24));
      String json = mapper.writeValueAsString(map);
      System.out.println("json字符串：" + json);
  
      //json转map<String,User>
      HashMap<String,User> map2=mapper.readValue(json,
                                   new TypeReference<HashMap<String,User>>(){});
      System.out.println("java对象：" + map2);
  }
  //json字符串 = {"sea一班":{"name":"张三","age":23},"sea二班":{....}
  //map对象 = {sea一班=User{name='张三', age=23}, sea二班=User{name='李四', age=24}}
  ```

* List

  ```java
  public void test05() throws Exception{
      //List<User>转json
      ArrayList<User> list = new ArrayList<>();
      list.add(new User("张三",23));
      list.add(new User("李四",24));
      String json = mapper.writeValueAsString(list);
      System.out.println("json字符串：" + json);
  
      //json转List<User>
      ArrayList<User> list2 = mapper.readValue(json,
  								new TypeReference<ArrayList<User>>(){});
      System.out.println("java对象：" + list2);
  }
  //json字符串 = [{"name":"张三","age":23},{"name":"李四","age":24}]
  //list对象 = [User{name='张三', age=23}, User{name='李四', age=24}]
  ```




***



### 正则

#### 正则表达式

正则表达式：是一种对字符串进行匹配的规则

RegExp：

* 构造方法

  | 构造方法           | 说明                 |
  | ------------------ | -------------------- |
  | RegExp(规则)       | 根据指定规则创建对象 |
  | let reg = /^规则$/ | 直接赋值             |

* 成员方法

  | 成员方法           | 说明                           |
  | ------------------ | ------------------------------ |
  | test(匹配的字符串) | 根据指定规则验证字符串是否符合 |

  

#### 验证用户

使用 onsubmit 表单提交事件

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/表单校验.png)

```html
<form class="login-form" action="#" id="registered" method="get" autocomplete="off">
	<input type="text" id="username" name="username">
	<input type="password" id="password" name="password">
    <input type="submit" value="注册">
</form>
<script>
    //1.为表单绑定提交事件  匿名函数
    document.getElementById("registered").onsubmit = function() {
        //2.获取填写的用户名和密码
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;

        //3.判断用户名是否符合规则  4~16位纯字母
        let reg1 = /^[a-zA-Z]{4,16}$/;
        if(!reg1.test(username)) {
            alert("用户名不符合规则，请输入4到16位的纯字母！");
            return false;
        }

        //4.判断密码是否符合规则  6位纯数字
        let reg2 = /^[\d]{6}$/;
        if(!reg2.test(password)) {
            alert("密码不符合规则，请输入6位纯数字的密码！");
            return false;
        }
        //5.如果所有条件都满足，则提交表单
        return true;
    }
</script>
```





***





## BOM

### BOM介绍

BOM(Browser Object Model)：浏览器对象模型。

将浏览器的各个组成部分封装成不同的对象，方便我们进行操作。

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/BOM介绍.png)





***



### Window

Windows窗口对象：

- **定时器**
  - 唯一标识 setTimeout(功能，毫秒值)：设置一次性定时器。
  - clearTimeout(标识)：取消一次性定时器。
  - 唯一标识 setInterval(功能，毫秒值)：设置循环定时器。
  - clearInterval(标识)：取消循环定时器。
- 加载事件
  - window.onload：在页面加载完毕后触发此事件的功能

```js
<script>
    //一、定时器
    function fun(){
        alert("该起床了！");
    }

	//设置一次性定时器
    let d1 = setTimeout("fun()",3000);
    //取消一次性定时器
    clearTimeout(d1);

    //设置循环定时器，3秒弹出一次
    let d2 = setInterval("fun()",3000);
    //取消循环定时器
    clearInterval(d2);

    //加载事件
    let div = document.getElementById("div");
    alert(div);
</script>
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>window窗口对象</title>
    <script>
        function fun(){
            alert("该起床了！");
        }
        //加载事件
        window.onload = function(){
            let div = document.getElementById("div");
            alert(div);
        }
    </script>
</head>
<body>
    <div id="div">dddd</div>
</body>
</html>
```



***



### Location

Location地址栏对象：

* href 属性：浏览器的地址栏。我们可以通过为该属性设置新的URL，使浏览器读取并显示新URL的内容

实现效果：秒数会自动变小，倒计时，5，4，3，2，1

```html
<body>
    <p>
        注册成功！<span id="time">5</span>秒之后自动跳转到首页...
    </p>
</body>
<script>
    //1.定义方法。改变秒数，跳转页面
    let num = 5;
    function showTime() {
        num--;

        if(num <= 0) {
            //跳转首页
            location.href = "index.html";
        }
        let span = document.getElementById("time");
        span.innerText = num;
    }
    //2.设置循环定时器，每1秒钟执行showTime方法
    setInterval("showTime()",1000);
</script>
```





***



## 封装

封装思想：

- **封装：**将复杂的操作进行封装隐藏，对外提供更加简单的操作。

- **获取元素的方法**

  - document.getElementById(id值)：根据 id 值获取元素 
  - document.getElementsByName(name值)：根据 name 属性值获取元素们 
  - document.getElementsByTagName(标签名)：根据标签名获取元素们

代码实现：

* my.js

  ```js
  function getById(id){
      return document.getElementById(id);
  }
  
  function getByName(name) {
      return document.getElementsByName(name);
  }
  
  function getByTag(tag) {
      return document.getElementsByTagName(tag);
  }
  ```

* 封装.html

  ```html
  <body>
      <div id="div1">div1</div>
  </body>
  <script src="my.js"></script>	<!--引入js文件-->
  <script>
      let div1 = getById("div1");
      alert(div1);
  </script>
  ```






***

## JQuery

### 简介

jQuery 是一个 JavaScript 库

- 所谓的库，就是一个 JS 文件，里面封装了很多预定义的函数，比如获取元素，执行隐藏、移动等，目的就是在使用时直接调用，不需要再重复定义，这样就可以极大地简化了 JavaScript 编程。
- jQuery 官网：https://www.jquery.com



引入jQ文件

```html
<!--引入 jQuery 文件-->
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    //jQ语句
</script>
```

- jQuery 的核心语法 $()



***



### 语法

#### 对象转换

jQuery 本质上虽然也是 JS，但二者的 API 方法不能混合使用，若想使用对方的 API，需要进行对象的转换

* JS 的 DOM 对象转换成 jQuery 对象：$(JS的DOM对象);

  ```js
  // JS方式，通过id属性值获取div元素
  let jsDiv = document.getElementById("div");
  // 将 JS 对象转换为jQuery对象
  let jq = $(jsDiv);
  ```

* jQuery 对象转换成 JS 对象

  * jQuery对象[索引];
  * jQuery对象.get(索引);

  ```js
  //jQuery方式，通过id属性值获取div元素
  let jqDiv = $("#div");
  //将 jQuery对象转换为JS对象
  let js = jqDiv[0];
  ```

  

***



#### 事件操作

##### 绑定解绑

在 jQuery 中将事件封装成了对应的方法。去掉了 JS 中的 .on 语法

* 绑定事件：`jQuery对象.on(事件名称,执行的功能);`

  ```js
  //给btn1按钮绑定单击事件
  $("#btn1").on("click",function(){
  	alert("点我干嘛?");
  });
  ```

* 解绑事件：`jQuery对象.off(事件名称);`
  如果不指定事件名称，则会把该对象绑定的所有事件都解绑

  ```js
  //通过btn2解绑btn1的单击事件
  $("#btn2").on("click",function(){
  	$("#btn1").off("click");
  });
  ```






##### 事件切换

事件切换：需要给同一个对象绑定多个事件，而且多个事件还有先后顺序关系

* 方式一：单独定义

  * $(元素).事件方法名1(要执行的功能); 
  * $(元素).事件方法名2(要执行的功能);

  ```js
  //将鼠标移到某元素，添加css样式
  $("#div").mouseover(function(){
      //背景色：红色
      //$("#div").css("background","red");
      $(this).css("background","red");
  });
  $("#div").mouseout(function(){
      //背景色：蓝色
      $(this).css("background","blue");
  });
  ```

* 方式二：链式定义

  * $(元素).事件方法名1(要执行的功能) .事件方法名2(要执行的功能);

  ```js
  $("#div").mouseover(function(){
     $(this).css("background","red");
  }).mouseout(function(){
     $(this).css("background","blue");
  });
  ```




***



#### 遍历操作

* 数据准备，实现按键后遍历无序列表

  ```html
  <body>
      <input type="button" id="btn" value="遍历列表项">
      <ul>
          <li>传智播客</li>
          <li>黑马程序员</li>
          <li>传智专修学院</li>
      </ul>
  </body>
  ```

* for循环

  ```js
  for(let i = 0; i < 容器对象长度; i++){
  		执行功能;
  }
  ```

* 对象.each方法

  ```js
  容器对象.each(function(index,ele){
  	执行功能;
  });
  ```

  ```js
  $("#btn").click(function(){
      let lis = $("li");
      lis.each(function(index,ele){
          alert(index + ":" + ele.innerHTML);
      });
  });
  ```

* $.each()方法

  ```js
  $.each(容器对象,function(index,ele){
  	执行功能;
  });
  ```

  ```js
  $("#btn").click(function(){
      let lis = $("li");
      $.each(lis,function(index,ele){
          alert(index + ":" + ele.innerHTML);
      });
  });
  ```

* for of语句

  ```js
  $("#btn").click(function(){
      let lis = $("li");
      for(ele of lis){
          alert(ele.innerHTML);
      }
  });
  ```

  

***



### 选择器

#### 基本选择器

选择器：类似于 CSS 的选择器，可以帮助我们获取元素。

* 下面所有的A B均为标签名

| 选择器     | 语法                | 作用                                |
| ---------- | ------------------- | ----------------------------------- |
| 元素选择器 | $("元素的名称")     | 根据元素名称获取元素对象（数组）    |
| id选择器   | $("#id的属性值")    | 根据id属性值获取元素对象            |
| 类选择器   | $(".class的属性值") | 根据class属性值获取元素对象（数组） |



#### 层级选择器

| 选择器     | 语法       | 作用                       |
| ---------- | ---------- | -------------------------- |
| 后代选择器 | $("A B")   | A下的所有B，包括B的子级    |
| 子选择器   | $("A > B") | A下的所有B，不 包括B的子级 |
| 兄弟选择器 | $("A + B") | A相邻的下一个B             |
| 兄弟选择器 | $("A ~ B") | A相邻的所有B               |



#### 属性选择器

| 选择器       | 语法                  | 作用                                       |
| ------------ | --------------------- | ------------------------------------------ |
| 属性名选择器 | $("A[属性名]")        | 根据指定属性名获取元素对象（数组）         |
| 属性值选择器 | $("A[属性名=属性值]") | 根据指定属性名和属性值获取元素对象（数组） |



***



#### 过滤器选择器

| 选择器         | 语法             | 作用                           |
| -------------- | ---------------- | ------------------------------ |
| 首元素选择器   | $("A:first")     | 获取选择的元素中的第一个元素   |
| 尾元素选择器   | $("A:last")      | 获取选择的元素中的最后一个元素 |
| 非元素选择器   | $("A:not(B)")    | 不包括指定内容的元素           |
| 偶数选择器     | $("A:even")      | 偶数，从0开始计数              |
| 奇数选择器     | $("A:odd")       | 奇数，从0开始计数              |
| 等于索引选择器 | $("A:eq(index)") | 指定索引的元素                 |
| 大于索引选择器 | $("A:gt(index)") | 大于指定索引的元素             |
| 小于索引选择器 | $("A:lt(index)") | 小于指定索引的元素             |

```html
<body>
    <div>div1</div>
    <div id="div2">div2</div>
    <div>div3</div>
    <div>div4</div>
</body>
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    // 首元素选择器	$("A:first");
    let div1 = $("div:first");
    //alert(div1.html());

    // 非元素选择器	$("A:not(B)");
    let divs1 = $("div:not(#div2)");//数组

    // 偶数选择器	    $("A:even");
    let divs2 = $("div:even");
    alert(divs2.length);
    alert(divs2[0].innerHTML);
    alert(divs2[1].innerHTML);

    // 等于索引选择器	 $("A:eq(index)");
    let div3 = $("div:eq(2)");
    //alert(div3.html());
</script>
```



***



#### 表单属性选择器

| 选择器                  | 语法            | 作用                        |
| ----------------------- | --------------- | --------------------------- |
| 可用选择器              | $("A:enabled")  | 获得可用元素                |
| 不可用元素选择器        | $("A:disabled") | 获得不可用元素              |
| 单选/复选框被选中的元素 | $("A:checked")  | 获取单选/复选框被选中的元素 |
| 下拉框被选中的元素      | $("A:selected") | 获取下拉框被选中的元素      |

```html
<body>
    <input type="text" disabled>
    <input type="text" >
    <input type="radio" name="gender" value="men" checked>男
    <input type="radio" name="gender" value="women">女
    <input type="checkbox" name="hobby" value="study" checked>学习
    <input type="checkbox" name="hobby" value="sleep" checked>睡觉
    <select>
        <option>---请选择---</option>
        <option selected>本科</option>
        <option>专科</option>
    </select>
</body>
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    // 1.可用元素选择器  $("A:enabled");
    let ins1 = $("input:enabled");

    // 2.不可用元素选择器  $("A:disabled");
    let ins2 = $("input:disabled");

    // 3.单选/复选框被选中的元素  $("A:checked");
    let ins3 = $("input:checked");
    alert(ins3.length);
    alert(ins3[0].name);
    alert(ins3[1].value);

    // 4.下拉框被选中的元素   $("A:selected");
    let select = $("select option:selected");
    alert(select.html());  
</script>
```



***



### DOM

#### 文本操作

| 方法        | 作用                         |
| ----------- | ---------------------------- |
| html()      | 获取标签的文本               |
| html(value) | 设置标签的文本内容，解析标签 |

```js
//获取div标签的文本内容
let value = $("#div").html();
//设置div标签的文本内容
$("#div").html("<b>我是div</b>");
```



***



#### 对象操作

| 方法               | 作用                                                       |
| ------------------ | ---------------------------------------------------------- |
| $("元素")          | 创建指定元素                                               |
| append(element)    | 添加成最后一个子元素，由添加者对象调用                     |
| appendTo(element)  | 添加成最后一个子元素，由被添加者对象调用                   |
| prepend(element)   | 添加成第一个子元素，由添加者对象调用                       |
| prependTo(element) | 添加成第一个子元素，由被添加者对象调用                     |
| before(element)    | 添加到当前元素的前面，两者之间是兄弟关系，由添加者对象调用 |
| after(element)     | 添加到当前元素的后面，两者之间是兄弟关系，由添加者对象调用 |
| remove()           | 删除指定元素（自己移除自己）                               |
| empty()            | 清空指定元素的所有子元素（自己还在）                       |

<div id="div"></div>
<ul id="city">
    <li id="bj">北京</li>
    <li id="sh">上海</li>
</ul>
<ul id="desc">
    <li id="jy">加油</li>
</ul> 


```js
// 按钮一：添加一个span到div
$("#btn1").click(function(){
    let span = $("<span>span</span>");
    $("#div").append(span);
});
//按钮二：将加油添加到城市列表最下方
$("#btn2").click(function(){
    $("#city").append($("#jy"));
});
//按钮三：将加油添加到城市列表最上方
$("#btn3").click(function(){
    $("#jy").prependTo($("#city"));
});
//按钮四：将加油添加到北京下方
$("#btn4").click(function(){
    $("#bj").after($("#jy"));
});
```



***



#### 样式操作

| 方法               | 作用                                   |
| ------------------ | -------------------------------------- |
| css(name)          | 根据样式名称获取css样式                |
| css(name,value)    | 设置css样式                            |
| addClass(value)    | 给指定的对象添加样式类名               |
| removeClass(value) | 给指定的对象删除样式类名               |
| toggleClass(value) | 没有样式类名就添加，有就删除，循环如此 |

```css
.cls{
    background: pink;
}
```

```html
<div style="border: 1px solid red;" id="div">我是div</div>
```

```js
// 1.css(name)   获取css样式
$("#btn1").click(function(){
    alert($("#div").css("border"));  //1px solid rgb(255, 0, 0)
});

// 2.css(name,value)   设置CSS样式
$("#btn2").click(function(){
    $("#div").css("background","blue");
});

// 3.addClass(value)   给指定的对象添加样式类名
$("#btn3").click(function(){
    $("#div").addClass("cls");  //cls是一个css样式
});

// 4.toggleClass(value)  如果没有样式类名，则添加。如果有，则删除
$("#btn5").click(function(){
    $("#div").toggleClass("cls");
});
```



***



#### 属性操作

| 方法               | 作用                                   |
| ------------------ | -------------------------------------- |
| attr(name,[value]) | 获得/设置属性的值                      |
| prop(name,[value]) | 获得/设置属性的值（checked, selected） |

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>操作属性</title>
</head>
<body>
    <input type="text" id="username"> <br>
    <input type="button" id="btn1" value="获取输入框的id属性">  &nbsp;&nbsp;
    <input type="button" id="btn2" value="给输入框设置value属性"><br/>
    <input type="radio" id="gender1" name="gender">男
	<input type="radio" id="gender2" name="gender">女<br/>
	<input type="button" id="btn3" value="选中女"><br/><br/>
    <select>
    <option>---请选择---</option>
    <option id="bk">本科</option>
    <option id="zk">专科</option>
	</select><br/>
    <input type="button" id="btn4" value="选中本科">
</body>
</html>


```html
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    //按钮一：获取输入框的id属性  attr(name,[value])
    $("#btn1").click(function(){
        alert($("#username").attr("id"));
    });
    
    //按钮二：给输入框设置value属性  attr(name,[value])
    $("#btn2").click(function(){
        $("#username").attr("value","hello...");
    });
    
    //按钮三：选中女   prop(name,[value]) 
    $("#btn3").click(function(){
        $("#gender2").prop("checked",true);
    });

    //按钮四：选中本科  prop(name,[value]) 
    $("#btn4").click(function(){
        $("#bk").prop("selected",true);
    });
</script>
```





***

