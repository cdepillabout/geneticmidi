#!/bin/bash

javac *.java && \
echo "Testing MidiHelper Test..." && \
java org.junit.runner.JUnitCore geneticmidi.MidiHelperTest 

