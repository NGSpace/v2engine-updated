package io.github.ngspace.hudder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.github.ngspace.hudder.compilers.HudderV2Compiler;
import io.github.ngspace.hudder.compilers.utils.CompileException;

public class V2Engine {
	
	public static void main(String[] args) throws CompileException {
		new HudderV2Compiler().buildRuntime("", "eval").importFile("/home/computer/Desktop/v2engine.v2", 0, 0);
		
	}

	public static String getFile(String file) throws FileNotFoundException {
		Scanner reader = new Scanner(new File(file));
		String res = "";
		while (reader.hasNextLine()) {
			res += reader.nextLine();
			res += '\n';
		}
		reader.close();
		if (res.isEmpty()) return res;
		return res.substring(0, res.length()-1);
	}
	
}
