package ts.math.eeg.surrogate;

import java.util.Random;

public class RandomVectorGenerator {

	private int length;

	public RandomVectorGenerator(int length) {
		super();
		this.length = length;
	}

	public double[] generate(double min, double max) {

		double[] result = new double[length];

		long seed = new Random().nextLong();

		Random random = new Random(seed);

		for (int i = 0; i < result.length; i++) {
			result[i] = min + (max - min) * random.nextDouble();
		}

		return result;

	}

}
