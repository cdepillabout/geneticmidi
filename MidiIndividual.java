package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MidiIndividual implements Individual<MidiIndividual> {

	Sequence sequence;

	public MidiIndividual() {

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			new Note(track, 0, 480, 0, 60, 100);
			new Note(track, 480, 480, 0, 64, 100);
			new Note(track, 960, 480, 0, 67, 100);
			new Note(track, 1440, 480, 0, 72, 100);

			System.out.println("Ideal Sequence: ");
			System.out.println(DebugMidi.sequenceEventsToString(
						IdealSequence.getIdealSequence()));

			System.out.println("This Sequence: ");
			System.out.println(DebugMidi.sequenceEventsToString(sequence));
			//MidiHelper.play(IdealSequence.getIdealSequence());
			MidiHelper.play(sequence);

		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public double fitness() {
		return -1000;
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

	public static void main(String[] args) {
		MidiIndividual midiIndv = new MidiIndividual();
		//System.out.println("fitness: " + bed.fitness());
	}

}

