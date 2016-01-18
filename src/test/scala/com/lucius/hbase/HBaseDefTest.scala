package com.lucius.hbase

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class HBaseDefTest extends FunSuite with BeforeAndAfterEach with LazyLogging {
  var hbaseDef: HBaseDef = new HBaseDef

  override def beforeEach() {
    hbaseDef = new HBaseDef
  }

  override def afterEach() {
    hbaseDef.closeConnection()
  }

  test("test PutInto") {
    hbaseDef.createOrOverwriteTable("myt", Array("info"))
    hbaseDef.putInto("myt", "row0001", Array(HBaseColumn("info", "name", "lucius"), HBaseColumn("info","age","26")))
  }

}
