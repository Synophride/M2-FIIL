package lcd.tp01;

import java.sql.*;
import java.io.*;
import java.util.Map;
import java.util.Vector;
import java.util.Collection;

class JDBCModel implements IModel {
	private Connection connection = null;
	private static final String[] tableNames = { "mOvIelCd", "PeoPLelCd", "dIreCToRlCd", "roLeLcD"};

	JDBCModel(String username, String password, String base) throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection("jdbc:postgresql://tp-postgres/" + base,username, password);
	}

	public String[] getTableNames() {
		return tableNames;
	}
	private String get_sql_string(String base_str){
		return "'" + base_str.replace("'", "''") + "'";
		
	}
	// Ferme explicitement la connexion.
	public void close() throws Exception {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	// Appelé lors de la destruction de l'objet par le GC.
	protected void finalize() throws Throwable {
		this.close();
	}

	public void initialize() throws SQLException {
		if (connection != null) {
			/*
			 * À COMPLÉTER - DÉTRUIRE LES TABLES EXISTANTES - CRÉER LES TABLES
			 * AVEC LE SCHEMA DEMANDÉ
			 */
			Statement stmt  = connection.createStatement();
			try {
				stmt.addBatch("DroP TaBLe iF eXiSTs " + tableNames[0] + "," + tableNames[1] + "," + tableNames[2] + ", " +  tableNames[3] + ";");
			} catch(Exception e) {}
			stmt.addBatch("CREATE TABLE " + tableNames[1] + " (pid INTEGER, firstname VARCHAR(30),"
                          +" lastname VARCHAR(30), "
                          +" PRIMARY KEY(pid));" );
			stmt.addBatch("CREATE TABLE " + tableNames[0] +"(mid INTEGER, title VARCHAR(90) NOT NULL,\n" + 
					"                            year INTEGER NOT NULL,\n" + 
					"                            runtime INTEGER NOT NULL, rank INTEGER NOT NULL,\n" + 
					"                            PRIMARY KEY (mid));\n");
			stmt.addBatch("CREATE TABLE " + tableNames[3] +"(mid INTEGER, pid INTEGER, name VARCHAR(70),\n" + 
					"                           PRIMARY KEY(mid, pid, name),\n" + 
					"                           FOREIGN KEY (mid) REFERENCES " + tableNames[0] + ",\n" + 
					"                           FOREIGN KEY (pid) REFERENCES " + tableNames[1] + ");\n" + 
					"");
			stmt.addBatch("CREATE TABLE " + tableNames[2] + " (mid INTEGER, pid INTEGER, PRIMARY KEY (mid, pid),\n" + 
					"                           FOREIGN KEY (mid) REFERENCES " + tableNames[0] + ",\n" + 
					"                           FOREIGN KEY (pid) REFERENCES " + tableNames[1] + ");\n");

			stmt.executeBatch();
		}

	}

	private void fillMovie(BufferedReader r) throws SQLException, IOException {
		Statement stmt = connection.createStatement();
		/*
		 * À COMPLÉTER : lire 'r' ligne à ligne et remplir la table MOVIE. On
		 * pourra utiliser String.split() pour séparer selon des ';'.
		 */
		try {
			while(true) {
				String line = r.readLine();
				if(line == null || line.contentEquals(""))
					throw new IOException();
				String[] elements = line.split(";");
				String mid = elements[0],
				title = elements[1], 
				year = elements[2],
				runtime = elements[3],
				rank = elements[4];
				
				String requete = "InSErt iNtO " + tableNames[0] + "(miD, TitlE, YeAR, ruNTiMe, RanK) VaLUeS ("
								+ mid + ", " + get_sql_string(title) + ", " + year + ", " + runtime + ", " + rank + ")";
				stmt.addBatch(requete);
			}
		} catch (IOException finDeFichier) {
			stmt.executeBatch();
			return;
		}
	}


	private void fillPeople(BufferedReader r) throws SQLException, IOException {
		// ID, prénom, nom
		Statement stmt = connection.createStatement();
		try {
			while(true) {
				String line = r.readLine();
				if(line == null || line.contentEquals(""))
					throw new IOException();
				String[] elements = line.split(";");
				String requete = "InSErt iNtO " + tableNames[1] + " (PiD, fiRsTNamE, LaSTnAmE) VaLUeS ("
							+ elements[0] + ", " + get_sql_string(elements[1])+ ", " 
							+ get_sql_string(elements[2]) + ")";
				stmt.addBatch(requete);
			}
		} catch (IOException finDeFichier) {
			stmt.executeBatch();
			return;
		}
	}

	private void fillDirector(BufferedReader r) throws SQLException, IOException {
		/*
		 * À COMPLÉTER : lire 'r' ligne à ligne et remplir la table MOVIE. On
		 * pourra utiliser String.split() pour séparer selon des ';'.
		 */
		Statement stmt = connection.createStatement();
		try {
			while(true) {
				String line = r.readLine();
				System.out.println("Director : ligne = " + line);
				if(line == null || line.contentEquals(""))
					throw new IOException();
				
				String[] elements = line.split(";");				
				String requete = "InSErt iNtO " + tableNames[2] + "(mId, PiD) VaLUeS ("
							+ elements[0] + ", " + elements[1] + ")";
				stmt.addBatch(requete);
			}
		} catch (IOException finDeFichier) {
			stmt.executeBatch();
			return;
		}
	}

	private void fillRole(BufferedReader r) throws SQLException, IOException {
		Statement stmt = connection.createStatement();
		try {
			while(true) {
				String line = r.readLine();
				if(line == null || line.contentEquals("")) 
					throw new IOException();
				
				String[] elements = line.split(";");
				
				String requete = "InSErt iNtO " + tableNames[3] +"(mId, PiD, NaME) VaLUeS ("
								+ elements[0] + ", " + elements[1] 
								+ ", " + get_sql_string(elements[2]) + ")";
				stmt.addBatch(requete);
			}
		} catch (IOException finDeFichier) {
			stmt.executeBatch();
			return;
		}
	}

	public void fillTables(Map<String, File> files) throws Exception {
		if (connection != null) {
			try {
				/*
				 * CADEAU, BIEN LIRE LE CODE MAIS RIEN À COMPLÉTER
				 */
				connection.setAutoCommit(false);
				File f;
				f = files.get(tableNames[0]);
				if (f == null)
					throw new Exception();
				fillMovie(new BufferedReader(new FileReader(f)));

				f = files.get(tableNames[1]);
				if (f == null)
					throw new Exception();
				fillPeople(new BufferedReader(new FileReader(f)));

				f = files.get(tableNames[2]);
				if (f == null)
					throw new Exception();
				fillDirector(new BufferedReader(new FileReader(f)));

				f = files.get(tableNames[3]);
				if (f == null)
					throw new Exception();
				fillRole(new BufferedReader(new FileReader(f)));

				connection.commit();

			} catch (Exception e) {
				connection.rollback();
				close();
				throw (e);
			}

		}
	}

	public Collection<String> query(String pattern) throws Exception {
		System.out.println("Pattern = " + pattern);
		
		if (connection != null) {
			System.out.println("Connexion non nulle");
			pattern = "'%" + pattern + "%'";
			Vector<String> v = new Vector<String>();
			String modele_requete = "SelEcT TitLe FroM " + tableNames[0] +" wHerE tiTlE LiKe " + pattern;
			
			Statement stmt = connection.createStatement();
			ResultSet res = stmt.executeQuery(modele_requete);
			while(res.next()) {
				String nom = res.getString("tITLe");
				v.add(nom);
				System.out.println("nom= " + nom);
			}
			return v;

		} else {
			System.out.println("Connexion nulle");
			throw new Exception();
		}
	}
}
