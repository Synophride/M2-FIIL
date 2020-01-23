package lcd.tpnote.exo4;

import lcd.tpnote.utils.Run;

public class RunDriverLess2000 {
	public static void main(String args[]) {
		try {
			System.exit(Run.exec("resources/scripts/run-spark-jar.sh", "DriverLess2000"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
