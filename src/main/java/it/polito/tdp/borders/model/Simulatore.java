package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore { // il simulatore diventa una classe ad uso esclusivo del modello

	// Coda degli eventi
	private PriorityQueue<Event> queue;

	// Parametri di simulazione --- quali sono le info iniziali necessarie per avviare la simulazione?
	private int nInizialeMigranti;
	private Country nazioneIniziale;

	// Output della simulazione
	private int nPassi; // var T del testo
	private Map<Country, Integer> persone; // per ogni nazione, quanti migranti sono stanziali in quella nazione
	// oppure: List<CountryAndNumber> personeStanziali ; --> tutte le volte che devo incrementare o decrem. devo prima fare una ricerca. Ciò risulta poco efficiente

	// Stato del mondo simulato
	private Graph<Country, DefaultEdge> grafo; // non è un param che il chiamante può modificare
	// Map persone Country -> Integer --- in un certo istante del tempo a caratterizzare lo stato del mondo è quante persone ci sono in ogni stato

	// Costruttore
	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}

	// metodo per inizializzare la simulazione
	public void init(Country partenza, int migranti) {
		this.nazioneIniziale = partenza;
		this.nInizialeMigranti = migranti;
		
		// qui devo inizializzare anche la struttura dati che userò per tener traccia del numero di persone stanziali in ogni stato
		this.persone = new HashMap<Country, Integer>(); // ad ogni simulazione, la mappia viene ricreata e riempita nuovamente
		for (Country c : this.grafo.vertexSet()) {
			this.persone.put(c, 0); // all'inizio della simulazione solo lo stato di partenza avrà degli stanziali
		}
		
		// creo la coda vuota
		this.queue = new PriorityQueue<>(); 

		this.queue.add(new Event(1, this.nazioneIniziale, this.nInizialeMigranti)); // inietto il primo evento dentro la coda degli eventi
	}

	// grazie a lui faccio andare avanti la simulazione
	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll(); // estraggo un evento dalla coda
//			System.out.println(e);
			processEvent(e); // elaboro l'evento estratto
		}
	}

	private void processEvent(Event e) { // ho un solo tipo di evento
		int stanziali = e.getPersone() / 2; // questa divisione funziona come divisione intera (approssima per difetto)
		int migranti = e.getPersone() - stanziali;
		int confinanti = this.grafo.degreeOf(e.getNazione()); // voglio sapere quanti stati confinanti ho chiedendolo al grafo (spero che il grado sia diverso da 0)
		int gruppiMigranti = migranti / confinanti; // i gruppi di migranti sono composti dal totale di migranti da suddividere / num di stati confinanti
		stanziali += migranti % confinanti; // gestisco eventuale resto dell'operazione precedente

		// qui sto aggiornando lo stato del mondo
		this.persone.put(e.getNazione(), this.persone.get(e.getNazione()) + stanziali); // valore che c'era + stanziali attuali
		this.nPassi = e.getTime();

		// predispongo eventi futuri
		if (gruppiMigranti != 0) {
			for (Country vicino : Graphs.neighborListOf(this.grafo, e.getNazione())) { // per ogni elemento della lista restituita dal metodo della classe Graphs creo un nuovo evento
				this.queue.add(new Event(e.getTime() + 1, vicino, gruppiMigranti)); // tempo attuale + 1
				                                                                    // nazione verso cui mi sposto --> vicino
				                                                                    // numero di persone che si spostano in vicino
			}
		}
	}

	// metodi per restituire al chiamante le info a cui è interessato
	public int getnPassi() {
		return nPassi;
	}

	public Map<Country, Integer> getPersone() {
		return persone;
	}

}
