package genetic;

import java.awt.geom.Point2D;

public class FlowerBed implements Individual<FlowerBed> {

	/** The total number of flowers in this FlowerBed. */
	public static int TOTAL_FLOWERS = 15;

	protected double [] flowersX;
	protected double [] flowersY;

	public FlowerBed() {

		flowersX = new double [TOTAL_FLOWERS];
		flowersY = new double [TOTAL_FLOWERS];

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			flowersX[i] = BitString.RAND.nextDouble() * 2;

			double ymax = Math.sqrt( 9 - Math.pow(flowersX[i], 2) );
			flowersY[i] = BitString.RAND.nextDouble() * (ymax - 2) + 2;
		}

		//System.out.println("Created Flowerbed: ");
		//System.out.println(this);

	}

	public FlowerBed(double [] newXs, double [] newYs) {
		flowersX = new double [TOTAL_FLOWERS];
		flowersY = new double [TOTAL_FLOWERS];

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			flowersX[i] = newXs[i];
			flowersY[i] = newYs[i];
		}

	}

	public double fitness() {
		double sum = 0;
		double closestEdgeDist;
		double closestOtherFlowerDist;

		//System.out.println("In fitness for " + this.toStringNoFitness());

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			
			closestEdgeDist = closestEdge(i);
			if (closestEdgeDist < 0)
			{
				System.out.println("EdgeDist for " + i + " (" + 
						flowersX[i] + ", " + flowersY[i] +
						") is less than 0: " + closestEdgeDist);
				System.out.println("Flowerbed: " + this.toStringNoFitness());
			}

			closestOtherFlowerDist = closestFlower(i);
			if (closestOtherFlowerDist < 0)
			{
				System.out.println("OtherFlowerDist for " + i + " (" + 
						flowersX[i] + ", " + flowersY[i] +
						") is less than 0: " + closestOtherFlowerDist);
				System.out.println("Flowerbed: " + this.toStringNoFitness());
			}

			sum += Math.min(closestEdgeDist, closestOtherFlowerDist);
		}

		return sum;
	}


	public double closestEdge(int index)
	{
		double shortestDistance;

		double x = flowersX[index];
		double y = flowersY[index];

		double distFromRight = x;
		shortestDistance = distFromRight;

		double distFromBottom = y - 2;
		shortestDistance =Math.min(shortestDistance, distFromBottom);

		double distFromLeft = 2 - x;
		shortestDistance = Math.min(shortestDistance, distFromLeft);

		double temp = Math.pow(x, 2) + Math.pow(y, 2);
		double distFromTop = 3 - Math.sqrt(temp);
		shortestDistance = Math.min(shortestDistance, distFromTop);

		return shortestDistance;
	}

	public double closestFlower(int index)
	{
		double shortestDistance = 999999999;

		double thisX = flowersX[index];
		double thisY = flowersY[index];

		double otherX;
		double otherY;

		double xDist;
		double yDist;
		double distance;

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			if (i != index)
			{
				otherX = flowersX[i];
				otherY = flowersY[i];

				xDist = Math.pow(thisX - otherX, 2);
				yDist = Math.pow(thisY - otherY, 2);
				distance = Math.sqrt(xDist + yDist);

				shortestDistance = Math.min(shortestDistance, distance);
			}

			//System.out.printf("Distance between this point and %d (%f, %f): %f\n", 
			//		i, otherFlower.getPosition().getX(), otherFlower.getPosition().getY(),
			//		distance);
		}

		return shortestDistance;
	}

	public FlowerBed makeAnother() {
		return new FlowerBed();
	}


	public FlowerBed crossover(FlowerBed that)
	{
		double [] newXs = new double [TOTAL_FLOWERS];
		double [] newYs = new double [TOTAL_FLOWERS];

		int crossPoint = BitString.RAND.nextInt(TOTAL_FLOWERS);

		int i;

		for (i = 0; i < crossPoint; i++)
		{
			newXs[i] = flowersX[i];
			newYs[i] = flowersY[i];
		}
		for (; i < TOTAL_FLOWERS; i++)
		{
			newXs[i] = that.getFlower(i).getX();
			newYs[i] = that.getFlower(i).getY();
		}

		return new FlowerBed(newXs, newYs);
	}

	public void mutate (double mutationRate)
	{
		double roll = BitString.RAND.nextDouble();
		if (roll < mutationRate)
		{
			int flowerIndex = BitString.RAND.nextInt(TOTAL_FLOWERS);

			flowersX[flowerIndex] = BitString.RAND.nextDouble() * 2;

			double ymax = Math.sqrt( 9 - Math.pow(flowersX[flowerIndex], 2) );
			flowersY[flowerIndex] = BitString.RAND.nextDouble() * (ymax - 2) + 2;
		}

	}

	/** Return the number of flowers in this flower bed. */
	public int getSize()
	{
		return TOTAL_FLOWERS;
	}

	/** Returns the 2D coordinates of the ith small flower in this bed. */
	public Point2D getFlower(int i) {
		Point2D p = new Point2D.Double();        
		p.setLocation(flowersX[i], flowersY[i]);
		return p;
	}

	public static void main(String[] args) {
		FlowerBed bed = new FlowerBed();
		System.out.println("fitness: " + bed.fitness());
	}

	public String toString()
	{
		String result = "";

		result += "\n";

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			result += "Flower " + i + " "; 
			result += "(" + flowersX[i] + ", " + flowersY[i] + "): ";
			result += "Fitness " + Math.min(closestEdge(i), closestFlower(i));
			result += "\n";
		}

		result += "Total Fitness: " + fitness() + "\n";

		return result;	
	}

	public String toStringNoFitness()
	{
		String result = "";

		result += "\n";

		for (int i = 0; i < TOTAL_FLOWERS; i++)
		{
			result += "Flower " + i + " "; 
			result += "(" + flowersX[i] + ", " + flowersY[i] + "): ";
			result += "Fitness " + Math.min(closestEdge(i), closestFlower(i));
			result += "\n";
		}

		result += "\n";

		return result;	
	}

	public String getCoordinates()
	{
		String result = "";

		for(int i = 0; i < TOTAL_FLOWERS; i++)
		{
			result += "Flower " + i + ": ";
			result += "(" + flowersX[i] + ", ";
			result += flowersY[i] + ")\n";
		}

		return result;
	}
}
