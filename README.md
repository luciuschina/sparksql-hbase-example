# sparksql-hbase-example

### 集群

在虚拟机上部署了三个节点的Hadoop和Hbase集群，如下：

|主机名\应用| HDFS              | YARN                         |  HBASE        | Zookeeper     |
|:---------|:------------------|:-----------------------------|:--------------|:--------------|
| centos03 | NameNode,DataNode | NodeManager                  | HMaster       | QuorumPeerMain|
| centos04 | DataNode          | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|
| centos05 | NameNode,DataNode | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|

具体配置信息可以参考：[Zookeeper集群搭建](http://blog.csdn.net/u014729236/article/details/44832631)、[Hadoop集群搭建](http://blog.csdn.net/u014729236/article/details/44835669)、[Hbase集群搭建](http://blog.csdn.net/u014729236/article/details/44945343)

集群搭建完成后。先后启动Zookeeper、HDFS\YARN和HBASE。

可以使用本项目中的src/resources/shells目录中的三个脚本,在一台机子上运行这些脚本,快速的启动集群。
分别执行：  
./zookeeper-cluster.sh start  
./hadoop-cluster.sh start  
./myhbase.sh start  