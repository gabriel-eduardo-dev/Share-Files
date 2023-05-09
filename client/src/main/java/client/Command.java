package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import client.commands.Util.StatusCode;

public abstract class Command {

	protected OutputStream out;
	protected InputStream in;

	protected BufferedWriter bfout;
	protected BufferedReader bfin;

	protected DataOutputStream dout;
	protected DataInputStream din;

	public Command(OutputStream out, InputStream in) {
		this.out = out;
		this.in = in;

		bfout = new BufferedWriter(new OutputStreamWriter(out));
		bfin = new BufferedReader(new InputStreamReader(in));

		dout = new DataOutputStream(out);
		din = new DataInputStream(in);
	}

	// Override it
	public abstract StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path);
}
