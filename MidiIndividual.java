package geneticmidi;


public class MidiIndividual implements Individual<MidiIndividual> {

	public MidiIndividual() {
		System.out.println(DebugMidi.sequenceEventsToString(
					IdealSequence.getIdealSequence()));


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

