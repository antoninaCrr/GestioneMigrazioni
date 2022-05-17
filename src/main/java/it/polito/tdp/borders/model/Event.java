package it.polito.tdp.borders.model;

public class Event implements Comparable<Event>{
	
	private int time ; // il testo ci parla solo di T di simulazione, non di anni o date
	private Country nazione ;
	private int persone ;
		
	public Event(int time, Country nazione, int persone) {
		super();
		this.time = time;
		this.nazione = nazione;
		this.persone = persone;
	}

	public int getTime() {
		return time;
	}

	public Country getNazione() {
		return nazione;
	}

	public int getPersone() {
		return persone;
	}

	// non abbiamo bisogno di set, in quanto l'oggetto, una volta creato, è immutabile

	@Override
	public int compareTo(Event other) { // l'evento deve essere ordinabile secondo tempo
		return this.time-other.time;
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", nazione=" + nazione + ", persone=" + persone + "]";
	}
	
	

}
