package ts.math.eeg.utils;

import java.io.IOException;
import java.io.OutputStream;

public final class ArrayUtils {

	private static final String TAB = "\t";

	private static final String NEW_LINE = "\n";

	private ArrayUtils() {

	}

	public static <T> void print(T[][] array, OutputStream os) throws IOException {
		for (int i = 0; i < array.length; i++) {
			T[] row = array[i];
			for (int j = 0; j < row.length; j++) {
				os.write((array[i][j].toString() + TAB).getBytes());
			}
			os.write(NEW_LINE.getBytes());
		}
	}

	public static void print(double[] array) {

		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i] + TAB);
		}

		System.out.println();

	}

	public static double[][] repmat(double[] m, int size) {
		double[][] repmap = new double[size][m.length];
		for (int i = 0; i < size; i++) {
			repmap[i] = m;
		}
		return repmap;
	}

}
