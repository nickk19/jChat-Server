import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ServerRunnable implements Runnable {
	Socket client = null;
	
    public ServerRunnable(Socket socket) {
		this.client = socket;
	}

	public void run() {
		try {
			BufferedReader inStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
			DataOutputStream outStream = new DataOutputStream(client.getOutputStream());

			String username = MultiServer.connections.get(client);
			broadcast(username + " connected.");

			while (true) {
				String strRicevuta = inStream.readLine();
				if (strRicevuta == null || strRicevuta.equals("DISCONNECT")) {
					break;
				} else {
					System.out.println(username + " says: " + strRicevuta);
					broadcast(username + ": " + strRicevuta);
				}
			}
			
			outStream.close();
			inStream.close();

			MultiServer.connections.remove(client);
			System.out.println(MultiServer.ANSI_YELLOW + username + " disconnected. Connected clients: " + MultiServer.connections.size() + MultiServer.ANSI_RESET);

			broadcast(username + " disconnected.");
			client.close();
		} catch (Exception e) {
			System.out.println(MultiServer.ANSI_RED + "Server Error." + MultiServer.ANSI_RESET);
			System.exit(1);
		}
	}

	public static void broadcast(String message) {
		try {
			for (Socket sk: MultiServer.connections.keySet()) {
				DataOutputStream out = new DataOutputStream(sk.getOutputStream());
				out.writeBytes(message + "\n");
			}
		} catch (IOException e) {
			System.out.println(MultiServer.ANSI_RED + "Error while broadcasting message." + MultiServer.ANSI_RESET);
		}
	}
}