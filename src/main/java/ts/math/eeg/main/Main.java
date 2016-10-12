package ts.math.eeg.main;

import ts.math.eeg.file.readers.EegFileReader;
import ts.math.eeg.mi.MutualInformationMatrixGenerator;
import ts.math.eeg.utils.ArrayUtils;
import ts.math.eeg.utils.Constants;

public class Main {

	public static void main(String[] args) {
		try {

			int measurements = 360_000;
			int electodes = 19;

			double[][] input = new EegFileReader(measurements, electodes).read(Constants.INPUT);

			int numberOfSamples = 1_000;

			double[][][] mi = new MutualInformationMatrixGenerator(numberOfSamples, 19).calculate(input);

			print(mi);

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}

	private static void print(double[][][] mi) {
		for (int i = 0; i < mi.length; i++) {

			for (int j = 0; j < mi[i].length; j++) {

				double[] single = mi[i][j];

				System.out.print(new Main.Point(i, j).toString() + "\t");

				ArrayUtils.print(single);

			}
		}
	}

	static class Point {

		private int x;

		private int y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Point [x=");
			builder.append(x);
			builder.append(", y=");
			builder.append(y);
			builder.append("]");
			return builder.toString();
		}

	}

}
