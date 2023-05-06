package server.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import server.Command;
/**
 * Ls
 */
public class Ls extends Command {

	public Ls(OutputStream out, InputStream in) {
		super(out, in);
	}

	// args[0] always ls
	// args[1] path
	// commandArgs[0] optional | -a | show all
	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs) {

		StringBuilder messageToClient = new StringBuilder();

		File folder = new File(args.get(1));
		boolean hidden = commandArgs.contains("-a");
		boolean ok = true;

		if (!folder.exists()) {
			messageToClient.append("Failed Folder not exist");
			ok = false;
		} else {
			messageToClient.append("Success ");
			if (hidden) {
				for (File file : folder.listFiles()) {
					messageToClient.append(file.getName() + ' ');
				}
			} else {
				for (File file :folder.listFiles()) {
					if (!file.isHidden()) {
						messageToClient.append(file.getName() + ' ');
					}
				}
			}
		}

		try {

			bfout.write(messageToClient.toString());
			bfout.newLine();
			bfout.flush();

			if (ok) return StatusCode.SUCCESS;
			else return StatusCode.COMMAND_ERROR;

		} catch (IOException e) {
			return StatusCode.CLIENT_DISCONNECTED;
		}
	}
}
