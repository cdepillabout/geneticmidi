#!/bin/bash

javac *.java && \
echo "Running MidiHelper..." && \
java -ea -server geneticmidi.MidiHelper && \
echo "Running MidiIndividualTrack..." && \
java -ea -server geneticmidi.MidiIndividualTrack && \
echo "Running MidiIndividual..." && \
java -ea -server geneticmidi.MidiIndividual && \
echo "Running IdealSequence..." && \
java -ea -server geneticmidi.IdealSequence

