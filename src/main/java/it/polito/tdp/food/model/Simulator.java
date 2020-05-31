package it.polito.tdp.food.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	private Graph<Food, DefaultWeightedEdge> grafo;
	private PriorityQueue<Event> coda;
	private int numeroStazioni;
	private Food foodPartenza;

	Set<Food> cibiPreparati; //Set con tutti i cibi già preparati e in preparazione
	int tempoTotale;

	public Simulator(Graph<Food, DefaultWeightedEdge> grafo, int numeroStazioni, Food foodPartenza) {
		this.grafo = grafo;
		this.numeroStazioni = numeroStazioni;
		this.foodPartenza = foodPartenza;
	}

	public void init() {
		tempoTotale = 0;
		cibiPreparati = new HashSet<Food>();
		coda = new PriorityQueue<Event>();

		//Aggiungo il cibo di partenza al set che contiene tutti i cibi che non posso più preparare (già preparato o in preparazione su un altro macchinario)
		cibiPreparati.add(foodPartenza);

		//Inizializzo la coda
		for (int i = 0; i < numeroStazioni && i < listaVicini(foodPartenza).size(); i++) {
			Food arrivo = listaVicini(foodPartenza).get(i);
			coda.add(new Event(arrivo, getPeso(foodPartenza, arrivo)));
			//Aggiungo al set i cibi in preparazione sui vari macchinari
			cibiPreparati.add(arrivo);
		}
	}

	public void run() {
		while (!coda.isEmpty()) {
			Event e = coda.poll();
			Food ciboDaAggiungere = getFoodMaxCalorie(e.getCibo()); //Prendo il cibo più calorico che non ho ancora preparato 
			if (ciboDaAggiungere != null) { //se null ho finito di aggiunge elementi alla coda
				int durataCiboDaAggingere = getPeso(e.getCibo(), ciboDaAggiungere); //durata della preparazione da aggiungere
				Event daAggiungere = new Event(ciboDaAggiungere, durataCiboDaAggingere);
				coda.add(daAggiungere);
				cibiPreparati.add(ciboDaAggiungere); //Aggiungo il nuovo cibo al set 
				tempoTotale = tempoTotale + durataCiboDaAggingere; //Incremento il tempo della simulazione
			}	
		}
	}

	private List<Food> listaVicini(Food partenza) {
		List<Food> verticiVicini = new LinkedList<Food>();
		List<DefaultWeightedEdge> archiVicini = new LinkedList<DefaultWeightedEdge>(grafo.edgesOf(partenza));
		archiVicini.sort(new Comparator<DefaultWeightedEdge>() {

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				Double d1 = grafo.getEdgeWeight(o1);
				Double d2 = grafo.getEdgeWeight(o2);
				return d1.compareTo(d2);
			}
		});
		for (DefaultWeightedEdge e : archiVicini) {
			verticiVicini.add(Graphs.getOppositeVertex(grafo, e, partenza));
		}
		return verticiVicini;
	}

	private int getPeso(Food partenza, Food arrivo) {
		return (int) grafo.getEdgeWeight(grafo.getEdge(partenza, arrivo));
	}


	private Food getFoodMaxCalorie(Food cibo) {
		List<Food> listaVicini = listaVicini(cibo);
		for (Food f : listaVicini) {
			if (!cibiPreparati.contains(f))
				return f;
		}
		return null;
	}

	public int getNumCibi() {
		return cibiPreparati.size();
	}

	public int getTempoTotale() {
		return tempoTotale;
	}

}
