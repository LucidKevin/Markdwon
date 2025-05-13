# MyBatis

## 基本介绍

ORM（Object Relational Mapping）： 对象关系映射，指的是持久化数据和实体对象的映射模式，解决面向对象与关系型数据库存在的互不匹配的现象

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-ORM%E4%BB%8B%E7%BB%8D.png)

**MyBatis**：

- MyBatis 是一个优秀的基于 Java 的持久层框架，它内部封装了 JDBC，使开发者只需关注 SQL 语句本身，而不需要花费精力去处理加载驱动、创建连接、创建 Statement 等过程。
- MyBatis 通过 XML 或注解的方式将要执行的各种 Statement 配置起来，并通过 Java 对象和 Statement 中 SQL 的动态参数进行映射生成最终执行的 SQL 语句。
- MyBatis 框架执行 SQL 并将结果映射为 Java 对象并返回。采用 ORM 思想解决了实体和数据库映射的问题，对 JDBC 进行了封装，屏蔽了 JDBC 底层 API 的调用细节，使我们不用操作 JDBC API，就可以完成对数据库的持久化操作。

MyBatis 官网地址：http://www.mybatis.org/mybatis-3/

参考视频：https://space.bilibili.com/37974444/







## 基本操作

### 相关API

Resources：加载资源的工具类

- `InputStream getResourceAsStream(String fileName)`：通过类加载器返回指定资源的字节流
  - 参数 fileName 是放在 src 的核心配置文件名：MyBatisConfig.xml

SqlSessionFactoryBuilder：构建器，用来获取 SqlSessionFactory 工厂对象

- `SqlSessionFactory build(InputStream is)`：通过指定资源的字节输入流获取 SqlSession 工厂对象

SqlSessionFactory：获取 SqlSession 构建者对象的工厂接口

- `SqlSession openSession()`：获取 SqlSession 构建者对象，并开启手动提交事务
- `SqlSession openSession(boolean)`：获取 SqlSession 构建者对象，参数为 true 开启自动提交事务

SqlSession：构建者对象接口，用于执行 SQL、管理事务、接口代理

- SqlSession **代表和数据库的一次会话**，用完必须关闭
- SqlSession 和 Connection 一样都是非线程安全，每次使用都应该去获取新的对象

注：**update 数据需要提交事务，或开启默认提交**

SqlSession 常用 API：

| 方法                                                  | 说明                           |
| ----------------------------------------------------- | ------------------------------ |
| List<E> selectList(String statement,Object parameter) | 执行查询语句，返回List集合     |
| T selectOne(String statement,Object parameter)        | 执行查询语句，返回一个结果对象 |
| int insert(String statement,Object parameter)         | 执行新增语句，返回影响行数     |
| int update(String statement,Object parameter)         | 执行删除语句，返回影响行数     |
| int delete(String statement,Object parameter)         | 执行修改语句，返回影响行数     |
| void commit()                                         | 提交事务                       |
| void rollback()                                       | 回滚事务                       |
| T getMapper(Class<T> cls)                             | 获取指定接口的代理实现类对象   |
| void close()                                          | 释放资源                       |





### 映射配置

映射配置文件包含了数据和对象之间的映射关系以及要执行的 SQL 语句，放在 src 目录下

命名：StudentMapper.xml

- 映射配置文件的文件头：

  ```
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  ```

- 根标签：

  - <mapper>：核心根标签
  - namespace：属性，名称空间

- 功能标签：

  - < select >：查询功能标签
  - <insert>：新增功能标签
  - <update>：修改功能标签
  - <delete>：删除功能标签
    - id：属性，唯一标识，配合名称空间使用
    - resultType：指定结果映射对象类型，和对应的方法的返回值类型（全限定名）保持一致，但是如果返回值是 List 则和其泛型保持一致
    - parameterType：指定参数映射对象类型，必须和对应的方法的参数类型（全限定名）保持一致
    - **statementType**：可选 STATEMENT，PREPARED 或 CALLABLE，默认值：PREPARED
      - STATEMENT：直接操作 SQL，使用 Statement 不进行预编译，获取数据：$
      - PREPARED：预处理参数，使用 PreparedStatement 进行预编译，获取数据：#
      - CALLABLE：执行存储过程，CallableStatement

- 参数获取方式：

  - SQL 获取参数：`#{属性名}`

    ```
    <mapper namespace="StudentMapper">
        <select id="selectById" resultType="student" parameterType="int">
        SELECT * FROM student WHERE id = #{id}
        </select>
    <mapper/>
    ```



强烈推荐官方文档：https://mybatis.org/mybatis-3/zh/sqlmap-xml.html





### 核心配置

核心配置文件包含了 MyBatis 最核心的设置和属性信息，如数据库的连接、事务、连接池信息等

命名：MyBatisConfig.xml

- 核心配置文件的文件头：

  ```xml-dtd
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
  ```

- 根标签：

  - <configuration>：核心根标签

- 引入连接配置文件：

  - <properties>： 引入数据库连接配置文件标签

    - resource：属性，指定配置文件名

    ```xml-dtd
    <properties resource="jdbc.properties"/>
    ```

- 调整设置

  - <settings>：可以改变 Mybatis 运行时行为

- 起别名：

  - <typeAliases>：为全类名起别名的父标签

    - <typeAlias>：为全类名起别名的子标签
      - type：指定全类名      
      - alias：指定别名
    - <package>：为指定包下所有类起别名的子标签，别名就是类名，首字母小写

    ```xml-dtd
    <!--起别名-->
    <typeAliases>
    	<typeAlias type="bean.Student" alias="student"/>
    	<package name="com.seazean.bean"/>
    		<!--二选一-->
    </typeAliase>
    ```

  - 自带别名：

    | 别名    | 数据类型          |
    | ------- | ----------------- |
    | string  | java.lang.String  |
    | long    | java.lang.Lang    |
    | int     | java.lang.Integer |
    | double  | java.lang.Double  |
    | boolean | java.lang.Boolean |
    | ....    | ......            |



- 配置环境，可以配置多个标签
  - <environments>：配置数据库环境标签，default 属性指定哪个 environment
  - <environment>：配置数据库环境子标签，id 属性是唯一标识，与 default 对应
  - <transactionManager>：事务管理标签，type 属性默认 JDBC 事务
  - <dataSoure>：数据源标签
    - type 属性：POOLED 使用连接池（MyBatis 内置），UNPOOLED 不使用连接池
  - <property>：数据库连接信息标签。
    - name 属性取值：driver，url，username，password
    - value 属性取值：与 name 对应
- 引入映射配置文件
  - <mappers>：引入映射配置文件标签
  - <mapper>：引入映射配置文件子标签
    - resource：属性指定映射配置文件的名称
    - url：引用网路路径或者磁盘路径下的 sql 映射文件
    - class：指定映射配置类
  - <package>：批量注册



参考官方文档：https://mybatis.org/mybatis-3/zh/configuration.html





### #{}和${}

**#{}：**占位符，传入的内容会作为字符串**加上引号**，以**预编译**的方式传入，将 sql 中的 #{} 替换为 ? 号，调用 PreparedStatement 的 set 方法来赋值，有效的防止 SQL 注入，提高系统安全性

**${}：**拼接符，传入的内容会**直接替换**拼接，不会加上引号，可能存在 sql 注入的安全隐患

- 能用 #{} 的地方就用 #{}，不用或少用 ${}
- 必须使用 ${} 的情况：
  - 表名作参数时，如：`SELECT * FROM ${tableName}`
  - order by 时，如：`SELECT * FROM t_user ORDER BY ${columnName}`
- sql 语句使用 #{}，properties 文件内容获取使用 ${} 

### 日志文件

在日常开发过程中，排查问题时需要输出 MyBatis 真正执行的 SQL 语句、参数、结果等信息，就可以借助 log4j 的功能来实现执行信息的输出。

- 在核心配置文件根标签内配置 log4j

  ```xml
  <!--配置LOG4J-->
  <settings>
  	<setting name="logImpl" value="log4j"/>
  </settings>
  ```

- 在 src 目录下创建 log4j.properties

  ```properties
  # Global logging configuration
  log4j.rootLogger=DEBUG, stdout
  # Console output...
  log4j.appender.stdout=org.apache.log4j.ConsoleAppender
  log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
  log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
  
  #输出到日志文件
    #log4j.appender.file=org.apache.log4j.FileAppender
    #log4j.appender.file.File=../logs/iask.log
    #log4j.appender.file.layout=org.apache.log4j.PatternLayout
    #log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss}  %l  %m%n
  ```

- pom.xml

  ```xml
  <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.7.21</version>
  </dependency>
  <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.21</version>
  </dependency>
  ```

  



### 代码实现

- 实体类

  ```java
  public class Student {
      private Integer id;
      private String name;
      private Integer age;
      .....
  }
  ```

- StudentMapper

  ```java
  public interface StudentMapper {
      //查询全部
      public abstract List<Student> selectAll();
  
      //根据id查询
      public abstract Student selectById(Integer id);
  
      //新增数据
      public abstract Integer insert(Student stu);
  
      //修改数据
      public abstract Integer update(Student stu);
  
      //删除数据
      public abstract Integer delete(Integer id);
  }
  ```

- config.properties

  ```properties
  driver=com.mysql.jdbc.Driver
  url=jdbc:mysql://192.168.2.184:3306/db1
  username=root
  password=123456
  ```

- MyBatisConfig.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
  
  <!--核心根标签-->
  <configuration>
      <!--引入数据库连接的配置文件-->
      <properties resource="jdbc.properties"/>
      
      <!--配置LOG4J-->
      <settings>
          <setting name="logImpl" value="log4j"/>
      </settings>
      
      <!--起别名-->
      <typeAliases>
          <typeAlias type="bean.Student" alias="student"/>
          <!--<package name="bean"/>-->
      </typeAliases>
  
      <!--配置数据库环境，可以多个环境，default指定哪个-->
      <environments default="mysql">
          <!--id属性唯一标识-->
          <environment id="mysql">
              <!--事务管理，type属性，默认JDBC事务-->
              <transactionManager type="JDBC"></transactionManager>
              <!--数据源信息   type属性连接池-->
              <dataSource type="POOLED">
                  <!--property获取数据库连接的配置信息-->
                  <property name="driver" value="${driver}"/>
                  <property name="url" value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
              </dataSource>
          </environment>
      </environments>
  
      <!--引入映射配置文件-->
      <mappers>
          <!--mapper引入指定的映射配置 resource属性执行的映射配置文件的名称-->
          <mapper resource="StudentMapper.xml"/>
      </mappers>
  </configuration>
  ```

- StudentMapper.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  
  <mapper namespace="StudentMapper">
      <select id="selectAll" resultType="student">
          SELECT * FROM student
      </select>
  
      <select id="selectById" resultType="student" parameterType="int">
          SELECT * FROM student WHERE id = #{id}
      </select>
  
      <insert id="insert" parameterType="student">
          INSERT INTO student VALUES (#{id},#{name},#{age})
      </insert>
  
      <update id="update" parameterType="student">
          UPDATE student SET name = #{name}, age = #{age} WHERE id = #{id}
      </update>
  
      <delete id="delete" parameterType="student">
          DELETE FROM student WHERE id = #{id}
      </delete>
  
  </mapper>
  ```

- 控制层测试代码：根据 id 查询

  ```java
  @Test
  public void selectById() throws Exception{
      //1.加载核心配置文件
      InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
      //2.获取SqlSession工厂对象
      SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
  
      //3.通过工厂对象获取SqlSession对象
      SqlSession sqlSession = ssf.openSession();
  
      //4.执行映射配置文件中的sql语句，并接收结果
      Student stu = sqlSession.selectOne("StudentMapper.selectById", 3);
  
      //5.处理结果
      System.out.println(stu);
  
      //6.释放资源
      sqlSession.close();
      is.close();
  }
  ```

- 控制层测试代码：新增功能

  ```java
  @Test
  public void insert() throws Exception{
      //1.加载核心配置文件
      //2.获取SqlSession工厂对象
      //3.通过工厂对象获取SqlSession对象
      SqlSession sqlSession = sqlSessionFactory.openSession(true);
  
      //4.执行映射配置文件中的sql语句，并接收结果
      Student stu = new Student(5, "周七", 27);
      int result = sqlSession.insert("StudentMapper.insert", stu);
  
      //5.提交事务
      //sqlSession.commit();
  
      //6.处理结果
      System.out.println(result);
  
      //7.释放资源
      sqlSession.close();
      is.close();
  }
  ```





### 批量操作

三种方式实现批量操作：

- <settings> 标签属性：这种方式属于**全局批量**

  ```xml
  <settings>
      <setting name="defaultExecutorType" value="BATCH"/>
  </settings>
  ```

  defaultExecutorType：配置默认的执行器

  - SIMPLE 就是普通的执行器（默认，每次执行都要重新设置参数）
  - REUSE 执行器会重用预处理语句（只预设置一次参数，多次执行）
  - BATCH 执行器不仅重用语句还会执行批量更新（只针对**修改操作**）

- SqlSession **会话内批量**操作：

  ```java
  public void testBatch() throws IOException{
      SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
  
      // 可以执行批量操作的sqlSession
      SqlSession openSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
      long start = System.currentTimeMillis();
      try{
          EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
          for (int i = 0; i < 10000; i++) {
              mapper.addEmp(new Employee(UUID.randomUUID().toString().substring(0, 5), "b", "1"));
          }
          openSession.commit();
          long end = System.currentTimeMillis();
          // 批量：（预编译sql一次==>设置参数===>10000次===>执行1次（类似管道））
          // 非批量：（预编译sql=设置参数=执行）==》10000   耗时更多
          System.out.println("执行时长：" + (end - start));
      }finally{
          openSession.close();
      }
  }
  ```

- Spring 配置文件方式（applicationContext.xml）：

  ```xml
  <!--配置一个可以进行批量执行的sqlSession  -->
  <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
      <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactoryBean"/>
      <constructor-arg name="executorType" value="BATCH"/>
  </bean>
  ```

  ```java
  @Autowired
  private SqlSession sqlSession;
  ```

  





## 代理开发

### 代理规则

分层思想：控制层（controller）、业务层（service）、持久层（dao）

调用流程：

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/%E5%88%86%E5%B1%82%E6%80%9D%E6%83%B3%E8%B0%83%E7%94%A8%E6%B5%81%E7%A8%8B.png)

传统方式实现 DAO 层，需要写接口和实现类。采用 Mybatis 的代理开发方式实现 DAO 层的开发，只需要编写 Mapper 接口（相当于 Dao 接口），由 Mybatis 框架根据接口定义创建接口的**动态代理对象**

接口开发方式：

1. 定义接口
2. 操作数据库，MyBatis 框架根据接口，通过动态代理的方式生成代理对象，负责数据库的操作

Mapper 接口开发需要遵循以下规范：

- Mapper.xml 文件中的 namespace 与 DAO 层 mapper 接口的全类名相同

- Mapper.xml 文件中的增删改查标签的id属性和 DAO 层 Mapper 接口方法名相同

- Mapper.xml 文件中的增删改查标签的 parameterType 属性和 DAO 层 Mapper 接口方法的参数相同

- Mapper.xml 文件中的增删改查标签的 resultType 属性和 DAO 层 Mapper 接口方法的返回值相同

  ![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/%E6%8E%A5%E5%8F%A3%E4%BB%A3%E7%90%86%E6%96%B9%E5%BC%8F%E5%AE%9E%E7%8E%B0DAO%E5%B1%82.png)

  



### 实现原理

通过动态代理开发模式，只编写一个接口不写实现类，通过 **getMapper()** 方法最终获取到 MapperProxy 代理对象，而这个代理对象是 MyBatis 使用了 JDK 的动态代理技术生成的

动态代理实现类对象在执行方法时最终调用了 **MapperMethod.execute()** 方法，这个方法中通过 switch case 语句根据操作类型来判断是新增、修改、删除、查询操作，最后一步回到了 MyBatis 最原生的 **SqlSession 方式来执行增删改查**

- 代码实现：

  ```java
  public Student selectById(Integer id) {
      Student stu = null;
      SqlSession sqlSession = null;
      InputStream is = null;
      try{
          //1.加载核心配置文件
          is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
          //2.获取SqlSession工厂对象
          SqlSessionFactory s = new SqlSessionFactoryBuilder().build(is);
  
          //3.通过工厂对象获取SqlSession对象
          sqlSession = s.openSession(true);
  
          //4.获取StudentMapper接口的实现类对象
          StudentMapper mapper = sqlSession.getMapper(StudentMapper.class); 
  
          //5.通过实现类对象调用方法，接收结果
          stu = mapper.selectById(id);
      } catch (Exception e) {
          e.getMessage();
      } finally {
          //6.释放资源
          if(sqlSession != null) {
              sqlSession.close();
          }
          if(is != null) {
              try {
                  is.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      //7.返回结果
      return stu;
  }
  ```

  







## 结果映射

### 相关标签

<resultType>：返回结果映射对象类型，和对应方法的返回值类型保持一致，但是如果返回值是 List 则和其泛型保持一致

<resultMap>：返回一条记录的 Map，key 是列名，value 是对应的值，用来配置**字段和对象属性**的映射关系标签，结果映射（和 resultType 二选一）

- id 属性：唯一标识
- type 属性：实体对象类型
- autoMapping 属性：结果自动映射

<resultMap> 内的核心配置文件标签：

- <id>：配置主键映射关系标签
- <result>：配置非主键映射关系标签
  - column 属性：表中字段名称
  - property 属性： 实体对象变量名称
- <association>：配置被包含**单个对象**的映射关系标签，嵌套封装结果集（多对一、一对一）
  - property 属性：被包含对象的变量名，要进行映射的属性名
  - javaType 属性：被包含对象的数据类型，要进行映射的属性的类型（Java 中的 Bean 类）
  - select 属性：加载复杂类型属性的映射语句的 ID，会从 column 属性指定的列中检索数据，作为参数传递给目标 select 语句
- <collection>：配置被包含**集合对象**的映射关系标签，嵌套封装结果集（一对多、多对多）
  - property 属性：被包含集合对象的变量名
  - ofType 属性：集合中保存的对象数据类型 
- <discriminator>：鉴别器，用来判断某列的值，根据得到某列的不同值做出不同自定义的封装行为

自定义封装规则可以将数据库中比较复杂的数据类型映射为 JavaBean 中的属性





### 嵌套查询

子查询：

```java
public class Blog {
    private int id;
    private String msg;
    private Author author;
    // set + get
}
<resultMap id="blogResult" type="Blog" autoMapping = "true">
    <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
    SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectAuthor" resultType="Author">
    SELECT * FROM AUTHOR WHERE ID = #{id}
</select>
```

循环引用：通过缓存解决

```xml
<resultMap id="blogResult" type="Blog" autoMapping = "true">
    <id column="id" property="id"/>
    <collection property="comment" ofType="Comment">
        <association property="blog" javaType="Blog" resultMap="blogResult"/><!--y-->
    </collection>
</resultMap
```







### 多表查询

#### 一对一

一对一实现：

- 数据准备

  ```mysql
  CREATE TABLE person(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	name VARCHAR(20),
  	age INT
  );
  INSERT INTO person VALUES (NULL,'张三',23),(NULL,'李四',24),(NULL,'王五',25);
  
  CREATE TABLE card(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	number VARCHAR(30),
  	pid INT,
  	CONSTRAINT cp_fk FOREIGN KEY (pid) REFERENCES person(id)
  );
  INSERT INTO card VALUES (NULL,'12345',1),(NULL,'23456',2),(NULL,'34567',3);
  ```

- bean 类

  ```java
  public class Card {
      private Integer id;     //主键id
      private String number;  //身份证号
      private Person p;       //所属人的对象
      ......
  }
  
  public class Person {
      private Integer id;     //主键id
      private String name;    //人的姓名
      private Integer age;    //人的年龄
  }
  ```

- 配置文件 OneToOneMapper.xml，MyBatisConfig.xml 需要引入（可以把 bean 包下起别名）

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  
  <mapper namespace="OneToOneMapper">
  
      <!--配置字段和实体对象属性的映射关系-->
      <resultMap id="oneToOne" type="card">
         	<!--column 表中字段名称，property 实体对象变量名称-->
          <id column="cid" property="id" />
          <result column="number" property="number" />
          <!--
              association：配置被包含对象的映射关系
              property：被包含对象的变量名
              javaType：被包含对象的数据类型
          -->
          <association property="p" javaType="bean.Person">
              <id column="pid" property="id" />
              <result column="name" property="name" />
              <result column="age" property="age" />
          </association>
      </resultMap>
  
      <select id="selectAll" resultMap="oneToOne"> <!--SQL-->
          SELECT c.id cid,number,pid,NAME,age FROM card c,person p WHERE c.pid=p.id
      </select>
  </mapper>
  ```

- 核心配置文件 MyBatisConfig.xml

  ```xml
  <!-- mappers引入映射配置文件 -->
  <mappers>
      <mapper resource="one_to_one/OneToOneMapper.xml"/>
      <mapper resource="one_to_many/OneToManyMapper.xml"/>
      <mapper resource="many_to_many/ManyToManyMapper.xml"/>
  </mappers>
  ```

- 测试类

  ```java
  public class Test01 {
      @Test
      public void selectAll() throws Exception{
          //1.加载核心配置文件
          InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
          //2.获取SqlSession工厂对象
          SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
  
          //3.通过工厂对象获取SqlSession对象
          SqlSession sqlSession = ssf.openSession(true);
  
          //4.获取OneToOneMapper接口的实现类对象
          OneToOneMapper mapper = sqlSession.getMapper(OneToOneMapper.class);
  
          //5.调用实现类的方法，接收结果
          List<Card> list = mapper.selectAll();
  
          //6.处理结果
          for (Card c : list) {
              System.out.println(c);
          }
  
          //7.释放资源
          sqlSession.close();
          is.close();
      }
  }
  ```

  

  



#### 一对多

一对多实现：

- 数据准备

  ```mysql
  CREATE TABLE classes(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	name VARCHAR(20)
  );
  INSERT INTO classes VALUES (NULL,'程序一班'),(NULL,'程序二班')
  
  CREATE TABLE student(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	name VARCHAR(30),
  	age INT,
  	cid INT,
  	CONSTRAINT cs_fk FOREIGN KEY (cid) REFERENCES classes(id)
  );
  INSERT INTO student VALUES (NULL,'张三',23,1),(NULL,'李四',24,1),(NULL,'王五',25,2);
  ```

- bean 类

  ```
  public class Classes {
      private Integer id;     //主键id
      private String name;    //班级名称
      private List<Student> students; //班级中所有学生对象
      ........
  }
  public class Student {
      private Integer id;     //主键id
      private String name;    //学生姓名
      private Integer age;    //学生年龄
  }
  ```

- 映射配置文件

  ```
  <mapper namespace="OneToManyMapper">
      <resultMap id="oneToMany" type="bean.Classes">
          <id column="cid" property="id"/>
          <result column="cname" property="name"/>
  
          <!--collection：配置被包含的集合对象映射关系-->
          <collection property="students" ofType="bean.Student">
              <id column="sid" property="id"/>
              <result column="sname" property="name"/>
              <result column="sage" property="age"/>
          </collection>
      </resultMap>
      <select id="selectAll" resultMap="oneToMany"> <!--SQL-->
          SELECT c.id cid,c.name cname,s.id sid,s.name sname,s.age sage FROM classes c,student s WHERE c.id=s.cid
      </select>
  </mapper>
  ```

- 代码实现片段

  ```
  //4.获取OneToManyMapper接口的实现类对象
  OneToManyMapper mapper = sqlSession.getMapper(OneToManyMapper.class);
  
  //5.调用实现类的方法，接收结果
  List<Classes> classes = mapper.selectAll();
  
  //6.处理结果
  for (Classes cls : classes) {
      System.out.println(cls.getId() + "," + cls.getName());
      List<Student> students = cls.getStudents();
      for (Student student : students) {
          System.out.println("\t" + student);
      }
  }
  ```







#### 多对多

学生课程例子，中间表不需要 bean 实体类

- 数据准备

  ```mysql
  CREATE TABLE course(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	name VARCHAR(20)
  );
  INSERT INTO course VALUES (NULL,'语文'),(NULL,'数学');
  
  CREATE TABLE stu_cr(
  	id INT PRIMARY KEY AUTO_INCREMENT,
  	sid INT,
  	cid INT,
  	CONSTRAINT sc_fk1 FOREIGN KEY (sid) REFERENCES student(id),
  	CONSTRAINT sc_fk2 FOREIGN KEY (cid) REFERENCES course(id)
  );
  INSERT INTO stu_cr VALUES (NULL,1,1),(NULL,1,2),(NULL,2,1),(NULL,2,2);
  ```

- bean类

  ```java
  public class Student {
      private Integer id;     //主键id
      private String name;    //学生姓名
      private Integer age;    //学生年龄
      private List<Course> courses;   // 学生所选择的课程集合
  }
  public class Course {
      private Integer id;     //主键id
      private String name;    //课程名称
  }
  ```

- 配置文件

  ```xml
  <mapper namespace="ManyToManyMapper">
      <resultMap id="manyToMany" type="Bean.Student">
          <id column="sid" property="id"/>
          <result column="sname" property="name"/>
          <result column="sage" property="age"/>
  
          <collection property="courses" ofType="Bean.Course">
              <id column="cid" property="id"/>
              <result column="cname" property="name"/>
          </collection>
      </resultMap>
      <select id="selectAll" resultMap="manyToMany"> <!--SQL-->
          SELECT sc.sid,s.name sname,s.age sage,sc.cid,c.name cname FROM student s,course c,stu_cr sc WHERE sc.sid=s.id AND sc.cid=c.id
      </select>
  </mapper>
  ```





### 鉴别器

需求：如果查询结果是女性，则把部门信息查询出来，否则不查询 ；如果是男性，把 last_name 这一列的值赋值

```xml
<!-- 
    column：指定要判断的列名 
    javaType：列值对应的java类型
   -->
<discriminator javaType="string" column="gender">
    <!-- 女生 -->
    <!-- resultType不可缺少，也可以使用resutlMap -->
    <case value="0" resultType="com.bean.Employee">
        <association property="dept"
                     select="com.dao.DepartmentMapper.getDeptById"
                     column="d_id">
        </association>
    </case>
    <!-- 男生 -->
    <case value="1" resultType="com.bean.Employee">
        <id column="id" property="id"/>
        <result column="last_name" property="lastName"/>
        <result column="gender" property="gender"/>
    </case>
</discriminator>
```







### 延迟加载

#### 两种加载

立即加载：只要调用方法，马上发起查询

延迟加载：在需要用到数据时才进行加载，不需要用到数据时就不加载数据，延迟加载也称懒加载

优点： 先从单表查询，需要时再从关联表去关联查询，提高数据库性能，因为查询单表要比关联查询多张表速度要快，节省资源

坏处：只有当需要用到数据时，才会进行数据库查询，这样在大批量数据查询时，查询工作也要消耗时间，所以可能造成用户等待时间变长，造成用户体验下降

核心配置文件：

| 标签名                | 描述                                                         | 默认值 |
| --------------------- | ------------------------------------------------------------ | ------ |
| lazyLoadingEnabled    | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载，特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | false  |
| aggressiveLazyLoading | 开启时，任一方法的调用都会加载该对象的所有延迟加载属性。否则每个延迟加载属性会按需加载（参考 lazyLoadTriggerMethods） | false  |

```
<settings> 
  <setting name="lazyLoadingEnabled" value="true"/> 
    <setting name="aggressiveLazyLoading" value="false"/> 
</settings>
```





#### assocation

分布查询：先按照身份 id 查询所属人的 id、然后根据所属人的 id 去查询人的全部信息，这就是分步查询

- 映射配置文件 OneToOneMapper.xml 

  一对一映射：

  - column 属性表示给要调用的其它的 select 标签传入的参数
  - select 属性表示调用其它的 select 标签
  - fetchType="lazy" 表示延迟加载（局部配置，只有配置了这个的地方才会延迟加载）

  ```xml
  <mapper namespace="OneToOneMapper">
      <!--配置字段和实体对象属性的映射关系-->
      <resultMap id="oneToOne" type="card">
          <id column="id" property="id" />
          <result column="number" property="number" />
          <association property="p" javaType="bean.Person"
                       column="pid" 
                       select="one_to_one.PersonMapper.findPersonByid"
                       fetchType="lazy">
              		<!--需要配置新的映射文件-->
          </association>
      </resultMap>
  
      <select id="selectAll" resultMap="oneToOne"> 
          SELECT * FROM card <!--查询全部，负责根据条件直接全部加载-->
      </select>
  </mapper>
  ```

- PersonMapper.xml

  ```xml
  <mapper namespace="one_to_one.PersonMapper">
      <select id="findPersonByid" parameterType="int" resultType="person">
          SELECT * FROM person WHERE id=#{pid}
      </select>
  </mapper>
  ```

- PersonMapper.java

  ```java
  public interface PersonMapper {
      User findPersonByid(int id);
  }
  ```

- 测试文件

  ```
  public class Test01 {
      @Test
      public void selectAll() throws Exception{
          InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
          SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
          SqlSession sqlSession = ssf.openSession(true);
          OneToOneMapper mapper = sqlSession.getMapper(OneToOneMapper.class);
          // 调用实现类的方法，接收结果
          List<Card> list = mapper.selectAll();
          
          // 不能遍历，遍历就是相当于使用了该数据，需要加载，不遍历就是没有使用。
          
          // 释放资源
          sqlSession.close();
          is.close();
      }
  }
  ```

  





#### collection

同样在一对多关系配置的 <collection> 结点中配置延迟加载策略，<collection> 结点中也有 select 属性和 column 属性

- 映射配置文件 OneToManyMapper.xml

  一对多映射：

  - column 是用于指定使用哪个字段的值作为条件查询
  - select 是用于指定查询账户的唯一标识（账户的 dao 全限定类名加上方法名称） 

  ```
  <mapper namespace="OneToManyMapper">
      <resultMap id="oneToMany" type="bean.Classes">
          <id column="id" property="id"/>
          <result column="name" property="name"/>
  
          <!--collection：配置被包含的集合对象映射关系-->
          <collection property="students" ofType="bean.Student"
                      column="id" 
                      select="one_to_one.StudentMapper.findStudentByCid">
          </collection>
      </resultMap>
      <select id="selectAll" resultMap="oneToMany">
        SELECT * FROM classes
      </select>
  </mapper>
  ```

- StudentMapper.xml

  ```xml
  <mapper namespace="one_to_one.StudentMapper">
      <select id="findPersonByCid" parameterType="int" resultType="student">
          SELECT * FROM person WHERE cid=#{id}
      </select>
  </mapper>
  ```









## 注解开发

### 单表操作

注解可以简化开发操作，省略映射配置文件的编写

常用注解：

- @Select(“查询的 SQL 语句”)：执行查询操作注解
- @Insert(“插入的 SQL 语句”)：执行新增操作注解
- @Update(“修改的 SQL 语句”)：执行修改操作注解
- @Delete(“删除的 SQL 语句”)：执行删除操作注解

参数注解：

- @Param：当 SQL 语句需要**多个（大于1）参数**时，用来指定参数的对应规则

核心配置文件配置映射关系：

```xml
<mappers>
	<package name="使用了注解的Mapper接口所在包"/>
</mappers>
<!--或者-->
<mappers>
 	<mapper class="包名.Mapper名"></mapper>
</mappers>
```

基本增删改查：

- 创建 Mapper 接口

  ```java
  package mapper;
  public interface StudentMapper {
      //查询全部
      @Select("SELECT * FROM student")
      public abstract List<Student> selectAll();
  
      //新增数据
      @Insert("INSERT INTO student VALUES (#{id},#{name},#{age})")
      public abstract Integer insert(Student student);
  
      //修改操作
      @Update("UPDATE student SET name=#{name},age=#{age} WHERE id=#{id}")
      public abstract Integer update(Student student);
  
      //删除操作
      @Delete("DELETE FROM student WHERE id=#{id}")
      public abstract Integer delete(Integer id);
  
  }
  ```

- 修改 MyBatis 的核心配置文件

  ```xml
  <mappers>
  	<package name="mapper"/>
  </mappers>
  ```

- bean类

  ```java
  public class Student {
      private Integer id;
      private String name;
      private Integer age;
  }
  ```

- 测试类

  ```java
  @Test
  public void selectAll() throws Exception{
      //1.加载核心配置文件
      InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
      //2.获取SqlSession工厂对象
      SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
  
      //3.通过工厂对象获取SqlSession对象
      SqlSession sqlSession = ssf.openSession(true);
  
      //4.获取StudentMapper接口的实现类对象
      StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
  
      //5.调用实现类对象中的方法，接收结果
      List<Student> list = mapper.selectAll();
  
      //6.处理结果
      for (Student student : list) {
          System.out.println(student);
      }
      
      //7.释放资源
      sqlSession.close();
      is.close();
  }
  ```





### 多表操作

#### 相关注解

实现复杂关系映射之前我们可以在映射文件中通过配置 <resultMap> 来实现，使用注解开发后，可以使用 @Results 注解，@Result 注解，@One 注解，@Many 注解组合完成复杂关系的配置

| 注解          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| @Results      | 代替 <resultMap> 标签，注解中使用单个 @Result 注解或者 @Result 集合<br/>使用格式：@Results({ @Result(), @Result() })或@Results({ @Result() }) |
| @Result       | 代替< id> 和 <result> 标签，@Result 中属性介绍：<br />column：数据库的列名      property：封装类的变量名<br />one：需要使用 @One 注解（@Result(one = @One)）<br />Many：需要使用 @Many 注解（@Result(many= @Many)） |
| @One(一对一)  | 代替 <association> 标签，多表查询的关键，用来指定子查询返回单一对象<br/>select：指定调用 Mapper 接口中的某个方法<br />使用格式：@Result(column="", property="", one=@One(select="")) |
| @Many(多对一) | 代替 <collection> 标签，多表查询的关键，用来指定子查询返回对象集合<br />select：指定调用 Mapper 接口中的某个方法<br />使用格式：@Result(column="", property="", many=@Many(select="")) |





#### 一对一

身份证对人

- PersonMapper 接口

  ```java
  public interface PersonMapper {
      //根据id查询
      @Select("SELECT * FROM person WHERE id=#{id}")
      public abstract Person selectById(Integer id);
  }
  ```

- CardMapper接口

  ```java
  public interface CardMapper {
      //查询全部
      @Select("SELECT * FROM card")
      @Results({
              @Result(column = "id",property = "id"),
              @Result(column = "number",property = "number"),
              @Result(
                      property = "p",             // 被包含对象的变量名
                      javaType = Person.class,    // 被包含对象的实际数据类型
                      column = "pid",  // 根据查询出的card表中的pid字段来查询person表
                       /* 
                       	one、@One 一对一固定写法
                          select属性：指定调用哪个接口中的哪个方法
                       */
                      one = @One(select = "one_to_one.PersonMapper.selectById")
              )
      })
      public abstract List<Card> selectAll();
  }
  ```

- 测试类（详细代码参考单表操作）

  ```java
  //1.加载核心配置文件
  //2.获取SqlSession工厂对象
  //3.通过工厂对象获取SqlSession对象
  
  //4.获取StudentMapper接口的实现类对象
  CardMapper mapper = sqlSession.getMapper(CardMapper.class);
  //5.调用实现类对象中的方法，接收结果
  List<Card> list = mapper.selectAll();
  ```

  



#### 一对多

班级和学生

- StudentMapper接口

  ```java
  public interface StudentMapper {
      //根据cid查询student表  cid是外键约束列
      @Select("SELECT * FROM student WHERE cid=#{cid}")
      public abstract List<Student> selectByCid(Integer cid);
  }
  ```

- ClassesMapper接口

  ```java
  public interface ClassesMapper {
      //查询全部
      @Select("SELECT * FROM classes")
      @Results({
              @Result(column = "id", property = "id"),
              @Result(column = "name", property = "name"),
              @Result(
                      property = "students",  //被包含对象的变量名
                      javaType = List.class,  //被包含对象的实际数据类型
                      column = "id",          //根据id字段查询student表
                      many = @Many(select = "one_to_many.StudentMapper.selectByCid")
              )
      })
      public abstract List<Classes> selectAll();
  }
  ```

- 测试类

  ```java 
  //4.获取StudentMapper接口的实现类对象
  ClassesMapper mapper = sqlSession.getMapper(ClassesMapper.class);
  //5.调用实现类对象中的方法，接收结果
  List<Classes> classes = mapper.selectAll();
  ```

  



#### 多对多

学生和课程

- SQL 查询语句

  ```mysql
  SELECT DISTINCT s.id,s.name,s.age FROM student s,stu_cr sc WHERE sc.sid=s.id
  SELECT c.id,c.name FROM stu_cr sc,course c WHERE sc.cid=c.id AND sc.sid=#{id}
  ```

- CourseMapper 接口

  ```java
  public interface CourseMapper {
      //根据学生id查询所选课程
      @Select("SELECT c.id,c.name FROM stu_cr sc,course c WHERE sc.cid=c.id AND sc.sid=#{id}")
      public abstract List<Course> selectBySid(Integer id);
  }
  ```

- StudentMapper 接口

  ```java
  public interface StudentMapper {
      //查询全部
      @Select("SELECT DISTINCT s.id,s.name,s.age FROM student s,stu_cr sc WHERE sc.sid=s.id")
      @Results({
              @Result(column = "id",property = "id"),
              @Result(column = "name",property = "name"),
              @Result(column = "age",property = "age"),
              @Result(
                      property = "courses",    //被包含对象的变量名
                      javaType = List.class,  //被包含对象的实际数据类型
                      column = "id", //根据查询出的student表中的id字段查询中间表和课程表
                      many = @Many(select = "many_to_many.CourseMapper.selectBySid")
              )
      })
      public abstract List<Student> selectAll();
  }
  ```

- 测试类

  ```java
  //4.获取StudentMapper接口的实现类对象
  StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
  //5.调用实现类对象中的方法，接收结果
  List<Student> students = mapper.selectAll();
  ```









## 缓存机制

### 缓存概述

缓存：缓存就是一块内存空间，保存临时数据

作用：将数据源（数据库或者文件）中的数据读取出来存放到缓存中，再次获取时直接从缓存中获取，可以减少和数据库交互的次数，提升程序的性能

缓存适用：

- 适用于缓存的：经常查询但不经常修改的，数据的正确与否对最终结果影响不大的
- 不适用缓存的：经常改变的数据 , 敏感数据（例如：股市的牌价，银行的汇率，银行卡里面的钱）等等

缓存类别：

- 一级缓存：SqlSession 级别的缓存，又叫本地会话缓存，自带的（不需要配置），一级缓存的生命周期与 SqlSession 一致。在操作数据库时需要构造 SqlSession 对象，**在对象中有一个数据结构（HashMap）用于存储缓存数据**，不同的 SqlSession 之间的缓存数据区域是互相不影响的
- 二级缓存：mapper（namespace）级别的缓存，二级缓存的使用，需要手动开启（需要配置）。多个 SqlSession 去操作同一个 Mapper 的 SQL 可以共用二级缓存，二级缓存是跨 SqlSession 的

开启缓存：配置核心配置文件中 <settings> 标签

- cacheEnabled：true 表示全局性地开启所有映射器配置文件中已配置的任何缓存，默认 true

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E7%BC%93%E5%AD%98%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86.png)



参考文章：https://www.cnblogs.com/ysocean/p/7342498.html





### 一级缓存

一级缓存是 SqlSession 级别的缓存

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E4%B8%80%E7%BA%A7%E7%BC%93%E5%AD%98.png)

工作流程：第一次发起查询用户 id 为 1 的用户信息，先去找缓存中是否有 id 为 1 的用户信息，如果没有，从数据库查询用户信息，得到用户信息，将用户信息存储到一级缓存中；第二次发起查询用户 id 为 1 的用户信息，先去找缓存中是否有 id 为 1 的用户信息，缓存中有，直接从缓存中获取用户信息。

一级缓存的失效：

- SqlSession 不同
- SqlSession 相同，查询条件不同时（还未缓存该数据）
- SqlSession 相同，手动清除了一级缓存，调用 `sqlSession.clearCache()`
- SqlSession 相同，执行 commit 操作或者执行插入、更新、删除，清空 SqlSession 中的一级缓存，这样做的目的为了让缓存中存储的是最新的信息，**避免脏读**

Spring 整合 MyBatis 后，一级缓存作用：

- 未开启事务的情况，每次查询 Spring 都会创建新的 SqlSession，因此一级缓存失效
- 开启事务的情况，Spring 使用 ThreadLocal 获取当前资源绑定同一个 SqlSession，因此此时一级缓存是有效的

测试一级缓存存在

```java
public void testFirstLevelCache(){
    //1. 获取sqlSession对象
    SqlSession sqlSession = SqlSessionFactoryUtils.openSession();
    //2. 通过sqlSession对象获取UserDao接口的代理对象
    UserDao userDao1 = sqlSession.getMapper(UserDao.class);
    //3. 调用UserDao接口的代理对象的findById方法获取信息
	User user1 = userDao1.findById(1);
	System.out.println(user1);
    
    //sqlSession.clearCache() 清空缓存
    
   	UserDao userDao2 = sqlSession.getMapper(UserDao.class);
    User user = userDao.findById(1);
    System.out.println(user2);
    
    //4.测试两次结果是否一样
    System.out.println(user1 == user2);//true
    
    //5. 提交事务关闭资源
    SqlSessionFactoryUtils.commitAndClose(sqlSession);
}
```







### 二级缓存

#### 基本介绍

二级缓存是 mapper 的缓存，只要是同一个命名空间（namespace）的 SqlSession 就共享二级缓存的内容，并且可以操作二级缓存

作用：作用范围是整个应用，可以跨线程使用，适合缓存一些修改较少的数据

工作流程：一个会话查询数据，这个数据就会被放在当前会话的一级缓存中，如果**会话关闭或提交**一级缓存中的数据会保存到二级缓存

二级缓存的基本使用：

1. 在 MyBatisConfig.xml 文件开启二级缓存，**cacheEnabled 默认值为 true**，所以这一步可以省略不配置

   ```xml
   <!--配置开启二级缓存-->
   <settings>
       <setting name="cacheEnabled" value="true"/>
   </settings>
   ```

2. 配置 Mapper 映射文件

   `<cache>` 标签表示当前这个 mapper 映射将使用二级缓存，区分的标准就看 mapper 的 namespace 值

   ```xml
   <mapper namespace="dao.UserDao">
       <!--开启user支持二级缓存-->
      	<cache eviction="FIFO" flushInterval="6000" readOnly="" size="1024"/>
   	<cache></cache> <!--则表示所有属性使用默认值-->
   </mapper>
   ```

   eviction（清除策略）：

   - `LRU` – 最近最少使用：移除最长时间不被使用的对象，默认
   - `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们
   - `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象
   - `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象

   flushInterval（刷新间隔）：可以设置为任意的正整数， 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新

   size（引用数目）：缓存存放多少元素，默认值是 1024

   readOnly（只读）：可以被设置为 true 或 false

   - 只读的缓存会给所有调用者返回缓存对象的相同实例，因此这些对象不能被修改，促进了性能提升
   - 可读写的缓存会（通过序列化）返回缓存对象的拷贝， 速度上会慢一些，但是更安全，因此默认值是 false

   type：指定自定义缓存的全类名，实现 Cache 接口即可

3. 要进行二级缓存的类必须实现 java.io.Serializable 接口，可以使用序列化方式来保存对象。

   ```java
   public class User implements Serializable{}
   ```







#### 相关属性

1. select 标签的 useCache 属性

   映射文件中的 `<select>` 标签中设置 `useCache="true"` 代表当前 statement 要使用二级缓存（默认）

   注意：如果每次查询都需要最新的数据 sql，要设置成 useCache=false，禁用二级缓存

   ```xml
   <select id="findAll" resultType="user" useCache="true">
       select * from user
   </select>
   ```

2. 每个增删改标签都有 flushCache 属性，默认为 true，代表在**执行增删改之后就会清除一、二级缓存**，保证缓存的一致性；而查询标签默认值为 false，所以查询不会清空缓存

3. localCacheScope：本地缓存作用域，<settings> 中的配置项，默认值为 SESSION，当前会话的所有数据保存在会话缓存中，设置为 STATEMENT 禁用一级缓存





#### 源码解析

事务提交二级缓存才生效：DefaultSqlSession 调用 commit() 时会回调 `executor.commit()`

- CachingExecutor#query()：执行查询方法，查询出的数据会先放入 entriesToAddOnCommit 集合暂存

  ```java
  // 从二缓存中获取数据，获取不到去一级缓存获取
  List<E> list = (List<E>) tcm.getObject(cache, key);
  if (list == null) {
      // 回调 BaseExecutor#query
      list = delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
      // 将数据放入 entriesToAddOnCommit 集合暂存，此时还没放入二级缓存
      tcm.putObject(cache, key, list);
  }
  ```

- commit()：事务提交，**清空一级缓存，放入二级缓存**，二级缓存使用 TransactionalCacheManager（tcm）管理

  ```java
  public void commit(boolean required) throws SQLException {
      // 首先调用 BaseExecutor#commit 方法，【清空一级缓存】
      delegate.commit(required);
      tcm.commit();
  }
  ```

- TransactionalCacheManager#commit：查询出的数据放入二级缓存

  ```java
  public void commit() {
      // 获取所有的缓存事务，挨着进行提交
      for (TransactionalCache txCache : transactionalCaches.values()) {
          txCache.commit();
      }
  }
  ```

  ```java
  public void commit() {
      if (clearOnCommit) {
          delegate.clear();
      }
      // 将 entriesToAddOnCommit 中的数据放入二级缓存
      flushPendingEntries();
      // 清空相关集合
      reset();
  }
  ```

  ```java
  private void flushPendingEntries() {
      for (Map.Entry<Object, Object> entry : entriesToAddOnCommit.entrySet()) {
          // 将数据放入二级缓存
          delegate.putObject(entry.getKey(), entry.getValue());
      }
  }
  ```

增删改操作会清空缓存：

- update()：CachingExecutor 的更新操作

  ```java
  public int update(MappedStatement ms, Object parameterObject) throws SQLException {
      flushCacheIfRequired(ms);
      // 回调 BaseExecutor#update 方法，也会清空一级缓存
      return delegate.update(ms, parameterObject);
  }
  ```

- flushCacheIfRequired()：判断是否需要清空二级缓存

  ```java
  private void flushCacheIfRequired(MappedStatement ms) {
      Cache cache = ms.getCache();
      // 判断二级缓存是否存在，然后判断标签的 flushCache 的值，增删改操作的 flushCache 属性默认为 true
      if (cache != null && ms.isFlushCacheRequired()) {
          // 清空二级缓存
          tcm.clear(cache);
      }
  }
  ```





### 自定义

自定义缓存

```xml
<cache type="com.domain.something.MyCustomCache"/>
```

type 属性指定的类必须实现 org.apache.ibatis.cache.Cache 接口，且提供一个接受 String 参数作为 id 的构造器

```java
public interface Cache {
  String getId();
  int getSize();
  void putObject(Object key, Object value);
  Object getObject(Object key);
  boolean hasKey(Object key);
  Object removeObject(Object key);
  void clear();
}
```

缓存的配置，只需要在缓存实现中添加公有的 JavaBean 属性，然后通过 cache 元素传递属性值，例如在缓存实现上调用一个名为 `setCacheFile(String file)` 的方法：

```xml
<cache type="com.domain.something.MyCustomCache">
  <property name="cacheFile" value="/tmp/my-custom-cache.tmp"/>
</cache>
```

- 可以使用所有简单类型作为 JavaBean 属性的类型，MyBatis 会进行转换。
- 可以使用占位符（如 `${cache.file}`），以便替换成在配置文件属性中定义的值

MyBatis 支持在所有属性设置完毕之后，调用一个初始化方法， 如果想要使用这个特性，可以在自定义缓存类里实现 `org.apache.ibatis.builder.InitializingObject` 接口

```java
public interface InitializingObject {
  void initialize() throws Exception;
}
```

注意：对缓存的配置（如清除策略、可读或可读写等），不能应用于自定义缓存

对某一命名空间的语句，只会使用该命名空间的缓存进行缓存或刷新，在多个命名空间中共享相同的缓存配置和实例，可以使用 cache-ref 元素来引用另一个缓存

```xml
<cache-ref namespace="com.someone.application.data.SomeMapper"/>
```









## 构造语句

### 动态 SQL

#### 基本介绍

动态 SQL 是 MyBatis 强大特性之一，逻辑复杂时，MyBatis 映射配置文件中，SQL 是动态变化的，所以引入动态 SQL 简化拼装 SQL 的操作

DynamicSQL 包含的标签：

- if
- where
- set
- choose (when、otherwise)
- trim
- foreach

各个标签都可以进行灵活嵌套和组合

OGNL：Object Graphic Navigation Language（对象图导航语言），用于对数据进行访问

参考文章：https://www.cnblogs.com/ysocean/p/7289529.html

#### where

<where>：条件标签，有动态条件则使用该标签代替 WHERE 关键字，封装查询条件

作用：如果标签返回的内容是以 AND 或 OR 开头的，标签内会剔除掉

表结构：

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E5%8A%A8%E6%80%81sql%E7%94%A8%E6%88%B7%E8%A1%A8.png)

if

基本格式：

```xml
<if test=“条件判断”>
	查询条件拼接
</if>
```

我们根据实体类的不同取值，使用不同的 SQL 语句来进行查询。比如在 id 如果不为空时可以根据 id 查询，如果username 不同空时还要加入用户名作为条件，这种情况在我们的多条件组合查询中经常会碰到。

- UserMapper.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  
  <mapper namespace="mapper.UserMapper">
      <select id="selectCondition" resultType="user" parameterType="user">
          SELECT * FROM user
          <where>
              <if test="id != null ">
                  id = #{id}
              </if>
              <if test="username != null ">
                  AND username = #{username}
              </if>
              <if test="sex != null ">
                  AND sex = #{sex}
              </if>
          </where>
      </select>	
  
  </mapper>
  ```

- MyBatisConfig.xml，引入映射配置文件

  ```xml
  <mappers>
      <!--mapper引入指定的映射配置 resource属性执行的映射配置文件的名称-->
      <mapper resource="UserMapper.xml"/>
  </mappers>
  ```

- DAO 层 Mapper 接口

  ```java
  public interface UserMapper {
      //多条件查询
      public abstract List<User> selectCondition(Student stu);
  }
  ```

- 实现类

  ```java
  public class DynamicTest {
      @Test
      public void selectCondition() throws Exception{
          //1.加载核心配置文件
          InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
          //2.获取SqlSession工厂对象
          SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(is);
  
          //3.通过工厂对象获取SqlSession对象
          SqlSession sqlSession = ssf.openSession(true);
  
          //4.获取StudentMapper接口的实现类对象
          UserMapper mapper = sqlSession.getMapper(UserMapper.class);
  
          User user = new User();
          user.setId(2);
          user.setUsername("李四");
          //user.setSex(男); AND 后会自动剔除
  
          //5.调用实现类的方法，接收结果
          List<Student> list = mapper.selectCondition(user);
  
          //6.处理结果
          for (User user : list) {
              System.out.println(user);
          }
          
          //7.释放资源
          sqlSession.close();
          is.close();
      }
  }
  ```


#### set

<set>：进行更新操作的时候，含有 set 关键词，使用该标签

```xml
<!-- 根据 id 更新 user 表的数据 -->
<update id="updateUserById" parameterType="com.ys.po.User">
    UPDATE user u
        <set>
            <if test="username != null and username != ''">
                u.username = #{username},
            </if>
            <if test="sex != null and sex != ''">
                u.sex = #{sex}
            </if>
        </set>
     WHERE id=#{id}
</update>
```

- 如果第一个条件 username 为空，那么 sql 语句为：update user u set u.sex=? where id=?
- 如果第一个条件不为空，那么 sql 语句为：update user u set u.username = ? ,u.sex = ? where id=?

#### choose

假如不想用到所有的查询条件，只要查询条件有一个满足即可，使用 choose 标签可以解决此类问题，类似于 Java 的 switch 语句

标签：<when>，<otherwise>

```xml
<select id="selectUserByChoose" resultType="user" parameterType="user">
    SELECT * FROM user
    <where>
        <choose>
            <when test="id !='' and id != null">
                id=#{id}
            </when>
            <when test="username !='' and username != null">
                AND username=#{username}
            </when>
            <otherwise>
                AND sex=#{sex}
            </otherwise>
        </choose>
    </where>
</select>
```

有三个条件，id、username、sex，只能选择一个作为查询条件

- 如果 id 不为空，那么查询语句为：select * from user where  id=?
- 如果 id 为空，那么看 username 是否为空
  - 如果不为空，那么语句为：select * from user where username=?
  - 如果 username 为空，那么查询语句为 select * from user where sex=?

#### trim

trim 标记是一个格式化的标记，可以完成 set 或者是 where 标记的功能，自定义字符串截取

- prefix：给拼串后的整个字符串加一个前缀，trim 标签体中是整个字符串拼串后的结果
- prefixOverrides：去掉整个字符串前面多余的字符
- suffix：给拼串后的整个字符串加一个后缀
- suffixOverrides：去掉整个字符串后面多余的字符

改写 if + where 语句：

```xml
<select id="selectUserByUsernameAndSex" resultType="user" parameterType="com.ys.po.User">
    SELECT * FROM user
    <trim prefix="where" prefixOverrides="and | or">
        <if test="username != null">
            AND username=#{username}
        </if>
        <if test="sex != null">
            AND sex=#{sex}
        </if>
    </trim>
</select>
```

改写 if + set 语句：

```xml
<!-- 根据 id 更新 user 表的数据 -->
<update id="updateUserById" parameterType="com.ys.po.User">
    UPDATE user u
    <trim prefix="set" suffixOverrides=",">
        <if test="username != null and username != ''">
            u.username = #{username},
        </if>
        <if test="sex != null and sex != ''">
            u.sex = #{sex},
        </if>
    </trim>
    WHERE id=#{id}
</update>
```

#### foreach

基本格式：

```xml
<foreach>：循环遍历标签。适用于多个参数或者的关系。
    <foreach collection=“”open=“”close=“”item=“”separator=“”>
		获取参数
</foreach>
```

属性：

- collection：参数容器类型， (list-集合， array-数组)
- open：开始的 SQL 语句
- close：结束的 SQL 语句
- item：参数变量名
- separator：分隔符

需求：循环执行 sql 的拼接操作，`SELECT * FROM user WHERE id IN (1,2,5)`

- UserMapper.xml片段

  ```xml
  <select id="selectByIds" resultType="user" parameterType="list">
      SELECT * FROM student
      <where>
          <foreach collection="list" open="id IN(" close=")" item="id" separator=",">
              #{id}
          </foreach>
      </where>
  </select>
  ```

- 测试代码片段

  ```java
  //4.获取StudentMapper接口的实现类对象
  UserMapper mapper = sqlSession.getMapper(UserMapper.class);
  
  List<Integer> ids = new ArrayList<>();
  Collections.addAll(list, 1, 2);
  //5.调用实现类的方法，接收结果
  List<User> list = mapper.selectByIds(ids);
  
  for (User user : list) {
      System.out.println(user);
  }
  ```

  



#### SQL片段

将一些重复性的 SQL 语句进行抽取，以达到复用的效果

格式：

```xml
<sql id=“片段唯一标识”>抽取的SQL语句</sql>		<!--抽取标签-->
<include refid=“片段唯一标识”/>				<!--引入标签-->
```

使用：

```xml
<sql id="select">SELECT * FROM user</sql>

<select id="selectByIds" resultType="user" parameterType="list">
    <include refid="select"/>
    <where>
        <foreach collection="list" open="id IN(" close=")" item="id" separator=",">
            #{id}
        </foreach>
    </where>
 </select>
```







### 逆向工程

MyBatis 逆向工程，可以针对**单表**自动生成 MyBatis 执行所需要的代码（mapper.java、mapper.xml、pojo…）

generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
  PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
  "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
 
<generatorConfiguration>
    <context id="testTables" targetRuntime="MyBatis3">
        <commentGenerator>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true" />
        </commentGenerator>
        <!--数据库连接的信息：驱动类、连接地址、用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
            connectionURL="jdbc:mysql://localhost:3306/mybatisrelation" userId="root"
            password="root">
        </jdbcConnection>
 
        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL和NUMERIC类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>
 
        <!-- targetProject:生成PO类的位置！！ -->
        <javaModelGenerator targetPackage="com.ys.po"
            targetProject=".\src">
            <!-- enableSubPackages:是否让schema作为包的后缀 -->
            <property name="enableSubPackages" value="false" />
            <!-- 从数据库返回的值被清理前后的空格 -->
            <property name="trimStrings" value="true" />
        </javaModelGenerator>
        <!-- targetProject:mapper映射文件生成的位置！！ -->
        <sqlMapGenerator targetPackage="com.ys.mapper"
            targetProject=".\src">
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
        <!-- targetPackage：mapper接口生成的位置，重要！！ -->
        <javaClientGenerator type="XMLMAPPER"
            targetPackage="com.ys.mapper"
            targetProject=".\src">
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
        <!-- 指定数据库表，要生成哪些表，就写哪些表，要和数据库中对应，不能写错！ -->
        <table tableName="items"></table>
        <table tableName="orders"></table>
        <table tableName="orderdetail"></table>
        <table tableName="user"></table>       
    </context>
</generatorConfiguration>
```

生成代码：

```java
public void testGenerator() throws Exception{
    List<String> warnings = new ArrayList<String>();
    boolean overwrite = true;
    //指向逆向工程配置文件
    File configFile = new File(GeneratorTest.class.
                               getResource("/generatorConfig.xml").getFile());
    ConfigurationParser cp = new ConfigurationParser(warnings);
    Configuration config = cp.parseConfiguration(configFile);
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                                                             callback, warnings);
    myBatisGenerator.generate(null);

}
```

参考文章：https://www.cnblogs.com/ysocean/p/7360409.html

### 构建 SQL

#### 基础语法

MyBatis 提供了 org.apache.ibatis.jdbc.SQL 功能类，专门用于构建 SQL 语句    

| 方法                          | 说明                 |
| ----------------------------- | -------------------- |
| SELECT(String... columns)     | 根据字段拼接查询语句 |
| FROM(String... tables)        | 根据表名拼接语句     |
| WHERE(String... conditions)   | 根据条件拼接语句     |
| INSERT_INTO(String tableName) | 根据表名拼接新增语句 |
| INTO_VALUES(String... values) | 根据值拼接新增语句   |
| UPDATE(String table)          | 根据表名拼接修改语句 |
| DELETE_FROM(String table)     | 根据表名拼接删除语句 |

增删改查注解：

- @SelectProvider：生成查询用的 SQL 语句
- @InsertProvider：生成新增用的 SQL 语句
- @UpdateProvider：生成修改用的 SQL 语句注解
- @DeleteProvider：生成删除用的 SQL 语句注解。
  - type 属性：生成 SQL 语句功能类对象 
  - method 属性：指定调用方法

#### 基本操作

- MyBatisConfig.xml 配置

  ```xml
   <!-- mappers引入映射配置文件 -->
  <mappers>
      <package name="mapper"/>
  </mappers>
  ```

- Mapper 类

  ```java
  public interface StudentMapper {
      //查询全部
      @SelectProvider(type = ReturnSql.class, method = "getSelectAll")
      public abstract List<Student> selectAll();
  
      //新增数据
      @InsertProvider(type = ReturnSql.class, method = "getInsert")
      public abstract Integer insert(Student student);
  
      //修改操作
      @UpdateProvider(type = ReturnSql.class, method = "getUpdate")
      public abstract Integer update(Student student);
  
      //删除操作
      @DeleteProvider(type = ReturnSql.class, method = "getDelete")
      public abstract Integer delete(Integer id);
  
  }
  ```

- ReturnSQL 类

  ```java
  public class ReturnSql {
      //定义方法，返回查询的sql语句
      public String getSelectAll() {
          return new SQL() {
              {
                  SELECT("*");
                  FROM("student");
              }
          }.toString();
      }
  
      //定义方法，返回新增的sql语句
      public String getInsert(Student stu) {
          return new SQL() {
              {
                  INSERT_INTO("student");
                  INTO_VALUES("#{id},#{name},#{age}");
              }
          }.toString();
      }
  
      //定义方法，返回修改的sql语句
      public String getUpdate(Student stu) {
          return new SQL() {
              {
                  UPDATE("student");
                  SET("name=#{name}","age=#{age}");
                  WHERE("id=#{id}");
              }
          }.toString();
      }
  
      //定义方法，返回删除的sql语句
      public String getDelete(Integer id) {
          return new SQL() {
              {
                  DELETE_FROM("student");
                  WHERE("id=#{id}");
              }
          }.toString();
      }
  }
  ```

- 功能实现类

  ```java
  public class SqlTest {	
  	@Test  //查询全部
      public void selectAll() throws Exception{
          //1.加载核心配置文件
          InputStream is = Resources.getResourceAsStream("MyBatisConfig.xml");
  
          //2.获取SqlSession工厂对象
          SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
  
          //3.通过工厂对象获取SqlSession对象
          SqlSession sqlSession = sqlSessionFactory.openSession(true);
  
          //4.获取StudentMapper接口的实现类对象
          StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
  
          //5.调用实现类对象中的方法，接收结果
          List<Student> list = mapper.selectAll();
  
          //6.处理结果
          for (Student student : list) {
              System.out.println(student);
          }
  
          //7.释放资源
          sqlSession.close();
          is.close();
      }
      
      @Test  //新增
      public void insert() throws Exception{
          //1 2 3 4获取StudentMapper接口的实现类对象
          StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
  
          //5.调用实现类对象中的方法，接收结果 ->6 7
          Student stu = new Student(4,"赵六",26);
          Integer result = mapper.insert(stu);
      }
      
      @Test //修改
      public void update() throws Exception{
          //1 2 3 4 5调用实现类对象中的方法，接收结果 ->6 7 
  		Student stu = new Student(4,"赵六wq",36);
          Integer result = mapper.update(stu);
      }
      @Test //删除
      public void delete() throws Exception{
          //1 2 3 4 5 6 7
          Integer result = mapper.delete(4);
      }
  }
  ```









## 运行原理

### 运行机制

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png)

MyBatis 运行过程：

1. 加载 MyBatis 全局配置文件，通过 XPath 方式解析 XML 配置文件，首先解析核心配置文件，<settings> 标签中配置属性项有 defaultExecutorType，用来配置指定 Executor 类型，将配置文件的信息填充到 Configuration对象。最后解析映射器配置的映射文件，并**构建 MappedStatement 对象填充至 Configuration**，将解析后的映射器添加到 mapperRegistry 中，用于获取代理
2. 创建一个 DefaultSqlSession 对象，**根据参数创建指定类型的 Executor**，二级缓存默认开启，把 Executor 包装成缓存执行器
3. DefaulSqlSession 调用 getMapper()，通过 JDK 动态代理获取 Mapper 接口的代理对象 MapperProxy 
4. 执行 SQL 语句：
   - MapperProxy.invoke() 执行代理方法，通过 MapperMethod#execute 判断执行的是增删改查中的哪个方法
   - 查询方法调用 sqlSession.selectOne()，从 Configuration 中获取执行者对象 MappedStatement，然后 Executor 调用 executor.query 开始执行查询方法
   - 首先通过 CachingExecutor 去二级缓存查询，查询不到去一级缓存查询，**最后去数据库查询并放入一级缓存**
   - Configuration 对象根据 <select> 标签的 statementType 属性创建 StatementHandler 对象，在 StatementHandler 的构造方法中，创建了 ParameterHandler 和 ResultSetHandler 对象
   - 最后获取 **JDBC 原生的** Connection 数据库连接对象，创建 Statement 执行者对象，然后通过 ParameterHandler 设置预编译参数，底层是 TypeHandler#setParameter 方法，然后通过 StatementHandler 回调执行者对象执行增删改查，最后调用 ResultsetHandler 处理查询结果

**四大对象**：

- StatementHandler：执行 SQL 语句的对象
- ParameterHandler：设置预编译参数用的
- ResultHandler：处理结果集
- Executor：执行器，真正进行 Java 与数据库交互的对象



参考视频：https://www.bilibili.com/video/BV1mW411M737?p=71





### 获取工厂

SqlSessionFactoryBuilder.build(InputStream, String,  Properties)：构建工厂

XMLConfigBuilder.parse()：解析核心配置文件每个标签的信息（**XPath**）

- `parseConfiguration(parser.evalNode("/configuration"))`：读取节点内数据，<configuration> 是 MyBatis 配置文件中的顶层标签

  `settings = settingsAsProperties(root.evalNode("settings"))`：读取核心配置文件中的 <settings> 标签

  `settingsElement(settings)`：设置框架相关的属性

  - `configuration.setCacheEnabled()`：**设置缓存属性，默认是 true**
  - `configuration.setDefaultExecutorType()`：**设置 Executor 类型到 configuration，默认是 SIMPLE**

  `mapperElement(root.evalNode("mappers"))`：解析 mappers 信息，分为 package 和 单个注册两种

  - `if...else...`：根据映射方法选择合适的读取方式

  - `XMLMapperBuilder.parse()`：解析 mapper 的标签的信息

    - `configurationElement(parser.evalNode("/mapper"))`：解析 mapper 文件，顶层节点 <mapper>

      - `buildStatementFromContext(context.evalNodes("select..."))`：解析**每个操作标签**

        `XMLStatementBuilder.parseStatementNode()`：解析**操作标签**的所有的属性

        `builderAssistant.addMappedStatement(...)`：**封装成 MappedStatement 对象加入 Configuration 对象**，代表一个增删改查的标签

  - `Class<?> mapperInterface = Resources.classForName(mapperClass)`：加载 Mapper 接口

  - `Configuration.addMappers()`：将核心配置文件配置的映射器添加到 mapperRegistry 中，用来**获取代理对象**

    - `MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type)`：创建**注解**解析器

    - `parser.parse()`：解析 Mapper 接口

      - `SqlSource sqlSource = getSqlSourceFromAnnotations()`：获取 SQL 的资源对象

        ![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-SQL%E8%B5%84%E6%BA%90%E5%AF%B9%E8%B1%A1.png)

      - `builderAssistant.addMappedStatement(...)`：封装成 MappedStatement 对象加入 Configuration 对象

- `return configuration`：返回配置完成的 configuration 对象

return new DefaultSqlSessionFactory(config)：返回工厂对象，包含 Configuration 对象

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E8%8E%B7%E5%8F%96%E5%B7%A5%E5%8E%82%E5%AF%B9%E8%B1%A1.png)

总结：解析 XML 是对 Configuration 中的属性进行填充，那么可以在一个类中创建 Configuration 对象，自定义其中属性的值来达到配置的效果





### 获取会话

DefaultSqlSessionFactory.openSession()：获取 Session 对象，并且创建 Executor 对象

DefaultSqlSessionFactory.openSessionFromDataSource(...)：ExecutorType 为 Executor 的类型，TransactionIsolationLevel 为事务隔离级别，autoCommit 是否开启事务

- `transactionFactory.newTransaction(DataSource, IsolationLevel, boolean`：事务对象
- `configuration.newExecutor(tx, execType)`：**根据参数创建指定类型的 Executor**
  - 批量操作笔记的部分有讲解到 <setting> 的属性 defaultExecutorType，根据配置创建对象
  - **二级缓存默认开启**，会包装 Executor 对象 `new CachingExecutor(executor)`

 return new DefaultSqlSession(configuration, executor, autoCommit)：返回 DefaultSqlSession 对象

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E8%8E%B7%E5%8F%96%E4%BC%9A%E8%AF%9D%E5%AF%B9%E8%B1%A1.png)





### 获取代理

Configuration.getMapper(Class, SqlSession)：获取代理的 mapper 对象

MapperRegistry.getMapper(Class, SqlSession)：MapperRegistry 是 Configuration 属性，在获取工厂对象时初始化

- `(MapperProxyFactory<T>) knownMappers.get(type)`：获取接口信息封装为 MapperProxyFactory 对象
- `mapperProxyFactory.newInstance(sqlSession)`：**创建代理对象**
  - `new MapperProxy<>(sqlSession, mapperInterface, methodCache)`：包装对象
    - methodCache 是并发安全的 ConcurrentHashMap 集合，存放要执行的方法
    - `MapperProxy<T> implements InvocationHandler` 说明 MapperProxy 默认是一个 InvocationHandler 对象
  - `Proxy.newProxyInstance()`：**JDK 动态代理**创建 MapperProxy 对象

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E8%8E%B7%E5%8F%96%E4%BB%A3%E7%90%86%E5%AF%B9%E8%B1%A1.png)





### 执行SQL

MapperProxy.invoke()：执行 SQL 语句，Object 类的方法直接执行

```
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
        // 当前方法是否是属于 Object 类中的方法
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
            // 当前方法是否是默认方法
        } else if (isDefaultMethod(method)) {
            return invokeDefaultMethod(proxy, method, args);
        }
    } catch (Throwable t) {
        throw ExceptionUtil.unwrapThrowable(t);
    }
    // 包装成一个 MapperMethod 对象并初始化该对象
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    // 【根据 switch-case 判断使用的什么类型的 SQL 进行逻辑处理】，此处分析查询语句的查询操作
    return mapperMethod.execute(sqlSession, args);
}
```

sqlSession.selectOne(String, Object)：查询数据

```
public Object execute(SqlSession sqlSession, Object[] args) {
    //.....
    // 解析传入的参数
    Object param = method.convertArgsToSqlCommandParam(args);
    result = sqlSession.selectOne(command.getName(), param);
}
// DefaultSqlSession.selectList(String, Object)
public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    // 获取执行者对象
    MappedStatement ms = configuration.getMappedStatement(statement);
    // 开始执行查询语句，参数通过 wrapCollection() 包装成集合类
    return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
}
```

Executor#query()：

- `CachingExecutor.query()`：先执行 CachingExecutor 去二级缓存获取数据

  ```
  public class CachingExecutor implements Executor {
    private final Executor delegate;    // 包装了 BaseExecutor，二级缓存不存在数据调用 BaseExecutor 查询
  }
  ```

  - `MappedStatement.getBoundSql(parameterObject)`：**把 parameterObject 封装成 BoundSql**

    构造函数中有：`this.parameterObject = parameterObject`

    ![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-boundSql%E5%AF%B9%E8%B1%A1.png)

  - `CachingExecutor.createCacheKey()`：创建缓存对象

  - `ms.getCache()`：获取二级缓存

  - `tcm.getObject(cache, key)`：尝试从**二级缓存**中获取数据

- `BaseExecutor.query()`：二级缓存不存在该数据，调用该方法

  - `localCache.getObject(key) `：尝试从**本地缓存（一级缓存**）获取数据

- `BaseExecutor.queryFromDatabase()`：缓存获取数据失败，**开始从数据库获取数据，并放入本地缓存**

  - `SimpleExecutor.doQuery()`：执行 query

    - `configuration.newStatementHandler()`：创建 StatementHandler 对象

      - 根据 <select> 标签的 statementType 属性，根据属性选择创建哪种对象
      - 判断 BoundSql 是否被创建，没有创建会重新封装参数信息到 BoundSql
      - **StatementHandler 的构造方法中，创建了 ParameterHandler 和 ResultSetHandler 对象**
      - `interceptorChain.pluginAll(statementHandler)`：拦截器链

    - `prepareStatement()`：通过 StatementHandler 创建 JDBC 原生的 Statement 对象

      - `getConnection()`：**获取 JDBC 的 Connection 对象**
      - `handler.prepare()`：初始化 Statement 对象
        - `instantiateStatement(Connection connection)`：Connection  中的方法实例化对象
          - 获取普通执行者对象：`Connection.createStatement()`
          - **获取预编译执行者对象**：`Connection.prepareStatement()`
      - `handler.parameterize()`：进行参数的设置
        - `ParameterHandler.setParameters()`：**通过 ParameterHandler 设置参数**
          - `typeHandler.setParameter()`：底层通过 TypeHandler 实现，回调 JDBC 的接口进行设置

    - `StatementHandler.query()`：**调用 JDBC 原生的 PreparedStatement 执行 SQL**

      ```java
      public <E> List<E> query(Statement statement, ResultHandler resultHandler) {
          // 获取 SQL 语句
          String sql = boundSql.getSql();
          statement.execute(sql);
          // 通过 ResultSetHandler 对象封装结果集，映射成 JavaBean
          return resultSetHandler.handleResultSets(statement);
        }
      ```

      `resultSetHandler.handleResultSets(statement)`：处理结果集

      - `handleResultSet(rsw, resultMap, multipleResults, null)`：底层回调

        - `handleRowValues()`：逐行处理数据，根据是否配置了 <resultMap> 属性选择是否使用简单结果集映射

          - 首先判断数据是否被限制行数，然后进行结果集的映射

          - 最后将数据存入 ResultHandler 对象，底层就是 List 集合

            ```java
            public class DefaultResultHandler implements ResultHandler<Object> {
            	private final List<Object> list;
              	public void handleResult(ResultContext<?> context) {
                	list.add(context.getResultObject());
              	}
            }
            ```

      - `return collapseSingleResultList(multipleResults)`：可能存在多个结果集的情况

  - `localCache.putObject(key, list)`：**放入一级（本地）缓存**

`return list.get(0)`：返回结果集的第一个数据

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E6%89%A7%E8%A1%8CSQL%E8%BF%87%E7%A8%8B.png)









## 插件使用

### 插件原理

实现原理：插件是按照插件配置顺序创建层层包装对象，执行目标方法的之后，按照逆向顺序执行（栈）

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/MyBatis-%E6%8F%92%E4%BB%B6%E5%8E%9F%E7%90%86.png)

在四大对象创建时：

- 每个创建出来的对象不是直接返回的，而是 `interceptorChain.pluginAll(parameterHandler)`
- 获取到所有 Interceptor（插件需要实现的接口），调用 `interceptor.plugin(target)`返回 target 包装后的对象
- 插件机制可以使用插件为目标对象创建一个代理对象，代理对象可以**拦截到四大对象的每一个执行**

```
@Intercepts(
    {
    @Signature(type=StatementHandler.class,method="parameterize",args=java.sql.Statement.class)
    })
public class MyFirstPlugin implements Interceptor{

  //intercept：拦截目标对象的目标方法的执行
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    System.out.println("MyFirstPlugin...intercept:" + invocation.getMethod());
    //动态的改变一下sql运行的参数：以前1号员工，实际从数据库查询11号员工
    Object target = invocation.getTarget();
    System.out.println("当前拦截到的对象：" + target);
    //拿到：StatementHandler==>ParameterHandler===>parameterObject
    //拿到target的元数据
    MetaObject metaObject = SystemMetaObject.forObject(target);
    Object value = metaObject.getValue("parameterHandler.parameterObject");
    System.out.println("sql语句用的参数是：" + value);
    //修改完sql语句要用的参数
    metaObject.setValue("parameterHandler.parameterObject", 11);
    //执行目标方法
    Object proceed = invocation.proceed();
    //返回执行后的返回值
    return proceed;
  }

  // plugin：包装目标对象的，为目标对象创建一个代理对象
  @Override
  public Object plugin(Object target) {
    //可以借助 Plugin 的 wrap 方法来使用当前 Interceptor 包装我们目标对象
    System.out.println("MyFirstPlugin...plugin:mybatis将要包装的对象" + target);
    Object wrap = Plugin.wrap(target, this);
    //返回为当前target创建的动态代理
    return wrap;
  }

  // setProperties：将插件注册时的property属性设置进来
  @Override
  public void setProperties(Properties properties) {
    System.out.println("插件配置的信息：" + properties);
  }
}
```

核心配置文件：

```xml
<!--plugins：注册插件  -->
<plugins>
    <plugin interceptor="mybatis.dao.MyFirstPlugin">
        <property name="username" value="root"/>
        <property name="password" value="123456"/>
    </plugin>
</plugins>
```







### 分页插件

![img](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/%E5%88%86%E9%A1%B5%E4%BB%8B%E7%BB%8D.png)

- 分页可以将很多条结果进行分页显示。如果当前在第一页，则没有上一页。如果当前在最后一页，则没有下一页，需要明确当前是第几页，这一页中显示多少条结果。  
- MyBatis 是不带分页功能的，如果想实现分页功能，需要手动编写 LIMIT 语句，不同的数据库实现分页的 SQL 语句也是不同，手写分页 成本较高。
- PageHelper：第三方分页助手，将复杂的分页操作进行封装，从而让分页功能变得非常简单





### 分页操作

开发步骤：

1. 导入 PageHelper 的 Maven 坐标

2. 在 MyBatis 核心配置文件中配置 PageHelper 插件

   注意：分页助手的插件配置在通用 Mapper 之前

   ```xml
   <plugins>
       <plugin interceptor="com.github.pagehelper.PageInterceptor">
           <!-- 指定方言 -->
       	<property name="dialect" value="mysql"/>
       </plugin> 
   </plugins>
   <mappers>.........</mappers>
   ```

3. 与 MySQL 分页查询页数计算公式不同

   `static <E> Page<E> startPage(int pageNum, int pageSize)`：pageNum第几页，pageSize页面大小

   ```java
   @Test
   public void selectAll() {
       //第一页：显示2条数据
       PageHelper.startPage(1,2);
       List<Student> students = sqlSession.selectList("StudentMapper.selectAll");
       for (Student student : students) {
           System.out.println(student);
       }
   }
   ```

   



### 参数获取

PageInfo构造方法：

- `PageInfo<Student> info = new PageInfo<>(list)` : list 是 SQL 执行返回的结果集合，参考上一节

PageInfo相关API：

1. startPage()：设置分页参数 
2. PageInfo：分页相关参数功能类。 
3. getTotal()：获取总条数 
4. getPages()：获取总页数
5. getPageNum()：获取当前页
6. getPageSize()：获取每页显示条数
7. getPrePage()：获取上一页 
8. getNextPage()：获取下一页 
9. isIsFirstPage()：获取是否是第一页 
10. isIsLastPage()：获取是否是最后一页 

***

# Mybatis的八股文

### Mybatis的执行流程![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/0f72ca58-2300-4b8d-a9e4-321447cb16a6.png)

1. 读取Mybatis配置文件：mybatis-config.xml加载运行环境和映射文件
2. 构造会话工厂SqlSessionFactory
3. 会话工厂创建SqlSession对象
4. 操作数据库的接口，Executor执行器，同时负责查询缓存的维护
5. Executor接口的执行方法中有一个MappedStatement类型的参数，封装了映射信息
6. 输入参数映射
7. 输出结果映射

### Mybatis的延迟加载是什么？

**MyBatis 的延迟加载（Lazy Loading）** 是指在使用 MyBatis 进行数据库查询时，某些关联对象不会立即加载，而是在需要使用这些对象时才进行查询。这种机制可以提高查询效率，尤其是在不需要所有关联对象数据时，避免不必要的数据库查询。

**延迟加载的基本概念**

​	•**立即加载（Eager Loading）**：关联对象在主对象被查询时立即加载，哪怕这些关联对象并没有被使用。

​	•**延迟加载（Lazy Loading）**：关联对象在第一次访问时才会被加载，而不是随着主对象一起被查询。

**延迟加载的工作原理**

​	MyBatis 的延迟加载机制通过代理模式实现。当查询主对象时，MyBatis 并不会立即加载关联对象，而是为关联对象创建一个代理对象。当应用程序首次访问这个代理对象时，MyBatis 才执行 SQL 查询，获取真实的关联对象数据。

例如，假设有两个实体：User 和 Address，User 包含 Address 的引用。

​	•如果使用 **延迟加载**，当查询 User 时，Address 不会立即被加载。只有当代码访问 User.getAddress() 时，MyBatis 才会执行查询 Address 的 SQL。

​	•如果使用 **立即加载**，则在查询 User 的时候，Address 也会立即查询并加载到内存中。
