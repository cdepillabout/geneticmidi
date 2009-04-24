package geneticmidi;


public class MidiIndividual implements Individual<MidiIndividual> {

	public MidiIndividual() {

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
		//FlowerBed bed = new FlowerBed();
		//System.out.println("fitness: " + bed.fitness());
	}

}

