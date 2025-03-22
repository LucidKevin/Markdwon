# MySQL

## DDL

- 查询所有数据库

```sql
show databases;
```

- 查询当前数据库

```sql
show databases();
```

- 创建数据库

```sql
create database [if not exists] 数据库名 [character set utf8mb4] [collate 排序规则];
```

- 删除数据库

```sql
drop database [if exists] 数据库表名;
```

- 使用数据库

```sql
use 数据库表名;
```

- 创建表

```sql
CREATE TABLE `dict` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级id',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '名称',
  `value` bigint(20) DEFAULT NULL COMMENT '值',
  `dict_code` varchar(20) DEFAULT NULL COMMENT '编码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT '1' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`),
  KEY `idx_dict_code` (`dict_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构表';
```

- 查询所有表

```sql
show tables;
```

- 查询表结构

```sql
desc 表名;
```

- 查询建表语句

```sql
show create table 表名;
```

- 删除表

```sql
drop table [if exists] 表名;
```

## DML

- 添加数据

```sql
INSERT INTO `dict` (`id`,`parent_id`,`name`,`value`,`dict_code`,`create_time`,`update_time`,`is_deleted`) VALUES (1,0,'全部分类',1,'ROOT','2019-06-10 10:43:31','2020-06-08 17:26:39',0),(86,1,'省',86,'Province','2019-06-10 10:43:35','2020-06-23 15:43:48',0),(10000,1,'医院等级',NULL,'Hostype','2019-06-10 10:43:32','2020-06-15 11:26:44',0);
```

- 修改数据

```sql
update 表名 set 字段名1=值1, 字段名2=值2,... [where 条件];
```

- 删除数据

```sql
delete from 表名 [where 条件];
```

## DQL

- 查询

```sql
select [distinct] 字段1 [别名1], 字段2 [别名2],... from 表名 where 条件 group by 分组字段列表 having 分组后条件列表 order by 排序字段 排序方式 limit 分页参数; 
```

## DCL

- 查询用户

```sql
use mysql;
select * from user;
```

- 创建用户

```sql
create user 用户名@主机名 identified by 密码;
```

- 修改用户密码

```sql
alter user 用户名@主机名 identified with 旧密码 by 新密码;
```

- 删除用户

```sql
drop user 用户名@主机名;
```



## 外键

- 添加外键

```sql
alter table 表名 add constraint 外键名称 foreign key(外键字段名) references dept(id) 主表(主表列名) on update cascade on delete cascade;
```

- 删除外键

```sql
alter tale emp drop foreign key 外键名称;
```

## 连接

- 内连接

```sql
select 字段列表 from 表1,表2 where 条件..;
```

- 左外连接

```sql
select 字段列表 from 表1 left join 表2 on 条件;
```

- 右外连接

```sql
select 字段列表 from 表1 right join 表2 on 条件;
```

- 内连接

```sql
select 字段列表 from 表A 别名A join 表A 别名B on 条件...;
```

- 联合查询

```sql
select 字段列表 from 表A ...
union [all]
select 字段列表 from 表B ...;
```

## 事务

### 事务的基本使用

- 查看事务设置

```sql
select @@autocommit;
```

- 设置事务为手动

```sql
set @@autocommit = 0;
```

- 提交事务

```sql
commit; 
```

- 回滚事务

```sql
rollback;
```

- 开启事务

```sql
start transaction;
```

- 查看事务隔离级别

```sql
select @@transaction_isolation
```

- 设置事务隔离级别

```sql
set [session|global] transaction isolation levl {read uncommitted | read committed | repeatable read | serializable}
```



### 事务的四大特性（ACID）

1. 原子性：事务是不可分割的最小操作单元，要么全部成功，要么全部失败

2. 一致性：事务完成时，必须使所有数据保持一致状态
3. 隔离性：保证事务在独立环境下运行
4. 持久性：事务一旦提交或回滚，它对数据库中的数据改变是永久的

### 并发事务问题

1. 脏读：一个事务读到另一个事务还没提交的数据
2. 不可重复读：读同一个数据，两次结果不同
3. 幻读：一开始没读到，后来又读到了

### 事务隔离级别

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/bb40ac59-e4b6-4d9a-93a3-eb231f62ca3c.png)

### 事务原理

1. redo log（持久性）：记录的是事务提交时数据页的物理修改，是用来实现事务的持久性。该日志由两部分组成：重做日志缓冲以及重做日志文件，前者在内存，后者在磁盘。当事务提交之后会把所有修改信息都存到该日志文件中，用于刷新脏页到磁盘，发生错误时，进行数据恢复使用
2. undo log（原子性）：用于记录数据被修改前的信息，作用：提供回滚和MVCC，与redo log不同，是逻辑日志。可以认为当delete一条记录时，undo log中会出现一条对应的insert的记录。

### MVCC

多版本并发控制。指的是维护一个数据的多个版本，使得读写操作没有冲突，快照读为Mysql实现MVCC提供了一个非阻塞读功能，MVCC的具体实现，还依赖于数据库记录的三个隐式字段，undo log日志，readView

1. 隐式字段：`DB_TRX_ID`最近修改事务的ID，`DB_ROLL_PTR`回滚指针，`DB_ROW_ID`隐藏主键
2. undo log文件：回滚指针指向undo log链
3. read view：用于记录并维护系统当前活跃的事务（未提交）id，包括四个字段：`m_ids`当前活跃事务ID集合, `min_trx_id`最小活跃事务, `max_trx_id`预分配的事务ID, `creator_trx_id` readview创建者ID![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/d371c4b2-f48b-49fb-9727-a681e5037591.png)

- 查看ibd文件

```sql
ibd2sdi stu.ibd
```

### 锁

- 悲观锁：每次去取数据的时候都认为别人会修改，所以每次拿数据的时候都会上锁
- 乐观锁：每次取数据都任务别人不好修改，所以不会上锁，但是更新会判断在此期间有没有人更新数据，可以使用版本号

## 索引

### 索引分类

1. 主键索引：针对表中主键创建的索引
2. 唯一索引：避免同一个表中某列数据重复
3. 聚集索引：将数据存储与索引放在一起，索引结构的叶子节点保存行数据，默认为主键索引
4. 非聚集索引：将数据与索引分开存储， 索引结构的叶子节点对应的是主键
5. 前缀索引：将字符串的一部分作为前缀索引
6. 联合索引：当索引包含多个字段时，称为联合索引
7. 哈希索引：通过hash算法定位到某个链表

### 索引语法

- 创建索引

```sql
create [unique | fulltext] index index_name on table_name (index_col_name, ...)
```

- 查看索引

```sql
show index from table_name;
```

- 删除索引

```
drop index index_name on table_name;
```

### SQL性能分析

- SQL执行频率

```sql
show global status like 'Com_______' # 7个下划线
```

- 慢查询日志

```sql
# 需要在/etc/my.cnf中配置
# 开启mysql慢查询日志开关
show_query_log = 1
# 设置慢日志查询时间为2秒
long_query_time = 2
```

- profile详情

```sql
# 查看profile设置
select @@have_profiling;
# 默认是关闭的
set profiling = 1;
# 查看SQL耗时情况
```

- explain

```sql
# 直接在select语句之前加上关键字explain
explain select 字段列表 from 表名 where 条件;
```

1. id：select的查询序列号，相同id执行顺序从上往下，不同id，越大越先执行
2. select type：select的类型，simple简单表，primary主查询，union，subquery子查询
3. type：连接类型，性能由好到差 null，system，const，eq_ref，ref，range Index，all
4. possible_key：可能使用的索引
5. key：实际使用的索引
6. key_len：表示索引中使用的字节数，
7. row：执行查询的行数，是一个估计值
8. filtered：表示返回结果行数占需读取行数的百分比

### 索引的使用规则

- 索引失效

1. 最左前缀法则：当索引了多列（联合索引），要遵守最左前缀法则，是指从查询的最左列开始，并且不跳过索引中的列

2. 范围查询：当出现范围查询（<,>）范围查询右侧的索引时效

3. 索引列运算：当在索引列运算，索引将失效
4. 模糊查询：尾部模糊查询不会失效，头部模糊查询索引失效
5. or连接：其中有一个没有索引其他的就不会用到

## SQL优化

- 插入优化
  - 批量插入数据应使用主键顺序插入
  - 大批量插入数据

```sql
# 客户端连接服务器加上参数
mysql --local-infile -u root -p
# 设置全局参数local_infile为1，开启从本地加载文件导入数据的开关
set global local_infile = 1;
# 执行load指令将准备好的数据加载到表结构中
load data local infile '文件路径.log' into table 'tb_user' fields terminated by ',' lines terminated by '\n';
```

- 主键优化：主键尽量短，插入数据尽量顺序插入，尽量不要用uuid或其他自然主键

- order by优化：使用索引直接通过索引返回数据，需要将将返回的结果在排序缓冲区排序
- group by优化：索引
- limit 优化：覆盖索引+子查询
- count优化：性能count(*) = count(1) > count(主键) > count(字段)
- update优化：尽量根据主键进行数据更新

## 日志

### 错误日志 

错误日志：记录了myqld运行过程中发生的相关错误

- 查看错误日志参数

```sql
show variables like '%log_error%'
```

### 二进制日志

二进制日志：记录了所有DDL和DML语句，但是不包含查询语句，默认是开启的

作用：1.数据恢复 2.mysql的主从复制

- 查看二进制日志参数

```sql
show variables like '%binlog_format%'
```

### 查询日志

查询日志：查询日记中记录了客户端所有操作语言，默认是关闭

- 查询日志的开启

```sql
# 修改mysql的配置文件 /etc/my.cnf , 1表示开启，0表示关闭
general_log = 1
# 设置日志的文件名
general_log_file=mysql_query.log
```

### 慢查询日志

慢查询日志：记录了所有执行时间超过参数long_query_time设置值，并且扫描记录数不小于min_examined_row_limit的所有sql语句的日志，默认未开启

```sql
# 慢查询日志
show_query_log=1
# 执行时间参数
long_query_time=2
```

## 主从复制

主从复制是指将主数据库的DDL和DML通过二进制日志传到从服务器中，然后从库对这些日志重新执行，从而使得从库和主库保持一致。

- 主从复制的优点

1. 主库出现问题，可以迅速切换
2. 实现读写分离，降低主库访问压力
3. 可以从从库中执行备份，避免备份期间影响主库服务

- 主从复制原理

1. 主库提交事务后，将数据变更记录在binlog文件中
2. 从库读取binlog文件，并写入relay log文件中
3. 从库读取relay log并执行语句 

## 分库分表

分库分表的中心思想就是将数据分散存储，使得单一数据库/表的数据量变小来缓解单一数据库性能的问题，从而达到提升数据库性能的目的

- 垂直分库：以表为依据，根据业务将不同表拆分到不同库中
- 垂直分表：以字段为依据，根据字段属性将将不同字段拆分到不同表中

- 水平分库：以字段为依据，按照一定策略，将一个库的数据拆分到多个库
- 水平分表：以字段为依据，按照一定策略，将一个表的数据 拆分到多个表



## 读写分离

读写分离，简单来说就是把对数据库的读操作和写操作分开后，以对应不同的数据库服务器，主数据库负责写操作，从数据库负责读操作的

- 双主双从：主机1用于处理所有写请求，从机1和主机2以及从机2负责读请求，当主机1宕机后，主机2负责写请求，从机负责读请求

## 函数

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/5a71f9be-7822-4260-9c97-b74b95e8e3f0.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/575b9c40-a58d-48f1-8921-7760c015084f.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/ad3b7a7f-b2cf-4e7b-8bb3-b9e52c798074.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/3c9d89c9-8d04-4ecb-93a1-5e1db984dbdc.png)

## 查询条件

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/5a3f472d-5464-4454-99ff-d11c1a0d8a48.png)

## 数据类型

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/61845518-7a5a-4b04-80a4-fda22b3a8080.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/877dbca5-5f59-452e-b67a-c22915616bc3.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/6558653a-13bb-4e1a-b4fc-9a16353e3a32.png)

## 约束

- 内键约束

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/1282b833-f909-4a8a-b39b-f06e00282c51.png)

- 外键约束

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/363ebfe7-638a-4377-b738-d24ac9d5b752.png)

## 存储引擎

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/f22d63db-2970-4167-91db-13a61628686f.png)



# Mysql八股文

### 索引创建的原则

- 数据量较大，且查询比较频繁的表
- 常作为查询条件，排序，分组的字段
- 字段内容区分度高
- 内容较长，使用前缀索引
- 尽量使用联合索引

### 什么情况下索引会失效？

- 违反最左前缀法则
- 范围查询右边的列，不能使用索引
- 不要在索引列上进行运算操作
- 字符串不加单引号
- 以%开头模糊查询

### undo log和redo log的区别？

**Undo Log**（一致性和原子性）：

​	1.当一个事务执行写操作时，先将修改前的数据写入 Undo Log。

​	2.如果事务被中止或者回滚，系统通过 Undo Log 将数据恢复为修改前的状态。

​	3.Undo Log 的生命周期与事务保持一致，当事务提交后，Undo Log 的数据可以被清理。

**Redo Log**（持久性）：

​	1.在事务提交之前，数据库系统会将数据的修改记录（即新值）写入 Redo Log，并将 Redo Log 刷入磁盘（称为“预写日志”机制，Write-Ahead Logging, WAL）。

​	2.如果事务提交后系统崩溃，通过 Redo Log 进行重做，将已提交事务的修改重新写入磁盘，以确保数据一致性。

​	3.Redo Log 是持久化日志，在事务提交后仍然保留，直到相应的数据被成功写入磁盘。、

### 什么是MVCC？

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/701ea4c0-0d17-471d-b987-95e06cf6efa4.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/02ac80b2-6062-43a0-9993-165a159a7633.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/1e151b72-1214-4c70-81c4-11515dff544d.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/77ebd9e0-a696-49b5-8e6d-2d0017c513a4.png)

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/4de15c2f-ff9e-4abb-9ffe-3e018a49fa29.png)

### 主从同步原理

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/adb8b194-48c8-4a26-93eb-9b9a6db2bdd3.png)















