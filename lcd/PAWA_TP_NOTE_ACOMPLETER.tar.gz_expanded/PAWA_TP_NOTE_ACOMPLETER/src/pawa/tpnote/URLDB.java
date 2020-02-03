package pawa.tpnote;

import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpSession;

public class URLDB {
	
	/**
	 *  QUESTION 1.1.1
	 */
	public final static String SUFFIX = "GUYOT2"; /* À REMPLACER */
	
	
	
	public final static String TABLE = "URL_" + SUFFIX;
	
	private Connection conn;
	private Random random;
	
	
	public URLDB () throws ClassNotFoundException, SQLException 
	{
		//Chargement du driver postgresql et connexion à la base
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection("jdbc:postgresql://tp-postgres:5432/knguye10_a", 
											"knguye10_a","knguye10_a");
		
		Statement s = conn.createStatement();
		
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE 
				        + " ( url TEXT NOT NULL,"
				        + "   hash INTEGER UNIQUE NOT NULL,"
				        + "   count INTEGER NOT NULL,"
				        + "   pass VARCHAR(8) NOT NULL, PRIMARY KEY(url, hash))");
				
		random = new Random ();
	}
	
	@Override
	protected void finalize()
	{
		try {
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (Exception e) {}
	}
	
	
	/**
	 * QUESTION 1.1.2
	 */
	public static URLDB getFromSession(HttpSession s) throws ClassNotFoundException, SQLException {
		Object att = s.getAttribute("db");
		if(att == null || !(att instanceof URLDB)) {
			att = new URLDB(); 
			s.setAttribute("db", att);
		}
		return (URLDB)att;
	}
	
	/**
	 * QUESTION 1.1.3
	 */
	public URLInfo insertURL(String url) throws SQLException {
		
		/* NE PAS MODIFIER LES 4 lignes ci-dessous */
		int hash = url.hashCode();
		hash = hash * (new Date()).hashCode();
		hash = Math.abs(hash);		
		String pass = String.format("%h", hash * random.nextInt());
		
		String requete = "INSERT INTO " + TABLE + " (url, hash, count, pass) VALUES "
					+ " ('"+url+"', " + hash + ", 0, '" + pass + "' )";
		
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate(requete);
		
		URLInfo info = new URLInfo(url, hash, 0, pass);
		return info ;
	}
	/**
	 * QUESTION 1.2.1
	 */
	
	public URLInfo getURLbyHash(int hash) throws SQLException {
		Statement stmt = conn.createStatement();
		
		String req = "SELECT * FROM " + TABLE + " WHERE hash = " + hash;
		ResultSet res = stmt.executeQuery(req);
		if( !res.next())
			return null;
		
		stmt.executeUpdate("UPDATE " + TABLE 
				+ " SET count = count + 1 WHERE hash=" + hash);
		URLInfo info = new URLInfo(res.getString("url"), 
								   hash, 
								   res.getInt("count"), 
								   res.getString("pass"));
		return info;
	}
	

	
	/**
	 * NE PAS MODIFIER MAIS LIRE POUR LA QUESTION 1.3.1
	 */
	public boolean deleteURLbyHash(int hash, String pass) throws SQLException {
		Statement s = conn.createStatement();
		String query = "DELETE FROM " + TABLE + " WHERE pass='" + pass + "' AND " + "hash=" + hash;
		
		int i = s.executeUpdate(query);
		return i != 0;
	}
	
	/**
	 * QUESTION 1.4.1
	 */
	public Vector<URLInfo> getAll() throws SQLException {
		Vector<URLInfo> v = new Vector<>();
		
		/* COMPLÉTER CI-DESSOUS */
		
		
		
		return v;
		
	}
	
	
	
}
