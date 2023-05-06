package client.Util;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import client.commands.Util.StatusCode;

public class Util {

	public static boolean isPath(String path) {
		try {
			Paths.get(path);
			return true;
		} catch (InvalidPathException e) {
			return false;
		}
	}

	public static 
		Pair<StatusCode, String> parseServerResponse(String response) {

			Pair<StatusCode, String> result = new Pair<>();

			String reponseSplit[] = response.split(" ");
			String status = reponseSplit[0];
			String text = response.substring(status.length()).trim();

			if (status.equalsIgnoreCase("success")) {
				result.setFirst(StatusCode.SUCCESS);
			} else {
				result.setFirst(StatusCode.FAILED);
			}
			
			result.setSecond(text);

			return result;
	}
}
