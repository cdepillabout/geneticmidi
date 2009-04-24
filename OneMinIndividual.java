package geneticmidi;

public class OneMinIndividual extends BitString<OneMinIndividual> implements Individual<OneMinIndividual> {

	public OneMinIndividual(int n) {
		super(n);
	}
	
	public OneMinIndividual(String bits) {
		super(bits);
	}
	
	public double fitness() {
		double j = 0;
		for (int i = 0; i < length(); i++) {
			if (!get(i))
				j++;
		}
		return j;
	}

	public OneMinIndividual makeAnother() {
		return new OneMinIndividual(length());
	}

	@Override
	public OneMinIndividual makeAnother(String bits) {
		return new OneMinIndividual(bits);
	}

}
