package ts.math.eeg.utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ts.math.eeg.file.readers.EegFileReader;
import ts.math.eeg.mi.MutualInformationMatrixGenerator;
import ts.math.eeg.surrogate.SurrogateMatrixGenerator;

public class MatrixTestCase {

	private int times;

	private int electrodes;

	private int frame;

	private Array2DRowRealMatrix input;

	private double[][][] mi;

	@Before
	public void load() {
		try {
			this.times = 360_000;
			this.electrodes = 19;

			double[][] data = new EegFileReader(times, electrodes).read(Constants.INPUT);
			this.input = new Array2DRowRealMatrix(data);

			this.frame = 1_000;

			this.mi = new MutualInformationMatrixGenerator(frame, electrodes).calculate(input);

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testLoadFile() {

		Assert.assertEquals(input.getRowDimension(), times);
		Assert.assertEquals(input.getColumnDimension(), electrodes);

	}

	@Test
	public void testMutualInformationCalculationMatrix() {
		Assert.assertEquals(mi.length, electrodes);
		Assert.assertEquals(mi[0].length, electrodes);
		Assert.assertEquals(mi[0][0].length, times / frame);
	}

	@Test
	public void testSurrogateMatrix() {
		
		

		double[][] foo = new SurrogateMatrixGenerator(frame).calculate(input);



	}



}
