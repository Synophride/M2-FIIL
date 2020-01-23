package lcd.tpnote.exo4;


import lcd.tpnote.utils.Run;


/*
 * NE PAS MODIFIER. EXÉCUTER CETTE CLASSE POUR ARRÊTER SPARK
 */

public class StopSpark {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/stop-spark.sh"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
