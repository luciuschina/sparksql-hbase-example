# sparksql-hbase-example

#### 集群的搭建和启动
在虚拟机上部署了三个节点的Hadoop和Hbase集群，如下：

|主机名\应用| HDFS              | YARN                         |  HBASE        | Zookeeper     |
|:---------|:------------------|:-----------------------------|:--------------|:--------------|
| centos03 | NameNode,DataNode | NodeManager                  | HMaster       | QuorumPeerMain|
| centos04 | DataNode          | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|
| centos05 | NameNode,DataNode | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|

搭建集群的步骤可以参考：[Zookeeper集群搭建](http://blog.csdn.net/u014729236/article/details/44832631)、[Hadoop集群搭建](http://blog.csdn.net/u014729236/article/details/44835669)、[Hbase集群搭建](http://blog.csdn.net/u014729236/article/details/44945343)
集群搭建完成后。先后启动Zookeeper、HDFS、YARN和HBASE。
为了更加方便的管理集群，我写了几个简要的脚本(src/resources/shells中的三个脚本)
只需要在一台机子上分别执行：  
./zookeeper-cluster.sh start  
./hadoop-cluster.sh start  
./myhbase.sh start  
就能快速的启动集群了。

#### 数据迁移
[使用sqoop将数据从mysql中迁移到Hbase中](http://blog.csdn.net/u014729236/article/details/50370385)

#### 客户端连接HBase集群
首先需要创建一个org.apache.hadoop.conf.Configuration对象:  
`val conf: Configuration = HBaseConfiguration.create()`  
然后需要获取到zookeeper的参数，有两种方法：  
第一种方法是获取获取Linux中的Hbase安装目录中的**hbase-site.xml**这个配置文件的路径，可以这样获取：  
`conf.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"))`  
其中"HBASE\_CONF\_DIR"是环境变量中设置的参数。  
即"cat /etc/profile"中的"export HBASE\_CONF\_DIR=/opt/hbase-1.1.2/conf"  
第二种方法在代码中直接设置"hbase.zookeeper.quorum"的值，如下：  
`conf.set("hbase.zookeeper.quorum", "centos03:2181,centos04:2181,centos05:2181")`


