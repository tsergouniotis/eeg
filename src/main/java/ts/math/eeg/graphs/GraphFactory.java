package ts.math.eeg.graphs;

import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public final class GraphFactory {

	private GraphFactory() {

	}

	public static DirectedGraph<Integer, DefaultEdge> createDefaultDirectedGraph(double[][] adjMatrix) {

		DirectedGraph<Integer, DefaultEdge> g = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);

		for (int i = 0; i < adjMatrix.length; i++) {
			g.addVertex(i);
			for (int j = 0; j < adjMatrix[i].length; j++) {
				g.addVertex(j);
				if (Double.valueOf(1).doubleValue() == adjMatrix[i][j]) {
					g.addEdge(i, j);
				}
			}
		}

		return g;

	}

	public static UndirectedGraph<Integer, DefaultEdge> createDefaultUnDirectedGraph(double[][] adjMatrix) {

		DirectedGraph<Integer, DefaultEdge> g = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);

		for (int i = 0; i < adjMatrix.length; i++) {
			g.addVertex(i);
			for (int j = 0; j < adjMatrix[i].length; j++) {
				g.addVertex(j);
				if (Double.valueOf(1).doubleValue() == adjMatrix[i][j]) {
					g.addEdge(i, j);
				}
			}
		}

		return new AsUndirectedGraph<Integer, DefaultEdge>(g);

	}

}
