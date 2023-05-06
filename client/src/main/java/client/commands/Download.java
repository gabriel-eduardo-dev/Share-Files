package client.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.function.Consumer;

import client.Command;
import client.commands.Util.StatusCode;
import client.Util.*;

public class Download extends Command {

	public Download(OutputStream out, InputStream in) {
		super(out, in);
	}

	@Override
	public StatusCode execute(ArrayList<String> args, ArrayList<String> commandArgs, StringBuilder path) {

		StringBuilder commandToServer = new StringBuilder("download ");


		if (args.size() != 2) {
			System.out.println("[download]: argument required: [FILE PATH]");
			return StatusCode.COMMAND_ERROR;
		}

		final String _path = args.get(1);
		final String _pathList[] = _path.split("/");
		final String fileName = _pathList[_pathList.length - 1];


		if (!Util.isPath(_path)) {
			System.out.println("[download]: invalid path");
			return StatusCode.COMMAND_ERROR;
		}

		if (_path.charAt(0) == '/') {
			commandToServer.append(_path + ' ');
		} else {
			commandToServer.append(path.toString() + '/' + _path + ' ');
		}

		try {
			bfout.write(commandToServer.toString());
			bfout.newLine();
			bfout.flush();

			long FileSize = din.readLong();

			long totalBytesRead = 0;
			int bytesRead = 0;
			byte buffer[] = new byte[1024*4];
			FileOutputStream fileOut = new FileOutputStream(fileName);

			while (totalBytesRead < FileSize && (bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, FileSize - totalBytesRead))) != -1) {
				fileOut.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				System.out.printf("[download]: progress : %.2f\n", (float)totalBytesRead * 100.00f / FileSize);
			}
			fileOut.close();
			System.out.println("[download]: " + fileName + " downloaded");
			} catch (IOException e) { 
				return StatusCode.SERVER_DISCONNECTED;
		}
		return StatusCode.SUCCESS;
	}
}
