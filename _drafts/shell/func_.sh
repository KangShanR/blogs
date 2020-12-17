#!/bin/bash
echo "parameters: $#"
echo "parameters: $@"
echo "parameters: $?"
test () {
    echo $1
    echo "date: `date`"
    return "323223227658903"
}

test "invoke"
ret=$?
echo "Returned value is $ret"

