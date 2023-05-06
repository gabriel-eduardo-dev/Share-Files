package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

import client.commands.Cd;
import client.commands.Clear;
import client.commands.Download;
import client.commands.Ls;
import client.commands.Pwd;
import client.commands.Util.StatusCode;

public class App {

    public static void main(String[] args) {

		// Default args
		String ip = "localhost";
		int port = 3885;

		// Pass by args
		if (args.length > 0) {
			if (args.length != 2) {
				System.out.println("Requires 2 arguments in following order: [IP] [PORT]");
				System.exit(-1);
			}
			// Error handle if is not ip address
			try {
				InetAddress.getByName(args[0]);
				ip = args[0];
			} catch (UnknownHostException e) {
				System.out.println("[IP] Invalid...");
				System.exit(-2);
			}
			// Error handle for character(s) in [PORT]
			try {
				port = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.out.println("[PORT] cannot have character(s)...");
				System.exit(-3);
			}
		}


		System.out.println("Connecting to address " + ip + " at port " + port);

		while (true) {
			try {
				Socket client = new Socket(ip, port);
				System.out.println("Connected...");

				Scanner scanner = new Scanner(System.in);

				OutputStream out = client.getOutputStream();
				InputStream in = client.getInputStream();

				BufferedWriter bfout = new BufferedWriter(new OutputStreamWriter(out));
				BufferedReader bfin = new BufferedReader(new InputStreamReader(in));


				Properties properties = System.getProperties();
				final String userName = properties.getProperty("user.name");

				bfout.write(userName);
				bfout.newLine();
				bfout.flush();

				final String homePath = bfin.readLine();

				
				if (homePath == null) {
					System.out.println("Server disconnected");

				} else {
					Terminal terminal = new Terminal(client.getOutputStream(), client.getInputStream(), homePath);
					terminal.commands.put("ls", new Ls(out, in));
					terminal.commands.put("pwd", new Pwd(out, in));
					terminal.commands.put("cd", new Cd(out, in));
					terminal.commands.put("clear", new Clear(out, in));
					terminal.commands.put("download", new Download(out, in));
					// Main loop
					while (client.isConnected()) {
						System.out.print("[" + client.getInetAddress().toString() + "]: ");
						String command = scanner.nextLine();
						if (command.isEmpty()) {
							continue;
						}
						if (terminal.execute(command) == StatusCode.SERVER_DISCONNECTED) {
							System.out.println("Server disconnected");
							break;
						}
					}
				}
				scanner.close();
				bfout.close();
				bfin.close();
				out.close();
				in.close();
				client.close();
				break;
			} catch (IOException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
