package io.github.ngspace.v2engine;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Predicate;

import io.github.ngspace.v2engine.compilers.V2EngineCompiler;
import io.github.ngspace.v2engine.compilers.utils.CompileException;

public class V2Engine {
	
	V2EngineCompiler compiler = new V2EngineCompiler();
	
	public static void main(String[] args) throws CompileException, IOException {
		var engine = new V2Engine();
		engine.setImportFilter(_->true);
		engine.executeFile("/home/computer/Desktop/v2engine.v2");
	}

	public static String getFile(String file) throws IOException {
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
	
	public void eval(String text) throws CompileException {
		compiler.buildRuntime(text, "//eval").execute();
	}
	public void executeFile(String filename) throws CompileException, IOException {
		compiler.buildRuntime(getFile(filename), filename).execute();
	}
	public Predicate<String> getImportFilter() {
		return compiler.getImportFilter();
	}


	public void setImportFilter(Predicate<String> importFilter) {
		compiler.setImportFilter(importFilter);
	}
}
