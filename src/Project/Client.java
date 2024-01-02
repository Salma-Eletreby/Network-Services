package Project;

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {

	public static void main(String[] args) {
		System.out.println("Welcome to the Network Service");
		System.out.println("Please choose the number corresponding to the desired service");
		System.out.println("\t[1300] FTP\n\t[5000] DNS\n\t[1] Exit");
		int port = 0;
		do {
			System.out.print("Your choice: ");
			Scanner input = new Scanner(System.in);
			port = input.nextInt(); 
			switch(port) {
			case 1:{
				System.out.println("Thank you for choosing our service. Please come again");
				break;
			}
			case 1300:{ //FTP
				System.out.println("You have chosen FTP. You will be redirected shortly.\n-----------");
				Socket client = null;
				BufferedReader from_server = null;
				BufferedReader from_user = null;
				PrintWriter to_server = null;
				try {
					client = new Socket("localhost", port);
					from_server = new BufferedReader(new InputStreamReader(client.getInputStream()));
					from_user = new BufferedReader(new InputStreamReader(System.in));
					to_server = new PrintWriter(client.getOutputStream(),true);

					while(true) {
						String message = from_server.readLine();

						if(message!=null) {
							if(message.equals("DONE")) {
								break;
							}
							System.out.println(message);

							if(message.contains(":")){
								String user_input = from_user.readLine();
								to_server.println(user_input);
								//uploading
								if(message.contains("File path")){
									File file=new File("Client/"+user_input);
									if(!file.exists()||!file.isFile()) {
										System.out.println("File does not exist");

										return;
									}
									if(file.length()>=1024 * 1024) {
										System.out.println("File is too large.");

										return;
									}

									FileInputStream fin=new FileInputStream(file);  
									OutputStream out = client.getOutputStream();
									byte[] buffer = new byte[1024];
									int bytesRead;
									while ((bytesRead = fin.read(buffer)) != -1) {
										out.write(buffer, 0, bytesRead);

									}
									fin.close();
									out.close();
									System.out.println("File has been uploaded.");
									System.out.println("---Completed---");
									break;
								}	
							}if(message.contains("downloading")){
								System.out.println("How would you like to name the file: ");
								String filename= from_user.readLine();
								File file=new File("Client/"+filename);
								FileOutputStream ToFile=new FileOutputStream(file);  

								String filetext;
								while ((filetext = from_server.readLine()) != null) {
									ToFile.write(filetext.getBytes());
									ToFile.write(System.lineSeparator().getBytes());
								}
								System.out.println("File downloaded successfully.");
								ToFile.close();
								System.exit(0);
							}
						}

					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						if(client != null)
							client.close();
						if(from_server != null)
							from_server.close();
						if(to_server != null)
							to_server.close();
						if(from_user != null)
							from_user.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			case 5000:{ //DNS
				System.out.println("You have chosen DNS. You will be redirected shortly.\n-----------");

				try {
					new dnsClient();

				} catch (Exception e) {
					System.exit(1);
				}
				break;
			}
			default:{
				System.out.println("Invalid number chosen.");
				break;
			}
			}
		}while(port<0);

	}
}