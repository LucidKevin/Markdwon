# SpringBoot

## 基本介绍

### Boot介绍

SpringBoot 提供了一种快速使用 Spring 的方式，基于约定优于配置的思想，可以让开发人员不必在配置与逻辑业务之间进行思维的切换，全身心的投入到逻辑业务的代码编写中，从而大大提高了开发的效率

SpringBoot 功能：

* 自动配置，自动配置是一个运行时（更准确地说，是应用程序启动时）的过程，考虑了众多因素选择使用哪个配置，该过程是SpringBoot 自动完成的

* 起步依赖，起步依赖本质上是一个 Maven 项目对象模型（Project Object Model，POM），定义了对其他库的传递依赖，这些东西加在一起即支持某项功能。简单的说，起步依赖就是将具备某种功能的坐标打包到一起，并提供一些默认的功能

* 辅助功能，提供了一些大型项目中常见的非功能性特性，如内嵌 web 服务器、安全、指标，健康检测、外部配置等


***

### 构建工程

普通构建：

1. 创建 Maven 项目

2. 导入 SpringBoot 起步依赖

   ```xml
   <!--springboot 工程需要继承的父工程-->
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>2.1.8.RELEASE</version>
   </parent>
   
   <dependencies>
       <!--web 开发的起步依赖-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   </dependencies>
   ```

3. 定义 Controller

   ```java
   @RestController
   public class HelloController {
       @RequestMapping("/hello")
       public String hello(){
           return " hello Spring Boot !";
       }
   }
   ```

4. 编写引导类

   ```java
   // 引导类，SpringBoot项目的入口
   @SpringBootApplication
   public class HelloApplication {
       public static void main(String[] args) {
           SpringApplication.run(HelloApplication.class, args);
       }
   }
   ```

快速构建：

![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/SpringBoot-IDEA构建工程.png)

***

## 自动装配

### 依赖管理

在 spring-boot-starter-parent 中定义了各种技术的版本信息，组合了一套最优搭配的技术版本。在各种 starter 中，定义了完成该功能需要的坐标合集，其中大部分版本信息来自于父工程。工程继承 parent，引入 starter 后，通过依赖传递，就可以简单方便获得需要的 jar 包，并且不会存在版本冲突，自动版本仲裁机制

***

### 底层注解

#### SpringBoot

@SpringBootApplication：启动注解，实现 SpringBoot 的自动部署

* 参数 scanBasePackages：可以指定扫描范围
* 默认扫描当前引导类所在包及其子包

假如所在包为 com.example.springbootenable，扫描配置包 com.example.config 的信息，三种解决办法：

1. 使用 @ComponentScan 扫描 com.example.config 包

2. 使用 @Import 注解加载类，这些类都会被 Spring 创建并放入 ioc 容器，默认组件的名字就是**全类名**

3. 对 @Import 注解进行封装

```java
//1.@ComponentScan("com.example.config")
//2.@Import(UserConfig.class)
@EnableUser
@SpringBootApplication
public class SpringbootEnableApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringbootEnableApplication.class, args);
    	//获取Bean
        Object user = context.getBean("user");
        System.out.println(user);

	}
}
```

UserConfig：

```java
@Configuration
public class UserConfig {
    @Bean
    public User user() {
        return new User();
    }
}
```

EnableUser 注解类：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserConfig.class)//@Import注解实现Bean的动态加载
public @interface EnableUser {
}
```

***

#### Configuration

@Configuration：设置当前类为 SpringBoot 的配置类

* proxyBeanMethods = true：Full 全模式，每个 @Bean 方法被调用多少次返回的组件都是单实例的，默认值，类组件之间**有依赖关系**，方法会被调用得到之前单实例组件
* proxyBeanMethods = false：Lite 轻量级模式，每个 @Bean 方法被调用多少次返回的组件都是新创建的，类组件之间**无依赖关系**用 Lite 模式加速容器启动过程

```java
@Configuration(proxyBeanMethods = true)
public class MyConfig {
    @Bean //给容器中添加组件。以方法名作为组件的 id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user(){
        User user = new User("zhangsan", 18);
        return user;
    }
}
```

***

#### Condition

**条件注解**

Condition 是 Spring4.0 后引入的条件化配置接口，通过实现 Condition 接口可以完成有条件的加载相应的 Bean

注解：@Conditional

作用：条件装配，满足 Conditional 指定的条件则进行组件注入，加上方法或者类上，作用范围不同

使用：@Conditional 配合 Condition 的实现类（ClassCondition）进行使用

ConditionContext 类API：

| 方法                                              | 说明                          |
| ------------------------------------------------- | ----------------------------- |
| ConfigurableListableBeanFactory  getBeanFactory() | 获取到 IOC 使用的 beanfactory |
| ClassLoader getClassLoader()                      | 获取类加载器                  |
| Environment getEnvironment()                      | 获取当前环境信息              |
| BeanDefinitionRegistry getRegistry()              | 获取到 bean 定义的注册类      |

* ClassCondition：

  ```java
  public class ClassCondition implements Condition {
      /**
       * context 上下文对象。用于获取环境，IOC容器，ClassLoader对象
       * metadata 注解元对象。 可以用于获取注解定义的属性值
       */
      @Override
      public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        
          //1.需求： 导入Jedis坐标后创建Bean
          //思路：判断redis.clients.jedis.Jedis.class文件是否存在
          boolean flag = true;
          try {
              Class<?> cls = Class.forName("redis.clients.jedis.Jedis");
          } catch (ClassNotFoundException e) {
              flag = false;
          }
          return flag;
      }
  }
  ```

* UserConfig：

  ```java
  @Configuration
  public class UserConfig {
      @Bean
      @Conditional(ClassCondition.class)
      public User user(){
          return new User();
      }
  }
  ```

* 启动类：

  ```java
  @SpringBootApplication
  public class SpringbootConditionApplication {
      public static void main(String[] args) {
          //启动SpringBoot应用，返回Spring的IOC容器
          ConfigurableApplicationContext context = SpringApplication.run(SpringbootConditionApplication.class, args);
  
          Object user = context.getBean("user");
          System.out.println(user);
      }
  }
  ```

***

**自定义注解**

将类的判断定义为动态的，判断哪个字节码文件存在可以动态指定

* 自定义条件注解类

  ```java
  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Conditional(ClassCondition.class)
  public @interface ConditionOnClass {
      String[] value();
  }
  ```

* ClassCondition

  ```java
  public class ClassCondition implements Condition {
      @Override
      public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
  
          //需求：通过注解属性值value指定坐标后创建bean
          Map<String, Object> map = metadata.getAnnotationAttributes
              					(ConditionOnClass.class.getName());
          //map = {value={属性值}}
          //获取所有的
          String[] value = (String[]) map.get("value");
  
          boolean flag = true;
          try {
              for (String className : value) {
                  Class<?> cls = Class.forName(className);
              }
          } catch (Exception e) {
              flag = false;
          }
          return flag;
      }
  }
  ```

* UserConfig

  ```java
  @Configuration
  public class UserConfig {
      @Bean
      @ConditionOnClass("com.alibaba.fastjson.JSON")//JSON加载了才注册 User 到容器
      public User user(){
          return new User();
      }
  }
  ```

* 测试 User 对象的创建

***

**常用注解**

SpringBoot 提供的常用条件注解：

@ConditionalOnProperty：判断**配置文件**中是否有对应属性和值才初始化 Bean

```java
@Configuration
public class UserConfig {
    @Bean
    @ConditionalOnProperty(name = "it", havingValue = "seazean")
    public User user() {
        return new User();
    }
}
```

```properties
it=seazean
```

@ConditionalOnClass：判断环境中是否有对应类文件才初始化 Bean

@ConditionalOnMissingClass：判断环境中是否有对应类文件才初始化 Bean

@ConditionalOnMissingBean：判断环境中没有对应Bean才初始化 Bean

*****

#### ImportRes

使用 bean.xml 文件生成配置 bean，如果需要继续复用 bean.xml，@ImportResource 导入配置文件即可

```java
@ImportResource("classpath:beans.xml")
public class MyConfig {
	//...
}
```

```xml
<beans ...>
    <bean id="haha" class="com.lun.boot.bean.User">
        <property name="name" value="zhangsan"></property>
        <property name="age" value="18"></property>
    </bean>

    <bean id="hehe" class="com.lun.boot.bean.Pet">
        <property name="name" value="tomcat"></property>
    </bean>
</beans>
```

****

#### Properties

@ConfigurationProperties：读取到 properties 文件中的内容，并且封装到 JavaBean 中

配置文件：

```properties
mycar.brand=BYD
mycar.price=100000
```

JavaBean 类：

```java
@Component	//导入到容器内
@ConfigurationProperties(prefix = "mycar")//代表配置文件的前缀
public class Car {
    private String brand;
    private Integer price;
}
```

***

### 装配原理

#### 启动流程

应用启动：

```java
@SpringBootApplication
public class BootApplication {
    public static void main(String[] args) {
        // 启动代码
        SpringApplication.run(BootApplication.class, args);
    }
}
```

SpringApplication 构造方法：

* `this.resourceLoader = resourceLoader`：资源加载器，初始为 null
* `this.webApplicationType = WebApplicationType.deduceFromClasspath()`：判断当前应用的类型，是响应式还是 Web 类
* `this.bootstrapRegistryInitializers = getBootstrapRegistryInitializersFromSpringFactories()`：**获取引导器**
  * 去 **`META-INF/spring.factories`** 文件中找 org.springframework.boot.Bootstrapper
  * 寻找的顺序：classpath → spring-beans → boot-devtools → springboot → boot-autoconfigure
* `setInitializers(getSpringFactoriesInstances(ApplicationContextInitializer.class))`：**获取初始化器**
  * 去 `META-INF/spring.factories` 文件中找 org.springframework.context.ApplicationContextInitializer
* `setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class))`：**获取监听器**
  * 去 `META-INF/spring.factories` 文件中找 org.springframework.context.ApplicationListener

* `this.mainApplicationClass = deduceMainApplicationClass()`：获取出 main 程序类

SpringApplication#run(String... args)：创建 IOC 容器并实现了自动装配

* `StopWatch stopWatch = new StopWatch()`：停止监听器，**监控整个应用的启停**

* `stopWatch.start()`：记录应用的启动时间

* `bootstrapContext = createBootstrapContext()`：**创建引导上下文环境**

  * `bootstrapContext = new DefaultBootstrapContext()`：创建默认的引导类环境
  * `this.bootstrapRegistryInitializers.forEach()`：遍历所有的引导器调用 initialize 方法完成初始化设置

* `configureHeadlessProperty()`：让当前应用进入 headless 模式

* `listeners = getRunListeners(args)`：**获取所有 RunListener（运行监听器）**

  * 去 `META-INF/spring.factories` 文件中找 org.springframework.boot.SpringApplicationRunListener

* `listeners.starting(bootstrapContext, this.mainApplicationClass)`：遍历所有的运行监听器调用 starting 方法

* `applicationArguments = new DefaultApplicationArguments(args)`：获取所有的命令行参数

* `environment = prepareEnvironment(listeners, bootstrapContext, applicationArguments)`：**准备环境**

  * `environment = getOrCreateEnvironment()`：返回或创建基础环境信息对象
    * `switch (this.webApplicationType)`：**根据当前应用的类型创建环境**
      * `case SERVLET`：Web 应用环境对应 ApplicationServletEnvironment
      * `case REACTIVE`：响应式编程对应 ApplicationReactiveWebEnvironment
      * `default`：默认为 Spring 环境 ApplicationEnvironment
  * `configureEnvironment(environment, applicationArguments.getSourceArgs())`：读取所有配置源的属性值配置环境
  * `ConfigurationPropertySources.attach(environment)`：属性值绑定环境信息
    * `sources.addFirst(ATTACHED_PROPERTY_SOURCE_NAME,..)`：把 configurationProperties 放入环境的属性信息头部

  * `listeners.environmentPrepared(bootstrapContext, environment)`：运行监听器调用 environmentPrepared()，EventPublishingRunListener 发布事件通知所有的监听器当前环境准备完成

  * `DefaultPropertiesPropertySource.moveToEnd(environment)`：移动 defaultProperties 属性源到环境中的最后一个源

  * `bindToSpringApplication(environment)`：与容器绑定当前环境

  * `ConfigurationPropertySources.attach(environment)`：重新将属性值绑定环境信息
  * `sources.remove(ATTACHED_PROPERTY_SOURCE_NAME)`：从环境信息中移除 configurationProperties 

  * `sources.addFirst(ATTACHED_PROPERTY_SOURCE_NAME,..)`：把 configurationProperties 重新放入环境信息

* `configureIgnoreBeanInfo(environment)`：**配置忽略的 bean**

* `printedBanner = printBanner(environment)`：打印 SpringBoot 标志

* `context = createApplicationContext()`：**创建 IOC 容器**

  `switch (this.webApplicationType)`：根据当前应用的类型创建 IOC 容器

  * `case SERVLET`：Web 应用环境对应 AnnotationConfigServletWebServerApplicationContext
  * `case REACTIVE`：响应式编程对应 AnnotationConfigReactiveWebServerApplicationContext
  * `default`：默认为 Spring 环境 AnnotationConfigApplicationContext

* `context.setApplicationStartup(this.applicationStartup)`：设置一个启动器

* `prepareContext()`：配置 IOC 容器的基本信息

  * `postProcessApplicationContext(context)`：后置处理流程

  * `applyInitializers(context)`：获取所有的**初始化器调用 initialize() 方法**进行初始化
  * `listeners.contextPrepared(context)`：所有的运行监听器调用 environmentPrepared() 方法，EventPublishingRunListener 发布事件通知 IOC 容器准备完成
  * `listeners.contextLoaded(context)`：所有的运行监听器调用 contextLoaded() 方法，通知 IOC 加载完成

* `refreshContext(context)`：**刷新 IOC 容器**

  * Spring 的容器启动流程
  * `invokeBeanFactoryPostProcessors(beanFactory)`：**实现了自动装配**
  * `onRefresh()`：**创建 WebServer** 使用该接口

* `afterRefresh(context, applicationArguments)`：留给用户自定义容器刷新完成后的处理逻辑

* `stopWatch.stop()`：记录应用启动完成的时间

* `callRunners(context, applicationArguments)`：调用所有 runners

* `listeners.started(context)`：所有的运行监听器调用 started() 方法

* `listeners.running(context)`：所有的运行监听器调用 running() 方法

  * 获取容器中的 ApplicationRunner、CommandLineRunner
  * `AnnotationAwareOrderComparator.sort(runners)`：合并所有 runner 并且按照 @Order 进行排序

  * `callRunner()`：遍历所有的 runner，调用 run 方法

* `handleRunFailure(context, ex, listeners)`：**处理异常**，出现异常进入该逻辑

  * `handleExitCode(context, exception)`：处理错误代码
  * `listeners.failed(context, exception)`：运行监听器调用 failed() 方法
  * `reportFailure(getExceptionReporters(context), exception)`：通知异常

****

#### 注解分析

SpringBoot 定义了一套接口规范，这套规范规定 SpringBoot 在启动时会扫描外部引用 jar 包中的 `META-INF/spring.factories` 文件，将文件中配置的类型信息加载到 Spring 容器，并执行类中定义的各种操作，对于外部的 jar 包，直接引入一个 starter 即可

@SpringBootApplication 注解是 `@SpringBootConfiguration`、`@EnableAutoConfiguration`、`@ComponentScan` 注解的集合

* @SpringBootApplication 注解

  ```java
  @Inherited
  @SpringBootConfiguration	//代表 @SpringBootApplication 拥有了该注解的功能
  @EnableAutoConfiguration	//同理
  @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
  		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
  // 扫描被 @Component (@Service,@Controller)注解的 bean，容器中将排除TypeExcludeFilter 和 AutoConfigurationExcludeFilter
  public @interface SpringBootApplication { }
  ```

* @SpringBootConfiguration 注解：

  ```java
  @Configuration	// 代表是配置类
  @Indexed
  public @interface SpringBootConfiguration {
  	@AliasFor(annotation = Configuration.class)
  	boolean proxyBeanMethods() default true;
  }
  ```

  @AliasFor 注解：表示别名，可以注解到自定义注解的两个属性上表示这两个互为别名，两个属性其实是同一个含义相互替代

* @ComponentScan 注解：默认扫描当前类所在包及其子级包下的所有文件

* **@EnableAutoConfiguration 注解：启用 SpringBoot 的自动配置机制**

  ````java
  @AutoConfigurationPackage	
  @Import(AutoConfigurationImportSelector.class)
  public @interface EnableAutoConfiguration { 
  	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
      Class<?>[] exclude() default {}; 
      String[] excludeName() default {};
  }
  ````

  * @AutoConfigurationPackage：**将添加该注解的类所在的 package 作为自动配置 package 进行管理**，把启动类所在的包设置一次，为了给各种自动配置的第三方库扫描用，比如带 @Mapper 注解的类，Spring 自身是不能识别的，但自动配置的 Mybatis 需要扫描用到，而 ComponentScan 只是用来扫描注解类，并没有提供接口给三方使用

    ```java
    @Import(AutoConfigurationPackages.Registrar.class)	// 利用 Registrar 给容器中导入组件
    public @interface AutoConfigurationPackage { 
    	String[] basePackages() default {};	//自动配置包，指定了配置类的包
        Class<?>[] basePackageClasses() default {};
    }
    ```

    `register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]))`：注册 BD

    * `new PackageImports(metadata).getPackageNames()`：获取添加当前注解的类的所在包
    * `registry.registerBeanDefinition(BEAN, new BasePackagesBeanDefinition(packageNames))`：存放到容器中
      * `new BasePackagesBeanDefinition(packageNames)`：把当前主类所在的包名封装到该对象中

  * @Import(AutoConfigurationImportSelector.class)：**自动装配的核心类**

    容器刷新时执行：**invokeBeanFactoryPostProcessors()** → invokeBeanDefinitionRegistryPostProcessors() → postProcessBeanDefinitionRegistry() → processConfigBeanDefinitions() → parse() → process() → processGroupImports() → getImports() → process() → **AutoConfigurationImportSelector#getAutoConfigurationEntry()**

    ```java
    protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
        if (!isEnabled(annotationMetadata)) {
            return EMPTY_ENTRY;
        }
        // 获取注解属性，@SpringBootApplication 注解的 exclude 属性和 excludeName 属性
        AnnotationAttributes attributes = getAttributes(annotationMetadata);
        // 获取所有需要自动装配的候选项
        List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
        // 去除重复的选项
        configurations = removeDuplicates(configurations);
        // 获取注解配置的排除的自动装配类
        Set<String> exclusions = getExclusions(annotationMetadata, attributes);
        checkExcludedClasses(configurations, exclusions);
        // 移除所有的配置的不需要自动装配的类
        configurations.removeAll(exclusions);
        // 过滤，条件装配
        configurations = getConfigurationClassFilter().filter(configurations);
        // 获取 AutoConfigurationImportListener 类的监听器调用 onAutoConfigurationImportEvent 方法
        fireAutoConfigurationImportEvents(configurations, exclusions);
        // 包装成 AutoConfigurationEntry 返回
        return new AutoConfigurationEntry(configurations, exclusions);
    }
    ```

    AutoConfigurationImportSelector#getCandidateConfigurations：**获取自动配置的候选项**

    * `List<String> configurations = SpringFactoriesLoader.loadFactoryNames()`：加载自动配置类

      参数一：`getSpringFactoriesLoaderFactoryClass()`：获取 @EnableAutoConfiguration 注解类

      参数二：`getBeanClassLoader()`：获取类加载器

      * `factoryTypeName = factoryType.getName()`：@EnableAutoConfiguration 注解的全类名
      * `return loadSpringFactories(classLoaderToUse).getOrDefault()`：加载资源
        * `urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION)`：获取资源类
        * `FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories"`：**加载的资源的位置**

    * `return configurations`：返回所有自动装配类的候选项

  * 从 spring-boot-autoconfigure-2.5.3.jar/META-INF/spring.factories 文件中寻找 EnableAutoConfiguration 字段，获取自动装配类，**进行条件装配，按需装配**

    ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/SpringBoot-自动装配配置文件.png)





***



#### 装配流程

Spring Boot 通过 `@EnableAutoConfiguration` 开启自动装配，通过 SpringFactoriesLoader 加载 `META-INF/spring.factories` 中的自动配置类实现自动装配，自动配置类其实就是通过 `@Conditional` 注解按需加载的配置类，想要其生效必须引入 `spring-boot-starter-xxx` 包实现起步依赖

* SpringBoot 先加载所有的自动配置类 xxxxxAutoConfiguration
* 每个自动配置类进行**条件装配**，默认都会绑定配置文件指定的值（xxxProperties 和配置文件进行了绑定）
* SpringBoot 默认会在底层配好所有的组件，如果用户自己配置了**以用户的优先**
* **定制化配置：**
  - 用户可以使用 @Bean 新建自己的组件来替换底层的组件
  - 用户可以去看这个组件是获取的配置文件前缀值，在配置文件中修改

以 DispatcherServletAutoConfiguration 为例：

```java
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
// 类中的 Bean 默认不是单例
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
// 条件装配，环境中有 DispatcherServlet 类才进行自动装配
@ConditionalOnClass(DispatcherServlet.class)
@AutoConfigureAfter(ServletWebServerFactoryAutoConfiguration.class)
public class DispatcherServletAutoConfiguration {
	// 注册的 DispatcherServlet 的 BeanName
	public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "dispatcherServlet";

	@Configuration(proxyBeanMethods = false)
	@Conditional(DefaultDispatcherServletCondition.class)
	@ConditionalOnClass(ServletRegistration.class)
    // 绑定配置文件的属性，从配置文件中获取配置项
	@EnableConfigurationProperties(WebMvcProperties.class)
	protected static class DispatcherServletConfiguration {
		
        // 给容器注册一个 DispatcherServlet，起名字为 dispatcherServlet
		@Bean(name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
		public DispatcherServlet dispatcherServlet(WebMvcProperties webMvcProperties) {
            // 新建一个 DispatcherServlet 设置相关属性
			DispatcherServlet dispatcherServlet = new DispatcherServlet();
            // spring.mvc 中的配置项获取注入，没有就填充默认值
			dispatcherServlet.setDispatchOptionsRequest(webMvcProperties.isDispatchOptionsRequest());
			// ......
            // 返回该对象注册到容器内
			return dispatcherServlet;
		}

		@Bean
        // 容器中有这个类型组件才进行装配
		@ConditionalOnBean(MultipartResolver.class)
        // 容器中没有这个名字 multipartResolver 的组件
		@ConditionalOnMissingBean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
        // 方法名就是 BeanName
		public MultipartResolver multipartResolver(MultipartResolver resolver) {
			// 给 @Bean 标注的方法传入了对象参数，这个参数就会从容器中找，因为用户自定义了该类型，以用户配置的优先
            // 但是名字不符合规范，所以获取到该 Bean 并返回到容器一个规范的名称：multipartResolver
			return resolver;
		}
	}
}
```

```java
// 将配置文件中的 spring.mvc 前缀的属性与该类绑定
@ConfigurationProperties(prefix = "spring.mvc")	
public class WebMvcProperties { }
```





***



### 事件监听

SpringBoot 在项目启动时，会对几个监听器进行回调，可以实现监听器接口，在项目启动时完成一些操作

ApplicationContextInitializer、SpringApplicationRunListener、CommandLineRunner、ApplicationRunner

* MyApplicationRunner

  **自定义监听器的启动时机**：MyApplicationRunner 和 MyCommandLineRunner 都是当项目启动后执行，使用 @Component 放入容器即可使用

  ```java
  //当项目启动后执行run方法
  @Component
  public class MyApplicationRunner implements ApplicationRunner {
      @Override
      public void run(ApplicationArguments args) throws Exception {
          System.out.println("ApplicationRunner...run");
          System.out.println(Arrays.asList(args.getSourceArgs()));//properties配置信息
      }
  } 
  ```

* MyCommandLineRunner

  ```java
  @Component
  public class MyCommandLineRunner implements CommandLineRunner {
      @Override
      public void run(String... args) throws Exception {
          System.out.println("CommandLineRunner...run");
          System.out.println(Arrays.asList(args));
      }
  }
  ```

* MyApplicationContextInitializer 的启用要**在 resource 文件夹下添加 META-INF/spring.factories**

  ```properties
  org.springframework.context.ApplicationContextInitializer=\
  com.example.springbootlistener.listener.MyApplicationContextInitializer
  ```

  ```java
  @Component
  public class MyApplicationContextInitializer implements ApplicationContextInitializer {
      @Override
      public void initialize(ConfigurableApplicationContext applicationContext) {
          System.out.println("ApplicationContextInitializer....initialize");
      }
  }
  ```

* MySpringApplicationRunListener 的使用要添加**构造器**

  ```java
  public class MySpringApplicationRunListener implements SpringApplicationRunListener {
  	//构造器
      public MySpringApplicationRunListener(SpringApplication sa, String[] args) {
      }
  
      @Override
      public void starting() {
          System.out.println("starting...项目启动中");//输出SPRING之前
      }
  
      @Override
      public void environmentPrepared(ConfigurableEnvironment environment) {
          System.out.println("environmentPrepared...环境对象开始准备");
      }
  
      @Override
      public void contextPrepared(ConfigurableApplicationContext context) {
          System.out.println("contextPrepared...上下文对象开始准备");
      }
  
      @Override
      public void contextLoaded(ConfigurableApplicationContext context) {
          System.out.println("contextLoaded...上下文对象开始加载");
      }
  
      @Override
      public void started(ConfigurableApplicationContext context) {
          System.out.println("started...上下文对象加载完成");
      }
  
      @Override
      public void running(ConfigurableApplicationContext context) {
          System.out.println("running...项目启动完成，开始运行");
      }
  
      @Override
      public void failed(ConfigurableApplicationContext context, Throwable exception) {
          System.out.println("failed...项目启动失败");
      }
  }
  ```

  

***

## 配置文件

### 配置方式

#### 文件类型

SpringBoot 是基于约定的，很多配置都有默认值，如果想使用自己的配置替换默认配置，可以使用 application.properties 或者 application.yml（application.yaml）进行配置

* 默认配置文件名称：application
* 在同一级目录下优先级为：properties > yml > yaml

例如配置内置 Tomcat 的端口

* properties：

  ```properties
  server.port=8080
  ```

* yml：

  ```yaml
  server: port: 8080
  ```

* yaml：

  ```yaml
  server: port: 8080
  ```


***

#### 加载顺序

所有位置的配置文件都会被加载，互补配置，**高优先级配置内容会覆盖低优先级配置内容**

扫描配置文件的位置按优先级**从高到底**：

- `file:./config/`：**当前项目**下的 /config 目录下

- `file:./`：当前项目的根目录，Project工程目录

- `classpath:/config/`：classpath 的 /config 目录

- `classpath:/`：classpath 的根目录，就是 resoureces 目录

项目外部配置文件加载顺序：外部配置文件的使用是为了对内部文件的配合

* 命令行：在 package 打包后的 target 目录下，使用该命令

  ```sh
  java -jar myproject.jar --server.port=9000
  ```

* 指定配置文件位置

  ```sh
  java -jar myproject.jar --spring.config.location=e://application.properties
  ```

* 按优先级从高到底选择配置文件的加载命令

  ```sh
  java -jar myproject.jar
  ```


***

### yaml语法

基本语法：

- 大小写敏感

- **数据值前边必须有空格，作为分隔符**

- 使用缩进表示层级关系

- 缩进时不允许使用Tab键，只允许使用空格（各个系统 Tab对应空格数目可能不同，导致层次混乱）

- 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可

- ''#" 表示注释，从这个字符一直到行尾，都会被解析器忽略

  ```yaml
  server: 
  	port: 8080  
      address: 127.0.0.1
  ```

数据格式：

* 纯量：单个的、不可再分的值

  ```yaml
  msg1: 'hello \n world'  # 单引忽略转义字符
  msg2: "hello \n world"  # 双引识别转义字符
  ```

* 对象：键值对集合，Map、Hash

  ```yaml
  person:  
     name: zhangsan
     age: 20
  # 行内写法
  person: {name: zhangsan}
  ```

  注意：不建议使用 JSON，应该使用 yaml 语法

* 数组：一组按次序排列的值，List、Array

  ```yaml
  address:
    - beijing
    - shanghai
  # 行内写法
  address: [beijing,shanghai]
  ```

  ```yaml
  allPerson	#List<Person>
    - {name:lisi, age:18}
    - {name:wangwu, age:20}
  # 行内写法
  allPerson: [{name:lisi, age:18}, {name:wangwu, age:20}]
  ```

* 参数引用：

  ```yaml
  name: lisi 
  person:
    name: ${name} # 引用上边定义的name值
  ```

***

### 获取配置

三种获取配置文件的方式：

* 注解 @Value

  ```java
  @RestController
  public class HelloController {
      @Value("${name}")
      private String name;
  
      @Value("${person.name}")
      private String name2;
  
      @Value("${address[0]}")
      private String address1;
  
      @Value("${msg1}")
      private String msg1;
  
      @Value("${msg2}")
      private String msg2;
      
      @RequestMapping("/hello")
      public String hello(){
          System.out.println("所有的数据");
          return " hello Spring Boot !";
      }
  }
  ```

* Evironment 对象

  ```java
  @Autowired
  private Environment env;
  
  @RequestMapping("/hello")
  public String hello() {
      System.out.println(env.getProperty("person.name"));
      System.out.println(env.getProperty("address[0]"));
      return " hello Spring Boot !";
  }
  ```

* 注解 @ConfigurationProperties 配合 @Component 使用

  **注意**：参数 prefix 一定要指定

  ```java
  @Component	//不扫描该组件到容器内，无法完成自动装配
  @ConfigurationProperties(prefix = "person")
  public class Person {
      private String name;
      private int age;
      private String[] address;
  }
  ```

  ```java
  @Autowired
  private Person person;
  
  @RequestMapping("/hello")
  public String hello() {
      System.out.println(person);
      //Person{name='zhangsan', age=20, address=[beijing, shanghai]}
      return " hello Spring Boot !";
  }
  ```


***

### 配置提示

自定义的类和配置文件绑定一般没有提示，添加如下依赖可以使用提示：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>

<!-- 下面插件作用是工程打包时，不将spring-boot-configuration-processor打进包内，让其只在编码的时候有用 -->
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-configuration-processor</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

***

### Profile

@Profile：指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何环境下都能注册这个组件

 * 加了环境标识的 bean，只有这个环境被激活的时候才能注册到容器中，默认是 default 环境
 * 写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效
 * 没有标注环境标识的 bean 在，任何环境下都是加载的

Profile 的配置：

* **profile 是用来完成不同环境下，配置动态切换功能**

* **profile 配置方式**：多 profile 文件方式，提供多个配置文件，每个代表一种环境

  * application-dev.properties/yml 开发环境
  * application-test.properties/yml 测试环境
  * sapplication-pro.properties/yml 生产环境

* yml 多文档方式：在 yml 中使用  --- 分隔不同配置

  ```yacas
  ---
  server:
    port: 8081
  spring:
    profiles:dev
  ---
  server:
    port: 8082
  spring:
    profiles:test
  ---
  server:
    port: 8083
  spring:
    profiles:pro
  ---
  ```

* **profile 激活方式**

  * 配置文件：在配置文件中配置：spring.profiles.active=dev

    ```properties
    spring.profiles.active=dev
    ```

  * 虚拟机参数：在VM options 指定：`-Dspring.profiles.active=dev`

    ![](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/SpringBoot-profile激活方式虚拟机参数.png)

  * 命令行参数：`java –jar xxx.jar  --spring.profiles.active=dev`

    在 Program arguments 里输入，也可以先 package

***

## Web开发

### 功能支持

SpringBoot 自动配置了很多约定，大多场景都无需自定义配置

* 内容协商视图解析器 ContentNegotiatingViewResolver 和 BeanName 视图解析器 BeanNameViewResolver
* 支持静态资源（包括 webjars）和静态 index.html 页支持
* 自动注册相关类：Converter、GenericConverter、Formatter
* 内容协商处理器：HttpMessageConverters
* 国际化：MessageCodesResolver

开发规范：

* 使用 `@Configuration` + `WebMvcConfigurer` 自定义规则，不使用 `@EnableWebMvc` 注解
* 声明 `WebMvcRegistrations` 的实现类改变默认底层组件
* 使用 `@EnableWebMvc` + `@Configuration` + `DelegatingWebMvcConfiguration` 全面接管 SpringMVC

****

### 静态资源

#### 访问规则

默认的静态资源路径是 classpath 下的，优先级由高到低为：/META-INF/resources、/resources、 /static、/public  的包内，`/` 表示当前项目的根路径

静态映射 `/**` ，表示请求 `/ + 静态资源名` 就直接去默认的资源路径寻找请求的资源

处理原理：静请求去寻找 Controller 处理，不能处理的请求就会交给静态资源处理器，静态资源也找不到就响应 404 页面

* 修改默认资源路径：

  ```yaml
  spring:
    web:
      resources:
        static-locations:: [classpath:/haha/]
  ```

* 修改静态资源访问前缀，默认是 `/**`：

  ```yaml
  spring:
    mvc:
      static-path-pattern: /resources/**
  ```

  访问 URL：http://localhost:8080/resources/ + 静态资源名，将所有资源**重定位**到 `/resources/`

* webjar 访问资源：

  ```xml
  <dependency>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
      <version>3.5.1</version>
  </dependency>
  ```

  访问地址：http://localhost:8080/webjars/jquery/3.5.1/jquery.js，后面地址要按照依赖里面的包路径

****

#### 欢迎页面

静态资源路径下 index.html 默认作为欢迎页面，访问 http://localhost:8080 出现该页面，使用 welcome page 功能不能修改前缀

网页标签上的小图标可以自定义规则，把资源重命名为 favicon.ico 放在静态资源目录下即可

***

#### 源码分析

SpringMVC 功能的自动配置类 WebMvcAutoConfiguration：

```java
public class WebMvcAutoConfiguration {
    //当前项目的根路径
    private static final String SERVLET_LOCATION = "/";
}
```

* 内部类 WebMvcAutoConfigurationAdapter：

  ```java
  @Import(EnableWebMvcConfiguration.class)
  // 绑定 spring.mvc、spring.web、spring.resources 相关的配置属性
  @EnableConfigurationProperties({ WebMvcProperties.class,ResourceProperties.class, WebProperties.class })
  @Order(0)
  public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {
  	//有参构造器所有参数的值都会从容器中确定
      public WebMvcAutoConfigurationAdapter(/*参数*/) {
  			this.resourceProperties = resourceProperties.hasBeenCustomized() ? resourceProperties
  					: webProperties.getResources();
  			this.mvcProperties = mvcProperties;
  			this.beanFactory = beanFactory;
  			this.messageConvertersProvider = messageConvertersProvider;
  			this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
  			this.dispatcherServletPath = dispatcherServletPath;
  			this.servletRegistrations = servletRegistrations;
  			this.mvcProperties.checkConfiguration();
  	}
  }
  ```

  * ResourceProperties resourceProperties：获取和 spring.resources 绑定的所有的值的对象
  * WebMvcProperties mvcProperties：获取和 spring.mvc 绑定的所有的值的对象
  * ListableBeanFactory beanFactory：Spring 的 beanFactory
  * HttpMessageConverters：找到所有的 HttpMessageConverters
  * ResourceHandlerRegistrationCustomizer：找到 资源处理器的自定义器。
  * DispatcherServletPath：项目路径
  * ServletRegistrationBean：给应用注册 Servlet、Filter

* WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.addResourceHandler()：两种静态资源映射规则

  ```java
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      //配置文件设置 spring.resources.add-mappings: false，禁用所有静态资源
      if (!this.resourceProperties.isAddMappings()) {
          logger.debug("Default resource handling disabled");//被禁用
          return;
      }
      //注册webjars静态资源的映射规则	映射			路径
      addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
      //注册静态资源路径的映射规则		 默认映射 staticPathPattern = "/**" 
      addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
          //staticLocations = CLASSPATH_RESOURCE_LOCATIONS
          registration.addResourceLocations(this.resourceProperties.getStaticLocations());
          if (this.servletContext != null) {
              ServletContextResource resource = new ServletContextResource(this.servletContext, SERVLET_LOCATION);
              registration.addResourceLocations(resource);
          }
      });
  }
  ```

  ```java
  @ConfigurationProperties("spring.web")
  public class WebProperties {
      public static class Resources {
      	//默认资源路径，优先级从高到低
      	static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
                                                   "classpath:/resources/", 
                                                   "classpath:/static/", "classpath:/public/" }
          private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
          //可以进行规则重写
          public void setStaticLocations(String[] staticLocations) {
  			this.staticLocations = appendSlashIfNecessary(staticLocations);
  			this.customized = true;
  		}
      }
  }
  ```

* WebMvcAutoConfiguration.EnableWebMvcConfiguration.welcomePageHandlerMapping()：欢迎页

  ```java
  //spring.web 属性
  @EnableConfigurationProperties(WebProperties.class)
  public static class EnableWebMvcConfiguration {
      @Bean
      public WelcomePageHandlerMapping welcomePageHandlerMapping(/*参数*/) {
          WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(
              new TemplateAvailabilityProviders(applicationContext), 
              applicationContext, getWelcomePage(),
              //staticPathPattern = "/**"
              this.mvcProperties.getStaticPathPattern());
          return welcomePageHandlerMapping;
      }
  }
  WelcomePageHandlerMapping(/*参数*/) {
      //所以限制 staticPathPattern 必须为 /** 才能启用该功能
      if (welcomePage != null && "/**".equals(staticPathPattern)) {
          logger.info("Adding welcome page: " + welcomePage);
          //重定向
          setRootViewName("forward:index.html");
      }
      else if (welcomeTemplateExists(templateAvailabilityProviders, applicationContext)) {
          logger.info("Adding welcome page template: index");
          setRootViewName("index");
      }
  }
  ```

  WelcomePageHandlerMapping，访问 / 能访问到 index.html

***

### Rest映射

开启 Rest 功能

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true   #开启页面表单的Rest功能
```

源码分析，注入了 HiddenHttpMethodFilte 解析 Rest 风格的访问：

```java
public class WebMvcAutoConfiguration {
    @Bean
	@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
	@ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled")
	public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new OrderedHiddenHttpMethodFilter();
	}
}
```

详细源码解析：SpringMVC → 基本操作 → Restful → 识别原理

Web 部分源码详解：SpringMVC → 运行原理



****



### 内嵌容器

SpringBoot 嵌入式 Servlet 容器，默认支持的 WebServe：Tomcat、Jetty、Undertow

配置方式：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion> <!--必须要把内嵌的 Tomcat 容器-->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

Web 应用启动，SpringBoot 导入 Web 场景包 tomcat，创建一个 Web 版的 IOC 容器：

* `SpringApplication.run(BootApplication.class, args)`：应用启动

* `ConfigurableApplicationContext.run()`：

  * `context = createApplicationContext()`：**创建容器**

    * `applicationContextFactory = ApplicationContextFactory.DEFAULT`

      ```java
      ApplicationContextFactory DEFAULT = (webApplicationType) -> {
          try {
              switch (webApplicationType) {
                  case SERVLET:
                      // Servlet 容器，继承自 ServletWebServerApplicationContext
                      return new AnnotationConfigServletWebServerApplicationContext();
                  case REACTIVE:
                      // 响应式编程
                      return new AnnotationConfigReactiveWebServerApplicationContext();
                  default:
                      // 普通 Spring 容器
                      return new AnnotationConfigApplicationContext();
              }
          } catch (Exception ex) {
              throw new IllegalStateException();
          }
      }
      ```

    * `applicationContextFactory.create(this.webApplicationType)`：根据应用类型创建容器

  * `refreshContext(context)`：容器启动刷新

内嵌容器工作流程：

- Spring 容器启动逻辑中，在实例化非懒加载的单例 Bean 之前有一个方法 **onRefresh()**，留给子类去扩展，Web 容器就是重写这个方法创建 WebServer

  ```java
  protected void onRefresh() {
      //省略....
  	createWebServer();
  }
  private void createWebServer() {
      ServletWebServerFactory factory = getWebServerFactory();
      this.webServer = factory.getWebServer(getSelfInitializer());
      createWebServer.end();
  }
  ```

  获取 WebServer 工厂 ServletWebServerFactory，并且获取的数量不等于 1 会报错，Spring 底层有三种：

  `TomcatServletWebServerFactory`、`JettyServletWebServerFactory`、`UndertowServletWebServerFactory`

- **自动配置类 ServletWebServerFactoryAutoConfiguration** 导入了 ServletWebServerFactoryConfiguration（配置类），根据条件装配判断系统中到底导入了哪个 Web 服务器的包，创建出服务器并启动

- 默认是 web-starter 导入 tomcat 包，容器中就有 TomcatServletWebServerFactory，创建出 Tomcat 服务器并启动，

  ```java
  public TomcatWebServer(Tomcat tomcat, boolean autoStart, Shutdown shutdown) {
  	// 初始化
     	initialize();
  }
  ```

  初始化方法 initialize 中有启动方法：`this.tomcat.start()`



***



### 自定义

#### 定制规则

```java
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            //进行一些方法重写，来实现自定义的规则
            //比如添加一些解析器和拦截器，就是对原始容器功能的增加
        }
    }
    //也可以不加 @Bean，直接从这里重写方法进行功能增加
}
```



***



#### 定制容器

@EnableWebMvc：全面接管 SpringMVC，所有规则全部自己重新配置

- @EnableWebMvc + WebMvcConfigurer + @Bean  全面接管SpringMVC

- @Import(DelegatingWebMvcConfiguration.**class**)，该类继承 WebMvcConfigurationSupport，自动配置了一些非常底层的组件，只能保证 SpringMVC 最基本的使用

原理：自动配置类 **WebMvcAutoConfiguration** 里面的配置要能生效，WebMvcConfigurationSupport 类不能被加载，所以 @EnableWebMvc 导致配置类失效，从而接管了 SpringMVC

```java
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
public class WebMvcAutoConfiguration {}
```

注意：一般不适用此注解

***

## 数据访问

### JDBC

**基本使用**

导入 starter：

```xml
<!--导入 JDBC 场景-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
<!--导入 MySQL 驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <!--版本对应你的 MySQL 版本<version>5.1.49</version>-->
</dependency>
```

单独导入 MySQL 驱动是因为不确定用户使用的什么数据库

配置文件：

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.0.107:3306/db1?useSSL=false	# 不加 useSSL 会警告
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
```

测试文件：

```java
@Slf4j
@SpringBootTest
class Boot05WebAdminApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        Long res = jdbcTemplate.queryForObject("select count(*) from account_tbl", Long.class);
        log.info("记录总数：{}", res);
    }
}
```

***

**自动配置**

DataSourceAutoConfiguration：数据源的自动配置

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {
    
	@Conditional(PooledDataSourceCondition.class) 
	@ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
	@Import({ DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class,
			DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class})
	protected static class PooledDataSourceConfiguration {}
}
// 配置项
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties implements BeanClassLoaderAware, InitializingBean {}
```

- 底层默认配置好的连接池是：**HikariDataSource**
- 数据库连接池的配置，是容器中没有 DataSource 才自动配置的
- 修改数据源相关的配置：spring.datasource

相关配置：

- DataSourceTransactionManagerAutoConfiguration： 事务管理器的自动配置
- JdbcTemplateAutoConfiguration： JdbcTemplate 的自动配置
  - 可以修改这个配置项 @ConfigurationProperties(prefix = **"spring.jdbc"**) 来修改JdbcTemplate
  - `@AutoConfigureAfter(DataSourceAutoConfiguration.class)`：在 DataSource 装配后装配
- JndiDataSourceAutoConfiguration： jndi 的自动配置
- XADataSourceAutoConfiguration： 分布式事务相关

****

### Druid

导入坐标：

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.17</version>
</dependency>
```

```java
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
    DruidStatViewServletConfiguration.class,
    DruidWebStatFilterConfiguration.class,
    DruidFilterConfiguration.class})
public class DruidDataSourceAutoConfigure {}
```

自动配置：

- 扩展配置项 **spring.datasource.druid**
- DruidSpringAopConfiguration： 监控 SpringBean，配置项为 `spring.datasource.druid.aop-patterns`

- DruidStatViewServletConfiguration：监控页的配置项为 `spring.datasource.druid.stat-view-servlet`，默认开启
- DruidWebStatFilterConfiguration：Web 监控配置项为 `spring.datasource.druid.web-stat-filter`，默认开启

- DruidFilterConfiguration：所有 Druid 自己 filter 的配置

配置示例：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_account
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver

    druid:
      aop-patterns: com.atguigu.admin.*  #监控SpringBean
      filters: stat,wall     # 底层开启功能，stat（sql监控），wall（防火墙）

      stat-view-servlet:   # 配置监控页功能
        enabled: true
        login-username: admin	#项目启动访问：http://localhost:8080/druid ，账号和密码是 admin
        login-password: admin
        resetEnable: false

      web-stat-filter:  # 监控web
        enabled: true
        urlPattern: /*
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'


      filter:
        stat:    # 对上面filters里面的stat的详细配置
          slow-sql-millis: 1000
          logSlowSql: true
          enabled: true
        wall:
          enabled: true
          config:
            drop-table-allow: false
```



配置示例：https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter

配置项列表：https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8

****

### MyBatis

**基本使用**

导入坐标：

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.4</version>
</dependency>
```

* 编写 MyBatis 相关配置：application.yml

  ```yaml
  # 配置mybatis规则
  mybatis:
  #  config-location: classpath:mybatis/mybatis-config.xml  建议不写
    mapper-locations: classpath:mybatis/mapper/*.xml
    configuration:
      map-underscore-to-camel-case: true
      
   #可以不写全局配置文件，所有全局配置文件的配置都放在 configuration 配置项中即可
  ```

* 定义表和实体类

  ```java
  public class User {
      private int id;
      private String username;
      private String password;
  }
  ```

* 编写 dao 和 mapper 文件/纯注解开发

  dao：**@Mapper 注解必须加，使用自动装配的 package，否则在启动类指定 @MapperScan() 扫描路径（不建议）**

  ```java
  @Mapper  //必须加Mapper
  @Repository
  public interface UserXmlMapper {
      public List<User> findAll();
  }
  ```

  mapper.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.seazean.springbootmybatis.mapper.UserXmlMapper">
      <select id="findAll" resultType="user">
          select * from t_user
      </select>
  </mapper>
  ```

* 纯注解开发

  ```java
  @Mapper
  @Repository
  public interface UserMapper {
      @Select("select * from t_user")
      public List<User> findAll();
  }
  ```

***

**自动配置**

MybatisAutoConfiguration：

```java
@EnableConfigurationProperties(MybatisProperties.class)	//MyBatis配置项绑定类。
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration {
    @Bean
  	@ConditionalOnMissingBean
  	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    	SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        return factory.getObject();
    }
    
    @org.springframework.context.annotation.Configuration
   	@Import(AutoConfiguredMapperScannerRegistrar.class)
   	@ConditionalOnMissingBean({ MapperFactoryBean.class, MapperScannerConfigurer.class })
   	public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {}
}

@ConfigurationProperties(prefix = "mybatis")
public class MybatisProperties {}
```

* 配置文件：`mybatis`
* 自动配置了 SqlSessionFactory
* 导入 `AutoConfiguredMapperScannerRegistra` 实现 @Mapper 的扫描

***

**MyBatis**-**Plus**

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
```

自动配置类：MybatisPlusAutoConfiguration 

只需要 Mapper 继承 **BaseMapper** 就可以拥有 CRUD 功能

***

### Redis

**基本使用**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

* 配置redis相关属性

  ```yaml
  spring:
    redis:
      host: 127.0.0.1 # redis的主机ip
      port: 6379
  ```

* 注入 RedisTemplate 模板

  ```java
  @RunWith(SpringRunner.class)
  @SpringBootTest
  public class SpringbootRedisApplicationTests {
      @Autowired
      private RedisTemplate redisTemplate;
  
      @Test
      public void testSet() {
          //存入数据
          redisTemplate.boundValueOps("name").set("zhangsan");
      }
      @Test
      public void testGet() {
          //获取数据
          Object name = redisTemplate.boundValueOps("name").get();
          System.out.println(name);
      }
  }
  ```

***

**自动配置**

RedisAutoConfiguration 自动配置类

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
```

- 配置项：`spring.redis`
- 自动导入了连接工厂配置类：LettuceConnectionConfiguration、JedisConnectionConfiguration

- 自动注入了模板类：RedisTemplate<Object, Object> 、StringRedisTemplate，k v 都是 String 类型

- 使用 @Autowired 注入模板类就可以操作 redis

****

## 单元测试

### Junit5

Spring Boot 2.2.0 版本开始引入 JUnit 5 作为单元测试默认库，由三个不同的子模块组成：

* JUnit Platform：在 JVM 上启动测试框架的基础，不仅支持 Junit 自制的测试引擎，其他测试引擎也可以接入

* JUnit Jupiter：提供了 JUnit5 的新的编程模型，是 JUnit5 新特性的核心，内部包含了一个测试引擎，用于在 Junit Platform 上运行

* JUnit Vintage：JUnit Vintage 提供了兼容 JUnit4.x、Junit3.x 的测试引擎

  注意：SpringBoot 2.4 以上版本移除了默认对 Vintage 的依赖，如果需要兼容 Junit4 需要自行引入

```java
@SpringBootTest
class Boot05WebAdminApplicationTests {
    @Test
    void contextLoads() { }
}
```

***

### 常用注解

JUnit5 的注解如下：

- @Test：表示方法是测试方法，但是与 JUnit4 的 @Test 不同，它的职责非常单一不能声明任何属性，拓展的测试将会由 Jupiter 提供额外测试，包是 `org.junit.jupiter.api.Test`
- @ParameterizedTest：表示方法是参数化测试

- @RepeatedTest：表示方法可重复执行
- @DisplayName：为测试类或者测试方法设置展示名称

- @BeforeEach：表示在每个单元测试之前执行
- @AfterEach：表示在每个单元测试之后执行

- @BeforeAll：表示在所有单元测试之前执行
- @AfterAll：表示在所有单元测试之后执行

- @Tag：表示单元测试类别，类似于 JUnit4 中的 @Categories
- @Disabled：表示测试类或测试方法不执行，类似于 JUnit4 中的 @Ignore

- @Timeout：表示测试方法运行如果超过了指定时间将会返回错误
- @ExtendWith：为测试类或测试方法提供扩展类引用

****

### 断言机制

**简单断言**

断言（assertions）是测试方法中的核心，用来对测试需要满足的条件进行验证，断言方法都是 org.junit.jupiter.api.Assertions 的静态方法

用来对单个值进行简单的验证：

| 方法            | 说明                                 |
| --------------- | ------------------------------------ |
| assertEquals    | 判断两个对象或两个原始类型是否相等   |
| assertNotEquals | 判断两个对象或两个原始类型是否不相等 |
| assertSame      | 判断两个对象引用是否指向同一个对象   |
| assertNotSame   | 判断两个对象引用是否指向不同的对象   |
| assertTrue      | 判断给定的布尔值是否为 true          |
| assertFalse     | 判断给定的布尔值是否为 false         |
| assertNull      | 判断给定的对象引用是否为 null        |
| assertNotNull   | 判断给定的对象引用是否不为 null      |

```java
@Test
@DisplayName("simple assertion")
public void simple() {
     assertEquals(3, 1 + 2, "simple math");
     assertNull(null);
     assertNotNull(new Object());
}
```

***

**数组断言**

通过 assertArrayEquals 方法来判断两个对象或原始类型的数组是否相等

```java
@Test
@DisplayName("array assertion")
public void array() {
 	assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
}
```

***

**组合断言**

assertAll 方法接受多个 org.junit.jupiter.api.Executable 函数式接口的实例作为验证的断言，可以通过 lambda 表达式提供这些断言

```java
@Test
@DisplayName("assert all")
public void all() {
	assertAll("Math",
              () -> assertEquals(2, 1 + 1),
              () -> assertTrue(1 > 0)
   	);
}
```

***

**异常断言**

Assertions.assertThrows()，配合函数式编程就可以进行使用

```java
@Test
@DisplayName("异常测试")
public void exceptionTest() {
    ArithmeticException exception = Assertions.assertThrows(
        //扔出断言异常
		ArithmeticException.class, () -> System.out.println(1 / 0)
    );
}
```

***

**超时断言**

Assertions.assertTimeout() 为测试方法设置了超时时间

```java
@Test
@DisplayName("超时测试")
public void timeoutTest() {
    //如果测试方法时间超过1s将会异常
    Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
}
```

***

**快速失败**

通过 fail 方法直接使得测试失败

```java
@Test
@DisplayName("fail")
public void shouldFail() {
	fail("This should fail");
}
```

***

### 前置条件

JUnit 5 中的前置条件（assumptions）类似于断言，不同之处在于**不满足的断言会使得测试方法失败**，而不满足的**前置条件只会使得测试方法的执行终止**，前置条件可以看成是测试方法执行的前提，当该前提不满足时，就没有继续执行的必要

```java
@DisplayName("测试前置条件")
@Test
void testassumptions(){
    Assumptions.assumeTrue(false,"结果不是true");
    System.out.println("111111");

}
```

***

### 嵌套测试

JUnit 5 可以通过 Java 中的内部类和 @Nested 注解实现嵌套测试，从而可以更好的把相关的测试方法组织在一起，在内部类中可以使用 @BeforeEach 和 @AfterEach 注解，而且嵌套的层次没有限制

```java
@DisplayName("A stack")
class TestingAStackDemo {

    Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        assertNull(stack)
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }
    }
}
```



### 参数测试

参数化测试是 JUnit5 很重要的一个新特性，它使得用不同的参数多次运行测试成为了可能

利用**@ValueSource**等注解，指定入参，我们将可以使用不同的参数进行多次单元测试，而不需要每新增一个参数就新增一个单元测试，省去了很多冗余代码。

* @ValueSource：为参数化测试指定入参来源，支持八大基础类以及 String 类型、Class 类型

* @NullSource：表示为参数化测试提供一个 null 的入参

* @EnumSource：表示为参数化测试提供一个枚举入参

* @CsvFileSource：表示读取指定 CSV 文件内容作为参数化测试入参

* @MethodSource：表示读取指定方法的返回值作为参数化测试入参（注意方法返回需要是一个流）





***





## 指标监控

### Actuator

每一个微服务在云上部署以后，都需要对其进行监控、追踪、审计、控制等，SpringBoot 抽取了 Actuator 场景，使得每个微服务快速引用即可获得生产级别的应用监控、审计等功能

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

暴露所有监控信息为 HTTP：

```yaml
management:
  endpoints:
    enabled-by-default: true #暴露所有端点信息
    web:
      exposure:
        include: '*'  #以web方式暴露
```

访问 http://localhost:8080/actuator/[beans/health/metrics/]

可视化界面：https://github.com/codecentric/spring-boot-admin



****



### Endpoint

默认所有的 Endpoint 除过 shutdown 都是开启的

```yaml
management:
  endpoints:
    enabled-by-default: false	#禁用所有的
  endpoint:						#手动开启一部分
    beans:
      enabled: true
    health:
      enabled: true
```

端点：

| ID                 | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| `auditevents`      | 暴露当前应用程序的审核事件信息。需要一个 `AuditEventRepository` 组件 |
| `beans`            | 显示应用程序中所有 Spring Bean 的完整列表                    |
| `caches`           | 暴露可用的缓存                                               |
| `conditions`       | 显示自动配置的所有条件信息，包括匹配或不匹配的原因           |
| `configprops`      | 显示所有 `@ConfigurationProperties`                          |
| `env`              | 暴露 Spring 的属性 `ConfigurableEnvironment`                 |
| `flyway`           | 显示已应用的所有 Flyway 数据库迁移。 需要一个或多个 Flyway 组件。 |
| `health`           | 显示应用程序运行状况信息                                     |
| `httptrace`        | 显示 HTTP 跟踪信息，默认情况下 100 个 HTTP 请求-响应需要一个 `HttpTraceRepository` 组件 |
| `info`             | 显示应用程序信息                                             |
| `integrationgraph` | 显示 Spring integrationgraph，需要依赖 `spring-integration-core` |
| `loggers`          | 显示和修改应用程序中日志的配置                               |
| `liquibase`        | 显示已应用的所有 Liquibase 数据库迁移，需要一个或多个 Liquibase 组件 |
| `metrics`          | 显示当前应用程序的指标信息。                                 |
| `mappings`         | 显示所有 `@RequestMapping` 路径列表                          |
| `scheduledtasks`   | 显示应用程序中的计划任务                                     |
| `sessions`         | 允许从 Spring Session 支持的会话存储中检索和删除用户会话，需要使用 Spring Session 的基于 Servlet 的 Web 应用程序 |
| `shutdown`         | 使应用程序正常关闭，默认禁用                                 |
| `startup`          | 显示由 `ApplicationStartup` 收集的启动步骤数据。需要使用 `SpringApplication` 进行配置 `BufferingApplicationStartup` |
| `threaddump`       | 执行线程转储                                                 |

应用程序是 Web 应用程序（Spring MVC，Spring WebFlux 或 Jersey），则可以使用以下附加端点：

| ID           | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| `heapdump`   | 返回 `hprof` 堆转储文件。                                    |
| `jolokia`    | 通过 HTTP 暴露 JMX bean（需要引入 Jolokia，不适用于 WebFlux），需要引入依赖 `jolokia-core` |
| `logfile`    | 返回日志文件的内容（如果已设置 `logging.file.name` 或 `logging.file.path` 属性），支持使用 HTTP Range标头来检索部分日志文件的内容。 |
| `prometheus` | 以 Prometheus 服务器可以抓取的格式公开指标，需要依赖 `micrometer-registry-prometheus` |

常用 Endpoint：

- Health：监控状况
- Metrics：运行时指标

- Loggers：日志记录





***





## 项目部署

SpringBoot 项目开发完毕后，支持两种方式部署到服务器：

* jar 包 (官方推荐，默认)
* war 包

**更改 pom 文件中的打包方式为 war**

* 修改启动类

  ```java
  @SpringBootApplication
  public class SpringbootDeployApplication extends SpringBootServletInitializer {
      public static void main(String[] args) {
          SpringApplication.run(SpringbootDeployApplication.class, args);
      }
  
      @Override
      protected SpringApplicationBuilder configure(SpringApplicationBuilder b) {
          return b.sources(SpringbootDeployApplication.class);
      }
  }
  ```

* 指定打包的名称

  ```xml
  <packaging>war</packaging>
  <build>
       <finalName>springboot</finalName>
       <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>
       </plugins>
  </build>
  ```

# SpringBoot 八股文

### SpringBoot的自动配置原理是什么？

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/4fb9e8af-89bd-4652-bb07-64e21c913403.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/f31283ff-c51b-4e56-9eb9-81e3d409461b.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/0e63982c-54e1-4053-8d2e-21db97282498.png)

### Springboot的常见注解

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/c2c9e450-de35-4977-8737-f50a01ad7d36.png)

