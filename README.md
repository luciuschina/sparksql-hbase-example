# sparksql-hbase-example

集群设置

| 节点\应用 | HDFS              | YARN                         |  HBASE        | Zookeeper     |
|:---------|:------------------|:-----------------------------|:--------------|:--------------|
| centos03 | NameNode,DataNode | NodeManager                  | HMaster       | QuorumPeerMain|
| centos04 | DataNode          | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|
| centos05 | NameNode,DataNode | ResourceManager,NodeManager  | HRegionServer | QuorumPeerMain|
