package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{

	private Food cibo;
	private int tempo; 
	
	public Event(Food cibo, int tempo) {
		super();
		this.cibo = cibo;
		this.tempo = tempo;
	}
	
	public Food getCibo() {
		return cibo;
	}
	
	public int getTempo() {
		return tempo;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}

	@Override
	public int compareTo(Event o) {
		return this.tempo-o.tempo;
	}
	
	
	
	
}
