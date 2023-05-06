package client.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import client.Command;
import client.commands.Util.StatusCode;

public class Clear extends Command {

	public Clear(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {

		System.out.print("\033[H\033[2J");
		System.out.flush();

		return StatusCode.SUCCESS;
	}
}
