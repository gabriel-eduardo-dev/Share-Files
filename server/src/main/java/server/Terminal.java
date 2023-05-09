package server;

import java.util.ArrayList;
import java.util.HashMap;

import server.commands.*;

/**
 * CommandManager
 */
public class Terminal {

	public HashMap<String, Command> commands = new HashMap<>();
	
	public Terminal() {

	}

	public StatusCode execute(String command) {
		
		ArrayList<String> _commands = parseCommands(command.trim());

		for (String _command : _commands) {

			ArrayList<String> args = parseArgs(_command);
			ArrayList<String> commandArgs = parseCommandArgs(_command);

			Command ___command = commands.get(args.get(0));
			if (___command != null) {
				StatusCode statusCode = ___command.execute(args, commandArgs);
				switch (statusCode) {
					case SUCCESS:
						System.out.print(" [SUCCESS]\n");
						break;
					case COMMAND_ERROR:
						System.out.print(" [FAILED]\n");
						break;
					case CLIENT_DISCONNECTED:
						System.out.print(" [DISCONNECTED]\n");
						// Well if the server is disconnected, theres no reason to continue process commands
						return statusCode;
					case EXIT:
						return statusCode;
				}
			} else {
				System.out.println("[Server]: Unknown command: " + args.get(0));
			}
		}
		return StatusCode.SUCCESS;
	}

	public ArrayList<String> parseCommands(String commands) {

		ArrayList<String> result = new ArrayList<String>();
		String _commands[] = commands.split(";");

		for (String command : _commands) {
			result.add(command.trim());
		}
		return result;
	}

	public ArrayList<String> parseArgs(String args) {
		ArrayList<String> result = new ArrayList<>();
		for (String arg : args.split(" ")) {
			if (arg.charAt(0) != '-') {
				result.add(arg);
			}
		}
		return result;
	}

	public ArrayList<String> parseCommandArgs(String args) {
		ArrayList<String> result = new ArrayList<>();
		for (String arg : args.split(" ")) {
			if (arg.charAt(0) == '-') {
				result.add(arg);
			}
		}
		return result;
	}

}
