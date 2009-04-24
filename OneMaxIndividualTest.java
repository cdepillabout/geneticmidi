package genetic;

import static org.junit.Assert.*;

import org.junit.Test;

public class OneMaxIndividualTest {

	@Test
	public void testCrossover() {
		OneMaxIndividual parentA = new OneMaxIndividual(15);
		OneMaxIndividual parentB = new OneMaxIndividual(15);
		OneMaxIndividual child = parentA.crossover(parentB);
		boolean found = false;
		for(int i = 0; i < 15; i++){
			boolean mismatch = false;
			for (int j = 0; j < 15; j++) {
				if(j < i){
					if(child.get(j) != parentA.get(j)){
						mismatch = true;
					}
				}else{
					if(child.get(j) != parentB.get(j)){
						mismatch = true;
					}
				}
			}
			if(!mismatch){
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	public void testFitness() {
		OneMaxIndividual n = new OneMaxIndividual("1000100101");
		assertEquals(4, n.fitness(), 0.1);
	}

	@Test
	public void testMutate() {
		int count = 0;
		for (int i = 0; i < 1000; i++){
			OneMaxIndividual n = new OneMaxIndividual(20);
			String before = n.toString();
			n.mutate(.5);
			String after = n.toString();
			if (before.equals(after)){
				count++;
			} else {
				int differenceCount = 0;
				for (int j = 0; j < 20 ; j++){
					if (before.charAt(j) != after.charAt(j)){
						differenceCount++;
						assertEquals(1, differenceCount);
					}
				}
				
			}
		}
		assertTrue(250 < count);
		assertTrue(count < 750);
	}

	
}
