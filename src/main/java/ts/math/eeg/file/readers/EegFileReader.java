package ts.math.eeg.file.readers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class EegFileReader {

	private int rows;

	private int columns;

	public EegFileReader(int rows, int columns) {
		super();
		this.rows = rows;
		this.columns = columns;
	}

	public double[][] read(String filePath) throws Exception {

		double[][] result = new double[rows][columns];

		int row = 0;

/*		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

			lines.forEach(line -> {
				String[] elements = line.split(",");
				for (int column = 0; column < columns; column++) {
					result[row][column] = Double.valueOf(elements[column]);
				}

			});

		}*/

		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			for (String line : (Iterable<String>) lines::iterator) {
				String[] elements = line.split(",");
				for (int column = 0; column < columns; column++) {
					result[row][column] = Double.valueOf(elements[column]);
				}
				row++;
			}
		}

		return result;

	}

}
