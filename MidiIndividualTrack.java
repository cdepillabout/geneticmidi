package geneticmidi;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.MidiEvent;

import java.util.Vector;

import java.io.File;

public class MidiIndividualTrack implements Individual<MidiIndividualTrack> {

	protected Track track;

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


	public MidiIndividualTrack(Track track, int channel) 
	{
		this(track, channel, generateRandomNotes(track, channel));
	}

	public MidiIndividualTrack(Track track, int channel, MidiIndividualTrack model) 
	{
		this(track, channel, MidiHelper.getNotesFromTrack(model.getTrack(), channel));
	}

	public MidiIndividualTrack(Track track, int channel, Vector<Note> notes) 
	{
		alreadyCalcedFitness = false; 
		this.channel = channel;
		this.track = track;

		// Add all midi events from this channel from the idealsequence
		// that are not Note On or Note Off events.
		Track idealSequenceTrack = IdealSequence.getIdealSequence().getTracks()[channel];

		
		for (int i = 0; i < idealSequenceTrack.size(); i++)
		{
			MidiEvent evnt = idealSequenceTrack.get(i);

			if (!MidiHelper.isNoteOnEvent(evnt) && !MidiHelper.isNoteOffEvent(evnt))
			{
				this.track.add(evnt);
			}
		}
		

		for (int i = 0; i < notes.size(); i++)
		{
			notes.get(i).setChannel(channel);
			notes.get(i).setTrack(track);
			notes.get(i).addToTrack();
		}

	}


	/**
	 * This generates random notes for channel.  It is loosely based on the
	 * number of notes for the corresponding track and channel on IdealSequence.
	 */
	static protected Vector<Note> generateRandomNotes(Track track, int channel)
	{
		Vector<Note> notes = new Vector<Note>();

		// find out how many ticks the ideal sequence is
		long totalTicks = IdealSequence.getIdealSequence().getTracks()[channel].ticks(); 

		// find out how many notes ideal sequence has
		Vector<Note> idealSequenceNotes = 
			MidiHelper.getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[channel],
					channel);
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

			notes.add(new Note(track, startingTick, length, channel, 
						BitString.RAND.nextInt(128), 100));
		}

		return notes;

	}

	public Track getTrack()
	{
		return track;
	}

	public int getChannel()
	{
		return channel;
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
				track, channel);

		// only do this if it is not empty
		if (!idealSequenceNotes.isEmpty())
		{
			// test this individual every FITNESS_TICK_AMOUNT ticks to see
			// if the right notes are being played
			for (long i = 0; i < idealSequenceNotes.lastElement().getEndTick(); 
					i += Population.FITNESS_TICK_AMOUNT)
			{
				Vector<Note> idealSequencePlayingNotes = 
					MidiHelper.getNotesPlayingAtTick(idealSequenceNotes, i);
				Vector<Note> ourSequencePlayingNotes = 
					MidiHelper.getNotesPlayingAtTick(ourNotes, i);

				// if the correct notes are being played, the fitness will increase.
				// if the wrong number of notes are being played, the fitness will decrease.
				if (idealSequencePlayingNotes.size() != 
						ourSequencePlayingNotes.size())
				{
					fitness -= 0.25;
				}
				else if (idealSequencePlayingNotes.equals(ourSequencePlayingNotes))
				{
					fitness += 1;
				}
			}
		}
		
		storedFitness = fitness;
		alreadyCalcedFitness = true;

		return fitness;
	}
	

	/** 
	 * This isn't used, but needs to be here because of Individual.java.
	 */
	public MidiIndividualTrack makeAnother()
	{
		return null;
	}

	public MidiIndividualTrack crossover(MidiIndividualTrack that)
	{
		// make sure they the channels for the two tracks are the same
		assert channel == that.getChannel();

		// it is assumed they are both the same length
		Vector<Note> thisNotes = MidiHelper.getNotesFromTrack(
				track, channel);
		Vector<Note> thatNotes = MidiHelper.getNotesFromTrack(
				that.getTrack(), that.getChannel());

		// make sure that the are the same number of notes in each
		assert thisNotes.size() == thatNotes.size();

		// actually do the crossover
		int crossPoint = BitString.RAND.nextInt(thisNotes.size() + 1);

		Vector<Note> resultNotes = new Vector<Note>();

		for(int i = 0; i < crossPoint; i++) {
			resultNotes.add(thisNotes.get(i));
		}

		for(int j = crossPoint; j < thatNotes.size(); j++) {
			resultNotes.add(thatNotes.get(j));
		}


		try {
			Sequence tempseq = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());
			return new MidiIndividualTrack(tempseq.createTrack(), channel, resultNotes);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	
	public void mutate (double mutationRate)
	{
		alreadyCalcedFitness = false;
		
		// Augment the mutation rate and then test to mutate on each note.
		Vector<Note> notes = MidiHelper.getNotesFromTrack(track, channel);

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



	public String toString()
	{
		return "Channel " + channel + "\n" +
			DebugMidi.trackEventsToString(track);
	}

	public static void main(String[] args) 
	{

		Sequence sequence = null;
		Track track1 = null;
		Track track2 = null;

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());
			track1 = sequence.createTrack();
			track2 = sequence.createTrack();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		int channel = 6;

		MidiIndividualTrack midiIndv1 = new MidiIndividualTrack(track1, channel);
		System.out.println("Individual 1: " + midiIndv1);
		System.out.println("fitness: " + midiIndv1.fitness());
		System.out.println();

		
		MidiIndividualTrack midiIndv2 = new MidiIndividualTrack(track2, channel);
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
	}

}

