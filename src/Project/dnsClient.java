package Project;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class dnsClient {
	private DatagramSocket client;
	private Scanner kb;

	public dnsClient() {
		try {
			client = new DatagramSocket();
			kb = new Scanner(System.in);
			System.out.println("Welcome to DNS service.");
			System.out.println("\t[1] Translate domain name to IP address\n\t[2] IP address to Name Translation\n\t[3] Exit");			
			String message1 = kb.nextLine();
			
			if ( Integer.parseInt(message1) == 1) {
				
				System.out.println("**********Domain to IP*************");
				System.out.print("Domain name: ");
				
				String message = kb.nextLine();

				byte[] data = message.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length,
						InetAddress.getLocalHost(), 5000);
				client.send(packet);

				byte[] receivedData = new byte[1000];
	          	DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				client.receive(receivedPacket);
				System.out.printf("%s\n", new String(receivedPacket.getData(),0,receivedPacket.getLength()));
			} else if (Integer.parseInt(message1) == 2) {
				
				System.out.println("**********IP to Domain*************");
				System.out.print("IP: ");
				String message = kb.nextLine();

				byte[] data = message.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length,
						InetAddress.getLocalHost(), 5000);
				client.send(packet);

				byte[] receivedData = new byte[1000];
	          	DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
				client.receive(receivedPacket);
				System.out.printf("%s\n", new String(receivedPacket.getData(),0,receivedPacket.getLength()));
			} else if (Integer.parseInt(message1) == 3) {
				System.out.println("Byee");
				System.exit(0);
			}else {
				System.out.println("You entered an invalid number. Try again");
			}
						
		} catch (IOException e) {
			System.exit(1);
		}
	}
}
