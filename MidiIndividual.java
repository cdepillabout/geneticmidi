package geneticmidi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.Vector;

import java.io.File;

public class MidiIndividual implements Individual<MidiIndividual> {

	protected Sequence sequence;

	/**
	 * This holds the fitness score. This is so that I can calculated it once
	 * and be done with it, instead of calculating it many times.
	 */
	protected double storedFitness;

	/**
	 * This holds whether the fitness score has already been calculated.
	 */
	protected boolean alreadyCalcedFitness;

	/**
	 * The individual tracks.
	 */
	Vector<MidiIndividualTrack> midiIndividualTracks;

	public MidiIndividual() 
	{
		
		alreadyCalcedFitness = false;

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			int tracks = IdealSequence.getIdealSequence().getTracks().length;
		
			midiIndividualTracks = new Vector<MidiIndividualTrack>();

			for (int i = 0; i < tracks; i++)
			{
				midiIndividualTracks.add(
						new MidiIndividualTrack(sequence.createTrack(), i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MidiIndividual(Vector<MidiIndividualTrack> midIndvTracks) 
	{
		
		alreadyCalcedFitness = false;

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			int tracks = IdealSequence.getIdealSequence().getTracks().length;
			assert tracks == midIndvTracks.size();
		
			midiIndividualTracks = new Vector<MidiIndividualTrack>();

			for (int i = 0; i < tracks; i++)
			{
				midiIndividualTracks.add(
						new MidiIndividualTrack(
							sequence.createTrack(), i, midIndvTracks.get(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Sequence getSequence()
	{
		return sequence;
	}

	public double fitness() 
	{
		if (alreadyCalcedFitness)
		{
			return storedFitness;
		}

		double fitness = 0;

		for(MidiIndividualTrack track: midiIndividualTracks)
		{
			fitness += track.fitness();
		}
		
		storedFitness = fitness;
		alreadyCalcedFitness = true;

		return fitness;
	}
	

	public MidiIndividual makeAnother() 
	{
		return new MidiIndividual();
	}

	
	/** 
	 * Get index-th individual track.
	 */
	public MidiIndividualTrack getIndividualTrack(int index)
	{
		return midiIndividualTracks.get(index);
	}


	public MidiIndividual crossover(MidiIndividual that)
	{
		Vector<MidiIndividualTrack> newIndividualTracks = 
			new Vector<MidiIndividualTrack>();

		for (int i = 0; i < midiIndividualTracks.size(); i++)
		{
			MidiIndividualTrack newIndv = 
				this.getIndividualTrack(i).crossover(that.getIndividualTrack(i));
			newIndividualTracks.add(newIndv);
		}

		return new MidiIndividual(newIndividualTracks);
	}

	
	public void mutate (double mutationRate)
	{
		alreadyCalcedFitness = false;

		for (MidiIndividualTrack t: midiIndividualTracks)
		{
			t.mutate(mutationRate);
		}
	}

	public void writeSequence(String filename)
	{
		try {
			MidiSystem.write(sequence, 1, new File(filename));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	public boolean allNotesExistInTrack()
	{

	}
	*/

	public String toString()
	{
		return DebugMidi.sequenceEventsToString(sequence);
	}

	public static void main(String[] args) {

		MidiIndividual midiIndv1 = new MidiIndividual();
		System.out.println("Individual 1: " + midiIndv1);
		System.out.println("fitness: " + midiIndv1.fitness());
		System.out.println();

		
		MidiIndividual midiIndv2 = new MidiIndividual();
		System.out.println("Individual 2: " + midiIndv2);
		System.out.println("fitness: " + midiIndv2.fitness());
		System.out.println();

		
		MidiIndividual newIndividual = midiIndv1.crossover(midiIndv2);
		System.out.println("crossover Individual: " + newIndividual);
		System.out.println("fitness: " + newIndividual.fitness());
		System.out.println();

		newIndividual.mutate(1);
		System.out.println("mutated crossover Individual: " + newIndividual);
		System.out.println("fitness: " + newIndividual.fitness());
		System.out.println();
		

	}

}

