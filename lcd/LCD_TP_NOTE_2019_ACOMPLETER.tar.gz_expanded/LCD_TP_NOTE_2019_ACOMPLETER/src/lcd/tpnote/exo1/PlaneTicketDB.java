package lcd.tpnote.exo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PlaneTicketDB {
	
	private String host;
    private String user;
    private String base;
    private String pw;
    private String TABLE_NAME;
	
	public PlaneTicketDB(String host, String user, String base, String pw) throws ClassNotFoundException {
	
		Class.forName("org.postgresql.Driver");
		this.base = base;
		this.pw = pw;
		this.host = host;
		this.user = user;
		this.TABLE_NAME = "TICKETS_" +  System.getProperty("user.name").replace(".", "_").replace("-", "_");
	}
	
	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:postgresql://" + host + ":5432/" + base, user, pw);	
	}

	private void createTable ()  throws SQLException {
		
		Connection cnx = connect();
		Statement stmt = cnx.createStatement();
		System.err.println("Création de " + TABLE_NAME);
		stmt.executeUpdate("DROP TABLE IF EXISTS " + TABLE_NAME + " CASCADE");
		stmt.executeUpdate("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, "
				+ "CITY1 VARCHAR(150), CITY2 VARCHAR(150),"
				+ "PRICE INTEGER, QTITY INTEGER)");
		cnx.close();
	}
	
	/***
	 * À COMPLÉTER : cette méthode lit les lignes du fichier et les insère dans la table
	 * en créant un identifiant unique.
	 * 
	 * @param input BufferedReader associé au fichié contenant les lignes
	 * @throws IOException 
	 */
	private void createFromReader (BufferedReader input) throws SQLException, IOException {
		Connection cnx = connect();
		Statement stmt = cnx.createStatement(); 
		int cpt = 0;
		
		while(true) {	
			String line = input.readLine();
			if(line == null)
				break;
			// city1, city2, ???, ???, quantity
			String[] p = line.split(";");
			stmt.addBatch("INSERT INTO " + TABLE_NAME 
				+ " (id, city1, city2, price, qtity) VaLuEs ("
				+ (cpt) + ",'" + p[0].replace("'", "''") + "','" 
				+ p[1].replace("'", "''") + "'," + p[2] + "," + p[3] + ")");
			cpt++;
		}
		int[] t = stmt.executeBatch();
		System.out.println("résultat de la première requête : " + t[0]);

		stmt.close();
		cnx.close();
	}
	
	/***
	 * À COMPLÉTER : Cette méthode renvoie tous les vols au départ de city, classé par ordre de prix croissant.
	 * @param city
	 * @return
	 * @throws SQLException
	 */
	private List<PlaneTicket> allTripsFrom(String city) throws SQLException {
		Connection cnx = connect();
		List<PlaneTicket> res = new Vector<>();
		Statement stmt = cnx.createStatement(); 	
		String requete = "SELECT * FROM " + TABLE_NAME 
				+ " WHERE city1 = '" + city + "'";
		ResultSet rset = stmt.executeQuery(requete);
		while(rset.next()) {
			res.add(new PlaneTicket(
							rset.getInt("id"), 
							rset.getString("city1"), 
							rset.getString("city2"), 
							rset.getInt("price"), 
							rset.getInt("qtity")));

		}
		stmt.close();
		cnx.close();
		return res;
	}
	
	/***
	 * À COMPLÉTER : cette méthode calcule pour chaque tranche de prix de 100€ le nombre de billets dans cette tranche, triée par tranche.
	 * @return
	 * @throws SQLException
	 */
	private List<String> tripCountByPrice() throws SQLException {
		Connection cnx = connect();
		List<String> res = new Vector<>();

		Statement stmt = cnx.createStatement();
		
		String requete = "SELECT (PRICE/100) * 100 AS P, COUNT(*) as N " 
					+ "FROM " + TABLE_NAME 
					+ " GROUP BY P ORDER BY P";
		System.out.println(requete);
		ResultSet rset = stmt.executeQuery(requete);
		int i = 0;
		while(rset.next()) {
			// TODO
		}
		
		
		stmt.close();
		cnx.close();
		return res;

	}
	
		
	
	public static void main(String[] args) {
		try {
			/** Ne pas modifier sur les machines du PUIO
			 * 
			 * Pour les étudiants utilisant knd, utiliser :  localhost, user, user, user_a où user est votre nom de login.
			 *  */
		
			PlaneTicketDB db = new PlaneTicketDB("tp-postgres", "knguye10_a", "knguye10_a", "knguye10_a");
			//PlaneTicketDB db = new PlaneTicketDB("localhost", "kim", "kim", "kim_a");

			/* Création des tables */
			db.createTable();
			
			/* Récupération du chemin du fichier CSV */
			String file = PlaneTicketDB.class.getClassLoader().getResource("resources/plane_tickets.txt").getPath();
			BufferedReader buff = new BufferedReader(new FileReader (file));
			
			/* Appel à la méthode de chargement */
			db.createFromReader(buff);
			
			/* Tous les voyages depuis Paris */
			for (PlaneTicket t : db.allTripsFrom("Paris")) {
				System.out.println(t);
			}
			
			/* Le nombre billets par tranche de prix de 100 euros */
			for (String s : db.tripCountByPrice()) {
				System.out.println(s);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
