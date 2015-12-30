package com.lucius.hbase

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm
import org.apache.hadoop.hbase.util.Bytes

object HBaseScalaDemo {
  private val TABLE_NAME = "MY_TABLE_NAME_TOO"
  private val CF_DEFAULT = "DEFAULT_COLUMN_FAMILY"
  val config = HBaseConfiguration.create()
  config.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"))

  def createOrOverwrite(admin: Admin, table: HTableDescriptor) {
    if (admin.tableExists(table.getTableName())) {
      admin.disableTable(table.getTableName())
      admin.deleteTable(table.getTableName())
    }
    admin.createTable(table)
  }

  def listTables = {
    val connection = ConnectionFactory.createConnection(config)
    val admin = connection.getAdmin()
    // list the tables
    val listTables = admin.listTables()
    listTables.foreach(println)
   
  }


  def putTest = {
    val connection = ConnectionFactory.createConnection(config)
    val table: Table = connection.getTable(TableName.valueOf("member"))
    val thePut = new Put(Bytes.toBytes("0004"))
    thePut.addColumn(Bytes.toBytes("address"), Bytes.toBytes("province"), Bytes.toBytes("浙江"))
    table.put(thePut)
    connection.close()
  }

  def getTest = {
    val connection = ConnectionFactory.createConnection(config)
    val table: Table = connection.getTable(TableName.valueOf("member"))
    val theGet = new Get(Bytes.toBytes("0003"))

    //这边只能输出一个数据啊！！！
    val result: Result = table.get(theGet)
    println(result)
    val province = result.getValue(Bytes.toBytes("address"), Bytes.toBytes("province"))
    val birthday = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("birthday"))
    println(Bytes.toString(province))
    println(Bytes.toString(birthday))
    connection.close()
  }

  def createSchemaTables() = {
    val connection: Connection = ConnectionFactory.createConnection(config)
    val admin: Admin = connection.getAdmin()
    val table: HTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME))
    table.addFamily(new HColumnDescriptor(CF_DEFAULT))
    //原来的Demo中有.setCompressionType(Algorithm.SNAPPY)，但是这个会报错
    //table.addFamily(new HColumnDescriptor(CF_DEFAULT).setCompressionType(Algorithm.SNAPPY))
    println("###### Creating table #######")
    createOrOverwrite(admin, table)
    System.out.println(" Done.")
  }

  def testScan() = {
    val connection: Connection = ConnectionFactory.createConnection(config)
    //表的名称是"member"
    val table: Table = connection.getTable(TableName.valueOf("member"))
    // 被废弃的方式：val table2: Table = new HTable(config, "member")
    val scan = new Scan()
    //增加对列族"info"的查询
    scan.addFamily(Bytes.toBytes("info"))
    //增加对列"address：country"的查询
    scan.addColumn(Bytes.toBytes("address"), Bytes.toBytes("country"))
    //过滤出前缀为"000"的RowKey的数据
    scan.setRowPrefixFilter(Bytes.toBytes("000"))

    val scanner = table.getScanner(scan)
    var result = scanner.next()
    while (result != null) {
      getColumnsInColumnFamily(result, "info").foreach(println)
      result = scanner.next()
    }
    scanner.close()
  }

  def getColumnsInColumnFamily(result: Result, ColumnFamily: String) = {
    val familyMap = result.getFamilyMap(Bytes.toBytes(ColumnFamily))
    val quantifers = new Array[String](familyMap.size())
    val it = familyMap.keySet().iterator()
    (0 until familyMap.size()).foreach { index => quantifers(index) = Bytes.toString(it.next()) }
    quantifers
  }

/*  def getColumnValues(result: Result) = {
    result.get
  }*/

  def modifySchema() = {
    val connection: Connection = ConnectionFactory.createConnection(config)
    val admin: Admin = connection.getAdmin()
    val tableName: TableName = TableName.valueOf(TABLE_NAME)
    if (!admin.tableExists(tableName)) {
      println("Table does not exist.")
      System.exit(-1)
    }
    val table: HTableDescriptor = new HTableDescriptor(tableName)

    val newColumn: HColumnDescriptor = new HColumnDescriptor("NEWCF")
    //newColumn.setCompactionCompressionType(Algorithm.GZ);
    newColumn.setMaxVersions(HConstants.ALL_VERSIONS)
    admin.addColumn(tableName, newColumn)

    // Update existing column family
    val existingColumn: HColumnDescriptor = new HColumnDescriptor(CF_DEFAULT)
    existingColumn.setCompactionCompressionType(Algorithm.GZ)
    existingColumn.setMaxVersions(HConstants.ALL_VERSIONS)
    table.modifyFamily(existingColumn)
    admin.modifyTable(tableName, table)

    // Disable an existing table
    admin.disableTable(tableName)

    // Delete an existing column family
    admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"))

    // Delete a table (Need to be disabled first)
    admin.deleteTable(tableName)
  }

  def main(args: Array[String]) {
    //createSchemaTables(config)
    //modifySchema(config);
    //testScan()
    testScan
    // println(System.getenv("HBASE_CONF_DIR"))
    // conf.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));
  }
}
