#!/bin/bash
echo -- -- begin init Yao... -- --

#定义docker-compose.yml文件和jar包位置
COMPOSE_FILE=./docs/docker/yao/docker-compose-build.yml
JAR_DIR=./docs/docker/yao/jar

echo -- -- stop and remove old docker-compose containers 停止并删除旧版本容器-- --
if docker-compose -f ${COMPOSE_FILE} ps
    then
        docker-compose -f ${COMPOSE_FILE} stop
        docker-compose -f ${COMPOSE_FILE} rm --force
fi

echo -- -- building jar 构建jar包并跳过单元测试-- --
mvn clean package -Dmaven.test.skip=true

echo -- -- move jar to ${JAR_DIR} -- --
if [ ! -d ${JAR_DIR} ];then
   mkdir -p ${JAR_DIR}
fi

echo -- -- 删除旧版本jar包并把新构建的jar包移动到指定目录 -- --
rm -rf ${JAR_DIR}/yao-auth*.jar
rm -rf ${JAR_DIR}/yao-gateway*.jar
rm -rf ${JAR_DIR}/yao-upms-server*.jar
rm -rf ${JAR_DIR}/yao-support-server*.jar
rm -rf ${JAR_DIR}/yao-log-server*.jar


cp ./yao-auth/target/yao-auth*.jar ${JAR_DIR}
cp ./yao-gateway/target/yao-gateway*.jar ${JAR_DIR}
cp ./yao-server/yao-upms/yao-upms-server/target/yao-upms-server*.jar ${JAR_DIR}
cp ./yao-server/yao-support/yao-support-server/target/yao-support-server*.jar ${JAR_DIR}
cp ./yao-server/yao-log/yao-log-server/target/yao-log-server*.jar ${JAR_DIR}


#echo -- -- 备份以前的镜像 -- --
#
#time=$(date "+%Y%m%d%H%M%S")
#docker tag yao_yao-auth:latest yao_yao-auth:$time
#docker tag yao_yao-gateway:latest yao_yao-gateway:$time
#docker tag yao_yao-upms-server:latest yao_yao-upms-server:$time
#docker tag yao_yao-support-server:latest yao_yao-support-server:$time
#docker tag yao_yao-log-server:latest yao_yao-log-server:$time


echo -- -- run docker-compose build 创建镜像-- --
docker-compose  -f ${COMPOSE_FILE} build

#echo -- -- 上传镜像-- --
#docker tag yao_yao-auth:latest y3tu/yao-auth:latest
#docker push y3tu/yao-auth:latest
#docker rmi y3tu/yao-auth:latest
#
#docker tag yao_yao-gateway:latest y3tu/yao-gateway:latest
#docker push y3tu/yao-gateway:latest
#docker rmi y3tu/yao-gateway:latest
#
#docker tag yao_yao-upms-server:latest y3tu/yao-upms-server:latest
#docker push y3tu/yao-upms-server:latest
#docker rmi y3tu/yao-upms-server:latest
#
#docker tag yao_yao-support-server:latest y3tu/yao-support-server:latest
#docker push y3tu/yao-support-server:latest
#docker rmi y3tu/yao-support-server:latest
#
#docker tag yao_yao-log-server:latest y3tu/yao-log-server:latest
#docker push y3tu/yao-log-server:latest
#docker rmi y3tu/yao-log-server:latest
#
#echo -- -- 上传镜像结束-- --
#
#
#docker images|grep docker|awk '{print $3 }'|xargs docker rmi

