package lcd.tpnote.exo3;

import lcd.tpnote.utils.Run;

public class StartHadoop {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/start-hadoop.sh"));
		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}
}
