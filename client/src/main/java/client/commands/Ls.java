package client.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import client.Command;
import client.Util.Pair;
import client.Util.Util;
import client.commands.Util.StatusCode;

public class Ls extends Command {

	public Ls(OutputStream out, InputStream in) {
		super(out, in);
	}

	// args[0] always ls
	// args[1] optional; should be the path if is there
	// commandArgs[0] optional; -a show all
	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {

		StringBuilder commandToServer = new StringBuilder("ls ");

		// Preparing command
		if (args.size() > 2 || commandArgs.size() > 1) {
			System.out.println("[ls]: optionals arguments are: [PATH] {-a}");
			return StatusCode.COMMAND_ERROR;
		}
	
		if (args.size() == 2 ) {
			final String _path = args.get(1);
			if (!Util.isPath(_path)) {
				System.out.println("[ls]: invalid path");
				return StatusCode.COMMAND_ERROR;
			}
			if (args.get(1).charAt(0) == '/') {
				commandToServer.append(_path + ' ');
			} else {
				commandToServer.append(path.toString() + '/' + _path + ' ');
			}
		} else {
			commandToServer.append(path.toString() + ' ');
		}

		for (String arg : commandArgs) {
			switch (arg) {
				case "-a":
					commandToServer.append("-a ");
					break;
				default:
					System.out.println("[ls]: unknown argument: " + arg);
					System.out.println("[ls]: optionals arguments are: {PATH} {-a}");
					return StatusCode.COMMAND_ERROR;
			}
		}

		try {
			// Sending to server
			bfout.write(commandToServer.toString().trim());
			bfout.newLine();
			bfout.flush();

			// Receive message from server
			final String messageFromServer = bfin.readLine();

			// Check connection
			if (messageFromServer == null) {
				throw new IOException();
			}

			Pair<StatusCode, String> result = Util.parseServerResponse(messageFromServer);
			String sortedResult[] = result.getSecond().split(" ");
			Arrays.sort(sortedResult);
			switch (result.getFirst()) {
				case SUCCESS:
					for (String str : sortedResult) {
						System.out.println(str);
					}
					break;
				case FAILED:
					// Print why it failed is server responsability
					System.out.println(result.getSecond());
					break;
				default:
					break;
			}
			return StatusCode.SUCCESS;
		} catch (IOException e) {
			return StatusCode.SERVER_DISCONNECTED;
		}
	}
}
