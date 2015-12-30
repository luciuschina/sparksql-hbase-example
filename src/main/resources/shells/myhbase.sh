#!/bin/bash

usage="Usage: $0 (start|stop|shell)"

if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi

behave=$1

if [ "$behave" == 'shell' ];then
  echo "$HBASE_HOME/bin/hbase shell"
  $HBASE_HOME/bin/hbase shell
elif [ "$behave" == 'start' ] || [ "$behave" == 'stop' ];then
  echo "$HBASE_HOME/bin/$behave-hbase.sh"
  ssh centos03 "$HBASE_HOME/bin/$behave-hbase.sh"
else
 echo $usage
fi

exit 0