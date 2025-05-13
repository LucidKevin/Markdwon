# AJAX

## 概述

- AJAX(Asynchronous JavaScript And XML)：异步的 JavaScript 和 XML。 

- 不是一种新技术，而是多个技术综合，用于快速创建动态网页的技术。

- 一般的网页如果需要更新内容，必需重新加载个页面。而 AJAX 通过浏览器与服务器进行少量数据交换，就可以使网页实现异步更新。也就是在不重新加载整个页 面的情况下，对网页的部分内容进行**局部更新**。

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/AJAX网页局部更新.png)



***



## 实现AJAX

### JS方式

- 核心对象：XMLHttpRequest

  * 用于在后台与服务器交换数据。可以在不重新加载整个网页的情况下，对网页的某部分进行更新。

- 打开链接：open(method,url,async)

  - method：请求的类型 GET 或 POST
  - url：请求资源的路径

  * async：true(异步) 或 false(同步)。

- 发送请求：send(String params) 

  - params：请求的参数(POST 专用)

- 处理响应：onreadystatechange

  - readyState：0-请求未初始化，1-服务器连接已建立，2-请求已接收，3-请求处理中，4-请求已完成，且响应已就绪。

  * status：200-响应已全部 OK。

- 获得响应数据形式

  * responseText：获得字符串形式的响应数据。

  * responseXML：获得 XML 形式的响应数据。



鼠标移出输入框，判断用户名是否被注册：

* Servlet

  ```java
  @WebServlet("/userServlet")
  public class UserServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          //设置请求和响应的乱码
          req.setCharacterEncoding("UTF-8");
          resp.setContentType("text/html;charset=UTF-8");
  
          //1.获取请求参数
          String username = req.getParameter("username");
          //模拟服务器处理请求需要1秒钟
          Thread.sleep(5000);
          
          //2.判断姓名是否已注册
          if ("zhangsan".equals(username)) {
              resp.getWriter().write("<font color='red'>用户名已注册");
          } else {
              resp.getWriter().write("<font color='green'>用户名可用");
          }
      }
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          doGet(req, resp);
      }
  }
  ```

* html文件

  <!DOCTYPE html>
  <html lang="en">
  <head>
      <meta charset="UTF-8">
      <title>用户注册</title>
  </head>
  <body>
      <form autocomplete="off">
          姓名：<input type="text" id="username">
          <span id="uSpan"></span>
          <br>
          密码：<input type="password" id="password">
          <br>
          <input type="submit" value="注册">
      </form>
  </body>


  ```html
<script>
    //1.为姓名绑定失去焦点事件
    document.getElementById("username").onblur = function() {
        //2.创建XMLHttpRequest核心对象
        let xmlHttp = new XMLHttpRequest();

        //3.打开链接
        let username = document.getElementById("username").value;
        xmlHttp.open("GET", "userServlet?username=" + username, true);

        //4.发送请求
        xmlHttp.send();

        //5.处理响应
        xmlHttp.onreadystatechange = function() {
            //判断请求和响应是否成功
            if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                //将响应的数据显示到span标签
                document.getElementById("uSpan").innerHTML = xmlHttp.responseText;
            }
        }
    }
</script>
</html>
  ```

  



***



### JQ方式

**核心语法：**`$.ajax({name:value,name:value,…}); `

- url：请求的资源路径。
- async：是否异步请求，true-是，false-否 (默认是 true)。
- data：发送到服务器的数据，可以是**键值对或者 js 对象**形式。
- type：请求方式，POST 或 GET (默认是 GET)。
- dataType：预期的返回数据的类型，取值可以是 xml, html, js, json, text等。 
- success：请求成功时调用的回调函数。
- error：请求失败时调用的回调函数。

```html
<script src="js/jquery-3.3.1.min.js"></script>
<script>
    //1.为用户名绑定失去焦点事件
    $("#username").blur(function () {
        let username = $("#username").val();
        //2.jQuery的通用方式实现AJAX
        $.ajax({
            //请求资源路径
            url:"userServletxxx",
            //是否异步
            async:true,
            //请求参数
            data:"username="+username,
            //请求方式
            type:"POST",
            //数据形式
            dataType:"text",
            //请求成功后调用的回调函数
            success:function (data) {
                //将响应的数据显示到span标签
                $("#uSpan").html(data);
            },
            //请求失败后调用的回调函数
            error:function () {
                alert("操作失败...");
            }
        });
    });
</script>
```



***



## 分页知识

![分页知识](https://seazean.oss-cn-beijing.aliyuncs.com/img/Web/分页知识.png)







***



