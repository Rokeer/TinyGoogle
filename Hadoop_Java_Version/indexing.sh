#!/bin/sh

set -x
source ~/.bash_profile

hadoop fs -rmr /user/colinzhang/prj2/temp
hadoop fs -rmr /user/colinzhang/prj2/output

if [ -z "$1" ]
  then
    hadoop jar indexing.jar Indexing prj2/smallinput/ prj2/temp/ prj2/output/
  else
  	hadoop jar indexing.jar Indexing $1 prj2/temp/ prj2/output/
fi


hadoop fs -cat '/user/colinzhang/prj2/output/part-00000' || exit -1

exit 0
