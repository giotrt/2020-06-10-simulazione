package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if(!idMap.containsKey(res.getInt("id"))) {
					Actor a = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
							res.getString("gender"));
					
					idMap.put(a.getId(), a);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getAllGeneri() {
		String sql = "SELECT distinct m.genre AS g "
				+ "FROM movies_genres m "
				+ "ORDER BY m.genre";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("g"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Actor> getAllVertici(String genere, Map<Integer, Actor> idMap) {
		String sql = "SELECT DISTINCT r.actor_id AS id "
				+ "FROM roles r, movies m, movies_genres mg "
				+ "WHERE r.movie_id = m.id AND m.id = mg.movie_id AND mg.genre = ? "
				+ "ORDER BY id";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Actor a = idMap.get(res.getInt("id"));
				
				result.add(a);
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Arco> getAllArchi(String genere, Map<Integer, Actor> idMap) {
		String sql = "SELECT r1.actor_id AS id1, r2.actor_id AS id2, COUNT(*) AS peso "
				+ "FROM roles r1, roles r2, movies m, movies_genres mg "
				+ "WHERE r1.movie_id  = m.id AND r2.movie_id = m.id "
				+ "	 	AND m.id = mg.movie_id AND mg.genre = ? AND "
				+ "	 	r1.actor_id > r2.actor_id "
				+ "GROUP BY id1, id2";
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Actor a1 = idMap.get(res.getInt("id1"));
				Actor a2 = idMap.get(res.getInt("id2"));
				int peso = res.getInt("peso");
				result.add(new Arco(a1,a2,peso));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	}
	
	
	
	
	
	
}
