package lcd.tpnote.exo3;

import lcd.tpnote.utils.Run;

public class RunDriverOneStop {
	public static void main(String args[]) {
		try {
			int i = Run.exec("resources/scripts/run-hadoop-jar.sh", "DriverOneStop");
			System.exit(i);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
