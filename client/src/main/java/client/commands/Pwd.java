package client.commands;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import client.Command;
import client.commands.Util.StatusCode;

/**
 * Pwd
 */
public class Pwd extends Command {

	public Pwd(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {

		System.out.println(path.toString());
		return StatusCode.SUCESS;
	}
}
