package it.polito.tdp.food.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private Map<Integer, Food> mappaCibi;
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;

	public Model() {
		dao = new FoodDao();
	}

	public Map<Integer, Food> getMappaCibi() {
		return mappaCibi;
	}

	public void creaGrafo(int porzioni) {
		mappaCibi = new TreeMap<Integer, Food>();
		dao.getCibiPerPorzione(mappaCibi, porzioni);
		grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);

		// VERTICI
		Graphs.addAllVertices(grafo, mappaCibi.values());

		// ARCHI
		for (Coppie c : dao.listAllCoppie(mappaCibi)) {
			Graphs.addEdge(grafo, c.getF1(), c.getF2(), c.getPeso());
		}

	}

	public int getNumVetex() {
		return grafo.vertexSet().size();
	}

	public int getNumEdges() {
		return grafo.edgeSet().size();
	}

	public List<Food> best(Food cibo) {
		List<DefaultWeightedEdge> archi = new LinkedList<DefaultWeightedEdge>(grafo.edgesOf(cibo));
		System.out.println(archi);
		List<Food> best = new LinkedList<Food>();
		archi.sort(new Comparator<DefaultWeightedEdge>() {
			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				if (grafo.getEdgeWeight(o1) - grafo.getEdgeWeight(o2) < 0)
					return 1;
				else
					return -1;
			}
		});
		if (archi.size() < 5) {
			return null;
		}
		for (int i = 0; i < 5; i++) {
			best.add(Graphs.getOppositeVertex(grafo, archi.get(i), cibo));
		}
		return best;
	}

}
