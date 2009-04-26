package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.Vector;

public class MidiIndividual implements Individual<MidiIndividual> {

	Sequence sequence;

	public MidiIndividual() {

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			
			/*
			new Note(track, 0, 480, 0, MidiHelper.getValueFromNote("C4"), 100);
			new Note(track, 480, 480, 0, MidiHelper.getValueFromNote("E4"), 100);
			new Note(track, 960, 480, 0, MidiHelper.getValueFromNote("G4"), 100);
			new Note(track, 1440, 480, 0, MidiHelper.getValueFromNote("C5"), 100);
			*/
			

			
			for (int i = 0; i < 4; i++)
			{
				new Note(track, (i * 480), 480, 0, 
						BitString.RAND.nextInt(128), 100);
			}
			

			//System.out.println("Ideal Sequence: ");
			//System.out.println(DebugMidi.sequenceEventsToString(
			//			IdealSequence.getIdealSequence()));

			//System.out.println("This Sequence: ");
			//System.out.println(DebugMidi.sequenceEventsToString(sequence));
			//MidiHelper.play(IdealSequence.getIdealSequence());
			//MidiHelper.play(sequence);

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public double fitness() 
	{
		double fitness = 0;

		
		Vector<Note> idealSequenceNotes = IdealSequence.getNotes();
		Vector<Note> ourNotes = MidiHelper.getNotesFromTrack(this.sequence.getTracks()[0]);


		/*


		Vector<Note> idealSequencePlayingNotes = 
			MidiHelper.getNotesPlayingAtTick(idealSequenceNotes, 0);
		Vector<Note> ourSequencePlayingNotes = 
			MidiHelper.getNotesPlayingAtTick(ourNotes, 0);

		System.out.println("idealSequencePlayingNotes: " + idealSequencePlayingNotes);
		System.out.println("ourSequencePlayingNotes: " + ourSequencePlayingNotes);
		
		System.out.println("are these vectors equal? " + 
				(idealSequencePlayingNotes.equals(ourSequencePlayingNotes)));
		
				*/

		
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



	public MidiIndividual makeAnother() {
		return new MidiIndividual();
	}


	public MidiIndividual crossover(MidiIndividual that)
	{
		return new MidiIndividual();
	}

	public void mutate (double mutationRate)
	{

	}

	public String toString()
	{
		return DebugMidi.sequenceEventsToString(sequence);
	}

	public static void main(String[] args) {
		MidiIndividual midiIndv = new MidiIndividual();
		System.out.println("Individual: " + midiIndv);
		System.out.println("fitness: " + midiIndv.fitness());
		//System.out.println("fitness: " + bed.fitness());
	}

}

