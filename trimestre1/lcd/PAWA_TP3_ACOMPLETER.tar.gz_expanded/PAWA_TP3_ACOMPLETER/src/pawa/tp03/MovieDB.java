package pawa.tp03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pawa.tp03.Pair;

//Classe représentant le modèle
public class MovieDB {
	
	private Connection conn;
	private int numMovies = -1;
	
	public MovieDB () throws ClassNotFoundException, SQLException 
	{
		//Chargement du driver postgresql et connexion à la base
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection("jdbc:postgresql://tp-postgres:5432/knguye10_a", 
											"knguye10_a","knguye10_a");
		
	}
	
	@Override
	protected void finalize()
	{
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception e) {}
	}
	
	public int getNumMovies() throws SQLException
	{
		
		if (numMovies == -1)
		{
			Statement s = conn.createStatement();
			ResultSet res = s
					.executeQuery("SELECT COUNT(*) AS NUM FROM MOVIE");
			res.next();
			numMovies = res.getInt("NUM");
		}
		return numMovies;
		
	}
	
	//Méthode publique appelée depuis le Contrôleur
	public List<Pair<Integer,String>> getMovieList(int from, int to) throws SQLException
	{
		List<Pair<Integer,String>> v = new ArrayList<>();
		Statement s = conn.createStatement();
		ResultSet res = s.executeQuery("SELECT * FROM MOVIE ORDER BY TITLE"
							 	 	 + " LIMIT " + (to - from)
							 	 	 + " OFFSET " + from);

		while (res.next())
			v.add(new Pair<>(res.getInt("MID"),res.getString("TITLE")));
	
		return v;
	}
	
	public String getTitle(int mid) throws SQLException {

		// À COMPLÉTER
		
		return null; //À REMPLACER
		
	}

	
}
