package ts.math.eeg.charts;

import javax.swing.JFrame;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ts.math.eeg.file.readers.EegFileReader;
import ts.math.eeg.utils.Constants;

public class EegChart extends JFrame {

	public EegChart(RealMatrix data) {

		XYDataset dataset = createDataset(data);

		JFreeChart chart = ChartFactory.createXYLineChart("asdfasfd", "time", "value", dataset, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(chart);

		// default size
		chartPanel.setPreferredSize(new java.awt.Dimension(1024, 768));
		// add it to our application
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(chartPanel);
		pack();
		setVisible(true);

	}

	private XYDataset createDataset(RealMatrix data) {
		final XYSeriesCollection dataset = new XYSeriesCollection();
		for (int i = 0; i < data.getColumnDimension(); i++) {
			final XYSeries electrode_i = new XYSeries(String.valueOf(i));
			for (int j = 0; j < data.getRowDimension(); j++) {
				electrode_i.add(j, data.getEntry(j, i));
			}
			dataset.addSeries(electrode_i);
		}
		return dataset;
	}

	public static void main(String[] args) {
		try {

			int times = 360_000;
			int electrodes = 19;

			double[][] data = new EegFileReader(times, electrodes).read(Constants.INPUT);

			RealMatrix matrix = new Array2DRowRealMatrix(data);
			//matrix = matrix.getSubMatrix(0, 150000, 0, 18);
			new EegChart(matrix);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
