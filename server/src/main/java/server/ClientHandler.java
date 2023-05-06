package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import server.commands.*;

/**
 * ClientHandler
 */
public class ClientHandler implements Runnable {
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Terminal terminal;
	private Socket socket;
	private String homePath;

	private BufferedReader bfin;
	private BufferedWriter bfout;

	private OutputStream out;
	private InputStream in;

	private String userName;

	ClientHandler(Socket socket) {
		try {
			this.socket = socket;

			this.out = socket.getOutputStream();
			this.in = socket.getInputStream();

			this.bfout = new BufferedWriter(new OutputStreamWriter(this.out));
			this.bfin = new BufferedReader(new InputStreamReader(this.in));

			terminal = new Terminal();
			terminal.commands.put("ls", new Ls(this.out, this.in));
			terminal.commands.put("cd", new Cd(this.out, this.in));
			terminal.commands.put("download", new Download(this.out, this.in));

			this.homePath = System.getProperty("user.home");
			this.userName = bfin.readLine();

			bfout.write(homePath);
			bfout.newLine();
			bfout.flush();
			
			if (userName == null) {
				throw new IOException();
			}

			clientHandlers.add(this);
		} catch (IOException e) {
			remove();
		}
	}

	@Override
	public void run() {
		System.out.println("[SERVER]: " + userName + " connected");
		while (socket.isConnected()) {
			try {
				String messageFromClient = bfin.readLine();
				if (messageFromClient == null) {
					remove();
					break;
				}
				System.out.print("[SERVER]: received message from " + userName + ": " + messageFromClient.toString());
				terminal.execute(messageFromClient);
			} catch (IOException e) {
				remove();
				break;
			}
		}
	}

	public void remove() {
		clientHandlers.remove(this);
		System.out.println("[SERVER]: " + userName + " has left");
		close();
	}

	public void close() {
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
