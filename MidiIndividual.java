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

		// TODO: take out this hard coded '4' from here and the other constructor
		for (int i = 0; i < 4; i++)
		{
			notes.add(new Note((i * 480), 480, 0, BitString.RAND.nextInt(128), 100));
		}

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			for (int i = 0; i < notes.size(); i++)
			{
				notes.get(i).addToTrack(track);
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
				notes.get(i).addToTrack(track);
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

	}

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

	}

}

