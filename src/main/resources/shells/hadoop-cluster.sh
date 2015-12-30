#!/bin/bash

usage="Usage: $0 (start|stop)"

if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi

behave=$1

ssh centos03 "$HADOOP_HOME/sbin/$behave-dfs.sh"
ssh centos04 "$HADOOP_HOME/sbin/$behave-yarn.sh"

exit 0