# SpringCloud

## 基本介绍

SpringCloud 是分布式微服务的一站式解决方案，是多种微服务落地技术的集合体，俗称微服务全家桶

![Cloud-组件概览](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-组件概览.png)



参考文档：https://www.yuque.com/mrlinxi/pxvr4g/wcwd39

***

## 服务注册

### Eureka

#### 基本介绍

Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务治理。Eureka 采用了 CS(Client-Server) 的设计架构，Eureka Server 是服务注册中心，系统中的其他微服务使用 Eureka 的客户端连接到 Eureka Server 并维持心跳连接

![Cloud-Eureka和Dubbo对比](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Eureka和Dubbo对比.png)

* Eureka Server 提供服务注册服务：各个微服务节点通过配置启动后，会在 EurekaServer 中进行注册，EurekaServer 中的服务注册表中将会存储所有可用服务节点的信息，并且具有可视化界面

* Eureka Client 通过注册中心进行访问：用于简化 Eureka Server的交互，客户端也具备一个内置的、使用轮询 (round-robin) 负载算法的负载均衡器。在应用启动后将会向 Eureka Server 发送心跳（默认周期为30秒），如果 Eureka Server 在多个心跳周期内没有接收到某个节点的心跳，将会从服务注册表中把这个服务节点移除（默认 90 秒）

****

#### 服务端

服务器端主启动类增加 @EnableEurekaServer 注解，指定该模块作为 Eureka 注册中心的服务器

构建流程如下：

* 主启动类

  ```java
  @SpringBootApplication
  @EnableEurekaServer  // 表示当前是Eureka的服务注册中心
  public class EurekaMain7001 {
      public static void main(String[] args) {
          SpringApplication.run(EurekaMain7001.class, args);
      }
  }
  ```

* 修改 pom 文件

  ```xml
  1.x:    server跟client合在一起
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-eureka</artifactId>
  </dependency>
  2.x： server跟client分开
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  ```

* 修改  application.yml 文件

  ```yaml
  server:
    port: 7001
  
  eureka:
    instance:
      hostname: localhost # eureka服务端的实例名称
    client:
      # false表示不向注册中心注册自己。
      register-with-eureka: false
      # false表示自己端就是注册中心，职责就是维护服务实例，并不需要去检索服务
      fetch-registry: false
    service-url:
      # 设置与 Eureka Server 交互的地址查询服务和注册服务都需要依赖这个地址。
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  ```

* 游览器访问 http://localhost:7001



#### 客户端

**生产者**

服务器端主启动类需要增加 @EnableEurekaClient 注解，表示这是一个 Eureka 客户端，要注册进 EurekaServer 中

* 主启动类：PaymentMain8001

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  public class PaymentMain8001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentMain8001.class, args);
      }
  }
  ```

* 修改 pom 文件：添加一个 Eureka-Client 依赖

  ```xml
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  </dependency>
  ```

* 写 yml 文件

  ```yaml
  server:
    port: 8001
   
  eureka:
    client:
      # 表示将自己注册进EurekaServer默认为true
      register-with-eureka: true
      # 表示可以从Eureka抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
      fetch-registry: true
      service-url: 
        defaultZone: http://localhost:7001/eureka
    instance:
      instance-id: payment8001 # 只暴露服务名，不带有主机名
      prefer-ip-address: true  # 访问信息有 IP 信息提示(鼠标停留在服务名称上时)
  ```

* 游览器访问 http://localhost:7001

***

**消费者**

* 主启动类：PaymentMain8001

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  @EnableDiscoveryClient
  public class PaymentMain8001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentMain8001.class, args);
      }
  }
  ```

* pom 文件同生产者

* 写 yml 文件

  ```yaml
  server:
    port: 80
  
  # 微服务名称
  spring:
   application:
    name: cloud-order-service
  eureka:
    client:
      register-with-eureka: true
      fetch-registry: true
      service-url: 
        defaultZone: http://localhost:7001/eureka
  ```

* 浏览器访问 http://localhost:7001

  ![Cloud-Eureka可视化界面](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Eureka可视化界面.png)



***



#### 集群构建

**服务端**

Server 端高可用集群原理：实现负载均衡和故障容错，互相注册，相互守望

![Cloud-Eureka集群原理](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Eureka集群原理.png)

多台 Eureka 服务器，每一台 Eureka 服务器需要有自己的主机名，同时各服务器需要相互注册

* Eureka1：

  ```yaml
  server:
    port: 7001
  
  eureka:
    instance:
      hostname: eureka7001.com
    client:
      register-with-eureka: false
      fetch-registry: false
      service-url:
      # 设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址。
        # 单机就是自己
        # defaultZone: http://eureka7001.com:7001/eureka/
        # 集群指向其他eureka
        #defaultZone: http://eureka7002.com:7002/eureka/
        # 写成这样可以直接通过可视化页面跳转到7002
        defaultZone: http://eureka7002.com:7002/
  ```

* Eureka2：

  ```yaml
  server:
    port: 7002
  
  eureka:
    instance:
      hostname: eureka7002.com
    client:
      register-with-eureka: false
      fetch-registry: false
      service-url:
        #写成这样可以直接通过可视化页面跳转到7001
        defaultZone: http://eureka7001.com:7001/
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableEurekaServer
  public class EurekaMain7002 {
      public static void main(String[] args) {
          SpringApplication.run(EurekaMain7002.class, args);
      }
  }
  ```

* 访问 http://eureka7001.com:7001 和 http://eureka7002.com:7002：

  ![Cloud-EurekaServer集群构建成功](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-EurekaServer集群构建成功.png)

* RPC 调用：controller.OrderController

  ```java
  @RestController
  @Slf4j
  public class OrderController {
      public static final String PAYMENT_URL = "http://localhost:8001";
  
      @Autowired
      private RestTemplate restTemplate;
  	
      // CommonResult 是一个公共的返回类型
      @GetMapping("/consumer/payment/get/{id}")
      public CommonResult<Payment> getPayment(@PathVariable("id") long id) {
          // 返回对象为响应体中数据转化成的对象，基本上可以理解为JSON
          return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
      }
  }
  ```

  

**生产者**

构建 PaymentMain8001 的服务集群

* 主启动类

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  @EnableDiscoveryClient
  public class PaymentMain8002 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentMain8002.class, args);
      }
  }
  ```

* 写 yml 文件：端口修改，并且 spring.application.name 均为 cloud-payment-service

  ```yaml
  server:
    port: 8002
  
  spring:
    application:
      name: cloud-payment-service
      
  eureka:
    client:
      # 表示将自己注册进EurekaServer默认为true
      register-with-eureka: true
      # 表示可以从Eureka抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
      fetch-registry: true
      service-url: 
        defaultZone: http://localhost:7001/eureka
  ```

***

**负载均衡**

消费者端的 Controller

```java
// public static final String PAYMENT_URL = "http://localhost:8001";
public static final String PAYMENT_URL = "http://localhost:8002";
```

由于已经建立了生产者集群，所以可以进行负载均衡的操作：

* Controller：只修改 PAYMENT_URL 会报错，因为 CLOUD-PAYMENT-SERVICE 对应多个微服务，需要规则来判断调用哪个端口

  ```java
  public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
  ```

* 使用 @LoadBlanced 注解赋予 RestTemplate 负载均衡的能力，增加 config.ApplicationContextConfig 文件：

  ```java
  @Configuration
  public class ApplicationContextConfig {
      @Bean
      @LoadBalanced
      public RestTemplate getRestTemplate() {
          return new RestTemplate();
      }
  }
  ```



****



#### 服务发现

服务发现：对于注册进 Eureka 里面的微服务，可以通过服务发现来获得该服务的信息

* 主启动类增加注解 @EnableDiscoveryClient：

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  @EnableDiscoveryClient
  public class PaymentMain8001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentMain8001.class, args);
      }
  }
  ```

* 修改生产者的 Controller

  ```java
  @RestController
  @Slf4j
  public class PaymentController {
      @Autowired
      private DiscoveryClient discoveryClient;
      
      @GetMapping(value = "/payment/discovery")
      public Object discovery() {
          List<String> services = discoveryClient.getServices();
          for (String service : services) {
              log.info("**** element:" + service);
          }
  
          List<ServiceInstance> instances = discoveryClient.getInstances("PAYMENT-SERVICE");
          for (ServiceInstance instance : instances) {
              log.info(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort());
          }
          return this.discoveryClient;
      }
  }
  ```



***



#### 自我保护

保护模式用于客户端和 EurekaServer 之间存在网络分区场景下的保护，一旦进入保护模式 EurekaServer 将会尝试保护其服务注册表中的信息，不在删除服务注册表中的数据，属于 CAP 里面的 AP 思想（可用性和分区容错性）

![Cloud-Eureka自我保护机制](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Eureka自我保护机制.png)

如果一定时间内丢失大量该微服务的实例，这时 Eureka 就会开启自我保护机制，不会剔除该服务。 因为这个现象可能是因为网络暂时不通，出现了 Eureka 的假死、拥堵、卡顿，客户端恢复后还能正常发送心跳

禁止自我保护：

* Server：

  ```yaml
  eureka:
    server:
      # 关闭自我保护机制，不可用的服务直接删除
      enable-self-preservation: false
      eviction-interval-timer-in-ms: 2000
  ```

* Client：

  ```yaml
  eureka:
    instance:
      # Eureka客户端向服务端发送心跳的时间间隔默认30秒 
      lease-renewal-interval-in-seconds: 1
      # Eureka服务端在收到最后一次心跳后，90s没有收到心跳，剔除服务
      lease-expiration-duration-in-seconds: 2
  ```





****









## 服务调用

### Ribbon

#### 基本介绍

SpringCloud Ribbon 是基于 Netflix Ribbon 实现的一套负载均衡工具，提供客户端的软件负载均衡算法和服务调用，Ribbon 客户端组件提供一系列完善的配置项如连接超时，重试等

官网： https://github.com/Netflix/ribbon/wiki/Getting-Started （已进入维护模式，未来替换为 Load Banlancer）

负载均衡 Load Balance (LB) 就是将用户的请求平摊的分配到多个服务上，从而达到系统的 HA（高可用）

**常见的负载均衡算法：**

- 轮询：为请求选择健康池中的第一个后端服务器，然后按顺序往后依次选择

- 最小连接：优先选择连接数最少，即压力最小的后端服务器，在会话较长的情况下可以采取这种方式

- 散列：根据请求源的 IP 的散列（hash）来选择要转发的服务器，可以一定程度上保证特定用户能连接到相同的服务器，如果应用需要处理状态而要求用户能连接到和之前相同的服务器，可以采取这种方式

Ribbon 本地负载均衡客户端与 Nginx 服务端负载均衡区别：

- Nginx 是服务器负载均衡，客户端所有请求都会交给 Nginx，然后由 Nginx 实现转发请求，即负载均衡是由服务端实现的
- Ribbon 本地负载均衡，在调用微服务接口时会在注册中心上获取注册信息服务列表，然后缓存到 JVM 本地，从而在本地实现 RPC 远程服务调用技术

集中式 LB 和进程内 LB 的对比：

* 集中式 LB：在服务的消费方和提供方之间使用独立的 LB 设施（如 Nginx），由该设施把访问请求通过某种策略转发至服务的提供方
* 进程内 LB：将 LB 逻辑集成到消费方，消费方从服务注册中心获知有哪些服务可用，然后从中选择出一个服务器，Ribbon 属于该类



***



#### 工作流程

Ribbon 是一个软负载均衡的客户端组件

![Cloud-Ribbon架构原理](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Ribbon架构原理.png)

- 第一步先选择 EurekaServer，优先选择在同一个区域内负载较少的 Server
- 第二步根据用户指定的策略，再从 Server 取到的服务注册列表中选择一个地址



***



#### 核心组件

Ribbon 核心组件 IRule 接口，主要实现类：

- RoundRobinRule：轮询
- RandomRule：随机
- RetryRule：先按照 RoundRobinRule 的策略获取服务，如果获取服务失败则在指定时间内会进行重试
- WeightedResponseTimeRule：对 RoundRobinRule 的扩展，响应速度越快的实例选择权重越大，越容易被选择
- BestAvailableRule：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
- AvailabilityFilteringRule：先过滤掉故障实例，再选择并发较小的实例
- ZoneAvoidanceRule：默认规则，复合判断 Server 所在区域的性能和 Server 的可用性选择服务器

![Cloud-IRule类图](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-IRule类图.png)

注意：官方文档明确给出了警告，自定义负载均衡配置类不能放在 @ComponentScan 所扫描的当前包下以及子包下

更换负载均衡算法方式：

* 自定义负载均衡配置类 MySelfRule：

  ```java
  @Configuration
  public class MySelfRule {
      @Bean
      public IRule myRule() {
          return new RandomRule();//定义为随机负载均衡算法
      }
  }
  ```

* 主启动类添加 @RibbonCilent 注解

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  // 指明访问的服务CLOUD-PAYMENT-SERVICE，以及指定负载均衡策略
  @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration= MySelfRule.class)
  public class OrderMain80 {
      public static void main(String[] args) {
          SpringApplication.run(OrderMain80.class, args);
      }
  }
  ```


****

### OpenFeign

#### 基本介绍

Feign 是一个声明式 WebService 客户端，能让编写 Web 客户端更加简单，只要创建一个接口并添加注解 @Feign 即可，可以与 Eureka 和 Ribbon 组合使用支持负载均衡，所以一般**用在消费者端**

OpenFeign 在 Feign 的基础上支持了 SpringMVC 注解，并且 @FeignClient 注解可以解析 @RequestMapping 注解下的接口，并通过动态代理的方式产生实现类，在实现类中做负载均衡和服务调用

优点：利用 RestTemplate 对 HTTP 请求的封装处理，形成了一套模版化的调用方法。但是对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以一个微服务接口上面标注一个 @Feign 注解，就可以完成包装依赖服务的调用

****

#### 基本使用

@FeignClient("provider name") 注解使用规则：

* 声明的方法签名必须和 provider 微服务中的 controller 中的方法签名一致
* 如果需要传递参数，那么 `@RequestParam` 、`@RequestBody` 、`@PathVariable` 也需要加上

改造消费者服务

* 引入 pom 依赖：OpenFeign 整合了 Ribbon，具有负载均衡的功能

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
  ```

* application.yml：不将其注册到 Eureka 作为微服务

  ```yaml
  server:
    port: 80
  
  eureka:
    client:
      # 表示不将其注入Eureka作为微服务，不作为Eureak客户端了，而是作为Feign客户端
      register-with-eureka: false
      service-url:
        # 集群版
        defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
  ```

* 主启动类：开启 Feign

  ```java
  @SpringBootApplication
  @EnableFeignClients //不作为Eureak客户端了，而是作为Feign客户端
  public class OrderOpenFeignMain80 {
      public static void main(String[] args) {
          SpringApplication.run(OrderOpenFeignMain80.class, args);
      }
  }
  ```
  
* 新建 Service 接口：PaymentFeignService 接口和 @FeignClient 注解，完成 Feign 的包装调用

  ```java
  @Component
  @FeignClient(value = "CLOUD-PAYMENT-SERVICE") // 作为一个Feign功能绑定的的接口
  public interface PaymentFeignService {
      @GetMapping(value = "/payment/get/{id}")
      public CommonResult<Payment> getPaymentById(@PathVariable("id") long id);
      
      @GetMapping("/payment/feign/timeout")
      public String paymentFeignTimeout();
  }
  ```

* Controller：

  ```java
  @RestController
  @Slf4j
  public class OrderFeignController {
      @Autowired
      private PaymentFeignService paymentFeignService;
  
      @GetMapping("/consumer/payment/get/{id}")
      public CommonResult<Payment> getPayment(@PathVariable("id") long id) {
          // 返回对象为响应体中数据转化成的对象，基本上可以理解为JSON
          return paymentFeignService.getPaymentById(id);
      }
      
      @GetMapping("/consumer/payment/feign/timeout")
      public String paymentFeignTimeout() {
          // openfeign-ribbon，客户端一般默认等待1s
          return paymentFeignService.paymentFeignTimeout();
      }
  }
  ```

***

#### 超时问题

Feign 默认是支持 Ribbon，Feign 客户端的负载均衡和超时控制都由 Ribbon 控制

设置 Feign 客户端的超时等待时间：

```yaml
ribbon:
  #指的是建立连接后从服务器读取到可用资源所用的时间
  ReadTimeout: 5000
  #指的是建立连接所用的时间，适用于网络状况正常的情况下,两端连接所用的时间
  ConnectTimeout: 5000
```

演示超时现象：OpenFeign 默认等待时间为 1 秒钟，超过后会报错

* 服务提供方 Controller：

  ```java
  @GetMapping("/payment/feign/timeout")
  public String paymentFeignTimeout() {
      try {
          TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      return serverPort;
  }
  ```

* 消费者 PaymentFeignService 和 OrderFeignController 参考上一小节代码

* 测试报错：

  ![Cloud-OpenFeign超时错误](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-OpenFeign超时错误.png)!](C:\Users\Seazean\Desktop\123\Cloud-OpenFeign超时错误.png)



***



#### 日志级别

Feign 提供了日志打印功能，可以通过配置来调整日志级别，从而了解 Feign 中 HTTP 请求的细节

| NONE    | 默认的，不显示任何日志                                    |
| ------- | --------------------------------------------------------- |
| BASIC   | 仅记录请求方法、URL、响应状态码及执行时间                 |
| HEADERS | 除了 BASIC 中定义的信息之外，还有请求和响应的头信息       |
| FULL    | 除了 HEADERS 中定义的信息外，还有请求和响应的正文及元数据 |

配置在消费者端

* 新建 config.FeignConfig 文件：配置日志 Bean

  ```java
  @Configuration
  public class FeignConfig {
      @Bean
      Logger.Level feignLoggerLevel() {
          return Logger.Level.FULL;
      }
  }
  ```

* application.yml：

  ```yaml
  logging:
    level:
      # feign 日志以什么级别监控哪个接口
      com.atguigu.springcloud.service.PaymentFeignService: debug
  ```

* Debug 后查看后台日志

****

## 服务熔断

### Hystrix

#### 基本介绍

Hystrix 是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖会出现调用失败，比如超时、异常等，Hystrix 能够保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性

断路器本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期的、可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间地占用，避免了故障在分布式系统中的蔓延，乃至雪崩

* 服务降级 Fallback：系统不可用时需要一个兜底的解决方案或备选响应，向调用方返回一个可处理的响应
* 服务熔断 Break：达到最大服务访问后，直接拒绝访问
* 服务限流 Flowlimit：高并发操作时严禁所有请求一次性过来拥挤，一秒钟 N 个，有序排队进行

官方文档：https://github.com/Netflix/Hystrix/wiki/How-To-Use

****



#### 服务降级

##### 案例构建

生产者模块：

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
  </dependency>
  ```

* 主启动类：开启 Feign

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  @EnableCircuitBreaker // 降级使用
  public class PaymentHystrixMain8001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentHystrixMain8001.class, args);
      }
  }
  ```

* Controller：

  ```java
  @RestController
  @Slf4j
  public class PaymentController {
      @Resource
      private PaymentService paymentService;
      @Value("${server.port}")
      private String serverPort;
  
      // 正常访问
      @GetMapping("/payment/hystrix/ok/{id}")
      private String paymentInfo_Ok(@PathVariable("id") Integer id) {
          return paymentService.paymentInfo_Ok(id);
      }
  	// 超时
      @GetMapping("/payment/hystrix/timeout/{id}")
      private String paymentInfo_Timeout(@PathVariable("id") Integer id) {
          // service 层有 Thread.sleep() 操作，保证超时
          return paymentService.paymentInfo_Timeout(id);
      }
  }
  ```

* Service：

  ```java
  @Service
  public class PaymentService {
      public String paymentInfo_Ok(Integer id) {
          return "线程池: " + Thread.currentThread().getName() + "paymentInfo_OK, id:  " + id";
      }
  
      public String paymentInfo_Timeout(Integer id) {
          int timeNumber = 3;
          try {
              TimeUnit.SECONDS.sleep(timeNumber);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          return "线程池: " + Thread.currentThread().getName() + " payment_Timeout, id:  " + id;
      }
  }
  ```

* jmeter 压测两个接口，发现接口 paymentInfo_Ok 也变的卡顿

消费者模块：

* Service 接口：

  ```java
  @Component
  @FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT")
  public interface PaymentHystrixService {
      @GetMapping("/payment/hystrix/ok/{id}")
      public String paymentInfo_Ok(@PathVariable("id") Integer id);
  
      @GetMapping("/payment/hystrix/timeout/{id}")
      public String paymentInfo_Timeout(@PathVariable("id") Integer id);
  }
  ```

* Controller：

  ```java
  @RestController
  @Slf4j
  public class OrderHystirxController {
      @Resource
      PaymentHystrixService paymentHystrixService;
  
      @GetMapping("/consumer/payment/hystrix/ok/{id}")
      public String paymentInfo_Ok(@PathVariable("id") Integer id) {
          return paymentHystrixService.paymentInfo_Ok(id);
      }
  
      @GetMapping("/consumer/payment/hystrix/timeout/{id}")
      public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
          return paymentHystrixService.paymentInfo_Timeout(id);
      }
  }
  ```

* 测试：使用的是 Feign 作为客户端，默认 1s 没有得到响应就会报超时错误，进行并发压测

* 解决：

  * 超时导致服务器变慢（转圈）：超时不再等待
  * 出错（宕机或程序运行出错）：出错要有兜底



****



##### 降级操作

生产者端和消费者端都可以进行服务降级，使用 @HystrixCommand 注解指定降级后的方法

生产者端：主启动类添加新注解 @EnableCircuitBreaker，业务类（Service）方法进行如下修改，

```java
// 模拟拥堵的情况
@HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler", commandProperties = {
    //规定这个线程的超时时间是3s，3s后就由fallbackMethod指定的方法“兜底”（服务降级）
    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "3000")
})
public String paymentInfo_Timeout(Integer id) {
    // 超时或者出错
}

public String paymentInfo_TimeoutHandler(Integer id) {
    return "线程池：" + Thread.currentThread().getName() + " paymentInfo_TimeoutHandler, id: " + id";
}
```

服务降级的方法和业务处理的方法混杂在了一块，耦合度很高，并且每个方法配置一个服务降级方法

- 在业务类Controller上加 @DefaultProperties(defaultFallback = "method_name") 注解
- 在需要服务降级的方法上标注 @HystrixCommand 注解，如果 @HystrixCommand 里没有指明 fallbackMethod，就默认使用 @DefaultProperties 中指明的降级服务

```java
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {
    @Resource
    PaymentHystrixService paymentHystrixService;

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_Ok(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @HystrixCommand
    public String paymentInfo_Timeout(@PathVariable("id") Integer id) {
        return paymentHystrixService.paymentInfo_Timeout(id);
    }

    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id) {
        return "fallback";
    }

    // 下面是全局fallback方法
    public String payment_Global_FallbackMethod() {
        return "Global fallback";
    }
}
```

客户端调用服务端，遇到服务端宕机或关闭等极端情况，为 Feign 客户端定义的接口添加一个服务降级实现类即可实现解耦

* application.yml：配置文件中开启了 Hystrix

  ```yaml
  # 用于服务降级 在注解 @FeignClient中添加fallbackFactory属性值
  feign:
    hystrix:
      enabled: true  #在Feign中开启Hystrix
  ```

* Service：统一为接口里面的方法进行异常处理，服务异常找 PaymentFallbackService，来统一进行服务降级的处理

  ```java
  @Component
  @FeignClient(value = "PROVIDER-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
  public interface PaymentHystrixService {
  
      @GetMapping("/payment/hystrix/ok/{id}")
      public String paymentInfo_OK(@PathVariable("id") Integer id);
  
      @GetMapping("/payment/hystrix/timeout/{id}")
      public String paymentInfo_Timeout(@PathVariable("id") Integer id);
  }
  ```

* PaymentFallbackService：

  ```java
  @Component
  public class PaymentFallbackService implements PaymentHystrixService {
      @Override
      public String paymentInfo_OK(Integer id) {
          return "------PaymentFallbackService-paymentInfo_Ok, fallback";
      }
  
      @Override
      public String paymentInfo_Timeout(Integer id) {
          return "------PaymentFallbackService-paymentInfo_Timeout, fallback";
      }
  }
  ```



***



#### 服务熔断

##### 熔断类型

熔断机制是应对雪崩效应的一种微服务链路保护机制，当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息

Hystrix 会监控微服务间调用的状况，当失败的调用到一定阈值，缺省时 5 秒内 20 次调用失败，就会启动熔断机制；当检测到该节点微服务调用响应正常后（检测方式是尝试性放开请求），自动恢复调用链路

- 熔断打开：请求不再进行调用当前服务，再有请求调用时将不会调用主逻辑，而是直接调用降级 fallback。实现了自动的发现错误并将降级逻辑切换为主逻辑，减少响应延迟效果。内部设置时钟一般为 MTTR（Mean time to repair，平均故障处理时间），当打开时长达到所设时钟则进入半熔断状态
- 熔断关闭：熔断关闭不会对服务进行熔断，服务正常调用
- 熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断，反之继续熔断

![Cloud-Hystrix熔断机制](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Hystrix熔断机制.png)



****



##### 熔断操作

涉及到断路器的四个重要参数：**快照时间窗、请求总数阀值、窗口睡眠时间、错误百分比阀值**

- circuitBreaker.enabled：是否开启断路器
- metrics.rollingStats.timeInMilliseconds：快照时间窗口，断路器确定是否打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的 10 秒
- circuitBreaker.requestVolumeThreshold：请求总数阀值，该属性设置在快照时间窗内（默认 10s）使断路器跳闸的最小请求数量（默认是 20），如果 10s 内请求数小于设定值，就算请求全部失败也不会触发断路器
- circuitBreaker.sleepWindowInMilliseconds：窗口睡眠时间，短路多久以后开始尝试是否恢复进入半开状态，默认 5s
- circuitBreaker.errorThresholdPercentage：错误百分比阀值，失败率达到多少后将断路器打开

```java
 //总的意思就是在n(10)毫秒内的时间窗口期内，m次请求中有p% (60%)的请求失败了，那么断路器启动
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), 
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")  
})
public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
    if (id < 0) {
        throw new RuntimeException("******id 不能负数");
    }
    String serialNumber = IdUtil.simpleUUID();  // 等价于UUID.randomUUID().toString()

    return Thread.currentThread().getName() + "\t" + "调用成功，流水号: " + serialNumber;
}
```

* 开启：满足一定的阈值（默认 10 秒内超过 20 个请求次数）、失败率达到阈值（默认 10 秒内超过 50% 的请求失败）
* 关闭：一段时间之后（默认是 5 秒），断路器是半开状态，会让其中一个请求进行转发，如果成功断路器会关闭，反之继续开启





***



#### 工作流程

具体工作流程：

1. 创建 HystrixCommand（用在依赖的服务返回单个操作结果的时候） 或 HystrixObserableCommand（用在依赖的服务返回多个操作结果的时候） 对象

2. 命令执行，其中 HystrixComand 实现了下面前两种执行方式，而 HystrixObservableCommand 实现了后两种执行方式

   * execute()：同步执行，从依赖的服务返回一个单一的结果对象， 或是在发生错误的时候抛出异常

   * queue()：异步执行， 直接返回 一个 Future 对象， 其中包含了服务执行结束时要返回的单一结果对象

   * observe()：返回 Observable 对象，代表了操作的多个结果，它是一个 Hot Obserable（不论事件源是否有订阅者，都会在创建后对事件进行发布，所以对于 Hot Observable 的每个订阅者都有可能是从事件源的中途开始的，并可能只是看到了整个操作的局部过程）

   * toObservable()：同样会返回 Observable 对象，也代表了操作的多个结果，但它返回的是一个 Cold Observable（没有订阅者的时候并不会发布事件，而是进行等待，直到有订阅者之后才发布事件，所以对于 Cold Observable 的订阅者，它可以保证从一开始看到整个操作的全部过程）

3. 若当前命令的请求缓存功能是被启用的，并且该命令缓存命中，那么缓存的结果会立即以 Observable 对象的形式返回
4. 检查断路器是否为打开状态，如果断路器是打开的，那么 Hystrix 不会执行命令，而是转接到 fallback 处理逻辑（第 8 步）；如果断路器是关闭的，检查是否有可用资源来执行命令（第 5 步）
5. 线程池/请求队列/信号量是否占满，如果命令依赖服务的专有线程池和请求队列，或者信号量（不使用线程池时）已经被占满， 那么 Hystrix 也不会执行命令， 而是转接到 fallback 处理逻辑（第 8 步）
6. Hystrix 会根据我们编写的方法来决定采取什么样的方式去请求依赖服务
   * HystrixCommand.run()：返回一个单一的结果，或者抛出异常
   * HystrixObservableCommand.construct()：返回一个Observable 对象来发射多个结果，或通过 onError 发送错误通知
7. Hystrix会将"成功"、"失败"、"拒绝"、"超时"等信息报告给断路器，而断路器会维护一组计数器来统计这些数据。断路器会使用这些统计数据来决定是否要将断路器打开，来对某个依赖服务的请求进行"熔断/短路"
8. 当命令执行失败的时候，Hystrix 会进入 fallback 尝试回退处理，通常也称该操作为"服务降级"，而能够引起服务降级情况：
   * 第 4 步：当前命令处于"熔断/短路"状态，断路器是打开的时候
   * 第 5 步：当前命令的线程池、请求队列或 者信号量被占满的时候
   * 第 6 步：HystrixObservableCommand.construct() 或 HystrixCommand.run() 抛出异常的时候
9. 当 Hystrix 命令执行成功之后， 它会将处理结果直接返回或是以 Observable 的形式返回

注意：如果、没有为命令实现降级逻辑或者在降级处理逻辑中抛出了异常， Hystrix 依然会返回一个 Observable 对象， 但是它不会发射任何结果数据，而是通过 onError 方法通知命令立即中断请求，并通过 onError() 方法将引起命令失败的异常发送给调用者

![Cloud-Hystrix工作流程](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Hystrix工作流程.png)



官方文档：https://github.com/Netflix/Hystrix/wiki/How-it-Works





****



#### 服务监控

Hystrix 提供了准实时的调用监控（Hystrix Dashboard），Hystrix 会持续的记录所有通过 Hystrix 发起的请求的执行信息，并以统计报表和图形的形式展示给用户，包括每秒执行多少请求多少成功，多少失败等，Netflix 通过 `hystrix-metrics-event-stream` 项目实现了对以上指标的监控，Spring Cloud 提供了 Hystrix Dashboard 的整合，对监控内容转化成可视化页面

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
  </dependency>
  ```

* application.yml：只需要端口即可

  ```yaml
  server:
    port: 9001
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableHystrixDashboard // 开启Hystrix仪表盘
  public class HystrixDashboardMain9001 {
      public static void main(String[] args) {
          SpringApplication.run(HystrixDashboardMain9001.class, args);
      }
  }
  ```

* 所有微服务（生产者）提供类 8001/8002/8003 都需要监控依赖配置

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

* 启动测试：http://localhost:9001/hystrix

  <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Hystrix可视化界面.png" alt="Cloud-Hystrix可视化界面" style="zoom: 67%;" />

* 新版本 Hystrix 需要在需要监控的微服务端的主启动类中指定监控路径，不然会报错

  ```java
  @SpringBootApplication
  @EnableEurekaClient  // 本服务启动后会自动注册进eureka服务中
  @EnableCircuitBreaker  // 对hystrixR熔断机制的支持
  public class PaymentHystrixMain8001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentHystrixMain8001.class, args);
      }
  
      /** ======================================需要添加的代码==================
       *此配置是为了服务监控而配置，与服务容错本身无关，springcloud升级后的坑
       *ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
       *只要在自己的项目里配置上下面的servlet就可以了
       */
      @Bean
      public ServletRegistrationBean getServlet() {
          HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
          ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
          registrationBean.setLoadOnStartup(1);
          registrationBean.addUrlMappings("/hystrix.stream");
          registrationBean.setName("HystrixMetricsStreamServlet");
          return registrationBean;
      }
  }
  ```

* 指标说明：

  ![Cloud-Hystrix界面图示说明](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Hystrix界面图示说明.png)

****

## 服务网关

### Gateway

#### 基本介绍

SpringCloud Gateway 是 Spring Cloud 的一个全新项目，基于 Spring 5.0+Spring Boot 2.0 和 Project Reactor 等技术开发的网关，旨在为微服务架构提供一种简单有效的统一的 API 路由管理方式。

* 基于 WebFlux 框架实现，而 WebFlux 框架底层则使用了高性能的 Reactor 模式通信框架 Netty（异步非阻塞响应式的框架）
* 基于 Filter 链的方式提供了网关基本的功能，例如：安全、监控/指标、限流等

Gateway 的三个核心组件：

* Route：路由是构建网关的基本模块，由 ID、目标 URI、一系列的断言和过滤器组成，如果断言为 true 则匹配该路由
* Predicate：断言，可以匹配 HTTP 请求中的所有内容（例如请求头或请求参数），如果请求参数与断言相匹配则进行路由
* Filter：指 Spring 框架中的 GatewayFilter实例，使用过滤器可以在请求被路由前或之后（拦截）对请求进行修改

![Cloud-Gateway工作流程](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Gateway工作流程.png)

核心逻辑：路由转发 + 执行过滤器链

- 客户端向 Spring Cloud Gateway 发出请求，然后在 Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到 Gateway Web Handler
- Handler 通过指定的过滤器链来将请求发送到际的服务执行业务逻辑，然后返回
- 过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（pre）或之后（post）执行业务逻辑
- Filter 在 pre 类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，在 post 类型的过滤器中可以做响应内容、响应头的修改、日志的输出、流量监控等





***



#### 网关使用

**配置方式**

Gateway 网关路由有两种配置方式，分别为通过 yml 配置和注入 Bean

* 引入 pom 依赖：Gateway 不需要 spring-boot-starter-web 依赖，否在会报错，原因是底层使用的是 WebFlux 与 Web 冲突

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-gateway</artifactId>
  </dependency>
  ```

* application.yml：

  ```yaml
  server:
    port: 9527
  
  spring:
    application:
      name: cloud-gateway
  
  eureka:
    instance:
      hostname: cloud-gateway-service
    client: #服务提供者provider注册进eureka服务列表内
      service-url:
        register-with-eureka: true
        fetch-registry: true
        defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka #集群版
  ```

* 主启动类（网关不需要业务类）：

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  public class GateWayMain9527 {
      public static void main(String[] args) {
          SpringApplication.run(GateWayMain9527.class, args);
      }
  }
  ```

* 以前访问 provider-payment8001 中的 Controller 方法，通过 localhost:8001/payment/get/id 和 localhost:8001/payment/lb，项目不想暴露 8001 端口号，希望在 8001 外面套一层 9527 端口：

  ```yaml
  server:
    port: 9527
  
  spring:
    application:
      name: cloud-gateway
  ## =====================新增====================
    cloud:
      gateway:
        routes:
          - id: payment_routh # payment_route	#路由的ID，没有固定规则但要求【唯一】，建议配合服务名
            uri: http://localhost:8001		#匹配后提供服务的路由地址
            predicates:
              - Path=/payment/get/**			# 断言，路径相匹配的进行路由
  
          - id: payment_routh2 # payment_route#路由的ID，没有固定规则但要求【唯一】，建议配合服务名
            uri: http://localhost:8001     	#匹配后提供服务的路由地址
            predicates:
              - Path=/payment/lb/**			# 断言，路径相匹配的进行路由
  ```

  * uri + predicate 拼接就是具体的接口请求路径，通过 localhost:9527 映射的地址
  * predicate 断言 http://localhost:8001下面有一个 /payment/get/** 的地址，如果找到了该地址就返回 true，可以用 9527 端口访问，进行端口的适配
  * `**` 表示通配符，因为这是一个不确定的参数

***

**注入Bean**

通过 9527 网关访问到百度的网址 https://www.baidu.com/，在 config 包下创建一个配置类，路由规则是访问 /baidu 跳转到百度

```java
@Configuration
public class GatewayConfig {
    // 配置了一个 id 为 path_route_cloud 的路由规则
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        // 构建一个路由器，这个routes相当于yml配置文件中的routes
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // 路由器的id是：path_route_cloud，规则是访问/baidu ，将会转发到 https://www.baidu.com/
        routes.route("path_route_cloud",
                r -> r.path("/baidu").uri(" https://www.baidu.com")).build();
        return routes.build();
    }
}
```

***

**动态路由**

Gateway 会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由和负载均衡，避免出现一个路由规则仅对应一个接口方法，当请求地址很多时需要很大的配置文件

application.yml 开启动态路由功能

```yaml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true		# 开启从注册中心动态创建路由的功能，利用微服务名进行路由
      routes:
        - id: payment_routh1   					# 路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: lb://cloud-payment-service		# 匹配后提供服务的路由地址
          predicates:
            - Path=/payment/get/**              # 断言，路径相匹配的进行路由

        - id: payment_routh2      				#路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: lb://cloud-payment-service		#匹配后提供服务的路由地址
          predicates:
            - Path=/payment/lb/**               # 断言，路径相匹配的进行路由
            - After=2021-09-28T19:14:51.514+08:00[Asia/Shanghai]
```

lb:// 开头代表从注册中心中获取服务，后面是需要转发到的服务名称

*****

#### 断言类型

After Route Predicate：匹配该断言时间之后的 URI 请求

* 获取时间：

  ```java
  public class TimeTest {
      public static void main(String[] args) {
          ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
          System.out.println(zbj); //2023-01-10T16:31:44.106+08:00[Asia/Shanghai]
      }
  }
  ```

* 配置 yml：动态路由小结有配置

* 测试：正常访问成功，将时间修改到 2023-01-10T16:31:44.106+08:00[Asia/Shanghai] 之后访问失败

  ![Cloud-Gateway时间断言](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Gateway时间断言.png)

常见断言类型：

* Before Route Predicate：匹配该断言时间之前的 URI 请求

* Between Route Predicate：匹配该断言时间之间的 URI 请求

  ```yaml
  - Between=2022-02-02T17:45:06.206+08:00[Asia/Shanghai],2022-03-25T18:59:06.206+08:00[Asia/Shanghai]
  ```

* Cookie Route Predicate：Cookie 断言，两个参数分别是 Cookie name 和正则表达式，路由规则会通过获取对应的 Cookie name 值和正则表达式去匹配，如果匹配上就会执行路由

  ```yaml
  - Cookie=username, seazean # 只有发送的请求有 cookie，而且有username=seazean这个数据才能访问，反之404
  ```

* Header Route Predicate：请求头断言

  ```yaml
  - Header=X-Request-Id, \d+ # 请求头要有 X-Request-Id 属性，并且值为整数的正则表达式
  ```

* Host Route Predicate：指定主机可以访问，可以指定多个用 `,` 分隔开

  ```yaml
  - Host=**.seazean.com
  ```

* Method Route Predicate：请求类型断言

  ```yaml
  - Method=GET	# 只有 Get 请求才能访问
  ```

* Path Route Predicate：路径匹配断言

* Query Route Predicate：请求参数断言

  ```yaml
  - Query=username, \d+ # 要有参数名 username 并且值还要是整数才能路由
  ```





****



#### Filter使用

Filter 链是同时满足一系列的过滤链，路由过滤器可用于修改进入的 HTTP 请求和返回的 HTTP 响应，路由过滤器只能指定路由进行使用，Spring Cloud Gateway 内置了多种路由过滤器，都由 GatewayFilter 的工厂类来产生

配置文件：https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#gatewayfilter-factories

自定义全局过滤器：实现两个主要接口 GlobalFilter, Ordered

```java
@Component
@Slf4j
public class MyLogGateWayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("*********************come in MyLogGateWayFilter:  "+ new Date());
        // 取出请求参数的uname对应的值
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        // 如果 uname 为空，就直接过滤掉，不走路由
        if(uname == null){
            log.info("************* 用户名为 NULL 非法用户 o(╥﹏╥)o");

            // 判断该请求不通过时：给一个回应，返回
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }

        // 反之，调用下一个过滤器，也就是放行：在该环节判断通过的 exchange 放行，交给下一个 filter 判断
        return chain.filter(exchange);
    }
	
    // 设置这个过滤器在Filter链中的加载顺序，数字越小，优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}

```

***

## 服务配置

### config

#### 基本介绍

SpringCloud Config 为微服务架构中的微服务提供集中化的外部配置支持（Git/GitHub），为各个不同微服务应用的所有环境提供了一个中心化的外部配置（Config Server）

![Cloud-Config工作原理](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Config工作原理.png)

SpringCloud Config 分为服务端和客户端两部分

* 服务端也称为分布式配置中心，是一个独立的微服务应用，连接配置服务器并为客户端提供获取配置信息，加密/解密等信息访问接口
* 客户端通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动时从配置中心获取和加载配置信息，配置服务器默认采用 Git 来存储配置信息，这样既有助于对环境配置进行版本管理，也可以通过 Git 客户端来方便的管理和访问配置内容

优点：

* 集中管理配置文件
* 不同环境不同配置，动态化的配置更新，分环境部署比如 dev/test/prod/beta/release
* 运行期间动态调整配置，服务向配置中心统一拉取配置的信息，**服务不需要重启即可感知到配置的变化并应用新的配置**
* 将配置信息以 Rest 接口的形式暴露

官网： https://cloud.spring.io/spring-cloud-static/spring-cloud-config/2.2.1.RELEASE/reference/html/


****

#### 服务端

构建 Config Server 模块

* 引入 pom 依赖：

  ```xml
  <!--springCloud Config Server-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-config-server</artifactId>
  </dependency>
  ```

* application.yml：

  ```yaml
  server:
    port: 3344
  
  spring:
    application:
      name:  cloud-config-center #注册进Eureka服务器的微服务名
    cloud:
      config:
        server:
          git:
            # GitHub上面的git仓库名字 这里可以写https地址跟ssh地址，https地址需要配置username和 password
            uri: git@github.com:seazean/springcloud-config.git
            default-label: main
            search-paths:
              - springcloud-config	# 搜索目录
            # username: 
            # password:
        label: main   # 读取分支,以前是master
  
  #服务注册到eureka地址
  eureka:
    client:
      service-url:
      defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka #集群版
  ```

  search-paths 表示远程仓库下有一个叫做 springcloud-config 的，label 则表示读取 main分支里面的内容

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  @EnableConfigServer   //开启SpringCloud的
  public class ConfigCenterMain3344 {
      public static void main(String[] args) {
          SpringApplication.run(ConfigCenterMain3344.class, args);
      }
  }
  ```

配置读取规则：

```yaml
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

* label：分支
* name：服务名
* profile：环境（dev/test/prod）

比如：http://localhost:3344/master/config-dev.yaml

***

#### 客户端

**基本配置**

配置客户端 Config Client，客户端从配置中心（Config Server）获取配置信息

* 引入 pom 依赖：

  ```xml
  <!--这里就是客户端的SpringCloud config 因为是客户端所以没有server-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
  </dependency>
  ```

* bootstrap.yml：系统级配置，优先级更高，application.yml 是用户级的资源配置项

  Spring Cloud 会创建一个 Bootstrap Context 作为 Spring 应用的 Application Context 的父上下文，初始化的时候 Bootstrap Context 负责从外部源加载配置属性并解析配置，这两个上下文共享一个从外部获取的 Environment，为了配置文件的加载顺序和分级管理，这里使用 bootstrap.yml

  ```yaml
  server:
    port: 3355	# 构建多个微服务，3366 3377 等
  
  spring:
    application:
      name: config-client
    cloud:
      #Config客户端配置
      config:
        label: main 	#分支名称 以前是master
        name: config 	#配置文件名称
        profile: dev 	#读取后缀名称   
        # main分支上config-dev.yml的配置文件被读取 http://config-3344.com:3344/master/config-dev.yml
        uri: http://localhost:3344 # 配置中心地址k
  
  #服务注册到eureka地址
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  public class ConfigClientMain3355 {
      public static void main(String[] args) {
          SpringApplication.run(ConfigClientMain3355.class, args);
      }
  }
  ```

* 业务类：将配置信息以 REST 窗口的形式暴露

  ```java
  @RestController
  public class ConfigClientController {
      @Value("${config.info}")
      private String configInfo;
  
      @GetMapping("/configInfo")
      public String getConfigInfo() {
          return configInfo;
      }
  }
  ```

***

**动态刷新**

分布式配置的动态刷新问题，修改 GitHub 上的配置文件，Config Server 配置中心立刻响应，但是 Config Client 客户端没有任何响应，需要重启客户端

* 引入 pom 依赖：

  ```xml
  <!--web/actuator这两个一般一起使用，写在一起-->
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
  </dependency>
  ```

* 修改 yml，暴露监控端口：SpringBoot 的 actuator 启动端点监控 Web 端默认加载默认只有两个 info，health 可见的页面节点

  ```yaml
  management: 
    endpoints:
      web:
        exposure:
          include: "*" 		# 表示包含所有节点页面
          exclude: env,beans	# 表示排除env、beans
  ```

* 业务类：加 @RefreshScope 注解

  ```java
  @RestController
  @RefreshScope
  public class ConfigClientController {
      // 从配置文件中取前缀为server.port的值
      @Value("${config.info}")
      private String configInfo;
  	// config-{profile}.yml
      @GetMapping("/configInfo")
      public String getConfigInfo() {
          return configInfo;
      }
  }
  ```

此时客户端还是没有刷新，需要发送 POST 请求刷新 3355：`curl -X POST "http://localhost:3355/actuator/refresh`

引出问题：

* 在微服务多的情况下，每个微服务都需要执行一个 POST 请求，手动刷新成本太大
* 可否广播，一次通知，处处生效，大范围的实现自动刷新

解决方法：Bus 总线





***





## 服务消息

### Bus

#### 基本介绍

Spring Cloud Bus 能管理和传播分布式系统间的消息，就像分布式执行器，可用于广播状态更改、事件推送、微服务间的通信通道等

消息总线：在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来。由于该主题中产生的消息会被所有实例监听和消费，所以称为消息总线

基本原理：ConfigClient 实例都监听 MQ 中同一个 Topic（默认 springCloudBus)，当一个服务刷新数据时，会把信息放入 Topic 中，这样其它监听同一 Topic 的服务就能得到通知，然后去更新自身的配置

****

#### 全局广播

利用消息总线接触一个服务端 ConfigServer 的 `/bus/refresh` 断点，从而刷新所有客户端的配置

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Bus全局广播架构.png" alt="Cloud-Bus全局广播架构" style="zoom:67%;" />

改造 ConfigClient：

* 引入 MQ 的依赖：

  ```xml
  <!--添加消息总线RabbitMQ支持-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-bus-amqp</artifactId>
  </dependency>
  ```

* yml 文件添加 MQ 信息：

  ```yaml
  server:
    port: 3344
  
  spring:
    application:
      name:  config-client #注册进Eureka服务器的微服务名
    cloud:
    # rabbitmq相关配置
    rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
  
  # rabbitmq相关配置,暴露bus刷新配置的端点
  management:
    endpoints: # 暴露bus刷新配置的端点
      web:
        exposure:
          include: 'bus-refresh'
  ```

* 只需要调用一次 `curl -X POST "http://localhost:3344/actuator/bus-refresh`，可以实现全局广播



***



#### 定点通知

动态刷新情况下，只通知指定的微服务，比如只通知 3355 服务，不通知 3366，指定具体某一个实例生效，而不是全部

公式：`http://localhost:port/actuator/bus-refresh/{destination}`

/bus/refresh 请求不再发送到具体的服务实例上，而是发给 Config Server 并通过 destination 参数类指定需要更新配置的服务或实例

![Cloud-Bus工作流程](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Bus工作流程.png)





****





### Stream

#### 基本介绍

Spring Cloud Stream 是一个构建消息驱动微服务的框架，通过定义绑定器 Binder 作为中间层，实现了应用程序与消息中间件细节之间的隔离，屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型

Stream 中的消息通信方式遵循了发布订阅模式，Binder 可以生成 Binding 用来绑定消息容器的生产者和消费者，Binding 有两种类型 Input 和 Output，Input 对应于消费者（消费者从 Stream 接收消息），Output 对应于生产者（生产者从 Stream 发布消息）

![Cloud-Stream工作流程](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Stream工作流程.png)

- Binder：连接中间件
- Channel：通道，是队列 Queue 的一种抽象，在消息通讯系统中实现存储和转发的媒介，通过 Channel 对队列进行配置
- Source、Sink：生产者和消费者



中文手册：https://m.wang1314.com/doc/webapp/topic/20971999.html



****



#### 基本使用

Binder 是应用与消息中间件之间的封装，目前实现了 Kafka 和 RabbitMQ 的 Binder，可以动态的改变消息类型（Kafka 的 Topic 和 RabbitMQ 的 Exchange），可以通过配置文件实现，常用注解如下：

* @Input：标识输入通道，接收的消息通过该通道进入应用程序
* @Output：标识输出通道，发布的消息通过该通道离开应用程序
* @StreamListener：监听队列，用于消费者队列的消息接收
* @EnableBinding：信道 Channel 和 Exchange 绑定

生产者发消息模块：

* 引入 pom 依赖：RabbitMQ

  ```xml
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
  </dependency>
  ```

* application.yml：

  ```yaml
  server:
    port: 8801
  
  spring:
    application:
      name: cloud-stream-provider
    cloud:
      stream:
        binders: # 在此处配置要绑定的rabbitmq的服务信息；
          defaultRabbit: # 表示定义的名称，用于于binding整合
            type: rabbit # 消息组件类型
            environment: # 设置rabbitmq的相关的环境配置
              spring:
                rabbitmq:
                  host: localhost
                  port: 5672
                  username: guest
                  password: guest
        bindings: # 服务的整合处理
          output: # 这个名字是一个通道的名称
            destination: studyExchange 		# 表示要使用的Exchange名称定义
            content-type: application/json	# 设置消息类型，本次为json，文本则设置“text/plain”
            binder: defaultRabbit 			# 设置要绑定的消息服务的具体设置
  
  eureka:
    client: # 客户端进行Eureka注册的配置
      service-url:
        defaultZone: http://localhost:7001/eureka,http://localhost:7002/eureka
    instance:
      lease-renewal-interval-in-seconds: 2 # 设置心跳的时间间隔（默认是30秒）
      lease-expiration-duration-in-seconds: 5 # 如果现在超过了5秒的间隔（默认是90秒）
      instance-id: send-8801.com  # 在信息列表时显示主机名称
      prefer-ip-address: true     # 访问的路径变为IP地址
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableEurekaClient
  public class StreamMQMain8801 {
      public static void main(String[] args) {
          SpringApplication.run(StreamMQMain8801.class, args);
      }
  }
  ```

* 业务类：MessageChannel 的实例名必须是 output，否则无法启动

  ```java
  // 可以理解为定义消息的发送管道Source对应output(生产者)，Sink对应input(消费者)
  @EnableBinding(Source.class)
  // @Service：这里不需要，不是传统的controller调用service。这个service是和rabbitMQ打交道的
  // IMessageProvider 只有一个 send 方法的接口
  public class MessageProviderImpl implements IMessageProvider {
      @Resource
      private MessageChannel output; // 消息的发送管道
  
      @Override
      public String send() {
          String serial = UUID.randomUUID().toString();
  
          //创建消息，通过output这个管道向消息中间件发消息
          this.output.send(MessageBuilder.withPayload(serial).build());
          System.out.println("***serial: " + serial);
          return serial;
      }
  }
  ```

* Controller：

  ```java
  @RestController
  public class SendMessageController {
      @Resource
      private IMessageProvider messageProvider;
  
      @GetMapping(value = "/sendMessage")
      public String sendMessage() {
          return messageProvider.send();
      }
  }
  ```

消费者模块：8802 和 8803 两个消费者

* application.yml：只标注出与生产者不同的地方

  ```yaml
  server:
    port: 8802
  
  spring:
    application:
      name: cloud-stream-consumer
    cloud:
      stream:
        # ...
        bindings: # 服务的整合处理
          input: # 这个名字是一个通道的名称
            # ...
            binder: { defaultRabbit } # 设置要绑定的消息服务的具体设置
  
  eureka:
    # ...
    instance:
      # ...
      instance-id: receive-8802.com  # 在信息列表时显示主机名称
  ```

* Controller：

  ```java
  @Component
  @EnableBinding(Sink.class) // 理解为定义一个消息消费者的接收管道
  public class ReceiveMessageListener {
      @Value("${server.port}")
      private String serverPort;
  
      @StreamListener(Sink.INPUT) //输入源：作为一个消息监听者
      public void input(Message<String> message) {
          // 获取到消息
          String messageStr = message.getPayload();
          System.out.println("消费者1号，------->接收到的消息：" + messageStr + "\t port: " + serverPort);
      }
  }
  ```



****



#### 高级特性

重复消费问题：生产者 8801 发送一条消息后，8802 和 8803 会同时收到 8801 的消息

解决方法：微服务应用放置于同一个 group 中，能够保证消息只会被其中一个应用消费一次。不同的组是可以全面消费的（重复消费），同一个组内的多个消费者会发生竞争关系，只有其中一个可以消费

```yaml
bindings:
  input:
    destination: studyExchange
    content-type: application/json
    binder: { defaultRabbit }
    group: seazean	# 设置分组
```

消息持久化问题：

* 停止 8802/8803 并去除掉 8802 的分组 group: seazean，8801 先发送 4 条消息到 MQ
* 先启动 8802，无分组属性配置，后台没有打出来消息，消息丢失
* 再启动 8803，有分组属性配置，后台打印出来了 MQ 上的消息





*****





### Sleuth

#### 基本介绍

Spring Cloud Sleuth 提供了一套完整的分布式请求链路跟踪的解决方案，并且兼容支持了 zipkin

在微服务框架中，一个客户端发起的请求在后端系统中会经过多次不同的服务节点调用来协同产生最后的请求结果，形成一条复杂的分布式服务调用链路，链路中的任何一环出现高延时或错误都会引起整个请求最后的失败，所以需要链路追踪



Sleuth 官网：https://github.com/spring-cloud/spring-cloud-sleuth

zipkin 下载地址：https://repo1.maven.org/maven2/io/zipkin/java/zipkin-server/



***



#### 链路监控

Sleuth 负责跟踪整理，zipkin 负责可视化展示

```bash
java -jar zipkin-server-2.12.9-exec.jar # 启动 zipkin 
```

访问 http://localhost:9411/zipkin/ 展示交互界面

一条请求链路通过 Trace ID 唯一标识，Span 标识发起的请求信息

* Trace：类似于树结构的 Span 集合，表示一条调用链路，存在唯一 ID 标识

* Span：表示调用链路来源，通俗的理解 Span 就是一次请求信息，各个 Span 通过 ParentID 关联起来

服务生产者模块：

* 引入 pom 依赖：

  ```xml
  <!--包含了sleuth+zipkin-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-zipkin</artifactId>
  </dependency>
  ```

* application.yml：

  ```yaml
  server:
    port: 8001
  
  spring:
    application:
      name: cloud-payment-service
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        #采样率值介于 0 到 1 之间，1 则表示全部采集
        probability: 1
  ```

* 业务类：

  ```java
  @GetMapping("/payment/zipkin")
  public String paymentZipkin() {
      return "hi ,i'am paymentzipkin server fall back，welcome to seazean";
  }
  ```

服务消费者模块：

* application.yml：

  ```yaml
  server:
    port: 80
  
  # 微服务名称
  spring:
    application:
      name: cloud-order-service
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        probability: 1
  ```

* 业务类：

  ```java
  @GetMapping("/comsumer/payment/zipkin")
  public String paymentZipKin() {
      String result = restTemplate.getForObject("http://localhost:8001" + "/payment/zipkin/", String.class);
      return result;
  }
  ```






****





## Alibaba

Spring Cloud Alibaba 致力于提供微服务开发的一站式解决方案，此项目包含开发分布式应用微服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务

- 服务限流降级：默认支持 WebServlet、WebFlux、OpenFeign、RestTemplate、Spring Cloud Gateway、Zuul、Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
- 服务注册与发现：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持
- 分布式配置管理：支持分布式系统中的外部化配置，配置更改时自动刷新
- 消息驱动能力：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力
- 分布式事务：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题
- 阿里云对象存储：阿里云提供的海量、安全、低成本、高可靠的云存储服务
- 分布式任务调度：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行

官方文档：https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md

官方手册：https://spring-cloud-alibaba-group.github.io/github-pages/greenwich/spring-cloud-alibaba.html

***

### Nacos

#### 基本介绍

Nacos 全称 Dynamic Naming and Configuration Service，一个更易于构建云原生应用的动态服务发现、配置管理和服务的管理平台，Nacos = Eureka + Config + Bus

下载地址：https://github.com/alibaba/nacos/releases

启动命令：命令运行成功后直接访问 http://localhost:8848/nacos，默认账号密码都是 nacos

```bash
startup.cmd -m standalone # standalone 代表着单机模式运行，非集群模式
```

关闭命令：

```bash
shutdown.cmd
```

注册中心对比：C 一致性，A 可用性，P 分区容错性

| 注册中心  | CAP 模型 | 控制台管理 |
| :-------: | :------: | :--------: |
|  Eureka   |    AP    |    支持    |
| Zookeeper |    CP    |   不支持   |
|  Consul   |    CP    |    支持    |
|   Nacos   |    AP    |    支持    |

切换模式：`curl -X PUT '$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP`

官网：https://nacos.io

****

#### 注册中心

Nacos 作为服务注册中心

服务提供者：

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
  </dependency>
  ```

* application.yml：

  ```yaml
  server:
    port: 9001
  
  spring:
    application:
      name: nacos-payment-provider
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848 #配置Nacos地址，注册到Nacos
  # 做监控需要把这个全部暴露出来
  management:
    endpoints:
      web:
        exposure:
          include: '*'
  ```

* 主启动类：注解是 EnableDiscoveryClient

  ```java
  @EnableDiscoveryClient
  @SpringBootApplication
  public class PaymentMain9001 {
      public static void main(String[] args) {
          SpringApplication.run(PaymentMain9001.class, args);
      }
  }
  ```

* Controller：

  ```java
  @RestController
  public class PaymentController {
      @Value("${server.port}")
      private String serverPort;
  
      @GetMapping(value = "/payment/nacos/{id}")
      public String getPayment(@PathVariable("id") Integer id) {
          return "nacos registry, serverPort: " + serverPort + "\t id" + id;
      }
  }
  ```

* 管理后台服务：

  ![Cloud-Nacos服务列表](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Nacos服务列表.png)

* 新建一个模块端口是 9002，其他与 9001 服务一样，nacos-payment-provider 的实例数就变为 2

服务消费者：

* application.yml：

  ```yaml
  server:
    port: 83
  
  spring:
    application:
      name: nacos-order-consumer
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848
  
  # 消费者将要去访问的微服务名称(注册成功进nacos的微服务提供者)
  service-url:
    nacos-user-service: http://nacos-payment-provider
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableDiscoveryClient
  public class OrderNacosMain83 {
      public static void main(String[] args) {
          SpringApplication.run(OrderNacosMain83.class, args);
      }
  }
  ```

* 业务类：

  ```java
  @Configuration
  public class ApplicationContextBean {
      @Bean
      @LoadBalanced // 生产者集群状态下，必须添加，防止找不到实例
      public RestTemplate getRestTemplate() {
          return new RestTemplate();
      }
  }
  ```

  ```java
  @RestController
  @Slf4j
  public class OrderNacosController {
      @Resource
      private RestTemplate restTemplate;
  	// 从配置文件中读取 URL
      @Value("${service-url.nacos-user-service}")
      private String serverURL;
  
      @GetMapping("/consumer/payment/nacos/{id}")
      public String paymentInfo(@PathVariable("id") Long id) {
          String result = restTemplate.getForObject(serverURL + "/payment/nacos/" + id, String.class);
          return result;
      }
  }
  ```

  

***

#### 配置中心

**基础配置**

把配置文件写进 Nacos，然后再用 Nacos 做 config 这样的功能，直接从 Nacos 上抓取服务的配置信息

在 Nacos 中，dataId 的完整格式如下 `${prefix}-${spring.profiles.active}.${file-extension}`

* `prefix`：默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix` 来配置
* `spring.profiles.active`：当前环境对应的 profile，当该值为空时，dataId 的拼接格式变成 `${prefix}.${file-extension}`
* `file-exetension`：配置内容的数据格式，可以通过配置项 `spring.cloud.nacos.config.file-extension` 来配置，目前只支持 properties 和 yaml 类型（不是 yml）

构建流程：

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
  </dependency>
  ```

* 配置两个 yml 文件：配置文件的加载是存在优先级顺序的，bootstrap 优先级高于 application

  bootstrap.yml：全局配置

  ```yaml
  # nacos配置
  server:
    port: 3377
  
  spring:
    application:
      name: nacos-config-client
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848 #Nacos服务注册中心地址
        config:
          server-addr: localhost:8848 #Nacos作为配置中心地址
          file-extension: yaml #指定yaml格式的配置
  
  # ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
  ```

  application.yml：服务独立配置，表示服务要去配置中心找名为 nacos-config-client-dev.yaml 的文件

  ```yaml
  spring:
    profiles:
      active: dev # 表示开发环境
  ```

* 主启动类：

  ```java
  @SpringBootApplication
  @EnableDiscoveryClient
  public class NacosConfigClientMain3377 {
      public static void main(String[] args) {
          SpringApplication.run(NacosConfigClientMain3377.class, args);
      }
  }
  ```

* 业务类：@RefreshScope 注解使当前类下的配置支持 Nacos 的动态刷新功能

  ```java
  @RestController
  @RefreshScope
  public class ConfigClientController {
      @Value("${config.info}")
      private String configInfo;
  
      @GetMapping("/config/info")
      public String getConfigInfo() {
          return configInfo;
      }
  }
  ```

* 新增配置，然后访问 http://localhost:3377/config/info

  ![Cloud-Nacos新增配置](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Nacos新增配置.png)

***

**分类配置**

分布式开发中的多环境多项目管理问题，Namespace 用于区分部署环境，Group 和 DataID 逻辑上区分两个目标对象

![Cloud-Nacos配置说明](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Nacos配置说明.png)

Namespace 默认 public，主要用来实现隔离，图示三个开发环境

Group 默认是 DEFAULT_GROUP，Group 可以把不同的微服务划分到同一个分组里面去

***

**加载配置**

DataID 方案：指定 `spring.profile.active` 和配置文件的 DataID 来使不同环境下读取不同的配置

Group 方案：通过 Group 实现环境分区，在 config 下增加一条 Group 的配置即可

Namespace 方案：

```yaml
server:
  port: 3377

spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #Nacos服务注册中心地址
      config:
        server-addr: localhost:8848 #Nacos作为配置中心地址
        file-extension: yaml #指定yaml格式的配置
        group: DEV_GROUP
        namespace: 95d44530-a4a6-4ead-98c6-23d192cee298
```

![Cloud-Nacos命名空间](https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Nacos命名空间.png)

****

#### 集群架构

集群部署参考官方文档，Nacos 支持的三种部署模式：

- 单机模式：用于测试和单机使用
- 集群模式：用于生产环境，确保高可用
- 多集群模式：用于多数据中心场景

集群部署文档：https://nacos.io/zh-cn/docs/v2/guide/admin/cluster-mode-quick-start.html

默认 Nacos 使用嵌入式数据库 derby 实现数据的存储，重启 Nacos 后配置文件不会消失，但是多个 Nacos 节点数据存储存在一致性问题，每个 Nacos 都有独立的嵌入式数据库，所以 Nacos 采用了集中式存储的方式来支持集群化部署，目前只支持 MySQL 的存储

Windows 下 Nacos 切换 MySQL 存储：

* 在 Nacos 安装目录的 conf 目录下找到一个名为 `nacos-mysql.sql` 的脚本并执行

* 在 conf 目录下找到 `application.properties`，增加如下数据

  ```properties
  spring.datasource.platform=mysql
   
  db.num=1
  db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
  db.user=username
  db.password=password
  ```

* 重新启动 Nacos，可以看到是个全新的空记录界面

Linux 参考：https://www.yuque.com/mrlinxi/pxvr4g/rnahsn#dPvMy

****

### Sentinel

#### 基本介绍

**Spring Cloud Alibaba Sentinel** 是一个由 Spring Cloud Alibaba 提供的、基于 Sentinel 的分布式系统熔断器和流量控制解决方案。Sentinel 是阿里巴巴开源的一款高性能流量控制和熔断器组件，主要用于保障微服务架构中的系统稳定性和可靠性。Spring Cloud Alibaba Sentinel 将 Sentinel 的功能集成到 Spring Cloud 项目中，提供了一整套流量控制和熔断的解决方案，支持在 Spring Boot 和 Spring Cloud 环境下使用 Sentinel。

Sentinel 分为两个部分：

- 核心库（Java 客户端）不依赖任何框架/库，能够运行于 Java 8 及以上的版本的运行时环境
- 控制台（Dashboard）主要负责管理推送规则、监控、管理机器信息等

下载到本地，运行命令：`java -jar sentinel-dashboard-1.8.2.jar` （要求 Java8，且 8080 端口不能被占用），访问 http://localhost:8080/，账号密码均为 sentinel

官网：https://sentinelguard.io

下载地址：https://github.com/alibaba/Sentinel/releases

****

#### 基本功能

1. **流量控制**：

​	•**QPS 限流**：基于每秒请求数进行流量控制。

​	•**并发线程数控制**：限制并发线程数，防止系统被大量请求拖垮。

2. **熔断机制**：

​	•**请求失败比例**：根据失败请求的比例来决定是否开启熔断器。

​	•**请求时长**：根据请求的平均响应时间来决定是否进行熔断。

3. **系统自适应保护**：

​	•通过自适应流控算法，Sentinel 可以动态调整流量控制的策略，以适应系统负载的变化。

4. **熔断降级**：

​	•提供熔断和降级机制，当服务出现异常时，能够自动切换到降级处理逻辑。

5. **实时监控**：

​	•提供 Web 界面监控、数据可视化功能，用于实时监控系统的运行状态。

6. **集成和配置**：

​	•与 Spring Cloud 集成，提供简便的配置方式，支持注解和配置文件两种方式进行流量控制和熔断设置。

#### 基本使用

构建演示工程：

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
  </dependency>
  ```

* application.yml：sentinel.transport.port 端口配置会在应用对应的机器上启动一个 HTTP Server，该 Server 与 Sentinel 控制台做交互。比如 Sentinel 控制台添加了 1 个限流规则，会把规则数据 Push 给 Server 接收，Server 再将规则注册到 Sentinel 中

  ```yaml
  server:
    port: 8401
  
  spring:
    application:
      name: cloudalibaba-sentinel-service
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848 # Nacos 服务注册中心地址【需要启动Nacos8848】
      sentinel:
        transport:
          # 配置Sentinel dashboard地址
          dashboard: localhost:8080
          # 默认8719端口，假如被占用会自动从8719开始依次+1扫描,直至找到未被占用的端口
          port: 8719
  
  management:
    endpoints:
      web:
        exposure:
          include: '*'
  ```

* 主启动类：

  ```java
  @EnableDiscoveryClient
  @SpringBootApplication
  public class SentinelMainApp8401 {
      public static void main(String[] args) {
          SpringApplication.run(SentinelMainApp8401.class, args);
      }
  }
  ```

* 流量控制 Controller：

  ```java
  @RestController
  @Slf4j
  public class FlowLimitController {
      @GetMapping("/testA")
      public String testA() {
          return "------testA";
      }
  
      @GetMapping("/testB")
      public String testB() {
          return "------testB";
      }
  }
  ```

* Sentinel 采用懒加载机制，需要先访问 http://localhost:8401/testA，控制台才能看到

***

#### 流控规则

流量控制规则 FlowRule：同一个资源可以同时有多个限流规则

* 资源名 resource：限流规则的作用对象，Demo 中为 testA
* 针对资源 limitApp：针对调用者进行限流，默认为 default 代表不区分调用来源
* 阈值类型 grade：QPS 或线程数模式
* 单机阈值 count：限流阈值
* 流控模式 strategy：调用关系限流策略
  * 直接：资源本身达到限流条件直接限流
  * 关联：当关联的资源达到阈值时，限流自身
  * 链路：只记录指定链路上的流量，从入口资源进来的流量
* 流控效果 controlBehavior：
  * 快速失败：直接失败，抛出异常
  * Warm Up：冷启动，根据 codeFactory（冷加载因子，默认 3）的值，从 count/codeFactory 开始缓慢增加，给系统预热时间
  * 排队等待：匀速排队，让请求以匀速的方式通过，阈值类型必须设置为 QPS，否则无效

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Sentinel增加流控规则.png" alt="Cloud-Sentinel增加流控规则" style="zoom: 40%;" />

通过调用 `SystemRuleManager.loadRules()` 方法来用硬编码的方式定义流量控制规则：

```java
private void initSystemProtectionRule() {
  List<SystemRule> rules = new ArrayList<>();
  SystemRule rule = new SystemRule();
  rule.setHighestSystemLoad(10);
  rules.add(rule);
  SystemRuleManager.loadRules(rules);
}
```

详细内容参考文档：https://sentinelguard.io/zh-cn/docs/flow-control.html

****

#### 降级熔断

Sentinel 熔断降级会在调用链路中某个资源出现不稳定状态时，对这个资源的调用进行限制，让请求快速失败，避免影响到其它的资源而导致级联错误。当资源被降级后，在接下来的降级时间窗口之内，对该资源的调用都自动熔断（默认行为是抛出 DegradeException）

Sentinel 提供以下几种熔断策略：

* 资源名 resource：限流规则的作用对象，Demo 中为 testA
* 熔断策略 grade：
  * 慢调用比例（SLOW_REQUEST_RATIO）：以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断
  * 异常比例（ERROR_RATIO）：当单位统计时长内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态，若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 `[0.0, 1.0]`，代表 0% - 100%
  * 异常数 （ERROR_COUNT）：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态，若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断
* 单机阈值 count：慢调用比例模式下为慢调用临界 RT；异常比例/异常数模式下为对应的阈值
* 熔断时长 timeWindow：单位为 s
* 最小请求数 minRequestAmount：熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断，默认 5
* 统计时长 statIntervalMs：单位统计时长
* 慢调用比例阈值 slowRatioThreshold：仅慢调用比例模式有效

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Sentinel增加熔断规则.png" alt="Cloud-Sentinel增加熔断规则" style="zoom: 67%;" />

注意异常降级仅针对业务异常，对 Sentinel 限流降级本身的异常 BlockException 不生效，为了统计异常比例或异常数，需要通过 `Tracer.trace(ex)` 记录业务异常或者通过`@SentinelResource` 注解会自动统计业务异常

```java
Entry entry = null;
try {
  entry = SphU.entry(resource);

  // Write your biz code here.
  // <<BIZ CODE>>
} catch (Throwable t) {
  if (!BlockException.isBlockException(t)) {
    Tracer.trace(t);
  }
} finally {
  if (entry != null) {
    entry.exit();
  }
}
```



详细内容参考文档：https://sentinelguard.io/zh-cn/docs/circuit-breaking.html



****



#### 热点限流

热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流，Sentinel 利用 LRU 策略统计最近最常访问的热点参数，结合令牌桶算法来进行参数级别的流控

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Sentinel热点参数限流.png" alt="Cloud-Sentinel热点参数限流" style="zoom: 67%;" />

引入 @SentinelResource 注解：https://sentinelguard.io/zh-cn/docs/annotation-support.html

- value：Sentinel 资源名，默认为请求路径，这里 value 的值可以任意写，但是约定与 Restful 地址一致

- blockHandler：表示触发了 Sentinel 中配置的流控规则，就会调用兜底方法 `del_testHotKey`

- blockHandlerClass：如果设置了该值，就会去该类中寻找 blockHandler 方法

- fallback：用于在抛出异常的时候提供 fallback 处理逻辑

  说明：fallback 对应服务降级（服务出错了需要有个兜底方法），blockHandler 对应服务熔断（服务不可用的兜底方法）

```java
@GetMapping("/testHotKey")
@SentinelResource(value = "testHotKey", blockHandler = "del_testHotKey")
public String testHotkey(@RequestParam(value = "p1", required = false) String p1,
                         @RequestParam(value = "p1", required = false) String p2) {
    return "------testHotkey";
}

// 自定义的兜底方法，必须是 BlockException
public String del_testHotKey(String p1, String p2, BlockException e) {
    return "不用默认的兜底提示 Blocked by Sentinel(flow limiting)，自定义提示：del_testHotKeyo.";
}
```

图示设置 p1 参数限流，规则是 1s 访问 1 次，当 p1=5 时 QPS > 100，只访问 p2 不会出现限流 `http://localhost:8401/testHotKey?p2=b`

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Sentinel增加热点规则.png" alt="Cloud-Sentinel增加热点规则" style="zoom:50%;" />

* 参数索引 paramIdx：热点参数的索引，图中索引 0 对应方法中的 p1 参数
* 参数例外项 paramFlowItemList：针对指定的参数值单独设置限流阈值，不受 count 阈值的限制，**仅支持基本类型和字符串类型**

说明：@SentinelResource 只管控制台配置规则问题，出现运行时异常依然会报错



详细内容参考文档：https://sentinelguard.io/zh-cn/docs/parameter-flow-control.html



***



#### 系统规则

Sentinel 系统自适应保护从整体维度对**应用入口流量**进行控制，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性

系统规则支持以下的阈值类型：

- Load（仅对 Linux/Unix-like 机器生效）：当系统 load1 超过阈值，且系统当前的并发线程数超过系统容量时才会触发系统保护，系统容量由系统的 `maxQps * minRt` 计算得出，设定参考值一般是 `CPU cores * 2.5`
- RT：当单台机器上所有入口流量的平均 RT 达到阈值即触发系统保护，单位是毫秒
- 线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护
- 入口 QPS：当单台机器上所有入口流量的 QPS 达到阈值即触发系统保护
- CPU usage：当系统 CPU 使用率超过阈值即触发系统保护（取值范围 0.0-1.0）

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Sentinel增加系统规则.png" alt="Cloud-Sentinel增加系统规则" style="zoom:50%;" />



详细内容参考文档：https://sentinelguard.io/zh-cn/docs/system-adaptive-protection.html



****



#### 服务调用

消费者需要进行服务调用

* 引入 pom 依赖：

  ```xml
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
  ```

* application.yml：激活 Sentinel 对 Feign 的支持

  ```yaml
  feign:
    sentinel:
      enabled: true
  ```

* 主启动类：加上 @EnableFeignClient 注解开启 OpenFeign

* 业务类：

  ```java
  // 指明调用失败的兜底方法在PaymentFallbackService，使用 fallback 方式是无法获取异常信息的，
  // 如果想要获取异常信息，可以使用 fallbackFactory 参数
  @FeignClient(value = "nacos-payment-provider", fallback = PaymentFallbackService.class)
  public interface PaymentFeignService {
      // 去生产则服务中找相应的接口，方法签名一定要和生产者中 controller 的一致
      @GetMapping(value = "/paymentSQL/{id}")
      public CommonResult<Payment> paymentSQL(@PathVariable("id") Long id);
  }
  
  ```

  ```java
  @Component   //不要忘记注解，降级方法
  public class PaymentFallbackService implements PaymentFeignService {
      @Override
      public CommonResult<Payment> paymentSQL(Long id) {
          return new CommonResult<>(444,"服务降级返回,没有该流水信息-------PaymentFallbackSe
  ```



****



#### 持久化

配置持久化：

* 引入 pom 依赖：

  ```xml
  <!--SpringCloud ailibaba sentinel-datasource-nacos 后续做持久化用到-->
  <dependency>
      <groupId>com.alibaba.csp</groupId>
      <artifactId>sentinel-datasource-nacos</artifactId>
  </dependency>
  ```

* 添加 Nacos 数据源配置：

  ```yaml
  server:
    port: 8401
  
  spring:
    application:
      name: cloudalibaba-sentinel-service
    cloud:
      nacos:
        discovery:
          server-addr: localhost:8848 #Nacos服务注册中心地址
      sentinel:
        transport:
          dashboard: localhost:8080
          port: 8719
        # 关闭默认收敛所有URL的入口context，不然链路限流不生效
        web-context-unify: false
        # filter:
          # enabled: false  # 关闭自动收敛
  
        #持久化配置
        datasource:
          ds1:
            nacos:
              server-addr: localhost:8848
              dataId: cloudalibaba-sentinel-service
              groupId: DEFAULT_GROUP
              data-type: json
              rule-type: flow
  ```





****





### Seata

#### 分布事物

一个分布式事务过程，可以用分布式处理过程的一 ID + 三组件模型来描述：

* XID (Transaction ID)：全局唯一的事务 ID，在这个事务ID下的所有事务会被统一控制

* TC (Transaction Coordinator)：事务协调者，维护全局和分支事务的状态，驱动全局事务提交或回滚

* TM (Transaction Manager)：事务管理器，定义全局事务的范围，开始全局事务、提交或回滚全局事务

* RM (Resource Manager)：资源管理器，管理分支事务处理的资源，与 TC 交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚

典型的分布式事务流程：

1. TM 向 TC 申请开启一个全局事务，全局事务创建成功并生成一个全局唯一的 XID
2. XID 在微服务调用链路的上下文中传播（也就是在多个 TM，RM 中传播）
3. RM 向 TC 注册分支事务，将其纳入 XID 对应全局事务的管辖
4. TM 向 TC 发起针对 XID 的全局提交或回滚决议
5. TC 调度 XID 下管辖的全部分支事务完成提交或回滚请求

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-分布式事务流程.png" alt="Cloud-分布式事务流程" style="zoom:67%;" />



***



#### 基本配置

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案

下载 seata-server 文件修改 conf 目录下的配置文件

* file.conf：自定义事务组名称、事务日志存储模式为 db、数据库连接信息

  **事务分组**：seata 的资源逻辑，可以按微服务的需要，在应用程序（客户端）对自行定义事务分组，每组取一个名字

  <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Seata配置文件.png" alt="Cloud-Seata配置文件" style="zoom:50%;" />

* 数据库新建库 seata，建表 db_store.sql 在 https://github.com/seata/seata/tree/2.x/script/server/db 目录里面

* registry.conf：指明注册中心为 Nacos，及修改 Nacos 连接信息

  <img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Seata注册中心配置.png" alt="Cloud-Seata注册中心配置" style="zoom: 71%;" />

启动 Nacos 和 Seata，如果 DB 报错，需要把将 lib 文件夹下 mysql-connector-java-5.1.30.jar 删除，替换为自己 MySQL 连接器版本

<img src="https://seazean.oss-cn-beijing.aliyuncs.com/img/Frame/Cloud-Seata配置成功.png" alt="Cloud-Seata配置成功" style="zoom:80%;" />



官网：https://seata.io

下载地址：https://github.com/seata/seata/releases

基本介绍：https://seata.io/zh-cn/docs/overview/what-is-seata.html



***



#### 基本使用

两个注解：

* Spring 提供的本地事务：@Transactional

* Seata 提供的全局事务：**@GlobalTransactional**

搭建简单 Demo：

* 创建 UNDO_LOG 表：SEATA AT 模式需要 `UNDO_LOG` 表，如果一个模块的事务提交了，Seata 会把提交了哪些数据记录到 undo_log 表中，如果这时 TC 通知全局事务回滚，那么 RM 就从 undo_log 表中获取之前修改了哪些资源，并根据这个表回滚

  ```sql
  -- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
  CREATE TABLE `undo_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `branch_id` bigint(20) NOT NULL,
    `xid` varchar(100) NOT NULL,
    `context` varchar(128) NOT NULL,
    `rollback_info` longblob NOT NULL,
    `log_status` int(11) NOT NULL,
    `log_created` datetime NOT NULL,
    `log_modified` datetime NOT NULL,
    `ext` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
  ```

* 引入 pom 依赖：

  ```xml
  <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
      <version>${spring-cloud-alibaba.version}</version>
  </dependency>
  ```

* application.yml：

  ```yaml
  spring:
    application:
      name: seata-order-service
    cloud:
      alibaba:
        seata:
          # 自定义事务组名称需要与seata-server中file.conf中配置的事务组ID对应
          # vgroup_mapping.my_test_tx_group = "my_group"
          tx-service-group: my_group
      nacos:
        discovery:
          server-addr: localhost:8848
    datasource:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/seata_order?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
      username: root
      password: 123456
  ```

* 构建三个服务：

  ```java
  // 仓储服务
  public interface StorageService {
      // 扣除存储数量
      void deduct(String commodityCode, int count);
  }
  
  // 订单服务
  public interface OrderService {
  	// 创建订单
      Order create(String userId, String commodityCode, int orderCount);
  }
  
  // 帐户服务
  public interface AccountService {
      // 从用户账户中借出
      void debit(String userId, int money);
  }
  ```

* 业务逻辑：增加 @GlobalTransactional 注解

  ```java
  public class OrderServiceImpl implements OrderService {
  	@Resource
      private OrderDAO orderDAO;
  	@Resource
      private AccountService accountService;
      
  	@Transactional(rollbackFor = Exception.class)
      public Order create(String userId, String commodityCode, int orderCount) {
          int orderMoney = calculate(commodityCode, orderCount);
  		// 账户扣钱
          accountService.debit(userId, orderMoney);
  
          Order order = new Order();
          order.userId = userId;
          order.commodityCode = commodityCode;
          order.count = orderCount;
          order.money = orderMoney;
  
          return orderDAO.insert(order);
      }
  }
  ```

  ```java
  public class BusinessServiceImpl implements BusinessService {
  	@Resource
      private StorageService storageService;
  	@Resource
      private OrderService orderService;
  
      // 采购，涉及多服务的分布式事务问题
      @GlobalTransactional
      @Transactional(rollbackFor = Exception.class)
      public void purchase(String userId, String commodityCode, int orderCount) {
          storageService.deduct(commodityCode, orderCount);
          orderService.create(userId, commodityCode, orderCount);
      }
  }
  ```





详细示例参考：https://github.com/seata/seata-samples/tree/master/springcloud-nacos-seata



# SpringCloud八股文

## 配置中心如何实现自动刷新？

1. **配置变更推送**

**轮询机制**：应用定期轮询配置中心的配置变更，检测配置是否发生了变化。这种方法较为简单，但可能会增加配置中心的负载和延迟。

**推送机制**：配置中心主动推送配置变更通知到客户端。这种方式效率更高，可以实时地将变更通知到客户端。

2. **客户端通知**

**长轮询**：客户端在与配置中心建立连接后，保持长时间的连接，当配置中心有变更时，立即通知客户端。这种机制减少了轮询的频率。

**消息总线**：利用消息总线（如 RabbitMQ、Kafka）将配置变更事件推送到客户端。这种方式可以在大规模系统中实现高效的通知。

3. **配置更新**

**配置注入**：通过 Spring 的 @RefreshScope 注解或者 Nacos 的动态配置更新机制，确保配置变更后，应用能够动态地注入新的配置值。

**动态刷新**：当检测到配置变更时，应用会重新加载配置并应用到相应的组件或服务中。

## 注册中心挂了怎么办？

1. **实现高可用架构**

**集群部署**：部署多个 Nacos 实例，形成一个集群。通过集群模式，Nacos 可以提供更高的可用性和容错能力。Nacos 支持集群模式，可以通过配置多个节点来实现高可用。

**负载均衡**：在客户端配置中，使用负载均衡策略来访问多个注册中心节点。这样即使某个节点挂掉，其他节点可以继续提供服务。

**数据同步**：确保所有 Nacos 实例的数据同步，避免数据不一致的问题。Nacos 提供了主从复制机制，可以将数据同步到从节点上。

2. **使用持久化存储**

**配置存储**：Nacos 提供了对不同存储介质的支持（如 MySQL、嵌入式数据库）。确保配置中心使用持久化存储，这样即使某些节点挂掉，也能从持久化存储中恢复数据。

3. **配置中心的故障转移**

**故障转移机制**：实现自动化的故障转移机制，确保当主注册中心挂掉时，系统能够自动切换到备用节点。这需要在客户端配置中指定备用节点或使用负载均衡机制。

4. **服务的容错和重试机制**

**熔断和重试**：在服务调用中实现熔断和重试机制。例如，使用 Hystrix 或 Resilience4j 来处理服务调用的失败情况，避免因注册中心故障导致的系统崩溃。

**服务降级**：实现服务降级策略，当注册中心不可用时，能够提供备份服务或降级处理，确保系统能够继续运行。

5. **监控和告警**

**监控**：设置对注册中心的监控，使用监控工具（如 Prometheus、Grafana）来实时监控注册中心的状态和性能指标。

**告警**：配置告警机制，当注册中心出现故障或性能异常时，能够及时通知运维人员进行处理。

6. **快速恢复**

**备份与恢复**：定期备份注册中心的数据，确保在故障发生时能够快速恢复。确保备份数据的可靠性，并测试恢复过程。

**自动化恢复**：实现自动化的恢复机制，当检测到注册中心故障时，可以自动启动备用节点或重新启动服务。

## Ribbon负载均衡原理是什么？

1. **服务列表获取**

**从注册中心获取服务实例**：Ribbon 从服务注册中心（如 Eureka、Consul、Zookeeper）获取可用的服务实例列表。这些实例通常包含服务的 IP 地址和端口号。

2. **负载均衡策略**

**选择策略**：Ribbon 提供了多种负载均衡策略，用于决定将请求路由到哪个服务实例。常见的负载均衡策略包括：

​	•**轮询（Round Robin）**：按顺序轮流选择服务实例。

​	•**随机（Random）**：随机选择一个服务实例。

​	•**加权轮询（Weighted Response Time）**：根据服务实例的响应时间加权选择实例。

​	•**响应时间（Response Time）**：选择响应时间最短的实例。

​	•**会话粘性（Sticky Sessions）**：保持会话的粘性，将来自同一客户端的请求始终路由到同一服务实例。

3. **请求路由**

**请求拦截**：Ribbon 在客户端拦截发出的请求，根据负载均衡策略选择一个服务实例。

**负载均衡器选择**：Ribbon 的 ILoadBalancer 接口负责选择一个具体的服务实例。Ribbon 提供了不同的实现类，如 RoundRobinRule 和 RandomRule，以支持不同的负载均衡策略。

**请求转发**：将请求转发到选择的服务实例上，并处理响应。

4. **健康检查**

**实例健康检查**：Ribbon 通过健康检查机制定期检查服务实例的健康状态。只有健康的实例才会被纳入负载均衡的候选列表中。

**动态调整**：当服务实例的健康状态发生变化时，Ribbon 会自动更新服务实例列表，并根据新的健康状态重新进行负载均衡。

5. **负载均衡配置**

**配置选项**：Ribbon 允许用户通过配置文件或代码配置负载均衡策略和参数。例如，可以通过 application.yml 文件配置负载均衡策略：

## 配置中心如何保证数据安全？

Nacos 的 RBAC（Role-Based Access Control，基于角色的访问控制）是用于管理和控制用户对 Nacos 资源的访问权限的一种机制。RBAC 通过定义角色和角色权限，将用户分配到不同的角色，从而控制用户对系统资源的访问。

## 注册中心原理是什么？

**1. 服务注册**

​	**服务实例注册**：服务提供者在启动时，将其服务实例信息（如服务名称、IP 地址、端口、健康检查信息等）注册到 Nacos 注册中心。服务注册通常通过 Nacos 提供的 SDK 或 HTTP API 完成。

​	**心跳检测**：服务提供者定期向 Nacos 发送心跳请求，以保持其注册信息的有效性。如果 Nacos 未接收到心跳请求，会将服务标记为不可用。

**2. 服务发现**

​	**服务查询**：服务消费者（客户端）可以通过 Nacos 查询特定服务的实例列表。Nacos 提供了多种查询方式，包括 HTTP API 和客户端 SDK。

​	**动态更新**：当服务实例的状态发生变化（如新增、删除或状态变更）时，Nacos 会更新服务实例列表，并推送更新通知给服务消费者。服务消费者接收到通知后，可以更新本地的服务实例缓存。

**3.负载均衡**

​	**客户端负载均衡**：Nacos 提供服务实例列表，客户端可以基于这些实例实现负载均衡。Nacos 支持与负载均衡器（如 Ribbon）集成，将请求均匀地分发到多个服务实例。

## 配置中心的原理是什么？

**1. 配置管理**

​	**配置存储**：Nacos 允许用户将应用配置集中存储在配置中心中。这些配置可以是键值对形式的配置项，也可以是 YAML、JSON 等格式的配置文件。

​	**配置版本控制**：Nacos 支持配置的版本管理，每次配置变更都会创建一个新的版本。用户可以查看历史版本并回滚到之前的版本。

**2. 配置发布与管理**

​	**配置发布**：用户可以通过 Nacos 的控制台、API 或 SDK 发布和修改配置。配置发布后，Nacos 会将最新的配置保存在配置中心中。

​	**动态更新**：Nacos 支持配置的动态更新。用户在配置中心修改配置后，Nacos 会将更新推送到所有相关的客户端，确保客户端获取到最新的配置。

**3. 客户端获取配置**

​	**配置拉取**：客户端定期从 Nacos 拉取最新的配置。客户端可以通过 Nacos 提供的 SDK 或 HTTP API 获取配置数据。

​	**配置推送**：Nacos 还支持配置的实时推送功能。当配置发生变化时，Nacos 会主动推送配置更新通知到客户端，客户端根据通知更新本地配置。

**4. 配置的加密和解密**

​	**敏感数据加密**：Nacos 支持对敏感配置项进行加密存储。用户可以配置加密密钥，确保配置数据的机密性。

​	**解密处理**：客户端在获取配置时，Nacos 会解密加密的配置项并提供给客户端使用。
