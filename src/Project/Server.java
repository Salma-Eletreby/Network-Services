package Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {
	public static void main(String[] args) {
		try {
			// FTP
			ServerSocket FTPserver = new ServerSocket(1300);
			System.out.println("FTP Service waiting for client on port " + FTPserver.getLocalPort());
			
			DatagramSocket DNSserver = new DatagramSocket(5000);
			System.out.println("DNS Service waiting for client on port " + DNSserver.getLocalPort());
			
			new DNSservice(DNSserver).start();
			for(;;) {
				Socket nextClient = FTPserver.accept();
//				new DNS_Service(DNSserver).start();
				
				if(nextClient!=null) {
					FTP_Service clientThread = new FTP_Service(nextClient);
	                clientThread.start();
				}
				
			}
			
		} catch(IOException ioe){
			System.out.println("Error" + ioe);
		}

	}

}
