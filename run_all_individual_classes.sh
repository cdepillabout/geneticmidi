#!/bin/bash

javac *.java && \
echo "Running MidiHelper..." && \
java geneticmidi.MidiHelper && \
echo "Running MidiIndividualTrack..." && \
java geneticmidi.MidiIndividualTrack && \
echo "Running MidiIndividual..." && \
java geneticmidi.MidiIndividual && \
echo "Running IdealSequence..." && \
java geneticmidi.IdealSequence

