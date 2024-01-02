package Project;

import java.net.Socket;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.io.*;

public class FTP_Service extends Thread{
	Socket nextClient;
	String role;
	long startTime;
	long duration=0;
	String service = "Failed login";
	PrintWriter to_client;
	BufferedReader from_client;

	public FTP_Service(Socket nextClient) 
	{
		this.nextClient = nextClient;
		this.startTime = System.currentTimeMillis();
	}

	//function to display the FTP main menu (seperated to make things look cleaner)
	public static int Menu(PrintWriter to_client, BufferedReader from_client) {
		int option=-1;
		to_client.println("Welcome to FTP service.");
		to_client.println("\t[1] Upload File\n\t[2] Download File\n\t[3] Directory Navigation\n\t[4] Compress a file\n\t[5] Decompress a file \n\t[6] Exit");
		to_client.println("Your choice: ");

		String choice="";
		try {
			choice = from_client.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		option = Integer.parseInt(choice);

		return option;
	}

	//Function to check the user name and password and returns the role to check privileges (cleaner code)
	public static String Authenticate(PrintWriter to_client, BufferedReader from_client) {
		String role = "";
		Scanner sc = null;
		try {
			to_client.println("Username: ");
			String username = from_client.readLine();

			to_client.println("Password: ");
			String password = from_client.readLine();

			File file = new File("data\\users.txt");
		
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String user = sc.nextLine();

				String[] details = user.split(" ");
				if (username.equals(details[0]) && password.equals(details[1])) {
					role = details[2];
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(sc != null) 
				sc.close();
		}

		return role;
	}

	//function for directory navigation = cleaner and recursive 
	public void Directory(File folder,PrintWriter to_client, BufferedReader from_client) {
		//the actual displaying files/folders part
		if(folder.listFiles()==null) {
			to_client.println("Directory contains no files or you have selected a file.");
		}else {
			for (File file : folder.listFiles()) {
				if (file.isDirectory()) {
					to_client.println("Directory = " + folder.getName()+ "\\" + file.getName());
				} else {
					if (file.isFile()) {
						to_client.println("File = " + folder.getName()+ "\\" + file.getName());
					} 
				} 
			} 
		}

		if(folder.getName().equals("FTP"))
		{
			to_client.println("Enter the path or EXIT: ");
		} else {
			to_client.println("Enter the path or BACK or EXIT: ");
		}

		//the actual navigation part
		try {
			String choice = from_client.readLine();

			if(choice.equals("EXIT")) {
				to_client.println("***********");
				to_client.println("DONE");
			} else if(choice.equals("BACK")){
				folder = folder.getParentFile();
				Directory(folder,to_client,from_client);
			} else {
				folder = new File(choice);
				Directory(folder,to_client,from_client);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Download(String filename,PrintWriter to_client, BufferedReader from_client) {
		try{    

			File file=new File("FTP/"+filename);
			if(!file.exists()||!file.isFile()) {
				System.out.println("File does not exist");
				to_client.println("File does not exist");
				return;
			}

			FileInputStream fin=new FileInputStream(file);  
			BufferedInputStream bin = new BufferedInputStream(fin);
			OutputStream out = nextClient.getOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			to_client.println("File is downloading");

			while ((bytesRead = fin.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			bin.close();
			fin.close();
			out.close();

		}catch(Exception e){System.out.println(e);}  	
	}
	
	public void Upload(String filename,PrintWriter to_client, BufferedReader from_client) {
		try{    
			to_client.println("Uploading file");
			File file=new File("FTP/"+filename);
			file.createNewFile();
			FileOutputStream ToFile=new FileOutputStream(file);         
			String filetext;
			while ((filetext = from_client.readLine()) != null) {

				ToFile.write(filetext.getBytes());
				ToFile.write(System.lineSeparator().getBytes());
			}
			ToFile.close();

		}catch(Exception e){System.out.println(e);}  	
	}
	
	public void Compress(String filename,PrintWriter to_client) {
		try{    
			to_client.println("Compressing file");
			File file=new File("FTP/"+filename);
			File fileC=new File("FTP/compressed"+filename);
			fileC.createNewFile();
			if(!file.exists())
			{
				to_client.println("File to be compressed does not exit.");
			}
			FileInputStream FromFile=new FileInputStream(file);
			FileOutputStream ToCFile=new FileOutputStream(fileC);  
			DeflaterOutputStream compresser = new DeflaterOutputStream(ToCFile);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = FromFile.read(buffer)) != -1) {
				compresser.write(buffer, 0, bytesRead);
			}
			compresser.finish();
			compresser.close();

			to_client.println("File has been compressed.");
		}catch(Exception e){System.out.println(e);}  	
	}
	public void Decompress(String filename,PrintWriter to_client) {
		try{    
			to_client.println("Decompressing file");
			File file=new File("FTP/decompressed_"+filename);
			File fileC=new File("FTP/"+filename);
			file.createNewFile();
			if(!fileC.exists())
			{
				to_client.println("File to be decompressed does not exit.");
			}
			FileInputStream FromCFile=new FileInputStream(fileC);
			FileOutputStream ToFile=new FileOutputStream(file);  
			InflaterInputStream  decompresser = new InflaterInputStream (FromCFile);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = decompresser.read(buffer)) != -1) {
				ToFile.write(buffer, 0, bytesRead);
			}
			to_client.println("File has been decompressed.");
			decompresser.close();
			FromCFile.close();
			ToFile.close();

		}catch(Exception e){System.out.println(e);}  	
	}

	public void run() {
		try {
			PrintWriter to_client = new PrintWriter(nextClient.getOutputStream(),true);
			BufferedReader from_client = new BufferedReader(new InputStreamReader(nextClient.getInputStream()));

			role = Authenticate(to_client,from_client);

			if(role.isBlank())
			{
				to_client.println("Invalid username or password.");
				to_client.println("DONE");
			}else {

				int option = -1;
				do {
					option = Menu(to_client,from_client);

					switch(option) {
					case 1:
					{
						if(role.equals("reader")) {
							service = "Unauthorised";
							to_client.println("Unauthorised Access!");
							to_client.println("DONE");
						}
						else {
							to_client.println("********Upload File**********");
							to_client.println("File path:");
							service = "Upload";

							String filename= from_client.readLine();
							Upload(filename, to_client,from_client);
							to_client.println("DONE");
						}
						break;
					}
					case 2:
					{	
						if(role.equals("writer")) {
							service = "Unauthorised";
							to_client.println("Unauthorised Access!");
							to_client.println("DONE");
						}
						else {
							to_client.println("********Download File**********");
							to_client.println("The File Name: ");
							service = "Download";

							String filename= from_client.readLine();

							Download(filename, to_client,from_client);
						}

						to_client.println("DONE");
						break;
					}
					case 3:
					{
						File folder = new File("FTP\\"); 
						to_client.println("********Directory Navigation**********");
						service = "Directory Navigation";
						Directory(folder, to_client,from_client);
						to_client.println("DONE");
						break;
					}case 4:
					{
						to_client.println("********Compress**********");
						to_client.println("File to be compressed: ");
						service = "Compression";

						String filename= from_client.readLine();
						Compress(filename, to_client);
						to_client.println("DONE");
						break;
					}
					case 5:
					{
						to_client.println("********Decompress**********");
						to_client.println("File to be decompressed: ");
						service = "Decompression";

						String filename= from_client.readLine();
						Decompress(filename, to_client);
						to_client.println("DONE");
						break;
					}
					case 6:
					{
						service = "none chosen";
						to_client.println("DONE");
						break;
					}
					default: {
						to_client.println("Please choose a valid number");
						break;
					}
					}

				}while(option > 6 || option <=0);	
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(nextClient != null)
					nextClient.close();
				
				if(to_client != null) 
					to_client.close();
				
				if(from_client != null) 
					from_client.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
		}
		this.duration = System.currentTimeMillis() - this.startTime;
		Log.Log(String.valueOf(this.nextClient.getInetAddress()),this.startTime,this.duration,"FTP -> "+service);
	}
}
