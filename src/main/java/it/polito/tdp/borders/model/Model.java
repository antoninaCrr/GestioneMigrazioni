package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country, DefaultEdge> graph;
	private Map<Integer, Country> countriesMap;
	
	private Map<Country, Integer> personeStanziali ;

	public Model() {
		this.countriesMap = new HashMap<>();
	}

	public void creaGrafo(int anno) {

		this.graph = new SimpleGraph<>(DefaultEdge.class);

		BordersDAO dao = new BordersDAO();

		// vertici
		dao.getCountriesFromYear(anno, this.countriesMap);
		Graphs.addAllVertices(graph, this.countriesMap.values());

		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno);
		for (Adiacenza c : archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), this.countriesMap.get(c.getState2no()));

		}
	}

	public List<CountryAndNumber> getCountryAndNumbers() {
		List<CountryAndNumber> result = new LinkedList<>();

		for (Country c : this.graph.vertexSet()) {
			result.add(new CountryAndNumber(c, this.graph.degreeOf(c)));
		}

		Collections.sort(result);
		return result;
	}

	public Set<Country> getCountries() {

		if (this.graph != null) {
			return this.graph.vertexSet();
		}
		return null;

	}
	// aggiungiamo questi metodi per mettere in relazione controller e simulatore
	public int simula(Country partenza) {
		Simulatore sim = new Simulatore(this.graph) ; // istanziamo un nuovo simulatore
		sim.init(partenza, 1000);
		sim.run();
		this.personeStanziali = sim.getPersone(); // mi salvo la mappa con gli stanziali in ogni stato prima di ritornare il numero di passi
		return sim.getnPassi() ;
	}
	
	// con questo metodo ritorno al controller una lista già ordinato con le info di cui ha bisogno
	// posso chiamarlo solo dopo che ho chiamato simula
	public List<CountryAndNumber> getPersoneStanziali() {
		List<CountryAndNumber> lista = new ArrayList<CountryAndNumber>() ;
		for(Country c: this.personeStanziali.keySet()) { // per ogni country della mappa, se il numero di stanziali è diverso da 0
			int persone = this.personeStanziali.get(c) ; // aggiungo stato e numero di stanziali a 'lista'
			if(persone!=0) {
				lista.add( new CountryAndNumber(c, persone));
			}
		}
		// devo ordinare per valore decrescente di numero di persone
		Collections.sort(lista) ; 
		return lista ;
	}
}
