package lcd.tpnote.exo3;

import lcd.tpnote.utils.Run;


/*
 * NE PAS MODIFIER. EXÉCUTER CETTE CLASSE POUR ARRÊTER HADOOP
 */

public class StopHadoop {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/stop-hadoop.sh"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
