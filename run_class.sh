#!/bin/bash

# check for the correct number of arguments
if [ $# != 1 ]
then
	echo "Give only one argument. A classname."
	exit 1
fi

filename="$1"

basenameOfFile=${filename%.*}

javac *.java && \
echo "Running $basenameOfFile..." && \
java geneticmidi.$basenameOfFile
