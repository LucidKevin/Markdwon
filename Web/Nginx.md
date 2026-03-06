# Nginx

## Nginx的概念

**Nginx** 是一个高性能的开源HTTP服务器和反向代理服务器，也可以作为IMAP/POP3邮件代理服务器。它以其高并发性、低资源占用和高稳定性著称，因此被广泛应用于各种规模的网站和服务。

**Nginx的优点**：

1. 速度更快，并发更高（底层采用多进程和I/O多路复用）
2. 配置简单，扩展性强
3. 高可靠性（多进程模式，一个master主进程，N个worker进程）
4. 热部署
5. 低成本

**Nginx的功能**：

1.**静态资源部署**：Nginx特别擅长处理静态文件（如HTML、CSS、JavaScript、图片等），其性能在这方面非常优越。

2.**反向代理**:Nginx可以作为反向代理服务器，将客户端的请求转发给后端服务器（如Tomcat、Node.js、Python的Flask/Django等），并将后端服务器的响应结果返回给客户端。它还能执行负载均衡。

3.**负载均衡**:Nginx可以将流量分配给多个后端服务器，支持多种负载均衡策略，如轮询、IP哈希、最少连接等。

4.**动静分离**:可以将静态内容交给Nginx处理，而动态内容由后端服务器处理，减轻后端服务器的负担，提升性能。

5.**安全性**:Nginx支持SSL/TLS，能够配置HTTPS安全连接。此外，它还支持通过各种模块进行访问控制和限制（如请求速率限制、IP过滤等）。

6.**缓存**:Nginx可以缓存后端服务器的响应结果，从而减轻后端服务器的负载，加速请求的响应。

7.**高并发处理**:Nginx采用异步、非阻塞的事件驱动架构，能够轻松处理成千上万的并发连接，是其高效性能的核心原因之一。

***

## Nginx文件

### 目录结构

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/5f2a3114-7456-48d3-88f0-d920d2c76b02.png)

1. **mime.types**：mime.types 文件存储了文件扩展名与 MIME 类型之间的映射关系，帮助系统或服务器确定在传输或接收文件时应使用何种 MIME 类型。例如，当一个文件上传到服务器时，服务器可以通过扩展名在 mime.types 文件中找到对应的 MIME 类型，并告知客户端该文件的类型。
2. **assess.log**：服务器访问信息
3. **error.log**：访问错误信息
4. **nginx.conf**：基本设置

***

### nginx.conf

***

1. **全局配置**：定义Nginx的全局参数，如工作进程数量，日志路径等

```
user  nginx;   # 定义 Nginx 进程的用户
worker_processes  1;  # 定义工作进程的数量

error_log  /var/log/nginx/error.log warn;  # 错误日志文件及其级别
pid        /var/run/nginx.pid;  # 进程 ID 文件的存放路径
```

- `user`：Nginx 进程运行的用户。

- `worker_processes`：工作进程的数量，通常设置为服务器 CPU 核心数。

- `error_log`：错误日志的路径和日志级别（如 `error`, `warn`, `info`）。

- `pid`：存储 Nginx 进程 ID 的文件路径。



***

2. **events块**:用于定义与网络相关配置

```
events {
    worker_connections  1024;  # 每个工作进程允许的最大连接数
}
```

- `worker_connections`：每个工作进程能够处理的最大并发连接数



***

3. **http块**

```
http {
    include       /etc/nginx/mime.types;  # 包含文件定义 MIME 类型
    default_type  application/octet-stream;  # 默认 MIME 类型

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;  # 访问日志

    sendfile        on;  # 启用高效的文件传输
    keepalive_timeout  65;  # 保持连接的超时时间

    include /etc/nginx/conf.d/*.conf;  # 包含其他配置文件
}
```

- `include`：包含其他配置文件，常用于引入 MIME 类型定义或其他虚拟主机配置。

- `default_type`：未指定 MIME 类型时的默认类型。

- `log_format`：定义访问日志的格式。

- `access_log`：指定访问日志的文件路径和使用的日志格式。

- `sendfile`：启用高效的文件传输。

- `keepalive_timeout`：连接保持的超时时间。



***

4. **server块**

```nginx
server {
    listen       80;  # 监听的端口
    server_name  example.com www.example.com;  # 服务器名称（域名）

    location / {
        root   /usr/share/nginx/html;  # 站点的根目录
        index  index.html index.htm;  # 默认访问的文件
    }

    location /images/ {
        root /data;  # 为 /images 路径指定不同的根目录
    }

    error_page  404  /404.html;  # 自定义错误页面
    location = /404.html {
        root   /usr/share/nginx/html;
    }

    location ~ \.php$ {
        fastcgi_pass   127.0.0.1:9000;  # PHP 解析
        fastcgi_index  index.php;
        fastcgi_param  SCRIPT_FILENAME  $document_root$fastcgi_script_name;
        include        fastcgi_params;
    }
}
```

- `listen`：监听的端口，常见的值是 `80`（HTTP）或 `443`（HTTPS）。

- `server_name`：指定虚拟主机的域名。

- `root`：指定站点根目录。

- `index`：指定默认访问的文件名。

- `location`：定义如何处理某个路径的请求，`/` 表示根路径。

- `error_page`：指定自定义错误页面。

- `fastcgi_pass`：指定 PHP 请求的处理方式，通常通过 FastCGI。

***

5. **location块**

`location` 块用于匹配 URL 路径，并指定如何处理匹配的请求。常见的匹配方式包括：

- 精确匹配 (`=`)：`location = /path`。
- 前缀匹配 (`/`)：`location /path`。
- 正则表达式匹配 (`~`)：`location ~ \.php$`。

**location优先级规则**

- 精确匹配 (=) 优先。

- 前缀匹配 (^~) 次之。

- 正则表达式匹配 (~ 或 ~*) 最后。

- 如果以上都没有匹配，则使用前缀匹配（默认方式

1. **静态文件处理**

```
location /images/ {
    root /var/www;  # root路径处理是:root路径+location路径 /var/www/images
    alias /var/www; # alias的处理结果是用alias路径替换location路径 /var/www
    index index.html;
}
```

请求 /images/photo.png 时，Nginx 会尝试查找 /var/www/images/photo.png

2. **代理请求**

```
location /api/ {
    proxy_pass http://127.0.0.1:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
```

将请求转发到后端服务器，如将 /api/ 下的请求代理到后端 API 服务：

3. **重定向请求**

```
location /old-page {
    return 301 /new-page;
}
```

通过 return 或 rewrite 实现 URL 重定向：此配置将 /old-page 的请求永久重定向到 /new-page。

4. **访问控制**

```
location /admin/ {
    allow 192.168.1.0/24;
    deny all;
}
```

通过 allow 和 deny 来控制 IP 访问权限: 仅允许 IP 地址在 192.168.1.0/24 网段的用户访问 /admin/ 路径。

5. Try_files指令

```
location / {
    try_files $uri $uri/ /index.html;
}
```

该配置表示先尝试请求的文件 uri，如果不存在，则尝试该目录 uri/，最后如果仍然不存在，则返回 /index.html。

6. error_page指令

```
location / {
    error_page 404 /404.html;  
}
```

当发生 404 错误时，返回自定义的 /404.html 页面。

***

## Nginx的使用

### 信号控制

- 获取nginx pid

```
ps -ef | grep nginx
```

- 信号

![](https://gulinall-hkw.oss-cn-shenzhen.aliyuncs.com/64a75812-5864-406c-be2d-6b52a0992b7b.png)

***

### 命令行控制

```java
nginx -h //显示help 
nginx -v //显示版本
nginx -V //显示版本（详细）
nginx -t //测试nginx的配置文件是否有效nginx.conf
nginx -T //测试nginx的配置文件是否有效nginx.conf,同时显示出所有内容
nginx -s signal//signal：stop,quit,reopen,reload
nginx -p prefix//指定nginx的路径
nginx -c filename//指定nginx的配置文件路径
nginx -g 
```

***

## Nginx的功能

### 跨域

**同源策略**

​	是一种约定，是浏览器最核心的安全功能，即协议，域名，端口相同

**跨域问题**

当有两台服务器A，B，当服务器A发送异步请求到服务器B获取数据，如果服务器A和服务器B不满足同源策略，就会出现跨域问题

**解决跨域问题**

添加请求头的ACAO

```nginx
server {
    listen 80;
    server_name example.com;

    location /api/ {
        add_header 'Access-Control-Allow-Origin' '*';
        add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
        add_header 'Access-Control-Allow-Headers' 'Origin, Content-Type, Accept, Authorization';
        
        if ($request_method = 'OPTIONS') {
            return 204;
        }

        # 其他的代理配置或处理逻辑
        proxy_pass http://backend_api;
    }
}
```

​	• Access-Control-Allow-Origin '*': 允许所有来源的请求。如果只希望允许特定的域名，可以将 * 替换为具体的域名，比如 https://example.com。

​	• Access-Control-Allow-Methods 'GET, POST, OPTIONS': 指定允许的 HTTP 方法（如 GET、POST、OPTIONS 等）。

​	•Access-Control-Allow-Headers 'Origin, Content-Type, Accept, Authorization': 指定允许的请求头，通常需要包含 Content-Type、Authorization 等自定义头。

​	• if ($request_method = 'OPTIONS') { return 204; }: 处理 **预检请求** (OPTIONS 请求)。浏览器会在跨域的 POST、PUT 等请求之前发送一个 OPTIONS 请求，询问服务器是否允许跨域访问。返回 204 响应表示不返回内容，且允许继续后续请求。

***

### 防盗链

​	Nginx 实现防盗链的常用方式是通过 **HTTP Referer** 头来判断请求的来源。如果请求的来源不在允许的域名列表中，Nginx 就可以拒绝访问或返回替代内容（如提示图片）。防盗链通常用于保护图片、视频、文件等静态资源，防止其他网站直接引用这些资源。

```nginx
server {
    listen 80;
    server_name example.com;

    location /images/ {
        # 检查 Referer 是否为空或是否来自允许的域名
        valid_referers none blocked example.com *.example.com;

        if ($invalid_referer) {
            # 如果是无效的 Referer，可以选择返回 403 Forbidden 或者重定向到特定的图片
            return 403;
            # 也可以返回一张提示防盗链的图片
            # rewrite ^/ /images/nohotlink.jpg;
        }

        root /var/www/static;
    }
}
```

**解释：**

​	•valid_referers：该指令用于指定允许的 Referer 来源，语法如下：

​	•none：允许 Referer 为空，通常表示用户是直接输入地址访问的。

​	•blocked：允许 Referer 被代理或防火墙隐藏的情况。

​	•example.com 和 *.example.com：允许来自指定域名及其子域名的请求。

​	•if (invalid_referer)：当 Referer 不在 valid_referers 中时，Nginx 会设置 invalid_referer 变量为 1，然后通过 if 语句处理无效 Referer 的情况。

​	•return 403：返回 HTTP 403 Forbidden 错误，拒绝访问。

​	•rewrite ^/ /images/nohotlink.jpg;：重定向到一张提示防盗链的图片（如警告图片）。

## rewrite

Nginx 的 rewrite 指令用于修改请求的 URI 或进行 URL 重定向，通常配合正则表达式使用，以便更灵活地处理请求。它可以在 server、location 或 if 语句中使用。

rewrite 通常用于以下场景：

​	•改变请求路径（如从 /old-path 重写为 /new-path）

​	•执行 301（永久）或 302（临时）重定向

​	•URL 的清理和优化

​	•SEO 优化，避免重复的 URL

**基本语法**

```
rewrite regex replacement [flag];
```

​	•regex：表示要匹配的正则表达式。

​	•replacement：是要将请求 URI 改写成的目标路径或 URL。

​	•flag：是可选的标志位，用于控制 rewrite 的行为。

**rewrite 的常用标志位（flags）**

​	1.**last**：停止当前 rewrite 指令的处理，重启匹配并使用修改后的 URI 继续处理。

​	2.**break**：类似于 last，但它会停止进一步的 rewrite 处理和匹配，仅在当前 location 块内继续处理请求。

​	3.**redirect**：返回 302 临时重定向，浏览器会跳转到新的 URL。

​	4.**permanent**：返回 301 永久重定向，浏览器会更新其缓存，搜索引擎会更新索引。

## return

​	在 Nginx 中，return 指令用于立即返回一个指定的 HTTP 状态码或者重定向到一个新的 URL，它比 rewrite 更简单和直接。当 return 指令被触发时，Nginx 会立即停止处理当前请求，并返回相应的响应给客户端。

**基本语法**

```
return code [text];
return code URL;
```

​	•**code**：表示 HTTP 状态码，如 200（成功）、301（永久重定向）、302（临时重定向）、403（禁止访问）、404（未找到）等。

​	•**text**：可以是返回的纯文本（可选，通常用于特定状态码，如 200）。

​	•**URL**：可以是一个 URL，用于重定向到其他页面。

***

### 反向代理

​	在 Nginx 中，反向代理（reverse proxy）是指 Nginx 充当客户端和后端服务器之间的中间人，接收客户端的请求，并将这些请求转发到后端服务器，然后再将后端服务器的响应返回给客户端。

1. 使用IP地址或域名作为后端服务器

```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        # 将请求代理到后端服务器，后端服务器可以是 IP 地址或域名
        proxy_pass http://127.0.0.1:8080;  # 本地服务器监听 8080 端口
        # 或者使用域名：
        # proxy_pass http://api.example.com;
    }
}
```

2. 配置HTTPS反向代理

```nginx
server {
    listen 443 ssl;
    server_name example.com;

    ssl_certificate /etc/nginx/ssl/example.com.crt;
    ssl_certificate_key /etc/nginx/ssl/example.com.key;

    location / {
        proxy_pass https://backend.example.com;  # 使用 HTTPS 代理
        proxy_ssl_certificate /etc/nginx/ssl/client-cert.pem;  # 客户端证书
        proxy_ssl_certificate_key /etc/nginx/ssl/client-cert.key;
        proxy_ssl_protocols TLSv1.2 TLSv1.3;  # 使用 TLS 1.2 和 1.3
        proxy_ssl_ciphers HIGH:!aNULL:!MD5;  # 使用安全的加密算法
    }
}
```

3. 配置多个后端服务器

```nginx
http {
    upstream backend_cluster {
        server 192.168.1.101;  # 后端服务器 1
        server 192.168.1.102;  # 后端服务器 2
    }

    server {
        listen 80;
        server_name example.com;

        location / {
            proxy_pass http://backend_cluster;
        }
    }
}
```

4. 保留客户端请求的host信息

```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://backend_server;
        proxy_set_header Host $host;  # 保留客户端的 Host 头
        proxy_set_header X-Real-IP $remote_addr;  # 传递客户端 IP
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;  # 传递所有代理服务器的 IP
        proxy_set_header X-Forwarded-Proto $scheme;  # 传递协议信息（HTTP 或 HTTPS）
    }
}
```

5. 设置超时和缓冲区大小

```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://backend_server;

        # 设置超时时间
        proxy_connect_timeout 60s;  # 连接超时
        proxy_send_timeout 120s;    # 向后端发送请求的超时
        proxy_read_timeout 120s;    # 从后端读取响应的超时

        # 设置缓冲区大小
        proxy_buffer_size 16k;      # 用于代理响应的缓冲区大小
        proxy_buffers 4 32k;        # 设置 4 个缓冲区，每个缓冲区 32k
        proxy_busy_buffers_size 64k;# 设置繁忙时的缓冲区大小
    }
}
```

***

### 负载均衡

 	是指帮助将流量分配到多个服务器，从而提高应用的可用性和性能。Nginx通过upstream指令定义后端服务器集群，并结合不同的负载均衡策略（轮询，权重，IP哈希等）来实现负载均衡

**基本负载均衡配置**

​	1.**定义后端服务器集群**（使用 upstream 指令）。

​	2.**将请求代理到后端服务器**（使用 proxy_pass 指令）。

```nginx
http {
    upstream backend_servers {
        server 192.168.1.101;
        server 192.168.1.102;
        server 192.168.1.103;
    }

    server {
        listen 80;
        server_name example.com;

        location / {
            proxy_pass http://backend_servers;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
}
```

**负载均衡算法**

1. **轮询**：轮询是 Nginx 的默认负载均衡算法，它会将请求依次分配给每一个后端服务器。这种方式适合负载均衡需求均衡的场景。

```nginx
upstream backend_servers {
    server 192.168.1.101;
    server 192.168.1.102;
    server 192.168.1.103;
}
```

2. **权重**：权重算法允许根据服务器的性能和负载能力分配更多的请求。例如，配置权重较大的服务器将分配到更多的请求。

```nginx
upstream backend_servers {
    server 192.168.1.101 weight=3;  # 权重 3
    server 192.168.1.102 weight=1;  # 权重 1
    server 192.168.1.103 weight=2;  # 权重 2
}
```

3. **IP哈希**：IP 哈希算法根据客户端的 IP 地址来选择后端服务器，每个客户端的请求都会被分配到固定的一台后端服务器，适合需要保持用户会话状态的场景。

```nginx
upstream backend_servers {
    ip_hash;  # 开启 IP 哈希
    server 192.168.1.101;
    server 192.168.1.102;
    server 192.168.1.103;
}
```

***

### 缓存

1. **反向代理缓存**

反向代理缓存是最常见的缓存配置方法，用于缓存从后端服务器返回的响应，主要涉及两个部分：缓存路径的配置和缓存的使用

**配置缓存路径**

```nginx
http {
    proxy_cache_path /data/nginx/cache levels=1:2 keys_zone=my_cache:10m max_size=1g inactive=60m use_temp_path=off;
}
```

​	•/data/nginx/cache：缓存文件存储的位置。

​	•levels=1:2：缓存目录结构，用于存储缓存文件。

​	•keys_zone=my_cache:10m：定义缓存区域 my_cache，并分配 10MB 的共享内存用于缓存键的存储。

​	•max_size=1g：缓存的最大大小为 1GB。

​	•inactive=60m：缓存内容在 60 分钟内未被访问则被删除。

​	•use_temp_path=off：缓存文件直接写入到缓存目录，减少临时文件的使用。

**使用缓存**

```nginx
server {
    listen 80;
    server_name example.com;

    location / {
        proxy_pass http://backend_server;
        proxy_cache my_cache;
        proxy_cache_valid 200 1h;  # 对 200 响应缓存 1 小时
        proxy_cache_valid 404 10m; # 对 404 响应缓存 10 分钟
        proxy_cache_use_stale error timeout updating;  # 在后台更新时使用旧缓存
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

​	•proxy_cache my_cache：使用之前定义的缓存区域 my_cache。

​	•proxy_cache_valid 200 1h：缓存 HTTP 状态码为 200 的响应 1 小时。

​	•proxy_cache_valid 404 10m：缓存 HTTP 状态码为 404 的响应 10 分钟。

​	•proxy_cache_use_stale error timeout updating：在缓存过期或后台更新时，使用旧的缓存响应。

***

2. 缓存静态文件

```nginx
server {
    listen 80;
    server_name example.com;

    location /static/ {
        alias /path/to/static/files/;
        expires 30d;  # 浏览器缓存静态文件 30 天
        add_header Cache-Control "public";  # 设置缓存控制头部
    }
}
```

​	•expires 30d：告诉客户端缓存文件 30 天。

​	•add_header Cache-Control "public"：设置缓存控制头部为 public，表示文件可以被任何缓存存储。

***

### 动静分离

 	Nginx 的动静分离是一种优化技术，将静态内容（如图片、CSS、JavaScript）与动态内容（如数据库查询结果）分开处理，以提高网站的性能和响应速度。静态内容可以由 Nginx 直接提供，而动态内容则通过反向代理转发到应用服务器处理。这样可以减少应用服务器的负载，并使静态内容的请求处理更为高效。

```nginx
server {
    listen 80;
    server_name example.com;

    # 配置静态文件的路径
    location /static/ {
        alias /var/www/static/;  # 指定静态文件存储的路径
        expires 30d;  # 浏览器缓存静态文件 30 天
        add_header Cache-Control "public";  # 设置缓存控制头部
    }

    # 配置动态内容的处理
    location / {
        proxy_pass http://backend_server;  # 代理到后端动态内容服务器
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

1. **静态文件配置**

​	•location /static/：匹配 URL 路径以 /static/ 开头的请求。

​	•alias /var/www/static/：将匹配到的路径映射到实际的文件系统路径 /var/www/static/。

​	•expires 30d：设置浏览器缓存静态文件 30 天。

​	•add_header Cache-Control "public"：设置 Cache-Control 头部，标记内容为可公开缓存。

2. 动态内容配置

​	•location /：匹配所有其他请求。

​	•proxy_pass http://backend_server：将匹配到的请求转发到后端服务器 backend_server。

​	•proxy_set_header 指令：设置代理请求头部，确保后端服务器能够正确处理客户端的请求信息。

# Nginx 八股文

## Nginx 有什么作用？

Nginx 是一款高性能的**开源 Web 服务器和反向代理服务器**，以高并发、低资源消耗和灵活的扩展性著称。以下从核心功能、应用场景、底层优势三个维度解析其作用：

### **一、核心功能：Web 服务与流量管理的全能工具**

#### 1. **反向代理（最核心功能）**

- **作用**：作为客户端与后端服务器的中间层，接收所有客户端请求，根据规则转发到不同的后端服务器（如 Tomcat、Node.js 服务），对客户端透明。

- 典型场景：

  - 隐藏后端服务器真实 IP，提升安全性；
  - 统一入口管理，简化客户端访问（如域名映射到不同服务）。

- 配置示例

  ```nginx
  server {
      listen 80;
      server_name api.example.com;
      location / {
          proxy_pass http://backend_server:8080;  # 转发到后端服务
      }
  }
  ```

#### 2. **负载均衡**

- **作用**：将客户端请求均匀分发到多个后端服务器，避免单节点过载，提升系统可用性。

- 支持策略：

  - 轮询（默认）、权重轮询、IP 哈希（相同 IP 请求固定到同一服务器）、URL 哈希等。

- 配置示例：

  ```nginx
  upstream backend {
      server 192.168.1.101:8080 weight=2;  # 权重更高的服务器处理更多请求
      server 192.168.1.102:8080;
  }
  ```

#### 3. **静态资源服务器**

- **作用**：直接处理静态文件请求（如 HTML、CSS、图片），无需转发给后端，减轻应用服务器压力。

- **性能优势**：Nginx 的文件读取和缓存机制针对静态资源优化，响应速度极快。

- 配置示例：

  ```nginx
  location /static/ {
      root /var/www/html;  # 静态资源根目录
      expires 7d;  # 缓存7天
      add_header Cache-Control "public";
  } 
  ```

#### 4. **HTTP 缓存**

- **作用**：缓存频繁访问的资源（如热点图片、API 结果），减少后端服务调用，提升响应速度。
- 支持机制：
  - 基于`Expires`和`Cache-Control`头部的缓存控制；
  - 动态缓存（如 Varnish 集成）。

### **二、应用场景：从中小企业到大型集群的全场景覆盖**

#### 1. **高并发 Web 服务**

- 典型案例：
  - 知乎、豆瓣等网站用 Nginx 处理每秒上万次请求；
  - 电商大促期间，Nginx 作为入口层扛住流量峰值。
- **核心优势**：单台服务器可维持数万并发连接（得益于事件驱动模型）。

#### 2. **微服务网关**

- **作用**：作为微服务架构的统一入口，处理请求路由、负载均衡、权限校验等。
- **结合工具**：与 Spring Cloud Gateway、Kong 等网关工具配合使用。

#### 3. **API 网关与流量控制**

- 功能：

  - 限制接口请求频率（如`limit_req`模块），防御爬虫和恶意攻击；
  - 按条件转发请求（如根据用户身份、请求参数路由到不同服务）。

- 配置示例：

  ```nginx
  limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;  # 限制IP每秒10请求
  location /api/ {
      limit_req zone=api_limit burst=20 nodelay;  # 突发请求缓冲
  }
  ```

#### 4. **SSL/TLS 加密与 HTTP/2 支持**

- 作用：
  - 卸载 SSL 加密计算（SSL Termination），减轻后端服务器负载；
  - 支持 HTTP/2 协议，减少页面加载延迟（多路复用、头部压缩）。

### **三、底层优势：高性能的技术基石**

#### 1. **事件驱动架构（Epoll/Kqueue）**

- **原理**：采用异步非阻塞模式，单进程可处理数千并发连接，避免传统服务器（如 Apache）的多进程 / 线程开销。
- **对比**：Apache 的`prefork`模式每个连接对应一个进程，高并发下内存占用激增，而 Nginx 内存效率提升 10 倍以上。

#### 2. **多进程模型**

- 架构：
  - 1 个 master 进程（管理工作进程）+ N 个 worker 进程（处理请求）；
  - 热重启机制：修改配置后，新 worker 进程接管请求，旧进程处理完剩余请求后退出，确保服务不中断。

#### 3. **模块化设计**

- 可扩展功能：
  - 内置模块：`ngx_http_rewrite_module`（URL 重写）、`ngx_http_gzip_module`（内容压缩）；
  - 第三方模块：`ngx_http_redis_module`（集成 Redis 缓存）、`ngx_lua_module`（Lua 脚本扩展）。

### **四、延伸：Nginx 与其他组件的协作**

#### 1. **与应用服务器配合**

- **场景**：Nginx 作为反向代理，转发动态请求到 Tomcat（Java）、Django（Python）等应用服务器。
- 通信协议：
  - `proxy_pass http://`：HTTP 协议转发（常用）；
  - `fastcgi_pass`：与 PHP-FPM 通信的专用协议。

#### 2. **与 CDN 集成**

- **作用**：Nginx 作为源站服务器，与 CDN 节点配合，实现全球内容分发（如图片、视频加速）。

### **五、总结：Nginx 的核心价值**

Nginx 的本质是**流量的智能调度者和优化器**，通过反向代理、负载均衡、缓存加速等功能，解决了分布式系统中 “高并发、高可用、低延迟” 的核心需求。其轻量级、高性能的特性使其成为从中小企业到大型互联网公司的基础设施标配，甚至在容器化场景（如 Kubernetes Ingress）中也扮演关键角色。
