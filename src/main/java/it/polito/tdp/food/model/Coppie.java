package it.polito.tdp.food.model;

public class Coppie {
	
	private Food f1;
	private Food f2;
	private double peso;
	
	public Coppie(Food f1, Food f2, double peso) {
		this.f1 = f1;
		this.f2 = f2;
		this.peso = peso;
	}
	
	public Food getF1() {
		return f1;
	}
	
	public Food getF2() {
		return f2;
	}
	
	public double getPeso() {
		return peso;
	}
	
	

}
