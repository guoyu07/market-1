1、打包测试本地环境

mvn clean install -DSkipTests -P dev

2、打包线上环境

mvn clean install -DSkipTests -P prod

3、依赖环境

nginx资源服务器

mysql数据库