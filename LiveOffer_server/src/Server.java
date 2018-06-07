import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {
	
	public static Connection connection;
	
    /*public static void main(String[] args) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));


            String inputLine, outputLine;

            // Initiate conversation with client
            Protocol kkp = new Protocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("End."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }*/
    public static void main(String[] args) throws IOException {

        /*if (args.length != 1) {
            System.err.println("Usage: java KKMultiServer <port number>");
            System.exit(1);
        }
*/
        int portNumber = 8882;
        boolean listening = true;
        
     // Load Driver
     	try {
     				
			Class.forName("com.mysql.jdbc.Driver");
					// Connect to DB server
	     	connection = DriverManager.getConnection("jdbc:mysql://localhost/live_offer?useSSL=false", "root", "admin");
	     	System.out.println("Connected to MySQL");
	     	
	     	
	     	try(ServerSocket serverSocket = new ServerSocket(portNumber)){
	        	
	            System.out.println("Created Server Socket in port: " + portNumber);          
	            
	            while (listening) {
	                new ServerThread(serverSocket.accept()).start();
	            }
	        }catch (IOException e) {
	            System.err.println("Could not listen on port " + portNumber);
	            System.exit(-1);
	        }
	     	
	     			
     	} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
        
        
    }
}