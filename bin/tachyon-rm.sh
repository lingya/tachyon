#!/usr/bin/env bash

Usage="Usage: tachyon-ls.sh <filePath>"

if [ "$#" -ne 1 ]; then
  echo $Usage
  exit 1
fi

bin=`cd "$( dirname "$0" )"; pwd`

# Load the Tachyon configuration
. "$bin/tachyon-config.sh"

java -cp $TACHYON_HOME/target/tachyon-1.0-SNAPSHOT-jar-with-dependencies.jar tachyon.command.Rm $@ 
