package lcd.tpnote.exo3;

import lcd.tpnote.utils.Run;

public class RunDriverOneStop {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/run-hadoop-jar.sh", "DriverOneStop"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
