# Maven

## Maven的简介

- maven的本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型(POM)
- POM（Project Object Model）：项目对象模型

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/0ba68daf-6fe0-44d9-b1c9-3803132d880d.png)

## Maven的作用

- 项目构建：提供标准的，跨平台的自动化项目构建方式
- 依赖管理：方便快捷的管理项目依赖的资源，避免资源间的版本冲突问题
- 统一开发结构：提供标准的，统一的项目结构

## Maven的使用

1. 安装maven，官网：http://maven.apache.org/

2. Maven环境变量配置，依赖java，所以需要配置JAVA_HOME, 设置MAVEN自身的运行环境, 需要配置MAVEN_HOME 

   ![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/8a5d0df3-ed78-410d-adca-53dbe24789e7.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/73e36098-ce8f-47c6-8308-54678dcd46bd.png) 

3. 配置本地仓库

```xml
<localRepository>/Users/huangkaiwen/Desktop/Study/Java_home/apache-maven-3.6.1/mvn_repo</localRepository>
```

4. 远程仓库的配置

```
  <mirrors>
    <mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Nexus aliyun</name>
        <url>https://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror>
  </mirrors>
```

## Maven仓库

- 仓库：用于存储资源，包含各种jar包
- 仓库分类：
  - 本地仓库：自己电脑存储资源的仓库，连接远程仓库获取资源
  - 中央仓库：Maven团队维护，存储所有资源的仓库
  - 私服：公司范围内存储资源的仓库，从中央仓库中获取资源

 ## Maven坐标

-  groupID：当前项目的隶属组织名称
- artifactID：定义当前Maven项目的名称
- version：定义当前版本号
- packaging：定义项目的打包方式

## 依赖管理

- 依赖传递：依赖具有传递性
  - 直接依赖：在当前项目中通过依赖配置建立的依赖关系
  - 间接依赖：被依赖的资源如果依赖其他资源，则当前项目间接依赖其他资源
- 依赖传递冲突问题
  - 路径优先：当依赖中出现相同资源，层级越深，优先级越低，层级越浅，优先级越高
  - 声明优先：当资源在相同级被依赖，配置顺序靠前的覆盖配置顺序靠后的
  - 特殊优先：当同级配置了相同资源的版本，后配置的覆盖先配置的
- 可选依赖：对外隐藏当前所依赖的资源

```xml
<dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>3.13.6</version>
            <optional>true</optional>
</dependency>
```

- 排除依赖：指的是主动断开锁依赖的资源，被排除的资源无需指定版本

```xml
<dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>3.13.6</version>
            <exclusions>
                <exclusion>
                    <groupId>log4j</groupId>
                    <artifactId>log4j</artifactId>
                </exclusion>
            </exclusions>
</dependency>
```

- 依赖的范围

依赖的jar默认情况可以在任何地方使用，可以通过scope标签设定作用范围

**作用范围**

1. 主程序范围有效（main文件夹范围内）
2. 测试程序范围有效（test文件夹范围）
3. 是否参与打包（package指令范围内）

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/2c540986-eee4-444a-8ac7-15d6c12e97ff.png)

## 声明周期与插件

- 声明周期：是指一次构建过程经历了多少事件
- 插件：插件与声明周期内的阶段绑定，在执行到对应的声明周期时，执行对应的插件功能

```xml
<plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>2.4.5</version>
    </plugin>
</plugins>
```

## Maven的打包方式

- jar：资源文件形式
- war：网络工程文件
- pom：专门用于聚合工程

## Maven的聚合

1. 创建一个空模块，打包类型为pom

```xml
<packaging>pom</packaging>
```

2. 定义当前模块进行构建操作时关联的其他模块名称

```xml
<modules>
        <module>heima-leadnews-common</module>
        <module>heima-leadnews-utils</module>
        <module>heima-leadnews-model</module>
        <module>heima-leadnews-feign-api</module>
        <module>heima-leadnews-service</module>
        <module>heima-leadnews-gateway</module>
        <module>heima-leadnews-test</module>
</modules>
```

## Maven的继承

```xml
  # 定义父工程 
	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.9.RELEASE</version>
    </parent>
# 声明依赖管理
<dependencyManagement>
		<dependencies>
            <!--内部依赖工程-->
            <dependency>
                <artifactId>heima-leadnews-feign-api</artifactId>
                <groupId>com.heima</groupId>
                <version>${project.version}</version>
            </dependency>	
   <dependencies>
</dependencyManagement>
```

## Maven属性

```xml
# 自定义属性 
<properties>
        <!-- 项目源码及编译输出的编码 -->
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <!-- 项目编译JDK版本 -->
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>

        <!-- 依赖包版本管理 -->
        <spring.boot.version>2.3.9.RELEASE</spring.boot.version>
        <commons.io.version>2.6</commons.io.version>
        <commons.codec.version>1.12</commons.codec.version>
        <lombok.version>1.18.8</lombok.version>
</properties>
# 调用格式
<dependency>
  <groupId>org.springframework</groupId>
  <artifactID>spring-context</artifactID>
  <version>${spring.version}</version>
</dependency>

# 内置属性
${basedir} ${version}

#setting属性:使用Maven配置文件setting.xml中的标签属性，用于动态配置
${settings.localRepository}

# Java系统属性
${user.home} 
CMB命令:mvn help:system
```

## Maven八股文

1. 如何管理微服务中的父子依赖，父依赖在子依赖中是否需要全部加载？

​	父依赖不一定需要全部加载到子项目中，这取决于父项目中依赖的声明方式。通过 dependencyManagement 可以实现依赖版本的统一管理，但不会自动加载依赖，子项目可以按需引入。对于必须在所有子项目中使用的依赖，可以在父项目中直接声明。优化微服务依赖管理的关键是合理分离公共依赖和业务依赖，减少冗余依赖的加载。
