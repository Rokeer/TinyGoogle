#!/bin/sh

set -x
source ~/.bash_profile

hadoop fs -rmr /user/colinzhang/prj2/result

if [ -z "$1" ]
  then
    hadoop jar indexing.jar Searching "hadoop world" prj2/output/ prj2/result/
  else
  	hadoop jar indexing.jar Searching $1 prj2/output/ prj2/result/
fi




hadoop fs -cat '/user/colinzhang/prj2/result/part-00000' | head -10 || exit -1

exit 0