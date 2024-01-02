package Project;

import java.io.*;

public class Log {
	public static synchronized void Log(String IP, long timeConnection, long duration, String type) { 
	    try {
	    	BufferedWriter writer = new BufferedWriter(new FileWriter("data\\serverLogs.txt", true));
	    	writer.append(IP+" : "+timeConnection+" : "+duration+" ms : "+type+"\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
