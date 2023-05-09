package client.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import client.Command;
import client.commands.Util.StatusCode;

public class Exit extends Command {

	public Exit(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {
		return StatusCode.EXIT;
	}
}
