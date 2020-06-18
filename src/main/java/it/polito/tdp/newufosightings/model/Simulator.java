package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;
import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {
	
	PriorityQueue<Event> queue = new PriorityQueue<>();
	
	private SimpleWeightedGraph<State, DefaultWeightedEdge> graph;
	
	private int alfa;    //parametro in input
	private int nGiorni; //giorni di simulazione
	
	private NewUfoSightingsDAO db;
	List<Sighting> listaAvvistamenti;
	Map<State, Double> mappaDefconStati;
	
	public Simulator(Map<String, State> stateIdMap, int year, String shape, int alfa, int nGiorni, SimpleWeightedGraph<State, DefaultWeightedEdge> graph) {
		this.alfa = alfa;
		this.nGiorni = nGiorni;
		this.graph = graph;
		this.db = new NewUfoSightingsDAO();
		this.mappaDefconStati = new HashMap<>();
		this.listaAvvistamenti = new ArrayList<Sighting>(this.db.loadAllSightingsSimulazione(year, shape));
		
		for (Sighting s : this.listaAvvistamenti) {
			Event eTemp = new Event(EventType.NUOVO_AVVISTAMENTO, stateIdMap.get(s.getState().toUpperCase()), s.getDatetime(), 0.0);
			this.queue.add(eTemp);
		}
		
		for (State s : this.graph.vertexSet())
			this.mappaDefconStati.put(s, 5.0);       //all'inizio tutti gli stati hanno livello di allerta minimo pari a 5
		
		this.run();
		
	}
	
	public void run() {

		while (!queue.isEmpty()) {
			Event e = queue.poll();
		//	System.out.println(e);
			processEvent(e);
		}

	}

	private void processEvent(Event e) {
		
		switch (e.getType()) {
		
			case NUOVO_AVVISTAMENTO:
				Double defconIniziale = this.mappaDefconStati.get(e.getS());
				if (defconIniziale >= 2) {
					this.mappaDefconStati.put(e.getS(), (defconIniziale - 1));
					System.out.println(e.toString()+" defcon parziale: "+(defconIniziale - 1));
				}
				else {
					this.mappaDefconStati.put(e.getS(), 1.0);
					System.out.println(e.toString()+" defcon parziale: "+1.0);
				}
				Event eTemp = new Event(EventType.CESSATA_ALLERTA, e.getS(), e.getGiorno().plusDays(nGiorni), 1.0);
				this.queue.add(eTemp);
				this.aggiornaDefconAdiacenti(e);
			
			break;
			
			case CESSATA_ALLERTA:
				
				Double defcon = this.mappaDefconStati.get(e.getS());
				Double defconFinale = defcon + e.getVariazioneDefcon();
				
				if (defconFinale > 5.0) {
					this.mappaDefconStati.put(e.getS(), 5.0);
					System.out.println(e.toString()+" defcon parziale: "+5.0);
				}
				else {
					this.mappaDefconStati.put(e.getS(), defconFinale);
					System.out.println(e.toString()+" defcon parziale: "+defconFinale);
				}
				
			break;
		
		}		
		
	}

	private void aggiornaDefconAdiacenti(Event e) {
		
		for (State s : Graphs.neighborListOf(this.graph, e.getS())) {
			if (Math.random() < this.alfa/100) {
				Double defconIniziale = this.mappaDefconStati.get(s);
				if (defconIniziale >= 1.5)
					this.mappaDefconStati.put(s, defconIniziale - 0.5);
				else
					this.mappaDefconStati.put(s, 1.0);
				Event eTemp = new Event(EventType.CESSATA_ALLERTA, s, e.getGiorno().plusDays(nGiorni), 0.5);
				this.queue.add(eTemp);
			}
		}
	}
	
	public Map<State, Double> getMappaDefconStati() {
		return mappaDefconStati;
	}

	public void setMappaDefconStati(Map<State, Double> mappaDefconStati) {
		this.mappaDefconStati = mappaDefconStati;
	}

	public int getAlfa() {
		return alfa;
	}

	public void setAlfa(int alfa) {
		this.alfa = alfa;
	}

	public int getnGiorni() {
		return nGiorni;
	}

	public void setnGiorni(int nGiorni) {
		this.nGiorni = nGiorni;
	}
}
