package geneticmidi;

public class Population<I extends Individual<I>> {

	/**
	 * This is the number of individuals to have in the population.
	 */
	public static int NUMBER_OF_INDIVIDUALS = 100;

	/**
	 * This is the mutation rate.  For example, if the mutation rate is
	 * 0.5, then half of the individuals will mutate each evolution.
	 */
	public static double MUTATION_RATE = .50;

	/** 
	 * When Population is run by itself, this determines how many
	 * evolutions it will go through.
	 */
	public static int TOTAL_GENERATIONS = 1000000;

	/**
	 * chooseParent() uses a tornament selection (not roulette wheel),
	 * and this is the number of individuals that will compete in
	 * that tournament.
	 */
	public static int CHOOSE_PARENT_AMOUNT = 10;

	/**
	 * How many ticks to skip when calculating the fitness
	 * in MidiIndividual.  For example, if it is 10, then
	 * the fitness will be calculated every 10 ticks.
	 */
	public static int FITNESS_TICK_AMOUNT = 10;

	/**
	 * This just holds the generation number that will are working on.
	 */
	protected int generation;

	public static void main(String[] args) {
		Population<MidiIndividual> pop = new Population<MidiIndividual>(
				new MidiIndividual());

		System.out.println(pop);

		for(int i = 1; i < TOTAL_GENERATIONS; i++)
		{
			pop.evolve();

			if (i % 10 == 0)
			{
				System.out.println(pop);
				pop.bestIndividual().writeSequence("best_individual.mid");
			}

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

		result += "Best Individual: " + best;
		
		result += "Best Individual fitness: " + 
			String.format("%.2f (%.0f%%)\n", best.fitness(), 
					(best.fitness() * 
					 100 / IdealSequence.perfectFitness()
					));

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

		I selection = individuals[BitString.RAND.nextInt(individuals.length)];

		for (int i = 0; i < CHOOSE_PARENT_AMOUNT; i++)
		{
			I tempSelection = individuals[BitString.RAND.nextInt(individuals.length)];

			if (selection.fitness() < tempSelection.fitness())
			{
				selection = tempSelection;
			}
		}

		return selection;
	}
}
