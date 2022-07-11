package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	private List<String> generi;
	
	private Map<Integer, Actor> idMap;
	
	private ImdbDAO dao;
	
	private List<Actor> vertici;
	
	private List<Arco> archi;
	
	public Model() {
		
		this.dao = new ImdbDAO();
		
		this.idMap = new HashMap<Integer, Actor>();
		
		this.dao.listAllActors(idMap);
				
		this.generi = dao.getAllGeneri();
	}

	public String creaGrafo(String genere) {
		
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getAllVertici(genere, idMap);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		this.archi = dao.getAllArchi(genere, idMap);
		
		for(Arco a : this.archi)
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), (double)a.getPeso());

		String result = "GRAFO CREATO!\n" + "#VERTICI: " + this.grafo.vertexSet().size() + "\n" + "#ARCHI: " + this.grafo.edgeSet().size()+"\n";
		return result;
	}
	
	public List<Actor> getAttoriSimili(Actor a) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(this.grafo);
		List<Actor> connessi = new ArrayList<>(ci.connectedSetOf(a));
		connessi.remove(a);
		Collections.sort(connessi);
		return connessi;
	}
	
	public void Simula(int n) {
		
		Simulatore s = new Simulatore(this.grafo);
		s.init(n);
		s.run();
		List<Actor> intervistati = s.getAttoriIntervistati();
		int pause = s.getnPause();
		for(Actor a : intervistati) {
			System.out.println(a.toString()+"\n");
		}
		System.out.println("NUMERO DI PAUSE: "+pause);

	}
	public List<String> getGeneri() {
		return generi;
	}

	public List<Actor> getVertici() {
		return vertici;
	}
	
	public boolean isCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}

	



}
