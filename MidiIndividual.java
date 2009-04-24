package geneticmidi;

import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

public class MidiIndividual implements Individual<MidiIndividual> {

	Sequence sequence;

	public MidiIndividual() {
		System.out.println(DebugMidi.sequenceEventsToString(
					IdealSequence.getIdealSequence()));

		try {
			sequence = new Sequence(IdealSequence.getDivisionType(),
					IdealSequence.getResolution());

			Track track = sequence.createTrack();

			track.add(MidiHelper.createNoteOnEvent(0, 60, 100));
			track.add(MidiHelper.createNoteOffEvent(480, 60, 127));

			track.add(MidiHelper.createNoteOnEvent(480, 64, 100));
			track.add(MidiHelper.createNoteOffEvent(960, 64, 127));

			track.add(MidiHelper.createNoteOnEvent(960, 67, 100));
			track.add(MidiHelper.createNoteOffEvent(1440, 67, 127));

			track.add(MidiHelper.createNoteOnEvent(1440, 72, 100));
			track.add(MidiHelper.createNoteOffEvent(1920, 72, 127));


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

