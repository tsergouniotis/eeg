package ts.math.eeg.surrogate;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ts.math.eeg.utils.ArrayUtils;

public class SurrogateMatrixGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(SurrogateMatrixGenerator.class);

	private int frame;

	public SurrogateMatrixGenerator(int frame) {
		super();
		this.frame = frame;
	}

	public double[][] calculate(Array2DRowRealMatrix input) {

		int epochs = input.getRowDimension() / frame;

		int electrodes = input.getColumnDimension();

		double[][] result = new double[input.getRowDimension()][electrodes];

		for (int epoch = 0; epoch < epochs; epoch++) {

			RealMatrix subMatrix = input.getSubMatrix(epoch * frame, (epoch + 1) * frame - 1, 0, electrodes - 1);

			double[] eMean = new double[subMatrix.getColumnDimension()];

			for (int j = 0; j < subMatrix.getColumnDimension(); j++) {
				double v = new DescriptiveStatistics(subMatrix.getRow(j)).getMean();
				eMean[j] = v;
			}

			Array2DRowRealMatrix repMatrix = new Array2DRowRealMatrix(ArrayUtils.repmat(eMean, frame));

			RealMatrix res = subMatrix.subtract(repMatrix);

			Array2DRowFieldMatrix<Complex> x = fft(res);

			Array2DRowFieldMatrix<Complex> random = fft(generateRandom(x.getRowDimension(), x.getColumnDimension()));

			Array2DRowFieldMatrix<Complex> exp = exp(random);

			Array2DRowRealMatrix abs = abs(x);

			LOGGER.info("Size of x is " + x.getRowDimension() + ", " + x.getColumnDimension());
			LOGGER.info("Size of exp is  " + exp.getRowDimension() + ", " + exp.getColumnDimension());

			Array2DRowFieldMatrix<Complex> foo = multiply(x, exp);

			System.out.println(foo);

		}

		return result;
	}

	private Array2DRowFieldMatrix<Complex> multiply(Array2DRowFieldMatrix<Complex> x,
			Array2DRowFieldMatrix<Complex> exp) {
		int row = x.getRowDimension();
		int column = x.getColumnDimension();
		Array2DRowFieldMatrix<Complex> res = new Array2DRowFieldMatrix<>(ComplexField.getInstance(), row, column);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				Complex a = x.getEntry(i, j);
				Complex b = exp.getEntry(i, j);
				res.setEntry(i, j, a.multiply(b));
			}
		}
		return res;
	}

	private Array2DRowRealMatrix abs(Array2DRowFieldMatrix<Complex> x) {
		int row = x.getRowDimension();
		int column = x.getColumnDimension();
		Array2DRowRealMatrix res = new Array2DRowRealMatrix(row, column);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				res.setEntry(i, j, x.getEntry(i, j).abs());
			}
		}
		return res;
	}

	private Array2DRowFieldMatrix<Complex> exp(Array2DRowFieldMatrix<Complex> random) {
		int row = random.getRowDimension();
		int column = random.getColumnDimension();

		Array2DRowFieldMatrix<Complex> exp = new Array2DRowFieldMatrix<Complex>(ComplexField.getInstance(), row,
				column);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				double value = random.getEntry(i, j).getArgument();
				Complex complex = new Complex(0, value).exp();
				exp.setEntry(i, j, complex);
			}
		}
		return exp;
	}

	private Array2DRowRealMatrix generateRandom(int row, int column) {
		Array2DRowRealMatrix random = new Array2DRowRealMatrix(row, column);
		for (int i = 0; i < random.getRowDimension(); i++) {
			random.setRow(i, new RandomVectorGenerator(column).generate(0, 1));
		}
		return random;
	}

	private Array2DRowFieldMatrix<Complex> fft(RealMatrix res) {
		Array2DRowFieldMatrix<Complex> x = null;

		for (int e = 0; e < res.getColumnDimension(); e++) {

			FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
			RealVector vector = res.getColumnVector(e);
			int n = vector.getDimension();
			while (!ArithmeticUtils.isPowerOfTwo(n)) {
				vector = vector.append(0);
				n++;
			}
			Complex[] complexArray = fft.transform(vector.toArray(), TransformType.FORWARD);

			if (null == x) {
				x = new Array2DRowFieldMatrix<Complex>(ComplexField.getInstance(), n, res.getColumnDimension());
			}

			x.setColumn(e, complexArray);
		}
		return x;
	}

}
