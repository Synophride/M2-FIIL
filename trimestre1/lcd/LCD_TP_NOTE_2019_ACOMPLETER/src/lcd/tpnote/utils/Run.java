package lcd.tpnote.utils;

import java.io.IOException;

public class Run {

	public static int exec(String... script) throws IOException, InterruptedException {
		String path = Run.class.getClassLoader().getResource(script[0]).getPath();
		script[0] = path;
		ProcessBuilder pb = new ProcessBuilder(script);
		pb.inheritIO();
		Process p = pb.start();
						
		return p.waitFor();
	}
	
}
