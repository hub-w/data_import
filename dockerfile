#拉取基础镜像，as指定镜像名称
FROM centos:latest as builder
#RUN 后边可以跟Linux命令，安装wget
RUN yum install -y wget
#换yum源
RUN mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo_bak && wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo && yum makecache
#安装jdk、maven、建立目录
RUN yum install -y java-1.8.0-openjdk maven && mkdir -p /root/project/data_import/src && mkdir -p /root/project/data_import/lib
#复制文件
COPY lib /root/project/data_import/lib
COPY src /root/project/data_import/src
COPY pom.xml /root/project/data_import/
RUN ls /root/project/data_import
RUN ls /root/project/data_import/src
RUN ls /root/project/data_import/lib
RUN cd /root/project/data_import && mvn install -e && mvn package && mkdir -p /root/output/
RUN cp /root/project/data_import/target/data_import-1.0-SNAPSHOT.jar /root/output

#一个dockerfile中可以有多个FROM,但是只会有一个生成镜像
FROM centos:latest
RUN yum install -y wget
RUN mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo_bak && wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo && yum makecache
RUN yum install -y java-1.8.0-openjdk maven
COPY --from=builder /root/output/data_import-1.0-SNAPSHOT.jar /root/
WORKDIR /root
ENTRYPOINT ["java","-jar","/root/data_import-1.0-SNAPSHOT.jar"]
