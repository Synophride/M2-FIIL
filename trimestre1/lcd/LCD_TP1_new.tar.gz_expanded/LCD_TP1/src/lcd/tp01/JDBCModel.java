package lcd.tp01;

import java.sql.*;
import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Collection;

class JDBCModel implements IModel {

	private Connection connection = null;
	private static final String[] tableNames = { "MOVIE", "PEOPLE", "DIRECTOR", "ROLE" };
	private static final String[] colNames = { "TITLE", "FIRSTNAME", "LASTNAME", "ROLE" };

	JDBCModel(String username, String password, String base) throws SQLException, ClassNotFoundException {
		// Charge dynamiquement le driver postgresql
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + base, username, password);

	}

	public String[] getTableNames() {
		return tableNames;
	}

	public String[] getColNames() {
		return colNames;
	}

	// Ferme explicitement la connexion.
	public void close() throws SQLException {
		if (connection != null) {
			// Statement stmt = connection.createStatement();
			// stmt.executeUpdate("DROP TABLE IF EXISTS DIRECTOR, MOVIE, PEOPLE,
			// ROLE CASCADE");
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
			Statement stmt = connection.createStatement();
			stmt.addBatch("DROP TABLE IF EXISTS DIRECTOR, MOVIE, PEOPLE, ROLE CASCADE");

			stmt.addBatch("CREATE TABLE PEOPLE (pid INTEGER, firstname VARCHAR(30),"
					+ " lastname VARCHAR(30), PRIMARY KEY(pid))");

			stmt.addBatch("CREATE TABLE MOVIE (mid INTEGER, title VARCHAR(90) NOT NULL,"
					+ " year INTEGER NOT NULL, runtime INTEGER NOT NULL,"
					+ " rank INTEGER NOT NULL, PRIMARY KEY (mid))");

			stmt.addBatch("CREATE TABLE ROLE (mid INTEGER, pid INTEGER, name VARCHAR(70),"
					+ " PRIMARY KEY(mid, pid, name), FOREIGN KEY (mid) REFERENCES MOVIE,"
					+ " FOREIGN KEY (pid) REFERENCES PEOPLE)");

			stmt.addBatch("CREATE TABLE DIRECTOR (mid INTEGER, pid INTEGER,"
					+ " PRIMARY KEY (mid, pid), FOREIGN KEY (mid) REFERENCES MOVIE,"
					+ " FOREIGN KEY (pid) REFERENCES PEOPLE)");
			stmt.executeBatch();
		}

	}

	private void fillMovie(BufferedReader r) throws SQLException, IOException {
		String values;
		Statement stmt = connection.createStatement();
		int n = 0;
		String[] v;
		while ((values = r.readLine()) != null) {
			v = values.split(";", 5);
			stmt.addBatch("INSERT INTO MOVIE VALUES (" + v[0] + ", '" + v[1].replace("'", "''") + "', " + v[2] + ", "
					+ v[3] + "," + v[4] + ")");
		}
		;

		for (int i : stmt.executeBatch())
			n += i;

		System.out.println("Inserted " + n + " values in Movie");

	}

	private void fillPeople(BufferedReader r) throws SQLException, IOException {
		String values;
		Statement stmt = connection.createStatement();
		int n = 0;
		String[] v;
		while ((values = r.readLine()) != null) {
			v = values.split(";", 3);
			stmt.addBatch("INSERT INTO PEOPLE VALUES (" + v[0] + ", '" + v[1].replace("'", "''") + "', '"
					+ v[2].replace("'", "''") + "')");
		}
		;

		for (int i : stmt.executeBatch())
			n += i;

		System.out.println("Inserted " + n + " values in People");

	}

	private void fillDirector(BufferedReader r) throws SQLException, IOException {
		String values;
		Statement stmt = connection.createStatement();
		int n = 0;
		String[] v;
		while ((values = r.readLine()) != null) {
			v = values.split(";", 2);
			stmt.addBatch("INSERT INTO DIRECTOR VALUES (" + v[0] + ", " + v[1] + ")");
		}
		;

		for (int i : stmt.executeBatch())
			n += i;

		System.out.println("Inserted " + n + " values in Director");

	}

	private void fillRole(BufferedReader r) throws SQLException, IOException {
		String values;
		Statement stmt = connection.createStatement();
		int n = 0;
		String[] v;
		while ((values = r.readLine()) != null) {
			v = values.split(";", 3);
			stmt.addBatch("INSERT INTO ROLE VALUES (" + v[0] + ", " + v[1] + ", '" + v[2].replace("'", "''") + "')");
		}
		;

		for (int i : stmt.executeBatch())
			n += i;

		System.out.println("Inserted " + n + " values in Role");

	}

	public void fillTables(Map<String, File> files) throws Exception {
		if (connection != null) {
			try {

				connection.setAutoCommit(false);

				File f;
				f = files.get("MOVIE");
				if (f == null)
					throw new Exception();
				fillMovie(new BufferedReader(new FileReader(f)));

				f = files.get("PEOPLE");
				if (f == null)
					throw new Exception();
				fillPeople(new BufferedReader(new FileReader(f)));

				f = files.get("DIRECTOR");
				if (f == null)
					throw new Exception();
				fillDirector(new BufferedReader(new FileReader(f)));

				f = files.get("ROLE");
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

		if (connection != null) {
			pattern = "'%" + pattern + "%'";
			Vector<String> v = new Vector<String>();
			Statement stmt = connection.createStatement();
			String sql  = "SELECT m.title FROM movie m where title LIKE " + pattern;
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				String s = result.getString(1);
				v.add(s);
			}
			
			sql  = "SELECT m.title, p.firstname, p.lastname FROM movie m, director d, people p"
					+ " WHERE m.mid = d.mid AND p.pid = d.pid AND"
					+ " (p.firstname LIKE " + pattern
					+ " OR p.lastname LIKE " + pattern
					+ ")";
			result = stmt.executeQuery(sql);
			while (result.next()) {
				String s = result.getString(1);
				s += ", " + result.getString(2);
				s += ", " + result.getString(3);
				v.add(s);
			}
			
			sql  = "SELECT m.title, p.firstname, p.lastname, r.name FROM movie m, role r, people p"
					+ " WHERE m.mid = r.mid AND p.pid = r.pid AND"
					+ " (p.firstname LIKE " + pattern
					+ " OR p.lastname LIKE " + pattern
					+ " OR r.name LIKE " + pattern
					+ ")";
			result = stmt.executeQuery(sql);
			while (result.next()) {
				String s = result.getString(1);
				s += ", " + result.getString(2);
				s += ", " + result.getString(3);
				s += ", " + result.getString(4);
				v.add(s);
			}
			
						return v;

		} else
			throw new Exception();

	}

	public static void main(String[] args) {
		try {
			IModel dbm = new JDBCModel("kim", "kim", "kim");
			dbm.initialize();
			Map<String, File> files = new TreeMap<String, File>();
			files.put("MOVIE", new File("src/main/resources/movie.txt"));
			files.put("PEOPLE", new File("src/main/resources/people.txt"));
			files.put("DIRECTOR", new File("src/main/resources/director.txt"));
			files.put("ROLE", new File("src/main/resources/role.txt"));

			dbm.fillTables(files);
			dbm.close();

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
