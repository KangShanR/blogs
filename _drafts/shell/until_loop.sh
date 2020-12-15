#!/bin/bash

i=100
until [ $i -lt 1 ]
do
    echo -n "$i "
    i=`expr $i - 1`
done
