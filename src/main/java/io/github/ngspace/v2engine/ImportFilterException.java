package io.github.ngspace.v2engine;

import io.github.ngspace.v2engine.compilers.utils.CompileException;

public class ImportFilterException extends CompileException {

	private static final long serialVersionUID = 97636312692876082L;

	public ImportFilterException(String string, int line, int col) {
		super(string, line, col);
	}
	
}
