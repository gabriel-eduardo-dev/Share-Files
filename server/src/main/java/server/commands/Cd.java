package server.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import server.Command;

public class Cd extends Command {

	private final String homePath;

	public Cd (OutputStream out, InputStream in) {
		super(out, in);
		homePath = System.getProperty("user.home");
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs) {

		if (args.size() != 2) {
			return send("failed [cd:server] requires the path");
		}

		File folder = new File(args.get(1));

		if (!folder.exists()) {
			return send("failed folder not exist");
		} else if (!folder.isDirectory()) {
			return send("failed not a folder");
		}

		return send("sucess " + folder.getPath());
	}

	public StatusCode send(String message) {
		try {
			bfout.write(message);
			bfout.newLine();
			bfout.flush();
			return StatusCode.SUCCESS;
		} catch (IOException e) {
			return StatusCode.CLIENT_DISCONNECTED;
		}
	}
}
