name := "sparksql-hbase-examples"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(

  //spark所需
  "org.apache.spark" % "spark-core_2.11" % "1.5.2",

  "org.apache.spark" % "spark-sql_2.11" % "1.5.2",

  "org.apache.spark" % "spark-hive_2.11" % "1.5.2",

  "org.apache.spark" % "spark-streaming_2.11" % "1.5.2",

  //HBase所需
  "org.apache.hbase" % "hbase-client" % "1.1.2",

  "org.apache.hbase" % "hbase-common" % "1.1.2",

  "org.apache.hbase" % "hbase-server" % "1.1.2",

  //测试所需
  "org.scalatest" % "scalatest_2.11" % "2.2.4",

  //日志所需
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",

  "org.slf4j" % "slf4j-api" % "1.7.13",

  "log4j" % "log4j" % "1.2.17"

  //使用logback打印不出相应信息，可能被HBase污染了，Hbase用的可能是log4j，改成log4j后可以打印出信息
  //"ch.qos.logback" % "logback-classic" % "1.1.3"
  //"ch.qos.logback" % "logback-core" % "1.1.3"

)

