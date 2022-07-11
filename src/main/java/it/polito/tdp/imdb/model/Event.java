package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event>{
	
	private int time;
	private Actor a;
	
	public Event(int time, Actor a) {
		super();
		this.time = time;
		this.a = a;
	}

	public int getTime() {
		return time;
	}

	public Actor getA() {
		return a;
	}

	@Override
	public int compareTo(Event o) {
		return this.time-o.time;
	}
	
	
	
	
	
	

}
