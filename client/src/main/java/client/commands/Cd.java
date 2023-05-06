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

		if (args.size() > 2) {
			System.out.println("[cd]: optional argument: [PATH]");
			return StatusCode.COMMAND_ERROR;
		}
		// if pass a path
		if (args.size() == 2) {

			final String _path = args.get(1);

			// FIX: add support for windows
			//
			// check if is from linux root
			if (_path.charAt(0) == '/') {
				messageToServer.append(_path);
			} else {
				messageToServer.append(path + "/" + _path);
			}
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

			switch (result.getFirst()) {
				case FAILED:
					System.out.println("[ls:server] " + result.getSecond());
					return StatusCode.FAILED;
				case SUCCESS:
					path.replace(0, path.length(), result.getSecond());
					break;
				default:
					break;
			}


		} catch (IOException e) {
			return StatusCode.SERVER_DISCONNECTED;
		}
		return StatusCode.SUCCESS;
	}
}
