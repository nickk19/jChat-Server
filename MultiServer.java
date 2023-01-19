import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiServer {
	public static HashMap<Socket,String> connections = new HashMap<>();
	
	int port = 6789;
	public static String username;

	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m";

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is now serving on port " + port + "\n");
			
			while (true) {
				Socket socket = serverSocket.accept();
				BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				username = inStream.readLine();
				connections.put(socket, username);
				System.out.println(ANSI_GREEN + username + " connected. Connected clients: " + connections.size() + ANSI_RESET);

				ServerRunnable serverRunnable = new ServerRunnable(socket);
				Thread serverThread = new Thread(serverRunnable);
				serverThread.start();
			}

		} catch (Exception e) {
			System.out.println(ANSI_RED + "Error while starting server.\n" + ANSI_RESET);
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		MultiServer tcpServer = new MultiServer();
		tcpServer.start();
	}
}