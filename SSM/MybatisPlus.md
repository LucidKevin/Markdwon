# MybatisPlus

## MP的快速入门

### MP的使用

1. 引入依赖

```xml
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.3.1</version>
        </dependency>
```

2. 定义mapper

```java
public interface UserMapper extends BaseMapper<User> {
}
```

### 常见注解

MybatisPlus通过扫描实体类，并基于反射获取实体类信息作为数据库表信息

- 类名驼峰转下划线作为表名
- 名id作为主键
- 变量名驼峰转下划线作为表的字段名

**注解**

- @TableName：用来指定表名

- @TableId：用来指定主键

- @TableField：用来指定普通字段

### 常见配置

```yaml
mybatis-plus:
  type-aliases-package: com.itheima.mp.domain.po  # 指定MyBatis的类型别名包
  mapper-locations: "classpath*:/mapper/**/*.xml" # 指定Mapper XML文件的位置
  configuration:
    map-underscore-to-camel-case: true            # 是否开启下划线和驼峰的映射
    cache-enabled: false                          # 是否开启二级缓存
  global-config:
    db-config:
      id-type: assign_id                          # id为雪花算法生成
      update-strategy: not_null                   # 更新策略：只更新非空字段
```

## 核心功能

### 条件构造器

- QueryWrapper

```java
@Test
    void testQueryWrapper(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "jack");
        userMapper.selectCount(wrapper);
    }
```

- LambdaQueryWrapper

```java
    @Test
    void testLambdaQueryWapper(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .select(User::getId)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        List<User> userList = new ArrayList<>();
    }
```

### Service接口

1. 创建Service接口继承IService

```java
public interface UserService extends IService<User> {}
```

2. 创建实现类，继承ServiceImpl

```java
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {
}
```

## 扩展功能

### 代码生成

1. Idea中安装插件MybatisPlus![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/ed938c0e-b1ef-412c-a5aa-695926f641bb.png)

2. 随后打开other进行配置，最后生成![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/319f5d60-3298-406e-89d4-d1c5362c541f.png)

### DB静态工具

Db静态工具能够简化数据库的开发，以下是一个例子

```java
@Test
void testDb(){
        List<Address> list = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, 2)
                .list();
}
```

### 枚举处理器

1. 创建枚举类型

```java
@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    FROZEN(2, "冻结"),
    ;
    private final int value;
    private final String desc;
    UserStatus(int value, String desc){
        this.value = value;
        this.desc = desc;
    }
}
```

2. 在配置文件中进行配置

```yaml
mybatis-plus:
  configuration:
    default-enum-type-handler: com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler
```

3. 将原有状态改为枚举类型

```java
private UserStatus status;
```

### JSON处理器

1. 首先需要在字段面前添加注解

```java
    @TableField(typeHandler = JacksonTypeHandler.class) //将该数据转为json数据
    private String info;
```

2. 在表名前添加注解

```java
@TableName(value = "user", autoResultMap = true) // 开启map转java对象的隐射
```

### 分页插件

1. 首先在配置类中注册核心插件，同时添加分页插件

```java
@Configuration
public class MybatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //1.初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //2.添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
```

2. 构建Page对象，获取page，然后用getRecords函数获取USer对象

```java
@Test
    void testPage(){
        Page<User> page = Page.of(1, 5);
        Page<User> p = userService.page(page);
        List<User> list = p.getRecords();
    }
```

