# CSS

## CSS入门

### 概述

CSS (层叠样式表——Cascading Style Sheets，缩写为 **CSS**），简单的说，它是用于设置和布局网页的计算机语言。会告知浏览器如何渲染页面元素。例如，调整内容的字体，颜色，大小等样式，设置边框的样式，调整模块的间距等。

层叠：是指样式表允许以多种方式规定样式信息。可以规定在单个元素中，可以在页面头元素中，也可以在另一个CSS文件中，规定的方式会有次序的差别。

样式：是指丰富的样式外观。拿边框距离来说，允许任何设置边框，允许设置边框与框内元素的距离，允许设置边框与边框的距离等等。



***



### 组成

CSS是一门基于规则的语言—你能定义用于你的网页中**特定元素**的一组**样式规则**。这里面提到了两个概念，一是特定元素，二是样式规则。对应CSS的语法，也就是**选择器（*selects*）**和**声明（*eclarations*）**。

* 选择器：指定要添加样式的 HTML元素的方式。可以使用标签名，class值，id值等多种方式。
* 声明：形式为**属性(property):值(value)**，用于设置特定元素的属性信息。
  * 属性：指示文体特征，例如`font-size`，`width`，`background-color`。
  * 值：每个指定的属性都有一个值，该值指示您如何更改这些样式。

格式：

```css
选择器 {
    属性名:属性值;
    属性名:属性值;
    属性名:属性值;
}
```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS的组成.png)



***



### 实现

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>页面标题</title>
	<style>
        h1{
            font-size:40px; /* 设置字体大小为100像素*/
        }
    </style>
  </head>
  <body>
      <h1>今天开始学CSS</h1>
  </body>
</html>






***



## CSS语法

### 注释方式

CSS中的注释以`/*`和开头`*/`。

```css
/* 设置h1的样式 */
h1 {
  color: blue;
  background-color: yellow;
  border: 1px solid black;
}
```



***



### 引入方式

#### 内联样式

内联样式是CSS声明在元素的`style`属性中，仅影响一个元素：

* 格式：

  ```html
  <标签 style="属性名:属性值; 属性名:属性值;">内容</标签>
  ```

* 例如：

  ```html
  <h1 style="color: blue;background-color: yellow;border: 1px solid black;">
      Hello World!
  </h1>
  ```

* 效果：

  <h1 style="color: blue;background-color: yellow;border: 1px solid black;">
      Hello World!
  </h1>

* 特点：格式简单，但是样式作用无法复用到多个元素上，不利于维护



#### 内部样式表

内部样式表是将CSS样式放在[style](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/style)标签中，通常style标签编写在HTML 的[head](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/head)标签内部。

* 格式：

  ```html
  <head>
      <style>
          选择器 {
              属性名: 属性值;
              属性名: 属性值;
          }
      </style>
  </head>
  ```

* 例如：

  ```html
   <head>
      <style>
        h1 {
          color: blue;
          background-color: yellow;
          border: 1px solid black;
        }
      </style>
    </head>
  ```

* 特点：内部样式只能作用在当前页面上，如果是多个页面，就无法复用了



#### 外部样式表

外部样式表是CSS附加到文档中的最常见和最有用的方法，因为您可以将CSS文件链接到多个页面，从而允许您使用相同的样式表设置所有页面的样式。

外部样式表是指将CSS编写在扩展名为`.css` 的单独文件中，并从HTML`<link>` 元素引用它，通常link标签`编写在HTML 的[head]标签内部。

* 格式

  ```html
  <link rel="stylesheet" href="css文件">
  ```

  * rel：表示“关系 (relationship) ”，属性值指链接方式与包含它的文档之间的关系，引入css文件固定值为stylesheet。
  * href：属性需要引用某文件系统中的一个文件。

* 举例

  * 创建styles.css文件

    ```html
    h1 {
      color: blue;
      background-color: yellow;
      border: 1px solid black;
    }
    ```

  * link标签引入文件

    ```html
    <!DOCTYPE html>
    <html>
      <head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="styles.css">
      </head>
      <body>
        <h1>Hello World!</h1>
      </body>
    </html>
    ```

    效果同上

* 为了CSS文件的管理，在项目中创建一个`css文件夹`，专门保存样式文件，并调整指定的路径以匹配

  ```html
  <link rel="stylesheet" href="../css/styles.css">
  <!--..代表上一级 相对路径-->
  ```

  



#### 优先级

规则层叠于一个样式表中，其中数字 4 拥有最高的优先权：

1. 浏览器缺省设置
2. 外部样式表
3. 内部样式表（位于 <head> 标签内部）
4. 内联样式（在 HTML 元素内部）



***



### 选择器

#### 介绍选择器

为了样式化某些元素，我们会通过选择器来选中HTML文档中的这些元素，每个CSS规则都以一个选择器或一组选择器为开始，去告诉浏览器这些规则应该应用到哪些元素上。

选择器的分类：

| 分类       | 名称       | 符号   | 作用                                                         | 示例         |
| ---------- | ---------- | ------ | ------------------------------------------------------------ | ------------ |
| 基本选择器 | 元素选择器 | 标签名 | 基于标签名匹配元素                                           | div{ }       |
|            | 类选择器   | `.`    | 基于class属性值匹配元素                                      | .center{ }   |
|            | ID选择器   | `#`    | 基于id属性值匹配元素                                         | #username{ } |
|            | 通用选择器 | `*`    | 匹配文档中的所有内容                                         | *{ }         |
| 属性选择器 | 属性选择器 | `[]`   | 基于某属性匹配元素                                           | [type]{ }    |
| 伪类选择器 | 伪类选择器 | `:`    | 用于向某些选择器添加特殊的效果                               | a:hover{ }   |
| 组合选择器 | 分组选择器 | ,      | 使用 , 号结合两个选择器，匹配两个选择器的元素                | span,p{}     |
|            | 后代选择器 | 空格   | 使用空格符号结合两个选择器，基于<br />第一个选择器，匹配第二个选择器的所有后代元素 | .top li{ }   |



#### 基本选择器

* 页面元素：

  ```html
  <body>
      <div>div1</div>
  
      <div class="cls">div2</div>
      <div class="cls">div3</div>
  
      <div id="d1">div4</div>
      <div id="d2">div5</div>
  </body>
  ```

  

* 元素选择器

  ```css
  /*选择所有div标签,字体为蓝色*/
  div{
  	 color: red;
  }
  ```

* 类选择器

  ```css
  /*选择class为cls的,字体为蓝色*/
  .cls{
  	color: blue;
  }
  ```

* ID选择器

  ```css
  /*id选择器*/
  #d1{
      color: green;/*id为d1的字体变成绿色*/
  }
  
  #d2{
      color: pink;/*id为d2的字体变成粉色*/
  }/
  ```

* 通用选择器

  ```css
  /*所有标签 */
  *{
      background-color: aqua;
  }
  ```

  

***



#### 属性选择器

* 页面：

  ```html
  <body>
      用户名：<input type="text"/> <br/>
      密码：<input type="password"/> <br>
      邮箱：<input type="email"/> <br>
  </body>
  ```

* 选择器：

  ```css
  /*输入框中输入的字符是红色*/
  [type] {
      color: red;
  }
  /*输入框中输入的字符是蓝色*/
  [type=password] {
      color: blue;
  }
  ```




***



#### 伪类选择器

* 页面元素

  ```html
  <body>
      <a href="https://www.baidu.com" target="_blank">百度一下</a>
  </body>
  ```

* 伪类选择器

  ```css
  /*未访问的状态*/
  a:link{
  	color: black;
  }
  
  /*已访问的状态*/
  a:visited{
  	color: blue;
  }
  
  /*鼠标悬浮的状态*/
  a:hover{
  	color: red;
  }
  
  /*已选中的状态*/
  a:active{
  	color: yellow;
  }
  ```

* 注意：伪类顺序 link ，visited，hover，active，否则有可能失效。



***



#### 组合选择器

* 页面：

  ```html
  <body>
      <span>span</span> <br/>
      <p>段落</p>
      
      <div class="top">
          <ol>
              <li>aa</li>
              <li>bb</li>
          </ol>
      </div>
      <div class="center">
          <ol>
              <li>cc</li>
              <li>dd</li>
          </ol>
      </div>
  </body>
  ```

* 分组选择器

  ```css
  /*span p两个标签下的字体为蓝色*/
  span,p{
  	color: blue;
  }
  ```

* 后代选择器

  ```css
  /*class为top下的所有li标签字体颜色为红色*/
  .top li{
  	color: red;
  }
  ```




***



#### 优先级

选择器优先级

* ID选择器 > 类选择器 > 标签选择器 > 通用选择器
* 如果优先级相同，那么就满足就近原则



***



### 边框样式

#### 单个边框

* 单个边框
  border：边框
  border-top: 上边框
  border-left: 左边框
  border-bottom: 底边框
  border-right:  右边框

* 无边框，当border值为none时，可以让边框不显示

  ```css
  div {
  	width: 200px;
      height: 200px;
      border: none;
  }
  ```

* 圆角

  通过使用[`border-radius`]属性设置盒子的圆角，虽然能分别设置四个角，但是通常我们使用一个值，来设置整体效果

```css
#d1{
    /*设置所有边框*/
    /*border: 5px solid black;*/

    /*设置上边框*/
    border-top: 5px solid black;
    /*设置左边框*/
    border-left: 5px double red;
    /*设置右边框*/
    border-right: 5px dotted blue;
    /*设置下边框*/
    border-bottom: 5px dashed pink;

    width: 150px;
    height: 150px;
}

#d2{
    border: 5px solid red;
    /*设置边框的弧度*/
    border-radius: 25px;
    width: 150px;
    height: 150px;
}
```

```html
<body>
    <div id="d1"></div>
    <br/>
    <div id="d2"></div>
</body>
```

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS-边框样式效果图.png" style="zoom:80%;" />



****



#### 边框轮廓

轮廓**outline**：是绘制于元素周围的一条线，位于边框边缘的外围，可起到突出元素的作用

* 属性值：double：双实线   dotted：圆点   dashed：虚线   none：无

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>样式演示</title>
    <style>
        input{
            outline: dotted;
        }
    </style>
</head>
<body>
    用户名：<input type="text"/> <br/>
</body>
</html>
```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS边框轮廓效果图.png)

***



#### 盒子模型

##### 模型介绍

盒子模型是通过设置**元素框**与**元素内容**和**外部元素**的边距，而进行布局的方式。

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS盒子模型.png)

- element : 元素。
- padding : 内边距，也有资料将其翻译为填充。
- border : 边框。
- margin : 外边距，也有资料将其翻译为空白或空白边。



##### 边距

内边距、边框和外边距都是可选的，默认值是零。在 CSS 中，width 和 height 指的是内容区域的宽度和高度。

* 外边距
  单独设置边框的外边距，设置上、右、下、左方向：

  ```css
  margin-top
  margin-right
  margin-bottom
  margin-left
  ```

  * ```css
    margin:  auto /*浏览器自动计算外边距，具有居中效果。*/
    ```

  * 一个值

    ```css
    /*  所有 4 个外边距都是 10px */
    margin:10px;
    ```

  * 两个值

    ```css
    margin:10px 5px;/* 上外边距和下外边距是 10px*/
    margin:10px auto;/*右外边距和左外边距是 5px */
    ```

  * 三个值

    ```css
    /* 上外边距是 10px，右外边距和左外边距是 5px，下外边距是 15px*/
    margin:10px 5px 15px;
    ```

  * 四个值

    ```css
    /*上外边距是 10px，右外边距是 5px，下外边距是 15px，左外边距是 20px*/
    /*上右下外*/
    margin:10px 5px 15px 20px;
    ```

    

* 内边距
  与外边距类似，单独设置边框的内边距，设置上、右、下、左方向：

  ```css
  padding-top
  padding-right
  padding-bottom
  padding-left
  ```

  

##### 布局

* 基本布局

  ```html
  <style>
         div{
             border: 2px solid blue;
         }
         .big{
             width: 200px;
             height: 200px;
         }
         .small{
             width: 100px;
             height: 100px;
             margin: 30px;/*  外边距 */
         }
  </style>
  
  <div class="big">
      <div class="small">
      </div>
  </div
  ```

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS盒子模式-效果图1.png)



* 增加内边距会增加元素框的总尺寸

  ```css
   <style>
  	div{
      	border: 2px solid blue;
  	}
  	.big{
      	width: 200px;
      	height: 200px;
      	padding: 30px;/*内边距 */
  	}
  	.small{
  		width: 100px;
      	height: 100px;
  	}
  </style>
  ```

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS盒子模式-效果图2.png)



***



### 文本样式

#### 基本属性

| 属性名          | 作用         | 属性取值                                                     |
| --------------- | ------------ | ------------------------------------------------------------ |
| width           | 宽度         |                                                              |
| height          | 高度         |                                                              |
| color           | 颜色         |                                                              |
| font-family     | 字体样式     | 宋体、楷体                                                   |
| font-size       | 字体大小     | px : 像素，文本高度像素绝对数值。<br />em : 1em等于当前元素的父元素设置的字体大小，是相对数值 |
| text-decoration | 下划线       | underline : 下划线  <br/>overline : 上划线 <br/>line-through : 删除线 <br/>none : 不要线条 |
| text-align      | 文本水平对齐 | lef : 左对齐文本<br />right : 右对齐文本<br />center : 使文本居中 <br />justify : 使文本散布，改变单词间的间距，使文本所有行具有相同宽度。 |
| line-height     | 行高，行间距 |                                                              |
| vertical-align  | 文本垂直对齐 | top：居上   bottom：居下  middle：居中   或者百分比          |
| display         | 元素如何显示 | 可以设置块级和行内元素的切换，也可以设置元素隐藏<br />inline：内联元素(无换行、无长宽)   <br />block：块级元素(有换行)  <br />inline-block：内联元素(有长宽)  <br />none：隐藏元素 |

```css
div{
    color: /*red*/ #ff0000;
    font-family: /*宋体*/ 微软雅黑;
    font-size: 25px;/
    text-decoration: none;
    text-align: center;
    line-height: 60px;
}

span{
    /*文字垂直对齐  top：居上   bottom：居下  middle：居中   百分比*/
    vertical-align: 50%;     /*居中对齐*/
}
```

```html
<div>
    我是文字
</div>
<div>
    我是文字
</div>

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/5add00ba-3cd1-442f-af43-c73c05d7096d.png)
<span>微信</span>
```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/CSS-文本样式效果图.png)



***



#### 文本显示

* 元素显示

  ```css
  /*   把列表项显示为内联元素，无长宽*/
  li {
      display:inline;
  }
  /*   把span元素作为块元素，有换行*/
  span {
      display:block;
  }
  /*   行内块元素，结合的行内和块级的优点，既可以行内显示，又可以设置长宽，*/
  li {
      display:inline-block;
  }
  /*所有div在一行显示*/
  div{
      display: inline-block;
      width: 100px;
  }
  ```

  

* 元素隐藏

  当设置为none时，可以隐藏元素。



****



## CSS案例

```css
/*背景图片*/
body{
    background: url("../img/bg.png");
}

/*中间表单样式*/
.center{
    background: white;      /*背景色*/
    width: 40%;             /*宽度*/
    margin: auto;           /*水平居中外边距*/
    margin-top: 100px;      /*上外边距*/
    border-radius: 15px;    /*边框弧度*/
    text-align: center;     /*文本水平居中*/
}

/*表头样式*/
thead th{
    font-size: 30px;    /*字体大小*/
    color: orangered;   /*字体颜色*/
}

/*表体提示信息样式*/
tbody label{
    font-size: 20px;    /*字体大小*/
}

/*表体输入框样式*/
tbody input{
    border: 1px solid gray; /*边框*/
    border-radius: 5px;     /*边框弧度*/
    width: 90%;             /*输入框的宽度*/
    height: 40px;           /*输入框的高度*/
    outline: none;          /*取消轮廓的样式*/
}

/*表底确定按钮样式*/
tfoot button{
    border: 1px solid crimson;  /*边框*/
    border-radius: 5px;         /*边框弧度*/
    width: 95%;                 /*宽度*/
    height: 40px;               /*高度*/
    background: crimson;        /*背景色*/
    color: white;               /*文字的颜色*/
    font-size: 20px;            /*字体大小*/
}

/*表行高度*/
tr{
    line-height: 60px;  /*行高*/
}

/*底部页脚样式*/
.footer{
    width: 35%; /*宽度*/
    margin: auto;   /*水平居中外边距*/
    font-size: 15px;    /*字体大小*/
    color: gray;    /*字体颜色*/
}

/*超链接样式*/
a{
    text-decoration: none;  /*去除超链接的下划线*/
    color: blue;            /*超链接颜色*/
}
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
    <link rel="stylesheet" href="../css/login.css"/>
</head>
<body>
    <!--顶部公司图标-->
    <div>
        ![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/17596c3d-b8eb-44fe-8d15-6583c72ddbb5.png)
    </div>

    <!--中间表单-->
    <div class="center">
        <form action="#" method="get" autocomplete="off">
            <table width="100%">
                <thead>
                    <tr>
                        <th colspan="2">账&nbsp;密&nbsp;登&nbsp;录<hr/></th>
                    </tr>
                </thead>

                <tbody>
                    <tr>
                        <td>
                            <label for="username">账号</label>
                        </td>
                        <td>
                            <input type="text" id="username" name="username" placeholder=" 请输入账号" required/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="password">密码</label>
                        </td>
                        <td>
                            <input type="password" id="password" name="password" placeholder=" 请输入密码" required/>
                        </td>
                    </tr>
                </tbody>

                <tfoot>
                    <tr>
                        <td colspan="2">
                            <button type="submit">确&nbsp;定</button>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </form>
    </div>

    <!--底部页脚-->
    <div class="footer">
        <br/><br/>
        登录/注册即表示您同意&nbsp;&nbsp;
        <a href="#" target="_blank">用户协议</a>&nbsp;&nbsp;
        和&nbsp;&nbsp;
        <a href="#" target="_blank">隐私条款</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="#" target="_blank">忘记密码?</a>
    </div>
</body>
</html>
```







***

