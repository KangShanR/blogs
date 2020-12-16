#! /bin/bash
echo "number of current shell: $$"
echo "What's your name?"
NAME = "NOTHING"
# read NAME


echo "Hello, $NAME!"

echo "loop test start---"
 
# loop-while
i=0
while [ "$i" -le 9 ]
do
    j="$i"
    while [ "$j" -ge 0 ]
    do
        echo -n "$j "
        j=`expr $j - 1`
    done
    echo
    i=`expr $i + 1`
done
 
echo "loop test end---"
