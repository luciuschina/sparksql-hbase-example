#!/bin/bash

usage="Usage: $0 (start|stop|status)"

if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi

behave=$1

echo "$behave zkServer cluster"
for i in 03 04 05
do
ssh centos$i "/opt/zookeeper-3.4.6/bin/zkServer.sh $behave"
done

exit 0