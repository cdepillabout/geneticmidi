package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.Vector;

public class MidiIndividual implements Individual<MidiIndividual> {

	Sequence sequence;

	public MidiIndividual() 
	{
		Vector<Note> notes = new Vector<Note>();

		/*
		   notes.add(new Note(track, 0, 480, 0, MidiHelper.getValueFromNote("C4"), 100));
		   notes.add(new Note(track, 480, 480, 0, MidiHelper.getValueFromNote("E4"), 100));
		   notes.add(new Note(track, 960, 480, 0, MidiHelper.getValueFromNote("G4"), 100));
		   notes.add(new Note(track, 1440, 480, 0, MidiHelper.getValueFromNote("C5"), 100));
		   */

		// find out how many ticks the ideal sequence is
		long totalTicks = IdealSequence.getIdealSequence().getTracks()[0].ticks(); 

		// find out how many notes ideal sequence has
		Vector<Note> idealSequenceNotes = 
			MidiHelper.getNotesFromTrack(IdealSequence.getIdealSequence().getTracks()[0]);
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

			//System.out.println("length = " + length + ", startingTick = " + startingTick);
			//System.out.println("nextLong() =  " + BitString.RAND.nextLong());
			//notes.add(new Note((i * 480), 480, 0, BitString.RAND.nextInt(128), 100));
			notes.add(new Note(startingTick, length, 0, BitString.RAND.nextInt(128), 100));
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

	public MidiIndividual(Vector<Note> notes) 
	{
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

	public Sequence getSequence()
	{
		return sequence;
	}

	public double fitness() 
	{
		double fitness = 0;

		
		Vector<Note> idealSequenceNotes = IdealSequence.getNotes();
		Vector<Note> ourNotes = MidiHelper.getNotesFromTrack(this.sequence.getTracks()[0]);


		for (long i = 0; i < idealSequenceNotes.lastElement().getEndTick(); i += 10)
		{
			Vector<Note> idealSequencePlayingNotes = 
				MidiHelper.getNotesPlayingAtTick(idealSequenceNotes, i);
			Vector<Note> ourSequencePlayingNotes = 
				MidiHelper.getNotesPlayingAtTick(ourNotes, i);

			if (idealSequencePlayingNotes.equals(ourSequencePlayingNotes))
			{
				fitness += 1;
			}
		}
		

		return fitness;
	}



	public MidiIndividual makeAnother() 
	{
		return new MidiIndividual();
	}


	public MidiIndividual crossover(MidiIndividual that)
	{
		// it is assumed they are both the same length
		Vector<Note> thisNotes = MidiHelper.getNotesFromTrack(
				this.sequence.getTracks()[0]);
		Vector<Note> thatNotes = MidiHelper.getNotesFromTrack(
				that.getSequence().getTracks()[0]);

		assert thisNotes.size() == thatNotes.size();

		int crossPoint = BitString.RAND.nextInt(thisNotes.size());

		Vector<Note> resultNotes = new Vector<Note>();

		for(int i = 0; i < crossPoint; i++) {
			resultNotes.add(thisNotes.get(i));
		}

		for(int j = crossPoint; j < thatNotes.size(); j++) {
			resultNotes.add(thatNotes.get(j));
		}

		return new MidiIndividual(resultNotes);
	}

	
	public void mutate (double mutationRate)
	{
		
		// Augment the mutation rate and then test to mutate on each note.
		Vector<Note> notes = MidiHelper.getNotesFromTrack(this.sequence.getTracks()[0]);

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
				long totalTicks = IdealSequence.getIdealSequence().getTracks()[0].ticks(); 

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

				/*
				System.out.println("\nthis track:\n" + 
						DebugMidi.trackEventsToString(this.sequence.getTracks()[0]));
				System.out.println("This note: " + notes.get(i));
				System.out.println("Events in this track: " + 
						this.sequence.getTracks()[0].size());
						*/

				notes.get(i).removeFromTrack();
				notes.get(i).setNoteValue(randomNoteValue);
				notes.get(i).setStartTickAndLength(startingTick, length);
				notes.get(i).addToTrack();

			}

		}
		
		
	}

	public void writeSequence(String filename)
	{

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

