package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	NewUfoSightingsDAO db = new NewUfoSightingsDAO();
	SimpleWeightedGraph<State, DefaultWeightedEdge> graph;
	Map<String, State> statesIdMap;
	Simulator simulator;
	
	public Model() {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		statesIdMap = new HashMap<>();
		for (State s : this.db.loadAllStates())
			this.statesIdMap.put(s.getId(), s);
	}
	
	public Map<State, Double> doSimula(int year, String shape, int nGiorni, int alfa) {
		simulator = new Simulator(this.statesIdMap, year, shape, alfa, nGiorni, this.graph);
		return simulator.getMappaDefconStati();
	}
	
	public void creaGrafo(Integer year, String shape) {
		Graphs.addAllVertices(this.graph, this.statesIdMap.values());
		System.out.println("Grafo creato correttamente con: "+this.graph.vertexSet().size()+" vertici\n");
		
		List<Adiacenza> listaAdiacenze = new ArrayList<>(this.db.loadAllAdiacenze(this.statesIdMap, year, shape));
		
		for (Adiacenza a : listaAdiacenze) {
			Graphs.addEdge(this.graph, a.s1, a.s2, a.peso);
		}
		
		System.out.println("Al grafo sono stati aggiunti: "+this.graph.edgeSet().size()+" archi\n");
		
	}
	
	public Map<State, Integer> getSommaPesi(){
		Map<State, Integer> result = new HashMap<>();
		for (State s : this.graph.vertexSet()) {
			int somma = 0;
			for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(s))
				somma += this.graph.getEdgeWeight(e);
			for (DefaultWeightedEdge e : this.graph.incomingEdgesOf(s))
				somma += this.graph.getEdgeWeight(e);
			result.put(s, somma);
		}
		return result;
	}
	
	public List<String> getAllShapes(Integer year){
		return this.db.loadAllShapes(year);
	}
	
}
