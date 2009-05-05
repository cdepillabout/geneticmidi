package geneticmidi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.Vector;

import java.io.File;

public class MidiIndividualTrack implements Individual<MidiIndividualTrack> {

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
	 * The channel (or track) that this individual is representing.
	 */
	protected int channel;


	public MidiIndividualTrack(int channel) 
	{
		alreadyCalcedFitness = false;
		this.channel = channel;

		Vector<Note> notes = new Vector<Note>();

		// find out how many ticks the ideal sequence is
		long totalTicks = IdealSequence.getIdealSequence().getTracks()[channel].ticks(); 

		// find out how many notes ideal sequence has
		Vector<Note> idealSequenceNotes = 
			MidiHelper.getNotesFromTrack(IdealSequence.getIdealSequence().
					getTracks()[channel], channel);
		int totalNotes = idealSequenceNotes.size();

		for (int i = 0; i < totalNotes; i++)
		{
			long length = BitString.RAND.nextLong();
			if (length < 0)
			{
				length = -length;
			}
			length = length % (totalTicks);
			// the length can't be zero
			length++;

			long startingTick = BitString.RAND.nextLong();
			if (startingTick < 0)
			{
				startingTick = -startingTick;
			}
			startingTick = startingTick % (totalTicks - length + 1);

			notes.add(new Note(startingTick, length, channel, 
						BitString.RAND.nextInt(128), 100));
		}

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			for (int i = 0; i < notes.size(); i++)
			{
				notes.get(i).setTrack(track);
				notes.get(i).addToTrack();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MidiIndividualTrack(Vector<Note> notes, int channel) 
	{
		alreadyCalcedFitness = false; 
		this.channel = channel;

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			for (int i = 0; i < notes.size(); i++)
			{
				notes.get(i).setChannel(channel);
				notes.get(i).setTrack(track);
				notes.get(i).addToTrack();
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

		
		Vector<Note> idealSequenceNotes = IdealSequence.getNotes(channel);
		Vector<Note> ourNotes = MidiHelper.getNotesFromTrack(
				this.sequence.getTracks()[channel], channel);


		// test this individual every FITNESS_TICK_AMOUNT ticks to see
		// if the right notes are being played
		for (long i = 0; i < idealSequenceNotes.lastElement().getEndTick(); 
				i += Population.FITNESS_TICK_AMOUNT)
		{
			Vector<Note> idealSequencePlayingNotes = 
				MidiHelper.getNotesPlayingAtTick(idealSequenceNotes, i);
			Vector<Note> ourSequencePlayingNotes = 
				MidiHelper.getNotesPlayingAtTick(ourNotes, i);

			// if the correct notes are being played, the fitness will increase
			if (idealSequencePlayingNotes.size() != 
					ourSequencePlayingNotes.size())
			{
				fitness -= 1.5;
			}
			else if (idealSequencePlayingNotes.equals(ourSequencePlayingNotes))
			{
				fitness += 1;
			}
		}
		
		storedFitness = fitness;
		alreadyCalcedFitness = true;

		return fitness;
	}
	



	public MidiIndividualTrack makeAnother() 
	{
		return new MidiIndividualTrack(channel);
	}


	public MidiIndividualTrack crossover(MidiIndividualTrack that)
	{
		// it is assumed they are both the same length
		Vector<Note> thisNotes = MidiHelper.getNotesFromTrack(
				this.sequence.getTracks()[channel], channel);
		Vector<Note> thatNotes = MidiHelper.getNotesFromTrack(
				that.getSequence().getTracks()[channel], channel);

		assert thisNotes.size() == thatNotes.size();

		int crossPoint = BitString.RAND.nextInt(thisNotes.size());

		Vector<Note> resultNotes = new Vector<Note>();

		for(int i = 0; i < crossPoint; i++) {
			resultNotes.add(thisNotes.get(i));
		}

		for(int j = crossPoint; j < thatNotes.size(); j++) {
			resultNotes.add(thatNotes.get(j));
		}

		return new MidiIndividualTrack(resultNotes, channel);
	}

	
	public void mutate (double mutationRate)
	{
		alreadyCalcedFitness = false;
		
		// Augment the mutation rate and then test to mutate on each note.
		Vector<Note> notes = MidiHelper.getNotesFromTrack(
				this.sequence.getTracks()[channel], channel);

		// new mutation rate
		mutationRate = mutationRate / notes.size();

		for (int i = 0; i < notes.size(); i++)
		{
			// check if note i will be mutated
			double roll = BitString.RAND.nextDouble();

			if (roll < mutationRate)
			{
				// get the new note value
				int randomNoteValue = BitString.RAND.nextInt(128);

				// find out how many ticks the ideal sequence is
				long totalTicks = IdealSequence.getIdealSequence().
					getTracks()[channel].ticks(); 

				long length = BitString.RAND.nextLong();
				if (length < 0)
				{
					length = -length;
				}
				length = length % (totalTicks);
				// the length can't be zero
				length++;

				long startingTick = BitString.RAND.nextLong();
				if (startingTick < 0)
				{
					startingTick = -startingTick;
				}
				startingTick = startingTick % (totalTicks - length + 1);


				notes.get(i).removeFromTrack();
				notes.get(i).setNoteValue(randomNoteValue);
				notes.get(i).setStartTickAndLength(startingTick, length);
				notes.get(i).addToTrack();

			}

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

		/*
		MidiIndividualTrack midiIndv1 = new MidiIndividualTrack();
		System.out.println("Individual 1: " + midiIndv1);
		System.out.println("fitness: " + midiIndv1.fitness());
		System.out.println();

		
		MidiIndividualTrack midiIndv2 = new MidiIndividualTrack();
		System.out.println("Individual 2: " + midiIndv2);
		System.out.println("fitness: " + midiIndv2.fitness());
		System.out.println();

		
		MidiIndividualTrack newIndividual = midiIndv1.crossover(midiIndv2);
		System.out.println("crossover Individual: " + newIndividual);
		System.out.println("fitness: " + newIndividual.fitness());
		System.out.println();

		newIndividual.mutate(1);
		System.out.println("mutated crossover Individual: " + newIndividual);
		System.out.println("fitness: " + newIndividual.fitness());
		System.out.println();
		
		*/

	}

}

