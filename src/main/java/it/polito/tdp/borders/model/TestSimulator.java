package it.polito.tdp.borders.model;

import java.util.ArrayList;

public class TestSimulator {

	public static void main(String[] args) {
		Model m = new Model() ; // prima creo il modello per chiedergli di restituirmi un grafo
		m.creaGrafo(2000);
		m.simula(new ArrayList<Country>(m.getCountries()).get(0)); // trasformo il set in una lista e accedo al primo elemento

	}

}
