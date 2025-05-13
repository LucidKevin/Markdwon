# RabbitMQ

## 基本介绍

- RabbitMQ是基于Erlang语言开发，AMQP（Advanced Message Queuing Protocol）协议的开源消息通信中间件，官网地址：http://www.rabbitmq.com/

- 由四个部分组成：virtual-host，publisher，consumer，queue，exchange

**AMQP 协议概述**

RabbitMQ 基于 AMQP 协议，它规定了消息传递的规则和流程。AMQP 是一个面向消息队列的协议，它包括以下几个关键组件：

​	•**Producer（生产者）**：发送消息的一方。

​	•**Consumer（消费者）**：接收和处理消息的一方。

​	•**Broker**：RabbitMQ 作为消息代理（broker），负责接收、存储和分发消息。

​	•**Queue**：存储消息的队列。

​	•**Exchange（交换器）**：负责根据路由规则将消息分发到一个或多个队列。

​	•**Binding（绑定）**：定义了交换器与队列之间的关系，决定消息如何从交换器路由到队列。

**核心组件**

- **Producer**：向 RabbitMQ 中的交换器发送消息。

- **Exchange**：交换器根据预设的路由规则决定消息流向哪些队列。RabbitMQ 支持四种类型的交换器：

- **Direct**：根据路由键精确匹配消息。

- **Fanout**：将消息广播给所有绑定的队列。

- **Topic**：根据模式匹配路由键，适用于主题订阅。

- **Headers**：根据消息头内容匹配，适合复杂的路由需求。

- **Queue**：队列是消息最终存储的地方，消费者会从队列中读取消息。

- **Consumer**：消费者从队列中取出并处理消息。

- **Binding**：将交换器和队列连接起来，指定消息如何从交换器路由到队列。

**架构设计**

​	•**Broker**：RabbitMQ 的消息代理服务，处理生产者发送的消息，管理队列和消费者之间的关系。

​	•**Virtual Host（vhost）**：类似于命名空间的概念，一个 RabbitMQ 服务器可以创建多个虚拟主机，每个虚拟主机可以隔离不同的交换器、队列和权限管理。

​	•**Channel**：信道是 RabbitMQ 与客户端之间通信的虚拟连接，多个信道可以复用同一个 TCP 连接，减小连接成本。

​	•**Connection**：客户端与 RabbitMQ 之间的 TCP 连接，通常会在一条连接上创建多个 Channel，以提高效率。

​	•**Message Acknowledgement（消息确认机制）**：RabbitMQ 确保消息的可靠传递，消费者需要确认已经成功消费了消息。如果确认机制没有收到，RabbitMQ 可以重新将消息投递给其他消费者。

## RabbitMQ的安装

1. RabbitMQ的运行在docker中

```dockerfile
docker run -d --name myrabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

2. 浏览器访问http://localhost:15672，账号和密码都是guest

## JavaAPI

### Java中的使用

1. 引入依赖

```xml
<dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-amqp</artifactId>
         <version>2.7.17</version>
 </dependency>
```

2. 配置RabbitMQ客户端信息

```yaml
spring:
  rabbitmq:
    host: 192.168.0.1
    port: 5672
    virtual-host: /huang
    username: huangkaiwen
    password: 744669
```

3. 发送消息

```java
@ExtendWith(SpringExtension.class) //
@SpringBootTest
public class SendMessage {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void Send() {
        String queueName = "q1";
        String message = "hello";
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

4. 接收消息

```java
@Slf4j
@Component
public class Listener {
    @RabbitListener(queues = "q1")
    public void listen(String msg){
        log.info("Spring 消费者收到消息【"+msg+"】");
    }
}
```

### Work模型

Work模型是指多个消费者绑定到一个队列，默认情况下，RabbitMQ会将消息依次轮询投递给绑定在队列上的每个消费者，但并没有考虑消费者是否处理完消息，因此我们需要修改application.yml

```java
rabbitTemplate.convertAndSend(queueName,message);//发送消息给队列
```

```yaml
spring:
  rabbitmq:
    listener:
      direct:
        prefetch: 1 #每次只能获取一条消息，处理完才能获取下一条
```

### Fanout交换机

Fanout交换机会将接收到的消息广播到每一个跟其绑定的queue

```java
rabbitTemplate.convertAndSend(FanoutExchangeName,"",message); // 发送消息给交换机
```

### Direct交换机

Direct Exchange会将收到的信消息根据规则路由到指定Queue，因此称为定向路由

```java
rabbitTemplate.convertAndSend(DirectExchangeName,"yellow",message); // 发送消息给交换机
```

### Topic交换机

与Direct交换机类似，区别在于routingKey可以是多个单词列表并且以 . 分割，Topic交换机与队列绑定时，可以制定分配符号， # 表示0个或多个单词，* 表示一个单词

### 声明队列和交换机

1. 基于Configuration的方式声明队列和交换机

```java
@Configuration
public class RabbitMQConfiguration{
  //创建exchange
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct.exchange1");
    }
    
    //创建 queue
    @Bean
    public Queue fanoutQ1(){
        return new Queue("fanout.q1");
    }
  
    //绑定
    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(fanoutQ1()).to(directExchange()).with("white");
    }
  
}
```

2. 使用@RabbitListener注解来声明队列和交换机

```java
@RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "q1", durable = "true"),
            exchange =  @Exchange(value = "direct.exchange", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listen3(String msg){
        log.info("Spring 消费者2收到消息【"+msg+"】");
    }
```

### 消息转换器

由于Spring的消息对象是由JDK序列化的，存在风险大，消息长， 可读性差等缺点，所以建议用json序列化代替默认的JDK序列化

1.引入json序列化坐标

```xml
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
```

2. 在publisher和consumer中配置MessageConverter

```java
//配置消息转换器
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
```

### 生产者可靠性

#### 生产者重连

```yaml
spring:
    connection-timeout: 1s # 连接超时时间
    template:
      retry:
        enabled: true # 开启
        max-attempts: 3 # 最大尝试次数
        multiplier: 1 # 下一次等待时长倍数
        max-interval: 10000ms #失败后初试等待时间
```

#### 生产者确认

RabbitMQ在开启确认机制后， 在MQ成功收到消息后会返回确认消息给生产者，返回结果有以下几种情况，

- 消息到了MQ，但是路由失败，会返回路由失败原因，ACK
- 临时消息到了MQ，并成功入队，ACK
- 持久消息到了MQ，并且入队完成持久化，ACK
- 其他均返回NACK

```yaml
spring:
  rabbitmq:
    publisher-returns: true   # 开启return机制
    publisher-confirm-type: correlated # 设置confirm类型 还有simple，none类型 
```

为RabbitTemplate配置ReturnCallback，当MQ消息路由到队列失败时的处理

```java
@Configuration
public class publisherConfiguration implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //设置returnCallback
        rabbitTemplate.setReturnCallback(((message, i, s, s1, s2) -> {
            log.info("消息发送失败，应答码{}, 原因{}，交换机{}，路由键{}，消息{}", message, i, s, s1, s2);
        }));
    }
}

```

为发送的信息配置ConfirmCallback

```java
@Test
    void testPublicsherConfirm() {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                //2.1future发生异常时的处理逻辑，基本不会触发
                log.error("fail");
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                //2.2 future 接收到回执的处理逻辑
                if(result.isAck()){
                    log.debug("消息发送成功");
                }else {
                    log.error("消息发送失败：{}", result.getReason());
                }
            }
        });
        //发送到交换机
        rabbitTemplate.convertAndSend("direct.exchange","Math.mul","hello", cd);
    }
```

### MQ可靠性

在默认情况下，RabbitMQ会将接收到的信息保存在内存中以降低消息收发的延迟，这样会导致两个问题：

1. 一旦MQ宕机，内存中存在的消息会丢失
2. 内存空间有限，当消费者故障的或处理过慢，会导致消息积压，引发MQ阻塞

#### 消息持久化

```java
@Test
void testPageOut(){
    Message message = MessageBuilder
                .withBody("hello".getBytes())
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();
    rabbitTemplate.convertAndSend("q1", message);

}
```

#### Lazy Queue

惰性队列接收到消息后直接存入磁盘而非内存，消费者需要消息时才会从磁盘中读取并加载到内存，MQ3.6之后所有queue都是默认惰性队列，开启持久化和生产者确认时，RabbitMQ只有在消息持久化完成后才会给生产者返回ACK回执

```java
@Bean
    public Queue lazyQueue(){
        return QueueBuilder.durable("q1").lazy() //开启lazy模式
                .build();
    }
```

### 消费者可靠性

为了确认消费者是否成功处理消息，RabbitMQ提供了消费者确认机制，当消费者处理消息结束后，应该向RabbitMQ发送一个回执

- ACK ： 成功处理消息，RabbitMQ从队列中删除该消息
- NACK：消息处理失败，RabbitMQ需要再次投递消息
- REJECT：消息处理失败并拒接该消息，从MQ队列中删除

```yaml
spring:
  rabbitmq:
    password: 744669
    listener:
      simple:
        acknowledge-mode: auto # 开启消费者确认机制
        prefetch: 1 #每次只能获取一条消息，处理完才能获取下一条
```

#### 失败重试机制

当消费者异常后，消息入队然后重新发送，导致mq的消息处理飙升，带来不必要的压力，所以我们可以使用Spring的retry机制，当消费者异常后利用本地重试

```yaml
spring:
  rabbitmq:
    listener:
        retry:
          enabled: true
          initial-interval: 1000ms
          multiplier: 1 
          max-attempts: 3 
          stateless: true # true无状态，false有状态，如果业务中包含事务则改为false
```

### 延迟消息

延迟消息：生产者发送消息时，指定一个时间，消费者不会立刻收到消息，而是在指定时间后才收到消息

延迟任务：设置在一定时间之后才执行的任务

#### 死信交换机

当一个队列中的消息满足下列情况之一时，就会成为死信：

- 消费者使用basic.reject或basic.nack声明消费失败，并且消息的requeue参数设置为false
- 消息是一个过期消息，超时无人消费
- 要投递的队列消息堆积满了，最早的消息可能成为死信

如果一个队列通过dead-letter-exchange属性指定了一个交换机，那么该队列中死信就会投递到这个交换机中，称为死信交换机（Dead letter exchange）![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/c1578076-9294-45c1-b148-0f381d24b9bc.png)



# RabbitMQ八股文

## 消息中间件对比

RabbitMQ、Kafka 和 RocketMQ 都是流行的消息中间件，它们用于分布式系统中的消息传递和事件流处理。它们有着不同的设计目标和应用场景，下面将从**消息模型、性能、架构、场景、可靠性**等多个方面进行比较。

**1. 消息模型**

​	**RabbitMQ**：

​	•基于 **AMQP（Advanced Message Queuing Protocol）** 协议。

​	•消息模型非常灵活，支持 **发布-订阅、点对点、路由、主题模式**。

​	•提供了丰富的交换机类型（如 **direct、topic、fanout、headers**），适用于复杂的消息路由需求。

​	**Kafka**：

​	•基于 **日志** 的模型设计，消息被组织成 **主题 (Topics)**，每个主题中的消息是按顺序存储的。

​	•Kafka 更适合 **发布-订阅** 模式，消息是持久化的，不删除，直到过期或手动清除。

​	•消费者以分区为单位消费消息，每个消费者组可以并行消费多个分区，提高并发。

​	**RocketMQ**：

​	•类似于 Kafka，采用了 **主题 (Topics)** 的概念，支持 **发布-订阅** 和 **顺序消息**。

​	•支持事务消息和延迟消息，适合需要 **事务保证** 或 **定时/延迟处理** 的场景。

​	•使用 Java NIO 的异步 I/O 模型，适合高并发场景。

**2. 性能对比**

**RabbitMQ**：

​	•RabbitMQ 性能相对较低，吞吐量一般适合中小规模的消息传递场景。它在 **高并发、高吞吐量** 的场景中表现稍弱。

​	•适合需要强大路由和复杂消息模式的应用场景。

**Kafka**：

​	•Kafka 的性能最优，能够处理 **百万级 TPS（每秒事务数）** 的吞吐量。由于其设计是顺序写入并且基于磁盘存储，因此非常适合 **海量日志数据处理、实时流数据分析**。

​	•非常适合 **高吞吐量、低延迟** 的场景，尤其是在 **日志收集、事件流处理、实时数据分析** 等领域广泛应用。

**RocketMQ**：

​	•RocketMQ 的性能介于 RabbitMQ 和 Kafka 之间。它支持 **分布式事务** 和 **顺序消息处理**，在需要确保 **消息顺序** 和 **高并发处理** 时表现良好。

​	•适合需要事务一致性、顺序消费、延时消息的业务场景。

**3. 架构**

**RabbitMQ**：

​	•使用的是 **集中式架构**，基于 **单主多从** 的模式来处理消息。

​	•依赖 **Erlang** 语言开发，支持 **集群模式**，但集群扩展性相对较弱。

**Kafka**：

​	•kafka 是一个 **分布式系统**，采用 **无中心化** 的架构设计。所有的节点都可以作为数据处理的 broker，分区机制让其扩展性极强。

​	•消息通过 **分区和副本机制** 保证高可用性和数据一致性。Kafka 更适合大规模 **横向扩展**，节点越多，处理能力越强。

**RocketMQ**：

​	•RocketMQ 同样是分布式架构，支持 **分区、主从复制和多副本机制**，同时具备很好的 **事务一致性支持**。

​	•基于 Java 开发，容易与其他 Java 系统集成，也支持较强的扩展性。

**4. 应用场景**

**RabbitMQ**：

​	•适用于 **需要灵活路由机制、复杂消息模式** 的场景，如任务队列、通知系统、订单处理系统。

​	•适合对延迟要求不高、但消息可靠性要求较高的业务。

​	•常用于微服务架构下的 **短周期任务和事件驱动系统**。

**Kafka**：

​	•广泛应用于 **大数据领域**，如 **日志收集、实时流数据处理、数据管道**，适合需要高吞吐、低延迟、持久化的场景。

​	•支持数据的批量处理和分析，常见于数据分析平台、流式处理框架（如 Apache Flink、Apache Storm）。

​	•**事件流处理** 是 Kafka 的核心优势。

**RocketMQ**：

​	•适用于 **电商、金融系统**，尤其是在需要支持 **分布式事务、消息延迟** 以及 **顺序消息** 场景中表现优越。

​	•常用于 **高并发事务处理、订单管理、支付系统**，例如阿里巴巴的分布式事务消息就是基于 RocketMQ 实现的。

**5. 消息可靠性和一致性**

**RabbitMQ**：

​	•提供 **消息确认机制 (ACK)** 和 **消息持久化** 选项，能够确保消息不会丢失。

​	•消息投递时会确认到达消费者，因此保证了 **消息投递的一致性**，但不具备分布式事务。

**Kafka**：

​	•通过 **副本机制 (Replication)** 和 **消费者确认机制** 保证消息不丢失，适合需要 **高可靠性、高可用性** 的场景。

​	•Kafka 提供 **“至少一次”** 和 **“最多一次”** 投递保证，支持幂等消费，具备良好的数据一致性保障。

**RocketMQ**：

​	•支持 **分布式事务**，可以确保消息在不同节点上的一致性。

​	•提供了多种消息确认和重试机制，以确保消息不会丢失。

**6. 总结**

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/6bef1a4f-1ae9-4df1-8e75-0f3bd802e653.png)
