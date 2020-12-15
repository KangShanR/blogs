#! /bin/bash
echo "number of current shell: $$"
echo "What's your name?"
NAME = "NOTHING"
# read NAME


echo "Hello, $NAME!"

 echo $#
 
 # loop
 for TOKEN in $*
 do
     echo $TOKEN
     echo "exit status: $?"
 done
 
