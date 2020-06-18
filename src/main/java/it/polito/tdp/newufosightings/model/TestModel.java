package it.polito.tdp.newufosightings.model;

import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		m.creaGrafo(1998, "circle");
		
		Map<State, Double> mappaSimulazione = m.doSimula(2010, "light", 3, 90);
		
		for (State s : mappaSimulazione.keySet())
			System.out.println("Stato: "+s+", livello di allerta: "+mappaSimulazione.get(s)+"\n");
	}

}
