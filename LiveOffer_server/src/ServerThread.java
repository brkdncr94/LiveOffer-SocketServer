import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.socket = socket;
    }

    public void run() {

        try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));) {
        	
            System.out.println("Accepted connection, inside the thread now.");                

            String inputLine, outputLine;
            Protocol kkp = new Protocol();

            while ((inputLine = in.readLine()) != null) {
            	
            	System.out.println("Client said: " + inputLine);
            	
            	String[] words = inputLine.split(";");
            	double latitude;
            	double longitude;
            	String city;
            	String category;
            	if(words.length == 4)
            	{
            		latitude = Double.parseDouble(words[0]);
            		longitude = Double.parseDouble(words[1]);
            		city = words[2];
            		category = words[3];            		
            		
            		outputLine = kkp.processInput(latitude, longitude, city, category);
                    out.println(outputLine);
                    
                    System.out.println("Server sent the jsonResult");
            		
            		
            	}
            	else
            	{
            		System.out.println("Inappropriate coordinates input sent.");
            	}
            
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error inside the thread.");
        }
    }
}