package se.amsen.par.twootoot.util.utils;

import java.io.IOException;

/**
 * @author params on 06/11/15
 */
public class ConnectionUtil {
	public static boolean isOnline() {

		Runtime runtime = Runtime.getRuntime();
		try {

			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int     exitValue = ipProcess.waitFor();
			return (exitValue == 0);

		} catch (IOException e)          { e.printStackTrace(); }
		catch (InterruptedException e) { e.printStackTrace(); }

		return false;
	}
}
