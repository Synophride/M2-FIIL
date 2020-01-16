package lcd.tpnote.exo4;

import lcd.tpnote.utils.Run;

public class StartSpark {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/start-spark.sh"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
