# Redis缓存

#### 1.单点模式配置
spring.redis.hostName= <br>
spring.redis.port=
#### 2.sentinel集群配置（优先级最高）
spring.redis.sentinel.nodes= <br>
spring.redis.sentinel.master=
#### 3.cluster集群配置
spring.redis.cluster.nodes= <br>
spring.redis.cluster.max-redirects=
#### 4.增加服务质量Metrics
