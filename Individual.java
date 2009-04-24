package geneticmidi;

/**
 * An individual in a genetic algorithm.
 */
public interface Individual<I extends Individual<I>> {

	/**
	 * Returns an individual that is a cross between this and that.
	 */
	public I crossover(I that);

	/**
	 * Returns the fitness of the individual as a double.
	 */
	public double fitness();

	/**
	 * Returns a new, random individual of this type.
	 */
	public I makeAnother();
	
	/**
	 * Mutates the individual with mutation rate mutationRate.
	 */
	public void mutate(double mutationRate);

}