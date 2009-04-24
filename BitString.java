package genetic;

import java.util.Random;

public abstract class BitString<I extends BitString<I>> {

	private boolean[] bits;

	public static final Random RAND = new Random();

	public BitString(int length) {
		bits = new boolean[length];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = RAND.nextBoolean();
		}
	}

	public BitString(String s) {
		bits = new boolean[s.length()];
		for (int i = 0; i < bits.length; i++) {
			if (s.charAt(i) == '0') {
				bits[i] = false;
			} else {
				bits[i] = true;
			}
		}
	}

	public I crossover(I that) {
		int crossPoint = RAND.nextInt(bits.length), i;
		String resultDescription = "";
		for(i = 0; i < crossPoint; i++) {
			if(bits[i]) {
				resultDescription += "1";
			} else {
				resultDescription += "0";
			}
		}
		for(; i < bits.length; i++) {
			if(that.bits[i]) {
				resultDescription += "1";
			} else {
				resultDescription += "0";
			}
		}
		return makeAnother(resultDescription);
	}

	public boolean get(int a) {
		return bits[a];
	}

	public void mutate(double mutationRate) {
		double roll = RAND.nextDouble();
		if (roll < mutationRate) {
			int random = RAND.nextInt(bits.length);
			bits[random] = !bits[random];
		}
	
	}

	protected int length() {
		return bits.length;
	}

	public String toString() {
		String string = new String();
		for (boolean b : bits) {
			if (b) {
				string += 1;
			} else {
				string += 0;
			}
		}
		return string;
	}

	public abstract I makeAnother(String bits);

}