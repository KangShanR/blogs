#!/bin/bash
echo "start"
i=0
until [ $i -gt 10 ]
do
    for j in 9 7 5
    do
        if [ $j -eq $i ]
        then
            echo "$i is equals $j"
            continue
        elif [ $i -lt 4 -a $i -gt 2 ]
        then
            echo "It's 3!"
            break
        elif [ $i -eq 8 ]
        then
            echo "It's 8,let's break 1"
            break 1 
        else
            echo -n "$j" 
        fi
    done
    i=`expr $i + 1`
done
echo "end"
