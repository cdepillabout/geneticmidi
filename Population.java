package geneticmidi;

public class Population<I extends Individual<I>> {

	public static int NUMBER_OF_INDIVIDUALS = 10000;
	public static double MUTATION_RATE = 0.5;
	public static int TOTAL_GENERATIONS = 1000000;

	protected int generation;

	public static void main(String[] args) {
		Population<MidiIndividual> pop = new Population<MidiIndividual>(
				new MidiIndividual());

		System.out.println(pop);

		for(int i = 1; i < TOTAL_GENERATIONS; i++)
		{
			pop.evolve();
			System.out.println(pop);
		}
	}

	public void run() {
		for (int i = 0; i < NUMBER_OF_INDIVIDUALS; i++) {
			evolve();
			System.out.println("Generation " + i + ": " + this);
			I best = bestIndividual();
			System.out.println("Best Individual: " + best + "'s fitness is "
					+ best.fitness());
		}		
	}

	public I bestIndividual() {
		I best = individuals[0];
		for (I ind : individuals) {
			//System.out.println("ind is : " + ind);
			if (ind.fitness() > best.fitness()) {
				best = ind;
			}
		}
		return best;
	}

	@SuppressWarnings("unchecked")
	public void evolve() {
		generation++;
		I[] nextGeneration = (I[]) (new Individual[individuals.length]);
		for (int i = 0; i < individuals.length; i++) {
			nextGeneration[i] = produceIndividual();
			//System.out.println("Generation " + generation + ", evolved " + i +
			//		" individuals");
		}
		individuals = nextGeneration;

		//System.out.println(this);
	}

	public I produceIndividual() {
		I parent1 = chooseParent();
		I parent2 = chooseParent();
		I child = parent2.crossover(parent1);
		child.mutate(MUTATION_RATE);
		return child;
	}

	private I[] individuals;

	@SuppressWarnings("unchecked")
	public Population(I prototype) {
		generation = 0;
		individuals = (I[]) (new Individual[NUMBER_OF_INDIVIDUALS]);
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = prototype.makeAnother();
			//System.out.println("Made " + i + " individuals.");
		}

	}

	public int getGeneration()
	{
		return generation;
	}

	public I[] getIndividuals()
	{
		return individuals;
	}

	/*
	public String toString() {
		String result = "";

		result += "\nGeneration " + generation + ":\n";

		for (int i = 0; i < individuals.length; i++) 
		{
			result += "Individual " + i + ": ";
			result += individuals[i].fitness();
			result += "\n";
		}

		result += "Best Fitness: " + bestIndividual().fitness();

		return result;
	}
	*/

	public String toString() {
		String result = "";

		result += "Generation " + this.getGeneration() + ": " + "\n";

		I best = bestIndividual();

		result += "Best Individual: " + best + "Best Individual fitness: "
				+ best.fitness() + " (" + 
				String.format("%.0f", 
						(best.fitness() * 100/ IdealSequence.perfectFitness())) +
				"%)\n";

		return result;
	}

	public double averageFitness() {
		double sum = 0;
		for (int i = 0; i < individuals.length; i++) {
			sum += individuals[i].fitness();
		}
		return sum / individuals.length;
	}

	public I chooseParent() {
		I a = individuals[BitString.RAND.nextInt(individuals.length)];
		I b = individuals[BitString.RAND.nextInt(individuals.length)];
		// System.out.println(a);
		// System.out.println(b);
		if (a.fitness() > b.fitness()) {
			return a;
		}
		return b;
	}
}
