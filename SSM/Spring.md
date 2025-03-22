#   Spring

## 概述

Spring 是分层的 JavaSE/EE 应用 full-stack 轻量级开源框架

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-框架介绍.png)

Spring 优点：

* 方便解耦，简化开发
* 方便集成各种框架
* 方便程序测试
* AOP 编程难过的支持
* 声明式事务的支持
* 降低 JavaEE API 的使用难度

体系结构：

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-体系结构.png)

参考视频：https://space.bilibili.com/37974444





***



## IoC

### 基本概述

- IoC（Inversion Of Control）控制反转，Spring 反向控制应用程序所需要使用的外部资源
- **Spring 控制的资源全部放置在 Spring 容器中，该容器称为 IoC 容器**（存放实例对象）
- 官方网站：https://spring.io/ → Projects → spring-framework → LEARN → Reference Doc

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-IOC介绍.png)



- 耦合（Coupling）：代码编写过程中所使用技术的结合紧密度，用于衡量软件中各个模块之间的互联程度
- 内聚（Cohesion）：代码编写过程中单个模块内部各组成部分间的联系，用于衡量软件中各个功能模块内部的功能联系
- 代码编写的目标：高内聚，低耦合。同一个模块内的各个元素之间要高度紧密，各个模块之间的相互依存度不紧密

***



### 入门项目

模拟三层架构中表现层调用业务层功能

- 表现层：UserApp 模拟 UserServlet（使用 main 方法模拟）

- 业务层：UserService

步骤：

1. 导入 spring 坐标（5.1.9.release）

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>5.1.9.RELEASE</version>
   </dependency>
   ```

2. 编写业务层与表现层（模拟）接口与实现类 service.UserService，service.impl.UserServiceImpl

   ```java
   public interface UserService {
   	//业务方法  
   	void save();
   }
   ```

   ```java
   public class UserServiceImpl implements UserService {
       public void save() {
           System.out.println("user service running...");
       }
   }
   ```

3. 建立 Spring 配置文件：resources.**applicationContext**.xml (名字一般使用该格式)

4. 配置所需资源（Service）为 Spring 控制的资源

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd">
       <!-- 1.创建spring控制的资源-->
       <bean id="userService" class="service.impl.UserServiceImpl"/>
   </beans>
   ```

5. 表现层（App）通过 Spring 获取资源（Service 实例）

   ```java
   public class UserApp {
       public static void main(String[] args) {
           //2.加载配置文件
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           //3.获取资源
           UserService userService = (UserService) ctx.getBean("userService");
           userService.save();//user service running...
       }
   }
   ```

   ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-IOC实现.png)



****



### XML开发

#### bean

##### 基本属性

标签：<bean> 标签，<beans> 的子标签

作用：定义 Spring 中的资源，受此标签定义的资源将受到 Spring 控制

格式：

```xml
<beans>
	<bean />
</beans>
```

基本属性

* id：bean 的名称，通过 id 值获取 bean (首字母小写)
* class：bean 的类型，使用完全限定类名
* name：bean 的名称，可以通过 name 值获取 bean，用于多人配合时给 bean 起别名

```xml
<bean id="beanId" name="beanName1,beanName2" class="ClassName"></bean>
```

```java
ctx.getBean("beanId") == ctx.getBean("beanName1") == ctx.getBean("beanName2")
```



***



##### 作用范围

作用：定义 bean 的作用范围

格式：

```xml
<bean scope="singleton"></bean>
```

取值：

- singleton：设定创建出的对象保存在 Spring 容器中，是一个单例的对象
- prototype：设定创建出的对象保存在 Spring 容器中，是一个非单例（原型）的对象
- request、session、application、 websocket ：设定创建出的对象放置在 web 容器对应的位置

Spring 容器中 Bean 的**线程安全**问题：

* 原型 Bean，每次创建一个新对象，线程之间并不存在 Bean 共享，所以不会有线程安全的问题

* 单例 Bean，所有线程共享一个单例实例 Bean，因此是存在资源的竞争，如果单例 Bean是一个**无状态 Bean**，也就是线程中的操作不会对 Bean 的成员执行查询以外的操作，那么这个单例 Bean 是线程安全的

  解决方法：开发人员来进行线程安全的保证，最简单的办法就是把 Bean 的作用域 singleton 改为 protopyte





***



##### 生命周期

作用：定义 bean 对象在初始化或销毁时完成的工作

格式：

```xml
<bean init-method="init" destroy-method="destroy></bean>
```

取值：bean 对应的类中对应的具体方法名

实现接口的方式实现初始化，无需配置文件配置 init-method：

* 实现 InitializingBean，定义初始化逻辑
* 实现 DisposableBean，定义销毁逻辑

注意事项：

- 当 scope=“singleton” 时，Spring 容器中有且仅有一个对象，init 方法在创建容器时仅执行一次
- 当 scope=“prototype” 时，Spring 容器要创建同一类型的多个对象，init 方法在每个对象创建时均执行一次
- 当 scope=“singleton” 时，关闭容器（`.close()`）会导致 bean 实例的销毁，调用 destroy 方法一次
- 当 scope=“prototype” 时，对象的销毁由垃圾回收机制 GC 控制，destroy 方法将不会被执行

bean 配置：

```xml
<!--init-method和destroy-method用于控制bean的生命周期-->
<bean id="userService3" scope="prototype" init-method="init" destroy-method="destroy" class="service.impl.UserServiceImpl"/>
```

业务层实现类：

```java
public class UserServiceImpl implements UserService{
    public UserServiceImpl(){
        System.out.println(" constructor is running...");
    }

    public void init(){
        System.out.println("init....");
    }

    public void destroy(){
        System.out.println("destroy....");
    }

    public void save() {
        System.out.println("user service running...");
    }
}
```

测试类：

```java
UserService userService = (UserService)ctx.getBean("userService3");
```





***



##### 创建方式

* 静态工厂

  作用：定义 bean 对象创建方式，使用静态工厂的形式创建 bean，兼容早期遗留系统的升级工作

  格式：

  ```xml
  <bean class="FactoryClassName" factory-method="factoryMethodName"></bean>
  ```

  取值：工厂 bean 中用于获取对象的静态方法名

  注意事项：class 属性必须配置成静态工厂的类名

  bean 配置：

  ```xml
  <!--静态工厂创建 bean-->
  <bean id="userService4" class="service.UserServiceFactory" factory-method="getService"/>
  ```

  工厂类：

  ```java
  public class UserServiceFactory {
      public static UserService getService(){
          System.out.println("factory create object...");
          return new UserServiceImpl();
      }
  }
  ```

  测试类：

  ```java
  UserService userService = (UserService)ctx.getBean("userService4");
  ```



* 实例工厂

  作用：定义 bean 对象创建方式，使用实例工厂的形式创建 bean，兼容早期遗留系统的升级工作

  格式：

  ```xml
  <bean factory-bean="factoryBeanId" factory-method="factoryMethodName"></bean>
  ```

  注意事项：

  - 使用实例工厂创建 bean 首先需要将实例工厂配置 bean，交由 Spring 进行管理

  - factory-bean 是实例工厂的 Id

  bean 配置：

  ```xml
  <!--实例工厂创建 bean，依赖工厂对象对应的 bean-->
  <bean id="factoryBean" class="service.UserServiceFactory2"/>
  <bean id="userService5" factory-bean="factoryBean" factory-method="getService"/>
  ```

  工厂类：

  ```java
  public class UserServiceFactory2 {
      public UserService getService(){
          System.out.println(" instance factory create object...");
          return new UserServiceImpl();
      }
  }
  ```




***



##### 获取Bean

ApplicationContext 子类相关API：

| 方法                                              | 说明                                             |
| ------------------------------------------------- | ------------------------------------------------ |
| String[] getBeanDefinitionNames()                 | 获取 Spring 容器中定义的所有 JavaBean 的名称     |
| BeanDefinition getBeanDefinition(String beanName) | 返回给定 bean 名称的 BeanDefinition              |
| String[] getBeanNamesForType(Class<?> type)       | 获取 Spring 容器中指定类型的所有 JavaBean 的名称 |
| Environment getEnvironment()                      | 获取与此组件关联的环境                           |





***



#### DI

##### 依赖注入

- IoC（Inversion Of Control）控制翻转，Spring 反向控制应用程序所需要使用的外部资源

- DI（Dependency Injection）依赖注入，应用程序运行依赖的资源由 Spring 为其提供，资源进入应用程序的方式称为注入，简单说就是利用反射机制为类的属性赋值的操作

  ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-DI介绍.png)

IoC 和 DI 的关系：IoC 与 DI 是同一件事站在不同角度看待问题



***



##### set 注入

标签：<property> 标签，<bean> 的子标签

作用：使用 set 方法的形式为 bean 提供资源

格式：

```xml
<bean>
	<property />
    <property />
    .....
</bean>
```

基本属性：

* name：对应 bean 中的属性名，要注入的变量名，要求该属性必须提供可访问的 set 方法（严格规范此名称是 set 方法对应名称，首字母必须小写）
* value：设定非引用类型属性对应的值，**不能与 ref 同时使用**
* ref：设定引用类型属性对应 bean 的 id ，不能与 value 同时使用

```xml
<property name="propertyName" value="propertyValue" ref="beanId"/>
```

代码实现：

* DAO 层：要注入的资源

  ```java
  public interface UserDao {
      public void save();
  }
  ```

  ```java
  public class UserDaoImpl implements UserDao{
      public void save(){
          System.out.println("user dao running...");
      }
  }
  ```

* Service 业务层

  ```java
  public interface UserService {
      public void save();
  }
  ```

  ```java
  public class UserServiceImpl implements UserService {
  	private UserDao userDao;
      private int num;
      
      //1.对需要进行注入的变量添加set方法
      public void setUserDao(UserDao userDao) {
          this.userDao = userDao;
      }
      
  	public void setNum(int num) {
          this.num = num;
      }
      
      public void save() {
          System.out.println("user service running..." + num);
          userDao.save();
          bookDao.save();
      }
  }
  ```

* 配置 applicationContext.xml

  ```xml
  <!--2.将要注入的资源声明为bean-->
  <bean id="userDao" class="dao.impl.UserDaoImpl"/>
  
  <bean id="userService" class="service.impl.UserServiceImpl">
  	<!--3.将要注入的引用类型的变量通过property属性进行注入，-->
      <property name="userDao" ref="userDao"/>
      <property name="num" value="666"/>
  </bean>
  ```

* 测试类

  ```java
  public class UserApp {
      public static void main(String[] args) {
          ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
          UserService userService = (UserService) ctx.getBean("userService");
          userService.save();
      }
  }
  ```



***



##### 构造注入

标签：<constructor-arg> 标签，<bean> 的子标签

作用：使用构造方法的形式为 bean 提供资源，兼容早期遗留系统的升级工作

格式：

```xml
<bean>
	<constructor-arg />
    .....<!--一个bean可以有多个constructor-arg标签-->
</bean>
```

属性：

* name：对应bean中的构造方法所携带的参数名
* value：设定非引用类型构造方法参数对应的值，不能与 ref 同时使用
* ref：设定引用类型构造方法参数对应 bean 的 id ，不能与 value 同时使用
* type：设定构造方法参数的类型，用于按类型匹配参数或进行类型校验
* index：设定构造方法参数的位置，用于按位置匹配参数，参数 index 值从 0 开始计数

```xml
<constructor-arg name="argsName" value="argsValue" />
<constructor-arg index="arg-index" type="arg-type" ref="beanId"/>
```

代码实现：

* DAO 层：要注入的资源

  ```java
  public class UserDaoImpl implements UserDao{
      private String username;
      private String pwd;
      private String driver;
  
      public UserDaoImpl(String driver,String username, String pwd) {
          this.driver = driver;
          this.username = username;
          this.pwd = pwd;
      }
      public void save(){
          System.out.println("user dao running..."+username+" "+pwd+" "+driver);
      }
  }
  ```

* Service 业务层：参考 set 注入

* 配置 applicationContext.xml

  ```xml
  <bean id="userDao" class="dao.impl.UserDaoImpl">
      <!--使用构造方法进行注入，需要保障注入的属性与bean中定义的属性一致-->
  	<!--一致指顺序一致或类型一致或使用index解决该问题-->
      <constructor-arg index="2" value="123"/>
      <constructor-arg index="1" value="root"/>
      <constructor-arg index="0" value="com.mysql.jdbc.Driver"/>
  </bean>
  
  <bean id="userService" class="service.impl.UserServiceImpl">
      <property name="userDao" ref="userDao"/>
      <property name="num" value="666"/>
  </bean>
  ```

  方式二：使用 UserServiceImpl 的构造方法注入

  ```xml
  <bean id="userService" class="service.impl.UserServiceImpl">
  	<constructor-arg name="userDao" ref="userDao"/>
  	<constructor-arg name="num" value="666666"/>
  </bean>
  ```

* 测试类：参考 set 注入



***



##### 集合注入

标签：<array> <list> <set> <map> <props>，<property> 或 <constructor-arg> 标签的子标签

作用：注入集合数据类型属性

格式：

```xml
<property>
	<list></list>
</property>
```

代码实现：

* DAO 层：要注入的资源

  ```java
  public interface BookDao {
      public void save();
  }
  ```

  ```java
  public class BookDaoImpl implements BookDao {
      private ArrayList al;
      private Properties properties;
      private int[] arr;
      private HashSet hs;
      private HashMap hm ;
  
      public void setAl(ArrayList al) {
          this.al = al;
      }
  
      public void setProperties(Properties properties) {
          this.properties = properties;
      }
  
      public void setArr(int[] arr) {
          this.arr = arr;
      }
  
      public void setHs(HashSet hs) {
          this.hs = hs;
      }
  
      public void setHm(HashMap hm) {
          this.hm = hm;
      }
  
      public void save() {
          System.out.println("book dao running...");
          System.out.println("ArrayList:" + al);
          System.out.println("Properties:" + properties);
          for (int i = 0; i < arr.length; i++) {
              System.out.println(arr[i]);
          }
          System.out.println("HashSet:" + hs);
          System.out.println("HashMap:" + hm);
      }
  }
  ```

* Service 业务层

  ```java
  public class UserServiceImpl implements UserService {
      private BookDao bookDao;
      
      public UserServiceImpl() {}
      
  	public void setBookDao(BookDao bookDao) {
          this.bookDao = bookDao;
      }
  
      public void save() {
          System.out.println("user service running..." + num);
          bookDao.save();
      }
  }
  ```

* 配置 applicationContext.xml

  ```xml
  <bean id="userService" class="service.impl.UserServiceImpl">
      <property name="bookDao" ref="bookDao"/>
  </bean>
  
  <bean id="bookDao" class="dao.impl.BookDaoImpl">
      <property name="al">
          <list>
              <value>seazean</value>
              <value>66666</value>
          </list>
      </property>
      <property name="properties">
          <props>
              <prop key="name">seazean666</prop>
              <prop key="value">666666</prop>
          </props>
      </property>
      <property name="arr">
          <array>
              <value>123456</value>
              <value>66666</value>
          </array>
      </property>
      <property name="hs">
          <set>
              <value>seazean</value>
              <value>66666</value>
          </set>
      </property>
      <property name="hm">
          <map>
              <entry key="name" value="seazean66666"/>
              <entry key="value" value="6666666666"/>
          </map>
      </property>
  </bean>
  ```

  

****



#### P

标签：<p:propertyName>，<p:propertyName-ref>

作用：为 bean 注入属性值

格式：

```xml
<bean p:propertyName="propertyValue" p:propertyName-ref="beanId"/>
```

开启 p 命令空间：开启 Spring 对 p 命令空间的的支持，在 beans 标签中添加对应空间支持

```xml
<beans xmlns="http://www.springframework.org/schema/beans"   		
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"    
       xmlns:p="http://www.springframework.org/schema/p"       
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans     
		https://www.springframework.org/schema/beans/spring-beans.xsd">
</beans>
```

实例：

```xml
<bean 
      id="userService"
      class="service.impl.UserServiceImpl"
      p:userDao-ref="userDao"
      p:bookDao-ref="bookDao"
      p:num="10"
	/>
```



****



#### SpEL

Spring 提供了对 EL 表达式的支持，统一属性注入格式

作用：为 bean 注入属性值

格式：

```xml
<property value="EL">
```

注意：所有属性值不区分是否引用类型，统一使用value赋值

所有格式统一使用  value=“#{}”

- 常量  #{10}  #{3.14}  #{2e5}  #{‘it’}

- 引用 bean  #{beanId}    

- 引用 bean 属性  #{beanId.propertyName}

- 引用 bean 方法  beanId.methodName().method2()

- 引用静态方法  T(java.lang.Math).PI

- 运算符支持  #{3 lt 4 == 4 ge 3}

- 正则表达式支持  #{user.name matches‘[a-z]{6,}’}

- 集合支持  #{likes[3]}

实例：

```xml
<bean id="userService" class="service.impl.UserServiceImpl">
        <property name="userDao" value="#{userDao}"/>
        <property name="bookDao" value="#{bookDao}"/>
        <property name="num" value="#{666666666}"/>
    </bean>
```



***



#### prop

Spring 提供了读取外部 properties 文件的机制，使用读取到的数据为 bean 的属性赋值

操作步骤：

1. 准备外部 properties 文件

2. 开启 context 命名空间支持

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd
           ">
   ```

3. 加载指定的 properties 文件

   ```xml
   <context:property-placeholder location="classpath:data.properties" />
   ```

4. 使用加载的数据

   ```xml
   <property name="propertyName" value="${propertiesName}"/>
   ```

- 注意：如果需要加载所有的 properties 文件，可以使用 `*.properties` 表示加载所有的 properties 文件

- 注意：读取数据使用 **${propertiesName}** 格式进行，其中 propertiesName 指 properties 文件中的属性名

代码实现：

* data.properties

  ```properties
  username=root
  pwd=123456
  ```

* DAO 层：注入的资源

  ```java
  public interface UserDao {
      public void save();
  }
  ```

  ```java
  public class UserDaoImpl implements UserDao{
      private String userName;
      private String password;
  
      public void setUserName(String userName) {
          this.userName = userName;
      }
      public void setPassword(String password) {
          this.password = password;
      }
  
      public void save(){
          System.out.println("user dao running..."+userName+" "+password);
      }
  }
  ```

* Service 业务层

  ```java
  public class UserServiceImpl implements UserService {
      private UserDao userDao;
      public void setUserDao(UserDao userDao) {
          this.userDao = userDao;
      }
      public void save() {
          System.out.println("user service running...");
          userDao.save();
      }
  }
  ```

* applicationContext.xml

  ```xml
  <context:property-placeholder location="classpath:*.properties"/>
  
  <bean id="userDao" class="dao.impl.UserDaoImpl">
      <property name="userName" value="${username}"/>
      <property name="password" value="${pwd}"/>
  </bean>
  
  <bean id="userService" class="service.impl.UserServiceImpl">
      <property name="userDao" ref="userDao"/>
  </bean>
  ```

* 测试类

  ```java
  public class UserApp {
      public static void main(String[] args) {
          ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
          UserService userService = (UserService) ctx.getBean("userService");
          userService.save();
      }
  }
  ```

  

***



#### import

标签：<import>，<beans>标签的子标签

作用：在当前配置文件中导入其他配置文件中的项

格式：

```xml
<beans>
    <import />
</beans>
```

属性：

* resource：加载的配置文件名

```xml
<import resource=“config2.xml"/>
```

Spring 容器加载多个配置文件：

* applicationContext-book.xml

  ```xml
  <bean id="bookDao" class="dao.impl.BookDaoImpl">
      <property name="num" value="1"/>
  </bean>
  ```

* applicationContext-user.xml

  ```xml
  <bean id="userDao" class="dao.impl.UserDaoImpl">
      <property name="userName" value="${username}"/>
      <property name="password" value="${pwd}"/>
  </bean>
  
  <bean id="userService" class="service.impl.UserServiceImpl">
      <property name="userDao" ref="userDao"/>
      <property name="bookDao" ref="bookDao"/>
  </bean>
  ```

* applicationContext.xml

  ```xml
  <import resource="applicationContext-user.xml"/>
  <import resource="applicationContext-book.xml"/>
  
  <bean id="bookDao" class="com.seazean.dao.impl.BookDaoImpl">
      <property name="num" value="2"/>
  </bean>
  ```

* 测试类

  ```java
  new ClassPathXmlApplicationContext("applicationContext-user.xml","applicationContext-book.xml");
  new ClassPathXmlApplicationContext("applicationContext.xml");
  ```

Spring 容器中的 bean 定义冲突问题

- 同 id 的 bean，后定义的覆盖先定义的

- 导入配置文件可以理解为将导入的配置文件复制粘贴到对应位置，程序执行选择最下面的配置使用

- 导入配置文件的顺序与位置不同可能会导致最终程序运行结果不同



***



#### 三方资源

##### Druid

第三方资源配置

* pom.xml

  ```xml
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.1.16</version>
  </dependency>
  ```

* applicationContext.xml

  ```xml
  <!--加载druid资源-->
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
      <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
      <property name="url" value="jdbc:mysql://192.168.2.185:3306/spring_db"/>
      <property name="username" value="root"/>
      <property name="password" value="123456"/>
  </bean>
  ```

* App.java

  ```java
  ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
  DruidDataSource datasource = (DruidDataSource) ctx.getBean("datasource");
  System.out.println(datasource);
  ```



****



##### Mybatis

Mybatis 核心配置文件消失

- 环境 environment 转换成数据源对象

- 映射 Mapper 扫描工作交由 Spring 处理

- 类型别名交由 Spring 处理

DAO 接口不需要创建实现类，MyBatis-Spring 提供了一个动态代理的实现 **MapperFactoryBean**，这个类可以让直接注入数据映射器接口到 service 层 bean 中，底层将会动态代理创建类

整合原理：利用 Spring 框架的 SPI 机制，在 META-INF 目录的 spring.handlers 中给 Spring 容器中导入 NamespaceHandler 类

* NamespaceHandler 的 init 方法注册 bean 信息的解析器 MapperScannerBeanDefinitionParser
* 解析器在 Spring 容器创建过程中去**解析 mapperScanner 标签**，解析出的属性填充到 MapperScannerConfigurer 中

* MapperScannerConfigurer 实现了 BeanDefinitionRegistryPostProcessor 接口，重写 postProcessBeanDefinitionRegistry() 方法，可以扫描到 MyBatis 的 Mapper



***



### 注解开发

#### 注解驱动

##### XML

启动注解扫描，加载类中配置的注解项：

```xml
<context:component-scan base-package="packageName1,packageName2"/>
```

说明：

- 在进行包扫描时，会对配置的包及其子包中所有文件进行扫描，多个包采用`,`隔开
- 扫描过程是以文件夹递归迭代的形式进行的
- 扫描过程仅读取合法的 Java 文件
- 扫描时仅读取 Spring 可识别的注解
- 扫描结束后会将可识别的有效注解转化为 Spring 对应的资源加入 IoC 容器
- 从加载效率上来说注解优于 XML 配置文件

注解：启动时使用注解的形式替代 xml 配置，将 Spring 配置文件从工程中消除，简化书写

缺点：为了达成注解驱动的目的，可能会将原先很简单的书写，变的更加复杂。XML 中配置第三方开发的资源是很方便的，但使用注解驱动无法在第三方开发的资源中进行编辑，因此会增大开发工作量

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/注解驱动示例.png)



***



##### 纯注解

注解配置类

名称：@Configuration、@ComponentScan

类型：类注解

作用：**设置当前类为 Spring 核心配置加载类**

格式：

```java
@Configuration
@ComponentScan({"scanPackageName1","scanPackageName2"})
public class SpringConfigClassName{
}
```

说明：

- 核心配合类用于替换 Spring 核心配置文件，此类可以设置空的，不设置变量与属性
- bean 扫描工作使用注解 @ComponentScan 替代，多个包用 `{} 和 ,` 隔开

加载纯注解格式上下文对象，需要使用 **AnnotationConfigApplicationContext**

```java
@Configuration
public class SpringConfig {
    @Bean
    public Person person() {
        return new Person1("lisi", 20);
    }
}

public class MainTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new 
            		AnnotationConfigApplicationContext(SpringConfig.class);
        //方式一：名称对应类名
        Person bean = applicationContext.getBean(Person.class);
        System.out.println(bean);
		
        //方式二：名称对应方法名 
        Person bean1 = (Person) applicationContext.getBean("person1");	
        
        //方法三：指定名称@Bean("person2")
    }
}
```



***



##### 扫描器

组件扫描过滤器

开发过程中，需要根据需求加载必要的 bean，排除指定 bean

名称：@ComponentScan

类型：**类注解**

作用：设置 Spring 配置加载类扫描规则

格式：

```java
@ComponentScan(
    value = {"dao","service"},			//设置基础扫描路径
    excludeFilters =					//设置过滤规则，当前为排除过滤
	@ComponentScan.Filter(				//设置过滤器
	    type= FilterType.ANNOTATION,  	//设置过滤方式为按照注解进行过滤
	    classes = Service.class)     	//设置具体的过滤项，过滤所有@Service修饰的bean
    )
)
```

属性：

* includeFilters：设置包含性过滤器 
* excludeFilters：设置排除性过滤器
* type：设置过滤器类型





***



#### 基本注解

##### 设置 bean

名称：@Component、@Controller、@Service、@Repository

类型：类注解，写在类定义上方

作用：设置该类为 Spring 管理的 bean

格式：

```java
@Component
public class ClassName{}
```

说明：@Controller、@Service 、@Repository 是 @Component 的衍生注解，功能同 @Component

属性：

- value（默认）：定义 bean 的访问 id



***



##### 作用范围

名称：@Scope

类型：类注解，写在类定义上方

作用：设置该类作为 bean 对应的 scope 属性

格式：

```java
@Scope
public class ClassName{}
```

相关属性

- value（默认）：定义 bean 的作用域，默认为 singleton，非单例取值 prototype



***



##### 生命周期

名称：@PostConstruct、@PreDestroy

类型：方法注解，写在方法定义上方

作用：设置该类作为 bean 对应的生命周期方法

示例：

```java
//定义bean，后面添加bean的id
@Component("userService")
//定义bean的作用域
@Scope("singleton")
public class UserServiceImpl implements UserService {
    //初始化
    @PostConstruct
    public void init(){
        System.out.println("user service init...");
    }
	//销毁
    @PreDestroy
    public void destroy(){
        System.out.println("user service destroy...");
    }
}
```

一个对象的执行顺序：Constructor >> @Autowired（注入属性） >> @PostConstruct（初始化逻辑）



***



##### 加载资源

名称：@Bean

类型：方法注解

作用：设置该方法的返回值作为 Spring 管理的 bean

格式：

```java
@Bean("dataSource")
public DruidDataSource createDataSource() {    return ……;    }
```

说明：

- 因为第三方 bean 无法在其源码上进行修改，使用 @Bean 解决第三方 bean 的引入问题

- 该注解用于替代 XML 配置中的静态工厂与实例工厂创建 bean，不区分方法是否为静态或非静态

- @Bean 所在的类必须被 Spring 扫描加载，否则该注解无法生效

相关属性

- value（默认）：定义 bean 的访问 id
- initMethod：声明初始化方法
- destroyMethod：声明销毁方法



***



#### 属性注入

##### 基本类型

名称：@Value

类型：属性注解、方法注解

作用：设置对应属性的值或对方法进行传参

格式：

```java
//@Value("${jdbc.username}")
@Value("root")
private String username;
```

说明：

- value 值**仅支持非引用类型数据**，赋值时对方法的所有参数全部赋值

- value 值支持读取 properties 文件中的属性值，通过类属性将 properties 中数据传入类中

- value 值支持 SpEL

- @value 注解如果添加在属性上方，可以省略 set 方法（set 方法的目的是为属性赋值）

相关属性：

- value（默认）：定义对应的属性值或参数值



****



##### 自动装配

###### 属性注入

名称：@Autowired、@Qualifier

类型：属性注解、方法注解

作用：设置对应属性的对象、对方法进行引用类型传参

格式：

```java
@Autowired(required = false)
@Qualifier("userDao")
private UserDao userDao;
```

说明：

- @Autowired 默认按类型装配，指定 @Qualifier 后可以指定自动装配的 bean 的 id

相关属性：

- required：**为 true （默认）表示注入 bean 时该 bean 必须存在**，不然就会注入失败抛出异常；为 false  表示注入时该 bean 存在就注入，不存在就忽略跳过

注意：在使用 @Autowired 时，首先在容器中查询对应类型的 bean，如果查询结果刚好为一个，就将该 bean 装配给 @Autowired 指定的数据，如果查询的结果不止一个，那么 @Autowired 会根据名称来查找，如果查询的结果为空，那么会抛出异常

解决方法：使用 required = false



***



###### 优先注入

名称：@Primary

类型：类注解

作用：设置类对应的 bean 按类型装配时优先装配

范例：

```java
@Primary
public class ClassName{}
```

说明：

- @Autowired 默认按类型装配，当出现相同类型的 bean，使用 @Primary 提高按类型自动装配的优先级，多个 @Primary 会导致优先级设置无效



***



###### 注解对比

名称：@Inject、@Named、@Resource

- @Inject 与 @Named 是 JSR330 规范中的注解，功能与 @Autowired 和 @Qualifier 完全相同，适用于不同架构场景
- @Resource 是 JSR250 规范中的注解，可以简化书写格式

@Resource 相关属性

- name：设置注入的 bean 的 id

- type：设置注入的 bean 的类型，接收的参数为 Class 类型

@Autowired 和 @Resource之间的区别：

*  @Autowired 默认是**按照类型装配**注入，默认情况下它要求依赖对象必须存在（可以设置 required 属性为 false）

*  @Resource 默认**按照名称装配**注入，只有当找不到与名称匹配的 bean 才会按照类型来装配注入



****



##### 静态注入

Spring 容器管理的都是实例对象，**@Autowired 依赖注入的都是容器内的对象实例**，在 Java 中 static 修饰的静态属性（变量和方法）是属于类的，而非属于实例对象

当类加载器加载静态变量时，Spring 上下文尚未加载，所以类加载器不会在 Bean 中正确注入静态类

```java
@Component
public class TestClass {
    @Autowired
    private static Component component;

    // 调用静态组件的方法
    public static void testMethod() {
        component.callTestMethod()；
    }  
}
// 编译正常，但运行时报java.lang.NullPointerException，所以在调用testMethod()方法时，component变量还没被初始化
```

解决方法：

* @Autowired 注解到**类的构造函数**上，Spring 扫描到 Component 的 Bean，然后赋给静态变量 component

  ```java
  @Component
  public class TestClass {
      private static Component component;
  
      @Autowired
      public TestClass(Component component) {
          TestClass.component = component;
      }
  
      public static void testMethod() {
          component.callTestMethod()；
      }
  }
  ```

* @Autowired 注解到**静态属性的 setter 方法**上

* 使用 @PostConstruct 注解一个方法，在方法内为 static 静态成员赋值

* 使用 Spring 框架工具类获取 bean，定义成局部变量使用

  ```java
  public class TestClass {
      // 调用静态组件的方法
     public static void testMethod() {
        Component component = SpringApplicationContextUtil.getBean("component");
        component.callTestMethod();
     }
  }
  ```



参考文章：http://jessehzx.top/2018/03/18/spring-autowired-static-field/



***



#### 文件读取

名称：@PropertySource

类型：类注解

作用：加载 properties 文件中的属性值

格式：

```java
@PropertySource(value = "classpath:filename.properties")
public class ClassName {
    @Value("${propertiesAttributeName}")
    private String attributeName;
}
```

说明：

- 不支持 * 通配符，加载后，所有 Spring 控制的 bean 中均可使用对应属性值，加载多个需要用 `{} 和 ,` 隔开

相关属性

- value（默认）：设置加载的 properties 文件名

- ignoreResourceNotFound：如果资源未找到，是否忽略，默认为 false





***



#### 加载控制

##### 依赖加载

@DependsOn

- 名称：@DependsOn

- 类型：类注解、方法注解

- 作用：控制 bean 的加载顺序，使其在指定 bean 加载完毕后再加载

- 格式：

  ```java
  @DependsOn("beanId")
  public class ClassName {
  }
  ```

- 说明：

  - 配置在方法上，使 @DependsOn 指定的 bean 优先于 @Bean 配置的 bean 进行加载

  - 配置在类上，使 @DependsOn 指定的 bean 优先于当前类中所有 @Bean 配置的 bean 进行加载

  - 配置在类上，使 @DependsOn 指定的 bean 优先于 @Component 等配置的 bean 进行加载

- 相关属性

  - value（默认）：设置当前 bean 所依赖的 bean 的 id

@Order

- 名称：@Order

- 类型：**配置类注解**

- 作用：控制配置类的加载顺序，值越小越先加载

- 格式：

  ```java
  @Order(1)
  public class SpringConfigClassName {
  }
  ```

@Lazy

- 名称：@Lazy

- 类型：类注解、方法注解

- 作用：控制 bean 的加载时机，使其延迟加载，获取的时候加载

- 格式：

  ```java
  @Lazy
  public class ClassName {
  }
  ```



****



##### 应用场景

@DependsOn

- 微信订阅号，发布消息和订阅消息的 bean 的加载顺序控制（先开订阅，再发布）

- 双 11 活动，零点前是结算策略 A，零点后是结算策略 B，策略 B 操作的数据为促销数据，策略 B 加载顺序与促销数据的加载顺序

@Lazy

- 程序灾难出现后对应的应急预案处理是启动容器时加载时机

@Order

- 多个种类的配置出现后，优先加载系统级的，然后加载业务级的，避免细粒度的加载控制





****



#### 整合资源

##### 导入

名称：@Import

类型：类注解

作用：导入第三方 bean 作为 Spring 控制的资源，这些类都会被 Spring 创建并放入 ioc 容器

格式：

```java
@Configuration
@Import(OtherClassName.class)
public class ClassName {
}
```

说明：

- @Import 注解在同一个类上，仅允许添加一次，如果需要导入多个，使用数组的形式进行设定
- 在被导入的类中可以继续使用 @Import 导入其他资源
- @Bean 所在的类可以使用导入的形式进入 Spring 容器，无需声明为 bean



***



##### Druid

* 加载资源

  ```java
  @Component
  public class JDBCConfig {
      @Bean("dataSource")
      public static DruidDataSource getDataSource() {
          DruidDataSource ds = new DruidDataSource();
          ds.setDriverClassName("com.mysql.jdbc.Driver");
          ds.setUrl("jdbc:mysql://192.168.2.185:3306/spring_db");
          ds.setUsername("root");
          ds.setPassword("123456");
          return ds;
      }
  }
  ```

* 导入资源

  ```java
  @Configuration
  @ComponentScan(value = {"service","dao"})
  @Import(JDBCConfig.class)
  public class SpringConfig {
  }
  ```

* 测试

  ```java
  DruidDataSource dataSource = (DruidDataSource) ctx.getBean("dataSource");
  System.out.println(dataSource);
  ```

  

***



##### Junit

Spring 接管 Junit 的运行权，使用 Spring 专用的 Junit 类加载器，为 Junit 测试用例设定对应的 Spring 容器

注意：

- 从 Spring5.0 以后，要求 Junit 的版本必须是4.12及以上

- Junit 仅用于单元测试，不能将 Junit 的测试类配置成 Spring 的 bean，否则该配置将会被打包进入工程中 

test / java / service / UserServiceTest

```java
//设定spring专用的类加载器
@RunWith(SpringJUnit4ClassRunner.class)
//设定加载的spring上下文对应的配置
@ContextConfiguration(classes = SpringConfig.class)
public class UserServiceTest {
    @Autowired
    private AccountService accountService;
    @Test
    public void testFindById() {
        Account account = accountService.findById(1);
        Assert.assertEquals("Mike", account.getName());
    }
}
```

pom.xml

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.1.9.RELEASE</version>
</dependency>
```





****



### IoC原理

#### 核心类

##### BeanFactory

ApplicationContext：

1. ApplicationContext 是一个接口，提供了访问 Spring 容器的 API

2. ClassPathXmlApplicationContext 是一个类，实现了上述功能

3. ApplicationContext 的顶层接口是 BeanFactory

4. BeanFactory 定义了 bean 相关的最基本操作

5. ApplicationContext 在 BeanFactory 基础上追加了若干新功能

**ApplicationContext 和 BeanFactory对比：**

* BeanFactory 和 ApplicationContext 是 Spring 的两大核心接口，都可以当做 Spring 的容器
* BeanFactory 是 Spring 里面最底层的接口，是 IoC 的核心，定义了 IoC 的基本功能，包含了各种 Bean 的定义、加载、实例化，依赖注入和生命周期管理。ApplicationContext 接口作为 BeanFactory 的子类，除了提供 BeanFactory 所具有的功能外，还提供了更完整的框架功能：

  * 继承 MessageSource，因此支持国际化
  * 资源文件访问，如 URL 和文件（ResourceLoader）。
  * 载入多个（有继承关系）上下文（即加载多个配置文件） ，使得每一个上下文都专注于一个特定的层次，比如应用的 web 层
  * 提供在监听器中注册 bean 的事件
* BeanFactory 创建的 bean 采用延迟加载形式，只有在使用到某个 Bean 时（调用 getBean），才对该 Bean 进行加载实例化（Spring 早期使用该方法获取 bean），这样就不能提前发现一些存在的 Spring 的配置问题；ApplicationContext 是在容器启动时，一次性创建了所有的 Bean，容器启动时，就可以发现 Spring 中存在的配置错误，这样有利于检查所依赖属性是否注入
* ApplicationContext 启动后预载入所有的单实例 Bean，所以程序启动慢，运行时速度快
* 两者都支持 BeanPostProcessor、BeanFactoryPostProcessor 的使用，但两者之间的区别是：BeanFactory 需要手动注册，而 ApplicationContext 则是自动注册

FileSystemXmlApplicationContext：加载文件系统中任意位置的配置文件，而 ClassPathXmlAC 只能加载类路径下的配置文件

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-ApplicationContext层级结构图.png)

BeanFactory 的成员属性：

```java
String FACTORY_BEAN_PREFIX = "&";
```

* 区分是 FactoryBean 还是创建的 Bean，加上 & 代表是工厂，getBean 将会返回工厂
* FactoryBean：如果某个 bean 的配置非常复杂，或者想要使用编码的形式去构建它，可以提供一个构建该 bean 实例的工厂，这个工厂就是 FactoryBean 接口实现类，FactoryBean 接口实现类也是需要 Spring 管理
  * 这里产生两种对象，一种是 FactoryBean 接口实现类（IOC 管理），另一种是 FactoryBean 接口内部管理的对象
  * 获取 FactoryBean 接口实现类，使用 getBean 时传的 beanName 需要带 & 开头
  * 获取 FactoryBean 内部管理的对象，不需要带 & 开头

BeanFactory 的基本使用：

```java
Resource res = new ClassPathResource("applicationContext.xml");
BeanFactory bf = new XmlBeanFactory(res);
UserService userService = (UserService)bf.getBean("userService");
```



****



##### FactoryBean

FactoryBean：对单一的 bean 的初始化过程进行封装，达到简化配置的目的

FactoryBean与 BeanFactory 区别：

- FactoryBean：封装单个 bean 的创建过程，就是工厂的 Bean

- BeanFactory：Spring 容器顶层接口，定义了 bean 相关的获取操作

代码实现：

* FactoryBean，实现类一般是 MapperFactoryBean，创建 DAO 层接口的实现类

  ```java
  public class EquipmentDaoImplFactoryBean implements FactoryBean {
      @Override	//获取Bean
      public Object getObject() throws Exception {
          return new EquipmentDaoImpl();
      }
      
      @Override	//获取bean的类型
      public Class<?> getObjectType() {
          return null;
      }
      
      @Override	//是否单例
      public boolean isSingleton() {
          return false;
      }
  }
  ```

* MapperFactoryBean 继承 SqlSessionDaoSupport，可以获取 SqlSessionTemplate，完成 MyBatis 的整合

  ```java
  public abstract class SqlSessionDaoSupport extends DaoSupport {
    	private SqlSessionTemplate sqlSessionTemplate;
  	// 获取 SqlSessionTemplate 对象
  	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
      	if (this.sqlSessionTemplate == null || 
          	sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
        		this.sqlSessionTemplate = createSqlSessionTemplate(sqlSessionFactory);
      	}
    	}
  }
  ```






***



#### 过滤器

##### 数据准备

* DAO 层 UserDao、AccountDao、BookDao、EquipmentDao

  ```java
  public interface UserDao {
  	public void save();
  }
  ```

  ```java
  @Component("userDao")
  public class UserDaoImpl implements UserDao {
      public void save() {
          System.out.println("user dao running...");
      }
  
  }
  ```

* Service 业务层

  ```java
  public interface UserService {
      public void save();
  }
  ```

  ```java
  @Service("userService")
  public class UserServiceImpl implements UserService {
      @Autowired
      private UserDao userDao;//...........BookDao等
  
      public void save() {
          System.out.println("user service running...");
          userDao.save();
      }
  }
  ```



***



##### 过滤器

名称：TypeFilter

类型：**接口**

作用：自定义类型过滤器

示例：

* config / filter / MyTypeFilter

  ```java
  public class MyTypeFilter implements TypeFilter {
      @Override
      /**
      * metadataReader:读取到的当前正在扫描的类的信息
      * metadataReaderFactory:可以获取到任何其他类的信息
      */
      //加载的类满足要求，匹配成功
      public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
          //获取当前类注解的信息
  		AnnotationMetadata am = metadataReader.getAnnotationMetadata();
  		//获取当前正在扫描的类的类信息
  		ClassMetadata classMetadata = metadataReader.getClassMetadata();
  		//获取当前类资源（类的路径）
  		Resource resource = metadataReader.getResource();
          
          
          //通过类的元数据获取类的名称
          String className = classMetadata.getClassName();
          //如果加载的类名满足过滤器要求，返回匹配成功
          if(className.equals("service.impl.UserServiceImpl")){
         	//返回true表示匹配成功，返回false表示匹配失败。此处仅确认匹配结果，不会确认是排除还是加入，排除/加入由配置项决定，与此处无关
              return true;
          }
          return false;
      }
  }
  ```

* SpringConfig

  ```java
  @Configuration
  //设置排除bean，排除的规则是自定义规则（FilterType.CUSTOM），具体的规则定义为MyTypeFilter
  @ComponentScan(
          value = {"dao","service"},
          excludeFilters = @ComponentScan.Filter(
                  type= FilterType.CUSTOM,
                  classes = MyTypeFilter.class
          )
  )
  public class SpringConfig {
  }
  ```




***



#### 导入器

bean 只有通过配置才可以进入 Spring 容器，被 Spring 加载并控制

- 配置 bean 的方式如下：

  - XML 文件中使用 <bean/> 标签配置
  - 使用 @Component 及衍生注解配置

导入器可以快速高效导入大量 bean，替代 @Import({a.class,b.class})，无需在每个类上添加 @Bean

名称： ImportSelector

类型：**接口**

作用：自定义bean导入器

* selector / MyImportSelector

  ```java
  public class MyImportSelector implements ImportSelector{
      @Override
      public String[] selectImports(AnnotationMetadata importingClassMetadata) {
  //      1.编程形式加载一个类
  //      return new String[]{"dao.impl.BookDaoImpl"};
  
  //      2.加载import.properties文件中的单个类名
  //      ResourceBundle bundle = ResourceBundle.getBundle("import");
  //      String className = bundle.getString("className");
  
  //      3.加载import.properties文件中的多个类名
          ResourceBundle bundle = ResourceBundle.getBundle("import");
          String className = bundle.getString("className");
          return className.split(",");
      }
  }
  ```

* import.properties

  ```properties
  #2.加载import.properties文件中的单个类名
  #className=dao.impl.BookDaoImpl
  
  #3.加载import.properties文件中的多个类名
  #className=dao.impl.BookDaoImpl,dao.impl.AccountDaoImpl
  
  #4.导入包中的所有类
  path=dao.impl.*
  ```

* SpringConfig

  ```java
  @Configuration
  @ComponentScan({"dao","service"})
  @Import(MyImportSelector.class)
  public class SpringConfig {
  }
  ```



***



#### 注册器

可以取代 ComponentScan 扫描器

名称：ImportBeanDefinitionRegistrar

类型：**接口**

作用：自定义 bean 定义注册器

* registrar / MyImportBeanDefinitionRegistrar

  ```java
  public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
  /**
   * AnnotationMetadata:当前类的注解信息
   * BeanDefinitionRegistry:BeanDefinition注册类，把所有需要添加到容器中的bean调用registerBeanDefinition手工注册进来
   */
      @Override
      public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
          //自定义注册器
          //1.开启类路径bean定义扫描器，需要参数bean定义注册器BeanDefinitionRegistry，需要制定是否使用默认类型过滤器
          ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false);
          //2.添加包含性加载类型过滤器（可选，也可以设置为排除性加载类型过滤器）
          scanner.addIncludeFilter(new TypeFilter() {
              @Override
              public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                  //所有匹配全部成功，此处应该添加实际的业务判定条件
                  return true;
              }
          });
          //设置扫描路径
          scanner.addExcludeFilter(tf);//排除
          scanner.scan("dao","service");
      }
  }
  ```

* SpringConfig

  ```java
  @Configuration
  @Import(MyImportBeanDefinitionRegistrar.class)
  public class SpringConfig {
  }
  ```

  



***



#### 处理器

通过创建类**继承相应的处理器的接口**，重写后置处理的方法，来实现**拦截 Bean 的生命周期**来实现自己自定义的逻辑

BeanPostProcessor：bean 后置处理器，bean 创建对象初始化前后进行拦截工作的

BeanFactoryPostProcessor：beanFactory 的后置处理器

 * 		加载时机：在 BeanFactory 初始化之后调用，来定制和修改 BeanFactory 的内容；所有的 bean 定义已经保存加载到 beanFactory，但是 bean 的实例还未创建

* 		执行流程：
    * ioc 容器创建对象
    * invokeBeanFactoryPostProcessors(beanFactory)：执行 BeanFactoryPostProcessor
      * 在 BeanFactory 中找到所有类型是 BeanFactoryPostProcessor 的组件，并执行它们的方法
      * 在初始化创建其他组件前面执行

BeanDefinitionRegistryPostProcessor：

* 加载时机：在所有 bean 定义信息将要被加载，但是 bean 实例还未创建，优先于 BeanFactoryPostProcessor 执行；利用 BeanDefinitionRegistryPostProcessor 给容器中再额外添加一些组件

* 执行流程：
  * ioc 容器创建对象
  * refresh() → invokeBeanFactoryPostProcessors(beanFactory)
  * 从容器中获取到所有的 BeanDefinitionRegistryPostProcessor 组件
    * 依次触发所有的 postProcessBeanDefinitionRegistry() 方法
    * 再来触发 postProcessBeanFactory() 方法



*****



#### 监听器

##### 基本概述

ApplicationListener：监听容器中发布的事件，完成事件驱动模型开发

```java
public interface ApplicationListener<E extends ApplicationEvent>
```

所以监听 ApplicationEvent 及其下面的子事件

应用监听器步骤：

 * 	  写一个监听器（ApplicationListener实现类）来监听某个事件（ApplicationEvent及其子类）
 * 	  把监听器加入到容器 @Component
 * 	  只要容器中有相关事件的发布，就能监听到这个事件；
      * 	  ContextRefreshedEvent：容器刷新完成（所有 bean 都完全创建）会发布这个事件
      * 	  ContextClosedEvent：关闭容器会发布这个事件
 * 	  发布一个事件：`applicationContext.publishEvent()`

```java
@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
	//当容器中发布此事件以后，方法触发
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		System.out.println("收到事件：" + event);
	}
}
```



****



##### 实现原理

ContextRefreshedEvent 事件：

- 容器初始化过程中执行 `initApplicationEventMulticaster()`：初始化事件多播器

  - 先去容器中查询 `id = applicationEventMulticaster` 的组件，有直接返回
  - 没有就执行 `this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory)` 并且加入到容器中
  - 以后在其他组件要派发事件，自动注入这个 applicationEventMulticaster

- 容器初始化过程执行 **registerListeners()** 注册监听器

  - 从容器中获取所有监听器：`getBeanNamesForType(ApplicationListener.class, true, false)`
  - 将 listener 注册到 ApplicationEventMulticaster

- 容器刷新完成：finishRefresh() → publishEvent(new ContextRefreshedEvent(this))

  发布 ContextRefreshedEvent 事件：

  - 获取事件的多播器（派发器）：getApplicationEventMulticaster()
  - multicastEvent 派发事件
    - 获取到所有的 ApplicationListener
    - 遍历 ApplicationListener
      - 如果有 Executor，可以使用 Executor 异步派发 `Executor executor = getTaskExecutor()`
      - 没有就同步执行 listener 方法 `invokeListener(listener, event)`，拿到 listener 回调 onApplicationEvent

容器关闭会发布 ContextClosedEvent



***



##### 注解实现

注解：@EventListener

基本使用：

```java
@Service
public class UserService{
    @EventListener(classes={ApplicationEvent.class})
	public void listen(ApplicationEvent event){
		System.out.println("UserService。。监听到的事件：" + event);
	}
}
```

原理：使用 EventListenerMethodProcessor 处理器来解析方法上的 @EventListener，Spring 扫描使用注解的方法，并为之创建一个监听对象

SmartInitializingSingleton 原理：afterSingletonsInstantiated()

 *   		IOC 容器创建对象并 refresh()
 *   		finishBeanFactoryInitialization(beanFactory)：初始化剩下的单实例 bean
       * 先创建所有的单实例 bean：getBean()
       * 获取所有创建好的单实例 bean，判断是否是 SmartInitializingSingleton 类型的，如果是就调用 afterSingletonsInstantiated()





***



## AOP

### 基本概述

AOP（Aspect Oriented Programing）：面向切面编程，一种编程**范式**，指导开发者如何组织程序结构

AOP 弥补了 OOP 的不足，基于 OOP 基础之上进行横向开发：

- uOOP 规定程序开发以类为主体模型，一切围绕对象进行，完成某个任务先构建模型

- uAOP 程序开发主要关注基于 OOP 开发中的共性功能，一切围绕共性功能进行，完成某个任务先构建可能遇到的所有共性功能（当所有功能都开发出来也就没有共性与非共性之分），将软件开发由手工制作走向半自动化/全自动化阶段，实现“插拔式组件体系结构”搭建

AOP 作用：

* 提高代码的可重用性
* 业务代码编码更简洁

* 业务代码维护更高效

* 业务功能扩展更便捷



***



### 核心概念

#### 概念详解

- Joinpoint（连接点）：就是方法

- Pointcut（切入点）：就是挖掉共性功能的方法

- Advice（通知）：就是共性功能，最终以一个方法的形式呈现

- Aspect（切面）：就是共性功能与挖的位置的对应关系

- Target（目标对象）：就是挖掉功能的方法对应的类产生的对象，这种对象是无法直接完成最终工作的

- Weaving（织入）：就是将挖掉的功能回填的动态过程

- Proxy（代理）：目标对象无法直接完成工作，需要对其进行功能回填，通过创建原始对象的代理对象实现

- Introduction（引入/引介）：就是对原始对象无中生有的添加成员变量或成员方法

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP连接点.png)

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP切入点切面通知.png)

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP织入.png)



***



#### 入门项目

开发步骤：

- 开发阶段

  - 制作程序

  - 将非共性功能开发到对应的目标对象类中，并制作成切入点方法

  - 将共性功能独立开发出来，制作成通知

  - 在配置文件中，声明切入点

  - 在配置文件中，声明切入点与通知间的关系（含通知类型），即切面

- 运行阶段（AOP 完成）

  - Spring 容器加载配置文件，监控所有配置的**切入点**方法的执行

  - 当监控到切入点方法被运行，**使用代理机制，动态创建目标对象的代理对象，根据通知类别，在代理对象的对应位置将通知对应的功能织入**，完成完整的代码逻辑并运行

1. 导入坐标 pom.xml

   ```xml
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>5.1.9.RELEASE</version>
   </dependency>
   <dependency>
       <groupId>org.aspectj</groupId>
       <artifactId>aspectjweaver</artifactId>
       <version>1.9.4</version>
   </dependency>
   ```

2. 业务层抽取通用代码  service / UserServiceImpl

   ```java
   public interface UserService {
       public void save();
   }
   ```

   ```java
   public class UserServiceImpl implements UserService {
       @Override
       public void save() {
           //System.out.println("共性功能");
           System.out.println("user service running...");
       }
   }
   ```

   aop.AOPAdvice

   ```java
   //1.制作通知类，在类中定义一个方法用于完成共性功能
   public class AOPAdvice {
       //共性功能抽取后职称独立的方法
       public void function(){
           System.out.println("共性功能");
       }
   }
   ```

3. 把通知加入spring容器管理，配置aop  applicationContext.xml

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd
           http://www.springframework.org/schema/aop
           https://www.springframework.org/schema/aop/spring-aop.xsd
           ">
       <!--原始Spring控制资源-->
       <bean id="userService" class= "service.impl.UserServiceImpl"/>
       <!--2.配置共性功能成功spring控制的资源-->
       <bean id="myAdvice" class="aop.AOPAdvice"/>
       <!--3.开启AOP命名空间: beans标签内-->
       <!--4.配置AOP-->
       <aop:config>
           <!--5.配置切入点-->
           <aop:pointcut id="pt" expression="execution(* *..*(..))"/>
           <!--6.配置切面（切入点与通知的关系）-->
           <aop:aspect ref="myAdvice">
               <!--7.配置具体的切入点对应通知中那个操作方法-->
               <aop:before method="function" pointcut-ref="pt"/>
           </aop:aspect>
       </aop:config>
   </beans>
   ```

4. 测试类

   ```java
   public class App {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
           UserService userService = (UserService) ctx.getBean("userService");
           userService.save();//先输出共性功能，然后 user service running...
       }
   }
   ```



***



### XML开发

#### AspectJ

Aspect（切面）用于描述切入点与通知间的关系，是 AOP 编程中的一个概念

AspectJ 是基于 java 语言对 Aspect 的实现



***



#### AOP

##### config

标签：<aop:config>，<beans> 的子标签

作用：设置 AOP

格式：

```xml
<beans>
    <aop:config>……</aop:config>
    <aop:config>……</aop:config>
    <!--一个beans标签中可以配置多个aop:config标签-->
</beans>
```



***



##### pointcut

标签：<aop:pointcut>，归属于 aop:config 标签和 aop:aspect 标签

作用：设置切入点

格式：

```xml
<aop:config>
    <aop:pointcut id="pointcutId" expression="……"/>
    <aop:aspect>
        <aop:pointcut id="pointcutId" expression="……"/>
    </aop:aspect>
</aop:config>
```

说明：

* 一个 aop:config 标签中可以配置多个 aop:pointcut 标签，且该标签可以配置在 aop:aspect 标签内

属性：

- id ：识别切入点的名称

- expression ：切入点表达式



***



##### aspect

标签：<aop:aspect>，aop:config 的子标签

作用：设置具体的 AOP 通知对应的切入点（切面）

格式：

```xml
<aop:config>
    <aop:aspect ref="beanId">……</aop:aspect>
    <aop:aspect ref="beanId">……</aop:aspect>
    <!--一个aop:config标签中可以配置多个aop:aspect标签-->
</aop:config>
```

属性：

- ref ：通知所在的 bean 的 id



***



#### Pointcut

##### 切入点

切入点描述的是某个方法

切入点表达式是一个快速匹配方法描述的通配格式，类似于正则表达式



***



##### 表达式

格式：

```xml
关键字(访问修饰符  返回值  包名.类名.方法名(参数)异常名)
```

示例：

```java
//匹配UserService中只含有一个参数的findById方法
execution(public User service.UserService.findById(int))
```

格式解析：

* 关键字：描述表达式的匹配模式（参看关键字列表）
* 访问修饰符：方法的访问控制权限修饰符
* 类名：方法所在的类（此处可以配置接口名称）
* 异常：方法定义中指定抛出的异常

关键字：

- execution ：匹配执行指定方法

- args ：匹配带有指定参数类型的方法

- within、this、target、@within、@target、@args、@annotation、bean、reference pointcut等


通配符：

* *：单个独立的任意符号，可以独立出现，也可以作为前缀或者后缀的匹配符出现

  ```java
  //匹配com.seazean包下的任意包中的UserService类或接口中所有find开头的带有一个任意参数的方法
  execution(public * com.seazean.*.UserService.find*(*)
  ```

* .. ：多个连续的任意符号，可以独立出现，常用于简化包名与参数

  ```java
  //匹配com包下的任意包中的UserService类或接口中所有名称为findById参数任意数量和类型的方法
  execution(public User com..UserService.findById(..))
  ```

* +：专用于匹配子类类型

  ```java
  //匹配任意包下的Service结尾的类或者接口的子类或者实现类
  execution(* *..*Service+.*(..))
  ```

逻辑运算符：

- &&：连接两个切入点表达式，表示两个切入点表达式同时成立的匹配
- ||：连接两个切入点表达式，表示两个切入点表达式成立任意一个的匹配
- ! ：连接单个切入点表达式，表示该切入点表达式不成立的匹配

示例：

```java
execution(* *(..))		//前三个都是匹配全部
execution(* *..*(..))
execution(* *..*.*(..))
execution(public * *..*.*(..))
execution(public int *..*.*(..))
execution(public void *..*.*(..))
execution(public void com..*.*(..)) 
execution(public void com..service.*.*(..))
execution(public void com.seazean.service.*.*(..))
execution(public void com.seazean.service.User*.*(..))
execution(public void com.seazean.service.*Service.*(..))
execution(public void com.seazean.service.UserService.*(..))
execution(public User com.seazean.service.UserService.find*(..))	//find开头
execution(public User com.seazean.service.UserService.*Id(..))		//I
execution(public User com.seazean.service.UserService.findById(..))
execution(public User com.seazean.service.UserService.findById(int))
execution(public User com.seazean.service.UserService.findById(int,int))
execution(public User com.seazean.service.UserService.findById(int,*))
execution(public User com.seazean.service.UserService.findById())
execution(List com.seazean.service.*Service+.findAll(..))
```



***



##### 配置方式

XML 配置规则：

- 企业开发命名规范严格遵循规范文档进行

- 先为方法配置局部切入点，再抽取类中公共切入点，最后抽取全局切入点

- 代码走查过程中检测切入点是否存在越界性包含

- 代码走查过程中检测切入点是否存在非包含性进驻

- 设定 AOP 执行检测程序，在单元测试中监控通知被执行次数与预计次数是否匹配（不绝对正确：加进一个不该加的，删去一个不该删的相当于结果不变）

- 设定完毕的切入点如果发生调整务必进行回归测试

```xml
<aop:config>
    <!--1.配置公共切入点-->
    <aop:pointcut id="pt1" expression="execution(* *(..))"/>
    <aop:aspect ref="myAdvice">
        <!--2.配置局部切入点-->
        <aop:pointcut id="pt2" expression="execution(* *(..))"/>
        <!--引用公共切入点-->
        <aop:before method="logAdvice" pointcut-ref="pt1"/>
        <!--引用局部切入点-->
        <aop:before method="logAdvice" pointcut-ref="pt2"/>
        <!--3.直接配置切入点-->
        <aop:before method="logAdvice" pointcut="execution(* *(..))"/>
    </aop:aspect>
</aop:config>
```





***



#### Advice

##### 通知类型

AOP 的通知类型共5种：前置通知，后置通知、返回后通知、抛出异常后通知、环绕通知

###### before

标签：<aop:before>，aop:aspect的子标签

作用：设置前置通知

* **前置通知**：原始方法执行前执行，如果通知中抛出异常，阻止原始方法运行
* 应用：数据校验

格式：

```xml
<aop:aspect ref="adviceId">
    <aop:before method="methodName" pointcut="execution(* *(..))"/>
    <!--一个aop:aspect标签中可以配置多个aop:before标签-->
</aop:aspect>
```

基本属性：

- method：在通知类中设置当前通知类别对应的方法

- pointcut：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突

- pointcut-ref：设置当前通知对应的切入点id，与pointcut属性冲突



###### after

标签：<aop:after>，aop:aspect的子标签

作用：设置后置通知

* **后置通知**：原始方法执行后执行，无论原始方法中是否出现异常，都将执行通知

* 应用：现场清理

格式：

```xml
<aop:aspect ref="adviceId">
    <aop:after method="methodName" pointcut="execution(* *(..))"/>
    <!--一个aop:aspect标签中可以配置多个aop:after标签-->
</aop:aspect>
```

基本属性：

- method：在通知类中设置当前通知类别对应的方法

- pointcut：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突

- pointcut-ref：设置当前通知对应的切入点id，与pointcut属性冲突



###### after-r

标签：<aop:after-returning>，aop:aspect的子标签

作用：设置返回后通知

* **返回后通知**：原始方法正常执行完毕并返回结果后执行，如果原始方法中抛出异常，无法执行

* 应用：返回值相关数据处理

格式：

```xml
<aop:aspect ref="adviceId">
    <aop:after-returning method="methodName" pointcut="execution(* *(..))"/>
    <!--一个aop:aspect标签中可以配置多个aop:after-returning标签-->
</aop:aspect>
```

基本属性：

- method：在通知类中设置当前通知类别对应的方法
- pointcut：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
- pointcut-ref：设置当前通知对应的切入点id，与pointcut属性冲突
- returning：设置接受返回值的参数，与通知类中对应方法的参数一致



###### after-t

标签：<aop:after-throwing>，aop:aspect的子标签

作用：设置抛出异常后通知

* **抛出异常后通知**：原始方法抛出异常后执行，如果原始方法没有抛出异常，无法执行
* 应用：对原始方法中出现的异常信息进行处理

格式：

```xml
<aop:aspect ref="adviceId">
    <aop:after-throwing method="methodName" pointcut="execution(* *(..))"/>
    <!--一个aop:aspect标签中可以配置多个aop:after-throwing标签-->
</aop:aspect>
```

基本属性：

- method：在通知类中设置当前通知类别对应的方法
- pointcut：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突
- pointcut-ref：设置当前通知对应的切入点id，与pointcut属性冲突
- throwing：设置接受异常对象的参数，与通知类中对应方法的参数一致



###### around

标签：<aop:around>，aop:aspect的子标签

作用：设置环绕通知

* **环绕通知**：在原始方法执行前后均有对应执行执行，还可以阻止原始方法的执行

* 应用：功能强大，可以做任何事情

格式：

```xml
<aop:aspect ref="adviceId">
    <aop:around method="methodName" pointcut="execution(* *(..))"/>
    <!--一个aop:aspect标签中可以配置多个aop:around标签-->
</aop:aspect>
```

基本属性：

- method ：在通知类中设置当前通知类别对应的方法

- pointcut ：设置当前通知对应的切入点表达式，与pointcut-ref属性冲突

- pointcut-ref ：设置当前通知对应的切入点id，与pointcut属性冲突

环绕通知的开发方式（参考通知顺序章节）：

- 环绕通知是**在原始方法的前后添加功能**，在环绕通知中，存在对原始方法的显式调用

  ```java
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();
      return ret;
  }
  ```

- 环绕通知方法相关说明：

  - 方法须设定 Object 类型的返回值，否则会**拦截**原始方法的返回。如果原始方法返回值类型为 void，通知方法也可以设定返回值类型为 void，最终返回 null

  - 方法需在第一个参数位置设定 ProceedingJoinPoint 对象，通过该对象调用 proceed() 方法，实现**对原始方法的调用**。如省略该参数，原始方法将无法执行

  - 使用 proceed() 方法调用原始方法时，因无法预知原始方法运行过程中是否会出现异常，强制抛出 Throwable 对象，封装原始方法中可能出现的异常信息



****



##### 通知顺序

当同一个切入点配置了多个通知时，通知会存在运行的先后顺序，该顺序以通知配置的顺序为准。

* AOPAdvice

  ```java
  public class AOPAdvice {
      public void before(){
          System.out.println("before...);
      }
      public void after(){
          System.out.println("after...");
      }
      public void afterReturing(){
          System.out.println("afterReturing...");
      }
      public void afterThrowing(){
          System.out.println("afterThrowing...");
      }
      public Object around(ProceedingJoinPoint pjp) {
          System.out.println("around before...");
         	//对原始方法的调用
          Object ret = pjp.proceed();
          System.out.println("around after..."+ret);
     	    return ret;
      }
  }
  ```

* applicationContext.xml  **顺序执行**

  ```xml
  <aop:config>
      <aop:pointcut id="pt" expression="execution(* *..*(..))"/>
      <aop:aspect ref="myAdvice">
  		<aop:before method="before" pointcut-ref="pt"/>
          <aop:after method="after" pointcut-ref="pt"/>
          <aop:after-returning method="afterReturing" pointcut-ref="pt"/>
          <aop:after-throwing method="afterThrowing" pointcut-ref="pt"/>
          <aop:around method="around" pointcut-ref="pt"/>
      </aop:aspect>
  </aop:config>
  ```

  

***



##### 获取数据

###### 参数

第一种方式：

* 设定通知方法第一个参数为 JoinPoint，通过该对象调用 getArgs() 方法，获取原始方法运行的参数数组

  ```java
  public void before(JoinPoint jp) throws Throwable {
      Object[] args = jp.getArgs();
  }
  ```

* 所有的通知均可以获取参数，环绕通知使用ProceedingJoinPoint.getArgs()方法

第二种方式：

* 设定切入点表达式为通知方法传递参数（锁定通知变量名）

* 流程图：![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP通知获取参数方式二.png)

* 解释：
  * `&amp` 代表并且 &
  * 输出结果：a = param1   b = param2

第三种方式：

* 设定切入点表达式为通知方法传递参数（改变通知变量名的定义顺序）
* 流程图：![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP通知获取参数方式三.png)

* 解释：输出结果 a = param2   b = param1



****



###### 返回值

环绕通知和返回后通知可以获取返回值，后置通知不一定，其他类型获取不到

第一种方式：适用于返回后通知（after-returning）

- 设定返回值变量名

- 原始方法：

  ```java
  public class UserServiceImpl implements UserService {
      @Override
      public int save() {
          System.out.println("user service running...");
          return 100;
      }
  }
  ```

- AOP 配置：

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt" expression="execution(* *(..))"/>
      <aop:after-returning method="afterReturning" pointcut-ref="pt" returning="ret"/>
  </aop:aspect>
  ```

- 通知类：

  ```java
  public class AOPAdvice {
      public void afterReturning(Object ret) {
          System.out.println("return:" + ret);
      }
  }
  ```

  


第二种：适用于环绕通知（around）

- 在通知类的方法中调用原始方法获取返回值

- 原始方法：

  ```java
  public class UserServiceImpl implements UserService {
      @Override
      public int save() {
          System.out.println("user service running...");
          return 100;
      }
  }
  ```

- AOP 配置：

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt" expression="execution(* *(..))  "/>
      <aop:around method="around" pointcut-ref="pt" />
  </aop:aspect>
  ```

- 通知类：

  ```java
  public class AOPAdvice {    
  	public Object around(ProceedingJoinPoint pjp) throws Throwable {
          Object ret = pjp.proceed();
          return ret;
      }
  }
  ```

- 测试类：

  ```java
  public class App {
      public static void main(String[] args) {
          ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
          UserService userService = (UserService) ctx.getBean("userService");
  		int ret = userService.save();
         	System.out.println("app....." + ret);
      }
  }
  ```

  

***



###### 异常

环绕通知和抛出异常后通知可以获取异常，后置通知不一定，其他类型获取不到

第一种：适用于返回后通知（after-throwing）

- 设定异常对象变量名

- 原始方法

  ```java
  public class UserServiceImpl implements UserService {
      @Override
  	public void save() {
          System.out.println("user service running...");
          int i = 1/0;
      }
  }
  ```

- AOP 配置

  ```xml
  <aop:aspect ref="myAdvice">
  	<aop:pointcut id="pt" expression="execution(* *(..))  "/>
      <aop:after-throwing method="afterThrowing" pointcut-ref="pt" throwing="t"/>
  </aop:aspect>
  ```

- 通知类

  ```java
  public void afterThrowing(Throwable t){
      System.out.println(t.getMessage());
  }
  ```

  


第二种：适用于环绕通知（around）

* 在通知类的方法中调用原始方法捕获异常

- 原始方法：

  ```java
  public class UserServiceImpl implements UserService {
      @Override
  	public void save() {
          System.out.println("user service running...");
          int i = 1/0;
      }
  }
  ```

- AOP 配置：

  ```xml
  <aop:aspect ref="myAdvice">
      <aop:pointcut id="pt" expression="execution(* *(..))  "/>
      <aop:around method="around" pointcut-ref="pt" />
  </aop:aspect>
  ```

- 通知类：try……catch……捕获异常后，ret为null

  ```java
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
      Object ret = pjp.proceed();	//对此处调用进行try……catch……捕获异常，或抛出异常
      /* try {
              ret = pjp.proceed();
          } catch (Throwable throwable) {
              System.out.println("around exception..." + throwable.getMessage());
          }*/
      return ret;
  }
  ```

* 测试类

  ```java
  userService.delete();
  ```

  



***



###### 获取全部

* UserService

  ```java
  public interface UserService {
      public void save(int i, int m);
  
      public int update();
  
      public void delete();
  }
  ```

  ```java
  public class UserServiceImpl implements UserService {
      @Override
      public void save(int i, int m) {
          System.out.println("user service running..." + i + "," + m);
      }
  
      @Override
      public int update() {
          System.out.println("user service update running...");
          return 100;
      }
  
      @Override
      public void delete() {
          System.out.println("user service delete running...");
          int i = 1 / 0;
      }
  }
  ```

* AOPAdvice

  ```java
  public class AOPAdvice {
      public void before(JoinPoint jp){
          //通过JoinPoint参数获取调用原始方法所携带的参数
          Object[] args = jp.getArgs();
          System.out.println("before..."+args[0]);
      }
  
      public void after(JoinPoint jp){
          Object[] args = jp.getArgs();
          System.out.println("after..."+args[0]);
      }
  
      public void afterReturing(Object ret){
          System.out.println("afterReturing..."+ret);
      }
  
      public void afterThrowing(Throwable t){
          System.out.println("afterThrowing..."+t.getMessage());
      }
  
      public Object around(ProceedingJoinPoint pjp) {
          System.out.println("around before...");
          Object ret = null;
          try {
              //对原始方法的调用
              ret = pjp.proceed();
          } catch (Throwable throwable) {
              System.out.println("around...exception...."+throwable.getMessage());
          }
          System.out.println("around after..."+ret);
          return ret;
      }
  }
  ```

* applicationContext.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xsi:schemaLocation="
          http://www.springframework.org/schema/beans
          https://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/context
          https://www.springframework.org/schema/context/spring-context.xsd
          http://www.springframework.org/schema/aop
          https://www.springframework.org/schema/aop/spring-aop.xsd
          ">
      <bean id="userService" class="service.impl.UserServiceImpl"/>
      <bean id="myAdvice" class="aop.AOPAdvice"/>
  
      <aop:config>
          <aop:pointcut id="pt" expression="execution(* *..*(..))"/>
          <aop:aspect ref="myAdvice">
              <aop:before method="before" pointcut="pt"/>
              <aop:around method="around" pointcut-ref="pt"/>
              <aop:after method="after" pointcut="pt"/>
              <aop:after-returning method="afterReturning" pointcut-ref="pt" returning="ret"/>
              <aop:after-throwing method="afterThrowing" pointcut-ref="pt" throwing="t"/>
          </aop:aspect>
      </aop:config>
  </beans>
  ```

* 测试类

  ```java
  public class App {
      public static void main(String[] args) {
          ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
          UserService userService = (UserService) ctx.getBean("userService");
  //        userService.save(666, 888);
  //        int ret = userService.update();
  //        System.out.println("app....." + ret);
          userService.delete();
      }
  }
  ```

  



***



### 注解开发

#### AOP注解

AOP 注解简化 XML：

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP注解开发.png)

注意事项：

1. 切入点最终体现为一个方法，无参无返回值，无实际方法体内容，但不能是抽象方法

2. 引用切入点时必须使用方法调用名称，方法后面的 () 不能省略

3. 切面类中定义的切入点只能在当前类中使用，如果想引用其他类中定义的切入点使用“类名.方法名()”引用

4. 可以在通知类型注解后添加参数，实现 XML 配置中的属性，例如 after-returning 后的 returning 性





****



#### 启动注解

##### XML

开启 AOP 注解支持：

```xml
<aop:aspectj-autoproxy/>
<context:component-scan base-package="aop,config,service"/><!--启动Spring扫描-->
```

开发步骤：

1. 导入坐标（伴随 spring-context 坐标导入已经依赖导入完成）
2. 开启 AOP 注解支持
3. 配置切面 @Aspect
4. 定义专用的切入点方法，并配置切入点 @Pointcut
5. 为通知方法配置通知类型及对应切入点 @Before



##### 纯注解

注解：@EnableAspectJAutoProxy

位置：Spring 注解配置类定义上方

作用：设置当前类开启 AOP 注解驱动的支持，加载 AOP 注解

格式：

```java
@Configuration
@ComponentScan("com.seazean")
@EnableAspectJAutoProxy
public class SpringConfig {
}
```



***



#### 基本注解

##### Aspect

注解：@Aspect

位置：类定义上方

作用：设置当前类为切面类

格式：

```java
@Aspect
public class AopAdvice {
}
```



##### Pointcut

注解：@Pointcut

位置：方法定义上方

作用：使用当前方法名作为切入点引用名称

格式：

```java
@Pointcut("execution(* *(..))")
public void pt() {
}
```

说明：被修饰的方法忽略其业务功能，格式设定为无参无返回值的方法，方法体内空实现（非抽象）





##### Before

注解：@Before

位置：方法定义上方

作用：标注当前方法作为前置通知

格式：

```java
@Before("pt()")
public void before(JoinPoint joinPoint){
    //joinPoint.getArgs();
}
```

注意：**多个参数时，JoinPoint参数一定要在第一位**



##### After

注解：@After

位置：方法定义上方

作用：标注当前方法作为后置通知

格式：

```java
@After("pt()")
public void after(){
}
```



##### AfterR

注解：@AfterReturning

位置：方法定义上方

作用：标注当前方法作为返回后通知

格式：

```java
@AfterReturning(value="pt()", returning = "result")
public void afterReturning(Object result) {
}
```

特殊参数：

- returning ：设定使用通知方法参数**接收**返回值的变量名



##### AfterT

注解：@AfterThrowing

位置：方法定义上方

作用：标注当前方法作为异常后通知

格式：

```java
@AfterThrowing(value="pt()", throwing = "t")
public void afterThrowing(Throwable t){
}
```

特殊参数：

- throwing ：设定使用通知方法参数接收原始方法中抛出的异常对象名



##### Around

注解：@Around

位置：方法定义上方

作用：标注当前方法作为环绕通知

格式：

```java
@Around("pt()")
public Object around(ProceedingJoinPoint pjp) throws Throwable {
    Object ret = pjp.proceed();
    return ret;
}
```



***



#### 执行顺序

AOP 使用 XML 配置情况下，通知的执行顺序由配置顺序决定，在注解情况下由于不存在配置顺序的概念，参照通知所配置的**方法名字符串对应的编码值顺序**，可以简单理解为字母排序

- 同一个通知类中，相同通知类型以方法名排序为准

  ```java
  @Before("aop.AOPPointcut.pt()")
  public void aop001Log(){}
  
  @Before("aop.AOPPointcut.pt()")
  public void aop002Exception(){}
  ```

- 不同通知类中，以类名排序为准

- 使用 @Order 注解通过变更 bean 的加载顺序改变通知的加载顺序

  ```java
  @Component
  @Aspect
  @Order(1)  //先执行
  public class AOPAdvice2 {
  }
  ```

  ```java
  @Component
  @Aspect
  @Order(2) 
  public class AOPAdvice1 {//默认执行此通知
  }
  ```






***



### AOP 原理

#### 静态代理

装饰者模式（Decorator Pattern）：在不惊动原始设计的基础上，为其添加功能

```java
public class UserServiceDecorator implements UserService{
    private UserService userService;
    
    public UserServiceDecorator(UserService userService) {
        this.userService = userService;
    }
    
    public void save() {
        //原始调用
        userService.save();
        //增强功能（后置）
        System.out.println("后置增强功能");
    }
}
```



***



#### Proxy

JDKProxy 动态代理是针对对象做代理，要求原始对象具有接口实现，并对接口方法进行增强，因为**代理类继承Proxy**

静态代理和动态代理的区别：

* 静态代理是在编译时就已经将接口、代理类、被代理类的字节码文件确定下来
* 动态代理是程序在运行后通过反射创建字节码文件交由 JVM 加载

```java
public class UserServiceJDKProxy {
    public static UserService createUserServiceJDKProxy(UserService userService) {
        UserService service = (UserService) Proxy.newProxyInstance(
            userService.getClass().getClassLoader(),//获取被代理对象的类加载器
            userService.getClass().getInterfaces(),	//获取被代理对象实现的接口
            new InvocationHandler() {				//对原始方法执行进行拦截并增强
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("save")) {
                        System.out.println("前置增强");
                        Object ret = method.invoke(userService, args);
                        System.out.println("后置增强");
                        return ret;
                    }
                    return null;
				}
 			});
        return service;
    }
}
```



***



#### CGLIB

CGLIB（Code Generation Library）：Code 生成类库 

CGLIB 特点：

* CGLIB 动态代理**不限定**是否具有接口，可以对任意操作进行增强
* CGLIB 动态代理无需要原始被代理对象，动态创建出新的代理对象
* CGLIB **继承被代理类**，如果代理类是 final 则不能实现

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP底层原理-cglib.png)

* CGLIB 类

  * JDKProxy 仅对接口方法做增强，CGLIB 对所有方法做增强，包括 Object 类中的方法（toString、hashCode）
  * 返回值类型采用多态向下转型，所以需要设置父类类型

  需要对方法进行判断是否是 save，来选择性增强

  ```java
  public class UserServiceImplCglibProxy {
      public static UserService createUserServiceCglibProxy(Class cls){
          //1.创建Enhancer对象（可以理解为内存中动态创建了一个类的字节码）
          Enhancer enhancer = new Enhancer();
          
          //2.设置Enhancer对象的父类是指定类型UserServerImpl
          enhancer.setSuperclass(cls);
          
          //3.设置回调方法
          enhancer.setCallback(new MethodInterceptor() {
              @Override
              public Object intercept(Object o, Method m, Object[] args, MethodProxy mp) throws Throwable {
                  //o是被代理出的类创建的对象，所以使用MethodProxy调用，并且是调用父类
                  //通过调用父类的方法实现对原始方法的调用
                  Object ret = methodProxy.invokeSuper(o, args);
                  //后置增强内容,需要判断是都是save方法
                  if (method.getName().equals("save")) {
                      System.out.println("I love Java");
                  }
                  return ret;
              }
          });
          //使用Enhancer对象创建对应的对象
          return (UserService)enhancer.create();
      }
  }
  ```

* Test类

  ```java
  public class App {
      public static void main(String[] args) {
          UserService userService = UserServiceCglibProxy.createUserServiceCglibProxy(UserServiceImpl.class);
          userService.save();
      }
  }
  ```

  



***



#### 代理选择

Spirng 可以通过配置的形式控制使用的代理形式，Spring 会先判断是否实现了接口，如果实现了接口就使用 JDK 动态代理，如果没有实现接口则使用 CGLIB 动态代理，通过配置可以修改为使用 CGLIB 

- XML 配置

  ```xml
  <!--XML配置AOP-->
  <aop:config proxy-target-class="false"></aop:config>
  ```

- XML 注解支持

  ```xml
  <!--注解配置AOP-->
  <aop:aspectj-autoproxy proxy-target-class="false"/>
  ```

- 注解驱动

  ```java
  //修改为使用 cglib 创建代理对象
  @EnableAspectJAutoProxy(proxyTargetClass = true)
  ```

* JDK 动态代理和 CGLIB 动态代理的区别：

  * JDK 动态代理只能对实现了接口的类生成代理，没有实现接口的类不能使用。
  * CGLIB 动态代理即使被代理的类没有实现接口也可以使用，因为 CGLIB 动态代理是使用继承被代理类的方式进行扩展
  * CGLIB 动态代理是通过继承的方式，覆盖被代理类的方法来进行代理，所以如果方法是被 final 修饰的话，就不能进行代理





***



#### 织入时机

![AOP织入时机](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/AOP织入时机.png)





***





## 事务

### 事务机制

#### 事务介绍

事务：数据库中多个操作合并在一起形成的操作序列，事务特征（ACID）

作用：

* 当数据库操作序列中个别操作失败时，提供一种方式使数据库状态恢复到正常状态（**A**），保障数据库即使在异常状态下仍能保持数据一致性（**C**）（要么操作前状态，要么操作后状态）
* 当出现并发访问数据库时，在多个访问间进行相互隔离，防止并发访问操作结果互相干扰（**I**）

Spring 事务一般加到业务层，对应着业务的操作，Spring 事务的本质其实就是数据库对事务的支持，没有数据库的事务支持，Spring 是无法提供事务功能的，Spring 只提供统一事务管理接口

Spring 在事务开始时，根据当前环境中设置的隔离级别，调整数据库隔离级别，由此保持一致。程序是否支持事务首先取决于数据库 ，比如 MySQL ，如果是 **Innodb 引擎**，是支持事务的；如果 MySQL 使用 MyISAM 引擎，那从根上就是不支持事务的

**保证原子性**：

* 要保证事务的原子性，就需要在异常发生时，对已经执行的操作进行**回滚**
* 在 MySQL 中，恢复机制是通过**回滚日志（undo log）** 实现，所有事务进行的修改都会先先记录到这个回滚日志中，然后再执行相关的操作。如果执行过程中遇到异常的话，直接利用回滚日志中的信息将数据回滚到修改之前的样子即可
* 回滚日志会先于数据持久化到磁盘上，这样保证了即使遇到数据库突然宕机等情况，当用户再次启动数据库的时候，数据库还能够通过查询回滚日志来回滚将之前未完成的事务



***



#### 隔离级别

TransactionDefinition 接口中定义了五个表示隔离级别的常量：

- TransactionDefinition.ISOLATION_DEFAULT：使用后端数据库默认的隔离级别，MySQL 默认采用的 REPEATABLE_READ 隔离级别，Oracle 默认采用的 READ_COMMITTED隔离级别.
- TransactionDefinition.ISOLATION_READ_UNCOMMITTED：最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
- TransactionDefinition.ISOLATION_READ_COMMITTED：允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
- TransactionDefinition.ISOLATION_REPEATABLE_READ：对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
- TransactionDefinition.ISOLATION_SERIALIZABLE：最高的隔离级别，完全服从 ACID 的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别

MySQL InnoDB 存储引擎的默认支持的隔离级别是 **REPEATABLE-READ（可重读）**

**分布式事务**：允许多个独立的事务资源（transactional resources）参与到一个全局的事务中。事务资源通常是关系型数据库系统，但也可以是其他类型的资源，全局事务要求在其中的所有参与的事务要么都提交，要么都回滚，这对于事务原有的 ACID 要求又有了提高

在使用分布式事务时，InnoDB 存储引擎的事务隔离级别必须设置为 SERIALIZABLE



****



#### 传播行为

事务传播行为是为了解决业务层方法之间互相调用的事务问题，也就是方法嵌套：

* 当事务方法被另一个事务方法调用时，必须指定事务应该如何传播。

* 例如：方法可能继续在现有事务中运行，也可能开启一个新事务，并在自己的事务中运行

  ```java
  //外层事务 Service A 的 aMethod 调用内层 Service B 的 bMethod
  class A {
      @Transactional(propagation=propagation.xxx)
      public void aMethod {
          B b = new B();
          b.bMethod();
      }
  }
  class B {
      @Transactional(propagation=propagation.xxx)
      public void bMethod {}
  }
  ```

**支持当前事务**的情况：

* TransactionDefinition.PROPAGATION_REQUIRED： 如果当前存在事务则**加入该事务**；如果当前没有事务则创建一个新的事务
  * 内外层是相同的事务，在 aMethod 或者在 bMethod 内的任何地方出现异常，事务都会被回滚
  * 工作流程：
    * 线程执行到 serviceA.aMethod() 时，其实是执行的代理 serviceA 对象的 aMethod
    * 首先执行事务增强器逻辑（环绕增强），提取事务标签属性，检查当前线程是否绑定 connection 数据库连接资源，没有就调用 datasource.getConnection()，设置事务提交为手动提交 autocommit(false)
    * 执行其他增强器的逻辑，然后调用 target 的目标方法 aMethod() 方法，进入 serviceB 的逻辑
    * serviceB 也是先执行事务增强器的逻辑，提取事务标签属性，但此时会检查到线程绑定了 connection，检查注解的传播属性，所以调用 DataSourceUtils.getConnection(datasource) 共享该连接资源，执行完相关的增强和 SQL 后，发现事务并不是当前方法开启的，可以直接返回上层
    * serviceA.aMethod() 继续执行，执行完增强后进行提交事务或回滚事务
* TransactionDefinition.PROPAGATION_SUPPORTS： 如果当前存在事务，则**加入该事务**；如果当前没有事务，则以非事务的方式继续运行
* TransactionDefinition.PROPAGATION_MANDATORY： 如果当前存在事务，则**加入该事务**；如果当前没有事务，则抛出异常

**不支持当前事务**的情况：

- TransactionDefinition.PROPAGATION_REQUIRES_NEW： 创建一个新的事务，如果当前存在事务，则把当前事务挂起
  - 内外层是不同的事务，如果 bMethod 已经提交，如果 aMethod 失败回滚 ，bMethod 不会回滚
  - 如果 bMethod 失败回滚，ServiceB 抛出的异常被 ServiceA 捕获，如果 B 抛出的异常是 A 会回滚的异常，aMethod 事务需要回滚，否则仍然可以提交
- TransactionDefinition.PROPAGATION_NOT_SUPPORTED： **以非事务方式运行**，如果当前存在事务，则把当前事务挂起
- TransactionDefinition.PROPAGATION_NEVER： **以非事务方式运行**，如果当前存在事务，则抛出异常

其他情况：

* TransactionDefinition.PROPAGATION_NESTED： 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务（两个事务没有关系）来运行
  * 如果 ServiceB 异常回滚，可以通过 try-catch 机制执行 ServiceC
  * 如果 ServiceB 提交， ServiceA 可以根据具体的配置决定是 commit 还是 rollback
  * **应用场景**：在查询数据的时候要向数据库中存储一些日志，系统不希望存日志的行为影响到主逻辑，可以使用该传播

requied：必须的、supports：支持的、mandatory：强制的、nested：嵌套的



****



#### 超时属性

事务超时，指一个事务所允许执行的最长时间，如果超过该时间限制事务还没有完成，则自动回滚事务。在 TransactionDefinition 中以 int 的值来表示超时时间，其单位是秒，默认值为 -1



***



#### 只读属性

对于只有读取数据查询的事务，可以指定事务类型为 readonly，即只读事务；只读事务不涉及数据的修改，数据库会提供一些优化手段，适合用在有多条数据库查询操作的方法中

读操作为什么需要启用事务支持：

* MySQL  默认对每一个新建立的连接都启用了 `autocommit` 模式，在该模式下，每一个发送到 MySQL 服务器的 SQL 语句都会在一个**单独**的事务中进行处理，执行结束后会自动提交事务，并开启一个新的事务
* 执行多条查询语句，如果方法加上了 `@Transactional` 注解，这个方法执行的所有 SQL 会被放在一个事务中，如果声明了只读事务的话，数据库就会去优化它的执行，并不会带来其他的收益。如果不加 `@Transactional`，每条 SQL 会开启一个单独的事务，中间被其它事务修改了数据，比如在前条 SQL 查询之后，后条 SQL 查询之前，数据被其他用户改变，则这次整体的统计查询将会出**现读数据不一致的状态**



***



### 核心对象

#### 事务对象

J2EE 开发使用分层设计的思想进行，对于简单的业务层转调数据层的单一操作，事务开启在业务层或者数据层并无太大差别，当业务中包含多个数据层的调用时，需要在业务层开启事务，对数据层中多个操作进行组合并归属于同一个事务进行处理

Spring 为业务层提供了整套的事务解决方案：

- PlatformTransactionManager
- TransactionDefinition

- TransactionStatus



***



#### PTM

PlatformTransactionManager，平台事务管理器实现类：

- DataSourceTransactionManager  适用于 Spring JDBC 或 MyBatis

- HibernateTransactionManager  适用于 Hibernate3.0 及以上版本

- JpaTransactionManager  适用于 JPA

- JdoTransactionManager  适用于 JDO

- JtaTransactionManager  适用于 JTA

管理器：

- JPA（Java Persistence API）Java EE 标准之一，为 POJO 提供持久化标准规范，并规范了持久化开发的统一 API，符合 JPA 规范的开发可以在不同的 JPA 框架下运行

  **非持久化一个字段**：

  ```java
  static String transient1; // not persistent because of static
  final String transient2 = “Satish”; // not persistent because of final
  transient String transient3; // not persistent because of transient
  @Transient
  String transient4; // not persistent because of @Transient
  ```

- JDO（Java Data Object）是 Java 对象持久化规范，用于存取某种数据库中的对象，并提供标准化 API。JDBC 仅针对关系数据库进行操作，JDO 可以扩展到关系数据库、XML、对象数据库等，可移植性更强

- JTA（Java Transaction API）Java EE 标准之一，允许应用程序执行分布式事务处理。与 JDBC 相比，JDBC 事务则被限定在一个单一的数据库连接，而一个 JTA 事务可以有多个参与者，比如 JDBC 连接、JDO 都可以参与到一个 JTA 事务中

此接口定义了事务的基本操作：

| 方法                                                         | 说明     |
| ------------------------------------------------------------ | -------- |
| TransactionStatus getTransaction(TransactionDefinition definition) | 获取事务 |
| void commit(TransactionStatus status)                        | 提交事务 |
| void rollback(TransactionStatus status)                      | 回滚事务 |



****



#### Definition

TransactionDefinition 此接口定义了事务的基本信息：

| 方法                         | 说明                 |
| ---------------------------- | -------------------- |
| String getName()             | 获取事务定义名称     |
| boolean isReadOnly()         | 获取事务的读写属性   |
| int getIsolationLevel()      | 获取事务隔离级别     |
| int getTimeout()             | 获取事务超时时间     |
| int getPropagationBehavior() | 获取事务传播行为特征 |



***



#### Status

TransactionStatus 此接口定义了事务在执行过程中某个时间点上的状态信息及对应的状态操作：

| 方法                       | 说明                           |
| -------------------------- | ------------------------------ |
| boolean isNewTransaction() | 获取事务是否处于新开始事务状态 |
| voin flush()               | 刷新事务状态                   |
| boolean isCompleted()      | 获取事务是否处于已完成状态     |
| boolean hasSavepoint()     | 获取事务是否具有回滚储存点     |
| boolean isRollbackOnly()   | 获取事务是否处于回滚状态       |
| void setRollbackOnly()     | 设置事务处于回滚状态           |



****



### 编程式

#### 控制方式

编程式、声明式（XML）、声明式（注解）



#### 环境准备

银行转账业务

* 包装类

  ```java
  public class Account implements Serializable {
      private Integer id;
      private String name;
      private Double money;
      .....
  }
  ```

* DAO层接口：AccountDao

  ```java
  public interface AccountDao {
      //入账操作	name:入账用户名	money:入账金额
      void inMoney(@Param("name") String name, @Param("money") Double money);
  
      //出账操作	name:出账用户名	money:出账金额
      void outMoney(@Param("name") String name, @Param("money") Double money);
  }
  ```

* 业务层接口提供转账操作：AccountService

  ```java
  public interface AccountService {
  	//转账操作	outName:出账用户名	inName:入账用户名	money:转账金额
  	public void transfer(String outName,String inName,Double money);
  }
  ```

* 业务层实现提供转账操作：AccountServiceImpl

  ```java
  public class AccountServiceImpl implements AccountService {
      private AccountDao accountDao;
      public void setAccountDao(AccountDao accountDao) {
          this.accountDao = accountDao;
      }
      @Override
      public void transfer(String outName,String inName,Double money){
  		accountDao.inMoney(outName,money);
          accountDao.outMoney(inName,money);
  	}
  }
  ```

* 映射配置文件：dao / AccountDao.xml

  ```xml
  <mapper namespace="dao.AccountDao">
      <update id="inMoney">
          UPDATE account SET money = money + #{money} WHERE name = #{name}
      </update>
  
      <update id="outMoney">
          UPDATE account SET money = money - #{money} WHERE name = #{name}
      </update>
  </mapper>
  ```

* jdbc.properties

  ```properties
  jdbc.driver=com.mysql.jdbc.Driver
  jdbc.url=jdbc:mysql://192.168.2.185:3306/spring_db
  jdbc.username=root
  jdbc.password=1234
  ```

* 核心配置文件：applicationContext.xml

  ```xml
  <context:property-placeholder location="classpath:*.properties"/>
  
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
      <property name="driverClassName" value="${jdbc.driver}"/>
      <property name="url" value="${jdbc.url}"/>
      <property name="username" value="${jdbc.username}"/>
      <property name="password" value="${jdbc.password}"/>
  </bean>
  
  <bean id="accountService" class="service.impl.AccountServiceImpl">
      <property name="accountDao" ref="accountDao"/>
  </bean>
  
  <bean class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource"/>
      <property name="typeAliasesPackage" value="domain"/>
  </bean>
  <!--扫描映射配置和Dao-->
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
      <property name="basePackage" value="dao"/>
  </bean>
  ```

* 测试类

  ```java
  ApplicationContext ctx = new ClassPathXmlApplicationContext("ap...xml");
  AccountService accountService = (AccountService) ctx.getBean("accountService");
  accountService.transfer("Jock1", "Jock2", 100d);
  ```

  

***



#### 编程式

编程式事务就是代码显式的给出事务的开启和提交

* 修改业务层实现提供转账操作：AccountServiceImpl

  ```java
  public void transfer(String outName,String inName,Double money){
      //1.创建事务管理器，
      DataSourceTransactionManager dstm = new DataSourceTransactionManager();
      //2.为事务管理器设置与数据层相同的数据源
      dstm.setDataSource(dataSource);
      //3.创建事务定义对象
      TransactionDefinition td = new DefaultTransactionDefinition();
      //4.创建事务状态对象，用于控制事务执行，【开启事务】
      TransactionStatus ts = dstm.getTransaction(td);
      accountDao.inMoney(inName,money);
      int i = 1/0;    //模拟业务层事务过程中出现错误
      accountDao.outMoney(outName,money);
      //5.提交事务
      dstm.commit(ts);
  }
  ```

* 配置 applicationContext.xml

  ```xml
  <!--添加属性注入-->
  <bean id="accountService" class="service.impl.AccountServiceImpl">
      <property name="accountDao" ref="accountDao"/>
      <property name="dataSource" ref="dataSource"/>
  </bean>
  ```

  

****



#### AOP改造

* 将业务层的事务处理功能抽取出来制作成 AOP 通知，利用环绕通知运行期动态织入

  ```java
  public class TxAdvice {
      private DataSource dataSource;
      public void setDataSource(DataSource dataSource) {
          this.dataSource = dataSource;
      }
  
      public Object tx(ProceedingJoinPoint pjp) throws Throwable {
          //开启事务
          PlatformTransactionManager ptm = new DataSourceTransactionManager(dataSource);
          //事务定义
          TransactionDefinition td = new DefaultTransactionDefinition();
          //事务状态
          TransactionStatus ts =  ptm.getTransaction(td);
          //pjp.getArgs()标准写法，也可以不加，同样可以传递参数
          Object ret = pjp.proceed(pjp.getArgs());
          
          //提交事务
          ptm.commit(ts);
  
          return ret;
      }
  }
  ```

* 配置 applicationContext.xml，要开启 AOP 空间

  ```xml
  <!--修改bean的属性注入-->
  <bean id="accountService" class="service.impl.AccountServiceImpl">
      <property name="accountDao" ref="accountDao"/>
  </bean>
  
  <!--配置AOP通知类，并注入dataSource-->
  <bean id="txAdvice" class="aop.TxAdvice">
      <property name="dataSource" ref="dataSource"/>
  </bean>
  
  <!--使用环绕通知将通知类织入到原始业务对象执行过程中-->
  <aop:config>
      <aop:pointcut id="pt" expression="execution(* *..transfer(..))"/>
      <aop:aspect ref="txAdvice">
          <aop:around method="tx" pointcut-ref="pt"/>
      </aop:aspect>
  </aop:config>
  ```

* 修改业务层实现提供转账操作：AccountServiceImpl

  ```java
  public class AccountServiceImpl implements AccountService {
      private AccountDao accountDao;
      public void setAccountDao(AccountDao accountDao) {
          this.accountDao = accountDao;
      }
      @Override
      public void transfer(String outName,String inName,Double money){
  		accountDao.inMoney(outName,money);
          //int i = 1 / 0;
          accountDao.outMoney(inName,money);
  	}
  }
  ```

  

  

***



### 声明式

#### XML

##### tx使用

删除 TxAdvice 通知类，开启 tx 命名空间，配置 applicationContext.xml

```xml
<!--配置平台事务管理器-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!--定义事务管理的通知类-->
<tx:advice id="txAdvice" transaction-manager="txManager">
    <!--定义控制的事务-->
    <tx:attributes>
        <tx:method name="transfer" read-only="false"/>
    </tx:attributes>
</tx:advice>

<!--使用aop:advisor在AOP配置中引用事务专属通知类，底层invoke调用-->
<aop:config>
    <aop:pointcut id="pt" expression="execution(* service.*Service.*(..))"/>
    <aop:advisor advice-ref="txAdvice" pointcut-ref="pt"/>
</aop:config>
```

* aop:advice 与 aop:advisor 区别
  * aop:advice 配置的通知类可以是普通 Java 对象，不实现接口，也不使用继承关系
  * aop:advisor 配置的通知类必须实现通知接口，底层 invoke 调用

    - MethodBeforeAdvice

    - AfterReturningAdvice

    - ThrowsAdvice

pom.xml 文件引入依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-tx</artifactId>
    <version>5.1.9.RELEASE</version>
</dependency>
```





***



##### tx配置

###### advice

标签：tx:advice，beans 的子标签

作用：专用于声明事务通知

格式：

```xml
<beans>
    <tx:advice id="txAdvice" transaction-manager="txManager">
    </tx:advice>
</beans>
```

基本属性：

- id：用于配置 aop 时指定通知器的 id
- transaction-manager：指定事务管理器 bean



###### attributes

类型：tx:attributes，tx:advice 的子标签

作用：定义通知属性

格式：

```xml
<tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
    </tx:attributes>
</tx:advice>
```



###### method

标签：tx:method，tx:attribute 的子标签

作用：设置具体的事务属性

格式：

```xml
<tx:attributes>
    <!--标准格式-->
    <tx:method name="*" read-only="false"/>
    <tx:method name="get*" read-only="true"/>
    <tx:method name="find*" read-only="true"/>
</tx:attributes>
<aop:pointcut id="pt" expression="execution(* service.*Service.*(..))"/><!--标准-->
```

说明：通常事务属性会配置多个，包含 1 个读写的全事务属性，1 个只读的查询类事务属性

属性：

* name：待添加事务的方法名表达式（支持 * 通配符）
* read-only：设置事务的读写属性，true 为只读，false 为读写
* timeout：设置事务的超时时长，单位秒，-1 为无限长
* isolation：设置事务的隔离界别，该隔离级设定是基于 Spring 的设定，非数据库端
* no-rollback-for：设置事务中不回滚的异常，多个异常使用 `,` 分隔
* rollback-for：设置事务中必回滚的异常，多个异常使用 `,` 分隔
* propagation：设置事务的传播行为



***



#### 注解

##### 开启注解

###### XML

标签：tx:annotation-driven

归属：beans 标签

作用：开启事务注解驱动，并指定对应的事务管理器

范例：

```xml
<tx:annotation-driven transaction-manager="txManager"/>
```



***



###### 纯注解

名称：@EnableTransactionManagement

类型：类注解，Spring 注解配置类上方

作用：开启注解驱动，等同 XML 格式中的注解驱动

范例：

```java
@Configuration
@ComponentScan("com.seazean")
@PropertySource("classpath:jdbc.properties")
@Import({JDBCConfig.class,MyBatisConfig.class,TransactionManagerConfig.class})
@EnableTransactionManagement
public class SpringConfig {
}
```

```java
public class TransactionManagerConfig {
    @Bean												//自动装配
    public PlatformTransactionManager getTransactionManager(@Autowired DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
```





***



##### 配置注解

名称：@Transactional

类型：方法注解，类注解，接口注解

作用：设置当前类/接口中所有方法或具体方法开启事务，并指定相关事务属性

范例：

```java
@Transactional(
    readOnly = false,
    timeout = -1,
    isolation = Isolation.DEFAULT,
    rollbackFor = {ArithmeticException.class, IOException.class},
    noRollbackFor = {},
    propagation = Propagation.REQUIRES_NEW
)
public void addAccount{} 
```

说明：

* `@Transactional` 注解只有作用到 public 方法上事务才生效

* 不推荐在接口上使用 `@Transactional` 注解

  原因：在接口上使用注解，**只有在使用基于接口的代理（JDK）时才会生效，因为注解是不能继承的**，这就意味着如果正在使用基于类的代理（CGLIB）时，那么事务的设置将不能被基于类的代理所识别

* 正确的设置 `@Transactional` 的 rollbackFor 和 propagation 属性，否则事务可能会回滚失败

* 默认情况下，事务只有遇到运行期异常 和 Error 会导致事务回滚，但是在遇到检查型（Checked）异常时不会回滚

  * 继承自 RuntimeException 或 error 的是非检查型异常，比如空指针和索引越界，而继承自 Exception 的则是检查型异常，比如 IOException、ClassNotFoundException，RuntimeException 本身继承 Exception
  * 非检查型类异常可以不用捕获，而检查型异常则必须用 try 语句块把异常交给上级方法，这样事务才能有效

**事务不生效的问题**

* 情况 1：确认创建的 MySQL 数据库表引擎是 InnoDB，MyISAM 不支持事务

* 情况 2：注解到 protected，private 方法上事务不生效，但不会报错

  原因：理论上而言，不用 public 修饰，也可以用 aop 实现事务的功能，但是方法私有化让其他业务无法调用

  AopUtils.canApply：`methodMatcher.matches(method, targetClass) --true--> return true`
  `TransactionAttributeSourcePointcut.matches()` ，AbstractFallbackTransactionAttributeSource 中 getTransactionAttribute 方法调用了其本身的 computeTransactionAttribute 方法，当加了事务注解的方法不是 public 时，该方法直接返回 null，所以造成增强不匹配

  ```java
  private TransactionAttribute computeTransactionAttribute(Method method, Class<?> targetClass) {
      // Don't allow no-public methods as required.
      if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
          return null;
      }
  }
  ```

* 情况 3：注解所在的类没有被加载成 Bean

* 情况 4：在业务层捕捉异常后未向上抛出，事务不生效

  原因：在业务层捕捉并处理了异常（try..catch）等于把异常处理掉了，Spring 就不知道这里有错，也不会主动去回滚数据，推荐做法是在业务层统一抛出异常，然后在控制层统一处理

* 情况 5：遇到检测异常时，也无法回滚

  原因：Spring 的默认的事务规则是遇到运行异常（RuntimeException）和程序错误（Error）才会回滚。想针对检测异常进行事务回滚，可以在 @Transactional 注解里使用 rollbackFor 属性明确指定异常

* 情况 6：Spring 的事务传播策略在**内部方法**调用时将不起作用，在一个 Service 内部，事务方法之间的嵌套调用，普通方法和事务方法之间的嵌套调用，都不会开启新的事务，事务注解要加到调用方法上才生效

  原因：Spring 的事务都是使用 AOP 代理的模式，动态代理 invoke 后会调用原始对象，而原始对象在去调用方法时是不会触发拦截器，就是**一个方法调用本对象的另一个方法**，所以事务也就无法生效

  ```java
  @Transactional
  public int add(){
      update();
  }
  //注解添加在update方法上无效，需要添加到add()方法上
  public int update(){}
  ```

* 情况 7：注解在接口上，代理对象是 CGLIB



***



##### 使用注解

* Dao 层

  ```java
  public interface AccountDao {
      @Update("update account set money = money + #{money} where name = #{name}")
      void inMoney(@Param("name") String name, @Param("money") Double money);
  
      @Update("update account set money = money - #{money} where name = #{name}")
      void outMoney(@Param("name") String name, @Param("money") Double money);
  }
  ```

* 业务层

  ```java
  public interface AccountService {
      //对当前方法添加事务，该配置将替换接口的配置
      @Transactional(
          readOnly = false,
          timeout = -1,
          isolation = Isolation.DEFAULT,
          rollbackFor = {},//java.lang.ArithmeticException.class, IOException.class
          noRollbackFor = {},
          propagation = Propagation.REQUIRED
          )
      public void transfer(String outName, String inName, Double money);
  }
  ```

  ```java
  public class AccountServiceImpl implements AccountService {
      @Autowired
      private AccountDao accountDao;
      public void transfer(String outName, String inName, Double money) {
          accountDao.inMoney(outName,money);
          //int i = 1/0;
          accountDao.outMoney(inName,money);
      }
  }
  ```

* 添加文件 Spring.config、Mybatis.config、JDBCConfig (参考ioc_Mybatis)、TransactionManagerConfig

  ```java
  @Configuration
  @ComponentScan({"","",""})
  @PropertySource("classpath:jdbc.properties")
  @Import({JDBCConfig.class,MyBatisConfig.class})
  @EnableTransactionManagement
  public class SpringConfig {
  }
  ```



****



### 模板对象

Spring 模板对象：TransactionTemplate、JdbcTemplate、RedisTemplate、RabbitTemplate、JmsTemplate、HibernateTemplate、RestTemplate

* JdbcTemplate：提供标准的 sql 语句操作API

* NamedParameterJdbcTemplate：提供标准的具名 sql 语句操作API

* RedisTemplate：

  ```java
  public void changeMoney(Integer id, Double money) {
      redisTemplate.opsForValue().set("account:id:"+id,money);
  }
  public Double findMondyById(Integer id) {
      Object money = redisTemplate.opsForValue().get("account:id:" + id);
      return new Double(money.toString());
  }
  ```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-RedisTemplate.png)





***





## 原理

### XML

三大对象：

* **BeanDefinition**：是 Spring 中极其重要的一个概念，存储了 bean 对象的所有特征信息，如是否单例、是否懒加载、factoryBeanName 等，和 bean 的关系就是类与对象的关系，一个不同的 bean 对应一个 BeanDefinition

* **BeanDefinationRegistry**：存放 BeanDefination 的容器，是一种键值对的形式，通过特定的 Bean 定义的 id，映射到相应的 BeanDefination，**BeanFactory 的实现类同样继承 BeanDefinationRegistry 接口**，拥有保存 BD 的能力

* **BeanDefinitionReader**：读取配置文件，**XML 用 Dom4j 解析**，**注解用 IO 流加载解析**

程序：

```java
BeanFactory bf = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
UserService userService1 = (UserService)bf.getBean("userService");
```

源码解析：

```java
public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) {
    super(parentBeanFactory);
    this.reader.loadBeanDefinitions(resource);
}
public int loadBeanDefinitions(Resource resource) {
    //将 resource 包装成带编码格式的 EncodedResource
    //EncodedResource 中 getReader()方法，调用java.io包下的 转换流 创建指定编码的输入流对象
    return loadBeanDefinitions(new EncodedResource(resource));
}
```

* `XmlBeanDefinitionReader.loadBeanDefinitions()`：**把 Resource 解析成 BeanDefinition 对象**

  * `currentResources = this.resourcesCurrentlyBeingLoaded.get()`：拿到当前线程已经加载过的所有 EncodedResoure 资源，用 ThreadLocal 保证线程安全
  * `if (currentResources == null)`：判断 currentResources 是否为空，为空则进行初始化
  * `if (!currentResources.add(encodedResource))`：如果已经加载过该资源会报错，防止重复加载
  * `inputSource = new InputSource(inputStream)`：资源对象包装成 InputSource，InputSource 是 **SAX** 中的资源对象，用来进行 XML 文件的解析
  * `return doLoadBeanDefinitions()`：**加载返回**
  * `currentResources.remove(encodedResource)`：加载完成移除当前 encodedResource
  * `resourcesCurrentlyBeingLoaded.remove()`：ThreadLocal 为空时移除元素，防止内存泄露

* `XmlBeanDefinitionReader.doLoadBeanDefinitions(inputSource, resource)`：真正的加载函数

  `Document doc = doLoadDocument(inputSource, resource)`：转换成有**层次结构**的 Document 对象

  * `getEntityResolver()`**：获取用来解析 DTD、XSD 约束的解析器**

  * `getValidationModeForResource(resource)`：获取验证模式

  `int count = registerBeanDefinitions(doc, resource)`：**将 Document 解析成 BD 对象，注册（添加）到  BeanDefinationRegistry 中**，返回新注册的数量

  * `createBeanDefinitionDocumentReader()`：创建 DefaultBeanDefinitionDocumentReader 对象
  * `getRegistry().getBeanDefinitionCount()`：获取解析前 BeanDefinationRegistry 中的 bd 数量
  * `registerBeanDefinitions(doc, readerContext)`：注册 BD
    * `this.readerContext = readerContext`：保存上下文对象
    * `doRegisterBeanDefinitions(doc.getDocumentElement())`：真正的注册 BD 函数
      * `doc.getDocumentElement()`：拿出顶层标签 <beans></beans>
  * `return getRegistry().getBeanDefinitionCount() - countBefore`：返回新加入的数量

* `DefaultBeanDefinitionDocumentReader.doRegisterBeanDefinitions()`：注册 BD 到 BR

  * `createDelegate(getReaderContext(), root, parent)`：beans 是标签的解析器对象
  * `delegate.isDefaultNamespace(root)`：判断 beans 标签是否是默认的属性
  * `root.getAttribute(PROFILE_ATTRIBUTE)`：解析 profile 属性
  * `preProcessXml(root)`：解析前置处理，自定义实现
  * `parseBeanDefinitions(root, this.delegate)`：**解析 beans 标签中的子标签**
    * `parseDefaultElement(ele, delegate)`：如果是默认的标签，用该方法解析子标签
      * 判断标签名称，进行相应的解析
      * `processBeanDefinition(ele, delegate)`：
    * `delegate.parseCustomElement(ele)`：解析自定义的标签
  * `postProcessXml(root)`：解析后置处理

* `DefaultBeanDefinitionDocumentReader.processBeanDefinition()`：**解析 bean 标签并注册到注册中心**

  * `delegate.parseBeanDefinitionElement(ele)`：解析 bean 标签封装为 BeanDefinitionHolder

    * `if (!StringUtils.hasText(beanName) && !aliases.isEmpty())`：条件一成立说明 name 没有值，条件二成立说明别名有值

      `beanName = aliases.remove(0)`：拿别名列表的第一个元素当作 beanName

    * `parseBeanDefinitionElement(ele, beanName, containingBean)`：**解析 bean 标签**

      * `parseState.push(new BeanEntry(beanName))`：当前解析器的状态设置为 BeanEntry
      * class 和 parent 属性存在一个，parent 是作为父标签为了被继承
      * `createBeanDefinition(className, parent)`：设置了class 的 GenericBeanDefinition对象
      * `parseBeanDefinitionAttributes()`：解析 bean 标签的属性
      * 接下来解析子标签

    * `beanName = this.readerContext.generateBeanName(beanDefinition)`：生成 className + # + 序号的名称赋值给 beanName 

    * `return new BeanDefinitionHolder(beanDefinition, beanName, aliases)`：**包装成 BeanDefinitionHolder**

  * `registerBeanDefinition(bdHolder, getReaderContext().getRegistry())`：**注册到容器**

    * `beanName = definitionHolder.getBeanName()`：获取beanName
    * `this.beanDefinitionMap.put(beanName, beanDefinition)`：添加到注册中心

  * `getReaderContext().fireComponentRegistered()`：发送注册完成事件

  

**说明：源码部分的笔记不一定适合所有人阅读，作者采用流水线式去解析重要的代码，解析的结构类似于树状，如果视觉疲劳可以去网上参考一些博客和流程图学习源码。**



****



### IOC

#### 容器启动

Spring IOC 容器是 ApplicationContext 或者 BeanFactory，使用多个 Map 集合保存单实例 Bean，环境信息等资源，不同层级有不同的容器，比如整合 SpringMVC 的父子容器（先看 Bean 部分的源码解析再回看容器）

ClassPathXmlApplicationContext 与 AnnotationConfigApplicationContext 差不多：

```java
public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
    this();
    register(annotatedClasses);// 解析配置类，封装成一个 BeanDefinitionHolder，并注册到容器
    refresh();// 加载刷新容器中的 Bean
}
```

```java
public AnnotationConfigApplicationContext() {
    // 注册 Spring 的注解解析器到容器
    this.reader = new AnnotatedBeanDefinitionReader(this);
    // 实例化路径扫描器，用于对指定的包目录进行扫描查找 bean 对象
    this.scanner = new ClassPathBeanDefinitionScanner(this);
}
```

AbstractApplicationContext.refresh()：

* prepareRefresh()：刷新前的**预处理**

  * `this.startupDate = System.currentTimeMillis()`：设置容器的启动时间
  * `initPropertySources()`：初始化一些属性设置，可以自定义个性化的属性设置方法
  * `getEnvironment().validateRequiredProperties()`：检查环境变量
  * `earlyApplicationEvents= new LinkedHashSet<ApplicationEvent>()`：保存容器中早期的事件

* obtainFreshBeanFactory()：获取一个**全新的 BeanFactory 接口实例**，如果容器中存在工厂实例直接销毁

  `refreshBeanFactory()`：创建 BeanFactory，设置序列化 ID、读取 BeanDefinition 并加载到工厂

  * `if (hasBeanFactory())`：applicationContext 内部拥有一个 beanFactory 实例，需要将该实例完全释放销毁
  * `destroyBeans()`：销毁原 beanFactory 实例，将 beanFactory 内部维护的单实例 bean 全部清掉，如果哪个 bean 实现了 Disposablejie接口，还会进行 bean distroy 方法的调用处理
    * `this.singletonsCurrentlyInDestruction = true`：设置当前 beanFactory 状态为销毁状态
    * `String[] disposableBeanNames`：获取销毁集合中的 bean，如果当前 bean 有**析构函数**就会在销毁集合
    * `destroySingleton(disposableBeanNames[i])`：遍历所有的 disposableBeans，执行销毁方法
      * `removeSingleton(beanName)`：清除三级缓存和 registeredSingletons 中的当前 beanName 的数据
      * `this.disposableBeans.remove(beanName)`：从销毁集合中清除，每个 bean 只能 destroy 一次
      * `destroyBean(beanName, disposableBean)`：销毁 bean
        * dependentBeanMap 记录了依赖当前 bean 的其他 bean 信息，因为依赖的对象要被回收了，所以依赖当前 bean 的其他对象都要执行 destroySingleton，遍历 dependentBeanMap 执行销毁
        * `bean.destroy()`：解决完成依赖后，执行 DisposableBean 的 destroy 方法
        * ` this.dependenciesForBeanMap.remove(beanName)`：保存当前 bean 依赖了谁，直接清除
    * 进行一些集合和缓存的清理工作
  * `closeBeanFactory()`：将容器内部的 beanFactory 设置为空，重新创建
  * `beanFactory = createBeanFactory()`：创建新的 DefaultListableBeanFactory 对象
  * `beanFactory.setSerializationId(getId())`：进行 ID 的设置，可以根据 ID 获取 BeanFactory 对象
  * `customizeBeanFactory(beanFactory)`：设置是否允许覆盖和循环引用
  * `loadBeanDefinitions(beanFactory)`：**加载 BeanDefinition 信息，注册 BD注册到 BeanFactory 中**
  * `this.beanFactory = beanFactory`：把 beanFactory 填充至容器中

  `getBeanFactory()`：返回创建的 DefaultListableBeanFactory 对象，该对象继承 BeanDefinitionRegistry

* prepareBeanFactory(beanFactory)：**BeanFactory 的预准备**工作，向容器中添加一些组件

  * `setBeanClassLoader(getClassLoader())`：给当前 bf 设置一个**类加载器**，加载 bd 的 class 信息
  * `setBeanExpressionResolver()`：设置 EL 表达式解析器
  * `addPropertyEditorRegistrar`：添加一个属性编辑器，解决属性注入时的格式转换
  * `addBeanPostProcessor()`：添加后处理器，主要用于向 bean 内部注入一些框架级别的实例
  * `ignoreDependencyInterface()`：设置忽略自动装配的接口，bean 内部的这些类型的字段   不参与依赖注入
  * `registerResolvableDependency()`：注册一些类型依赖关系
  * `addBeanPostProcessor()`：将配置的监听者注册到容器中，当前 bean 实现 ApplicationListener 接口就是监听器事件
  * `beanFactory.registerSingleton()`：添加一些系统信息

* postProcessBeanFactory(beanFactory)：BeanFactory 准备工作完成后进行的后置处理工作，扩展方法

* invokeBeanFactoryPostProcessors(beanFactory)：**执行 BeanFactoryPostProcessor 的方法**

  * `processedBeans = new HashSet<>()`：存储已经执行过的 BeanFactoryPostProcessor 的 beanName

  * `if (beanFactory instanceof BeanDefinitionRegistry)`：**当前 BeanFactory 是 bd 的注册中心，bd 全部注册到 bf**

  * `for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors)`：遍历所有的 bf 后置处理器

  * `if (postProcessor instanceof BeanDefinitionRegistryPostProcessor)`：是 Registry 类的后置处理器

    `registryProcessor.postProcessBeanDefinitionRegistry(registry)`：向 bf 中注册一些 bd

    `registryProcessors.add(registryProcessor)`：添加到 BeanDefinitionRegistryPostProcessor 集合

  * `regularPostProcessors.add(postProcessor)`：添加到 BeanFactoryPostProcessor 集合

  * 逻辑到这里已经获取到所有 BeanDefinitionRegistryPostProcessor 和 BeanFactoryPostProcessor  接口类型的后置处理器

  * **首先回调 BeanDefinitionRegistryPostProcessor 类的后置处理方法 postProcessBeanDefinitionRegistry()**

    * 获取实现了 PriorityOrdered（主排序接口）接口的 bdrpp，进行 sort 排序，然后全部执行并放入已经处理过的集合
    * 再执行实现了 Ordered（次排序接口）接口的 bdrpp

    * 最后执行没有实现任何优先级或者是顺序接口 bdrpp，`boolean reiterate = true` 控制 while 是否需要再次循环，循环内是查找并执行 bdrpp 后处理器的 registry 相关的接口方法，接口方法执行以后会向 bf 内注册 bd，注册的 bd 也有可能是 bdrpp 类型，所以需要该变量控制循环
    * `processedBeans.add(ppName)`：已经执行过的后置处理器存储到该集合中，防止重复执行
    * ` invokeBeanFactoryPostProcessors()`：bdrpp 继承了 BeanFactoryPostProcessor，有 postProcessBeanFactory 方法

  * **执行普通 BeanFactoryPostProcessor 的相关 postProcessBeanFactory 方法，按照主次无次序执行**

    * `if (processedBeans.contains(ppName))`：会过滤掉已经执行过的后置处理器

  * `beanFactory.clearMetadataCache()`：清除缓存中合并的 Bean 定义，因为后置处理器可能更改了元数据

**以上是 BeanFactory 的创建及预准备工作，接下来进入 Bean 的流程**

* registerBeanPostProcessors(beanFactory)：**注册 Bean 的后置处理器**，为了干预 Spring 初始化 bean 的流程，这里仅仅是向容器中**注入而非使用**

  * `beanFactory.getBeanNamesForType(BeanPostProcessor.class)`：**获取配置中实现了 BeanPostProcessor 接口类型**

  * `int beanProcessorTargetCount`：后置处理器的数量，已经注册的 + 未注册的 + 即将要添加的一个

  * `beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker())`：添加一个检查器

    `BeanPostProcessorChecker.postProcessAfterInitialization()`：初始化后的后处理器方法

    * `!(bean instanceof BeanPostProcessor) `：当前 bean 类型是普通 bean，不是后置处理器
    * `!isInfrastructureBean(beanName)`：成立说明当前 beanName 是用户级别的 bean  不是 Spring 框架的
    * `this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount`：BeanFactory 上面注册后处理器数量 < 后处理器数量，说明后处理框架尚未初始化完成

  * `for (String ppName : postProcessorNames)`：遍历 PostProcessor 集合，**根据实现不同的顺序接口添加到不同集合**

  * `sortPostProcessors(priorityOrderedPostProcessors, beanFactory)`：实现 PriorityOrdered 接口的后处理器排序

    `registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors)`：**注册到 beanFactory 中**

  * 接着排序注册实现 Ordered 接口的后置处理器，然后注册普通的（ 没有实现任何优先级接口）后置处理器

  * 最后排序 MergedBeanDefinitionPostProcessor 类型的处理器，根据实现的排序接口，排序完注册到 beanFactory 中

  * `beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext))`：重新注册 ApplicationListenerDetector 后处理器，用于在 Bean 创建完成后检查是否属于 ApplicationListener 类型，如果是就把 Bean 放到**监听器容器**中保存起来

* initMessageSource()：初始化 MessageSource 组件，主要用于做国际化功能，消息绑定与消息解析

  * `if (beanFactory.containsLocalBean(MESSAGE_SOURCE_BEAN_NAME))`：容器是否含有名称为 messageSource 的 bean
  * `beanFactory.getBean(MESSAGE_SOURCE_BEAN_NAME, MessageSource.class)`：如果有证明用户自定义了该类型的 bean，获取后直接赋值给 this.messageSource
  * `dms = new DelegatingMessageSource()`：容器中没有就新建一个赋值

* initApplicationEventMulticaster()：**初始化事件传播器**，在注册监听器时会用到

  * `if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME))`：**条件成立说明用户自定义了事件传播器**，可以实现 ApplicationEventMulticaster 接口编写自己的事件传播器，通过 bean 的方式提供给 Spring
  * 如果有就直接从容器中获取；如果没有则创建一个 SimpleApplicationEventMulticaster 注册

* onRefresh()：留给用户去实现，可以硬编码提供一些组件，比如提供一些监听器

* registerListeners()：注册通过配置提供的 Listener，这些**监听器**最终注册到 ApplicationEventMulticaster 内

  * `for (ApplicationListener<?> listener : getApplicationListeners()) `：注册编码实现的监听器

  * `getBeanNamesForType(ApplicationListener.class, true, false)`：注册通过配置提供的 Listener

  * `multicastEvent(earlyEvent)`：**发布前面步骤产生的事件 applicationEvents**

    `Executor executor = getTaskExecutor()`：获取线程池，有线程池就异步执行，没有就同步执行

* finishBeanFactoryInitialization()：**实例化非懒加载状态的单实例**

  * `beanFactory.freezeConfiguration()`：**冻结配置信息**，就是冻结 BD 信息，冻结后无法再向 bf 内注册 bd

  * `beanFactory.preInstantiateSingletons()`：实例化 non-lazy-init singletons

    * `for (String beanName : beanNames)`：遍历容器内所有的 beanDefinitionNames

    * `getMergedLocalBeanDefinition(beanName)`：获取与父类合并后的对象（Bean → 获取流程部分详解此函数）

    * `if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit())`：BD 对应的 Class 满足非抽象、单实例，非懒加载，需要预先实例化

      `if (isFactoryBean(beanName))`：BD 对应的 Class 是 factoryBean 对象

      * `getBean(FACTORY_BEAN_PREFIX + beanName)`：获取工厂 FactoryBean 实例本身
      * `isEagerInit`：控制 FactoryBean 内部管理的 Bean 是否也初始化
      * `getBean(beanName)`：**初始化 Bean，获取 Bean 详解此函数**

      `getBean(beanName)`：不是工厂 bean 直接获取

    * `for (String beanName : beanNames)`：检查所有的 Bean 是否实现 SmartInitializingSingleton 接口，实现了就执行 afterSingletonsInstantiated()，进行一些创建后的操作

* `finishRefresh()`：完成刷新后做的一些事情，主要是启动生命周期

  * `clearResourceCaches()`：清空上下文缓存
  * `initLifecycleProcessor()`：**初始化和生命周期有关的后置处理器**，容器的生命周期
    * `if (beanFactory.containsLocalBean(LIFECYCLE_PROCESSOR_BEAN_NAME))`：成立说明自定义了生命周期处理器
    * `defaultProcessor = new DefaultLifecycleProcessor()`：Spring 默认提供的生命周期处理器
    * ` beanFactory.registerSingleton()`：将生命周期处理器注册到 bf 的一级缓存和注册单例集合中
  * `getLifecycleProcessor().onRefresh()`：获取该**生命周期后置处理器回调 onRefresh()**，调用 `startBeans(true)`
    * `lifecycleBeans = getLifecycleBeans()`：获取到所有实现了 Lifecycle 接口的对象包装到 Map 内，key 是beanName， value 是 Lifecycle 对象
    * `int phase = getPhase(bean)`：获取当前 Lifecycle 的 phase 值，当前生命周期对象可能依赖其他生命周期对象的执行结果，所以需要 phase 决定执行顺序，数值越低的优先执行
    * `LifecycleGroup group = phases.get(phase)`：把 phsae 相同的 Lifecycle 存入 LifecycleGroup
    * `if (group == null)`：group 为空则创建，初始情况下是空的
    * `group.add(beanName, bean)`：将当前 Lifecycle 添加到当前 phase 值一样的 group 内
    * `Collections.sort(keys)`：**从小到大排序，按优先级启动**
    * `phases.get(key).start()`：遍历所有的 Lifecycle 对象开始启动
    * `doStart(this.lifecycleBeans, member.name, this.autoStartupOnly)`：底层调用该方法启动
      * `bean = lifecycleBeans.remove(beanName)`： 确保 Lifecycle 只被启动一次，在一个分组内被启动了在其他分组内就看不到 Lifecycle 了
      * `dependenciesForBean = getBeanFactory().getDependenciesForBean(beanName)`：获取当前即将被启动的 Lifecycle 所依赖的其他 beanName，需要**先启动所依赖的 bean**，才能启动自身
      * `if ()`：传入的参数 autoStartupOnly 为 true 表示启动 isAutoStartUp 为 true 的 SmartLifecycle 对象，不会启动普通的生命周期的对象；false 代表全部启动
      * bean.start()：**调用启动方法**
  * `publishEvent(new ContextRefreshedEvent(this))`：**发布容器刷新完成事件**
  * `liveBeansView.registerApplicationContext(this)`：暴露 Mbean

补充生命周期 stop() 方法的调用

* DefaultLifecycleProcessor.stop()：调用 DefaultLifecycleProcessor.stopBeans()

  * 获取到所有实现了 Lifecycle 接口的对象并按 phase 数值分组的

  * `keys.sort(Collections.reverseOrder())`：按 phase 降序排序 Lifecycle 接口，最先启动的最晚关闭（责任链？）

  * `phases.get(key).stop()`：遍历所有的 Lifecycle 对象开始停止

    * `latch = new CountDownLatch(this.smartMemberCount)`：创建 CountDownLatch，设置 latch 内部的值为当前分组内的  smartMemberCount 的数量

    * `countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet<>())`：保存当前正在处理关闭的smartLifecycle 的 BeanName

    * `for (LifecycleGroupMember member : this.members)`：处理本分组内需要关闭的 Lifecycle

      `doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames)`：真正的停止方法

      * `getBeanFactory().getDependentBeans(beanName)`：**获取依赖当前 Lifecycle 的其他对象的 beanName**，因为当前的 Lifecycle 即将要关闭了，所有的依赖了当前 Lifecycle 的 bean 也要关闭
      * `countDownBeanNames.add(beanName)`：将当前 SmartLifecycle beanName 添加到 countDownBeanNames 集合内，该集合表示正在关闭的 SmartLifecycle

      * `bean.stop()`：调用停止的方法



***



#### 获取Bean

单实例：在容器启动时创建对象

多实例：在每次获取的时候创建对象

获取流程：**获取 Bean 时先从单例池获取，如果没有则进行第二次获取，并带上工厂类去创建并添加至单例池**

Java 启动 Spring 代码：

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
UserService userService = (UserService) context.getBean("userService");
```

AbstractBeanFactory.doGetBean()：获取 Bean，context.getBean() 追踪到此

* `beanName = transformedBeanName(name)`：name 可能是一个别名，重定向出来真实 beanName；也可能是一个 & 开头的 name，说明要获取的 bean 实例对象，是一个 FactoryBean 对象（IOC 原理 → 核心类）

  * `BeanFactoryUtils.transformedBeanName(name)`：判断是哪种 name，返回截取 & 以后的 name 并放入缓存
    * `transformedBeanNameCache.computeIfAbsent`：缓存是并发安全集合，key == null || value == null 时 put 成功 
    * do while 循环一直去除 & 直到不再含有 &
  * `canonicalName(name)`：aliasMap 保存别名信息，其中的 do while 逻辑是迭代查找，比如 A 别名叫做 B，但是 B 又有别名叫 C， aliasMap 为 {"C":"B", "B":"A"}，get(C) 最后返回的是  A

* `DefaultSingletonBeanRegistry.getSingleton()`：**第一次获取从缓存池获取**（循环依赖详解此代码）

  * 缓存中有数据进行 getObjectForBeanInstance() 获取可使用的 Bean（本节结束部分详解此函数）
  * 缓存中没有数据进行下面的逻辑进行创建

* `if(isPrototypeCurrentlyInCreation(beanName))`：检查 bean 是否在原型（Prototype）正在被创建的集合中，如果是就报错，说明产生了循环依赖，**原型模式解决不了循环依赖**

  原因：先加载 A，把 A 加入集合，A 依赖 B 去加载 B，B 又依赖 A，去加载 A，发现 A 在正在创建集合中，产生循环依赖

* `markBeanAsCreated(beanName)`：把 bean 标记为已经创建，**防止其他线程重新创建 Bean**

* `mbd = getMergedLocalBeanDefinition(beanName)`：**获取合并父 BD 后的 BD 对象**，BD 是直接继承的，合并后的 BD 信息是包含父类的 BD 信息

  * `this.mergedBeanDefinitions.get(beanName)`：从缓存中获取

  * `if(bd.getParentName()==null)`：beanName 对应 BD 没有父 BD 就不用处理继承，封装为 RootBeanDefinition 返回

  * `parentBeanName = transformedBeanName(bd.getParentName())`：处理父 BD 的 name 信息

  * `if(!beanName.equals(parentBeanName))`：一般情况父子 BD 的名称不同

    `pbd = getMergedBeanDefinition(parentBeanName)`：递归调用，最终返回父 BD 的父 BD 信息

  * `mbd = new RootBeanDefinition(pbd)`：按照父 BD 信息创建 RootBeanDefinition 对象

  * `mbd.overrideFrom(bd)`：**子 BD 信息覆盖 mbd**，因为是要以子 BD 为基准，不存在的才去父 BD 寻找（**类似 Java 继承**）

  * `this.mergedBeanDefinitions.put(beanName, mbd)`：放入缓存

* `checkMergedBeanDefinition()`：判断当前 BD 是否为**抽象 BD**，抽象 BD 不能创建实例，只能作为父 BD 被继承

* `mbd.getDependsOn()`：获取 bean 标签 depends-on

* `if(dependsOn != null)`：**遍历所有的依赖加载，解决不了循环依赖**

  `isDependent(beanName, dep)`：判断循环依赖，出现循环依赖问题报错

  * 两个 Map：`<bean name="A" depends-on="B" ...>`

    * dependentBeanMap：记录依赖了当前 beanName 的其他 beanName（谁依赖我，我记录谁）
    * dependenciesForBeanMap：记录当前 beanName 依赖的其它 beanName 
    * 以 B 为视角 dependentBeanMap {"B"：{"A"}}，以 A 为视角 dependenciesForBeanMap {"A" :{"B"}}

  * `canonicalName(beanName)`：处理 bean 的 name

  * `dependentBeans = this.dependentBeanMap.get(canonicalName)`：获取依赖了当前 bean 的 name

  * `if (dependentBeans.contains(dependentBeanName))`：依赖了当前 bean 的集合中是否有该 name，有就产生循环依赖

  * 进行递归处理所有的引用：假如 `<bean name="A" dp="B"> <bean name="B" dp="C"> <bean name="C" dp="A">`

    ```java
    dependentBeanMap={A:{C}, B:{A}, C:{B}} 
    // C 依赖 A     		判断谁依赖了C				递归判断				谁依赖了B
    isDependent(C, A)  → C#dependentBeans={B} → isDependent(B, A); → B#dependentBeans={A} //返回true
    ```

  `registerDependentBean(dep, beanName)`：把 bean 和依赖注册到两个 Map 中，注意参数的位置，被依赖的在前

  `getBean(dep)`：**先加载依赖的 Bean**，又进入 doGetBean() 的逻辑

* `if (mbd.isSingleton())`：**判断 bean 是否是单例的 bean**

  `getSingleton(String, ObjectFactory<?>)`：**第二次获取，传入一个工厂对象**，这个方法更倾向于创建实例并返回

  ```java
  sharedInstance = getSingleton(beanName, () -> {
      return createBean(beanName, mbd, args);//创建，跳转生命周期
      //lambda表达式，调用了ObjectFactory的getObject()方法，实际回调接口实现的是 createBean()方法进行创建对象
  });
  ```

  * `singletonObjects.get(beanName)`：从一级缓存检查是否已经被加载，单例模式复用已经创建的 bean

  * `this.singletonsCurrentlyInDestruction`：容器销毁时会设置这个属性为 true，这时就不能再创建 bean 实例了

  * `beforeSingletonCreation(beanName)`：检查构造注入的依赖，**构造参数注入产生的循环依赖无法解决**

    `!this.singletonsCurrentlyInCreation.add(beanName)`：将当前 beanName 放入到正在创建中单实例集合，放入成功说明没有产生循环依赖，失败则产生循环依赖，进入判断条件内的逻辑抛出异常

    原因：加载 A，向正在创建集合中添加了 {A}，根据 A 的构造方法实例化 A 对象，发现 A 的构造方法依赖 B，然后加载 B，B 构造方法的参数依赖于 A，又去加载 A 时来到当前方法，因为创建中集合已经存在 A，所以添加失败

  * `singletonObject = singletonFactory.getObject()`：**创建 bean**（生命周期部分详解）

  * **创建完成以后，Bean 已经初始化好，是一个完整的可使用的 Bean**

  * `afterSingletonCreation(beanName)`：从正在创建中的集合中移出

  * `addSingleton(beanName, singletonObject)`：**添加一级缓存单例池中，从二级三级缓存移除**

  `bean = getObjectForBeanInstance`：**单实例可能是普通单实例或者 FactoryBean**，如果是 FactoryBean 实例，需要判断 name 是带 & 还是不带 &，带 & 说明 getBean 获取 FactoryBean 对象，否则是获取 FactoryBean 内部管理的实例

  * 参数 name 是未处理 & 的 name，beanName 是处理过 & 和别名后的 name

  * `if(BeanFactoryUtils.isFactoryDereference(name))`：判断 doGetBean 中参数 name 前是否带 &，不是处理后的

  * `if(!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name))`：Bean 是普通单实例或者是 FactoryBean 就可以直接返回，否则进入下面的获取 **FactoryBean 内部管理的实例**的逻辑

  * `getCachedObjectForFactoryBean(beanName)`：尝试到缓存获取，获取到直接返回，获取不到进行下面逻辑

  * `if (mbd == null && containsBeanDefinition(beanName))`：Spring 中有当前 beanName 的 BeanDefinition 信息

    `mbd = getMergedLocalBeanDefinition(beanName)`：获取合并后的 BeanDefinition

  * `mbd.isSynthetic()`：默认值是 false 表示这是一个用户对象，如果是 true 表示是系统对象

  * `object = getObjectFromFactoryBean(factory, beanName, !synthetic)`：从工厂内获取实例

    * `factory.isSingleton() && containsSingleton(beanName)`：工厂内部维护的对象是单实例并且一级缓存存在该 bean
    * 首先去缓存中获取，获取不到就**使用工厂获取**然后放入缓存，进行循环依赖判断

* `else if (mbd.isPrototype())`：**bean 是原型的 bean**

  `beforePrototypeCreation(beanName)`：当前线程正在创建的原型对象 beanName 存入 prototypesCurrentlyInCreation

  * `curVal = this.prototypesCurrentlyInCreation.get()`：获取当前线程的正在创建的原型类集合
  * `this.prototypesCurrentlyInCreation.set(beanName)`：集合为空就把当前 beanName 加入
  * `if (curVal instanceof String)`：已经有线程相关原型类创建了，把当前的创建的加进去

  `createBean(beanName, mbd, args)`：创建原型类对象，不需要三级缓存

  `afterPrototypeCreation(beanName)`：从正在创建中的集合中移除该 beanName， **与 beforePrototypeCreation逻辑相反**

* `convertIfNecessary()`：**依赖检查**，检查所需的类型是否与实际 bean 实例的类型匹配

* `return (T) bean`：返回创建完成的 bean




***



#### 生命周期

##### 四个阶段

Bean 的生命周期：实例化 instantiation，填充属性 populate，初始化 initialization，销毁 destruction

AbstractAutowireCapableBeanFactory.createBean()：进入 Bean 生命周期的流程

* `resolvedClass = resolveBeanClass(mbd, beanName)`：判断 mdb 中的 class 是否已经**加载到 JVM**，如果未加载则使用类加载器将 beanName 加载到 JVM中并返回 class 对象
* `if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null)`：条件成立封装 mbd 并把 resolveBeanClass 设置到 bd 中
  * 条件二：mbd 在 resolveBeanClass 之前是否有 class
  * 条件三：mbd 有 className
* `bean = resolveBeforeInstantiation(beanName, mbdToUse)`：实例化前的后置处理器返回一个代理实例对象（不是 AOP）
  * 自定义类继承 InstantiationAwareBeanPostProcessor，重写 postProcessBeforeInstantiation 方法，**方法逻辑为创建对象** 
  * 并配置文件 `<bean class="intefacePackage.MyInstantiationAwareBeanPostProcessor">` 导入为 bean
  * 条件成立，**短路操作**，直接 return bean

* `Object beanInstance = doCreateBean(beanName, mbdToUse, args)`：Do it

AbstractAutowireCapableBeanFactory.**doCreateBean**(beanName, RootBeanDefinition, Object[] args)：创建 Bean

* `BeanWrapper instanceWrapper = null`：**Spring 给所有创建的 Bean 实例包装成 BeanWrapper**，内部最核心的方法是获取实例，提供了一些额外的接口方法，比如属性访问器

* `instanceWrapper = this.factoryBeanInstanceCache.remove(beanName)`：单例对象尝试从缓存中获取，会移除缓存

* `createBeanInstance()`：**缓存中没有实例就进行创建实例**（逻辑复杂，下一小节详解）

* `if (!mbd.postProcessed)`：每个 bean 只进行一次该逻辑

  `applyMergedBeanDefinitionPostProcessors()`：**后置处理器，合并 bd 信息**，接下来要属性填充了

  `AutowiredAnnotationBeanPostProcessor.postProcessMergedBeanDefinition()`：**后置处理逻辑（@Autowired）**

  * `metadata = findAutowiringMetadata(beanName, beanType, null)`：提取当前 bean 整个继承体系内的 **@Autowired、@Value、@Inject** 信息，存入一个 InjectionMetadata 对象，保存着当前 bean 信息和要自动注入的字段信息

    ```java
    private final Class<?> targetClass;							//当前 bean 
    private final Collection<InjectedElement> injectedElements;	//要注入的信息集合
    ```

    * `metadata = buildAutowiringMetadata(clazz)`：查询当前 clazz 感兴趣的注解信息

      * `ReflectionUtils.doWithLocalFields()`：提取**字段**的注解的信息

        `findAutowiredAnnotation(field)`：代表感兴趣的注解就是那三种注解，获取这三种注解的元数据

      * `ReflectionUtils.doWithLocalMethods()`：提取**方法**的注解的信息

      * `do{} while (targetClass != null && targetClass != Object.class)`：循环从父类中解析，直到 Object 类

    * `this.injectionMetadataCache.put(cacheKey, metadata)`：存入缓存

  `mbd.postProcessed = true`：设置为 true，下次访问该逻辑不会再进入

* `earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName)`：单例、解决循环引用、是否在单例正在创建集合中

  ```java
  if (earlySingletonExposure) {
      // 【放入三级缓存一个工厂对象，用来获取提前引用】
      addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
      // lamda 表达式，用来获取提前引用，循环依赖部分详解该逻辑
  }
  ```




* ` populateBean(beanName, mbd, instanceWrapper)`：**属性填充，依赖注入，整体逻辑是先处理标签再处理注解，填充至 pvs 中，最后通过 apply 方法最后完成属性依赖注入到 BeanWrapper **

  * `if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName))`：实例化后的后置处理器，默认返回 true，可以自定义类继承 InstantiationAwareBeanPostProcessor 修改后置处理方法的返回值为 false，使 continueWithPropertyPopulation 为 false，**会导致直接返回，不进行属性的注入**

  * `if (!continueWithPropertyPopulation)`：自定义方法返回值会造成该条件成立，逻辑为直接返回，**不进行依赖注入**

  * `PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null)`：处理依赖注入逻辑开始

  * `mbd.getResolvedAutowireMode() == ?`：**根据 bean 标签配置的 autowire** 判断是 BY_NAME 或者 BY_TYPE

    `autowireByName(beanName, mbd, bw, newPvs)`：根据字段名称去获取依赖的 bean，还没注入，只是添加到 pvs

    * `propertyNames = unsatisfiedNonSimpleProperties(mbd, bw)`：bean 实例中有该字段和该字段的 setter 方法，但是在 bd 中没有 property 属性

      * 拿到配置的 property 信息和 bean 的所有字段信息

      * `pd.getWriteMethod() != null`：**当前字段是否有 set 方法，配置类注入的方式需要 set 方法**

        `!isExcludedFromDependencyCheck(pd)`：当前字段类型是否在忽略自动注入的列表中

        `!pvs.contains(pd.getName()`：当前字段不在 xml 或者其他方式的配置中，也就是 bd 中不存在对应的 property

        `!BeanUtils.isSimpleProperty(pd.getPropertyType()`：是否是基本数据类型和内置的几种数据类型，基本数据类型不允许自动注入

    * `if (containsBean(propertyName))`：BeanFactory 中存在当前 property 的 bean 实例，说明找到对应的依赖数据

    * `getBean(propertyName)`：**拿到 propertyName 对应的 bean 实例**

    * `pvs.add(propertyName, bean)`：填充到 pvs 中

    * `registerDependentBean(propertyName, beanName))`：添加到两个依赖 Map（dependsOn）中

    `autowireByType(beanName, mbd, bw, newPvs)`：根据字段类型去查找依赖的 bean

    * `desc = new AutowireByTypeDependencyDescriptor(methodParam, eager)`：依赖描述信息
    * `resolveDependency(desc, beanName, autowiredBeanNames, converter)`：根据描述信息，查找依赖对象，容器中没有对应的实例但是有对应的 BD，会调用 getBean(Type) 获取对象

    `pvs = newPvs`：newPvs 是处理了依赖数据后的 pvs，所以赋值给 pvs

  * `hasInstAwareBpps`：表示当前是否有 InstantiationAwareBeanPostProcessors 的后置处理器（Autowired）

  * `pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName)`：**@Autowired 注解的注入**，这个传入的 pvs 对象，最后原封不动的返回，不会添加东西

    * `findAutowiringMetadata()`：包装着当前 bd 需要注入的注解信息集合，**三种注解的元数据**，直接缓存获取

    * `InjectionMetadata.InjectedElement.inject()`：遍历注解信息解析后注入到 Bean，方法和字段的注入实现不同

      以字段注入为例：

      * `value = resolveFieldValue(field, bean, beanName)`：处理字段属性值

        `value = beanFactory.resolveDependency()`：解决依赖

        `result = doResolveDependency()`：**真正处理自动注入依赖的逻辑**

        * `Object shortcut = descriptor.resolveShortcut(this)`：默认返回 null

        * `Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor)`：**获取 @Value 的值**

        * `converter.convertIfNecessary(value, type, descriptor.getTypeDescriptor())`：如果 value 不是 null，就直接进行类型转换返回数据

        * `matchingBeans = findAutowireCandidates(beanName, type, descriptor)`：如果 value 是空说明字段是引用类型，**获取 @Autowired 的 Bean**

          ```java
          // addCandidateEntry() → Object beanInstance = descriptor.resolveCandidate()
          public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) throws BeansException {
          	// 获取 bean
              return beanFactory.getBean(beanName);
          }
          ```

      * `ReflectionUtils.makeAccessible(field)`：修改访问权限

      * `field.set(bean, value)`：获取属性访问器为此 field 对象赋值

  * `applyPropertyValues()`：**将所有解析的 PropertyValues 的注入至 BeanWrapper 实例中**（深拷贝）

    * `if (pvs.isEmpty())`：注解 @Autowired 和 @Value 标注的信息在后置处理的逻辑注入完成，此处为空直接返回
    * 下面的逻辑进行 XML 配置的属性的注入，首先获取转换器进行数据转换，然后**获取 WriteMethod (set) 方法进行反射调用**，完成属性的注入



* `initializeBean(String,Object,RootBeanDefinition)`：**初始化，分为配置文件和实现接口两种方式**

  * `invokeAwareMethods(beanName, bean)`：根据 bean 是否实现 Aware 接口执行初始化的方法

  * `wrappedBean = applyBeanPostProcessorsBeforeInitialization`：初始化前的后置处理器，可以继承接口重写方法

    * `processor.postProcessBeforeInitialization()`：执行后置处理的方法，默认返回 bean 本身
    * `if (current == null) return result`：重写方法返回 null，会造成后置处理的短路，直接返回

  * `invokeInitMethods(beanName, wrappedBean, mbd)`：**反射执行初始化方法**

    * `isInitializingBean = (bean instanceof InitializingBean)`：初始化方法的定义有两种方式，一种是自定义类实现 InitializingBean 接口，另一种是配置文件配置 <bean id="..." class="..." init-method="init"/ >

    * `isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))`：

      * 条件一：当前 bean 是不是实现了 InitializingBean 

      * 条件二：InitializingBean 接口中的方法 afterPropertiesSet，判断该方法是否是容器外管理的方法

    * `if (mbd != null && bean.getClass() != NullBean.class)`：成立说明是配置文件的方式

      `if(!(接口条件))`表示**如果通过接口实现了初始化方法的话，就不会在调用配置类中 init-method 定义的方法**

      `((InitializingBean) bean).afterPropertiesSet()`：调用方法

      `invokeCustomInitMethod`：执行自定义的方法

      * `initMethodName = mbd.getInitMethodName()`：获取方法名
      * `Method initMethod = ()`：根据方法名获取到 init-method 方法
      * ` methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(initMethod)`：将方法转成从接口层面获取
      * `ReflectionUtils.makeAccessible(methodToInvoke)`：访问权限设置成可访问
      * ` methodToInvoke.invoke(bean)`：**反射调用初始化方法**，以当前 bean 为角度去调用

  * `wrappedBean = applyBeanPostProcessorsAfterInitialization`：初始化后的后置处理器

    * `AbstractAutoProxyCreator.postProcessAfterInitialization()`：如果 Bean 被子类标识为要代理的 bean，则使用配置的拦截器**创建代理对象**，AOP 部分详解

    * 如果不存在循环依赖，创建动态代理 bean 在此处完成；否则真正的创建阶段是在属性填充时获取提前引用的阶段，**循环依赖**详解，源码分析：

      ```java
      // 该集合用来避免重复将某个 bean 生成代理对象，
      private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);
      
      public Object postProcessAfterInitialization(@Nullable Object bean,String bN){
          if (bean != null) {
              // cacheKey 是 beanName 或者加上 &
              Object cacheKey = getCacheKey(bean.getClass(), beanName);y
                  if (this.earlyProxyReferences.remove(cacheKey) != bean) {
                      // 去提前代理引用池中寻找该key，不存在则创建代理
                      // 如果存在则证明被代理过，则判断是否是当前的 bean，不是则创建代理
                      return wrapIfNecessary(bean, bN, cacheKey);
                  }
          }
          return bean;
      }
      ```




* `if (earlySingletonExposure)`：是否允许提前引用

  `earlySingletonReference = getSingleton(beanName, false)`：**从二级缓存获取实例**，放入一级缓存是在 doGetBean 中的sharedInstance = getSingleton() 逻辑中，此时在 createBean 的逻辑还没有返回，所以一级缓存没有

  `if (earlySingletonReference != null)`：当前 bean 实例从二级缓存中获取到了，说明**产生了循环依赖**，在属性填充阶段会提前调用三级缓存中的工厂生成 Bean 的代理对象（或原始实例），放入二级缓存中，然后使用原始 bean 继续执行初始化

  * ` if (exposedObject == bean)`：**初始化后的 bean == 创建的原始实例**，条件成立的两种情况：当前的真实实例不需要被代理；当前实例存在循环依赖已经被提前代理过了，初始化时的后置处理器直接返回 bean 原实例

    `exposedObject = earlySingletonReference`：**把代理后的 Bean 传给 exposedObject 用来返回，因为只有代理对象才封装了拦截器链，main 方法中用代理对象调用方法时会进行增强，代理是对原始对象的包装，所以这里返回的代理对象中含有完整的原实例（属性填充和初始化后的），是一个完整的代理对象，返回后外层方法会将当前 Bean 放入一级缓存**

  * `else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName))`：是否有其他 bean 依赖当前 bean，执行到这里说明是不存在循环依赖、存在增强代理的逻辑，也就是正常的逻辑

    * `dependentBeans = getDependentBeans(beanName)`：取到依赖当前 bean 的其他 beanName

    * `if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean))`：判断 dependentBean 是否创建完成

      * `if (!this.alreadyCreated.contains(beanName))`：成立当前 bean 尚未创建完成，当前 bean 是依赖exposedObject 的 bean，返回 true

    * `return false`：创建完成返回 false

      `actualDependentBeans.add(dependentBean)`：创建完成的 dependentBean 加入该集合

    * `if (!actualDependentBeans.isEmpty())`：条件成立说明有依赖于当前 bean 的 bean 实例创建完成，但是当前的 bean 还没创建完成返回，依赖当前 bean 的外部 bean 持有的是不完整的 bean，所以需要报错

* `registerDisposableBeanIfNecessary`：判断当前 bean 是否需要**注册析构函数回调**，当容器销毁时进行回调

  * `if (!mbd.isPrototype() && requiresDestruction(bean, mbd))`

    * 如果是原型 prototype 不会注册析构回调，不会回调该函数，对象的回收由 JVM 的 GC 机制完成

    * requiresDestruction()：

      * `DisposableBeanAdapter.hasDestroyMethod(bean, mbd)`：bd 中定义了 DestroyMethod 返回 true

      * `hasDestructionAwareBeanPostProcessors()`：后处理器框架决定是否进行析构回调

  * `registerDisposableBean()`：条件成立进入该方法，给当前单实例注册回调适配器，适配器内根据当前 bean 实例是继承接口（DisposableBean）还是自定义标签来判定具体调用哪个方法实现

* `this.disposableBeans.put(beanName, bean)`：向销毁集合添加实例



****



##### 创建实例

AbstractAutowireCapableBeanFactory.createBeanInstance(beanName, RootBeanDefinition, Object[] args)

* `resolveBeanClass(mbd, beanName)`：确保 Bean 的 Class 真正的被加载

* 判断类的访问权限是不是 public，不是进入下一个判断，是否允许访问类的 non-public 的构造方法，不允许则报错

* `Supplier<?> instanceSupplier = mbd.getInstanceSupplier()`：获取创建实例的函数，可以自定义，没有进入下面的逻辑

* `if (mbd.getFactoryMethodName() != null)`：**判断 bean 是否设置了 factory-method 属性，优先使用**

  <bean class="" factory-method="">，设置了该属性进入 factory-method 方法创建实例

* `resolved = false`：代表 bd 对应的构造信息是否已经解析成可以反射调用的构造方法

* `autowireNecessary = false`：是否自动匹配构造方法

* `if(mbd.resolvedConstructorOrFactoryMethod != null)`：获取 bd 的构造信息转化成反射调用的 method 信息

  * method 为 null 则 resolved 和 autowireNecessary 都为默认值 false
  * `autowireNecessary = mbd.constructorArgumentsResolved`：构造方法有参数，设置为 true

* **bd 对应的构造信息解析完成，可以直接反射调用构造方法了**：

  * `return autowireConstructor(beanName, mbd, null, null)`：**有参构造**，根据参数匹配最优的构造器创建实例

  * `return instantiateBean(beanName, mbd)`：**无参构造方法通过反射创建实例**

    * `SimpleInstantiationStrategy.instantiate()`：**真正用来实例化的函数**（无论如何都会走到这一步）

      * `if (!bd.hasMethodOverrides())`：没有方法重写覆盖

        `BeanUtils.instantiateClass(constructorToUse)`：调用 `Constructor.newInstance()` 实例化

      * `instantiateWithMethodInjection(bd, beanName, owner)`：**有方法重写采用 CGLIB  实例化**

    * `BeanWrapper bw = new BeanWrapperImpl(beanInstance)`：包装成 BeanWrapper 类型的对象

    * `return bw`：返回实例

* `ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName)`：**@Autowired 注解**，对应的后置处理器 AutowiredAnnotationBeanPostProcessor 逻辑

  * 配置了 lookup 的相关逻辑

  * `this.candidateConstructorsCache.get(beanClass)`：从缓存中获取构造方法，第一次获取为 null，进入下面逻辑

  * `rawCandidates = beanClass.getDeclaredConstructors()`：获取所有的构造器

  * `Constructor<?> requiredConstructor = null`：唯一的选项构造器，**@Autowired(required = "true")** 时有值

  * `for (Constructor<?> candidate : rawCandidates)`：遍历所有的构造器：

    `ann = findAutowiredAnnotation(candidate)`：有三种注解中的一个会返回注解的属性

    * 遍历 this.autowiredAnnotationTypes 中的三种注解：

      ```java
      this.autowiredAnnotationTypes.add(Autowired.class);//！！！！！！！！！！！！！！
      this.autowiredAnnotationTypes.add(Value.class);
      this.autowiredAnnotationTypes.add(...ClassUtils.forName("javax.inject.Inject"));
      ```

    * ` AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type)`：获取注解的属性

    * `if (attributes != null) return attributes`：任意一个注解属性不为空就注解返回

    `if (ann == null)`：注解属性为空

    * `userClass = ClassUtils.getUserClass(beanClass)`：如果当前 beanClass 是代理对象，方法上就已经没有注解了，所以**获取原始的用户类型重新获取该构造器上的注解属性**（**事务注解失效**也是这个原理）

    `if (ann != null)`：注解属性不为空了

    * `required = determineRequiredStatus(ann)`：获取 required 属性的值

      * `!ann.containsKey(this.requiredParameterName) || `：判断属性是否包含 required，不包含进入后面逻辑
      * `this.requiredParameterValue == ann.getBoolean(this.requiredParameterName)`：获取属性值返回

    * `if (required)`：代表注解 @Autowired(required = true)

      `if (!candidates.isEmpty())`：true 代表只能有一个构造方法，构造集合不是空代表可选的构造器不唯一，报错

      `requiredConstructor = candidate`：把构造器赋值给 requiredConstructor

    * `candidates.add(candidate)`：把当前构造方法添加至 candidates 集合

    ` if(candidate.getParameterCount() == 0)`：当前遍历的构造器的参数为 0 代表没有参数，是**默认构造器**，赋值给 defaultConstructor 

  * `candidateConstructors = candidates.toArray(new Constructor<?>[0])`：**将构造器转成数组返回**

* `if(ctors != null)`：条件成立代表指定了**构造方法数组**

  `mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR`：<bean autowire=""> 标签内 autowiremode 的属性值，默认是 no，AUTOWIRE_CONSTRUCTOR 代表选择最优的构造方法

  `mbd.hasConstructorArgumentValues()`：bean 信息中是否配置了构造参数的值

  `!ObjectUtils.isEmpty(args)`：getBean 时，指定了参数 arg

* `return autowireConstructor(beanName, mbd, ctors, args)`：**选择最优的构造器进行创建实例**（复杂，不建议研究）

  * `beanFactory.initBeanWrapper(bw)`：向 BeanWrapper 中注册转换器，向工厂中注册属性编辑器

  * `Constructor<?> constructorToUse = null`：实例化反射构造器

    `ArgumentsHolder argsHolderToUse`：实例化时真正去用的参数，并持有对象

    * rawArguments 是转换前的参数，arguments 是类型转换完成的参数

    `Object[] argsToUse`：参数实例化时使用的参数

  * `Object[] argsToResolve`：表示构造器参数做转换后的参数引用

  * `if (constructorToUse != null && mbd.constructorArgumentsResolved)`：

    * 条件一成立说明当前 bd 生成的实例不是第一次，缓存中有解析好的构造器方法可以直接拿来反射调用
    * 条件二成立说明构造器参数已经解析过了

  * `argsToUse = resolvePreparedArguments()`：argsToResolve 不是完全解析好的，还需要继续解析

  * `if (constructorToUse == null || argsToUse == null)`：条件成立说明缓存机制失败，进入构造器匹配逻辑

  * `Constructor<?>[] candidates = chosenCtors`：chosenCtors  只有在构造方法上有 autowaire 三种注解时才有数据

  * `if (candidates == null)`：candidates 为空就根据 beanClass 是否允许访问非公开的方法来获取构造方法

  * `if (candidates.length == 1 && explicitArgs == null && !mbd.hasConstructorArgumentValues())`：默认无参

    `bw.setBeanInstance(instantiate())`：**使用无参构造器反射调用，创建出实例对象，设置到 BeanWrapper 中去**

  * `boolean autowiring`：**需要选择最优的构造器**

  * `cargs = mbd.getConstructorArgumentValues()`：获取参数值

    `resolvedValues = new ConstructorArgumentValues()`：获取已经解析后的构造器参数值

    * `final Map<Integer, ValueHolder> indexedArgumentValues`：key 是 index， value 是值
    * `final List<ValueHolder> genericArgumentValues`：没有 index 的值

    `minNrOfArgs = resolveConstructorArguments(..,resolvedValues)`：从 bd 中解析并获取构造器参数的个数

    * `valueResolver.resolveValueIfNecessary()`：将引用转换成真实的对象
    * `resolvedValueHolder.setSource(valueHolder)`：将对象填充至 ValueHolder 中
    * ` resolvedValues.addIndexedArgumentValue()`：将参数值封装至 resolvedValues 中

  * `AutowireUtils.sortConstructors(candidates)`：排序规则 public > 非公开的 > 参数多的 > 参数少的

  * ` int minTypeDiffWeight = Integer.MAX_VALUE`：值越低说明构造器**参数列表类型**和构造参数的匹配度越高

  * `Set<Constructor<?>> ambiguousConstructors`：模棱两可的构造器，两个构造器匹配度相等时放入

  * `for (Constructor<?> candidate : candidates)`：遍历筛选出 minTypeDiffWeight 最低的构造器

  * `Class<?>[] paramTypes = candidate.getParameterTypes()`：获取当前处理的构造器的参数类型

  * `if()`：candidates 是排过序的，当前筛选出来的构造器的优先级一定是优先于后面的 constructor

  * `if (paramTypes.length < minNrOfArgs)`：需求的小于给的，不匹配

  * `int typeDiffWeight`：获取匹配度

    * `mbd.isLenientConstructorResolution()`：true 表示 ambiguousConstructors 允许有数据，false 代表不允许有数据，有数据就报错（LenientConstructorResolution：宽松的构造函数解析）
    * `argsHolder.getTypeDifferenceWeight(paramTypes)`：选择参数转换前和转换后匹配度最低的，循环向父类中寻找该方法，直到寻找到 Obejct 类

  * ` if (typeDiffWeight < minTypeDiffWeight)`：条件成立说明当前循环处理的构造器更优

  * `else if (constructorToUse != null && typeDiffWeight == minTypeDiffWeight)`：当前处理的构造器的计算出来的 DiffWeight 与上一次筛选出来的最优构造器的值一致，说明有模棱两可的情况

  * `if (constructorToUse == null)`：未找到可以使用的构造器，报错

  * ` else if (ambiguousConstructors != null && !mbd.isLenientConstructorResolution())`：模棱两可有数据，LenientConstructorResolution == false，所以报错

  * `argsHolderToUse.storeCache(mbd, constructorToUse)`：匹配成功，进行缓存，方便后来者使用该 bd 实例化

  * ` bw.setBeanInstance(instantiate(beanName, mbd, constructorToUse, argsToUse))`：匹配成功调用 instantiate 创建出实例对象，设置到 BeanWrapper 中去

* `return instantiateBean(beanName, mbd)`：默认走到这里



****



#### 循环依赖

##### 循环引用

循环依赖：是一个或多个对象实例之间存在直接或间接的依赖关系，这种依赖关系构成一个环形调用

Spring 循环依赖有四种：

* DependsOn 依赖加载【无法解决】（两种 Map）
* 原型模式 Prototype 循环依赖【无法解决】（正在创建集合）
* 单例 Bean 循环依赖：构造参数产生依赖【无法解决】（正在创建集合，getSingleton() 逻辑中）
* 单例 Bean 循环依赖：setter 产生依赖【可以解决】

解决循环依赖：提前引用，提前暴露创建中的 Bean

* Spring 先实例化 A，拿到 A 的构造方法反射创建出来 A 的早期实例对象，这个对象被包装成 ObjectFactory 对象，放入三级缓存
* 处理 A 的依赖数据，检查发现 A 依赖 B 对象，所以 Spring 就会去根据 B 类型到容器中去 getBean(B)，这里产生递归
* 拿到 B 的构造方法，进行反射创建出来 B 的早期实例对象，也会把 B 包装成 ObjectFactory 对象，放到三级缓存，处理 B 的依赖数据，检查发现 B 依赖了 A 对象，然后 Spring 就会去根据 A 类型到容器中去 getBean(A.class)
* 这时从三级缓存中获取到 A 的早期对象进入属性填充

循环依赖的三级缓存：

```java
//一级缓存：存放所有初始化完成单实例 bean，单例池，key是beanName，value是对应的单实例对象引用
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

//二级缓存：存放实例化未进行初始化的 Bean，提前引用池
private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

/** Cache of singleton factories: bean name to ObjectFactory. 3*/
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
```

* 为什么需要三级缓存？

  * 循环依赖解决需要提前引用动态代理对象，AOP 动态代理是在 Bean 初始化后的后置处理中进行，这时的 bean 已经是成品对象。因为需要提前进行动态代理，三级缓存的 ObjectFactory 提前产生需要代理的对象，把提前引用放入二级缓存
  * 如果只有二级缓存，提前引用就直接放入了一级缓存，然后 Bean 初始化完成后又会放入一级缓存，产生数据覆盖，**导致提前引用的对象和一级缓存中的并不是同一个对象**
  * 一级缓存只能存放完整的单实例，**为了保证 Bean 的生命周期不被破坏**，不能将未初始化的 Bean 暴露到一级缓存
  * 若存在循环依赖，**后置处理不创建代理对象，真正创建代理对象的过程是在 getBean(B) 的阶段中**

* 三级缓存一定会创建提前引用吗？

  * 出现循环依赖就会去三级缓存获取提前引用，不出现就不会，走正常的逻辑，创建完成直接放入一级缓存
  * 存在循环依赖，就创建代理对象放入二级缓存，如果没有增强方法就返回 createBeanInstance 创建的实例，因为 addSingletonFactory 参数中传入了实例化的 Bean，在 singletonFactory.getObject() 中返回给 singletonObject，所以**存在循环依赖就一定会使用工厂**，但是不一定创建的是代理对象，不需要增强就是原始对象

* wrapIfNecessary 一定创建代理对象吗？（AOP 动态代理部分有源码解析）

  * 存在增强器会创建动态代理，不需要增强就不需要创建动态代理对象
  * 存在循环依赖会提前增强，初始化后不需要增强

* 什么时候将 Bean 的引用提前暴露给第三级缓存的 ObjectFactory 持有？

  * 实例化之后，依赖注入之前

    ```java
    createBeanInstance -> addSingletonFactory -> populateBean
    ```

    



***



##### 源码解析

假如 A 依赖 B，B 依赖 A

* 当 A 创建实例后填充属性前，执行：

  ````java
  addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean))
  ````

  ```java
  // 添加给定的单例工厂以构建指定的单例
  protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
      Assert.notNull(singletonFactory, "Singleton factory must not be null");
      synchronized (this.singletonObjects) {
          // 单例池包含该Bean说明已经创建完成，不需要循环依赖
          if (!this.singletonObjects.containsKey(beanName)) {
              //加入三级缓存
              this.singletonFactories.put(beanName,singletonFactory);
              this.earlySingletonObjects.remove(beanName);
              // 从二级缓存移除，因为三个Map中都是一个对象，不能同时存在！
              this.registeredSingletons.add(beanName);
          }
      }
  }
  ```

* 填充属性时 A 依赖 B，这时需要 getBean(B)，也会把 B 的工厂放入三级缓存，接着 B 填充属性时发现依赖 A，去进行**第一次 ** getSingleton(A)

  ```java
  public Object getSingleton(String beanName) {
      return getSingleton(beanName, true);//为true代表允许拿到早期引用。
  }
  protected Object getSingleton(String beanName, boolean allowEarlyReference) {
      // 在一级缓存中获取 beanName 对应的单实例对象。
      Object singletonObject = this.singletonObjects.get(beanName);
      // 单实例确实尚未创建；单实例正在创建，发生了循环依赖
      if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
          synchronized (this.singletonObjects) {
              // 从二级缓存获取
              singletonObject = this.earlySingletonObjects.get(beanName);
              // 二级缓存不存在，并且允许获取早期实例对象，去三级缓存查看
              if (singletonObject == null && allowEarlyReference) {
                  ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                  if (singletonFactory != null) {
                      // 从三级缓存获取工厂对象，并得到 bean 的提前引用
                      singletonObject = singletonFactory.getObject();
                      // 【缓存升级】，放入二级缓存，提前引用池
                      this.earlySingletonObjects.put(beanName, singletonObject);
                      // 从三级缓存移除该对象
                      this.singletonFactories.remove(beanName);
                  }
              }
          }
      }
      return singletonObject;
  }
  ```

* 从三级缓存获取 A 的 Bean：`singletonFactory.getObject()`，调用了 lambda 表达式的 getEarlyBeanReference 方法：

  ```java
  public Object getEarlyBeanReference(Object bean, String beanName) {
      Object cacheKey = getCacheKey(bean.getClass(), beanName);
      // 【向提前引用代理池 earlyProxyReferences 中添加该 Bean，防止对象被重新代理】
      this.earlyProxyReferences.put(cacheKey, bean);
      // 创建代理对象，createProxy
      return wrapIfNecessary(bean, beanName, cacheKey);
  }
  ```

* B 填充了 A 的提前引用后会继续初始化直到完成，**返回原始 A 的逻辑继续执行**



***



### AOP

#### 注解原理

@EnableAspectJAutoProxy：AOP 注解驱动，给容器中导入 AspectJAutoProxyRegistrar

```java
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {
    // 是否强制使用 CGLIB 创建代理对象 
    // 配置文件方式：<aop:aspectj-autoproxy proxy-target-class="true"/>
	boolean proxyTargetClass() default false;
	
    // 将当前代理对象暴露到上下文内，方便代理对象内部的真实对象拿到代理对象
    // 配置文件方式：<aop:aspectj-autoproxy expose-proxy="true"/>
	boolean exposeProxy() default false;
}
```

AspectJAutoProxyRegistrar 在用来向容器中注册 **AnnotationAwareAspectJAutoProxyCreator**，以 BeanDefiantion 形式存在，在容器初始化时加载。AnnotationAwareAspectJAutoProxyCreator 间接实现了 InstantiationAwareBeanPostProcessor，Order 接口，该类会在 Bean 的实例化和初始化的前后起作用

工作流程：创建 IOC 容器，调用 refresh() 刷新容器，`registerBeanPostProcessors(beanFactory)` 阶段，通过 getBean() 创建 AnnotationAwareAspectJAutoProxyCreator 对象，在生命周期的初始化方法中执行回调 initBeanFactory() 方法初始化注册三个工具类：BeanFactoryAdvisorRetrievalHelperAdapter、ReflectiveAspectJAdvisorFactory、BeanFactoryAspectJAdvisorsBuilderAdapter



***



#### 后置处理

Bean 初始化完成的执行后置处理器的方法：

```java
public Object postProcessAfterInitialization(@Nullable Object bean,String bN){
    if (bean != null) {
        // cacheKey 是 【beanName 或者加上 & 的 beanName】
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
            if (this.earlyProxyReferences.remove(cacheKey) != bean) {
                // 去提前代理引用池中寻找该 key，不存在则创建代理
                // 如果存在则证明被代理过，则判断是否是当前的 bean，不是则创建代理
                return wrapIfNecessary(bean, bN, cacheKey);
            }
    }
    return bean;
}
```

AbstractAutoProxyCreator.wrapIfNecessary()：根据通知创建动态代理，没有通知直接返回原实例

```java
protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    // 条件一般不成立，很少使用 TargetSourceCreator 去创建对象 BeforeInstantiation 阶段，doCreateBean 之前的阶段
    if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    // advisedBeans 集合保存的是 bean 是否被增强过了
    // 条件成立说明当前 beanName 对应的实例不需要被增强处理，判断是在 BeforeInstantiation 阶段做的
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }
    // 条件一：判断当前 bean 类型是否是基础框架类型，这个类的实例不能被增强
    // 条件二：shouldSkip 判断当前 beanName 是否是 .ORIGINAL 结尾，如果是就跳过增强逻辑，直接返回
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // 【查找适合当前 bean 实例的增强方法】（下一节详解）
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    // 条件成立说明上面方法查询到适合当前class的通知
    if (specificInterceptors != DO_NOT_PROXY) {
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        // 根据查询到的增强创建代理对象（下一节详解）
        // 参数一：目标对象
        // 参数二：beanName
        // 参数三：匹配当前目标对象 clazz 的 Advisor 数据
        Object proxy = createProxy(
            bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        // 保存代理对象类型
        this.proxyTypes.put(cacheKey, proxy.getClass());
        // 返回代理对象
        return proxy;
    }
	// 执行到这里说明没有查到通知，当前 bean 不需要增强
    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    // 【返回原始的 bean 实例】
    return bean;
}
```



***



#### 获取通知

AbstractAdvisorAutoProxyCreator.getAdvicesAndAdvisorsForBean()：查找适合当前类实例的增强，并进行排序

```java
protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
	// 查询适合当前类型的增强通知
    List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
    if (advisors.isEmpty()) {
        // 增强为空直接返回 null，不需要创建代理
        return DO_NOT_PROXY;
    }
    // 不是空，转成数组返回
    return advisors.toArray();
}
```

AbstractAdvisorAutoProxyCreator.findEligibleAdvisors()：

* `candidateAdvisors = findCandidateAdvisors()`：**获取当前容器内可以使用（所有）的 advisor**，调用的是 AnnotationAwareAspectJAutoProxyCreator 类的方法，每个方法对应一个 Advisor 

  * `advisors = super.findCandidateAdvisors()`：**查询出 XML 配置的所有 Advisor 类型**

    * `advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors()`：通过 BF 查询出来 BD 配置的 class 中 是 Advisor 子类的 BeanName
    * `advisors.add()`：使用 Spring 容器获取当前这个 Advisor 类型的实例

  * `advisors.addAll(....buildAspectJAdvisors())`：**获取所有添加 @Aspect 注解类中的 Advisor**

    `buildAspectJAdvisors()`：构建的方法，**把 Advice 封装成 Advisor**

    * ` beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Object.class, true, false)`：获取出容器内 Object 所有的 beanName，就是全部的

    * ` for (String beanName : beanNames)`：遍历所有的 beanName，判断每个 beanName 对应的 Class 是否是 Aspect 类型，就是加了 @Aspect 注解的类

      * `factory = new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName)`：使用工厂模式管理 Aspect 的元数据，关联的真实 @Aspect 注解的实例对象

      * `classAdvisors = this.advisorFactory.getAdvisors(factory)`：添加了 @Aspect 注解的类的通知信息

        * aspectClass：@Aspect 标签的类的 class

        * `for (Method method : getAdvisorMethods(aspectClass))`：遍历**不包括 @Pointcut 注解的方法**

          `Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName)`：**将当前 method 包装成 Advisor 数据**

          * `AspectJExpressionPointcut expressionPointcut = getPointcut()`：获取切点表达式

          * `return new InstantiationModelAwarePointcutAdvisorImpl()`：把 method 中 Advice 包装成 Advisor，Spring 中每个 Advisor 内部一定是持有一个 Advice 的，Advice 内部最重要的数据是当前 method 和aspectInstanceFactory，工厂用来获取实例

            `this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut)`：实例化 Advice 对象，逻辑是获取注解信息，根据注解的不同生成对应的 Advice 对象

      * `advisors.addAll(classAdvisors)`：保存通过 @Aspect 注解定义的 Advisor 数据

    * `this.aspectBeanNames = aspectNames`：将所有 @Aspect 注解 beanName 缓存起来，表示提取 Advisor 工作完成

    * `return advisors`：返回 Advisor 列表

* `eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, ...)`：**选出匹配当前类的增强**

  * `if (candidateAdvisors.isEmpty())`：条件成立说明当前 Spring 没有可以操作的 Advisor

  * `List<Advisor> eligibleAdvisors = new ArrayList<>()`：存放匹配当前 beanClass 的 Advisors 信息

  * `for (Advisor candidate : candidateAdvisors)`：**遍历所有的 Advisor**

    ` if (canApply(candidate, clazz, hasIntroductions))`：判断遍历的 advisor 是否匹配当前的 class，匹配就加入集合

    * `if (advisor instanceof PointcutAdvisor)`：创建的 advisor 是 InstantiationModelAwarePointcutAdvisorImpl 类型

      `PointcutAdvisor pca = (PointcutAdvisor) advisor`：封装当前 Advisor

      `return canApply(pca.getPointcut(), targetClass, hasIntroductions)`：重载该方法

      * `if (!pc.getClassFilter().matches(targetClass))`：**类不匹配 Pointcut 表达式，直接返回 false**
      * `methodMatcher = pc.getMethodMatcher()`：**获取 Pointcut 方法匹配器**，类匹配进行类中方法的匹配
      * `Set<Class<?>> classes`：保存目标对象 class 和目标对象父类超类的接口和自身实现的接口
      * `if (!Proxy.isProxyClass(targetClass))`：判断当前实例是不是代理类，确保 class 内存储的数据包括目标对象的class  而不是代理类的 class
      * `for (Class<?> clazz : classes)`：**检查目标 class 和上级接口的所有方法，查看是否会被方法匹配器匹配**，如果有一个方法匹配成功，就说明目标对象 AOP 代理需要增强
        * `specificMethod = AopUtils.getMostSpecificMethod(method, targetClass)`：方法可能是接口的，判断当前类有没有该方法
        * `return (specificMethod != method && matchesMethod(specificMethod))`：**类和方法的匹配**，不包括参数

* `extendAdvisors(eligibleAdvisors)`：在 eligibleAdvisors 列表的索引 0 的位置添加 DefaultPointcutAdvisor，**封装了 ExposeInvocationInterceptor 拦截器**

* ` eligibleAdvisors = sortAdvisors(eligibleAdvisors)`：**对拦截器进行排序**，数值越小优先级越高，高的排在前面

  * 实现 Ordered 或 PriorityOrdered 接口，PriorityOrdered 的级别要优先于 Ordered，使用 OrderComparator 比较器
  * 使用 @Order（Spring 规范）或 @Priority（JDK 规范）注解，使用 AnnotationAwareOrderComparator 比较器
  * ExposeInvocationInterceptor 实现了 PriorityOrdered ，所以总是排在第一位，MethodBeforeAdviceInterceptor 没实现任何接口，所以优先级最低，排在最后

* `return eligibleAdvisors`：返回拦截器链



****



#### 创建代理

AbstractAutoProxyCreator.createProxy()：根据增强方法创建代理对象

* `ProxyFactory proxyFactory = new ProxyFactory()`：**无参构造 ProxyFactory**，此处讲解一下两种有参构造方法：

  * public ProxyFactory(Object target)：

    ```java
    public ProxyFactory(Object target) {
    	// 将目标对象封装成 SingletonTargetSource 保存到父类的字段中
       	setTarget(target);
        // 获取目标对象 class 所有接口保存到 AdvisedSupport 中的 interfaces 集合中
       	setInterfaces(ClassUtils.getAllInterfaces(target));
    }
    ```

    ClassUtils.getAllInterfaces(target) 底层调用 getAllInterfacesForClassAsSet(java.lang.Class<?>, java.lang.ClassLoader)：

    *  `if (clazz.isInterface() && isVisible(clazz, classLoader))`：
       * 条件一：判断当前目标对象是接口
       * 条件二：检查给定的类在给定的 ClassLoader 中是否可见
    *  `Class<?>[] ifcs = current.getInterfaces()`：拿到自己实现的接口，拿不到接口实现的接口
    *  `current = current.getSuperclass()`：递归寻找父类的接口，去获取父类实现的接口

  * public ProxyFactory(Class<?> proxyInterface, Interceptor interceptor)：

    ```java
    public ProxyFactory(Class<?> proxyInterface, Interceptor interceptor) {
        // 添加一个代理的接口
        addInterface(proxyInterface);
        // 添加通知，底层调用 addAdvisor
        addAdvice(interceptor);
    }
    ```

    * `addAdvisor(pos, new DefaultPointcutAdvisor(advice))`：Spring 中 Advice 对应的接口就是 Advisor，Spring 使用 Advisor 包装 Advice 实例

* `proxyFactory.copyFrom(this)`：填充一些信息到 proxyFactory

* `if (!proxyFactory.isProxyTargetClass())`：条件成立说明 proxyTargetClass 为 false（默认），两种配置方法：

  * `<aop:aspectj-autoproxy proxy-target-class="true"/> `：强制使用 CGLIB
  * `@EnableAspectJAutoProxy(proxyTargetClass = true)`

  `if (shouldProxyTargetClass(beanClass, beanName))`：如果 bd 内有 preserveTargetClass = true ，那么这个 bd 对应的 class **创建代理时必须使用 CGLIB**，条件成立设置 proxyTargetClass 为 true

  `evaluateProxyInterfaces(beanClass, proxyFactory)`：**根据目标类判定是否可以使用 JDK 动态代理**

  * `targetInterfaces = ClassUtils.getAllInterfacesForClass()`：获取当前目标对象 class 和父类的全部实现接口
  * `boolean hasReasonableProxyInterface = false`：实现的接口中是否有一个合理的接口
  * `if (!isConfigurationCallbackInterface(ifc) && !isInternalLanguageInterface(ifc) && ifc.getMethods().length > 0)`：遍历所有的接口，如果有任意一个接口满足条件，设置 hRPI 变量为 true
    * 条件一：判断当前接口是否是 Spring 生命周期内会回调的接口
    * 条件二：接口不能是 GroovyObject、Factory、MockAccess 类型的
    * 条件三：找到一个可以使用的被代理的接口
  * `if (hasReasonableProxyInterface)`：**有合理的接口，将这些接口设置到 proxyFactory 内**
  * `proxyFactory.setProxyTargetClass(true)`：**没有合理的代理接口，强制使用 CGLIB 创建对象**

* `advisors = buildAdvisors(beanName, specificInterceptors)`：匹配目标对象 clazz 的 Advisors，填充至 ProxyFactory

* `proxyFactory.setPreFiltered(true)`：设置为 true 表示传递给 proxyFactory 的 Advisors 信息做过基础类和方法的匹配

* `return proxyFactory.getProxy(getProxyClassLoader())`：创建代理对象

  ```java
  public Object getProxy() {
      return createAopProxy().getProxy();
  }
  ```

  DefaultAopProxyFactory.createAopProxy(AdvisedSupport config)：参数是一个配置对象，保存着创建代理需要的生产资料，会加锁创建，保证线程安全

  ```java
  public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
      // 条件二为 true 代表强制使用 CGLIB 动态代理
      if (config.isOptimize() || config.isProxyTargetClass() || 
          // 条件三：被代理对象没有实现任何接口或者只实现了 SpringProxy 接口，只能使用 CGLIB 动态代理
          hasNoUserSuppliedProxyInterfaces(config)) {
          Class<?> targetClass = config.getTargetClass();
          if (targetClass == null) {
              throw new AopConfigException("");
          }
          // 条件成立说明 target 【是接口或者是已经被代理过的类型】，只能使用 JDK 动态代理
          if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
              return new JdkDynamicAopProxy(config);	// 使用 JDK 动态代理
          }
          return new ObjenesisCglibAopProxy(config);	// 使用 CGLIB 动态代理
      }
      else {
          return new JdkDynamicAopProxy(config);		// 【有接口的情况下只能使用 JDK 动态代理】
      }
  }
  ```

  JdkDynamicAopProxy.getProxy(java.lang.ClassLoader)：获取 JDK 的代理对象

  ```java
    public JdkDynamicAopProxy(AdvisedSupport config) throws AopConfigException {
        // 配置类封装到 JdkDynamicAopProxy.advised 属性中
        this.advised = config;
    }
    public Object getProxy(@Nullable ClassLoader classLoader) {
        // 获取需要代理的接口数组
        Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
        
        // 查找当前所有的需要代理的接口，看是否有 equals 方法和 hashcode 方法，如果有就做一个标记
        findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        
        // 该方法最终返回一个代理类对象
        return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
        // classLoader：类加载器  proxiedInterfaces：生成的代理类，需要实现的接口集合
        // this JdkDynamicAopProxy 实现了 InvocationHandler
    }
  ```

  AopProxyUtils.completeProxiedInterfaces(this.advised, true)：获取代理的接口数组，并添加 SpringProxy 接口

  * `specifiedInterfaces = advised.getProxiedInterfaces()`：从 ProxyFactory 中拿到所有的 target 提取出来的接口
    * `if (specifiedInterfaces.length == 0)`：如果没有实现接口，检查当前 target 是不是接口或者已经是代理类，封装到 ProxyFactory 的 interfaces 集合中

  * ` addSpringProxy = !advised.isInterfaceProxied(SpringProxy.class)`：判断目标对象所有接口中是否有 SpringProxy 接口，没有的话需要添加，这个接口**标识这个代理类型是 Spring 管理的**
    * `addAdvised = !advised.isOpaque() && !advised.isInterfaceProxied(Advised.class)`：判断目标对象的所有接口，是否已经有 Advised 接口
    * ` addDecoratingProxy = (decoratingProxy && !advised.isInterfaceProxied(DecoratingProxy.class))`：判断目标对象的所有接口，是否已经有 DecoratingProxy 接口
    * `int nonUserIfcCount = 0`：非用户自定义的接口数量，接下来要添加上面的三个接口了
    * `proxiedInterfaces = new Class<?>[specifiedInterfaces.length + nonUserIfcCount]`：创建一个新的 class 数组，长度是原目标对象提取出来的接口数量和 Spring 追加的数量，然后进行 **System.arraycopy 拷贝到新数组中**
    * `int index = specifiedInterfaces.length`：获取原目标对象提取出来的接口数量，当作 index
    * `if(addSpringProxy)`：根据上面三个布尔值把接口添加到新数组中
    * `return proxiedInterfaces`：返回追加后的接口集合

  JdkDynamicAopProxy.findDefinedEqualsAndHashCodeMethods()：查找在任何定义在接口中的 equals 和 hashCode 方法

  * `for (Class<?> proxiedInterface : proxiedInterfaces)`：遍历所有的接口
    * ` Method[] methods = proxiedInterface.getDeclaredMethods()`：获取接口中的所有方法
    * `for (Method method : methods)`：遍历所有的方法
      * `if (AopUtils.isEqualsMethod(method))`：当前方法是 equals 方法，把 equalsDefined 置为 true
      * `if (AopUtils.isHashCodeMethod(method))`：当前方法是 hashCode 方法，把 hashCodeDefined 置为 true

    * `if (this.equalsDefined && this.hashCodeDefined)`：如果有一个接口中有这两种方法，直接返回



***



#### 方法增强

main() 函数中调用用户方法，会进入代理对象的 invoke 方法

JdkDynamicAopProxy 类中的 invoke 方法是真正执行代理方法

```java
// proxy：代理对象，method：目标对象的方法，args：目标对象方法对应的参数
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object oldProxy = null;
    boolean setProxyContext = false;

    // advised 就是初始化 JdkDynamicAopProxy 对象时传入的变量
    TargetSource targetSource = this.advised.targetSource;
    Object target = null;

    try {
        // 条件成立说明代理类实现的接口没有定义 equals 方法，并且当前 method 调用 equals 方法，
        // 就调用 JdkDynamicAopProxy 提供的 equals 方法
        if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
            return equals(args[0]);
        } //.....

        Object retVal;
		// 需不需要暴露当前代理对象到 AOP 上下文内
        if (this.advised.exposeProxy) {
            // 【把代理对象设置到上下文环境】
            oldProxy = AopContext.setCurrentProxy(proxy);
            setProxyContext = true;
        }

        // 根据 targetSource 获取真正的代理对象
        target = targetSource.getTarget();
        Class<?> targetClass = (target != null ? target.getClass() : null);

        // 查找【适合该方法的增强】，首先从缓存中查找，查找不到进入主方法【下文详解】
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

		// 拦截器链是空，说明当前 method 不需要被增强
        if (chain.isEmpty()) {
            Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
            retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
        }
        else {
            // 有匹配当前 method 的方法拦截器，要做增强处理，把方法信息封装到方法调用器里
            MethodInvocation invocation =
                new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // 【拦截器链驱动方法，核心】
            retVal = invocation.proceed();
        }

        Class<?> returnType = method.getReturnType();
        if (retVal != null && retVal == target &&
            returnType != Object.class && returnType.isInstance(proxy) &&
            !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
          	// 如果目标方法返回目标对象，这里做个普通替换返回代理对象
            retVal = proxy;
        }
        
        // 返回执行的结果
        return retVal;
    }
    finally {
        if (target != null && !targetSource.isStatic()) {
            targetSource.releaseTarget(target);
        }
        // 如果允许了提前暴露，这里需要设置为初始状态
        if (setProxyContext) {
            // 当前代理对象已经完成工作，【把原始对象设置回上下文】
            AopContext.setCurrentProxy(oldProxy);
        }
    }
}
```

this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass)：查找适合该方法的增强，首先从缓存中查找，获取通知时是从全部增强中获取适合当前类的，这里是**从当前类的中获取适合当前方法的增强**

* `AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance()`：向容器注册适配器，**可以将非 Advisor 类型的增强，包装成为 Advisor，将 Advisor 类型的增强提取出来对应的 MethodInterceptor**

  * `instance = new DefaultAdvisorAdapterRegistry()`：该对象向容器中注册了 MethodBeforeAdviceAdapter、AfterReturningAdviceAdapter、ThrowsAdviceAdapter 三个适配器

  * Advisor 中持有 Advice 对象

    ```java
    public interface Advisor {
    	Advice getAdvice();
    }
    ```

* `advisors = config.getAdvisors()`：获取 ProxyFactory 内部持有的增强信息

* `interceptorList = new ArrayList<>(advisors.length)`：拦截器列表有 5 个，1 个 ExposeInvocation和 4 个增强器

* `actualClass = (targetClass != null ? targetClass : method.getDeclaringClass())`：真实的目标对象类型

* `Boolean hasIntroductions = null`：引介增强，不关心

* `for (Advisor advisor : advisors)`：**遍历所有的 advisor 增强**

* `if (advisor instanceof PointcutAdvisor)`：条件成立说明当前 Advisor 是包含切点信息的，进入匹配逻辑

  `pointcutAdvisor = (PointcutAdvisor) advisor`：转成可以获取到切点信息的接口

  `if(config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass))`：当前代理被预处理，或者当前被代理的 class 对象匹配当前 Advisor 成功，只是 class 匹配成功

  * `mm = pointcutAdvisor.getPointcut().getMethodMatcher()`：获取切点的方法匹配器，不考虑引介增强

  * `match = mm.matches(method, actualClass)`：**静态匹配成功返回 true，只关注于处理类及其方法，不考虑参数**

  * `if (match)`：如果静态切点检查是匹配的，在运行的时候才进行**动态切点检查，会考虑参数匹配**（代表传入了参数）。如果静态匹配失败，直接不需要进行参数匹配，提高了工作效率

    `interceptors = registry.getInterceptors(advisor)`：提取出当前 advisor 内持有的 advice 信息 

    * `Advice advice = advisor.getAdvice()`：获取增强方法

    * `if (advice instanceof MethodInterceptor)`：当前 advice 是 MethodInterceptor 直接加入集合

    * `for (AdvisorAdapter adapter : this.adapters)`：**遍历三个适配器进行匹配**（初始化时创建的），匹配成功创建对应的拦截器返回，以 MethodBeforeAdviceAdapter 为例

      `if (adapter.supportsAdvice(advice))`：判断当前 advice 是否是对应的 MethodBeforeAdvice

      `interceptors.add(adapter.getInterceptor(advisor))`：条件成立就往拦截器链中添加 advisor

      * `advice = (MethodBeforeAdvice) advisor.getAdvice()`：**获取增强方法**
      * `return new MethodBeforeAdviceInterceptor(advice)`：**封装成 MethodBeforeAdviceInterceptor 返回**

    `interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm))`：向拦截器链添加动态匹配器

    `interceptorList.addAll(Arrays.asList(interceptors))`：将当前 advisor 内部的方法拦截器追加到 interceptorList

* `interceptors = registry.getInterceptors(advisor)`：进入 else 的逻辑，说明当前 Advisor 匹配全部 class 的全部 method，全部加入到 interceptorList

* `return interceptorList`：返回 method 方法的拦截器链

retVal = invocation.proceed()：**拦截器链驱动方法**

* `if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1)`：条件成立说明方法拦截器全部都已经调用过了（index 从 - 1 开始累加），接下来需要执行目标对象的目标方法

  `return invokeJoinpoint()`：**调用连接点（目标）方法**

* `this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex)`：**获取下一个方法拦截器**

* `if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher)`：需要运行时匹配

  `if (dm.methodMatcher.matches(this.method, targetClass, this.arguments))`：判断是否匹配成功

  * `return dm.interceptor.invoke(this)`：匹配成功，执行方法
  * `return proceed()`：匹配失败跳过当前拦截器

* `return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this)`：**一般方法拦截器都会执行到该方法，此方法内继续执行 proceed() 完成责任链的驱动，直到最后一个  MethodBeforeAdviceInterceptor 调用前置通知，然后调用 mi.proceed()，发现是最后一个拦截器就直接执行连接点（目标方法），return 到上一个拦截器的 mi.proceed() 处，依次返回到责任链的上一个拦截器执行通知方法**

图示先从上往下建立链，然后从下往上依次执行，责任链模式

* 正常执行：（环绕通知）→ 前置通知 → 目标方法 → 后置通知 → 返回通知

 * 出现异常：（环绕通知）→ 前置通知 → 目标方法 → 后置通知 → 异常通知

 * MethodBeforeAdviceInterceptor 源码：

   ```java
   public Object invoke(MethodInvocation mi) throws Throwable {
       // 先执行通知方法，再驱动责任链
       this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
       // 开始驱动目标方法执行，执行完后返回到这，然后继续向上层返回
       return mi.proceed();
   }
   ```

   AfterReturningAdviceInterceptor 源码：没有任何异常处理机制，直接抛给上层

   ```java
   public Object invoke(MethodInvocation mi) throws Throwable {
       // 先驱动责任链，再执行通知方法
       Object retVal = mi.proceed();
       this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
       return retVal;
   }
   ```

   AspectJAfterThrowingAdvice 执行异常处理：

   ```java
   public Object invoke(MethodInvocation mi) throws Throwable {
       try {
           // 默认直接驱动责任链
           return mi.proceed();
       }
       catch (Throwable ex) {
           // 出现错误才执行该方法
           if (shouldInvokeOnThrowing(ex)) {
               invokeAdviceMethod(getJoinPointMatch(), null, ex);
           }
           throw ex;
       }
   }
   ```

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Spring-AOP动态代理执行方法.png)



参考视频：https://www.bilibili.com/video/BV1gW411W7wy




***



### 事务

#### 解析方法

##### 标签解析

```xml
<tx:annotation-driven transaction-manager="txManager"/>
```

容器启动时会根据注解注册对应的解析器：

```java
public class TxNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
		registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
        // 注册解析器
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
		registerBeanDefinitionParser("jta-transaction-manager", new JtaTransactionManagerBeanDefinitionParser());
	}
}
protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
    this.parsers.put(elementName, parser);
}
```

获取对应的解析器 NamespaceHandlerSupport#findParserForElement：

```java
private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
    String localName = parserContext.getDelegate().getLocalName(element);
    // 获取对应的解析器
    BeanDefinitionParser parser = this.parsers.get(localName);
	// ...
    return parser;
}
```

调用解析器的方法对 XML 文件进行解析：

```java
public BeanDefinition parse(Element element, ParserContext parserContext) {
	// 向Spring容器注册了一个 BD -> TransactionalEventListenerFactory.class
    registerTransactionalEventListenerFactory(parserContext);
    String mode = element.getAttribute("mode");
    if ("aspectj".equals(mode)) {
        // mode="aspectj"
        registerTransactionAspect(element, parserContext);
        if (ClassUtils.isPresent("javax.transaction.Transactional", getClass().getClassLoader())) {
            registerJtaTransactionAspect(element, parserContext);
        }
    }
    else {
        // mode="proxy"，默认逻辑，不配置 mode 时
        // 用来向容器中注入一些 BeanDefinition，包括事务增强器、事务拦截器、注解解析器
        AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
    }
    return null;
}
```





****



##### 注解解析

@EnableTransactionManagement 导入 TransactionManagementConfigurationSelector，该类给 Spring 容器中两个组件：

```java
protected String[] selectImports(AdviceMode adviceMode) {
    switch (adviceMode) {
        // 导入 AutoProxyRegistrar 和 ProxyTransactionManagementConfiguration（默认）
        case PROXY:
            return new String[] {AutoProxyRegistrar.class.getName(),
                                 ProxyTransactionManagementConfiguration.class.getName()};
        // 导入 AspectJTransactionManagementConfiguration（与声明式事务无关）
        case ASPECTJ:
            return new String[] {determineTransactionAspectClass()};
        default:
            return null;
    }
}
```

AutoProxyRegistrar：给容器中注册 InfrastructureAdvisorAutoProxyCreator，**利用后置处理器机制拦截 bean 以后包装并返回一个代理对象**，代理对象中保存所有的拦截器，利用拦截器的链式机制依次进入每一个拦截器中进行拦截执行（就是 AOP 原理）

ProxyTransactionManagementConfiguration：是一个 Spring 的事务配置类，注册了三个 Bean：

* BeanFactoryTransactionAttributeSourceAdvisor：事务驱动，利用注解 @Bean 把该类注入到容器中，该增强器有两个字段：
* TransactionAttributeSource：解析事务注解的相关信息，真实类型是 AnnotationTransactionAttributeSource，构造方法中注册了三个**注解解析器**，解析 Spring、JTA、Ejb3 三种类型的事务注解
* TransactionInterceptor：**事务拦截器**，代理对象执行拦截器方法时，调用 TransactionInterceptor 的 invoke 方法，底层调用TransactionAspectSupport.invokeWithinTransaction()，通过 PlatformTransactionManager 控制着事务的提交和回滚，所以事务的底层原理就是通过 AOP 动态织入，进行事务开启和提交

注解解析器 SpringTransactionAnnotationParser **解析 @Transactional 注解**：

```java
protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
    RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
	// 从注解信息中获取传播行为
    Propagation propagation = attributes.getEnum("propagation");
    rbta.setPropagationBehavior(propagation.value());
    // 获取隔离界别
    Isolation isolation = attributes.getEnum("isolation");
    rbta.setIsolationLevel(isolation.value());
    rbta.setTimeout(attributes.getNumber("timeout").intValue());
    // 从注解信息中获取 readOnly 参数
    rbta.setReadOnly(attributes.getBoolean("readOnly"));
    // 从注解信息中获取 value 信息并且设置 qualifier，表示当前事务指定使用的【事务管理器】
    rbta.setQualifier(attributes.getString("value"));
	// 【存放的是 rollback 条件】，回滚规则放在这个集合
    List<RollbackRuleAttribute> rollbackRules = new ArrayList<>();
    // 表示事务碰到哪些指定的异常才进行回滚，不指定的话默认是 RuntimeException/Error 非检查型异常菜回滚
    for (Class<?> rbRule : attributes.getClassArray("rollbackFor")) {
        rollbackRules.add(new RollbackRuleAttribute(rbRule));
    }
    // 与 rollbackFor 功能相同
    for (String rbRule : attributes.getStringArray("rollbackForClassName")) {
        rollbackRules.add(new RollbackRuleAttribute(rbRule));
    }
    // 表示事务碰到指定的 exception 实现对象不进行回滚，否则碰到其他的class就进行回滚
    for (Class<?> rbRule : attributes.getClassArray("noRollbackFor")) {
        rollbackRules.add(new NoRollbackRuleAttribute(rbRule));
    }
    for (String rbRule : attributes.getStringArray("noRollbackForClassName")) {
        rollbackRules.add(new NoRollbackRuleAttribute(rbRule));
    }
    // 设置回滚规则
    rbta.setRollbackRules(rollbackRules);

    return rbta;
}
```





****





#### 驱动方法

TransactionInterceptor 事务拦截器的核心驱动方法：

```java
public Object invoke(MethodInvocation invocation) throws Throwable {
    // targetClass 是需要被事务增强器增强的目标类，invocation.getThis() → 目标对象 → 目标类
    Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
	// 参数一是目标方法，参数二是目标类，参数三是方法引用，用来触发驱动方法
    return invokeWithinTransaction(invocation.getMethod(), targetClass, invocation::proceed);
}

protected Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass,
                                         final InvocationCallback invocation) throws Throwable {

    // 事务属性源信息
    TransactionAttributeSource tas = getTransactionAttributeSource();
    //  提取 @Transactional 注解信息，txAttr 是注解信息的承载对象
    final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);
    // 获取 Spring 配置的事务管理器
    // 首先会检查是否通过XML或注解配置 qualifier，没有就尝试去容器获取，一般情况下为 DatasourceTransactionManager
    final PlatformTransactionManager tm = determineTransactionManager(txAttr);
    // 权限定类名.方法名，该值用来当做事务名称使用
    final String joinpointIdentification = methodIdentification(method, targetClass, txAttr);
    
	// 条件成立说明是【声明式事务】
    if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
    	// 用来【开启事务】
        TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);

        Object retVal;
        try {
            // This is an 【around advice】: Invoke the next interceptor in the chain.
            // 环绕通知，执行目标方法（方法引用方式，invocation::proceed，还是调用 proceed）
            retVal = invocation.proceedWithInvocation();
        }
        catch (Throwable ex) {
            //  执行业务代码时抛出异常，执行回滚逻辑
            completeTransactionAfterThrowing(txInfo, ex);
            throw ex;
        }
        finally {
            // 清理事务的信息
            cleanupTransactionInfo(txInfo);
        }
        // 提交事务的入口
        commitTransactionAfterReturning(txInfo);
        return retVal;
    }
    else {
       // 编程式事务，省略
    }
}
```



***



#### 开启事务

##### 事务绑定

创建事务的方法：

```java
protected TransactionInfo createTransactionIfNecessary(@Nullable PlatformTransactionManager tm,
                                                       @Nullable TransactionAttribute txAttr, 
                                                       final String joinpointIdentification) {

    // If no name specified, apply method identification as transaction name.
    if (txAttr != null && txAttr.getName() == null) {
        // 事务的名称： 类的权限定名.方法名
        txAttr = new DelegatingTransactionAttribute(txAttr) {
            @Override
            public String getName() {
                return joinpointIdentification;
            }
        };
    }
    TransactionStatus status = null;
    if (txAttr != null) {
        if (tm != null) {
            // 通过事务管理器根据事务属性创建事务状态对象，事务状态对象一般情况下包装着 事务对象，当然也有可能是null
            // 方法上的注解为 @Transactional(propagation = NOT_SUPPORTED || propagation = NEVER) 时
            // 【下一小节详解】
            status = tm.getTransaction(txAttr);
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping transactional joinpoint [" + joinpointIdentification +
                             "] because no transaction manager has been configured");
            }
        }
    }
    // 包装成一个上层的事务上下文对象
    return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
}
```

TransactionAspectSupport#prepareTransactionInfo：为事务的属性和状态准备一个事务信息对象

* `TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification)`：创建事务信息对象
* `txInfo.newTransactionStatus(status)`：填充事务的状态信息
* `txInfo.bindToThread()`：利用 ThreadLocal **把当前事务信息绑定到当前线程**，不同的事务信息会形成一个栈的结构
  * `this.oldTransactionInfo = transactionInfoHolder.get()`：获取其他事务的信息存入 oldTransactionInfo 
  * `transactionInfoHolder.set(this)`：将当前的事务信息设置到 ThreadLocalMap 中



***



##### 事务创建

```java
public final TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
    // 获取事务的对象
    Object transaction = doGetTransaction();
    boolean debugEnabled = logger.isDebugEnabled();

    if (definition == null) {
        // Use defaults if no transaction definition given.
        definition = new DefaultTransactionDefinition();
    }
	// 条件成立说明当前是事务重入的情况，事务中有 ConnectionHolder 对象
    if (isExistingTransaction(transaction)) {
        // a方法开启事务，a方法内调用b方法，b方法仍然加了 @Transactional 注解，需要检查传播行为
        return handleExistingTransaction(definition, transaction, debugEnabled);
    }
    
	// 逻辑到这说明当前线程没有连接资源，一个连接对应一个事务，没有连接就相当于没有开启事务
    // 检查事务的延迟属性
    if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
        throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
    }

    // 传播行为是 MANDATORY，没有事务就抛出异常
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
        throw new IllegalTransactionStateException();
    }
    // 需要开启事务的传播行为
    else if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
             definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
             definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
        // 什么也没挂起，因为线程并没有绑定事务
        SuspendedResourcesHolder suspendedResources = suspend(null);
        try {
            // 是否支持同步线程事务，一般是 true
            boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
            // 新建一个事务状态信息
            DefaultTransactionStatus status = newTransactionStatus(
                definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
            // 【启动事务】
            doBegin(transaction, definition);
            // 设置线程上下文变量，方便程序运行期间获取当前事务的一些核心的属性，initSynchronization() 启动同步
            prepareSynchronization(status, definition);
            return status;
        }
        catch (RuntimeException | Error ex) {
            // 恢复现场
            resume(null, suspendedResources);
            throw ex;
        }
    }
    // 不支持事务的传播行为
    else {
        // Create "empty" transaction: no actual transaction, but potentially synchronization.
        boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
        // 创建事务状态对象
        // 参数2 transaction 是 null 说明当前事务状态是未手动开启事，线程上未绑定任何的连接资源，业务程序执行时需要先去 datasource 获取的 conn，是自动提交事务的，不需要 Spring 再提交事务
        // 参数6 suspendedResources 是 null 说明当前事务状态未挂起任何事务，当前事务执行到后置处理时不需要恢复现场
        return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
    }
}
```

DataSourceTransactionManager#doGetTransaction：真正获取事务的方法

* `DataSourceTransactionObject txObject = new DataSourceTransactionObject()`：**创建事务对象**

* `txObject.setSavepointAllowed(isNestedAllowed())`：设置事务对象是否支持保存点，由事务管理器控制（默认不支持）

* `ConnectionHolder conHolder = TransactionSynchronizationManager.getResource(obtainDataSource())`：

  * 从 ThreadLocal 中获取 conHolder 资源，可能拿到 null 或者不是 null

  * 是 null：举例

    ```java
    @Transaction
    public void a() {...b.b()....}
    ```

  * 不是 null：执行 b 方法事务增强的前置逻辑时，可以拿到 a 放进去的 conHolder 资源

    ```java
    @Transaction
    public void b() {....}
    ```

* `txObject.setConnectionHolder(conHolder, false)`：将 ConnectionHolder 保存到事务对象内，参数二是 false 代表连接资源是上层事务共享的，不是新建的连接资源

* `return txObject`：返回事务的对象

DataSourceTransactionManager#doBegin：事务开启的逻辑

* `txObject = (DataSourceTransactionObject) transaction`：强转为事务对象

* 事务中没有数据库连接资源就要分配：

  `Connection newCon = obtainDataSource().getConnection()`：**获取 JDBC 原生的数据库连接对象**

  `txObject.setConnectionHolder(new ConnectionHolder(newCon), true)`：代表是新开启的事务，新建的连接对象

* `previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition)`：修改连接属性

  * `if (definition != null && definition.isReadOnly())`：注解（或 XML）配置了只读属性，需要设置

  * `if (..definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT)`：注解配置了隔离级别

    `int currentIsolation = con.getTransactionIsolation()`：获取连接的隔离界别

    `previousIsolationLevel = currentIsolation`：保存之前的隔离界别，返回该值

    ` con.setTransactionIsolation(definition.getIsolationLevel())`：**将当前连接设置为配置的隔离界别**

* `txObject.setPreviousIsolationLevel(previousIsolationLevel)`：将 Conn 原来的隔离级别保存到事务对象，为了释放 Conn 时重置回原状态

* `if (con.getAutoCommit())`：默认会成立，说明还没开启事务

  `txObject.setMustRestoreAutoCommit(true)`：保存 Conn 原来的事务状态

  `con.setAutoCommit(false)`：**开启事务，JDBC 原生的方式**

* `txObject.getConnectionHolder().setTransactionActive(true)`：表示 Holder 持有的 Conn 已经手动开启事务了

* `TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder())`：将 ConnectionHolder 对象绑定到 ThreadLocal 内，数据源为 key，为了方便获取手动开启事务的连接对象去执行 SQL



***



##### 事务重入

事务重入的核心处理逻辑：

```java
private TransactionStatus handleExistingTransaction( TransactionDefinition definition, 
                                                    Object transaction, boolean debugEnabled){
	// 传播行为是 PROPAGATION_NEVER，需要以非事务方式执行操作，如果当前事务存在则【抛出异常】
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NEVER) {
        throw new IllegalTransactionStateException();
    }
	// 传播行为是 PROPAGATION_NOT_SUPPORTED，以非事务方式运行，如果当前存在事务，则【把当前事务挂起】
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
        // 挂起事务
        Object suspendedResources = suspend(transaction);
        boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
        // 创建一个非事务的事务状态对象返回
        return prepareTransactionStatus(definition, null, false, newSynchronization, debugEnabled, suspendedResources);
    }
	// 开启新事物的逻辑
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW) {
        // 【挂起当前事务】
        SuspendedResourcesHolder suspendedResources = suspend(transaction);
       	// 【开启新事物】
    }
	// 传播行为是 PROPAGATION_NESTED，嵌套事务
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
        // Spring 默认不支持内嵌事务
        // 【开启方式】：<property name="nestedTransactionAllowed" value="true">
        if (!isNestedTransactionAllowed()) {
            throw new NestedTransactionNotSupportedException();
        }
        
        if (useSavepointForNestedTransaction()) {
            //  为当前方法创建一个 TransactionStatus 对象，
            DefaultTransactionStatus status =
                prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
            // 创建一个 JDBC 的保存点
            status.createAndHoldSavepoint();
            // 不需要使用同步，直接返回
            return status;
        }
        else {
            // Usually only for JTA transaction，开启一个新事务
        }
    }

    // Assumably PROPAGATION_SUPPORTS or PROPAGATION_REQUIRED，【使用当前的事务】
    boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
    return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
}
```



***



##### 挂起恢复

AbstractPlatformTransactionManager#suspend：**挂起事务**，并获得一个上下文信息对象

```java
protected final SuspendedResourcesHolder suspend(@Nullable Object transaction) {
    // 事务是同步状态的
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
        List<TransactionSynchronization> suspendedSynchronizations = doSuspendSynchronization();
        try {
            Object suspendedResources = null;
            if (transaction != null) {
                // do it
                suspendedResources = doSuspend(transaction);
            }
            //将上层事务绑定在线程上下文的变量全部取出来
            //...
            // 通过被挂起的资源和上层事务的上下文变量，创建一个【SuspendedResourcesHolder】返回
            return new SuspendedResourcesHolder(suspendedResources, suspendedSynchronizations, 
                                                name, readOnly, isolationLevel, wasActive);
        } //...
}
protected Object doSuspend(Object transaction) {
    DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
    // 将当前方法的事务对象 connectionHolder 属性置为 null，不和上层共享资源
    // 当前方法有可能是不开启事务或者要开启一个独立的事务
    txObject.setConnectionHolder(null);
    // 【解绑在线程上的事务】
    return TransactionSynchronizationManager.unbindResource(obtainDataSource());
}
```

AbstractPlatformTransactionManager#resume：**恢复现场**，根据挂起资源去恢复线程上下文信息

```java
protected final void resume(Object transaction, SuspendedResourcesHolder resourcesHolder) {
    if (resourcesHolder != null) {
        // 获取被挂起的事务资源
        Object suspendedResources = resourcesHolder.suspendedResources;
        if (suspendedResources != null) {
            //绑定上一个事务的 ConnectionHolder 到线程上下文
            doResume(transaction, suspendedResources);
        }
        List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
        if (suspendedSynchronizations != null) {
            //....
            // 将线程上下文变量恢复为上一个事务的挂起现场
            doResumeSynchronization(suspendedSynchronizations);
        }
    }
}
protected void doResume(@Nullable Object transaction, Object suspendedResources) {
    // doSuspend 的逆动作，【绑定资源】
    TransactionSynchronizationManager.bindResource(obtainDataSource(), suspendedResources);
}
```





***



#### 提交回滚

##### 回滚方式

```java
protected void completeTransactionAfterThrowing(@Nullable TransactionInfo txInfo, Throwable ex) {
    // 事务状态信息不为空进入逻辑
    if (txInfo != null && txInfo.getTransactionStatus() != null) {
        // 条件二成立 说明目标方法抛出的异常需要回滚事务
        if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
            try {
                // 事务管理器的回滚方法
                txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
            }
            catch (TransactionSystemException ex2) {}
        }
        else {
            // 执行到这里，说明当前事务虽然抛出了异常，但是该异常并不会导致整个事务回滚
            try {
                // 提交事务
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            }
            catch (TransactionSystemException ex2) {}
        }
    }
}
public boolean rollbackOn(Throwable ex) {
    // 继承自 RuntimeException 或 error 的是【非检查型异常】，才会归滚事务
    // 如果配置了其他回滚错误，会获取到回滚规则 rollbackRules 进行判断
    return (ex instanceof RuntimeException || ex instanceof Error);
}
```

```java
public final void rollback(TransactionStatus status) throws TransactionException {
    // 事务已经完成不需要回滚
    if (status.isCompleted()) {
        throw new IllegalTransactionStateException();
    }
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    // 开始回滚事务
    processRollback(defStatus, false);
}
```

AbstractPlatformTransactionManager#processRollback：事务回滚

* `triggerBeforeCompletion(status)`：用来做扩展逻辑，回滚前的前置处理

* `if (status.hasSavepoint())`：条件成立说明当前事务是一个**内嵌事务**，当前方法只是复用了上层事务的一个内嵌事务

  `status.rollbackToHeldSavepoint()`：内嵌事务加入事务时会创建一个保存点，此时恢复至保存点

* `if (status.isNewTransaction())`：说明事务是当前连接开启的，需要去回滚事务

  `doRollback(status)`：真正的的回滚函数

  * `DataSourceTransactionObject txObject = status.getTransaction()`：获取事务对象
  * `Connection con = txObject.getConnectionHolder().getConnection()`：获取连接对象
  * `con.rollback()`：**JDBC 的方式回滚事务**

* `else`：当前方法是共享的上层的事务，和上层使用同一个 Conn 资源，**共享的事务不能直接回滚，应该交给上层处理**

  `doSetRollbackOnly(status)`：设置 con.rollbackOnly = true，线程回到上层事务 commit 时会检查该字段，然后执行回滚操作

* `triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK)`：回滚的后置处理

* `cleanupAfterCompletion(status)`：清理和恢复现场



***



##### 提交方式

```java
protected void commitTransactionAfterReturning(@Nullable TransactionInfo txInfo) {
    if (txInfo != null && txInfo.getTransactionStatus() != null) {
        // 事务管理器的提交方法
        txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
    }
}
```

```java
public final void commit(TransactionStatus status) throws TransactionException {
    // 已经完成的事务不需要提交了
    if (status.isCompleted()) {
        throw new IllegalTransactionStateException();
    }
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    // 条件成立说明是当前的业务强制回滚
    if (defStatus.isLocalRollbackOnly()) {
        // 回滚逻辑，
        processRollback(defStatus, false);
        return;
    }
	// 成立说明共享当前事务的【下层事务逻辑出错，需要回滚】
    if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
        // 如果当前事务还是事务重入，会继续抛给上层，最上层事务会进行真实的事务回滚操作
        processRollback(defStatus, true);
        return;
    }
	// 执行提交
    processCommit(defStatus);
}
```

AbstractPlatformTransactionManager#processCommit：事务提交

* `prepareForCommit(status)`：前置处理

* `if (status.hasSavepoint())`：条件成立说明当前事务是一个**内嵌事务**，只是复用了上层事务

  `status.releaseHeldSavepoint()`：清理保存点，因为没有发生任何异常，所以保存点没有存在的意义了

* `if (status.isNewTransaction())`：说明事务是归属于当前连接的，需要去提交事务

  `doCommit(status)`：真正的提交函数

  * `Connection con = txObject.getConnectionHolder().getConnection()`：获取连接对象
  * `con.commit()`：**JDBC 的方式提交事务**

* `doRollbackOnCommitException(status, ex)`：**提交事务出错后进行回滚**

* ` cleanupAfterCompletion(status)`：清理和恢复现场



***



##### 清理现场

恢复上层事务：

```java
protected void cleanupTransactionInfo(@Nullable TransactionInfo txInfo) {
    if (txInfo != null) {
        // 从当前线程的 ThreadLocal 获取上层的事务信息，将当前事务出栈，继续执行上层事务
        txInfo.restoreThreadLocalStatus();
    }
}
private void restoreThreadLocalStatus() {
    // Use stack to restore old transaction TransactionInfo.
    transactionInfoHolder.set(this.oldTransactionInfo);
}
```

当前层级事务结束时的清理：

```java
private void cleanupAfterCompletion(DefaultTransactionStatus status) {
    // 设置当前方法的事务状态为完成状态
    status.setCompleted();
    if (status.isNewSynchronization()) {
        // 清理线程上下文变量以及扩展点注册的 sync
        TransactionSynchronizationManager.clear();
    }
    // 事务是当前线程开启的
    if (status.isNewTransaction()) {
        // 解绑资源
        doCleanupAfterCompletion(status.getTransaction());
    }
    // 条件成立说明当前事务执行的时候，【挂起了一个上层的事务】
    if (status.getSuspendedResources() != null) {
        Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
        // 恢复上层事务现场
        resume(transaction, (SuspendedResourcesHolder) status.getSuspendedResources());
    }
}
```

DataSourceTransactionManager#doCleanupAfterCompletion：清理工作

* `TransactionSynchronizationManager.unbindResource(obtainDataSource())`：解绑数据库资源

* `if (txObject.isMustRestoreAutoCommit())`：是否恢复连接，Conn 归还到 DataSource**，归还前需要恢复到申请时的状态**

  `con.setAutoCommit(true)`：恢复连接为自动提交

* `DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel())`：恢复隔离级别

* `DataSourceUtils.releaseConnection(con, this.dataSource)`：**将连接归还给数据库连接池**

* `txObject.getConnectionHolder().clear()`：清理 ConnectionHolder 资源

***

### 注解

#### Component

@Component 解析流程：

* 注解类启动容器的时，注册 ClassPathBeanDefinitionScanner 到容器，用来扫描 Bean 的相关信息

  ```java
  protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
      Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
      // 遍历指定的所有的包，【这就相当于扫描了】
      for (String basePackage : basePackages) {
          // 读取当前包下的资源装换为 BeanDefinition，字节流的方式
          Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
          for (BeanDefinition candidate : candidates) {
              // 遍历，封装，类似于 XML 的解析方式，注册到容器中
              registerBeanDefinition(definitionHolder, this.registry)
          }
      return beanDefinitions;
  }
  ```

* ClassPathScanningCandidateComponentProvider.findCandidateComponents()

  ```java
  public Set<BeanDefinition> findCandidateComponents(String basePackage) {
      if (this.componentsIndex != null && indexSupportsIncludeFilters()) {
          return addCandidateComponentsFromIndex(this.componentsIndex, basePackage);
      }
      else {
          return scanCandidateComponents(basePackage);
      }
  }
  ```

  ```java
  private Set<BeanDefinition> scanCandidateComponents(String basePackage) {}
  ```

    * `String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX resolveBasePackage(basePackage) + '/' + this.resourcePattern` ：将 package 转化为 ClassLoader 类资源搜索路径 packageSearchPath，例如：`com.sea.spring.boot` 转化为 `classpath*:com/sea/spring/boot/**/*.class`

    * `resources = getResourcePatternResolver().getResources(packageSearchPath)`：加载路径下的资源

    * `for (Resource resource : resources) `：遍历所有的资源

      `metadataReader = getMetadataReaderFactory().getMetadataReader(resource)`：获取元数据阅读器

      `if (isCandidateComponent(metadataReader))`：**当前类不匹配任何排除过滤器，并且匹配一个包含过滤器**，返回 true

        * includeFilters 由 `registerDefaultFilters()` 设置初始值，方法有 @Component，没有 @Service，因为 @Component 是 @Service 的元注解，Spring 在读取 @Service 时也读取了元注解，并将 @Service 作为 @Component 处理

          ```java
          this.includeFilters.add(new AnnotationTypeFilter(Component.class))
          ```

          ```java
          @Target({ElementType.TYPE})
          @Retention(RetentionPolicy.RUNTIME)
          @Documented
          @Component	// 拥有了 Component 功能
          public @interface Service {}
          ```

      `candidates.add(sbd)`：添加到返回结果的 list

参考文章：https://my.oschina.net/floor/blog/4325651

***

#### Autowired

打开 @Autowired 源码，注释上写 Please consult the javadoc for the AutowiredAnnotationBeanPostProcessor

AutowiredAnnotationBeanPostProcessor 间接实现 InstantiationAwareBeanPostProcessor，就具备了实例化前后（而不是初始化前后）管理对象的能力，实现了 BeanPostProcessor，具有初始化前后管理对象的能力，实现 BeanFactoryAware，具备随时拿到 BeanFactory 的能力，所以这个类**具备一切后置处理器的能力**

**在容器启动，为对象赋值的时候，遇到 @Autowired 注解，会用后置处理器机制，来创建属性的实例，然后再利用反射机制，将实例化好的属性，赋值给对象上，这就是 Autowired 的原理**

作用时机：

* Spring 在每个 Bean 实例化之后，调用 AutowiredAnnotationBeanPostProcessor 的 `postProcessMergedBeanDefinition()` 方法，查找该 Bean 是否有 @Autowired 注解，进行相关元数据的获取
* Spring 在每个 Bean 调用 `populateBean()` 进行属性注入的时候，即调用 `postProcessProperties()` 方法，查找该 Bean 属性是否有 @Autowired 注解，进行相关数据的填充

****

# Spring的八股文

### spring框架中的单例bean是线程安全的吗？

当多用户同时请求一个服务时，容器会给每一个请求分配一个线程，当多个线程并发执行请求对应的业务逻辑，如果该处理逻辑中有对单例状态的修改，则必须考虑线程同步问题

### 什么是AOP？

AOP称为面向切面编程，用于将那些与业务无关，但却对多个对象产生影响的公共行为和逻辑，抽取并封装为一个可重用的模块，这个模块被命名为"切面"，减少系统中的重复代码，降低了模块间的耦合度，同时提高系统的可维护性

**应用**

- 记录操作日志思路
- 实现spring事务

### Spring中的事务是如何实现的？

Spring支持编程式事务管理和声明式事务管理两种方式

- 编程式事务控制：需使用transactionTemplate来实现，对业务代码有侵入性，项目中很少使用
- 声明式事务管理：声明式事务管理建立在AOP之上，其本质是通过AOP功能，对方法前后进行拦截，将事务处理的功能编织到拦截方法中，也就是在目标方法开始之前加入一个事务，在执行目标方法之后根据执行情况提交或回滚事务

### Spring事务失效的场景

- 异常捕获处理
- 抛出检查异常

### Spring的常见注解有哪些？

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/155f95dc-3357-4143-b6b1-c1836c276661.png)

