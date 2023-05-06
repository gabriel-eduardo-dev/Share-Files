package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import client.commands.Util.StatusCode;

/**
 * Terminal
 */
public class Terminal {

	// Home user path
	private String homePath;
	// Dynamic path to navigate
	private StringBuilder path;

	// Commands
	public HashMap<String, Command> commands = new HashMap<String, Command>();

	public Terminal(OutputStream out, InputStream in, String path) {

		this.path = new StringBuilder(path);
		this.homePath = path;
	}

	public static ArrayList<String> parseArgs(String args) {
		ArrayList<String> result = new ArrayList<String>();
		for (String arg : args.split(" ")) {
			if (arg.charAt(0) != '-') {
				result.add(arg);
			}
		}
		return result;
	}

	public static ArrayList<String> parseCommandArgs(String args) {
		ArrayList<String> result = new ArrayList<String>();
		for (String arg : args.split(" ")) {
			if (arg.charAt(0) == '-') {
				result.add(arg);
			}
		}
		return result;
	}

	public static ArrayList<String> parseCommands(String commands) {

		ArrayList<String> result = new ArrayList<String>();
		String _commands[] = commands.split(";");
		for (String _command : _commands) {
			result.add(_command.trim());
		}
		return result;
	}

	public StatusCode execute(String command) {
		
		// return false if server sudently disconnected
		// return true if everything is ok

		ArrayList<String> _commands = parseCommands(command);

		for (String _command : _commands) {

			ArrayList<String> args = parseArgs(_command);
			ArrayList<String> commandArgs = parseCommandArgs(_command);

			Command __command = commands.get(args.get(0));
			if (__command != null) {
				final StatusCode result = __command.execute(args, commandArgs, path);
				switch (result) {
					case SUCCESS:
						break;
					case COMMAND_ERROR:
						// Commands erros must display in the command class
						break;
					case SERVER_DISCONNECTED:
						// Well if server is disconnected, theres no reason to continue process commands
						return result;
				}
			} else {
				System.out.println("Unknown command: " + args.get(0));
			}
		}
		return StatusCode.SUCESS;
	}
	
	public StringBuilder getPath() {
		return path;
	}

	public void setPath(String newPath) {
		path.replace(0, path.length(), newPath);
	}
}
