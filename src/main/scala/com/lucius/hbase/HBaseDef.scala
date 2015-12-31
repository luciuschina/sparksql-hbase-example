package com.lucius.hbase


import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HColumnDescriptor, TableName, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.conf.Configuration

class HBaseDef extends LazyLogging {

  var connection: Connection = getConnection
  var admin = getConnection.getAdmin

  def getTableDescriptorBy(tableName: String): HTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName))

  def getTableBy(tableName: String): Table = connection.getTable(TableName.valueOf(tableName))

  /**
   * 往表中插入数据
   * @param tableName 表名
   * @param rowKey 行键
   * @param columns 列的数组。
   */
  def putInto(tableName: String, rowKey: String, columns: Array[HBaseColumn]) = {
    val thePut = new Put(Bytes.toBytes(rowKey))
    columns.foreach {
      HBaseColumn =>
      thePut.addColumn(Bytes.toBytes(HBaseColumn.family), Bytes.toBytes(HBaseColumn.qualifier), Bytes.toBytes(HBaseColumn.value))
      logger.info(s"执行语句:put '$tableName','$rowKey','${HBaseColumn.family}:${HBaseColumn.qualifier}','${HBaseColumn.value}'")
    }
    getTableBy(tableName).put(thePut)

  }

  private def getConnection = {
    if(Option(connection).isEmpty || connection.isClosed) {
      logger.info("建立连接:connection = ConnectionFactory.createConnection(HBaseDef.conf)")
      connection = ConnectionFactory.createConnection(HBaseDef.conf)
    }
    connection
  }

  /**
   * 创建表
   * @param tableName 表名
   * @param columnFamilies 列族名
   */
  def createTable(tableName: String, columnFamilies: Array[String]) = {
    logger.info(s"执行语句:create '$tableName','${columnFamilies.mkString("','")}' ")
    val table = new HTableDescriptor(TableName.valueOf(tableName))
    columnFamilies.foreach(columnFamily => table.addFamily(new HColumnDescriptor(columnFamily)))
    admin.createTable(table)
    logger.info(s"成功创建表：$tableName")
  }

  /**
   * 创建或者覆盖表
   * @param tableName 表名
   * @param columnFamilies 列族名
   */
  def createOrOverwriteTable(tableName: String, columnFamilies: Array[String]) = {
    deleteTable(tableName)
    createTable(tableName, columnFamilies)
  }

  /**
   * 删除表
   * @param tableName 表名
   */
  def deleteTable(tableName: String) = {
    //这边还可以这样写：val tableDescriptor = getTableDescriptorBy(tableName)，
    //table.getName用tableDescriptor.getTableName来替代
    val table = getTableBy(tableName)
    if (admin.tableExists(table.getName)) {
      admin.disableTable(table.getName)
      admin.deleteTable(table.getName)
      logger.info(s"删除表:${table.getName}")
    }
  }

  /**
   * 注意：在该类使用完成后，需要手动调用此方法关闭连接
   */
  def closeConnection() = {
    logger.info("关闭连接:connection.close()")
    connection.close()
  }

}

object HBaseDef extends LazyLogging {
  val conf: Configuration = HBaseConfiguration.create()

  //下面两种方法取其一
  //方法一：
  //conf.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"))
  //方法二：
  conf.set("hbase.zookeeper.quorum", Props.get("hbase.zookeeper.quorum"))

}
