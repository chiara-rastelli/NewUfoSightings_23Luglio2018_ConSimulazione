package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		NUOVO_AVVISTAMENTO,
		CESSATA_ALLERTA
	}
	
	private EventType type;
	private State s;
	private LocalDateTime giorno;
	private Double variazioneDefcon;
	
	public int compareTo(Event o) {
		return this.giorno.compareTo(o.giorno);
	}

	public EventType getType() {
		return type;
	}

	public State getS() {
		return s;
	}

	public LocalDateTime getGiorno() {
		return giorno;
	}

	public Event(EventType type, State s, LocalDateTime giorno, Double variazioneDefcon) {
		super();
		this.type = type;
		this.s = s;
		this.giorno = giorno;
		this.variazioneDefcon = variazioneDefcon;
	}

	@Override
	public String toString() {
		return "Event [s=" + s +type+ ", giorno=" + giorno + "]";
	}

	public Double getVariazioneDefcon() {
		return variazioneDefcon;
	}
}
