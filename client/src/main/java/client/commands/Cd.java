package client.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import client.Command;
import client.Util.Pair;
import client.Util.Util;
import client.commands.Util.StatusCode;

/**
 * Cd
 */
public class Cd extends Command {

	public Cd(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {

		StringBuilder messageToServer = new StringBuilder("cd ");

		final String _path = args.get(1);

		if (_path.charAt(0) == '/') {
			messageToServer.append(_path);
		} else {
			messageToServer.append(path + "/" + _path);
		}

		try {
			bfout.write(messageToServer.toString());
			bfout.newLine();
			bfout.flush();

			final String messageFromServer = bfin.readLine();

			if (messageFromServer == null) {
				throw new IOException();
			}

			Pair<StatusCode, String> result = Util.parseServerResponse(messageFromServer);

			path.replace(0, path.length(), result.getSecond());

		} catch (IOException e) {
			return StatusCode.SERVER_DISCONNECTED;
		}
		return StatusCode.SUCESS;
	}
}
