package server.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import server.Command;

public class Cd extends Command {

	public Cd (OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs) {

		File folder = new File(args.get(1));

		try {
			bfout.write("success " + args.get(1));
			bfout.newLine();
			bfout.flush();
		} catch (IOException e) {
			return StatusCode.CLIENT_DISCONNECTED;
		}

		return StatusCode.SUCCESS;
	}
}
