# echo

SpringBoot + MyBatis Plus + Sa-token + Redis + MySQL

## 推荐启动

[IntelliJ IDEA](https://www.jetbrains.com/idea) + [MySQL 8](https://www.mysql.com/) + [Redis 7.2.4](https://redis.io/)

### 默认配置

| 服务 | 端口 |
| --- | --- |
| MySQL | localhost:3306/echo |
| Redis | localhost:6379 |

## 实验性功能

### 缓存

- 集成方式：SpringBoot Cache 集成 Redis
- 使用位置：服务层注解，控制层和服务层少量使用 RedisTemplate
- 缓存数据：仅 get、put、update、delete 基础接口，分页数据处理一致性代码逻辑侵入性过强，未实现

### 内容推荐算法

- 分词：HanLP，默认提取前 5 个频次最高分词
- 算法：TF-IDF + 余弦相似度
- 语料库：默认使用 1000 条文章和 1000 条问答
- 数据暂存：Redis 语料库 Map 存储，对应分词向量 Hash 存储，临时推荐数据 Value 存储
- 更新时机: 语料库每日 12 点，推荐数据访问时更新，热数据 1 小时过期，临近过期访问过期自动后置更新

## 其他

### API 文档

Spingdoc+Swagger: http://localhost:8080/swagger-ui/index.html