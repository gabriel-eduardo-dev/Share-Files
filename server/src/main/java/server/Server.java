package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket = null;
	private final int PORT;

	Server(int PORT) {
		this.PORT = PORT;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Failed to start Server");
		}
		System.out.println("[SERVER]: created");
	}

	public void start() {
		System.out.println("[SERVER]: listening for connections at port: " + PORT);
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("[SERVER]: " + socket.getInetAddress().toString() + " connecting");
				ClientHandler clientHandler = new ClientHandler(socket);

				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (serverSocket == null) {
				return;
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
