package ts.math.eeg.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;

public class JGraphTConverter extends JFrame {

	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");

	private static final int NODE_SIZE = 20;

	private static final int WIDTH = 800;

	private static final int HEIGHT = 600;

	private static final Dimension DEFAULT_SIZE = new Dimension(WIDTH, HEIGHT);

	private Graph<Integer, DefaultEdge> graph;

	private JGraphModelAdapter<Integer, DefaultEdge> adapter;

	public JGraphTConverter(double[][] matrix) {

		this.graph = GraphFactory.createDefaultUnDirectedGraph(matrix);

		this.adapter = new JGraphModelAdapter<>(graph);

		this.adapter = new JGraphModelAdapter<>(graph);
		JGraph jgraph = new JGraph(adapter);

		jgraph.setPreferredSize(DEFAULT_SIZE);
		jgraph.setBackground(DEFAULT_BG_COLOR);

		positionVertices(matrix);

		getContentPane().add(jgraph);
		pack();
		setVisible(Boolean.TRUE);
		setSize(DEFAULT_SIZE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void positionVertices(double[][] adjmatrix) {

		for (int i = 0; i < adjmatrix.length; i++) {

			int randomX = new Random().nextInt(WIDTH);
			int randomY = new Random().nextInt(HEIGHT);

			positionVertexAt(i, randomX, randomY);

		}

	}

	private void positionVertexAt(Integer vertex, int x, int y) {
		DefaultGraphCell cell = adapter.getVertexCell(vertex);
		AttributeMap attr = cell.getAttributes();
		Rectangle2D b = GraphConstants.getBounds(attr);

		GraphConstants.setBounds(attr, new Rectangle(x, y, NODE_SIZE, NODE_SIZE));

		Map<DefaultGraphCell, AttributeMap> cellAttr = new HashMap<>();

		cellAttr.put(cell, attr);

		adapter.edit(cellAttr, null, null, null);
	}

	public Graph<Integer, DefaultEdge> getGraph() {
		return graph;
	}

	public static void main(String[] args) {
		double[][] matrix = new double[19][19];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (isEven(i) && isOdd(j)) {
					matrix[i][j] = 1;
				}
			}
		}

		JGraphTConverter c = new JGraphTConverter(matrix);
		List<DefaultEdge> result = DijkstraShortestPath.findPathBetween(c.getGraph(), 1, 9);
		if (null != result) {
			for (DefaultEdge edge : result) {
				System.out.println(edge);
			}
		}
	}

	private static boolean isEven(int i) {
		return i % 3 == 0;
	}

	private static boolean isOdd(int i) {
		return i % 5 == 0;
	}

}
