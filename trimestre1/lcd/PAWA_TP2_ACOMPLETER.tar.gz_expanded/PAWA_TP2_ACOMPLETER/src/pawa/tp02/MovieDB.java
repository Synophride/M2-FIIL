package pawa.tp02;


// select title from movie order offset i limit j
// select 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.LinkedList;

public class MovieDB {
	private Connection conn;

	public MovieDB () throws ClassNotFoundException, SQLException
	{
		// si vous utilisez votre propre base
		// remplacez knguye10 par votre login court

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

	/* TODO: Question 3 */
	public int getNumMovies() throws SQLException
	{
		if(conn == null)
			return -1;
		String requete = "SELECT count(*) as c FROM movies";
		Statement s = conn.createStatement();
		ResultSet res = s.executeQuery(requete);
		
		if(!res.next()) return -1; 
		return res.getInt("c");
	}

	/* TODO: Question 4 */
	public List<Pair<Integer,String>> getMovieList(int from, int to) throws SQLException
	{
		List<Pair<Integer, String>> movieList = new LinkedList<>();
		Statement stmt = conn.createStatement();
		String requete = "SELECT mid, title FROM movie ORDER BY title OFFSET " 
					+ from +" LIMIT " + to;
		
		ResultSet res = stmt.executeQuery(requete);
		
		while(res.next()) {
			Integer id = res.getInt("mid");
			String title = res.getString("title");
			
			movieList.add(new Pair<Integer, String>(id, title));
		}
		
		return movieList;
	}
}
