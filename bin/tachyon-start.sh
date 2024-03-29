#!/usr/bin/env bash

# Start all Tachyon workers.
# Starts the master on this node.
# Starts a worker on each node specified in conf/slaves

Usage="Usage: tachyon-start.sh"

if [ "$#" -ne 0 ]; then
  echo $Usage
  exit 1
fi

bin=`cd "$( dirname "$0" )"; pwd`

# Load the Tachyon configuration
. "$bin/tachyon-config.sh"

rm -rf $TACHYON_HOME/logs
mkdir -p $TACHYON_HOME/logs
mkdir -p $TACHYON_HOME/data

$bin/tachyon-stop.sh

$bin/tachyon-start-master.sh

sleep 1

# $bin/slaves.sh $bin/clear-cache.sh
$bin/slaves.sh $bin/tachyon-start-worker.sh
