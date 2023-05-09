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
		ArrayList<String> folders = new ArrayList<>();

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
					folders.add(file.getName());
				}
			} else {
				for (File file :folder.listFiles()) {
					if (!file.isHidden()) {
						folders.add(file.getName());
					}
				}
			}
		}

		try {

			final int totalFiles = folders.size();
			dout.writeInt(totalFiles);

			for (String file : folders) {
				bfout.write(file);
				bfout.newLine();
				bfout.flush();
			}
			ok = true;
			if (ok) return StatusCode.SUCCESS;
			else return StatusCode.COMMAND_ERROR;

		} catch (IOException e) {
			return StatusCode.CLIENT_DISCONNECTED;
		}
	}
}
