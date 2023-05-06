package server.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import server.Command;

public class Download extends Command {

	public Download(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs) {

		final String path = args.get(1);

		File file = new File(path);

		if (!file.exists()) {
			try {
				bfout.write("failed file not exist");
				bfout.newLine();
				bfout.flush();
		    } catch (IOException e) {
				return StatusCode.CLIENT_DISCONNECTED;
			}
		} else {
			try {

				dout.writeLong(file.length());
				dout.flush();

				FileInputStream fileIn = new FileInputStream(path);
				byte buffer[] = new byte[1024*4];
				int bytesRead = 0;

				System.out.println("file size: " + file.length());

				while ((bytesRead = fileIn.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
				fileIn.close();
			} catch (IOException e) {
				return StatusCode.CLIENT_DISCONNECTED;
			}
		}
		return StatusCode.SUCCESS;
	}
}
