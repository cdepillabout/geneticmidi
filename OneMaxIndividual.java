package geneticmidi;

public class OneMaxIndividual extends BitString<OneMaxIndividual> implements Individual<OneMaxIndividual> {

	public OneMaxIndividual(int length) {
		super(length);
	}

	public OneMaxIndividual(String s) {
		super(s);
	}

	public double fitness() {
		double j = 0;
		for (int i = 0; i < length(); i++) {
			if (get(i))
				j++;
		}
		return j;
	}

	public OneMaxIndividual makeAnother() {
		return new OneMaxIndividual(length());
	}

	public OneMaxIndividual makeAnother(String bits) {
		return new OneMaxIndividual(bits);
	}

	public static void main(String[] args) {
		OneMaxIndividual individual = new OneMaxIndividual(10);
		System.out.println(individual);
	}

}
