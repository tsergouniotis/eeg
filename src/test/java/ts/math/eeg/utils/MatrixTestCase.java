package ts.math.eeg.utils;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ts.math.eeg.file.readers.EegFileReader;
import ts.math.eeg.mi.MutualInformationMatrixGenerator;

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
}
