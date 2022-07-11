package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
	
	private PriorityQueue<Event> coda;
	
//	parametri
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private List<Actor> allActors;
	
	private List<Actor> attoriDaIntevistare;
	private List<Actor> attoriIntervistati;
	
	private int nPause;
	
	private int nGiorni;
	
	public Simulatore(Graph<Actor, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public void init(int n) {
		
		this.coda = new PriorityQueue<Event>();
		
		this.nGiorni = n;
		
		this.nPause = 0;
		
		this.allActors = new ArrayList<>(this.grafo.vertexSet());
		
		this.attoriDaIntevistare = new ArrayList<>(this.allActors);
		this.attoriIntervistati = new ArrayList<Actor>();
		
		int indice = (int)(Math.random()*this.attoriDaIntevistare.size());
		Actor iniziale = this.attoriDaIntevistare.get(indice);
		this.attoriIntervistati.add(iniziale);
		this.attoriDaIntevistare.remove(iniziale);
		
		Event e = new Event(0, iniziale);
		this.coda.add(e);
	}
	
	public void run() {
		while(!(this.coda.isEmpty()) || !(this.attoriIntervistati.size() == this.nGiorni)) {
			Event e = this.coda.poll();
			processEvent(e);
		}
	}

	
	private void processEvent(Event e) {
		int time = e.getTime();
		Actor a = e.getA();
		
		if(this.attoriIntervistati.size()>=2) {
			Actor ultimo = this.attoriIntervistati.get(this.attoriIntervistati.size()-1);
			Actor penultimo = this.attoriIntervistati.get(this.attoriIntervistati.size()-2);
			if(ultimo != null && penultimo != null) {
				if(ultimo.getGender().compareTo(penultimo.getGender())==0) {
					if(Math.random()<0.9) {
						this.nPause++;
						this.coda.add(new Event(time+1, null));
					}
				}
			}
			
		}
		
		if(a == null) {
			int indice = (int)Math.random()*this.attoriDaIntevistare.size()+1;
			Actor nuovoAttore = this.attoriDaIntevistare.get(indice);
			this.coda.add(new Event(time+1, nuovoAttore));
			this.attoriDaIntevistare.remove(nuovoAttore);
		}
		
		
		
		if(Math.random()<0.6) {
			int indice = (int)Math.random()*this.attoriDaIntevistare.size()+1;
			Actor nuovoAttore = this.attoriDaIntevistare.get(indice);
			this.coda.add(new Event(time+1, nuovoAttore));
			this.attoriDaIntevistare.remove(nuovoAttore);
			this.attoriIntervistati.add(a);
		}else {
			Actor nuovoAttore = cercaVicino(a);
			if(nuovoAttore == null) {
				int indice = (int)Math.random()*this.attoriDaIntevistare.size();
				nuovoAttore = this.attoriDaIntevistare.get(indice);
			}
			this.coda.add(new Event(time+1, nuovoAttore));
			this.attoriDaIntevistare.remove(nuovoAttore);
			this.attoriIntervistati.add(a);
		}
	}

	private Actor cercaVicino(Actor a) {
		List<Actor> candidati = new ArrayList<Actor>();
		List<Actor> attoriVicini = new ArrayList<Actor>();
		attoriVicini = Graphs.neighborListOf(this.grafo, a);
		if(attoriVicini == null)
			return null;
		int max = 0;
		for(Actor v : attoriVicini) {
			if(this.grafo.degreeOf(v)>max) {
				max = this.grafo.degreeOf(v);
				candidati.clear();
				candidati.add(v);
			}else if(this.grafo.degreeOf(v)==max) {
				candidati.add(v);
			}
		}
		
		if(candidati.size() == 1)
			return candidati.get(0);
		else {
			int indice = (int)Math.random()*this.attoriDaIntevistare.size()+1;
			Actor nuovoAttore = this.attoriDaIntevistare.get(indice);
			return nuovoAttore;
		}
			
	}

	public int getnPause() {
		return nPause;
	}

	public List<Actor> getAttoriIntervistati() {
		return attoriIntervistati;
	}
	
	

}
