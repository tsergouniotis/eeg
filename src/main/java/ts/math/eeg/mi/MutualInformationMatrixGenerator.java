package ts.math.eeg.mi;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import JavaMI.MutualInformation;

public class MutualInformationMatrixGenerator {

	private int frame;

	private int electrodes;

	public MutualInformationMatrixGenerator(int frame, int electodes) {
		super();
		this.frame = frame;
		this.electrodes = electodes;
	}

	public double[][][] calculate(double[][] input) {

		int epochs = input.length / frame;

		double[][][] result = new double[electrodes][electrodes][epochs];

		for (int i = 0; i < electrodes; i++) {

			for (int epoch = 0; epoch < epochs; epoch++) {

				double[] ei = new double[frame];
				for (int t = 0; t < frame; t++) {
					ei[t] = input[epoch * frame + t][i];
				}

				for (int j = 0; j < electrodes; j++) {

					if (i != j) {
						double[] ej = new double[frame];

						for (int t = 0; t < frame; t++) {
							ej[t] = input[epoch * frame + t][j];
						}

						result[i][j][epoch] = MutualInformation.calculateMutualInformation(ei, ej);
					}

				}

			}

		}

		return result;
	}

	public double[][][] calculate(Array2DRowRealMatrix input) {

		int times = input.getRowDimension();

		int frame = 1_000;

		int epochs = times / frame;

		double[][][] result = new double[electrodes][electrodes][epochs];
		for (int epoch = 0; epoch < epochs; epoch++) {
			for (int i = 0; i < electrodes; i++) {
				double[] ei = input.getSubMatrix(epoch * frame, (epoch + 1) * frame - 1, i, i).getColumn(0);
				for (int j = 0; j < electrodes; j++) {
					if (i != j) {
						double[] ej = input.getSubMatrix(epoch * frame, (epoch + 1) * frame - 1, j, j).getColumn(0);
						result[i][j][epoch] = MutualInformation.calculateMutualInformation(ei, ej);
					}
				}
			}
		}

		return result;

	}

}
