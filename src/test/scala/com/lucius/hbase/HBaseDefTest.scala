package com.lucius.hbase

import org.scalatest.{BeforeAndAfterEach, FunSuite}

class HBaseDefTest extends FunSuite with BeforeAndAfterEach {
  var hbaseDef: HBaseDef = new HBaseDef

  override def beforeEach() {
    hbaseDef = new HBaseDef
  }

  override def afterEach() {
    hbaseDef.closeConnection()
  }

  test("test PutInto") {
    hbaseDef.createOrOverwriteTable("mytb", Array("info"))
    hbaseDef.putInto("mytb", "row0001", Array(HBaseColumn("info", "name", "lucius"), HBaseColumn("info","age","26")))
  }

  test("test CreateOrOverwriteTable") {

  }

}
