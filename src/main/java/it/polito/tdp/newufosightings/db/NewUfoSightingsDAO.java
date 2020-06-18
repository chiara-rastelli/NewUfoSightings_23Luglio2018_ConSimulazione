package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Adiacenza;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<String> loadAllShapes(Integer year) {
		String sql = "SELECT distinct shape FROM sighting s WHERE year(s.datetime) = ? ORDER BY shape asc";
		
		List<String> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, year);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(res.getString("shape"));
			//	System.out.println(res.getString("shape"));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}
	
	public List<Adiacenza> loadAllAdiacenze(Map<String, State> statesIdMap, Integer year, String shape) {
		
		String sql = 	"	SELECT s1.id as id1, s2.id as id2, COUNT(*) AS peso " + 
						"	FROM state s1, state s2, neighbor n, sighting si1, sighting si2 " + 
						"	WHERE s1.id < s2.id " + 
						"	AND n.state1 = s1.id " + 
						"	AND n.state2 = s2.id " + 
						"	AND si1.state = s1.id " + 
						"	AND si2.state = s2.id " + 
						"	AND si1.shape = si2.shape " + 
						"	AND year(si1.datetime) = ? " + 
						"	AND YEAR(si2.datetime) = ? " + 
						"	AND si1.shape = ? " + 
						"	GROUP BY s1.id, s2.id";
		
		List<Adiacenza> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, year);
			st.setInt(2, year);
			st.setString(3, shape);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State s1 = statesIdMap.get(rs.getString("id1"));
				State s2 = statesIdMap.get(rs.getString("id2"));
				int peso = rs.getInt("peso");
				Adiacenza aTemp = new Adiacenza(s1, s2, peso);
				result.add(aTemp);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}
	
	public List<Sighting> loadAllSightingsSimulazione(int year, String shape) {
		String sql = 	"SELECT * " + 
						"FROM sighting s " + 
						"WHERE YEAR(s.datetime) = ? " + 
						"AND s.shape = ? " + 
						"ORDER BY s.datetime asc";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, year);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}

