# Redis缓存

####1.单点模式配置
spring.redis.hostName=10.200.152.113
spring.redis.port=6379
####2.sentinel集群配置（优先级最高）
spring.redis.sentinel.nodes=
spring.redis.sentinel.master=
####3.cluster集群配置
spring.redis.cluster.nodes=
spring.redis.cluster.max-redirects=
####4.增加服务质量Metrics