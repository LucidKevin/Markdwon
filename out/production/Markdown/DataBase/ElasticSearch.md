# ElasticSearch

## 入门

###  概念

- ES具有支持分布式，可水平扩展的优势，同时提供restful接口，可被任何语言调用

- 官方链接 https://www.elastic.co/cn/

- ES结合kibana，Logstash，Beats是一套技术栈，叫做ELK，被广泛应用在日志数据分析，实时监控等领域，通常kibna用于数据可视化，ES用于存储，计算，搜索数据，其他两个用于数据的抓取

- 是一款开源搜索引擎，可以帮助我们从海量数据中快速找到需要的内容

- 文档：elasticsearch是面向文档存储的，可以是数据库中一条商品数据，一个订单信息，文档数据会被序列化为json格式后存储在elasticsearch中

- 索引：相同类型文档的集合，映射Mapping相当于表结构

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/206d0a62-06f4-4ac8-bce1-8dc44569c05f.png)

### ES的安装

1. 拉取镜像

```
docker pull elasticsearch:7.4.0
```

2. 创建容器

```
docker run -id --name elasticsearch -d --restart=always -p 9200:9200 -p 9300:9300 -v D:\App\elasticsearch-7.4.0\plugins:/usr/share/elasticsearch/plugins -e "discovery.type=single-node" elasticsearch:7.4.0
```

3. 下载kibana

```dockerfile
docker run -d
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601 \
kibana:7.12.1
```

3. 配置kibana.yml文件

```
elasticsearch.hosts: ["http://localhost:9200"]
```

5. 运行kibana访问 http://localhost:5601
6. 安装分词器 https://release.infinilabs.com/analysis-ik/stable/

### 倒排索引

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/04c34aac-7fdf-4b06-ae64-09d8b47780d0.png)

## 索引库操作

- **mapping属性**
  - type：字段数据类型


1. 字符串：text(可分词文本)， keyword(精确值，例如：品牌，国家)

2. 数值：long，interger，short，byte，double，float

3. 布尔：boolean

4. 日期：date

5. 对象：object

   - Index：是否创建索引，默认为true


   - analyzer：使用哪种分词器

   - properties：子字段


- **创建索引库**

```json
put /heima{
	"mappings":{
		"properties":{
			"info":{
				"type":"text"
				"analyzer":"ik_smart"
			}
			"email":{
				"type":"kayword",
				"index":false;
			}
		}
	}
}
```

- 修改索引库

索引库和mapping一旦创建无法修改，但是可以增加新的字段

```sql
put /heima/_mapping
{
	"properties":{
		"age":{
			"type":"integer"
		}
	}
}
```

- 查询索引库

```sql
get /heima
```

- 删除索引库

```
delete /heima
```

## 文档操作

- 新增文档

```sql
post /heima/_doc/1{
	"info":"黑马程序员",
	"email":"zy@itcast.cn",
	"name":{
		"fisrtName":"云",
		"lastName":"赵"
	}
}
```

- 查询文档

```sql
get /heima/_doc/1
```

- 删除文档

```sql
delete /heima/_doc/1
```

- 修改文档

```sql
# 全量修改，会删除旧文档
post /heima/_doc/1{
	"info":"黑马程序员",
	"email":"zy@itcast.cn",
	"name":{
		"fisrtName":"云",
		"lastName":"赵"
	}
}
# 增量修改,修改指定字段值
post /heima/_update/1
{
	"doc":{
		"email":"sdasada/.cn"
}
```

## RestClient操作

- 连接ES

```java
// 创建客户端对象
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
```

- 创建索引

```java
// 发送请求，获取响应
        CreateIndexResponse response = client.indices()
                .create(request, RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        // 响应状态
        System.out.println("操作状态 = " + acknowledged);
        // 关闭客户端连接
        client.close();
```

- 查询索引

```java
      // 查询索引 - 请求对象
        GetIndexRequest request = new GetIndexRequest("user");
        // 发送请求，获取响应
        GetIndexResponse response = client.indices()
          .get(request, RequestOptions.DEFAULT);
        System.out.println("aliases:" + response.getAliases());
        System.out.println("mappings:" + response.getMappings());
        System.out.println("settings:" + response.getSettings());
        client.close();
```

- 删除索引

```java
        // 删除索引 - 请求对象
        DeleteIndexRequest request = new DeleteIndexRequest("user");
        // 发送请求，获取响应
        AcknowledgedResponse response = client.indices()
          .delete(request, RequestOptions.DEFAULT);
        // 操作结果
        System.out.println("操作结果：" + response.isAcknowledged());
        client.close();
```

- 新增文档

```java
						// 新增文档 - 请求对象
            IndexRequest request = new IndexRequest();
            // 设置索引及唯一性标识
            request.index("user").id("1001");

            // 创建数据对象
            User user = new User();
            user.setName("zhangsan");
            user.setAge(30);
            user.setSex("男");

            // Model -> JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String productJson = objectMapper.writeValueAsString(user);
            // 添加文档数据，数据格式为 JSON 格式
            request.source(productJson, XContentType.JSON);
            // 客户端发送请求，获取响应对象
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
```

- 修改文档

```java
  					// 修改文档 - 请求对象
            UpdateRequest request = new UpdateRequest();
            // 配置修改参数
            request.index("user").id("1001");
            // 设置请求体，对数据进行修改
            request.doc(XContentType.JSON, "sex", "女");
            // 客户端发送请求，获取响应对象
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
```

- 删除文档

```java
					// 1.创建请求对象
            DeleteRequest request = new DeleteRequest().index("user").id("1001");
            // 2.客户端发送请求，获取响应对象
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
```

- 批量新增

```java
					// 创建批量新增请求对象
            BulkRequest request = new BulkRequest();
            request.add(new IndexRequest().index("user").id("1001")
                    .source(XContentType.JSON, "name", "zhangsan", "age", 30));
            request.add(new IndexRequest().index("user").id("1002")
                    .source(XContentType.JSON, "name", "lisi"));
            request.add(new IndexRequest().index("user").id("1003")
                    .source(XContentType.JSON, "name", "wangwu"));
            // 客户端发送请求，获取响应对象
            BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
```

- 批量删除

```java
 // 创建批量删除请求对象
            BulkRequest request = new BulkRequest();
            request.add(new DeleteRequest().index("user").id("1001"));
            request.add(new DeleteRequest().index("user").id("1002"));
            request.add(new DeleteRequest().index("user").id("1003"));
            // 客户端发送请求，获取响应对象
            BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
```

- 全量查询

```java
        // 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 请求体的构建由调用者进行
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
```

- 条件查询

```java
			// 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 请求体的构建由调用者进行
        sourceBuilder.query(QueryBuilders.termQuery("age", "30"))
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
```

- 分页查询

```java
				// 创建搜索请求对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 请求体的构建由调用者进行
        sourceBuilder.query(QueryBuilders.matchAllQuery()).from(0).size(2)
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
```

